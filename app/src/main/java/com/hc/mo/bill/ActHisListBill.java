package com.hc.mo.bill;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.hc.MainActions;
import com.hc.R;
import com.hc.SysData;
import com.hc.dal.Bill;
import com.hc.dal.d;
import com.hc.db.DBHelper;
import com.hc.db.DBLocal;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.data.DataTable;
import hylib.toolkits.ExProc;
import hylib.toolkits.HyColor;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.ui.dialog.Msgbox;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.widget.HyEvent;
import hylib.widget.HyListView;

public class ActHisListBill extends ActHisList {

	@Override
    protected void setInitParams()
	{
		CIDs = new int[] { 0, Bill.BTID_WTDX, Bill.BTID_DB_BU, Bill.BTID_DB_TH };
		pageNames = "全部, 使用单, 补货单, 返货单";
		mTitle = "历史记录";
    }

	@Override
    public HyListView CreateList(int CID) throws Exception{
		return super.CreateList(CID);
    }

    @Override
    protected void setViewPagerParams(ParamList pl) throws Exception {
		super.setViewPagerParams(pl);
    }

    @Override
    protected Object getListItemViewConfig() {
        // 列表项视图配置
        return "items: [" + 
	    	"[v: { id:2010, w:16dp,h:16dp,lay-grv:c }, "+ 
	    	"tv: { id:2011, fs: 14.2dp, marginLeft:-8dp, text: headinfo, w:1w,color:#FF555555,padding: 3dp,h:wrap }, "+ 
	    	"tv: { id:2012, fs: 13.6dp, text: date,w:80dp,bg1:g,color:gray, padding: 0dp, h:wrap, grv:r }] "+
	        	
	    	"[v: { id:2036, w:28dp,h:28dp,lay-grv:c }, "+ 
	    	"v: { id:2030, w:48dp,h:48dp,lay-grv:c }, "+ 
	    	"tv: { id:2031, fs: 14.2dp, marginLeft:-8dp, text: info,w:1w,bgc1:r,color:text, padding: 0dp,h:wrap }, "+ 
	    	"tv: { id:2032, fs: 14.2dp, text: tot,w:90dp,bgc1:g,color:black, padding: 0dp, h:wrap, grv:r }] "+ 
	    "], padding: '10dp, 5dp, 10dp, 5dp', margin: 0dp, w:match, h:wrap, bg1:#33CCAAFF";
    }
    
    @Override
    protected void setListItemViewData(ParamList arg) {
		View convertView = arg.Get("view", View.class);

		View vHeadImg = convertView.findViewById(2010);
		TextView tvHeadInfo = type.as(convertView.findViewById(2011), TextView.class);
		TextView tvDate = type.as(convertView.findViewById(2012), TextView.class);

		View vImg = convertView.findViewById(2030);
		View vSelect = convertView.findViewById(2036);
		TextView tvInfo = type.as(convertView.findViewById(2031), TextView.class);
		TextView tvTot = type.as(convertView.findViewById(2032), TextView.class);
		
		DataRow dr = arg.Get("item", DataRow.class);
		int CID = dr.getIntVal("CID");
		ParamList pl = dr.$("Ext");
		String info = pl.toString();

		
		tvHeadInfo.setText(dr.getStrVal("Cust"));
		tvDate.setText(dr.getStrVal("FDate"));
		
		String hzInfo = gs.JoinArray(new Object[] {
				pl.DispText("", "HZNo HZ"),
				pl.containsKey("Sex") ? pl.get("Sex") : "",
				pl.IntValue("Age") > 0 ? pl.IntValue("Age") + "岁" : ""
		}, " ");
		
		info = gs.JoinArray(new Object[] {
				Param.SVal("患者：", hzInfo),
    			pl.DispText("医生：", "Doc ZJ"),
    			pl.get("FNote"),
    			SysData.isAdmin ? d.getDispUser(dr.getIntVal("UID")) : null
		}, "\n");
		if(info.isEmpty()){
    		tvInfo.setText("无描述信息");
    		tvInfo.setTextColor(Color.GRAY);
		} else {
			tvInfo.setText(info);
    		tvInfo.setTextColor(HyColor.Text);
		}

		vHeadImg.setBackgroundResource(dr.getIntVal("state") == 1 ? R.drawable.s_upload : R.drawable.s_upload_gray);
		int rid = CID == Bill.BTID_WTDX ? R.drawable.y2 : 
				  CID == Bill.BTID_DB_BU ? R.drawable.y3 :
				  CID == Bill.BTID_DB_TH ? R.drawable.y7 :
				  CID == Bill.CID_PD ? R.drawable.y4 :
				  R.drawable.y1;
		vImg.setBackgroundResource(rid);
		vSelect.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
		vSelect.setBackgroundResource(dr.getBool("sel") ? R.drawable.c_select : R.drawable.c_none);
		
		SpannableStringHelper sh = new SpannableStringHelper();
		gs.getFmtQty(sh, dr.getIntVal("Qty"));
		sh.appendObj("\n");
		gs.getFmtMoney(sh, dr.getFVal("Amount"));
		tvInfo.setLineSpacing(0, 1.2f);
		tvTot.setLineSpacing(0, 1.2f);
		tvTot.setText(sh);
    }
    
    @Override
    protected void onListItemClick(HyListView s, HyEvent.LvItemClickEventParams arg) throws Exception{
    	if(isEditMode)
    		toggleSelect();
    	else
    		MainActions.OpenBill(context, drItem.getIntVal("CID"), drItem.getIntVal("ID"));
    }
    
    public void ActDelete() {
    	try {
    		DataRowCollection rows = getSelected();
        	if(gv.IsEmpty(rows)) ExProc.ThrowMsgEx("所选不能为空！");
    		
        	int[] IDs = getSelectedIDs(rows);
    		if(MainActions.DeleteBill(context, drItem.getIntVal("CID"), IDs)) {
    			for (DataRow dr : rows) 
    				dr.Remove();
    			ActRefresh();
    			//mAdapter.notifyDataSetChanged();
    		}
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
    
    public void ActStats() {
    	try {
    		final DataRowCollection rows = getSelected();
        	if(gv.IsEmpty(rows)) ExProc.ThrowMsgEx("所选不能为空！");
        	
    		Msgbox.Choose("汇总统计", ActStatsBill.statNames, new ParamList("w: 270dp"), new DialogInterface.OnClickListener(){
    	  	    public void onClick(DialogInterface dialog, int which) {  
    	  			if(which < 0) return;
    	  			startActivity(ActStatsBill.class, 0, new Param("CID", which + 1), new Param("IDs", getSelectedIDs(rows)));
    	  			
    	  	    }	
    	  	});
		} catch (Exception e) {
			ExProc.Show(e);
		}
    }

    protected String sqlBill = "select * from Bill where @COND";
    protected String sqlStat = "select m.BID, SUM(Qty) Qty, SUM(Price*Qty) Amount from Bill m, BillDetail d where m.BID=d.BID and @COND group by m.BID";
    @Override
    protected void LoadData() throws Exception {
    	String cond = SysData.isAdmin ? "1=1" : "UID=" + SysData.op_id;
    	DataTable dtBill = DBLocal.OpenTable(DBHelper.Cfg_Bill, sqlBill.replace("@COND", cond));
    	DataTable dtStat = DBLocal.OpenTable("BID/i|Qty/i|Amount/f", sqlStat.replace("@COND", cond));
    	
    	int ordNo = 0;
    	dtHis = new DataTable("his", "OrdNo/i|ID/i|UID/i|FDate|Cust|CID/i|Qty/i|Amount/f|state/i|sel/b|Ext/e");
    	for (DataRow drBill : dtBill.getRows()) {
			if(!SysData.isAdmin && SysData.CustID > 0){
				ParamList plBillExt = new ParamList(drBill.getValue("Ext"));
				int CustID = plBillExt.IntValue("CustID");
				if(SysData.CustID != CustID) continue;
			}

			int BID = drBill.getIntVal("BID");
			DataRow drStat = dtStat.FindRow("BID", BID);
			
			DataRow dr = dtHis.newRow();
			
			dr.setValue("OrdNo", ordNo++);
			dr.setValue("UID", drBill.getValue("UID"));
			dr.setValue("CID", drBill.getValue("BTID"));
			dr.setValue("ID", drBill.getValue("BID"));

			UpdateRowData(dr, drBill, drStat);
			
			dtHis.addRow(dr);
		}

    	dtHis.Sort("ordNo desc");
	}
    
    public void UpdateRowData(DataRow dr, DataRow drBill, DataRow drStat) throws Exception {

		ParamList plBillExt = new ParamList(drBill.getValue("Ext"));
		int CustID = plBillExt.IntValue("CustID");
		
		ParamList pl = new ParamList(plBillExt);
		
		dr.setValue("FDate", plBillExt.get("BillDate"));
		dr.setValue("SaveDate", drBill.getValue("SaveDate"));
		dr.setValue("Cust", d.GetCustName(CustID));
		
		if(drStat != null)
		{
			dr.setValue("Qty", drStat.getIntVal("Qty"));
			dr.setValue("Amount", drStat.getFVal("Amount"));
		}
		
		dr.setValue("Ext", pl);
		dr.setValue("state", drBill.getIntVal("state"));
	}

    @Override
    public void UpdateRowData() throws Exception {
		int BID = drItem.getIntVal("ID");
    	DataRow drBill = DBLocal.OpenDataRow(DBHelper.Cfg_Bill, sqlBill.replace("@COND", "BID=?"), BID);
    	DataRow drStat = DBLocal.OpenDataRow("BID/i|Qty/i|Amount/f", sqlStat.replace("@COND", "m.BID=?"), BID);
    	UpdateRowData(drItem, drBill, drStat);
		mAdapter.notifyDataSetChanged();
    }
}
