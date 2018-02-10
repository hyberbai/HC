package com.hc.mo.bill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import hylib.ui.dialog.UICreator;
import hylib.data.DataColumn;
import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.data.DataTable;
import hylib.toolkits.ExProc;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits.gs;
import hylib.toolkits.gu;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.ui.dialog.UIUtils;
import hylib.util.Hson;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.widget.HyListAdapter;
import android.R.integer;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hc.ActBase;
import com.hc.ID;
import com.hc.R;
import com.hc.pu;
import com.hc.dal.Bill;
import com.hc.tools.ActInputProduct;
import com.hc.tools.ActSearchItem;
import com.hc.tools.Search;

public class ActProductReplace extends ActBase {
	private TextView tvProductInfo;
	private DataTable dtReplace;
	private ParamList mValues;

	private float orginPrice;
	private int CustID;
	private int mID;
	private ListView lvReplace;
	public HyListAdapter listAdapter; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
    	mID = Params.IntValue("ID");
    	CustID = Params.IntValue("CustID");
    	
		setContentView(R.layout.activity_product_replace);

		CreateOptionsPopupMenu();
		
		tvProductInfo = (TextView) this.findViewById(R.id.tv_product_info);
		lvReplace = (ListView) this.findViewById(R.id.lv_replace);
		tvProductInfo.setHint("");
		
        
		lvReplace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
		    	EditReplace(position);
			}
		});

		LoadValues();
	}

	public void EditOrginProduct() {
		try {
			startActivity(ActInputProduct.class, REQ_CODE_EDIT,
					new Param("InputType", ActInputProduct.TYPE_SALE),
					new Param("title", "编辑"),
					new Param("values", mValues.toArray()),
					new Param("disables", "Price"),
					new Param("CustID", CustID)
			); 
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
    @Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.tv_product_info) EditOrginProduct();
		if(id == R.id.ib_add_replace) EditReplace(-1);
		if(id == R.id.ib_ok) ActOK();
		super.onClick(v);
	}
	
	@Override
	protected void setOptionsMenuParams(ParamList pl) throws Exception {
		final DataRowCollection rows = new DataRowCollection("name|text|pl", new Object[][] {
		});
		pl.set("Items", rows);
		pl.set("width", "100dp");
	}
	
	private void EditReplace(int position) {
		Param[] values = null;
		if(position >= 0)
		{
			DataRow dr = (DataRow)listAdapter.SelectAt(position);
			ParamList pl = dr.toParamList();
			pl.RenKeys("IID FItemID, IName FName");
			values = pl.toArray();
		}
		
		startActivity(ActInputProduct.class, REQ_CODE_REPLACE
				,new Param("ID", position)
				,new Param("values", values)
				,new Param("title", "替换产品")
				,new Param("CustID", CustID)
			//	,new Param("InputType", ActInputProduct.TYPE_ONLY_PRODUCT)
		); 
	}

	private void UpdateBaseInfo() {
    	String product_info = gs.JoinArray(new Object[]{
        	Param.SVal("NO", mValues.get("SNo"), "."),
    	//	Param.SVal("产品编码", plProduct.get("FNumber"), "："),
    		Param.SVal("", mValues.get("FName"), ""),
    		Param.SVal("规格型号", mValues.get("FModel"), "："),
    		Param.SVal("产品批号", mValues.get("BatchNo"), "："),
    		Param.SVal("生产日期", mValues.get("FKFDate"), "："),
    		Param.SVal("有效期至", mValues.get("FPeriodDate"), "："),
    		Param.SVal("备  注", mValues.get("Note"), "："),
    		Param.SVal("", mValues.get(""), "："),
    	}, "\n");

    	tvProductInfo.setText(product_info);
	}
	
	private void UpdatePrice() {
		float price = 0;
		for (DataRow dr : dtReplace.getRows()) {
			price += dr.getFVal("price");
		}
		if(dtReplace.getRowCount() > 0) {
			if(orginPrice == 0) orginPrice = mValues.FVal("Price");
			mValues.SetValue("Price", price);
		}
		else {
			mValues.SetValue("Price", orginPrice);
			orginPrice = 0;
		}
		updatePriceView();
	}
	
	public void ActOK() {
		List<ParamList> list = new ArrayList<ParamList>();
		for (DataRow dr : dtReplace.getRows()) {
			list.add(dr.toParamList());
		}
		
		if(orginPrice != 0) mValues.SetValue("orginPrice", orginPrice);
		mValues.SetPathValue("Ext/reps", list);
		
		// 更新产品替换方案
		try {
			String SNo = mValues.SValue("SNo");
			int FItemID = Bill.GetSnIID(SNo);
			if(FItemID < 1) FItemID = Bill.FindProductItemID(mValues.SValue("FName"), mValues.SValue("FModel"));
			ProductRelpace.updateRepsToMap(CustID, FItemID, list);
		} catch (Exception e) {
			ExProc.Show(e);
		}
		
		//返回结果
		setResultAndClose(
				new Param("ID", mID), 
				new Param("values", mValues) 
		);
	}
	
	private void updatePriceView() {
		SpannableStringHelper sh = new SpannableStringHelper();
		float price = mValues.FVal("Price");
		
		sh.appendMoney(price, Color.RED, 1.0f);

		$TX(R.id.tv_price).setText(sh);
	}
	
    // 更新溯源信息
    public void LoadValues() {
        try {
        	mValues = new ParamList(Params.SValue("values"));
    		// 解析扩展字段
        	ParamList plExt = new ParamList(mValues.get("Ext"));
    		mValues.set("Ext", plExt);
    		orginPrice = plExt.FVal("orginPrice");
    		
        	Bill.CoverExtParams(mValues);
            
    		UpdateBaseInfo();
    		
    		updatePriceView();
    		
        	dtReplace = ProductRelpace.getProductReplaceTable(plExt.get("reps"));

    		listAdapter = new HyListAdapter(this, dtReplace.getRows()); 

    		listAdapter.getViewListener = new HyListAdapter.OnGetViewListener() {

    			@Override
    			public View onGet(int position, View convertView, ViewGroup parent) {
    				DataRow dr = type.as(listAdapter.getItem(position), DataRow.class);
    				if (convertView == null) {
    					//convertView = LayoutInflater.from(context).inflate(R.layout.list_sale_item, null);

						LinearLayout.LayoutParams lp;
						
    					LinearLayout ll = new LinearLayout(context);
    			        int padding = gu.dp2px(context, 10);
    				  //  ll.setBackgroundColor(0xFFCCCCFF);
    			        ll.setOrientation(LinearLayout.HORIZONTAL);
    			        ll.setPadding(padding, padding, padding, padding);
    			        
    					TextView tvInfo = new TextView(context);
						lp = new LinearLayout.LayoutParams(0,  LayoutParams.WRAP_CONTENT);
						lp.gravity = Gravity.CENTER_VERTICAL;
						lp.weight = 1.0f;
						tvInfo.setLayoutParams(lp);
						
    					tvInfo.setWidth(LayoutParams.WRAP_CONTENT);
    					
    					tvInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.2f);
    					tvInfo.setWidth(1000);
    					tvInfo.setTextColor(Color.BLACK);
    					tvInfo.setGravity(Gravity.CENTER_VERTICAL);
    					tvInfo.setId(100);
    					ll.addView(tvInfo);
    					

    					ImageView ivDel = new ImageView(context);
    			        int iconSize = gu.dp2px(context, 24);
    					lp = new LinearLayout.LayoutParams(iconSize,  iconSize);
    					lp.gravity = Gravity.CENTER_VERTICAL;
    					ivDel.setLayoutParams(lp);
    					ivDel.setBackgroundResource(R.drawable.del);
    					ivDel.setTag(position);
    					
    					ivDel.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
			    				DataRow dr = type.as(listAdapter.getItem((Integer)v.getTag()), DataRow.class);
								dr.Remove();
								UpdatePrice();
								listAdapter.notifyDataSetChanged();
							}
						});

    					ll.addView(ivDel);
    					convertView = ll;
    				}


    				String info = gs.JoinArray(new String[] {
    						dr.getStrVal("FName"),
    						Param.SVal("规格型号: ", dr.getValue("FModel")),
    						Param.SVal("产品批号: ", dr.getValue("BatchNo")),
    						Param.SVal("有效期至: ", dr.getValue("FPeriodDate")),
    				}, "\n");
    				
    				SpannableStringHelper sh = new SpannableStringHelper();
    				sh.appendObj(info + "\n");
    				
    				sh.startSpan();
    				float price = dr.getFVal("Price");
    				sh.appendMoney(price, Color.RED, 1.0f);
    				
    				TextView v = type.as(convertView.findViewById(100), TextView.class);
    				v.setText(Bill.getBillItemInfo(dr, Bill.GI_PRICE));
    				
    				v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    				return convertView;
    			}
    		};
    		
    		lvReplace.setAdapter(listAdapter);
        } catch (Exception e) {
			ExProc.Show(e);
        }
    }

    public void UpdateRow(int position, ParamList plValues) {
    	try{
    		if(position < 0)
    			plValues.addRowToDataTable(dtReplace);
    		else {
    			DataRow dr = (DataRow)listAdapter.SelectAt(position);
				plValues.setDataRow(dr, true);
			}
        	listAdapter.notifyDataSetChanged();
        	UpdatePrice();
    	} catch (Exception e) {
    		ExProc.Show(e);
    	}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	try {
            if(resultCode == RESULT_OK) {
            	if(requestCode == ActBase.REQ_CODE_EDIT) {
                	ParamList pl = new ParamList(data.getStringExtra("values"));
                	mValues.AddRange(pl);
                	UpdateBaseInfo();
                }
            	if(requestCode == ActBase.REQ_CODE_REPLACE) {
                	ParamList pl = new ParamList(data.getStringExtra("values"));
                	UpdateRow(data.getIntExtra("ID", 0), pl);
                }
            }
		} catch (Exception e) {
			ExProc.Show(e);
		}
        super.onActivityResult(requestCode, resultCode, data);
    } 
    
}
