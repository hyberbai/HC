package com.dev;

import java.io.File;
import java.io.FileWriter;

import com.dev.prt.PrinterClass;
import com.dev.util.PrintQueue;

import hylib.sys.HyApp;
import hylib.toolkits.ExProc;
import hylib.toolkits._D;
import hylib.toolkits.gc;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import android.R.integer;
import android.content.Context;
import android.os.Handler;
import android.posapi.PosApi;
import android.posapi.PosApi.OnCommEventListener;
import android.print.PrinterCapabilitiesInfo;
public class PL50Prt {
	private static PosApi mPosApi = null;
	private static PrintQueue mPrintQueue = null;
	private static Context context = HyApp.getInstance();

	private static Handler mHandler;

	//1倍字体大小 
	private final static byte[] _1x = new byte[]{0x1b,0x57,0x01};
	
	//2倍字体大小 
	private final static byte[] _2x = new byte[]{0x1b,0x57,0x02};
	
	public static void Init(){
		setSamOn(true);
		
		mPosApi = PosApi.getInstance(context);
		//设置初始化回调
		mPosApi.setOnComEventListener(mCommEventListener);
		//使用扩展方式初始化接口
		mPosApi.initDeviceEx("/dev/ttyMT2");

		mPrintQueue = new PrintQueue(context, mPosApi);
		mPrintQueue.init();
		mPrintQueue.setOnPrintListener(new PrintQueue.OnPrintListener() {

			@Override
			public void onGetState(int state) {
				if(state == 0) gc.Hint("有纸");
				if(state == 1) gc.Hint("缺纸");
			}

			@Override
			public void onPrinterSetting(int state) {
				if(state == 0) gc.Hint("持续有纸");
				if(state == 1) gc.Hint("缺纸");
				if(state == 2) gc.Hint("检测到黑标");
			}

			@Override
			public void onFinish() {
				//mPosApi.gpioControl((byte)0x23,2,0);
				//gc.Hint("打印完成");
				HyApp.SendMessage(mHandler, PrinterClass.MESSAGE_PRT_STATE, PrinterClass.STATE_PRT_SUCCESS, 0);
			}

			@Override
			public void onFailed(int state) {
				//mPosApi.gpioControl((byte)0x23,2,0);
				String msg = state == PosApi.ERR_POS_PRINT_NO_PAPER ? "打印缺纸" :
							 state == PosApi.ERR_POS_PRINT_FAILED ? "打印失败" :
							 state == PosApi.ERR_POS_PRINT_VOLTAGE_LOW ? "电压过低" :
							 state == PosApi.ERR_POS_PRINT_VOLTAGE_HIGH ? "电压过高" :
							 "";
				gc.Hint("打印失败， " + msg + "！");
				HyApp.SendMessage(mHandler, PrinterClass.MESSAGE_PRT_STATE, PrinterClass.STATE_PRT_FAIL, 0);
			}
		});
	}

	// 单片机上电
	public static void setSamOn(boolean on) {
		try {
			FileWriter localFileWriterOn = new FileWriter(new File("/proc/gpiocontrol/set_sam"));
			localFileWriterOn.write(on ? "1" : "0");
			localFileWriterOn.close();
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
	}
	
	public static void setHandler(Handler handler) {
		mHandler = handler;
	}
	
	public static void closeDev() {
		if(mPosApi != null) mPosApi.closeDev();
		setSamOn(false);
	}

 	private static OnCommEventListener mCommEventListener = new OnCommEventListener() {

		@Override
		public void onCommState(int cmdFlag, int state, byte[] resp, int respLen) {
			switch(cmdFlag){
			case PosApi.POS_INIT:
				if(state==PosApi.COMM_STATUS_SUCCESS)
					_D.Out ("打印头初始化成功");
				else
					gc.Hint("打印头初始化失败");
				break;
			}
		}
	};
	
	public static byte[] PackData(byte[] cmd, byte[] data) {
		byte[] result = new byte[cmd.length + data.length];
		System.arraycopy(cmd, 0, result, 0, cmd.length);
		System.arraycopy(data, 0, result, cmd.length, data.length);
		return result;
	}

	public static int concentration = 50;
	/*
	 * 打印文字   size 1 --倍大小    2--2倍大小
	 */
	public static void addPrintText(String text, int size){
		try {
			if(mPrintQueue == null) return;
			if(text.isEmpty()) return;
			byte[] data = text.getBytes("GBK");
			if(data == null) return;
			byte[] cmd = size == 1 ? _1x : _2x;
			data = PackData(cmd, data);
			mPrintQueue.addText(concentration, data);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public static byte[] getPrintTextData(String text, int size){
		try {
			byte[] data = text.getBytes("GBK");
			if(data == null) return null;
			byte[] cmd = size == 1 ? _1x : _2x;
			return PackData(cmd, data);
		} catch (Exception e) {
			_D.Out(e);
			return null;
		}
	}
	
	public static void addPrintData(byte[] data){
		try {
			if(mPrintQueue == null) return;
			mPrintQueue.addText(concentration, data);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
	public static void addPrintText(String text){
		addPrintText(text, 1);
	}

	public static void feedingEnd(){
		addPrintText("\n\n\n");
	}
	
	public static void startPrint() {
		try {
			if(mPrintQueue == null) return;
			mPrintQueue.printStart();
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
}
