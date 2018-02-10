package com.hc;

import hylib.toolkits.gv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ListBhAdapter extends BaseAdapter {
	private List<HashMap<String, String>> listItems;
	private LayoutInflater listContainer;

	public final class ListItemView { // 自定义控件集合
		public TextView tvInfo;
		public Button btnAdd;
		public Button btnSub;
		public TextView tvNum;
		public int pos;
	}

	public ListBhAdapter(Context context, List<HashMap<String, String>> listItems) {
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
	}

	public int getCount() {
		return listItems.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	private String product_info = "";
	private void AddInfo(Map<String, String> map, String key, String name) {
		String val = map.get(key);
		if(val == "" || val == null) return;
		String c = product_info.equals("") ? "" : "<br>";
		if(name.length() == 0) name = key;
		product_info += c + String.format("%s: %s", name, val); 
	}
	
	private View.OnClickListener btnNumClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			ListItemView listItemView = (ListItemView) v.getTag();
			Map<String, String> map = listItems.get(listItemView.pos);
			int n = gv.IntVal(map.get("num"));
			if(v.getId() == R.id.btn_add) {
				n++;
			} else if (v.getId() == R.id.btn_sub) {
				n--;
			}
			if(n < 0) n = 0;
			map.put("num", String.valueOf(n));
			listItemView.tvNum.setText(map.get("num"));
		}
	};
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView listItemView;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = listContainer.inflate(R.layout.list_bh_item, null);

			listItemView.tvInfo = (TextView) convertView.findViewById(R.id.item_info);
			listItemView.btnAdd = (Button) convertView.findViewById(R.id.btn_add);
			listItemView.btnSub = (Button) convertView.findViewById(R.id.btn_sub);
			listItemView.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		
		listItemView.pos = position;
		
		listItemView.btnAdd.setTag(listItemView);
		listItemView.btnSub.setTag(listItemView);
		
		product_info = "";
		Map<String, String> map = listItems.get(position);

		//tvInfo.setMovementMethod(ScrollingMovementMethod.getInstance());//滚动  
		//listItemView.tvInfo.setText(map.get("item_info"));
		
		//AddInfo(map, "uid", "患者编号");
		AddInfo(map, "FName", "产品名称");
		AddInfo(map, "FNumber", "产品编号");
		AddInfo(map, "FModel", "规格型号");
		AddInfo(map, "FQty", "库存数量");
		String color="#000000";
		int resid;

		listItemView.tvNum.setText(map.get("num"));
		
		//listItemView.ivItem.setBackgroundResource(resid);
		
		String item_info = String.format("<font color='%s'>%s</font>", 
										color, product_info);  

	//	listItemView.tvInfo.setText(Html.fromHtml(item_info)); 
		listItemView.tvInfo.setText(Html.fromHtml(product_info));    
		

		listItemView.btnAdd.setOnClickListener(btnNumClickListener);
		listItemView.btnSub.setOnClickListener(btnNumClickListener);

		return convertView;
	}
}
