package hylib.data;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;

import hylib.edit.DType;
import hylib.toolkits.gv;

public class DataColumnCollection extends ArrayList<DataColumn> {
	private static final long serialVersionUID = 1L;
	private HashMap<String, DataColumn> mMap;
	
	public DataColumnCollection() {
		mMap = new HashMap<String, DataColumn>();
	}

	public DataColumnCollection(String config) {
		this();
		CreateTableColumns(config);
	}

    public void CreateTableColumns(String config)
    {
        String[] ss = config.split("\\|");
        int dataType;
        for (int i = 0; i < ss.length; i++)
        {
        	if(gv.IsEmpty(ss[i])) continue;
        	String[] ss1 = ss[i].split("/");
            if (ss1.length == 0) continue;
            String name = ss1[0].trim();

            boolean isPK = name.charAt(0) == '@';
            if(isPK) name = name.substring(1);
            boolean isAutoInc = name.charAt(0) == '+';
            if(isAutoInc) name = name.substring(1);
            dataType = ss1.length == 1 ? DType.STRING :
                       ss1[1].equalsIgnoreCase( "d") ? DType.DateTime :
                       ss1[1].equalsIgnoreCase( "i") ? DType.Int :
                       ss1[1].equalsIgnoreCase( "f") ? DType.Dec :
                       ss1[1].equalsIgnoreCase( "m") ? DType.Money :
                       ss1[1].equalsIgnoreCase( "b") ? DType.Bool :
                       ss1[1].equalsIgnoreCase( "e") ? DType.Ext :
                       DType.STRING;
            DataColumn dc = addColumn(name, dataType);
            if(isPK) dc.setPK(true);
            if(isAutoInc) dc.setAutoInc(true);
        }
    }

    public Class<?>[] getColClasses(){
		Class<?>[] result = new Class<?>[size()];
		int i = 0;
		for (DataColumn dc : this)
			result[i++] = DType.getClass(dc.getDataType());
		return result;
	}

	public boolean containsCol(String columnName) {
		return get(columnName) != null;
	}
    
	public int getColumnsIndex(String columnName) {
		DataColumn dc = get(columnName); 
		return dc == null ? -1 : dc.getColumnIndex();
	}
    
	@SuppressLint("DefaultLocale")
	public DataColumn get(String columnName) {
		return mMap.get(columnName.toLowerCase());
//		for (DataColumn col : this) 
//			if(col.getColumnName().equals(columnName)) return col;
//		return null;
	}

	public DataColumn getPKColumn() {
		for (DataColumn dc : this)
			if(dc.IsPK()) return dc;
		return null;
	}

	public DataColumnCollection getSelectCols(int flag) {
		DataColumnCollection cols = new DataColumnCollection();
		for (DataColumn dc : this)
			if(
				(flag & DataColumn.FLAG_NOT_PK) > 0 && !dc.IsPK() ||
				(flag & DataColumn.FLAG_NOT_AUTOINC) > 0 && !dc.IsAutoInc()
			  ) 
				cols.add(dc);
		return cols;
	}
	
	public DataColumnCollection getNoPkColumns() {
		DataColumnCollection cols = new DataColumnCollection();
		for (DataColumn dc : this)
			if(!dc.IsPK()) cols.add(dc);
		return cols;
	}
	
	public int getDataType(int colIndex) {
		if(colIndex < 0) return -1;
		DataColumn dc = this.get(colIndex);
		return dc == null ? -1 : dc.getDataType();
	}

	public int getDataType(String colName) {
		return getDataType(getColumnsIndex(colName));
	}
	
	@SuppressLint("DefaultLocale")
	@Override
	public boolean add(DataColumn col) {
		mMap.put(col.getColumnName().toLowerCase(), col);
		if(col.getColumnIndex() < 0)col.setColumnIndex(this.size());
		return super.add(col);
	}
	
	public DataColumn addColumn(String columnName, int dataType) {
		if(containsCol(columnName)) return null;
		DataColumn col = new DataColumn();
		col.setColumnName(columnName);
		col.setDataType(dataType);
		add(col);
		return col;
	}
}
