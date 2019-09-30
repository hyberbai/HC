package hylib.sys;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

    private static boolean isNetOK(int type) {
        Context context = HyApp.getAppContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null) return false;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == type;
    }

    //判断网络连接是否可用
    public static boolean isNetworkAvailable() {
        Context context = HyApp.getAppContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;

        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        if (networkInfo == null) return false;
        for (int i = 0; i < networkInfo.length; i++)
            if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                return true;
        return false;
    }

    //判断WiFi是否打开
    public static boolean isWiFiAvailable() {
        return isNetOK(ConnectivityManager.TYPE_WIFI);
    }

    //判断移动数据是否打开
    public static boolean isMobileAvailable() {
        return isNetOK(ConnectivityManager.TYPE_MOBILE);
    }

}
