package hylib.sys;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import hylib.toolkits.DelayTask;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits._D;
import hylib.ui.dialog.Msgbox;
import hylib.util.ParamList;


public class LoopMsg {
	private Handler mHandler;
	private int mr;
	public boolean isLooping;
	public int TIMEOUT = -1;

	public LoopMsg() {
	}
	
	public void StopLoop(int result) {
		mr = result;
		StopLoop();
	}
	
	public void StopLoop() {
		if(mHandler == null) return;
		mHandler.sendMessage(mHandler.obtainMessage());
		_D.Out("StopLoop: " + mr);
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

	public void CreateHandler(){
        mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                CloseTimeoutBreakTask();
                throw new LoopExitException();
            }
        };
    }

	private DelayTask mTimeoutBreakTask;
	public void Loop(int timeout) {
		if(timeout > 0) CreateTimeoutBreakTask(timeout);

        CreateHandler();
		try {
			_D.Out("StartLoop");
			mr = -1;
			isLooping = true;
			Looper.loop();
		} catch (LoopExitException e) {
			_D.Out("LoopOut");
		} finally {
			isLooping = false;
		}

//		catch (Exception e) {
//			_D.Out(e.getMessage());
//		}
	}

	public void Loop() {
		Loop(0);
	}
	
	public int Result() {
		return mr;
	}
}