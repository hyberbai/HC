package hylib.data;

import hylib.toolkits.MapList;

import java.util.HashMap;


public class TableList extends MapList<TableInfo> {

	@Override
	protected String getKey(TableInfo table) {
		return table.Name;
	}
}
