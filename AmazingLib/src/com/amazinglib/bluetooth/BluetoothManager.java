package com.amazinglib.bluetooth;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import com.amazinglib.util.LogUtil;

/** Bluetooth関連の処理を行うマネージャークラスです。 */
public final class BluetoothManager {

	/** Bluetoothデバイス検知を通知するリスナーです。 */
	public static interface OnDeviceDiscoveryListener {
		/** 検知開始時に呼び出されます。 */
		void onStart();

		/** デバイスを発見時に呼び出されます。 */
		void onFound(BluetoothDevice device);

		/** 名前が変更されたデバイスを発見時に呼び出されます。 */
		void onNameChanged(BluetoothDevice device);

		/** 検知終了時に呼び出されます。 */
		void onFinished();
	}

	/** アプリ名(ソケット作成時使用) */
	private static final String APP_NAME = "AmazingLib";

	/** アプリ名((Insecureな)ソケット作成時使用) */
	private static final String APP_NAME_INSECURE = "AmazingLibInsecure";

	/** ソケット作成時に使用するUUID(UUIDは重複してはいけないのでアプリ事に生成する必要があります。) */
	private static final UUID UUID_FOR_BLUETOOTH = UUID.fromString("xxxxxxxxxxxxxxxxxxxx");

	/** Insecureなソケット作成時に使用するUUID(UUIDは重複してはいけないのでアプリ事に生成する必要があります。) */
	private static final UUID UUID_FOR_BLUETOOTH_INSECURE = UUID.fromString("xxxxxxxxxxxxxxxxxxxx");

	/** 自インスタンス */
	private static BluetoothManager mInstance;

	/** BluetoothAPIを使用するためのアダプタ */
	private BluetoothAdapter mBluetoothAdapter;

	/** デバイス検知用レシーバー */
	private DeviceDiscoveryReceiver mDiscoveryReceiver;

	/** 普通のコンテキスト */
	private Context mContext;

	/** 本クラスのインスタンスを取得します。 */
	public static synchronized BluetoothManager getInstance(Context context) {
		LogUtil.d(BluetoothManager.class, "getInstance()");
		if (mInstance == null) {
			mInstance = new BluetoothManager(context);
		}
		return mInstance;
	}

	/** コンストラクタです。通常のコンテキストが必要です。 */
	private BluetoothManager(Context context) {
		mContext = context;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	/** デバイスがBluetoothをサポートしている場合trueを返します。 */
	public boolean isSupportedBluetooth() {
		LogUtil.d(this, "isSupportedBluetooth()");
		return mBluetoothAdapter != null ? true : false;
	}

	/** デバイスのBluetoothがONになっている場合trueを返します。 */
	public boolean isEnabled() {
		LogUtil.d(this, "isEnabled()");
		return isSupportedBluetooth() ? mBluetoothAdapter.isEnabled() : false;
	}

	/** Bluetoothの設定画面を開きます。 */
	public void startBluetoothRequestEnableActivity(Activity activity, int requestCode) {
		LogUtil.d(this, "startBluetoothRequestEnableActivity()");
		activity.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), requestCode);
	}

	/** 自デバイスの検出を指定した秒数の間だけ有効にするかどうかを問い合わせる画面を開きます。 */
	public void startBluetoothRequestDiscoverableActivity(Activity activity, int duration) {
		LogUtil.d(this, "startBluetoothRequestDiscoverableActivity()");
		Intent discoverableOn = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableOn.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
		activity.startActivity(discoverableOn);
	}

	/** ペアリングしたことのあるBluetooth端末のリストを取得します。 */
	public Set<BluetoothDevice> getBondedDevices() {
		LogUtil.d(this, "getBondedDevices()");
		return mBluetoothAdapter.getBondedDevices();
	}

	/**
	 * デバイスの検出を開始します。<br>
	 * 検出結果は指定したリスナーにより受け取ります。
	 */
	public void startDiscovery(OnDeviceDiscoveryListener listener) {
		LogUtil.d(this, "startDiscovery()");
		registDeviceDiscoveryReceiver(listener);
		stopDiscovery();
		mBluetoothAdapter.startDiscovery();
	}

	/**
	 * サーバ側で使用するソケットを取得します。 <br>
	 * ソケット取得に失敗した場合nullが返ります。
	 */
	public BluetoothServerSocket getServerSocket() {
		LogUtil.d(this, "getServerSocket()");
		try {
			return mBluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, UUID_FOR_BLUETOOTH);
		} catch (IOException e) {
			LogUtil.e(this, "getServerSocket failed");
		}
		return null;
	}

	/**
	 * サーバ側で使用するInsecureなソケットを取得します。 <br>
	 * ソケット取得に失敗した場合nullが返ります。<br>
	 * API LEVEL 10未満の端末で実行した場合もnullが返ります。
	 */
	@TargetApi(10)
	public BluetoothServerSocket getServerInsecureSocket() {
		LogUtil.d(this, "getServerInsecureSocket()");
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
			try {
				return mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME_INSECURE, UUID_FOR_BLUETOOTH_INSECURE);
			} catch (IOException e) {
				LogUtil.e(this, "getServerInsecureSocket failed");
			}
		}
		return null;
	}

	/**
	 * クライアント側で使用するソケットを取得します。<br>
	 * ソケット取得に失敗した場合nullが返ります。
	 */
	public BluetoothSocket getClientSocket(BluetoothDevice device) {
		LogUtil.d(this, "getClientSocket()");
		try {
			return device.createRfcommSocketToServiceRecord(UUID_FOR_BLUETOOTH);
		} catch (IOException e) {
			LogUtil.e(this, "getClientSocket failed");
		}
		return null;
	}

	/**
	 * クライアント側で使用するInsecureなソケットを取得します。<br>
	 * ソケット取得に失敗した場合nullが返ります。<br>
	 * API LEVEL 10未満の端末で実行した場合もnullが返ります。
	 */
	@TargetApi(10)
	public BluetoothSocket getClientInsecureSocket(BluetoothDevice device) {
		LogUtil.d(this, "getClientInsecureSocket()");
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
			try {
				return device.createInsecureRfcommSocketToServiceRecord(UUID_FOR_BLUETOOTH_INSECURE);
			} catch (IOException e) {
				LogUtil.e(this, "getClientInsecureSocket failed");
			}
		}
		return null;
	}

	/** 検索処理を強制的に中断させます。 */
	public void stopDiscovery() {
		LogUtil.d(this, "stopDiscovery()");
		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}
	}

	/** 端末検出用レシーバの登録をします。 */
	private void registDeviceDiscoveryReceiver(OnDeviceDiscoveryListener listener) {
		if (mDiscoveryReceiver == null) {
			mDiscoveryReceiver = new DeviceDiscoveryReceiver(listener);
			// インテントフィルターとBroadcastReceiverの登録
			IntentFilter filter = new IntentFilter();
			filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
			filter.addAction(BluetoothDevice.ACTION_FOUND);
			filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
			filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			mContext.registerReceiver(mDiscoveryReceiver, filter);
		}
	}

	/** 端末検出用レシーバの登録解除をします。 */
	private void unregistDeviceDiscoveryReceiver() {
		if (mDiscoveryReceiver != null) {
			try {
				mContext.unregisterReceiver(mDiscoveryReceiver);
			} catch (IllegalArgumentException e) {
				// do nothing
			}
			mDiscoveryReceiver = null;
		}
	}

	/** Bluetoothデバイスの検出を行うレシーバークラスです。 */
	private final class DeviceDiscoveryReceiver extends BroadcastReceiver {

		private OnDeviceDiscoveryListener mListener;

		public DeviceDiscoveryReceiver(OnDeviceDiscoveryListener listener) {
			LogUtil.d(this, "DeviceDiscoveryReceiver()");
			mListener = listener;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtil.d(this, "onReceive()");
			String action = intent.getAction();
			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				mListener.onStart();
			}
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				mListener.onFound((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
			}
			if (BluetoothDevice.ACTION_NAME_CHANGED.equals(action)) {
				mListener.onNameChanged((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
			}
			if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				mListener.onFinished();
				// 自身の登録解除を行う。
				unregistDeviceDiscoveryReceiver();
			}
		}
	}

}
