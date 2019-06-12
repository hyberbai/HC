package com.hc.mo.bill;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.hc.MainActions;
import com.hc.R;
import com.hc.SysData;
import com.hc.dal.Bill;
import com.hc.dal.WS;
import com.hc.dal.d;
import com.hc.db.DBHelper;
import com.hc.db.DBLocal;

import java.util.ArrayList;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.data.DataTable;
import hylib.toolkits.ExProc;
import hylib.toolkits.HyColor;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits._D;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.util.ParamList;
import hylib.widget.HyEvent;
import hylib.widget.HyListView;

import static com.hc.SysData.StockID;

public class ActHisListStockBill extends ActHisList {
    public boolean IsRealTime = false;

    @Override
    protected void setInitParams() {
        CIDs = new int[]{0, Bill.BTID_DB_BH, Bill.BTID_DB_FH, Bill.BTID_DB_FK, Bill.BTID_WTDX};
        pageNames = "全部, 备货单, 调拔单, 返库单, 代销单";
        mTitle = "金蝶单据";
    }

    @Override
    protected void setOptionsMenuParams(ParamList pl) throws Exception {
        final DataRowCollection rows = new DataRowCollection("name|text|pl", new Object[][]{
                new Object[]{"RealTime", "实时数据", "checked: false"},
                new Object[]{"Refresh", "刷新", ""},
        });

        pl.set("Items", rows);
        pl.set("width", "100dp");
    }

    private String GetCond(String extTypeFieldName) {
        String cond = SysData.isAdmin ? "1=1" : "(" + extTypeFieldName + "='FK' and FEmpID=0 or FEmpID=" + SysData.emp_id + ")";
        if (!SysData.isAdmin && SysData.CustID > 0) cond += " and FCustID=" + SysData.CustID;
        return cond;
    }

    public String tabBillConfig = DBHelper.Cfg_StockBill + "|Qty/i|Amount/f|FSCStockID/i|FDCStockID/i";

    private DataTable LoadBillTableFromLocal() {
        String sqlBill = "select m.*, d.*, e.FName Emp from StockBill m \n"
                + "JOIN (select FID, SUM(FQty) Qty, SUM(FInTaxAmount) Amount, MIN(FSCStockID) FSCStockID, MAX(FDCStockID) FDCStockID from StockBillEntry group by FID) d on m.FID=d.FID\n"
                + "JOIN Emp e ON m.FEmpID=e.FItemID\n"
                + "where @COND";

        return DBLocal.OpenTable(tabBillConfig, sqlBill.replace("@COND", GetCond("ExtType")));
    }

    private DataTable LoadBillTableFromServer() throws Exception {
        String cond = GetCond("FUserDefine5");
       // cond = "(" + cond + ")" + " and m.FID>=15711";
        DataTable result = WS.GetJdBillList(cond);
        result.SetColsType(tabBillConfig);
        return result;
    }

    @Override
    protected void LoadData() throws Exception {
        DataTable dtBill = IsRealTime ? LoadBillTableFromServer() : LoadBillTableFromLocal();

        int ordNo = 0;
        dtHis = new DataTable("his", "OrdNo/i|ID/i|FBillNo|FDate|Cust|FDCStock|FSCStock|Emp|CID/i|Qty/i|Amount/f|state/i|Flag/i|sel/b|Ext/e");

        for (DataRow drBill : dtBill.getRows()) {
            //ParamList plBillExt = new ParamList(drBill.getValue("Ext"));
            _D.Out(drBill.getValue("FDate"));

            int BTID = drBill.getIntVal("FBillTypeID");
            int ETID = Bill.valueOfExtType(drBill.getStrVal("ExtType"));
            int FDCStockID = drBill.getIntVal("FDCStockID");
            int FSCStockID = drBill.getIntVal("FSCStockID");

            if (BTID == Bill.BTID_DB) {
                if (ETID > 0)
                    BTID = BTID * 10 + ETID;
                else {
                    //	drStat = dtStat.FindRow("FID", FID);
                    if (FDCStockID == StockID)
                        BTID = Bill.BTID_DB_FH;

                    if (FDCStockID == SysData.MainStockID)
                        BTID = Bill.BTID_DB_FK;
                }
            }

            if(BTID == Bill.BTID_DB_FK && StockID != 0 && FSCStockID != StockID) continue;


            if (!gv.In(BTID, CIDs)) continue;

            DataRow dr = dtHis.newRow();

            dr.setValue("OrdNo", ordNo++);
            dr.setValue("Emp", drBill.getValue("Emp"));
            dr.setValue("CID", BTID);
            dr.setValue("ID", drBill.getValue("FID"));
            dr.setValue("Flag", drBill.getValue("Flag"));
            dr.setValue("FBillNo", drBill.getValue("FBillNo"));

            ParamList plBillExt = new ParamList(drBill.getValue("Ext"));
            int CustID = drBill.getIntVal("FCustID");

            dr.setValue("FDate", drBill.getValue("FDate"));
            dr.setValue("SaveDate", drBill.getValue("SaveDate"));
            dr.setValue("Cust", d.GetCustName(CustID));

            plBillExt.SetValue("FSCStock", d.getStockName(FSCStockID));
            plBillExt.SetValue("FDCStock", d.getStockName(FDCStockID));

            dr.setValue("Qty", drBill.getIntVal("Qty"));
            dr.setValue("Amount", drBill.getFVal("Amount"));

            dr.setValue("Ext", plBillExt);
            dr.setValue("state", drBill.getIntVal("state"));

            dtHis.addRow(dr);
        }

        dtHis.Sort("ordNo desc");
    }

    private void SetRealTime(boolean isRealTime) {
        IsRealTime = isRealTime;
        pwOptions.setItemParams("RealTime", "checked:" + isRealTime);
    }

    public void ActRealTime() {
        SetRealTime(!IsRealTime);
        if (!ActRefresh()) {
            if (IsRealTime) SetRealTime(false);
        }
        ;
        $Set(R.id.tv_title, mTitle + (IsRealTime ? " - 实时" : ""));
    }

    @Override
    protected Object getListItemViewConfig() {
        // 列表项视图配置
        return "items: [" +
                "[v: { id:2010, w:16dp,h:16dp,lay-grv:c }, " +
                "tv: { id:2011, fs: 14.2dp, text: headinfo, w:1w,color:#FF555555,padding: 0dp,h:wrap }, " +
                "tv: { id:2012, fs: 13.6dp, text: date,w:80dp,bg1:g,color:gray, padding: 0dp, h:wrap, grv:r }] " +

                "[v: { id:2036, w:28dp,h:28dp,lay-grv:c, marginRight:0dp }, " +
                "v: { id:2030, w:48dp,h:48dp,lay-grv:c }, " +
                "tv: { id:2031, fs: 14.2dp, text: info,w:5w,bg1:r,color:text, padding: 0dp,h:wrap }, " +
                "tv: { id:2032, fs: 14.2dp, text: tot,w:90dp,bg1:g, padding: 0dp, h:wrap, grv:r }] " +
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

        int CID = dr.getIntVal("CID");

        tvHeadInfo.setText(dr.getStrVal("Cust"));
        tvDate.setText(dr.getStrVal("FDate"));

        ArrayList<Object> infoList = new ArrayList<Object>();
        infoList.add(dr.getValue("FBillNo"));
        if (CID == Bill.BTID_WTDX) {
            infoList.add(pl.SValue("FSCStock"));
        } else {
            infoList.add(pl.DispText("从：", "FSCStock"));
            infoList.add(pl.DispText("到：", "FDCStock"));
        }
        infoList.add(pl.get("FNote"));
        if (SysData.isAdmin) infoList.add(dr.DispText("业务员：", "Emp"));

        String info = gs.JoinArray(infoList.toArray(), "\n");

        boolean readed = dr.getIntVal("Flag") == Bill.FLAG_READED;

        if (info.isEmpty()) {
            tvInfo.setText("无描述信息");
            tvInfo.setTextColor(Color.GRAY);
        } else {
            tvInfo.setText(info);
            tvInfo.setTextColor(readed ? Color.GRAY : HyColor.Text);
        }
        tvHeadInfo.setTextColor(readed ? Color.GRAY : 0xFF555555);

        tvInfo.setLineSpacing(0, 1.2f);

        int rid = CID == Bill.BTID_WTDX ? R.drawable.y2 :
                CID == Bill.BTID_DB_BH ? R.drawable.y8 :
                CID == Bill.BTID_DB_FH ? R.drawable.y9 :
                CID == Bill.BTID_DB_FK ? R.drawable.y7 :
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
    protected void onListItemClick(HyListView s, HyEvent.LvItemClickEventParams arg) {
        MainActions.OpenStockBill(context, drItem.getIntVal("CID"), drItem.getIntVal("ID"), IsRealTime);
    }

    @Override
    protected void onListItemLongClick(HyListView s, HyEvent.LvItemLongClickEventParams arg) throws Exception {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (drItem == null || IsRealTime) return;
            int FID = drItem.getIntVal("ID");
            int flag = DBLocal.ExecuteIntScalar("select Flag from StockBill where FID=?", FID);
            if (flag == 0) {
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
