package com.amazinglib.UIKit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class ALAsyncTask {

	public static interface OnAsyncListener {
		public void onPreExecute();

		public void doInBackground();

		public void onPostExecute();
	}

	public static interface OnSimpleAsyncListener {
		public void onBackground();

		public void onFinish();
	}

	public static void doAsyncTask(OnAsyncListener listener) {
		new Task(listener).execute();
	}

	public static void doAsyncTask(OnSimpleAsyncListener listener) {
		new Task(listener).execute();
	}

	public static void doAsyncTaskWithDialog(Activity act, OnAsyncListener listener) {
		new TaskWithDialog(act, listener).execute();
	}

	public static void doAsyncTaskWithDialog(Activity act, OnSimpleAsyncListener listener) {
		new TaskWithDialog(act, listener).execute();
	}

	private static class Task extends AsyncTask<Void, Void, Void> {

		private OnAsyncListener mListener;
		private OnSimpleAsyncListener mSimpleListener;

		private boolean isSimple = true;

		public Task(OnAsyncListener listener) {
			mListener = listener;
			isSimple = false;
		}

		public Task(OnSimpleAsyncListener listener) {
			mSimpleListener = listener;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!isSimple) {
				mListener.onPreExecute();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (isSimple) {
				mSimpleListener.onBackground();
			} else {
				mListener.doInBackground();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (isSimple) {
				mSimpleListener.onFinish();
			} else {
				mListener.onPostExecute();
			}
		}

	}

	private static class TaskWithDialog extends AsyncTask<Void, Void, Void> {
		private OnAsyncListener mListener;
		private OnSimpleAsyncListener mSimpleListener;
		private ProgressDialog mDialog;
		private boolean isSimple = true;

		public TaskWithDialog(Activity act, OnSimpleAsyncListener listener) {
			mSimpleListener = listener;
			setup(act);
		}

		public TaskWithDialog(Activity act, OnAsyncListener listener) {
			mListener = listener;
			isSimple = false;
			setup(act);
		}

		private void setup(Activity act) {
			mDialog = new ProgressDialog(act);
			mDialog.setIndeterminate(true);
			mDialog.setCancelable(false);
			mDialog.setMessage("Now Loading...");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!isSimple) {
				mListener.onPreExecute();
			}
			mDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (isSimple) {
				mSimpleListener.onBackground();
			} else {
				mListener.doInBackground();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
			if (isSimple) {
				mSimpleListener.onFinish();
			} else {
				mListener.onPostExecute();
			}
		}

	}
}
