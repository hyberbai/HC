package com.hc.mo.bill;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import com.dev.DataList;
import com.dev.HyPrt;
import com.dev.HyScanner;
import com.hc.ActBase;
import com.hc.MyApp;
import com.hc.MyApp.WorkState;
import com.hc.R;
import com.hc.SysData;
import com.hc.dal.Bill;
import com.hc.dal.SCID;
import com.hc.dal.Setting;
import com.hc.dal.WS;
import com.hc.db.DBHelper;
import com.hc.db.DBLocal;
import com.hc.mo.sy.ActSy;
import com.hc.pu;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import hylib.data.DataColumnCollection;
import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.data.DataSort;
import hylib.data.DataTable;
import hylib.db.SqlDataAdapter;
import hylib.io.FileUtil;
import hylib.toolkits.DelayTask;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits.Speech;
import hylib.toolkits.SysOpen;
import hylib.toolkits.gc;
import hylib.toolkits.gi;
import hylib.toolkits.gi.CallBack;
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
 * 单据基类 
 */
public abstract class ActBill extends ActBase {
	protected int LayID; 
	protected boolean hasChanged; 
	protected int mBID;	// 单据ID
	protected int BTID; // 单据类型ID
	protected String pageNames;

	protected ParamList plHeader;
	
	protected DataTable dtRpt;
	protected DataTable dtStats;
	protected String HintName;
	protected ViewPager mViewPager;

	protected ViewGroup mHeaderView;
	protected ViewGroup mDetailView;
	protected ViewGroup mStatsView;
	
	protected DelayTask mDelaySave;
	protected WorkState mWorkState; 
	
	protected DataRow drBill;
	protected DataRow drDetail;

	protected TextView tvTitle;
	protected TextView tvStats;
	
	protected DataTable dtBill;
	protected DataTable dtDetail;

	protected HyListView lvDetail;
	protected HyListView lvStat;
	protected Button btnReport;
	protected Button btnSubmit;
	protected Button btnClear;
	protected Button btnAddNew;
	
	protected ListT1Adapter listDetailAdapter;
	protected HyListAdapter listStatAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	setInitParams();
        super.onCreate(savedInstanceState);
        DecoderMode = HyScanner.Mode.Enabled;
        
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

    protected abstract void setInitParams();

    protected void InitViews() throws Exception {
		setContentView(LayID);
		
		tvTitle = $(R.id.tv_title);
		tvStats = $(R.id.tv_Stat);
		btnSubmit = $(R.id.ib_submit);
		btnClear = $(R.id.ib_clear);
		btnAddNew = $(R.id.ib_new);
		
		mDelaySave = new DelayTask();
		mDelaySave.TaskHandleListener = new EventHandleListener() {
			
			@Override
			public void Handle(Object sender, ParamList arg) throws Exception {

		        runOnUiThread(new Runnable() {
		            public void run() {
		            	try {
							ActSave();
						} catch (Exception e) {
							ExProc.Show(e);
						}
		            }
		        });
			}
		};
		
		btnReport = $(R.id.ib_report);
		
		tvTitle.setText("[" + SysData.op_name + "]" + HintName);
        
		ParamList plViewPager = new ParamList();
		plViewPager.set("PageSelected", new EventHandleListener() {
			
			@Override
			public void Handle(Object sender, ParamList arg) throws Exception {
				ShowSoftInput(false);
			}
		});
		
		plViewPager.SetValue("pageNames", pageNames);
		
		CreateOptionsPopupMenu();
		
		LockChange = true;
		try {
			mHeaderView = CreateBillHeaderView();
			mDetailView = CreateDetailView();
			mStatsView = CreateStatView();

			loadLastInput();
		} catch (Exception e) {
			ExProc.Show(e);
		}
		LockChange = false;
		
        CreateBillStatListAdapter("Cls");
        
		// 创建分页
        mViewPager = UICreator.CreateViewPager(this, (ViewGroup)$(R.id.ll_page), new View[]{
        	mHeaderView,
        	mDetailView,
        	mStatsView,
        }, plViewPager);

		listDetailAdapter = new ListT1Adapter(this, null); 
		listDetailAdapter.onNotify = listAdapterNotify;
		lvDetail.setAdapter(listDetailAdapter);
		lvDetail.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		lvDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
	        	//OpenDlgSale(position);
	        	onLvItemClick(parent, view, position, id);
	        }  
	          
	    });  

		lvDetail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
				return onLvItemLongClick(parent, view, position, id);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T $$(int resId) {
		return (T) mHeaderView.findViewById(resId);
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
        return null;
    }

	protected ViewGroup CreateBillHeaderView() {
        try {
        	plHeader = GetHeaderConfig();
        	if(plHeader == null) return null;
            plHeader.set("ValueChanged", new EventHandleListener() {
				
				@Override
				public void Handle(Object sender, ParamList arg) throws Exception {
					//View v = type.as(sender, View.class);
					setChanged(true);
				}
				
			});
            return UICreator.CreateScrollPanel(this, plHeader);
		} catch (Exception e) {
			ExProc.Show(e);
			return null;
		}
	}
	
	protected HyListView CreateDetailView() {
		lvDetail = UCCreator.CreateListView(this, true);
		return lvDetail;
	}

	protected HyListView CreateStatView() {
		lvStat = UCCreator.CreateListView(this, true);
		return lvStat;
	}
	
	@Override
	public void onStop() {
		try {
			ActSave();
		} catch (Exception e) {
			ExProc.Show(e);
		}
	  	super.onStop();
	}

	@Override
	public void onDestroy() {
		try {
			ActSave();
			saveLastInput();
		} catch (Exception e) {
			ExProc.Show(e);
		}
	  	super.onDestroy();
	}
	
    @Override
	public void onClick(View v) {
		int id = v.getId();
		
		// 上传临时单据
		if(id == R.id.ib_submit) ActSubmit();

		// 清空单据
		if (id == R.id.ib_clear) ActClear();
		if (id == R.id.ib_new) ActNew();
		
		// 导出报表
		if (id == R.id.ib_report) ActReport();
		
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

    @Override
	public void Finish()  
    {
    	try {
        	if(hasChanged) {
    			ActSave();
        		setResult(RESULT_CHANGED);
        	}
    		super.Finish();
		} catch (Exception e) {
			ExProc.Show(e);
		}
    }
	
	private gi.NotifyListener listAdapterNotify = new gi.NotifyListener() {
		
		@Override
		public Object Notify(Object sender, Object arg) {
			HyListView lv = lvDetail;
			lv.setSelection((Integer)arg);
            
            if(lv == lvDetail) {
            	drDetail = (DataRow)(listDetailAdapter.getCurrentItem());
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
					DataRow dr = dtStats.getRow(((LvItemClickEventParams)arg).getPosition());
					DataRowCollection rows = dtDetail.SelectRows("Cls", dr.getValue("Cls"));
					Msgbox.Choose("", rows, new ParamList("dm: 'SNo, FModel'"));
				}
			}
		});

        
		listStatAdapter = HyListAdapter.Create(context, lvStat, null, plLv);
		UCCreator.setListViewParam(context, lvStat, plLv);
	}
	
	public void UpdateDetailAdapter() {
		if(dtDetail == null) return;
		listDetailAdapter.setListItems(dtDetail.getRows());
	}
	
	private void addStatDataRow(DataRow drSrc, String[] group, Object[] groupValues, float total, float qty){
		DataRow drStat = dtStats.newRow();
		for (int i = 0; i < group.length; i++) {
			String fn = group[i];
			drStat.setValue(fn, groupValues[i]);
			groupValues[i] = drSrc.getValue(fn);
		}
		drStat.setValue("Amount", total);
		drStat.setValue("Qty", qty);
		dtStats.addRow(drStat);
	}

	protected void UpdateStats() {
		if(dtDetail == null) return;
		DataTable dt = dtDetail.Copy();
		final String[] group = gs.Split("Cls");
		dt.Sort("Cls,IID");
		String groupKey = group[0];
		float amount = 0;
		int qty = 0;
		
		float totAmount = 0.0f;
		int totQty = 0;
		
		dtStats = new DataTable("stat", gs.JoinArray(group, "|") + "|Amount/m|Qty/i|items/e");
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
			int n = dr.getIntVal("Qty");
			float m = dr.getFVal("Price") * n;
			qty += n;
			amount += m;

			totQty += n;
			totAmount += m;
		}
		if(qty > 0) addStatDataRow(dr, group, groupValues, amount, qty);
		
		if(dtStats.getRowCount() > 0) {
			dr = dtStats.getRow(0);
			if(gv.IsEmpty(dr.getValue(groupKey))) {
				dr.setValue(groupKey, "其 它");
				dtStats.getRows().MoveTo(0, dtStats.RowCount());
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
		tvStats.setText(sh);
		listStatAdapter.setListItems(dtStats.getRows());
	}
	
	public void RefreshList() {
		listDetailAdapter.notifyDataSetChanged();
		UpdateStats();
	}

	public void setWorkState(WorkState state) {
		$(R.id.ll_bottom).setVisibility(drBill != null ? View.VISIBLE : View.GONE);
		if(mWorkState == state) return; 
		
		//setDecoderEnabled(state == WorkState.Normal);
		mWorkState = state;
		btnSubmit.setVisibility(mWorkState == WorkState.Normal ? View.VISIBLE : View.GONE);
		btnClear.setVisibility(mWorkState == WorkState.Normal ? View.VISIBLE : View.GONE);
		
		btnReport.setVisibility(mWorkState == WorkState.Done ? View.VISIBLE : View.GONE);
		btnAddNew.setVisibility(mWorkState == WorkState.Done ? View.VISIBLE : View.GONE);
		//gc.setViewGroupCtrlsState(mHeaderView, mWorkState == WorkState.Normal);
		UpdateDetailAdapter();
	}

	@Override
	public void beforeShowOptionsMenu(PopupWindowEx pw) {
		super.beforeShowOptionsMenu(pw);
	}

	public void ActNew() {
		if(mWorkState == WorkState.Normal) {
			if(Msgbox.Ask(this, "数据尚未提交，确认继续吗？") == false) return;
		}
		InnerClearBill();
	}

	public void ActClear() {
		if(mWorkState == WorkState.Normal) {
			if(Msgbox.Ask(this, "确认" + (mBID < 0 ? "删除" : "清空") + "当前单据吗？") == false) return;
		}
		InnerClearBill();
		setWorkState(mWorkState);
	}

	public void ClearBill() {
		if(mBID <= 0 && drBill != null && drBill.getIntVal("state") == 0)
			DeleteBill(mBID);
		mBID = 0;
		if(dtDetail != null) dtDetail.ClearRows();
		drDetail = null;
		drBill = null;
		RefreshList();
		tvStats.setText("");
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
    
    public void DeleteBill(int BID) {
    	String sqlCond = " where BID=" + BID;
		DBLocal.ExecSQL("delete from Bill" + sqlCond);
		DBLocal.ExecSQL("delete from BillDetail" + sqlCond);
		hasChanged = true;
	}

	// 创建一个单据
	public void CreateBill(){
    	try {
    	//	BID = DBLocal.getMaxID(dtBill) + 1;
    		
    		mBID = DBLocal.getMinID("Bill", "BID");
    		if(mBID < 0) mBID--; else mBID = -BTID * 1000 - 1;
    		
    		// 创建单据头
    		drBill = dtBill.newRow();
    		drBill.setValue("BID", mBID);
    		drBill.setValue("BTID", BTID);
    		drBill.setValue("BillNo", Bill.NewBillNo(BTID));
    		drBill.setValue("BDate", gv.SDate(new Date()));
    		drBill.setValue("SaveDate", new Date());
    		drBill.setValue("state", 0);
    		drBill.setValue("UID", SysData.op_id);

    		ParamList plEx = new ParamList();
    		plEx.set("CustID", SysData.CustID);
    		drBill.updateValue("Ext", plEx.toString());
    		
    		dtBill.addRow(drBill);
			
    		// 加载单据明细表
    		OpenDetailTable(mBID);

    		UpdateDetailAdapter();
    		setWorkState(WorkState.Normal);
    		
			setChanged(true);
    		//lvPD.setSelected(true);
		} catch (Exception e) {
			ExProc.Show(this, e);
		}
	}

	protected void UpdateTitle() {
	}
	
//	public void ChooseCust() throws Exception {
//		if(dtDetail != null && dtDetail.getRowCount() > 0) return;
//
//		try {
//			ParamList pl = new ParamList("hint", "搜索客户");
//			pl.SetValue("MaxListNum", 5);
//			pl.SetValue("LeastInputChars", 2);
//			Search.Execute(context, Search.CUST, "", pl);
//		} catch (Exception e) {
//			ExProc.Show(e);
//		}
//	}
	
    protected void setChanged(boolean value) {
		if(LockChange) return;
		IsChanged = value;
		hasChanged = true;
		if(IsChanged) SetWorking();
		// 数据发生改变每3秒自动保存一次
		if(IsChanged) mDelaySave.Start(3000); else mDelaySave.Cancel();
	}
    
	public void setBillHeaderExtFields(ParamList plEx) throws Exception {
	}
    
	public void UpdateBillHeaderFields() throws Exception {
		if(!IsChanged) return;

		drBill.updateValue("SSID", SysData.StockID);
		drBill.updateValue("YSID", SysData.StockID);
		
		// 单据主表添加扩展信息
		ParamList plEx = new ParamList();
		
		setBillHeaderExtFields(plEx);
		
		Date billDate = gv.getNowDate();
		plEx.weakSetValue("CustID", SysData.CustID);
		plEx.weakSetValue("BillDate", gv.SDate(billDate));

		drBill.setValue("SaveDate", new Date());
		drBill.weakSetValue("BDate", billDate);
		plEx.set("Note", "");
		
		drBill.updateValue("Ext", plEx.toString());
	}
	
//	protected void SetCust(DataRow drCust) throws Exception {
//		if(drCust == null)
//		{
//			CustID = 0;
//	        StockID= 0;
//	        CustName = "";
//	        UpdateTitle();
//	        return;
//		}
//        CustID = drCust.getIntVal("FItemID");
//        StockID= drCust.getIntVal("FStockID");
//        CustName = drCust.getStrVal("SName");
//        setChanged(true);
//        UpdateBillHeaderFields();
//        UpdateTitle();
//
//		SaveLastCust();
//	}

//	protected void SetCust(int CustID) throws Exception {
//		SetCust(dtCust == null ? null : dtCust.FindRow("FItemID", CustID));
//	}

	public void LoadData() throws Exception {
		//InnerClearBill();
		String cond = SysData.isAdmin ? "" : " and UID=" + SysData.op_id;
		if(SysData.StockID > 0) cond += " and YSID=" + SysData.StockID;
		dtBill = DBLocal.OpenSingleTable("Bill", "select * from Bill where BTID=?" + cond, BTID);
//		drBill = dtBill.FindRow("BID", 0);
//		BID = drBill == null ? -1 : 0;
		int BID = Params.IntValue("ID");
		if(BID == 0) {
			//DataRow dr = dtBill.FindRow("state", 0);
			DataRow dr = dtBill.lastRow();
			if(dr != null)
				if(dr.getIntVal("state") == 0)
					BID = dr.getIntVal("BID");
		}
		if(BID != 0) LoadBill(BID); else OpenDetailTable(0);
		UpdateDetailAdapter();
	}
	
	public void ActSY(String SNo) {
		startActivity(ActSy.class, 0, new Param("SNo", SNo)); 
	}
	
	public void ActDeleteItem() {
		try {
			SetWorking();
			drDetail.Delete();
			drDetail = null;
			IsChanged = true;
			ActSave();
			RefreshList();
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public synchronized void ActSave() throws Exception {
		if(dtBill == null) return;
		if(!IsChanged) return;

		if(mBID == 0) CreateBill();
		gi.CallBack Exec = new CallBack() {
			
			@Override
			public Object Call() throws Exception {
				SQLiteDatabase db = DBLocal.getDB();
				SqlDataAdapter apt = new SqlDataAdapter(db);

	    		UpdateBillHeaderFields();
	    		
	    		UpdateDB(apt);
	    		
				setChanged(false);
				return true;
			}
		};
		DBLocal.ExecTrans(Exec);
	}
	
	public void UpdateDB(SqlDataAdapter apt) {
		apt.Update(dtBill);
		apt.Update(dtDetail);
	}
	
	private void OpenDetailTable(int BID) throws Exception {
		String sql = "select * from BillDetail d\n" + 
					 "left join SN on d.SNo=SN.FSerialNo\n" +
					 "left join Item ic on SN.FItemID=ic.FItemID\n" +
    				 "where BID=?\n" +
    				 "order by BDID";
		dtDetail = DBLocal.OpenTable(DBHelper.Cfg_BillDetail + "|" + DBHelper.Cfg_Item, sql, BID);
		dtDetail.setTableName("BillDetail");
		CoverExtFieldValues(dtDetail);
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
	
	public void LoadBill(int ID) throws Exception {
		LockChange = true;
		
		drBill = dtBill.FindRow("BID", ID);
		mBID = drBill == null ? 0 : drBill.getIntVal("BID");

		ParamList plEx = new ParamList(drBill == null ? "" : drBill.getStrVal("Ext"));

		LoadExtHeader(plEx);
		
		//plEx.set("Note", "");
		
		OpenDetailTable(mBID);
		
		UpdateDetailAdapter();
		
		LockChange = false;
		setWorkState(drBill == null || drBill.getIntVal("state")== 0 ? WorkState.Normal : WorkState.Done);
		UpdateStats();

        if(dtDetail.getRowCount() > 0)
        	mViewPager.setCurrentItem(1);
		//lvPD.setSelected(true);
	}

    @Override
    public synchronized void DecoderResult(String result) {
		HandleSNo(result);
    }
    
    @Override
    public void HandleSNo(String code) {
		try {
			AddSNo(code);
		} catch (Exception e) {
			ExProc.Show(e);
		}
    }
	
	public void CheckBeforeEdit() {
		if(SysData.StockID == 0) ExProc.ThrowMsgEx("尚未设置库位！");
		if(SysData.CustID == 0) ExProc.ThrowMsgEx("尚未设置客户！");
	}

    protected void EditRow(int position) {
    }

    protected void UpdateRow(int BDID, ParamList pl) throws Exception {
    	Bill.TrimParamToExt (pl);
    	boolean isAdd = BDID <= 0;

		if(mBID == 0) CreateBill();
		
    	if(isAdd) {
    		drDetail = dtDetail.newRow();
    		drDetail.updateValue("BDID", DBLocal.getMaxID(dtDetail) + 1);
    		pl.remove("BDID");
    		
    		if(BTID == Bill.BTID_WTDX)
    			ProductRelpace.AppendExtReps(SysData.CustID, pl.IntValue("IID"), pl);
    	}
    	else 
			drDetail = dtDetail.FindRow("BDID", BDID);
    	pl.updateDataRow(drDetail);
		
		drDetail.updateValue("BID", mBID);	 
                 
		if(isAdd) dtDetail.addNewRow(drDetail);
		
		setChanged(true);
		ActSave();
		
		RefreshList();
    	Speech.Speak("[n1]" + drDetail.getStrVal("SNo"));

    	SetWorking();
    	
        listDetailAdapter.Select(drDetail);
        if(mViewPager != null && mViewPager.getCurrentItem() == 0)
        	mViewPager.setCurrentItem(1);
    }
	
    protected void AddSNo(String code) throws Exception {
    //	if(mWorkState != WorkState.Normal) return;

		String sn = pu.getSNo(code);
    	CheckBeforeEdit();
		drDetail = dtDetail.FindRow("SNo", sn);
        if(drDetail == null)
        {
        	//final ParamList item_data = WS.GetProductTraceInfo(sn + ":sale#" + CustID);
	    	//item_data = WS.GetProductTraceInfo(sn );
        	DataRow dr = Bill.GetSNInfo(sn);
        	ParamList pl = null;
	    	if(dr == null) {
	    		pl = pu.GetProdcutBySN(code);
	    		if(pl == null) ExProc.ThrowMsgEx(String.format("数据库没有该流水号[%s]的记录！", sn));
	    	}
	    	else
	    		pl = dr.toParamList();
	    	pl.SetValue("SP", Bill.GetCustSP());
	    	pl.SetValue("Price", Bill.GetPlanPrice(SysData.CustID, pl.IntValue("FItemID")));
	    	pl.RenKeys("FSerialNo SNo, FBatchNo BatchNo, FItemID IID, SName FName");
	    	pl.SetValue("Qty", 1);
	    	UpdateRow(-1, pl);
        }
        else {
            listDetailAdapter.Select(drDetail);
        	Speech.Speak("已添加");
		}
    }
    
    public static void DeletePdfFile() {
		String[] pdfFiles = FileUtil.searchFiles(pu.TEMP_PATH, "*.pdf");
		for (String fn : pdfFiles)
			FileUtil.DeleteFile(fn);
	}

	public void ActSubmit() {
		try {
			if(dtDetail.RowCount() == 0) {
				mViewPager.setCurrentItem(1);
				ExProc.ThrowMsgEx("单据不能为空！");
			}
			CheckBeforeEdit();
    		if(Msgbox.Ask(this, "确认提交数据吗？") == false) return;

	    	ActSave();
	    	
	    	// 提交上传数据
			int ID = WS.UploadBill(getUploadDataParamList());

			//if(BID < 0)
			{
				// 更新BID,更新是否已提交标记
	    		DBLocal.ExecSQL("Update Bill set BID=?, state=1 where BID=?", ID, mBID);
	    		DBLocal.ExecSQL("Update BillDetail set BID=? where BID=?", ID, mBID);
	    		drBill.setValue("BID", ID);
	    		drBill.setValue("state", 1);
	    		dtDetail.setValues("BID", ID);
	    		mBID = ID;
			}
    		
    		DeletePdfFile();
			
        	gc.HintTd("提交" + HintName + "记录完成！");
        	
			setWorkState(WorkState.Done);
			hasChanged = true;
			
        	// 导出报表
			ExportReport();
        	//InnerClearBill();
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public void ActSort(String key) {
		if(key.equals("PName")) SortList("FName");
		if(key.equals("Spec")) SortList("FModel");
		if(key.equals("ExpDate")) SortList("FPeriodDate");
	}
	
	public void SetWorking() {
		if(mWorkState != WorkState.Done) return;
		try {
			drBill.updateValue("state", 0);
			ActSave();
			setWorkState(WorkState.Normal);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
	public ParamList getUploadDataParamList() throws Exception {
		ParamList pl = new ParamList();
    	DataTable dtCloneBill = dtBill.Clone();
    	dtCloneBill.addRow(drBill);
    	pl.SetSerializeValue("billContent", dtCloneBill.Copy("BID, BillNo, BTID, UID, YSID, Ext, BDate"));
    	pl.SetSerializeValue("detailContent", dtDetail.Copy("BID, BDID, SNo, IID, Note, Price, Qty, FNumber, FName, FModel, BatchNo, FPeriodDate, FKFDate, FKFPeriod, Ext, state"));
    	return pl;
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
		dtDetail.Sort(DataSort.getSortName(colName, IsAscSort));
	}
	
	public void PrintFile(String pathName) {
		if(!FileUtil.FileExists(pathName)) return;
		SysOpen.OpenFile(this,pathName);
	}
	
	public void AppendMiniTemplate() {
		if(dtRpt == null) dtRpt = new DataTable("rt", "RTID/i|RTName");
		DataRow dr = dtRpt.newRow();
		dr.setValue("RTID", 0);
		dr.setValue("RTName", "打印小票");
		dtRpt.addRow(0, dr);
	}
	
	public void PrtMiniRpt(String RTName) {
		try {
			if(RTName.isEmpty()) return;
			HyPrt.Print(new gi.IFunc<DataList>() {

			    public  DataList Call() {
					DataList lsData = new DataList();
					
					String title = getPrintTitle();
					HyPrt.addTextToListData(lsData, HyPrt.getCenterText(title, 2), 2);
					
					String text = gs.JoinArray(new String[] {
						title.isEmpty() ? null : " ",
						"时间：" + gv.getFormatDate(gv.FMT_YMDHM),
						SysData.StockName,
						getPrintHeader(),
						//PrtPortable.getSepText(),
						getPrintDetail(),
						getPrintEnd(),
						HyPrt.getFeedingEnd(),
					}, "\n") + "\n";
					
					HyPrt.addTextToListData(lsData, text, 1);
					
					return lsData;
			    }
			});
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public String getPrintTitle() {
		return "";
	}

	public String getPrintHeader() {
		return "";
	}

	public String getPrintEnd() {
		return "业务员：" + SysData.op_name + "\n";
	}
	
	/**
	 * 获取替换产品使用单明细 
	 */
	public static DataTable getReplBillDetail(DataTable dtDetail) {
		DataTable dt = dtDetail.Copy();
		for (DataRow dr : dt.getRows().toArray(new DataRow[0])) {
			Object[] reps = ProductRelpace.getExtParamReps(dr.getValue("Ext"));
			if(reps != null) {
				for (Object o : reps) {
					DataRow drRep = dt.addRow(dr.ItemArray);
					ParamList pl = type.as(o, ParamList.class);
					pl.setDataRow(drRep, false);
				}
				dr.Remove();
			}
		}
		return dt;
	}
	
//	public String getPrintDetail1() {
//		StringBuilder sb = new StringBuilder();
//		SelectColList selects = SqlUtils.ParserSqlSelect("FModel, Price, Sum(Qty), Sum(Qty*Price) Amount");
//		DataTable dt = getReplBillDetail(dtDetail).GroupBy(selects);
//		dt.getColumn("Amount").setDataType(DType.Money);
//
//		int restWidth = HyPrt.MAX_LINECHARS_1X;
//		PrtFieldList prtFields = new PrtFieldList();
//		for (SelectColInfo selCol : selects) {
//			PrtField pfSrc = RptMini.find(selCol.Alias);
//			if(pfSrc != null)  restWidth -= pfSrc.Len;
//			PrtField pf = new PrtField(
//					selCol.Alias,
//					pfSrc == null ? selCol.Alias : pfSrc.Disp,
//					pfSrc == null ? 0 : pfSrc.Len,
//					pfSrc == null ? TextAlign.LEFT : pfSrc.Align
//				);
//			pf.ColInfo = selCol;
//			prtFields.add(pf);
//		}
//
//		for (PrtField pf : prtFields)
//			if(pf.Len == 0) pf.Len = restWidth;
//
//		// 输出表头
//		for (PrtField pf : prtFields) {
//			sb.append(gs.padStrA(pf.Disp, pf.Len, pf.Align));
//		}
//
//		sb.append("\n");
//		sb.append(HyPrt.getSepLine());
//
//		// 输出表单
//		for (DataRow dr : dt.getRows()) {
//			dr.setValue("Amount", dr.getFVal("Price") * dr.getIntVal("Qty"));
//			for (PrtField pf : prtFields) {
//				sb.append(gs.padStrA(RptMini.StrVal(dr.$(pf.Name)), pf.Len, pf.Align));
//			}
//			sb.append("\n");
//		}
//
//		// 输出表尾
//		sb.append(HyPrt.getSepLine());
//
//		for (PrtField pf : prtFields) {
//			Object value = pf == prtFields.get(0) ? "合计:" :
//						   pf.ColInfo.isStat() ? dt.getRows().Sum(pf.Name) :
//						   "";
//			sb.append(gs.padStrA(RptMini.StrVal(value), pf.Len, pf.Align));
//		}
//		_D.Out(sb);
//		return sb.toString();
//	}

	public void getPrtRowValues(StringBuilder sb, DataRow dr, String[] cols, String[] colDisps) {
		for (int i = 0; i < cols.length; i++) {
			String s = gv.StrVal(dr.getValue(cols[i]));
			if(gv.IsEmpty(s)) continue;

			if(colDisps[i].length() > 0) s = colDisps[i] + "：" + s;
			AppendLine(sb, s);
		}
	}

	public void AppendLine(StringBuilder sb, String s){
		sb.append(s);
		if(s.length() < HyPrt.MAX_LINECHARS_1X)
			sb.append("\n");
	}

	public String getPrintDetail() {
		StringBuilder sb = new StringBuilder();

		sb.append(HyPrt.getSepLine());

		for (DataRow dr : dtDetail.getRows()) {
			AppendLine(sb, GetLRLine("No." + dr.getStrVal("SNo"), dr.getMoneyVal("Price")));
			getPrtRowValues(sb, dr,
					new String[] { "FName", "FModel", "FBatchNo", "FPeriodDate", "Manuf", "Reg" },
					new String[] { "", 		"型号", 	"批号", 	"效期", 		"", 	"" });
			sb.append("\n");
		}

		// 输出表尾
		sb.append(HyPrt.getSepLine());

		sb.append(GetLRLine("合计：  X " + dtDetail.getRowCount(), gv.FmtMoney(gv.FVal(dtDetail.getRows().Sum("Price")))));
		return sb.toString();
	}

	public String GetLRLine(String sLeft, String sRight){
		int len = HyPrt.MAX_LINECHARS_1X;
		int i = len - gs.getTextLenA(sLeft) - gs.getTextLenA(sRight);
		String s = sLeft;
		if(i > 0) s += gs.nChar(' ', i);
		s += sRight;
		return s;
	}
	
	protected void ExportReport()throws Exception {
		String[] pdfFiles = FileUtil.searchFiles(pu.TEMP_PATH, "*.pdf");
		final String fheader = "[" + mBID + "].";
		for (String fn : pdfFiles) {
			if(fn.indexOf(fheader) < 0) FileUtil.DeleteFile(fn);
		}
		if(dtRpt == null) {
			dtRpt = WS.GetRptTemplate(SysData.StockID, BTID);
			AppendMiniTemplate();
		}
		if(dtRpt.isEmpty()) ExProc.ThrowMsgEx("当前模块没有报表模板！");

		ParamList pl = new ParamList("stay, buttons:[Close], min-ih:45dp");
		Msgbox.Choose("选择模板", dtRpt.getColStrValues("RTName"), pl, new DialogInterface.OnClickListener(){
	  	    public void onClick(DialogInterface dialog, int which) {  
	  			if(which < 0) return;
	  			DataRow dr = dtRpt.getRow(which);
	  			String RTName = dr.getStrVal("RTName");
	  			String fileName = fheader + RTName + ".pdf";
	  			String fileNames = fileName;
	  			
	  			int RTID = dr.getIntVal("RTID");
	  			
	  			// 打印小票
	  			if(RTID == 0) {
	  				PrtMiniRpt(RTName);
	  				return;
	  			}

	  			String RIDs = gv.StrVal(RTID);
	  			if(RIDs.isEmpty()) return;
	  		
	  			String pathName = pu.TEMP_PATH + fileName;
	  			if(!FileUtil.FileExists(pathName))
	  			{
	  				//WS.GetCloudReport(BID, RIDs, fileNames);
	  				
//	  				ParamList plReport = getUploadDataParamList();
//	  				plReport.SetValue("RIDs", RIDs);
//	  				plReport.SetValue("fileNames", fileNames);
	  				try {
	  		  			// 回传数据到云端生成报表
		  				WS.GetCloudReportV2(mBID, RIDs, fileNames);
		  				
		  				gc.Hint(MyApp.CurrentActivity(), "报表导出完成");
					} catch (Exception e) {
						ExProc.Show(e);
					}
	  			}
	  			PrintFile(pathName);
	  	    }	
	  	});
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	try {
        	//ShowSoftInput(false);
        	ShowSoftInput(false);

    		//toggleSoftInput();
            if(resultCode == RESULT_OK) {
            	if(requestCode == ActBase.REQ_CODE_SEARCH) {
                	//int tid = data.getIntExtra("tid", 0);
                	//if(tid == Search.CUST) SetCust(ActSearchItem.drSelected);
                }
                if(requestCode == REQ_CODE_EDIT) {
                	SetWorking();
                	ParamList plValues = new ParamList(data.getStringExtra("values"));
                	UpdateRow(data.getIntExtra("ID", 0), plValues);
                }
            }
		} catch (Exception e) {
			ExProc.Show(e);
		}
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void loadLastInput() {
		ParamList pl = Setting.GetUserSettingParams(SCID.getLastBillInput(BTID));
		if(pl == null) return;
		LockChange = true;
        for (Map.Entry<String, Object> item : pl.entrySet()) {
        	int id = gv.IntVal(item.getKey());
        	$$Set(id, item.getValue());
        }		
		LockChange = false;
	}

    protected void saveLastInput() {
		Object[] items = plHeader.$("items");
		if(items == null) return;
		ParamList plResult = new ParamList();
		for (Object item  : items) {
			ParamList pl = type.as(((Param)item).Value, ParamList.class);
			if(pl == null) continue;
			String def = pl.SValue("def");
			if(def.equalsIgnoreCase("last")) {
				int id = pl.IntValue("id");
				plResult.set(String.valueOf(id), $T(id));
			}
		}
		Setting.SetUserSetting(SCID.getLastBillInput(BTID), plResult);
	}
}
