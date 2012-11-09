package com.amazinglib.UIKit;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ALListView {

	/** 各Mapには必ず"title"キーでString,"image"キーでDrawableオブジェクトを含める必要がある。 */
	public static ListAdapter createImageAndTextAdapter(Context context, List<Map<String, Object>> datas) {
		return new ImageAndTextAdapter(context, datas);
	}

	private static class ImageAndTextAdapter extends BaseAdapter {

		List<Map<String, Object>> mDatas;
		Context mContext;

		public ImageAndTextAdapter(Context context, List<Map<String, Object>> datas) {
			mContext = context;
			mDatas = datas;
		}

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public Object getItem(int position) {
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder tag = null;
			if (convertView == null) {
				tag = new Holder();
				LinearLayout layout = new LinearLayout(mContext);
				tag.mImage = new ImageView(mContext);
				tag.mImage.setLayoutParams(new LinearLayout.LayoutParams(pixel2dip(50), pixel2dip(50)));
				tag.mText = new TextView(mContext);
				tag.mText.setHorizontallyScrolling(true);
				layout.setGravity(Gravity.CENTER_VERTICAL);
				layout.addView(tag.mImage);
				layout.addView(tag.mText);
				layout.setTag(tag);
				convertView = layout;
			} else {
				tag = (Holder) convertView.getTag();
			}

			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>) getItem(position);

			Drawable image = (Drawable) data.get("image");
			String title = (String) data.get("title");

			ALImageView.setBackground(tag.mImage, image);
			tag.mText.setText(title);

			return convertView;
		}

		private int pixel2dip(int pixel) {
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixel, mContext.getResources().getDisplayMetrics());
		}

		private class Holder {
			ImageView mImage;
			TextView mText;
		}

	}
}
