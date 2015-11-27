package com.lb.quicknews.http.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lb.quicknews.bean.NewsDetailModle;

import android.content.Context;

public class NewsDetailJson extends JsonPacket {
	public static NewsDetailJson newsDetailJson;
	public NewsDetailModle newsDetailModle;

	public NewsDetailJson(Context context) {
		super(context);
	}

	public static NewsDetailJson getInstance(Context context) {
		if (newsDetailJson == null) {
			newsDetailJson = new NewsDetailJson(context);
		}
		return newsDetailJson;
	}

	public NewsDetailModle readjsonNewsModles(String res, String newId) {
		try {
			if (res == null || res.equals("")) {
				return null;
			}
			JSONObject jsonObject = new JSONObject(res).getJSONObject(newId);
			newsDetailModle = readNewsModle(jsonObject);
		} catch (Exception e) {
		} finally {
			System.gc();
		}
		return newsDetailModle;
	}

	public NewsDetailModle readNewsModle(JSONObject jsonObject)
			throws Exception {
		NewsDetailModle newsDetailModle = null;
		String docid = "";
		String title = "";
		String source = "";
		String ptime = "";
		String body = "";
		String url_mp4 = "";
		String cover = "";
		docid = getString("docid", jsonObject);
		title = getString("title", jsonObject);
		source = getString("source", jsonObject);
		ptime = getString("ptime", jsonObject);
		body = getString("body", jsonObject);
		if (jsonObject.has("video")) {
			JSONObject jsonObje = jsonObject.getJSONArray("video")
					.getJSONObject(0);
			url_mp4 = getString("url_mp4", jsonObje);
			cover = getString("cover", jsonObje);
		}
		JSONArray jsonArray = jsonObject.getJSONArray("img");
		List<String> imgList = readImgList(jsonArray);
		newsDetailModle = new NewsDetailModle();
		newsDetailModle.setDocid(docid);
		newsDetailModle.setImgList(imgList);
		newsDetailModle.setPtime(ptime);
		newsDetailModle.setSource(source);
		newsDetailModle.setTitle(title);
		newsDetailModle.setBody(body);
		newsDetailModle.setUrl_mp4(url_mp4);
		newsDetailModle.setCover(cover);
		return newsDetailModle;
	}

	public List<String> readImgList(JSONArray jsonArray) throws Exception {
		List<String> imgList = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			imgList.add(getString("src", jsonArray.getJSONObject(i)));
		}
		return imgList;
	}
}
