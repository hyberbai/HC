package com.hc.mo.bill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hc.R;
import com.hc.g;
import com.hc.R.id;
import com.hc.R.layout;

import android.content.Context;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListSaleHisAdapter extends BaseAdapter {
	private List<HashMap<String, Object>> listItems;
	private LayoutInflater listContainer;

	public final class ListItemView { // 自定义控件集合
		public TextView tvInfo;
		public ImageView ivItem;
		public int pos;
	}

	public ListSaleHisAdapter(Context context, List<HashMap<String, Object>> listItems) {
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
	private void AddInfo(Map<String, Object> map, String key, String name) {
		Object val = map.get(key);
		if(val == "" || val == null) return;
		String c = product_info.equals("") ? "" : "<br>";
		if(name.length() == 0) name = key;
		product_info += c + String.format("%s: %s", name, val); 
	}
	
	private BaseAdapter adp = this;
	private ListItemView listItemView = null;
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = listContainer.inflate(R.layout.list_sale_his_item, null);

			listItemView.tvInfo = (TextView) convertView.findViewById(R.id.item_info);
			listItemView.ivItem = (ImageView) convertView.findViewById(R.id.iv_item);
			listItemView.ivItem.setTag(position);
			listItemView.pos = position;
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		product_info = "";
		Map<String, Object> map = new HashMap<String, Object>();
		map = listItems.get(position);

		//tvInfo.setMovementMethod(ScrollingMovementMethod.getInstance());//滚动  
		//listItemView.tvInfo.setText(map.get("item_info"));
		
		//AddInfo(map, "uid", "患者编号");
		AddInfo(map, "unames", "患者");
		AddInfo(map, "dt", "出库时间");
		AddInfo(map, "num", "数量");
		String color="#000000";
		int resid;

		//listItemView.ivItem.setBackgroundResource(resid);
		
		String item_info = String.format("<font color='%s'>%s</font>", 
										color, product_info);  

	//	listItemView.tvInfo.setText(Html.fromHtml(item_info)); 
		listItemView.tvInfo.setText(Html.fromHtml(product_info));    
		
		listItemView.tvInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});

		listItemView.ivItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int pos=(Integer)(v.getTag());
				listItems.remove(pos);
				g.Vibrate(v.getContext(), 80);
				adp.notifyDataSetChanged();
			}
		});

		return convertView;
	}
}
