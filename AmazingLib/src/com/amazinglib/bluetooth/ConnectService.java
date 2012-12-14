package com.amazinglib.bluetooth;

import java.lang.annotation.Target;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.amazinglib.util.LogUtil;
import com.example.amazinglib.R;

/**
 * Bluetooth常時接続用サービスクラス。<br>
 * Bluetoothの使用可/不可はここでは行いません。<br>
 * TODO BluetoothがONになったことを検知するReceiverを持ち、ライフサイクルを適合させる。
 */
public final class ConnectService extends Service {

	/** Bluetoothに関する処理を行うマネージャー */
	private BluetoothManager mManager;

	/** Bluetooth接続スレッドインスタンス */
	private ConnectManager mConnectManager = null;

	/** TODO 複数接続の為のインチキインスタンス */
	private ConnectManager mConnectManager2 = null;

	/** 接続先のデバイス名 */
	private String mConnectedDeviceName = "";

	/** サービスに接続するためのBinderクラス */
	public class LocalBinder extends Binder {
		/** サービスの取得 */
		public ConnectService getService() {
			LogUtil.d(ConnectService.this, "getService()");
			return ConnectService.this;
		}
	}

	/** Binderの生成 */
	private final IBinder mBinder = new LocalBinder();

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtil.d(this, "onCreate()");
		mManager = BluetoothManager.getInstance(this);
		setup();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtil.d(this, "onStartCommand()");
		if (intent == null) {
			// 再起動時等はサーバー起動を指示しておく
			startServer();
		} else {
			// startServiceにより呼び出し
			startServer();
		}
		// 強制的に再起動させるフラグ
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		LogUtil.d(this, "onBind()");
		return mBinder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(this, "onDestroy");
		stopServer();
	}

	/** 接続中のデバイスにメッセージを送信します。 */
	public void sendMessage(String message) {
		LogUtil.d(this, "sendMessage()");
		if (mConnectManager.getState() != StatusHandler.STATE_CONNECTED) {
			Toast.makeText(this, getString(R.string.notify_not_connected), Toast.LENGTH_SHORT).show();
		} else if (message.length() > 0) {
			byte[] send = message.getBytes();
			mConnectManager.write(send);
		}
		if (mConnectManager2.getState() != StatusHandler.STATE_CONNECTED) {
			Toast.makeText(this, getString(R.string.notify_not_connected), Toast.LENGTH_SHORT).show();
		} else if (message.length() > 0) {
			byte[] send = message.getBytes();
			mConnectManager2.write(send);
		}
	}

	/** 接続の状態を返します。 */
	public int getStatus() {
		LogUtil.d(this, "getStatus()");
		if (mConnectManager != null) {
			return mConnectManager.getState();
		}
		return StatusHandler.STATE_NONE;
	}

	/** 接続の状態を返します。 */
	public int getStatus2() {
		LogUtil.d(this, "getStatus2()");
		if (mConnectManager2 != null) {
			return mConnectManager2.getState();
		}
		return StatusHandler.STATE_NONE;
	}

	/** 接続端末名を返します。 */
	public String getConnectedDeviceName() {
		LogUtil.d(this, "getConnectedDeviceName()");
		return mConnectedDeviceName;
	}

	/** 指定した端末と接続します。 */
	public void connect(BluetoothDevice device, boolean secure) {
		LogUtil.d(this, "connect()");
		// TODO 空いてるマネージャーを探すように変更
		mConnectManager.connect(device, secure);
	}

	/** Bluetooth通信用サーバスレッドを起動します。 */
	private void startServer() {
		boolean isFore = false;
		if (mConnectManager != null) {
			if (mConnectManager.getState() == StatusHandler.STATE_NONE) {
				mConnectManager.start();
				isFore = true;

			}
		}

		if (mConnectManager2 != null) {
			if (mConnectManager2.getState() == StatusHandler.STATE_NONE) {
				mConnectManager2.start();
				isFore = true;
			}
		}
		if (isFore) {
			// サービス永続化。
			forground(true);
		}
	}

	// TODO NotificationのAPIがバージョンごとに違うのでキッチリと対処する。一番古いAPIでも動く。
	@SuppressWarnings("deprecation")
	private void forground(boolean isStart) {
		if (isStart) {
			Intent t_intent = new Intent(this, Target.class);
			t_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent intent = PendingIntent.getActivity(this, 0, t_intent, 0);
			Notification notification = new Notification(R.drawable.ic_launcher, getString(R.string.notify_start_service), System.currentTimeMillis());
			notification.setLatestEventInfo(this, getString(R.string.app_name), getString(R.string.notify_service_alive), intent);
			// 消せない通知。代わりに殺されない。
			notification.flags = Notification.FLAG_ONGOING_EVENT;
			startForeground(1234, notification);
		} else {
			stopForeground(isStart);
		}
	}

	/** Bluetooth通信用サーバスレッドを停止します。 */
	private void stopServer() {
		if (mConnectManager != null) {
			mConnectManager.stop();
		}

		if (mConnectManager2 != null) {
			mConnectManager2.stop();
		}
		// サービス永続化のキャンセル
		forground(false);
	}

	/** スレッド用クラスのセットアップをします。 */
	private void setup() {
		if (mManager.isEnabled()) {
			if (mConnectManager == null) {
				mConnectManager = new ConnectManager(this, mStatusHandler, "1");
			}

			if (mConnectManager2 == null) {
				mConnectManager2 = new ConnectManager(this, mStatusHandler, "2");
			}
		}
	}

	/** 状態変更に関する通知を受け取るハンドラークラスです。 */
	private StatusHandler mStatusHandler = new StatusHandler(new StatusHandler.OnStatusListener() {

		@Override
		public void onWrite(String message) {
			LogUtil.d(ConnectService.this, "onWrite()");
			LogUtil.argD(message, "message");
		}

		@Override
		public void onToastMessage(String message) {
			LogUtil.d(ConnectService.this, "onToastMessage()");
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStateChange(int state) {
			LogUtil.d(ConnectService.this, "onStateChange()");
			LogUtil.argD(StatusHandler.getStatusName(state), "state");
			switch (state) {
			case StatusHandler.STATE_CONNECTED:
				break;
			case StatusHandler.STATE_CONNECTING:
				break;
			case StatusHandler.STATE_LISTEN:
				mConnectedDeviceName = "";
				break;
			case StatusHandler.STATE_NONE:
				mConnectedDeviceName = "";
				break;
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		public void onReceiveDeviceName(String name) {
			LogUtil.d(ConnectService.this, "onReceiveDeviceName()");
			LogUtil.argD(name, "name");
			mConnectedDeviceName = name;
			Toast.makeText(getApplicationContext(), mConnectedDeviceName + getString(R.string.notify_connected), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRead(String message) {
			LogUtil.d(ConnectService.this, "onRead()");
			LogUtil.argD(message, "message");
		}
	});
}
