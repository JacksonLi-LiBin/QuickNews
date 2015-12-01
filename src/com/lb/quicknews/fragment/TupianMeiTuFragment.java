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
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.activity.BaseActivity;
import com.lb.quicknews.activity.PhotoDetailActivity_;
import com.lb.quicknews.adapter.CardsAnimationAdapter;
import com.lb.quicknews.adapter.PhotoAdapter;
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
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

@EFragment(R.layout.fragment_main)
public class TupianMeiTuFragment extends BaseFragment
		implements SwipeRefreshLayout.OnRefreshListener, OnSliderClickListener {
	SliderLayout mDemoSlider;
	@ViewById(R.id.swipe_container)
	SwipeRefreshLayout swipeLayout;
	@ViewById(R.id.listview)
	SwipeListView mListView;
	@ViewById(R.id.progressBar)
	ProgressBar mProgressBar;
	HashMap<String, String> url_maps;
	HashMap<String, PhotoModle> newHashMap;
	public int indexId;
	public int index;
	public int count = 0;
	public long lastupdatetimemeitu;
	@Bean
	PhotoAdapter photoAdapter;
	List<PhotoModle> listModles;
	private boolean isRefresh = false;
	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			String result = (String) msg.obj;
			switch (msg.what) {
			case 0:
				getMyActivity().setCacheStr("TupianMeiTuFragment", result);
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
		newHashMap = new HashMap<String, PhotoModle>();
		int count = PreferencesUtils.getInt(getActivity(), "indexmeitu");
		lastupdatetimemeitu = PreferencesUtils.getLong(getActivity(), "lastupdatetimemeitu");
		if (count != -1) {
			if ((lastupdatetimemeitu + (24 * 60 * 60 * 1000) < System.currentTimeMillis())) {
				lastupdatetimemeitu = System.currentTimeMillis();
				PreferencesUtils.putLong(getActivity(), "lastupdatetimemeitu", lastupdatetimemeitu);
				int beishu = (int) (System.currentTimeMillis() / (lastupdatetimemeitu + (24 * 60 * 60 * 1000)));
				count = (count + 5) * beishu;
			} else {
				indexId = count;
			}
		} else {
			indexId = 42262;
			PreferencesUtils.putLong(getActivity(), "lastupdatetimemeitu", System.currentTimeMillis());
			PreferencesUtils.putInt(getActivity(), "indexmeitu", indexId);
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
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(photoAdapter);
		animationAdapter.setAbsListView(mListView);
		mListView.setAdapter(animationAdapter);
		loadData(getMeiTuPicsUrl(indexId + ""));
		mListView.setOnBottomListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				currentPage++;
				indexId = indexId - 10;
				loadData(getMeiTuPicsUrl(indexId + ""));
			}
		});
	}

	@ItemClick(R.id.listview)
	void onItemClick(int position) {
		PhotoModle photoModle = listModles.get(position);
		Bundle bundle = new Bundle();
		bundle.putString("photoUrl", photoModle.getSeturl());
		((BaseActivity) getActivity()).openActivity(PhotoDetailActivity_.class, bundle, 0);
	}

	private void loadData(String url) {
		if (getMyActivity().hasNetWork()) {
			loadNewList(url);
		} else {
			mListView.onBottomComplete();
			mProgressBar.setVisibility(View.GONE);
			getMyActivity().showShortToast(getString(R.string.not_network));
			String result = getMyActivity().getCacheStr("TupianMeiTuFragment");
			if (!StringUtils.isEmpty(result)) {
				getResult(result);
			}
		}
	}

	@Background
	void loadNewList(String url) {
		String result;
		try {
			result = HttpUtil.getByHttpClient(getActivity(), url, null);
			getResult(result);
		} catch (Exception e) {
		}
	}

	@UiThread
	void getResult(String res) {
		Message msg = new Message();
		msg.what = 0;
		msg.obj = res;
		handler.sendMessage(msg);
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {

	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				currentPage = 1;
				isRefresh = true;
				index += 5;
				loadData(getMeiTuPicsUrl(index + ""));
				url_maps.clear();
				mDemoSlider.removeAllSliders();
				PreferencesUtils.putInt(getActivity(), "indexmeitu", index);
			}
		}, 2000);
	}

}
