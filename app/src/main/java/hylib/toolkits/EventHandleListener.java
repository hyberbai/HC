package hylib.toolkits;

import hylib.util.ParamList;

public interface EventHandleListener {
	public void Handle(Object sender, ParamList arg) throws Exception;
}