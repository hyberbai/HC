package hylib.util;

import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import hylib.data.DataColumn;
import hylib.data.DataRow;
import hylib.data.DataTable;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.type;



public class ParamList extends HashMap<String, Object>
{
	private static final long serialVersionUID = 4419480374232528418L;

	public ParamList()
    {
    }

    public ParamList(Param... items)
    {
        AddRange(items);
    }

    public ParamList(Object objParam)
    {
    	if(objParam instanceof String) 
    		SetParams((String)objParam);
    	else if(objParam instanceof Param) 
    		SetValue(((Param)objParam).Name, ((Param)objParam).Value);
    	else if(objParam instanceof ParamList) 
    		AddRange(((ParamList)objParam));
    	else if(objParam instanceof Param[]) 
    		AddRange(((Param[])objParam));
    	else if(objParam instanceof Object[]) 
    		AddRange(((Object[])objParam));
    }
    
    public ParamList(String name, Object value)
    {
    	SetValue(name, value, false);
    }

    public void SetParams(String sParams)
    {
    	Object v = Hson.Parse(sParams);
    	if(v instanceof String) 
    		SetValue((String)v, true);
    	if(v instanceof Param) 
    		SetValue(((Param)v).Name, ((Param)v).Value);
    	else
    		AddRange(type.as(v, ParamList.class));
    }
    
    /*
     * 弱设参数，如果参数名已存在不更新
     */
    public void WeakSetParams(Object values) {
		ParamList pl = new ParamList(values);

        for (Entry<String, Object> item : pl.entrySet()) {
        	Object key = item.getKey();
        	if(containsKey(key)) continue;
            SetValue(item.getKey(), item.getValue(), false);
        }		
	}
    
    public ParamList GetParamList(String name)
    {
    	Object o = get(name);
    	if(o instanceof ParamList) return (ParamList)o;
    	return new ParamList(o);
    }

    public <T> T Get(String name, Class<T> tClass)
	{
    	Object v = get(name);
    	return type.as(v, tClass);
	}
    
	public <T> T $(String name) {
    	Object v = get(name);
		return (T)v;
	}
	
    public void set(String name, Object value) 
    {
        super.put(name, value instanceof String ? Hson.Parse((String)value) : value);
    }
	
    public void setArray(String name, Object... values)
    {
        super.put(name, values);
    }
    
    public void writeToInent(Intent intent) {
    	writeToInent(intent, "Params");
	}
    
    public void writeToInent(Intent intent, String key) {
    	intent.putExtra(key, toString());
	}

    public void SetValue(String name, Object value, boolean EmptyRemove)
    {
//        if (value instanceof TriState)
//        {
//            if (value.Equals(TriState.Indeterminate)) return;
//            value = value.Equals(TriState.Checked);
//        }

        if (EmptyRemove && (value == null || value.equals(false)))
            remove(name);
        else
            put(name, value == null || value.equals(true) ? null : value);
    }
    
    public void SetValue(String name)
    {
    	SetValue(name, null, false);
    }
    
    public void SetValue(String name, Object value)
    {
    	SetValue(name, value, true);
    }

    public void weakSetValue(String name, Object value) throws Exception
    {
    	if(!gv.IsEmpty(get(name))) return;
    	SetValue(name, value);
    }
    
    public void SetValidValue(String name, Object value)
    {
		if(gv.IsEmpty(value)) Remove(name); else SetValue(name, value);	
    }

    public Object PathValue(String path)
    {
    	ParamList pms = this;
    	String[] ps = path.split("\\\\|/");
        int i = 0;
        for(String key : ps)
        {
            Object v = pms.get(key.trim());
            if (v == null) return null;
            if (++i >= ps.length) return v;
            pms = type.as(v, ParamList.class);
            if (pms == null) return null;
        }
        return null;
    }
    
    public <T> T PathValue(String path, Class<T> cls)
    {
    	Object v = PathValue(path);
    	return type.as(v, cls);
    }

    public void SetPathValue(String path, Object value)
    {
        ParamList pms = this;
        String[] ps = path.split("\\\\|/");
        int i = 0;
        for(String key : ps)
        {
            if (++i >= ps.length)
            {
                pms.SetValue(key, value);
                return;
            }
            ParamList v = type.as(pms.get(key), ParamList.class);
            if (v == null)
            {
                v = new ParamList();
                pms.SetValue(key, v);
            }
            pms = (ParamList)v;
        }
    }
    
    public int IntValue(String name)
    {
        Object v = get(name);
        return gv.IntVal(v);
//        if (v == null) return 0;
//
//        if (v instanceof String) {
//            v = gv.IntVal((String)v);
//            put(name, v);
//        }
//        return (Integer)v;
    }

	public float FVal(String name) {
		Object o = get(name);
		return gv.FVal(o);
	}

    public void SetValueByDataRowItem(DataRow dr, String name)throws Exception
    {
        SetValue(name, dr.getValue(name), false);
    }
    
    public void AddRange(ParamList list)
    {
        if (list == null) return;
        for (Entry<String, Object> item : list.entrySet())
            SetValue(item.getKey(), item.getValue(), false);
    }

    public void AddRange(Param... items)
    {
        if (items == null) return;
        for(Param item : items) setParam(item);
    }

    public void AddRange(Object[] items) 
    {
        if (items == null) return;
        for(Object item : items)
        	if(item instanceof String) 
        		SetParams((String)item);
        	else if(item instanceof Param) 
        		setParam((Param)item);
    }
    
    public void AddObjects(Object[] os)throws Exception
    {
        if (os == null) return;
        for (Object o : os)
        {
            if (o instanceof Param)
                SetValue(((Param)o).Name, ((Param)o).Value, true);
            else
                AddRange((ParamList)(o instanceof String ? Hson.Parse((String)o) : o));
        }
    }

    public Object[] getValues(String names, char splitChr) {
    	String[] ss = gs.Split(names, String.valueOf(splitChr));
    	Object[] values = new Object[ss.length];
    	for (int i = 0; i < ss.length; i++)
    		values[i] = get(ss[i]);
    	return values;
	}

    public Object[] getValues(String names) {
    	return getValues(names, ',');
	}

    public String DispText(String title, String names)
    {
    	char splitChr = gs.getSplitChar(names);
    	Object value = gs.JoinArray(getValues(names, splitChr), splitChr);
        if (gv.IsEmpty(value)) return null;
        return title + gv.StrVal(value);
    }
    
    public String SValue(String name)
    {
        return gv.StrVal(get(name));
    }

    public String getValue(String name, String defaultValue)
    {
        return containsKey(name) ? SValue(name) : defaultValue;
    }
    
    public int getValue(String name, int defaultValue) 
    {
        return containsKey(name) ? IntValue(name) : defaultValue;
    }

    public boolean getValue(String name, boolean defaultValue) 
    {
        return containsKey(name) ? BValue(name) : defaultValue;
    }
    
	public EventHandleListener getEvent(String name) {
    	return type.as(get(name), EventHandleListener.class);
	}
	
    public boolean BValue(String name)
    {
    	if(!this.containsKey(name)) return false;
        Object v = get(name);
        return v == null ? true : gv.BoolVal(v);
    }
	
    // 阅后即焚
    public String GetOnce(String name)
    {
        String v = SValue(name);
        remove(name);
        return v;
    }

    public boolean GetOnceBool(String name)
    {
    	boolean v = BValue(name);
        remove(name);
        return v;
    }

    public Object[] getArray(String name)
    {
        return type.as(get(name), Object[].class);
    }

    public <T extends Enum<T>> T GetOnceEnum(Class<T> enumType, String name)throws Exception
    {
    	T v = EnumValue(enumType, name);
    	remove(name);
        return v;
    }
    
    public <T extends Enum<T>> T EnumValue(Class<T> enumType, String name)throws Exception
    {
        Object o = get(name);
        if (o == null) return null;
        if (enumType.isInstance(o)) return (T)o;

        T e = Enum.valueOf(enumType, (String)o);
        put(name, e);
        return e;
    }

    public String SerializeValue(String name)
    {
        return gv.Serialize(get(name));
    }

//    public T[] ArrayValue<T>(String name)
//    {
//        Object v = get(name] as Object[];
//        if (v == null) return null;
//        return ArrayValue<T>((Object[])v);
//    }
//
//    public T[] ArrayValue<T>(Object[] os)
//    {
//        if (os == null) return null;
//        var arr = new T[os.Length];
//        for (int i = 0; i < os.Length; i++)
//            arr[i] = (T)Convert.ChangeType(os[i], typeof(T));
//        return arr;
//    }

    public int[] IntArrayValue(String name)throws Exception
    {
        Object v = get(name);
        if (v == null) return null;
        if (v instanceof int[]) return ((int[])v);

        if (v instanceof String)
        {
            String[] ss = ((String)v).split(",");
            int[] ia = new int[ss.length];
            int i = 0;
            for (String s : ss)
                ia[i++] = gv.IntVal(s);
            v = ia;
            put(name, v);
        }
        else if (v instanceof Object[])
        {
        	int[] ia = new int[((Object[])v).length];
        	int i = 0;
        	for(Object o : (Object[])v)
        		ia[i++] = gv.IntVal(o.toString());
            return ia;
        }
        return (int[])v;
    }

    public String[] SArrayValue(String name, String SplitStr) throws Exception
    {
        Object v = get(name);
        if (v == null) return null;
        if (v instanceof String)
        {
            String[] ss = ((String)v).split(SplitStr);
            int i = 0;
            for (String s : ss)
                ss[i++] = gs.RemoveQuotes(s, '[', ']');
            v = ss;
            put(name, v);
        }
        return (String[])v;
    }

    public ParamList ParamListValue(String name)throws Exception
    {
        Object v = get(name);
        v = type.as((v instanceof String ? Hson.Parse((String)v) : v), ParamList.class);
        if (v == null)
        {
            v = new ParamList();
            put(name, v);
        }
        return (ParamList)v;
    }
//
//    public void GetParam(ref Object o, String name)
//    {
//        if (o instanceof Int32)
//            o = IntValue(name);
//        else if (o instanceof Point)
//        {
//            int[] ia = IntArrayValue(name);
//            o = new Point(ia[0], ia[1]);
//        }
//        else if (o instanceof Size)
//        {
//            int[] ia = IntArrayValue(name);
//            o = new Size(ia[0], ia[1]);
//        }
//    }
    
    // 先将value 序列化再做为参数值
    public void SetSerializeValue(String name, Object value) throws Exception
    {
        this.SetValue(name, "%%" + gv.SerializeWithType(value), false);
    }

    /// <summary>
    /// 获取反序列化的参数值
    /// </summary>
    public Object GetDeserializeValue(String name) throws Exception
    {
    	Object o = this.get(name);
        if (!(o instanceof String)) return o;
        String s = (String)o;
        if (!gs.Sub(s, 0, 2).equals("%%")) return o;
        return gv.DeserializeWithType(s.substring(2));
    }
    
    @Override
    public String toString()
    {
        return gv.SerializeMap(this, ":", ",");
    }

    public Param getParam(String name)
    {
    	return new Param(name, get(name));
    }

    public void setParam(Param param)
    {
    	if(param == null) return;
        SetValue(param.Name, param.Value, false);
    }
    
    public Param[] getParams(String paramNames)
    {
    	List<Param> list = new ArrayList<Param>();
        for (String key : gs.Split(paramNames))
            if(containsKey(key)) list.add(getParam(key));
        return list.toArray(new Param[0]);
    }
    
    public Param[] toArray()
    {
    	Param[] pms = new Param[this.keySet().size()];
    	int i = 0;
        for (String key : this.keySet())
            pms[i++] = getParam(key);

        return pms;
    }

    public void Call(String name, Object sender, ParamList pl) throws Exception
    {
    	EventHandleListener e = type.as(get(name), EventHandleListener.class);
    	if(e != null) e.Handle(sender, pl);
    }

    public void Remove(String sItems)
	{
    	for (String item : gs.Split(sItems, ","))
			remove(item);
	}

    public void RemoveEmpties()
	{
        Iterator<?> iter = entrySet().iterator();
        while (iter.hasNext()) {
	        Entry<?, ?> entry = (Entry<?, ?>) iter.next();
	        String key = (String)entry.getKey();
	        Object val = entry.getValue();
	        if(gv.IsEmpty(val)) iter.remove();
        }
	}
    
    public void setDataRow(DataRow dr, boolean createColumn) {
    	DataTable dt = dr.getTable();
        Iterator<?> iter = entrySet().iterator();
        while (iter.hasNext()) {
	        Entry<?, ?> entry = (Entry<?, ?>) iter.next();
	        String key = (String)entry.getKey();
	        Object val = entry.getValue();
	        DataColumn dc = dt.getColumn(key);
	        
            if(dc == null) 
            	if(createColumn) dc = dt.addColumn(key, 0); else continue;
            
            dr.setValue(dc, val);
        }
	}

    public String getItemsText(String items, String pmStr, String joinStr) throws Exception {
    	List<String> list = new ArrayList<String>();

		for (String s: gs.Split(items, ",")) {
			gs.CutResult r = gs.Cut(s, " ", gs.SC_EmptyToS1 | gs.SC_Trim);
			list.add(Param.SVal(r.S2, get(r.S1), pmStr));
		}
    	return gs.JoinList(list, joinStr);
	}


    public void updateDataRow(DataRow dr) throws Exception {
    	DataTable dt = dr.getTable();
        Iterator<?> iter = entrySet().iterator();
        while (iter.hasNext()) {
	        Entry<?, ?> entry = (Entry<?, ?>) iter.next();
	        String key = (String)entry.getKey();
	        Object val = entry.getValue();
	        DataColumn dc = dt.getColumn(key);
	        if(dc == null) continue;
            dr.updateValue(dc, val);
        }
	}
    
    public void addRowToDataTable(DataTable dt) {
        DataRow dr = dt.newRow();
        setDataRow(dr, true);
        dt.addRow(dr);
	}
	
	public void RenKey(String oldKey, String newKey) {
		if(!containsKey(oldKey)) return;
		put(newKey, get(oldKey));
		remove(oldKey);
	}
	
	public void RenKeys(String items) {
		for (String s: gs.Split(items, ",")) {
			gs.CutResult r = gs.Cut(s, " ");
			RenKey(r.S1, r.S2);
		}
	}
	
	private void copyParams(String items, ParamList plTo, boolean isMove) {
		boolean all = items.equals("all");
		if(all) {
	        Iterator<?> iter = entrySet().iterator();
	        while (iter.hasNext()) {
		        Entry<?, ?> entry = (Entry<?, ?>) iter.next();
		        String key = (String)entry.getKey();
		        Object val = entry.getValue();
		        plTo.SetValue(key, val);
		        if(isMove) remove(key);
	        }
		}
		else
			for (String key: gs.Split(items, ",")) 
				if(containsKey(key)) plTo.SetValue(key, isMove ? GetOnce(key) : get(key), false);
	}

	public void CopyTo(ParamList plTo) {
		copyParams("all", plTo, false);
	}

	public void CopyTo(String items, ParamList plTo) {
		copyParams(items, plTo, false);
	}
	
	public void MoveTo(String items, ParamList plTo) {
		copyParams(items, plTo, true);
	}
	
//    public String ToXml1()
//    {
//        XmlDocument xd = xu.GetFieldXmlDocument("", true);
//        for(Param p : this) xu.SetXmlElementValue(xd, p.Key, p.Value, false);
//        return xu.GetXmlDocumentText(xd);
//    }

//    public ParamList Clone()
//    {
//    	ParamList NewList = new ParamList();
//        for(Map.Entry<String, Object> item : this.entrySet())
//            NewList.SetValue(item.getKey(), gv.Clone(item.getValue(), false));
//
//        return NewList;
//    }

//    public void CallAction(String name, Object sender, EventExArgs ea = null)
//    {
//        var Call = get(name) as EventExHandler;
//        if (Call != null) Call(sender, ea);
//    }
}