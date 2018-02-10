package hylib.data;


public class DataSort {
	public static String getSortName(String colName, boolean isAsc) {
		return colName + (isAsc ? "" : " desc");
	}
}
