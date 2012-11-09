package com.amazinglib.UIKit;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;

import com.amazinglib.util.Validate;

public class ALEditText {

	/**
	 * EditTextから文字列を取得します。
	 * 
	 * @return 文字列
	 */
	public static String getString(Activity activity, int editTextId) {
		Validate.notNull(activity, "activity");
		return (((EditText) activity.findViewById(editTextId))).getText().toString();
	}

	/**
	 * 文字列に、指定した文字が含まれているかどうかを検査します。
	 * 
	 * @param text 文字列
	 * @param filters 検査文字列配列
	 * @return 含まれていればtrue
	 */
	public static boolean isContain(String text, String[] filters) {
		if (TextUtils.isEmpty(text)) {
			return false;
		} else {
			for (String filter : filters) {
				if (text.indexOf(filter) != -1) {
					return true;
				}
			}
			return false;
		}
	}
}
