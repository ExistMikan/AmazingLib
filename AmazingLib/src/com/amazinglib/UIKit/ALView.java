package com.amazinglib.UIKit;

import android.app.Activity;
import android.view.View;

public class ALView {

	public static void gone(Activity act, int viewId) {
		act.findViewById(viewId).setVisibility(View.GONE);
	}

	public static void visible(Activity act, int viewId) {
		act.findViewById(viewId).setVisibility(View.VISIBLE);
	}

	public static void invisible(Activity act, int viewId) {
		act.findViewById(viewId).setVisibility(View.INVISIBLE);
	}
}
