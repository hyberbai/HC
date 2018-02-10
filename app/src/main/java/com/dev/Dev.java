package com.dev;

import hylib.sys.SysUtils;
import hylib.toolkits.ExProc;
import hylib.toolkits.Speech;

public class Dev {
	public final static int DEV_DEFAULT = 0;
	public final static int DEV_PL50 = 1;
	public final static int DEV_ZLTD = 2;  
	
	public static int DevType;
	
	public static void CheckDevType(){
		String devModel = SysUtils.getModel();
		if(devModel.indexOf("PL-50") == 0) DevType = DEV_PL50;
		if(devModel.indexOf("N7000") == 0) DevType = DEV_ZLTD;
	}

	public static void Init() {
		CheckDevType();

		try {
			// 打印机初始化
			HyPrt.Init();

			// 条码扫描头初始化
			HyScanner.Init();

			// 语音合成初始化
			Speech.Init();
		} catch(Exception ex){
		    ExProc.Show(ex);
		}
	}
	
	public static void Close(){
		HyScanner.Close(true);

		if(isPL50()) PL50Prt.closeDev();
	}
	
	public static boolean isPL50(){
		return DevType == DEV_PL50;
	}
	
	public static boolean isZLTD(){
		return DevType == DEV_ZLTD;
	}
}
