package hylib.ui.dialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.dev.HyScanner;

import hylib.sys.LoopMsg;
import hylib.toolkits.gc;
import hylib.toolkits.gcon;
import hylib.toolkits.gi;
import hylib.toolkits.type;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class HyDialog extends Dialog {
	public final static int R_OK = 1;
	public final static int R_CANCEL = 4;
	public final static int R_YES = 1;
	public final static int R_NO = 3;

	public int DialogResult;
	public Object Result;
	public gi.NotifyListener onNotify;
	private LoopMsg lm;
	
	public View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Click(view);
		}
	};
	
	public HyDialog(Context context) {
		super(context);
		Init();
	}
	
	public HyDialog(Context context, int theme) {
		super(context, theme);
		Init();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
	    	dismiss();
	    	return true;
		}
	    return super.onTouchEvent(event);
	}
  
	private void Init() {
		lm = new LoopMsg();
        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        
		setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
        		CloseDialog();
            }
        });
	}

	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	dismiss();
        	return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    public void Click(View view) {
    }

	@Override
    public void onStop() {
		super.onStop();
	//	CloseDialog();
    }
	
	private void CloseDialog() {
		Notify(gi.NF_EXIT);
    	lm.StopLoop();
	}
	
	public void BindingOnClick(ViewGroup viewGroup){
		for (View v : gc.getGroupViews(viewGroup, Button.class))
			((Button)v).setOnClickListener(clickListener);
	}

	public void Notify(int lsid) {
		if(onNotify != null) onNotify.Notify(this, lsid);
	}

	public void Refresh() {
		Notify(gi.NF_REFRESH);
	}
	
	public void setDialogResult(int result) {
		lm.StopLoop(result);
		DialogResult = result;
	}

	public Object showDialog() {
		show();
		lm.Loop();
		//DialogResult=LoopMsg.Result();
		//dismiss();
		return DialogResult;
	}
}