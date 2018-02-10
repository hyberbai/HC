package hylib.view;

import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.gi;
import hylib.util.ParamList;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public class TextWatcherEx implements TextWatcher {
    private TextView view;
    public EventHandleListener changedListener;

    public TextWatcherEx(TextView v) {
        view = v;
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    	try {
    		ParamList pl = new ParamList();
    		pl.SetValue("s", s);
    		pl.SetValue("start", start);
    		pl.SetValue("before", before);
    		pl.SetValue("count", count);
        	if(changedListener != null) changedListener.Handle(view, pl);
		} catch (Exception e) {
			ExProc.Show(e);
		}
    }
}