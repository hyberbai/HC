package hylib.toolkits;

import com.hc.MyApp;

import hylib.sys.HyApp;
import hylib.ui.dialog.Msgbox;
import android.R.bool;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

// 异常处理类
public class ExProc {
	
	// 产生一个消息提示异常
    public static void ThrowMsgEx(String msg) {
    	throw new MsgException(msg);
    }
    
	// 产生一个空异常
    public static void ThrowEmpty() {
    	ThrowMsgEx("");
    }
    
    public static void ThrowMsgEx(String msg, Throwable throwable) {
    	throw new MsgException(msg, throwable);
    }
    
    private static String GetDefaultErrorMsg(Throwable e) {
		String s = e.getLocalizedMessage();
		s += "\n调用堆栈:\n" + getStackTraceInfo(e);
		return s;
	}
    
    private static String getStackTraceInfo(Throwable e){
    	if(e == null) return "";
    	StringBuilder sb = new StringBuilder();
    	for (StackTraceElement te : e.getStackTrace()) 
			sb.append(String.format("%s.%s:%s\n",
					te.getFileName().replace(".java", ""), te.getMethodName(), te.getLineNumber()));
    	return sb.toString();
    }

	public static boolean IsNetException(Throwable e) {
    	if(e instanceof RuntimeException) e = ((RuntimeException)e).getCause();
    	return e instanceof java.net.ConnectException || 
    		   e instanceof java.net.UnknownHostException;
	}
	
	public enum Level { Normal, Warning, Error }
	public static class ExMsg {
		public String msg;
		public String MoreInfo;
		public Level level = Level.Normal;
		
		@Override
		public String toString() {
			if(level == Level.Normal) return msg;
	    	return msg +  "\n错误信息: " + MoreInfo;		
		}
	}

	public static ExMsg GetExMsg(Throwable e, String hint) {
    	if(e instanceof RuntimeException && !(e instanceof MsgException))
    	{
    		Throwable cause = ((RuntimeException)e).getCause();
    		if(cause != null) e = cause;
    	}
    	ExMsg em = new ExMsg();
    	em.msg = e instanceof MsgException ? e.getMessage() : 
    			 e instanceof java.net.ConnectException ? "无法连接服务器，请检查WLAN是否已开启！":                
    			 e instanceof java.net.UnknownHostException ? "连接服务器失败！[无效主机]":          
        		 e instanceof java.net.SocketTimeoutException ? "连接超时，请检查无线连接是否正确！":    
        		 null;
    	if(em.msg == null) {
    		em.msg = hint.length() == 0 ? "操作失败！" : hint;
    		em.MoreInfo = GetDefaultErrorMsg(e);
    		em.level = Level.Error;
    	}

    	return em;
	}

	public static String GetErrMsg(Throwable e, String hint) {
		return GetExMsg(e, hint).toString();
	}
	
	public static String GetErrMsg(Throwable e) {
		return GetErrMsg(e, "");
	}
	
    public static void Show(Context context, String hint, Throwable e) {
    	if(e == null) return;
		ExMsg em = GetExMsg(e, hint);
    	if(em.level == Level.Normal)
    		gc.HintTd(context, em.toString());
    	else
    		Msgbox.Hint(em.toString());
    }
    
    public static void Show(Context context, Exception e) {
    	Show(context, "", e);
    }
    
    public static void Show(String hint, Throwable e) {
    	Show(HyApp.CurrentActivity(), hint, e);
    }
    
    public static void Show(Exception e) {
    	Show(HyApp.CurrentActivity(), "", e);
    }
    
    public static void ShowMsgbox(Context context, Exception e) {
    	Msgbox.Hint(context, GetErrMsg(e, ""));
    }
}
