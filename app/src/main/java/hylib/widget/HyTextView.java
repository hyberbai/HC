package hylib.widget;

import hylib.toolkits._D;
import hylib.toolkits.type;
import android.R.integer;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HyTextView extends TextView {

    private Context context;

    public HyTextView(Context context) {
        super(context);
        this.context = context;
    }

    public HyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public HyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);

        Layout layout = getLayout();
        if (layout != null) {
            int height = getHeight(this.getText().toString(), mode);
            int width = getMeasuredWidth();
            setMeasuredDimension(width, height);
        }
    }

    private int getHeight(String str, int mode) {
        float height = 0.0f;
        float width = getMeasuredWidth();
        float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        //这里具体this.getPaint()要注意使用，要看你的TextView在什么位置，
        // 这个是拿TextView父控件的Padding的，为了更准确的算出换行
        ViewGroup parent = (ViewGroup) getParent();
        int othersWidth = 0;
        for (int i = 0; i < parent.getChildCount(); i++) {
			View v = parent.getChildAt(i);
			if(v == this) continue;
			v.measure(0, 0);
			othersWidth += v.getMeasuredWidth();
        	_D.Out("sibling: %d", v.getMeasuredWidth());
		}
        
        while(parent != null){
//        	LinearLayout.LayoutParams lp = type.as(parent.getLayoutParams(), LinearLayout.LayoutParams.class);
//        	if(lp != null) othersWidth += lp.getMarginStart() + lp.getMarginEnd();
        	othersWidth += parent.getPaddingLeft() + parent.getPaddingRight();
        	_D.Out("Padding: %d, %d", parent.getPaddingLeft(), parent.getPaddingRight());
        	parent = type.as(parent.getParent(), ViewGroup.class);
        }

        //检测字符串中是否包含换行符,获得换行的次数，在之后计算高度时加上

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        Paint paint = this.getPaint();
        FontMetrics fm = paint.getFontMetrics();

        /**
         *  wrap_content/未指定宽度(MeasureSpec.UNSPECIFIED)，则用屏幕宽度计算
         *  否则就使用View自身宽度计算,并且无需计算Parent的Padding
         */
        int line = 0;
        float lineWidth = mode == MeasureSpec.UNSPECIFIED ?
        				widthPixels - paddingLeft - paddingRight - othersWidth :
        				width - paddingLeft - paddingRight;
        for (String s : str.split("\n")) {
            float textWidth = paint.measureText(s);
            line += (int) Math.ceil((textWidth / lineWidth));  
		}
line++;
        height = (fm.descent - fm.ascent) * line;

    	_D.Out(str);
    	_D.Out("---------------------------othersWidth: %d, line: %d height: %f", othersWidth, line, height);
        return (int) Math.ceil(height) + getCompoundPaddingTop() + getCompoundPaddingBottom();
    }
    
    
}