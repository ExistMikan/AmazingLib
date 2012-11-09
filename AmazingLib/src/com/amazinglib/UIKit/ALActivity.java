package com.amazinglib.UIKit;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.amazinglib.util.Validate;

public class ALActivity {

	/**
	 * タイトル無し、IME自動表示機能OFFの汎用的な設定をします。<br>
	 * setContentViewも行われます。
	 */
	public static void setContentViewWithHiddenSetting(Activity activity, int layoutId) {
		Validate.notNull(activity, "activity");
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.setContentView(layoutId);
		activity.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	/**
	 * 指定したアプリケーションのストア画面を開きます。<br>
	 * 直接詳細画面まで遷移します。<br>
	 * 起動に失敗した場合falseが返ります。
	 * 
	 * @param 指定先アプリケーションのパッケージ名
	 */
	public static boolean startStoreActivity(Activity activity, String packageName) {
		Validate.notNull(activity, "activity");
		Validate.notNullOrEmpty(packageName, "packageName");
		try {
			Uri uri = Uri.parse("market://details?id=" + packageName);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			activity.startActivity(it);
		} catch (ActivityNotFoundException e) {
			return false;
		}
		return true;
	}

	/**
	 * 指定したアプリケーションのストア検索画面を開きます。<br>
	 * アプリケーション名で検索をかけます。<br>
	 * 起動に失敗した場合falseが返ります。
	 * 
	 * @param 指定先アプリケーションのパッケージ名
	 */
	public static boolean startStoreActivityBySearch(Activity activity, String packageName) {
		Validate.notNull(activity, "activity");
		Validate.notNullOrEmpty(packageName, "packageName");
		try {
			Uri uri = Uri.parse("market://search?q=pname:" + packageName);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			activity.startActivity(it);
		} catch (ActivityNotFoundException e) {
			return false;
		}
		return true;
	}

}
