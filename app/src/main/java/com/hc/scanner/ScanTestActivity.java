
package com.hc.scanner;
import java.lang.Override;
import java.lang.Runnable;
import java.lang.String;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.HashMap;

import com.zltd.decoder.Constants;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.view.KeyEvent;
import com.hc.R;

public class ScanTestActivity extends TestBaseActivity implements OnCheckedChangeListener{
    private static final String TAG = "ScanTestActivity";
    private ListView mResultListView;
    private TextView mScanTotalTextView;

    protected ArrayList<HashMap<String, String>> mBarcodeList = new ArrayList<HashMap<String, String>>();
    protected SimpleAdapter mListAdaper;
    private int ScanTotalNum = 0;

    private static final int STARTCONTINUESHOOT = 1;
    private static final int STOPCONTINUESHOOT = 2;
    //private int scanMode = -1;
    private int scanMode = Constants.SINGLE_SHOOT_MODE;;

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    HashMap<String, String> result = (HashMap<String, String>) msg.obj;
                    mBarcodeList.add(0, result);
                    mListAdaper.notifyDataSetChanged();
                    break;
                case 1:
                   // Toast.makeText(ScanTestActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
                    //enableSaveFile = true;
                    break;
                case 2:
                   // Toast.makeText(ScanTestActivity.this, R.string.save_fail, Toast.LENGTH_SHORT).show();
                  //  enableSaveFile = true;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate ..");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_test);
        mResultListView = (ListView) findViewById(R.id.result_listView);

        mListAdaper = new SimpleAdapter(this, mBarcodeList, R.layout.list_item1, new String[] {
                "decodeTime", "decodeResult"
        }, new int[] {
                R.id.text1, R.id.text2
        });
        mResultListView.setAdapter(mListAdaper);
        //mDecoderMgr.addDecoderStatusListener(this);
    }

    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        scanRing = getScanRingEnable();
        initUI();
    }

    private void initUI() {
    }

    public void onPause() {
        Log.d(TAG, "onPause");
        if (scanCase == STARTCONTINUESHOOT) {
            scanCase = STOPCONTINUESHOOT;
            //mDecoderMgr.stopContinuousShoot();
        }
        super.onPause();

        // mScanLightController.enableFlashLight(false);
        // mScanLightController.enableFloodLight(false);
        // mScanLightController.enableLocationLight(false);
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (mDecoderMgr != null) {
            mDecoderMgr.enableLight(Constants.FLASH_LIGHT, false);
            mDecoderMgr.enableLight(Constants.FLOOD_LIGHT, false);
            mDecoderMgr.enableLight(Constants.LOCATION_LIGHT, false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
        case 0:
            Intent intent = new Intent();
            ComponentName name = new ComponentName("com.zltd.decoder", "com.zltd.decoder.ScannerSettings");
            intent.setComponent(name);
            startActivity(intent);
            return true;

        default:
            break;
        }
        return false;
    }


    public void singleShootOnClick(View view) {
        //mDecoderMgr.enableLight(Constants.FLASH_LIGHT, true);
        //mDecoderMgr.enableLight(Constants.FLOOD_LIGHT, true);
        mDecoderMgr.singleShoot();
    }

    public void continuousShootOnClick(View view) {
        Log.d(TAG, "continuousShootOnClick");
//        if (mContinousScanButton.isChecked()) {
//            ScanTotalNum = 0;
//            scanCase = STARTCONTINUESHOOT;
//            mDecoderMgr.continuousShoot();
//        } else {
//            scanCase = STOPCONTINUESHOOT;
//            mDecoderMgr.stopContinuousShoot();
//        }
    }

    public void exitOnClick(View view) {
        onBackPressed();
    }

    public void clearOnClick(View view) {
        mBarcodeList.clear();
        mListAdaper.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown keyCode = " + keyCode + " repeatCount = " + event.getRepeatCount());
        switch (keyCode) {
        case KeyEvent.KEYCODE_BUTTON_A:
            if(isOnResume){
                // Synchronous the state of scan key and the mContinousScanButton when CONTINUOUS_SHOOT_MODE
//                if (scanMode == Constants.CONTINUOUS_SHOOT_MODE && event.getRepeatCount() == 0) {
//                    if(mContinousScanButton.isChecked()){
//                        scanCase = STOPCONTINUESHOOT;
//                        mContinousScanButton.setChecked(false);
//                    }else{
//                        if(scanCase != STARTCONTINUESHOOT){
//                            ScanTotalNum = 0;
//                        }
//                        scanCase = STARTCONTINUESHOOT;
//                        mContinousScanButton.setChecked(true);
//                    }
//                    mContinousScanButton.setEnabled(false);
//                }else if(scanMode == Constants.SINGLE_SHOOT_MODE){
//                    mSingleScanButton.setEnabled(false);
//                }
                mDecoderMgr.dispatchScanKeyEvent(event);
            }
            return true;
        default:
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyUp keyCode=" + keyCode);
        switch (keyCode) {
        case KeyEvent.KEYCODE_BUTTON_A:
            if(isOnResume){
//                if(scanMode == Constants.CONTINUOUS_SHOOT_MODE){
//                    mContinousScanButton.setEnabled(true);
//                }else if(scanMode == Constants.SINGLE_SHOOT_MODE){
//                    mSingleScanButton.setEnabled(true);
//                }
                mDecoderMgr.dispatchScanKeyEvent(event);
            }
            return true;
        default:
            return super.onKeyUp(keyCode, event);
        }
    }

//    @Override
//    public void onCheckedChanged(CompoundButton checkBox, boolean enable) {
//        switch (checkBox.getId()) {
//            case R.id.white_light_checkBox:
//                mDecoderMgr.enableLight(Constants.FLASH_LIGHT, enable);
//                break;
//            case R.id.red_light_checkBox:
//                mDecoderMgr.enableLight(Constants.FLOOD_LIGHT, enable);
//                break;
//            case R.id.location_light_checkBox:
//                mDecoderMgr.enableLight(Constants.LOCATION_LIGHT, enable);
//                break;
//            case R.id.scan_ring_checkBox:
//                setScanRingEnable(enable);
//                scanRing = enable;
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    public void onCheckedChanged(CompoundButton checkBox, boolean enable) {
        switch (checkBox.getId()) {
//            case R.id.white_light_checkBox:
//                mDecoderMgr.enableLight(Constants.FLASH_LIGHT, enable);
//                break;
//            case R.id.red_light_checkBox:
//                mDecoderMgr.enableLight(Constants.FLOOD_LIGHT, enable);
//                break;
//            case R.id.location_light_checkBox:
//                mDecoderMgr.enableLight(Constants.LOCATION_LIGHT, enable);
//                break;
//            case R.id.scan_ring_checkBox:
//                setScanRingEnable(enable);
//                scanRing = enable;
//                break;
            default:
                break;
        }
    }
    
    @Override
    public void onDecoderResultChanage(String result, String time) {
        super.onDecoderResultChanage(result, time);
        if(isOnResume){
            Log.d(TAG, "onDecoderResultChanage decodeTime=" + time
                    + " decodeResult " + result);
            HashMap<String, String> hResult = new HashMap<String, String>();
            hResult.put("decodeTime", time);
            hResult.put("decodeResult", result);
            switch (scanMode) {
            case Constants.SINGLE_SHOOT_MODE:
                mHandler.obtainMessage(0, hResult).sendToTarget();
                break;
            case Constants.CONTINUOUS_SHOOT_MODE:
                if(scanCase == STARTCONTINUESHOOT){
                    ScanTotalNum++;
                    mHandler.obtainMessage(0, hResult).sendToTarget();
                }
                break;
            case Constants.HOLD_SHOOT_MODE:
                if(!isScanTimeOut())
                {
                    mHandler.obtainMessage(0, hResult).sendToTarget();
                }
                break;
            default:
                break;
            }
        }
    }
}
