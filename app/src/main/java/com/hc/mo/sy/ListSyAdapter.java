package com.hc.mo.sy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hc.R;
import com.hc.R.drawable;
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

public class ListSyAdapter extends BaseAdapter {
	private List<HashMap<String, String>> listItems;
	private LayoutInflater listContainer;

	public final class ListItemView { // 自定义控件集合
		public TextView tvInfo;
		public TextView tvTime;
		public ImageView ivItem;
	}

	public ListSyAdapter(Context context, List<HashMap<String, String>> listItems) {
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

	public View getView(int position, View convertView, ViewGroup parent) {

		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = listContainer.inflate(R.layout.list_sy_item, null);

			listItemView.tvTime = (TextView) convertView.findViewById(R.id.item_time);
			listItemView.tvInfo = (TextView) convertView.findViewById(R.id.item_info);
			listItemView.ivItem = (ImageView) convertView.findViewById(R.id.iv_item);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		
		Map<String, String> map = new HashMap<String, String>();
		map = listItems.get(position);
		listItemView.tvTime.setText(map.get("item_time"));

		//tvInfo.setMovementMethod(ScrollingMovementMethod.getInstance());//滚动  
		//listItemView.tvInfo.setText(map.get("item_info"));
		String op = map.get("op");
		String operator = map.get("operator");
		String desc = map.get("desc");
		String color;
		int resid;
		
		if(op.indexOf("待") == 0) {
			color = "#808080";
			resid = R.drawable.tl_gray2;			
		} else if(op.indexOf("删") >= 0 || op.indexOf("亏") >= 0) {
			color = "#ff3030";
			resid = R.drawable.tl_red2;
		} else if(op.indexOf("返货") >= 0 || op.indexOf("退") >= 0) {
			color = "#bbbb30";
			resid = R.drawable.tl_yellow2;
		} else if(op.indexOf("换") >= 0) {
			color = "#00ddcc";
			resid = R.drawable.tl_green2;
		} else {
			color = "#00bbaa";
			resid = R.drawable.tl_green2;
		}

		listItemView.ivItem.setBackgroundResource(resid);
		
		String item_info = String.format("<font color='%s'>%s" +
										"%s</font>" +
										"<font color='%s'>%s</font></font>", 
										color, op, 
										operator.equals("") ? "" : "<br>操作员：" + operator, 
										color, 
										desc.equals("") ? "" : "<br>" + desc);  

		listItemView.tvInfo.setText(Html.fromHtml(item_info));     
		
		listItemView.tvInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});

		return convertView;
	}
}
