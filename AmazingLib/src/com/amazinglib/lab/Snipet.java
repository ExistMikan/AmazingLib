package com.amazinglib.lab;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

//スニペットクラス
//とりあえずいいなと思ったコードを断片的に貼り付けてあるだけ。
public class Snipet {

	// EditTextのリターンキーをハンドリングできるリスナー
	private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
			// キー離したときに、取ってくる。
			if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
				String message = view.getText().toString();
			}
			return true;
		}
	};

	/*
	 * 匿名クラスで作成するとエンクロージングクラスであるMainActivityに強参照(VMが勝手に作成する)が残りメモリリークの可能性があるので
	 * staticクラスにての実装及びエンクロージングクラス参照のためのインスタンスを弱参照にて保持する。
	 */
	private static class MyHandler extends Handler {

		// 任意のActivityクラス名を入れること。
		private WeakReference<Activity> mActivity;

		public MyHandler(Activity activity) {
			this.mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Activity activity = mActivity.get();
		}
	}

}
