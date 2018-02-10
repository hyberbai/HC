package com.hc.mo.bill;

import hylib.data.DataRow;
import hylib.data.DataTable;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits._D;
import hylib.toolkits.gs;
import hylib.toolkits.gu;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.ui.dialog.ListItemViewInfo;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.widget.HyListAdapter;
import hylib.widget.HyListView;
import hylib.widget.HyTextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hc.R;
import com.hc.R.id;
import com.hc.R.layout;
import com.hc.dal.Bill;
import com.hc.db.DBLocal;
import com.hc.mo.pd.ListPd2Adapter.PDListItemViewInfo;

import android.R.raw;
import android.R.raw;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ListT1Adapter extends HyListAdapter {

	public final class ListItemView extends ListItemViewInfo { // 自定义控件集合
		public TextView tvInfo;
		public TextView tvPriceInfo;
		public ImageView ivItem;
		public HyListView lvReps;
		
		public ListItemView(View view){
			super(view);
		}
	}

	public ListT1Adapter(Context context, List<?> listItems) {
		super(context, listItems, R.layout.list_sale_item);
	}

	@Override
	public ListItemViewInfo NewItemView(View view) {
		ListItemView vi  = new ListItemView(view);
		vi.tvInfo = (TextView) vi.getView(R.id.item_info);
		vi.tvPriceInfo = (TextView) vi.getView(R.id.item_price_info);
		vi.ivItem = (ImageView) vi.getView(R.id.iv_item);
		
		vi.lvReps = (HyListView) vi.getView(R.id.lv_reps);
		vi.lvReps.setFocusable(false);
		vi.lvReps.setClickable(true);
		vi.lvReps.setBackgroundResource(R.drawable.search_bg);
		return vi;
	}
	
	@Override
	public void ShowView(ListItemViewInfo vInfo, View convertView, ViewGroup parent) {
		String product_info = "";
		
		DataRow dr = type.as(getItem(vInfo.index), DataRow.class);

		//tvInfo.setMovementMethod(ScrollingMovementMethod.getInstance());//滚动  

		ListItemView vi = (ListItemView)vInfo;

		// 显示替换产品子列表
		ListView lv = vi.lvReps;
		try {
			Object ext = dr.getValue("Ext");
    		ParamList plExt = ext instanceof ParamList ? (ParamList)ext : new ParamList((String)ext);
    		
    		// 产品信息
    		String SNo = dr.getStrVal("SNo");
    		if(!gv.IsEmpty(SNo)) SNo = "No." + SNo;
    		
    		product_info = gs.JoinArray(new String[] {
    				SNo,
    				//"产品编码: " + dr.getValue("FNumber"),
    				dr.getStrVal("FName"),
    				Param.SVal("规格型号: ", dr.getValue("FModel")),
    				Param.SVal("产品批号: ", dr.getValue("BatchNo")),
    				Param.SVal("有效期至: ", dr.getValue("FPeriodDate")),
    				Param.SVal("生产厂家: ", dr.getValue("Manuf")),
    				Param.SVal("备　注:　", plExt.get("FNote")),
    		}, "<br>");

    		vi.tvInfo.setText(Html.fromHtml(product_info));    

    		// 价格信息
    	       // msp = new SpannableString("字体测试字体大小一半两倍前景色背景色正常粗体斜体粗斜体下划线删除线x1x2电话邮件网站短信彩信地图X轴综合/bot");   
    		SpannableStringHelper sh = new SpannableStringHelper();
    		
    		float price = dr.getFVal("Price");
    		
    		sh.appendMoney(price, Color.RED, 1.0f);
//    		sh.appendLine();

//    		int qty = dr.$("Qty");
//    		sh.startSpan();
//    		sh.appendLine("x " + qty);
//    		sh.setForeColor(Color.GRAY);
//    		sh.setFontSize(0.9f);
//    		
//    		sh.appendMoney(price * qty, Color.BLACK, 1.0f);

    		int clBG = Color.WHITE;
    		DrawBG(vInfo, convertView, clBG);
    		
    		vi.tvPriceInfo.setText(sh);
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		// 替换产品信息
        	DataTable dt = ProductRelpace.getProductReplaceTable(plExt.get("reps"));
        	
			final HyListAdapter listAdapter = new HyListAdapter(context, dt.getRows()); 

			listAdapter.getViewListener = new HyListAdapter.OnGetViewListener() {

				@Override
				public View onGet(int position, View convertView, ViewGroup parent) {
					DataRow dr = type.as(listAdapter.getItem(position), DataRow.class);
					if (convertView == null) {
						LinearLayout.LayoutParams lp;
						
				        int padding = gu.dp2px(context, 3);
						LinearLayout ll = new LinearLayout(context);
						lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,  LayoutParams.WRAP_CONTENT);
						ll.setLayoutParams(lp);
						ll.setPadding(padding, 0, padding, 0);
					    //ll.setBackgroundColor(0xFFCCCCFF);
				        ll.setOrientation(LinearLayout.HORIZONTAL);
				        ll.setPadding(padding*3, padding, padding*2, padding);
				        
						TextView tvInfo = new TextView(context);
						
						tvInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.6f);
						lp = new LinearLayout.LayoutParams(0,  LayoutParams.WRAP_CONTENT);
						lp.gravity = Gravity.CENTER_VERTICAL;
						lp.weight = 1.0f;
						tvInfo.setLayoutParams(lp);
						tvInfo.setTextColor(0xFF2255CC);
						tvInfo.setGravity(Gravity.CENTER_VERTICAL);
						tvInfo.setId(100);
						ll.addView(tvInfo);

						TextView tvPrice = new TextView(context);
						lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,  LayoutParams.WRAP_CONTENT);
						lp.gravity = Gravity.RIGHT;
						tvPrice.setLayoutParams(lp);
						tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
						tvPrice.setGravity(Gravity.CENTER_VERTICAL);
						tvPrice.setId(120);
						ll.addView(tvPrice);

						convertView = ll;
					}

					TextView tvInfo = type.as(convertView.findViewById(100), TextView.class);
					tvInfo.setText(Bill.getBillItemInfo(dr, 0));

					TextView tvPrice = type.as(convertView.findViewById(120), TextView.class);
					tvPrice.setText(Bill.getBillPriceInfo(dr));

					return convertView;
				}
			};

//			int totalHeight = 0;
//			for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
//	            View listItem = listAdapter.getView(i, null, lv);
//	            listItem.measure(0, 0);
//	            totalHeight += listItem.getMeasuredHeight();
//	        }
//	        ViewGroup.LayoutParams params = lv.getLayoutParams();
//	        params.height = totalHeight+ (lv.getDividerHeight() * (listAdapter.getCount() - 1));
//	        lv.setLayoutParams(params);
	        
	        lv.setAdapter(listAdapter);
	        lv.setVisibility(dt.getRowCount() > 0 ? View.VISIBLE : View.GONE);
		} catch (Exception e) {
			_D.Out(e);
		}
//		vi.ivItem.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				int pos=(Integer)(v.getTag());
//				removeItem(pos);
//				g.Vibrate(v.getContext(), 80);
//				notifyDataSetChanged();
//			}
//		});
	}
	
}
