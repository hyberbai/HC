package hylib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import hylib.toolkits._D;
import hylib.toolkits.gv;

public class VGWrap extends LinearLayout { 
    private int mMargin;
    public int Mode;

    public final static int MODE_TABLEVIEW = 0;
    public final static int MODE_TOOLBAR = 1;
    
    public VGWrap(Context context) { 
        super(context); 
        setMargin(1);
    } 
    
	public VGWrap(Context context, AttributeSet attrs) {
		super(context, attrs);
		setMargin(1);
	}
	
	public void setMargin(int margin_dp) {
        mMargin = dipsToPixels(getContext(), margin_dp);
	}
	
	public int getMargin() {
        return mMargin; 
	}
	
    public static int dipsToPixels(Context context, int dips) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dips * scale + 0.5f);
    }

    int maxCW = 0;
    int maxCH = 0;
    int colCount = 0;
    boolean isOutWidth = false;
    @Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
    //    super.onMeasure(widthMeasureSpec, heightMeasureSpec);         if(1==1) return;
        int count = getChildCount();

        super.onMeasure(widthMeasureSpec, 0); 
        int clientWidth = getClientWidth();
        maxCW = 0;
        maxCH = 0;
        for (int i = 0; i < count; i++) { 
            final View child = this.getChildAt(i); 
            int w = child.getMeasuredWidth(); 
            int h = child.getMeasuredHeight();
            if(w > maxCW) maxCW = w;
            if(h > maxCH) maxCH = h;
        }
        int width = getMeasuredWidth();
        int height = 0;
        
        if(Mode == MODE_TABLEVIEW) {
            super.onMeasure(widthMeasureSpec, 0); 
            colCount = clientWidth / (maxCW + mMargin);
            height = gv.CeilDiv(count, colCount) * (maxCH + mMargin) +
            		 	+ getPaddingTop() + getPaddingRight();
        } else if (Mode == MODE_TOOLBAR) {
        	isOutWidth = (maxCW + mMargin) * count > clientWidth;
        	if(isOutWidth) 
        		width = getPaddingLeft() + getPaddingRight() + (maxCW + mMargin) * count;
            height = maxCH + getPaddingTop() + getPaddingRight();
        }
        //height = heightMeasureSpec;
        super.onMeasure(width, height);        
      //  height = getMeasuredHeight();
        setMeasuredDimension(width, height);   
    } 

    public int getClientWidth() {
		return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
	}
    
    @Override 
    protected void onLayout(boolean changed, int l, int t, int r, int b) { 
        int count = getChildCount(); 
    //    setBackgroundColor(0xFFEFF1E3);

        int width = getClientWidth();
        int left = getPaddingLeft();
        int top = getPaddingTop();

        if(Mode == MODE_TABLEVIEW) {
            if(colCount == 0) return;
            float cellWidth = width / colCount;
            float cellHeight = maxCH + mMargin;
            
            for (int i = 0; i < count; i++) { 
            	   
                View child = this.getChildAt(i); 

                int w = child.getMeasuredWidth(); 
                int h = child.getMeasuredHeight();
                int x = left + (int)((i % colCount) * cellWidth + (cellWidth - w) / 2);
                int y = top + (int)((i / colCount) * (maxCH + mMargin)  + (cellHeight - h) / 2);
              //  child.setBackgroundColor(0xFFFFEEAA);
                child.layout(x, y, x + w, y + h); 

                _D.Out(x , y );
            } 
        } else if (Mode == MODE_TOOLBAR) {
            if(count == 0) return;
        	float cellWidth = isOutWidth ? maxCW + mMargin : width / count;
            for (int i = 0; i < count; i++) { 
            	   
                View child = this.getChildAt(i); 

                int w = child.getMeasuredWidth(); 
                int h = child.getMeasuredHeight();
                int x = left + (int)(i * cellWidth + (cellWidth - w) / 2);
                int y = top + (int)((maxCH - h) / 2);
              //  child.setBackgroundColor(0xFFFFEEAA);
                child.layout(x, y, x + w, y + h); 

                _D.Out(x , y );
            } 
        }
    } 
   
   
}