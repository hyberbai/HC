package com.dev;

import java.lang.reflect.Array;

import com.hc.scanner.Utils;
import com.zltd.decoder.Constants;
import com.zltd.decoder.DecoderManager;

import hylib.sys.HyApp;
import hylib.toolkits.ArrayTools;
import hylib.toolkits.ExProc;
import hylib.toolkits._D;
import hylib.toolkits.gcon;
import hylib.toolkits.gs;
import android.R.bool;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanDevice;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.content.ContextWrapper;

public class PL50 {
	private final static String SCAN_ACTION = "scan.rcv.message";
	private static ScanDevice sm;

	

    private static String lastResult;
    private static int sameCount;
	
    private static BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            byte[] data = intent.getByteArrayExtra("barocode");
            int len = intent.getIntExtra("length", 0);
            
            String result = gs.guessEncoding(ArrayTools.Copy(data, 0, len));
            if(result.equals(lastResult) && sameCount++ < 5) return;
            
//            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            HyScanner.HandleResult(result);
            sm.stopScan();
            sameCount = 0;
            lastResult = result;
        }

    };
    
    protected static boolean Init(){
    	try {
    		if(sm == null) sm = new ScanDevice();

    		RegScanReceiver();
			Close();
    		return true;
		} catch (Exception e) {
			_D.Out("PL50扫描头初始化失败：" + ExProc.GetErrMsg(e));
			return false;
		}
    }

	protected static void Open() {
		try {
			if(sm == null) return;
			sm.setOutScanMode(0); 
			sm.setScanLaserMode(4);
			sm.openScan();

//			sm.setScanLaserMode(4);
//			sm.setScanLaserMode(8);
			
		} catch (Exception e) {
			_D.Out("Open PL50 Failed!");
		}
	}

	protected static void Close() {
		if(sm == null) return;
		sm.stopScan();
		sm.closeScan();
	}
    
    public static void RegScanReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        HyApp.getAppContext().registerReceiver(mScanReceiver, filter);
    }
}
