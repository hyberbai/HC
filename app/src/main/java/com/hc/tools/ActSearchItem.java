package com.hc.tools;

import hylib.toolkits.ExProc;
import hylib.util.ParamList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hc.db.DBHelper;
import com.hc.db.DBLocal;

public class ActSearchItem extends ActSearch {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
	}
	
	@Override
	public void setTID(int tid){
		try {
			this.TID = tid;
			if(dsMap.indexOfKey(tid) >= 0) {
				DataSource = dsMap.get(tid);
				return;
			}
			
			String tabName = tid == Search.PRODCUT ? "Item" :
							 tid == Search.STOCK ? "Stock" :
							 tid == Search.CUST ? "Cust" :
							 tid == Search.SP ? "SP" :
						     tid == Search.MANUF ? "Manuf" : 
							 tid == Search.USER ? "User" : 
							 tid == Search.CLS ? "ItemCls" : 
							 null;

			String PK = tid == Search.USER ? "FUserID" :
						tid == Search.CLS ? "IID" : 
						"FItemID";

			DispMembers = tid == Search.PRODCUT ? "FName,FModel" : 
						tid == Search.CUST ? "SName" : 
						tid == Search.CLS ? "IName" : 
						"FName";

			String cfgCols = tid == Search.PRODCUT ? DBHelper.Cfg_Item :
							 tid == Search.STOCK ? DBHelper.Cfg_Stock :
							 tid == Search.CUST ? DBHelper.Cfg_Cust :
							 tid == Search.SP ? DBHelper.Cfg_SP :
						     tid == Search.MANUF ? DBHelper.Cfg_Manuf : 
							 tid == Search.USER ? DBHelper.Cfg_User : 
							 tid == Search.CLS ? DBHelper.Cfg_ItemCls : 
							 null;

			
			if(tabName == null) ExProc.ThrowMsgEx("无效检索数据源！" + tid);
			DataSource = DBLocal.OpenTable(cfgCols, "select " + PK + "," + DispMembers + " from " + tabName).getRows();
			
			dsMap.put(tid, DataSource);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
}
