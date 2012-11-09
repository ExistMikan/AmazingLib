package com.amazinglib.UIKit;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ALDialog {

	public static interface OnDialogButtonListener {
		public void onClick(boolean isOk);
	}

	/**
	 * OKボタンとCANCELボタンで構成されるダイアログを生成及び表示します。<br>
	 * このダイアログはキャンセルできません。<br>
	 * 各ボタン押下後はdismiss()メソッドが自動的に呼ばれます。<br>
	 * 
	 * @param act アクティビティインスタンス
	 * @param layoutId レイアウトID
	 * @param okButtonId OKボタンのリソースID
	 * @param cancelButtonId CANCELボタンのリソースID
	 * @param lisetener リスナーインスタンス
	 */
	public static void setOkCancelDialog(final Activity act, final int layoutId, final int okButtonId, final int cancelButtonId, final OnDialogButtonListener listener) {
		View layout = ((LayoutInflater) act.getSystemService(Activity.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(act);
		alertDialogBuilder.setInverseBackgroundForced(true).setCancelable(false).setView(layout);
		final AlertDialog alertDialog = alertDialogBuilder.create();
		layout.findViewById(okButtonId).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClick(true);
				}
				alertDialog.dismiss();
			}
		});
		layout.findViewById(cancelButtonId).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClick(false);
				}
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	/**
	 * OKボタンのみで構成されるダイアログを生成及び表示します。<br>
	 * このダイアログはキャンセルできません。<br>
	 * 各ボタン押下後はdismiss()メソッドが自動的に呼ばれます。<br>
	 * 
	 * @param act アクティビティインスタンス
	 * @param layoutId レイアウトID
	 * @param okButtonId OKボタンのリソースID
	 * @param lisetener リスナーインスタンス
	 */
	public static void setOkDialog(final Activity act, final int layoutId, final int okButtonId, final OnDialogButtonListener listener) {
		View layout = ((LayoutInflater) act.getSystemService(Activity.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(act);
		alertDialogBuilder.setInverseBackgroundForced(true).setCancelable(false).setView(layout);
		final AlertDialog alertDialog = alertDialogBuilder.create();
		layout.findViewById(okButtonId).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClick(true);
				}
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	/**
	 * OKボタンとテキストのみで構成されるダイアログを生成及び表示します。<br>
	 * このダイアログはキャンセルできません。<br>
	 * 各ボタン押下後はdismiss()メソッドが自動的に呼ばれます。<br>
	 * 
	 * @param act アクティビティインスタンス
	 * @param layoutId レイアウトID
	 * @param okButtonId OKボタンのリソースID
	 * @param textId テキストビューのリソースID
	 * @param text セットするテキスト
	 * @param lisetener リスナーインスタンス
	 */
	public static void setOkTextDialog(final Activity act, final int layoutId, final int okButtonId, final int textId, final String text,
			final OnDialogButtonListener listener) {
		View layout = ((LayoutInflater) act.getSystemService(Activity.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(act);
		alertDialogBuilder.setInverseBackgroundForced(true).setCancelable(false).setView(layout);
		final AlertDialog alertDialog = alertDialogBuilder.create();
		((TextView) layout.findViewById(textId)).setText(text);
		layout.findViewById(okButtonId).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClick(true);
				}
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

}
