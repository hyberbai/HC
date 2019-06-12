package com.hc;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;

import com.dev.Dev;
import com.hc.dal.WS;
import com.hc.dal.d;
import com.hc.db.DBLocal;

import hylib.sys.HyApp;
import hylib.toolkits.ExProc;
import hylib.toolkits._D;
import hylib.toolkits.gi;
import hylib.ui.dialog.UCCreator;
import hylib.util.Param;
import hylib.util.ParamList;

import static com.hc.pu.pObj;


public class MyApp extends HyApp {
    public boolean m_bKeyRight = true;

    public static int TestMode;

    public static final int TM_MAIN = 1;

    public enum WorkState {None, Ready, Normal, Done}

    ;

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

    private void Quit() {
        Dev.Close();
    }

    public static void InitResParams() {
        try {
            Resources res = getInstance().getResources();
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
        if (actMain != null) actMain.finish();
    }

    public static void LoadActMain() {
        Intent intent = new Intent(CurrentActivity(), ActHome.class);
        CurrentActivity().startActivity(intent);
    }

    public static void AfterLogin() {
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

    public static void Init(gi.Action actInitDB) throws Exception {
        // 工程初始化
        pu.Init();

        // 设备初始化
        Dev.Init();

        // 初始化资源参数
        InitResParams();

        // web服务调用初始化
        WS.Init();

        // 本地数据库初始化
        DBLocal.Init();

        if (actInitDB != null)
            actInitDB.Execute();

        // 数据表对象初始化
        d.Init();

        // 加载系统参数
        SysData.LoadParams();
    }

    public static void CheckEmptyInit() {
        _D.Out("CheckEmptyInit:" + pObj);
        if (pObj != null) return;
        try {
            Init(null);
            SysData.ReloadUser();
        } catch (Exception e) {
            ExProc.Show(e);
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