package hylib.ui.dialog;

import com.hc.R;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.toolkits.ExProc;
import hylib.util.ParamList;
import android.R.integer;
import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

public class PopupWindowEx extends PopupWindow {
	public DataRowCollection Items;
	public ParamList Params;

	public final static int NOMAL = 0;
	public final static int HIDE = 1;
	public final static int DISABLE = 1;

    public PopupWindowEx(View contentView) {
        super(contentView);
    }
	
    public void setItemText(String name, String text) {
		DataRow dr = Items.FindRow("name", name);
		if(dr != null) dr.setValue("text", text);
	}
    
    public void setItemState(String name, int state) {
		DataRow dr = Items.FindRow("name", name);
		if(dr != null) dr.setValue("state", state);
	}
    
    public void Popup(View v) {
		if (isShowing()) 
			dismiss();// 关闭
		else {
			try {
				Params.Call("BeforePopup", this, Params);
				showAsDropDown(v);// 显示
			} catch (Exception e) {
				ExProc.Show(e);
			}
		}
	}
}
