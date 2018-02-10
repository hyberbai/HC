package com.hc.mo.pd;

import java.util.Date;

import hylib.data.DataColumn;
import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.data.DataRowState;
import hylib.data.DataSort;
import hylib.data.DataTable;
import hylib.data.SelectColInfo;
import hylib.data.SelectColList;
import hylib.data.SqlUtils;
import hylib.db.SqlDataAdapter;
import hylib.db.SqlHelper;
import hylib.edit.DType;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.HyColor;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits.Speech;
import hylib.toolkits.StringBuilderEx;
import hylib.toolkits.TempV;
import hylib.toolkits._D;
import hylib.toolkits.gc;
import hylib.toolkits.gcon;
import hylib.toolkits.gi;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.toolkits.gi.CallBack;
import hylib.ui.dialog.HyDialog;
import hylib.ui.dialog.Msgbox;
import hylib.ui.dialog.UCCreator;
import hylib.util.ActionInfo;
import hylib.util.CountStat;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.util.TextAlign;
import hylib.widget.HyEvent;
import hylib.widget.HyListAdapter;
import hylib.widget.HyListView;
import hylib.widget.HyEvent.LvItemClickEventParams;
import hylib.widget.HyEvent.LvItemLongClickEventParams;
import android.R.integer;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument.Page;
import android.hardware.Camera.Face;
import android.os.Bundle;
import android.renderscript.Element.DataType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dev.DataList;
import com.dev.HyPrt;
import com.dev.HyScanner;
import com.hc.ActBase;
import com.hc.MainActivityOld;
import com.hc.MyApp;
import com.hc.MyApp.WorkState;
import com.hc.ActHome;
import com.hc.ActMore;
import com.hc.MainActions;
import com.hc.PD;
import com.hc.R;
import com.hc.SysData;
import com.hc.g;
import com.hc.pu;
import com.hc.dal.Bill;
import com.hc.dal.WS;
import com.hc.dal.d;
import com.hc.db.DBHelper;
import com.hc.db.DBLocal;
import com.hc.report.PrtField;
import com.hc.report.PrtFieldList;
import com.hc.report.RptMini;


public class ActPD extends ActBase {
	private DataTable dtPD;
	private DataTable dtInv;
	private DataTable dtDetail;
	private DataRow drPD;
	private DataRow drInv;
	private DataRow drDetail;
	private DataRowCollection InvItems;
	private HyListAdapter listHisAdapter;
	private HyListAdapter listPdAdapter;
	private HyListAdapter listDetailAdapter;
	private TextView tvStat;
	private TextView tvTitle;
	private LinearLayout llList;
	private LinearLayout llDetail;
	private LinearLayout llInv;
	private int mPDID;
	private DlgPdNote dlgPdNote;

	private HyListView lvHis;
	private HyListView lvPD;
	private HyListView lvPdDetail;
	private int PageID;
	
	private final static int PAGE_MAIN = 1;
	private final static int PAGE_DETAIL = 2;
	
	private final static int REQ_CODE_BARCODE = 20;
	private CountStat mStat;
	
	private WorkState mWorkState; 
	
	private int StockID = 0;

	public String RStr(int id) {
		return getResources().getString(id);
	}

	public void setWorkState(WorkState state, int pageID) {
		if(mWorkState == state && PageID == pageID) return;
		mWorkState = state;
		if(pageID > 0) PageID = pageID;
		UpdateActivityState();
		UpdateListAdapter();
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState); 
			setContentView(R.layout.activity_pd);
	        DecoderMode = HyScanner.Mode.Enabled;

			mStat= new CountStat();
			mStat.AddStatItem(100, "库存", CountStat.CST_CONST);
			mStat.AddStatItem(gcon.S_CHECKED, RStr(R.string.s_ok));
			mStat.AddStatItem(gcon.S_WRONG, RStr(R.string.s_wrong));
			mStat.AddStatItem(gcon.S_EXTRA, RStr(R.string.s_extra), CountStat.CST_INC);
			mStat.Rest.Name = "未盘";
			mStat.ChangedListener = new gi.Listener() {
				
				@Override
				public void Listen(Object sender) {
					refreshStatInfo();
				}
			};

			CreateOptionsPopupMenu();
			
			tvTitle = (TextView) this.findViewById(R.id.tv_title);
			
			lvHis = $(R.id.lv_his);
			
			lvPD = (HyListView) this.findViewById(R.id.lv_pd);
			lvPdDetail = (HyListView) this.findViewById(R.id.lv_pd_detail);
			
			tvStat = ((TextView) this.findViewById(R.id.tv_Stat));
			
			llList  = ((LinearLayout) this.findViewById(R.id.ll_list));
			llInv  = ((LinearLayout) this.findViewById(R.id.ll_inv));
			llDetail  = ((LinearLayout) this.findViewById(R.id.ll_detail));

			lvPD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				
		        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    		SelectInvItem(dtInv.getRow(position));
		        	setWorkState(WorkState.Normal, PAGE_DETAIL);
		        }  
		          
		    });  

			lvPD.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
					//UICreator.CreatePopupMenu(MyApp.CurrentActivity(), v, new String[] { "重置", "全部重置" });
					SelectInvItem(dtInv.getRow(position));
		        	ParamList pl = new ParamList("width", "240dp");
					Msgbox.Choose("", new String[] { "重置", "全部重置" }, pl, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(which == 0) ActReset(false); 
							if(which == 1) ActReset(true); 
						}
					});
		        	return true;
		        }
			});

			lvPdDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				
		        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		        	PopDialog(position, true);
		        }  
		          
		    });  

			lvPdDetail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
		        	SelectDetailItem(position);
					//UICreator.CreatePopupMenu(MyApp.CurrentActivity(), v, new String[] { "重置", "全部重置" });
		        	final String SNo = drDetail.getStrVal("SNo");
		        	ParamList pl = new ParamList("width", "240dp");
					Msgbox.Choose("NO." + SNo, new String[] { "删除", "溯源" }, pl,
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if(which == 0)
										try {
											InvItems.Remove(drDetail);
											if(InvItems.size() == 0 && drInv.getValue("state").equals(gcon.S_EXTRA))
											{
												drInv.Delete();
												SelectInvItem(null);
											}
											ActSave();
											UpdateListAdapter();
										} catch (Exception e) {
											ExProc.Show(e);
										}
										
									if(which == 1) MainActivityOld.ShowSY(SNo);
								}
							}
					);
		        	return true;
		        }
			});
			
			LoadData();
			FillHisList();

			CreateEditBar("ToggleSelAll Delete More");
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
	public void refreshStatInfo() {
		try {
			if(drPD != null) {
				ParamList pl = new ParamList(drPD.getStrVal("Ext"));
				pl.SetValue("qty", mStat.getItemCount(100));
				pl.SetValue("ok", mStat.getItemCount(gcon.S_CHECKED));
				pl.SetValue("wrg", mStat.getItemCount(gcon.S_WRONG));
				pl.SetValue("etr", mStat.getItemCount(gcon.S_EXTRA));
				pl.SetValue("rest", mStat.getRestCount());
				pl.SetValue("tot", mStat.getTotalCount());
				drPD.updateValue("Ext", pl);
			}
		} catch (Exception e) {
			ExProc.Show(e);
		}
		tvStat.setText(mStat.Total.Count == 0 ? "" : mStat.getItemsAndRestDesc());
	}

	public void FillHisList() {
		if(listHisAdapter == null) {
			ParamList pl = new ParamList();
			pl.SetValue("items", dtPD.getRows());

			String itemViewConfig = 
					"items: [" + 
		        	"[v: { id:2010, w:16dp,h:16dp,lay-grv:c }, "+ 
		        	"tv: { id:2011, fs: 14.2dp, text: headinfo, w:1w,color:#FF555555,padding: 3dp,h:wrap }, "+ 
		        	"tv: { id:2012, fs: 13.6dp, text: date,w:120dp,bg1:g,color:gray, padding: 3dp, h:wrap, grv:r }] "+

			    	"[v: { id:2036, w:28dp,h:28dp,lay-grv:c, marginRight:5dp }, "+ 
			    	"v: { id:2030, w:48dp,h:48dp,lay-grv:c }, "+ 
					"tv: { id:2031, fs: 14.2dp, text: info,w:5w,bg1:r,color:text, padding: 3dp,h:wrap }, ]"+ 
		        "], padding: 5dp, margin: 0dp, w:match, h:wrap, bg1:#33CCAAFF";
			
	        pl.SetValue("itemViewConfig", itemViewConfig);

	        pl.SetValue("setViewData", new EventHandleListener() {
	        	
	        	@Override
	        	public void Handle(Object sender, ParamList arg) throws Exception {
	        		View convertView = arg.Get("view", View.class);
	        		DataRow dr = arg.Get("item", DataRow.class);
	        		View vHeadImg = convertView.findViewById(2010);
	        		TextView tvHeadInfo = type.as(convertView.findViewById(2011), TextView.class);
	        		TextView tvDate = type.as(convertView.findViewById(2012), TextView.class);
	        		
	        		View vImg = convertView.findViewById(2030);
	        		View vSelect = convertView.findViewById(2036);
	        		TextView tvInfo = type.as(convertView.findViewById(2031), TextView.class);
	        		ParamList pl = new ParamList(dr.$("Ext"));
	        		
	        		tvHeadInfo.setText(d.getStockName(dr.getIntVal("YSID")));
	        		tvDate.setText(gv.SDate(dr.getDate("PDDate"), "yyyy-MM-dd HH:mm"));
	        		
	        		String info = gs.JoinArray(new Object[] {
	        				"库存数量：" + pl.IntValue("qty"),
	        				"正常：" + pl.IntValue("ok") + "，异况：" + pl.IntValue("wrg") + 
	        				"，未盘：" + pl.IntValue("rest") + "，多出：" + pl.IntValue("etr"),
	            			dr.getStrVal("Note"),
	            			SysData.isAdmin ? d.getDispUser(dr.getIntVal("OpID")) : null,
	        		}, "\n");
	        		
	        		boolean readed = dr.getIntVal("Flag") == Bill.FLAG_READED;

	        		vSelect.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
	        		vSelect.setBackgroundResource(dr.getBool("sel") ? R.drawable.c_select : R.drawable.c_none);
	        		
	        		if(info.isEmpty()){
	            		tvInfo.setText("无描述信息");
	            		tvInfo.setTextColor(Color.GRAY);
	        		} else {
	        			tvInfo.setText(info);
	            		tvInfo.setTextColor(readed ? Color.GRAY : HyColor.Text);
	        		}
	        		tvHeadInfo.setTextColor(readed ? Color.GRAY : 0xFF555555);
	        		
	        		tvInfo.setLineSpacing(0, 1.2f);

	        		vHeadImg.setBackgroundResource(dr.getIntVal("state") == 1 ? R.drawable.s_upload : R.drawable.s_upload_gray);
	        		vImg.setBackgroundResource(R.drawable.y4);
	        	}
	        });
	        
	        pl.SetValue("onEvent", new EventHandleListener() {
				
				@Override
				public void Handle(Object sender, ParamList arg) throws Exception {
					HyListView lv = (HyListView)sender;
					HyListAdapter mAdapter = (HyListAdapter)lv.getAdapter();
					drPD = (DataRow)(mAdapter.getItem(((HyEvent.LvItemEventParams)arg).getPosition()));
					if(arg instanceof LvItemClickEventParams) {
						if(isEditMode)
				    		toggleSelect();
				    	else
				    		LoadPdList(drPD.getIntVal("PDID"));
					}
					else if(arg instanceof LvItemLongClickEventParams) 
						onListItemLongClick(lv, (LvItemLongClickEventParams)arg);
				}
			});
	        
			UCCreator.setListViewParam(context, lvHis, pl);
			listHisAdapter = (HyListAdapter)lvHis.getAdapter();
		} else {
			listHisAdapter.setListItems(dtPD.getRows());
		}
	}
    
    protected void setEditMode(boolean isSelect) {
    	if(isEditMode == isSelect) return;
		isEditMode = isSelect;
		if(isSelect) dtPD.setColValue("sel", false);
		mToolbar.setVisibility(isSelect ? View.VISIBLE : View.GONE);
		listHisAdapter.notifyDataSetChanged();
    }

	public DataRowCollection getSelected() {
		return dtPD == null ? null : dtPD.getRows().SelectRows("sel", true);
	}

	public int[] getSelectedIDs(DataRowCollection rows) {
		return rows.getColIntValues("PDID");
	}
	
	public int[] getSelectedIDs() {
		return getSelected().getColIntValues("PDID");
	}
	
    public void ActDelete() {
    	try {
    		DataRowCollection rows = getSelected();
        	if(gv.IsEmpty(rows)) ExProc.ThrowMsgEx("所选不能为空！");
        	
        	if(Msgbox.Ask(context, "确认删除所选的盘点吗？") == false) return;
        	int[] IDs = getSelectedIDs(rows);
    		if(DeletePdData(IDs)) {
    			for (DataRow dr : rows) 
    				dr.Remove();
    			listHisAdapter.notifyDataSetChanged();
    		}
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
    
    protected void onListItemLongClick(HyListView s, HyEvent.LvItemLongClickEventParams arg) throws Exception {
    	setEditMode(true);
    }

//    protected void onListItemLongClick(HyListView s, HyEvent.LvItemLongClickEventParams arg) throws Exception{
////    	ParamList pl = new ParamList("width:240dp, item-height:48dp");
////		Msgbox.Choose("", new String[] { "删除当前单据" }, pl,
////				new DialogInterface.OnClickListener() {
////					@Override
////					public void onClick(DialogInterface dialog, int which) {
////						if(which == 0) {
////							if(Msgbox.Ask(context, "确认删除本次盘点吗？") == false) return;
////							DeletePdData();
////						}
////					}
////				}
////		);
//    }
    
	public void DeleteDetailItems(DataRowCollection items) {
		for (DataRow dr : items) {
			dr.Delete();
		}
	}
    
	public void ActNew() {
		try {
			final TempV tmpCurrent = new TempV();
			DataRowCollection  rows = SysData.dtUserStock.SelectRows(new gi.IFunc1<DataRow, Boolean>() { 
				public Boolean Call(DataRow dr) {
					if(dr.getIntVal("FItemID") == SysData.StockID) {
						tmpCurrent.set(dr);
						return false;
					}
					return dr.getStrVal("FName").indexOf("库") > 0;
				}
			});
			if(tmpCurrent.hasValue()) rows.add(0, (DataRow)tmpCurrent.get());
			
			int choice = Msgbox.Choose("选择盘点库位", rows.getTextRows("FName"), new ParamList("width:300dp"));
			if(choice <0) return;
			DataRow dr= rows.get(choice);
			StockID = dr.getIntVal("FItemID");
			tvTitle.setText("盘点 - " + dr.getValue("FName"));
			CreateNewPd(StockID);
		} catch (Exception e) {
			ExProc.Show(this, e);
		}
	}
	
	public int GetPdTotal(){
		int total = 0;
		for (DataRow dr : dtInv.getRows()) total += dr.getIntVal("Qty"); 
		return total;
	}
	
	public DataRowCollection getNewDetailRows() {
		return new DataRowCollection(dtDetail);
	}
	
	public void UpdatePdAdapter() {
		listPdAdapter = new ListPd2Adapter(this, dtInv.getRows()); 
		lvPD.setAdapter(listPdAdapter);
		lvPD.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listPdAdapter.onNotify = listAdapterNotify;
	}
	
	public void LoadData() throws Exception {
		String cond = SysData.isAdmin ? "1=1" : "OpID=" + SysData.op_id;
		//if(SysData.StockID > 0) cond += " and YSID=" + SysData.StockID;
		dtPD = DBLocal.OpenSingleTable("PD", "select rowid row, * from PD where " + cond);
		
		dtPD.addColumn("sel", DType.Bool);
		
		dtPD.getColumn("PDID").setPK(true);
		dtPD.Sort("row desc");

		int PDID = Params.IntValue("ID");

		if(PDID == 0) {
			DataRow dr = dtPD.firstRow();
			if(dr != null && dr.getIntVal("state") == 0) 
				PDID = dr.getIntVal("PDID");
		}

		LoadPdList(PDID);
		if(mPDID == 0)
			setWorkState(WorkState.Ready, 0);
	}
	
	private void AddInvTableExtFields(){
		dtInv.addColumn("Items", DType.Ext);// 盘点明细项
		dtInv.addColumn("Stat", DType.Ext);	// 盘点统计
	}

	// 创建一个库存盘点表
	public void CreateNewPd(int StockID){
    	try {
    		mPDID = DBLocal.getMinID("PD", "PDID");
    		if(mPDID < 0) mPDID--; else mPDID = -1001;

    		// 远程获取库位盘点数据
    		dtInv = WS.GetPDList2(StockID);
    		dtInv.addColumn("PDIID", 0);
    		dtInv.addColumn("PDID", 0);
    		dtInv.addColumn("State", 0);	// 状态
    		dtInv.addColumn("Note", 0);		// 备注
    		AddInvTableExtFields();
    		setDtInvBaseParams();

    		// 创建盘点记录
    		drPD = dtPD.newRow();
    		drPD.setValue("PDID", mPDID);
    		drPD.setValue("PDName", gv.getFormatDate("yyyy年M月d日盘点"));
    		drPD.setValue("YSID", StockID);
    		drPD.setValue("PDDate", new Date());
    		drPD.setValue("Note", "");
    		drPD.setValue("state", 0);
    		drPD.setValue("OpID", SysData.op_id);
    		dtPD.addRow(0, drPD);
    		
    		int PDIID = DBLocal.getMaxID(dtInv);
    		for (DataRow dr : dtInv.getRows()) {
    			dr.setState(DataRowState.Added);
				dr.setValue("PDIID", ++PDIID); 
				dr.setValue("PDID", mPDID); 
				dr.setValue("State", 0); 
				dr.setValue("Items", getNewDetailRows()); 
			}
    		
    		// 加载盘点明细表
    		LoadPdDetail();

    		UpdatePdAdapter();
    		setWorkState(WorkState.Normal, PAGE_MAIN);
    		
    		UpdateStat();
    		
    		// 保存盘点数据
    		ActSave();
    		
    		//lvPD.setSelected(true);
		} catch (Exception e) {
			ExProc.Show(this, e);
		}
	}
	
	public void UpdateStat(){
		if(drPD == null) return;
		PD.StatQtyInfo total = new PD.StatQtyInfo();
		for (DataRow dr : dtInv.getRows())
		{
			PD.StatQtyInfo sq = PD.StatRowQty(dr);
			dr.setValue("Stat", sq);
			total.Inc(sq);
		}

		mStat.LockChange = true;
		mStat.SetTotal(total.getQty());
		mStat.setItemCount(100, total.getQty());
		mStat.Inc(gcon.S_CHECKED, total.getOkQty());
		mStat.Inc(gcon.S_CHECKED, total.getOkQty());
		mStat.Inc(gcon.S_WRONG, total.getWrongQty());
		mStat.Inc(gcon.S_EXTRA, total.getExtraQty());
		mStat.LockChange = false;
		mStat.DoChanged();
	}

	public void LoadPdList(int PDID) throws Exception {
		drPD = dtPD.FindRow("PDID", PDID);
		mPDID = drPD == null ? 0 : PDID;
		StockID = drPD == null ? 0 : drPD.getIntVal("YSID");
		tvTitle.setText(StockID > 0 ? "盘点" : "盘点 - " + d.getStockName(StockID));
		
		String sql = "select * from PdInventory pi join Item ic on pi.FItemID=ic.FItemID"  +
    				 " where PDID=? order by pi.state asc,FNumber"; 
		
		dtInv = DBLocal.OpenTable(DBHelper.Cfg_PdInventory + "|" + DBHelper.Cfg_Item, sql, mPDID);
		setDtInvBaseParams();
		AddInvTableExtFields();

		LoadPdDetail();

		// 加载每个产品对应的盘点明细
		for (DataRow dr : dtInv.getRows()) {
			DataRowCollection items = dtDetail.SelectRows("IID", dr.getValue("FItemID"));
			dr.setValue("Items", items); 
		}

		UpdatePdAdapter();
		
		UpdateStat();

		if(drPD == null) return;
		if(drPD.getIntVal("state") == 1)
			setWorkState(WorkState.Done, PAGE_MAIN);
		else
			setWorkState(WorkState.Normal, PAGE_MAIN);
		//lvPD.setSelected(true);
	}
	
	private void SelectInvItem(DataRow dr) {
		if(drInv == dr) return;
		drInv = dr;
		InvItems = drInv == null ? new DataRowCollection(dtDetail) : 
				  (DataRowCollection)drInv.getValue("Items");
		if(mWorkState == WorkState.Normal)
			listPdAdapter.Select(drInv);
	}
	
	public void SelectDetailItem(int position){
		if(InvItems != null) drDetail = InvItems.get(position);	
		SelectDetailItem();
	}
	
	public void SelectDetailItem(){
		if(PageID == PAGE_DETAIL)
			listDetailAdapter.Select(drDetail);
	}
	
	private DataRow drLastInv;
	public void setPdDetailAdapter() {
		if(listDetailAdapter != null && drLastInv == drInv) {
			listDetailAdapter.Select(drDetail);
			return;
		}
		drLastInv = drInv;
		// 盘点明细表
		listDetailAdapter = new ListPdDetailAdapter(this, InvItems);
		lvPdDetail.setAdapter(listDetailAdapter);
		lvPdDetail.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listDetailAdapter.Select(drDetail);
		listDetailAdapter.onNotify = listAdapterNotify;
	}

	private gi.NotifyListener listAdapterNotify = new gi.NotifyListener() {
		
		@Override
		public Object Notify(Object sender, Object arg) {
			ListView lv = sender == listPdAdapter ? lvPD : lvPdDetail;
			int pos = (Integer)arg;
            int p0 = lv.getFirstVisiblePosition();
            int p1 = lv.getLastVisiblePosition();
            if(pos < p0 || pos > p1)
            	lv.setSelectionFromTop(pos, lv.getHeight() * 4 / 10);
			return null;
		}
	};

	private String LastSortCol = "";
	private boolean IsAscSort;
	protected void SortList(String colName) {
		IsAscSort = LastSortCol.equals(colName) ? !IsAscSort : true;
		DataTable dt = dtInv;
		
		dt.Sort(DataSort.getSortName(colName, IsAscSort));
		listPdAdapter.notifyDataSetChanged();
		LastSortCol = colName;
	}

	protected void SortList1(String sortItems) {
		String[] items = sortItems.split(",");
		
    	gs.CutResult r = gs.Cut(items[0], " ");
		IsAscSort = LastSortCol.equals(r.S1) ? !IsAscSort : true;
		items[0] = r.S1 + (IsAscSort ? "" : " desc");
		DataTable dt = dtInv;
		dt.Sort(gs.JoinArray(items, ","));
		listPdAdapter.notifyDataSetChanged();
		LastSortCol = r.S1;
	}
	
	public void ActSort(String key) {
		if(key.equals("PName")) SortList("FName");
		if(key.equals("Spec")) SortList("FModel");
		if(key.equals("ExpDate")) SortList("FPeriodDate");
		if(key.equals("State")) SortList1("State, FName, FModel");
	}
	
	@Override
	protected void setOptionsMenuParams(ParamList pl) throws Exception {
		final DataRowCollection rows = new DataRowCollection("name|text|pl", new Object[][] {
				new Object[] { "HandInput", "手工录入", null },
				new Object[] { "Sort#PName", "产品名称排序", null },
				new Object[] { "Sort#Spec", "规格型号排序", null },
				new Object[] { "Sort#State", "盘点状态排序", null },
				//new Object[] { "Sort#ExpDate", "有效期至排序", null },
		});
		AppendDefaultMenu(rows);
		pl.set("Items", rows);
		pl.set("width", "120dp");
	}
    
    @Override
    public void HandleSNo(String SNo) {
    	AddItem(SNo);
    }
	
	private void setDtInvBaseParams() {
		dtInv.setTableName("PdInventory");
		dtInv.setPKColumn("PDIID");
	}
	
	public void LoadPdDetail() throws Exception {
		String sql = "select * from PdDetail pi, Item ic "  +
    				 " where pi.IID=ic.FItemID and PDID=?" +
    				 " order by FNumber";
		dtDetail = DBLocal.OpenTable(DBHelper.Cfg_PdDetail + "|" + DBHelper.Cfg_Item, sql, mPDID);
		dtDetail.setTableName("PdDetail");
		dtDetail.getColumn("PDDID").setPK(true);
	}
	
	private void UpdateActivityState() {
		if(mWorkState == WorkState.Normal && drPD != null && drPD.getIntVal("state") == 1) mWorkState = WorkState.Done;
		boolean isMain = mWorkState == WorkState.Normal && PageID == PAGE_MAIN;
		llList.setVisibility(mWorkState == WorkState.Ready ? View.VISIBLE : View.GONE);
		llInv.setVisibility(mWorkState != WorkState.Ready && PageID == PAGE_MAIN ? View.VISIBLE : View.GONE);
		llDetail.setVisibility(mWorkState != WorkState.Ready && PageID == PAGE_DETAIL ? View.VISIBLE : View.GONE );
		$(R.id.ib_cancel).setVisibility(isMain ? View.VISIBLE : View.GONE );
		$(R.id.ib_submit).setVisibility(isMain ? View.VISIBLE : View.GONE );
		$(R.id.ib_reset).setVisibility(mWorkState == WorkState.Normal ? View.VISIBLE : View.GONE );
		$(R.id.ib_detail_reset).setVisibility(mWorkState == WorkState.Normal ? View.VISIBLE : View.GONE );
		$(R.id.ib_close).setVisibility(mWorkState == WorkState.Done ? View.VISIBLE : View.GONE );
		$(R.id.ib_print).setVisibility(mWorkState == WorkState.Done ? View.VISIBLE : View.GONE );
		
		$(R.id.ib_reset).setVisibility(View.GONE );
		if(mWorkState == WorkState.Ready && listHisAdapter != null) listHisAdapter.notifyDataSetChanged();
	}
	
	private void UpdateListAdapter() {
		if(mWorkState == WorkState.Normal) {
			drDetail = null;
			listPdAdapter.Select(drInv);
		}
		if(PageID == PAGE_DETAIL)
		{
			setPdDetailAdapter();
			listPdAdapter.notifyDataSetChanged();
		}

		if(mWorkState != WorkState.Ready) {
			if(PageID == PAGE_MAIN)
				tvTitle.setText("盘点 - " +  d.getStockName(StockID));
			else if(PageID == PAGE_DETAIL && drInv != null)
				tvTitle.setText("盘点明细 - " +  drInv.getValue("FModel"));
		} else
			tvTitle.setText("库存盘点");
	}
	
	public void Clear() {
		mPDID = 0;
		drPD = null;
		dtInv.ClearRows();
		dtDetail.ClearRows();
		listPdAdapter.notifyDataSetChanged();
		mStat.SetTotal(0);
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	ActBack();
        	return true;
         //   return false;
        } if (keyCode == KeyEvent.KEYCODE_0) {
        	
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private void SetPDState(DataRow dr, int state){
    	int state0 = gv.IntVal(dr.getValue("State"));
    	if(state0 == state) return;
		dr.updateValue("State", state);
		//UpdateListAdapter();
		mStat.ChangeKey(state0, state);
    }

    public static void ScrollListViewToPosition(final ListView lv, final int position) {
        lv.post(new Runnable() {
            @Override
            public void run() {
                ((BaseAdapter)lv.getAdapter()).notifyDataSetChanged();
                lv.setSelection(position);
            }
        });
    }
    
    // 获取流水号信息
    public static ParamList GetSnInfo(String SNo) throws Exception
    {
    	ParamList pl;
    	try {
        	pl = WS.IsConnected() ? type.as(WS.GetProductTraceInfo(SNo), ParamList.class) :
        		Bill.GetLocalSnInfo(SNo);
		} catch (Exception e) {
			if(!ExProc.IsNetException(e)) throw e;
			pl = Bill.GetLocalSnInfo(SNo);
		}
    	//int FItemID = DBLocal.ExecuteIntScalar("select FItemID from SN where FSerialNo=?",  SNo);
    	return pl == null ? new ParamList() : type.as(pl.get("info"), ParamList.class);
    }
    
    public DataRow FindPdInvRow(int FItemID)
    {
    	return dtInv.FindRow("FItemID", FItemID);
    }
    
    public DataRow CreateInvDataRow(ParamList pl) throws Exception {
    	DataRow dr = dtInv.newRow();
		dr.updateValue("PDIID", DBLocal.getMaxID(dtInv) + 1);
		dr.updateValue("PDID", mPDID);
		dr.updateValue("Items", getNewDetailRows()); 
		dr.updateValue("FItemID",  pl.IntValue("FItemID"));
		dr.updateValue("FName",  pl.SValue("FName"));
		dr.updateValue("FUnit",  pl.SValue("FUnit"));		
		dr.updateValue("FModel",  pl.SValue("FModel"));	
		dr.updateValue("Qty", 0);				// 数量
		dr.updateValue("State", gcon.S_EXTRA);	// 状态
		dr.updateValue("Note", "多出产品");		// 备注
		UpdateInvRowState(dr);
		return dr;
	}
    
    public void PopDialog(int position, boolean popNow) {
    	if(InvItems == null) return;
    	if(position >= 0) SelectDetailItem(position);	  

		if(drDetail == null) return;
		if(dlgPdNote != null) {
			dlgPdNote.Refresh();
			return;
		}
		if(!popNow) return;
    	// 弹出盘点情况对话框
    	dlgPdNote = new DlgPdNote(MyApp.CurrentActivity());
		
		dlgPdNote.onNotify = new gi.NotifyListener() {
			
			@Override
			public Object Notify(Object sender, Object arg) {
				if(arg.equals(gi.NF_INIT) || arg.equals(gi.NF_REFRESH))
				{
					if(drDetail == null) return null;
		    		dlgPdNote.SetInfo(gs.JoinArray(new String[] {
		    				Param.SVal("No.", drDetail.getValue("SNo")),
		    				drDetail.getStrVal("FName"),
		    				Param.SVal("规　格：　", drDetail.getValue("FModel")),
		    				Param.SVal("批　号：　", drDetail.getValue("BatchNo")),
		    				Param.SVal("产品序号：", drDetail.getValue("PN")),
		    				Param.SVal("生产厂家：", drDetail.getValue("Manuf")),
		    				Param.SVal("生产日期：", drDetail.getValue("MfgDate")),
		    				Param.SVal("有效期至：", drDetail.getValue("ExpDate")),
		    		}, "\r\n"));
		    		dlgPdNote.drItem = drDetail;
		    		dlgPdNote.SetNote(gv.StrVal(drDetail.getValue("Note")));
		    		dlgPdNote.SetState(gv.IntVal(drDetail.getValue("State")));
				}
	    		else if (arg.equals(gi.NF_OK)) {
	    			int state = dlgPdNote.GetState();
	    			String note = dlgPdNote.GetNote();
//	    			if(state == 0) ExProc.ThrowMsgEx("没有选择盘点状态！");
	    			if(state == gcon.S_WRONG && note.isEmpty())
	    				ExProc.ThrowMsgEx("请在备注中说明导致异况的原因！");
	    			
	    			drDetail.updateValue("Note", note);
	    			SetPDState(drDetail, dlgPdNote.GetState());
	    			dlgPdNote.setDialogResult(HyDialog.R_OK);
	    		}

				return null;
	    	}
		};
		//dlgPdNote.show();
		Object r = dlgPdNote.showDialog();
		try {
			if(r.equals(HyDialog.R_OK)) {
				UpdateInvRowState(drInv);
				ActSave();
			}
		} catch (Exception e) {
			ExProc.Show(e);
		}
		dlgPdNote = null;
    }  
    
    private void AddItem(String code) {
		String sn = pu.getSNo(code);
    	if(sn.isEmpty() || drPD == null) return;
    	try {
            drDetail = dtDetail.FindRow("SNo", sn);
            
        	ParamList pl = GetSnInfo(sn);
        	if(pl == null) pl = new ParamList();

        	int FItemID = pl.IntValue("FItemID");
        	if(FItemID <= 0)
			{
    			ParamList plProduct =  pu.GetProdcutBySN(code);
    			if(plProduct != null)
    			{
    				plProduct.CopyTo(pl);
    				FItemID = pl.IntValue("FItemID");
    			}
			}

    		SelectInvItem(FindPdInvRow(FItemID));
    		if(drInv == null)
    		{
    			//int FItemID = d.getFItemID(sn);
        		if(FItemID <= 0) ExProc.ThrowMsgEx("无法找到当前流水号“" + sn + "”对应产品！");
    			
    			SelectInvItem(CreateInvDataRow(pl));
    			dtInv.addRow(drInv);
    			//ExProc.ThrowMsgEx("库存产品不包含当前流水号！");
    		}

    	//	drDetail = null;
    		boolean isNew = drDetail == null;

    		if(isNew) {	// 处理不在盘点列表中的流水码
	        	drDetail = dtDetail.newRow();

	        	drDetail.updateValue("PDDID", DBLocal.getMaxID(dtDetail) + 1);
	        	drDetail.updateValue("PDID", mPDID);
	        	drDetail.updateValue("FName", drInv.getValue("FName"));
	        	drDetail.updateValue("FModel", drInv.getValue("FModel"));
	        	drDetail.updateValue("SNo", sn);
	        	drDetail.updateValue("IID", FItemID);
	        	drDetail.updateValue("BatchNo", pl.get("FBatchNo"));
	        	drDetail.updateValue("MfgDate", pl.get("FKFDate"));
	        	drDetail.updateValue("ExpDays", pl.get("FKFPeriod"));
	        	drDetail.updateValue("ExpDate", pl.get("FPeriodDate"));
	        	
	        	PD.StatQtyInfo sq = drInv.$("Stat");
	        	
	        	int state = sq.getPDQty() < sq.getQty() ? gcon.S_CHECKED : gcon.S_EXTRA;
				SetPDState(drDetail, state);
				
				dtDetail.addNewRow(drDetail);

				InvItems.add(drDetail);
				UpdateInvRowState(drInv);
				
				sq = drInv.$("Stat");

//				pu.Speech.Speak("OK");
				String Unit = drInv.getStrVal("FUnit");
				if(Unit.isEmpty()) Unit = "个";
				String stext = state == gcon.S_CHECKED ? "[n0]" + sq.getPDQty() + Unit :
							   state == gcon.S_EXTRA ? "多出[n0]" + sq.getExtraQty() + Unit :
							   "";
				Speech.Speak(stext);
				g.Vibrate(this, 80);
    		}
    		else
    			Speech.Speak("已盘点");

			SelectDetailItem();
			UpdateListAdapter();
			
			if(mWorkState == WorkState.Done) {
				drPD.updateValue("state", 0);
				setWorkState(WorkState.Normal, 0);
			}
			ActSave();
            drDetail = dtDetail.FindRow("SNo", sn);
			PopDialog(-1, isNew);
			
    		//ScrollListViewToPosition(lvPD, drDetail.getRowIndex());
    		//lvPD.setSelection(dr.getRowIndex());
		} catch (SQLiteConstraintException e) {
			gc.Hint("数据库约束异常，系统将重新载入！");
			finish();
			ActHome.ACL.Execute("PD");
		} catch (Exception e) {
			ExProc.Show(this, e);
		}
    }
    
    private void UpdateInvRowState(DataRow drInv){
    	PD.StatQtyInfo sq = PD.StatRowQty(drInv);
		int InvState = sq.getWrongQty() > 0 ? gcon.S_WRONG :
					   sq.getExtraQty() > 0 ? gcon.S_EXTRA :
					   sq.getQty() > 0 && sq.getRestQty() == 0 ? gcon.S_CHECKED :
					   0;
		drInv.updateValue("State", InvState);
		drInv.setValue("Stat", sq);
    }


    private void ResetInvRow(DataRow dr) {
		int state = gv.IntVal(dr.getValue("State"));
		if(state == gcon.S_EXTRA)
			dr.Delete();
		else if (state > 0)
		{
			dr.updateValue("State", gcon.S_NONE);
			dr.updateValue("Note", "");
		}
		drLastInv = null;
	}
    
    private void ActReset(boolean All) {
    	if(Msgbox.Ask(this, "确认将已盘点的数据重置为未检状态吗？") == false) return;
    	
    	try {
    		drDetail = null;
    		if(All) {
        		SelectInvItem(null);
        		// 重置库存
                for (DataRow dr : dtInv.getRows())
                	ResetInvRow(dr);
            	dtDetail.DeleteAll();
    		} else {
    			ResetInvRow(drInv);
            	InvItems.DeleteAll();
    		}
        	ActSave();
    		if(All) {
                for (DataRow dr : dtInv.getRows())
                {
                	DataRowCollection ds = (DataRowCollection)dr.getValue("Items");
                	ds.clear();
                }
    		} else {
            	InvItems.clear();
    		}
    		UpdateListAdapter();
    		UpdateStat();
		} catch (Exception e) {
			ExProc.Show(e);
		}
    }

	public void ActSave() throws Exception {
//		// 创建盘点主表记录
//		if(drPD.getState() == DataRowState.Added)
//		{
//			DBLocal.ExecSQL("Insert into PD(PDID, PDName, YSID, PDDate, Note, state, OpID) " + 
//							"Values(?, ?, ?, ?, ?, ?, ?, ?)", 
//							0,
//							gv.getFormatDate("yyyyMMdd-HHmmss"),
//							pu.stock_id, 
//							new Date(),
//							"", 
//							pu.user_id
//							);
//		}
		
		gi.CallBack Exec = new CallBack() {
			
			@Override
			public Object Call() throws Exception {
				SQLiteDatabase db = DBLocal.getDB();
				
				SqlDataAdapter apt = new SqlDataAdapter(db);
				
				apt.Update(dtPD);

				apt.Update(dtInv);

				apt.Update(dtDetail);

				return true;
			}
		};

		DBLocal.ExecTrans(Exec);
		
//		DataTable dt = DBLocal.OpenSingleTable("PD");
//		int n = dt.RowCount();
//		
//		 dt = DBLocal.OpenSingleTable("PdInventory");
//		 n = dt.RowCount();
	}

	public void ActBack() {
        if(isEditMode)
        	setEditMode(false);
        else if(mWorkState == WorkState.Ready)
			Finish();
		else if(PageID == PAGE_DETAIL)
			setWorkState(WorkState.Normal, PAGE_MAIN);
		else if(PageID == PAGE_MAIN)
			setWorkState(WorkState.Ready, 0);
	}
	
	private boolean DeletePdData(final int... IDs) {
    	try {
    		gi.CallBack Exec = new CallBack() {
    			
    			@Override
    			public Object Call() throws Exception {
    				String sqlIn = SqlHelper.SqlIn(IDs);
    				//int PDID = drPD.getIntVal("PDID");
    	    		DBLocal.ExecSQL("delete from PD where PDID" + sqlIn);
    	    		DBLocal.ExecSQL("delete from PdInventory where PDID" + sqlIn);
    	    		DBLocal.ExecSQL("delete from PdDetail where PDID" + sqlIn);
    	    		drPD.Remove();
    				return true;
    			}
    		};
    		
    		DBLocal.ExecTrans(Exec);
        	Clear();
        	setWorkState(WorkState.Ready, 0);
        	return true;
		} catch (Exception e) {
			ExProc.Show(this, e);
        	return false;
		}
	}
	
	public void ActCancel() {
		if(mWorkState == WorkState.Normal) {
			if(Msgbox.Ask(this, "确认" + (mPDID < 0 ? "删除" : "取消") + "本次盘点吗？") == false) return;
		}

		if(mPDID <= 0 && drPD != null && drPD.getIntVal("state") == 0)
			DeletePdData(drPD.getIntVal("PDID"));
		Clear();
		setWorkState(WorkState.Ready, 0);
	}

	public void ActClose() {
		Clear();
		setWorkState(WorkState.Ready, 0);
	}

	public void ActSubmit() {
    	try {
    		if(dtInv.getRowCount() == 0) ExProc.ThrowMsgEx("列表不能为空！");
    		if(StockID == 0) ExProc.ThrowMsgEx("请选择用户！");

    		int RestCount = mStat.Rest.Count;
        	if(RestCount > 0)
        		if(Msgbox.Ask(this, "还有" + RestCount + "条记录未盘点，确认继续提交吗？") == false) return;
        	
        	ParamList pl = new ParamList();
        	
        	DataTable dtClonePD = dtPD.Clone();
        	dtClonePD.addRow(drPD);
        	pl.SetSerializeValue("PD", dtClonePD);
        	pl.SetSerializeValue("PdInventory", dtInv.Copy("FItemID, Qty, Note, state"));
        	pl.SetSerializeValue("PdDetail", dtDetail.Copy("SNo, IID, Note, state, BatchNo, ExpDate"));
    		
    		// 回传数据
        	int ID = WS.BackPdDataEx(pl);

			// 更新BID,更新是否已提交标记
    		DBLocal.ExecSQL("Update PD set PDID=?, state=1 where PDID=?", ID, mPDID);
    		DBLocal.ExecSQL("Update PdInventory set PDID=? where PDID=?", ID, mPDID);
    		DBLocal.ExecSQL("Update PdDetail set PDID=? where PDID=?", ID, mPDID);
    		drPD.setValue("PDID", ID);
    		drPD.setValue("state", 1);
    		dtDetail.setValues("PDID", ID);
    		mPDID = ID;
    		
//			// 删除本地数据
//			PD.DeletePd(mPDID);
			
        	gc.Hint(this, "提交数据完成！");
        	
        	setWorkState(WorkState.Done, 0);
       // 	Clear();
		} catch (Exception e) {
			ExProc.Show(this, e);
		}
	}
	
	public void onClick(View v) {
		int id = v.getId(); 
		if(id == R.id.ib_back)
			ActBack();
		else if(id == R.id.ib_back2)
			ActBack();
		else if(id == R.id.ib_cancel)
			ActCancel();
		else if(id == R.id.ib_close)
			ActClose();
		else if(id == R.id.ib_submit)
			ActSubmit();
		else if (id == R.id.ib_reset)
			ActReset(true);
		else if (id == R.id.ib_detail_reset)
			ActReset(false);
		else if(id == R.id.ib_add || id == R.id.ib_new) 
			ActNew();
		else if (id == R.id.ib_print)
			ActPrint();
		else
			super.onClick(v);
	}
	
    @Override
    public synchronized void DecoderResult(String result) {
		AddItem(result);
    }

    protected void toggleSelect() throws Exception {
    	if(drPD == null) return;
    	drPD.setValue("sel", !drPD.getBool("sel"));
    	listHisAdapter.notifyDataSetChanged();
    }

    public void ActToggleSelAll() {
    	ActionInfo act = ACL.get("ToggleSelAll");
    	act.Checked = !act.Checked;
    	act.setProps(
    			new Param("title", act.Checked ? "取消全选" : "全选"),
    			new Param("res", act.Checked ? R.drawable.muti_select_gray : R.drawable.muti_select)
    		); 
		dtPD.setColValue("sel", act.Checked);
		listHisAdapter.notifyDataSetChanged();
	}

	public String getPrintHeader() {
		return "";
	}
	
	public void appendPrtDetail(PrtFieldList prtFields, SelectColList selects, StringBuilderEx sb, final int qtyType) {

		DataRowCollection rowsSelect = dtInv.SelectRows(new gi.IFunc1<DataRow, Boolean>() { 
			public Boolean Call(DataRow dr) {
				PD.StatQtyInfo sq = dr.$("Stat");
				return sq.Qtys[qtyType] > 0;
			}
		});
		
		DataTable dt = rowsSelect.GroupBy(selects);

		sb.append(HyPrt.getSepLine());

		// 输出表头
		for (PrtField pf : prtFields) {
			sb.append(gs.padStrA(pf.Disp, pf.Len, pf.Align));
		}
		sb.append("\n" + HyPrt.getSepLine());
		
		// 输出表单
		int total = 0;
		for (DataRow dr : dt.getRows()) {
			sb.appendLine(Bill.GetShortName(dr.getStrVal("FName")));
			
			DataRowCollection rows = dr.$("items");
			for (DataRow drInv : rows) {
				PD.StatQtyInfo sq = drInv.$("Stat");
				int qty = sq.Qtys[qtyType];
				total += qty;
				
				for (PrtField pf : prtFields) {
					Object v = pf.Name.equals("Qty") ? qty : drInv.getStrVal(pf.Name);
					sb.append(gs.padStrA(RptMini.StrVal(v), pf.Len, pf.Align));
				}
				sb.appendLine();
			}
			if(dr != dt.lastRow()) sb.appendLine();
		}
		
		// 输出表尾
		sb.append(HyPrt.getSepLine());

		for (PrtField pf : prtFields) {
			Object value = pf == prtFields.get(0) ? "合计:" : total;
			sb.append(gs.padStrA(RptMini.StrVal(value), pf.Len, pf.Align));
		}
		sb.appendLine();
	}
	
	public String getPrintDetail() {
		StringBuilderEx sb = new StringBuilderEx();
		SelectColList selects = SqlUtils.ParserSqlSelect("FName, items");
		int restWidth = HyPrt.MAX_LINECHARS_1X;
		
		PrtFieldList prtFields = new PrtFieldList();
		for (SelectColInfo selCol : SqlUtils.ParserSqlSelect("FModel, Qty")) {
			PrtField pfSrc = RptMini.find(selCol.Alias);
			if(pfSrc != null)  restWidth -= pfSrc.Len;
			PrtField pf = new PrtField(
					selCol.Alias,
					pfSrc == null ? selCol.Alias : pfSrc.Disp,
					pfSrc == null ?  0 : pfSrc.Len,
					pfSrc == null ?  TextAlign.LEFT : pfSrc.Align
				);
			pf.ColInfo = selCol;
			prtFields.add(pf);
		}

		for (PrtField pf : prtFields)
			if(pf.Len == 0) pf.Len = restWidth;

		sb.appendLine();
		
		sb.append(HyPrt.getCenterText("盘点明细", 1));
		appendPrtDetail(prtFields, selects, sb, PD.StatQtyInfo.PD);
		sb.appendLine();

		sb.append(HyPrt.getCenterText("多出数据", 1));
		appendPrtDetail(prtFields, selects, sb, PD.StatQtyInfo.EXTRA);
		sb.appendLine();

		sb.append(HyPrt.getCenterText("未盘点", 1));
		appendPrtDetail(prtFields, selects, sb, PD.StatQtyInfo.REST);
		
		String s = sb.toString();
		return sb.toString();
	}

	public String getPrintEnd() {
		return "业务员：" + SysData.op_name + "\n";
	}
	
	public DataList getPrtData() {
		DataList lsData = new DataList();
		
		String title = "盘库单";
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
	
	public void PrtMiniRpt() {
		try {
			HyPrt.Print(new gi.IFunc<DataList>() {
			    public DataList Call() {
			    	return getPrtData();
			    }
			});
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
	public void ActPrint(){
		try {
			PrtMiniRpt();
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
}
