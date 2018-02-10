package com.hc.tools;

import hylib.toolkits.ExProc;
import hylib.util.ParamList;
import android.app.Activity;
import android.content.Intent;

import com.hc.ActBase;

public class Search {

	public final static int PRODCUT = 1; // 产品
	public final static int STOCK = 2;	// 库位
	public final static int CUST = 3;	// 客户
	public final static int SP = 4;		// 供应商
	public final static int MANUF = 5;	// 生产厂家
	public final static int USER = 6;	// 用户
	public final static int CLS = 9;	// 产品类别
	
	public static String getSrchConfig(int searchID){
		return "srch: { id: " + searchID + "}";
	}
	
	public static String nameOf(int id) {
		if(id == PRODCUT) return "产品";
		if(id == STOCK) return "库位";
		if(id == CUST) return "客户";
		if(id == SP) return "供应商";
		if(id == MANUF) return "生产厂家";
		if(id == USER) return "用户";
		if(id == CLS) return "产品类别";
		return null;
	}

	public static String getProductSrchCfg(){
		return getSrchConfig(Search.PRODCUT);
	}

	public static String getProductClsSrchCfg(){
		return getSrchConfig(Search.CLS);
	}

    /*
     * 输入项的相关搜索
     */
    public static void Execute(Activity act, int tid, String text, ParamList pl) {
		try {
			Intent intent = new Intent(act, ActSearchItem.class);
			intent.putExtra("tid", tid);
			intent.putExtra("text", text);
			if(pl != null) pl.writeToInent(intent);
			act.startActivityForResult(intent, ActBase.REQ_CODE_SEARCH); 
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
}
