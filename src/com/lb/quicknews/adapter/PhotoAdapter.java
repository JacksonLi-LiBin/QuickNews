package com.lb.quicknews.adapter;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import com.lb.quicknews.bean.PhotoModle;
import com.lb.quicknews.view.PhotoItemView;
import com.lb.quicknews.view.PhotoItemView_;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class PhotoAdapter extends BaseAdapter {
	public List<PhotoModle> lists = new ArrayList<PhotoModle>();

	public void appendList(List<PhotoModle> list) {
		if (!lists.contains(list) && list != null && list.size() > 0) {
			lists.addAll(list);
		}
		notifyDataSetChanged();
	}

	@RootContext
	Context context;

	public void clear() {
		lists.clear();
		notifyDataSetChanged();
	}

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
		PhotoItemView photoItemView;
		if (convertView == null) {
			photoItemView = PhotoItemView_.build(context);
		} else {
			photoItemView = (PhotoItemView) convertView;
		}
		PhotoModle photoModle = lists.get(position);
		photoItemView.setData(photoModle.getSetname(), photoModle.getClientcover());
		return photoItemView;
	}

}
