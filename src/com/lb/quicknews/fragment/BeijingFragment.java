package com.lb.quicknews.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.activity.BaseActivity;
import com.lb.quicknews.activity.DetailsActivity_;
import com.lb.quicknews.activity.ImageDetailActivity_;
import com.lb.quicknews.adapter.CardsAnimationAdapter;
import com.lb.quicknews.adapter.NewsAdapter;
import com.lb.quicknews.bean.NewsModle;
import com.lb.quicknews.http.HttpUtil;
import com.lb.quicknews.http.Url;
import com.lb.quicknews.http.json.NewsListJson;
import com.lb.quicknews.initview.InitView;
import com.lb.quicknews.utils.StringUtils;
import com.lb.quicknews.wedget.swipelistview.SwipeListView;
import com.lb.quicknews.wedget.viewimage.Animations.DescriptionAnimation;
import com.lb.quicknews.wedget.viewimage.Animations.SliderLayout;
import com.lb.quicknews.wedget.viewimage.SliderTypes.BaseSliderView;
import com.lb.quicknews.wedget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.lb.quicknews.wedget.viewimage.SliderTypes.TextSliderView;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

@EFragment(R.layout.fragment_main)
public class BeijingFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnSliderClickListener {

	SliderLayout mDemoSlider;
	@ViewById(R.id.swipe_container)
	SwipeRefreshLayout swipeLayout;
	@ViewById(R.id.listview)
	SwipeListView mListView;
	@ViewById(R.id.progressBar)
	ProgressBar mProgressBar;
	HashMap<String, String> url_maps;
	HashMap<String, NewsModle> newHashMap;

	@Bean
	NewsAdapter newsAdapter;
	List<NewsModle> listModles;
	private int index = 0;
	private boolean isRefresh = false;
	private Handler handler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String result = (String) msg.obj;
				try {
					getMyActivity().setCacheStr("BeijingFragment" + currentPage, result);
					if (isRefresh) {
						isRefresh = false;
						newsAdapter.clear();
						listModles.clear();
					}
					mProgressBar.setVisibility(View.GONE);
					swipeLayout.setRefreshing(false);
					List<NewsModle> list = NewsListJson.getInstance(getActivity()).readJsonNewsModles(result,
							Url.BeiJingId);
					if (index == 0 && list.size() >= 4) {
						initSliderLayout(list);
					} else {
						newsAdapter.appendList(list);
					}
					listModles.addAll(list);
					mListView.onBottomComplete();
				} catch (Exception e) {
					e.printStackTrace();
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
		listModles = new ArrayList<NewsModle>();
		url_maps = new HashMap<String, String>();
		newHashMap = new HashMap<String, NewsModle>();
	}

	@AfterViews
	void initView() {
		swipeLayout.setOnRefreshListener(this);
		InitView.getInstance().initSwipeRefreshLayout(swipeLayout);
		InitView.getInstance().initListView(mListView, getActivity());
		View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_item, null);
		mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
		mListView.addHeaderView(headView);
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(newsAdapter);
		animationAdapter.setAbsListView(mListView);
		mListView.setAdapter(animationAdapter);
		loadData(getCommonUrl(index + "", Url.BeiJingId));
		mListView.setOnBottomListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPage++;
				index += 20;
				loadData(getCommonUrl(index + "", Url.BeiJingId));
			}
		});
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				index = 0;
				currentPage = 1;
				isRefresh = true;
				loadData(getCommonUrl(index + "", Url.BeiJingId));
				url_maps.clear();
				mDemoSlider.removeAllSliders();
			}
		}, 2000);
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		NewsModle newsModle = newHashMap.get(slider.getUrl());
		enterDetailActivity(newsModle);
	}

	@ItemClick(R.id.listview)
	void onItemClick(int position) {
		NewsModle newsModle = listModles.get(position - 1);
		enterDetailActivity(newsModle);
	}

	private void loadData(String url) {
		if (getMyActivity().hasNetWork()) {
			loadNewsList(url);
		} else {
			mListView.onBottomComplete();
			mProgressBar.setVisibility(View.GONE);
			getMyActivity().showShortToast(getString(R.string.not_network));
			String result = getMyActivity().getCacheStr("BeijingFragment" + currentPage);
			if (!StringUtils.isEmpty(result)) {
				getResult(result);
			}
		}
	}

	@Background
	void loadNewsList(String url) {
		String result;
		try {
			result = HttpUtil.getByHttpClient(getActivity(), url);
			if (!StringUtils.isEmpty(result)) {
				getResult(result);
			} else {
				swipeLayout.setRefreshing(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@UiThread
	public void getResult(String result) {
		Message msg = new Message();
		msg.what = 0;
		msg.obj = result;
		handler.sendMessage(msg);
	}

	private void initSliderLayout(List<NewsModle> newModles) {
		if (!isNullString(newModles.get(0).getImgsrc()))
			newHashMap.put(newModles.get(0).getImgsrc(), newModles.get(0));
		if (!isNullString(newModles.get(1).getImgsrc()))
			newHashMap.put(newModles.get(1).getImgsrc(), newModles.get(1));
		if (!isNullString(newModles.get(2).getImgsrc()))
			newHashMap.put(newModles.get(2).getImgsrc(), newModles.get(2));
		if (!isNullString(newModles.get(3).getImgsrc()))
			newHashMap.put(newModles.get(3).getImgsrc(), newModles.get(3));
		if (!isNullString(newModles.get(0).getImgsrc()))
			url_maps.put(newModles.get(0).getTitle(), newModles.get(0).getImgsrc());
		if (!isNullString(newModles.get(1).getImgsrc()))
			url_maps.put(newModles.get(1).getTitle(), newModles.get(1).getImgsrc());
		if (!isNullString(newModles.get(2).getImgsrc()))
			url_maps.put(newModles.get(2).getTitle(), newModles.get(2).getImgsrc());
		if (!isNullString(newModles.get(3).getImgsrc()))
			url_maps.put(newModles.get(3).getTitle(), newModles.get(3).getImgsrc());
		for (String name : url_maps.keySet()) {
			TextSliderView textSliderView = new TextSliderView(getActivity());
			textSliderView.setOnSliderClickListener(this);
			textSliderView.description(name).image(url_maps.get(name));
			textSliderView.getBundle().putString("extra", name);
			mDemoSlider.addSlider(textSliderView);
		}
		mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
		mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
		mDemoSlider.setCustomAnimation(new DescriptionAnimation());
		newsAdapter.appendList(newModles);
	}

	public void enterDetailActivity(NewsModle newsModle) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("newsModle", newsModle);
		Class<?> class1;
		if (newsModle.getImagesModle() != null && newsModle.getImagesModle().getImgList().size() > 1) {
			class1 = ImageDetailActivity_.class;
		} else {
			class1 = DetailsActivity_.class;
		}
		((BaseActivity) getActivity()).openActivity(class1, bundle, 0);
	}
}