package hylib.data;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hylib.edit.DType;
import hylib.toolkits.PY;
import hylib.toolkits.gi;
import hylib.toolkits.gs;
import hylib.toolkits.gv;

public class DataRowCollection extends ArrayList<DataRow>  {
	private static final long serialVersionUID = 1L;
	public DataTable Table;

	public DataRowCollection() {
		Table = new DataTable();
	}
	
	public DataRowCollection(DataTable table) {
		Table = table;
	}
	
	public DataRowCollection(String colsConfig, Object[]... values) {
		Table = new DataTable("", colsConfig, this);
		SetRowValues(values);
	}
	
	public DataRowCollection(DataRowCollection values) {
		SetRowValues(values);
	}
	
	public DataRowCollection(String colsConfig, List<?> values) {
		Table = new DataTable("", colsConfig, this);
		SetRowValues(values);
	}

	public void SetRowValues(Object[]... values) {
		for (Object[] item : values)
			Table.addRow(item);
	}

	public void SetRowValues(List<?> values) {
		for (Object item : values)
			if(item instanceof Object[])
				Table.addRow((Object[])item);
			else if(item instanceof DataRow)
				Table.addRow((DataRow)item);
			else
				Table.addRow(item);
	}

	public void DeleteAll() {
		for (DataRow dr : this) dr.Delete();
	}
	
	public void Delete(DataRow dr) {
		dr.Delete();
	}
	
	public void Remove(DataRow dr) {
		//Delete(dr);
		int index = indexOf(dr);
		if(index < 0) return;
		remove(index);
	}
	
	public void MoveTo(int fromIndex, int toIndex) {
		if(fromIndex == toIndex) return;
		DataRow dr = get(fromIndex);
		if(toIndex > size()) toIndex = size();
		if(fromIndex < toIndex) toIndex--;
		remove(fromIndex);
		add(toIndex, dr);
	}

    public DataRow FindRow(String columnName, Object value) {
    	int col = Table.getColumn(columnName).getColumnIndex();
    	for (DataRow dr : this)
			if(gv.Same(dr.getValue(col), value)) return dr;
        return null;
    }

	public DataRow FindRow(gi.IFunc1<DataRow, Boolean> funcFind) {
		for (DataRow dr : this)
			if(funcFind.Call(dr)) return dr;
		return null;
	}
    
    public DataRowCollection SelectRows(String columnName, Object value) {
    	DataRowCollection rs = new DataRowCollection(Table);
    	int col = Table.getColumn(columnName).getColumnIndex();
    	for (DataRow dr : this)
			if(gv.Same(dr.getValue(col), value))
				rs.add(dr);
        return rs;
    }

	public String[] getTextRows(String fieldName) {
		String[] ss = new String[size()];
		int i = 0;
		for (DataRow dr : this) 
			ss[i++] = dr.getStrVal(fieldName);
		return ss;
	}

	public Object[] getColValues(String columnName) {
		DataColumn col = Table.getColumns().get(columnName);
		if(col == null) return null;
		Object[] values = new Object[size()];
		int colIndex = col.getColumnIndex();
		for (int i = 0; i < size(); i++)
			values[i] = get(i).getValue(colIndex);
		return values;
	}

	public String[] getColStrValues(String columnName) {
		Object[] values = getColValues(columnName);
		if(values == null) return null;
		String[] ss = new String[values.length];
		for (int i = 0; i < values.length; i++)
			ss[i] = gv.StrVal(values[i]);
		return ss;
	}

	public int[] getColIntValues(String columnName) {
		Object[] values = getColValues(columnName);
		if(values == null) return null;
		int[] ii = new int[values.length];
		for (int i = 0; i < values.length; i++)
			ii[i] = gv.IntVal(values[i]);
		return ii;
	}
    
    public void setColValue(String columnName, Object value) {
    	int colIndex = Table.getColumns().getColumnsIndex(columnName);
		for (DataRow dr : this)
			dr.setValue(colIndex, value);
	}

	public DataRowCollection MatchText(String text, int top) {
		DataRowCollection rows = new DataRowCollection(Table);
		int i = 0;
		for (DataRow dr : this)
			if(dr.MatchText(text)) {
				rows.add(dr);
				if(++i >= top) break;
			}
		return rows;
	}

    @SuppressLint("DefaultLocale")
	public DataRowCollection MatchText(String scols, String text, int top) {
    	if(Table == null) assert false;
		DataRowCollection rows = new DataRowCollection(Table);
		int i = 0;
    	String[] cols = scols.split(",");
    	String[] ss = gs.Split(text.toUpperCase(), " ", gs.SP_RemoveEmpty);
    	
    	int[] colIndices = new int[100];
    	int colCount = 0;
    	for (String colName : cols)
    	{
    		int colIndex = Table.getColumns().getColumnsIndex(colName);
    		if(colIndex > 0) colIndices[colCount++] = colIndex;
    	}
    	
    	colIndices = (int[])gv.ResizeArrayEx(colIndices, colCount);
    		
		for (DataRow dr : this)
		{
			String sval = "";
			for (int col : colIndices)
				sval += "|" + gv.StrVal(dr.getValue(col)).toUpperCase();
			
			boolean match = true;
			for (String s : ss)
    			if(sval.indexOf(s) < 0 && PY.Get(sval).indexOf(s) < 0){
    				match = false;
    				break;
    			}
    		if(match) {
    			rows.add(dr);
    			if(rows.size() >= top) break;
    		}
		}
		return rows;
    }
    
    public Object Sum(String colName) {
		DataColumn dc = Table.getColumn(colName);
		int colIndex = dc.getColumnIndex();
		Object result = null;
		for (DataRow dr : this) {
			if(dc.getDataType() == DType.Int)
				result = gv.IntVal(result) + dr.getIntVal(colIndex);
			else if(gv.In(dc.getDataType(), DType.Dec, DType.Money))
				result = gv.FVal(result) + dr.getFVal(colIndex);
		}
		return result;
	}

    public void Sort(String sortItems) {
    	List<Integer> list = new ArrayList<Integer>();
    	DataColumnCollection columns = Table.getColumns();
    	for (String item : sortItems.split(",")) {
    		gs.CutResult r = gs.Cut(item, " ");
    		int col = columns.getColumnsIndex(r.S1);
    		if(r.S2.equalsIgnoreCase("desc")) col = -col;
    		list.add(col);
		}
    	
    	final Integer[] items = list.toArray(new Integer[0]); 
		Collections.sort(this, new java.util.Comparator<DataRow>() {
			
			@Override
			public int compare(DataRow r0, DataRow r1) {
				for (int item : items) {
					int col = item;
					int v = col > 0 ? gv.Compare(r0.getValue(col), r1.getValue(col)) :
							gv.Compare(r1.getValue(-col), r0.getValue(-col));;
					if(v != 0) return v;
				}
				return 0;
			}
		});
	}
    
    public DataTable GroupBy(SelectColList selects) {
		SelectColList normalSelCols = selects.getNormalCols();
		SelectColList computeSelCols = selects.getComputeCols();
		Sort(normalSelCols.getAliasText());
		DataTable dt = Table;
		
		DataTable result = selects.createTable();
		boolean changed = true;
		DataRow drResult = null;
		int groupCount = 0;

		DataRowCollection items = null;
		
		// 设置列数据类型
		for (SelectColInfo sc : selects) {
			int colDataType = 0; 
			if(sc.isItems()) 
				colDataType = DType.Ext;
			else {
				colDataType = dt.getColumnDataType(sc.Name);
				if(colDataType < 0) continue;
			}
			DataColumn dc = result.getColumn(sc.Alias);
			dc.setDataType(colDataType);
		}
		
		for (int rowIndex = 0; rowIndex < size(); rowIndex++) {
			DataRow dr = get(rowIndex);
			// 比较分组字段是否变动
			if(drResult != null)
				for (SelectColInfo sc : normalSelCols) {
					if(!gv.Same(dr.getValue(sc.Name), drResult.getValue(sc.Alias)))
					{
						changed =true;
						break;
					}
				}
			
			if(changed)
			{
				if(drResult != null) {
					for (SelectColInfo sc : computeSelCols)
						if(sc.expression.isAvgType()) drResult.setAvg(sc.Alias, groupCount);
				}
				
				drResult = result.addRow();
				for (SelectColInfo sc : normalSelCols) {
					drResult.setValue(sc.Alias, dr.getValue(sc.Name));
				}

				if(drResult != null && selects.containItems()) {
					items = new DataRowCollection(dr.getTable());
					drResult.setValue("items", items);
				}
				changed = false;
				groupCount = 0;
			}

			// 计算列
			for (SelectColInfo sc : computeSelCols) {
				SqlExpression exp = sc.expression;
				Object value = dr.getValue(sc.Name);
				if(exp.isMaxType()) drResult.setMax(sc.Alias, value); else
				if(exp.isMinType()) drResult.setMin(sc.Alias, value); else
				if(exp.isCountType()) drResult.setValue(sc.Alias, groupCount); else
				drResult.setSum(sc.Alias, value);
			}

			// 如果包含分组明细字段，则将其数据行记录
			if(items != null) items.add(dr);
			groupCount++;
		}

		if(drResult != null)
			for (SelectColInfo sc : computeSelCols)
				if(sc.expression.isAvgType()) drResult.setAvg(sc.Alias, groupCount);
		return result;
	}

	public DataTable GroupBy(String sql) {
		return GroupBy(SqlUtils.ParserSqlSelect(sql));
	}
}
