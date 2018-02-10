package hylib.toolkits;

import hylib.edit.DType;
import hylib.util.TextAlign;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.common.StringUtils;

import android.R.bool;
import android.R.integer;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.provider.SyncStateContract.Constants;
import android.text.TextUtils;


@SuppressLint("DefaultLocale")
public class gs {
	public final static int SP_RemoveEmpty = 1;
	
	public static String Sub(char[] S, int offset, int count){
		if(offset >= S.length) return "";
		if(offset + count > S.length) count = S.length - offset;
		return new String(S, offset, count);
	}
	
	public static String Sub(String S, int start, int count){
		if(S == null) return null;
		if(start >= S.length()) return "";
		if(start + count > S.length()) return S.substring(start);
		return S.substring(start, start + count);
	}

    static public boolean SameText(String s1, String s2)
    {
        return s1.equalsIgnoreCase(s2);
    }

    public static String Left(String s, int len)
    {
        return len < 1 ? "" : len > s.length() ? s : s.substring(0, len);
    }

    public static String Right(String s, int len)
    {
        return len < 1 ? "" : len > s.length() ? s : s.substring(s.length() - len);
    }

    public static String Connect(Object O1, Object O2, String Space)
    {
    	String S1 = gv.StrVal(O1);
    	String S2 = gv.StrVal(O2);
        if (S1 == null || S1.isEmpty()) return S2;
        if (S2 == null || S2.isEmpty()) return S1;
        return S1 + Space + S2;
    }
    
    // v = 123, text = '2' 
	public static boolean Match(Object v, String text) {
		String sval = gv.StrVal(v).toUpperCase();		text = text.toUpperCase();
		if(sval.indexOf(text) >= 0) return true;
		sval = PY.Get(sval);
		return sval.indexOf(text) >= 0;
	}

    public static class CutResult {
		public String S1;
		public String S2;
	}
    
    // 规格化日期字符串
    public static String NormalizeDate(String v, Boolean MonthEnd)
    {
        String sy = null;
        String sm = null;
        String sd = null;
        try
        {
        	String s;
            v = v.replace('.', '-').replace('/', '-');
            String[] ss = v.split("-");
            if (ss.length > 1)
            {
                sy = ss[0];
                sm = ss[1];
                if(gv.IntVal(sy) < 100) sy = String.valueOf(2000 + gv.IntVal(sy));
                if (ss.length == 3) sd = ss[2];
                if (ss.length > 3) return "";
            }
            else if (v.length() == 4)
            { sy = v.substring(0, 2); sm = v.substring(2, 4); if (gv.IntVal(sm) > 12) { s = sy; sy = sm; sm = s; } }
            else if (v.length() == 6)
            { 
            	sy = v.substring(0, 2); sm = v.substring(2, 4); sd = v.substring(4, 6); 
            	if (gv.IntVal(sy) < 13) {
            		s = sy; sy = sm; sm = s;
            		s = sy; sy = sd; sd = s;
            	}  
            }
            else if (v.length() == 8)
            { sy = v.substring(0, 4); sm = v.substring(4, 6); sd = v.substring(6, 8); }
            else
                return "";
            if (sy.length() == 2) sy = "20" + sy;
            
			Date date = sd != null ? gv.getDate(gv.IntVal(sy), gv.IntVal(sm), gv.IntVal(sd)) :
                        MonthEnd ? gv.GetMonthLastDay(gv.getDate(gv.IntVal(sy), gv.IntVal(sm), 1)) :
                        gv.getDate(gv.IntVal(sy), gv.IntVal(sm), 1);
            return gv.SDate(date);
        }
        catch(Exception e)
        {
            return "";
        }
    }

    /*
     *  规格化数值
     */
    public static String NormalizeNumeric(String v)
    {
    	return String.valueOf(gv.IntVal(v));
    }

    /*
     * 规格化小数数值
     */
    public static String NormalizeDec(String v)
    {
    	return String.valueOf(gv.FVal(v));
    }

    /*
     * 规格化金额
     */
    public static String NormalizeMoney(String v)
    {
    	return String.valueOf(gv.FVal(v));
    }
    
    public static String NormalizeValue(String value, int valueType)
    {
		if(valueType == DType.Date) return gs.NormalizeDate((String)value, false);
		if(valueType == DType.Num) return gs.NormalizeNumeric((String)value);
		if(valueType == DType.Dec) return gs.NormalizeDec((String)value);
		if(valueType == DType.Money) return gs.NormalizeMoney((String)value);
		return value;
    }
    
    // StrCutOptions
    public final static int SC_CutLast = 1;
    public final static int SC_Trim = 2;
    public final static int SC_EmptyToS1 = 4;
    public final static int SC_Main2 = 8;
    public final static int SC_Normal = 6;
    
    public static CutResult Cut(String Text, String CutStr, int Options)
    {
    	CutResult r = new CutResult();
        Text = Text.trim();
        
        int i = ((Options & SC_CutLast) != 0) ? Text.indexOf(CutStr) : Text.lastIndexOf(CutStr);
        if (i < 0)
        {
            r.S1 = ((Options & SC_Main2) == 0) ? Text : "";
            r.S2 = (Options & SC_EmptyToS1) > 0 ? Text :
                 (Options & SC_Main2) > 0 ? Text :
                    "";
        }
        else
        {
        	r.S1 = Text.substring(0, i);
        	r.S2 = Text.substring(i + CutStr.length());
        }
        if ((Options & SC_Trim) > 0)
        {
        	r.S1 = r.S1.trim();
        	r.S2 = r.S2.trim();
        }
        return r;
    }

    public static CutResult Cut(String Text, String CutStr)
    {
    	return Cut(Text, CutStr, SC_Trim);
    }


    public static void TrimStrArray(String[] ss)
    {
        for (int i = 0; i < ss.length; i++) ss[i] = ss[i].trim();
    }

    public static String[] Split(String Text, String SplitStr, int Options)
    {
        String[] ss = Text.split(SplitStr);
        if(Options == SP_RemoveEmpty)
        {
        	TrimStrArray(ss);
        	
        	boolean containEmpty = false;
        	for (String s : ss)
				if(s.length() == 0) {
					containEmpty = false;
					break;
				}
        	
			if(containEmpty) {
	        	ArrayList<String> list = new ArrayList<String>();
	        	for (String s : ss) 
					if(s.length() > 0) list.add(s); 
		        return list.toArray(new String[0]);
			}
        }
        return ss;
    }

    public static String[] Split(String Text, String SplitStr)
    {
    	return Split(Text, SplitStr, SP_RemoveEmpty);
    }

    public static String[] Split(String Text)
    {
    	return Split(Text, ",", SP_RemoveEmpty);
    }


    public static char getSplitChar(String Text)
    {
    	final String splitChrs = " ,;/|，；"; 
    	for (char c : Text.toCharArray()) {
			int i = splitChrs.indexOf(c);
			if(i >= 0) return splitChrs.charAt(i);
		}
    	return 0;
    }
    
    public static String nStr(String str, int n, String sConnect)
    {
    	String result = "";
    	for(int i = 0; i < n; i++)
    		result += (i == 0 ? "" : sConnect) + str;
        return result;
    }
    
    public static String nStr(String str, int n)
    {
        return nStr(str, n, "");
    }
    
    public static String nChar(char chr, int n)
    {
    	char[] chrs = new char[n];
    	for(int i = 0; i < n; i++) chrs[i] = chr;
        return new String(chrs);
    }
	
	public static int getTextLenA(String text) {
		try {
			return text.getBytes("GBK").length;
		} catch (Exception e) {
			return text.length();
		}
	}
    
    public static String leftPad(String s, int len, char c)
    {
    	if(s.length() >= len) return s;
    	return s + nChar(c, len - s.length());
    }
    
    public static String leftPad(String s, int len)
    {
    	return leftPad(s, len, ' ');
    }
    
    public static String leftPadA(String s, int len, char c)
    {
    	int textLen = getTextLenA(s);
    	if(textLen >= len) return s;
    	return s + nChar(c, len - textLen);
    }
    
    public static String leftPadA(String s, int len)
    {
    	return leftPadA(s, len, ' ');
    }
    
    public static String rightPadA(String s, int len, char c)
    {
    	int textLen = getTextLenA(s);
    	if(textLen >= len) return s;
    	return nChar(c, len - textLen) + s;
    }
    
    public static String rightPadA(String s, int len)
    {
    	return rightPadA(s, len, ' ');
    }

    
    public static String centerPadA(String s, int len, char c)
    {
    	int textLen = getTextLenA(s);
    	if(textLen >= len) return s;
    	len -= textLen;
    	int half = len / 2;
    	return nChar(c, half) + s + nChar(c, len - half);
    }
    
    public static String centerPadA(String s, int len)
    {
    	return centerPadA(s, len, ' ');
    }
    
    public static String padStrA(String s, int len, int align)
    {
    	if(align == TextAlign.LEFT) return leftPadA(s, len);
    	if(align == TextAlign.RIGHT) return rightPadA(s, len);
    	if(align == TextAlign.CENTER) return centerPadA(s, len);
    	return s;
    }
    
    // 去除引号
    public static String RemoveQuotes(String s, char quoCharHead, char quoCharEnd)
    {
        int l = s.length();
        if (l == 0) return "";
        int i = 0;
        if (quoCharEnd == '\0') quoCharEnd = quoCharHead;
        if (s.charAt(0) == quoCharHead) i = 1;
        if (s.charAt(l - 1) == quoCharEnd) l--;
        if (i == 1) l--;
        if (l <= 0) return "";
        return s.substring(i, l);
    }

    public static String RemoveQuotes(String s, char quoCharHead)
    {
    	return RemoveQuotes(s, quoCharHead, '\0');
    }
    
	public static String JoinList(List<?> list, String JoinStr){
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if(i == 0) 
				sb.append(obj.toString());
			else
				sb.append(JoinStr + obj.toString());
		}
		return sb.toString();
	}
    
	public static <T>  String JoinList(List<T> list, String JoinStr, gi.IFunc1<T, String> selector){
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < list.size(); i++) {
			String s = gv.StrVal(selector.Call(list.get(i)));
			sb.append(i == 0 ? s : JoinStr + s);
		}
		return sb.toString();
	}
	
	public static <T> String JoinArray(T[] array, String JoinStr, gi.IFunc1<T, String> selector){
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < array.length; i++) {
			String s = gv.StrVal(selector.Call(array[i]));
			if(i == 0) 
				sb.append(s);
			else
				sb.append(JoinStr + s);
		}
		return sb.toString();
	}
	
	public static <T> String JoinArray(T[] array, String JoinStr, boolean RemoveEmpty, int from){
		StringBuilder sb = new StringBuilder();
		
		for (int i = from; i < array.length; i++) {
			Object obj = array[i];
            if (RemoveEmpty && gv.IsEmpty(obj)) continue;
			if(sb.length() == 0) 
				sb.append(obj.toString());
			else
				sb.append(JoinStr + obj.toString());
		}
		return sb.toString();
	}
	
	public static <T> String JoinArray(T[] array, String JoinStr){
		return JoinArray(array, JoinStr, true, 0);
	}
	
	public static <T> String JoinArray(int[] array, String JoinStr){
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < array.length; i++)
			if(sb.length() == 0) 
				sb.append(String.valueOf(array[i]));
			else
				sb.append(JoinStr + String.valueOf(array[i]));
		return sb.toString();
	}
	
	public static <T> String JoinArray(T[] array, char JoinChr){
		return JoinArray(array, String.valueOf(JoinChr), true, 0);
	}

	public static String TextEscape(String text, String escChrs){
		char[] cc = text.toCharArray();
		char[] r = new char[cc.length * 2];
		int i = 0;
		for (char c : cc) {
			if(escChrs.indexOf(c) < 0) 
				r[i++] = c;
			else {
				r[i++] = '\\';
				r[i++] = c;
			}
		}
		return new String(r, 0, i);
	}

	public static CharSequence getFmtMoney(float money) {
		SpannableStringHelper sh = new SpannableStringHelper();
		getFmtMoney(sh, money);
		return sh;
	}
	
	public static CharSequence getFmtQty(int qty) {
		SpannableStringHelper sh = new SpannableStringHelper();
		getFmtQty(sh, qty);
		return sh;
	}

	public static void getFmtMoney(SpannableStringHelper sh, float money) {
		sh.appendMoney(money, Color.RED, 1.0f);
	}
	
	public static void getFmtQty(SpannableStringHelper sh, int qty) {
		sh.startSpan();
		sh.appendObj("x " + qty);
		sh.setForeColor(Color.GRAY);
		sh.setFontSize(0.9f);
	}

	public static String guessEncoding(byte[] bytes) {
		try {
			return new String(bytes, TextCodingUtils.detectEncodingCharset(bytes));
		} catch (UnsupportedEncodingException ignored) {
			return null;
		}
	}
}
