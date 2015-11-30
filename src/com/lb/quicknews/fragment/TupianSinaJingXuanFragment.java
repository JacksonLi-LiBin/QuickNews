package com.lb.quicknews.fragment;

import java.util.ArrayList;
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
import com.lb.quicknews.adapter.PictureAdapter;
import com.lb.quicknews.bean.PictureModle;
import com.lb.quicknews.http.HttpUtil;
import com.lb.quicknews.http.json.PictureSinaJson;
import com.lb.quicknews.initview.InitView;
import com.lb.quicknews.utils.StringUtils;
import com.lb.quicknews.wedget.swipelistview.SwipeListView;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ProgressBar;

@EFragment(R.layout.fragment_main)
public class TupianSinaJingXuanFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
	@ViewById(R.id.swipe_container)
	SwipeRefreshLayout swipeLayout;
	@ViewById(R.id.listview)
	SwipeListView mListView;
	@ViewById(R.id.progressBar)
	ProgressBar mProgressBar;
	public int index = 1;
	@Bean
	PictureAdapter pictureAdapter;
	List<PictureModle> listModles;
	private boolean isRefresh = false;
	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			String result = (String) msg.obj;
			switch (msg.what) {
			case 0:
				getMyActivity().setCacheStr("TupianJingXuanFragment" + currentPage, result);
				if (isRefresh) {
					isRefresh = false;
					pictureAdapter.clear();
					listModles.clear();
				}
				mProgressBar.setVisibility(View.GONE);
				swipeLayout.setRefreshing(false);
				List<PictureModle> list = PictureSinaJson.getInstance(getActivity()).readJsonPictureListModles(result);
				pictureAdapter.appendList((ArrayList<PictureModle>) list);
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
		listModles = new ArrayList<PictureModle>();
	}

	@AfterViews
	void initView() {
		swipeLayout.setOnRefreshListener(this);
		InitView.getInstance().initSwipeRefreshLayout(swipeLayout);
		InitView.getInstance().initListView(mListView, getActivity());
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(pictureAdapter);
		animationAdapter.setAbsListView(mListView);
		mListView.setAdapter(animationAdapter);
		loadData(getSinaJingXuan(index + ""));
		mListView.setOnBottomListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				currentPage++;
				index += 1;
				loadData(getSinaJingXuan(index + ""));
			}
		});
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				currentPage = 1;
				isRefresh = true;
				index = 1;
				loadData(getSinaJingXuan(index + ""));
			}
		}, 2000);
	}

	private void loadData(String url) {
		if (getMyActivity().hasNetWork()) {

		} else {
			mListView.onBottomComplete();
			mProgressBar.setVisibility(View.GONE);
			getMyActivity().showShortToast(getString(R.string.not_network));
			String result = getMyActivity().getCacheStr("TupianJingXuanFragment" + currentPage);
			if (!StringUtils.isEmpty(result)) {
				getResult(result);
			}
		}
	}

	@Background
	void loadNewList(String url) {
		String result = "";
		try {
			result = HttpUtil.getByHttpClient(getActivity(), url, null);
			getResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@UiThread
	void getResult(String result) {
		Message msg = new Message();
		msg.what = 0;
		msg.obj = result;
		handler.sendMessage(msg);
	}
}
