package com.amazinglib.UIKit;

import android.view.View;

import com.amazinglib.util.Validate;

public class ALViewInView {

	public static void gone(View view, int viewId) {
		Validate.notNull(view, "view");
		view.findViewById(viewId).setVisibility(View.GONE);
	}

	public static void visible(View view, int viewId) {
		Validate.notNull(view, "view");
		view.findViewById(viewId).setVisibility(View.VISIBLE);
	}

	public static void invisible(View view, int viewId) {
		Validate.notNull(view, "view");
		view.findViewById(viewId).setVisibility(View.INVISIBLE);
	}
}
