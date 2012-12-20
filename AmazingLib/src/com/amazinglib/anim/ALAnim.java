package com.amazinglib.anim;

import android.view.View;
import android.view.animation.AlphaAnimation;

public class ALAnim {

	/**
	 * だんだん表示されるアニメーションを開始します。<br>
	 * アニメーション終了後もビューは表示されたままです。
	 * 
	 * @param duration ミリ秒単位でアニメーションする時間を指定します。
	 */
	public static void toVisibleAnimStart(View v, long duration) {
		setTransAnim(v, 0F, 1F, duration);
	}

	/**
	 * だんだん消えていくアニメーションを開始します。 アニメーション終了後もビューは消えたままです。
	 * 
	 * @param duration ミリ秒単位でアニメーションする時間を指定します。
	 */
	public static void toInvisibleAnimStart(View v, long duration) {
		setTransAnim(v, 1F, 0F, duration);
	}

	/** 透明度を変更するアニメーションをビューに設定します。 */
	private static void setTransAnim(View v, float from, float to, long duration) {
		AlphaAnimation animation = new AlphaAnimation(from, to);
		animation.setDuration(duration);
		animation.setFillAfter(true);
		v.startAnimation(animation);
	}

}
