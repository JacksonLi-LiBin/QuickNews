package com.lb.quicknews.http.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lb.quicknews.bean.WeatherModle;
import com.lb.quicknews.utils.TimeUtils;

import android.content.Context;

public class WeatherListJson extends JsonPacket {
	public List<WeatherModle> weatherModles = new ArrayList<WeatherModle>();
	private static WeatherListJson weatherListJson;

	public WeatherListJson(Context context) {
		super(context);
	}

	public static WeatherListJson getInstance(Context context) {
		if (weatherListJson == null) {
			weatherListJson = new WeatherListJson(context);
		}
		return weatherListJson;
	}

	public List<WeatherModle> readJsonWeatherForecastListModles(String res) {
		weatherModles.clear();
		try {
			if (res == null || res.equals("")) {
				return null;
			}
			WeatherModle weatherModle = null;
			JSONObject jsonObject = new JSONObject(res);
			JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("forecast");
			for (int i = 0; i < jsonArray.length(); i++) {
				weatherModle = readJsonWeatherModles(jsonArray.getJSONObject(i));
				weatherModles.add(weatherModle);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
		return weatherModles;
	}

	public List<WeatherModle> readJsonWeatherFutureListModles(String res) {
		weatherModles.clear();
		try {
			if (res == null || res.equals("")) {
				return null;
			}
			WeatherModle weatherModle = null;
			JSONObject jsonObject = new JSONObject(res);
			JSONObject jsonArray = jsonObject.getJSONObject("result").getJSONObject("future");
			for (int i = 0; i < 7; i++) {
				weatherModle = readJsonWeatherModles(jsonArray.getJSONObject("day_" + TimeUtils.dateToWeek(i)));
				weatherModles.add(weatherModle);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
		return weatherModles;
	}

	private WeatherModle readJsonWeatherModles(JSONObject jsonObject) throws Exception {
		WeatherModle weatherModle = null;
		String temperature = "";
		String weather = "";
		String wind = "";
		String date = "";
		temperature = getString("high", jsonObject) + " " + getString("low", jsonObject);
		weather = getString("type", jsonObject);
		wind = getString("fengxiang", jsonObject);
		date = getString("date", jsonObject);
		weatherModle = new WeatherModle();
		weatherModle.setDate(TimeUtils.getCurrentTime() + date);
		weatherModle.setTemprature(temperature);
		weatherModle.setWeather(weather);
		weatherModle.setWeek("");
		weatherModle.setWind(wind);
		return weatherModle;
	}
}
