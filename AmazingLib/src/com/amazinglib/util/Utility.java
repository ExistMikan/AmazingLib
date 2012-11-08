package com.amazinglib.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

final public class Utility {

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
