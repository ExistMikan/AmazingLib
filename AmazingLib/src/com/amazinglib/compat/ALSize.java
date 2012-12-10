package com.amazinglib.compat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * 生成すると画面サイズを読みこんでくれるクラスです。
 */
public class ALSize {

	public int width;
	public int height;

	@SuppressWarnings("deprecation")
	@TargetApi(13)
	public ALSize(Activity activity) {
		WindowManager manager = (WindowManager) activity.getSystemService(Activity.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();

		if (CompatUtil.canUse(13)) {
			Point p = new Point();
			display.getSize(p);
			width = p.x;
			height = p.y;
		} else {
			width = display.getWidth();
			height = display.getHeight();
		}
	}

}
