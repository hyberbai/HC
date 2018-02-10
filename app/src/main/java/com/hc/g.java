package com.hc;

import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.ui.dialog.LoadingDialog;
import hylib.ui.dialog.UICreator;
import hylib.util.Param;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;





import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public final class g {

	public static String GetSysParam(String name) {
		Context context = MyApp.CurrentActivity();
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		String value = sp.getString(name, "");
		return value == null ? "" : value;
	}

	public static void SetSysParam(String name, Object value) {
		SetSysParams(new Param(name, value));
	}

	public static void SetSysParams(Param... params) {
		Context context = MyApp.CurrentActivity();
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		for (Param p : params) {
			sp.edit().putString(p.Name, gv.StrVal(p.Value)).commit();
		}
	}
	
	public static void Vibrate(Context context, int ms){
		Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
		if(vibrator != null) vibrator.vibrate(ms);
	}

}