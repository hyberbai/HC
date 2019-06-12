package hylib.util;

import java.util.ArrayList;

import hylib.toolkits.EventHandleListener;
import hylib.toolkits.gs;

public class ActionList extends ArrayList<ActionInfo>
{
	private static final long serialVersionUID = 1L;
	private static ActionList mIntance;
	
	public EventHandleListener onPropChangedListener;
	
    public ActionList()
    {
    }

    public ActionList(ActionInfo... items)
    {
    	for (ActionInfo item : items)
			add(item);
    }

    public ActionList(ActionInfo[] items, Action action)
    {
    	for (ActionInfo item : items) {
    		item.Action = action;
			add(item);
    	}
    }
    
    public ActionInfo get(String name)
    {
    	for (ActionInfo item : this) {
    		if(item.Name.equalsIgnoreCase(name)) return item;
    	}
    	return null;
    }

    @Override
    public boolean add(ActionInfo act) {
        act.Owner = this;
    	return super.add(act);
    }

    public ActionInfo add(String title, String name, int resID)
    {
    	ActionInfo item = new ActionInfo(name, title, resID);
    	add(item);
        return item;
    }

    public void setVisible(String names, boolean visible)
    {
    	for (String name : gs.Split(names)) {
        	ActionInfo act = get(name);
        	if(act != null) act.Visible = visible;
		}
    }

	public void Execute(String name) {
		ActionInfo act = get(name);
		if(act != null) act.Execute();
	}
    
    
    
    /*************************** 静态实例调用  **********************************/

	public static boolean isInstEmpty() {
		return mIntance == null;
	}

	public  static void Init(){
		 mIntance = new ActionList();
	}

    public static void Add(String title, String name, int resID) {
    	mIntance.add(title, name, resID);
	}
    
    public static void Add(String title, String name) {
    	mIntance.add(title, name, 0);
	}
    
    public static ActionInfo getAction(String name, ActionList outACL) {
    	if(outACL == null) return mIntance.get(name);
    	
    	ActionInfo act = outACL.get(name);
    	if(act == null) {
    		act = mIntance.get(name);
    		if(act != null)
    			outACL.add((ActionInfo)(act.clone()));
    	}
    	return act;
	}

    public static ActionInfo getAction(String name) {
    	return getAction(name, null);
    }
}