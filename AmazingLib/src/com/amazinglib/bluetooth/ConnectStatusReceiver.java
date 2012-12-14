package com.amazinglib.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.amazinglib.util.LogUtil;

/**
 * ConnectServiceから接続状況の通知を受け取るためのレシーバークラスです。<br>
 * 本アプリ上のBluetooth接続状況を受け取りたいActivity等でセットすると便利です。
 */
public final class ConnectStatusReceiver extends BroadcastReceiver {

	/** 未接続 */
	public static final String ACTION_STATUS_NONE = "com.amazinglib.bluetooth.ACTION_STATUS_NONE";

	/** 接続待機 */
	public static final String ACTION_STATUS_LISTEN = "com.amazinglib.bluetooth.ACTION_STATUS_LISTEN";

	/** 接続開始 */
	public static final String ACTION_STATUS_CONNECTING = "com.amazinglib.bluetooth.ACTION_STATUS_CONNECTING";

	/** 接続中 */
	public static final String ACTION_STATUS_CONNECTED = "com.amazinglib.bluetooth.ACTION_STATUS_CONNECTED";

	/** 接続状況の通知をハンドリングするためのリスナーです。 */
	public static interface OnConnectStatusListener {

		/** 状態が未接続に移行した時に呼び出されます。 */
		void onStatusNone();

		/** 状態が接続待機に移行した時に呼び出されます。 */
		void onStatusListen();

		/** 状態が接続開始に移行した時に呼び出されます。 */
		void onStatusConnecting();

		/** 状態が接続中に移行した時に呼び出されます。 */
		void onStatusConnected(String deviceName);
	}

	/** クライアントから渡されたリスナー */
	private OnConnectStatusListener mListener;

	/** 本レシーバーの登録の際に必要なフィルターを生成して返します。 */
	public static IntentFilter makeIntentFilter() {
		LogUtil.d(IntentFilter.class, "makeIntentFilter()");
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_STATUS_NONE);
		filter.addAction(ACTION_STATUS_LISTEN);
		filter.addAction(ACTION_STATUS_CONNECTING);
		filter.addAction(ACTION_STATUS_CONNECTED);
		return filter;
	}

	/** コンストラクタです。リスナーを登録します。 */
	public ConnectStatusReceiver(OnConnectStatusListener listener) {
		LogUtil.d(this, "ConnectStatusReceiver()");
		mListener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		LogUtil.d(this, "onReceive()");
		LogUtil.argD(action, "action");
		if (ACTION_STATUS_NONE.equals(action)) {
			mListener.onStatusNone();
		}
		if (ACTION_STATUS_LISTEN.equals(action)) {
			mListener.onStatusListen();
		}
		if (ACTION_STATUS_CONNECTING.equals(action)) {
			mListener.onStatusConnecting();
		}
		if (ACTION_STATUS_CONNECTED.equals(action)) {
			String devicename = intent.getStringExtra("device");
			mListener.onStatusConnected(devicename);
		}
	}
}
