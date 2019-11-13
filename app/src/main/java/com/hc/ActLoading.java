package com.hc;

import android.os.Bundle;

import com.hc.db.DBLocal;

import hylib.toolkits.DelayTask;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.gi;
import hylib.util.ParamList;
import hylib.view.ActivityEx;

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

			MyApp.Init(new gi.Action() {
				public void Execute(){
					// 如果数据库没有用户数据，首先执行数据同步
					int usercount = DBLocal.ExecuteIntScalar("select count(*) from user");
					if(usercount == 0)
					{
						$Set(R.id.tv_loading_msg, "初始化数据库，请稍候...");
						MainActions.SynAllData(false);
					}
				}
			});
			
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
