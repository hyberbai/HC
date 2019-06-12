package com.hc;

import android.app.Activity;

import com.hc.dal.Bill;
import com.hc.dal.WS;
import com.hc.dal.d;
import com.hc.db.DBLocal;
import com.hc.mo.bill.ActBillBH;
import com.hc.mo.bill.ActBillSale;
import com.hc.mo.bill.ActBillTH;
import com.hc.mo.bill.ActStockBill;
import com.hc.mo.bill.ProductRelpace;
import com.hc.mo.pd.ActPD;

import java.util.Date;

import hylib.data.DataRow;
import hylib.data.DataTable;
import hylib.db.SqlDataAdapter;
import hylib.db.SqlHelper;
import hylib.toolkits.ExProc;
import hylib.toolkits.TempV;
import hylib.toolkits.gc;
import hylib.toolkits.gi;
import hylib.toolkits.gi.CallBack;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.ui.dialog.LoadingDialog;
import hylib.ui.dialog.Msgbox;
import hylib.ui.dialog.UIUtils;
import hylib.util.OMap;
import hylib.util.Param;
import hylib.util.ParamList;

public class MainActions {

	/*
	 *  同步内容：流水号数据、产品信息、产品类别、用户信息、员工、部门、仓库
	 *  		价格方案、客户、生产商、条码规则、医院信息、产品掩码、金蝶单据信息
	 */
	public static String[] synItems = new String[] { 
			"SN", "Item", "ItemCls", "User", "Emp", "Dept", "Stock", 
			"PriceCust", "PricePlanDetail", 
			"Cust", "StockCust", "Manuf", "PCodeRules", "YY",
			"ProductReps", "StockBill", "StockBillEntry"
	};
	public static Param[] synGroupItems =  new Param[]{
			new Param("基础数据", "Item, ItemCls, User, Emp, Dept, Manuf, Stock, YY, StockCust, Cust, PriceCust, PricePlanDetail, PCodeRules"),
			new Param("流水记录", "SN"),
			new Param("历史单据", "StockBill, StockBillEntry"),
	};


	/**
	 * 打开单据
	 */
	public static void OpenBill(Activity parent, int CID, int ID) {
		Class<?> cls = CID == Bill.BTID_WTDX ? ActBillSale.class : 
			   		   CID == Bill.BTID_DB_BU ? ActBillBH.class :
	  				   CID == Bill.BTID_DB_TH ? ActBillTH.class :
	  				   CID == Bill.CID_PD ? ActPD.class :
	  				   null;
		
		ParamList pl = new ParamList("ID", ID);
		UIUtils.startActivity(parent, cls, pl, CID);
	}

	/**
	 * 打开金蝶单据
	 */
	public static void OpenStockBill(Activity parent, int CID, int ID, boolean isRealTime) {
		Class<?> cls = ActStockBill.class;
		
		ParamList pl = new ParamList(
				new Param("IsRealTime", isRealTime),
				new Param("ID", ID), 
				new Param("CID", CID) 
		);
		UIUtils.startActivity(parent, cls, pl, CID);
	}

	/**
	 * 删除单据
	 */
	public static boolean DeleteBill(Activity parent, final int CID, final int[] IDs) {
		if(!Msgbox.Ask(parent, "确认删除所选的单据吗？")) return false;
		gi.CallBack Exec = new CallBack() {
			
			@Override
			public Object Call() throws Exception {
				String sqlIn = SqlHelper.SqlIn(IDs);
				if(gv.In(CID, Bill.BTID_WTDX, Bill.BTID_DB_BU, Bill.BTID_DB_TH))
				{
					DBLocal.ExecSQL("delete from Bill where BID" + sqlIn);
					DBLocal.ExecSQL("delete from BillDetail where BID" + sqlIn);
				}
				else if(CID == Bill.CID_PD) {
					DBLocal.ExecSQL("delete from PD where PDID" + sqlIn);
					DBLocal.ExecSQL("delete from PdInventory where PDID" + sqlIn);
					DBLocal.ExecSQL("delete from PdDetail where PDID" + sqlIn);
				}
				return null;
			}
		};
		try {
			DBLocal.ExecTrans(Exec);
			return true;
		} catch (Exception e) {
			ExProc.Show(e);
			return false;
		}
	}

	/**
	 * 同步所有数据
	 */
	public static void SynAllData(boolean showHint) {
//		synItems = new String[] { "StockBill", "StockBillEntry" };
			
		ParamList pl = new ParamList();
		getSynParam(pl);

		try {
			WS.showLoading = showHint;
			int count = SysGroupData(synGroupItems, pl, showHint);
			if(showHint) gc.Hint(String.format("数据同步完成！\n导入数据 %d 条", count));
		} catch (Exception e) {
			ExProc.Show(e);
		} finally {
			LoadingDialog.ImmeClose();
			WS.showLoading = true;
		}
	}

	public static int SysGroupData(Param[] groups, ParamList pl, boolean showHint) throws Exception {
		int count = 0;
		for(Param group : groups) {
			String[] items = gs.Split((String)group.Value, ",");
			count += SynData(items, pl, group.Name, showHint, false);
		}
		return count;
	}

	public static int SynData(final String[] items, ParamList pl, String info, final boolean showLoading, boolean autoCloseLoading) throws Exception {
		final TempV tmp = new TempV();
		
		if(showLoading) LoadingDialog.Show("正在接收" + info + "...");

		try {
			final TempV tc = new TempV();
			tmp.S = "";
			// 同步服务器数据
			final ParamList result = WS.SynData(items, pl);
			// 更新数据库
			if(showLoading) LoadingDialog.Show("更新" + info + "...");
			MyApp.ExecThreadCallBack(new gi.CallBack() {
				@Override
				public Object Call() throws Exception {
					DBLocal.ExecTrans(new CallBack() {
						
						@Override
						public Object Call() throws Exception {
							for (String key : items) {
								tmp.S = key;
								DataTable dt = (DataTable)result.GetDeserializeValue(key);
								dt.setTableName(key);
								ImportData(dt);
								tc.I += dt.RowCount();
							}
							return true;
						}
					});
					return null;
				}
			}, showLoading);
			return tc.I;
		} catch (Exception e) {
			LoadingDialog.ImmeClose();
			ExProc.ThrowMsgEx("数据同步失败！" + tmp.S, e);
			return 0;
		}
		finally {
			if(autoCloseLoading) LoadingDialog.ImmeClose();
		}
	}

	public static int SynData(final String[] items, ParamList pl, String info, final boolean showLoading) throws Exception {
		return SynData(items, pl, info, showLoading, true);
	}

	static int MaxSynDays = 7;	// 最多同步天数，只保留该天数金蝶的单据数据
	static int HotSynDays = 3;	// 热同步天数，保证与金蝶数据完全同步
	static Date dateStart = gv.DateAdd(gv.getNowDate(), -MaxSynDays);
	static Date dateSyn =  gv.DateAdd(gv.getNowDate(), -HotSynDays);
	
	/**
	 * 同步金蝶单据数据
	 */
	public static void SynJdBill() {
		// 清除老数据
		DBLocal.ExecSQL("delete from StockBillEntry where FID in (select FID from T_CC_StockBill where FDate<?)", dateStart);
		DBLocal.ExecSQL("delete from StockBill where FDate<?", dateStart);

		// 同步新数据
		try {
			ParamList pl = new ParamList();
			int FID = DBLocal.ExecuteIntScalar("select MAX(FID) from StockBill");
			if(FID < 1) 
				pl.SetValue("FID", FID);
			else
				pl.SetValue("dateFrom", dateSyn);
			SynData(new String[] { "StockBill", "StockBillEntry" }, pl, "历史单据", true);

		} catch (Exception e) {
			ExProc.Show(e);
		} finally {
			LoadingDialog.Hide();
		}
	}

	/**
	 * 获取上一次金蝶单据同步数据的最大FID
	 */
	public static int getLastSynFID() {
		return DBLocal.ExecuteIntScalar("select MAX(FID) from StockBill");
	}

	/**
	 * 获取同步参数
	 */
	public static void getSynParam(ParamList pl) {
		Date date = gv.DateVal(DBLocal.ExecuteScalar("select MAX(FDate) from StockBill"));
		Date now = gv.getNowDate();
		Object syn = null;
		if(date != null)
		{
			int days = gv.DateDiff(now, date);
			if(days < MaxSynDays) 
				syn = days < HotSynDays ? dateSyn : getLastSynFID();
		}
		if(syn == null) syn = dateStart;
		
		if(syn instanceof Integer)
			pl.SetValue("FID", syn);
		else
			pl.SetValue("dateFrom", syn);
	}
	
	public static void ImportData(DataTable dt) throws Exception {
		if(dt.isEmpty()) return;
		
		String tableName = dt.getTableName();
		if(tableName.equals("ProductReps")) 
			ImportProductReps(dt);
		else if(tableName.equals("StockBill")) 
			ImportStockBill(dt);
		else if(tableName.equals("StockBillEntry")) 
			ImportStockBillEntry(dt);
		else if(tableName.equals("YY")) 
			ImportYY(dt);
		else {
			DBLocal.ExecSQL("delete from " + tableName); 
			DBLocal.ImportData(dt);
		}
	}
	
	private static void	ImportYY(DataTable dt) throws Exception {
		DBLocal.ExecSQL("delete from Dict where CID>450 and CID<480");
		dt.setTableName("Dict");
		DBLocal.ImportData(dt);
	}
	
	private static void ImportProductReps(DataTable dt) throws Exception {
		DBLocal.ExecSQL("delete from ProductReps where ifnull(Flag, 0) & 1 =0");
		DataTable dtDst = DBLocal.OpenSingleTable("ProductReps");
		OMap map = dtDst.getMap("Key");
		DataRow drDst = null;
		for (DataRow dr : dt.getRows()) {
			String key = dr.getStrVal("Key");
			drDst = (DataRow)map.get(key);
			if(drDst != null) { // 如果表中包含当前键值且被编辑过，则不进行更新
				if((drDst.getIntVal("Flag") & ProductRelpace.FLAG_EDITED) > 0) continue;
			}
			else {
				drDst = dtDst.newRow();
				dtDst.addRow(drDst);
				drDst.setValue("Key", dr.getValue("Key"));
			}
			drDst.updateValue("Reps", dr.getValue("Reps"));
		}
		
		SqlDataAdapter apt = d.getSqlAdapter();
		apt.Update(dtDst);
	}

	private static void ImportStockBill(DataTable dt) throws Exception {
		int FID = dt.getRow(0).getIntVal("FID");
		DataTable dt1 = DBLocal.OpenTable("FID/i", "select FID from StockBill where Flag=" + Bill.FLAG_READED + " and FID>=?", FID);
		int[] readedFIDs = dt1.getColIntValues("FID");
		DBLocal.ExecSQL("delete from StockBill where FID>=?", FID);
		DBLocal.ImportData(dt);
		DBLocal.ExecSQL("update StockBill set Flag=" + Bill.FLAG_READED + " where FID" + SqlHelper.SqlIn(readedFIDs));
	}

	private static void ImportStockBillEntry(DataTable dt) throws Exception {
		int FEntryID = dt.getRow(0).getIntVal("FEntryID");
		int FID = dt.getRow(0).getIntVal("FID");
		DBLocal.ExecSQL("delete from StockBillEntry where FEntryID>=?", FEntryID);
		DBLocal.ExecSQL("delete from StockBillEntry where FID>=?", FID);
		DBLocal.ImportData(dt);
	}
	
	/**
	 *  重建数据库
	 */
	public static boolean ResetDB() {
		if(!Msgbox.Ask(MyApp.CurrentActivity(), "重建数据库将清空所有数据！\n是否继续？")) return false;
		DBLocal.Rebuild();
		SynAllData(true);
		return true;
	}
}
