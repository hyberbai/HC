package com.hc.oa;

import hylib.sys.HyApp;
import hylib.toolkits.gv;
import android.R.drawable;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.hc.MyApp;
import com.hc.SysData;
import com.hc.g;
import com.hc.dal.WS;
import com.hc.dal.d;

public class OA {

    private final static int OA_SCAN_INTERVAL = 3000;  
    private static int LastMailID = 0;
    
    public static Handler oa_handler = new Handler();

    private static int oa_lockcount = 0;

    public static void SendNotify(CharSequence title, CharSequence content) {
    	HyApp app = MyApp.getInstance();
 	   	NotificationManager mNotificationManager = (NotificationManager) app.getSystemService(Context.NOTIFICATION_SERVICE);
 	   	Notification notification = new Notification(drawable.ic_dialog_email, "通知: " + title, System.currentTimeMillis());
        //Intent intent = new Intent(getApplicationContext(), ActMain.class);
        Intent intent = new Intent("cn.finalist.msm.android.Main");
        PendingIntent contentIntent = PendingIntent.getActivity(app.getApplicationContext(), 100, intent, 0);
        //notification.setLatestEventInfo(app.getApplicationContext(), title, content, contentIntent);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        //notification.sound=Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.mm);
        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        mNotificationManager.notify(LastMailID, notification);
    }
    
    private static Runnable runOA = new Runnable() {  
    	  
        @Override  
        public void run() {
        	if(oa_lockcount > 0) return;
        	oa_lockcount++;
        	if(SysData.oa_account != "")
                new Thread(new Runnable() {

                    @Override
                    public void run() {

	    	            try {
	    	            	String result = WS.GetOAMail("getcount");
	    	            	if(result != null) {
	    	            		String[] ss = result.split("\\|");
		    	            	if(ss.length == 3) {
			    	            	int MailID = gv.IntVal(ss[1]);
			    	            	if(MailID > LastMailID) {
			    	            		SendNotify("您的OA邮箱有" + ss[0] +"封新邮件，请查收", "最新邮件: " + ss[2]);
			    	            		LastMailID = MailID;
			    	            	}
		    	            	}
	    	            	}
	    	            } catch (Exception e) {  
	    	                e.printStackTrace();  
	    	            }
    	            	oa_lockcount--;
                    }
                }).start();
                
               // g.LoopMsg();
//        	else
//                new Thread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                    	WSInvoke.ServerConnected();
//    	            	oa_lockcount--;
//                    }
//                }).start();
        	oa_handler.postDelayed(this, OA_SCAN_INTERVAL);
        }
    };  

	public static void StartWatch(){
		// 开启OA 后台邮件监控句柄
	//	oa_handler.postDelayed(runOA, OA_SCAN_INTERVAL);
	}
}
