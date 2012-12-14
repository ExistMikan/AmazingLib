package com.amazinglib.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.amazinglib.util.LogUtil;
import com.example.amazinglib.R;

/**
 * Smartphone側のための設定画面です。
 */
public class ConnectActivity extends Activity {

	/** Bluetooth設定画面用リクエストコード */
	private static final int REQ_ENABLE_BLUETOOTH = 0;

	/** Bluetoothに関する処理を行うマネージャー */
	private BluetoothManager mManager;

	/** 接続状況の通知を受け取るためのレシーバー */
	private ConnectStatusReceiver mReceiver;

	/** 接続を管理しているサービスのインスタンス(バインドにより取得) */
	private ConnectService mBoundService;

	/** バインドされている間true */
	private boolean mIsBound;

	/** 接続先のデバイス名 */
	private String mConnectedDeviceName = "";

	/** 端末検索用ダイアログ */
	private DiscoveryDeviceDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(this, "onCreate()");
		mManager = BluetoothManager.getInstance(this);
		setUI();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.d(this, "onActivityResult()");
		switch (requestCode) {
		case REQ_ENABLE_BLUETOOTH:
			if (resultCode == RESULT_OK) {
				Toast.makeText(getApplicationContext(), getString(R.string.notify_bluetooth_on), Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), getString(R.string.notify_bluetooth_off), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogUtil.d(this, "onResume()");
		registConnectStatusReceiver();
		doBindService();
	}

	@Override
	protected void onStop() {
		super.onStop();
		LogUtil.d(this, "onStop()");
		unregistConnectStatusReceiver();
		doUnbindService();
	}

	protected void onChangeStatus(String status) {
		// do nothing
	}

	/** Bluetooth接続用サービスを起動します。 */
	protected void startService() {
		LogUtil.d(this, "startService()");
		if (mManager.isEnabled()) {
			if (!mIsBound) {
				doBindService();
			}
			getApplicationContext().startService(new Intent(getApplicationContext(), ConnectService.class));
		} else {
			Toast.makeText(getApplicationContext(), getString(R.string.notify_bluetooth_off_attention), Toast.LENGTH_SHORT).show();
		}
	}

	/** Bluetooth接続用サービスを停止します。 */
	protected void stopService() {
		LogUtil.d(this, "stopService()");
		if (mManager.isEnabled()) {
			doUnbindService();
			getApplicationContext().stopService(new Intent(getApplicationContext(), ConnectService.class));
		} else {
			Toast.makeText(getApplicationContext(), getString(R.string.notify_bluetooth_off_attention), Toast.LENGTH_SHORT).show();
		}
	}

	/** Bluetooth接続用Activityを開きます。 */
	protected void startBluetoothRequestEnableActivity() {
		mManager.startBluetoothRequestEnableActivity(ConnectActivity.this, REQ_ENABLE_BLUETOOTH);
	}

	/** 端末検索のためのダイアログを表示します。 */
	protected void displayDialog() {
		if (mManager.isEnabled()) {
			if (mDialog == null) {
				mDialog = new DiscoveryDeviceDialog(this, new DiscoveryDeviceDialog.OnSelectedItemListener() {
					@Override
					public void onSelectedDevice(BluetoothDevice device) {
						LogUtil.d(this, "onSelectedDevice()");
						connectDevice(device, false);
					}
				});
			}
			mDialog.show();
		} else {
			Toast.makeText(getApplicationContext(), getString(R.string.notify_bluetooth_off_attention), Toast.LENGTH_SHORT).show();
		}
	}

	/** Bluetooth接続の状態を受け取るためのレシーバーを登録します。 */
	private void registConnectStatusReceiver() {
		if (mReceiver == null) {
			mReceiver = new ConnectStatusReceiver(new ConnectStatusReceiver.OnConnectStatusListener() {
				@Override
				public void onStatusNone() {
					LogUtil.d("onStatusNone()");
					onChangeStatus(getString(R.string.layout_status_none));
					mConnectedDeviceName = "";
				}

				@Override
				public void onStatusListen() {
					LogUtil.d("onStatusListen()");
					onChangeStatus(getString(R.string.layout_status_listen));
					mConnectedDeviceName = "";
				}

				@Override
				public void onStatusConnected(String deviceName) {
					LogUtil.d("onStatusConnected()");
					mConnectedDeviceName = deviceName;
					onChangeStatus(getString(R.string.layout_status_connected) + getString(R.string.layout_c) + deviceName);
				}

				@Override
				public void onStatusConnecting() {
					LogUtil.d("onStatusConnecting()");
					onChangeStatus(getString(R.string.layout_status_connecting));
				}
			});
		}
		registerReceiver(mReceiver, ConnectStatusReceiver.makeIntentFilter());
	}

	/** Bluetooth接続の状態を受け取るためのレシーバーを登録解除します。 */
	private void unregistConnectStatusReceiver() {
		if (mReceiver != null) {
			try {
				unregisterReceiver(mReceiver);
			} catch (IllegalArgumentException e) {
				// do nothing
			}
			mReceiver = null;
		}
	}

	/** UIをセットします。 */
	private void setUI() {
		// setSettingButton();
		// setStartServerButton();
		// setDiscoveryDeviceButton();
		// setStopServerButton();
	}

	/** デバイスとBluetooth接続を行います。 */
	private void connectDevice(BluetoothDevice device, boolean secure) {
		if (mIsBound && mBoundService != null) {
			mBoundService.connect(device, secure);
		}
	}

	/** 指定されたステータスの表示名を返します。 */
	private String getStatusName(int status) {
		switch (status) {
		case StatusHandler.STATE_NONE:
			return getString(R.string.layout_status_none);
		case StatusHandler.STATE_LISTEN:
			return getString(R.string.layout_status_listen);
		case StatusHandler.STATE_CONNECTING:
			return getString(R.string.layout_status_connecting);
		case StatusHandler.STATE_CONNECTED:
			return getString(R.string.layout_status_connected) + getString(R.string.layout_c);
		default:
			throw new IllegalArgumentException();
		}
	}

	/** Bluetooth接続用サービスとのバインドによる接続のためのインスタンス */
	private ServiceConnection mConnection = new ServiceConnection() {

		/** バインド成功時に呼び出されます。 */
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			LogUtil.d(this, "onServiceConnected()");
			mBoundService = ((ConnectService.LocalBinder) service).getService();
			mConnectedDeviceName = mBoundService.getConnectedDeviceName();
			String status = getStatusName(mBoundService.getStatus());
			String status2 = getStatusName(mBoundService.getStatus2());
			String s = mConnectedDeviceName + "\n" + status + "\n" + status2;
			onChangeStatus(s);
		}

		/** 予期せぬバインドの終了時に呼び出されます。 */
		@Override
		public void onServiceDisconnected(ComponentName className) {
			LogUtil.d(this, "onServiceDisconnected()");
			mBoundService = null;
		}
	};

	/** Bluetooth接続用サービスとのバインドを開始します。 */
	private void doBindService() {
		bindService(new Intent(ConnectActivity.this, ConnectService.class), mConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	/** Bluetooth接続用サービスとのバインドを終了します。 */
	private void doUnbindService() {
		if (mIsBound) {
			unbindService(mConnection);
			mIsBound = false;
		}
	}

}
