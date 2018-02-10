package hylib.util;

import hylib.toolkits.gi;
import hylib.toolkits.gs;
import hylib.toolkits.gv;

import java.util.ArrayList;
import java.util.List;

import android.database.CursorJoiner.Result;

// 计数统计处理类
public class CountStat {
	public final static int CST_CONST = 3;		// 常量，不参与计数
	public final static int CST_NORMAL = 0;		// 一般计数项，减少剩余项
	public final static int CST_INC = 1;		// 增量计数项，增加合计
	public final static int CST_EXTRA = 5;		// 多出项，不增减剩余及合计
	public final static int CST_REST = 8;		// 剩余项
	public final static int CST_TOTAL = 9;		// 合计项
	
	public boolean LockChange; 
	public StatItem Total;
	public StatItem Rest;
	public List<StatItem> StatItems;
	public gi.Listener ChangedListener;
	
	public CountStat(int total){
		StatItems = new ArrayList<StatItem>();
		Total = new StatItem(CST_TOTAL, "合计", CST_TOTAL);
		Rest = new StatItem(CST_REST, "剩余", CST_REST);
		SetTotal(total);
	}

	public CountStat(){
		this(0);
	}
	
	public class StatItem {
		public int Key;
		public String Name;
		public int Type;
		public int Count;
		public StatItem(int key, String name, int type) {
			Key = key;
			Name = name;
			Type = type;
		}
	}
	
	public void DoChanged(){
		if(LockChange) return;
		if(ChangedListener == null) return;
		ChangedListener.Listen(this);
	}
	
	public StatItem AddStatItem(int key, String name, int type) {
		StatItem item = new StatItem(key, name, type);
		StatItems.add(item);
		return item;
	}
	
	public StatItem AddStatItem(int key, String name) {
		return AddStatItem(key, name, CST_NORMAL);
	}
	
	public StatItem FindItem(int key){
		for (StatItem statItem : StatItems)
			if(statItem.Key == key) return statItem;
		return null;
	}
	
	public int getItemCount(int key){
		StatItem item = FindItem(key);
		return item == null ? 0 : item.Count;
	}

	public void setItemCount(int key, int count){
		StatItem item = FindItem(key);
		if(item != null) item.Count = count;
	}

	public int getRestCount(){
		return Rest.Count;
	}

	public int getTotalCount(){
		return Total.Count;
	}

	public void Clear(){
		SetTotal(0);
	}
	
	public void SetTotal(int total){
		Total.Count = total;
		Rest.Count = total;
		ClearItems();
	}
	
	public void ClearItems(){
		for (StatItem item : StatItems)
			item.Count = 0;
		DoChanged();
	}
	
	// 计数累加
	public void Inc(int key, int diff){
		if(key == 0 || diff == 0) return;
		StatItem item = FindItem(key);
		if(item == null) return;
		if(item.Type == CST_CONST) return;
		item.Count += diff;
		if(item.Type == CST_NORMAL)
			Rest.Count -= diff;
		else if (item.Type == CST_INC)
			Total.Count += diff;
		DoChanged();
	}

	// 计数加一
	public void Inc(int key){
		Inc(key, 1);
	}

	// 设置计数值
	public void SetItemCount(int key, int count){
		StatItem item = FindItem(key);
		if(item == null) return;
		Inc(key, count - item.Count);
	}

	// 计数项变更
	public void ChangeKey(int key0, int key1){
		Inc(key0, -1);
		Inc(key1, 1);
	}
	
	public String GetItemDesc(StatItem item){
		return item.Name + "：" + item.Count;
	}
	
	public String getItemsDesc() {
		String desc = gs.JoinArray(StatItems.toArray(new StatItem[0]), "，", new gi.IFunc1<StatItem, String>(){ 
			public String Call(StatItem item) { return GetItemDesc(item); } 
		});
		return desc;
	}

	public String getItemsAndRestDesc() {
		return gs.Connect(getItemsDesc(), GetItemDesc(Rest), "，");
	}
	
	public String getTotalDesc() {
		return GetItemDesc(Rest) + "，" + GetItemDesc(Total);
	}

	// 获取统计说明
	public String getDesc() {
		return String.format("%s\n%s", getItemsDesc(), getTotalDesc());
	}
	
	// 获取统计说明
	public String getDesc2() {
		return String.format("%s，%s", getItemsDesc(), getTotalDesc());
	}
}