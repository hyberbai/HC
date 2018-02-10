package com.hc;

import hylib.data.DataRow;
import hylib.toolkits.ExProc;
import hylib.toolkits.gc;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.ui.dialog.UCParamList;
import hylib.ui.dialog.UICreator;
import hylib.ui.dialog.UIUtils;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.view.ActivityEx;

import com.hc.dal.*;
import com.hc.db.DBLocal;
import com.hc.tools.boxUtils;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ActLogin extends ActivityEx
{
	private EditText etName;
	private EditText etPwd;
	private EditText etCust;
	private EditText etStock;
	private Button btnLogin;
	private boolean isRemPwd;
	private CheckBox chkRemPwd;
	private boolean mRelogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		this.setTitle(R.string.act_login_title);
		// setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
		// R.drawable.abc_menu_hardkey_panel_holo_dark);

		InitViews();
	}

	public void InitViews() {
		try {
			CreatePanel();

			etName = $(ID.User) ;
			etPwd = $(ID.Pwd); 
			etCust = $(ID.Cust); 
			etStock = $(ID.Stock); 
			chkRemPwd = $(ID.RemPwd);
			
			isRemPwd = g.GetSysParam("rp").equals("1");
			chkRemPwd.setChecked(isRemPwd);
			chkRemPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					isRemPwd = isChecked;
					g.SetSysParam("rp", isChecked ? "1" : "0");
				}
			});

			etName.setText(SysData.op_name);
			
			$Set(ID.Cust, SysData.CustName);
			$Set(ID.Stock, SysData.StockName);
			if(isRemPwd) etPwd.setText(g.GetSysParam("upd"));

			if(!gv.IsEmpty(SysData.op_name))
				etPwd.requestFocus();
			// g.createShortCut(this);
			Bundle bd = this.getIntent().getExtras();
			if (bd != null)
				if (bd.getBoolean("relogin"))
					mRelogin = true;

			RefreshState();
			btnLogin = $(ID.Login);
			//UICreator.SetViewParam(btnLogin, "margin:10dp");
			btnLogin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Login();
				}
			});

			BindingValueChangeEvent(etCust, etStock);
			BindingFocusChangeEvent(etName, etPwd, etCust, etStock);
			
//			((Button) $(ID.Exit)).setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View view) {
//					MyApp.ExitNow();
//				}
//			});
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
	@Override
	public void onTextChanged(Object sender, ParamList arg) {
		try {
			LockChange = true;
			if(UIUtils.TextChangePassed(arg)) return;
			if(sender == etCust) {
				if(boxUtils.ChooseCust(etCust) != null)
					etStock.setText(SysData.StockName);
			}
			if(sender == etStock) boxUtils.ChooseStock(etStock);
		} catch (Exception e) {
			ExProc.Show(e);
		} finally {
			LockChange = false;
		}
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus) return;
		LockChange = true;
		if(v == etName) RefreshState();
		if(v == etPwd) RefreshState();
		if(v == etCust) etCust.setText(SysData.CustName);
		if(v == etStock) etStock.setText(SysData.StockName);
		LockChange = false;
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
    protected void CreatePanel() {
		// 手工录入产品信息
        String config = "items: [" + 
//    		"[et: { title: '单价', w: 3w }, et: { title: '单位', marginLeft: 10dp, lw: match, w: 2w } ], " + 
//    		"[et: { text: '单价', w: 3w }, et: {  text: '单位', lw: match, w: 2w } ], " + 
        		
        	"et: { id:" + ID.User + ", color: text, hint: '用户名', }, " + 
        	"et: { id:" + ID.Pwd + ", color: text,hint: '密　码', pwd }, " + 
        	"et: { id:" + ID.Cust + ", color: text,hint: '客　户'  }, " + 
        	"et: { id:" + ID.Stock + ", color: text,hint: '库  位'  }, " + 
        	"chk: { id:" + ID.RemPwd + ", text: '记住密码', color: #ff2080bf, fs:13sp, w: wrap, lay-grv: r }, " + 
        	"btn: { id:" + ID.Login + ", text: '登录', marginTop: 20dp, style: btn  }, " +  
        //    "btn: { id:" + ID.Exit + ", text: '退出', style: btn  }, " + 
        "], width: 60dp, margin: 10dp";

        //items[*.id]==
        try {
            UCParamList pl = new UCParamList(config);
            pl.setStyles("styles", new Param[] {
            	pl.newStyle("btn", "fs: 16sp, margin: 1dp,h:36dp, padding: 0dp, color: #FFFFFFFF,", new Param("bg", R.drawable.button_bg)),
            });
            //findPanelItemPM(pl, ID.FModel).set("searchListener", searchListener);

            ViewGroup vg = UICreator.CreateScrollPanel(this, pl);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)vg.getLayoutParams();
            if(lp == null) lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            vg.setLayoutParams(lp);
            
            this.getRootLayout().addView(vg, 1);

		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	

    public ParamList findPanelItemPM(ParamList pl, int id) throws Exception {
        Object[] os = type.as(pl.get("items"), Object[].class);
        for (Object o : os) {
        	Param item = type.as(o, Param.class);
        	ParamList plItem = type.as(item.Value, ParamList.class);
        	if(plItem.IntValue("id") == id) return plItem;
		}
        return null;
	}
	
	public void Login() {
		final String name = etName.getText().toString();
		final String pwd = etPwd.getText().toString();
	//	etPwd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
		RefreshState();
		// 登陆验证
		try {
	    	WS.Login(name, pwd);
        	if(isRemPwd && SysData.op_id != SysData.SUPER_OP_ID) g.SetSysParam("upd", pwd);
        	g.SetSysParam("user_name", name);
    		setResult(RESULT_OK);
			LoadActMain();
			finish();
		} catch (Exception e) {
    		setResult(RESULT_CANCELED);
			ExProc.ShowMsgbox(this, e);
		}
	}

	private void LoadActMain() {
		if (!mRelogin) {
			Intent intent = new Intent(this, ActHome.class);
			startActivity(intent);
			MyApp.AfterLogin();
		}
	}

	@Override
	public void RefreshState() {
		final String name = etName.getText().toString();
		final String pwd = etPwd.getText().toString();
		if(!SysData.trySuperPwdLoad(pwd)) {
			DataRow dr = d.findUser(name);
			SysData.RT = dr == null ? "" : dr.getStrVal("RT"); 
		}
		SysData.LoadRTs();
		LockChange = true;
		etStock.setText(SysData.StockName);
		etCust.setText(SysData.CustName);
		LockChange = false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			if (!mRelogin)
				System.exit(0);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
