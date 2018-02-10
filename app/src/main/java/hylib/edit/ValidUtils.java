package hylib.edit;

import hylib.toolkits.gv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidUtils {

	public static boolean isValidDateTime(String str, String fmt) {
       SimpleDateFormat format = new SimpleDateFormat(fmt);
       try {
          format.setLenient(false);
          format.parse(str);
          return true;
       } catch (ParseException e) {
          // e.printStackTrace();
           return false;
       } 
	}

	public static boolean isValidDate(String str) {
		return isValidDateTime(str, "yyyy-MM-dd");
	}
	
	public static boolean isValidTime(String str) {
		return isValidDateTime(str, "hh:mm:ss");
	}

	public static boolean isNumeric(String str) {
		for (char c : str.toCharArray())
		   if (!Character.isDigit(c)) return false;
		return true;
	}

	public static boolean matchPattern(String str, String patt){ 
	   Pattern pattern = Pattern.compile(patt); 
	   Matcher isNum = pattern.matcher(str);
	   return isNum.matches();
	}

	public static boolean isInt(String str){ 
		return matchPattern(str, "^-?[0-9]+");
	}

	public static boolean isDec(String str){ 
		return matchPattern(str, "-?[0-9]+(.[0-9]+)?");
	}

	public static boolean isMoney(String str){ 
		return matchPattern(str, "[0-9]+(.[0-9]+)?");
	}

	public static boolean validValue(String str, int valueType){ 
		if(valueType == DType.Int) return isInt(str);
		if(valueType == DType.Dec) return isDec(str);
		if(valueType == DType.Money) return isDec(str);
		if(valueType == DType.Date) return isValidDate(str);
		if(valueType == DType.Time) return isValidTime(str);
		if(valueType == DType.Num) return isNumeric(str);
		return true;
	}

	public static boolean validValue(Object o, int valueType){ 
		return validValue(gv.StrVal(o), valueType);
	}
}
