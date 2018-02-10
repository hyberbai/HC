package hylib.sys;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

public class HyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {      
	private WeakReference<Activity> wrCurrentActivity;  
	
	public Activity GetCurrentActivity(){
        if (wrCurrentActivity == null) return null;  
        return wrCurrentActivity.get();  
	}

    @Override  
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {  
    	wrCurrentActivity = new WeakReference<Activity>(activity);  
    }  

    @Override  
    public void onActivityResumed(Activity activity) {
    	wrCurrentActivity = new WeakReference<Activity>(activity);  
    }

    @Override  
    public void onActivityStarted(Activity activity) {  
    }  

    @Override  
    public void onActivityStopped(Activity activity) {  
    } 

    @Override  
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {  
    }  

    @Override  
    public void onActivityPaused(Activity activity) {  
    }  

    @Override  
    public void onActivityDestroyed(Activity activity) {  
    }  
}
