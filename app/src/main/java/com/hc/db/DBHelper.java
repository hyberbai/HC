package com.hc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import hylib.io.FileUtil;

public class DBHelper extends SQLiteOpenHelper  
{  
	public static final String DB_NAME = "hc.sdb"; // 保存的数据库文件名
	public static final String PACKAGE_NAME = "com.hc"; // 工程包名
	
	public static final String DB_PATH = "/data"
										+ Environment.getDataDirectory().getAbsolutePath()
										+ "/" + PACKAGE_NAME + "/databases";  //在手机里存放数据库的位置
	public static final String DB_PATH1 = "/";  //在手机里存放数据库的位置
	
	public static final String DB_PATH_NAME = DB_PATH + "/" + DB_NAME;
	
	public static final String Cfg_Item = "FItemID/i";
	public static final String Cfg_Stock = "FItemID/i";
	public static final String Cfg_PD = "FItemID/i|YSID/i|BackDate/d|PDDate/d|State/i|OpID/i";
	public static final String Cfg_PdInventory = "@PDIID/i|PDID/i|FItemID/i|Qty/i|State/i";
	public static final String Cfg_PdDetail = "@PDDID/i|PDID/i|IID/i|Qty/i|State/i";
	public static final String Cfg_Cust = "FItemID/i";
	public static final String Cfg_SP = "FItemID/i";
	public static final String Cfg_Manuf = "FItemID/i";
	public static final String Cfg_User = "FUserID/i";
	public static final String Cfg_ItemCls = "IID/i";
	public static final String Cfg_PCodeRules = "FItemID/i";
	public static final String Cfg_Dict = "ID/i|CID|I1/i|I2/i|I3/i";
	
	public static final String Cfg_Bill = "@BID/i|UID/i|YSID/i|State/i";
	public static final String Cfg_BillDetail = "@BDID/i|BID/i|IID/i|Qty/i|Price/f|State/i";

	public static final String Cfg_StockBill = "@FID/i|FClassTypeID/i|FDate/d|FBillTypeID/i|FSupplyID/i|FCustID/i|FBillerID/i|FCheckerID/i|FEmpID/i|FCheckDate/d|FCheckerDate/d|FAmount/f|FAllAmount/f|FCostAmount/f|FSaveDate/d|Flag/i";
	public static final String Cfg_StockBillEntry = "@FEntryID|FID/i|FIndex/i|FItemID/i|FDCStockID/i|FSCStockID/i|FQty/i|FPrice/f|FAmount/f|FTaxRate/i|FTaxPrice/f|FTaxAmount/f|FInTaxAmount/f|FKFDate/d|FKFPeriod/i|FPeriodDate/d|FOrderQty/i|FSettleQty/i|FRejectQty/i|FUnSettleQty/i|FUnRejectQty/i|FRate/i|FID_SRC/i|FEntryID_SRC/i|FClassID_SRC/i|FDate_SRC/d|YB/i|WB/i";

	   
	private static int db_version = 1;
	
   public DBHelper(Context context) {  
       super(context, DB_PATH_NAME, null, db_version);  
	//   FileUtil.DeleteFile(DB_PATH_NAME);
   }

   public static void Rebuild() {  
	   FileUtil.DeleteFile(DB_PATH_NAME);
   }

   private void CreateTable(SQLiteDatabase db, String TableName, String sql){
	   db.execSQL("DROP TABLE IF EXISTS " + TableName);
	   db.execSQL(String.format("CREATE TABLE %s(%s)", TableName, sql));
   }

   private void CreateUniqueIndex(SQLiteDatabase db, String TableName, String fields){
	   db.execSQL("CREATE UNIQUE INDEX " + TableName + "_I ON " + TableName + "(" + fields + ")");  
   }
   
   @Override  
   public void onCreate(SQLiteDatabase db) {
	   // 创建流水号码表
	   CreateTable(db, "SN", "FID INT PRIMARY KEY, CustID INT, FSerialNo VARCHAR(20), FItemID INT, FBatchNo VARCHAR(50), FKFDate datetime, FPeriodDate datetime, FKFPeriod INT, SP VARCHAR(30)");
	
	   // 创建产品信息表	     
	   CreateTable(db, "Item", "FItemID INT PRIMARY KEY, FName VARCHAR(200) COLLATE NOCASE, SName VARCHAR(100) COLLATE NOCASE, FNumber VARCHAR(50), FModel VARCHAR(100) COLLATE NOCASE, FUnit VARCHAR(4), Cls VARCHAR(20), Manuf VARCHAR(100), Reg VARCHAR(100), PRule VARCHAR(100), Ext TEXT");

	   // 创建产品类别表	     
	   CreateTable(db, "ItemCls", "IID INT PRIMARY KEY, INo VARCHAR(10), IName VARCHAR(50) COLLATE NOCASE");

	   // 创建员工表
	   CreateTable(db, "User", "FUserID INT PRIMARY KEY, FName VARCHAR(20) COLLATE NOCASE, FGroup VARCHAR(20), OA VARCHAR(20), RT TEXT, gRT TEXT, PD VARCHAR(32)");

	   // 创建职员表
	   CreateTable(db, "Emp", "FItemID INT PRIMARY KEY, FNumber VARCHAR(20), FName VARCHAR(20) COLLATE NOCASE, FDeptID INT, FEmpGroup INT");

	   // 创建部门表
	   CreateTable(db, "Dept", "FItemID INT PRIMARY KEY, FNumber VARCHAR(20), FName VARCHAR(20) COLLATE NOCASE");
			   
	   // 创建库位信息表	     
	   CreateTable(db, "Stock", "FItemID INT PRIMARY KEY, FName VARCHAR(200), FNumber VARCHAR(50)");
	   
	   // 创建产品替换方案表
	   CreateTable(db, "ProductReps", "ID INTEGER PRIMARY KEY AUTOINCREMENT, Key VARCHAR(20), Reps TEXT, Flag INT");

	   // 创建客户价格方案对应表
	   CreateTable(db, "PriceCust", "PCID INT PRIMARY KEY, PPID INT, CustID INT, Discount INT");

	   // 创建价格方案明细表
	   CreateTable(db, "PricePlanDetail", "PPDID INT PRIMARY KEY, PPID INT, FItemID INT,  FPrice Float, ZJM VARCHAR(30)");

	   // 创建客户信息表	     
	   CreateTable(db, "Cust", "FItemID INT PRIMARY KEY, FName VARCHAR(200), SName VARCHAR(100), FNumber VARCHAR(50), FStockID INT, INC VARCHAR(50)");
	   
	   // 创建库位关联客户表
	   CreateTable(db, "StockCust", "ID INT PRIMARY KEY, StockID INT, KIDs VARCHAR(100)");
	   
	   // 创建供应商信息表	     
	   CreateTable(db, "SP", "FItemID INT PRIMARY KEY, FName VARCHAR(200), FNumber VARCHAR(50)");
	   
	   // 创建生产商信息表	     
	   CreateTable(db, "Manuf", "FItemID INT PRIMARY KEY, FName VARCHAR(200), FNumber VARCHAR(50)");
	   	
	   // 创建条码规则信息表	     
	   CreateTable(db, "PCodeRules", "PRID INT PRIMARY KEY, PRName VARCHAR(50), PRNo VARCHAR(50),  MfID INT, BaseCode VARCHAR(50), CodeNum INT, ExpDays INT, CodeMask VARCHAR(100), CodeRef VARCHAR(100)");
	   
	   // 创建盘点信息主表	     
	   CreateTable(db, "PD", "PDID INT PRIMARY KEY, PDName VARCHAR(100), YSID INT, OpID INT, BackDate datetime, PDDate datetime, Note VARCHAR(100), state INT, Ext TEXT");

	   // 创建盘点库存表	  
	   CreateTable(db, "PdInventory", "PDIID INT PRIMARY KEY, PDID INT, FItemID INT, Qty INT, Note VARCHAR(100), state INT");

	   // 创建盘点信息子表  
	   CreateTable(db, "PdDetail", "PDDID INT PRIMARY KEY, PDID INT, SNo VARCHAR(15) NOT NULL, IID INT, Note VARCHAR(100), state INT, BatchNo VARCHAR(50), ExpDate DateTime, MfgDate DateTime, ExpDays INT");
	   CreateUniqueIndex(db, "PdDetail", "PDID, SNo");

	   // 创建单据信息主表  
	   CreateTable(db, "Bill", "BID INT PRIMARY KEY, BillNo VARCHAR(15), BTID INT, UID INT, YSID INT, Ext Text, BDate DateTime, SaveDate DateTime, state INT");

	   // 创建单据信息子表  
	   CreateTable(db, "BillDetail", "BDID INT PRIMARY KEY, BID INT, SNo VARCHAR(15), IID INT, Qty INT, Price Float, BatchNo VARCHAR(50), FKFDate datetime, FPeriodDate datetime, FKFPeriod INT, Note VARCHAR(100), Ext Text, state INT");
	   
	   // 创建金蝶单据信息主表  
	   CreateTable(db, "StockBill", "FID INT PRIMARY KEY, FClassTypeID INT, FBillNo VARCHAR(15), FDate DateTime, FBillTypeID INT, FSupplyID INT, FCustID INT, FBillerID INT, FCheckerID INT, FEmpID INT, FCheckDate DateTime, FExplanation VARCHAR(30), FCheckerDate DateTime, FPhone VARCHAR(15), FContact VARCHAR(15), FAmount float, FAllAmount float, FCostAmount float, ExtType VARCHAR(8), FUserDefine1 VARCHAR(15), FUserDefine3 VARCHAR(15), FSaveDate DATETIME, Flag INT, Ext Text");

	   // 创建金蝶单据信息子表  
	   CreateTable(db, "StockBillEntry", "FEntryID INT PRIMARY KEY, FID INT, FIndex INT, FItemID INT, FBatchNo VARCHAR(15), FDCStockID INT, FSCStockID INT, FQty INT, FPrice float, FAmount float, FTaxRate INT, FTaxPrice float, FTaxAmount float, FInTaxAmount float, FKFDate DateTime, FKFPeriod INT, FPeriodDate DATETIME, FOrderQty INT, FSettleQty INT, FRejectQty INT, FUnSettleQty INT, FUnRejectQty INT, FRate INT, FID_SRC INT, FEntryID_SRC INT, FBillNo_SRC VARCHAR(15), FClassID_SRC INT, FBillUserText3 VARCHAR(100), SNo VARCHAR(15), BoxNO VARCHAR(15), KD VARCHAR(15), FNote VARCHAR(100), FDate_SRC DATETIME, YB INT, WB INT");
	   
	   // 系统设置表
	   CreateTable(db, "Settings", "ID INTEGER PRIMARY KEY AUTOINCREMENT, UID INT, SCID INT, V TEXT");
	   
	   // 数据字典表
	   CreateTable(db, "Dict", "ID INTEGER PRIMARY KEY AUTOINCREMENT, CID INT, FName VARCHAR(50) COLLATE NOCASE, SName VARCHAR(15) COLLATE NOCASE, No VARCHAR(20), T1 VARCHAR(15), T2 VARCHAR(15), T3 VARCHAR(15), T4 VARCHAR(15), T5 VARCHAR(15), I1 INT, I2 INT, I3 INT, I4 INT, I5 INT, Ext TEXT");
   }  

   @Override  
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	   if(!upgradeVersion(2, oldVersion, newVersion)) return; 
	   if(!upgradeVersion(3, oldVersion, newVersion)) return;
//          onCreate(db);     
   }
   
   public static boolean upgradeVersion(int currrent, int oldVersion, int newVersion) {
	   if(oldVersion >= currrent) return false;
	   if(newVersion < currrent) return true;
	   if(currrent == 2) {
		   
	   }
	   return true;
   }
}  
