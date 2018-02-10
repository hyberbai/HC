package hylib.data;

import java.util.List;
import java.util.Map;

public class DataExpression {
	public DataTable Table;

	public DataExpression(DataTable table) {
		Table = table;
	}
	
	public Object Compute(String filterString, Object[] values) {
    	return null;
    }
	
	public Object Compute(String filterString, List<DataRow> rows) {
    	return null;
    }
}
