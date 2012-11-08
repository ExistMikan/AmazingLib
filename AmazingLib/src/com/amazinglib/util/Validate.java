package com.amazinglib.util;

import java.util.Collection;

/**
 * 各種正当性検証のための処理が実装されているクラス。<br>
 * 検査を通らない場合は各種指定した名前を含めたメッセージと共に各種RuntimeExceptionがthrowされる。
 */
final public class Validate {

	/** オブジェクトがnullでないか検証 */
	public static void notNull(Object arg, String name) {
		if (arg == null) {
			throw new NullPointerException("Argument " + name + " cannot be null");
		}
	}

	/**
	 * コレクションが空でないか検証
	 * 
	 * @param container nullは許されない
	 */
	public static <T> void notEmpty(Collection<T> container, String name) {
		if (container.isEmpty()) {
			throw new IllegalArgumentException("Container '" + name + "' cannot be empty");
		}
	}

	/**
	 * コレクションにnullが含まれていないか検証
	 */
	public static <T> void containsNoNulls(Collection<T> container, String name) {
		Validate.notNull(container, name);
		for (T item : container) {
			if (item == null) {
				throw new NullPointerException("Container '" + name + "' cannot contain null values");
			}
		}
	}

	/** コレクションが、nullでなく、nullが含まれておらず、空でないかを検証 */
	public static <T> void notEmptyAndContainsNoNulls(Collection<T> container, String name) {
		Validate.containsNoNulls(container, name);
		Validate.notEmpty(container, name);
	}

	/** 文字列がnullでも空でも空白文字だけでもないことを検証 */
	public static void notNullOrEmpty(String arg, String name) {
		if (Utility.isNullOrEmpty(arg)) {
			throw new IllegalArgumentException("Argument " + name + " cannot be null or empty");
		}
	}

	/**
	 * 指定したオブジェクトが、検査範囲の中のどれかと一致するか検証
	 * 
	 * @param arg 検査対象のオブジェクト
	 * @param values 検査範囲
	 */
	public static void oneOf(Object arg, String name, Object... values) {
		for (Object value : values) {
			if (value != null) {
				if (value.equals(arg)) {
					return;
				}
			} else {
				if (arg == null) {
					return;
				}
			}
		}
		throw new IllegalArgumentException("Argument " + name + " was not one of the allowed values");
	}

}
