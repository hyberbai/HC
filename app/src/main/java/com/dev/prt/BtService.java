package com.dev.prt;

import hylib.sys.HyApp;
import hylib.toolkits.ArrayTools;
import hylib.toolkits._D;
import hylib.toolkits.gc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.R.bool;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.dev.prt.BlueToothService;
import com.dev.prt.DeviceInfo;
import com.dev.prt.PrintService;
import com.dev.prt.PrinterClass;

public class BtService extends PrintService implements PrinterClass {

	Context context;
	public static BlueToothService mBTService = null;

	public BtService(Context _context) {
		context = _context;
		mBTService = new BlueToothService(context);
	}

	@Override
	public void setHandler(Handler handler) {
		mBTService.setHandler(handler);
	}
	
	@Override
	public boolean open() {
		if (isOpen()) return true; 
		try {
			mBTService.OpenDevice();
			return true;
		} catch (Exception e) {
			_D.Out(e);
			return false;
		}
	}

	@Override
	public boolean close() {
		mBTService.CloseDevice();
		return false;
	}

	@Override
	public void scan() {
		if (!mBTService.IsOpen()) {
			mBTService.OpenDevice();
			return;
		}
		if (mBTService.getState() == STATE_SCANING)
			return;

		new Thread() {
			public void run() {
				mBTService.ScanDevice();
			}
		}.start();
	}

	@Override
	public boolean connect(String device) {
		if (mBTService.getState() == STATE_SCANING) stopScan();
		if (mBTService.getState() == STATE_CONNECTING) return false;
		if(mBTService.getState() == STATE_CONNECTED) mBTService.DisConnected();
		mBTService.ConnectToDevice(device);
		return true;
	}

	@Override
	public boolean disconnect() {
		mBTService.DisConnected();
		return true;
	}

	@Override
	public int getState() {
		return mBTService.getState();
	}

	@Override
	public boolean write(byte[] bt) {
		if(getState()!= PrinterClass.STATE_CONNECTED)
		{
			gc.Hint("连接失败！");
			return false;
		}
		try {
			mBTService.write(bt);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean printText(String textStr) {
		byte[] buffer = getText(textStr);

		if (buffer.length <= 100) {
			return write(buffer);
		}
		int sendSize = 100;
		for (int j = 0; j < buffer.length; j += sendSize) {

			byte[] btPackage = new byte[sendSize];
			if (buffer.length - j < sendSize) {
				btPackage = new byte[buffer.length - j];
			}
			System.arraycopy(buffer, j, btPackage, 0, btPackage.length);
			boolean ok = write(btPackage);
			if(!ok) return false;
			
			try {
				Thread.sleep(86);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return true;
		////////return write(getText(textStr));
	}
	
	public boolean printData(byte[] data) {
		if(getState()!= PrinterClass.STATE_CONNECTED)
		{
			gc.Hint("连接失败！");
			return false;
		}
		mBTService.printData(data);
		return true;
	}

	@Override
	public boolean printImage(Bitmap bitmap) {
		return write(getImage(bitmap));
		// return write(new byte[]{0x0a});
	}

	@Override
	public boolean printUnicode(String textStr) {
		return write(getTextUnicode(textStr));
	}

	@Override
	public boolean isOpen() {
		return mBTService.IsOpen();
	}

	@Override
	public void stopScan() {
		if (getState() == PrinterClass.STATE_SCANING) {
			mBTService.StopScan();
			mBTService.setState(PrinterClass.STATE_SCAN_STOP);
		}
	}

	@Override
	public void setState(int state) {
		mBTService.setState(state);
	}

	@Override
	public List<DeviceInfo> getDeviceList() {
		List<DeviceInfo> devList = new ArrayList<DeviceInfo>();
		Set<BluetoothDevice> devices = mBTService.GetBondedDevice();
		for (BluetoothDevice bd : devices) {
			int dcid = bd.getBluetoothClass().getMajorDeviceClass();
			devList.add(new DeviceInfo(bd.getName(), bd.getAddress(), dcid));
		}
		return devList;
	}
	

	private static PrinterClass mInstance = null;// 打印机操作类

	public static void Init() {
		if(mInstance == null) mInstance = new BtService(HyApp.getAppContext());
		Open();
	}
	
	public static void SetHandler(Handler handler) {
		if(mInstance == null) return;
		mInstance.setHandler(handler);
	}
	
	public static boolean IsOpen() {
		if(mInstance == null) return false;
		return mInstance.isOpen();
	}
	
	public static boolean Open() {
		if(mInstance == null) return false;
		return mInstance.open();
	}
	
	public static void Close() {
		if(mInstance == null) return;
		mInstance.close();
	}
	
	public static void Scan() {
		if(mInstance == null) return;
		mInstance.scan();
	}

	public static List<DeviceInfo> GetDeviceList() {
		if(mInstance == null) return null;
		return mInstance.getDeviceList();
	}

	public static int GetState() {
		if(mInstance == null) return 0;
		return mInstance.getState();
	}

	public static void StopScan() {
		if(mInstance == null) return;
		mInstance.stopScan();
	}

	public static void Connect(String address) {
		if(mInstance == null) return;
		mInstance.connect(address);
	}

	public static void Disonnect() {
		if(mInstance == null) return;
		mInstance.disconnect();
	}

	public static void PrintText(String text) {
		if(mInstance == null) return;
		mInstance.printText(text);
	}

	public static void PrintData(byte[] data) {
		if(mInstance == null) return;
		mInstance.printData(data);
	}
}
