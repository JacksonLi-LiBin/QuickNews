package com.lb.quicknews.http.json;

import org.json.JSONObject;

import android.content.Context;

/**
 * Json解析抽象类
 * 
 * @author jacks
 *
 */
public abstract class JsonPacket {
	private final Context context;

	public JsonPacket(Context context) {
		this.context = context;
	}

	Context getContext() {
		return context;
	}

	public static String getString(String key, JSONObject jsonObject) throws Exception {
		String res = "";
		if (jsonObject.has(key)) {
			if (key == null) {
				return "";
			}
			res = jsonObject.getString(key);
		}
		return res;
	}

	public static Integer getInt(String key, JSONObject jsonObject) throws Exception {
		int res = -1;
		if (jsonObject.has(key)) {
			res = jsonObject.getInt(key);
		}
		return res;
	}

	public static double getDouble(String key, JSONObject jsonObject) throws Exception {
		double res = 0l;
		if (jsonObject.has(key)) {
			res = jsonObject.getDouble(key);
		}
		return res;
	}
}
