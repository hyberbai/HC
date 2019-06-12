package com.hc;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.widget.EditText;

import com.dev.HyPrt;
import com.dev.prt.BtService;
import com.dev.prt.DeviceInfo;
import com.hc.dal.WS;
import com.hc.tools.boxUtils;

import java.util.List;

import hylib.data.DataRow;
import hylib.data.DataTable;
import hylib.sys.HyApp;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits.SysOpen;
import hylib.toolkits.TempV;
import hylib.toolkits.gi;
import hylib.toolkits.gv;
import hylib.ui.dialog.LoadingDialog;
import hylib.ui.dialog.Msgbox;
import hylib.ui.dialog.UIUtils;
import hylib.util.HttpSoap;
import hylib.util.ParamList;


public class ActSettings extends ActBasePreference {
	private static ListPreference preServerAddr = null; 
	private static Preference preConnServer = null; 
	private static Preference preLogin = null; 
	private static Preference preReportOpen = null; 
	private static Preference preSetCust; 
	private static Preference preSetStock; 
	private static Preference prePrinter; 
	private static Preference preResetDB; 

	@Override
	protected void Init() {
		addPreferencesFromResource(R.xml.pref_settings);

//		PreferenceCategory fakeHeader = new PreferenceCategory(this);
//		fakeHeader.setTitle(R.string.pref_header_notifications);
//		getPreferenceScreen().addPreference(fakeHeader);
//		addPreferencesFromResource(R.xml.pref_notification);

		preServerAddr = $("server_addr");
		preConnServer = $("conn_server");
		preLogin = $("login");
		preReportOpen = $("report_open");
		preResetDB = $("db_reset");
		
		preSetCust = $("cust");
		preSetStock = $("stock");
		prePrinter = $("printer");
		
		preServerAddr.setEntryValues(pu.dtServerAddrs.getColStrValues("addr"));
		preServerAddr.setEntries(pu.dtServerAddrs.getStrValues(new gi.IFunc1<DataRow, String>() {
		    public String Call(DataRow dr){
		    	
		    	String addr = dr.getStrVal("addr");
		    	
		    	SpannableStringHelper sh = new SpannableStringHelper();
		    	sh.appendObj(dr.getValue("name"));
		    	
		    	if(!addr.isEmpty())
		    	{
			    	sh.startSpan();
			    	sh.appendObj("[" + addr + "]");
					sh.setForeColor(Color.GRAY);
					sh.setFontSize(0.64f);
		    	}
				return sh.toString();
		    	//return dr.getValue("name") + (addr.isEmpty() ? "" : "[" + addr + "]"); 
		    }
		}));
	}

	@Override
	public void onResume() {
		String addr = HttpSoap.GetServerAddr();
		if(gv.IsEmpty(preServerAddr.getValue()))
			preServerAddr.setValue(addr);

		DataRow dr = pu.dtServerAddrs.FindRow("addr", addr);
		if(dr == null || preServerAddr.getSummary().toString().isEmpty())
			preServerAddr.setSummary(addr);

		preSetCust.setSummary(SysData.CustName);
		preSetStock.setSummary(SysData.StockName);
		preConnServer.setSummary(HttpSoap.IsConected() ? "已连接" : "未连接");
		preLogin.setSummary(SysData.ug_name + " - " + SysData.op_name);
		super.onResume();
	}

	@Override
	protected boolean onClick(final Preference preference) {
		boolean bOK = true;
		final TempV t = new TempV();
		
		if (preference == preSetCust || preference == preSetStock) {
			ParamList pl = new ParamList();
			pl.SetValue("value", preference.getSummary());
			pl.SetValue("onChanged", new EventHandleListener() {
				@Override
				public void Handle(Object sender, ParamList arg) throws Exception {
					if(t.isOK()) return;
					if(UIUtils.TextChangePassed(arg)) return;
					EditText et = (EditText)sender;
					t.set(true);
					DataRow dr = preference == preSetCust ? boxUtils.ChooseCust(et) :
									boxUtils.ChooseStock(et);
					t.set(false);
					if(dr != null) arg.SetValue("dismiss", true);
					if(preference == preSetCust) {
						preference.setSummary(SysData.CustName);
					}
					preSetStock.setSummary(SysData.StockName);
				}
			});
			String s = preference == preSetCust ? "客户" : "库位";
			Msgbox.Input(HyApp.CurrentActivity(), "设置" + s, pl);
		}

		if (preference == preResetDB) {
			if(MainActions.ResetDB())
				ReLogin(false);
		}
		if (preference == preConnServer) {
			bOK = TryConnectServer(HttpSoap.GetServerAddr());
			if (bOK)
				setResult(RESULT_OK, null);  
			else {
				preference.setSummary("连接失败！");
				Msgbox.Hint("无法连接服务器, 请检查地址设置是否正确！");
			}
		}
		else if (preference == preLogin)
			ReLogin(true);
		else if (preference == prePrinter)
			SetPrinter();
		else if (preference == preReportOpen) {
			SysOpen.ChooseOpen(MyApp.CurrentActivity());
			//SysOpen.startSelectHomeDialog(MyApp.CurrentActivity());
		}
		return bOK;
	}

	public void ReLogin(boolean clearUser) {
		if(clearUser) {
			g.SetSysParam("user_name", "");
			g.SetSysParam("upd", "");
			SysData.op_name = "";
		}
		Intent intent = new Intent(this, ActLogin.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("relogin", true);
        intent.putExtras(bundle);
        startActivityForResult(intent, ActBase.REQ_CODE_LOGIN);
	}

	public static void SetPrinter() {
		//UIUtils.startActivity(act, ActPrintSetting.class, ActBase.REQ_CODE_PRINTER);
		try {
			List<DeviceInfo> list = BtService.GetDeviceList();
			final DataTable dt = new DataTable("prts", "name|addr");
			dt.addNewRow("自动识别", "");
			//if(Dev.isPL50()) dt.addNewRow("自带打印机", "");

			if(list != null)
				for (DeviceInfo di : list)
					if(HyPrt.isPrt(di)) dt.addNewRow(di.Name, di.Addr);
			
			if(dt.isEmpty()) ExProc.ThrowMsgEx("未找到匹配的打印设备！");
			
			Msgbox.Choose("选择打印机", dt.getRows(), new ParamList("summary; dm: 'name, addr'"), new DialogInterface.OnClickListener(){
		  	    public void onClick(DialogInterface dialog, int which) {  
		  			if(which < 0) return;
	  				DataRow dr = dt.getRow(which);
	  				SysData.setPrinter(HyPrt.getPrtFullName(dr.getStrVal("name"), dr.getStrVal("addr")), true);
	  				prePrinter.setSummary(SysData.Printer);
		  	    }
			});
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	@Override
	public Object onListChange(ListPreference preference, int index) {
		setResult(RESULT_OK, null);
		if(preference == preServerAddr) {
			Object result = SetServerAddr(index);
			if(index == preServerAddr.getEntries().length - 1)
				return result;
		}
		return super.onListChange(preference, index);
	}

	private Object SetServerAddr(int index) {
		if(index == pu.dtServerAddrs.getRowCount() - 1)
		{
			try {
				ParamList pl = new ParamList();
				pl.set("loop", true);
				pl.SetValue("value", HttpSoap.GetServerAddr(), false);
				pl.set("input", new EventHandleListener() {

					@Override
					public void Handle(Object sender, ParamList arg) throws Exception {
						String val = pu.GetFullServerAddr(arg.SValue("value"));
						if (TryConnectServer(val))
						{
							setResult(RESULT_OK, null);
							g.SetSysParam("server_addr", val);
							pu.dtServerAddrs.lastRow().setValue("addr", val);
							preServerAddr.setSummary(val);
						}
						else
							ExProc.ThrowMsgEx("连接失败, 请重试！");
					}
				});
				Object v = Msgbox.Input(MyApp.CurrentActivity(), "请输入服务器地址:", pl);
				if(v == null) return null;
				if(v instanceof String)	HttpSoap.SetServerAddr((String)v);
				return v;
			} catch (Exception e) {
				ExProc.Show(e);
				return false;
			}
		}

		HttpSoap.SetServerAddr((String)pu.dtServerAddrs.getValue(index, "addr"));
		return true;
	}

	private boolean TryConnectServer(String addr) {
		String old_addr = HttpSoap.GetServerAddr();
		HttpSoap.SetServerAddr(addr);
        LoadingDialog.Show(this, "正在连接服务器...");

        try {
            WS.ConnectServer();
            preConnServer.setSummary("已连接");
			return true;
		} catch (Exception e) {
			HttpSoap.SetServerAddr(old_addr);
			return false;
		} finally {
	        LoadingDialog.Hide();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == ActBase.REQ_CODE_LOGIN) {
			finish();
			MyApp.CloseActMain();
			if(resultCode == RESULT_OK) 
				MyApp.LoadActMain();
		}
	}


	public static void Load(Activity act) {
//		if(SysData.ug_id != SysData.UG_ADMIN) {
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("input_type", "pwd");
//			map.put("input_vertify", new g.OnInputVerifyListener() {
//				@Override
//				public boolean onInputVerify(String input) {
//					boolean ok = d.IsSuperPwd(input);
//					if(!ok) g.HintTd("输入密码不正确！");
//					return ok;
//				}
//			});
//			if(g.Msgbox("", "请输入管理员密码：", g.MB_Input, map) == g.MR_No) return;
//		}
		Intent intent = new Intent(act, ActSettings.class);
		act.startActivityForResult(intent, R.id.action_settings);
	}
}
