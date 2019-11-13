package com.hc.dal;

import android.graphics.Color;

import com.hc.SysData;
import com.hc.db.DBLocal;
import com.hc.mo.bill.ProductRelpace;
import com.hc.pu;

import java.util.Date;

import hylib.data.DataRow;
import hylib.data.DataTable;
import hylib.toolkits.ArrayTools;
import hylib.toolkits.ExProc;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.util.Param;
import hylib.util.ParamList;

public class Bill {
    public final static int BTID_CGDD = 1011;	// *采购订单
    public final static int BTID_XSDD = 3091;	// *补货订单
    public final static int BTID_BHDD = 4091;	// *补货订单

    public final static int BTID_CG = 2011;	// 采购收货	
    public final static int BTID_QTRK = 9001;  //*其他入库单类型编号
    public final static int BTID_QTCK = 9002;  //其他出库单据类型（盘亏单）
    public final static int BTID_CGTH = 2012;   // 采购退货

    public final static int BTID_DB = 1;       //*调拨

    public final static int BTID_DB_BH = 11;   // *备货
    public final static int BTID_DB_FH = 12;   // *发货
    public final static int BTID_DB_FK = 13;   // *返库
    public final static int BTID_DB_BU = 14;   // *备货补库
    public final static int BTID_DB_TH = 15;   // *医院退货

    public final static int BTID_WTDX = 3010;  // 委托代销
    public final static int BTID_WTTH = 3011;  // 委托退货
    public final static int BTID_WTJS = 3003;  // 委托结算
    public final static int BTID_WTJSTH = 3004; // 委托结算退货

    public final static int BH = 1;     // 备货
    public final static int FH = 2;     // 发货
    public final static int FK = 3;     // 备货返库
    public final static int BU = 4;     // 备货补货
    public final static int TH = 5;     // 医院退货返库

    public static String nameOfExtTypeID(int ETID){
        if (ETID == BH) return "BH";
        if (ETID == FH) return "FH";
        if (ETID == FK) return "FK";
        if (ETID == BU) return "BU";
        if (ETID == TH) return "TH";
        return "";
    }

    public static int valueOfExtType(String name){
        if (name.equals("BH")) return BH;
        if (name.equals("FH")) return FH;
        if (name.equals("FK")) return FK;
        if (name.equals("BU")) return BU;
        if (name.equals("TH")) return TH;
        return 0;
    }
    
    public static int getExtBTID(int BTID, int ETID) {
		if(BTID == BTID_DB && ETID > 0) return BTID * 10 + ETID;
		return BTID;
	}

    public final static int CID_PD = 7896; 		// 盘点单

    public final static int FLAG_READED = 2; 		// 已读标记

    // 扩展字段定义
    public final static String FULL_FIELDS = "IID, FName, FModel, BatchNo, FKFDate, FPeriodDate, FUnit, Manuf, Reg, SP, Cls, orginPrice, FNote"; 
    public final static String EXT_FIELDS = "FKFDate, FPeriodDate, FUnit, Manuf, Reg, SP, orginPrice, Cls, FNote"; 
    public final static String REP_FIELDS = "FName, FModel, BatchNo, FKFDate, FPeriodDate, FUnit, Price, Manuf, Reg, FNote"; 
    
    public static int AppID2BTID(int ID)
    {
        if (ID == 610) return Bill.BTID_CGDD;
        if (ID == 620) return Bill.BTID_WTDX;
        return 0;
    }

    public static int BTID2AppID(int BTID)
    {
        if (BTID == Bill.BTID_CGDD) return 610;
        if (BTID == Bill.BTID_WTDX) return 620;
        return 0;
    }
    
    public static String GetBillNoHeader(int BTID)
    {
        if (BTID == BTID_CGDD) return "D";
        if (BTID == BTID_BHDD) return "BH";
        if (BTID == BTID_DB) return "DB";
        if (BTID == BTID_WTDX) return "X";
        return "";
    }

    public static String nameOf(int ExtBTID)
    {
        if (ExtBTID == BTID_CGDD) return "采购订单";
        if (ExtBTID == BTID_BHDD) return "备货补货";
        if (ExtBTID == BTID_DB) return "调拔";
        if (ExtBTID == BTID_WTDX) return "使用";
        if (ExtBTID == BTID_DB_BH) return "备货";
        if (ExtBTID == BTID_DB_FH) return "发货";
        return "";
    }
    public static String jdNameOf(int ExtBTID)
    {
        if (ExtBTID == BTID_WTDX) return "代销";
        return nameOf(ExtBTID);
    }

    public static String nameOf(int BTID, int ETID)
    {
    	return nameOf(getExtBTID(BTID, ETID));
    }

    public static String jdNameOf(int BTID, int ETID)
    {
        return jdNameOf(getExtBTID(BTID, ETID));
    }
	
    public static ParamList GetLocalSnInfo(String SNo, boolean tryConnServer) throws Exception {
    	ParamList pl = new ParamList();
		DataTable dt = DBLocal.OpenTable("",
				"select * from Item ic, SN s where ic.FItemID=s.FItemID and FSerialNo=?", SNo
				);
		if(dt.RowCount() == 0) {
            if(!pu.isValidSNo(SNo)) ExProc.ThrowMsgEx("无效流水号“" + SNo + "”！");
//            if(tryConnServer && dt.isEmpty() && SN.IsCorrectFormat(SNo) && Msgbox.Ask("无法获取流水号，是否远程连接服务器获取数据？"))
//            {
//                //if(!Network.isMobileAvailable()) ExProc.ThrowMsgEx("无可用网络！请检查地址设置是否正确！");
//                return type.as(WS.GetProductTraceInfo(SNo), ParamList.class);
//            }

            pl.SetValue("FItemID", 0);   // 产品编码
            pl.SetValue("FName", "未同步产品");
            pl.SetValue("SName", "未同步产品");
            //ExProc.ThrowMsgEx("数据库查无此流水号[" + SNo + "]记录！");
        }
        else{
            DataRow dr = dt.getRow(0);

            pl.SetValueByDataRowItem(dr, "FItemID");   // 产品编码
            pl.SetValueByDataRowItem(dr, "FNumber");   // 产品编码
            pl.SetValueByDataRowItem(dr, "FName");     // 产品名称
            pl.SetValueByDataRowItem(dr, "SName");     // 产品简称
            pl.SetValueByDataRowItem(dr, "FBatchNo");  // 产品批号
            pl.SetValueByDataRowItem(dr, "FModel");    // 规格型号
            pl.SetValueByDataRowItem(dr, "FSerialNo"); // 流水号
            pl.SetValueByDataRowItem(dr, "FKFDate"); 	// 生产日期
            pl.SetValueByDataRowItem(dr, "FPeriodDate");// 有效期至
            pl.SetValueByDataRowItem(dr, "FKFPeriod"); 	// 保质期
        }

    	return new ParamList("info", pl);
    }

    public static DataRow FindProductByNameAndSpec(String name, String spec) throws Exception
    {
        DataTable dt = DBLocal.OpenTable("FItemID/i", "select * from Item where FModel='" + spec + "'");
        if(dt.getRowCount() > 1) 
	        // 如果规格一致，模糊匹配名称，找到最名称符合的一个产品
	        for (int i = 0; i < name.length(); i++) {
	        	char c = name.charAt(i);
	            for (int j = dt.getRows().size() - 1; j >= 0; j--) {
	    			String FName = (String)dt.getValue(j, 1);
	    			if(FName.length() < i || FName.charAt(i) != c)
	    				dt.getRows().remove(j);
	    		}
	            if(dt.getRowCount() == 1) break;
			}
        return dt.getRowCount() == 1 ? dt.getRow(0) : null;
    }

    public static int FindProductItemID(String name, String spec) throws Exception
    {
    	DataRow dr = FindProductByNameAndSpec(name, spec);
    	return dr == null ? -1 : dr.getIntVal("FItemID");
    }
    
    public static int GetBillNoMaxIndex(int BTID)
    {
        String header = GetBillNoDateHeader(BTID, new Date());
        String No = DBLocal.ExecuteStrScalar("select MAX(BillNo) BillNo from Bill where BillNo like '" + header + "_____'");
        return gv.IsEmpty(No) ? 0 : gv.IntVal(No.substring(header.length()));
    }

    public static String MakeBillNo(int BTID, Date date, int No)
    {
        return GetBillNoDateHeader(BTID, date) + String.format("%05d", No);
    }
    
    public static String NewBillNo(int BTID)
    {
        int index = GetBillNoMaxIndex(BTID) + 1; 
        return MakeBillNo(BTID, new Date(), index);
    }

    public static String GetBillNoDateHeader(int BTID, Date date)
    {
        return GetBillNoHeader(BTID) + gv.getFormatDate(date, "yyyyMMdd");
    }

    public static Float GetPlanPrice(int CustID, int FItemID)
    {
        return DBLocal.ExecuteFloatScalar("select pd.FPrice from PriceCust pc,PricePlanDetail pd where pc.PPID=pd.PPID and pc.CustID=? and FItemID=?", 
        									CustID, FItemID);
    }

    /*
     * 获取产品批号对应的效期
     */
    public static DataRow GeBatchNoExpInfo(int FItemID, String BatchNo) throws Exception
    {
        return DBLocal.OpenDataRow("", "select top 1 FKFDate, FPeriodDate, FKFPeriod from BillDetail where IID=? and BatchNo=? order by BDID desc", 
        							FItemID, BatchNo);
    }

    /*
     * 获取产品批号对应的效期
     */
    public static DataRow GetSNInfo(String SNo) throws Exception
    {
    	return DBLocal.OpenDataRow("FItemID/i|FID/i", "select * from SN, Item i where SN.FItemID=i.FItemID and FSerialNo=?", SNo);
    }

    /*
     * 获取流水号对应的产品ID
     */
    public static int GetSnIID(String SNo)
    {
    	return DBLocal.ExecuteIntScalar("select FItemID from SN where FSerialNo=?", SNo);
    }

    public final static int GI_PRICE = 1;
	public static CharSequence getBillItemInfo(DataRow dr, int options) {

		String SNo = dr.getStrVal("SNo");
		if(!gv.IsEmpty(SNo)) SNo = "No." + SNo;
		
		String info = gs.JoinArray(new String[] {
				SNo,
				dr.getStrVal("FName"),
				Param.SVal("规格型号: ", dr.getValue("FModel")),
				Param.SVal("产品批号: ", dr.getValue("BatchNo")),
				Param.SVal("有效期至: ", dr.getValue("FPeriodDate")),
				Param.SVal("备  注：       ", dr.getValue("Note")),
		}, "\n");
		
		if(!gv.ContainEnumVal(options, GI_PRICE)) return info;
		
		SpannableStringHelper sh = new SpannableStringHelper();
		sh.appendObj(info + "\n");
		
		sh.appendObj("单 价：");
		sh.startSpan();
		float price = dr.getFVal("Price");
		sh.appendMoney(price, Color.RED, 1.0f);
		
		return sh;
	}

	public static CharSequence getStockBillItemInfo(DataRow dr) {
		String SNo = dr.getStrVal("SNo");
		if(!gv.IsEmpty(SNo)) SNo = "No." + SNo;
		String info = gs.JoinArray(new String[] {
				SNo,
				dr.getStrVal("FName"),
				Param.SVal("规格型号: ", dr.getValue("FModel")),
				Param.SVal("产品批号: ", dr.getValue("FBatchNo")),
				Param.SVal("有效期至: ", dr.getValue("FPeriodDate")),
				Param.SVal("备  注：       ", dr.getValue("Note")),
		}, "\n");
		
		return info;
	}
	
	public static CharSequence getBillPriceInfo(DataRow dr) {
		SpannableStringHelper sh = new SpannableStringHelper();
		float price = dr.getFVal("Price");
		sh.appendMoney(price, Color.RED, 1.0f);
		return sh;
	}
	
	public static CharSequence getStockBillPriceInfo(DataRow dr) {
		SpannableStringHelper sh = new SpannableStringHelper();
		float price = dr.getFVal("FInTaxAmount");
		sh.appendMoney(price, Color.RED, 1.0f);
		return sh;
	}

	/*
	 * 整理扩展参数，如果参数值与流水码信息一致，去除相同参数，并将差异值写入扩展参数
	 */
	public static void TrimParamToExt(ParamList pl) throws Exception {
		Object[] reps = ProductRelpace.getReps(pl);
		String SNo = pl.SValue("SNo");
		DataRow drSN = Bill.GetSNInfo(SNo);
		ParamList plSN = drSN == null ? new ParamList() : drSN.toParamList();
		plSN.RenKeys("FBatchNo BatchNo, FItemID IID");
		
		Param[] orgin = plSN == null ? null : plSN.toArray();
		Param[] current = pl.getParams(plSN.isEmpty() ? FULL_FIELDS : EXT_FIELDS);
		Param[] except = Param.convertParamArray(ArrayTools.Except(current, orgin, gv.hashFunc));
		
		ParamList plExt = new ParamList(except);

		// 以下字段移至扩展字段
		pl.MoveTo("SP", plExt);
		pl.MoveTo("FNote", plExt);
		
		plExt.RemoveEmpties();
		ProductRelpace.setExtParamReps(plExt, reps);
		pl.SetValidValue("Ext", plExt);
	}

	/*
	 * 用扩展参数覆盖基参数
	 */
	public static void CoverExtParams(ParamList pl) throws Exception {
    	ParamList plExt = pl.GetParamList("Ext");
    	pl.AddRange(plExt);
    	//pl.Remove("Ext");
	}

	public static String GetCustSP() {
		DataRow dr = d.dtCust.FindRow("FItemID", SysData.CustID);
		return dr == null ? "辽宁冠美科技有限公司" : dr.getStrVal("Inc");
	}
	

	public static String GetShortName(String name) {
		int pos = name.replace("（", "(").indexOf("(");
		return pos > 0 ? name.substring(0, pos) : name;
	}
}
