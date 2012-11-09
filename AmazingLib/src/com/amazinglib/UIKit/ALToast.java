package com.amazinglib.UIKit;

import android.content.Context;
import android.widget.Toast;

import com.amazinglib.util.Validate;

public class ALToast {

	public static void showShort(Context context, String text) {
		Validate.notNull(context, "context");
		Validate.notNullOrEmpty(text, "text");
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void showLong(Context context, String text) {
		Validate.notNull(context, "context");
		Validate.notNullOrEmpty(text, "text");
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

}
