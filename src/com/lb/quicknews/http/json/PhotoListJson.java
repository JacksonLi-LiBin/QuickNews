package com.lb.quicknews.http.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lb.quicknews.bean.PhotoModle;

import android.content.Context;

public class PhotoListJson extends JsonPacket {
	public List<PhotoModle> photoModles = new ArrayList<PhotoModle>();
	public static PhotoListJson photoListJson;

	public PhotoListJson(Context context) {
		super(context);
	}

	public static PhotoListJson getInstance(Context context) {
		if (photoListJson == null) {
			photoListJson = new PhotoListJson(context);
		}
		return photoListJson;
	}

	public List<PhotoModle> readJsonPhotoListModles(String res) {
		photoModles.clear();
		try {
			if (res == null || res.equals("")) {
				return null;
			}
			PhotoModle photoModle = null;
			JSONArray jsonArray = new JSONArray(res);
			for (int i = 0; i < jsonArray.length(); i++) {
				photoModle = readJsonPhotoModle(jsonArray.getJSONObject(i));
				photoModles.add(photoModle);
			}
			return photoModles;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
		return null;
	}

	private PhotoModle readJsonPhotoModle(JSONObject jsonObject) throws Exception {
		PhotoModle photoModle = null;
		String setid = "";
		String seturl = "";
		String clientcover = "";
		String setname = "";
		setid = getString("setid", jsonObject);
		seturl = getString("seturl", jsonObject);
		clientcover = getString("clientcover1", jsonObject);
		setname = getString("datetime", jsonObject);
		setname = setname.split(" ")[0];
		photoModle = new PhotoModle();
		photoModle.setClientcover(clientcover);
		photoModle.setSetid(setid);
		photoModle.setSetname(setname);
		photoModle.setSeturl(seturl);
		return photoModle;
	}
}
