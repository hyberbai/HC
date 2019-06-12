package hylib.toolkits;

// 通用接口类
public class gi {
	public static final int NF_INIT = 1;
	public static final int NF_OK = 2;
	public static final int NF_CANCEL = 3;
	public static final int NF_REFRESH = 5;
	
	public static final int NF_EXIT = -1;

	public interface IFunc<R> {
	    R Call();
	}
	
	public interface IFunc1<P, R> {
	    R Call(P p1);
	}
	
	public interface IFunc2<P1, P2, R> {
	    R Call(P1 p1, P2 p2);
	}
	
	public interface IFunc3<P1, P2, P3, R> {
	    R Call(P1 p1, P2 p2, P3 p3);
	}

	public interface CallBack {
	    Object Call() throws Exception;
	}

	public interface Action {
		void Execute() throws Exception;
	}

    public interface Action1<P> {
        void Execute(P p1);
    }

    public interface Action2<P1, P2> {
        void Execute(P1 p1, P2 p2);
    }

    public interface Action3<P1, P2, P3> {
        void Execute(P1 p1, P2 p2, P3 p3);
    }

    public interface Listener {
    	void Listen(Object sender);
    }
    
    public interface NotifyListener {
    	Object Notify(Object sender, Object arg);
    }

    public interface InputListener {
        boolean onInput(String input);
    }

}
