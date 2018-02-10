package hylib.data;

import org.xml.sax.DTDHandler;

import android.util.Base64;
import hylib.db.SqlDbType;
import hylib.edit.DType;
import hylib.toolkits.ExProc;
import hylib.toolkits.gv;
import hylib.util.Hson;
import hylib.util.Param;
import hylib.util.SimpleLex;
import hylib.util.Hson.HsonLex;

public class TableUtils {
	
    public static SqlDbType getDbTypeValueOf(String name) {
    	if(name.equalsIgnoreCase("int") || name.equalsIgnoreCase("integer") || name.equalsIgnoreCase("int32")) 
    		return SqlDbType.Int;
    	
    	if(name.equalsIgnoreCase("tinyint")) return SqlDbType.Int;
    	
    	if(name.equalsIgnoreCase("bit")) return SqlDbType.Bit;

    	if(name.equalsIgnoreCase("text")) return SqlDbType.Text;
    	
    	if(name.equalsIgnoreCase("datetime")) return SqlDbType.DateTime;
    	if(name.equalsIgnoreCase("date")) return SqlDbType.Date;
    	
    	if(name.equalsIgnoreCase("decimal")) return SqlDbType.Decimal;
    	if(name.equalsIgnoreCase("image")) return SqlDbType.Image;
    	if(name.equalsIgnoreCase("varchar")) return SqlDbType.VarChar;
    	if(name.equalsIgnoreCase("nvarchar")) return SqlDbType.NVarChar;
    	return SqlDbType.NVarChar;
    }

    public static int getDataTypeValueOf(SqlDbType dbType) {

    	if(dbType == SqlDbType.Int) return DType.Int;
    	if(dbType == SqlDbType.Bit) return DType.Bool;
    	if(dbType == SqlDbType.DateTime) return DType.DateTime;
    	if(dbType == SqlDbType.Date) return DType.Date;
    	if(dbType == SqlDbType.Decimal) return DType.Dec;
    	if(dbType == SqlDbType.Image) return DType.Image;
    	return DType.STRING;
    }
    
	public static DataTable ParseSqliteCreateTableText(String text) {
        SqlLex lex = new SqlLex(text);

        if(!lex.GetTokenTextEquals("create") || !lex.GetTokenTextEquals("table")) 
        	ExProc.ThrowMsgEx("解析建表Sql语句失败!\n" + text);
         
    	if(lex.GetToken() != SqlLex.LT_Indent) ExProc.ThrowMsgEx("解析表名失败!\n" + text);
    	DataTable tab = new DataTable(lex.Text);

    	if(lex.GetToken() != SqlLex.LT_Items) ExProc.ThrowMsgEx("解析字段结构失败!\n" + text);

    	lex = new SqlLex(lex.Text);
        while(lex.Token != SqlLex.LT_End) {

        	// 字段名 
        	if(lex.GetToken() != SqlLex.LT_Indent) ExProc.ThrowMsgEx("解析字段失败!\n" + text);
        	DataColumn dc = new DataColumn(lex.Text);

        	// 字段类型
        	if(lex.GetToken() != SqlLex.LT_Indent) ExProc.ThrowMsgEx("解析字段失败!\n" + text);
        	dc.setDbType(getDbTypeValueOf(lex.Text));
        	
        	// 字段长度
        	lex.GetToken();
        	if(lex.Token == SqlLex.LT_Items) { // [xxx] nvarchar(10)
        		dc.DataLength = gv.IntVal(lex.Text);
        		lex.GetToken();
        	}

        	// 其它属性，是否为主键、是否自动增量字段、默认值
        	while(lex.Token != SqlLex.LT_ItemSplitChr && lex.Token != SqlLex.LT_End)
        	{
        		if(lex.TokenTextEquals("PRIMARY") && lex.GetTokenTextEquals("KEY")) // [XX] int PRIMARY KEY NOT NULL 
        			dc.setPK(true);
        		else if(lex.TokenTextEquals("AUTOINCREMENT"))  
        			dc.setAutoInc(true);
        		else if(lex.TokenTextEquals("DEFAULT")) {	// xxx xx DEFAULT '0'
        			lex.GetToken();
        			dc.DefaultValue = lex.Text;
        		}
    			lex.GetToken();
        	}
        	tab.addColumn(dc);
        }
        return tab;
	}
}
