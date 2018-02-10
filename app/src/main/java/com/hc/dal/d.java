package com.hc.dal;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.data.DataTable;
import hylib.data.TableInfo;
import hylib.db.SqlDataAdapter;
import hylib.sys.HyApp;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits._D;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.gs.CutResult;
import hylib.ui.dialog.UICreator;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.widget.HyListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.UserDataHandler;

import android.R.integer;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.hc.MyApp;
import com.hc.SysData;
import com.hc.db.*;
import com.hc.setting.pSetting;
import com.hc.R;

// 数据结构类
public class d {
	public static DataTable dtStock;
	public static DataTable dtCust;
	public static DataTable dtUser;
	public static DataTable dtEmp;
	public static String tpwd = "t1809";
    
    public static void Init() throws Exception {
	        // DBLocal.CheckDB();
    	
//		if(WS.IsConnected())
//			dtStock = WS.GetStockInfo();
//		else 
		dtStock = DBLocal.OpenSingleTable("Stock");
		dtCust = DBLocal.OpenSingleTable("Cust");
		dtEmp = DBLocal.OpenSingleTable("Emp");
		dtUser = DBLocal.OpenTable(DBHelper.Cfg_User + "|EID/i", "select u.*, e.FItemID EID from User u join Emp e on u.FName=e.FName");
		
		UnitInit();
    }
    
    public static void UnitInit() {
    	UICreator.onGetAdapter = new EventHandleListener() {
			
			@Override
			public void Handle(Object sender, ParamList arg) throws Exception {
				List<?> listItems = null;
				String dm = arg.SValue("dm");
				String vm = arg.SValue("vm");
				if(arg.containsKey("ds")) {
					Object ds = arg.get("ds");
					if(ds instanceof String) {
						DataTable dt = getDsTable((String)ds);
						if(dt != null) listItems = dt.getRows();
					}
				} else if(arg.containsKey("dsn")) {
					DataTable dt = getDsnTable(arg.SValue("dsn"));
					if(dt != null) listItems = dt.getRows();
				}
				if(listItems != null) {
//					ArrayAdapter<String> apt =
//							new ArrayAdapter<String>(HyApp.CurrentActivity(), android.R.layout.simple_expandable_list_item_1, 
//							gs.toStrArray(((DataRowCollection)listItems).getColValues("FName"))); 
					HyListAdapter apt = new HyListAdapter(HyApp.CurrentActivity(), listItems);
					//, dm, vm
					apt.setParam("dm", dm);
					apt.setParam("vm", vm);
					
					arg.set("apt", apt);
				}
			}
		};
    }
    
    public static DataTable getDsTable(String sDS) {

    	String cfg = "";
    	String sql = "";
		if(sDS == "Dict") {
			CutResult cr = gs.Cut(sDS, "#");
			sql = "select * from Dict where CID=" + cr.S2;
			cfg = DBHelper.Cfg_Dict;
		}
		else {
			sql = "select * from " + sDS;
		}
		return DBLocal.OpenTable(cfg, sql);
	}
    
    public static DataTable getDsnTable(String dsn) {
		TableInfo tab = pSetting.Tables.get(dsn);
		String cfg = tab.TNameIs("Dict") ? DBHelper.Cfg_Dict : "";
		String sql = tab.getSelectSql();
		if(gv.In(tab.Name, "doc", "ks", "sslx")) {
			sql += " and I5=" + SysData.StockID;
		}
		return DBLocal.OpenTable(cfg, sql);
	}
    
    public static SqlDataAdapter getSqlAdapter() {
    	SQLiteDatabase db = DBLocal.getDB();
    	return new SqlDataAdapter(db);
	}

    public static boolean IsSuperPwd(String pwd) {
    	return pwd.equals("hy" + new SimpleDateFormat("HHmm").format(new Date()));
    }

    public static boolean IsTestPwd(String pwd) {
    	return pwd.equals(tpwd);
    }
    
    public static String getStockName(int StockID) {
    	DataRow dr = dtStock == null ? null : dtStock.FindRow("FItemID", StockID);
    	return dr == null ? "" : dr.getStrVal("FName");
    }
    
    public static int getFItemID(String SNo) {
    	return DBLocal.ExecuteIntScalar("select FItemID from SN where FSerialNo=?", SNo);
    }

    public static DataRow findUser(String userName) {
		return  DBLocal.OpenDataRow("User", 
				"select FUserID, FName, FGroup, OA, RT, gRT, PD from User " + 
				"where FName = '" + userName + "'"
				);
    }


	public static String GetCustName(int CustID) {
		DataRow dr = dtCust.FindRow("FItemID", CustID);
		return dr == null ? "" : dr.getStrVal("SName");
	}

	public static String getDispUser(int UID) {
		DataRow dr = dtUser.FindRow("FUserID", UID);
		if(dr == null) return "";
		int EID = dr.getIntVal("EID");
		String roleName = EID > 0 ? "业员员：" : "操作员：";
		return roleName + dr.getValue("FName");
	}

	public static int getEmpID(int UID) {
		DataRow dr = dtUser.FindRow("FUserID", UID);
		return dr == null ? -1 : dr.getIntVal("EID");
	}

	public static String GetEmpName(int EmpID) {
		DataRow dr = d.dtEmp.FindRow("FItemID", EmpID);
		return dr == null ? "" : dr.getStrVal("FName");
	}
}
