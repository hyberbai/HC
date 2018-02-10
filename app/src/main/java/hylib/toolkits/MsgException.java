package hylib.toolkits;

public class MsgException extends RuntimeException {
    private static final long serialVersionUID = -260351655312421809L;
    
	public MsgException() {
	}

    public MsgException(String detailMessage) {
        super(detailMessage);
    }
    
    public MsgException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
    
    @Override
    public String getMessage() {
    	String detailMessage = super.getMessage();
    	Throwable cause = getCause();
    	if(cause != null) detailMessage += "\n错误信息：\n" + cause.getMessage();
        return detailMessage;
    }
}
