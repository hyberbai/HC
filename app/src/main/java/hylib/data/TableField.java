package hylib.data;

import java.util.Map;

import hylib.db.SqlDbType;
import hylib.toolkits.gs;
import hylib.util.ParamList;


public class TableField {
	public String Name;
	public SqlDbType Type;
	public int DataLength;
	public boolean Visible;
	public boolean IsPK;
	public boolean IsAuotInc;
    public int Prop;

    public String CnName;   // 中文名
    public String Disp;
    public String Alias;    // 别名
    
	public TableInfo Table;
    public ParamList Params;
    
    public boolean AutoIncrement;

    // 字段属性                       内设       自定义      连接      扩展XML  关联引用  临时
    public final static int FP_Fixed = 1;
    public final static int FP_Custom = 2;
    public final static int FP_Join = 3;
    public final static int FP_Ext = 9;
    public final static int FP_Ref = 10;
    public final static int FP_Temp = 5;

    public TableField()
    {
        Visible = true;
//        SqlType = SqlDbType.NVarChar;
        Params = new ParamList();
    }

    public TableField(String name) 
    {
    	this();
        if (name.charAt(0) == '@') // 主键字段
        {
            IsPK = true;
            Prop = FP_Fixed;
            Name = name.substring(1);
        }
        else
            Name = name;
    }
    
    public TableField(String name, ParamList pmsField)
    {
    	this(name);
        LoadParams(pmsField);
    }

    public TableField(String name, String config)
    {
    	this (name, new ParamList(config));
    }

    public void LoadParams(ParamList pmsField)
    {
        // 读取字段详细参数
        if (pmsField == null)
        {
            Visible = false;
            return;
        }
        
        CnName = pmsField.SValue("cn");     // 中文名
        Disp = pmsField.SValue("disp");     // 显示名
        Alias = pmsField.SValue("alias");   // 别名

        AutoIncrement = pmsField.BValue("AutoInc");   // 是否为自动增量字段

        if (pmsField.BValue("hide")) Visible = false;
    }


	@Override
    public String toString()
    {
		ParamList pms = new ParamList();

//        var et = EditInfo.EType;
//        var w = Width;
        pms.SetValue("cn", CnName);
        if (Disp != CnName && Disp != Name) pms.SetValue("disp", Disp);

        if (!Visible) pms.SetValue("hide", null, false);
//        if (IsUnique) pms.SetValue("uniq", null, false);
//        if (IsNeeded) pms.SetValue("need", null, false);

        //if (et != EType.Text && et != EType.None) pms.Add("et", et);
        pms.SetValue("Config", Params.get("Config"));

        return pms.isEmpty() ? Name : gs.leftPad(Name + ":", 20) + pms.toString();
    }
}
