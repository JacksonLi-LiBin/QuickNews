package com.lb.quicknews.http.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lb.quicknews.bean.VideoModle;

import android.content.Context;

public class VideoListJson extends JsonPacket {
	private static VideoListJson videoListJson;
	private List<VideoModle> videoModles;

	public VideoListJson(Context context) {
		super(context);
	}

	public static VideoListJson getInstance(Context context) {
		if (videoListJson == null) {
			videoListJson = new VideoListJson(context);
		}
		return videoListJson;
	}

	public List<VideoModle> readJsonVideoModles(String res, String value) {
		videoModles = new ArrayList<VideoModle>();
		try {
			if (res == null || res.equals("")) {
				return null;
			}
			VideoModle videoModle = null;
			JSONObject jsonObject = new JSONObject(res);
			JSONArray jsonArray = jsonObject.getJSONArray(value);
			for (int i = 0; i < jsonArray.length(); i++) {
				videoModle = readVideoModle(jsonArray.getJSONObject(i));
				videoModles.add(videoModle);
			}
		} catch (Exception e) {
		} finally {
			System.gc();
		}
		return videoModles;
	}

	private VideoModle readVideoModle(JSONObject jsonObject) throws Exception {
		VideoModle videoModle = null;
		String vid = "";
		String title = "";
		int length = 0;
		String cover = "";
		String mp4Hd_url = "";
		vid = getString("vid", jsonObject);
		title = getString("title", jsonObject);
		length = getInt("length", jsonObject);
		cover = getString("cover", jsonObject);
		mp4Hd_url = getString("mp4_url", jsonObject);
		videoModle = new VideoModle();
		videoModle.setCover(cover);
		if (length == -1) {
			videoModle.setTitle(getString("sectiontitle", jsonObject));
			videoModle.setLength(getTitle(title));
		} else {
			videoModle.setLength(getTime(length));
			videoModle.setTitle(getTitle(title));
		}
		videoModle.setMp4Hd_url(mp4Hd_url);
		videoModle.setVid(vid);
		return videoModle;
	}

	private String getTitle(String title) {
		if (title.contains("&quot;")) {
			title = title.replace("&quot;", "\"");
		}
		return title;
	}

	private String getTime(int length) {
		int fen = length / 60;
		int miao = length % 60;
		String fenString = fen + "";
		String miaoString = miao + "";
		fenString = fenString.length() == 1 ? "0" + fenString : fenString;
		miaoString = miaoString.length() == 1 ? "0" + miaoString : miaoString;
		return fenString + ":" + miaoString;
	}
}
