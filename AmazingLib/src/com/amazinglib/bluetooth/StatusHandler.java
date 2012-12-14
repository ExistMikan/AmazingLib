package com.amazinglib.bluetooth;

import android.os.Handler;
import android.os.Message;

import com.amazinglib.util.LogUtil;

/**
 * Bluetooth接続のStatus状況の追跡のためのハンドラークラスです。
 */
public final class StatusHandler extends Handler {

	// 管理するステータス値

	/** 未接続 */
	public static final int STATE_NONE = 0;

	/** 接続待機 */
	public static final int STATE_LISTEN = 1;

	/** 接続開始 */
	public static final int STATE_CONNECTING = 2;

	/** 接続中 */
	public static final int STATE_CONNECTED = 3;

	// handleMessageのための値

	/** ステータス変更 */
	private static final int MESSAGE_STATE_CHANGE = 1;

	/** データ書き込み(送信) */
	private static final int MESSAGE_READ = 2;

	/** データ読み込み(受信) */
	private static final int MESSAGE_WRITE = 3;

	/** 接続先デバイス名取得 */
	private static final int MESSAGE_DEVICE_NAME = 4;

	/** トーストによるメッセージの表示 */
	private static final int MESSAGE_TOAST = 5;

	/** 各種状態変化等の通知を受け取るためのリスナーです */
	public static interface OnStatusListener {
		/** ステータスに変更があった時 */
		void onStateChange(int state);

		/** データ送信時 */
		void onWrite(String message);

		/** データ受信時 */
		void onRead(String message);

		/** 接続先デバイス名取得時 */
		void onReceiveDeviceName(String name);

		/** トーストによるメッセージの表示時 */
		void onToastMessage(String message);
	}

	/** 通知用リスナー */
	private OnStatusListener mListener;

	public StatusHandler(OnStatusListener listener) {
		mListener = listener;
	}

	@Override
	public void handleMessage(Message msg) {
		LogUtil.d(this, "handleMessage()");
		LogUtil.argD(msg.toString(), "msg");
		super.handleMessage(msg);
		switch (msg.what) {
		case MESSAGE_STATE_CHANGE:
			mListener.onStateChange(msg.arg1);
			break;
		case MESSAGE_WRITE:
			byte[] writeBuf = (byte[]) msg.obj;
			String writeMessage = new String(writeBuf);
			LogUtil.argD(writeMessage, "writeMessage");
			mListener.onWrite(writeMessage);
			break;
		case MESSAGE_READ:
			byte[] readBuf = (byte[]) msg.obj;
			String readMessage = new String(readBuf, 0, msg.arg1);
			LogUtil.argD(readMessage, "readMessage");
			// TODO BluetoothSocketをcloseする際、全て0のbyte配列が流れ込むことがあるため<br>
			// ここでチェックして回避している。(GalaxySでのみ確認)
			if (!readMessage.equals("")) {
				mListener.onRead(readMessage);
			}
			break;
		case MESSAGE_DEVICE_NAME:
			String name = (String) msg.obj;
			mListener.onReceiveDeviceName(name);
			break;
		case MESSAGE_TOAST:
			String message = (String) msg.obj;
			mListener.onToastMessage(message);
			break;
		}
	}

	/** 状態変更を通知します */
	public void sendStateChange(int state) {
		LogUtil.d(this, "sendStatusChange()");
		Message msg = obtainMessage(MESSAGE_STATE_CHANGE, state, -1);
		msg.sendToTarget();
	}

	/** 接続先デバイス名を通知します */
	public void sendDeviceName(String name) {
		LogUtil.d(this, "sendDeviceName()");
		obtainMessage(MESSAGE_DEVICE_NAME, name).sendToTarget();
	}

	/** トースト表示を通知します */
	public void sendToast(String message) {
		LogUtil.d(this, "sendToast()");
		obtainMessage(MESSAGE_TOAST, message).sendToTarget();
	}

	/** データ受信を通知します */
	public void sendRead(int byteCount, byte[] buffer) {
		LogUtil.d(this, "sendRead()");
		LogUtil.argD("" + byteCount, "byteCount");
		obtainMessage(MESSAGE_READ, byteCount, -1, buffer).sendToTarget();
	}

	/** データ送信を通知します */
	public void sendWrite(byte[] buffer) {
		LogUtil.d(this, "sendWrite()");
		LogUtil.argD("" + buffer.length, "byteCount");
		obtainMessage(MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
	}

	/** ステータスの識別名を返します */
	public static String getStatusName(int status) {
		switch (status) {
		case STATE_NONE:
			return "STATE_NONE";
		case STATE_LISTEN:
			return "STATE_LISTEN";
		case STATE_CONNECTING:
			return "STATE_CONNECTING";
		case STATE_CONNECTED:
			return "STATE_CONNECTED";
		default:
			throw new IllegalArgumentException();
		}
	}

}
