package com.amazinglib.UIKit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ALImageView {

	public static void setImage(Activity act, int resId, Drawable d) {
		((ImageView) act.findViewById(resId)).setImageDrawable(d);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void setBackground(ImageView view, Drawable d) {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackgroundDrawable(d);
		} else {
			view.setBackground(d);
		}
	}
}
