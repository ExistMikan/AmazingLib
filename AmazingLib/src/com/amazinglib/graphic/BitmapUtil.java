package com.amazinglib.graphic;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.FloatMath;

import com.amazinglib.compat.ALSize;
import com.amazinglib.compat.CompatUtil;
import com.amazinglib.util.LogUtil;

public class BitmapUtil {

	@SuppressWarnings("deprecation")
	public Drawable bitmap2Drawable(Resources res, Bitmap bitmap) {
		if (CompatUtil.canUse(4)) {
			return new BitmapDrawable(res, bitmap);
		} else {
			return new BitmapDrawable(bitmap);
		}
	}

	private static Bitmap makeBitmap(int densityDpi, Resources resources, int resid, int maxWidth, int maxHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, resid, options);
		int inDensity = options.inDensity;
		float scale = (float) inDensity / densityDpi;
		options.inJustDecodeBounds = false;
		float max = Math.max(options.outWidth * scale / maxWidth, options.outHeight * scale / maxHeight);
		max = FloatMath.floor(max);
		if (max > 1) {
			options.inSampleSize = (int) max;
			LogUtil.argE("size", Float.toString(max));
		} else if (max < 1) {
			LogUtil.argE("makeBitmap", "low Size");
		}
		options.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeResource(resources, resid, options);
		Matrix matrix = new Matrix();
		matrix.postScale(scale * scale, scale * scale);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		bitmap.recycle();
		bitmap = null;
		return newBitmap;
	}

	/**
	 * 端末画面の大きさに最適化されたビットマップ画像を取得します。
	 */
	public static Bitmap getScreenSizeBitmap(Activity activity, int resid) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int densityDpi = metrics.densityDpi;
		ALSize alSize = new ALSize(activity);
		Bitmap newBitmap = makeBitmap(densityDpi, activity.getResources(), resid, alSize.width, alSize.height);
		return newBitmap;
	}

}
