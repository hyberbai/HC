package com.hc;

import java.util.List;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class MainActivity extends ActivityGroup {
	String tabViewTagPrev = null;  
    public TabHost tabHost;

	private void updateTab(final TabHost tabHost) {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			View view = tabHost.getTabWidget().getChildAt(i);
			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title);

			if (tabHost.getCurrentTab() == i) {// 选中
				view.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
				tv.setTextColor(getResources().getColor(R.color.white));
				// view.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_sel));//选中后的背景
				// tv.setTextColor(this.getResources().getColorStateList(
				// android.R.color.white));
			} else {// 不选中
				view.setBackgroundColor(0xFFBBBBBB);// 非选择的背景
				tv.setTextColor(0xFF444444);
				// view.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_nor));//选中后的背景
				// view.setBackgroundDrawable(null);
				// tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
			}
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        
		tabHost = (TabHost) findViewById(R.id.tabhost);

		// 如果不是继承TabActivity，则必须在得到tabHost之后，添加标签之前调用tabHost.setup()

		tabHost.setup(this.getLocalActivityManager());
		tabHost.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));

		// 这里content的设置采用了布局文件中的view
		tabHost.addTab(tabHost.newTabSpec("tabHome")
				.setIndicator("主页", getResources().getDrawable(R.drawable.tl_red1))
				.setContent(new Intent(this, ActHome.class)));

//		tabHost.addTab(tabHost.newTabSpec("tabSale")
//		// .setIndicator("出库", getResources().getDrawable(R.drawable.tl_red1))
//				.setIndicator("出库").setContent(new Intent(this, ActBillSale.class)));

		
		tabHost.addTab(tabHost.newTabSpec("tabMore")
		// .setIndicator("出库", getResources().getDrawable(R.drawable.tl_red1))
				.setIndicator("更多").setContent(new Intent(this, ActMore.class)));
		
		
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			View v = tabHost.getTabWidget().getChildAt(i);

			TextView tv = (TextView) v.findViewById(android.R.id.title);
			// tv.setTextColor(Color.GREEN);
			tv.setGravity(Gravity.CENTER);
			tv.setTextSize(12);
			// tv.setTypeface(Typeface.SERIF, 2); // 设置字体和风格
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
			tv.setLayoutParams(params);
		}

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (tabViewTagPrev != null) {
					// View view = tabHost.getTabWidget().getChildAt(Integer.parseInt(tabId));
				}
				updateTab(tabHost);
				tabViewTagPrev = tabId;
			}
		});

		//tabHost.setCurrentTab(1);
		tabHost.setCurrentTab(0);
		updateTab(tabHost);

		// 数据测试
		// DBUtil.Test();

		Clear();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyApp.Exit();
			return false;
		}
		if (keyCode == KeyEvent.KEYCODE_0) {
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case R.id.action_settings:
			if (resultCode == RESULT_OK) {
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch (item.getItemId()) {
		case R.id.action_settings:
//			if(SysData.ug_id != SysData.UG_ADMIN) {
//				HashMap<String, Object> map = new HashMap<String, Object>();
//				map.put("input_type", "pwd");
//				map.put("input_vertify", new g.OnInputVerifyListener() {
//					@Override
//					public boolean onInputVerify(String input) {
//						boolean ok = d.IsSuperPwd(input);
//						if(!ok) gc.HintTd("输入密码不正确！");		   
//						return ok;
//					}
//				});
//				if(Msgbox.Show(this, "", "请输入管理员密码：", Msgbox.MB_Input, map) == Msgbox.MR_No) return false;
//			}
			Intent intent = new Intent(this, ActSettings.class);
			startActivityForResult(intent, R.id.action_settings);
			break;

		// case R.id.action_about:
		// showVersionInfo();
		// break;

		default:
			ret = super.onOptionsItemSelected(item);
			break;
		}

		return ret;
	}
	
	private void Clear() {
	}

	public String[] GetListItem(List<String[]> list, int index) {
		return list != null && index < list.size() ? list.get(index) : null;
	}
	
}
