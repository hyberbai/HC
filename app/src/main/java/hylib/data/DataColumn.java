package hylib.data;

import hylib.db.SqlDbType;
import hylib.edit.DType;
import hylib.toolkits.gv;
import android.R.drawable;
import android.provider.SyncStateContract.Constants;
import android.renderscript.Element.DataType;


public class DataColumn {
	public final static int FLAG_NOT_PK = 1;
	public final static int FLAG_NOT_AUTOINC = 2;
	public final static int FLAG_NOT_PK_AUTOINC = FLAG_NOT_PK | FLAG_NOT_AUTOINC;
	
	private boolean readOnly; // 只读
	
	private DataTable table; //dataTable的引用
	
	private String columnName; //列名
	
	private String captionName; //显示名称
	
	private int columnIndex;//列索引
	
	private int dataType;	//列数据类型
	
	private boolean mIsPK;			//是否为主键
	private boolean mIsAutoInc;	//是否自动增量字段

	private SqlDbType mDbType;
	
	public int DataLength;
	public Object DefaultValue;
	
	public DataColumn() {
		columnIndex = -1;
	}
	
	public DataColumn(int dataType) {
	    this("", dataType);
	}
	
	public DataColumn(String columnName) {
	    this(columnName, 0);
	}
	
	public DataColumn(String columnName, int dataType) {
	    this.setDataType(dataType);
	    this.columnName = columnName;
	}
	
	public boolean IsPK() {
	    return mIsPK;
	}
	
	public boolean IsAutoInc() {
	    return mIsAutoInc;
	}
	
	public void setPK(boolean value) {
	    mIsPK = value;
	}
	
	public void setAutoInc(boolean value) {
	    mIsAutoInc = value;
	}
	
	public String getColumnName() {
	    return this.columnName;
	}
	
	public void setColumnName(String columnName) {
	    this.columnName = columnName;
	}
	
	public String getCaptionName() {
	    return captionName;
	}
	
	public void setCaptionName(String captionName) {
	    this.captionName = captionName;
	}
	
	public boolean isReadOnly() {
	    return this.readOnly;
	}
	
	public void setReadOnly(boolean readOnly) {
	    this.readOnly = readOnly;
	}
	
	public DataTable getTable() {
	    return this.table;
	}
	
	public void setTable(DataTable table) {
	    this.table = table;
	}

	public static Object ConvertType(Object value, int dataType) {
		if(dataType == DType.Int) return gv.IntVal(value);
		if(dataType == DType.Date) return gv.StrToDate((String)value);
		if(dataType == DType.Dec) return gv.ValF((String)value);
		return null;
	}

	public void setDataType(int dataType) {
	    this.dataType = dataType;
	    if(table == null || dataType == 0) return;
	    for (DataRow dr : table.getRows()) {
	    	Object value = dr.getValue(columnIndex);
	    	if(value == null) continue;
	    	if(value instanceof String)
	    	{
	    		Object v = ConvertType(value, dataType);
	    		if(v != null) dr.setValue(columnIndex, v);
	    	}
		}
	}
	
	/**  
	 * @return  the dataType   
	*/
	public int getDataType() {
	    return dataType;
	}

	public void setDbType(SqlDbType dbType) {
		if(mDbType == dbType) return;
		mDbType = dbType;
		dataType = TableUtils.getDataTypeValueOf(dbType);
	}
	
	/**  
	 * @return  the dataType   
	*/
	public SqlDbType getDbType() {
	    return mDbType;
	}
	
	/**  
	 * @param columnIndex
	 */
	public void setColumnIndex(int columnIndex) {
	    this.columnIndex = columnIndex;
	}
	
	/**  
	 * @return  the columnIndex   
	*/
	public int getColumnIndex() {
	    return columnIndex;
	}
	
	// public String getDataTypeName() {
	//     return DataTypes.getDataTypeName(dataType);
	// }
	
	/**  
	 * 功能描述：  将输入数据转为当前列的数据类型返回
	 * @param
	 */
	public Object convertTo(Object value) {
	    return value;
	}
	
	@Override
	public String toString(){
	    return this.columnName;
	}

}
