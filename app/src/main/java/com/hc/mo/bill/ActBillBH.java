package com.hc.mo.bill;

import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;

import com.hc.ID;
import com.hc.R;
import com.hc.SysData;
import com.hc.dal.Bill;

import hylib.data.DataRowCollection;
import hylib.toolkits.gc;
import hylib.ui.dialog.Msgbox;
import hylib.util.ParamList;

/**
 * 备货补货单 
 */
public class ActBillBH extends ActBill {

	@Override
    protected void setInitParams()
	 {
    	this.LayID = R.layout.activity_bill;
		this.BTID = Bill.BTID_DB_BU;
		this.HintName = "补货";
		this.pageNames = "补货信息,补货明细,分类统计";
    }
	
	@Override
    protected ParamList GetHeaderConfig() throws Exception {
        String config = "items: [" + 
        		"et: { id:" + ID.FNote + ", title: '备　注' }, " +
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
		plEx.SetValue("FNote", $$V(ID.FNote));
	}
	
	@Override
	protected void UpdateTitle() {
		String title = SysData.CustName.isEmpty() ? "备货补货" : "补货 - " + SysData.CustName;
		//title = "产品明细" + (drDetail == null ? "" : " - " + drDetail.getStrVal("FModel"));
		tvTitle.setText(title);
	}

	@Override
	public String getPrintTitle() {
		return "补货单";
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
