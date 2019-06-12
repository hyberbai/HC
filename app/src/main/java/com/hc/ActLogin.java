package com.hc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hc.dal.WS;
import com.hc.dal.d;
import com.hc.tools.boxUtils;

import hylib.data.DataRow;
import hylib.sys.HyApp;
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
		//this.setTitle(R.string.act_login_title);
		// setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
		// R.drawable.abc_menu_hardkey_panel_holo_dark);

		InitViews();

        $Set(ID.Title, getString(R.string.app_name) + " V" + HyApp.getVersion());
	}

	public void CreateHeaderBar(){
		Context context = HyApp.CurrentActivity();
		UCParamList pl = new UCParamList(
				"items: [" +
						"ib: { style: header_button,bgc1:lb, bg:ic_launcher }, " +
						"tv: { id:Title, fs: 16sp, text: 登录, w:1w, h:wrap, grv:l, color:title_text_color, padding: 8dp }, " +
						"], style: header_bar");

		ParamList plStyles = new ParamList(new Object[]{
				"header_button: { w:27dp, h:27dp, margin: 6dp }",
				"header_bar: { hor, grv:c, margin: 0dp, h:wrap, w:match, padding: 0dp, color: w, bgc: head_layout_bg_color }",
		});

		pl.setStyles("styles", plStyles);

		View pnlHeader = UICreator.CreatePanel(context, pl);
		getRootLayout().addView(pnlHeader);
	}

	public void InitViews() {
		try {
			CreateHeaderBar();
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

			BindingClickEvent($(ID.SynData), $(222));
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

	private void SynUserData(){
		try{
			MainActions.SynData(new String[] { "User", "Emp" }, null, "用户数据", true);
			d.Init();
			RefreshState();
			gc.Hint("同步用户数据完成！");
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
	public void onClick(View v) {
		int id = v.getId();
		if(v == btnLogin) Login();
		if(id == ID.SynData || id == 222) SynUserData();
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
//    		"[et: { text: '单价', w: 3w }, et: {  text: '单位', lw: match, w: 2w } ], " + 
        		
        	"et: { id:User, color: text, hint: '用户名', }, " +
        	"et: { id:Pwd , color: text,hint: '密　码', pwd }, " +
        	"et: { id:Cust, color: text,hint: '客　户'  }, " +
        	"et: { id:Stock, color: text,hint: '库  位'  }, " +
			"[btn: { id: SynData, bg: refresh_96, marginLeft: 3dp, w:20dp, h: 20dp, lay-grv:c }, " +
			 "tv: { id:222, text: '同步用户', color: #ff2080bf, fs:13sp, margin: 3dp, w:1w, lay-grv:c }, " +
			 "chk: { id: RemPwd, text: '记住密码', color: #ff2080bf, fs:13sp, w: wrap, lay-grv: r }," +
		    "], " +
        	"btn: { id: Login, text: '登录', marginTop: 20dp, style: btn  }, " +
        //    "btn: { id:" + ID.Exit + ", text: '退出', style: btn  }, " + 
        "], width: 60dp, margin: 10dp, space: 0";

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
		//etPwd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
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
