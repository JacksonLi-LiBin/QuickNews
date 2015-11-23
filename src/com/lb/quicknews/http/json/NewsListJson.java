package com.lb.quicknews.http.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.lb.quicknews.bean.ImagesModle;
import com.lb.quicknews.bean.NewsModle;

public class NewsListJson extends JsonPacket {
	public static NewsListJson newsListJson;
	public List<NewsModle> newsModles;

	public NewsListJson(Context context) {
		super(context);
	}

	public static NewsListJson getInstance(Context context) {
		if (newsListJson == null) {
			newsListJson = new NewsListJson(context);
		}
		return newsListJson;
	}

	public List<NewsModle> readJsonNewsModles(String res, String value) {
		newsModles = new ArrayList<NewsModle>();
		try {
			if (res == null || res.equals("")) {
				return null;
			}
			NewsModle newsModle = null;
			JSONObject jsonObject = new JSONObject(res);
			JSONArray jsonArray = jsonObject.getJSONArray(value);
			for (int i = 0; i < jsonArray.length(); i++) {
				newsModle = new NewsModle();
				JSONObject jo = jsonArray.getJSONObject(i);
				if (jo.has("skipType") && jo.getString("skipType").equals("special")) {
					continue;
				}
				if (jo.has("TAGS") && jo.has("TAG")) {
					continue;
				}
				if (jo.has("imgextra")) {
					newsModle.setTitle(getString("title", jo));
					newsModle.setDocid(getString("docid", jo));
					ImagesModle imagesModle = new ImagesModle();
					List<String> list;
					list = readImgList(jo.getJSONArray("imgextra"));
					list.add(getString("imgsrc", jo));
					imagesModle.setImgList(list);
					newsModle.setImagesModle(imagesModle);
				} else {
					newsModle = readNewsModle(jo);
				}
				newsModles.add(newsModle);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
		return newsModles;
	}

	/**
	 * 解析图片集
	 */
	public List<String> readImgList(JSONArray jsonArray) throws Exception {
		List<String> imgList = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			imgList.add(getString("imgsrc", jsonArray.getJSONObject(i)));
		}
		return imgList;
	}

	/**
	 * 获取图文列表
	 */
	public NewsModle readNewsModle(JSONObject jsonObject) throws Exception {
		NewsModle newsModle = null;
		String docid = "";
		String title = "";
		String digest = "";
		String imgsrc = "";
		String source = "";
		String ptime = "";
		String tag = "";
		docid = getString("docid", jsonObject);
		title = getString("title", jsonObject);
		digest = getString("digest", jsonObject);
		imgsrc = getString("imgsrc", jsonObject);
		source = getString("source", jsonObject);
		ptime = getString("ptime", jsonObject);
		tag = getString("TAG", jsonObject);
		newsModle = new NewsModle();
		newsModle.setDocid(docid);
		newsModle.setTitle(title);
		newsModle.setDigest(digest);
		newsModle.setImgsrc(imgsrc);
		newsModle.setSource(source);
		newsModle.setPtime(ptime);
		newsModle.setTag(tag);
		return newsModle;
	}
}
