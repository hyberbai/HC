package hylib.util;

import android.content.Context;
import hylib.sys.HyApp;
import hylib.toolkits.ExProc;
import hylib.toolkits.gc;

public class ActionInfo implements Cloneable
{
    public String Name; 
    public String Title;
    //public Keys[] ShortcutKeys;
    public int ResID;
    public boolean Checked; 
    public boolean Visible; 
    public Object Tag; 
    public ActionList Owner;
    public Action Action;
    //  public Control EventHandler Execute;

    public ActionInfo(String name, String title) {
		Name = name;
		Title = title;
		Visible = true;
	}
    
    public ActionInfo(String name, String title, int rid) {
    	this(name, title);
		ResID = rid;
	}
    
    public ActionInfo(String name, String title, int rid, Action action) {
    	this(name, title, rid);
		Action = action;
	}

    public void setProps(Param... props) {
    	for (Param prop : props) {
			if(prop.Name == "title") Title = (String)prop.Value;
			if(prop.Name == "res") ResID = (Integer)prop.Value;
		}
    	DoChanged();
	}
    
    public void DoChanged() {
		if(Owner == null) return;
		if(Owner.onPropChangedListener == null) return;
		try {
			Owner.onPropChangedListener.Handle(this, null);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public ActionInfo clone() {
		try {
			return (ActionInfo)super.clone();
		} catch (Exception e) {
			return null;
		}
	}

	public void Execute() {
		if(Action != null)
			Action.Execute(this);
		else {
			Context context = HyApp.CurrentActivity();
			if(context == null) return;
			gc.ExecMethod(context, context.getClass(), "Act" + Name);
		}
	}
}

