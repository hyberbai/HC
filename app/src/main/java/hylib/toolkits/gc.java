package hylib.toolkits;

import hylib.sys.HyApp;
import hylib.util.ActionInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hc.MyApp;
import com.hc.g;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class gc {
	public static int HO_ICON_INFO = 1 << 0;
	public static int HO_CENTER = 1 << 5;

	public static boolean EditIsEmpty(View v) {
		if(v instanceof EditText)
		{
			EditText et = type.as(v, EditText.class);
			String s = GetEditText(et);
			return s == null || s.length() == 0;
		}
		return false;
	}

	public static Object ExecMethod(Object receiver, Class<?> cls, String methodName, Class<?>[] types, Object... args) {
		try {
			for(int deep = 3; deep > 0; deep--)
				try {
					Method md = cls.getDeclaredMethod(methodName, types);
					return md.invoke(receiver, args);
				} catch (NoSuchMethodException e) {
					_D.Out("NoSuchMethod: " + cls.getName() + "." + methodName);
					cls = cls.getSuperclass();
					if(cls == null) return null;
				}
			return null;
		} catch (Exception e) {
			ExProc.Show(e);
			return null;
		}
	}
	
	public static Object ExecMethod(Object receiver, Class<?> cls, String methodName, Object... args) {
		Class<?>[] types = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			types[i] =  args[i].getClass();
		}
		return ExecMethod(receiver, cls, methodName, types, args);
	}
	
	public static void ExecAction(Object receiver, Class<?> cls, ActionInfo act, Object... args) {
		ExecMethod(receiver, cls, "Act" + act.Name, args);
	}
	
	public static void Hint(Context context, String msg, int options) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		if(gv.ContainEnumVal(options, HO_CENTER)) toast.setGravity(Gravity.CENTER, 0, 0);
		
		if(gv.ContainEnumVal(options, HO_ICON_INFO))
		{
			LinearLayout toastView = (LinearLayout) toast.getView();
			toastView.setOrientation(LinearLayout.HORIZONTAL);
			
			LinearLayout.LayoutParams lp;
	        TextView tv = type.as(toastView.findViewById(0x102000B), TextView.class);
	        if(tv == null) tv = type.as(toastView.getChildAt(0), TextView.class);
	        if(tv != null)
	        {
	    		int padding = gu.dp2px(context, 10);
	    		tv.setPadding(padding, padding, padding, padding);
	    		
	    		int margin = gu.dp2px(context, 5);
	    		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	
	    		lp.gravity = Gravity.START;  
	    		lp.setMargins(margin, 0, margin, 0);
	    		tv.setLayoutParams(lp);
	        }
			
			ImageView iv = new ImageView(context);
			lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.CENTER;
			iv.setLayoutParams(lp);
			iv.setImageResource(android.R.drawable.ic_dialog_info);
			toastView.addView(iv, 0);
		}
		toast.show();
	}

	public static void Hint(Context context, String msg) {
		Hint(context, msg, 0);
	}
	
	public static void Hint(String msg) {
		Hint(HyApp.CurrentActivity(), msg);
	}
	
	public static void HintTd(final Context context, String msg) {
    	if(msg == null || msg.isEmpty()) return;
    	final String thread_msg = msg;
    	if(context instanceof Activity)
	        ((Activity)context).runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	                Toast.makeText(context, thread_msg, Toast.LENGTH_SHORT).show();
	            }
	        });
    	else
    		Hint(context, msg);
    }
	
	public static void HintTd(String msg) {
		HintTd(HyApp.CurrentActivity(), msg);
	}
	
    public static void ViewFailCheck(View v, boolean IsFail, String msg)
    {
       // if (v is FieldControl) ctrl = ((FieldControl)ctrl).EditControl;
        if (v == null) return;
        if (!IsFail) return;

		if(gc.EditIsEmpty(v)) {
			HintTd(msg);
			v.requestFocus();
			return;
		}
        ExProc.ThrowMsgEx(msg);
    }

	private static <T extends View> void getGroupViews(List<View> list, ViewGroup viewGroup, Class<T> clz) {
		if (viewGroup == null) return;
		int count = viewGroup.getChildCount();
		for (int i = 0; i < count; i++) {
			View view = viewGroup.getChildAt(i);
			if (view instanceof ViewGroup) {
				// 若是布局控件（LinearLayout或RelativeLayout）,继续查询子View
				getGroupViews(list, (ViewGroup)view, clz);
			} else if (clz == null || clz.isInstance(view)) {
				list.add(view);
			}
		}
	}
	
	public static <T extends View> View[] getGroupViews(ViewGroup viewGroup, Class<T> clz) {
		ArrayList<View> list = new ArrayList<View>();
		getGroupViews(list, viewGroup, clz);
		return list.toArray(new View[0]);
	}
	
	public static String GetEditText(EditText et) {
		return et.getText().toString().trim();
	}

	public static void clearGroupView(ViewGroup viewGroup) {
		for (View view : getGroupViews(viewGroup, null)) {
			if(view instanceof EditText) ((EditText)view).setText("");
		}
	}

	public static void clearGroupView(ViewGroup viewGroup, int[] excludeIds) {
		SparseArray<Object> map = new SparseArray<Object>();
		for (int id : excludeIds) map.put(id, null);
			
		for (View view : getGroupViews(viewGroup, null)) {
			if(map.indexOfKey(view.getId()) >= 0) continue;
			if(view instanceof EditText) ((EditText)view).setText("");
		}
	}
	
	public static ViewGroup GetViewGroup(Dialog dlg){
		return type.as(dlg.getWindow().getDecorView(), ViewGroup.class);
	}

	public static void setViewGroupCtrlsState(ViewGroup viewGroup, boolean enabled){
		for (View view : getGroupViews(viewGroup, null))
			view.setEnabled(enabled);
	}
	
	public static ViewGroup GetViewGroup(Activity act){
		return type.as(act.getWindow().getDecorView(), ViewGroup.class);
	}

	public static void setDatePickerValue(DatePicker dp, Object val){
		Date dt = gv.DateVal(val);
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH);
		int d = c.get(Calendar.DAY_OF_MONTH);
		dp.updateDate(y, m, d);
	}
}
