package com.hc.mo.bill;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.data.DataTable;
import hylib.io.FileUtil;
import hylib.toolkits.*;
import hylib.ui.dialog.*;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.widget.HyListView;
import android.R.integer;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hc.ID;
import com.hc.MyApp;
import com.hc.SysData;
import com.hc.MyApp.WorkState;
import com.hc.R;
import com.hc.pu;
import com.hc.dal.Bill;
import com.hc.dal.WS;
import com.hc.dal.d;
import com.hc.tools.ActInputProduct;

/**
 * 手术使用单
 */
public class ActBillSale extends ActBill {
	
	@Override
    protected void setInitParams()
	 {
    	this.LayID = R.layout.activity_bill;
		this.BTID = Bill.BTID_WTDX;
		this.HintName = "使用";
		this.pageNames = "基本信息,使用明细,分类统计";
    }
	
	@Override
    protected ParamList GetHeaderConfig() throws Exception {
        String config = "items: [" + 
        		"et: { id:" + ID.HZNo + ", title: '患者编号', vt: num }, " + 
        		"et: { id:" + ID.HZ + 	", title: '患者姓名' }, " + 
        		"rg: { id:" + ID.Sex + 	", title: '性　别', items: [男,女] }, " + 
        		"et: { id:" + ID.Age + 	", title: '年　龄', vt: num }, " + 
        		"et: { id:" + ID.CWNo + ", title: '床位号', vt: nt }, " + 
        		"et: { id:" + ID.Doc + 	", title: '医 生1', auto-comp: { dsn: doc, dm: FName } }, " + 
        		"et: { id:" + ID.ZJ + 	", title: '医 生2', auto-comp: { dsn: doc, dm: FName }  }, " + 
        		"et: { id:" + ID.KS + 	", title: '科　室', auto-comp: { dsn: ks, dm: FName }, def: last  }, " + 
        		"et: { id:" + ID.SSLX + ", title: '手术类型', auto-comp: { dsn: sslx, dm: FName }  }, " + 
        		"et: { id:" + ID.FNote + ", title: '备　注' }, " + 
        "]";

        return new ParamList(config);
    }

	@Override
    protected void onLvItemClick(AdapterView<?> parent, View view, int position, long id) {
        EditRow(position);
    }
    
	@Override
    protected boolean onLvItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		listDetailAdapter.SelectAt(position);
    	final String SNo = drDetail.getStrVal("SNo");
    	ParamList pl = new ParamList();
		Msgbox.Choose("NO." + SNo, new String[] { "删除", "溯源", "产品替换" }, pl,
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(which == 0) ActDeleteItem();
						if(which == 1) ActSY(SNo);
						if(which == 2) ActProductReplace();
					}
				}
		);
		return true;
    }
    
	@Override
	public void ActHandInput() {
		EditRow(-1);
	}
	
//	private View CreateDetailView() {
//		lvSale = new HyListView(this);
//		lvSale.setDivider(new ColorDrawable(0x55CCCCCC));
//		lvSale.setDividerHeight(1);
//		return lvSale;
//	}
	
	@Override
	protected void UpdateTitle() {
		String title = SysData.CustName.isEmpty() ? "手术使用" : "使用 - " + SysData.CustName;
//		if(mViewPager.getCurrentItem() == 2)
//			title = "使用明细" + (drDetail == null ? "" : " - " + drDetail.getStrVal("FModel"));
		tvTitle.setText(title);
	}

	/*
	 * 产品替换
	 */
	public void ActProductReplace() {
		startActivity(ActProductReplace.class, REQ_CODE_EDIT,
				new Param("values", drDetail),
				new Param("CustID", SysData.CustID),
				new Param("ID", drDetail.getValue("BDID"))
		);
	}
	
	@Override
	public void setWorkState(WorkState state) {
		super.setWorkState(state);
	}
	
	@Override
	public void LoadExtHeader(ParamList plEx) {
		$$Set(ID.HZNo, plEx.get("HZNo"));
		$$Set(ID.HZ, plEx.get("HZ"));
		$$Set(ID.Doc, plEx.get("Doc"));
		$$Set(ID.ZJ, plEx.get("ZJ"));
		$$Set(ID.Sex, UIUtils.getRadioItemIDByText((RadioGroup)$$(ID.Sex), plEx.get("Sex")));
		$$Set(ID.Age, plEx.get("Age"));
		$$Set(ID.KS, plEx.get("KS"));
		$$Set(ID.SSLX, plEx.get("SSLX"));
		$$Set(ID.CWNo, plEx.get("CWNo"));
		$$Set(ID.FNote, plEx.get("FNote"));
	}
//	
//	// 填充出库历史列表
//	private void FillSaleHisList(){
//		try {
//			DataTable dtHis = WS.GetSaleHisList();
//			
//			for(DataRow dr : dtHis.getRows())
//			{
//				HashMap<String, Object> map = new HashMap<String, Object>();
//				//map.put("uid", item[FI.get("uid")]);
//				map.put("unames", dr.getValue("unames"));	// 患者
//				map.put("num", dr.getValue("num"));
//				map.put("dt", dr.getValue("dt"));
//				HisItems.add(map);
//			}
//
//			listHisAdapter = new ListSaleHisAdapter(this, HisItems); 
//			lvSaleHis.setAdapter(listHisAdapter);
//			lvSaleHis.setSelected(true);
//			lvSaleHis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				}
//			});
//			SetMode(MODE_LIST);
//		} catch (Exception e) {
//	        pu.ShowException(e, "");
//	        return;
//	    }
//	}

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
	 
	@Override
	public void ClearBill() {
		super.ClearBill();
		gc.clearGroupView(mViewPager, new int[] { ID.Doc, ID.ZJ, ID.KS, ID.SSLX });
	}
	
	@Override
	public void setBillHeaderExtFields(ParamList plEx) throws Exception {
		// 单据主表添加扩展信息
		String sex = UIUtils.getViewText($$(ID.Sex));
		plEx.SetValue("HZNo", $$V(ID.HZNo));
		plEx.SetValue("HZ", $$V(ID.HZ));
		plEx.SetValue("Doc", $$V(ID.Doc));
		plEx.SetValue("ZJ", $$V(ID.ZJ));
		plEx.SetValue("Sex", sex);
		plEx.SetValue("Age", $$V(ID.Age));
		plEx.SetValue("KS", $$V(ID.KS));
		plEx.SetValue("SSLX", $$V(ID.SSLX));
		plEx.SetValue("CWNo", $$V(ID.CWNo));
		plEx.SetValue("FNote", $$V(ID.FNote));
	}
    
    @Override
    protected void AddSNo(String sn) throws Exception {
    	super.AddSNo(sn);
    }

    @Override
    protected void EditRow(int position) {
		try {
	    	CheckBeforeEdit();
	    	
			boolean bAdd = position < 0;
			Param[] values;
			if(!bAdd)
			{
	    		drDetail = (DataRow)listDetailAdapter.SelectAt(position);

	        	if(ProductRelpace.hasReps(drDetail))
	        	{
	        		ActProductReplace();
	        		return;
	        	}
	        		
	    		ParamList pl = drDetail.toParamList();
	    		pl.RenKeys("IID FItemID, IName FName");
	    		values = pl.toArray();
			}
			else
			{
				CheckBeforeEdit();
				values = new Param[] {
							new Param("FUnit", "个"),
							new Param("SP", Bill.GetCustSP())
						};
			}
						
			startActivity(ActInputProduct.class, REQ_CODE_EDIT,
					new Param("InputType", ActInputProduct.TYPE_SALE),
					new Param("ID", bAdd ? 0 : drDetail.getValue("BDID")),
					new Param("title", bAdd ? "产品录入" : "编辑"),
					new Param("values", values),
					new Param("CustID", SysData.CustID)
			); 
		} catch (Exception e) {
			ExProc.Show(e);
		}
    }
    
	
	protected void ExportReport() throws Exception {
		super.ExportReport();
	}

	@Override
	public String getPrintTitle() {
		return "手术使用单";
	}
	
	@Override
	public String getPrintHeader() {
		ParamList pl = new ParamList(drBill.$("Ext"));
		
		String hzInfo = gs.JoinArray(new Object[] {
				pl.DispText("", "HZNo HZ"),
				pl.containsKey("Sex") ? pl.get("Sex") : "",
				pl.IntValue("Age") > 0 ? pl.IntValue("Age") + "岁" : ""
		}, " ");
		
		return gs.JoinArray(new Object[] {
				Param.SVal("患者：", hzInfo),
    			pl.DispText("医生：", "Doc ZJ"),
    			pl.get("FNote"),
		}, "\n");
	}
}
