package com.hc;

import java.util.ArrayList;
import java.util.List;

import com.dev.Dev;
import com.dev.HyScanner;
import com.dev.PL50Prt;
import com.dev.ZltdDecoder;
import com.hc.dal.WS;
import com.hc.dal.d;
import com.hc.db.DBLocal;
import com.hc.oa.OA;

import hylib.sys.*;
import hylib.toolkits.ExProc;
import hylib.toolkits.Speech;
import hylib.toolkits._D;
import hylib.toolkits.gi;
import hylib.toolkits.gu;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.ui.dialog.UCCreator;
import hylib.util.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.EditText;


public class MyApp extends HyApp {
    public boolean m_bKeyRight = true;
    
	public static int TestMode;
	
	public static final int TM_MAIN = 1;

	public enum WorkState { None, Ready, Normal, Done };

    public static Activity actMain;
    
    static {
    //	TestMode = TM_MAIN;
    }
    
	@Override
    public void onCreate() {
	    super.onCreate();
	  //  ExProc.ThrowMsgEx("asdsadfsadf");

	    AppExitListener = new gi.NotifyListener() {
			public Object Notify(Object sender, Object arg) {
				Quit();
				return null;
			}
		};
		
		//p.GetSystemParams();
	}
	
	private void Quit(){
		Dev.Close();
	}

	public static void InitResParams() {
		try {
			Resources res = getInstance().getResources();
			Object a = res.getColor(R.color.divider_color);
			UCCreator.Init(new ParamList(
					new Param("dividerColor", res.getColor(R.color.divider_color)),
					new Param("dividerHeight", res.getDimension(R.dimen.divider_height)),
					null
			));
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public static void CloseActMain() {
		if(actMain != null) actMain.finish();
	}
	
	public static void LoadActMain() {
		Intent intent = new Intent(CurrentActivity(), ActHome.class);
		CurrentActivity().startActivity(intent);
	}
	
	public static void AfterLogin(){
		// 开启OA 后台邮件监控句柄
		//OA.StartWatch();
	}

    public static void Test() {
		try {
			Test1();
		} catch (Exception e) {
			_D.Dumb();
		}
    }

    public static void Test1() throws Exception {
//		ParamList pl = new ParamList("{ a:1, b:'aaa' }");
//		List<Object> list = new ArrayList<Object>();
//		list.add(pl);
//
//		ParamList pl1 = new ParamList();
//		pl1.SetValue("11111", pl);
//		String s = gv.Serialize(pl1);
//		
//		_D.Out(s);
    }
}