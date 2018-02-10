package hylib.widget;

import hylib.util.ParamList;

public class HyEvent {

	public static class LvItemEventParams extends ParamList {
		
		private static final long serialVersionUID = 1L;

		public int getPosition() {
			return IntValue("position");
		}

		public void setPosition(int position) {
			SetValue("position", position);
		}
	}
	
	public static class LvItemClickEventParams extends LvItemEventParams {

		private static final long serialVersionUID = 1L;
		
	}

	public static class LvItemLongClickEventParams extends LvItemEventParams {
		
		private static final long serialVersionUID = 1L;

		public boolean getHandled() {
			return getValue("handled", true);
		}

		public void setHandled(boolean handled) {
			SetValue("handled", handled);
		}
	}
}
