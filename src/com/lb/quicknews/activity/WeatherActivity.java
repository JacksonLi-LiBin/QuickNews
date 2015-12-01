package com.lb.quicknews.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.adapter.MyViewPagerAdapter;
import com.lb.quicknews.adapter.WeatherAdapter;
import com.lb.quicknews.bean.WeatherModle;
import com.lb.quicknews.http.ResponseData;
import com.lb.quicknews.http.json.WeatherListJson;
import com.lb.quicknews.initview.SlidingMenuView;
import com.lb.quicknews.utils.StringUtils;
import com.lb.quicknews.utils.TimeUtils;
import com.lb.quicknews.utils.VolleyUtils;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EActivity(R.layout.activity_weather)
public class WeatherActivity extends BaseActivity implements ResponseData {
	@ViewById(R.id.title)
	TextView mTitle;
	@ViewById(R.id.local)
	TextView mLocal;
	@ViewById(R.id.layout)
	RelativeLayout mLayout;
	@ViewById(R.id.weatherTemp)
	TextView mWeatherTemp;
	@ViewById(R.id.weather)
	TextView mWeather;
	@ViewById(R.id.wind)
	TextView mWind;
	@ViewById(R.id.weatherImage)
	ImageView mWeatherImage;
	@ViewById(R.id.week)
	TextView mWeek;
	@ViewById(R.id.weather_date)
	TextView mWeatherDate;
	@ViewById(R.id.vPager)
	ViewPager mViewPager;
	private List<View> views;
	private View weatherGridView1, weatherGridView2;
	private GridView view1, view2;
	@Bean
	WeatherAdapter mWeatherAdapter1, mWeatherAdapter2;
	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			String result = (String) msg.obj;
			switch (msg.what) {
			case 0:
				setCacheStr("WeatherActivity", result);
				List<WeatherModle> weatherModles = WeatherListJson.getInstance(WeatherActivity.this)
						.readJsonWeatherForecastListModles(result);
				if (weatherModles.size() > 0) {
					setWeather(weatherModles.get(0));
					mWeatherAdapter1.clear();
					mWeatherAdapter2.clear();
					mWeatherAdapter1.appendList(weatherModles.subList(1, 4));
					mWeatherAdapter2.appendList(weatherModles.subList(4, weatherModles.size()));
				} else {
					showShortToast("错误");
				}
				break;

			default:
				break;
			}
			return false;
		}
	});

	@AfterInject
	void init() {
		views = new ArrayList<View>();
	}

	@AfterViews
	void initView() {
		try {
			initPager();
			String titleName = getCacheStr("titleName");
			if (titleName == null || StringUtils.isEmpty(titleName)) {
				titleName = "北京";
			}
			mTitle.setText(titleName + "天气");
			mLocal.setVisibility(View.VISIBLE);
			setBack(titleName);
			loadData(getWeatherUrl(titleName));
			mWeatherDate.setText(TimeUtils.getCurrentTime());
		} catch (Exception e) {
		}
	}

	private void loadData(String url) {
		if (hasNetWork()) {
			loadNewDetailData(url);
		} else {
			showShortToast(getString(R.string.not_network));
			String result = getCacheStr("WeatherActivity");
			if (!StringUtils.isEmpty(result)) {
				getResult(result);
			}
		}
	}

	@Background
	public void loadNewDetailData(String url) {
		try {
			VolleyUtils.getVolleyData(url, this, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@UiThread
	void getResult(String result) {
		Message msg = new Message();
		msg.what = 0;
		msg.obj = result;
		handler.sendMessage(msg);
	}

	private void initPager() {
		weatherGridView1 = LayoutInflater.from(this).inflate(R.layout.gridview_weather, null);
		weatherGridView2 = LayoutInflater.from(this).inflate(R.layout.gridview_weather, null);
		view1 = (GridView) weatherGridView1.findViewById(R.id.gridView);
		view2 = (GridView) weatherGridView2.findViewById(R.id.gridView);
		view1.setAdapter(mWeatherAdapter1);
		view2.setAdapter(mWeatherAdapter2);
		views.add(weatherGridView1);
		views.add(weatherGridView2);
		mViewPager.setOffscreenPageLimit(1);
		MyViewPagerAdapter mAdapetr = new MyViewPagerAdapter(views);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setCurrentItem(0);
	}

	private void setBack(String cityName) {
		if (cityName.equals("北京")) {
			mLayout.setBackgroundResource(R.drawable.biz_plugin_weather_beijin_bg);
		} else if (cityName.equals("上海")) {
			mLayout.setBackgroundResource(R.drawable.biz_plugin_weather_shanghai_bg);
		} else if (cityName.equals("广州")) {
			mLayout.setBackgroundResource(R.drawable.biz_plugin_weather_guangzhou_bg);
		} else if (cityName.equals("深圳")) {
			mLayout.setBackgroundResource(R.drawable.biz_plugin_weather_shenzhen_bg);
		} else {
			mLayout.setBackgroundResource(R.drawable.biz_news_local_weather_bg_big);
		}
	}

	private void setWeather(WeatherModle weatherModle) {
		mWeather.setText(weatherModle.getWeather());
		mWind.setText(weatherModle.getWind());
		mWeatherTemp.setText(weatherModle.getTemprature());
		mWeek.setText(weatherModle.getDate());
		SlidingMenuView.instance().setWeatherImage(mWeatherImage, weatherModle.getWeather());
	}

	@Override
	public void getResponseData(int id, String result) {
		getResult(result);
	}

}
