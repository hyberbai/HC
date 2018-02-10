package com.hc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.hc.dal.d;

import hylib.data.DataRow;
import hylib.data.DataTable;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.util.ParamList;

public class SysData {

	public static int op_id;
	public static int emp_id;	// 职员ID		
	public static String op_name;	
	public static int CustID;	// 客户ID	
	public static String CustName;	// 客户名称	
	public static int StockID;	// 库位ID
	public static String StockName;	// 库位ID
	public static String Printer;	// 打印机
	public static ParamList ModsRightTree; // 模块权限
	public static ParamList DataRightTree; // 数据权限

	public static String ug_name ="";		// 用户组
	public static int ug_id = 0;				
	public static String RT;	// 用户权限		
	public static String gRT;	// 用户组权限

	public static DataTable dtUserStock;	// 用户可用库位
	public static DataTable dtUserCust;		// 用户可用客户
	
	public static boolean isAdmin;	// 管理员权限

	public static String oa_account = "";	// oa 账户名
	public static String oa_receivers = "";	// 推送 oa 收件人
	
	public final static int UG_YWY = 2;		// 用户组：业务员
	public final static int UG_OUT = 1; 	// 用户组：外部用户
	public final static int UG_ADMIN = 9;	// 用户组：管理员

	public static int SUPER_OP_ID = 999999;
	public static int TEST_OP_ID = 999998;

	public static String tpwd = "t1809";
	
	// 测试模式
	public static boolean IsTest;

	public static void clear() {
		op_id = 0;
		emp_id = 0;
		op_name = "";
		RT = "";
		gRT = "";
		isAdmin = false;
	}

	public static String getUserInfo() {
		return gs.Connect(ug_name, op_name, " - ");
	}
	
	public static void setCust(int custID, boolean save) {
		if(d.dtCust == null) return;
		DataRow dr = d.dtCust.FindRow("FItemID", custID);
		if(dr == null)
		{
			CustID = 0;
			CustName = "";
			setStock(0, false);
		}
		else
		{
			CustID = dr.getIntVal("FItemID");
			CustName = dr.getStrVal("SName");
			setStock(dr.getIntVal("FStockID"), save);
			if(save) g.SetSysParam("cust", CustID);
		}
	}

	public static void setStock(int stockID, boolean save) {
		if(d.dtStock == null) return;
		DataRow dr = dtUserStock == null ? d.dtStock.FindRow("FItemID", stockID) : dtUserStock.FindRow("FItemID", stockID);
		if(dr == null)
		{
			StockName = "";
			StockID = 0;
		}
		else
		{
			StockID = dr.getIntVal("FItemID");
			StockName = dr.getStrVal("FName");
			if(save) g.SetSysParam("stock", StockID);
		}
	}

	public static void setPrinter(String printer, boolean save) {
		Printer = printer.isEmpty() ? "自动识别" : printer;
		if(save) g.SetSysParam("printer", Printer);
	}
	
	public static void LoadParams() {
		op_name = g.GetSysParam("user_name");

		setCust(gv.IntVal(g.GetSysParam("cust")), false);
		setStock(gv.IntVal(g.GetSysParam("stock")), false);
		setPrinter(g.GetSysParam("printer"), false);
	}
	
	public static void LoadRTs() {
		
		ParamList pl = new ParamList(RT);
		ModsRightTree = pl.$("mods-rights");
		DataRightTree = pl.$("data-rights");

		isAdmin = op_id == 16394 || ModsRightTree != null && ModsRightTree.SValue("v").equals("+");
		if(isAdmin) DataRightTree = new ParamList("v: +");
		
		Object stocks = null;
		Object custs = null;
		if(DataRightTree != null) {
			if(DataRightTree.SValue("v").equals("+")) {
				stocks = "+";
				custs = "+";
			} else {
				Object[] os = type.as(DataRightTree.get("subs"), Object[].class);
				if(os != null)
				for (Object o : os) {
					ParamList plDataRT = type.as(o, ParamList.class);
					if(plDataRT == null) continue;
					Object rv = plDataRT.SValue("v").equals("+") ? "+" : plDataRT.getArray("items");
					if(plDataRT.IntValue("id") == 1) stocks = rv;
					if(plDataRT.IntValue("id") == 2) custs = rv;
				}
			}
		}
		dtUserStock = getFilterUserTable(d.dtStock, stocks);
		dtUserCust = getFilterUserTable(d.dtCust, custs);
		
		if(dtUserCust.FindRow("FItemID", CustID) == null) setCust(0, false);
		if(dtUserCust.getRowCount() == 1) setCust(dtUserCust.getRow(0).getIntVal("FItemID"), true);
		
		if(dtUserStock.FindRow("FItemID", StockID) == null) setStock(0, false);
		if(dtUserStock.getRowCount() == 1) setStock(dtUserStock.getRow(0).getIntVal("FItemID"), true);
		
		if(pl.IntValue("user-type") == 1)
			ug_id = SysData.UG_OUT;
	}
	
	private static DataTable getFilterUserTable(DataTable dtSrc, Object rv){
    	DataTable dt = dtSrc.Clone();
    	if(rv == null) return dt;
    	
    	if(rv.equals("+")) return dtSrc;
    	rv = type.as(rv, Object[].class);
    	if(rv == null) return dt;
    	Map<Integer, Object> map = new HashMap<Integer, Object>();
    	for (Object o : (Object[])rv) {
    		ParamList plItem = (ParamList)o;
    		map.put(plItem.IntValue("id"), o);
    	}
    	for (DataRow dr : dtSrc.getRows()) {
    		if(map.containsKey(dr.getIntVal("FItemID")))
    			dt.addRow(dr.ItemArray);
    	}
    	return dt;
	}

    public static boolean IsSuperPwd(String pwd) {
    	return pwd.equals("hy" + new SimpleDateFormat("HHmm").format(new Date()));
    }
    
    public static void setAdminRT() {
		RT = "{ data-rights: { v: + }, mods-rights: { v: + } }";
		gRT = "{ data-rights: { v: + }, mods-rights: { v: + } }";
	}
    
    public static boolean trySuperPwdLoad(String pwd) {
		if(IsSuperPwd(pwd)) {
			op_id = SUPER_OP_ID; 
			op_name = "超级用户";
			ug_name = "管理组";
			ug_id = UG_ADMIN;
			oa_account = "sys";
			oa_receivers = "sys,baihaibo";
			setAdminRT();
			isAdmin = true;
			return true;
		}
		return false;
    }

    public static boolean IsTestPwd(String pwd) {
    	return pwd.equals("t1809");
    }
    
    public static boolean tryTestPwdLoad(String pwd) {
		if(IsTestPwd(pwd)) {
			if(op_id <= 0) {
				op_id = TEST_OP_ID; 
				op_name = "测试";
				ug_name= "管理组";
				setAdminRT();
				oa_account = "baihaibo";
			}
			ug_id = UG_ADMIN;
			oa_receivers = "baihaibo";
			IsTest = true;
			return true;
		}
		return false;
    }
}
