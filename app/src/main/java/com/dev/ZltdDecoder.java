package com.dev;

import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits._D;
import hylib.toolkits.gc;
import hylib.toolkits.gcon;
import android.content.Context;
import android.os.Handler;

import com.google.zxing.aztec.decoder.Decoder;
import com.hc.MyApp;
import com.hc.scanner.Utils;
import com.zltd.decoder.Constants;
import com.zltd.decoder.DecoderManager;

public class ZltdDecoder implements DecoderManager.IDecoderStatusListener {

    private boolean isScanTimeOut = false;
    protected boolean isOpenDevice = false;
    protected boolean deviceExist = true;
    protected static boolean scanRing = true;
    DecoderManager mDecoderMgr = null;

    protected int scanCase = 0;
    protected Utils mUtils;
    protected static boolean LockScanner;
    public EventHandleListener DecoderListener;
    
    protected ZltdDecoder() {
    	try {
    		mDecoderMgr = DecoderManager.getInstance();
		} catch (Exception e) {
			ExProc.Show(e);
		}
    }

    protected boolean isScanTimeOut(){
        return isScanTimeOut;
    }

    public void PauseDevice() {
        if(mUtils != null) mUtils.release();
        CloseDevice(false);
    }

    public int getScanMode() {
    	return mDecoderMgr == null ? -1 : mDecoderMgr.getScanMode();
    }

    // ScanMode
    public void OpenDevice(){
    	if(isOpenDevice) return;
    	try {
        	Context context = MyApp.CurrentActivity();
            mUtils = Utils.getInstance();
            mUtils.init(context);
            scanCase = 0;
            
            mDecoderMgr.setScanMode(Constants.CONTINUOUS_SHOOT_MODE);
			deviceExist = true;
//            _D.Out("OpenDevice");
//            start();
		} catch (Exception e) {
			deviceExist = false;
			_D.Out(e);
		}
    }

    // ScanMode
    public void checkDevice(){
    	try {
        	Context context = MyApp.CurrentActivity();
            mUtils = Utils.getInstance();
            mUtils.init(context);
			deviceExist = true;
		} catch (Exception e) {
			deviceExist = false;
		}
    }

    public void start(){
    	if(!deviceExist) return;
    	if(isOpenDevice) return;
        int res = mDecoderMgr.connectDecoderSRV();
        if(res == Constants.RETURN_CAMERA_CONN_ERR){
            gc.Hint("读头连接错误！");
            return;
        }
        mDecoderMgr.addDecoderStatusListener(this);
        //enableLight(mLightEnabled);
        setLightEnabled(true);
        isOpenDevice = true;
        _D.Out("start decoder");
    }
    
    public void resume(){
    	if(!isOpenDevice) start();
//		if(getScanMode() == Constants.CONTINUOUS_SHOOT_MODE)
//			gu.sendKey(KeyEvent.KEYCODE_BUTTON_A);
        _D.Out("resume: ");
    }
    
    public void pause() {
    	if(!deviceExist) return;
    	if(!isOpenDevice) return;
        if (mDecoderMgr == null) return;
        setLightEnabled(false);
        
        mDecoderMgr.removeDecoderStatusListener(this);
        mDecoderMgr.stopDecode();
        mDecoderMgr.setFlashMode(Constants.FLASH_ALWAYS_OFF_MODE);
        mDecoderMgr.enableLight(Constants.LOCATION_LIGHT, false);
       // mDecoderMgr.setScannerEnable(false);
        isOpenDevice = false;
        _D.Out("pause decoder");
    }
    
    public void setLightEnabled(boolean enabled){
    	if(!deviceExist) return;
    	mLightEnabled = enabled;
    	if(mDecoderMgr != null)
    		mDecoderMgr.setFlashMode(enabled ? Constants.FLASH_ALWAYS_ON_MODE : Constants.FLASH_ALWAYS_OFF_MODE);
    	enableLight(enabled);
    }

    protected boolean mLightEnabled = true;
	public void switchLight() {
		setLightEnabled(!mLightEnabled);
	}
    
    public void enableLight(boolean enabled){
    	if(!deviceExist) return;
        mDecoderMgr.enableLight(Constants.FLASH_LIGHT, enabled);
        mDecoderMgr.enableLight(Constants.FLOOD_LIGHT, enabled);
        _D.Out("enableLight: " + enabled);
    }
    
    public void CloseDevice(boolean forceClose){
    	if(!deviceExist) return;
        if (mDecoderMgr == null) return;
    	if(!isOpenDevice && !forceClose) return;
        setLightEnabled(false);
        mDecoderMgr.enableLight(Constants.LOCATION_LIGHT, false);
        
        mDecoderMgr.removeDecoderStatusListener(this);
        mDecoderMgr.stopDecode();
        mDecoderMgr.disconnectDecoderSRV();
        isOpenDevice = false;
        _D.Out("CloseDevice");
    }

    private String lastResult;
    private int sameCount;
    @Override
    public void onDecoderResultChanage(String result, String time) {
    	if(LockScanner) return;
        isScanTimeOut = false;
        if(result.equals(DecoderManager.DECODER_TIMEOUT)){
            isScanTimeOut = true;
            //mUtils.playSound(Utils.SOUND_TYPE_BEEP);
            HyScanner.HandleResult(gcon.S_TIMEOUT, result);
        }
        else{
            isScanTimeOut = false;
            if(getScanMode() == Constants.CONTINUOUS_SHOOT_MODE &&
        	  result.equals(lastResult) && sameCount++ < 10) return;

            sameCount = 0;
            mUtils.playSound(Utils.SOUND_TYPE_SUCCESS);
            
            HyScanner.HandleResult(gcon.S_OK, result);
        	lastResult = result;
        }
    }
    
    @Override
    public void onDecoderStatusChanage(int state) {
    	_D.Out("onDecoderStatusChanage:" + state);
    }

	
	public void ActFlashLight() {
		setLightEnabled(!mLightEnabled);
	}
}
