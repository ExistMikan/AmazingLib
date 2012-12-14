package com.amazinglib.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import dalvik.system.PathClassLoader;

public class RCollection {

	private static RCollection singleton;
	private ArrayList<Integer> drawableList = new ArrayList<Integer>();
	private HashMap<String, Integer> drawableMap = new HashMap<String, Integer>();

	// 自アプリケーションの場合
	public final Class<?>[] getMyClasses() {
		return com.example.amazinglib.R.class.getClasses();
	}

	private final Class<?>[] getDataClasses(Context context, String packageName) {
		Class<?> clss = null;
		try {
			ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, 0);
			ClassLoader loader = new PathClassLoader(info.sourceDir + ":", context.getClassLoader().getParent());
			clss = Class.forName(packageName + ".R", true, loader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clss.getClasses();
	}

	private RCollection(Context context, String packageName) {
		Class<?>[] classes = getDataClasses(context, packageName);
		for (Class<?> cls : classes) {
			if (cls.getSimpleName().equalsIgnoreCase("drawable")) {
				Field[] fields = cls.getFields();
				String name;
				for (Field field : fields) {
					try {
						name = field.getName();
						if (name.equals("icon")) {
							continue;
						}
						this.drawableMap.put(name, ((Integer) field.get(name)));
						this.drawableList.add((Integer) field.get(name));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static RCollection getInstance(Context context, String packageName) {
		if (singleton == null) {
			singleton = new RCollection(context, packageName);
		}
		return RCollection.singleton;
	}

	public ArrayList<Integer> getDrawableList() {
		return drawableList;
	}

	public HashMap<String, Integer> getDrawableMap() {
		return drawableMap;
	}

	public int getDrawableId(String name) {
		return drawableMap.get(name);
	}
}
