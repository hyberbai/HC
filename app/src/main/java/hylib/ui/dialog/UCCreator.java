package hylib.ui.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.view.View;
import android.widget.AdapterView;

import java.util.List;

import hylib.toolkits.ExProc;
import hylib.util.ParamList;
import hylib.widget.HyEvent;
import hylib.widget.HyEvent.LvItemClickEventParams;
import hylib.widget.HyListAdapter;
import hylib.widget.HyListView;

public class UCCreator {
	public static Drawable dividerDrawable;
	public static int dividerHeight = 1;
	
	public static void Init(ParamList pl) {
		int dividerColor = pl.IntValue("dividerColor");
		dividerDrawable = new ColorDrawable(dividerColor);
		//dividerDrawable = type.as(pl.get("dividerDrawable"), Drawable.class);
		dividerHeight = pl.IntValue("dividerHeight");
	}

	public static HyListView CreateListView(Context context, boolean showDivider) {
		HyListView lv = new HyListView(context);
		if(showDivider)
		{
			lv.setDivider(dividerDrawable);
			lv.setDividerHeight(dividerHeight);
		}
		else {
			lv.setDivider(null);
		}
		return lv;
	}

	public static HyListView CreateListView(final Context context, final ParamList pl) {
		if(pl == null) return CreateListView(context, true);
		boolean showDivider = pl.getValue("showDivider", true);
		final HyListView lv = CreateListView(context, showDivider);
		setListViewParam(context, lv, pl);
		return lv;
	}
	
	public static void setListViewParam(final Context context, final HyListView lv, final ParamList pl) {
		lv.disableMeasure = pl.BValue("no-measure");
        List<?> listItems = pl.Get("items", List.class);
        if(listItems != null) 
        	HyListAdapter.Create(context, lv, listItems, pl); 
        if(pl.containsKey("id")) lv.setId(pl.IntValue("id"));
		
		if(pl.containsKey("onItemClick") || pl.containsKey("onEvent"))
			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				
		        public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
		        	LvItemClickEventParams arg = new HyEvent.LvItemClickEventParams();
		        	try {
		        		arg.setPosition(position);
			        	pl.Call("onItemClick", lv, arg);
			        	pl.Call("onEvent", lv, arg);
					} catch (Exception e) {
						ExProc.Show(e);
					}
		        }  
		          
		    });  

		if(pl.containsKey("onItemLongClick") || pl.containsKey("onEvent"))
			lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
					HyEvent.LvItemLongClickEventParams arg = new HyEvent.LvItemLongClickEventParams();
		        	try {
		        		arg.setPosition(position);
			        	if(pl.containsKey("onItemLongClick"))
			        		pl.Call("onItemLongClick", lv, arg);
			        	else if(pl.containsKey("onEvent"))
			        		pl.Call("onEvent", lv, arg);
			        	return arg.getHandled();
					} catch (Exception e) {
						ExProc.Show(e);
						return false;
					}
		        }
			});
	}
	
	public static Shape CreateShape(int radius) {
		int outRadius = radius;
		int innerRadius = radius-1;
		/*圆角矩形*/
		float[] innerRadii = {innerRadius, innerRadius, 0, 0, 0, 0, 0, 0};//内矩形 圆角半径
		float[] outerRadii = {outRadius, outRadius, outRadius, outRadius, outRadius, outRadius, outRadius, outRadius};//左上x2,右上x2,右下x2,左下x2，注意顺序（顺时针依次设置）
		int spaceBetOutAndInner = outRadius - innerRadius;//内外矩形间距
		//RectF inset = new RectF(spaceBetOutAndInner, spaceBetOutAndInner, spaceBetOutAndInner, spaceBetOutAndInner);
		RoundRectShape shape = new RoundRectShape(outerRadii, null, innerRadii);
		
		return shape;
	}
	
	public static GradientDrawable getGradientDrawable(int color, int strokecolor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setShape(GradientDrawable.RECTANGLE);  
        drawable.setStroke(1, strokecolor);
        return drawable;
	}
	
	public static ShapeDrawable getShapeDrawable(int color, int radius) {
		ShapeDrawable drawable = new ShapeDrawable(CreateShape(radius));
        drawable.getPaint().setColor(color);
        drawable.getPaint().setAntiAlias(true);
        return drawable;
	}
}


//plLv.SetValue("getItemView", new EventHandleListener() {
//	
//	@Override
//	public void Handle(Object sender, ParamList arg) throws Exception {
//		HyListView hv = (HyListView)sender;
//		Context context = hv.getContext();
//		
//        String config = "items: [" + 
//        	"tv: { id:Info, fs: 16dp, text: info,color:black, padding: 8dp, w:match,h:wrap }, "+ 
//        "], padding: 0dp, margin: 0dp, w:match, h:wrap, bg1:#33CCAAFF";
//        	
//        ViewGroup panel = UICreator.CreatePanel(context, new ParamList(config));
//
//		arg.SetValue("view", panel);
//	}
//});
//
//plLv.SetValue("setViewData", new EventHandleListener() {
//	
//	@Override
//	public void Handle(Object sender, ParamList arg) throws Exception {
//		View convertView = arg.Get("view", View.class);
//		DataRow dr = arg.Get("item", DataRow.class);
//		TextView tvInfo = type.as(convertView.findViewById(ID.Info), TextView.class);
//		String info = dr.getStrVal("SName");
//		tvInfo.setText(info);
//	}
//});
