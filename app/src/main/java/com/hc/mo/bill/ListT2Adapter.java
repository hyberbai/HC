package com.hc.mo.bill;

import hylib.data.DataRow;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits.gs;
import hylib.toolkits.type;
import hylib.ui.dialog.ListItemViewInfo;
import hylib.widget.HyListAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hc.R;
import com.hc.R.id;
import com.hc.R.layout;
import com.hc.mo.pd.ListPd2Adapter.PDListItemViewInfo;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListT2Adapter extends ListT1Adapter {

	public ListT2Adapter(Context context, List<?> listItems) {
		super(context, listItems);
	}

	
	@Override
	public void ShowView(ListItemViewInfo vInfo, View convertView, ViewGroup parent) {
		String product_info = "";
		
		DataRow dr = type.as(getItem(vInfo.index), DataRow.class);

		Object val = dr.getValue("uid");
		if(val != null && !val.equals("")) 
			product_info = String.format("患者: %s %s%s", dr.getValue("uid"), dr.getValue("uname"),
					dr.getValue("doc") != null ? ", 医生: " + dr.getValue("doc") : ""	);
		//tvInfo.setMovementMethod(ScrollingMovementMethod.getInstance());//滚动  

		// 产品信息
		product_info = gs.JoinArray(new String[] {
				"NO. " + dr.getValue("SNo"),
				//"产品编码: " + dr.getValue("FNumber"),
//				"产品名称: " + dr.getValue("FName"),
//				"规格型号: " + dr.getValue("FModel"),
//				"单 价: " + dr.getMoneyVal("Price"),
				dr.getStrVal("FName"),
				"规格型号: " + dr.getStrVal("FModel"),
				"产品批号: " + dr.getStrVal("BatchNo"),
				"有效期至: " + dr.getStrVal("FPeriodDate"),
		}, "<br>");
		ListItemView vi = (ListItemView)vInfo;
		vi.tvInfo.setText(Html.fromHtml(product_info));    

		// 价格信息
	       // msp = new SpannableString("字体测试字体大小一半两倍前景色背景色正常粗体斜体粗斜体下划线删除线x1x2电话邮件网站短信彩信地图X轴综合/bot");   
		SpannableStringHelper sh = new SpannableStringHelper();
		
		float price = dr.getFVal("Price");
		sh.appendMoney(price, Color.RED, 1.0f);

		int clBG = Color.WHITE;
		DrawBG(vInfo, convertView, clBG);
		
		vi.tvPriceInfo.setText(sh);
	}
	
}
