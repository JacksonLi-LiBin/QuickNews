package com.lb.quicknews.adapter;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import com.lb.quicknews.bean.PictureModle;
import com.lb.quicknews.view.PhotoItemView;
import com.lb.quicknews.view.PhotoItemView_;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class PictureAdapter extends BaseAdapter {
	public List<PictureModle> lists = new ArrayList<PictureModle>();

	public void appendList(ArrayList<PictureModle> list) {
		if (!lists.containsAll(list) && list != null && list.size() > 0) {
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
		PictureModle pictureModle = lists.get(position);
		photoItemView.setData(pictureModle.getTitle(), pictureModle.getPic());
		return photoItemView;
	}

}
