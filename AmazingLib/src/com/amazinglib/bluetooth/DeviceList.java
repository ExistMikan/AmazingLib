package com.amazinglib.bluetooth;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Bluetooth端末をリスト表示するためのUIコンポーネントクラスです。<br>
 * アダプターを意識せず利用できるようにしてあります。
 */
public final class DeviceList extends ListView {

	/** 選択されたデバイスをハンドリングするためのリスナーです。 */
	public static interface OnSelectedDeviceListener {
		/** デバイス選択時に呼び出されます。 */
		void onSelectedDevice(BluetoothDevice device);
	}

	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			BluetoothDevice device = mDeviceList.get(position);
			mSelectedDeviceListener.onSelectedDevice(device);
		}
	};

	private OnSelectedDeviceListener mSelectedDeviceListener;
	private List<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
	private ArrayAdapter<String> mDeviceAdapter;

	public DeviceList(Context context) {
		super(context, null);
		init(context);
	}

	public DeviceList(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init(context);
	}

	public DeviceList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.setOnItemClickListener(mOnItemClickListener);
		this.mDeviceAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
		this.setAdapter(mDeviceAdapter);
	}

	public void setOnSelectedDeviceListener(OnSelectedDeviceListener listener) {
		this.mSelectedDeviceListener = listener;
	}

	public void addDevices(Set<BluetoothDevice> devices) {
		for (BluetoothDevice device : devices) {
			addDevice(device);
		}
	}

	public void addDevice(BluetoothDevice device) {
		mDeviceList.add(device);
		String item = device.getName() + " : " + device.getAddress();
		mDeviceAdapter.add(item);
	}

	public void clear() {
		mDeviceAdapter.clear();
	}

}
