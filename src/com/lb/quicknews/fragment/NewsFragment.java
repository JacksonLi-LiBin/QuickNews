package com.lb.quicknews.fragment;

import java.util.HashMap;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.bean.NewsModle;
import com.lb.quicknews.wedget.swipelistview.SwipeListView;
import com.lb.quicknews.wedget.viewimage.Animations.SliderLayout;
import com.lb.quicknews.wedget.viewimage.SliderTypes.BaseSliderView;
import com.lb.quicknews.wedget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;

import android.R;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ProgressBar;

@EFragment(R.layout.fragment_news)
public class NewsFragment extends BaseFragment implements
		SwipeRefreshLayout.OnRefreshListener, OnSliderClickListener {
	SliderLayout mDemoSlider;
	@ViewById(R.id.swipe_container)
	SwipeRefreshLayout swipeLayout;
	@ViewById(R.id.listview)
	SwipeListView mListView;
	@ViewById(R.id.progressBar)
	ProgressBar mProgressBar;
	HashMap<String, String> url_maps;
	HashMap<String, NewsModle> newHashMap;

	// @Bean

	@Override
	public void onRefresh() {

	}

	@Override
	public void onSliderClick(BaseSliderView slider) {

	}

}
