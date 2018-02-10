package hylib.util;

import java.util.ArrayList;

import android.util.Pair;
import hylib.toolkits.ExProc;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.type;

// Hson语法解析器，比JSON的语法检测更宽松
public class Hson {

    public static class HsonParseOptions
    {
    	public final static int None = 0;
    	public final static int AllowEmpty = 1;
    }

    // Hson符号提取器
    public static class HsonLex extends SimpleLex
    {
        public HsonLex(String Text)
        {
            super(Text);
        }

        @Override
        public int GetCharToken(char c)
        {
            return c == ',' ? LT_ItemSplitChr :
                    c == ';' ? LT_ItemSplitChr :
                    c == '|' ? LT_ItemSplitChr :

                    c == ':' ? LT_PropSplitChr :
                    c == '=' ? LT_PropSplitChr :

                    c == '\"' ? LT_String :
                    c == '\'' ? LT_String :

                    c == '{' ? LT_Bracket1 :
                    c == '[' ? LT_Bracket2 :
                    c == '(' ? LT_Bracket3 :

                    c == '\0' ? LT_End :
                    IsIdent(c) ? LT_Indent :
                    LT_Unknown;
        }
    }

    // 将一组标识组合成一个字符串，s1 s2 s3 
    public static String ParseIndentS(HsonLex lex)
    {
        String s = "";
        while (lex.Token == SimpleLex.LT_Indent)
        {
        	if(s.length() == 0)
        		s = lex.Text;
        	else
        		s += " " + lex.Text;
            lex.GetToken();
        }
        lex.Back();
        return s;
    }

    public static Object ParseKeyValue(HsonLex lex, int Options)
    {
        String pname = "";
        Object pvalue = null;
        pname = lex.Text;

        int token = lex.GetToken();
        if (token == SimpleLex.LT_PropSplitChr) // pname : pvalue
        {
            token = lex.GetToken();
            if (token == SimpleLex.LT_String)
                pvalue = lex.Text;
            else if (token == SimpleLex.LT_Indent)
                pvalue = ParseIndentS(lex);
            else if (token == SimpleLex.LT_Bracket1)    // pname : { key value pairs }
                pvalue = Parse(lex.Text, token, Options);
            else if (token == SimpleLex.LT_Bracket2)    // pname : [ array ]
                pvalue = Parse(lex.Text, token, Options);

            return new Param(pname, pvalue);
        }
        else if (token == SimpleLex.LT_ItemSplitChr || token == SimpleLex.LT_End)  // pname 
            return pname;
        else if (token == SimpleLex.LT_Indent)  // s1 s2 s3 
        {
            pname += " " + ParseIndentS(lex);
            return pname;
        }
        return null;
    }

    public static ParamList ParseKeyValues(HsonLex lex, int Options) 
    {
    	ParamList pms = new ParamList();
        while (true)
        {
            int token = lex.GetToken();
            if (token == SimpleLex.LT_End) break;

            if (token == SimpleLex.LT_Indent || token == SimpleLex.LT_String)
            {
                Object o = ParseKeyValue(lex, Options);
                if (o instanceof Param)
                    pms.put(((Param)o).Name, ((Param)o).Value);
                else if (o instanceof String)
                    pms.put((String)o, null);
            }
            else if (token == SimpleLex.LT_ItemSplitChr)
                continue;
            else
            {
                lex.Back();
                ExProc.ThrowMsgEx("无效键值对！" + lex.Text + "\n" + lex.NearText());
            }
        }
        return pms;
    }

    public static Object ParseArray(HsonLex lex, int Options)
    {
    	ArrayList<Object> list = new ArrayList<Object>();
        while (true)
        {
            Object pvalue = null;
            int token = lex.GetToken();

            if (token == SimpleLex.LT_End) break;

            if (token == SimpleLex.LT_Indent || token == SimpleLex.LT_String)
                pvalue = ParseKeyValue(lex, Options);
            else if (token == SimpleLex.LT_Bracket1)    // keyvalues
                pvalue = Parse(lex.Text, token, Options);
            else if (token == SimpleLex.LT_Bracket2)    // array
                pvalue = Parse(lex.Text, token, Options);
            else if (token == SimpleLex.LT_ItemSplitChr)    // 空元素
                if ((Options & HsonParseOptions.AllowEmpty) > 0) list.add(null);
            if (pvalue != null) list.add(pvalue);
        }
        //   _D.Out("ParseArray", list.ToArray());
        return list.toArray();
    }

    public static Object Parse(String sParams, int token, int Options)
    {
        if (sParams == null) return null;

        HsonLex lex = new HsonLex(sParams);

        if (token == SimpleLex.LT_Bracket1)
            return ParseKeyValues(lex, Options);
        else if (token == SimpleLex.LT_Bracket2)    // array
            return ParseArray(lex, Options);
        else
            return ParseKeyValues(lex, Options);
    }

    public static Object Parse(String sParams, int Options)
    {
        if (gv.IsEmpty(sParams)) return null;

        HsonLex lex = new HsonLex(sParams);
        int token = lex.GetToken();
        if (token == SimpleLex.LT_Bracket1 || token == SimpleLex.LT_Bracket2)   // { [
            return Parse(lex.Text, token, Options);
        else if (token == SimpleLex.LT_Indent || token == SimpleLex.LT_String)  // aaaa "ssss"
        {
            token = lex.GetToken();
            if (token == SimpleLex.LT_End) return lex.Text;
        }
        return Parse(sParams, SimpleLex.LT_Unknown, Options);
    }

    public static Object Parse(String sParams)
    {
    	return Parse(sParams, HsonParseOptions.None);
    }
}
