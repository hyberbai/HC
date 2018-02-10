package com.dev;

import hylib.sys.SysUtils;
import hylib.toolkits.ExProc;
import hylib.toolkits.gcon;
import android.os.Handler;

public class HyScanner {
	public static ZltdDecoder zltdDecoder;

	public enum Mode { Ignore, Enabled, Disable };

	public static Handler mHandler;
    
    protected static void HandleResult(int state, String result) {
    	if(state == gcon.S_TIMEOUT) { // 超时
    		//gc.Hint(MyApp.CurrentActivity(), (String)msg.obj);;
    		return;
    	} else if (state == gcon.S_OK) { 
    	//	LockScanner = true;
    		try {
        		//if(DecoderListener != null) DecoderListener.Handle(this, new ParamList("result", result));

                if(mHandler != null) mHandler.obtainMessage(state, result).sendToTarget();
			} catch (Exception e) {
				ExProc.Show(e);
			}
		 // 	LockScanner = false;
    	}
    }
    
	//---------------------------------------------------------------------
	public static void Init() {
		if(Dev.isPL50()) {
			PL50.Init();
		} else {
			if(zltdDecoder != null) return;
			zltdDecoder = new ZltdDecoder();
			zltdDecoder.OpenDevice();
		}
	}
	
	public static boolean DeviceExists() {
		if(zltdDecoder == null) return false;
		return zltdDecoder.deviceExist;
	}
	
	public static boolean isZltdOpen() {
		return zltdDecoder != null && zltdDecoder.isOpenDevice;
	}
	
	public static void Open() {
		if(Dev.isPL50())
			PL50.Open();
		else {
			Init();
			zltdDecoder.OpenDevice();
		}
	}
	
	public static void Start() {
		if(Dev.isPL50())
			PL50.Open();
		else {
			Init();
			zltdDecoder.start();
		}
	}
	
	public static void Resume() {
		if(Dev.isPL50())
			PL50.Open();
		else {
//			Init();
//			zltdDecoder.resume();
		}
	}
	
	public static void BindingHandler(Handler handler) {
		mHandler = handler;
	}
	
	public static void Pause() {
		if(Dev.isPL50())
			;//PL50.Close();
		else
		//decoder.PauseDevice();
			zltdDecoder.pause();
	}
	
	public static void Close(boolean forceClose) {
		if(Dev.isPL50())
			PL50.Close();
		else
			zltdDecoder.CloseDevice(forceClose);
	}

	public static void SwitchLight() {
		if(zltdDecoder == null) return;
		zltdDecoder.switchLight();
	}

	public static void HandleResult(String result) {
		HandleResult(gcon.S_OK, result);
	}
	
	public static boolean isLightEnabled() {
		if(zltdDecoder == null) return false;
		return zltdDecoder.mLightEnabled;
	}
	
	public static boolean isOpen() {
		if(zltdDecoder == null) return false;
		return zltdDecoder.isOpenDevice;
	}

//    public static boolean isFlashOn(){
//    	Camera cam = Camera.open();
//    	Camera.Parameters p = cam.getParameters();
//    	boolean isOn = p.getFlashMode().equals(Camera.Parameters.FLASH_MODE_ON);
//    	cam.release();
//    	return isOn;
//    }
}
