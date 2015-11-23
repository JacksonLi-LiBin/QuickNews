package com.lb.quicknews.adapter;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lb.quicknews.bean.NewsModle;
import com.lb.quicknews.view.NewItemView;
import com.lb.quicknews.view.NewItemView_;

@EBean
public class NewsAdapter extends BaseAdapter {
	public List<NewsModle> lists = new ArrayList<NewsModle>();
	private String currentItem;

	public void appendList(List<NewsModle> list) {
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

	public void currentItem(String item) {
		this.currentItem = item;
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
		NewItemView newItemView;
		if (convertView == null) {
			newItemView = NewItemView_.build(context);
		} else {
			newItemView = (NewItemView) convertView;
		}
		NewsModle newsModle = lists.get(position);
		if (newsModle.getImagesModle() == null) {
			newItemView.setTexts(newsModle.getTitle(), newsModle.getDigest(), newsModle.getImgsrc(), currentItem);
		}
		return newItemView;
	}

}
