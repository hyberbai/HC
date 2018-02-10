package com.hc.mo.sy;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.data.DataTable;
import hylib.toolkits.ExProc;
import hylib.toolkits.MsgException;
import hylib.toolkits.type;
import hylib.ui.dialog.UICreator;
import hylib.util.ParamList;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.plurals;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.HyScanner;
import com.hc.*;
import com.hc.R;
import com.hc.R.id;
import com.hc.R.layout;
import com.hc.dal.Bill;
import com.hc.dal.WS;
import com.hc.db.DBLocal;
import com.hc.scanner.TestBaseActivity;
import com.hc.scanner.Utils;
import com.hc.tools.ActSearch;
import com.zltd.decoder.DecoderManager;
import com.zxing.*;

public class ActSy extends ActBase {
	private TextView tvProductInfo;

	private EditText etInput;
	private ListView lvSy;
	
	private final static int REQ_CODE_BARCODE = 20;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_sy);
		DecoderMode = HyScanner.Mode.Enabled;
		
		etInput = $(R.id.et_input);
        UICreator.SetViewParam(etInput, "vt:num,hint:输入流水号");
        
		tvProductInfo = $(R.id.tv_product_info);
		lvSy = $(R.id.lv_sy);
		tvProductInfo.setHint("");
		CreateOptionsPopupMenu();
		
		BindingKeyEvent(etInput);
		
		Clear();
		// 数据测试
	//	DBUtil.Test();
		if(Params.containsKey("SNo"))
			FindSYInfo(Params.SValue("SNo"));
	}

	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.et_input) id = R.id.ib_search;
		if(id == R.id.ib_search) FindSYInfo($T(R.id.et_input));
		super.onClick(v);
	}
    
	@Override
	protected void setOptionsMenuParams(ParamList pl) throws Exception {
		final DataRowCollection rows = new DataRowCollection("name|text|pl", new Object[][] {
				new Object[] { "HandInput", "手工录入", null },
		});
		AppendDefaultMenu(rows);
		pl.set("Items", rows);
		pl.set("width", "100dp");
	}
	
    @Override
    public void HandleSNo(String SNo) {
    	FindSYInfo(SNo);
    }
	
	String product_info = "";
	private void AddSYItem(String key, String name) {
		String val = sy_base.Get(key, String.class);
		if(val == "" || val == null) return;
		String c = product_info.equals("") ? "" : "\n";
		if(name.length() == 0) name = key;
		product_info += c + String.format("%s: %s", name, val); 
	}
	
	private void Clear() {
    	tvProductInfo.setText("");
	}

    private ParamList sy_data = null;
    private ParamList sy_base = null;	// 产品基本信息
    
    // 更新溯源信息
    public void FindSYInfo(String code) {
        final String SNo = pu.getSNo(code);
        etInput.setText(SNo);
        etInput.selectAll();
        final Context context = this;
        ShowSoftInput(false);
        
        try {
        	try {
            	sy_data = WS.GetProductTraceInfo(SNo + ":sy");
			} catch (Exception e) {
				if(e instanceof MsgException) throw e;
				if(e instanceof RuntimeException) e = (Exception)((RuntimeException)e).getCause();
				if(e == null) return;
				if(!(e instanceof java.net.ConnectException || e instanceof UnknownHostException)) throw e;
				sy_data = Bill.GetLocalSnInfo(SNo);
				ExProc.Show(e);
			}

        	if(sy_data == null || sy_data.isEmpty()) {

    			ParamList pl = pu.GetProdcutBySN(code);
    			if(pl == null) ExProc.ThrowMsgEx("无效流水号！ " + SNo);
				sy_data = new ParamList();
				sy_data.set("info", pl);
        	}
        	
        	sy_base = type.as(sy_data.get("info"), ParamList.class);
        	if(sy_base == null) return;
        	
        	product_info = "NO." + sy_base.Get("FSerialNo", String.class);

            AddSYItem("FNumber", "产品编码");
            AddSYItem("FName", "产品名称");
            AddSYItem("Manuf", "生产厂家");
            AddSYItem("Reg", "注册证号");
            AddSYItem("PN", "产品序号");
            AddSYItem("FModel", "规格型号");
            AddSYItem("FBatchNo", "产品批号");   
            AddSYItem("FKFDate", "生产日期");    
            AddSYItem("FPeriodDate", "有效期至");    
//            AddSYItem("单价");
//            AddSYItem("数量");
//            AddSYItem("单位");
//            product_info += String.format("\n单价: %s/%s", sy_data.get("FPrice"), sy_data.get("FUnit")); 
        
            
            Object[] hisItems = (Object[])sy_data.get("sy");
    		List<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();
            if(hisItems != null)
    	        for(Object hisItem : hisItems){
    	        	Object[] items = (Object[])hisItem;
    				HashMap<String, String> map = new HashMap<String, String>();
    				
    	            map.put("item_time", items[0].toString());
    	            map.put("op", items[1].toString());  
    	            map.put("operator", "");
    	            map.put("desc", "");
    	            if(items.length > 2) map.put("operator", items[2].toString());  
    	            if(items.length > 3) map.put("desc", items[3].toString());  
//    	            
    	            listItems.add(0, map);
    	        }
        	tvProductInfo.setText(product_info);
            
        	ListSyAdapter adapter = new ListSyAdapter(context, listItems);	  
    		lvSy.setAdapter(adapter);
        } catch (Exception e) {
			tvProductInfo.setText(ExProc.GetErrMsg(e));//数据库没有该流水号[%s]的记录！", sy_lsh));
			lvSy.setAdapter(null);
			ExProc.Show(this, e);
        }
    }

    @Override
    public void DecoderResult(String result) {
		FindSYInfo(result);
    }
}
