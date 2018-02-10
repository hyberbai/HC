package com.hc.mo.bill;

import java.lang.reflect.Field;

import com.hc.ID;
import com.hc.R;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import hylib.data.DataRow;
import hylib.data.DataTable;
import hylib.sys.HyApp;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.HyColor;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits._D;
import hylib.toolkits.gc;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.ui.dialog.UCCreator;
import hylib.ui.dialog.UCParamList;
import hylib.ui.dialog.UICreator;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.view.ActivityEx;
import hylib.widget.HyListAdapter;
import hylib.widget.HyListView;

public abstract class ActStats extends ActivityEx {
    LinearLayout llRoot;
    private HyListView lvStats;
    protected DataTable dtStats;
    private TextView tvTotal;
    protected HyListAdapter mListAdapter;
    public int CID;
    public static ParamList mapDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        llRoot = UICreator.CreateLayout(context, "bgc:white");
        getContentView().addView(llRoot);
        
        View hb = CreateHeaderBar();
        llRoot.addView(hb);
        
        CreateContentView();
        
        View bb = CreateBottomBar();
        llRoot.addView(bb);
        
        InitListAdapter();
        
        CID = Params.IntValue("CID");

        try {
        	Stats();
		} catch (Exception e) {
			ExProc.Show(e);
		}
    }

    public void setTitle(String title) {
    	$Set(ID.Title, title);
    }

    public void Stats() {
    	UpdateTotal();
    }
    
    public static ParamList getResMap(Class<?> clsRes) {
		Class<ID> clazz = ID.class;

		ParamList map = new ParamList();
		
        for( Field field : clazz.getFields())
			try {
				if(field.getType() != int.class) continue;
		        map.set(field.getName(), field.getInt(clazz));
			} catch (Exception e) {
				_D.Out(e);
			}
        return map;
	}
    
    public static ViewGroup CreateHeaderBar(){
    	Context context = HyApp.CurrentActivity();
    	UCParamList pl = new UCParamList(
			"items: [" + 
	        	"ib: { id:Back, style: header_button,bgc1:lb, bg:back }, " + 
	        	"tv: { id:Title, fs: 16sp, text: 标题, w:1w, h:wrap, grv:c, color:title_text_color, padding: 8dp }, " + 
	        	"ib: { id:Options, w:27dp, h:27dp, style: header_button, bg:options }, " + 
			"], style: header_bar");

    	ParamList plStyles = new ParamList(new Object[]{
                "header_button: { w:24dp, h:24dp, margin: 3dp }",
            	"header_bar: { hor, grv:c, margin: 0dp, h:wrap, w:match, padding: 0dp, color: w, bgc: head_layout_bg_color }",	
    	});
    	
        pl.setStyles("styles", plStyles);
        
		return UICreator.CreatePanel(context, pl);
    }

    public void CreateContentView(){
        LinearLayout ll = UICreator.CreateLayout(context, "bgc:white, h: 1w, w: match");

		ParamList pl = new ParamList("w: match, h:1w");
		lvStats = UCCreator.CreateListView(this, pl);
		ll.addView(lvStats);
		
		llRoot.addView(ll);;
    }
    
	protected void UpdateTotal() {
		if(dtStats == null) return;
		float totAmount = (Float)dtStats.getRows().Sum("Amount");
		int totQty = (Integer)dtStats.getRows().Sum("Qty");

		SpannableStringHelper sh = new SpannableStringHelper();

		if(totQty > 0)
		{
			sh.appendObj("合计：");
	
			sh.startSpan();
			sh.appendObj("x " + totQty + "  ");
			sh.setForeColor(Color.GRAY);
			sh.setFontSize(0.9f);
			
			sh.appendMoney(totAmount, Color.RED, 1.0f);
		}
		tvTotal.setText(sh);
	}
    
    public ViewGroup CreateBottomBar(){
    	Context context = HyApp.CurrentActivity();
    	
    	LinearLayout llBottom = UICreator.CreateLayout(context, "w: match, h: wrap");
        View v = new View(context);
        UICreator.SetViewParam(v, "w: match, h: 1px, bgc:bgl");
        llBottom.addView(v);
        
    	UCParamList pl = new UCParamList(
			"items: [" + 
		        "tv: { id:Total, text: 合计, style: sum }, " + 
	        	"btn: { id:Back, text: 返回, style: button }, " + 
			"], style: bottom_bar");
    	
    	ParamList plStyles = new ParamList(new Object[]{
                "sum: { w:1w, h: wrap, margin: 5dp, color: gray, fs: 14.2sp }",
                "button: { w:wrap, h: 30dp, color: button_text_color, margin: 5dp, bg: button_bg, fs: 14.2sp, padding: '20dp,5dp,20dp,5dp' }",
            	"bottom_bar: { hor, padding: 5dp, grv:r, margin: 0dp, h:wrap, w:match, padding: 0dp, bgc: item_bg_color }",	
    	});
    	
        pl.setStyles("styles", plStyles);
        llBottom.addView(UICreator.CreatePanel(context, pl));
        
        tvTotal = (TextView)llBottom.findViewById(ID.Total);
        
        return llBottom;
    }

    protected abstract Object getListItemViewConfig();
    protected abstract void setListItemViewData(ParamList arg);
    
    public void InitListAdapter(){
		ParamList pl = new ParamList();
		pl.SetValue("items", null);

        pl.SetValue("itemViewConfig", getListItemViewConfig());

        pl.SetValue("setViewData", new EventHandleListener() {
        	
        	@Override
        	public void Handle(Object sender, ParamList arg) throws Exception {
        		setListItemViewData(arg);
        	}
        });
        
//	        pl.SetValue("onEvent", new EventHandleListener() {
//				
//				@Override
//				public void Handle(Object sender, ParamList arg) throws Exception {
//					HyListView lv = (HyListView)sender;
//					drItem = (DataRow)(mListAdapter.getItem(((HyEvent.LvItemEventParams)arg).getPosition()));
//					if(arg instanceof LvItemClickEventParams) 
//						onListItemClick(lv, (LvItemClickEventParams)arg); 
//					else if(arg instanceof LvItemLongClickEventParams) 
//						onListItemLongClick(lv, (LvItemLongClickEventParams)arg);
//				}
//			});

        mListAdapter = HyListAdapter.Create(context, lvStats, null, pl);
        lvStats.setAdapter(mListAdapter);
		//UCCreator.setListViewParam(context, lvStats, pl);
		//mListAdapter = (HyListAdapter)lvStats.getAdapter();
    }
}
