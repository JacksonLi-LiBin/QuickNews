package com.lb.quicknews.fragment;

import java.util.ArrayList;
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
import com.lb.quicknews.activity.VideoPlayActivity_;
import com.lb.quicknews.adapter.CardsAnimationAdapter;
import com.lb.quicknews.adapter.VideoAdapter;
import com.lb.quicknews.bean.VideoModle;
import com.lb.quicknews.http.HttpUtil;
import com.lb.quicknews.http.Url;
import com.lb.quicknews.http.json.VideoListJson;
import com.lb.quicknews.initview.InitView;
import com.lb.quicknews.utils.StringUtils;
import com.lb.quicknews.wedget.swipelistview.SwipeListView;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

@EFragment(R.layout.fragment_main)
public class VideoGaoXiaoFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
	@ViewById(R.id.swipe_container)
	SwipeRefreshLayout swipeLayout;
	@ViewById(R.id.listview)
	SwipeListView mListView;
	@ViewById(R.id.progressBar)
	ProgressBar mProgressBar;
	@Bean
	VideoAdapter videoAdapter;
	List<VideoModle> listModles;
	private int index = 0;
	private boolean isRefresh = false;
	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			String result = (String) msg.obj;
			switch (msg.what) {
			case 0:
				getMyActivity().setCacheStr("VideoGaoXiaoFragment" + currentPage, result);
				if (isRefresh) {
					isRefresh = false;
					videoAdapter.clear();
					listModles.clear();
				}
				mProgressBar.setVisibility(View.GONE);
				swipeLayout.setRefreshing(false);

				List<VideoModle> list = VideoListJson.getInstance(getActivity()).readJsonVideoModles(result,
						Url.VideoGaoXiaoId);
				videoAdapter.appendList(list);
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
		listModles = new ArrayList<VideoModle>();
	}

	@AfterViews
	void initView() {
		swipeLayout.setOnRefreshListener(this);
		InitView.getInstance().initSwipeRefreshLayout(swipeLayout);
		InitView.getInstance().initListView(mListView, getActivity());
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(videoAdapter);
		animationAdapter.setAbsListView(mListView);
		mListView.setAdapter(animationAdapter);
		loadData(getVideoUrl(index + "", Url.VideoGaoXiaoId));
		mListView.setOnBottomListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPage++;
				index += 10;
				loadData(getVideoUrl(index + "", Url.VideoGaoXiaoId));
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
			String result = getMyActivity().getCacheStr("VideoGaoXiaoFragment" + currentPage);
			if (!StringUtils.isEmpty(result)) {
				getResult(result);
			}
		}
	}

	@ItemClick(R.id.listview)
	void onItemClick(int position) {
		Bundle bundle = new Bundle();
		bundle.putString("playUrl", listModles.get(position).getMp4Hd_url());
		((BaseActivity) getActivity()).openActivity(VideoPlayActivity_.class, bundle, 0);
	}

	@Background
	void loadNewList(String url) {
		String result;
		try {
			result = HttpUtil.getByHttpClient(getActivity(), url, null);
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
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				currentPage = 1;
				isRefresh = true;
				index = 0;
				loadData(getVideoUrl(index + "", Url.VideoGaoXiaoId));
			}
		}, 2000);
	}

}
