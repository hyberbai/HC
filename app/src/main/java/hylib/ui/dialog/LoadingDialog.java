package hylib.ui.dialog;

import com.hc.R;

import hylib.sys.HyApp;
import hylib.sys.LoopMsg;
import hylib.toolkits.gv;
import android.R.bool;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoadingDialog extends AlertDialog {
    private TextView tvMsg;
	private LoopMsg lm;

    private String message = null;

    public LoadingDialog(Context context) {
        super(context);
        lm = new LoopMsg(); 
        this.message = "";//正在加载中...";
    }

    public LoadingDialog(Context context, String message) {
        super(context);
        this.message = message;
        this.setCancelable(false);
    }

    public LoadingDialog(Context context, int theme, String message) {
        super(context, theme);
        this.message = message;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_loading);
        tvMsg = (TextView) findViewById(R.id.tv_loading_msg);
        setMessage(message);
    }

    public void setMessage(String message) {
        this.message = message;
        if(tvMsg == null) return;
        tvMsg.setText(message);
        tvMsg.setVisibility(gv.IsEmpty(message) ? View.GONE : View.VISIBLE);
    }

    public void setText(int resId) {
        setMessage(getContext().getResources().getString(resId));
    }

    public void Loop() {
        lm.Loop();
    }

    public void Stop() {
        lm.StopLoop();
    }

    public int getLoadingLockCount() {
        return loading_lock_count;
    }

	private static LoadingDialog dialog = null;
	private static int loading_lock_count = 0;
	public static synchronized void Show(Context context, String msg) {
		if(context == null) return;
		if(loading_lock_count++ > 0) {
			if(dialog != null && !msg.isEmpty()) dialog.setMessage(msg);
			return;
		}
		dialog = new LoadingDialog(context, msg);
		dialog.show();
	}

	public static void Show(String msg) {
		Show(HyApp.CurrentActivity(), msg);
	}

//	public static synchronized void setMsg(String msg) {
//		if(dialog == null) Show(msg); else { 
//			dialog.setMessage(msg);
//		//	if(!dialog.isShowing()) 
//				dialog.show();
//		}
//	}
	
	public static void ShowDialog(Context context, String dialogStr) {
        Show(context, dialogStr);
        if(loading_lock_count == 1) dialog.Loop();
        Hide();
	}
	
	public static void StopLoop() {
		if(--loading_lock_count > 0) return;
		dialog.Stop();
		loading_lock_count++;
	}

	public static void Hide() {
		if(--loading_lock_count > 0) return;
		ImmeClose();
	}

	public static void ImmeClose() {
		if (dialog != null && dialog.isShowing()) {
			dialog.hide();
			dialog.dismiss();
		}
		loading_lock_count = 0;
	}
}
