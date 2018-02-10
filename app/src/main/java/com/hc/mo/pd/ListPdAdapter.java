package com.hc.mo.pd;

import hylib.data.DataRow;
import hylib.toolkits.HyColor;
import hylib.toolkits.gcon;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.ui.dialog.ListItemViewInfo;
import hylib.widget.HyListAdapter;

import java.util.List;

import com.hc.R;
import com.hc.R.drawable;
import com.hc.R.id;
import com.hc.R.layout;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

// 盘点列表
public class ListPdAdapter extends HyListAdapter {

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

	public ListPdAdapter(Context context, List<?> listItems) {
		super(context, listItems, R.layout.list_item);
	}

	@Override
	public ListItemViewInfo NewItemView(View view) {
		PDListItemViewInfo vi  = new PDListItemViewInfo(view);
		vi.tvInfo = (TextView) vi.getView(R.id.item_info);
//		vi.btnNote = (Button) vi.getView(R.id.btn_note);
//		vi.btnDelete = (Button) vi.getView(R.id.btn_delete);
		//vi.btnNote.setOnClickListener(btnNumClickListener);
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

		int color = state == gcon.S_CHECKED ? 0x00CF91 : 
					state == gcon.S_WRONG ? 0xE0C60F : 
					state == gcon.S_EXTRA ? 0x007EDA : 
					0;
		
		String fs = "<font color='#" + Integer.toHexString(color & 0xFFFFFF) + "'>";
		String fe = "</font>";
		
		String info = String.format("%s %s - %s\n批号：%s 有效期至：%s", 
						dr.getValue("FSerialNo"),
						dr.getValue("FName"), 
						dr.getValue("FModel"),
						dr.getValue("FBatchNo"),
						dr.getValue("FDestDate") 
				);
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

