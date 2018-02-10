package hylib.data;

import android.R.integer;
import hylib.toolkits.ExProc;
import hylib.toolkits.gv;
import hylib.util.IdMap;

public class SqlUtils {
	public enum ComputeType { None, COUNT, SUM, MIN, MAX, AVG, Items };
	
	public static IdMap mIdMap;
	
	
	public static int ID_COUNT = getNewID();
	public static int ID_SUM = getNewID();
	public static int ID_MIN = getNewID();
	public static int ID_MAX = getNewID();
	public static int ID_AVG = getNewID();
	public static int ID_ITEMS = getNewID();
	
	private static int startID = 1000;
	public static int getNewID() {
		return ++startID;
	}
	
	static {
		mIdMap = new IdMap();
		mIdMap.put("COUNT", ID_COUNT);
		mIdMap.put("SUM", ID_SUM);
		mIdMap.put("MIN", ID_MIN);
		mIdMap.put("MAX", ID_MAX);
		mIdMap.put("AVG", ID_AVG);
		mIdMap.put("ITEMS", ID_ITEMS);
	}
	
	public static SelectColList ParserSqlSelect(String sql) {
		SelectColList result = new SelectColList();

        SqlLex lex = new SqlLex(sql);
        String s = "";
        while(lex.Token != SqlLex.LT_End) {
        	SelectColInfo sc = new SelectColInfo();

        	// 表达式 
        	if(lex.GetToken() == SqlLex.LT_Indent)
        	{
        		s = lex.Text;
    			sc.setName(s);
    			result.add(sc);
        		Integer id = mIdMap.get(s);
        		if(id != null)
        		{
        			ComputeType compType =
        					id == ID_COUNT ? ComputeType.COUNT :
	        				id == ID_SUM ? ComputeType.SUM :
	        				id == ID_MIN ? ComputeType.MIN :
	        				id == ID_MAX ? ComputeType.MAX :
		        			id == ID_AVG ? ComputeType.AVG :
			        		id == ID_ITEMS ? ComputeType.Items :
		        			ComputeType.None;
        			
            		if(compType != ComputeType.None) {
            			sc.setExp(compType, null);
            			if(id != ID_ITEMS && compType != ComputeType.COUNT) {
            				if(lex.GetToken() != SqlLex.LT_Items) 
            	        		ExProc.ThrowMsgEx("无效表达式!\n" + sql);
            				sc.expression.Exp = lex.Text;
            				sc.setName(lex.Text);
            			}
            		}
        		}
        	} 
        	else
        		ExProc.ThrowMsgEx("解析字段失败!\n" + sql);
        	
        	lex.GetToken();
        	if(lex.Token == SqlLex.LT_Indent) {
        		sc.Alias = lex.Text;
        		lex.GetToken();
        	}

        	if(lex.Token == SqlLex.LT_End) break;
        	if(lex.Token != SqlLex.LT_ItemSplitChr) ExProc.ThrowMsgEx("解析字段分隔符失败!\n" + sql);
        }
		return result;
	}
}
