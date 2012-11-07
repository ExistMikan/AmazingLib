package com.amazinglib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Preferences {

	/*
	 * default values
	 */

	private static final String DEFAULT_TEMPLATE_KEY = "";

	/*
	 * preference keys
	 */

	/** access key */
	private static final String KEY_TEMPLATE = "prefTemplateKey";

	/*
	 * others
	 */

	private SharedPreferences mPreferences;

	public Preferences(Context context) {
		mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void setTemplate(String value) {
		putValue(KEY_TEMPLATE, value);
	}

	public String getTemplate() {
		return mPreferences.getString(KEY_TEMPLATE, DEFAULT_TEMPLATE_KEY);
	}

	private void putValue(String key, Object value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}

		Editor editor = mPreferences.edit();
		if (value instanceof Integer) {
			editor.putInt(key, (Integer) value);
		} else if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
		} else if (value instanceof String) {
			editor.putString(key, (String) value);
		} else {
			throw new IllegalArgumentException();
		}
		editor.commit();
	}

}
