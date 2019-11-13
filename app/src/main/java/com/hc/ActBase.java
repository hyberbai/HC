package com.hc;


import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.dev.BarcodeScannerBLE;
import com.dev.HyScanner;
import com.zxing.ActCapture;

import java.lang.ref.WeakReference;

import hylib.data.DataRowCollection;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.gcon;
import hylib.toolkits.type;
import hylib.ui.dialog.Msgbox;
import hylib.ui.dialog.PopupWindowEx;
import hylib.ui.dialog.UICreator;
import hylib.util.ParamList;
import hylib.view.ActivityEx;

public class ActBase extends ActivityEx {
	public PopupWindowEx pwOptions;
    protected HyScanner.Mode DecoderMode;
	protected ViewGroup mToolbar;
	protected boolean isEditMode;

	public final static int REQ_CODE_LOGIN = 10;
    public final static int REQ_CODE_BARCODE = 20;
	public final static int REQ_CODE_EDIT = 50;
	public final static int REQ_CODE_REPLACE = 60;
	public final static int REQ_CODE_SEARCH = 100;
	public final static int REQ_CODE_PRODUCT_REPACE = 60;

	public final static int REQ_CODE_PRINTER = 200;
	
    private static class MyHandler extends Handler {
        private final WeakReference<ActBase> mActivity;
        
        public MyHandler(ActBase activity) {
          mActivity = new WeakReference<ActBase>(activity);
        }
            
        @Override
        public void handleMessage(Message msg) {
        	ActBase activity = mActivity.get();
        	if (activity == null) return;
			if(msg.what == gcon.S_FAIL) return; // 无效状态
			if(msg.what == gcon.S_OK) activity.DecoderResult((String)msg.obj);
        }
    }

	public void CreateEditBar(String items) {
		try {
			ParamList pl = new ParamList();
			pl.put("ACL", ACL);
			mToolbar = UICreator.CreateToolBar(context, items, pl);
			mToolbar.setVisibility(View.GONE);
			getRootLayout().addView(mToolbar);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
	private final Handler mHandler = new MyHandler(this);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		MyApp.CheckEmptyInit();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Android M Permission check
			if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
			}
		}

		BarcodeScannerBLE.Init();
//		BarcodeScannerBLE.setListener((ble, barcode) -> {
//			HandleSNo(barcode);
//		});
		BarcodeScannerBLE.setListener(
			new BarcodeScannerBLE.OnReceiveDataListener() {
				  @Override
				  public void OnReceive(BluetoothDevice device, String barcode) {
						  HandleSNo(barcode);
				  }
			}
		);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(DecoderMode == HyScanner.Mode.Ignore) return;
       // setDecoderEnabled(DecoderMode == Scanner.Mode.Enabled);
        HyScanner.Resume(); 
        HyScanner.BindingHandler(mHandler);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if(DecoderMode == HyScanner.Mode.Enabled)
        	HyScanner.Pause();
    }
    
    @Override
    public void onStop() {
        if(DecoderMode == HyScanner.Mode.Enabled) 
        	HyScanner.Pause();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if(DecoderMode == HyScanner.Mode.Enabled)
        	HyScanner.Close(true);

		BarcodeScannerBLE.Destory();
        super.onStop();
    }
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //return false;
        } if (keyCode == KeyEvent.KEYCODE_BUTTON_A) {
        	if(DecoderMode == HyScanner.Mode.Ignore) return true;
        	if(HyScanner.isOpen()) 
        		HyScanner.Close(false);
        	else
				HyScanner.Start();
        	//return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void OpenDecoder() {
    	 if(DecoderMode == HyScanner.Mode.Enabled)
    		 HyScanner.Start();
    	 //Scanner.Open();
	}

    public void setDecoderEnabled(boolean enabled) {
        if(DecoderMode == HyScanner.Mode.Ignore) return;
    	DecoderMode = enabled ? HyScanner.Mode.Enabled : HyScanner.Mode.Disable;
    	if(DecoderMode == HyScanner.Mode.Enabled) OpenDecoder(); else HyScanner.Pause();//.Close();
    }
    
    public boolean getDecoderEnabled() {
    	return DecoderMode == HyScanner.Mode.Enabled;
    }
    
    public synchronized void DecoderResult(String result){
    }

	public void ActFlashLight() {
		HyScanner.SwitchLight();
	}

	public void ActOpenDecoder() {
        if(DecoderMode == HyScanner.Mode.Ignore) return;
     	setDecoderEnabled(!(DecoderMode == HyScanner.Mode.Enabled));
	}

	public void ActScanBLE() {
        BarcodeScannerBLE.Start();
	}

	public void ActScanQR() {
		g.Vibrate(this, 80);
		Intent intent = new Intent(this, ActCapture.class);
		startActivityForResult(intent, REQ_CODE_BARCODE); 
	}

	public void ActHandInput() {
		try {
			ParamList pl = new ParamList();
			pl.set("input_type", "num");
			pl.set("loop", false);
			pl.set("input", new EventHandleListener() {
				@Override
				public void Handle(Object sender, ParamList arg) throws Exception {
					HandleSNo(pu.getSNo(arg.SValue("value")));
				}
			});
			Msgbox.Input(this, "请输入流水号：", pl);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case R.id.action_settings:
			if (resultCode == RESULT_OK) {
			}
			break;
		case REQ_CODE_BARCODE:
			if (resultCode == RESULT_OK) {
			    Bundle bundle = data.getExtras(); 
			    String barcode = bundle.getString("barcode");
			    HandleSNo(barcode);
				break;
			}			
		}
	}

	protected void setOptionsMenuParams(ParamList pl) throws Exception {
	}

	protected void CreateOptionsPopupMenu() {
		if(pwOptions != null) return;
		try {
			ParamList pl = new ParamList();
			setOptionsMenuParams(pl);
			pl.set("BeforePopup", new EventHandleListener() {
				@Override
				public void Handle(Object sender, ParamList arg) throws Exception {
					beforeShowOptionsMenu(type.as(sender, PopupWindowEx.class));
				}
			});
			
			pwOptions = UICreator.CreatePopupMenu(this, pl);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
    public void HandleSNo(String barcode) {
    }

	public void beforeShowOptionsMenu(PopupWindowEx pw){
		pw.setItemText("FlashLight", (HyScanner.isLightEnabled() ? "关闭" : "开启") + "照明");
		pw.setItemText("OpenDecoder", (HyScanner.isOpen() ? "关闭" : "开启") + "读头");
	}

	public void AppendDefaultMenu(DataRowCollection rows){
		if(HyScanner.DeviceExists())
			rows.SetRowValues(new Object[][] {
					new Object[] { "FlashLight", "照明", null },
					new Object[] { "OpenDecoder", "读头", null },
				});
		else
			rows.SetRowValues(new Object[][] {
					new Object[] { "ScanQR", "扫二维码", null },
					new Object[] { "ScanBLE", "蓝牙扫码", null },
				});
	}

	public void onOptionsClick(View v) {
		if(pwOptions == null) return;
		pwOptions.Popup((View)$(R.id.ib_options));	
	}
}
