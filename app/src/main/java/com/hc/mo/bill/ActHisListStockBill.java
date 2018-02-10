package com.hc.mo.bill;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.data.DataTable;
import hylib.toolkits.ArrayTools;
import hylib.toolkits.ExProc;
import hylib.toolkits.HyColor;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits._D;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.ui.dialog.UCCreator;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.widget.HyEvent;
import hylib.widget.HyListView;
import android.R.integer;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.hc.ActBase;
import com.hc.MainActions;
import com.hc.R;
import com.hc.SysData;
import com.hc.dal.Bill;
import com.hc.dal.d;
import com.hc.db.DBHelper;
import com.hc.db.DBLocal;
import com.hc.tools.ActInputProduct;
import com.hc.tools.ActSearchItem;
import com.hc.tools.Search;

public class ActHisListStockBill extends ActHisList {

	@Override
    protected void setInitParams()
	{
		CIDs = new int[] { 0, Bill.BTID_DB_BH, Bill.BTID_DB_FH };
		pageNames = "全部, 备货单, 发货单";
		mTitle = "金蝶单据";
    }


    protected String sqlBill = "select m.*, e.FName Emp from StockBill m JOIN Emp e ON m.FEmpID=e.FItemID where @COND";
    protected String sqlStat = "select m.FID, SUM(FQty) Qty, SUM(FInTaxAmount) Amount, MIN(FSCStockID) FSCStockID, MAX(FDCStockID) FDCStockID from StockBill m, StockBillEntry d where m.FID=d.FID and @COND group by m.FID";
    @Override
    protected void LoadData() throws Exception {
    	String cond = SysData.isAdmin ? "1=1" : "FEmpID=" + SysData.emp_id;
    	if(!SysData.isAdmin && SysData.CustID > 0) cond += " and FCustID=" + SysData.CustID;
    	DataTable dtBill = DBLocal.OpenSingleTable("StockBill", sqlBill.replace("@COND", cond));
    	DataTable dtStat = DBLocal.OpenTable("FID/i|Qty/i|Amount/f|FSCStockID/i|FDCStockID/i", sqlStat.replace("@COND", cond));

    	int ordNo = 0;
    	dtHis = new DataTable("his", "OrdNo/i|ID/i|FBillNo|FDate|Cust|FDCStock|FSCStock|Emp|CID/i|Qty/i|Amount/f|state/i|Flag/i|sel/b|Ext/e");

    	for (DataRow drBill : dtBill.getRows()) {
			//ParamList plBillExt = new ParamList(drBill.getValue("Ext"));
			int CustID = drBill.getIntVal("FCustID");

			int CID = drBill.getIntVal("FBillTypeID");
			int ETID = Bill.valueOfExtType(drBill.getStrVal("ExtType"));
	    	if(CID == Bill.BTID_DB && ETID > 0) 
	    		CID = CID * 10 + ETID;
	    	
	    	if(!gv.In(CID, CIDs)) continue;

			int FID = drBill.getIntVal("FID");
			DataRow drStat = dtStat.FindRow("FID", FID);
			
			DataRow dr = dtHis.newRow();
	    	
			dr.setValue("OrdNo", ordNo++);
			dr.setValue("Emp", drBill.getValue("Emp"));
			dr.setValue("CID", CID);
			dr.setValue("ID", drBill.getValue("FID"));
			dr.setValue("Flag", drBill.getValue("Flag"));
			dr.setValue("FBillNo", drBill.getValue("FBillNo"));

			UpdateRowData(dr, drBill, drStat);
			
			dtHis.addRow(dr);
		}

    	dtHis.Sort("ordNo desc");
	}

    public void UpdateRowData(DataRow dr, DataRow drBill, DataRow drStat) throws Exception {

		ParamList plBillExt = new ParamList(drBill.getValue("Ext"));
		int CustID = drBill.getIntVal("FCustID");
		
		ParamList pl = new ParamList(plBillExt);
		
		dr.setValue("FDate", drBill.getValue("FDate"));
		dr.setValue("SaveDate", drBill.getValue("SaveDate"));
		dr.setValue("Cust", d.GetCustName(CustID));

		if(drStat != null)
		{
			pl.SetValue("FSCStock", d.getStockName(drStat.getIntVal("FSCStockID")));
			pl.SetValue("FDCStock", d.getStockName(drStat.getIntVal("FDCStockID")));
			
			dr.setValue("Qty", drStat.getIntVal("Qty"));
			dr.setValue("Amount", drStat.getFVal("Amount"));
		}
		
		dr.setValue("Ext", pl);
		dr.setValue("state", drBill.getIntVal("state"));
	}

    @Override
    public void UpdateRowData() throws Exception {
		int FID = drItem.getIntVal("ID");
    	DataRow drBill = DBLocal.OpenDataRow(DBHelper.Cfg_StockBill, sqlBill.replace("@COND", "FID=?"), FID);
    	DataRow drStat = DBLocal.OpenDataRow("FID/i|Qty/i|Amount/f", sqlStat.replace("@COND", "m.FID=?"), FID);
    	UpdateRowData(drItem, drBill, drStat);
		mAdapter.notifyDataSetChanged();
    }

    @Override
    protected Object getListItemViewConfig() {
        // 列表项视图配置
        return "items: [" + 
        	"[v: { id:2010, w:16dp,h:16dp,lay-grv:c }, "+ 
        	"tv: { id:2011, fs: 14.2dp, text: headinfo, w:1w,color:#FF555555,padding: 0dp,h:wrap }, "+ 
        	"tv: { id:2012, fs: 13.6dp, text: date,w:80dp,bg1:g,color:gray, padding: 0dp, h:wrap, grv:r }] "+

	    	"[v: { id:2036, w:28dp,h:28dp,lay-grv:c, marginRight:0dp }, "+ 
	    	"v: { id:2030, w:48dp,h:48dp,lay-grv:c }, "+ 
        	"tv: { id:2031, fs: 14.2dp, text: info,w:5w,bg1:r,color:text, padding: 0dp,h:wrap }, "+ 
        	"tv: { id:2032, fs: 14.2dp, text: tot,w:90dp,bg1:g, padding: 0dp, h:wrap, grv:r }] "+ 
        "], padding: '10dp, 5dp, 10dp, 5dp', margin: 0dp, w:match, h:wrap, bg1:#33CCAAFF";
    }
    
    @Override
    protected void setListItemViewData(ParamList arg) {
		View convertView = arg.Get("view", View.class);
		DataRow dr = arg.Get("item", DataRow.class);
		View vHeadImg = convertView.findViewById(2010);
		TextView tvHeadInfo = type.as(convertView.findViewById(2011), TextView.class);
		TextView tvDate = type.as(convertView.findViewById(2012), TextView.class);

		View vImg = convertView.findViewById(2030);
		View vSelect = convertView.findViewById(2036);
		TextView tvInfo = type.as(convertView.findViewById(2031), TextView.class);
		TextView tvTot = type.as(convertView.findViewById(2032), TextView.class);
		ParamList pl = dr.$("Ext");
		String info = pl.toString();
		
		tvHeadInfo.setText(dr.getStrVal("Cust"));
		tvDate.setText(dr.getStrVal("FDate"));
		
		info = gs.JoinArray(new Object[] {
				dr.getValue("FBillNo"),
    			pl.DispText("从：", "FSCStock"),
    			pl.DispText("到：", "FDCStock"),
    			pl.get("FNote"),
    			SysData.isAdmin ? dr.DispText("业务员：", "Emp") : null,
		}, "\n");
		
		boolean readed = dr.getIntVal("Flag") == Bill.FLAG_READED;
		
		if(info.isEmpty()){
    		tvInfo.setText("无描述信息");
    		tvInfo.setTextColor(Color.GRAY);
		} else {
			tvInfo.setText(info);
    		tvInfo.setTextColor(readed ? Color.GRAY : HyColor.Text);
		}
		tvHeadInfo.setTextColor(readed ? Color.GRAY : 0xFF555555);
		
		tvInfo.setLineSpacing(0, 1.2f);

		int CID = dr.getIntVal("CID");
		int rid = CID == Bill.BTID_WTDX ? R.drawable.y2 : 
				  CID == Bill.BTID_DB_BH ? R.drawable.y8 : 
				  CID == Bill.BTID_DB_FH ? R.drawable.y9 : 
				  R.drawable.y6;
		vHeadImg.setBackgroundResource(readed ? R.drawable.mail_open : R.drawable.mail);
		vImg.setBackgroundResource(rid);
		
		vSelect.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
		vSelect.setBackgroundResource(dr.getBool("sel") ? R.drawable.c_select : R.drawable.c_none);
		
		SpannableStringHelper sh = new SpannableStringHelper();
		gs.getFmtQty(sh, dr.getIntVal("Qty"));
		sh.appendObj("\n");
		gs.getFmtMoney(sh, dr.getFVal("Amount"));
		tvTot.setLineSpacing(0, 1.2f);
		tvTot.setText(sh);
    }
    
    @Override
    protected void onListItemClick(HyListView s, HyEvent.LvItemClickEventParams arg){
		MainActions.OpenStockBill(context, drItem.getIntVal("CID"), drItem.getIntVal("ID"));
    }

    @Override
    protected void onListItemLongClick(HyListView s, HyEvent.LvItemLongClickEventParams arg) throws Exception{
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	try {
    		if(drItem == null) return;
        	int FID = drItem.getIntVal("ID");
    		int flag = DBLocal.ExecuteIntScalar("select Flag from StockBill where FID=?", FID);
    		if(flag == 0) {
    			DBLocal.ExecSQL("update StockBill set Flag=" + Bill.FLAG_READED + " where FID=?", FID);
    			ActRefresh();
//    			drItem.setValue("Flag", Bill.FLAG_READED);
//    			mAdapter.notifyDataSetChanged();
    		}
		} catch (Exception e) {
			ExProc.Show(e);
		}
        super.onActivityResult(requestCode, resultCode, data);
    } 

}
