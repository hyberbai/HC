package hylib.toolkits;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import java.lang.reflect.Array;
import java.text.Collator;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import hylib.data.DataTable;
import hylib.util.Param;
import hylib.util.ParamList;

public class gv {
	public static String FMT_DATE = "yyyy-MM-dd";
	public static String FMT_TIME = "HH:mm:ss";
	public static String FMT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static String FMT_YMDHM = "yyyy-MM-dd HH:mm";

    public static gi.IFunc1<Object, String> hashFunc = new gi.IFunc1<Object, String>() {
    	
    	public String Call(Object o){
    		if(o instanceof Param) return ((Param)o).toString();
    		return gv.StrVal(o);
    	}
    };
  
    public static boolean IsEmpty(Object o)
    {
        //if(o instanceof TempValue)) o = ((TempValue)o).value;
    	return
	        o == null ||
	        o instanceof String && ((String)o).length() == 0 ||
	        o.getClass().isArray() && Array.getLength(o) == 0 ||
	        o instanceof Collection && ((Collection)o).size() == 0 ||
	        o instanceof Map && ((Map)o).size() == 0 ||
	        o instanceof SparseArray && ((SparseArray)o).size() == 0;
    }
	
	public static Object ConvertType(Object value, Object refValue) {
		if(refValue == null) return value;
		return ConvertType(value, refValue.getClass());
	}
	public static Object ConvertType(Object value, Class<?> cls) {
		if(cls == String.class) return gv.StrVal(value);
		if(cls == Integer.class) return gv.IntVal(value);
		if(cls == short.class) return (short)gv.IntVal(value);
		if(cls == Float.class) return gv.FVal(value);
		if(cls == Double.class) return gv.DoubleVal(value);
		if(cls == Date.class) return gv.DateVal(value);
		if(cls == Boolean.class) return gv.BoolVal(value);
		return value;
	}

	public static Class<?> GetTypeByName(String typeName)
	{
		typeName = typeName.toLowerCase();
		if (typeName == "s") return String.class;
		if (typeName == "i" || typeName == "int") return Integer.class;
		if (typeName == "d" || typeName == "dt" || typeName == "datetime") return Date.class;
		if (typeName == "b" || typeName == "bool") return Boolean.class;
		if (typeName == "m" || typeName == "dec") return Double.class;
		if (typeName == "f") return Float.class;
		if (typeName == "sint") return short.class;
		if (typeName == "byte") return Byte.class;
		return null;
	}

    public static Object Max(Object o1, Object o2)
    {
    	int v = Compare(o1, o2);
		return v < 0 ? o2 : o1;
    }

    public static Object Min(Object o1, Object o2)
    {
    	int v = Compare(o1, o2);
		return v > 0 ? o2 : o1;
    }
	
	public static String[] toStrArray(Object... args) {
		String[] result = new String[args.length];
		for (int i = 0; i < result.length; i++) 
			result[i] = gv.StrVal(args[i]);
		return result;
	}
	
	public static int[] toIntArray(Object... args) {
		int[] result = new int[args.length];
		for (int i = 0; i < result.length; i++) 
			result[i] = gv.IntVal(args[i]);
		return result;
	}

	public static Object[] toObjArray(int... args) {
		Object[] result = new Object[args.length];
		for (int i = 0; i < result.length; i++) 
			result[i] = args[i];
		return result;
	}

	public static Object[] toObjArray(String... args) {
		Object[] result = new Object[args.length];
		for (int i = 0; i < result.length; i++) 
			result[i] = args[i];
		return result;
	}
	
    public static String StrVal(Object o)
    {
        if (o == null) return "";
        if (o instanceof String) return (String)o;
//        if (o is Decimal) return ((Decimal)o).ToString("0.00");
        if (o instanceof Date) return SDateTime((Date)o);
//        if (o is TempValue) return gv.StrVal(((TempValue)o).value);
        if (o instanceof byte[]) return gv.BytesToHex((byte[])o, 0, "");
        return o.toString();
    }
    
	public static int IntVal(String s) {
		try {
            if (s.length() == 0) return 0;
            int n1 = 0;
            char c = 0;
            while(n1 < s.length())
            {
            	c = s.charAt(n1);
                if (c >= '0' && c <= '9' || c == '-') break;
                n1++;
            }
            int n2 = n1 + 1;
            while (n2 < s.length())
            {
                c = s.charAt(n2);
                if (!(c >= '0' && c <= '9')) break;
                n2++;
            }
            if (n1 == n2) return 0;
			return Integer.parseInt(s.substring(n1, n2));
		} catch (Exception e) {
			return 0;
		}
	}

	public static boolean ContainEnumVal(int v, int enumVal) {
		return (v & enumVal) != 0;
	}
	
	public static int IntVal(Object o) {
		if(o == null) return 0;
		try {
			if(o instanceof Integer) return (Integer)o;
			return IntVal(StrVal(o));
		} catch (Exception e) {
			return 0;
		}
	}

    public static boolean In(int v, int... InValues)
    {
        for(int t : InValues)
            if(v == t) return true;
        return false;
    }

    public static boolean In(String v, String... InValues)
    {
        for(String t : InValues)
            if(v.equalsIgnoreCase(t)) return true;
        return false;
    }
    
	public static Date getNowDate() {
	   try {
		   SimpleDateFormat formatter = new SimpleDateFormat(FMT_DATE);
			return formatter.parse(formatter.format(new Date()));
		} catch (Exception e) {
			return null;
		}
	}

    public static String getWeek(Date dt) {
        char[] weekDays = { '日', '一', '二', '三', '四', '五', '六' };
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;

        return "星期" + weekDays[w];
    }
    
	public static String getNowText() {
	   try {
		   Date dt = getNowDate();
		   return StrVal(dt) + " " + getWeek(dt);
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean DateContainHSM(Date date) {
		return date.getHours() + date.getMinutes() + date.getSeconds() > 0;
	}

	public static Date getDate(int year, int month, int day) {
		return new GregorianCalendar(year, month - 1, day).getTime(); 
	}
	
	public static String SDate(Date date, String fmt) {
		return getFormatDate(date, fmt);
	}
	
	public static String SDate(Date date) {
		return getFormatDate(date, FMT_DATE);
	}
	
	public static String SDate(int year, int month, int day) {
		return SDate(getDate(year, month, day));
	}
	
	public static String SDateTime(Date date) {
		String fmt = DateContainHSM(date) ? FMT_DATETIME : FMT_DATE;
		return getFormatDate(date, fmt);
	}
	
	public static String getFormatDate(Date date, String fmt) {
	   SimpleDateFormat formatter = new SimpleDateFormat(fmt);
	   return formatter.format(date);
	}

	public static String getFormatDate(String fmt) {
	   return getFormatDate(new Date(), fmt);
	}
	
    public static boolean BoolVal(Object o)
    {
        return o instanceof Boolean ? (Boolean)o :
	            o instanceof String ? (String)o == "1" || gs.SameText((String)o, "true") :
	            o instanceof Integer ? (Integer)o == 1 :
	            false;
    }
    
	public static Date StrToDate(String str) {
		boolean isDate = str.indexOf('-') > 0;
		boolean isTime = str.indexOf(':') > 0;
		String fmt = isDate && isTime ? FMT_DATETIME :
					 isDate ? FMT_DATE :
					 FMT_TIME;
		try {
			Date date = new SimpleDateFormat(fmt).parse(str);
			return date;
		} catch (Exception e) {
			return null;
		}
	}

	public static Date DateVal(Object val) {
		if(val == null) return null;
		if(val instanceof Date) return (Date)val;
		return StrToDate((String)val);
	}

	public static int DateDiff(Date d1, Date d2) {
		return (int)((d1.getTime() - d2.getTime()) / (24*60*60*1000));    
	}

	public static float ValF(String s) {
		try {
			return Float.parseFloat(s);
		} catch (Exception e) {
			return 0;
		}
	}

	public static int CeilDiv(int value, int divisor) {
		if(divisor == 0) return 0;
		int r = value / divisor;
		return value % divisor > 0 ? r + 1 : r; 
	}

	public static int Half(int value) {
		int r = value / 2;
		return value % 2 > 0 ? r + 1 : r; 
	}

    public static float FVal(Object o)
    {
        if (IsEmpty(o)) return 0;
        if (o instanceof Integer) return (float)(Integer)o;
        if (o instanceof Double) return ValF(o.toString());// (Float)(Double)o;
        return ValF(o.toString());
    }
    
    public static double DoubleVal(Object o)
    {
        if (IsEmpty(o)) return 0;
        if (o instanceof Integer) return (double)(Integer)o;
        if (o instanceof Double) return (Double)o;
        return Double.valueOf(o.toString());
    }

	public static boolean Same(Object o1, Object o2) {
		if(o1 == null) return o1 == o2;
		return o1.equals(o2);
	}
	
	public static boolean SameS(Object o1, Object o2) {
		if(o1 == null) return o1 == o2;
		return StrVal(o1).equals(StrVal(o2));
	}

	public static String FmtMoney(String s) {
		return FmtMoney(ValF(s));
	}

	@SuppressLint("DefaultLocale")
	public static String FmtMoney(float M) {
		try {
			return new DecimalFormat("￥0.##").format(M);
		} catch (Exception e) {
			return "";
		}
	}

	public final static int AM_LETTER = 1 << 0;
	public final static int AM_UPCASE = 1 << 1;
	public final static int AM_LOWCASE = 1 << 2;
	public final static int AM_NUMBER = 1 << 3;
	public final static int AM_FLOAT = 1 << 4;
	public final static int AM_HEX = 1 << 5;
	private static byte[] mAsciiMaskTable = GetAsciiMaskTable();
	public static byte[] GetAsciiMaskTable()
	{
		byte[] table = new byte[256];
		for(int i = 0; i < 256; i++)
		{
			if(i >= 'a' && i <= 'z') table[i] |= AM_LETTER | AM_LOWCASE; 
			if(i >= 'A' && i <= 'Z') table[i] |= AM_LETTER | AM_UPCASE; 
			if(i >= '0' && i <= '9') table[i] |= AM_NUMBER | AM_HEX | AM_FLOAT; 
			if(i >= 'a' && i <= 'f') table[i] |= AM_HEX; 
			if(i >= 'A' && i <= 'F') table[i] |= AM_HEX; 
			if(i == '.') table[i] |= AM_FLOAT; 
		}	
		return table;
	}
	
	private static boolean checkAsciiMaskTableFlag(char c, int flag) {
		return c < mAsciiMaskTable.length && (mAsciiMaskTable[c] & flag) != 0;
	}

	public static boolean IsFloat(char c){
		return checkAsciiMaskTableFlag(c, AM_FLOAT);
	}
	
	public static boolean IsLetter(char c){
		return checkAsciiMaskTableFlag(c, AM_LETTER);
	}

	public static boolean IsUpper(char c){
		return checkAsciiMaskTableFlag(c, AM_UPCASE);
	}

	public static boolean IsLower(char c){
		return checkAsciiMaskTableFlag(c, AM_LOWCASE);
	}

	public static boolean IsHex(char c){
		return checkAsciiMaskTableFlag(c, AM_HEX);
	}

	public static boolean IsDigit(char c){
		return checkAsciiMaskTableFlag(c, AM_NUMBER);
	}
	
	private static final int AaDiff = 'a' - 'A'; 
	public static char toUpper(char c){
		return IsLower(c) ? (char)(c - AaDiff): c;
	}
	
	public static char toLower(char c){
		return IsUpper(c) ? (char)(c + AaDiff): c;
	}

	private static byte[] mHexTable = GetHexMap();
	public static byte[] GetHexMap()
	{
		byte[] table = new byte[256];
		for(int i = 0; i < 256; i++)
			table[i] = i >= '0' && i <= '9' ? (byte)(i - '0') :  
						i >= 'a' && i <= 'f' ? table[i] = (byte)(i - 'a' + 10) :
						i >= 'A' && i <= 'F' ? table[i] = (byte)(i - 'A' + 10) :
						-1;
				
		return table;
	}

	public static char toHexChr(int n){
		return (char)(n < 10 ? n + '0' : n < 16 ? n - 10 + 'A' : 0);
	}

	public static String BytesToHex(byte[] bytes){
		char[] chrs = new char[bytes.length * 2];
		int p = 0;
		for(int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			chrs[p++] = toHexChr((b & 0xF0) >> 4);
			chrs[p++] = toHexChr(b & 0x0F);			
		}
		return new String(chrs);
	}

	public static byte[] HexToBytes(String hexStr){
		int len = hexStr.length()/2;
		byte[] bytes = new byte[len];
		
		for(int i=0;i<len;i++)
			bytes[i] = (byte)(mHexTable[hexStr.charAt(2*i)]<<4 |
							  mHexTable[hexStr.charAt(2*i+1)]);
		return bytes;
	}

    public static String BytesToHex(byte[] buffer, int line_len, String Space)
    {
        StringBuilder sb = new StringBuilder();
        if (buffer != null)
            for (int i = 0; i < buffer.length; i++)
            {        
            	int v = buffer[i] & 0xFF;   
            	String hv = Integer.toHexString(v); 
                if (hv.length() < 2) sb.append(0);   
                sb.append(hv + Space);   
                if ((line_len > 0) && ((i + 1) % line_len == 0))
                    sb.append('\n');
            }
        return sb.toString();
    }
    
	public static Object[] ResizeArray(Object[] original, int newLength){
		Object[] newArray = new Object[newLength];
		System.arraycopy(original, 0, newArray, 0, Math.min(original.length, newLength));
		return newArray;
	}

	public static Object ResizeArrayEx(Object oldArray, int newSize){
		int oldSize = Array.getLength(oldArray);
		Class elementType = oldArray.getClass().getComponentType();
		Object newArray = Array.newInstance(elementType, newSize);
		int preserveLength = Math.min(oldSize,newSize);
		if (preserveLength > 0) System.arraycopy (oldArray,0,newArray,0,preserveLength);
		return newArray; 
	
	}
	
	@SuppressWarnings("unchecked")
    public static <T> T[] InitArray(Class<T> type, int size, T defValue)
    {
    	T[] a =  (T[]) Array.newInstance(type, size);//new T[size];
    	for(int i = 0; i < size; i++) a[i] = defValue;
    	return a;
    }

	public enum SerializeOption
	{
		None(0),
		SingleQuote(1),    // 字符串加单引号
		UseEscape(2);      // 使用转义符

		private int  mOption = 0;
		private SerializeOption(int value)
		{
			mOption = value;
		}

		public boolean Contains(SerializeOption value)
		{
			return (mOption & value.mOption)  != 0;
		}

		public void Include(SerializeOption value)
		{
			mOption |= value.mOption;
		}

		public void Exclude(SerializeOption value)
		{
			 mOption &= ~value.mOption;
		}
	}

	private static char strQuoteChar = '\"';
	private static char strQuoteChar1 = '\'';
	private static SerializeOption mSerializeOptions = SerializeOption.None;
	public static void SetSerializeOptions(SerializeOption include, SerializeOption exclude)
	{
		mSerializeOptions.Include(include);
		mSerializeOptions.Exclude(exclude);
		boolean isSingleQuote = mSerializeOptions.Contains(SerializeOption.SingleQuote);
		strQuoteChar = isSingleQuote ? '\'' : '\"';
		strQuoteChar1 = isSingleQuote ? '\"' : '\'';
	}

	public static void SetSerializeOptions(SerializeOption include) {
		SetSerializeOptions(include, SerializeOption.None);
	}

    public static String SerializeStr(String v)
    {
        if (v == null) return "";
    	int len = v.length();
        if (len == 0) return "''";
        if (len > 1)
        {
            char c0 = v.charAt(0);
            char c1 = v.charAt(len - 1);
            if(c0 == c1 && (c0 == '\"' || c0 == '\'' )) return v;   // 已添加过引号忽略处理
        }

		int flag = 0;
		if (v.indexOf(strQuoteChar) >= 0)
			flag = 1; // 加转义符
		else {
			String needQuoteChars  = strQuoteChar1 + " ,;|:={}[]()\t\r\n";
			for (char c : v.toCharArray())
			if (needQuoteChars.indexOf(c) >= 0)
			{
				flag = 2; // 需要添加引号的情况
				break;
			}
		}
		if (flag > 0)
		{
			if (v.indexOf('\\') >= 0) v = v.replace("\\", "\\\\");
			if (flag == 1) return strQuoteChar + v.replace(String.valueOf(strQuoteChar), "\\" + strQuoteChar) + strQuoteChar; // 加转义符
			//if (v[v.Length - 1] == '\\') return '\"' + v + "\\\"";
			v = strQuoteChar + v + strQuoteChar;
		}

		if (mSerializeOptions.Contains(SerializeOption.UseEscape))
			v = v.replace("\n", "\\n").replace("\r", "\\r");
		return v;
    }

    public static String SerializeArray(int[] arr)
    {
    	StringBuilder sb = new StringBuilder();
        for (int item : arr)
            sb.append((sb.length() == 0 ? "" : ", ") + Serialize(item));

        return "[" + sb.toString() + "]";
    }

    public static <T> String SerializeArray(T[] os)
    {
    	StringBuilder sb = new StringBuilder();
        for (T item : os)
            sb.append((sb.length() == 0 ? "" : ", ") + Serialize(item));

        return "[" + sb.toString() + "]";
    }

    public static String SerializeMap(Map<String, Object> map, String NameValueSplitStr, String ParamSplitStr)
    {
    	StringBuilder sb = new StringBuilder();
    	for (Map.Entry<String, Object> item : map.entrySet())
    	{
    		String key = item.getKey();
    		Object val = item.getValue();
            sb.append(	(sb.length() == 0 ? "" : ParamSplitStr) + 
            			SerializeStr(key) + 
            			(val == null ? "" : NameValueSplitStr + Serialize(val)));
    	}
        return "{ " + sb.toString() + " }";
    }

    // 序列化对象
    public static String Serialize(Object o)
    {
    	//   ObjectOutputStream oos = new ObjectOutputStream(fos);  
    	if(o == null) return "";
    	Class<?> ctype = o.getClass();
        return 	ctype.equals(String.class) ? SerializeStr((String)o) :
        		ctype.equals(Param.class) ? SerializeStr(((Param)o).Name)+ ":" + Serialize(((Param)o).Value) :
        		ctype.equals(HashMap.class) ? SerializeMap((Map)o, ":", ",") :
            	ctype.equals(DataTable.class) ? ((DataTable)o).Serialize() :
            	o instanceof ParamList ? ((ParamList)o).toString() :
            	o instanceof Collection ? SerializeCollection((Collection<?>)o) :
                ctype == int[].class ? SerializeArray((int[])o) :
            	ctype.isArray() ? SerializeArray((Object[])o) :
//                o instanceof IDictionary<String, object> ? SerializeDict((IDictionary<String, object>)o) :
                SerializeStr(gv.StrVal(o));
    }
    
    public static String SerializeCollection(Collection<?> collection) {
    	StringBuilder sb = new StringBuilder();
    	for (Object v : collection)
            sb.append((sb.length() == 0 ? "" : ", ") + Serialize(v));

        return "[" + sb.toString() + "]";
	}

    public static Collator collator = Collator.getInstance(java.util.Locale.getDefault());
    public static int Compare(Object a, Object b)
    {
    	if(a == null) return b == null ? 0 : -1;
    	if(b == null) return 1;
    	if(!a.getClass().equals(b.getClass())) ExProc.ThrowMsgEx("值类型不一致，无法比较！");
    	if(a instanceof String) return collator.compare((String)a, (String)b); 
    	if(a instanceof Integer) return (Integer)a - (Integer)b; 
    	if(a instanceof Float) {
    		float r = (Float)a - (Float)b;
    		return r == 0 ? 0 : r > 0 ? 1 : -1;
    	}
    	if(a instanceof Double) {
    		Double r = (Double)a - (Double)b;
    		return r == 0 ? 0 : r > 0 ? 1 : -1;
    	}
    	return collator.compare(a.toString(), b.toString());
    	//return a.toString().compareTo(b.toString());
    }

    // 序列化对象包含类型
    public static String SerializeWithType(Object o)
    {
        if(o == null) return null;
        return o.getClass().getSimpleName() + ":" + Serialize(o);
    }

    // 反序列化对象
    public static Object DeserializeWithType(String s) throws Exception
    {
        int i = s.indexOf(":");
        if (i < 0) return s;
        String typeName = s.substring(0, i);
        s = s.substring(i + 1);
        return typeName.equals(DataTable.class.getSimpleName()) ? (Object) DataTable.Create(s) :
               (Object)s;
    }

    public static class DatePart {
		public int year;
		public int month;
		public int day;
		public int hour;
		public int min;
		public int sec;
	}
    
    public static DatePart GetDatePart(Date dt) {
        Calendar c = Calendar.getInstance();
        if(dt != null) c.setTime(dt);
        DatePart dp = new DatePart();
        dp.year = c.get(Calendar.YEAR);
        dp.month = c.get(Calendar.MONTH);
        dp.day = c.get(Calendar.DAY_OF_MONTH);
        dp.hour = c.get(Calendar.HOUR_OF_DAY);
        dp.min = c.get(Calendar.MINUTE);
        dp.sec = c.get(Calendar.SECOND);
        return dp;
	}

    public static Date CalcDate(Date date, int field, int value)
    {
        Calendar calendar = new GregorianCalendar(); 
        calendar.setTime(date); 
        calendar.add(field, value);
//        calendar.add(Calendar.YEAR, 1);//把日期往后增加一年.整数往后推,负数往前移动
//        calendar.add(Calendar.DAY_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
//        calendar.add(Calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
//        calendar.add(Calendar.WEEK_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
        return calendar.getTime();   //这个时间就是日期往后推一天的结果 
    }

    public static Date DateAdd(Date date, int days)
    {
    	return CalcDate(date, Calendar.DATE, days);
    }
    
    public static Date GetMonthLastDay(Date date)
    {
        Calendar calendar = new GregorianCalendar(); 
        calendar.setTime(date); 
        calendar.add(calendar.MONTH, 1);
        calendar.add(calendar.DATE, -1);
        return calendar.getTime(); 
    }
}
