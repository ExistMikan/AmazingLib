package com.amazinglib.util;

import android.util.Log;

import com.example.amazinglib.BuildConfig;

final public class LogUtil {

	private static final boolean DEBUG = BuildConfig.DEBUG;
	private static final String TAG = "AmazingLib";

	public static void argD(String arg, String name) {
		if (DEBUG) {
			Log.d(TAG, name + " = " + arg);
		}
	}

	public static void argI(String arg, String name) {
		if (DEBUG) {
			Log.i(TAG, name + " = " + arg);
		}
	}

	public static void argE(String arg, String name) {
		if (DEBUG) {
			Log.e(TAG, name + " = " + arg);
		}
	}

	public static void argV(String arg, String name) {
		if (DEBUG) {
			Log.v(TAG, name + " = " + arg);
		}
	}

	public static void d(String log) {
		if (DEBUG) {
			Log.d(TAG, log);
		}
	}

	public static void i(String log) {
		if (DEBUG) {
			Log.i(TAG, log);
		}
	}

	public static void e(String log) {
		if (DEBUG) {
			Log.e(TAG, log);
		}
	}

	public static void v(String log) {
		if (DEBUG) {
			Log.v(TAG, log);
		}
	}

	public static void d(String clazz, String log) {
		d(clazz + "#" + log);
	}

	public static void i(String clazz, String log) {
		i(clazz + "#" + log);
	}

	public static void e(String clazz, String log) {
		e(clazz + "#" + log);
	}

	public static void v(String clazz, String log) {
		v(clazz + "#" + log);
	}
}
