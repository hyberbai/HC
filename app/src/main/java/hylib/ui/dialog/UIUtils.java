package hylib.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.gc;
import hylib.toolkits.gv;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.view.TextWatcherEx;

public class UIUtils {
	public final static String CHRS_ANY = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-().:[]-+*/=\\%$@!#"; 
	public final static String CHRS_DATE = "0123456789-"; 
	public final static String CHRS_TIME = "0123456789:"; 
	public final static String CHRS_DEC = "0123456789."; 
	public final static String CHRS_DATETIME = "0123456789:- "; 
	
	public static int valueOfInputTypeName(String name) {
		if(name.equals("pwd")) return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD; 
		if(name.equals("n")) return InputType.TYPE_CLASS_NUMBER; 
		if(name.equals("num")) return InputType.TYPE_CLASS_NUMBER; 
		if(name.equals("money")) return InputType.TYPE_CLASS_NUMBER; 
		if(name.equals("nt")) return InputType.TYPE_CLASS_NUMBER; 
		if(name.equals("dt")) return InputType.TYPE_CLASS_NUMBER; 
		if(name.equals("date")) return InputType.TYPE_CLASS_NUMBER; 
		if(name.equals("time")) return InputType.TYPE_CLASS_NUMBER; 
		if(name.equals("dec")) return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
		return InputType.TYPE_CLASS_TEXT;
	}
	
	public static void startActivity(Activity parent, Class<?> cls, ParamList pl, int reqCode) {
		Intent intent = new Intent(parent, cls);
		pl.writeToInent(intent);
		parent.startActivityForResult(intent, reqCode); 
	}

	
	public static void startActivity(Activity parent, Class<?> cls, String config, int reqCode) {
		try {
			ParamList pl = new ParamList(config);
			startActivity(parent, cls, pl, reqCode);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public static void startActivity(Activity parent, Class<?> cls, int reqCode, Param... pms) {
		try {
			ParamList pl = new ParamList(pms);
			startActivity(parent, cls, pl, reqCode);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public static boolean TextChangePassed(ParamList arg) {
		String s = arg.SValue("s");
		int start = arg.IntValue("start");
		if(start >= s.length()) return true;
		char c = s.charAt(start);
		if(c == KeyEvent.KEYCODE_BACK) return true;
		return false;
	}
	
	public static Object getViewValue(View v) {
	 	if(v instanceof TextView) return ((TextView)v).getText().toString();
	 	if(v instanceof CheckBox) return ((CheckBox)v).isChecked();
	 	if(v instanceof RadioGroup) return ((RadioGroup)v).getCheckedRadioButtonId();
		return null;
	}

	public static String getViewText(View v) {
	 	if(v instanceof RadioGroup) {
	 		int id = ((RadioGroup)v).getCheckedRadioButtonId();
	 		return id < 0 ? "" : getViewText(v.findViewById(id));
	 	}
		Object value = getViewValue(v);
		return gv.StrVal(value);
	}
	
	public static void setViewValue(View v, Object value) {
	 	if(v instanceof TextView) ((TextView)v).setText(gv.StrVal(value));
	 	if(v instanceof CheckBox) ((CheckBox)v).setChecked(gv.BoolVal(value));
	 	if(v instanceof RadioGroup) ((RadioGroup)v).check(gv.IntVal(value));
	}
	
	public static int getRadioItemIDByText(RadioGroup rg, Object value) {
	 	for (View view : gc.getGroupViews(rg, RadioButton.class)) 
			if(getViewValue(view).equals(value)) return view.getId();
		return -1;
	}

	public static void clearViews(ViewGroup viewGroup){
		for (View view : gc.getGroupViews(viewGroup, null)) setViewValue(view, null);
	}

	public static void clearViews(ViewGroup viewGroup, View[] views){
		for (View view : views) setViewValue(view, null);
	}

	public static void clearViews(Activity act, ViewGroup viewGroup, int[] viewIds){
		clearViews(viewGroup, getIdViews(act, viewIds));
	}

	public static void clearViews(Activity act, int[] viewIds){
		ViewGroup root = (ViewGroup)act.findViewById(android.R.id.content);
		clearViews(act, root, viewIds);
	}
	
	public static int[] getViewIds(View[] views) {
		int[] ids = new int[views.length];
		for (int i = 0; i < views.length; i++)
			ids[i] = views[i].getId();
		return ids;
	}
	
	public static View[] getIdViews(Activity act, int[] viewIds) {
		View[] views = new View[viewIds.length];
		for (int i = 0; i < viewIds.length; i++)
			views[i] = act.findViewById(viewIds[i]);
		return views;
	}
	
	public static void BindingValueChangeEvent(final EventHandleListener onChanged, View... views) {
		for (View v : views) {
			if(v instanceof TextView)
			{
				TextWatcherEx watcherEx = new TextWatcherEx((TextView)v);
		        watcherEx.changedListener =  new EventHandleListener() {
					
					@Override
					public void Handle(Object sender, ParamList arg) throws Exception {
						onChanged.Handle(sender, arg);
					}
				};
				((TextView)v).addTextChangedListener(watcherEx);
			}
		}
	}
	
	public static void BindingClickEvent(final EventHandleListener onClick, View... views) {
		View.OnClickListener onViewClick = new View.OnClickListener() {
			public void onClick(View v) {
				try {
					onClick.Handle(v, null);
				} catch (Exception e) {
					ExProc.Show(e);
				}
			}
		};
		for (View v : views) v.setOnClickListener(onViewClick);
	}

	public static void BindingClickEvent(final EventHandleListener onClick, ViewGroup viewGroup) {
		BindingClickEvent(onClick, gc.getGroupViews(viewGroup, Button.class));
	}
	
	public static void ShowSoftInput(Activity act, boolean show) {
		
//		final View view = getCurrentFocus();
//		if(view == null) return;
//
//	    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//	    
//	    if(show)
//	    {
//		    view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {  
//		        @Override  
//		        public void onGlobalLayout() {
//		            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
//		            manager.showSoftInput(view, 0);  
//		        }  
//		    });
//	    	//imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
//	    }
//	    else
//	    	imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
		if(isSoftShowing(act.getWindow()) ^ show) toggleSoftInput(act);
	}


	public static void ShowSoftInput(Dialog dlg, boolean show) {
		if(isSoftShowing(dlg.getWindow()) ^ show) toggleSoftInput(dlg.getContext());
	}
	
    protected static boolean isSoftShowing(Window window) {  
        //获取当前屏幕内容的高度  
        int screenHeight = window.getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    //	return getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
    }
    
	public static void toggleSoftInput(Context context) {
	    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
	    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

    public static int getFontHeight(float fontSize)  
    {  
       Paint paint = new Paint();  
       paint.setTextSize(fontSize);  
       FontMetrics fm = paint.getFontMetrics();  
       return (int) Math.ceil(fm.descent - fm.top) + 2;  
    } 
}
