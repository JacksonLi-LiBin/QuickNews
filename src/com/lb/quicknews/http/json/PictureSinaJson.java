package com.lb.quicknews.http.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lb.quicknews.bean.PictureDetailModle;
import com.lb.quicknews.bean.PictureModle;

import android.content.Context;

public class PictureSinaJson extends JsonPacket {
	private final List<PictureModle> pictureModles = new ArrayList<PictureModle>();
	private List<PictureDetailModle> pictureDetailModles;
	public static PictureSinaJson pictureJson;
	private String title;

	public PictureSinaJson(Context context) {
		super(context);
	}

	public static PictureSinaJson getInstance(Context context) {
		if (pictureJson == null) {
			pictureJson = new PictureSinaJson(context);
		}
		return pictureJson;
	}

	public List<PictureDetailModle> readJsonPictureModle(String res) {
		pictureDetailModles = new ArrayList<PictureDetailModle>();
		try {
			if (res == null || res.equals("")) {
				return null;
			}
			PictureDetailModle pictureDetailModle = null;
			JSONObject jsonObject = new JSONObject(res).getJSONObject("data");
			title = getString("title", jsonObject);
			JSONArray jsonArray = jsonObject.getJSONArray("pics");
			for (int i = 0; i < jsonArray.length(); i++) {
				pictureDetailModle = readJsonPictureDetailModle(jsonArray.getJSONObject(i));
				pictureDetailModles.add(pictureDetailModle);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
		return pictureDetailModles;
	}

	private PictureDetailModle readJsonPictureDetailModle(JSONObject jsonObject) throws Exception {
		PictureDetailModle pictureDetailModle = null;
		String pic = "";
		String alt = "";
		pic = getString("pic", jsonObject);
		alt = getString("alt", jsonObject);
		pictureDetailModle = new PictureDetailModle();
		pictureDetailModle.setAlt(alt);
		pictureDetailModle.setPic(pic);
		pictureDetailModle.setTitle(title);
		return pictureDetailModle;
	}

	public List<PictureModle> readJsonPictureListModles(String res) {
		pictureModles.clear();
		try {
			if (res == null || res.equals("")) {
				return null;
			}
			PictureModle pictureModle = null;
			JSONArray jsonArray = new JSONObject(res).getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				pictureModle = readJsonPictureModle(jsonArray.getJSONObject(i));
				pictureModles.add(pictureModle);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
		return pictureModles;
	}

	private PictureModle readJsonPictureModle(JSONObject jsonObject) throws Exception {
		PictureModle pictureModle = null;
		String id = "";
		String title = "";
		String pic = "";
		id = getString("id", jsonObject);
		title = getString("title", jsonObject);
		pic = getString("pic", jsonObject);
		pictureModle = new PictureModle();
		pictureModle.setId(id);
		pictureModle.setPic(pic);
		pictureModle.setTitle(title);
		return pictureModle;
	}
}
