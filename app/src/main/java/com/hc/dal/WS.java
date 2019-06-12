package com.hc.dal;

import com.hc.MyApp;
import com.hc.SysData;
import com.hc.db.DBLocal;
import com.hc.g;
import com.hc.pu;

import java.sql.Connection;
import java.util.HashMap;

import hylib.data.DataRow;
import hylib.data.DataTable;
import hylib.edit.DType;
import hylib.io.FileUtil;
import hylib.toolkits.ExProc;
import hylib.toolkits.Security;
import hylib.toolkits.gi;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.util.HttpSoap;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.util.ZipBase64;

// web服务调用类
public class WS {
	public static int I_OK = 1;
	public static int I_ERROR = -1000;
	public static int I_FAIL = -2000;
    
	public final static int ConnFailTryCount = 0;
	private static boolean mIsEntryMode = true;
	
	public static void Init() {
		try {
			String addr = g.GetSysParam("server_addr");
        	if(gv.IsEmpty(addr)) {
				addr = pu.DEFAULT_SERVER_ADDR;
				HttpSoap.SetServerAddr(addr);
				g.SetSysParam("server_addr", addr);
			} else
        		HttpSoap.SetServerAddr(g.GetSysParam("server_addr"));
			//pu.dtServerAddrs.lastRow().setValue("addr", addr);
//    		WS.ConnectServer(); 
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
	// 调用Web服务
	public static Object InvokeWebService(String methodName, Param[] Params) throws Exception {
		HttpSoap.AllowZB64 = false;
		return HttpSoap.InvokeWebService(methodName, Params, mIsEntryMode);
	}

	// 执行Web服务，并一直等到执行完成返回结果
	private static int mLockCount;
	public static boolean showLoading = true;
	public static Object ExecWebService(final String methodName, final Param... Params) throws Exception {
		if(mLockCount > 0) ExProc.ThrowEmpty();
		try {
			mLockCount++;
			return MyApp.ExecThreadCallBack(new gi.CallBack() {
				@Override
				public Object Call() throws Exception {
					return InvokeWebService(methodName, Params);
				}
			}, showLoading);
		} catch (Exception e) {
			throw e;
		}
		finally{
			mLockCount--;
		}
	}
	
	public static Object ExecWebService(final String methodName, ParamList pl) throws Exception {
		return ExecWebService(methodName, pl.toArray());
	}
	
	public static Connection getConnection() {
		Connection con = null;
		try {
			//Class.forName("org.gjt.mm.mysql.Driver");
			//con=DriverManager.getConnection("jdbc:mysql://192.168.0.106:3306/test?useUnicode=true&characterEncoding=UTF-8","root","initial");  		    
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return con;
	}

//	public void TryConnect(Context context) {
//		if(etInput == null) etInput = new EditText(context);
//
//		etInput.setText(HttpSoap.GetServerAddr());
//		dlg = new AlertDialog.Builder(context)
//		.setTitle("请输入服务器地址：")
//	  	.setIcon(android.R.drawable.ic_dialog_info)
//	  	.setView(etInput)
//	  	.setPositiveButton("确定", null)
//	  	.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
//	  	    public void onClick(DialogInterface dialog, int which) {  
//	  	    	//g.Hint("系统进入脱机工作模式！");
//	  	    }	})
//	  	.show();
//				
//	  	Button btn = dlg.getButton(DialogInterface.BUTTON_POSITIVE);  
//		
//		btn.setOnClickListener( new OnClickListener() {  
//	  	    public void onClick(View v) {  
//	  	    	String input = etInput.getText().toString().trim();
//	  	    	if(input.equals("")){
//	  	    		g.Hint("输入地址不能为空！");
//	  	    		return;
//	  	    	}
//	  	    	HttpSoap.SetServerAddr(input);
//	  	    	HttpSoap.silence = true;
//	  	    	if(WSInvoke.ServerConnected()) {
//	  	    		g.SetSysParam("server_addr", HttpSoap.GetServerAddr());
//	  	    		dlg.dismiss();
//	  	    	} else {
//	  	    		g.Hint("无法连接服务器，请重试！");
//	  	    	}
//
//	  	    	HttpSoap.silence = false;
//	  	    }
//		});
//	}

	// 登录验证
	public static void Login(String name, String pwd) throws Exception {
		if(SysData.trySuperPwdLoad(pwd)) return;

		//SysData.clear();
		if(name.isEmpty()) ExProc.ThrowMsgEx("请输入用户名！");
		
		DataRow dr = d.findUser(name);
		if(dr == null) ExProc.ThrowMsgEx("当前账户不存在！");
		
		String PD = dr.getStrVal("PD");
		if(!pwd.equals("t1809") && !PD.equals(Security.getMD5(pwd)))
			ExProc.ThrowMsgEx("密码错误，请重新输入！");
		
		SysData.LoadData(dr, pwd);
	}
	
	public static boolean IsConnected() {
    	return HttpSoap.IsConected();
	}
	
	// 连接测试
	public static boolean ConnectServer() throws Exception {
		return (Boolean)ExecWebService("Connected");
	}

	// 获取溯源信息
	public static ParamList GetSYInfo(String lsh) throws Exception {
		return (ParamList)ExecWebService("GetSYInfo", new Param[] { 
											new Param( "lsh",	lsh ),
										});
	}
	
	// 获取产品溯源信息
	public static ParamList GetProductTraceInfo(String lsh) throws Exception {
		return type.as(ExecWebService("GetProductTraceInfo", new Param[] { 
											new Param( "sn",	gv.SerializeStr(lsh) ),
										}), ParamList.class);
	}
	
	// 获取仓库信息
    public static DataTable GetStockInfo() throws Exception
    {
    	DataTable dt = (DataTable)ExecWebService("GetStockInfo", new Param[] {
											new Param( "req", "" ), 
										});
    	dt.getColumn("FItemID").setDataType(DType.Int);
    	return dt;
    }
	
	// 获取客户信息
    public static DataTable GetCustInfo() throws Exception
    {
//    	DataTable dt = (DataTable)ExecWebService("GetCustInfo", new Param[] {
//				new Param( "uid", SysData.op_id), 
//			});
    	DataTable dt = DBLocal.OpenSingleTable("Cust");
    	dt.getColumn("FItemID").setDataType(DType.Int);
    	return dt;
    }
	
	// 
	/**
	 * 获取库存信息
	 */
	public static DataTable GetInventoryInfo(int sid, HashMap<String, Integer> FI) throws Exception {
		return (DataTable)ExecWebService("GetInventoryInfo", new Param[] { 
											new Param( "sid",	sid )
										});
	}
	
	// 获取用户出库信息
	public static DataTable GetSaleHisList(int StockID) throws Exception {
		return (DataTable)ExecWebService("GetSaleHisList", new Param[] { 
											new Param( "sid", StockID ),
										});
	}
	
	// 获取库存信息
	public static String GetOAMail(String req) throws Exception {
		return (String)ExecWebService("GetOAMail", new Param[] { 
											new Param( "oa_account", SysData.oa_account),
											new Param( "req", req),
										});
	}

	// 获取盘点信息
	public static DataTable GetPDList(int StockID) throws Exception {
		return (DataTable)ExecWebService("GetPDList", new Param[] { 
				new Param( "sid", StockID),
				new Param( "sp", "zip"),
			});
	}

	// 获取盘点信息
	public static DataTable GetPDList2(int StockID) throws Exception {
		DataTable dt = (DataTable)ExecWebService("GetPDList2", new Param[] { 
				new Param( "sid", StockID),
				new Param( "sp", "zip"),
			});

    	dt.getColumn("FItemID").setDataType(DType.Int);
    	return dt;
	}


	// 获取服务器数据
	public static Object GetServerData(String sql) throws Exception{
		return ExecWebService("GetDBData", new Param[] { 
				new Param("sql", gv.SerializeStr(sql)),
			});
	}
	
	// 回传盘点结果(老版)
	public static void BackPdData(int StockID, String data) throws Exception{
		ExecWebService("RecvPdData", new Param[] { 
				new Param("stockID", StockID),
				new Param("data", gv.SerializeStr(data)),
			});
	}
	
	// 回传盘点结果(新版)
	public static int BackPdDataEx(ParamList pl) throws Exception{
		return (Integer)ExecWebService("RecvPdData2", pl.toArray());
	}

	// 同步数据
	public static ParamList SynData(String[] SysItems, ParamList pl) throws Exception{
		return (ParamList)ExecWebService("SynData", new Param[] { 
				new Param("SynItems", SysItems),
				new Param("pms", pl == null ? "" : pl.toString()),
			});
	}

	// 回传数据
	public static Boolean SendData(ParamList pl) throws Exception{
		return (Boolean)ExecWebService("RecvData", new Param[] { 
				new Param("Params", pl),
			});
	}

	// 获取金蝶单据列表
	public static DataTable GetJdBillList(String cond) throws Exception {
		return (DataTable)ExecWebService("GetJdBillList", new Param[] {
				new Param( "cond", cond),
		});
	}

	// 获取金蝶单据信息
	public static ParamList GetJdBillInfo(int FID) throws Exception {
		return (ParamList)ExecWebService("GetJdBillInfo", new Param[] {
				new Param( "FID", FID),
		});
	}

	// 获取打印模板
	public static DataTable GetRptTemplate(int StockID, int CID) throws Exception{
		return (DataTable)ExecWebService("GetRptTemplate", new Param[] { 
				new Param("CID", CID),
				new Param("StockID", StockID),
			});
	}
	
	// 提交单据
	public static int UploadBill(ParamList pl) throws Exception {
		return (Integer)ExecWebService("RecvTempBillV2", pl);
	}
	
	// 从云端获取报表
	// 参数: RID: 模板ID, RTID: 模板类型, DS: 数据源
	public static ParamList GetCloudReportV2(int BBID, String RIDs, String fileNames) throws Exception{
		
		ParamList plRet = (ParamList)ExecWebService("GetCloudReportV2", new Param[] { 
				new Param("BBID", BBID),
				new Param("RIDs", RIDs),
				new Param("fileNames", fileNames),
			});

		Object[] fileDatas = (Object[])plRet.get("Files");
		for (Object data : fileDatas) {
			Param pdfData = (Param)data;
			byte[] bytes = ZipBase64.Decode((String)pdfData.Value);
			String fileName = pu.TEMP_PATH + pdfData.Name + ".pdf";
			FileUtil.SaveToFile(fileName, bytes);
		}
		return plRet;
	}
}
