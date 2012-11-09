package com.amazinglib.UIKit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.amazinglib.util.Validate;

public class ALImageView {

	public static void setImage(Activity activity, int imageViewId, Drawable image) {
		Validate.notNull(activity, "activity");
		Validate.notNull(image, "image");
		((ImageView) activity.findViewById(imageViewId)).setImageDrawable(image);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void setBackground(ImageView view, Drawable image) {
		Validate.notNull(view, "view");
		Validate.notNull(image, "image");
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackgroundDrawable(image);
		} else {
			view.setBackground(image);
		}
	}
}
