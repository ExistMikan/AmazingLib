package com.amazinglib.UIKit;

import android.app.Activity;
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
}
