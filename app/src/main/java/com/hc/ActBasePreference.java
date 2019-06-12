package com.hc;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hylib.sys.HyApp;
import hylib.toolkits.type;
import hylib.ui.dialog.UCParamList;
import hylib.ui.dialog.UICreator;
import hylib.util.ParamList;


public class ActBasePreference extends PreferenceActivity {

	private static final boolean ALWAYS_SIMPLE_PREFS = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CreateHeaderBar();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setupSimplePreferencesScreen();
	}
	
	@SuppressWarnings("deprecation")
	private void setupSimplePreferencesScreen() {
		if (!isSimplePreferences(this)) {
			return;
		}

//		PreferenceCategory fakeHeader = new PreferenceCategory(this);
//		fakeHeader.setTitle(R.string.pref_header_notifications);
//		getPreferenceScreen().addPreference(fakeHeader);
//		addPreferencesFromResource(R.xml.pref_notification);
		
		Init();
		Binding();
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T $(int resId) {
		return (T) super.findViewById(resId);
	}

	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.ib_back || id == ID.Back) finish();
	}

	public View getRootView()
	{
		return ((ViewGroup)$(android.R.id.content)).getChildAt(0);
	}

	public ViewGroup getRootLayout()
	{
		return type.as(getRootView(), ViewGroup.class);
	}

	public void CreateHeaderBar(){
		Context context = HyApp.CurrentActivity();
		UCParamList pl = new UCParamList(
				"items: [" +
						"ib: { id:Back, style: header_button,bgc1:lb, bg:back }, " +
						"tv: { id:Title, fs: 16sp, text: 系统设置, w:1w, h:wrap, grv:l, color:title_text_color, padding: 8dp }, " +
						"], style: header_bar");

		ParamList plStyles = new ParamList(new Object[]{
				"header_button: { w:27dp, h:27dp, margin: 6dp }",
				"header_bar: { hor, grv:c, margin: 0dp, h:wrap, w:match, padding: 0dp, color: w, bgc: #888 }",
		});

		pl.setStyles("styles", plStyles);

		View pnlHeader = UICreator.CreatePanel(context, pl);
		getRootLayout().addView(pnlHeader, 0);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public <T extends Preference> T $(String key) {
		return (T) findPreference(key);
	}
	
	protected void Init(){
	}

	protected void Binding(){
		AutoBinding();
	}

	/** {@inheritDoc} */
	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this) && !isSimplePreferences(this);
	}

	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
				|| !isXLargeTablet(context);
	}

	/** {@inheritDoc} */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(this)) {
//			loadHeadersFromResource(R.xml.pref_headers, target);
		}
	}
	
	private Preference.OnPreferenceChangeListener OnPreferenceChange = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			return onChange(preference, value);
		}
	};

	public boolean onChange(Preference preference, Object value) {
		String sValue = value.toString();
		boolean bOK = true;
		if (preference instanceof ListPreference) {
			if(bOK) {
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(sValue);
				Object v = onListChange(listPreference, index);
				if(v == null) return false;
				preference.setSummary(v instanceof String ? (String)v :
					index >= 0 ? listPreference.getEntries()[index] : null);
				setResult(RESULT_OK, null);
			}
		} else {
  	    	if(!bOK) return false;
			preference.setSummary(sValue);
		}
//		if(preference.getSummary().equals(sValue)) return true;
		return true;
	}
	
	protected boolean onClick(Preference preference) {
		return true;
	}

	public Object onListChange(ListPreference preference, int index) {
		return true;
	}

	private Preference.OnPreferenceClickListener OnPreferenceClick = new Preference.OnPreferenceClickListener() {

		@Override
		public boolean onPreferenceClick(Preference preference) {
			return onClick(preference);
		}
	};

	protected void SetPrefSummary(Preference preference) {
		String key = PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(
					 preference.getKey(), "");
		if (preference instanceof ListPreference) {
			ListPreference listPreference = (ListPreference) preference;
			int index = listPreference.findIndexOfValue(key);

			preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : key);
		} else
			preference.setSummary(key);
	}

	protected void BindPreference(Preference preference) {
		preference.setOnPreferenceChangeListener(OnPreferenceChange);
		preference.setOnPreferenceClickListener(OnPreferenceClick);
		SetPrefSummary(preference);
	}
	
	protected void AutoBinding() {
		PreferenceScreen ps = getPreferenceScreen();
		for (int i = 0; i < ps.getPreferenceCount(); i++)
			BindPreference(ps.getPreference(i));
	}

//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	public class GeneralPreferenceFragment extends PreferenceFragment {
//		@Override
//		public void onCreate(Bundle savedInstanceState) {
//			super.onCreate(savedInstanceState);
//			addPreferencesFromResource(R.xml.pref_settings);
//
//			BindPreference(findPreference("server_addr"));
//		}
//	}
	
}
