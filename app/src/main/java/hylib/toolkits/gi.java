package hylib.toolkits;

import hylib.util.ParamList;

import java.util.ArrayList;

import android.text.TextWatcher;

// 通用接口类
public class gi {
	public static final int NF_INIT = 1;
	public static final int NF_OK = 2;
	public static final int NF_CANCEL = 3;
	public static final int NF_REFRESH = 5;
	
	public static final int NF_EXIT = -1;

	public interface IFunc<R> {
	    public  R Call();
	}
	
	public interface IFunc1<P, R> {
	    public  R Call(P p1);
	}
	
	public interface IFunc2<P1, P2, R> {
	    public  R Call(P1 p1, P2 p2);
	}
	
	public interface IFunc3<P1, P2, P3, R> {
	    public  R Call(P1 p1, P2 p2, P3 p3);
	}

	public interface CallBack {
	    public Object Call() throws Exception;
	}

    public interface Listener {
    	public void Listen(Object sender);
    }
    
    public interface NotifyListener {
    	public Object Notify(Object sender, Object arg);
    }

    public interface InputListener {
        boolean onInput(String input);
    }

}
