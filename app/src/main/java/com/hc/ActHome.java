package com.hc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hc.mo.bill.ActBill;
import com.hc.mo.bill.ActBillBH;
import com.hc.mo.bill.ActBillSale;
import com.hc.mo.bill.ActBillTH;
import com.hc.mo.bill.ActHisListBill;
import com.hc.mo.bill.ActHisListStockBill;
import com.hc.mo.bill.ActStatsBill;
import com.hc.mo.bill.ActTest;
import com.hc.mo.pd.ActPD;
import com.hc.mo.sy.ActSy;

import hylib.sys.HyApp;
import hylib.toolkits.ExProc;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.ui.dialog.UICreator;
import hylib.util.Action;
import hylib.util.ActionInfo;
import hylib.util.ActionList;
import hylib.util.ParamList;
import hylib.view.ActivityEx;
import hylib.widget.VGWrap;


public class ActHome extends ActivityEx {
	private VGWrap vwItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_home);

			MyApp.actMain = this;
			MyApp.CheckEmptyInit();

			vwItems = $(R.id.vw_items);

			FillList();
			RefreshHeaderInfo();

			//CreateNaviBar();
		} catch (Exception e) {
		    ExProc.Show(e);
		}
	}

	public void CreateNaviBar() {
		try {
			ParamList pl = new ParamList();
			ViewGroup bar = UICreator.CreateToolBar(context, "Delete ToggleSelAll Stat More", pl);
			//View v = UICreator.CreateLayout(this, "w:100dp, h:100dp, bgc:#80000");
			getRootLayout().addView(bar);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MyApp.Exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == R.id.action_settings) {
			RefreshHeaderInfo();
		}
	}

	private void RefreshHeaderInfo() {
		$Set(R.id.tv_info1, gs.Connect(SysData.StockName.replace("-", " - "), SysData.getUserInfo(), "\n"));
		$Set(R.id.tv_info2, gv.getNowText());
	}

	public static void CheckBeforeEdit() {
		if(SysData.StockID == 0) ExProc.ThrowMsgEx("尚未设置库位！");
		if(SysData.CustID == 0) ExProc.ThrowMsgEx("尚未设置客户！");
	}

	public static void loadActBill(Class<? extends ActBill> cls) {
		try {
			CheckBeforeEdit();
			loadActivity(cls, 0);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public static void loadActivity(Class<? extends Activity> cls, int reqCode) {
		ActivityEx activity = (ActivityEx)HyApp.CurrentActivity();
		activity.startActivity(cls, reqCode); 
	}

	public static void loadActivity(Class<? extends Activity> cls) {
		loadActivity(cls, 0);
	}
	
	public static ActionList ACL = new ActionList(
		new ActionInfo[] {
				new ActionInfo("SY", "产品溯源", R.drawable.y1), 
				new ActionInfo("Sale", "手术使用", R.drawable.y2), 
				new ActionInfo("BH", "备货补货", R.drawable.y3), 
				new ActionInfo("TH", "返货处理", R.drawable.y7), 
				new ActionInfo("PD", "库存盘点", R.drawable.y4), 
				new ActionInfo("HisListBill", "历史记录", R.drawable.y5), 
				new ActionInfo("HisListStockBill", "金蝶单据", R.drawable.y6), 
	
				new ActionInfo("SynData", "数据同步", R.drawable.m_syn), 
				new ActionInfo("Setting", "系统设置", R.drawable.m_setting), 

				new ActionInfo("Test", "测试", R.drawable.more), 
		},
		new Action() {
			public void Execute(ActionInfo action) {
				
				// 产品溯源
				if(action.Name.equalsIgnoreCase("SY"))
					loadActivity(ActSy.class); 
				
				// 手术使用
				else if (action.Name.equalsIgnoreCase("Sale"))
					loadActBill(ActBillSale.class);
				
				// 备货补货
				else if (action.Name.equalsIgnoreCase("BH"))
					loadActBill(ActBillBH.class); 
				
				// 返库处理
				else if (action.Name.equalsIgnoreCase("TH"))
					loadActBill(ActBillTH.class); 
				
				// 盘点
				else if (action.Name.equalsIgnoreCase("PD"))
					loadActivity(ActPD.class); 
				
				// 历史记录
				else if (action.Name.equalsIgnoreCase("HisListBill"))
					loadActivity(ActHisListBill.class); 
				
				// 金蝶历史记录
				else if (action.Name.equalsIgnoreCase("HisListStockBill"))
					loadActivity(ActHisListStockBill.class); 
				
				// 同步数据
				else if (action.Name.equalsIgnoreCase("SynData"))
					MainActions.SynAllData(true);
				
				// 系统设置
				else if (action.Name.equalsIgnoreCase("Setting"))
					loadActivity(ActSettings.class, R.id.action_settings); 
				
				// test
				else if (action.Name.equalsIgnoreCase("Test"))
					loadActivity(ActTest.class); 
			}
		}
	);
	
    private void FillList() {
    	View.OnClickListener onClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ActionInfo act = (ActionInfo)v.getTag();
				act.Execute();
				//gc.ExecMethod(context, context.getClass(), "Act" + act.Name);
			}
		};

		ACL.setVisible("Test", MyApp.TestMode == MyApp.TM_MAIN);
		if(SysData.ug_id == SysData.UG_OUT) {
			ACL.setVisible("BH, HisListStockBill", false);
		}
		
		for (ActionInfo act : ACL) {
			if(!act.Visible) continue;
			
	        LinearLayout lay = (LinearLayout)View.inflate(this, R.layout.list_image_item, null);
			
			// 图标
			ImageView imgView = (ImageView)lay.findViewById(R.id.image);
			imgView.setImageDrawable(getResources().getDrawable(act.ResID));
			
			// 文字
			TextView tvTitle = (TextView)lay.findViewById(R.id.title);
			tvTitle.setText(act.Title);
			tvTitle.setTextColor(0xFF555555);

	        lay.setTag(act);
	        
		    lay.setOnClickListener(onClickListener);
			vwItems.addView(lay);
		}
    }

	// 测试
	public void ActTest() {
		//startActivity(ActTest.class); 
		startActivity(ActStatsBill.class); 
	}
}
