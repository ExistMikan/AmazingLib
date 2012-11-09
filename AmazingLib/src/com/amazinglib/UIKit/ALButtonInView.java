package com.amazinglib.UIKit;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.amazinglib.lab.AndroidSettingManager;
import com.amazinglib.util.Validate;

public class ALButtonInView {

	/**
	 * ボタンにActivity起動処理を付加します。
	 * 
	 * @param view ボタンのあるビュー
	 * @param cls 起動先Activityクラス名
	 */
	public static void setStartActivityButton(Activity activity, View view, int buttonId, Class<?> cls) {
		Validate.notNull(activity, "activity");
		Validate.notNull(view, "view");
		Validate.notNull(cls, "cls");
		ALButtonInView.setStartActivityButton(activity, view, buttonId, new Intent(activity, cls));
	}

	/**
	 * ボタンにActivity起動処理を付加します。
	 * 
	 * @param view ボタンのあるビュー
	 * @param intent 起動用Intent
	 */
	public static void setStartActivityButton(final Activity activity, View view, int buttonId, final Intent intent) {
		Validate.notNull(activity, "activity");
		Validate.notNull(view, "view");
		Validate.notNull(intent, "intent");
		(view.findViewById(buttonId)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.startActivity(intent);
			}
		});
	}

	/**
	 * ボタンにActivity起動処理を付加します。
	 * 
	 * @param view ボタンのあるビュー
	 * @param packageName 起動先パッケージ名
	 * @param activityName 起動先Activity名
	 */
	public static void setStartActivityOfApplicationButton(Activity activity, View view, int buttonId, String packageName, String activityName) {
		Validate.notNull(activity, "activity");
		Validate.notNull(view, "view");
		Validate.notNullOrEmpty(packageName, "packageName");
		Validate.notNullOrEmpty(activityName, "activityName");
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClassName(packageName, activityName);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		setStartActivityButton(activity, view, buttonId, intent);
	}

	/**
	 * ボタンにActivity起動処理を付加します。
	 * 
	 * @param view ボタンのあるビュー
	 * @param cls 起動先Activityクラス名
	 * @param requestCode リクエストコード
	 */
	public static void setStartActivityForResultButton(Activity activity, View view, int buttonId, Class<?> cls, int requestCode) {
		Validate.notNull(activity, "activity");
		Validate.notNull(view, "view");
		Validate.notNull(cls, "cls");
		setStartActivityForResultButton(activity, view, buttonId, new Intent(activity, cls), requestCode);
	}

	/**
	 * ボタンにActivity起動処理を付加します。
	 * 
	 * @param view ボタンのあるビュー
	 * @param intent 起動用Intent
	 * @param requestCode リクエストコード
	 */
	public static void setStartActivityForResultButton(final Activity activity, View view, int buttonId, final Intent intent, final int requestCode) {
		Validate.notNull(activity, "activity");
		Validate.notNull(view, "view");
		Validate.notNull(intent, "intent");
		(view.findViewById(buttonId)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.startActivityForResult(intent, requestCode);
			}
		});
	}

	/**
	 * ボタンにActivity終了処理を付加します。
	 * 
	 * @param view ボタンのあるビュー
	 */
	public static void setFinishButton(final Activity activity, View view, int buttonId) {
		Validate.notNull(activity, "activity");
		Validate.notNull(view, "view");
		(view.findViewById(buttonId)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
	}

	/**
	 * ボタンにActivity終了処理を付加します。
	 * 
	 * @param view ボタンのあるビュー
	 * @param resultCode RESULT_OK または RESULT_CANCELED
	 */
	public static void setFinishButton(Activity activity, View view, int buttonId, int resultCode) {
		Validate.notNull(activity, "activity");
		Validate.notNull(view, "view");
		setFinishButton(activity, view, buttonId, resultCode, null);
	}

	/**
	 * ボタンにActivity終了処理を付加します。
	 * 
	 * @param view ボタンのあるビュー
	 * @param resultCode RESULT_OK または RESULT_CANCELED
	 * @param intent 付加するIntent
	 */
	public static void setFinishButton(final Activity activity, View view, int buttonId, final int resultCode, final Intent intent) {
		Validate.notNull(activity, "activity");
		Validate.notNull(view, "view");
		(view.findViewById(buttonId)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (intent != null) {
					activity.setResult(resultCode, intent);
				} else {
					activity.setResult(resultCode);
				}
				activity.finish();
			}
		});
	}

	/**
	 * ボタンに指定したアプリケーションの設定画面を開く処理を付加します。
	 * 
	 * @param view ボタンのあるビュー
	 * @param packageName 設定画面を開くアプリのパッケージ名
	 */
	public static void setSettingActivityButton(final Activity activity, View view, int buttonId, final String packageName) {
		Validate.notNull(activity, "activity");
		Validate.notNull(view, "view");
		Validate.notNullOrEmpty(packageName, "packageName");
		(view.findViewById(buttonId)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AndroidSettingManager.openApplicationDetailsSettingActivity(activity, packageName);
			}
		});
	}
}
