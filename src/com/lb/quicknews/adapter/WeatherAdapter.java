package com.lb.quicknews.adapter;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import com.lb.quicknews.bean.WeatherModle;
import com.lb.quicknews.view.WeatherItemView;
import com.lb.quicknews.view.WeatherItemView_;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class WeatherAdapter extends BaseAdapter {
	public List<WeatherModle> lists = new ArrayList<WeatherModle>();

	public void appendList(List<WeatherModle> list) {
		if (!lists.containsAll(list) && list != null && list.size() > 0) {
			lists.addAll(list);
		}
		notifyDataSetChanged();
	}

	public void clear() {
		lists.clear();
		notifyDataSetChanged();
	}

	@RootContext
	Context context;

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WeatherItemView weatherItemView;
		if (convertView == null) {
			weatherItemView = WeatherItemView_.build(context);
		} else {
			weatherItemView = (WeatherItemView) convertView;
		}
		WeatherModle weatherModle = lists.get(position);
		weatherItemView.setData(weatherModle);
		return weatherItemView;
	}

}
