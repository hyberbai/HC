package com.dev;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hylib.sys.HyApp;
import hylib.toolkits._D;
import hylib.toolkits.gc;

public class BarcodeScannerBLE {
    public UUID UUID_Barcode_Service = UUID.fromString("0000FEEA-0000-1000-8000-00805F9B34FB");
    public UUID UUID_Barcode_Characteristic = UUID.fromString("00002AA1-0000-1000-8000-00805F9B34FB");

    public List<BleDevice> devices;
    private OnReceiveDataListener receiveDataListener;

    public interface OnReceiveDataListener {
        public void OnReceive(BluetoothDevice device, String barcode);
    }

    public void listenReceiveData(BluetoothDevice device, String barcode)
    {
        if(receiveDataListener == null) return;
        receiveDataListener.OnReceive(device, barcode);

     //   gc.HintTd(barcode);
    }

    public void setOnReceiveDataListener(OnReceiveDataListener listener)
    {
        receiveDataListener = listener;
    }

    public void init(){
        devices = new ArrayList<BleDevice>();

        BleManager.getInstance().init(HyApp.getInstance());
//        BleManager.getInstance()
//                .enableLog(true)
//                .setReConnectCount(1, 5000)
//                .setConnectOverTime(20000)
//                .setOperateTimeout(50000);
//        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(new UUID[] { UUID_Barcode_Service })      // 只扫描指定的服务的设备，可选
//               // .setDeviceMac(mac)                // 只扫描指定mac的设备，可选
//                .setAutoConnect(true)               // 连接时的autoConnect参数，可选，默认false
//                .setScanTimeOut(5000)             // 扫描超时时间，可选，默认10秒
//                .build();
//        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    public void destroy() {
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }

    public void scanDevices(){
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                String name = bleDevice.getName();
               // _D.Out(name);
                if(name == null || name.indexOf("BarCode") < 0) return;
                devices.add(bleDevice);
                connectDevice(bleDevice);
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList)
            {
              //  connect();
            }
        });
    }

    public void connect()
    {
        if(devices.size() == 0)
        {
            gc.HintTd("未发现蓝牙扫描设备！");
            return;
        }
//        devices.forEach(dev -> {
//            connectDevice(dev);
//        });
    }

    public void openDevice(BleDevice bleDevice, BluetoothGatt gatt)
    {
        //BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
        BluetoothGattService svrBarcode = null;
        for (BluetoothGattService service : gatt.getServices())
            if(service.getUuid().equals(UUID_Barcode_Service))
            {
                svrBarcode = service;
                break;
            }
        if(svrBarcode == null) return;

        BluetoothGattCharacteristic chaBarcode = null;
        for (BluetoothGattCharacteristic characteristic : svrBarcode.getCharacteristics())
            if(characteristic.getUuid().equals(UUID_Barcode_Characteristic))
            {
                chaBarcode = characteristic;
                break;
            }

        if(chaBarcode == null) return;

        int prop = chaBarcode.getProperties();

        if ((prop & BluetoothGattCharacteristic.PROPERTY_NOTIFY) == 0) return;

        final BluetoothGattCharacteristic  cha = chaBarcode;

        BleManager.getInstance().notify(
            bleDevice,
            svrBarcode.getUuid().toString(),
            chaBarcode.getUuid().toString(),
            new BleNotifyCallback() {

                @Override
                public void onNotifySuccess() {
                  //  gc.HintTd("OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK");
                }

                @Override
                public void onNotifyFailure(final BleException exception) {
                }

                @Override
                public void onCharacteristicChanged(byte[] data) {
                    procData(data);
                }
            }
         );
    }

    private byte[] mData;
    private void procData(byte[] data)
    {
        try {
            if(mData == null)
                mData = data;
            else {
                byte[] result = new byte[mData.length + data.length];
                System.arraycopy(mData, 0, result, 0, mData.length);
                System.arraycopy(data, 0, result, mData.length, data.length);
                mData = result;
            }
            //mData = ArrayTools.Merge(mData, data);
         //   String barcode = new String(mData, "UTF-8");
            String barcode = new String(mData, "GB2312");
            Matcher matcher = Pattern.compile("[A-Zo]{1,4}\\.([\\d]{6,12})").matcher(barcode);

            String SNo = "";
            if(matcher.find()) // 扫码流水号
            {
                SNo = matcher.group(1);
                _D.Out("BLE 读取条码：\n" + barcode);
                listenReceiveData(null, barcode);
                mData = null;
            }
        } catch (Exception e) {
        }
    }

    public boolean EncodeCharset(String[] ss, String charsetSrc, String charsetDst) {
        if (Charset.forName(charsetSrc).newEncoder().canEncode(ss[0])) {
            try {
                ss[0] = new String(ss[0].getBytes(charsetSrc), charsetDst);
            } catch(Exception e) {
                return false;
            }
            return true;
        } else
            return false;
    }


//    public boolean EncodeCharset(byte[] data, String charsetSrc, String charsetDst) {
//        if (Charset.forName(charsetSrc).newEncoder().canEncode(ss[0])) {
//            try {
//                ss[0] = new String(ss[0].getBytes(charsetSrc), charsetDst);
//            } catch(Exception e) {
//                return false;
//            }
//            return true;
//        } else
//            return false;
//    }

    private String recode(String str) {
        String[] ss = new String[] { str };

        if (EncodeCharset(ss, "ISO-8859-1", "GB2312"))
            return ss[0];
        else if (EncodeCharset(ss, "UTF-8", "UTF-8"))
            return ss[0];
        else
            return str;
    }

    private void connectDevice(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
           //    progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                gc.HintTd("连接蓝牙扫码失败！\n" + bleDevice.getName() + " " + bleDevice.getKey());
//                img_loading.clearAnimation();
//                img_loading.setVisibility(View.INVISIBLE);
//                btn_scan.setText(getString(R.string.start_scan));
//                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
//                progressDialog.dismiss();
//                mDeviceAdapter.addDevice(bleDevice);
//                mDeviceAdapter.notifyDataSetChanged();
                gc.HintTd("连接蓝牙扫描扫码完成！\n" + bleDevice.getName() + " " + bleDevice.getKey());
                openDevice(bleDevice, gatt);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                gc.HintTd("蓝牙扫描设备连接已断开！\n" + bleDevice.getName() + " " + bleDevice.getKey());
//                progressDialog.dismiss();
//
//                mDeviceAdapter.removeDevice(bleDevice);
//                mDeviceAdapter.notifyDataSetChanged();
//
//                if (isActiveDisConnected) {
//                    Toast.makeText(MainActivity.this, getString(R.string.active_disconnected), Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(MainActivity.this, getString(R.string.disconnected), Toast.LENGTH_LONG).show();
//                    ObserverManager.getInstance().notifyObserver(bleDevice);
//                }
            }
        });
    }

    private static BarcodeScannerBLE instance;

    public static void Init() {
        if(instance != null) return;

        instance = new BarcodeScannerBLE();
        instance.init();
    }

    public static void setListener(OnReceiveDataListener listener) {
        if(instance == null) return;
        instance.setOnReceiveDataListener(listener);
    }

    public static void Start() {
        if(instance == null) instance.init();
        instance.scanDevices();
    }

    public static void Destory() {
        if(instance == null) return;
        instance.destroy();
        instance = null;
    }
}
