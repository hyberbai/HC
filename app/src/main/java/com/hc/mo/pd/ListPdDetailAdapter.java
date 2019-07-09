package com.hc.mo.pd;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hc.R;

import java.util.List;

import hylib.data.DataRow;
import hylib.toolkits.gcon;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.ui.dialog.ListItemViewInfo;
import hylib.widget.HyListAdapter;

// 盘点列表
public class ListPdDetailAdapter extends HyListAdapter {

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

	public ListPdDetailAdapter(Context context, List<?> listItems) {
		super(context, listItems, R.layout.list_pd_item);
	}

	@Override
	public ListItemViewInfo NewItemView(View view) {
		PDListItemViewInfo vi  = new PDListItemViewInfo(view);
		vi.tvInfo = (TextView) vi.getView(R.id.item_info);
		vi.ivState = (ImageView) vi.getView(R.id.iv_state);
		vi.ivMore = (ImageView) vi.getView(R.id.iv_more);
		vi.ivMore.setVisibility(View.GONE);
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

		int clBG = state == gcon.S_CHECKED ? 0x00CF91 :
				state == gcon.S_WRONG ? 0xE0C60F :
						state == gcon.S_EXTRA ? 0x007EDA :
								0;

		String fs  = "<font color='#" + Integer.toHexString(clBG & 0xFFFFFF) + "'>";
		String fs1 = "<font color='#" + Integer.toHexString(0x338833) + "'>";
		String fs2 = "<font color='#" + Integer.toHexString(0x333333) + "'>";
		String fe = "</font>";

//		String info = String.format("NO.%s\n%s\n规格：%s\n批号：%s\n效期：%s",
//						dr.getValue("SNo"),
//						dr.getValue("FName"),
//						dr.getValue("FModel"),
//						dr.getValue("BatchNo"),
//						dr.getValue("ExpDate")
//				);

		String info = gs.JoinArray(new String[] {
				"NO. " + dr.getValue("SNo"),
				dr.getStrVal("FName"),
				"规格型号: " + dr.getStrVal("FModel"),
				"产品批号: " + dr.getStrVal("BatchNo"),
				"产品效期: " + gs.Connect(dr.getStrVal("MfgDate"), dr.getStrVal("ExpDate"), " / "),
		}, "\n");

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

