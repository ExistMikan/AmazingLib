package com.amazinglib.compat;

import android.os.Build.VERSION;

public class CompatUtil {

	public static boolean canUse(int deprecatedVersion) {
		int myVersion = VERSION.SDK_INT;
		if (deprecatedVersion >= myVersion) {
			return false;
		} else {
			return true;
		}
	}

}
