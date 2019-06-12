package com.hc.db;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.SparseIntArray;

import com.hc.MyApp;

import hylib.data.DataColumn;
import hylib.data.DataColumnCollection;
import hylib.data.DataRow;
import hylib.data.DataTable;
import hylib.data.TableUtils;
import hylib.edit.DType;
import hylib.toolkits.ExProc;
import hylib.toolkits.gi;
import hylib.toolkits.gs;
import hylib.toolkits.gv;

public class DBLocal {
    public static DBHelper dbHelper;  
    
    public static void Init() {
		if(dbHelper != null) return;
    	dbHelper = new DBHelper(MyApp.CurrentActivity());
	}
    
    public static void Rebuild() {
    	if(dbHelper != null) dbHelper.close();
		DBHelper.Rebuild();
		Init();
	}

//	public static boolean DBExists() {
//		return gu.FileExists(DB_PATH_NAME);
//	}
//	
//	public static boolean CopyDB() {
//		try {
//			// 执行数据库导入
//			InputStream is = MyApp.CurrentActivity().getResources().getAssets().open(DB_NAME); // 欲导入的数据库
//			FileOutputStream fos = new FileOutputStream(DB_PATH_NAME);
//			byte[] buffer = new byte[BUFFER_SIZE];
//			int count = 0;
//
//			while ((count = is.read(buffer)) > 0)
//				fos.write(buffer, 0, count);
//
//			fos.close();// 关闭输出流
//			is.close();	// 关闭输入流
//
//			return true;
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return false;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	public static boolean CheckDB() {
//		if(DBExists()) return true; else return CopyDB(); 
//	}


//  @Override
//  private int bulkInsert(Uri uri, ContentValues[] values) {
//      SQLiteDatabase db = getWritableDatabase();
//      
//      db.beginTransaction();
//      try {
//          int count = values.length;
//          for (int i = 0; i < count; i++) {
//              if (db.insert(tableName, null, values[i]) < 0) {
//                  return 0;
//              }
//          }
//          db.setTransactionSuccessful();
//      } finally {
//          db.endTransaction();
//      }
//      return values.length;
//  }
    

	public static SQLiteDatabase getDB() {
		return dbHelper.getWritableDatabase();
	}

	public static void ExecTrans(gi.CallBack Exec) throws Exception {
		SQLiteDatabase db = getDB();
		db.beginTransaction();
		try {
			Exec.Call();
			db.setTransactionSuccessful();  
		} catch (Exception e) {
			throw e;
		}
		finally{
			db.endTransaction();
		}
	}

	// 向本机数据库导入数据
	public static boolean ImportData(DataTable dt) throws Exception {
		boolean bOK = true;
	
		if(dt == null) return false;
		
		gi.IFunc1<DataColumn, String> selector = new gi.IFunc1<DataColumn, String>() { 
			@Override public String Call(DataColumn dc) { return dc.getColumnName(); } 
		};
		
		String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
								dt.getTableName(),
								gs.JoinList(dt.getColumns(), ",", selector), 
								gs.nStr("?", dt.getColumns().size(), ",")
							);

		SQLiteDatabase db = getDB();
		
		db.beginTransaction();
		try {
			SQLiteStatement st = db.compileStatement(sql);  
			// 导入新数据
			for (DataRow dr : dt.getRows()) {
				for (DataColumn dc : dt.getColumns()) {
					int index = dc.getColumnIndex();
					st.bindString(index + 1, gv.StrVal(dr.getValue(index)));
				}
				st.execute();
				st.clearBindings();
			}
			db.setTransactionSuccessful();  
		} catch (Exception e) {
			throw e;
		}
		finally{
			db.endTransaction();
		}
		return bOK;
	}

	public static void ExecSQL(String sql, Object... args) {
		if(args == null) args = new Object[] {};
		SQLiteDatabase db = getDB();
		db.execSQL(sql, args);
	}

	public static Cursor Query(String sql, String... args) {
		SQLiteDatabase db = dbHelper.getReadableDatabase(); 
		Cursor cursor = db.rawQuery(sql, args);
		return cursor;
	}

	public static int getMaxID(String tableName, String idFieldName) {
		return ExecuteIntScalar("select Max(" + idFieldName + ") from " + tableName);
	}

	public static int getMinID(String tableName, String idFieldName) {
		return ExecuteIntScalar("select Min(" + idFieldName + ") from " + tableName);
	}

	public static int getMaxID(DataTable dt) {
		int max = getMaxID(dt.getTableName(), dt.getPKColumn().getColumnName());
		int maxDt = dt.getMaxID();
		return max > maxDt ? max : maxDt;
	}

	public static int getMinID(DataTable dt) {
		return getMinID(dt.getTableName(), dt.getPKColumn().getColumnName());
	}

	public static String ExecuteScalar(String sql, Object... args) {
		Cursor cursor = Query(sql, gv.toStrArray(args));
		if(!cursor.moveToFirst()) return null;
		return cursor.getString(0);
	}

	public static int ExecuteIntScalar(String sql, Object... args) {
		Cursor cursor = Query(sql, gv.toStrArray(args));
		if(!cursor.moveToFirst()) return -1;
		return cursor.getInt(0);
	}

	public static Double ExecuteDoubleScalar(String sql, Object... args) {
		Cursor cursor = Query(sql, gv.toStrArray(args));
		if(!cursor.moveToFirst()) return null;
		return cursor.getDouble(0);
	}

	public static Float ExecuteFloatScalar(String sql, Object... args) {
		Cursor cursor = Query(sql, gv.toStrArray(args));
		if(!cursor.moveToFirst()) return null;
		return cursor.getFloat(0);
	}
	
	public static String ExecuteStrScalar(String sql, Object... args) {
		return gv.StrVal(ExecuteScalar(sql, args));
	}
	
	public static Object getCursorValue(Cursor cur, int curIndex, DataColumn dc) {
		int type = dc.getDataType(); //cur.getType(colIndex);
			
//		if(type == Cursor.FIELD_TYPE_INTEGER) return cur.getInt(colIndex);
//		if(type == Cursor.FIELD_TYPE_FLOAT) return cur.getFloat(colIndex);
//		if(type == Cursor.FIELD_TYPE_BLOB) return cur.getBlob(colIndex);
		if(type == DType.Int) return cur.getInt(curIndex);
		if(type == DType.Dec) return cur.getFloat(curIndex);
		//if(type == DType.DateTime) return cur.getString(curIndex);
		return cur.getString(curIndex);
	}

	public static DataTable createTable(String tableName) {
		String text = DBLocal.ExecuteScalar("select sql from sqlite_master where name = ?", tableName);
		if(text == null) ExProc.ThrowMsgEx("无效表名：" + tableName);
		return TableUtils.ParseSqliteCreateTableText(text);
	}

	public static DataTable OpenTable(DataColumnCollection cfgCols, String sql, Object... args) {
		Cursor cur = Query(sql, gv.toStrArray(args));

		DataTable dt = new DataTable("");
		SparseIntArray map = new SparseIntArray();
		
		int curIndex = -1, colIndex = 0;
		for (String colName : cur.getColumnNames())
		{
			curIndex++;
			DataColumn dc = dt.addColumn(colName, 0);
			if(dc == null) continue;
			DataColumn dcCfg = cfgCols.get(colName);
			if(dcCfg != null)
			{
				dc.setDataType(dcCfg.getDataType());
				dc.setPK(dcCfg.IsPK());
				dc.setAutoInc(dcCfg.IsAutoInc());
			}
			map.put(colIndex++, curIndex);
		}
		
		int col_count = dt.ColumnCount();
		while(cur.moveToNext()) 
		{
			Object[] items = new Object[col_count];
			for(int i = 0; i < col_count; i++) {
				items[i] = getCursorValue(cur, map.get(i), dt.getColumn(i)); 
				//items[i] = cur.getString(i);
			}
			dt.addRow(items);
		}	
		
		return dt;
	}
	
	public static DataTable OpenTable(String tabConfig, String sql, Object... args) {
		DataColumnCollection cfgCols = new DataColumnCollection(tabConfig);
		return OpenTable(cfgCols, sql, args);
	}

	public static DataTable OpenSingleTable(String tableName, String sql, Object... args) {
		DataTable dt = createTable(tableName);
		dt = OpenTable(dt.getColumns(), sql, args);
		dt.setTableName(tableName);
		return dt;
	}

	public static DataTable OpenSingleTable(String tableName) {
		return OpenSingleTable(tableName, "select * from " + tableName);
	}
	
	public static DataRow OpenDataRow(String tabConfig, String sql, Object... args) {
		DataTable dt = OpenTable(tabConfig, sql, args);
		return dt.isEmpty() ? null : dt.getRow(0);
	}
}
