package com.hc.dal;

import android.R.integer;

import com.hc.SysData;

public class SCID {
	public final static int SN = 101;		// 流水号
	public final static int Emp = 2101;		// 员工
	public final static int Dept = 2102;	// 部门
	public final static int Manuf = 2201;	// 生产商 
	public final static int SP = 2202;		// 供应商  
	public final static int Cust = 2205;	// 客户   
	public final static int PCls = 2301;	// 产品类别 
	public final static int Stock = 2401;	// 仓库   
	public final static int Product = 2501;	// 产品信息 
	public final static int User = 2601;	// 用户   
	public final static int Prop = 101002;	// 产品属性 
	public final static int SSLX = 101003;	// 手术类型
	

	public final static int LastPage = 1600;
	public final static int LastPage_StockBill = LastPage + 1;
	
	public final static int LastCust = getExtID(Cust, 9);
	
	public final static int getExtID(int baseID, int subID) {
		return baseID * 1000 + subID;
	}
	
	public static int getLastBillInput(int BTID) {
		return 32150000 + BTID*10000 + SysData.StockID;
	}
}
