package hylib.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.hc.ID;
import com.hc.R;

import java.util.ArrayList;
import java.util.List;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.toolkits.ExProc;
import hylib.toolkits.HyColor;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits.gi;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.ui.dialog.ListItemViewInfo;
import hylib.ui.dialog.UICreator;
import hylib.util.ParamList;

public class HyListAdapter extends BaseAdapter implements Filterable {
	private LayoutInflater inflater;
	private List<?> listItems;
	private int mResItemID;
	private int mItemIndex;
	private ParamList mParams;
	public OnGetViewListener getViewListener;
	public gi.NotifyListener onNotify;
	public Context context;
    private HyListFilter mFilter;
	
	public interface OnGetViewListener {
		View onGet(int position, View convertView, ViewGroup parent);
	}
	
	public HyListAdapter(Context context, List<?> listItems) {
		this.context = context;
		inflater = LayoutInflater.from(context); // 创建视图容器并设置上下文
		if(listItems == null) listItems = new ArrayList<Object>();
		this.listItems = listItems;
		mItemIndex = -1;
		mParams = new ParamList(); 
	}

	public void setParams(Object pms) {
		mParams = new ParamList(pms); 
	}

	public void setParam(String name, Object value) {
		mParams.SetValue(name, value, false); 
	}
	
	public String getDispMember() {
		return mParams.SValue("dm");
	}
	
	public String getValueMember() {
		String vm = mParams.SValue("vm");
		return vm.isEmpty() ? getDispMember() : vm;
	}
	
	public void setListItems(List<?> items){
		listItems = items;
		notifyDataSetChanged();
	}
	
	public List<?> getListItems(){
		return listItems;
	}

	public HyListAdapter(Context context, List<?> listItems, int r_item_id) {
		this(context, listItems);
		mResItemID = r_item_id;
	}

	public int getCount() {
		return listItems.size();
	}

	public Object getItem(int location) {
		return location < 0 ? null : listItems.get(location);
	}


	public void removeItem(int location) {
		listItems.remove(location);
	}
	
	public long getItemId(int location) {
		return 0;
	}
	
	public  void setItemIndex(int value) {
		mItemIndex = value;
	}
	
	public int getItemIndex() {
		return mItemIndex;
	}

    public Filter getFilter() {
        if (mFilter == null) mFilter = new HyListFilter();
        return mFilter;
    }
    
	public void Select(Object item) {
		SelectAt(item == null ? -1 : listItems.indexOf(item));
	}

	public Object SelectAt(int index) {
		//if(mItemIndex == index) return;
		mItemIndex = index;
		notifyDataSetChanged();
		if(onNotify != null) onNotify.Notify(this, mItemIndex);
		return getCurrentItem();
	}
	
	public Object getCurrentItem() {
		return getItem(mItemIndex);
	}

	public Object getItem(View v) {
		ListItemViewInfo listItemView = (ListItemViewInfo) v.getTag();
		return getItem(listItemView.index);
	}
	
	public ListItemViewInfo NewItemView(View view) {
		return new ListItemViewInfo(view);
	}
	
	public void ShowView(ListItemViewInfo vInfo, View convertView, ViewGroup parent) {
		if(mResItemID > 0) {
			TextView tv = type.as(convertView.findViewById(R.id.item_info), TextView.class);
			if(tv != null) tv.setText(gv.StrVal(getItem(vInfo.index)));  
			return;
		}

		Object item = getItem(vInfo.index);
		if(item instanceof DataRow){
			DataRow dr = type.as(item, DataRow.class);
			TextView tvInfo = type.as(convertView.findViewById(ID.Info), TextView.class);

			SpannableStringHelper sh = new SpannableStringHelper();
			if(mParams.containsKey("dm"))
			{
				String[] dispMembers = gs.Split(mParams.SValue("dm"));
				if(mParams.BValue("summary")) {

					sh.appendObj(dr.getStrVal(dispMembers[0]));
					String s = "";
					for (String name : dispMembers)
						if(name != dispMembers[0]) {
							String v = dr.getStrVal(name);
							if(!v.isEmpty()) s += v + " ";
						}
					if(!s.isEmpty()) sh.appendText("\n" + s, Color.GRAY, 0.8f);

					tvInfo.setText(sh);
				}
				else
					for (String name : dispMembers)
						sh.appendObj(dr.getStrVal(name) + " ");
			}
			else {
				sh.appendObj(dr.getStrVal(0));
			}
			tvInfo.setText(sh);
		}

		
	}
	
	protected void DrawBG(ListItemViewInfo vInfo, View convertView, int clBG){
		if(getItemIndex() == vInfo.index) clBG = HyColor.Blend(clBG, HyColor.LightBlue, 0.8f);
		if(clBG != 0) convertView.setBackgroundColor(clBG);
	}
	
	/**
	 * 获取ListView的视图
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if(getViewListener != null) 
			return getViewListener.onGet(position, convertView, parent);
		
		ListItemViewInfo listItemInfo;
		if (convertView == null) {
			convertView = mResItemID > 0 ? inflater.inflate(mResItemID, null) :
						  CreateDefaultConvertView();
			listItemInfo = NewItemView(convertView);
			convertView.setTag(listItemInfo);
		} else {
			listItemInfo = (ListItemViewInfo) convertView.getTag();
		}
		
		listItemInfo.index = position;
		ShowView(listItemInfo, convertView, parent);
		
		return convertView;
	}
	
	private View CreateDefaultConvertView(){
		ParamList config = new ParamList("items: [" + 
        	"tv: { id:Info, fs: 16dp, text: info,w:match,bgc1:r,color:black, padding: 8dp,h:wrap }, "+ 
			"], grv: c, padding: 0dp, margin: 0dp, w:match, h:wrap, bg1:#33CCAAFF, lp-type: listview");
		return UICreator.CreatePanel(context, config);
	}
	
	public static HyListAdapter Create(final Context context, final HyListView lv, List<?> listItems, final ParamList pl) {

		final HyListAdapter listAdapter = new HyListAdapter(context, listItems);
		listAdapter.mParams = pl;
		listAdapter.getViewListener = new OnGetViewListener() {

			@Override
			public View onGet(int position, View convertView, ViewGroup parent) {
				try {
					if (convertView == null) {
						if(pl.containsKey("getItemView"))
						{
							ParamList arg = new ParamList();
							pl.Call("getItemView", lv, arg);
							convertView = arg.Get("view", View.class);
						}
						else {
							ParamList config = pl.GetParamList("itemViewConfig");
							config.SetValue("lp-type", "listview");
							convertView = config.isEmpty() ? listAdapter.CreateDefaultConvertView() :
										  UICreator.CreatePanel(context, config);
						}
					}

					if(convertView == null) return null;

					Object item = listAdapter.getItem(position);
					String cfg = "";
					if(pl.containsKey("min-ih")) cfg += ",min-h:" + pl.get("min-ih");
					UICreator.SetViewParam(convertView, cfg);
					if(pl.containsKey("setViewData"))
					{
						ParamList arg = new ParamList("view", convertView);
						arg.SetValue("item", item);
						pl.Call("setViewData", lv, arg);
					}
					else {		
						if(item instanceof DataRow){
							listAdapter.ShowView(new ListItemViewInfo(null, position), convertView, parent);
						}
					}
				} catch (Exception e) {
					ExProc.Show(e);
				}

				return convertView;
			}
		};
        lv.setAdapter(listAdapter);
        return listAdapter;
	}

	// 列表过滤器
	private List<?> mOriginalValues;
    private final Object mLock = new Object();
    private class HyListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null)
                synchronized (mLock) {
                    mOriginalValues = listItems;
                }
            
            if (prefix == null || prefix.length() == 0) {
                results.values = mOriginalValues;
                results.count = mOriginalValues.size();
            } else {
                DataRowCollection values;
                synchronized (mLock) {
                	if(mOriginalValues instanceof DataRowCollection)
                		values = (DataRowCollection)mOriginalValues;
                	else
                		values = new DataRowCollection("", mOriginalValues);
                }

                String dm = mParams.SValue("dm");
                DataRowCollection newValues = dm.isEmpty() ? values.MatchText(prefix.toString(), 5) :
                							  values.MatchText(dm, prefix.toString(), 5);

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
        	if(!(resultValue instanceof DataRow)) return gv.StrVal(resultValue);
        	DataRow dr = (DataRow)resultValue;
            return dr.getStrVal(getValueMember());
        }
        
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        	listItems = (List<?>) results.values;
            if (results.count > 0)
                notifyDataSetChanged();
            else
                notifyDataSetInvalidated();
        }
    }
}

