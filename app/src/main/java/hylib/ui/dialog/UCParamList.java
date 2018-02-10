package hylib.ui.dialog;

import hylib.toolkits.type;
import hylib.util.Param;
import hylib.util.ParamList;

public class UCParamList extends ParamList {
	private static final long serialVersionUID = 1L;

	public UCParamList()
    {
		super();
    }

    public UCParamList(Object objParam)
    {
    	super(objParam);
    }

    public ParamList getStyles()  {
    	if(!containsKey("styles")) SetValue("styles", new ParamList());
    	ParamList plStyles = GetParamList("styles");
    	return plStyles;
	}

    public void setStyles(String name, Param... styles)  {
    	ParamList plStyles = getStyles();
    	plStyles.AddRange(styles);
	}
    
    public void setStyles(String name, ParamList styles)  {
    	ParamList plStyles = getStyles();
    	plStyles.AddRange(styles);
	}
    
    public void addStyle(String name, Object... values)  {
    	ParamList plStyles = getStyles();
    	plStyles.SetValue(name, new ParamList(values));
	}

    public Param newStyle(String name, Object... values)  {
    	return new Param(name, new ParamList(values));
    }

    public ParamList findViewParams(int id) {
    	ParamList pl = this;
    	return findViewParams(pl, id);
    }
    
    private ParamList findViewParams(ParamList pl, int id){
    	if(pl.IntValue("id") == id) return pl;
        Object[] os = type.as(pl.get("items"), Object[].class);
        if(os == null) return null;
        for (Object o : os) {
        	Param item = type.as(o, Param.class);
        	if(item == null) continue;
        	if(item.Value instanceof Object[])
        		for (Object oi : (Object[])item.Value) {
        			if(oi instanceof ParamList)
                	{
                		ParamList plSub = findViewParams((ParamList)oi, id);
        	        	if(plSub != null) return plSub;
                	}
				}
        	else if(item.Value instanceof ParamList)
        	{
        		ParamList plSub = findViewParams((ParamList)item.Value, id);
	        	if(plSub != null) return plSub;
        	}
		}
    	return null;
    }
}
