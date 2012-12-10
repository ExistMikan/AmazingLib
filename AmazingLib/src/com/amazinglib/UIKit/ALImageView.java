package com.amazinglib.UIKit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.amazinglib.compat.CompatUtil;
import com.amazinglib.util.Validate;

public class ALImageView {

	public static void setImage(Activity activity, int imageViewId, Drawable image) {
		Validate.notNull(activity, "activity");
		Validate.notNull(image, "image");
		((ImageView) activity.findViewById(imageViewId)).setImageDrawable(image);
	}

	@TargetApi(16)
	@SuppressWarnings("deprecation")
	public static void setBackground(ImageView view, Drawable image) {
		Validate.notNull(view, "view");
		Validate.notNull(image, "image");
		if (CompatUtil.canUse(16)) {
			view.setBackground(image);
		} else {
			view.setBackgroundDrawable(image);
		}
	}
}
