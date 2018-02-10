package com.hc.mo.bill;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.hc.dal.d;
import com.hc.db.DBLocal;

import android.R.integer;
import android.database.sqlite.SQLiteDatabase;
import hylib.data.DataRow;
import hylib.data.DataRowState;
import hylib.data.DataTable;
import hylib.db.SqlDataAdapter;
import hylib.toolkits.ExProc;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.util.Hson;
import hylib.util.Param;
import hylib.util.ParamList;

public class ProductRelpace {
	public static final int FLAG_EDITED = 1;

	public static Object[] getExtParamReps(Object ext) {
		ParamList plExt = ext instanceof ParamList ? (ParamList)ext : new ParamList((String)ext);
		
    	Object value = plExt.get("reps");
    	if(value instanceof Object[]) return (Object[])value;
    	if(value instanceof String) return type.as(Hson.Parse((String)value), Object[].class);
    	if(value instanceof Collection) return ((Collection<?>)value).toArray();
		return null;
	}
	
	public static void setExtParamReps(ParamList plExt, Object[] reps) throws Exception {
		plExt.SetValidValue("reps", reps);
	}
	
	public static Object[] getReps(ParamList pl) throws Exception {
		return getExtParamReps(pl.get("Ext"));
	}
	
	public static Object[] getReps(DataRow dr) throws Exception {
		return getExtParamReps(dr.getValue("Ext"));
	}
	
	public static void setReps(ParamList pl, Object reps) throws Exception {
		pl.SetPathValue("Ext/reps", reps);
	}
	
	public static boolean hasReps(DataRow dr) throws Exception {
		return !gv.IsEmpty(getReps(dr));
	}

	public static DataTable getProductReplaceTable(Object value) throws Exception {
		Object[] items = type.as(value, Object[].class);
		if(items == null) items = type.as(Hson.Parse((String)value), Object[].class);
		DataTable dt = new DataTable();
		if(items != null)
		for (Object o : items) {
			ParamList pl = type.as(o, ParamList.class);
			pl.remove("Ext");
			pl.addRowToDataTable(dt);
		}
		return dt;
	}
    
	public static Map<String, Object> mapProductReps;
	
	/*
	 * 加载替换产品表
	 */
	public static void LoadProductReps() {
		if(mapProductReps != null) return;
		try {
			mapProductReps = new HashMap<String, Object>();
			DataTable dt = DBLocal.OpenSingleTable("ProductReps");
			for (DataRow dr : dt.getRows()) {
				mapProductReps.put(dr.getStrVal("key"), dr.getValue("reps"));
			}
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public static Object getHisReps(int CustID, int FItemID){
		if(mapProductReps == null) LoadProductReps();
		String key = CustID + ":" + FItemID;
		return mapProductReps.get(key);
	}
	
	public static void updateRepsToMap(int CustID, int FItemID, Object reps) {
		if(mapProductReps == null) LoadProductReps();
		if(FItemID < 1 || CustID < 1) return;
		String key = CustID + ":" + FItemID;

		try {
			DataTable dt = DBLocal.OpenTable("@+ID/i", "select * from ProductReps where key=?", key);
			dt.setTableName("ProductReps");
			DataRow dr = dt.getRowCount() == 0 ? null : dt.getRow(0);
			if(dr == null) {
				dr = dt.newRow();
				dt.addRow(dr);
				dr.setValue("Key", key);
			}
			
			if(gv.IsEmpty(reps)) {
				dr.Delete();
				mapProductReps.remove(key);
			} else {
				dr.updateValue("Reps", reps);
				dr.updateValue("Flag", FLAG_EDITED);
				mapProductReps.put(key, reps);
			}

			SqlDataAdapter apt = d.getSqlAdapter();
			apt.Update(dt);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
	/*
	 * 如果当前客户的产品曾有替换过，则在扩展参数里附加替换产品
	 */
	public static void AppendExtReps(int CustID, int FItemID, ParamList pl) throws Exception {
		if(getReps(pl) != null) return;
		Object reps = getHisReps(CustID, FItemID);
		if(reps == null) return;
		setReps(pl, reps);
		CalcRepsPrice(pl);
	}

	private static void CalcRepsPrice(ParamList pl) {
		try {
			Object[] reps = getReps(pl);
			if(reps == null) return;
			
			float price = 0;
			for (Object rep : reps) {
				ParamList plRep = type.as(rep, ParamList.class);
				price += plRep.FVal("Price");
			}
			float orginPrice = pl.FVal("orginPrice");
			if(reps.length > 0) {
				if(orginPrice == 0) orginPrice = pl.FVal("Price");
				pl.SetValue("Price", price);
			}
			else
				pl.SetValue("Price", orginPrice);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
}
