package com.amazinglib.UIKit;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class ALActivity {

	/**
	 * タイトル無し、IME自動表示機能OFFの汎用的な設定をします。<br>
	 * setContentViewも行われます。
	 * 
	 * @param act アクティビティインスタンス
	 * @param layoutId 表示するレイアウトID
	 */
	public static void setContentViewWithHiddenSetting(Activity act, int layoutId) {
		act.requestWindowFeature(Window.FEATURE_NO_TITLE);
		act.setContentView(layoutId);
		act.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	/***
	 * 指定したアプリケーションの設定画面を開きます。<br>
	 * 起動に失敗した場合falseが返ります。
	 */
	public static boolean startSettingActivity(Activity act, String packageName) {
		// インストールチェック
		if (!isExistPackage(act, packageName)) {
			return false;
		}

		final int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= 9) {
			Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + packageName));
			act.startActivity(intent);
		} else {
			try {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
				String key = (apiLevel == 8 ? "pkg" : "com.android.settings.ApplicationPkgName");
				i.putExtra(key, packageName);
				act.startActivity(i);
			} catch (ActivityNotFoundException ex) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 指定したアプリケーションのストア画面を開きます。<br>
	 * 直接詳細画面まで遷移します。<br>
	 * 起動に失敗した場合falseが返ります。
	 */
	public static boolean startStoreActivity(Activity act, String packageName) {
		try {
			Uri uri = Uri.parse("market://details?id=" + packageName);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			act.startActivity(it);
		} catch (ActivityNotFoundException e) {
			return false;
		}
		return true;
	}

	/**
	 * 指定したアプリケーションのストア画面を開きます。<br>
	 * アプリケーション名で検索をかけます。<br>
	 * 起動に失敗した場合falseが返ります。
	 */
	public static boolean startStoreActivityBySearch(Activity act, String packageName) {
		try {
			Uri uri = Uri.parse("market://search?q=pname:" + packageName);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			act.startActivity(it);
		} catch (ActivityNotFoundException e) {
			return false;
		}
		return true;
	}

	/** 指定したパッケージがインストールされているか調べる */
	private static boolean isExistPackage(Activity act, String packageName) {
		try {
			act.getPackageManager().getApplicationInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			return false;
		}
		return true;
	}
}
