package hylib.sys;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import java.lang.Thread.UncaughtExceptionHandler;

import hylib.toolkits.TempV;
import hylib.toolkits._D;
import hylib.toolkits.gc;
import hylib.toolkits.gi;
import hylib.ui.dialog.LoadingDialog;

public class HyApp extends Application implements UncaughtExceptionHandler  {
    private HyActivityLifecycleCallbacks mActLifecycleCallbacks;
    
    public Context mContext;
	public static gi.NotifyListener AppExitListener;
    
	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
		mContext = mInstance.getApplicationContext();
		
	    mActLifecycleCallbacks = new HyActivityLifecycleCallbacks(); 
	    registerActivityLifecycleCallbacks(mActLifecycleCallbacks);
	    
		Init();
	}

	public void Init() {
		Resources = mContext.getResources();
	//	CrashHandler.getInstance().init(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		//ExProc.Show(ex);
		thread.setDefaultUncaughtExceptionHandler( this);
	}
	
	/************************** 以下是静态方法 ******************************/
    private static HyApp mInstance = null;
	public static Resources Resources;
	private static long exitTime = 0;
	
	
	public static HyApp getInstance() {
		return mInstance;
	}

	public static Context getAppContext() {
		return mInstance.mContext;
	}

	public static String getVersion(){
		try {
			Context context = getAppContext();
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			return "1.0";
		}
	}

	public static int getResId(String resName,String defType){
    	Context context = getAppContext();
        return Resources.getIdentifier(resName, defType, context.getPackageName());  
    }  
    
	public static Drawable getDrawable(String name) {
		int resId= getResId(name, "drawable");
        return resId == 0 ? null : Resources.getDrawable(resId);
	} 
    
	public static Integer getColor(String name) {
		int resId= getResId(name, "color");
        return resId == 0 ? null : Resources.getColor(resId);
	}
    
	public static String getString(String name) {
		int resId= getResId(name, "string");
        return resId == 0 ? null : Resources.getString(resId);
	}
	
    public static Activity CurrentActivity(){
    	return mInstance.mActLifecycleCallbacks.GetCurrentActivity();
    }

    public static void runOnUiThread(Runnable runnable){
    	Activity act = CurrentActivity();
    	if(act == null) return;
    	act.runOnUiThread(runnable);
    }

	public static void Exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			//gc.Hint(msg);(CurrentActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
			gc.Hint("再按一次退出程序");
			exitTime = System.currentTimeMillis();
		} else {
			ExitNow();
		}
	}

	public static void ExitNow() {
		Activity act = CurrentActivity();
		if(act != null) act.finish();
		if(AppExitListener != null) AppExitListener.Notify(act, null);
		System.exit(0);
	}
	
	// 执行线程方法调用，并等待执行完毕
	public static Object ExecThreadCallBack(final gi.CallBack callback, boolean showLoading) throws Exception {
		final TempV r = new TempV();
		final Activity act = CurrentActivity();
		final LoopMsg lm = new LoopMsg();
		
		Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
            	try {
            		r.O = callback.Call();
            		lm.StopLoop(act, 1);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
            }
        });
		
		t.setUncaughtExceptionHandler(new UncaughtExceptionHandler(){
		    @Override
		    public void uncaughtException(Thread thread, Throwable ex){
		    	r.O = ex;
        		lm.StopLoop(act, 0);
		    	_D.Out(ex);
		    }
		});
		t.start();
        if(showLoading) LoadingDialog.Show(act, "");
        lm.Loop();
        if(showLoading) LoadingDialog.Hide();
        if(r.O instanceof Exception) throw (Exception)r.O;
        return r.O;
	}

	public static void SendMessage(Handler handler, int what, int arg1, int arg2){
		if(handler == null) return;
		handler.obtainMessage(what, arg1, arg2).sendToTarget();
	}

	public static void SendMessage(Handler handler, int what, int arg1, int arg2, Object obj){
		if(handler == null) return;
		handler.obtainMessage(what, arg1, arg2, obj).sendToTarget();
	}
}
