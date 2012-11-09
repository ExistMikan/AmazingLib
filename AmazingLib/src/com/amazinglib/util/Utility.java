package com.amazinglib.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;

final public class Utility {

	/**
	 * アプリケーションのアイコンを取得します。<br>
	 * 存在しなければnullが返されます。
	 * 
	 * @param context コンテキスト
	 * @param packageName パッケージ名
	 * @return アイコンのDrawableインスタンス
	 */
	public static Drawable getApplicationIcon(Context context, String packageName) {
		Validate.notNull(context, "context");
		Validate.notNullOrEmpty(packageName, "packageName");
		Drawable icon = null;
		try {
			icon = context.getPackageManager().getApplicationIcon(packageName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return icon;
	}

	/**
	 * Android標準のギャラリーを表示します。<br>
	 * 
	 * @param activity
	 * @param requestCode
	 */
	public static void openGallery(Activity activity, int requestCode) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * Android標準のギャラリーを表示します。<br>
	 * 
	 * @param activity
	 * @param requestCode
	 */
	public static void openGallery(Fragment fragment, int requestCode) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		fragment.startActivityForResult(intent, requestCode);
	}

	/**
	 * InputStreamオブジェクトをクローズします。<br>
	 * finally節でIOException対応を特にしない場合に利用できます。
	 * 
	 * @param in
	 */
	public static void closeInputSteram(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ImageViewからBitmapを取得します。
	 * 
	 * @param view
	 * @return 取得したbitmap
	 */
	public static Bitmap getBitmapFromImageView(ImageView view) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) view.getDrawable();
		Bitmap bitmap = bitmapDrawable.getBitmap();
		return bitmap;
	}

	/**
	 * メタデータを取得します。<br>
	 * 
	 * @param context
	 * @return メタデータとして埋め込まれている文字列
	 */
	public static String getMetadataApplicationId(Context context) {
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			if (ai.metaData != null) {
				return ai.metaData.getString("com.existmikan.amazinglib.api");
			}
		} catch (NameNotFoundException e) {
			// 無ければnullでも返しておく
		}
		return null;
	}

	/**
	 * コレクションオブジェクトがnullもしくは空であるか検査します。
	 * 
	 * @param c 任意の方のコレクションオブジェクト
	 * @return nullもしくは空である場合trueが返る
	 */
	public static <T> boolean isNullOrEmpty(Collection<T> c) {
		return (c == null) || (c.size() == 0);
	}

	/**
	 * 文字列がnullもしくは長さ0であるか検査します。<br>
	 * なお、文字列にはtrim()を掛け、空白文字のみで構成されている場合も長さ0とみなします。
	 * 
	 * @param s 文字列
	 * @return nullもしくは長さ0もしくは空白のみならばtrueが返る
	 */
	public static boolean isNullOrEmpty(String s) {
		return (s == null) || (s.trim().length() == 0);
	}

	/**
	 * 引数に指定された値をArrayListに詰めて返す。
	 * 
	 * @param ts 任意の型の引数群
	 * @return 引数に指定した型で構成されるArrayList
	 */
	public static <T> ArrayList<T> arrayList(T... ts) {
		ArrayList<T> arrayList = new ArrayList<T>(ts.length);
		for (T t : ts) {
			arrayList.add(t);
		}
		return arrayList;
	}

	/**
	 * 指定されたドメインのクッキーを削除します。
	 * 
	 * @param context
	 * @param domain ドメイン。
	 */
	public static void clearCookiesForDomain(Context context, String domain) {
		// 確実にインスタンスの作成が行われるようにする作業
		CookieSyncManager syncManager = CookieSyncManager.createInstance(context);
		syncManager.sync();

		CookieManager cookieManager = CookieManager.getInstance();

		String cookies = cookieManager.getCookie(domain);
		if (cookies == null) {
			return;
		}

		// 直接Cookieを削除するコマンドはないので期限切れを設定しremoveExpiredCookieを利用して削除する。
		String[] splitCookies = cookies.split(";");
		for (String cookie : splitCookies) {
			String[] cookieParts = cookie.split("=");
			if (cookieParts.length > 0) {
				String newCookie = cookieParts[0].trim() + "=;expires=Sat, 1 Jan 2000 00:00:01 UTC;";
				cookieManager.setCookie(domain, newCookie);
			}
		}
		cookieManager.removeExpiredCookie();
	}

	/**
	 * ViewのAlpha値を設定します。
	 * 
	 * @param view 値を設定したいView
	 * @param alpha Alpha値
	 */
	public static void setAlpha(View view, float alpha) {
		// setAlphaはAPI11以上でのみ使用可能。よって全てのAPIで利用できる方法で代用。
		AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
		alphaAnimation.setDuration(0);
		alphaAnimation.setFillAfter(true);
		view.startAnimation(alphaAnimation);
	}

	/**
	 * MD5ハッシュ値を計算して返します。
	 * 
	 * @param key 計算対象のキー
	 * @return MD5ハッシュ値
	 */
	public static String md5hash(String key) {
		MessageDigest hash = null;
		try {
			hash = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}

		hash.update(key.getBytes());
		byte[] digest = hash.digest();
		StringBuilder builder = new StringBuilder();
		for (int b : digest) {
			builder.append(Integer.toHexString((b >> 4) & 0xf));
			builder.append(Integer.toHexString((b >> 0) & 0xf));
		}
		return builder.toString();
	}

}
