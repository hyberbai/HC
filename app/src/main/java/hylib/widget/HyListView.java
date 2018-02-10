package hylib.widget;

import hylib.toolkits.type;
import android.R.bool;
import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class HyListView extends ListView {
	public boolean disableMeasure;
	
    public HyListView(Context context) {
        super(context);
    }

    public HyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void handleDataChanged() {
        super.handleDataChanged();
    	setSelection(getSelectedItemPosition());
    }
    
    public void setSelection(int pos) {
        int p0 = getFirstVisiblePosition();
        int p1 = getLastVisiblePosition();
        if(pos < p0 || pos > p1)
        	setSelectionFromTop(pos, getHeight() * 4 / 10);
	}
    
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
    {  
    	if(!disableMeasure)
    	{
	    	LinearLayout.LayoutParams lp = type.as(getLayoutParams(), LinearLayout.LayoutParams.class);
	    	if(lp != null && lp.height == LayoutParams.WRAP_CONTENT)
	    		heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
    	}
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
    }  
}