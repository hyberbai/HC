package com.hc.tools;

import android.widget.EditText;
import android.widget.TextView;

import com.hc.SysData;
import com.hc.dal.d;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.data.DataRowState;
import hylib.sys.SysUtils;
import hylib.ui.dialog.Msgbox;
import hylib.ui.dialog.UIUtils;
import hylib.util.ParamList;

public class boxUtils {
	public static int MAX_LIST_NUMS = 5;
	
	public static DataRow ChooseCust(EditText v) throws Exception {
		String text = UIUtils.getViewText(v);
		if(text.equals(" ")) {
			SysData.setCust(0, false);
			return null;
		}
		if(text.length() < 2) return null;
		ParamList pl = new ParamList("Cancelable: 0");
		if(SysData.dtUserCust == null) return null;
		DataRowCollection rows  = SysData.dtUserCust.getRows().MatchText("SName", text, MAX_LIST_NUMS);
		if(rows.size() == 0) return null;
		//if(rows.size() > MAX_LIST_NUMS) return null;
		
		int which = Msgbox.Choose("选择客户", rows.getColStrValues("SName"), pl);
		if(which < 0) return null;
		DataRow dr = rows.get(which);
		UIUtils.setViewValue(v, dr.getStrVal("SName"));
		v.setSelected(true);
		v.selectAll();

		SysData.setCust(dr.getIntVal("FItemID"), true);
		return dr;
	}

	public static DataRow ChooseStock(EditText v) throws Exception {
		String text = UIUtils.getViewText(v);
		if(text.equals(" ")) {
			SysData.setStock(0, false);
			return null;
		}
		if(text.length() < 2) return null;
		ParamList pl = new ParamList("Cancelable: 0");
		DataRowCollection rows  = SysData.dtUserStock.getRows().MatchText("FName", text, MAX_LIST_NUMS);
		if(rows.size() == 0) return null;
		//if(rows.size() > MAX_LIST_NUMS) return null;
		
		int which = Msgbox.Choose("选择库位", rows.getColStrValues("FName"), pl);
		if(which < 0) return null;
		DataRow dr = rows.get(which);
		UIUtils.setViewValue(v, dr.getStrVal("FName"));
		v.setSelected(true);
		v.selectAll();

		SysData.setStock(dr.getIntVal("FItemID"), true);
		return dr;
	}
}
