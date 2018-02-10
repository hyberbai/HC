package hylib.sys;

import java.util.Locale;

public class SysUtils {  
	  
    /** 
     * 获取当前手机系统语言。 
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN” 
     */  
    public static String getSystemLanguage() {  
        return Locale.getDefault().getLanguage();  
    }  
  
    /** 
     * 获取当前系统上的语言列表(Locale列表) 
     */  
    public static Locale[] getSystemLanguageList() {  
        return Locale.getAvailableLocales();  
    }  
  
    /** 
     * 获取当前手机系统版本号 
     */  
    public static String getSystemVersion() {  
        return android.os.Build.VERSION.RELEASE;  
    }  
  
    /** 
     * 获取手机型号 
     */  
    public static String getModel() {  
        return android.os.Build.MODEL;  
    }  
  
    /** 
     * 获取手机厂商 
     */  
    public static String getDeviceBrand() {  
        return android.os.Build.BRAND;  
    }  
  
//    /** 
//     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限) 
//     */  
//    public static String getIMEI(Context ctx) {  
//        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);  
//        if (tm != null) {  
//            return tm.getDeviceId();  
//        }  
//        return null;  
//    }  
}  