package com.amazinglib.util;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebView;

import com.example.amazinglib.R;

public class DeviceInfoProvider {

	private Context mContext;

	public DeviceInfoProvider(Context context) {
		mContext = context;
	}

	/** センサーの一覧を取得します。 */
	public String getSensors() {
		SensorManager manager = (SensorManager) mContext.getSystemService(Activity.SENSOR_SERVICE);
		List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ALL);
		StringBuilder sb = new StringBuilder();
		for (Sensor s : sensors) {
			sb.append(s.getName()).append("\n");
		}
		return sb.toString();
	}

	/** トータルメモリをKB単位で取得します。 */
	public String getTotalMemory() {
		Runtime runtime = Runtime.getRuntime();
		return "" + runtime.totalMemory() / 1024;
	}

	/** Dalvikで使用出来る最大メモリをKB単位で取得します。 */
	public String getMaxMemory() {
		Runtime runtime = Runtime.getRuntime();
		return "" + runtime.maxMemory() / 1024;
	}

	/** 空きメモリをKB単位で取得します。 */
	public String getFreeMemory() {
		Runtime runtime = Runtime.getRuntime();
		return "" + runtime.freeMemory() / 1024;
	}

	/** 現在使用しているメモリをKB単位で取得します。 */
	public String getUsedMemory() {
		Runtime runtime = Runtime.getRuntime();
		return "" + (runtime.totalMemory() - runtime.freeMemory()) / 1024;
	}

	/** webviewのUserAngentを取得します。 */
	public String getUserAgent() {
		return new WebView(mContext).getSettings().getUserAgentString();
	}

	/** ボード名称を取得します。 */
	public String getBoard() {
		return Build.BOARD;
	}

	/** ブートローダのバージョン番号を取得します。 */
	public String getBootLoader() {
		return Build.BOOTLOADER;
	}

	/** ブランド名を取得します。 */
	public String getBrand() {
		return Build.BRAND;
	}

	/** 開発コードネームを取得します。 */
	public String getCodeName() {
		return Build.VERSION.CODENAME;
	}

	/** ネイティブコードの命令セットを取得します。 */
	public String getCPUABI() {
		return Build.CPU_ABI;
	}

	/** ネイティブコードの第2命令セットを取得します。 */
	public String getCPUABI2() {
		return Build.CPU_ABI2;
	}

	/** デバイス名を取得します。 */
	public String getDevice() {
		return Build.DEVICE;
	}

	/** ユーザへ表示するビルドIDを取得します。 */
	public String getDisplay() {
		return Build.DISPLAY;
	}

	/** 一意にビルドを識別することができる識別子を取得します。 */
	public String getFingerPrint() {
		return Build.FINGERPRINT;
	}

	/** ハードウェア情報を取得します。 */
	public String getHardware() {
		return Build.HARDWARE;
	}

	/** ホスト名を取得します。 */
	public String getHost() {
		return Build.HOST;
	}

	/** 変更番号もしくは一意なラベルを取得します。 */
	public String getId() {
		return Build.ID;
	}

	/** ソースコード管理番号(チェンジセットの変更回数など)を取得します。 */
	public String getIncremental() {
		return Build.VERSION.INCREMENTAL;
	}

	/** 製造社名を取得します。 */
	public String getManufacturer() {
		return Build.MANUFACTURER;
	}

	/** モデル名を取得します。 */
	public String getModel() {
		return Build.MODEL;
	}

	/** 製品名を取得します。 */
	public String getProduct() {
		return Build.PRODUCT;
	}

	/** 無線ファームウェアのバージョンを取得します。 */
	@TargetApi(14)
	@SuppressWarnings("deprecation")
	public String getRadio() {
		if (Integer.parseInt(getVersion()) < 14) {
			return Build.RADIO;
		} else {
			return Build.getRadioVersion();
		}
	}

	/** ユーザに表示するバージョン番号 */
	public String getRelease() {
		return Build.VERSION.RELEASE;
	}

	/** ビルドのタグ名を取得します。 */
	public String getTags() {
		return Build.TAGS;
	}

	/** システム時刻を取得します。 */
	public String getTime() {
		return Long.toString(Build.TIME);
	}

	/** ビルドタイプを取得します。 */
	public String getType() {
		return Build.TYPE;
	}

	/** 情報不明時の識別子を取得します。 */
	public String getUnknown() {
		return Build.UNKNOWN;
	}

	/** ユーザ情報を取得します。 */
	public String getUser() {
		return Build.USER;
	}

	/** フレームワークのバージョン番号を取得します。 */
	public String getVersion() {
		return "" + Build.VERSION.SDK_INT;
	}

	/** 画面の高さを取得します。 */
	@SuppressWarnings("deprecation")
	@TargetApi(13)
	public String getHeight(Activity activity) {
		WindowManager manager = (WindowManager) activity.getSystemService(Activity.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		if (13 <= VERSION.SDK_INT) {
			Point p = new Point();
			display.getSize(p);
			return "" + p.y;
		} else {
			return "" + display.getHeight();
		}
	}

	/** 画面の幅を取得します。 */
	@SuppressWarnings("deprecation")
	@TargetApi(13)
	public String getWidth(Activity activity) {
		WindowManager manager = (WindowManager) activity.getSystemService(Activity.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		if (13 <= VERSION.SDK_INT) {
			Point p = new Point();
			display.getSize(p);
			return "" + p.x;
		} else {
			return "" + display.getWidth();
		}
	}

	/** 画面密度を取得します。 */
	public String getDensity(Context context) {
		return "" + context.getResources().getDisplayMetrics().density;
	}

	/** 画面横DPを取得します。 */
	public String getWidthDP(Activity activity) {
		float density = Float.parseFloat(getDensity(activity));
		int width = Integer.parseInt(getWidth(activity));
		float dp = width / density;
		return dp + "";
	}

	/** 画面縦DPを取得します。 */
	public String getHeightDP(Activity activity) {
		float density = Float.parseFloat(getDensity(activity));
		int height = Integer.parseInt(getHeight(activity));
		float dp = height / density;
		return dp + "";
	}

	/** 密度(DPI)を取得します。 */
	public String getDensityDpi(Context context) {
		return "" + context.getResources().getDisplayMetrics().densityDpi;
	}

	/** 現在の1ピクセルに対する1dipの倍率を取得します。 */
	public String getScaledDensity(Context context) {
		return "" + context.getResources().getDisplayMetrics().scaledDensity;
	}

	/** 横ピクセル値を取得します。 */
	public String getWidthPixels(Context context) {
		return "" + context.getResources().getDisplayMetrics().widthPixels;
	}

	/** 縦ピクセル値を取得します。 */
	public String getHeightPixels(Context context) {
		return "" + context.getResources().getDisplayMetrics().heightPixels;
	}

	/** X方向の密度(DPI)を取得します。 */
	public String getXDpi(Context context) {
		return "" + context.getResources().getDisplayMetrics().xdpi;
	}

	/** Y方向の密度(DPI)を取得します。 */
	public String getYDpi(Context context) {
		return "" + context.getResources().getDisplayMetrics().ydpi;
	}

	/** タブレットかどうかを判定します。 */
	public boolean isTablet(Context context) {
		return context.getResources().getBoolean(R.bool.isTablet);
	}

}
