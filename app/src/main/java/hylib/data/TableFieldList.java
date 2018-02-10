package hylib.data;

import hylib.toolkits.MapList;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.util.ParamList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableFieldList extends MapList<TableField> {
	private HashMap<String, TableField> map;

	@Override
	protected String getKey(TableField value) {
		return value.Name;
	}
	
	@Override
    public String toString()
    {
		StringBuilder sb = new StringBuilder();
        // 字段配置信息
    	for (TableField fi : this)
            sb.append("\t" + fi.toString() + ",\n");
    	
        return sb.toString();
    }
	
	public TableField[] getVisibleFields() {
		List<TableField> list = new ArrayList<TableField>();

    	for (TableField fi : this)
            if(fi.Visible) list.add(fi);
    	
		return list.toArray(new TableField[0]);
	}

	
	public TableField[] findFields(String[] keys) {
		List<TableField> list = new ArrayList<TableField>();

    	for (String key : keys)
    	{
    		TableField f = get(key);
    		if(f == null) continue;
            list.add(f);
    	}
		return list.toArray(new TableField[0]);
	}

	public static String getFieldsText(TableField[] fields) {
		String result = "";
		for (TableField field : fields)
			result += (result.length() > 0 ? "," : "") + field.Name;
		return result;
	}

	public String getVisibleFieldsText() {
		return getFieldsText(getVisibleFields());
	}

	public String getFieldsText(String keys) {
		return getFieldsText(findFields(gs.Split(keys)));
	}
}
