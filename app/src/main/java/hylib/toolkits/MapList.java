package hylib.toolkits;

import hylib.data.TableField;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MapList<T> extends ArrayList<T> {
	private HashMap<String, Object> map;
	
	public MapList() {
		map = new HashMap<String, Object>();
	}
	
	protected abstract String getKey(T value);

	@Override
	public boolean add(T item){
		if(item == null) return false;

		String key = getKey(item);
		T exist = get(key);
		if(exist != null) ExProc.ThrowMsgEx(key + "数据项已在列表中，不能重复添加！");
		map.put(key.toLowerCase(), item);
		return super.add(item);
	}

	public T get(String key){
		return (T)map.get(key.toLowerCase());
	}

	public int indexOf(String key){
		T item = get(key);
		return item == null ? -1 : indexOf(item);
	}
	
	public void set(T item){
		if(item == null) return;

		String key = getKey(item);
		int index = indexOf(key);
		if(index < 0) 
			add(item);
		else
			this.set(index, item);
	}
}
