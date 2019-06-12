package hylib.edit;

import java.util.Date;

import hylib.toolkits.gv;


public class DType {
	public final static int STRING = 0; // 字符文本
	public final static int Num = 1;	// 正整数型
	public final static int Int = 2;	// 整数型
	public final static int Dec = 3;	// 带小数型
	public final static int Money = 3;	// 金额型
	public final static int Date = 5;	// 日期型
	public final static int Time = 6;	// 时间型
	public final static int DateTime = 7;// 完整日期时间
	public final static int Bool = 8;	// 布尔型 
	public final static int Image = 9;	// 日间型

	public final static int Ext = -1;	// 扩展型

	public static int valueOf(String sType) {
		return 	sType.equalsIgnoreCase("int") ? Int :
		 		sType.equalsIgnoreCase("n") ? Int :
			 	sType.equalsIgnoreCase("b") ? Bool :
			 	sType.equalsIgnoreCase("num") ? Int :
				sType.equalsIgnoreCase("dec") ? Dec :
				sType.equalsIgnoreCase("money") ? Money :
				sType.equalsIgnoreCase("date") ? Date :
				sType.equalsIgnoreCase("time") ? Time :
				sType.equalsIgnoreCase("dt") ? DateTime :
				sType.equalsIgnoreCase("img") ? Image :
				STRING;
	}

	public static String getName(int valueType) {
		return valueType == Num ? "num" :
				valueType == Int ? "int" :
				valueType == Dec ? "dec" :
				valueType == Money ? "money" :
				valueType == Time ? "time" :
				valueType == Date ? "date" :	
				valueType == DateTime ? "dt" :
				valueType == Bool ? "b" :
				valueType == Image ? "img" :	
				"";
	}

	public static Class<?> getClass(int valueType) {
		if (valueType == Num) return Integer.class;
		if (valueType == Int) return Integer.class;
		if (valueType == Dec) return Double.class;
		if (valueType == Money) return Double.class;
		if (valueType == Time) return Date.class;
		if (valueType == Date) return Date.class;
		if (valueType == DateTime) return Date.class;
		if (valueType == Bool) return boolean.class;
		if (valueType == Image) return byte[].class;
		return null;
	}

	public static int valueOf(Class<?> cls) {
		if (cls == Integer.class) return Int;
		if (cls == Double.class) return Dec;
		if (cls == Date.class) return Date;
		if (cls == boolean.class) return Bool;
		if (cls == byte[].class) return Image;
		return 0;
	}

	public static Object ConvertType(Object value, int dataType) {
		if(dataType == STRING) return gv.StrVal(value);
		if(dataType == Int || dataType == Num) return gv.IntVal(value);
		if(dataType == Dec) return gv.FVal(value);
		if(dataType == Money) return gv.FVal(value);
		if(dataType == DateTime || dataType == Date || dataType == Time) return gv.DateVal(value);
		if(dataType == Bool) return gv.BoolVal(value);
		if(dataType < 0) return value;
		return gv.Serialize(value);
	}
}
