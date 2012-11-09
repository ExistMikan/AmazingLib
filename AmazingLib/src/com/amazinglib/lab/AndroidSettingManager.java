package com.amazinglib.lab;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.provider.Settings;

import com.amazinglib.util.Utility;
import com.amazinglib.util.Validate;

/**
 * Androidの設定に関する処理をまとめたマネージャークラスです。
 */
public class AndroidSettingManager {

	/** BASE(API Level1)で実装された設定画面用のAction名のリスト */
	private static final String[] SETTING_LIST_BASE = new String[] { Settings.ACTION_APN_SETTINGS, Settings.ACTION_APPLICATION_SETTINGS,
			Settings.ACTION_BLUETOOTH_SETTINGS, Settings.ACTION_DATE_SETTINGS, Settings.ACTION_DISPLAY_SETTINGS, Settings.ACTION_LOCALE_SETTINGS,
			Settings.ACTION_LOCATION_SOURCE_SETTINGS, Settings.ACTION_SECURITY_SETTINGS, Settings.ACTION_SETTINGS, Settings.ACTION_SOUND_SETTINGS,
			Settings.ACTION_WIFI_SETTINGS, Settings.ACTION_WIRELESS_SETTINGS, Settings.AUTHORITY, };

	/** CUPCAKE(API Level3)で実装された設定画面用のAction名のリスト */
	private static final String[] SETTING_LIST_CUPCAKE = new String[] { Settings.ACTION_AIRPLANE_MODE_SETTINGS,
			Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS, Settings.ACTION_DATA_ROAMING_SETTINGS, Settings.ACTION_INPUT_METHOD_SETTINGS,
			Settings.ACTION_INTERNAL_STORAGE_SETTINGS, Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS, Settings.ACTION_MEMORY_CARD_SETTINGS,
			Settings.ACTION_NETWORK_OPERATOR_SETTINGS, Settings.ACTION_QUICK_LAUNCH_SETTINGS, Settings.ACTION_SYNC_SETTINGS,
			Settings.ACTION_USER_DICTIONARY_SETTINGS, Settings.ACTION_WIFI_IP_SETTINGS, };

	/** ECLAIR(API Level5)で実装された設定画面用のAction名のリスト */
	private static final String[] SETTING_LIST_ECLAIR = new String[] { Settings.ACTION_ACCESSIBILITY_SETTINGS, Settings.ACTION_PRIVACY_SETTINGS, };

	/** FROYO(API Level8)で実装された設定画面用のAction名のリスト */
	private static final String[] SETTING_LIST_FROYO = new String[] { Settings.ACTION_ADD_ACCOUNT, Settings.ACTION_DEVICE_INFO_SETTINGS,
			Settings.ACTION_SEARCH_SETTINGS, Settings.EXTRA_AUTHORITIES, };

	/** GINGERBREAD(API Level9)で実装された設定画面用のAction名のリスト */
	private static final String[] SETTING_LIST_GINGERBREAD = new String[] { Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
			Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS, };

	/** HONEYCOMB(API Level11)で実装された設定画面用のAction名のリスト */
	private static final String[] SETTING_LIST_HONEYCOMB = new String[] { Settings.ACTION_INPUT_METHOD_SUBTYPE_SETTINGS, Settings.EXTRA_INPUT_METHOD_ID, };

	/** ICE_CREAM_SANDWITH(API Level14)で実装された設定画面用のAction名のリスト */
	private static final String[] SETTING_LIST_ICE_CREAM_SANDWICH = new String[] { Settings.ACTION_NFCSHARING_SETTINGS, };

	/** JELLY_BEAN(API Level16)で実装された設定画面用のAction名のリスト */
	private static final String[] SETTING_LIST_JELLY_BEAN = new String[] { Settings.ACTION_NFC_SETTINGS, };

	/**
	 * 利用可能な設定用ActionActivityのリストを取得します。
	 * 
	 * @return ActionActivityのリスト
	 */
	public static List<String> getSettingList() {
		return getSettingList(VERSION.SDK_INT);
	}

	/**
	 * 指定したAPIバージョンで利用可能な設定用ActionActivityのリストを取得します。
	 * 
	 * @param versionCode バージョンコード
	 * @return ActionActivityのリスト
	 */
	public static List<String> getSettingList(int versionCode) {
		if (versionCode < 1) {
			throw new IllegalArgumentException("Version is started from 1.You specified version is " + versionCode + ".");
		}
		List<String> list = new ArrayList<String>();
		switch (versionCode) {
		default:
		case VERSION_CODES.JELLY_BEAN:
			for (String s : SETTING_LIST_JELLY_BEAN) {
				list.add(s);
			}
		case VERSION_CODES.ICE_CREAM_SANDWICH_MR1:
		case VERSION_CODES.ICE_CREAM_SANDWICH:
			for (String s : SETTING_LIST_ICE_CREAM_SANDWICH) {
				list.add(s);
			}
		case VERSION_CODES.HONEYCOMB_MR2:
		case VERSION_CODES.HONEYCOMB_MR1:
		case VERSION_CODES.HONEYCOMB:
			for (String s : SETTING_LIST_HONEYCOMB) {
				list.add(s);
			}
		case VERSION_CODES.GINGERBREAD_MR1:
		case VERSION_CODES.GINGERBREAD:
			for (String s : SETTING_LIST_GINGERBREAD) {
				list.add(s);
			}
		case VERSION_CODES.FROYO:
			for (String s : SETTING_LIST_FROYO) {
				list.add(s);
			}
		case VERSION_CODES.ECLAIR_MR1:
		case VERSION_CODES.ECLAIR_0_1:
		case VERSION_CODES.ECLAIR:
			for (String s : SETTING_LIST_ECLAIR) {
				list.add(s);
			}
		case VERSION_CODES.DONUT:
		case VERSION_CODES.CUPCAKE:
			for (String s : SETTING_LIST_CUPCAKE) {
				list.add(s);
			}
		case VERSION_CODES.BASE_1_1:
		case VERSION_CODES.BASE:
			for (String s : SETTING_LIST_BASE) {
				list.add(s);
			}
			break;
		}
		return list;
	}

	/**
	 * 指定された設定用Activityを開きます
	 * 
	 * @param action 設定先を表すAction名
	 * @return Activityを開くことに成功した場合trueを返します
	 */
	public static boolean openSettingActivity(Activity activity, String action) {
		Validate.notNull(activity, "activity");
		Validate.notNullOrEmpty(action, "action");
		try {
			activity.startActivity(new Intent(action));
		} catch (ActivityNotFoundException e) {
			return false;
		}
		return true;
	}

	/**
	 * 自分のアプリケーションの詳細設定画面を開きます
	 * 
	 * @return 詳細設定画面を開くことに成功した場合trueを返します
	 */
	public static boolean openApplicationDetailsSettingActivity(Activity activity) {
		Validate.notNull(activity, "activity");
		return openApplicationDetailsSettingActivity(activity, activity.getPackageName());
	}

	/**
	 * 指定したアプリケーションの詳細設定画面を開きます
	 * 
	 * @param packageName 指定先アプリケーションのパッケージ名
	 * @return 詳細設定画面を開くことに成功した場合trueを返します
	 */
	public static boolean openApplicationDetailsSettingActivity(Activity activity, String packageName) {
		Validate.notNull(activity, "activity");
		Validate.notNullOrEmpty(packageName, "packageName");
		if (!Utility.isInstalledPackage(activity, packageName)) {
			return false;
		}
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			i.setData(Uri.parse("package:" + packageName));
			try {
				activity.startActivity(i);
			} catch (ActivityNotFoundException e) {
				return false;
			}
		} else {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
			String key = (VERSION.SDK_INT == 8 ? "pkg" : "com.android.settings.ApplicationPkgName");
			i.putExtra(key, packageName);
			try {
				activity.startActivity(i);
			} catch (ActivityNotFoundException e) {
				return false;
			}
		}
		return true;
	}

}
