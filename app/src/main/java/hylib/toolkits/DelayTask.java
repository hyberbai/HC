package hylib.toolkits;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hylib.sys.HyApp;
import hylib.util.ParamList;

public class DelayTask {
	public EventHandleListener TaskHandleListener;  
	public Timer timer;
    public Object Key;
	private int mDelayMs;

	public DelayTask() {
	}

	public void Start(int delay_ms){
	    final DelayTask c = this; 
		Cancel();
		mDelayMs = delay_ms;
		
	    timer = new Timer();
	    timer.schedule(new TimerTask() {
	      public void run() {
	    	try {
				Cancel();
	    		if(TaskHandleListener != null)
	    			TaskHandleListener.Handle(c, null);
			} catch (final Exception e) {
		        HyApp.runOnUiThread(new Runnable() {
		            public void run() {
						ExProc.Show(e);
		            }
		        });
			}
	      }
	    }, delay_ms);
	}
	
	public void Cancel(){
		if(timer == null) return;
		timer.cancel();
		timer.purge();
		timer = null;
	}

	
	public void Resume(){
		Start(mDelayMs);
	}

    private static List<DelayTask> mInstances;

    public static DelayTask findTask(Object key)
    {
    	for (DelayTask task : mInstances)
			if(task.Key.equals(key)) return task;
    	return null;
    }

    public static void Execute(Object key, final EventHandleListener execTask, int delay_ms)
    {
    	if(mInstances == null) mInstances = new ArrayList<DelayTask>();
        if (findTask(key) != null) return;

        final DelayTask inst = new DelayTask();
        inst.Key = key;
		inst.TaskHandleListener = new EventHandleListener() {
			
			public void Handle(final Object sender, final ParamList arg) throws Exception {

		        HyApp.CurrentActivity().runOnUiThread(new Runnable() {
		            public void run() {
		            	try {
		            		execTask.Handle(sender, arg);
						} catch (Exception e) {
							ExProc.Show(e);
						}
		                finally
		                {
		                    mInstances.remove(inst);
		                }
		            }
		        });
			}
		};

        mInstances.add(inst);
        
        inst.Start(delay_ms);
        return;
    }
}
