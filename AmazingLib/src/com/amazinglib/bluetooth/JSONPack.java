package com.amazinglib.bluetooth;

import java.io.Serializable;

public final class JSONPack implements Serializable {

	/**
	 * version1 = 1L。
	 */
	private static final long serialVersionUID = 1L;

	private final String json;

	public JSONPack(String json) {
		this.json = json;
	}

	public String getJson() {
		return json;
	}

}
