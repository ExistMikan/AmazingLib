package com.amazinglib.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.amazinglib.util.LogUtil;
import com.example.amazinglib.R;

/**
 * Bluetooth接続用クラスです。<br>
 * 接続待機スレッド(AcceptThread)<br>
 * 接続開始スレッド(ConnectThread)<br>
 * 接続維持スレッド(ConnectedThread)<br>
 * の3つのスレッドにより接続を管理します。
 * */
public final class ConnectManager {

	/** Bluetoothに関する処理を行うマネージャー */
	private BluetoothManager mManager;

	/** 接続に関するステータスを管理するハンドラー */
	private StatusHandler mStatusHandler;

	/** 接続待機用のスレッド */
	private AcceptThread mSecureAcceptThread;

	/** 接続待機用(Insecure)のスレッド */
	private AcceptThread mInsecureAcceptThread;

	/** 接続開始用のスレッド */
	private ConnectThread mConnectThread;

	/** 接続維持スレッド */
	private ConnectedThread mConnectedThread;

	/** 接続状態 */
	private int mState;

	/** 通常のコンテキスト */
	private Context mContext;

	/** 接続中のデバイス名 */
	private String mConnectedDeviceName = "";

	/** 本クラスよりBroadcastされるIntentに付加されるID */
	private final String mBroadcastId;

	public ConnectManager(Context context, StatusHandler handler, String id) {
		LogUtil.d(this, "ConnectManager()");
		mBroadcastId = id;
		mManager = BluetoothManager.getInstance(context);
		mState = StatusHandler.STATE_NONE;
		mStatusHandler = handler;
		mContext = context;
	}

	/** 接続待機用スレッドを起動します。 */
	public synchronized void start() {
		LogUtil.d(this, "start()");
		// 接続開始、接続維持用スレッドを停止する
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		setState(StatusHandler.STATE_LISTEN);
		// 接続待機用スレッドを生成し起動します(secure、Insecure両方)。
		if (mSecureAcceptThread == null) {
			mSecureAcceptThread = new AcceptThread(true);
			mSecureAcceptThread.start();
		}
		// バージョンに満たない端末ではInsecureスレッドを起動しない
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
			if (mInsecureAcceptThread == null) {
				mInsecureAcceptThread = new AcceptThread(false);
				mInsecureAcceptThread.start();
			}
		}
	}

	/** 全てのスレッドを停止します。 */
	public synchronized void stop() {
		LogUtil.d(this, "stop()");
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		if (mSecureAcceptThread != null) {
			mSecureAcceptThread.cancel();
			mSecureAcceptThread = null;
		}
		if (mInsecureAcceptThread != null) {
			mInsecureAcceptThread.cancel();
			mInsecureAcceptThread = null;
		}
		setState(StatusHandler.STATE_NONE);
	}

	/** 接続開始用スレッドを起動します。 */
	public synchronized void connect(BluetoothDevice device, boolean secure) {
		LogUtil.d(this, "connect()");
		LogUtil.argD(device.toString(), "device");
		// 起動している接続開始用スレッドを止める
		if (mState == StatusHandler.STATE_CONNECTING) {
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}
		// 接続維持スレッドも止める
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		setState(StatusHandler.STATE_CONNECTING);
		// 接続開始用スレッドを生成し起動
		mConnectThread = new ConnectThread(device, secure);
		mConnectThread.start();
	}

	/** 接続維持用スレッドを開始します。 */
	public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
		LogUtil.d(this, "connected()");
		// 稼働中のスレッドを全て止める。
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		if (mSecureAcceptThread != null) {
			mSecureAcceptThread.cancel();
			mSecureAcceptThread = null;
		}
		if (mInsecureAcceptThread != null) {
			mInsecureAcceptThread.cancel();
			mInsecureAcceptThread = null;
		}
		mConnectedDeviceName = device.getName();
		setState(StatusHandler.STATE_CONNECTED);
		// 接続維持用スレッドを生成し起動
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();
		mStatusHandler.sendDeviceName(mConnectedDeviceName);
	}

	/**
	 * 非同期で書き込み処理を行います。<br>
	 */
	public void write(byte[] out) {
		LogUtil.d(this, "write()");
		ConnectedThread r;
		synchronized (this) {
			if (mState != StatusHandler.STATE_CONNECTED) {
				return;
			}
			r = mConnectedThread;
		}
		r.write(out);
	}

	/** ステータスを取得します。 */
	public synchronized int getState() {
		LogUtil.d(this, "getState()");
		return mState;
	}

	/** ステータスを変更します。 */
	private synchronized void setState(int state) {
		LogUtil.d(this, "setState() " + StatusHandler.getStatusName(mState) + " -> " + StatusHandler.getStatusName(state));
		mState = state;
		mStatusHandler.sendStateChange(state);
		sendStatusBroadcast(state);
	}

	/** 接続に失敗した通知を出し、接続待機用スレッドを再起動します。 */
	private void connectionFailed() {
		mStatusHandler.sendToast(mContext.getString(R.string.notify_unable_connect));
		ConnectManager.this.start();
	}

	/** 接続が失われた通知を出し、接続待機用スレッドを再起動します。 */
	private void connectionLost() {
		mStatusHandler.sendToast(mContext.getString(R.string.notify_connect_lost));
		ConnectManager.this.start();
	}

	/**
	 * 接続待機のためのスレッドです。<br>
	 * サーバ側のソケットを取得して接続要求を待ち受けます。<br>
	 * 接続確立後は、接続維持のためのスレッドを起動します。
	 */
	private class AcceptThread extends Thread {

		private final BluetoothServerSocket mmServerSocket;

		private final boolean hasServerSocket;

		public AcceptThread(boolean secure) {
			LogUtil.d(this, "AcceptThread()");
			BluetoothServerSocket tmp = null;
			if (secure) {
				tmp = mManager.getServerSocket();
			} else {
				tmp = mManager.getServerInsecureSocket();
			}
			mmServerSocket = tmp;
			hasServerSocket = mmServerSocket != null ? true : false;
		}

		@Override
		public void run() {
			LogUtil.d(this, "run() : BEGIN AcceptThread");
			BluetoothSocket socket = null;
			// ソケット取得に失敗している場合stopによりNONEへ移行
			if (!hasServerSocket) {
				ConnectManager.this.stop();
				return;
			}
			while (mState != StatusHandler.STATE_CONNECTED) {
				try {
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					LogUtil.e(this, "accept() failed");
					break;
				}
				if (socket != null) {
					synchronized (ConnectManager.this) {
						switch (mState) {
						case StatusHandler.STATE_LISTEN:
						case StatusHandler.STATE_CONNECTING:
							connected(socket, socket.getRemoteDevice());
							break;
						case StatusHandler.STATE_NONE:
						case StatusHandler.STATE_CONNECTED:
							try {
								socket.close();
							} catch (IOException e) {
								LogUtil.e(this, "Could not close unwanted socket");
							}
							break;
						}
					}
				}
			}
		}

		/** 本スレッドの利用をキャンセルします。 */
		public void cancel() {
			LogUtil.d(this, "cancel()");
			try {
				if (mmServerSocket != null) {
					mmServerSocket.close();
				}
			} catch (IOException e) {
				LogUtil.e(this, "close() of server failed");
			}
		}
	}

	/**
	 * 接続開始のためのスレッドクラスです。<br>
	 * 接続先と接続するためのソケットを取得して接続を試みます。<br>
	 * 接続確立後は、接続維持のためのスレッドを起動します。
	 */
	private class ConnectThread extends Thread {

		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		private final boolean hasServerSocket;
		private boolean isCanceled = false;

		public ConnectThread(BluetoothDevice device, boolean secure) {
			LogUtil.d(this, "ConnectThread()");
			mmDevice = device;
			BluetoothSocket tmp = null;
			if (secure) {
				tmp = mManager.getClientSocket(device);
			} else {
				tmp = mManager.getClientInsecureSocket(device);
			}
			mmSocket = tmp;
			hasServerSocket = mmSocket != null ? true : false;
		}

		@Override
		public void run() {
			LogUtil.d(this, "run() : BEGIN ConnectThread");
			mManager.stopDiscovery();
			// ソケット取得に失敗している場合stopによりNONEへ移行
			if (!hasServerSocket) {
				ConnectManager.this.stop();
				return;
			}
			try {
				mmSocket.connect();
			} catch (IOException e) {
				try {
					mmSocket.close();
				} catch (IOException e2) {
					LogUtil.e(this, "unable to close() socket during connection failure");
				}
				// キャンセル指示が出ている場合はLISTENモードへ移行しない
				if (!isCanceled) {
					connectionFailed();
				}
				return;
			}
			synchronized (ConnectManager.this) {
				mConnectThread = null;
			}
			connected(mmSocket, mmDevice);
		}

		/** 本スレッドの利用をキャンセルします。 */
		public void cancel() {
			LogUtil.d(this, "cancel()");
			isCanceled = true;
			try {
				if (mmSocket != null) {
					mmSocket.close();
				}
			} catch (IOException e) {
				LogUtil.e(this, "close() of connect socket failed");
			}
		}
	}

	/**
	 * 接続維持のためのスレッドクラスです。<br>
	 * 入出力のストリームを保持し、情報の書き込み/読み込み処理を行います。
	 */
	private class ConnectedThread extends Thread {

		private final BluetoothSocket mmSocket;
		private final InputStream mmInput;
		private final OutputStream mmOutput;
		private boolean isCanceled = false;

		public ConnectedThread(BluetoothSocket socket) {
			LogUtil.d(this, "ConnectedThread()");
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				LogUtil.e(this, "temp sockets not created");
			}
			mmInput = tmpIn;
			mmOutput = tmpOut;
		}

		@Override
		public void run() {
			LogUtil.d(this, "run() : BEGIN ConnectedThread");
			// readLogicSimple();
			readLogicSerialize();
		}

		/**
		 * @deprecated
		 */
		@Deprecated
		@SuppressWarnings("unused")
		private void readLogicSimple() {
			LogUtil.e(this, "readLogicSimple()");
			byte[] buffer = new byte[1024];
			int bytes = 0;
			// 入力ストリームへ情報が流れてくるのを待ち受けます。
			while (true) {
				try {
					bytes = mmInput.read(buffer);
					// 情報取得後、読み込み処理を行います。
					mStatusHandler.sendRead(bytes, buffer);
				} catch (IOException e) {
					disconnected();
					break;
				}
			}
		}

		private void readLogicSerialize() {
			LogUtil.d(this, "readLogicSerialize()");
			while (true) {
				try {
					ObjectInputStream objreader = new ObjectInputStream(mmInput);
					Object o = objreader.readObject();
					if (o != null) {
						if (o instanceof JSONPack) {
							String json = ((JSONPack) o).getJson();
							byte[] buffer = json.getBytes();
							mStatusHandler.sendRead(buffer.length, buffer);
						}
					}
				} catch (StreamCorruptedException e) {
					disconnected();
					break;
				} catch (ClassNotFoundException e) {
					disconnected();
					break;
				} catch (IOException e) {
					disconnected();
					break;
				}
			}
		}

		private void disconnected() {
			LogUtil.e(this, "disconnected()");
			// 接続相手のデバイス名を消す
			synchronized (ConnectManager.this) {
				mConnectedDeviceName = "";
			}
			// キャンセル指示が出ている場合はLISTENモードへ移行しない
			if (!isCanceled) {
				connectionLost();
			}
		}

		/** 出力ストリームへの書き込みと、書き込み通知を行います。 */
		public void write(byte[] buffer) {
			LogUtil.d(this, "write()");
			// writeLogicSimple(buffer);
			writeLogicSerialize(buffer);
		}

		/**
		 * @deprecated
		 */
		@Deprecated
		@SuppressWarnings("unused")
		public void writeLogicSimple(byte[] buffer) {
			LogUtil.e(this, "writeLogicSimple()");
			try {
				mmOutput.write(buffer);
				mmOutput.flush();
				mStatusHandler.sendWrite(buffer);
			} catch (IOException e) {
				LogUtil.e(this, "Exception during write");
			}
		}

		private void writeLogicSerialize(byte[] buffer) {
			LogUtil.d(this, "writeLogicSerialize()");
			JSONPack pack = new JSONPack(new String(buffer));
			ObjectOutputStream objwriter;
			try {
				objwriter = new ObjectOutputStream(mmOutput);
				objwriter.writeObject(pack);
				objwriter.flush();
			} catch (IOException e) {
				LogUtil.e(this, "Exception during write");
			}
		}

		/** 本スレッドの利用をキャンセルします。 */
		public void cancel() {
			LogUtil.d(this, "cancel()");
			isCanceled = true;
			try {
				mmSocket.close();
			} catch (IOException e) {
				LogUtil.e(this, "close() of connect socket failed");
			}
		}

	}

	/** 状態通知のためのIntentをブロードキャストします。 */
	private void sendStatusBroadcast(int status) {
		Intent intent = new Intent();
		String action = "";
		switch (status) {
		case StatusHandler.STATE_LISTEN:
			action = ConnectStatusReceiver.ACTION_STATUS_LISTEN;
			break;
		case StatusHandler.STATE_CONNECTING:
			action = ConnectStatusReceiver.ACTION_STATUS_CONNECTING;
			break;
		case StatusHandler.STATE_NONE:
			action = ConnectStatusReceiver.ACTION_STATUS_NONE;
			break;
		case StatusHandler.STATE_CONNECTED:
			action = ConnectStatusReceiver.ACTION_STATUS_CONNECTED;
			intent.putExtra("device", mConnectedDeviceName);
			break;
		default:
			throw new IllegalArgumentException();
		}
		intent.setAction(action);
		intent.putExtra("id", mBroadcastId);
		mContext.sendBroadcast(intent);
	}

}
