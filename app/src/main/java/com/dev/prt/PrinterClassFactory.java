package com.dev.prt;

import android.content.Context;
import android.os.Handler;

import com.dev.prt.BtService;
//import com.zkc.helper.printer.usb.UsbService;
//import com.zkc.helper.printer.wifi.WifiService;

public class PrinterClassFactory {
    public static PrinterClass create(int type, Context _context){
        if(type==0){
            return new BtService(_context);
        }else if(type==1){
            //    return new WifiService(_context,_mhandler, _handler);
        }else if(type==2){
            //   return new UsbService(_mhandler);
        }
        return null;
    }

}
