package hylib.ui.dialog;

import android.view.View;

public class ListItemViewInfo {
	private View mView;
	public int index;
	
	public ListItemViewInfo(View view){
		mView = view;
	}

	public ListItemViewInfo(View view, int index){
		this(view);
		this.index = index;
	}
	
	public View getView(int id){
		View v = mView.findViewById(id);
		if(v == null) return null;
		v.setTag(this);
		return v;
	}
}
