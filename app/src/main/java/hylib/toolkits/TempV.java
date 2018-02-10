package hylib.toolkits;

import com.hc.g;

import android.R.bool;

public class TempV {
	public int I;
	public String S;
	public Object O;
	
	public <T> T get() {
		return (T)O;
	}

	public void set(Object v) {
		O = v;
	}

	public boolean isOK() {
		return O != null && gv.BoolVal(O);
	}

	public boolean hasValue() {
		return O != null;
	}
}
