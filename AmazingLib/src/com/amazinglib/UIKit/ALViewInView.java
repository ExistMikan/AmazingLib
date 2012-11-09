package com.amazinglib.UIKit;

public class ALViewInView {

	public static void gone(android.view.View v, int viewId) {
		v.findViewById(viewId).setVisibility(android.view.View.GONE);
	}

	public static void visible(android.view.View v, int viewId) {
		v.findViewById(viewId).setVisibility(android.view.View.VISIBLE);
	}

	public static void invisible(android.view.View v, int viewId) {
		v.findViewById(viewId).setVisibility(android.view.View.INVISIBLE);
	}
}
