package com.hc;

import com.dev.Dev;
import com.hc.dal.WS;
import com.hc.dal.d;
import com.hc.db.DBLocal;

import hylib.sys.HyApp;
import hylib.toolkits.DelayTask;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.gc;
import hylib.util.ParamList;
import hylib.view.ActivityEx;
import android.content.Intent;
import android.os.Bundle;

public class ActLoading extends ActivityEx
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		DelayTask.Execute("loading", new EventHandleListener() {
			public void Handle(Object sender, ParamList arg) throws Exception {
				Loading();
			}
		}, 100);
	}

	/**
	 *  系统加载初始化数据
	 */
    public void Loading() {
		try {
			ptest.testFirst();
			
			// 工程初始化
			pu.Init();
			
			// 设备初始化
	        Dev.Init();

	        // 初始化资源参数
			MyApp.InitResParams();
			
			// web服务调用初始化
			WS.Init();

			// 本地数据库初始化
	    	DBLocal.Init();
			
			// 如果数据库没有用户数据，首先执行数据同步
			int usercount = DBLocal.ExecuteIntScalar("select count(*) from user");
			if(usercount == 0)
			{
				$Set(R.id.tv_loading_msg, "初始化数据库...");
				MainActions.SynAllData(false);
			}
			
	        // 数据表对象初始化
			d.Init();

	        // 加载系统参数
			SysData.LoadParams();
			
			ptest.testLoad();
		} catch (Exception e) {
			ExProc.Show("系统初始化失败！", e);
		}

		if(MyApp.TestMode == MyApp.TM_MAIN) {
			MyApp.LoadActMain();
		} else {
			// 加载登录界面
			startActivity(ActLogin.class);
		}
		finish();
	}
	
}
