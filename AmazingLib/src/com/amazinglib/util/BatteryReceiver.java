package com.amazinglib.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BatteryReceiver extends BroadcastReceiver {

	private static final String CLASS = "BatteryReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtil.d(CLASS, "onReceive()");

		String action = intent.getAction();
		if (action != null && action.equals(Intent.ACTION_BATTERY_CHANGED)) {
			// 残量
			int level = intent.getIntExtra("level", 0);
			// 状況 0:未接続 1: AC 2:USB
			int plugged = intent.getIntExtra("plugged", 0);

			LogUtil.argD("" + level, "level");
			LogUtil.argD("" + plugged, "plugged");
		}
	}

}
