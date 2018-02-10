package hylib.ui.dialog;

import com.google.zxing.common.StringUtils;

import hylib.sys.HyApp;
import hylib.toolkits.HyColor;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import android.R.integer;
import android.R.raw;
import android.view.Gravity;

public class HyKeyValues {

	public static int getGravitys(String sval) {
		int gravity = 0;
		for (String s : gs.Split(sval, "|"))
			gravity |= getGravity(s);
		return gravity;
	}
	
	public static int getGravity(String sval) {
		if(sval.equals("l")) return Gravity.START;
		if(sval.equals("r")) return Gravity.END;
		if(sval.equals("t")) return Gravity.TOP;
		if(sval.equals("b")) return Gravity.BOTTOM;
		if(sval.equals("c")) return Gravity.CENTER;
		if(sval.equals("cv")) return Gravity.CENTER_VERTICAL;
		if(sval.equals("ch")) return Gravity.CENTER_HORIZONTAL;
		return 0;
	}
	
	public static Object ParseVal(Object v) {
		if(v instanceof Integer) return (Integer)v;
		if(v instanceof String) {
			if(((String) v).charAt(0) == '#') return Long.valueOf(((String) v).substring(1), 16).intValue();
			return gv.IntVal(v);
		}
		return null;
	}

	public static Object ParseDrawrable(Object v) {
		return v instanceof String ? HyApp.getDrawable((String)v) : null;
	}
	
	public static Integer ParseColorVal(Object v) {
		if(v instanceof Integer) return (Integer)v;
		if(HyColor.plColor.containsKey(v)) return (Integer)HyColor.plColor.get(v);

		if(v instanceof String) {
			Integer c = HyApp.getColor((String)v);
			if(c != null) return c;
			
			if(((String) v).charAt(0) == '#') {
				String sv = ((String) v).substring(1);
				if(sv.length() == 3) sv =  "FF" + sv.charAt(0) + sv.charAt(0)
										+ sv.charAt(1) + sv.charAt(1)
										+ sv.charAt(2) + sv.charAt(2);
				if(sv.length() <= 6) sv = "FF" + gs.leftPad(sv, 6, '0');
				return Long.valueOf(sv, 16).intValue();
			}
		}
		return null;
	}
	
}
