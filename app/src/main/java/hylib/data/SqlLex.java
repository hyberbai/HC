package hylib.data;

import hylib.util.SimpleLex;

// sql 符号提取器
public class SqlLex extends SimpleLex
{
	public final static int LT_Items = LT_CUSTOM + 1;

    public SqlLex(String Text)
    {
        super(Text);
    }

    @Override
    protected String GetText()
    {
        if (Token == LT_Bracket2) {	// []
            Token = LT_Indent;
          //  return GetPureText();
        }
        if (Token == LT_Bracket3) { // ()
            Token = LT_Items;
          //  return GetPureText();
        }
        if (Token == LT_String) {
            Token = LT_Indent;
        }
        return super.GetText();
    }
    
    @Override
    public int GetCharToken(char c)
    {
        return c == ',' ? LT_ItemSplitChr :

                c == '=' ? LT_PropSplitChr :

                c == '\"' ? LT_String :
                c == '\'' ? LT_String :

                c == '[' ? LT_Bracket2 :
                c == '(' ? LT_Bracket3 :

                c == '\0' ? LT_End :
                IsIdent(c) ? LT_Indent :
                LT_Unknown;
    }
}
