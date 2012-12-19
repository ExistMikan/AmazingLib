package com.amazinglib.UIKit;

import android.app.Activity;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ALSeekBar {

	public static interface Listener extends OnSeekBarChangeListener {
	}

	public static SeekBar setSeekBar(Activity activity, int resourceId, int max, int defaultProgress, final Listener listener) {
		SeekBar seekBar = (SeekBar) activity.findViewById(resourceId);
		seekBar.setMax(max);
		seekBar.setProgress(defaultProgress);
		if (listener != null) {
			seekBar.setOnSeekBarChangeListener(listener);
		}
		return seekBar;
	}

}
