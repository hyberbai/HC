package hylib.util;

import android.R.bool;
import android.R.integer;
import android.R.raw;
import hylib.toolkits.gs;

public class SimpleLex {
    // LexToken
	public final static int LT_Unknown = 0;
	public final static int LT_Indent = 1;
	public final static int LT_PropSplitChr = 2;   // :
	public final static int LT_ItemSplitChr = 3;   // ; 
	public final static int LT_LineSplitChr = 4;   // \r\n  
	public final static int LT_String = 5;         // ""
	public final static int LT_Bracket1 = 10;      // {}
	public final static int LT_Bracket1S = 11;     // {
	public final static int LT_Bracket1E = 12;     // }
	public final static int LT_Bracket2 = 20;      // []
	public final static int LT_Bracket2S = 21;     // [
	public final static int LT_Bracket2E = 22;     // ]
	public final static int LT_Bracket3 = 30;      // ()
	public final static int LT_Bracket3S = 31;     // (
	public final static int LT_Bracket3E = 32;     // )
	public final static int LT_BracketArray = LT_Bracket2; // []
	public final static int LT_End = -1;

	public final static int LT_CUSTOM = 10000;
	
    protected char[] S;
    public int Token;
    protected int iLast;
    protected int i0;
    protected int i;
    
	public String Text;

    public SimpleLex(String Text)
    {
        S = Text.toCharArray();
        i = 0;
        i0 = 0;
    }

    public static boolean IsIdent(char c)
    {
        return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '<' || c == '>' ||
               c == '_' || c == '-' || c == '@' || c == '%' || c == '@' || c == '&' || c == '^' || c == '.' ||
               c == '$' || c == '#' || c == '!' || c == '+' || c == '*' || c == '/' || c == '\\' ||
               c > (char)0xff;
    }

    public int GetCharToken(char c)
    {
        return c == ',' ? LT_ItemSplitChr :
                c == ';' ? LT_ItemSplitChr :
                c == '|' ? LT_ItemSplitChr :

                c == '\"' ? LT_String :
                c == '\'' ? LT_String :
                	
				c == '\r' ? LT_LineSplitChr :
				c == '\n' ? LT_LineSplitChr :

                c == '{' ? LT_Bracket1 :
                c == '[' ? LT_Bracket2 :
                c == '(' ? LT_Bracket3 :

                c == '\0' ? LT_End :
                IsIdent(c) ? LT_Indent :
                LT_Unknown;
    }

    public static char GetBracketEndChar(char c)
    {
        return c == '{' ? '}' :
                c == '[' ? ']' :
                c == '(' ? ')' :
                '\0';
    }

    public void SetLast()
    {
        iLast = i0;
        i0 = i;
    }

    public void Back()
    {
        i = i0;
        i0 = iLast;
    }

    public int GetToken()
    {
        PassSpace();
        if (i >= S.length)
            Token = LT_End;
        else
        {
            Text = "";
            Token = LT_Unknown;
            while (i < S.length)
            {
                char c = S[i];
                Token = GetCharToken(c);

                if (i > i0 && Token != LT_Unknown) break;    // 未知符号

                if (Token == LT_ItemSplitChr || Token == LT_PropSplitChr || Token == LT_End)
                {
                    Text = new String(S, i0, i - i0);
                    i++;
                    break;
                }

                if (Token == LT_String)
                {
                    i++; i0 = i;
                    NextStr(c);
                    break;
                }
                
                if (Token == LT_LineSplitChr)
                {
                    i++; i0 = i;
                    while (i < S.length)
                    {
                        c = S[i];
                        if(c != '\r' && c != '\n') break;
                        i++;
                    }
                    break;
                }

                if (Token == LT_Bracket1 || Token == LT_Bracket2 || Token == LT_Bracket3)
                {
                    i++; i0 = i;
                    Next(GetBracketEndChar(c));
                    break;
                }

                if (Token == LT_Indent)
                {
                    i++;
                    while (i < S.length && IsIdent(S[i])) i++;
                    break;
                }
                i++;
            }
            int oToken = Token;
            Text = GetText();

            if (oToken == LT_String || oToken == LT_Bracket1 || oToken == LT_Bracket2 || oToken == LT_Bracket3)
                Inc();
        }
        return Token;
    }
    
    public String GetTokenText()
    {
    	int token = GetToken();
    	return token == LT_Indent || token == LT_String ? Text : "";
    }
    
    public boolean GetTokenTextEquals(String text)
    {
    	return GetTokenText().equalsIgnoreCase(text);
    }
    
    public boolean TokenTextEquals(String text)
    {
    	return Text.equalsIgnoreCase(text);
    }

    protected String GetText()
    {
        String text = new String(S, i0, i - i0);
        if (Token != LT_String) return text;
        if(text.indexOf('\"') >= 0) return text.replace("\\\"", "\"");
        if(text.indexOf('\'') >= 0) return text.replace("\\\'", "\'");
        return text;
    }

    // 读取当前位置前后10个字符，指示错误用
    public String NearText()
    {
        return gs.Sub(S, i - 10, 10) + '^' + gs.Sub(S, i, 10);
    }

    protected void PassSpace()
    {
        while (i < S.length && " \t\r\n".indexOf(S[i]) >= 0) i++;
        SetLast();
    }

    private void NextStr(char cTo)
    {
        while (i < S.length)
        {
            if (S[i] == cTo)
                if (i < 1 || S[i - 1] != '\\') break;   // 如果在引号前没有转义符则识读完成
            i++;
        }
    }

    public void Next(char cTo)
    {
        while (i < S.length)
        {
            char c = S[i];
            if (c == cTo) break;
            int token = GetCharToken(c);
            if (token == LT_Bracket1 || token == LT_Bracket2 || token == LT_Bracket3)
            {
                i++;
                Next(GetBracketEndChar(c));
            }
            else if (token == LT_String)
            {
            	i++;
                NextStr(c);
            }
            i++;
        }
    }

    public void Inc()
    {
        if (i < S.length) i++;
    }
}
