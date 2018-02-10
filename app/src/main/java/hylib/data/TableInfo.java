package hylib.data;

import hylib.toolkits._D;
import hylib.toolkits.gs;
import hylib.toolkits.type;
import hylib.util.Param;
import hylib.util.ParamList;

public class TableInfo {
    public int TCID;
    public int TID;
 //   public TableProp TProp;

    public String Name;
    public String FullName;
    public String TName;
    public String TCnName;
    public String TDisp;
    public TableField PK;   // 主键

    public TableFieldList Fields;
    public ParamList Params;
    
    public TableInfo() {
		
	}
    
    public boolean TNameIs(String name) {
		return TName.equalsIgnoreCase(name);
	}
    
    public TableInfo(String config) {
		LoadParams(new ParamList(config));
	}

    public void LoadParams(ParamList pms)
    {
    	ParamList pmsTable = pms.GetParamList("table");
        TID = pmsTable.IntValue("TID");
        TCID = pmsTable.IntValue("TCID");
        Name = pmsTable.SValue("Name");
        TName = pmsTable.SValue("TName");
        TCnName = pmsTable.SValue("TCnName");
        if(TName.isEmpty()) TName = Name;

        Fields = new TableFieldList();

        Params = new ParamList(pmsTable.get("pms"));
        //ParseJoins(pmsTable["Joins"] as object[]);

        Object[] pmsFields = pms.Get("fields", Object[].class);
        LoadFields(pmsFields);
    }

    public void LoadFields(Object[] pmsFields)
    {
        if (TID == 2501)
            _D.Dumb();
        if (pmsFields == null) return;
        Fields.clear();
        for(Object item : pmsFields)
        {
            TableField Field = null;
            if (item instanceof String)
                Field = new TableField((String)item, (ParamList)null);
            else if (item instanceof Param)
            {
            	Param pm = (Param)item;
                Field = new TableField(pm.Name, type.as(pm.Value, ParamList.class));
            }

            if (Field == null) continue;
            AddField(Field);
        }
    }

    public void AddField(TableField field)
    {
        field.Table = this;
        if (field.IsPK) PK = field;
        //Field.Visible = true;
        Fields.set(field);
    }

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();

        // => 表信息
        sb.append("table: { " + 
                gs.JoinArray(new String[] {
                    Param.SValEx("Name: ", Name),
                    Param.SValEx("TID: ", TID),
                    Param.SValEx("TCID: ", TCID),
                    Param.SValEx("TName: ", TName),
                    Param.SValEx("TCnName: ", TCnName),
                }, ",") + " },\n");

        sb.append("fields: [\n");

        // 字段配置信息
        sb.append(Fields.toString());

        sb.append("]\n");

        return sb.toString();
	}
	
	public String getSelectSql() {
		String sql = "select " + Fields.getVisibleFieldsText() + " from " + TName + 
					(TCID > 0 ? " where CID=" + TCID : "");
		return sql;
	}
}
