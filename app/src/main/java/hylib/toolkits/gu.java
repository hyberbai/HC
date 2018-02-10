package hylib.toolkits;

import hylib.util.ActionInfo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Locale;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.widget.LinearLayout.LayoutParams;

public class gu {
	public static int V_NULL = -16552603;

	public static boolean FileExists(String filename) {
		File file = new File(filename);
		return file.exists();
	}


	public static void Sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			return;
		}
	}
    
    public static int dp2px(Context context, float dpValue) {  
    	final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    public static int px2dp(Context context, float pxValue) {  
    	final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  

    public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
    	final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    	return (int) (spValue * fontScale + 0.5f);
    }
    
    public static int getPx(Context context, String s) {
    	UnitValue unit = caleCommonUnitValue(context, s);
    	if(unit == null) return V_NULL;
    	return unit.Type == UnitType.px ? (int)unit.Value : 0;
    }
    
    public static int getWeight(Context context, String s) {
    	UnitValue unit = caleCommonUnitValue(context, s) ;
    	return unit.Type == UnitType.weight ? (int)unit.Value : 0;
    }
    
    /*
     * 将文本换算为通用单位，像素或权值
     */
    public static UnitValue caleCommonUnitValue(Context context, String s) {
        int n = 0;
        while(n < s.length()) if (" \t\r\n".indexOf(s.charAt(n))<0) break; else n++;
        int n0 = n;
        if(n >= s.length()) return null;
        if(s.charAt(n) == '-') n++;
        while(n < s.length()) if (gv.IsFloat(s.charAt(n))) n++; else break;
        
        float v = n0 == n ? 0 : gv.FVal(s.substring(n0, n));
        
        while(n < s.length()) if (" \t\r\n".indexOf(s.charAt(n))<0) break; else n++;
        int nu = n;
        while(n < s.length()) if (gv.IsLetter(s.charAt(n))) n++; else break;
        
        if(n == n0) return null;

        String unit = s.substring(nu, n).toLowerCase();
    	return unit.equals("dp") ? new UnitValue(UnitType.px, dp2px(context, v)) :
    		   unit.equals("sp") ? new UnitValue(UnitType.px, sp2px(context, v)) :
    		   unit.equals("wrap") ? new UnitValue(UnitType.fit, LayoutParams.WRAP_CONTENT) :
    		   unit.equals("match") ? new UnitValue(UnitType.fit, LayoutParams.MATCH_PARENT) :
    		   unit.equals("w") ? new UnitValue(UnitType.weight, v) :
    		   new UnitValue(UnitType.px, v);
    }
    
	public static String BytesToHex(byte[] buffer, int offset, int length, int line_len) {

		String s = "";

		if (buffer != null) {
			int n = Math.min(offset + length, buffer.length);
			for (int i = offset; i < n; i++) {

				String hexChar = Integer.toHexString(buffer[i] & 0xFF);
				if (hexChar.length() == 1) {
					hexChar = "0" + hexChar;
				}

				s += hexChar.toUpperCase(Locale.US) + " ";
				if ((line_len > 0) && ((i + 1) % line_len == 0))
					s += "\n";
			}
		}

		return s;
	}
	
	public static Class<?> getBaseType(Object o){
		if (o instanceof Integer) return int.class;
		if (o instanceof String) return String.class;
		if (o instanceof Double) return double.class;
		if (o instanceof Float) return float.class;
		if (o instanceof Date) return Date.class;
		if (o instanceof Long) return long.class;
		if (o != null) return o.getClass();
		return Object.class;
	}
    
	public static Object ExecuteMethod(Object owner, String name, Object... args) throws Exception {
		Class<?> cls = owner.getClass();
		int n = name.indexOf('#');
		if(n > 0)
		{
			if(args == null) args = new Object[0];
			String key = name.substring(n + 1);
			name = name.substring(0, n);
			args = ArrayTools.Insert(args, 0, key);
		}

	    Class[] argsClass = new Class[args.length];   
	    for (int i = 0, j = args.length; i < j; i++)
	        argsClass[i] = getBaseType(args[i]);

		Method md = cls.getMethod(name, argsClass);
		return md.invoke(owner, args);
	}
    
	public static Object SafeExecuteMethod(Object owner, String name, Object... args) throws Exception {
		try {
			return ExecuteMethod(owner, name, args);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	
	public static void sendKey(int key) {
	    try  
	    {  
	        String keyCommand = "input keyevent " + key;  
	        Runtime runtime = Runtime.getRuntime();  
	        runtime.exec(keyCommand);  
	    }  
	    catch (IOException e)  
	    {  
	        e.printStackTrace();  
	    }  
	}
}
