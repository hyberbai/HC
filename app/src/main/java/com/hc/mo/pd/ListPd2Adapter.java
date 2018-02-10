package com.hc.mo.pd;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.toolkits.HyColor;
import hylib.toolkits._D;
import hylib.toolkits.gcon;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.ui.dialog.ListItemViewInfo;
import hylib.widget.HyListAdapter;

import java.util.List;

import com.hc.PD;
import com.hc.R;
import com.hc.pconst;
import com.hc.PD.StatQtyInfo;
import com.hc.R.drawable;
import com.hc.R.id;
import com.hc.R.layout;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

// 盘点列表
public class ListPd2Adapter extends HyListAdapter {

	public class PDListItemViewInfo extends ListItemViewInfo {
		public TextView tvInfo;
		public Button btnNote;
		public Button btnDelete;
		public ImageView ivState;	// 盘点状态图标
		public ImageView ivMore;	

		public PDListItemViewInfo(View view){
			super(view);
		}
	}

	public ListPd2Adapter(Context context, List<?> listItems) {
		super(context, listItems, R.layout.list_pd_item);
	}

	@Override
	public ListItemViewInfo NewItemView(View view) {
		PDListItemViewInfo vi  = new PDListItemViewInfo(view);
		vi.tvInfo = (TextView) vi.getView(R.id.item_info);
		vi.ivState = (ImageView) vi.getView(R.id.iv_state);
		return vi;
	}
	
	@Override
	public void ShowView(ListItemViewInfo vInfo, View convertView, ViewGroup parent) {
		DataRow dr = type.as(getItem(vInfo.index), DataRow.class);
		int state = gv.IntVal(dr.getValue("State"));
		int resid = state == gcon.S_NONE ? R.drawable.s_uncheck : 
					state == gcon.S_CHECKED ? R.drawable.s_checked : 
					state == gcon.S_WRONG ? R.drawable.s_wrong : 
					state == gcon.S_EXTRA ? R.drawable.s_new: 
					0;

		int clBG = state == gcon.S_CHECKED ? pconst.CL_CHECKED : 
					state == gcon.S_WRONG ? pconst.CL_WRONG : 
					state == gcon.S_EXTRA ? pconst.CL_NEW : 
					0;
		
		String fs = "<font color='#" + Integer.toHexString(clBG & 0xFFFFFF) + "'>";
		String fsOK = "<font color='#" + Integer.toHexString(0x338833) + "'>";
		String fsUncheck = "<font color='#666666'>";
		String fsWrong = "<font color='#" + Integer.toHexString(pconst.CL_FG_WRONG) + "'>";
		String fsNew = "<font color='#" + Integer.toHexString(pconst.CL_FG_NEW) + "'>";
		String fe = "</font>";
		
		StatQtyInfo pq = dr.$("Stat");
		
		String info = String.format(
						"%s\n" +
						"规格：%s\n" +
						"库存：%d" + "，" +
						"正常：" + fsOK + "%d" + fe + "，" +
						"未盘：" + fsUncheck + "%d" + fe,
						
						dr.getValue("FName"), 
						dr.getValue("FModel"),
						pq.getQty(),
						pq.getOkQty(),
						pq.getRestQty()
				);

		if(pq.getWrongQty() > 0) info += "，" + fsWrong + "异况：" + pq.getWrongQty() + fe;
		if(pq.getExtraQty() > 0) info += "，" + fsNew + "多出：" + pq.getExtraQty() + fe;


		clBG = Color.WHITE;
		DrawBG(vInfo, convertView, clBG);
		String note = (String)dr.getValue("Note");		
		if(!gv.IsEmpty(note)) info += "\n" + fs + "备注：" + note + fe;
		
		
		PDListItemViewInfo vi = (PDListItemViewInfo)vInfo;
		vi.ivState.setBackgroundResource(resid);
		//vi.tvInfo.setBackgroundColor(Color.RED);
		
		info = info.replace("\n", "<br>");
		//vi.tvInfo.setText(info);
		vi.tvInfo.setText(Html.fromHtml(info));    
	}
}

