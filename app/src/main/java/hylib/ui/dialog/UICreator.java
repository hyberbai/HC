package hylib.ui.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hc.ID;
import com.hc.R;

import java.util.HashMap;
import java.util.Map;

import hylib.data.DataRowCollection;
import hylib.edit.DType;
import hylib.edit.EditField;
import hylib.edit.EditFieldList;
import hylib.edit.ValidType;
import hylib.sys.HyApp;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.UnitType;
import hylib.toolkits.UnitValue;
import hylib.toolkits._D;
import hylib.toolkits.gc;
import hylib.toolkits.gs;
import hylib.toolkits.gu;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.util.ActionInfo;
import hylib.util.ActionList;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.view.TextWatcherEx;
import hylib.widget.HyListAdapter;
import hylib.widget.HyListView;
import hylib.widget.VGWrap;

public class UICreator {
	public Context context;
	public EditFieldList mEFields;
	public ParamList mStyles;

	public final static int FVT_VIEW = 1;
	public final static int FVT_TEXTVIEW = 2;
	public final static int FVT_EDITTEXT = 3;
	public final static int FVT_COMBOBOX = 4;
	public final static int FVT_BUTTON = 5;
	public final static int FVT_IMG_BUTTON = 51;
	public final static int FVT_DATEPICKER = 6;
	public final static int FVT_RADIOGUP = 7;
	public final static int FVT_CHECKBOX = 8;
	public final static int FVT_LISTVIEW = 9;
	
	public final static int FVT_SLINE = 21;	// 分隔线

	public static int PAGE_INDICATOR_TYPE_DOT = 1;	// 圆点类型指示器
	public static int PAGE_INDICATOR_TYPE_TAB = 2;	// 页签类型指示器
	
	public static EventHandleListener onGetAdapter;
	
	public static int valueOfFVTypeName(String name) {
		if(name.equals("v")) return FVT_VIEW;
		if(name.equals("tv")) return FVT_TEXTVIEW;
		if(name.equals("et")) return FVT_EDITTEXT;
		if(name.equals("cb")) return FVT_COMBOBOX;
		if(name.equals("dp")) return FVT_DATEPICKER;
		if(name.equals("rg")) return FVT_RADIOGUP;
		if(name.equals("chk")) return FVT_CHECKBOX;
		if(name.equals("ib")) return FVT_IMG_BUTTON;
		if(name.equals("btn")) return FVT_BUTTON;
		if(name.equals("lv")) return FVT_LISTVIEW;
		if(name.equals("-")) return FVT_SLINE;
		return 0;
	}
	
	public UICreator(Context context) {
		this.context = context;
	}

    public ViewGroup CreateToolBar(String config, ParamList pl) throws Exception {
		VGWrap bar = new VGWrap(context);
		bar.Mode = VGWrap.MODE_TOOLBAR;
		setViewParam(bar, "w:match, padding:0dp, bgc:#EEE");
		ActionList acl = pl.$("ACL");

        String[] ss = config.split(" |,");
        final View.OnClickListener onClick = new View.OnClickListener() {
            public void onClick(View v){
            	ActionInfo act = type.as(v.getTag(), ActionInfo.class);
            	if(act == null) return;
            	gc.ExecAction(context, context.getClass(), act);
            }
        };
        
        final Map<String, ViewGroup> mapViews = new HashMap<String, ViewGroup>();
        for(String s : ss)
        {
        	ActionInfo act = ActionList.getAction(s, acl);
        	if(act == null) continue;
        	
			UCParamList plView = new UCParamList("items: [" + 
	        	"v: { id: 501, w: 30dp, h: 30dp, lay-grv:c  }, " + 
	        	"tv: { id: 502, fs: 12.8sp, text: " + act.Title  + ",margin1:0dp, w: wrap, grv:c, bgc1:#CFC }" +
	        "], w: wrap, h: wrap, bgc1:#CCF,padding:5dp, grv:c ");
	        
	        ParamList plImg = plView.findViewParams(501);
	        plImg.put("bg", act.ResID);
	        ViewGroup pnl = CreatePanel(plView);
			pnl.setTag(act);
			pnl.setOnClickListener(onClick);
        	bar.addView(pnl);
        	mapViews.put(act.Name, pnl);
		}
        acl.onPropChangedListener = new EventHandleListener() {
        	public void Handle(Object s, ParamList e) {
        		ActionInfo act = (ActionInfo)s;
        		ViewGroup pnlView = mapViews.get(act.Name);
        		
        		View vImg = pnlView.findViewById(501);
        		vImg.setBackgroundResource(act.ResID);
        		
        		TextView tv = (TextView)pnlView.findViewById(502);
        		tv.setText(act.Title);
			}
		};
		return bar;
	}

	public void CreatePopupMenu(View v, String[] items) {
        PopupMenu popup = new PopupMenu(context, v);//第二个参数是绑定的那个view
        Menu menu = popup.getMenu();
        int i = 0;
        for (String item : items) {
            menu.add(0, i, i, item);
            i++;
		}

        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				return false;
			}
			
		});

        popup.show();
	}

	public PopupWindowEx CreatePopupMenu(final ParamList pl) throws Exception {
		LinearLayout ll = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    ll.setLayoutParams(lp);
        int padding = gu.dp2px(context, 0);
       // Drawable drawable = UCCreator.getGradientDrawable(0xFFFCFCFC, 0xCCCCCCCC);
        //drawable = getShapeDrawable(Color.WHITE, 0xCCCCCCCC, gu.dp2px(context, 3));
        //ll.setBackground(drawable);
     //   ll.setBackgroundResource(R.drawable.menu_bg);
        
     //   ll.setBackgroundResource(R.drawable.btn_num_bg);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(padding, padding, padding, padding);
        
		HyListView lv = UCCreator.CreateListView(context, true);
		final PopupWindowEx pw = new PopupWindowEx(ll);
		pw.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.abc_menu_dropdown_panel_holo_light));
		pw.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件
		pw.Items =  pl.Get("Items", DataRowCollection.class);
		pw.Params = pl;
		//lv.setLayoutParams(new ViewGroup.LayoutParams(300, LayoutParams.WRAP_CONTENT));

		HyListAdapter apt = new HyListAdapter(context, pw.Items);
		apt.getViewListener = new HyListAdapter.OnGetViewListener() {

			@Override
			public View onGet(int position, View convertView, ViewGroup parent) {
				String value = pw.Items.get(position).getStrVal("text");
				if (convertView == null) {
					//convertView = LayoutInflater.from(context).inflate(R.layout.list_sale_item, null);
					
					LinearLayout ll = new LinearLayout(context);
			        int padding = gu.dp2px(context, 10);
//					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,  LayoutParams.MATCH_PARENT);
//					lp.gravity = Gravity.CENTER_VERTICAL;
//				    ll.setLayoutParams(lp);
				    //ll.setBackgroundColor(0xFFCCCCFF);
			        ll.setOrientation(LinearLayout.HORIZONTAL);
			        ll.setPadding(padding, padding, padding, padding);
			        
					TextView tv = new TextView(context);
					int w = gu.getPx(context, pl.SValue("width"));
					tv.setWidth(w == 0 ? LayoutParams.WRAP_CONTENT : w);
					
					tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
					tv.setSingleLine();
					tv.setTextColor(Color.BLACK);
					tv.setGravity(Gravity.CENTER_VERTICAL);
					tv.setId(100);
					
					ll.addView(tv);
					convertView = ll;
				}
//				View v =convertView.findViewById(R.id.item_info);
//				UIUtils.setViewValue(v, value);
				View v = convertView.findViewById(100);
				UIUtils.setViewValue(v, value);
				v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				return convertView;
			}
		};
		lv.setAdapter(apt);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					gu.SafeExecuteMethod(context, "Act" + pw.Items.get(position).getStrVal("name"));
					pl.Call("ItemClick", view, new ParamList("position", position));
					pw.dismiss();
				} catch (Exception e) {
					ExProc.Show(e);
				}
			}
		});

		// 控制popupwindow的宽度和高度自适应
		lv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		
		ll.addView(lv);

		pw.setWidth(lv.getMeasuredWidth());
		pw.setHeight((lv.getMeasuredHeight() + 20) * 10);
	//	pw.setWidth(LayoutParams.WRAP_CONTENT);//lv.getMeasuredWidth());
		pw.setHeight(LayoutParams.WRAP_CONTENT);
		
		

		// 控制popupwindow点击屏幕其他地方消失
	//	pw.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
		pw.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
		return pw;
	}

    public ViewPager CreateViewPager(ViewGroup parent, View[] pageViews, final ParamList pl) {
    	Activity act = (Activity)context;
        final UICreator uc = new UICreator(act);
        // 创建分页组件
    	final ViewPager vp = new ViewPager(act);
    	uc.setViewParam(vp, "w:match,h:1w");

        final int type = pl.containsKey("pageNames") ? PAGE_INDICATOR_TYPE_TAB : PAGE_INDICATOR_TYPE_DOT;
        try {

        	ViewGroup panel;
            if(type == PAGE_INDICATOR_TYPE_TAB)
            {
            	panel = uc.createLayout("w:match, h:1w");
            	//panel = new LinearLayout(act);
//            	LinearLayout.LayoutParams lpPanel = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//                panel.setLayoutParams(lpPanel);
            	//uc.setViewParam(panel, "w:match, h:match");
            }
            else {
            	panel = new FrameLayout(act);
            	setViewParam(panel, "w:match, h:1w");
            }

            LinearLayout llTab = new LinearLayout(act);
            llTab.setOrientation(LinearLayout.HORIZONTAL);

            final ParamList plPage = new ParamList();
            
            
            final View[] indiViews = new View[pageViews.length];
            final int padding = uc.getUnitIntValue("5dp");
            if(type == PAGE_INDICATOR_TYPE_TAB)
            {
            	uc.setViewParam(llTab, "ver, w: match, h: wrap, lay-grv: top, bgc:bg ");
            
                // 创建页签
                LinearLayout llItems = uc.createLayout("hor, w:match, padding: " + padding + ", h: wrap");
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                 
            	String[] names = gs.Split(pl.SValue("pageNames"));
            	View.OnClickListener onClick = new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						vp.setCurrentItem((Integer)v.getTag());
					}
            	};
                for(int i = 0; i < indiViews.length; i++)
                	try {

                		TextView v = new TextView(act);
            	        v.setText(names[i]);
            	        uc.setViewParam(v, "w:1w, fs:15sp, grv:c");
            	       // v.setBackgroundResource(R.drawable.button_bg_normal);
            	        v.setOnClickListener(onClick);
            	        v.setTag(i);
            	        llItems.addView(v);
            	        indiViews[i] = v;
    				} catch (Exception ex) {
    					ExProc.Show(ex);
    				}

                LinearLayout llCursor = uc.createLayout("w:100dp,h: 2dp, bgc:holo, paddingLeft:" + padding + ",paddingRight:" + padding);
                llTab.addView(llItems);
                llTab.addView(llCursor);
                llTab.addView(uc.createView("w:match,h: 1px, bgc:bgl"));
                pl.SetValue("llCursor", llCursor);
            }
            else {
                // 创建圆点页面指示器
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, gu.dp2px(act, 20));
                lp.gravity = Gravity.BOTTOM;
                llTab.setLayoutParams(lp);
                llTab.setBackgroundColor(0x00000000);
                llTab.setGravity(Gravity.CENTER);
                
                // 创建圆点
                int size = gu.dp2px(act, 8);
                int margin = gu.dp2px(act, 2);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(size, size);
                for(int i = 0; i < indiViews.length; i++)
                {
        	        View v = new View(act);
        	        lp2.setMargins(margin, 0, margin, 0);
        	        v.setLayoutParams(lp2);
        	        v.setBackgroundResource(i == 0 ? R.drawable.dot_focused : R.drawable.dot_normal);
        	        llTab.addView(v);
        	        indiViews[i] = v;
                }
    		}

            plPage.SetValue("index", 0);

            // 创建视图页适配器
            ViewPagerAdapter apt = new ViewPagerAdapter(); 
            apt.views = pageViews;
            vp.setAdapter(apt);
            
            OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

            	@Override
                public void onPageSelected(int position) {
            		if(indiViews.length == 0) return;
            		int lastIndex = plPage.$("index");
            		if(type == PAGE_INDICATOR_TYPE_TAB)
            		{
            			LinearLayout llCursor = pl.$("llCursor");
            	        Display display = HyApp.CurrentActivity().getWindowManager().getDefaultDisplay();  
            	        DisplayMetrics dm = new DisplayMetrics();  
            	        display.getMetrics(dm);  
            	        
            	        int screenWidth = dm.widthPixels;  
            			int count = indiViews.length;
            			int tabWidth = (screenWidth - padding * 2) / count;
            			int textWidth = tabWidth * 2 / 3;
            			int offset = (tabWidth - textWidth) / 2;  
            			uc.setViewParam(llCursor, "w:" + textWidth + "px, marginLeft:" + offset);
            			
                        Animation animation = new TranslateAnimation(tabWidth * lastIndex + padding, tabWidth * position + padding, 0, 0);  
                        animation.setDuration(100l);  
                        animation.setFillAfter(true);// 动画结束后停留在当前所处位置  
                        llCursor.startAnimation(animation);
            			
            			uc.setViewParam(indiViews[lastIndex], "bgc:0, color: gray");
            			uc.setViewParam(indiViews[position], "color: black");
            		}
            		else{
            			indiViews[lastIndex].setBackgroundResource(R.drawable.dot_normal);
                		indiViews[position].setBackgroundResource(R.drawable.dot_focused);
            		}
            		plPage.SetValue("index", position);
                    try {
                        ParamList ea = new ParamList("pos", position);
                    	pl.Call("PageSelected", vp, ea);
    				} catch (Exception e) {
    					ExProc.Show(e);
    				}
                    // currentItem = position;
                }
                
                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }
                
                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            }; 
            
            onPageChangeListener.onPageSelected(0);
            vp.setOnPageChangeListener(onPageChangeListener);

            panel.addView(llTab);
            panel.addView(vp);
            panel.addView(uc.createView("w:match,h: 1px, bgc:bgl"));
            parent.addView(panel);
		} catch (Exception e) {
			ExProc.Show(e);
		}
        return vp;
	}

    public UnitValue[] getPxValues(Object[] value) {
//    	if(value instanceof String) 
//    	if(value instanceof Object[]) 
//    	return null;
		UnitValue[] values = new UnitValue[((Object[])value).length];
		int i = 0;
		for (Object o : (Object[])value) {
			values[i++] = gu.caleCommonUnitValue(context, gv.StrVal(o));
		}
		return values;
    }

    public UnitValue getUnitValue(String value) {
    	return gu.caleCommonUnitValue(context, value);
    }

    public int getPx(String value) {
    	return gu.getPx(context, value);
    }

    public int[] getPxValues(String value) {
    	if(value.isEmpty()) return null;
    	String[] ss = gs.Split(value);

		int[] values = new int[ss.length];
		int i = 0;
		for (String s : ss) values[i++] = getPx(s);
		return values;
    }

    public int getUnitIntValue(Object value) {
    	if(value instanceof Integer) return (Integer)value;
    	if(value instanceof String) {
        	UnitValue v = getUnitValue((String)value);
        	if(v != null) return v.IntVal();
    	}
    	return V_NULL;
    }

    public View createView(String params) throws Exception {
    	View v = new View(context);
    	setViewParam(v, params);
        return v;
	}
    
    public View createLine(String size, String color) throws Exception {
        return createView("w:match,h: " + size + ", bgc:" + color);
	}
	
	public LinearLayout createLayout(String config) {
        return createLayout(new ParamList(config));
	}
	
	public LinearLayout createLayout(ParamList pl) {
        LinearLayout ll = new LinearLayout(context);
		int ori = pl.BValue("hor") ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL;
        ll.setOrientation(ori);
        setViewParam(ll, pl);
        return ll;
	}

	// 参数
	private static class PM {
		public boolean isNeed;
		public int margin;
		public float fontSize;
		public UnitValue lw;
	//	public LinearLayout ll;
	}
	
	public View createTitleTextView(String text, PM pm) {
        if(text == null || text.length() == 0) return null;
        
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp1.gravity = Gravity.CENTER;
        lp1.setMarginEnd(pm.margin);
        tv.setLayoutParams(lp1);
        tv.setTextColor(pm.isNeed ? 0xFF3355AA : 0xFF888888);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, pm.fontSize);
        if(pm.lw.Type == UnitType.px) tv.setWidth(pm.lw.IntVal());
        //v.setHeight(100);
        tv.setText(text);
        
       // pm.ll.addView(tv);
        return tv;
	}
	
	private static int V_NULL = gu.V_NULL;

	public interface SetLTRBCallBack {
	    public void set(int left, int top, int right, int bottom);
	}
	
	public LayoutParams newLayoutParams(){
    	return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	
	public static int getParentOrientation(View v) {
		LinearLayout ll = type.as(v.getParent(), LinearLayout.class);
		return ll == null ? -1 : ll.getOrientation();
	}
	
	public LayoutParams UpdateLayoutParams(View v, int ori, UnitValue uw, UnitValue uh){
		LayoutParams lp = v.getLayoutParams();
		if(uw == null && uh == null) return lp;
		if(lp == null) lp = newLayoutParams();
		
		if(uw != null) { 
			if(uw.Type == UnitType.fit)
				_D.Dumb();
			if(uw.Type == UnitType.weight) {
				lp.width = 0;
				((LinearLayout.LayoutParams)lp).weight = uw.IntVal(); 
			} else {
				lp.width = uw.IntVal();
				if(ori == LinearLayout.HORIZONTAL) ((LinearLayout.LayoutParams)lp).weight = 0;
			}
		}

		if(uh != null){ 
			if(uh.Type == UnitType.weight) {
				lp.height = 0;
				((LinearLayout.LayoutParams)lp).weight = uh.IntVal(); 
			} else {
				lp.height = uh.IntVal();
				if(ori == LinearLayout.VERTICAL) ((LinearLayout.LayoutParams)lp).weight = 0;
			}
		} 

    	return lp;
	}
	
	public void setViewParam(final View v, ParamList pl) {
		if(v == null) return;
		if(pl == null) return;

		LinearLayout ll = type.as(v.getParent(), LinearLayout.class);
		
		// 先加载样式参数
    	String style = pl.SValue("style");
    	if(!style.isEmpty()) setViewParam(v, mStyles.GetParamList(style));

        UnitValue uw = getUnitValue(pl.SValue("w"));
        UnitValue uh = getUnitValue(pl.SValue("h"));

		//int ori = getParentOrientation(v);
        int ori = pl.containsKey("hor") ? LinearLayout.HORIZONTAL :
        		  pl.containsKey("ver") ? LinearLayout.VERTICAL :
        		  V_NULL;
		
        // 设置宽高
    	LayoutParams lp = UpdateLayoutParams(v, ori == V_NULL ? LinearLayout.HORIZONTAL : ori, uw, uh);
        LTRB ltrb = getParamLTRB("margin", pl);
		
        // 设置边距
        if(ltrb != null) {
        	if(lp == null) lp = newLayoutParams();
        	setMargin(lp, ltrb);
        }
        int gravity = HyKeyValues.getGravity(pl.SValue("lay-grv"));
        if(gravity != 0){
        	if(lp == null) lp = newLayoutParams();
        	if(lp instanceof LinearLayout.LayoutParams)
        	{
        		if(ll != null)
        			ll.setGravity(gravity);
        		else
        			((LinearLayout.LayoutParams)lp).gravity = gravity;
        	}
        }
		
        if(ori != V_NULL && v instanceof LinearLayout)
        	((LinearLayout)v).setOrientation(ori);

        // 更新布局
        if(lp != null) v.setLayoutParams(lp);
        
        // 设置填充
        ltrb = getParamLTRB("padding", pl);
        if(ltrb != null) 
    	{
        	setPadding(v, ltrb);
    	}

        // 设置背景
        if(pl.containsKey("bg")) {
            Object bg = pl.get("bg");
            if(bg != null)
            {
            	if(bg instanceof String) {
            		bg = HyKeyValues.ParseDrawrable(bg);
            		if(bg == null) bg = HyKeyValues.ParseColorVal(bg);
            	}
            	if(bg instanceof Integer) v.setBackgroundResource((Integer)bg);
            	if(bg instanceof Drawable) v.setBackground((Drawable)bg);
            }
        }

        // 设置背景色
        if(pl.containsKey("bgc")) {
            Object bg = pl.get("bgc");
	        if(bg != null)
	        {
	        	bg = HyKeyValues.ParseColorVal(bg);
	        	if(bg instanceof Integer) v.setBackgroundColor((Integer)bg);
	        }
        }
        if(pl.BValue("disabled")) v.setEnabled(false);

        // 设置最小高宽
        if(pl.containsKey("min-w")) v.setMinimumWidth(getUnitIntValue(pl.get("min-w")));
        if(pl.containsKey("min-h")) v.setMinimumHeight(getUnitIntValue(pl.get("min-h")));


        // 设置内对齐方式
        gravity = HyKeyValues.getGravity(pl.SValue("grv"));
    	if(gravity != 0) {
            if(v instanceof LinearLayout)
            	((LinearLayout)v).setGravity(gravity);
            else if(v instanceof TextView)
            	((TextView)v).setGravity(gravity);
    	}
    	
        if(v instanceof TextView)
        {
            // 设置文本内容
        	if(pl.containsKey("text")) ((TextView)v).setText(pl.SValue("text"));
        	if(pl.containsKey("hint")) ((TextView)v).setHint(pl.SValue("hint"));

            // 设置字体颜色
            Object color = HyKeyValues.ParseColorVal(pl.get("color"));
        	if(color != null && color instanceof Integer)
        		((TextView)v).setTextColor((Integer)color);
        	
          //  if(pl.BValue("r")) ((TextView)v).setKeyListener(null);

            // 设置字体大小
        	if(pl.containsKey("fs")) {
	            int fs = getPx(pl.SValue("fs"));
	        	if(fs > 0)
	        		((TextView)v).setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)fs);
        	}

            // 设置输入类型
            if(v instanceof EditText) {
            	EditText et = (EditText)v;
      	    	setEditTextInputType(et, pl);
            }
        }
	} 
    
    private void SetDrawableBounds(TextView v, Drawable drawable){
    	if(drawable == null) return;
        int fh = UIUtils.getFontHeight(v.getTextSize());
        drawable.setBounds(0, 0, fh, fh);
    }
	
	private void setEditTextInputType(final EditText et, ParamList pl){
		String svt = pl.SValue("vt");
		int vt = DType.valueOf(svt);

		if(pl.SValue("act").equals("search"))
			et.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
			
		String accepted = null;
		if(svt.equals("nt")) accepted = UIUtils.CHRS_ANY;
		if(vt == DType.Date) accepted = UIUtils.CHRS_DATE;
		if(vt == DType.Time) accepted = UIUtils.CHRS_TIME;
		if(vt == DType.DateTime) accepted = UIUtils.CHRS_DATETIME;
		if(vt == DType.Dec) accepted = UIUtils.CHRS_DEC;
		if(vt == DType.Money) accepted = UIUtils.CHRS_DEC;
		int inputType = UIUtils.valueOfInputTypeName(svt);
		if(pl.containsKey("pwd")) inputType |= InputType.TYPE_TEXT_VARIATION_PASSWORD;
		et.setInputType(inputType);
		
		if(vt == DType.Date) {
			Drawable drawable = et.getResources().getDrawable(R.drawable.i_date);  
			SetDrawableBounds(et, drawable);

	        et.setCompoundDrawables(null, null, drawable, null);
	        et.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
			        if (event.getAction() == MotionEvent.ACTION_UP) {  
			                boolean ok = event.getX() > (v.getWidth() - ((TextView)v).getTotalPaddingRight())  
			                        && (event.getX() < ((v.getWidth() - v.getPaddingRight())));  
			  
			                if (ok)
			                	try {
				                	DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {  
				                        
				                        @Override  
				                        public void onDateSet(DatePicker arg0, int year, int month, int day) {
				                        	et.setText(gv.SDate(year, month + 1, day));
				                        }  
				                    };
				                    gv.DatePart dp = gv.GetDatePart(gv.StrToDate(et.getText().toString()));
				                	DatePickerDialog dialog = new DatePickerDialog(HyApp.CurrentActivity(), 
														                			DatePickerDialog.THEME_HOLO_LIGHT,
														                			listener, 
														                			dp.year, dp.month, dp.day);
				                	dialog.show();  
								} catch (Exception e) {
									ExProc.Show(e);
								}
			        }
			        return false;
				}
			});
		}
			
		if(accepted != null) et.setKeyListener(DigitsKeyListener.getInstance(accepted));
	}
	
	public void setViewParam(final View v, Object... params) {
		try {
			setViewParam(v, new ParamList(params));
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
	public static class LTRB {
		public int Left;
		public int Top;
		public int Right;
		public int Bottom;
		
		public LTRB() {
			set(V_NULL, V_NULL, V_NULL, V_NULL);
		}
		
		public LTRB(int size) {
			set(size);
		}
		
		public LTRB(int l, int r) {
			setLeftRight(l, r);
		}
		
		public LTRB(int l, int t, int r, int b) {
			set(l, t, r, b);
		}
		
		public void set(int l, int t, int r, int b) {
			Left = l; Top = t; Right = r; Bottom = b;
		}
		
		public void set(int size) {
			set(size, size, size, size);
		}
		
		public void setLeftRight(int l, int r) {
			set(l, V_NULL, r, V_NULL);
		}

		public LTRB(int[] values) {
			this(values[0]);
        	if(values.length == 1) set(values[0]);
        	if(values.length == 2) setLeftRight(values[0], values[1]);
        	if(values.length == 4) set(values[0], values[1], values[2], values[3]);
		}
		
		@Override
		public String toString() {
			return String.format("(%d, %d, %d, %d)", Left, Top, Right, Bottom);
		}
	}
	
	public LTRB getParamLTRBPart(LTRB ltrb, String name, String part, int pos, ParamList pl){
		String partName = name + part;

        if(!pl.containsKey(partName)) return ltrb;
        if(ltrb == null) ltrb = new LTRB();
        if(part.isEmpty())
        {
        	int[] values = getPxValues(pl.SValue(name));
            if(values != null && values.length > 0) {
            	if(values.length == 1) ltrb.set(values[0]);
            	if(values.length == 2) ltrb.setLeftRight(values[0], values[1]);
            	if(values.length == 4) ltrb.set(values[0], values[1], values[2], values[3]);
            }
        }
        else {
        	int value = getUnitIntValue(pl.SValue(partName));
        	if(pos == 0) ltrb.Left = value; else
            	if(pos == 1) ltrb.Top = value; else
                	if(pos == 2) ltrb.Right = value; else
                    	if(pos == 3) ltrb.Bottom = value;
		}
        return ltrb;
	}
	
	public LTRB getParamLTRB(String name, ParamList pl){
		LTRB ltrb = null;
		ltrb = getParamLTRBPart(ltrb, name, "", 4, pl);
		ltrb = getParamLTRBPart(ltrb, name, "Left", 0, pl);
		ltrb = getParamLTRBPart(ltrb, name, "Top", 1, pl);
		ltrb = getParamLTRBPart(ltrb, name, "Right", 2, pl);
		ltrb = getParamLTRBPart(ltrb, name, "Bottom", 3, pl);
		return ltrb;
	}

	public void setMargin(LayoutParams lp, LTRB ltrb){
		if(!(lp instanceof ViewGroup.MarginLayoutParams)) return;
    	ViewGroup.MarginLayoutParams mp = ((ViewGroup.MarginLayoutParams)lp);
		if(ltrb.Left == V_NULL) ltrb.Left = mp.getMarginStart();
		if(ltrb.Top == V_NULL) ltrb.Top = mp.topMargin;
		if(ltrb.Right == V_NULL) ltrb.Right = mp.getMarginEnd();
		if(ltrb.Bottom == V_NULL) ltrb.Bottom = mp.bottomMargin;
		mp.setMargins(ltrb.Left, ltrb.Top, ltrb.Right, ltrb.Bottom);
		mp.setMarginStart(ltrb.Left);
		mp.setMarginEnd(ltrb.Right);
	}
	
	public void setPadding(View v, LTRB ltrb){
		if(ltrb.Left == V_NULL) ltrb.Left = v.getPaddingLeft();     
		if(ltrb.Top == V_NULL) ltrb.Top = v.getPaddingTop();      
		if(ltrb.Right == V_NULL) ltrb.Right = v.getPaddingRight();    
		if(ltrb.Bottom == V_NULL) ltrb.Bottom = v.getPaddingBottom();   
		v.setPadding(ltrb.Left, ltrb.Top, ltrb.Right, ltrb.Bottom);
	}

	public void setViewMargin(View v, LTRB ltrb){
		LayoutParams lp = v.getLayoutParams();
    	if(lp == null) lp = newLayoutParams();
    	setMargin(lp, ltrb);
    	v.setLayoutParams(lp);
	}
	
	public Object getParam(ParamList pl, String name){
		if(pl.containsKey(name)) return pl.get(name);
		if(mStyles == null) return null;
		if(pl.containsKey("style")) return getParam(mStyles.GetParamList(pl.SValue("style")), name);
		return null;
	}

	public Object getParam(ParamList pl, String name, String defaultValue){
		Object v = getParam(pl, name);
		return v == null ? defaultValue : v;
	}
	
	public boolean containParam(ParamList pl, String name){
		if(pl.containsKey(name)) return true;
		if(mStyles == null) return false;
		if(pl.containsKey("style")) return containParam(mStyles.GetParamList(pl.SValue("style")), name);
		return false;
	}
	
	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			gc.ExecMethod(context, context.getClass(), "onClick", new Class<?>[] { View.class }, v);
		}
	};
	
	public View CreateFieldView(String viewTypeName, final ParamList plView, final ParamList plPanel, int ori) {

    	int fvType = valueOfFVTypeName(viewTypeName);
    	if(fvType == 0)  fvType = FVT_EDITTEXT;
		
        plView.SetValue("w", getParam(plView, "w", "match"));
        plView.SetValue("h", getParam(plView, "h", "wrap"));
        
		View v = null;
		
        if(fvType == FVT_SLINE)
        {
        	if(ori == LinearLayout.HORIZONTAL) {
//            	v = new View(context);
//            	pl.WeakSetParams("w:1dp, h:1, bgc: gray");
//            	setViewParam(ll, pl);
//            	ll.addView(v);
//            	return v;
        	}
        	else{
        		v = new View(context);
            	plView.WeakSetParams("w:match, h:1dp, bgc: holo");
            	setViewParam(v, plView);
            	return v;
        	}
        }
        
    	// 获取ID值
    	Object oid = plView.get("id");
    	int id = -1;
    	if(oid instanceof String) id = ID.valueOf((String)oid);
    	if(id < 0) id = gv.IntVal(oid);
    	
    	EditField ef = mEFields == null ? null : mEFields.findItem(id);
    	
        int margin = gu.dp2px(context, 5);
        boolean isNeed = plView.BValue("need");
        float fontSize = 16;//gu.sp2px(context, 8);
        
        if(ef != null) {
        	isNeed = gv.ContainEnumVal(ef.validOptions, ValidType.Need);
        	if(!ef.getValTypeName().isEmpty()) plView.SetValue("vt", ef.getValTypeName());
        	plView.SetValue("title", ef.Disp);
        }

        UnitValue lw = gu.caleCommonUnitValue(context, plView.getValue("lw", "70dp"));
        
        PM pm = new PM();
        pm.isNeed = isNeed;
        pm.margin = 0;
        pm.fontSize = fontSize;
        pm.lw = lw;
       // pm.ll = ll;
    		
        LinearLayout.LayoutParams lp;
        
        /********************************输入框**************************************/
        lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        
        final EventHandleListener changedListener = new EventHandleListener() {
			
			@Override
			public void Handle(Object sender, ParamList arg) throws Exception {
				plPanel.Call("ValueChanged", sender, null);
			}
		};

        if(fvType == FVT_VIEW)
        	v = new View(context);
		
        if(fvType == FVT_TEXTVIEW)
        	v = createTitleTextView(plView.SValue("text"), pm);
        
        LinearLayout container = null; // 复合组件容器

        String title = plView.SValue("title");
        if(!title.isEmpty()) {
        	ParamList plContainer = new ParamList("hor");
        	plContainer.SetValue("w", getParam(plView, "w"));
        	plContainer.SetValue("h", getParam(plView, "h"));
    		container = createLayout(plContainer);
    		//setViewParam(v, "text:test, w:1w,h:wrap");
    		setViewParam(container, plContainer);
    		container.addView(createTitleTextView(title, pm));
        }
        
        if(fvType == FVT_EDITTEXT)
        {
        	EditText etT = null;
        	if(plView.containsKey("auto-comp")) {
        		final AutoCompleteTextView atv = new AutoCompleteTextView(context);
    	        ParamList plApt = new ParamList(plView.$("auto-comp"));
    	        
    	        try {
            		onGetAdapter.Handle(etT, plApt);
				} catch (Exception e) {
					ExProc.Show(e);
				}
        		HyListAdapter apt = plApt.$("apt");
        		atv.setThreshold(1);
        		atv.setAdapter(apt);
        		atv.setOnItemClickListener(new  OnItemClickListener() {

        	        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        	        	atv.selectAll();
        	        }
				});
        		etT = atv;
        	}
        	else
        		etT = new EditText(context);
			
	        final EditText et = etT;
	        et.setLayoutParams(lp);
	        et.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

  			et.setSelectAllOnFocus(true);

			//android:windowSoftInputMode="stateVisible|adjustPan"

  			if(container != null) container.addView(et);
  			
//  			et.setSingleLine(true);
  			// 输入框包含搜索功能
  			if(plView.containsKey("srch")) {
  				ImageButton ib = new ImageButton(context);
  		        lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
  		        lp.gravity = Gravity.CENTER_VERTICAL|Gravity.END;
  		        lp.setMarginStart(margin);
  		        ib.setLayoutParams(lp);
  		        
  				ib.setBackgroundResource(R.drawable.search2);
  				ib.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						try {
							plView.Call("searchListener", et, plView);
							if(plView.containsKey("result")) 
								et.setText(plView.SValue("result"));
						} catch (Exception e) {
							ExProc.Show(e);
						}
					}
				});
  	  			if(container != null) container.addView(ib);
  			}

	        v = et;
            TextWatcherEx watcherEx = new TextWatcherEx(et);
            watcherEx.changedListener = changedListener;
            et.addTextChangedListener(watcherEx);
        }

        if(fvType == FVT_BUTTON || fvType == FVT_IMG_BUTTON){
        	v = fvType == FVT_IMG_BUTTON ? new ImageButton(context) : new Button(context);
        	v.setLayoutParams(lp);
        	v.setOnClickListener(mOnClickListener);
        }

        if(fvType == FVT_CHECKBOX){
        	CheckBox chk = new CheckBox(context);
        	chk.setLayoutParams(lp);
        	v = chk;
        }
        
        if(fvType == FVT_DATEPICKER){
        	DatePicker dp = new DatePicker(context);
	        dp.setLayoutParams(lp);
        	v = dp;
        }
        
        if(fvType == FVT_LISTVIEW){
        	final HyListView lv = UCCreator.CreateListView(context, plView);
	        lv.setLayoutParams(lp);
        	v = lv;
        }
        
        if(fvType == FVT_RADIOGUP)
        {
        	RadioGroup rg = new RadioGroup(context);
        	rg.setOrientation(LinearLayout.HORIZONTAL);
        	Object[] items = plView.$("items");
        	
        	for (Object item : items) {
            	RadioButton rb = new RadioButton(context);
            	rb.setText((String)item);
    	        rb.setWidth(gu.dp2px(context, 64));
            	rb.setTextColor(Color.BLACK);
    	        rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            	rg.addView(rb);
			};
			rg.check(rg.getChildAt(0).getId());
			rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					try {
						changedListener.Handle(group, new ParamList("checkedId", checkedId));
					} catch (Exception e) {
						ExProc.Show(e);
					}
				}
			});
	  		if(container != null) container.addView(rg);
	        v = rg;
        }
        
        if(plView.containsKey("hint") && v instanceof TextView)
        	((TextView)v).setHint((String)plView.get("hint"));

		
        if(v != null)
        {
        	if(container != null) plView.SetValue("w", "1w");
        	
        	// 设置View参数
			setViewParam(v, plView);
			
  			v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
  			    @Override  
  			    public void onFocusChange(View v, boolean hasFocus) {
  			    	try {
  			    		if(v instanceof EditText) ((EditText)v).selectAll();
  	  					plPanel.Call("onFocusChange", v, new ParamList("hasFocus", hasFocus));
					} catch (Exception e) {
						ExProc.Show(e);
					}
  			    }
  			});
  			
        	if(id > 0) v.setId(id);
        }
        View result =  container == null ? v : container;
        return result;
     //   TestV(v);
	}
	
	@SuppressWarnings("unused")
	private void TestV(View v){
		if(v == null) return;
    	if(v instanceof CheckBox){
    		CheckBox chk = (CheckBox)v;
    		LinearLayout.LayoutParams lp1  = (LinearLayout.LayoutParams)chk.getLayoutParams();
    		chk.setWidth(300);
    		lp1.gravity= Gravity.END;
    		chk.setLayoutParams(lp1);
    	}
	}
	
	public void LoadStyles(ParamList pl) {
		mStyles = new ParamList(pl.get("styles"));
	}
//        		CreateFieldView(llHor, item, pl, orient);
//        	int last = llHor.getChildCount() - 1;
//        	if(last >= 0) 
//        		setViewMargin(llHor.getChildAt(last), new LTRB(V_NULL, 0));
//        	llPanel.addView(llHor);
//        }
//
//        return llPanel;
//	}

	public ViewGroup CreatePanel(ParamList plPanel) {
        LinearLayout llPanel = new LinearLayout(context);
        int padding = gu.dp2px(context, 15);

        llPanel.setPadding(padding, padding, padding, padding);
        mEFields = plPanel.Get("EFields", EditFieldList.class);

        LoadStyles(plPanel);

        if(!containParam(plPanel, "hor")) plPanel.SetValue("ver");
        setViewParam(llPanel, plPanel);

        int orient = llPanel.getOrientation();
        int anti_orient = orient == LinearLayout.HORIZONTAL ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL;
        
        Object[] items = plPanel.$("items");
        for (Object item : items)
        {
        	if(item instanceof Object[]) {
        		LinearLayout llHor = createLayout("hor, w:match, h:wrap");
        		Object[] horItems = (Object[])item;
                for (Object itemHor : horItems) {
                	Param pm = (Param)itemHor;
                	ParamList plView = type.as(pm.Value, ParamList.class);
                	if(anti_orient == LinearLayout.HORIZONTAL && itemHor != horItems[horItems.length - 1]) 
                		plView.SetParams("marginRight: 10dp");
                	llHor.addView(CreateFieldView(pm.Name, plView, plPanel, anti_orient));
                }
            	llPanel.addView(llHor);
        	}
        	else if(item instanceof Param) {
            	Param pm = (Param)item;
            	ParamList plView = type.as(pm.Value, ParamList.class);
				View v = CreateFieldView(pm.Name, plView, plPanel, orient);
        		if(v != null) llPanel.addView(v);
        	}
        }

        return llPanel;
	}
	
	public ViewGroup CreateScrollPanel(ParamList pl) throws Exception {
		ScrollView sv = new ScrollView(context);
		View panel = CreatePanel(pl);
		sv.addView(panel);
        return sv;
	}
	
	/************************ 静态方法调用 ***************************/
	public static void CreatePopupMenu(Context context, View v, String[] items) {
        UICreator creator = new UICreator(context);
        creator.CreatePopupMenu(v, items);
	}

	public static ViewPager CreateViewPager(Activity act, ViewGroup parent, View[] pageViews, final ParamList pl)  {
        UICreator creator = new UICreator(act);
        if(pageViews == null) pageViews = pl.$("items");
        return creator.CreateViewPager(parent, pageViews, pl);
	}
	
	public static PopupWindowEx CreatePopupMenu(Context context, final ParamList pl) throws Exception {
        UICreator creator = new UICreator(context);
        return creator.CreatePopupMenu(pl);
	}

	public static ViewGroup CreatePanel(Context context, ParamList pl) {
        UICreator creator = new UICreator(context);
        return creator.CreatePanel(pl);
	}

	public static ViewGroup CreateScrollPanel(Context context, ParamList pl) throws Exception {
        UICreator creator = new UICreator(context);
        return creator.CreateScrollPanel(pl);
	}

	public static ViewGroup CreateToolBar(Context context, String config, ParamList pl) throws Exception{ 
        UICreator creator = new UICreator(context);
        return creator.CreateToolBar(config, pl);
	}
	
	public static void SetViewParam(final View v, Object... params) {
		UICreator creator = new UICreator(v.getContext());
        creator.setViewParam(v, params);
	}
	
	public static LinearLayout CreateLayout(Context context, String config) {
		UICreator creator = new UICreator(context);
        return creator.createLayout(config);
	}
}
