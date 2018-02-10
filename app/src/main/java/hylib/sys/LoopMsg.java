package hylib.sys;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;
import hylib.toolkits.*;
import hylib.ui.dialog.Msgbox;
import hylib.util.ParamList;


public class LoopMsg {
	private Handler mHandler;
	public String msg;
	private int mr;
	public boolean isLooping;
	public final int TIMEOUT = -1;;

	public LoopMsg() {
	}
	
	public LoopMsg(String msg) {
		this.msg = msg;
	}
	
	public void StopLoop(int result) {
		mr = result;
		StopLoop();
		_D.Out("StopLoop");
	}
	
	public void StopLoop() {
		if(mHandler == null) return;
		mHandler.sendMessage(mHandler.obtainMessage());
		mHandler = null;
	}

	public void StopLoop(Activity act, final int result) {
		if(act == null)
		{
			StopLoop(result);
			return;
		}
		act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	StopLoop(result);
            };
        });
	}
	
	public static class LoopExitException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}
	
	private void CreateTimeoutBreakTask(int timeout){
		if(mTimeoutBreakTask == null) {
			mTimeoutBreakTask = new DelayTask();
			mTimeoutBreakTask.TaskHandleListener = new EventHandleListener() {
				public void Handle(Object sender, ParamList arg) throws Exception {
					HyApp.runOnUiThread(new Runnable() {
			            public void run() {
			            	mTimeoutBreakTask.Cancel();
			            	if(Msgbox.Ask("操作无响应，是否继续取消？")) 
			            		StopLoop(TIMEOUT);
			            	else 
			            		mTimeoutBreakTask.Resume();
			            }
			        });
				}
			};
		}
		mTimeoutBreakTask.Start(timeout);
	}

	private void CloseTimeoutBreakTask(){
		if(mTimeoutBreakTask == null) return;
		mTimeoutBreakTask.Cancel();
	}

	private DelayTask mTimeoutBreakTask;
	public void Loop(int timeout) {
		if(timeout > 0) CreateTimeoutBreakTask(timeout);
		
		mHandler = new Handler(Looper.getMainLooper()) {
			public void handleMessage(Message mesg) {
				CloseTimeoutBreakTask(); 
				throw new LoopExitException();
			}
		};
		try {
			//Looper.getMainLooper();
			_D.Out("StartLoop");
			mr = -1;
			isLooping = true;
			Looper.loop();
			isLooping = false;
		} catch (LoopExitException e) {
			isLooping = false;
		} catch (Exception e) {
			_D.Out(e.getMessage());
		}
	}

	public void Loop() {
		Loop(0);
	}
	
	public int Result() {
		return mr;
	}
}