package hylib.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hc.ID;
import com.hc.R;

import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.ui.dialog.UIUtils;
import hylib.util.ActionList;
import hylib.util.Param;
import hylib.util.ParamList;

public class ActivityEx extends Activity 
{
	public static final int RESULT_CHANGED = 77;

	public ParamList Params;
	public boolean IsChanged;
	public boolean LockChange;
	public ActionList ACL;
	
	protected int mLockCount;
	
	protected ActivityEx context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		context = this;
		ACL = new ActionList();
        try {
        	// 加载参数
            String sParams = getIntent().getStringExtra("Params");
            Params = new ParamList(sParams);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public void BindingClickEvent(View... views) {
		View.OnClickListener onClickListener = new View.OnClickListener() {
			public void onClick(View v) {
				context.onClick(v);
			}
		};

		for (View v : views)
			v.setOnClickListener(onClickListener);
	}
	
	public void BindingValueChangeEvent(View... views) {
		for (View v : views) {
			if(v instanceof TextView)
			{
				TextWatcherEx watcherEx = new TextWatcherEx((TextView)v);
		        watcherEx.changedListener =  new EventHandleListener() {
					
					@Override
					public void Handle(Object sender, ParamList arg) throws Exception {
						if(!LockChange) onTextChanged(sender, arg);
					}
				};
				((TextView)v).addTextChangedListener(watcherEx);
			}
		}
	}
	
	public void BindingFocusChangeEvent(View... views) {
		View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {  
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {
		    	context.onFocusChange(v, hasFocus);
		    }
		};
		for (View v : views)
	        v.setOnFocusChangeListener(onFocusChangeListener);
	}
	
	public void BindingKeyEvent(View... views) {
		View.OnKeyListener onKeyListener = new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN) {
					context.onKeyDown(v, keyCode, event);
					if (keyCode == KeyEvent.KEYCODE_ENTER) {
						//	ShowSoftInput(false);
						if(!$T(v.getId()).isEmpty()) onClick(v);
						return true;
					}
				}
				return false;
			}

		};
		for (View v : views)
	        v.setOnKeyListener(onKeyListener);
	}
	
	public void onKeyDown(View v, int keyCode, KeyEvent event) {
	}
	
	public void onTextChanged(Object sender, ParamList arg) {
	}
	
	public void onFocusChange(View v, boolean hasFocus) {
	}
	
	/**
	 * 刷新界面状态
	 */
	public void RefreshState() {
	}
	
	@SuppressWarnings("unchecked")
	public <T extends View> T $(int resId) {
		return (T) super.findViewById(resId);
	}

	public EditText $ET(int resId) {
		return (EditText)$(resId);
	}
	
	public Button $BTN(int resId) {
		return (Button)$(resId);
	}
	
	public TextView $TX(int resId) {
		return (TextView)$(resId);
	}

	public boolean isLockChange() {
		return LockChange && mLockCount == 0;
	}

    protected void setChanged(boolean value) {
    	if(isLockChange()) return;
		IsChanged = value;
	}
	
	public void $Set(int resId, Object value) {
		UIUtils.setViewValue((View)$(resId), value);
	}

	/*
	 * 获取当前id 对应控件的数据
	 */
	public Object $V(int resId) {
		return UIUtils.getViewValue((View)$(resId));
	}
	
	/*
	 * 获取当前id 对应控件的文本
	 */
	public String $T(int resId) {
		return gv.StrVal($V(resId));
	}

	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.ib_back || id == ID.Back) Finish();
		if(id == R.id.ib_cancel || id == ID.Cancel) finish();
	}

	public void Finish() {  
		finish();
    }

	public ViewGroup getContentView()
    {  
        return (ViewGroup)$(android.R.id.content);  
    }

	public View getRootView()  
    {  
        return getContentView().getChildAt(0);  
    }

	public ViewGroup getRootLayout()  
    {  
        return type.as(getRootView(), ViewGroup.class);  
    }
    
	public void startActivity(Class<?> cls, ParamList pl, int reqCode) {
		UIUtils.startActivity(this, cls, pl, reqCode);
	}
	
	public void startActivity(Class<?> cls, String config, int reqCode) {
		UIUtils.startActivity(this, cls, config, reqCode);
	}

	public void startActivity(Class<?> cls, int reqCode, Param... pms) {
		UIUtils.startActivity(this, cls, reqCode, pms);
	}
	
	public void startActivity(Class<?> cls) {
		UIUtils.startActivity(this, cls, "", 0);
	}

	public void setResultAndClose(Param... pms) {
		Intent intent = new Intent();
		for (Param p : pms) putIntent(intent, p.Name, p.Value);
        setResult(RESULT_OK, intent);
		finish();
	}
	
	public void putIntent(Intent intent, String name, Object value) {
		if(value instanceof String) intent.putExtra(name, (String)value); else
		if(value instanceof Boolean) intent.putExtra(name, (Boolean)value); else
		if(value instanceof Integer) intent.putExtra(name, (Integer)value); else
		if(value instanceof Float) intent.putExtra(name, (Float)value); else
		if(value instanceof Double) intent.putExtra(name, (Double)value); else
		intent.putExtra(name, gv.Serialize(value));
	}
	
	public void ShowSoftInput(boolean show) {
		UIUtils.ShowSoftInput(this, show);
	}
}
