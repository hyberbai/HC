package com.hc.mo.bill;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;

import com.dev.HyScanner;
import com.hc.ActBase;
import com.hc.R;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.data.DataTable;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.ui.dialog.UCCreator;
import hylib.ui.dialog.UICreator;
import hylib.util.ActionInfo;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.widget.HyEvent;
import hylib.widget.HyEvent.LvItemClickEventParams;
import hylib.widget.HyEvent.LvItemLongClickEventParams;
import hylib.widget.HyListAdapter;
import hylib.widget.HyListView;

/*
 * 查看历史数据
 */
public abstract class ActHisList extends ActBase {
	protected HyListView lvList;
	protected ViewPager mViewPager;
	protected DataTable dtHis;
	protected DataRowCollection mRows;
	protected String mTitle;

	protected DataRow drItem;
	protected HyListAdapter mAdapter;
	protected int LID_START = 10000;
	protected int[] CIDs;
	protected String pageNames;
	protected SparseArray<HyListView> mapLV = new SparseArray<HyListView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DecoderMode = HyScanner.Mode.Ignore;
    	setInitParams();
        super.onCreate(savedInstanceState);
        
        try {
            InitViews();
		} catch (Exception e) {
			ExProc.Show(e);
		}

        try {
            LoadData();
            mRows = dtHis.getRows();
            FillPages();
		} catch (Exception e) {
			ExProc.Show(e);
		}
    }

	@Override
	protected void setOptionsMenuParams(ParamList pl) throws Exception {
		final DataRowCollection rows = new DataRowCollection("name|text|pl", new Object[][] {
			new Object[] { "Refresh", "刷新", null },
		});
		
		pl.set("Items", rows);
		pl.set("width", "120dp");
	}

    protected abstract void setInitParams();
    
    protected void InitViews() throws Exception {
		CreateOptionsPopupMenu();
		
		setContentView(R.layout.activity_empty);
        $Set(R.id.tv_title, mTitle);
        $(R.id.ll_bottom).setVisibility(View.GONE);
		CreateViewPager();
		CreateEditBar("ToggleSelAll Delete Stats More");
	}
    
    protected void CreateViewPager()  {
    	try {
    		ParamList plViewPager = new ParamList();
    		plViewPager.set("PageSelected", new EventHandleListener() {
    			
    			@Override
    			public void Handle(Object sender, ParamList arg) throws Exception {
    				onPageSelected(sender, arg);
    			}
    		});

    		setViewPagerParams(plViewPager);
    		
    		// 创建分页
            mViewPager = UICreator.CreateViewPager(this, getRootLayout(), null, plViewPager);
		} catch (Exception e) {
			ExProc.Show(e);
		}
    }
	
	public DataRowCollection getSelected() {
		return mRows == null ? null : mRows.SelectRows("sel", true);
	}

	public int[] getSelectedIDs(DataRowCollection rows) {
		return rows.getColIntValues("ID");
	}
	
	public int[] getSelectedIDs() {
		return getSelected().getColIntValues("ID");
	}
    
    protected void onPageSelected(Object sender, ParamList arg) {
		//ShowSoftInput(false);
    	int pos = arg.IntValue("pos");
    	lvList = mapLV.get(CIDs[pos]);
    	mAdapter = (HyListAdapter)lvList.getAdapter();
    	mRows = mAdapter == null ? null : ((DataRowCollection)mAdapter.getListItems());
	}
    
    public HyListView CreateList(int CID) throws Exception {
		ParamList pl = new ParamList("w: match, h:1w");
		pl.SetValue("id", LID_START + CID);
		HyListView lv = UCCreator.CreateListView(this, pl);
		mapLV.put(CID, lv);
		return lv;
    }
    
    protected abstract Object getListItemViewConfig();
    protected abstract void setListItemViewData(ParamList arg);
    protected void onListItemClick(HyListView s, HyEvent.LvItemClickEventParams arg) throws Exception {
    }

	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isEditMode) {
            	setEditMode(false);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    
    public void AdapterNodify() {
    	for (int CID : CIDs) {
    		HyListView lv = mapLV.get(CID);
    		HyListAdapter apt = (HyListAdapter)lv.getAdapter();
        	apt.notifyDataSetChanged();
		}
	}

    protected void toggleSelect() throws Exception {
    	if(drItem == null) return;
    	drItem.setValue("sel", !drItem.getBool("sel"));
    	AdapterNodify();
    }

    public void ActToggleSelAll() {
    	ActionInfo act = ACL.get("ToggleSelAll");
    	act.Checked = !act.Checked;
    	act.setProps(
    			new Param("title", act.Checked ? "取消全选" : "全选"),
    			new Param("res", act.Checked ? R.drawable.muti_select_gray : R.drawable.muti_select)
    		); 
		mRows.setColValue("sel", act.Checked);
		AdapterNodify();
	}
    
    protected void setEditMode(boolean isSelect) {
    	if(isEditMode == isSelect) return;
		isEditMode = isSelect;
		if(isSelect) {
			dtHis.setColValue("sel", false);
	    	ActionInfo act = ACL.get("ToggleSelAll");
	    	act.Checked = false;
		}

		mToolbar.setVisibility(isSelect ? View.VISIBLE : View.GONE);
		AdapterNodify();
    }
	
    protected void onListItemLongClick(HyListView s, HyEvent.LvItemLongClickEventParams arg) throws Exception {
    	setEditMode(true);
    }
    
    protected DataRowCollection getListItems(int CID) throws Exception {
		return CID == 0 ? dtHis.getRows() : dtHis.SelectRows("CID", CID);
    }
    
    public void FillPageItem(int CID) throws Exception {

		HyListView lv = mapLV.get(CID);
		HyListAdapter apt = (HyListAdapter)lv.getAdapter();
		DataRowCollection rows = getListItems(CID);

		if(apt == null) {
			ParamList pl = new ParamList();
			pl.SetValue("items", rows);
	
	        pl.SetValue("itemViewConfig", getListItemViewConfig());
	
	        pl.SetValue("setViewData", new EventHandleListener() {
	        	
	        	@Override
	        	public void Handle(Object sender, ParamList arg) throws Exception {
	        		setListItemViewData(arg);
	        	}
	        });
	        
	        
	        pl.SetValue("onEvent", new EventHandleListener() {
				
				@Override
				public void Handle(Object sender, ParamList arg) throws Exception {
					HyListView lv = (HyListView)sender;
					mAdapter = (HyListAdapter)lv.getAdapter();
					drItem = (DataRow)(mAdapter.getItem(((HyEvent.LvItemEventParams)arg).getPosition()));
					if(arg instanceof LvItemClickEventParams) 
						onListItemClick(lv, (LvItemClickEventParams)arg); 
					else if(arg instanceof LvItemLongClickEventParams) 
						onListItemLongClick(lv, (LvItemLongClickEventParams)arg);
				}
			});
	        
			UCCreator.setListViewParam(context, lv, pl);
		} else {
			apt.setListItems(rows);
			apt.notifyDataSetChanged();
		}
    }
    
    protected void FillPages() throws Exception {
    	for (int CID : CIDs)
			FillPageItem(CID);
	}
    
    public boolean ActRefresh() {
    	try {
    		LoadData();
    		FillPages();
			return true;
		} catch (Exception e) {
			ExProc.Show(e);
			return false;
		}
	}
    
    protected void setViewPagerParams(ParamList pl) throws Exception {
    	View[] views = new View[CIDs.length];
    	for (int i = 0; i< CIDs.length; i++)
			views[i] = CreateList(CIDs[i]);
		
    	pl.SetValue("pageNames", pageNames);
    	pl.SetValue("items", views);
    }
    
    protected void LoadData() throws Exception {
    }

    public void UpdateRowData() throws Exception {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	try {
            if(resultCode == RESULT_CHANGED) ActRefresh();//UpdateRowData();
		} catch (Exception e) {
			ExProc.Show(e);
		}
        super.onActivityResult(requestCode, resultCode, data);
    } 
}
