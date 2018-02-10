package hylib.db;

import java.util.Date;

import hylib.data.DataColumn;
import hylib.toolkits.gi;
import hylib.toolkits.gs;
import hylib.toolkits.gu;
import hylib.toolkits.gv;
import android.R.integer;
import android.view.View;

public class SqlHelper {

    public static String SqlVal(Object o)
    {
        //if (o is TempValue) o = ((TempValue)o).value;
        if (o == null) return "NULL";
        return 
               o instanceof String ? "'" + ((String)o).replace("\'", "\'\'") + "'" :
               o instanceof Date ? GetDateTimeSqlValue((Date)o) :
               o instanceof Boolean ? ((Boolean)o ? "1" : "0") :
               o instanceof byte[] ? "0x" + gv.BytesToHex((byte[])o, 0, "") :
               gv.StrVal(o);
    }

    public static String GetDateTimeSqlValue(Date d)
    {
        if (d == null) return "NULL";
        return "'" + gv.SDateTime(d) + "'";
    }

    public static String SqlIn(int[] items)
    {
        String sIn = gv.IsEmpty(items) ? "-1" : gs.JoinArray(items, ",");
        return " in (" + sIn + ")";
    }
    
    public static String SqlIn(Object[] items)
    {
        String sIn = gv.IsEmpty(items) ? "-1" : gs.JoinArray(items, ",");
        return " in (" + sIn + ")";
    }
}
