package com.amazinglib.compat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

final public class ALNotification {

	private Context mContext;
	private String mTicker;
	private String mText;
	private String mTitle;
	private int mIconResid;
	private PendingIntent mContentIntent;

	public ALNotification(Context context) {
		mContext = context;
	}

	public void setTicker(String ticker) {
		mTicker = ticker;
	}

	public void setText(String text) {
		mText = text;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public void setIcon(int resid) {
		mIconResid = resid;
	}

	public void setContentIntent(PendingIntent intent) {
		mContentIntent = intent;
	}

	@SuppressWarnings("deprecation")
	@TargetApi(16)
	public void notify(int notificationId) {
		Notification notification = null;
		if (CompatUtil.canUse(11)) {
			notification = new Notification(mIconResid, mTicker, System.currentTimeMillis());
			notification.setLatestEventInfo(mContext, mTitle, mText, mContentIntent);
		} else {
			Notification.Builder builder = new Notification.Builder(mContext);
			builder.setContentTitle(mTitle);
			builder.setContentText(mText);
			builder.setSmallIcon(mIconResid);
			builder.setContentIntent(mContentIntent);
			if (CompatUtil.canUse(16)) {
				notification = builder.build();
			} else {
				notification = builder.getNotification();
			}
		}
		NotificationManager manager = (NotificationManager) mContext.getSystemService(Activity.NOTIFICATION_SERVICE);
		manager.notify(notificationId, notification);
	}

	public PendingIntent makeSimpleContentIndent(Class<?> clazz) {
		Intent intent = new Intent(mContext, clazz);
		return PendingIntent.getActivity(mContext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
	}

}
