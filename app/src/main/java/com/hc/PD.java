package com.hc;

import java.util.Iterator;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.toolkits.ExProc;
import hylib.toolkits.gcon;
import hylib.toolkits.gi;
import hylib.toolkits.gv;
import android.R.integer;

import com.hc.db.DBLocal;

public class PD {


	public static void DeletePd(int PDID) {
		final int tPDID = PDID;
		
		gi.CallBack Exec = new gi.CallBack() {
			
			@Override
			public Object Call() throws Exception {
				DBLocal.ExecSQL("delete from PD where PDID=?", tPDID);
				DBLocal.ExecSQL("delete from PdInventory where PDID=?", tPDID);
				DBLocal.ExecSQL("delete from PdDetail where PDID=?", tPDID);
				return null;
			}
		};
		try {
			DBLocal.ExecTrans(Exec);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
	public static class StatQtyInfo {
		public static final int QTY = 0;	// 在库数量 
		public static final int OK = 1; 	// 正常数量
		public static final int WRONG = 2; 	// 异况数量
		public static final int EXTRA = 3; 	// 多出数量
		public static final int PD = 4; 	// 盘点数量=正常数量+异况数量
		public static final int REST = 5; 	// 未盘数量
		private static final int LEN = REST + 1; 
		
		public int[] Qtys;
		
		public int getQty() { return Qtys[QTY]; }
		public int getOkQty()  { return Qtys[OK]; }
		public int getWrongQty()  { return Qtys[WRONG]; }
		public int getExtraQty() { return Qtys[EXTRA]; }
		public int getPDQty() { return Qtys[PD]; }
		public int getRestQty()  { return Qtys[REST]; }
		

		public void setQty(int qty) { Qtys[QTY] = qty; }
		public void setOkQty(int qty)  { Qtys[OK] = qty; }
		public void setWrongQty(int qty)  { Qtys[WRONG] = qty; }
		public void setExtraQty(int qty) { Qtys[EXTRA] = qty; }
		public void setPDQty(int qty) { Qtys[PD] = qty; }
		public void setRestQty(int qty)  { Qtys[REST] = qty; }
		
		public StatQtyInfo() {
			Qtys = new int[LEN];
		}

		public void Inc(StatQtyInfo sq) {
			for(int i = 0; i < LEN; i++)
				Qtys[i] += sq.Qtys[i];
		}
	}

	public static StatQtyInfo StatRowQty(DataRow dr)
	{
		DataRowCollection rows = dr.getValue(DataRowCollection.class, "Items");
		StatQtyInfo sq = new StatQtyInfo();
		sq.setQty(dr.getIntVal("Qty"));
		sq.setOkQty(0);
		sq.setWrongQty(0);
		sq.setExtraQty(0);
		for (DataRow r : rows) {
			int state = gv.IntVal(r.getValue("State"));
			if(state == gcon.S_CHECKED) sq.Qtys[StatQtyInfo.OK]++;
			if(state == gcon.S_WRONG) sq.Qtys[StatQtyInfo.WRONG]++;
			if(state == gcon.S_EXTRA) sq.Qtys[StatQtyInfo.EXTRA]++;
		}
		sq.setPDQty(sq.getOkQty() + sq.getWrongQty());
		sq.setRestQty(sq.getQty() - sq.getPDQty());
		if(sq.getRestQty() < 0) sq.setRestQty(0);
		return sq;
	}
}
