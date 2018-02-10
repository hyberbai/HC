package hylib.sys;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.text.TextUtils;

public class SysTools {


	public static boolean isShortcutExist(Context context) {
		return !TextUtils.isEmpty(getShortcutTitle(context));
	}

	public static String getAuthorityFromPermission(Context context, String permission) {
		if (permission == null)
			return null;
		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(
				PackageManager.GET_PROVIDERS);
		if (packs != null) {
			for (PackageInfo pack : packs) {
				ProviderInfo[] providers = pack.providers;
				if (providers != null) {
					for (ProviderInfo provider : providers) {
						if (permission.equals(provider.readPermission))
							return provider.authority;
						if (permission.equals(provider.writePermission))
							return provider.authority;
					}
				}
			}
		}
		return null;
	}

	public static String getShortcutTitle(Context context) {
		if (context == null) {
			return null;
		}
		String appLabel = null;
		try {
			PackageManager pm = context.getPackageManager();
			appLabel = pm.getApplicationLabel(
					pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA))
					.toString();
		} catch (Exception e) {
			return null;
		}
		String authority = getAuthorityFromPermission(context,
				"com.android.launcher.permission.READ_SETTINGS");
		if (authority == null)
			return null;
		final String uriStr = "content://" + authority + "/favorites?notify=true";
		final Uri uri = Uri.parse(uriStr);
		final Cursor c = context.getContentResolver().query(uri, new String[] { "title" },
				"title=?", new String[] { appLabel }, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			do {
				return c.getString(c.getColumnIndexOrThrow("title"));
			} while (c.moveToNext());
		}
		return null;
	}

	public static void createShortCut(Activity act, String appName) {
		Context context = act.getApplicationContext();

		if (isShortcutExist(context))
			return;
		// 创建快捷方式的Intent
		Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重复创建
		shortcutintent.putExtra("duplicate", false);
		// 需要现实的名称
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
		// 快捷图片
	//	Parcelable icon = Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher);
	//	shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		// 点击快捷图片，运行的程序主入口
	//	shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(context, MainActivity.class));
		// 发送广播
		act.sendBroadcast(shortcutintent);
	}

}
