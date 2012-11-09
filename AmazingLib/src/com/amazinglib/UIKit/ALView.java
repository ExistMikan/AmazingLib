package com.amazinglib.UIKit;

import android.app.Activity;
import android.view.View;

import com.amazinglib.util.Validate;

public class ALView {

	public static void gone(Activity activity, int viewId) {
		Validate.notNull(activity, "activity");
		activity.findViewById(viewId).setVisibility(View.GONE);
	}

	public static void visible(Activity activity, int viewId) {
		Validate.notNull(activity, "activity");
		activity.findViewById(viewId).setVisibility(View.VISIBLE);
	}

	public static void invisible(Activity activity, int viewId) {
		Validate.notNull(activity, "activity");
		activity.findViewById(viewId).setVisibility(View.INVISIBLE);
	}
}
