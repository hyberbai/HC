package hylib.toolkits;

import java.io.File;

import android.util.Log;

// 调试类
public class _D {
	
	public static String LOG_TAG = "<T>";

    public static void Dumb()
    {
    }

	public static void Out(Throwable e) {
    	String msg = e instanceof MsgException ? e.getMessage() : 
			e instanceof java.net.ConnectException || e instanceof java.net.UnknownHostException ? "无法连接服务器！":                                                                                                                                                                                                                                                                                                        
				ExProc.GetErrMsg(e);
    	Out("!Exception: " + msg);
	}
    
	public static void Print(Object... os) {
		System.out.print(gs.JoinArray(os, ", "));
	}
    
	public static void Out(Object obj) {
		String msg = gv.StrVal(obj);
       // System.out.println(msg);
		Log.i(LOG_TAG, msg);
	}

	public static void Out(Object... os) {
		Out(gs.JoinArray(os, ", "));
	}

	public static void Out(String sh, String s) {
		Out(sh + ": " + s);
	}
	
    public static void Out(String format, Object... args) {
    	Out(String.format(format, args));
    }
    
	public static void Out(byte[] bytes) {
		Out(gu.BytesToHex(bytes, 0, bytes.length, 0));
	}
}
