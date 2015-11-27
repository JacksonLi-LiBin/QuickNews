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
import com.lb.quicknews.adapter.CardsAnimationAdapter;
import com.lb.quicknews.adapter.PhotoAdapter;
import com.lb.quicknews.bean.NewsModle;
import com.lb.quicknews.bean.PhotoModle;
import com.lb.quicknews.http.HttpUtil;
import com.lb.quicknews.http.json.PhotoListJson;
import com.lb.quicknews.initview.InitView;
import com.lb.quicknews.utils.PreferencesUtils;
import com.lb.quicknews.utils.StringUtils;
import com.lb.quicknews.wedget.swipelistview.SwipeListView;
import com.lb.quicknews.wedget.viewimage.Animations.SliderLayout;
import com.lb.quicknews.wedget.viewimage.SliderTypes.BaseSliderView;
import com.lb.quicknews.wedget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
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
public class TupianFragment extends BaseFragment
		implements SwipeRefreshLayout.OnRefreshListener, OnSliderClickListener {

	SliderLayout mDemoSlider;
	@ViewById(R.id.swipe_container)
	SwipeRefreshLayout swipeLayout;
	@ViewById(R.id.listview)
	SwipeListView mListView;
	@ViewById(R.id.progressBar)
	ProgressBar mProgressBar;
	HashMap<String, String> url_maps;
	HashMap<String, NewsModle> newHashMap;
	public int indexId;
	public int index;
	public int count = 0;
	public long lastupdatetime;
	@Bean
	PhotoAdapter photoAdapter;
	List<PhotoModle> listModles;

	private boolean isRefresh = false;
	private Handler handler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String result = (String) msg.obj;
				try {
					getMyActivity().setCacheStr("TupianFragment" + currentPage, result);
					if (isRefresh) {
						isRefresh = false;
						photoAdapter.clear();
						listModles.clear();
					}
					mProgressBar.setVisibility(View.GONE);
					swipeLayout.setRefreshing(false);
					List<PhotoModle> list = PhotoListJson.getInstance(getActivity()).readJsonPhotoListModles(result);
					photoAdapter.appendList(list);
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
		listModles = new ArrayList<PhotoModle>();
		url_maps = new HashMap<String, String>();
		newHashMap = new HashMap<String, NewsModle>();
		int count = PreferencesUtils.getInt(getActivity(), "index");
		lastupdatetime = PreferencesUtils.getLong(getActivity(), "lastupdatetime");
		if (count != -1) {
			if ((lastupdatetime + (24 * 60 * 60 * 1000)) < System.currentTimeMillis()) {
				lastupdatetime = System.currentTimeMillis();
				PreferencesUtils.putLong(getActivity(), "lastupdatetime", lastupdatetime);
				int beishu = (int) (System.currentTimeMillis() / (lastupdatetime + (24 * 60 * 60 * 1000)));
				count = (count + 20) * beishu;
			} else {
				indexId = count;
			}
		} else {
			indexId = 43374;
			PreferencesUtils.putLong(getActivity(), "lastupdatetime", lastupdatetime);
			PreferencesUtils.putInt(getActivity(), "index", indexId);
		}
		index = indexId;
	}

	@AfterViews
	void initView() {
		swipeLayout.setOnRefreshListener(this);
		InitView.getInstance().initSwipeRefreshLayout(swipeLayout);
		InitView.getInstance().initListView(mListView, getActivity());
		View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_item, null);
		mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
		mListView.addHeaderView(headView);
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(photoAdapter);
		animationAdapter.setAbsListView(mListView);
		mListView.setAdapter(animationAdapter);
		loadData(getPhotosUrl(indexId + ""));
		mListView.setOnBottomListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPage++;
				indexId -= 10;
				loadData(getPhotosUrl(indexId + ""));
			}
		});
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				currentPage = 1;
				isRefresh = true;
				index += 5;
				loadData(getPhotosUrl(index + ""));
				url_maps.clear();
				mDemoSlider.removeAllSliders();
				PreferencesUtils.putInt(getActivity(), "index", index);
			}
		}, 2000);
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		// NewsModle newsModle = newHashMap.get(slider.getUrl());
		// enterDetailActivity(newsModle);
	}

	@ItemClick(R.id.listview)
	void onItemClick(int position) {
		PhotoModle photoModle = listModles.get(position);
		Bundle bundle = new Bundle();
		bundle.putString("photoUrl", photoModle.getSeturl());
		// ((BaseActivity)getActivity()).openActivity(pClass);
	}

	private void loadData(String url) {
		if (getMyActivity().hasNetWork()) {
			loadNewsList(url);
		} else {
			mListView.onBottomComplete();
			mProgressBar.setVisibility(View.GONE);
			getMyActivity().showShortToast(getString(R.string.not_network));
			String result = getMyActivity().getCacheStr("TupianFragment" + currentPage);
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
			getResult(result);
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
	//
	// private void initSliderLayout(List<NewsModle> newModles) {
	// if (!isNullString(newModles.get(0).getImgsrc()))
	// newHashMap.put(newModles.get(0).getImgsrc(), newModles.get(0));
	// if (!isNullString(newModles.get(1).getImgsrc()))
	// newHashMap.put(newModles.get(1).getImgsrc(), newModles.get(1));
	// if (!isNullString(newModles.get(2).getImgsrc()))
	// newHashMap.put(newModles.get(2).getImgsrc(), newModles.get(2));
	// if (!isNullString(newModles.get(3).getImgsrc()))
	// newHashMap.put(newModles.get(3).getImgsrc(), newModles.get(3));
	// if (!isNullString(newModles.get(0).getImgsrc()))
	// url_maps.put(newModles.get(0).getTitle(), newModles.get(0).getImgsrc());
	// if (!isNullString(newModles.get(1).getImgsrc()))
	// url_maps.put(newModles.get(1).getTitle(), newModles.get(1).getImgsrc());
	// if (!isNullString(newModles.get(2).getImgsrc()))
	// url_maps.put(newModles.get(2).getTitle(), newModles.get(2).getImgsrc());
	// if (!isNullString(newModles.get(3).getImgsrc()))
	// url_maps.put(newModles.get(3).getTitle(), newModles.get(3).getImgsrc());
	// for (String name : url_maps.keySet()) {
	// TextSliderView textSliderView = new TextSliderView(getActivity());
	// textSliderView.setOnSliderClickListener(this);
	// textSliderView.description(name).image(url_maps.get(name));
	// textSliderView.getBundle().putString("extra", name);
	// mDemoSlider.addSlider(textSliderView);
	// }
	// mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
	// mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
	// mDemoSlider.setCustomAnimation(new DescriptionAnimation());
	// newsAdapter.appendList(newModles);
	// }

	// public void enterDetailActivity(NewsModle newsModle) {
	// Bundle bundle = new Bundle();
	// bundle.putSerializable("newsModle", newsModle);
	// Class<?> class1;
	// if (newsModle.getImagesModle() != null &&
	// newsModle.getImagesModle().getImgList().size() > 1) {
	// class1 = ImageDetailActivity_.class;
	// } else {
	// class1 = DetailsActivity_.class;
	// }
	// ((BaseActivity) getActivity()).openActivity(class1, bundle, 0);
	// }
}
