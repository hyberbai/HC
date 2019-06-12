package com.hc.mo.bill;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dev.HyScanner;
import com.hc.ActBase;
import com.hc.ID;
import com.hc.MyApp.WorkState;
import com.hc.R;
import com.hc.SysData;
import com.hc.dal.Bill;
import com.hc.dal.SCID;
import com.hc.dal.Setting;
import com.hc.dal.WS;
import com.hc.dal.d;
import com.hc.db.DBHelper;
import com.hc.db.DBLocal;
import com.hc.mo.sy.ActSy;

import java.util.Iterator;
import java.util.Map;

import hylib.data.DataColumnCollection;
import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.data.DataSort;
import hylib.data.DataTable;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits.gi;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.ui.dialog.Msgbox;
import hylib.ui.dialog.PopupWindowEx;
import hylib.ui.dialog.UCCreator;
import hylib.ui.dialog.UICreator;
import hylib.ui.dialog.UIUtils;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.widget.HyEvent.LvItemClickEventParams;
import hylib.widget.HyListAdapter;
import hylib.widget.HyListView;

/**
 * 金蝶单据 
 */
public class ActStockBill extends ActBase {
	protected int mFID;	// 单据ID
	protected int BTID; // 单据类型ID
	protected int ETID; // 单据扩展类型ID
	protected String pageNames;

	protected String mBillNo;
	protected DataTable dtStat;
	protected ViewPager mViewPager;

	protected ViewGroup mHeaderView;
	protected ViewGroup mDetailView;
	protected ViewGroup mStatView;
	
	protected WorkState mWorkState;
	
	protected DataRow drBill;
	protected DataRow drEntry;

	protected TextView tvTitle;
	protected TextView tvStat;
	
	protected DataTable dtBill;
	protected DataTable dtEntry;

	protected HyListView lvEntry;
	protected HyListView lvStat;
	protected Button btnReport;
	protected Button btnSubmit;
	
	protected HyListAdapter listDetailAdapter;
	protected HyListAdapter listStatAdapter;

	public boolean IsRealTime;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DecoderMode = HyScanner.Mode.Ignore;
    	loadInitParams();

        try {
            InitViews();
            UpdateTitle();
		} catch (Exception e) {
			ExProc.Show(e);
		}

        try {
            LoadData();
		} catch (Exception e) {
			ExProc.Show(e);
		}
    }

    protected void loadInitParams() {
		BTID = Params.IntValue("CID");
		IsRealTime = Params.BValue("IsRealTime");
		if(BTID / 10 == Bill.BTID_DB) {
			ETID = BTID % 10;
			BTID = BTID / 10;
		}
		mFID = Params.IntValue("ID");
		this.pageNames = "基本信息," + Bill.nameOf(BTID, ETID) + "明细,分类统计";
    }

    protected void InitViews() throws Exception {
		setContentView(R.layout.activity_stockbill);
		
		tvTitle = $(R.id.tv_title);
		tvStat = $(R.id.tv_Stat);
		btnSubmit = $(R.id.ib_submit);
		
		btnReport = $(R.id.ib_report);
		
		ParamList plViewPager = new ParamList();
		plViewPager.set("PageSelected", new EventHandleListener() {
			
			@Override
			public void Handle(Object sender, ParamList arg) throws Exception {
//					UpdateStat();
//					UpdateTitle();
			}
		});
		
		plViewPager.SetValue("pageNames", pageNames);
		
		CreateOptionsPopupMenu();
		mHeaderView = CreateBillHeaderView();
		mDetailView = CreateDetailView();
		mStatView = CreateStatView();

        CreateBillStatListAdapter("Cls");
        
		// 创建分页
        mViewPager = UICreator.CreateViewPager(this, (ViewGroup)$(R.id.ll_page), new View[]{
        	mHeaderView,
        	mDetailView,
        	mStatView,
        }, plViewPager);

		lvEntry.setAdapter(listDetailAdapter);
		lvEntry.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		lvEntry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
	        	//OpenDlgSale(position);
	        	onLvItemClick(parent, view, position, id);
	        }  
	          
	    });  
 
		lvEntry.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
				return onLvItemLongClick(parent, view, position, id);
			}
		});

		ParamList pl = new ParamList();
        pl.SetValue("itemViewConfig", getListItemViewConfig());
        pl.SetValue("setViewData", new EventHandleListener() {
        	
        	@Override
        	public void Handle(Object sender, ParamList arg) throws Exception {
        		setListItemViewData(arg);
        	}
        });
        
		listDetailAdapter = HyListAdapter.Create(context, lvEntry, null, pl);
		listDetailAdapter.onNotify = listAdapterNotify;
		
		mViewPager.setCurrentItem(Setting.GetUserSetting(SCID.LastPage_StockBill, 1));
	}

	@Override
	protected void setOptionsMenuParams(ParamList pl) throws Exception {
		final DataRowCollection rows = new DataRowCollection("name|text|pl", new Object[][] {
				new Object[] { "Refresh", "刷新", null },
		});

		pl.set("Items", rows);
		pl.set("width", "120dp");
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T $$(int resId) {
		return (T) mHeaderView.findViewById(resId);
	}
	
	@Override
	public void onDestroy() {
		Setting.SetUserSetting(SCID.LastPage_StockBill, mViewPager.getCurrentItem());
	  	super.onDestroy();
	}


    protected Object getListItemViewConfig() {
        // 列表项视图配置
        return "items: [" + 
	    	"[tv: { id:2031, fs: 14.2dp, text: info,w:5w,bg1:r,color:black, padding: 3dp,h:wrap }, "+ 
	    	"tv: { id:2032, fs: 14.2dp, text: price,w:100dp,bg1:g,color:black, padding: 3dp, h:wrap, grv:r }] "+ 
	    "], padding: '10dp, 5dp, 10dp, 5dp', margin: 0dp, w:match, h:wrap, bg1:#33CCAAFF";
    }
    
    protected void setListItemViewData(ParamList arg) {
		View convertView = arg.Get("view", View.class);

		TextView tvInfo = type.as(convertView.findViewById(2031), TextView.class);
		TextView tvPrice = type.as(convertView.findViewById(2032), TextView.class);
		
		DataRow dr = arg.Get("item", DataRow.class);
		ParamList pl = dr.$("Ext");

		tvInfo.setText(Bill.getStockBillItemInfo(dr));
		tvPrice.setText(Bill.getStockBillPriceInfo(dr));
		
//		SpannableStringHelper sh = new SpannableStringHelper();
//		gs.getFmtMoney(sh, dr.getFVal("Amount"));
//		sh.appendObj("\n");
//		gs.getFmtQty(sh, dr.getIntVal("Qty"));
//		tvInfo.setLineSpacing(0, 1.2f);
//		tvTot.setLineSpacing(0, 1.2f);
//		tvTot.setText(sh);
    }
    
	public Object $$V(int resId) {
		return UIUtils.getViewValue((View)$$(resId));
	}
	
	public void $$Set(int resId, Object value) {
		UIUtils.setViewValue((View)$$(resId), value);
	}
	
    protected void onLvItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
    
    protected boolean onLvItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		return true;
    }
    
    protected ParamList GetHeaderConfig() throws Exception {
        String config = "items: [" + 
        		"et: { id:" + ID.BillNo + ", title: '单据编号', disabled }, " +
        		"et: { id:" + ID.Cust + ", title: '客　户', disabled }, " +
				( BTID == Bill.BTID_WTDX ?
					"et: { id:" + ID.SCStock + ", title: '库　位', disabled }, "
				: BTID < 20 ?
					"et: { id:" + ID.SCStock + ", title: '调出库', disabled }, " +
					"et: { id:" + ID.DCStock + ", title: '调入库', disabled }, "
				:
					""
				) +
        		"et: { id:" + ID.FNote + ", title: '备　注', disabled }, " +
        "]";

        return new ParamList(config);
    }

	protected ViewGroup CreateBillHeaderView() {
        try {
        	ParamList pl = GetHeaderConfig();
        	if(pl == null) return null;
            pl.set("ValueChanged", new EventHandleListener() {
				
				@Override
				public void Handle(Object sender, ParamList arg) throws Exception {
					//View v = type.as(sender, View.class);
					setChanged(true);
				}
				
			});
            return UICreator.CreateScrollPanel(this, pl);
		} catch (Exception e) {
			ExProc.Show(e);
			return null;
		}
	}
	
	protected HyListView CreateDetailView() {
		lvEntry = UCCreator.CreateListView(this, true);
		return lvEntry;
	}

	protected HyListView CreateStatView() {
		lvStat = UCCreator.CreateListView(this, true);
		return lvStat;
	}
	
    @Override
	public void onClick(View v) {
		int id = v.getId();
		
		// 清空单据
		if (id == R.id.ib_clear) ActClear();
		
		// 导出报表
		if (id == R.id.ib_report) ActReport();

		if (id == R.id.ib_back2) Finish();
		super.onClick(v);
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Finish();
            return true;
        }
//        } if (keyCode == KeyEvent.KEYCODE_BUTTON_A) {
//        	//return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

	private gi.NotifyListener listAdapterNotify = new gi.NotifyListener() {
		
		@Override
		public Object Notify(Object sender, Object arg) {
			HyListView lv = lvEntry;
			lv.setSelection((Integer)arg);
            
            if(lv == lvEntry) {
            	drEntry = (DataRow)(listDetailAdapter.getCurrentItem());
            }
            //if(lv == lvT2) ();
			return null;
		}
	};
	
	protected void CreateBillStatListAdapter(final String infoName) {
		ParamList plLv = new ParamList();

        // 列表项视图配置
        String cfgListItem = "items: [" + 
        	"[tv: { id:2031, fs: 16dp, text: info,w:5w,bg1:r,color:black, padding: 8dp,h:wrap }, "+ 
        	"tv: { id:2032, fs: 16dp, text: qty,w:64dp,bg1:b,color:black, padding: 8dp, h:wrap,grv:r }, "+ 
        	"tv: { id:2033, fs: 14.2dp, text: tot,w:112dp,bg1:g,color:black, padding: 8dp, h:wrap, grv:r }] "+ 
        "], padding: 0dp, margin: 0dp, w:match, h:wrap, bg1:#33CCAAFF";
//	        	
        plLv.SetValue("itemViewConfig", cfgListItem);

        plLv.SetValue("setViewData", new EventHandleListener() {
        	
        	@Override
        	public void Handle(Object sender, ParamList arg) throws Exception {
        		View convertView = arg.Get("view", View.class);
        		DataRow dr = arg.Get("item", DataRow.class);
        		TextView tvInfo = type.as(convertView.findViewById(2031), TextView.class);
        		TextView tvQty = type.as(convertView.findViewById(2032), TextView.class);
        		TextView tvTot = type.as(convertView.findViewById(2033), TextView.class);
        		String info = dr.getStrVal(infoName);
        		tvInfo.setText(info);
        		tvQty.setText(gs.getFmtQty(dr.getIntVal("Qty")));
        		tvTot.setText(gs.getFmtMoney(dr.getFVal("Amount")));
        	}
        });
        
        
        plLv.SetValue("onEvent", new EventHandleListener() {
			
			@Override
			public void Handle(Object sender, ParamList arg) throws Exception {
				if(arg instanceof LvItemClickEventParams) {
					DataRow dr = dtStat.getRow(((LvItemClickEventParams)arg).getPosition());
					DataRowCollection rows = dtEntry.SelectRows("Cls", dr.getValue("Cls"));
					Msgbox.Choose("", rows, new ParamList("dm: 'SNo, FModel'"));
				}
			}
		});

        
		listStatAdapter = HyListAdapter.Create(context, lvStat, null, plLv);
		UCCreator.setListViewParam(context, lvStat, plLv);
	}
	
	public void UpdateDetailAdapter() {
		if(dtEntry == null) return;
		listDetailAdapter.setListItems(dtEntry.getRows());
	}
	
	private void addStatDataRow(DataRow drSrc, String[] group, Object[] groupValues, float total, float qty){
		DataRow drStat = dtStat.newRow();
		for (int i = 0; i < group.length; i++) {
			String fn = group[i];
			drStat.setValue(fn, groupValues[i]);
			groupValues[i] = drSrc.getValue(fn);
		}
		drStat.setValue("Amount", total);
		drStat.setValue("Qty", qty);
		dtStat.addRow(drStat);
	}

	protected void UpdateStat() {
		if(dtEntry == null) return;
		DataTable dt = dtEntry.Copy();
		final String[] group = gs.Split("Cls");
		dt.Sort("Cls,IID");
		String groupKey = group[0];
		float amount = 0;
		int qty = 0;
		
		float totAmount = 0.0f;
		int totQty = 0;
		
		dtStat = new DataTable("stat", gs.JoinArray(group, "|") + "|Amount/m|Qty/i|items/e");
		Object[] groupValues = new Object[group.length];
		boolean changed = false;
		DataRow dr = null;

		for (int rowIndex = 0; rowIndex < dt.getRowCount(); rowIndex++) {
			dr = dt.getRow(rowIndex);
			// 比较分组字段是否变动
			for (int i = 0; i < group.length; i++) {
				if(rowIndex == 0)
					groupValues[i] = dr.getValue(group[i]);
				else if(!gv.Same(dr.getValue(group[i]), groupValues[i]))
				{
					changed =true;
					break;
				}
			}
			
			if(changed)
			{
				addStatDataRow(dr, group, groupValues, amount, qty);
				amount = 0;
				qty = 0;
				changed = false;
			}
			int n = dr.getIntVal("FQty");
			float m = dr.getFVal("FInTaxAmount");
			qty += n;
			amount += m;

			totQty += n;
			totAmount += m;
		}
		if(qty > 0) addStatDataRow(dr, group, groupValues, amount, qty);
		
		if(dtStat.getRowCount() > 0) {
			dr = dtStat.getRow(0);
			if(gv.IsEmpty(dr.getValue(groupKey))) {
				dr.setValue(groupKey, "其 它");
				dtStat.getRows().MoveTo(0, dtStat.RowCount());
			}
		}

		SpannableStringHelper sh = new SpannableStringHelper();

		if(totQty > 0)
		{
			sh.appendObj("合计：");
	
			sh.startSpan();
			sh.appendObj("x " + totQty + "  ");
			sh.setForeColor(Color.GRAY);
			sh.setFontSize(0.9f);
			
			sh.appendMoney(totAmount, Color.RED, 1.0f);
		}
		tvStat.setText(sh);
		listStatAdapter.setListItems(dtStat.getRows());
	}
	
	public void RefreshList() {
		listDetailAdapter.notifyDataSetChanged();
		UpdateStat();
	}

	public void setWorkState(WorkState state) {
	}

	@Override
	public void beforeShowOptionsMenu(PopupWindowEx pw) {
	}

	public void ActClear() {
		if(Msgbox.Ask(this, "确认清空数据吗？") == false) return;
		InnerClearBill();
	}

	public void ClearBill() {
		if(mFID <= 0 && drBill != null && drBill.getIntVal("state") == 0)
			DeleteBill(mFID);
		
		mFID = 0;
		if(dtEntry != null) dtEntry.ClearRows();
		drEntry = null;
		RefreshList();
		tvStat.setText("");
		setWorkState(WorkState.Normal);
	}

	public void InnerClearBill() {
		LockChange = true;
		try {
			ClearBill();
		} catch (Exception e) {
			ExProc.Show(e);
		}
		LockChange = false;
	}
    
    public static void DeleteBill(int FID) {
    	String sqlCond = " where FID=" + FID;
		DBLocal.ExecSQL("delete from StockBill" + sqlCond);
		DBLocal.ExecSQL("delete from StockBillEntry" + sqlCond);
	}

	protected void UpdateTitle() {
		String title = Bill.jdNameOf(BTID, ETID) + "单" + (gv.IsEmpty(mBillNo) ? "" : " - " + mBillNo);
		tvTitle.setText(title);
	}

	private String GetCond(){
		String cond = SysData.isAdmin ? "" : " and FEmpID=" + SysData.emp_id;
		if(!SysData.isAdmin) {
			if(SysData.CustID > 0) {
				cond += " and FCustID=" + SysData.CustID;
			}
		}
		return cond;
	}

	public  void LoadLocalBill() throws Exception {
		dtBill = DBLocal.OpenSingleTable("StockBill",
				"select * from StockBill where FBillTypeID=" + BTID +
						(ETID == 1 ? " and ExtType='" + Bill.nameOfExtTypeID(ETID) + "'" : "") + GetCond());
		LoadBill(mFID);
	}

	private String cfgEntryCols = DBHelper.Cfg_StockBillEntry + "|" + DBHelper.Cfg_Item;
	public  void LoadServerBill() throws Exception {
		ParamList plData = WS.GetJdBillInfo(mFID);
		if(plData == null) return;
		dtBill = (DataTable)plData.GetDeserializeValue("StockBill");
		dtEntry = (DataTable)plData.GetDeserializeValue("StockBillEntry");
		dtBill.SetColsType(DBHelper.Cfg_StockBill);
		dtEntry.SetColsType(cfgEntryCols);
		LoadBill(mFID);
	}

	public void LoadData() throws Exception {
		if(IsRealTime) LoadServerBill(); else LoadLocalBill();
		UpdateDetailAdapter();
	}
	
	public void ActSY(String SNo) {
		startActivity(ActSy.class, 0, new Param("SNo", SNo)); 
	}
	
	private void OpenDetailTable(int FID) throws Exception {
		if(!IsRealTime) {
			String sql = "select * from StockBillEntry d\n" +
					"left join SN on d.SNo=SN.FSerialNo\n" +
					"left join Item ic on SN.FItemID=ic.FItemID\n" +
					"where d.FID=?\n";
			dtEntry = DBLocal.OpenTable(cfgEntryCols, sql, FID);
		}
		dtEntry.setTableName("StockBillEntry");

		DataRow dr = dtEntry.firstRow();
		if(dr != null) {
			$$Set(ID.SCStock, d.getStockName(dr.getIntVal("FSCStockID")));
			$$Set(ID.DCStock, d.getStockName(dr.getIntVal("FDCStockID")));
		}
		CoverExtFieldValues(dtEntry);
	}


    public void CoverExtFieldValues(DataTable dt) throws Exception
    {
    	DataColumnCollection cols = dt.getColumns();
        for (DataRow dr : dt.getRows())
            SetExtDataFields(cols, dr);
    }


    public void SetExtDataFields(DataColumnCollection cols, DataRow dr) throws Exception
    {
    	ParamList pl = new ParamList(dr.getStrVal("Ext"));

        Iterator<?> iter = pl.entrySet().iterator();
        while (iter.hasNext()) {
	        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();
	        String key = (String)entry.getKey();
	        Object val = entry.getValue();
            if(cols.containsCol(key)) dr.setValue(key, val);
        }
    }

	public void LoadExtHeader(ParamList plEx) {
		
	}
	
	public void LoadBill(int FID) throws Exception {
		LockChange = true;
		
		drBill = dtBill.FindRow("FID", FID);

		mFID = drBill == null ? 0 :  drBill.getIntVal("FID");
		mBillNo = drBill == null ? "" :  drBill.getStrVal("FBillNo");
//		SetCust(plEx.IntValue("CustID"));
		if(drBill != null) {
			$$Set(ID.BillNo, mBillNo);
			$$Set(ID.Cust, d.GetCustName(drBill.getIntVal("FCustID")));
			$$Set(ID.FNote, drBill.getStrVal("FExplanation"));
		}

		ParamList plEx = new ParamList(drBill == null ? "" : drBill.getStrVal("Ext"));
		LoadExtHeader(plEx);
		
		OpenDetailTable(mFID);
		
		UpdateDetailAdapter();
		
		LockChange = false;
		setWorkState(drBill == null || drBill.getIntVal("state")== 0 ? WorkState.Normal : WorkState.Done);
		UpdateStat();
		UpdateTitle();
		//lvPD.setSelected(true);
	}

	public void SetWorking() {
		if(mWorkState != WorkState.Done) return;
		try {
			drBill.updateValue("state", 0);
			setWorkState(WorkState.Normal);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public boolean ActRefresh() {
		try {
			LoadData();
			return true;
		} catch (Exception e) {
			ExProc.Show(e);
			return false;
		}
	}
	
	public void ActReport(){
		try {
			ExportReport();
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	private String LastSortCol = "";
	private boolean IsAscSort;
	protected void SortList(String colName) {
		IsAscSort = LastSortCol.equals(colName) ? !IsAscSort : true;
		CallSort(colName);
		RefreshList();
		LastSortCol = colName;
	}
	
	protected void CallSort(String colName) {
		dtEntry.Sort(DataSort.getSortName(colName, IsAscSort));
	}
	
	protected void ExportReport()throws Exception {
	}


}
