package com.lb.quicknews.activity;

import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.adapter.PictureDetailAdapter;
import com.lb.quicknews.bean.PictureDetailModle;
import com.lb.quicknews.http.HttpUtil;
import com.lb.quicknews.http.Url;
import com.lb.quicknews.http.json.PictureSinaJson;
import com.lb.quicknews.utils.StringUtils;
import com.lb.quicknews.wedget.flipview.FlipView;
import com.lb.quicknews.wedget.flipview.FlipView.OnFlipListener;
import com.lb.quicknews.wedget.flipview.FlipView.OnOverFlipListener;
import com.lb.quicknews.wedget.flipview.OverFlipMode;

@EActivity(R.layout.activity_photo_detail)
public class PictureDetailActivity extends BaseActivity implements OnFlipListener, OnOverFlipListener {
	@ViewById(R.id.flip_view)
	FlipView mFlipView;
	@Bean
	PictureDetailAdapter pictureDetailAdapter;
	private String imgUrl;

	@AfterInject
	void init() {
		try {
			if (getIntent().getExtras().getString("pic_id") != null) {
				imgUrl = getIntent().getExtras().getString("pic_id");
				showProcessDialog();
				loadData(Url.JINGXUANDETAIL_ID + imgUrl);
			}
		} catch (Exception e) {
		}
	}

	@AfterViews
	void initView() {
		try {
			mFlipView.setOnFlipListener(this);
			mFlipView.setAdapter(pictureDetailAdapter);
			mFlipView.peakNext(false);
			mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
			mFlipView.setOnOverFlipListener(this);
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
			e.printStackTrace();
		}
	}

	@UiThread
	public void getResult(String result) {
		setCacheStr(imgUrl, result);
		dismissProcessDialog();
		try {
			List<PictureDetailModle> list = PictureSinaJson.getInstance(this).readJsonPictureModle(result);
			pictureDetailAdapter.appendList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance,
			float flipDistancePerPage) {

	}

	@Override
	public void onFlippedToPage(FlipView v, int position, long id) {

	}
}
