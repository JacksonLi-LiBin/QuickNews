package com.lb.quicknews.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.bean.WeatherModle;
import com.lb.quicknews.initview.SlidingMenuView;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@EViewGroup(R.layout.item_weather)
public class WeatherItemView extends LinearLayout {
	@ViewById(R.id.week)
	TextView mWeek;
	@ViewById(R.id.weather_image)
	ImageView mWeatherImage;
	@ViewById(R.id.temperature)
	TextView mTemperature;
	@ViewById(R.id.weather)
	TextView mWeather;
	@ViewById(R.id.wind)
	TextView mWind;

	public WeatherItemView(Context context) {
		super(context);
	}

	public void setData(WeatherModle weatherModle) {
		try {
			mWeek.setText("星" + weatherModle.getDate().split("日星")[1]);
			mTemperature.setText(weatherModle.getTemprature().replace("低温", "~").split("高温")[1]);
			mWeather.setText(weatherModle.getWeather());
			mWind.setText(weatherModle.getWind());
			SlidingMenuView.instance().setWeatherImage(mWeatherImage, weatherModle.getWeather());
		} catch (Exception e) {
		}
	}
}
