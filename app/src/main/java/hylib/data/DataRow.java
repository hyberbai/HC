package hylib.data;

import hylib.edit.DType;
import hylib.toolkits.ExProc;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.util.Param;
import hylib.util.ParamList;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.RowSet;

import junit.framework.Assert;

import android.R.bool;
import android.R.integer;
import android.R.plurals;
import android.renderscript.Element.DataType;

public class DataRow {
	// 定义该行记录在table所处的行数
	private int rowIndex = -1;
	// private DataColumnCollection columns;
	// table的一个引用
	private DataTable table;
	private DataRowState mState;
	public Object Tag;
	public Object[] ItemArray;

	// 用于存储数据的Map对象，这里保存的对象不包括顺序信息，数据获取的索引通过行信息标识
	// private Map<String, Object> itemMap = new LinkedHashMap<String,
	// Object>();

	public DataRow() {
		mState = DataRowState.Unchanged;
	}
	
	public DataRow(DataTable table) {
		this();
		this.table = table;
		ItemArray = new Object[table.getColumns().size()];
	}
	
	public DataRow(DataTable table, Boolean Added) {
		this(table);
		if(Added) setState(DataRowState.Added);
	}

	public DataRowState getState() {
		return mState;
	}

	public void setState(DataRowState state) {
		mState = state;
	}

	public int getDataType(int colIndex) {
		if(table == null) return -1;
		return table.getColumnDataType(colIndex);
	}
	
	public void Delete() {
		if(mState == DataRowState.Added)
			Remove();
		else
			setState(DataRowState.Deleted);
	}
	
	public void Remove() {
		if(table != null) table.getRows().Remove(this);
	}
	
	public void AcceptChanges() {
		if(mState == DataRowState.Unchanged) return;
		if(mState == DataRowState.Deleted) 
			Remove();
		else 
			setState(DataRowState.Unchanged);
	}
	/**
	 * 功能描述： 获取当前行的行索引
	 * 
	 * @param
	 * @return: int
	 */
	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * 功能描述： 获取当前行所属数据表对象
	 * 
	 * @param
	 * @return: DataTable
	 */
	public DataTable getTable() {
		return this.table;
	}

	private boolean mLockState;
	public void LockState() {
		mLockState = true;
	}
	
	public void UnlockState() {
		mLockState = false;
	}

	public boolean IsStateLocked() {
		return mLockState || table != null && table.IsStateLocked();
	}
	
	public boolean setValue(int index, Object value) {
		if(index < 0) return false;
		if(index >= ItemArray.length)
		{
			int colCount = table.ColumnCount();
			if(index >= colCount) return false;
			ItemArray = gv.ResizeArray(ItemArray, colCount);
		}
		if(value != null && value.equals(ItemArray[index]) ||
			value == null && ItemArray[index] == null) return false;
		ItemArray[index] = value;
		return true;
	}

	public boolean setValue(DataColumn column, Object value) {
		if (column == null) return false;
		//value = DType.ConvertType(value, column.getDataType());
		value = DType.ConvertType(value, column.getDataType());
		return setValue(column.getColumnIndex(), value);
	}

	public boolean setValue(String columnName, Object value) {
		DataColumn dc = table.getColumns().get(columnName);
		return setValue(dc, value);
	}
	
	public boolean weakSetValue(String columnName, Object value) {
    	if(!gv.IsEmpty(getValue(columnName))) return false;
		return setValue(columnName, value);
	}
	
	public void updateValue(int index, Object value) {
		if(index < 0 || index >= table.ColumnCount()) return;
		DataColumn dc = table.getColumns().get(index);
		updateValue(dc, value);
	}
	
	public void updateValue(DataColumn dc, Object value) {
		if(!setValue(dc, value)) return;
		// !IsStateLocked() && getDataType(index) >= 0 &&
		if(mState == DataRowState.Unchanged) 
			mState = DataRowState.Modified;
	}
	
	private int getColIndex(String columnName){
		DataColumn dc = table.getColumns().get(columnName);
		if(dc == null) return -1;
		return dc.getColumnIndex();
	}
	
	public void updateValue(String columnName, Object value) {
		updateValue(getColIndex(columnName), value);
	}

	public void setValues(Object[] values) {
		int count = table.ColumnCount();
		if(count < values.length) count = values.length;
		for(int i = 0; i < count; i++)
			setValue(i, values[i]);
	}

	public Object getValue(int index) {
		if(index >= ItemArray.length) return null;
		return ItemArray[index];
	}

	public Object getValue(DataColumn dc) {
		return getValue(dc.getColumnIndex());
	}

	public int getIntVal(String columnName) {
		Object o = getValue(columnName);
		return o == null ? 0 : gv.IntVal(o);
	}

	public boolean getBool(String columnName) {
		Object o = getValue(columnName);
		return gv.BoolVal(o);
	}

	public int getIntVal(int columnIndex) {
		Object o = getValue(columnIndex);
		return o == null ? 0 : gv.IntVal(o);
	}
	
	public String getMoneyVal(String columnName) {
		Object o = getValue(columnName);
		return o == null ? "" : gv.FmtMoney(gv.StrVal(o));
	}

	public String getStrVal(int columnIndex) {
		Object o = getValue(columnIndex);
		return o == null ? "" : gv.StrVal(o);
	}

	public String getStrVal(String columnName) {
		Object o = getValue(columnName);
		return o == null ? "" : gv.StrVal(o);
	}

	public float getFVal(String columnName) {
		Object o = getValue(columnName);
		return gv.FVal(o);
	}

	public float getFVal(int colIndex) {
		Object o = getValue(colIndex);
		return gv.FVal(o);
	}

	public double getDblVal(String columnName) {
		Object o = getValue(columnName);
		return gv.DoubleVal(o);
	}

	public Date getDate(String columnName) {
		Object o = getValue(columnName);
		return gv.DateVal(o);
	}
	
	public Object getValue(String columnName) {
		DataColumn dc = table.getColumns().get(columnName);
		if (dc == null) return null;
		return getValue(dc.getColumnIndex());
	}
	
	public <T> T getValue(Class<T> t, String columnName) {
		Object v = getValue(columnName);
		if (v == null) return null;
		if(!v.getClass().equals(t)) return null;
		return (T)v;
	}

	public <T> T $(String columnName) {
		return (T)getValue(columnName);
	}

    public Object[] getValues(String names, char splitChr) {
    	String[] ss = gs.Split(names, String.valueOf(splitChr));
    	Object[] values = new Object[ss.length];
    	for (int i = 0; i < ss.length; i++)
    		values[i] = getValue(ss[i]);
    	return values;
	}
    
    public String DispText(String title, String names)
    {
    	char splitChr = gs.getSplitChar(names);
    	Object value = gs.JoinArray(getValues(names, splitChr), splitChr);
        if (gv.IsEmpty(value)) return null;
        return title + gv.StrVal(value);
    }
    
    public boolean MatchText(String text) {
    	for (Object v : ItemArray)
    		if(gs.Match(v, text)) return true;
        return false;
    }
    
    @Override
    public String toString() {
		return toParamList().toString();
	}

    public Param[] toParams() {
		return toParamList().toArray();
	}
    
    public ParamList toParamList() {
		ParamList pl = new ParamList();
		putParamList(pl);
		return pl;
	}

    public void putParamList(ParamList pl) {
		DataColumnCollection cols = table.getColumns();
		for (DataColumn dc : cols)
			pl.SetValue(dc.getColumnName(), getValue(dc));
	}
    
//	public <T> T getV(int index) {
//		if(index >= ItemArray.length) return null;
//		Object o = ItemArray[index];
//
//		if(o == null) return new T();
//		return (T)o;
//	}
//	public <T> T get(String columnName) {
//		Object v = getValue(columnName);
//		if(v.getClass() == Integer.class) v 
//		return (T)v;
//	}
	/**
	 * @param rowIndex
	 */
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public void copyFrom(DataRow row) {
		ItemArray = row.ItemArray.clone();
	}

	public Object setSum(String colName, Object value) {
		if(value == null) return getValue(colName);
		
		if(value instanceof Integer) value = (Integer)value + getIntVal(colName); else 
		if(value instanceof Float) value = (Float)value + getFVal(colName); else
		if(value instanceof Double) value = (Double)value + getDblVal(colName);
		else
			ExProc.ThrowMsgEx("无效计算值：" + colName + " v:" + value);;
		setValue(colName, value);
		return value;
	}

	public Object setAvg(String colName, int count) {
		Object value = getValue(colName);
		if(count == 0) return value;
		if(value instanceof Integer) value = (Integer)value/count; else 
		if(value instanceof Float) value = (Float)value/count; else
		if(value instanceof Double) value = (Double)value/count;
		setValue(colName, value);
		return value;
	}

	public Object setMin(String colName, Object value) {
		value = gv.Min(getValue(colName), value);
		setValue(colName, value);
		return value;
	}
	
	public Object setMax(String colName, Object value) {
		value = gv.Max(getValue(colName), value);
		setValue(colName, value);
		return value;
	}
}
