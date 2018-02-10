package hylib.widget;  
  
import hylib.toolkits.gc;
import hylib.ui.dialog.UICreator;
import hylib.ui.dialog.UIUtils;
import android.R.integer;
import android.content.Context;  
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.Drawable;  
import android.text.Editable;  
import android.text.TextWatcher;  
import android.util.AttributeSet;  
import android.view.MotionEvent;  
import android.view.View;  
import android.view.View.OnFocusChangeListener;  
import android.view.animation.Animation;  
import android.view.animation.CycleInterpolator;  
import android.view.animation.TranslateAnimation;  
import android.widget.EditText;  

import com.hc.R;
import com.hc.R.color;
import com.hc.pu;
  
public class HyEditText extends EditText implements OnFocusChangeListener, TextWatcher {  
    private Drawable mClearDrawable;  
    private ViewBorderStyle borerStyle = ViewBorderStyle.RoundCorner;
    /** 
     * 控件是否有焦点 
     */  
    private boolean hasFoucs;  
  
    public HyEditText(Context context) {  
        this(context, null);
    }  
  
    public HyEditText(Context context, AttributeSet attrs) {  
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义  
        this(context, attrs, android.R.attr.editTextStyle);  
    }  
  
    public HyEditText(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        init();  
        UICreator.SetViewParam(this, "margin: 8dp");
        if(borerStyle == ViewBorderStyle.RoundCorner) {
        	setBackgroundResource(R.drawable.lv_num_bg);
        }
    }  
    
    private void init() {  
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片  
        setBackgroundColor(color.gray);
        mClearDrawable = getCompoundDrawables()[2];  
        if (mClearDrawable == null) {  
            setClearDrawable(R.drawable.input_delete);  
        }  
  
        
        setClearIconVisible(false);  
        setOnFocusChangeListener(this);  
        addTextChangedListener(this);  
    }  
  
    private void SetDrawableBounds(Drawable drawable){
    	if(drawable == null) return;
        int fh = UIUtils.getFontHeight(getTextSize());
        drawable.setBounds(0, 0, fh, fh);
    }
  
    /** 
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和 
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑 
     */  
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        if (event.getAction() == MotionEvent.ACTION_UP) {  
            if (getCompoundDrawables()[2] != null) {  
  
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())  
                        && (event.getX() < ((getWidth() - getPaddingRight())));  
  
                if (touchable) {  
                    this.setText("");  
                }  
            }  
        }  
  
        return super.onTouchEvent(event);  
    }  
  
    /** 
     * 当HyEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏 
     */  
    @Override  
    public void onFocusChange(View v, boolean hasFocus) {  
        this.hasFoucs = hasFocus;  
        if (hasFocus) {  
            setClearIconVisible(getText().length() > 0);  
        } else {  
            setClearIconVisible(false);  
        }  
    }  

    @Override  
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {  
  
    }  
  
    @Override  
    public void afterTextChanged(Editable s) {  
  
    }  
    
    public void setClearDrawable(int id){
    	mClearDrawable = getResources().getDrawable(id);
        SetDrawableBounds(mClearDrawable);
    	setCompoundDrawables(0, 0, id, 0);
    }
    
    /** 
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去 
     */  
    protected void setClearIconVisible(boolean visible) {  
        Drawable right = visible ? mClearDrawable : null;  
        Drawable left = getCompoundDrawables()[0];//getResources().getDrawable(R.drawable.abc_ic_search_api_holo_light);  

        SetDrawableBounds(left);
        setCompoundDrawables(left, getCompoundDrawables()[1], right, getCompoundDrawables()[3]);  
    }  

    public void setCompoundDrawables(int l, int t, int r, int b) {  
    	Drawable[] drawables = getCompoundDrawables();
        Drawable left = l == 0 ? drawables[0] : getResources().getDrawable(l);  
        Drawable top = t == 0 ? drawables[1] : getResources().getDrawable(t);  
        Drawable right = r == 0 ? drawables[2] : getResources().getDrawable(r);  
        Drawable bottom = b == 0 ? drawables[3] : getResources().getDrawable(b);  
        setCompoundDrawables(left, top, right, bottom);  
    }
  
    /** 
     * 当输入框里面内容发生变化的时候回调的方法 
     */  
    @Override  
    public void onTextChanged(CharSequence s, int start, int count,  
                              int after) {  
        if(hasFoucs) setClearIconVisible(s.length() > 0);  
    }  
  
    /** 
     * 设置晃动动画 
     */  
    public void setShakeAnimation(){  
        this.setAnimation(shakeAnimation(5));  
    }  
  
    /** 
     * 晃动动画 
     * @param counts 1秒钟晃动多少下 
     */  
    public static Animation shakeAnimation(int counts){  
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);  
        translateAnimation.setInterpolator(new CycleInterpolator(counts));  
        translateAnimation.setDuration(1000);  
        return translateAnimation;  
    }  
  
  
}  