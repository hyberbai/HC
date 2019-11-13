package com.hc;

import android.app.Dialog;

import com.hc.dal.Bill;
import com.hc.setting.pSetting;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hylib.data.DataRow;
import hylib.data.DataTable;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.util.ActionList;
import hylib.util.ParamList;


//工程函数类
public class pu {
	public static String DEFAULT_SERVER_ADDR = "218.25.17.250:2603";
//	public static String DEFAULT_SERVER_ADDR = "192.168.1.222:2603";

	public static String DEFAULT_IP_PORT = "2603";
	public static String TEMP_PATH = "/sdcard/";
	
	public static DataTable dtServerAddrs = new DataTable("ServerAddrs", "name|addr", new Object[][] {
		//new Object[] { "内网地址", "192.168.1.200:2603" },
		new Object[] { "外网地址", DEFAULT_SERVER_ADDR },
		//new Object[] { "综合测试", "192.168.1.200:2603" },
		new Object[] { "系统测试", "192.168.1.222:2603" },
		new Object[] { "<自定义> ", "" },
	});
	
	public static boolean IsTest = false;
	public static Object pObj;

	public static void Init() {
		if(pObj != null) return;
		pObj = "HC";
//		boolean a = Network.isNetworkAvailable();
//		boolean a1 = Network.isWiFiAvailable();
//		boolean a2 = Network.isMobileAvailable();
		pSetting.Init();
		LoadActionList();
	}

	// 在你的setPositiveButton中添加，用于不关闭对话框
	public void SetDialogClose(Dialog dialog, boolean closed) {
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, closed);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String GetFullServerAddr(String addr) {
		addr = addr.trim();
		if(addr.equals("hy")) return DEFAULT_SERVER_ADDR;
    	if(Pattern.compile("^[0-9]{1,3}$").matcher(addr).find()){
    		return "192.168.1." + addr + ":" + DEFAULT_IP_PORT;
    	} else if(Pattern.compile("^[\\w\\-_]+(\\.[\\w\\-_]+)+$").matcher(addr).find()){
    		return addr + ":" + DEFAULT_IP_PORT;
    	} else {
    		return addr;
    	}		
	}
	
	public static String GetFullSN(String sn) {
		if (sn.length() >= 6) return sn; 
		return String.format("%06d", gv.IntVal(sn));
	}
	
    // 获得取流水号
    public static String getSNo(String barcode) {
    	barcode = barcode.trim(); 
    	Matcher matcher = Pattern.compile("[A-Zo]{1,4}\\.([\\d]{6,12})").matcher(barcode);

    	String SNo = "";
    	if(matcher.find()) // 扫码流水号
    		SNo = matcher.group(1);
    	else {   
        	matcher = Pattern.compile("^([0-9]+)").matcher(barcode);    
        	if(matcher.find()) {	// 手工输入流水号，补全6位
            	SNo = GetFullSN(matcher.group(1));
        	} else {
        		matcher = Pattern.compile("^.([0-9]+)").matcher(barcode);
            	if(matcher.find())	// 手工输入流水号，不补全
                	SNo = matcher.group(1);
            	else {
            		//gc.Hint(barcode + "\n为无效二维码！\n");
            		return "";
            	}
        	}
    	}
    	
    	return SNo;
    }

    public static void LoadActionList()
    {
		if(!ActionList.isInstEmpty()) return;
		ActionList.Init();
        ActionList.Add("确定", "OK");
        ActionList.Add("取消", "Cancel");
        ActionList.Add("提交", "Submit");
        ActionList.Add("清空", "Clear");
        ActionList.Add("打印", "Print");
        ActionList.Add("重置", "Reset");
        ActionList.Add("前单", "Prev");
        ActionList.Add("后单", "Next");
        ActionList.Add("新增", "New");
        ActionList.Add("添加", "Add");
        ActionList.Add("修改", "Edit");
        ActionList.Add("删除", "Delete", R.drawable.del);
        
        ActionList.Add("全选", "ToggleSelAll", R.drawable.muti_select);
        ActionList.Add("汇总统计", "Stats", R.drawable.chart);
        ActionList.Add("更多", "More", R.drawable.more);
        ActionList.Add("移除", "Remove");
        ActionList.Add("保存", "Save");
        ActionList.Add("复制", "Copy");
        ActionList.Add("剪切", "Cut");
        ActionList.Add("粘贴", "Paste");
        ActionList.Add("搜索", "Search", R.drawable.search);
        ActionList.Add("过滤", "Filter");
        ActionList.Add("导出", "Export");
        ActionList.Add("导入", "Import");
        ActionList.Add("刷新", "Refresh");
        ActionList.Add("溯源", "SY", R.drawable.y1);
        ActionList.Add("测试", "Test");
        ActionList.Add("设置", "Setting");
        ActionList.Add("重置", "Reset");
        ActionList.Add("选择", "Select");
        
        ActionList.Add("退出", "Exit");
        ActionList.Add("登录", "Login");
        ActionList.Add("关闭", "Close");
    }

	public static String PickSNFromLine(String sLine)
	{
		return  getSNo(sLine);

//		var matches = Pattern.compile("^([A-Zo]{1,4})\\.([\\d]{6,12})$").matcher(sLine);
//		if (matches. == 0 || matches[0].Groups.Count != 3) return null;
//		SPNo = matches[0].Groups[1].Value;
//		if (gs.SameText(SPNo, "NO")) SPNo = "";
//		String sn = matches[0].Groups[2].Value;
//		return sn.Length == 6 || sn.Length == 12 ? sn : null;
	}

    // 解析金蝶流水号
	public static ParamList ParseJDSN(String code)
	{
		ParamList result = new ParamList();
		code = code.trim();

		if (code.length() > 20)
		{
			String[] ss = code.split("\\r|\\n");
			for (String s : ss)
			{
				if(s.length() == 0) continue;
				String[] ss1 = s.split("：");
				if (ss1.length > 1)
				{
					String key = null;
					String v = ss1[1];
					if (ss1[0] == "厂家") key = "Manuf";
					if (ss1[0] == "规格") key = "Spec";
					if (ss1[0] == "批号") key = "BatchNo";
					if (ss1[0] == "序号") key = "PN";
					if (ss1[0] == "生产日期") { key = "MfgDate"; v = gs.NormalizeDate(v, false); }
					if (ss1[0] == "有效期") { key = "ExpDate"; v = gs.NormalizeDate(v, true); }
					if (key != null) result.SetValue(key, v);
				} else {
					String SN = PickSNFromLine(ss1[0]);
					if (!gv.IsEmpty(SN))
						result.SetValue("SN", SN);
					else
						result.SetValue("PName", ss1[0]);
				}
			}
		}
		else
			result.SetValue("SN",  getSNo(code));

		return result;
	}
    
    public static ParamList GetProductBySN(String code) throws Exception {
		ParamList pl = ParseJDSN(code);
		if(pl == null) return null;
		int FItemID = -1;
		DataRow dr = Bill.FindProductByNameAndSpec(pl.SValue("PName"), pl.SValue("Spec"));
		if(dr != null)
		{
			FItemID = dr.getIntVal("FItemID");
			dr.putParamList(pl);
			pl.SetValue("FUnit", dr.getValue("FUnit"));
			pl.SetValue("FItemID", FItemID);
			pl.SetValue("Cls", dr.getValue("Cls"));
		}
		pl.RenKeys("SNo FSerialNo, PName FName, Spec FModel, BatchNo FBatchNo, MfgDate FKFDate, ExpDate FPeriodDate");
		return pl;
	}
    
    public static boolean isValidSNo(String SNo) {
		return SNo != null && SNo.length() == 6;
	}
	
}
