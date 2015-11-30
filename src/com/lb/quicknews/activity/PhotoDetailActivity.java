package com.lb.quicknews.activity;

import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.adapter.PhotoDetailAdapter;
import com.lb.quicknews.bean.PhotoDetailModle;
import com.lb.quicknews.http.HttpUtil;
import com.lb.quicknews.utils.SplitNetStringUtils;
import com.lb.quicknews.utils.StringUtils;
import com.lb.quicknews.wedget.flipview.FlipView;
import com.lb.quicknews.wedget.flipview.FlipView.OnFlipListener;
import com.lb.quicknews.wedget.flipview.FlipView.OnOverFlipListener;
import com.lb.quicknews.wedget.flipview.OverFlipMode;

import android.os.Handler;
import android.os.Message;

@EActivity(R.layout.activity_photo_detail)
public class PhotoDetailActivity extends BaseActivity implements OnFlipListener, OnOverFlipListener {
	@ViewById(R.id.flip_view)
	FlipView mFlipView;
	@Bean
	PhotoDetailAdapter photoDetailAdapter;
	private String imgUrl;
	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			String result = (String) msg.obj;
			switch (msg.what) {
			case 0:
				setCacheStr(imgUrl, result);
				dismissProcessDialog();
				try {
					List<PhotoDetailModle> list = SplitNetStringUtils.getPhotoDetailModles(result);
					photoDetailAdapter.appendList(list);
				} catch (Exception e) {
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
		try {
			if (getIntent().getExtras().getString("photoUrl") != null) {
				imgUrl = getIntent().getExtras().getString("photoUrl");
				showProcessDialog();
				loadData(imgUrl);
			}
		} catch (Exception e) {
		}
	}

	private void loadData(String url) {
		if (hasNetWork()) {
			loadPhotoList(url);
		} else {
			dismissProcessDialog();
			showShortToast(getString(R.string.not_network));
			String result = getCacheStr(imgUrl);
			if (!StringUtils.isEmpty(result)) {
				getResult(result);
			}
		}
	}

	@Background
	void loadPhotoList(String url) {
		String result;
		try {
			result = HttpUtil.getByHttpClient(this, url);
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
	public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance,
			float flipDistancePerPage) {

	}

	@Override
	public void onFlippedToPage(FlipView v, int position, long id) {

	}

}
