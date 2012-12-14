package com.amazinglib.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.amazinglib.util.LogUtil;
import com.example.amazinglib.R;

/**
 * 端末検出用ダイアログクラスです。<br>
 * TODO 重複している場合リストに追加しない機能追加
 */
public final class DiscoveryDeviceDialog {

	/** 選択されたデバイスをハンドリングするためのリスナーです。 */
	public static interface OnSelectedItemListener {
		/** デバイス選択時に呼び出されます。 */
		void onSelectedDevice(BluetoothDevice device);
	}

	/** Bluetoothに関する処理を行うマネージャー */
	private BluetoothManager mManager;

	/** ダイアログインスタンス */
	private AlertDialog mAlertDialog;

	/** デバイスリストビュー */
	private DeviceList mDeviceList;

	/** 検索ボタン */
	private Button mSearchButton;

	/** レイアウトのルートビュー */
	private LinearLayout mRoot;

	public DiscoveryDeviceDialog(Activity activity, OnSelectedItemListener listener) {
		LogUtil.d(this, "DiscoveryDeviceDialog()");
		mManager = BluetoothManager.getInstance(activity);
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.discovery_device, null);
		mRoot = layout;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setInverseBackgroundForced(true).setCancelable(true).setView(layout);
		mAlertDialog = alertDialogBuilder.create();
		mAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				mManager.stopDiscovery();
			}
		});
		setDeviceListView(layout, listener);
		setSearchButton(layout);
	}

	public void show() {
		LogUtil.d(this, "show()");
		mAlertDialog.show();
		mDeviceList.clear();
		mSearchButton.setEnabled(false);
		getPairedDevices();
		startDiscovery();
	}

	private void setDeviceListView(View view, final OnSelectedItemListener listener) {
		mDeviceList = (DeviceList) view.findViewById(R.id.devicelist);
		mDeviceList.setOnSelectedDeviceListener(new DeviceList.OnSelectedDeviceListener() {
			@Override
			public void onSelectedDevice(BluetoothDevice device) {
				LogUtil.d(this, "onSelectedDevice()");
				listener.onSelectedDevice(device);
				mAlertDialog.dismiss();
			}
		});
	}

	private void setSearchButton(View view) {
		mSearchButton = (Button) view.findViewById(R.id.search);
		mSearchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LogUtil.d(this, "onClick()");
				mDeviceList.clear();
				getPairedDevices();
				startDiscovery();
				mSearchButton.setEnabled(false);
			}
		});
	}

	/** 接続したことのあるデバイスの一覧を取得しリストに表示します */
	private void getPairedDevices() {
		mDeviceList.addDevices(mManager.getBondedDevices());
	}

	/** Bluetoothデバイスの検知を開始します */
	private void startDiscovery() {
		mManager.startDiscovery(new BluetoothManager.OnDeviceDiscoveryListener() {

			@Override
			public void onStart() {
				LogUtil.d(this, "onStart()");
				startProgress();
			}

			@Override
			public void onNameChanged(BluetoothDevice device) {
				LogUtil.d(this, "onNameChanged()");
				insertDevice(device);
			}

			@Override
			public void onFound(BluetoothDevice device) {
				LogUtil.d(this, "onFound()");
				insertDevice(device);
			}

			@Override
			public void onFinished() {
				LogUtil.d(this, "onFinished()");
				stopProgress();
				mSearchButton.setEnabled(true);
			}

			/**
			 * 取得した端末情報をリストに追加します。 <br>
			 * 接続したことのある端末(BOND_BONDED)は追加されません。
			 */
			private void insertDevice(BluetoothDevice device) {
				if (device.getName() != null) {
					if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
						mDeviceList.addDevice(device);
					}
				}
			}
		});
	}

	/** 進捗情報を表示します。 */
	private void startProgress() {
		mRoot.findViewById(R.id.progress).setVisibility(View.VISIBLE);
		mRoot.findViewById(R.id.progressText).setVisibility(View.VISIBLE);
	}

	/** 進捗情報を非表示にします。 */
	private void stopProgress() {
		mRoot.findViewById(R.id.progress).setVisibility(View.INVISIBLE);
		mRoot.findViewById(R.id.progressText).setVisibility(View.INVISIBLE);
	}
}
