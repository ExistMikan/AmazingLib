package com.amazinglib.UIKit;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class ALButton {

	/**
	 * ボタンにActivity起動処理を付加します。
	 * 
	 * @param act Activityインスタンス
	 * @param resId リソースID
	 * @param cls 起動先Activityクラス名
	 */
	public static void setStartActivityButton(final Activity act, final int resId, final Class<?> cls) {
		(act.findViewById(resId)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				act.startActivity(new Intent(act, cls));
			}
		});
	}

	/**
	 * ボタンにActivity起動処理を付加します。
	 * 
	 * @param act Activityインスタンス
	 * @param resId リソースID
	 * @param packageName 起動先パッケージ名
	 * @param activityName 起動先Activity名
	 */
	public static void setStartActivityOfApplicationButton(final Activity act, final int resId, final String packageName, final String activityName) {
		(act.findViewById(resId)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.setClassName(packageName, activityName);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					act.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
					ALToast.showShort(act, packageName + " not installed.");
				}
			}
		});
	}

	/**
	 * ボタンにActivity起動処理を付加します。
	 * 
	 * @param act Activityインスタンス
	 * @param resId リソースID
	 * @param data 起動用Intent
	 */
	public static void setStartActivityButton(final Activity act, final int resId, final Intent data) {
		(act.findViewById(resId)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				act.startActivity(data);
			}
		});
	}

	/**
	 * ボタンにActivity起動処理を付加します。
	 * 
	 * @param act Activityインスタンス
	 * @param resId リソースID
	 * @param cls 起動先Activityクラス名
	 * @param requestCode リクエストコード
	 */
	public static void setStartActivityForResultButton(final Activity act, final int resId, final Class<?> cls, final int requestCode) {
		(act.findViewById(resId)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				act.startActivityForResult(new Intent(act, cls), requestCode);
			}
		});
	}

	/**
	 * ボタンにActivity起動処理を付加します。
	 * 
	 * @param act Activityインスタンス
	 * @param resId リソースID
	 * @param data 起動用Intent
	 * @param requestCode リクエストコード
	 */
	public static void setStartActivityForResultButton(final Activity act, final int resId, final Intent data, final int requestCode) {
		(act.findViewById(resId)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				act.startActivityForResult(data, requestCode);
			}
		});
	}

	/**
	 * ボタンにActivity終了処理を付加します。
	 * 
	 * @param act Activityインスタンス
	 * @param resId リソースID
	 */
	public static void setFinishButton(final Activity act, final int resId) {
		(act.findViewById(resId)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				act.finish();
			}
		});
	}

	/**
	 * ボタンにActivity終了処理を付加します。
	 * 
	 * @param act Activityインスタンス
	 * @param resId リソースID
	 * @param resultCode RESULT_OK または RESULT_CANCELED
	 */
	public static void setFinishButton(final Activity act, final int resId, final int resultCode) {
		(act.findViewById(resId)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				act.setResult(resultCode);
				act.finish();
			}
		});
	}

	/**
	 * ボタンにActivity終了処理を付加します。
	 * 
	 * @param act Activityインスタンス
	 * @param resId リソースID
	 * @param resultCode RESULT_OK または RESULT_CANCELED
	 * @param data 付加するIntent
	 */
	public static void setFinishButton(final Activity act, final int resId, final int resultCode, final Intent data) {
		(act.findViewById(resId)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				act.setResult(resultCode, data);
				act.finish();
			}
		});
	}

	/**
	 * ボタンに指定したアプリケーションの設定画面を開く処理を付加します。
	 * 
	 * @param act Activityインスタンス
	 * @param resId リソースID
	 * @param packageName 設定画面を開くアプリのパッケージ名
	 */
	public static void setSettingActivityButton(final Activity act, final int resId, final String packageName) {
		(act.findViewById(resId)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ALActivity.startSettingActivity(act, packageName);
			}
		});
	}

}
