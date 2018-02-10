package hylib.edit;

import android.R.bool;
import android.view.View;
import hylib.toolkits.ExProc;
import hylib.toolkits.MsgException;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.ui.dialog.UIUtils;
import hylib.util.Param;
import hylib.util.ParamList;


public class EditField {
	public int Id;
	public int GroupId;
	public boolean Changed;
	public String Name;
	public String Disp;
	public Object Value;
	public View view;
	public int validOptions;
	public int valueType;
	
	public ParamList Params;
	
	public EditField(int id, String name, String disp, String config) {
		this.Id = id;
		this.Name = name;
		this.Disp = disp;
		try {
			Params = new ParamList(config);
			if(Params.containsKey("cn")) Disp = Params.GetOnce("cn");
			if(Params.GetOnceBool("need")) this.validOptions += ValidType.Need;
			if(Params.GetOnceBool("uniq")) this.validOptions += ValidType.Unique;
			if(Params.containsKey("vt")) valueType = DType.valueOf(Params.GetOnce("vt"));
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
	public boolean isNeed() {
		return gv.ContainEnumVal(validOptions, ValidType.Need); 
	}
	
	public boolean hasValue() {
		return !gv.IsEmpty(Value); 
	}
	
	public void Validate() throws Exception {
		if(view == null) return;
		try {
			if(gv.IsEmpty(Value)) {
				if(isNeed()) ExProc.ThrowMsgEx("“" + Disp + "”不能为空！"); 
			} else 
				if(!ValidUtils.validValue(Value, valueType))
					ExProc.ThrowMsgEx("“" + Disp + "”数据格式不正确！"); 
		} catch (Exception e) {
			view.requestFocus();
			throw e;
		}
	}
	
	public boolean isUnique() {
		return gv.ContainEnumVal(validOptions, ValidType.Unique); 
	}

	public String getValTypeName() {
		return DType.getName(valueType);
	}

	public EditField(int id, String name, String disp) {
		this(id, name, disp, "");
	}
	
	public boolean putValue(Object value) {
		Object fmtValue = null;
		if(value instanceof String && ((String)value).length() > 0)
			fmtValue = gs.NormalizeValue((String)value, valueType);
		
		boolean fmtValueChanged = fmtValue != null && !gv.SameS(value, fmtValue);
		if(fmtValueChanged) value = fmtValue;
		if(gv.SameS(Value, value)) return false;
		Value = value;
		
		if(view != null || fmtValueChanged) UIUtils.setViewValue(view, value);
		return true;
	}
	
	public void setValue(Object value) {
		if(!putValue(value)) return;
		Changed = false;
		if(view != null) UIUtils.setViewValue(view, value);
	}

	public void updateValue(Object value) {
		if(!putValue(value)) return;
		Changed = true;
	}
}
