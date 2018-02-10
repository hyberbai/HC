package hylib.util;

import hylib.toolkits.gv;



public class Param {
	public String Name;
	public Object Value;
	
	public Param(String name, Object value) {
		Name = name;
		Value = value;
	}

    @Override
    public String toString()
    {
        return Name + ": " + gv.Serialize(Value);
    }

    public static String SVal(String name, Object value, String cnStr)
    {
        if (gv.IsEmpty(value)) return null;
        return name + cnStr + gv.StrVal(value);
    }
    
    public static String SVal(String name, Object value)
    {
        return SVal(name, value, "");
    }

    public static String SValEx(String name, Object value)
    {
    	if(value.equals(0)) value = null;
    	if(value.equals(0.0f)) value = null;
    	if(value.equals(0.0)) value = null;
        return SVal(name, value);
    }
    
    public static Param[] convertParamArray(Object[] items)
    {
    	Param[] pms = new Param[items.length];
    	for (int i = 0; i < pms.length; i++) {
			pms[i] = (Param)items[i];
		}
        return pms;
    }	
}