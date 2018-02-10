package com.dev;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.List;

import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothClass;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.posapi.PosApi;

import com.dev.prt.BtService;
import com.dev.prt.DeviceInfo;
import com.dev.prt.PrinterClass;
import com.dev.util.PrintQueue;
import com.hc.ActBase;
import com.hc.MyApp;
import com.hc.SysData;

import hylib.data.DataRow;
import hylib.data.DataTable;
import hylib.sys.HyApp;
import hylib.sys.LoopMsg;
import hylib.toolkits.ExProc;
import hylib.toolkits._D;
import hylib.toolkits.gc;
import hylib.toolkits.gcon;
import hylib.toolkits.gi;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.ui.dialog.LoadingDialog;
import hylib.ui.dialog.Msgbox;
import hylib.util.ParamList;

public class HyPrt {
	private static PrintQueue mPrintQueue = null;
	private static String currentPrt;

	public static DataList prtDataList;
	
	public enum PageWidthType { _56mm, _80mm };

	public static int MAX_LINECHARS_1X = 32-1;
	public static int MAX_LINECHARS_2X = 16;
	
	private static LoopMsg lm;
	
	private static void StopLoop() {
		if(lm != null) lm.StopLoop();
	}
	
    private static class MyHandler extends Handler {
            
        @Override
        public void handleMessage(Message msg) {
			int state = msg.arg1;
			if(msg.what == gcon.S_FAIL) return; // 无效状态
			if(msg.what == PrinterClass.MESSAGE_STATE_CHANGE) {
				if(state == PrinterClass.SUCCESS_CONNECT) {
					StopLoop();
				} else if(state == PrinterClass.FAILED_CONNECT) {
					currentPrt = "";
					StopLoop();
				} else if(state == PrinterClass.LOSE_CONNECT) {
					currentPrt = "";
				} else {
				} 

			}

			if(msg.what == PrinterClass.MESSAGE_PRT_STATE) {
				if(state == PrinterClass.STATE_PRT_SUCCESS)
					gc.Hint("打印完成！");
				StopLoop();
			}
        }
    }
    
    private static MyHandler prtHandler;
    
	public static void Init(){
		try {
			prtHandler = new MyHandler();
			
			if(Dev.isPL50()) {
		        // PL50自带打印机初始化
			    PL50Prt.Init(); 
			    PL50Prt.setHandler(prtHandler);
			}

			// 蓝牙服务初始化
			BtService.Init();
			BtService.SetHandler(prtHandler);

//			HyPrt.Print(new gi.IFunc<DataList>() {
//
//			    public  DataList Call() {
//					DataList lsData = new DataList();
//					String text ="Test"+ "\n";
//					HyPrt.addTextToListData(lsData, text, 1);
//					return lsData;
//			    }
//			});
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public static String getPrtFullName(String name, String addr) {
		return new DeviceInfo(name, addr, 0).getFullName();
	}
	
	public static String getPrtAddr(String prt) {
		if(prt == null) return "";
		int index = prt.indexOf("@");
		return index > 0 ? prt.substring(index + 1) : ""; 
	}
	
	public static String getPrtName(String prt) {
		int index = prt.indexOf("@");
		return index > 0 ? prt.substring(0, index) : prt; 
	}

	
	public static boolean isPL50Prt() {
		return currentPrt.equals("PL50");
	}
	
	public static PageWidthType getPageWidthType(String prt) {
		String prtName = getPrtName(prt);
		return prtName.indexOf("80") > 0 ? PageWidthType._80mm : PageWidthType._56mm;  
	}
	
	public static boolean isPrt(DeviceInfo di) {
		return di.Type == BluetoothClass.Device.Major.UNCATEGORIZED;
				//&& (di.Name.indexOf("80") > 0 || di.Name.indexOf("58") > 0);
	}
	
	public static List<DeviceInfo> getPrinters() {
		List<DeviceInfo> list = BtService.GetDeviceList();
		final DataTable dt = new DataTable("prts", "name|addr");
		dt.addNewRow("自动识别", "");
		for (int i = list.size() - 1; i >= 0; i --)
			if(!isPrt(list.get(i))) list.remove(i);
		return list;
	}

	public static String TryConnectPrts() {
		List<DeviceInfo> prts = getPrinters();

		for (DeviceInfo di : prts)
			if(di.getFullName().equals(currentPrt)) {
				prts.remove(di);
				prts.add(0, di);
				break;
			}
		for (DeviceInfo di : prts) {
			try {
				ConnectPrt(di.Addr); 
			} catch (Exception e) {
				continue;
			}
			return di.getFullName();
		}
		ExProc.ThrowMsgEx("没有找到可用打印机！");
		return "";
	}
	
	public static void ConnectPrt(String prtAddr) {
		lm = new LoopMsg();
		
		BtService.Connect(prtAddr);
		lm.Loop(10*1000);
		if(BtService.GetState() != PrinterClass.STATE_CONNECTED) 
			ExProc.ThrowMsgEx("连接打印机失败！");;
	}
	
	public static void SetPrinter(String prt) {
		if(currentPrt != null && currentPrt.equals(prt)) return;
		String prtAddr = getPrtAddr(prt);

		LoadingDialog.Show("正在连接打印机...");
		if(prtAddr.isEmpty()) {
			prt = Dev.isPL50() ? "PL50" : TryConnectPrts();
			prtAddr = getPrtAddr(prt);
		}
		else 
			ConnectPrt(prtAddr);

		LoadingDialog.Hide();
		currentPrt = prt;
		PageWidthType widType = getPageWidthType(prt);
		MAX_LINECHARS_1X = widType == PageWidthType._80mm ? 48 : 32;
		MAX_LINECHARS_2X = MAX_LINECHARS_1X/2;
		MAX_LINECHARS_1X--;
	}
	
	public static String getSepText(){
		return gs.nChar('-', MAX_LINECHARS_1X);
	}

	public static String getSepLine(){
		return getSepText() + "\n";
	}

	public static String getCenterText(String text, int size){
		int maxLineChars = getMaxLineChars(size);
		int len = gs.getTextLenA(text);
		int n = len < maxLineChars ? gv.Half(maxLineChars - len) : 0; 
		text = gs.nChar(' ', n) + text + "\n";
		return text;
	}
//	public static void addSepLine() {
//		addPrintText(getSepLine());
//	}


	public static String getFeedingEnd(){
		return "\n\n";
	}

	public static int getMaxLineChars(int size) {
		return size == 1 ? MAX_LINECHARS_1X : MAX_LINECHARS_2X;
	}

	public static void addCenterText(String text, int size){
	}

	public static byte[] getText(String textStr) {
		byte[] send=null;
		try {
			send = textStr.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			send = textStr.getBytes();
		}
		return send;
	}
	
	public static byte[] getBtPrintTextData(String text, int size){
		byte[] buffer = getText(text);

		byte[] cmd = size == 1 ? PrinterClass.CMD_FONTSIZE_NORMAL : PrinterClass.CMD_FONTSIZE_DOUBLE;
		byte[] result = new byte[cmd.length + buffer.length];
		System.arraycopy(cmd, 0, result, 0, cmd.length);
		System.arraycopy(buffer, 0, result, cmd.length, buffer.length);
		return result;
	}
	
	public static void addTextToListData(DataList lsData, String text, int size) {
		if(text.isEmpty()) return;
		if(isPL50Prt())
			lsData.add(PL50Prt.getPrintTextData(text, size));
		else
			lsData.add(getBtPrintTextData(text, size));
	}
	
	public static void Print(gi.IFunc<DataList> getDataFunc) {
		try {
			SetPrinter(SysData.Printer);
			LoadingDialog.Show("正在打印...");
			
			DataList lsData = getDataFunc.Call();
			
			byte[] data = lsData.toBytes();
			if(isPL50Prt()) {
				PL50Prt.addPrintData(data);
				PL50Prt.startPrint();
			} else {
				BtService.PrintData(data);
			}
			
			if(lm == null) lm = new LoopMsg();
			lm.Loop(60*1000);
		} catch (Exception e) {
			ExProc.Show(e);
		}
		finally {
			LoadingDialog.ImmeClose();
		}
	}
	/*
	 * 打印文字   size 1 --倍大小    2--2倍大小
	 */
	public static void addPrintText(String text, int size){
		try {
			if(mPrintQueue == null) return;
			if(text.isEmpty()) return;
			byte[] data = text.getBytes("GBK");
			if(data == null) return;
//			byte[] cmd = size == 1 ? _1x : _2x;
//			data = PackData(cmd, data);
			mPrintQueue.addText(0, data);
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
