package com.lb.quicknews.adapter;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import com.lb.quicknews.bean.PictureDetailModle;
import com.lb.quicknews.view.PhotoDetailView;
import com.lb.quicknews.view.PhotoDetailView_;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class PictureDetailAdapter extends BaseAdapter {
	public List<PictureDetailModle> lists = new ArrayList<PictureDetailModle>();
	@RootContext
	Context context;

	public void appendList(List<PictureDetailModle> list) {
		if (!lists.containsAll(list) && list != null && list.size() > 0) {
			lists.addAll(list);
		}
		notifyDataSetChanged();
	}

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
		PhotoDetailView pictureDetailView;
		if (convertView == null) {
			pictureDetailView = PhotoDetailView_.build(context);
		} else {
			pictureDetailView = (PhotoDetailView) convertView;
		}
		PictureDetailModle pictureDetailModle = lists.get(position);
		pictureDetailView.setImage(lists.size(), position, pictureDetailModle.getAlt(), pictureDetailModle.getTitle(),
				pictureDetailModle.getPic());
		return pictureDetailView;
	}

}
