package com.lb.quicknews.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
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

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

@EFragment(R.layout.fragment_main)
public class TupianDuJiaFragment extends BaseFragment
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
	public long lastupdatetimedujia;
	@Bean
	protected PhotoAdapter photoAdapter;
	protected List<PhotoModle> listModles;
	private boolean isRefresh = false;

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			String result = (String) msg.obj;
			switch (msg.what) {
			case 0:
				getMyActivity().setCacheStr("TupianDuJiaFragment" + currentPage, result);
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
	protected void init() {
		listModles = new ArrayList<PhotoModle>();
		url_maps = new HashMap<String, String>();
		newHashMap = new HashMap<String, PhotoModle>();

		int count = PreferencesUtils.getInt(getActivity(), "indexdujia");
		lastupdatetimedujia = PreferencesUtils.getLong(getActivity(), "lastupdatetimedujia");
		if (count != -1) {
			if ((lastupdatetimedujia + (24 * 60 * 60 * 1000)) < System.currentTimeMillis()) {
				lastupdatetimedujia = System.currentTimeMillis();
				PreferencesUtils.putLong(getActivity(), "lastupdatetimedujia", lastupdatetimedujia);
				int beishu = (int) (System.currentTimeMillis() / (lastupdatetimedujia + (24 * 60 * 60 * 1000)));
				count = (count + 10) * beishu;
			} else {
				indexId = count;
			}
		} else {
			indexId = 42011;
			PreferencesUtils.putLong(getActivity(), "lastupdatetimedujia", System.currentTimeMillis());
			PreferencesUtils.putInt(getActivity(), "indexdujia", indexId);
		}

		index = indexId;
	}

	@AfterViews
	protected void initView() {
		swipeLayout.setOnRefreshListener(this);
		InitView.getInstance().initSwipeRefreshLayout(swipeLayout);
		InitView.getInstance().initListView(mListView, getActivity());
		View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_item, null);
		mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(photoAdapter);
		animationAdapter.setAbsListView(mListView);
		mListView.setAdapter(animationAdapter);
		System.out.println(getDuJiaPicsUrl(indexId + ""));
		loadData(getDuJiaPicsUrl(indexId + ""));

		mListView.setOnBottomListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPage++;
				indexId = indexId - 10;
				loadData(getDuJiaPicsUrl(indexId + ""));
			}
		});
	}

	private void loadData(String url) {
		if (getMyActivity().hasNetWork()) {
			loadNewList(url);
		} else {
			mListView.onBottomComplete();
			mProgressBar.setVisibility(View.GONE);
			getMyActivity().showShortToast(getString(R.string.not_network));
			String result = getMyActivity().getCacheStr("TupianDuJiaFragment" + currentPage);
			if (!StringUtils.isEmpty(result)) {
				getResult(result);
			}
		}
	}

	@Background
	void loadNewList(String url) {
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
				index = index + 5;
				loadData(getDuJiaPicsUrl(index + ""));
				url_maps.clear();
				mDemoSlider.removeAllSliders();
				PreferencesUtils.putInt(getActivity(), "indexdujia", index);
			}
		}, 2000);
	}

}
