package com.hc.tools;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.toolkits.ExProc;
import hylib.toolkits.gc;
import hylib.toolkits.gs;
import hylib.toolkits.gu;
import hylib.toolkits.type;
import hylib.ui.dialog.UIUtils;
import hylib.view.ActivityEx;
import hylib.widget.HyEditText;
import hylib.widget.HyListAdapter;
import hylib.widget.HyListView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hc.R;

public class ActSearch extends ActivityEx {
	protected int TID;
	public DataRowCollection DataSource;
	private DataRowCollection mRows;
	public String ColNames;
	private HyEditText etInput;
	public HyListAdapter listAdapter; 
	private HyListView lv;
	
	protected String DispMembers;	// 列表显示的成员
	protected String mText;
	
	public int MaxListNum = 100;	// 列表显示搜索结果数量
	public int LeastInputChars;		// 最少输入字符数，之后开始搜索
	public static SparseArray<DataRowCollection> dsMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_search);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		Intent intent = getIntent();
		dsMap = new SparseArray<DataRowCollection>();
		
		etInput = $(R.id.et_input);
		etInput.setBackgroundResource(R.drawable.search_bg2);
		etInput.setTextColor(Color.WHITE);
		etInput.setHintTextColor(0x88ffffff);
		etInput.setCompoundDrawables(R.drawable.search, 0, 0, 0);
		etInput.setClearDrawable(R.drawable.input_delete2);
		lv = $(R.id.lv_search);

		etInput.setInputType(InputType.TYPE_CLASS_TEXT);
		etInput.setHint(Params.SValue("hint"));
		etInput.setFocusable(true);
		etInput.setFocusableInTouchMode(true);
		mText = intent.getStringExtra("text");
		LeastInputChars = Params.getValue("LeastInputChars", LeastInputChars);
		MaxListNum = Params.getValue("MaxListNum", MaxListNum);
		
		etInput.addTextChangedListener(new TextWatcher() {
	        @Override  
	        public void beforeTextChanged(CharSequence s, int start, int cnt,  int after) {  }
	  
	        @Override  
	        public void onTextChanged(CharSequence s, int start, int before, int count) {  }
	        
	        @Override  
	        public void afterTextChanged(Editable s) {
	        	search(gc.GetEditText(etInput));
	        }
	    });;

		listAdapter = new HyListAdapter(this, null); 

		listAdapter.getViewListener = getViewListener;
		
		lv.setAdapter(listAdapter);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
	        	SelectItem(mRows.get(position));
	        }  
	          
	    });  
		

		try {
			LoadDataSource();
		} catch (Exception e) {
			ExProc.Show(e);
		}
		
		ShowSoftInput(true);
	}
	
	protected HyListAdapter.OnGetViewListener getViewListener  = new HyListAdapter.OnGetViewListener() {

		public View onGet(int position, View convertView, ViewGroup parent) {
			DataRow dr = type.as(listAdapter.getItem(position), DataRow.class);
			if (convertView == null) {
				//convertView = LayoutInflater.from(context).inflate(R.layout.list_sale_item, null);
				
				LinearLayout ll = new LinearLayout(context);
		        int padding = gu.dp2px(context, 10);
//					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,  LayoutParams.MATCH_PARENT);
//					lp.gravity = Gravity.CENTER_VERTICAL;
//				    ll.setLayoutParams(lp);
			  //  ll.setBackgroundColor(0xFFCCCCFF);
		        ll.setOrientation(LinearLayout.HORIZONTAL);
		        ll.setPadding(padding, padding, padding, padding);
		        
				TextView tv = new TextView(context);
				tv.setWidth(LayoutParams.WRAP_CONTENT);
				
				tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				tv.setWidth(1000);
				tv.setTextColor(Color.BLACK);
				tv.setGravity(Gravity.CENTER_VERTICAL);
				tv.setId(100);
				ll.addView(tv);
				convertView = ll;
			}

			String value = "";
			if(dr.ItemArray.length > 1)
			{
				value = gs.JoinArray(dr.ItemArray, " ", true, 1);
			}
//				View v =convertView.findViewById(R.id.item_info);
//				UIUtils.setViewValue(v, value);
			View v = convertView.findViewById(100);
			UIUtils.setViewValue(v, value);
			v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			return convertView;
		}
	};

	public void setTID(int tid){
		
	}

    @Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.ib_search) {
			search(UIUtils.getViewText(etInput));
			etInput.selectAll();
		}
		super.onClick(v);
	}

	
	public void LoadDataSource() {
		Intent intent = getIntent();
		setTID(intent.getIntExtra("tid", 0));
		
		if(DataSource == null) return;
		//if(!mText.isEmpty()) 
		{
			$Set(R.id.et_input, mText);
			search(mText);
			etInput.selectAll();
		}
	}

	public static DataRow drSelected;
	public void SelectItem(DataRow dr){
		drSelected = dr;
		Intent intent = new Intent();
		intent.putExtra("tid", TID);
        setResult(RESULT_OK, intent);  
		finish();
	}

	protected void search(String text){
		if(DataSource == null) return;
		mRows = text.length() < LeastInputChars ? new DataRowCollection() :
				DataSource.MatchText(DispMembers, text, MaxListNum);
		listAdapter.setListItems(mRows);
	}

}
