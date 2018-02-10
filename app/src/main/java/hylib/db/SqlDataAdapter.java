package hylib.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import hylib.data.DataColumn;
import hylib.data.DataColumnCollection;
import hylib.data.DataRow;
import hylib.data.DataRowState;
import hylib.data.DataTable;
import hylib.toolkits.ExProc;
import hylib.toolkits.gi;
import hylib.toolkits.gv;
import hylib.toolkits.gi.IFunc;
import hylib.toolkits.gs;

public class SqlDataAdapter {
	public DataTable Table;
	public String PK;
	public SQLiteDatabase db;
	public SQLiteStatement InsertStatement;
	public SQLiteStatement UpdateStatement;
	public SQLiteStatement DeleteStatement;
	
	private DataColumnCollection InsertCols;
	private DataColumnCollection UpdateCols; 

	public SqlDataAdapter(SQLiteDatabase db){
		this.db = db;
	}
	
	public SqlDataAdapter(DataTable dt, SQLiteDatabase db){
		this(db);
		setDataTable(dt);
	}
	
	public void setDataTable(DataTable dt){
		Table = dt;
		

		Cursor cursor = db.rawQuery("select * from " + dt.getTableName() + " where 1=0", new String[]{});
		
		DataColumnCollection cols = new DataColumnCollection();
		for (String colName : cursor.getColumnNames()) {
			DataColumn dc = dt.getColumns().get(colName);
			if(dc != null) cols.add(dc);
		} 

		DataColumn dcPK = cols.getPKColumn();
		if(dcPK == null) ExProc.ThrowMsgEx("表" + dt.getTableName() + " 未设置主键！");
		
		PK = dcPK.getColumnName();
		
		gi.IFunc1<DataColumn, String> selector = new gi.IFunc1<DataColumn, String>() { 
			@Override public String Call(DataColumn dc) { return dc.getColumnName(); } 
		};
		
		InsertCols = cols.getSelectCols(DataColumn.FLAG_NOT_AUTOINC);
		InsertStatement = db.compileStatement(
				String.format("INSERT INTO %s (%s) VALUES (%s)",
					dt.getTableName(),
					gs.JoinList(InsertCols, ",", selector), 
					gs.nStr("?", InsertCols.size(), ",")
				));
		
		selector = new gi.IFunc1<DataColumn, String>() { 
			@Override public String Call(DataColumn dc) {
				return dc.getColumnName() + "=?"; 
			} 
		};

		UpdateCols = cols.getSelectCols(DataColumn.FLAG_NOT_PK_AUTOINC);
		UpdateStatement = db.compileStatement(
				String.format("UPDATE %s SET %s WHERE %s=?",
					dt.getTableName(),
					gs.JoinList(UpdateCols, ",", selector), 
					PK
				));

		DeleteStatement = db.compileStatement(
				String.format("DELETE FROM %s WHERE %s=?",
					dt.getTableName(),
					PK
				));
	}
	
	public void UpdateRow(DataRow dr) {
		if(dr.getState() == DataRowState.Unchanged) return;
		SQLiteStatement st; 
		int index = 1;
		if(dr.getState() == DataRowState.Added)
		{
			st = InsertStatement;  

			for (DataColumn dc : InsertCols) {
				st.bindString(index++, gv.StrVal(dr.getValue(dc)));
			}
			st.execute();
		}
		if(dr.getState() == DataRowState.Modified)
		{
			st = UpdateStatement; 
			for (DataColumn dc : UpdateCols) {
				st.bindString(index++, gv.StrVal(dr.getValue(dc)));
			} 
			st.bindLong(index, dr.getIntVal(PK));
			st.execute();
		}
		if(dr.getState() == DataRowState.Deleted)
		{
			st = DeleteStatement;  
			st.bindLong(index, dr.getIntVal(PK));
			st.execute();
		}
		dr.AcceptChanges();
	}

	public void Update() {
		for (DataRow dr : Table.getChanges())
			UpdateRow(dr);
	}
	
	public void Update(DataTable dt) {
		setDataTable(dt);
		Update();
	}
}
