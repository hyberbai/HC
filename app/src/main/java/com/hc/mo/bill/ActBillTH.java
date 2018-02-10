package com.hc.mo.bill;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits.gc;
import hylib.ui.dialog.Msgbox;
import hylib.ui.dialog.UIUtils;
import hylib.util.ParamList;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dev.ZltdDecoder;
import com.hc.ID;
import com.hc.MainActivityOld;
import com.hc.MyApp;
import com.hc.R;
import com.hc.SysData;
import com.hc.MyApp.WorkState;
import com.hc.R.id;
import com.hc.R.layout;
import com.hc.dal.Bill;

/**
 * 医院退货返库 
 */
public class ActBillTH extends ActBill {

	@Override
    protected void setInitParams()
	 {
    	this.LayID = R.layout.activity_bill;
		this.BTID = Bill.BTID_DB_TH;
		this.HintName = "返货";
		this.pageNames = "返货信息,返货明细,分类统计";
    }
	
	@Override
    protected ParamList GetHeaderConfig() throws Exception {
        String config = "items: [" + 
        		"et: { id:" + ID.FNote + ", text: '备　注' }, " + 
        "]";

        return new ParamList(config);
    }
	 
	@Override
	protected void onLvItemClick(AdapterView<?> parent, View view, int position, long id) {
    	//OpenDlgSale(position);
		listDetailAdapter.SelectAt(position);
	}
	
	@Override
	protected boolean onLvItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		listDetailAdapter.SelectAt(position);
    	final String SNo = drDetail.getStrVal("SNo");
    	ParamList pl = new ParamList("width", "240dp");
		Msgbox.Choose("NO." + SNo, new String[] { "删除", "溯源" }, pl, 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(which == 0) ActDeleteItem();
						if(which == 1) ActSY(SNo);
					}
				}
		);
    	return true;
	}

	@Override
	public void ClearBill() {
		super.ClearBill();
		gc.clearGroupView(mViewPager, new int[] {  });
	}
	
	@Override
	public void LoadExtHeader(ParamList plEx) {
		$$Set(ID.FNote, plEx.get("FNote"));
	}
	
	@Override
	public void setBillHeaderExtFields(ParamList plEx) throws Exception {
		// 单据主表添加扩展信息
		plEx.set("FNote", $$V(ID.FNote));
	}
	
	@Override
	protected void UpdateTitle() {
		String title = SysData.CustName.isEmpty() ? "返货处理" : "返货 - " + SysData.CustName;
		//title = "产品明细" + (drDetail == null ? "" : " - " + drDetail.getStrVal("FModel"));
		tvTitle.setText(title);
	}


	@Override
	public String getPrintTitle() {
		return "返货单";
	}
	
	@Override
	protected void setOptionsMenuParams(ParamList pl) throws Exception {
		final DataRowCollection rows = new DataRowCollection("name|text|pl", new Object[][] {
			new Object[] { "HandInput", "手工录入", null },
			new Object[] { "Sort#PName", "产品名称排序", null },
			new Object[] { "Sort#Spec", "规格型号排序", null },
			new Object[] { "Sort#ExpDate", "有效期至排序", null },
		});
		
		AppendDefaultMenu(rows);
		pl.set("Items", rows);
		pl.set("width", "120dp");
	}
	

}
