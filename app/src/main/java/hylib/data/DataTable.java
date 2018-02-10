package hylib.data;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import hylib.toolkits._D;
import hylib.toolkits.gi;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.util.OMap;
import hylib.util.Param;
import hylib.util.SimpleLex;


/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public final class DataTable {

	private DataRowCollection rows; // 用于保存DataRow的集合对象
	private DataColumnCollection columns; // 用于保存DataColumn的对象
	private String tableName; // 表名
	private boolean readOnly = false;
	private int nextRowIndex = 0;
	private DataExpression dataExpression;
	private Object tag;

	public DataTable() {
		this.columns = new DataColumnCollection();
		this.rows = new DataRowCollection(this);
		dataExpression = new DataExpression(this);
	}

	public DataColumn getPKColumn() {
		return columns.getPKColumn();
	}
	
	public DataTable(String dataTableName) {
		this();
		this.tableName = dataTableName;
	}
	
	public DataTable(String dataTableName, String config) {
		this(dataTableName);
		this.columns.CreateTableColumns(config);
	}
	
	public DataTable(String dataTableName, String config, DataRowCollection rows) {
		this(dataTableName, config);
		this.rows = rows;
	}
	
	public DataTable(String dataTableName, String config, Object[][] rowValues) {
		this(dataTableName, config);
		this.rows.SetRowValues(rowValues);
	}
	
	public DataTable(String dataTableName, String config, String[] rowTexts) {
		this(dataTableName, config);
		for (String s : rowTexts) {
			String[] rowValues = gs.Split(s);
			addRow((Object[]) rowValues);
		}
	}

	public DataTable Clone(){
		DataTable tab = new DataTable();
		for (DataColumn dc : columns)
			tab.addColumn(dc);
		return tab;
	}
	
	public DataTable Copy(String sFieldNames) throws Exception {
		String[] ss = sFieldNames.split(",");
		DataTable tab = new DataTable();
		
		int index = 0;
		int[] cis = new int[ColumnCount()];
		for (String s : ss)
		{
			DataColumn dc = getColumn(s.trim());
			if(dc != null) {
				tab.addColumn(dc);
				cis[index++] = dc.getColumnIndex();
			}
		}
		
		for(DataRow dr: rows) {
			Object[] items = new Object[tab.ColumnCount()];
			for (int i = 0; i < tab.ColumnCount(); i++) {
				items[i] = dr.getValue(cis[i]);
			}
			tab.addRow(items);
		}
		return tab;
	}
	
	public DataTable Copy() {
		DataTable tab = this.Clone();
		for(DataRow dr: rows)
			tab.addRow(dr.ItemArray);
		return tab;
	}
	
	public int getTotalCount() {
		return rows.size();
	}

	public boolean isReadOnly() {
		return this.readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	private boolean mLockState;
	public void LockState() {
		mLockState = true;
	}
	
	public void UnlockState() {
		mLockState = false;
	}
	
	public boolean IsStateLocked() {
		return mLockState;
	}
	
	/**
	 * 功能描述： 返回表名
	 */
	public String getTableName() {
		return this.tableName;
	}

	/**
	 * 功能描述： 设置表名
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 功能描述： 返回该表引用的封装类
	 */
	public DataRowCollection getRows() {
		return this.rows;
	}

	public DataRow[] getChanges() {
		return Select(DataRowState.Changed).toArray(new DataRow[0]);
	}
	
	public DataRow getRow(int rowIndex) {
		return rows.get(rowIndex);
	}
	
	public DataRow firstRow() {
		return rows.size() > 0 ? rows.get(0) : null;
	}
	
	public DataRow lastRow() {
		return rows.size() > 0 ? rows.get(rows.size() - 1) : null;
	}
	
	public int getRowCount() {
		return this.rows.size();
	}
	
	public boolean isEmpty() {
		return rows.size() == 0;
	}

	public DataRow Row(int rowIndex) {
		return rows.get(rowIndex);
	}
	
	public String[] getTextRows(String fieldName) {
		return rows.getTextRows(fieldName);
	}

	public int RowCount() {
		return rows.size();
	}

	public DataColumnCollection getColumns() {
		return this.columns;
	}

	public DataColumn getColumn(String colName) {
		return columns.get(colName);
	}

	public DataColumn getColumn(int colIndex) {
		return columns.get(colIndex);
	}

	public void setPKColumn(String colName) {
		getColumn(colName).setPK(true);
	}	
	
	public boolean containsCol(String columnName) {
		return columns.containsCol(columnName);
	}
	
	public DataColumnCollection getNoPkColumns() {
		return columns.getNoPkColumns();
	}

	public int getColumnDataType(int colIndex) {
		return columns.getDataType(colIndex);
	}

	public int getColumnDataType(String colName) {
		return columns.getDataType(colName);
	}

	public int ColumnCount() {
		return columns.size();
	}

	/**
	 * 功能描述： 获取指定行指定列的数据
	 */
	public Object getValue(int row, String colName) {
		return this.rows.get(row).getValue(colName);
	}

	public Object getValue(int row, int col) {
		return this.rows.get(row).getValue(col);
	}

	public int getMaxID() {
		int max = 0;
		int pkColIndex = getPKColumn().getColumnIndex();
		for (DataRow dr : rows) {
			int id = dr.getIntVal(pkColIndex);
			if(max < id) max = id;
		}
		return max;
	}
	
	public int getMinID() {
		int min = Integer.MAX_VALUE;
		int pkColIndex = getPKColumn().getColumnIndex();
		for (DataRow dr : rows) {
			int id = dr.getIntVal(pkColIndex);
			if(min > id) min = id;
		}
		return min == Integer.MAX_VALUE ? 0 : min;
	}
	
	public DataRow createRow() {
		DataRow dr = new DataRow(this);
		nextRowIndex = nextRowIndex < rows.size() ? rows.size() : nextRowIndex;
		dr.setRowIndex(nextRowIndex++);
		return dr;
	}
	
	/**
	 * 功能描述： 为该表数据新建一行
	 */
	public DataRow newRow() {
		DataRow dr = createRow();
		dr.setState(DataRowState.Added);
		return dr;
	}
	
	public DataRow addRow() {
		DataRow dr = createRow();
		addRow(dr);
		return dr;
	}
	
	public DataRow addRow(Object... values) {
		DataRow dr = createRow();
		dr.setValues(values);
		addRow(dr);
		return dr;
	}
	
	public DataRow addNewRow(Object... values) {
		DataRow dr = addRow(values);
		dr.setState(DataRowState.Added);
		return dr;
	}

	public void addRow(DataRow row) {
		if (row.getRowIndex() > this.rows.size())
			row.setRowIndex(this.rows.size());
		this.rows.add(row);
	}

	public void addRow(int index, DataRow row) {
		if (row.getRowIndex() > this.rows.size())
			row.setRowIndex(this.rows.size());
		this.rows.add(index, row);
	}

	public void addRow(int index, Object[] item) {
		DataRow dr = newRow();
		dr.ItemArray = item;
		this.rows.add(index, dr);
	}

	public boolean addNewRow(DataRow row) {
		row.setState(DataRowState.Added);;
		return this.rows.add(row);
	}

	public void DeleteAll() {
		rows.DeleteAll();
	}
	
	public void setValue(int row, int col, Object value) {
		this.rows.get(row).setValue(col, value);
	}

	public void setValue(int row, String colName, Object value) {
		this.rows.get(row).setValue(colName, value);
	}

	public void setValues(String colName, Object value) {
		int col = columns.getColumnsIndex(colName);
		for (DataRow dr : rows) dr.setValue(col, value);
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	public Object getTag() {
		return tag;
	}

	public DataColumn addColumn(String columnName, int dataType) {
		DataColumn dc = this.columns.addColumn(columnName, dataType);
		if(dc != null) dc.setTable(this);
		return dc;
	}

	public void addColumn(DataColumn dc) {
		this.columns.add(dc);
		dc.setTable(this);
	}

	/**
	 * 功能描述： 返回符合过滤条件的数据行集合，并返回
	 */
	public DataRow[] Select(String filterString) {
		List<DataRow> rows = new ArrayList<DataRow>();
		if (!gv.IsEmpty(filterString)) {
			for (Object row : this.rows) {
				DataRow currentRow = (DataRow) row;
				if ((Boolean) dataExpression.Compute(filterString, currentRow.ItemArray)) {
					rows.add(currentRow);
				}
			}
		} else 
			rows = this.rows;
		return rows.toArray(new DataRow[0]);
	}

    public DataRow FindRow(String columnName, Object value) {
    	int col = columns.get(columnName).getColumnIndex();
    	for (DataRow dr : rows)
			if(gv.Same(dr.getValue(col), value)) return dr;
        return null;
    }
    
    public DataRowCollection SelectRows(String columnName, Object value) {
    	DataRowCollection rs = new DataRowCollection(this);
    	int col = columns.get(columnName).getColumnIndex();
    	for (DataRow dr : rows)
			if(gv.Same(dr.getValue(col), value))
				rs.add(dr);
        return rs;
    }
    
    public DataRowCollection SelectRows(String columnName, Param... conds) {
    	DataRowCollection rs = new DataRowCollection(this);
    	for (DataRow dr : rows) {
    		boolean match = true;
    		for (Param cond : conds) {
    			if(!gv.Same(dr.getValue(cond.Name), cond.Value)) {
    				match = false;
    				break;
    			}
			}
			if(match) rs.add(dr);
    	}
        return rs;
    }

    public DataRowCollection SelectRows(gi.IFunc1<DataRow, Boolean> matchFunc) {
    	DataRowCollection rs = new DataRowCollection(this);
    	for (DataRow dr : rows)
			if(matchFunc.Call(dr)) rs.add(dr);
        return rs;
    }
    
    public void Sort(String sortItems) {
    	rows.Sort(sortItems);
	}
    
	/**
	 * 功能描述： 对当前表进行查询 过滤，并返回指定列集合拼装的DataTable对象
	 */
	public DataTable Select(String filterString, String[] columns, boolean distinct)
			throws Exception {
		DataTable result = new DataTable();
		DataRow[] rows = Select(filterString);
		// 构造表结构
		for (String c : columns) {
			DataColumn dc = this.columns.get(c);
			DataColumn newDc = new DataColumn(dc.getColumnName(), dc.getDataType());
			newDc.setCaptionName(dc.getCaptionName());
			result.columns.add(newDc);
		}
		// 填充数据
		for (DataRow r : rows) {
			DataRow newRow = result.createRow();
			newRow.copyFrom(r);
			result.addRow(newRow);
		}
		return result;
	}

	public DataTable Select(String tableName, String selectField, String filterString,
			String groupField) {
		DataTable result = new DataTable();
		//
		return result;
	}

	/**
	 * 功能描述： 根据指定表达式对符合过滤条件的数据进行计算
	 * 
	 * @author: James Cheung
	 * @version: 2.0
	 */
	public Object Compute(String expression, String filter) {
		return dataExpression.Compute(expression, Select(filter));
	}

	// public Object max(String columns, String filter) {
	// return null;
	// }
	//
	// public Object min(String columns, String filter) {
	// return null;
	// }
	//
	// public Object avg(String columns, String filter) {
	// return null;
	// }
	//
	// public Object max(String columns, String filter, String groupBy) {
	// return null;
	// }
	//
	// public Object min(String columns, String filter, String groupBy) {
	// return null;
	// }
	//
	// public Object avg(String columns, String filter, String groupBy) {
	// return null;
	// }
	
	public DataColumn Column(int colIndex) {
		return columns.get(colIndex);
	}

    // Hson符号提取器
    public static class TableLex extends SimpleLex
    {
        public TableLex(String Text)
        {
            super(Text);
        }

        @Override
        public int GetCharToken(char c)
        {
            return c == ',' ? LT_ItemSplitChr :
                    c == ';' ? LT_ItemSplitChr :
                    c == '|' ? LT_ItemSplitChr :

                    c == '\"' ? LT_String :
                    c == '\'' ? LT_String :
                    	
                    c == '\r' ? LT_LineSplitChr :
                    c == '\n' ? LT_LineSplitChr :

                    c == '{' ? LT_Bracket1 :
                    c == '[' ? LT_Bracket2 :
                    c == '(' ? LT_Bracket3 :

                    c == '\0' ? LT_End :
                    IsIdent(c) ? LT_Indent :
                    LT_Unknown;
        }
        
        @Override
        protected void PassSpace()
        {
            while (i < S.length && " \t".indexOf(S[i]) >= 0) i++;
            SetLast();
        }
    }
    
	public String Serialize() {
		StringBuilder sbuilder = new StringBuilder();
        // 添加表头字段名
        sbuilder.append(gs.JoinArray(columns.toArray(new DataColumn[0]), "|", 
        		new gi.IFunc1<DataColumn, String>() { @Override public String Call(DataColumn dc) { return dc.getColumnName(); } })
        	);
        sbuilder.append("\r\n");
        //foreach (DataColumn col in dt.Columns)
        //{
        //    sbuilder.Append(col.ColumnName + (i < n ? "|" : "\r\n"));
        //    var type = col.DataType;
        //    //if (rd.GetFieldType(i) == typeof(int))
        //    FieldTypes[i] = type == typeof(DateTime) ? FT_DATE :
        //                    type == typeof(int) ? FT_INT :
        //                    type == typeof(Int32) ? FT_INT :
        //                    type == typeof(Decimal) ? FT_MONEY :
        //                    type == typeof(float) ? FT_FLOAT :
        //                    FT_STRING;
        //    i++;
        //}

        //// 返回表行数据
        for(DataRow dr : rows)
        {
            sbuilder.append(gs.JoinArray(dr.ItemArray, "|", 
            		new gi.IFunc1<Object, String>() { @Override public String Call(Object val) { return gv.Serialize(val); } })
            	);
            sbuilder.append("\r\n");
        }
        return sbuilder.toString();
	}

	public Object[] getColValues(String columnName) {
		return rows.getColValues(columnName);
	}
    
    public void setColValue(String columnName, Object value) {
    	rows.setColValue(columnName, value);
	}

	public String[] getColStrValues(String columnName) {
		return rows.getColStrValues(columnName);
	}

	public int[] getColIntValues(String columnName) {
		return rows.getColIntValues(columnName);
	}

	public ArrayList<Object> getColValueList(String columnName) {
		DataColumn col = columns.get(columnName);
		if(col == null) return null;
		int colIndex = col.getColumnIndex();
		ArrayList<Object> list = new ArrayList<Object>();
		for (DataRow dr : rows)
			list.add(dr.getValue(colIndex));
		return list;
	}

	public String[] getStrValues(gi.IFunc1<DataRow, String> func) {
		String[] ss = new String[rows.size()];
		int i = 0;
		for (DataRow dr: rows)
			ss[i++] = func.Call(dr);
		return ss;
	}
	
	public static DataTable Create(String Text) throws Exception {
		SimpleLex lex = new TableLex(Text);

		String[] temp = new String[256];
		DataTable dt = new DataTable();
		
        int token = lex.GetToken();
        boolean IsFirstLine = true;
    	int index = 0;
    	int ColCount = 0;
        while (true)
        {
        	if(token == SimpleLex.LT_Indent || token == SimpleLex.LT_String)
        	{
        		if(index < temp.length) temp[index] = lex.Text;
        		if(index == 8)
        			_D.Dumb();
        	}
        	else if(token == SimpleLex.LT_ItemSplitChr) 
        		index++;
        	else if(token == SimpleLex.LT_LineSplitChr || token == SimpleLex.LT_End)
        	{
        		if(index > 0)
        		{
	        		if(IsFirstLine)
	        		{
	        			ColCount = index + 1;
	        			for(int i = 0; i < ColCount; i++)
	        				dt.addColumn(temp[i], 0);
	        			IsFirstLine = false;
	        		}
	        		else {
	        			dt.addRow((Object[]) temp);
	        		}
	    			temp = new String[ColCount];
        		}
        		index = 0;
        	}
        	if(token == SimpleLex.LT_End) break;
            token = lex.GetToken();
        }
		return dt;
	}

	public void ClearRows(){
		rows.clear();
	}

	public DataRowCollection Select(EnumSet<DataRowState> states)
	{
		DataRowCollection rs = new DataRowCollection(this);
		for (DataRow dr : rows)
			if(states.contains(dr.getState())) rs.add(dr);
		
		return rs;
	}
	
	public void AcceptChanges(){
		for (DataRow dr : getChanges())
			dr.AcceptChanges();
	}
	
	public OMap getMap(String colName){
		OMap map = new OMap();
		for (DataRow dr : rows)
			map.put(dr.getStrVal("Key"), dr);
		return map;
	}

	public DataTable GroupBy(SelectColList selects) {
		DataTable dt = Copy();
		return dt.getRows().GroupBy(selects);
	}

	public DataTable GroupBy(String sql) {
		return GroupBy(SqlUtils.ParserSqlSelect(sql));
	}
}