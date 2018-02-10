package hylib.edit;

import hylib.data.DataColumn;
import hylib.data.DataRow;
import hylib.data.DataTable;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.util.ParamList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import android.R.bool;
import android.R.integer;
import android.app.Activity;

public class EditFieldList extends ArrayList<EditField> {
	private static final long serialVersionUID = 1L;
	
	public EditField findItem(int id) {
		for (EditField item : this)
			if(item.Id == id) return item;
		return null;
	}

	public EditField findItem(String name) {
		for (EditField item : this)
			if(item.Name.equalsIgnoreCase(name)) return item;
		return null;
	}
	
	public EditField get(int id) {
		return findItem(id);
	}

	public Object getValue(int id) {
		return get(id).Value;
	}

	public void setValue(int id, Object value) {
		findItem(id).setValue(value);
	}

	public void setValue(String name, Object value) {
		findItem(name).setValue(value);
	}


	public void setSafeValue(int id, Object value) {
		EditField ef = findItem(id);
		if(ef != null) ef.setValue(value);
	}

	public void setSafeValue(String name, Object value) {
		EditField ef = findItem(name);
		if(ef != null) ef.setValue(value);
	}
	
	public void readParamList(ParamList pl) {
        Iterator<?> iter = pl.entrySet().iterator();
        while (iter.hasNext()) {
	        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();
	        String key = (String)entry.getKey();
	        Object val = entry.getValue();
	        setSafeValue(key, val);
        }
	}
	
	public String SValue(int id) {
		return gv.StrVal(getValue(id));
	}
	
	public void addRange(EditField... items) {
		for (EditField item : items) {
			add(item);
		}
	}
	
	public void clearValues(int... ids) {
		for (int id : ids)
		{
			EditField ef = findItem(id);
			if(ef != null) ef.setValue(null);
		}
	}
	
	public void setItemChange(int id, boolean changed){
		EditField item = findItem(id);
		if(item != null) item.Changed = changed;
	}

	public boolean getItemChange(int id){
		EditField item = findItem(id);
		return item != null && item.Changed;
	}
	
	public boolean isChanged() {
		for (EditField ef: this)
			if(ef.Changed) return true;
		return false;
	}
	
	public void setGroup(int gid, int... efIds) {
		for (int id : efIds) {
			EditField ef = findItem(id);
			if(ef != null) ef.GroupId = gid;
		}
	}
	
	public void setGroupChanged(int gid, boolean changed) {
		for (EditField ef : this)
			if(ef.GroupId == gid) ef.Changed = changed;
	}
	
	public void BindingViews(Activity act) {
		for (EditField item : this)
			item.view = act.findViewById(item.Id);
	}

	public void writeToParamList(ParamList pl) {
		for (EditField ef : this)
			if(ef.hasValue()) pl.SetValue(ef.Name, ef.Value);
	}
	
}
