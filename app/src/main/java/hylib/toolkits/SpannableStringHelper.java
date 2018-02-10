package hylib.toolkits;

import java.text.DecimalFormat;

import android.R.integer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

public class SpannableStringHelper extends SpannableStringBuilder {
	private int len;
	private float mLastFS = 1;
	
	public SpannableStringHelper() {
		len = 0;
	}
	
	public int getLen() {
		return len;
	}
	
	public void setForeColor(int color, int start, int end){
		setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
	}
	
	public void setFontSize(float fs, int start, int end){
		setSpan(new RelativeSizeSpan(fs/mLastFS), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		mLastFS = fs;
	}
	
	public void setForeColor(int color){
		setForeColor(color, mStart, len);
	}
	
	public void setFontSize(float fs){
		setFontSize(fs, mStart, len);
	}
	
	public int appendObj(Object o){
		String text = gv.StrVal(o);
		append(text);
		len += text.length();
		return len;
	}

	public int append(Object o, Object span){
		int start = len;
		appendObj(o);
		setSpan(span, start, len, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		return len;
	}

	public int appendSizeSpan(Object o, float fs){
		startSpan();
		appendObj(o);
		setFontSize(fs);
		return len;
	}
	
	public int appendLine(){
		return appendObj("\n");
	}
	
	public int appendLine(Object o){
		appendObj(o);
		return appendLine();
	}

	private int mStart;
	public void startSpan(){
		mStart = len;
	}

	public void endSpan(Object what){
		setSpan(what, mStart, len, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
	}

	public int appendMoney(Float money, int color, float fontsize){
        DecimalFormat df = new DecimalFormat("#,##0.00");  
        String text = df.format(money);  
		//String text = String.format("%.2f", money);
		int pi = text.indexOf('.');
		gs.CutResult r = gs.Cut(text, ".");
		if(fontsize <= 0) fontsize = 1;
		int start = len;

		appendSizeSpan("￥", fontsize * 0.9f);
		
		appendSizeSpan(r.S1, fontsize * 1.12f);
		
		//if(gv.IntVal(r.S2) > 0)
		appendSizeSpan("." + r.S2, fontsize * 0.95f);

		setForeColor(color, start, len);
		return len;
	}


	public void appendText(String text, int color, float fontsize){
		startSpan();
		appendObj(text);
		setForeColor(color);
		setFontSize(fontsize);
	}
}
