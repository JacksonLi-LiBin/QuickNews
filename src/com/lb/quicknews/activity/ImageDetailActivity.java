package com.lb.quicknews.activity;

import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.adapter.ImageAdapter;
import com.lb.quicknews.bean.NewsDetailModle;
import com.lb.quicknews.bean.NewsModle;
import com.lb.quicknews.wedget.flipview.FlipView;
import com.lb.quicknews.wedget.flipview.FlipView.OnFlipListener;
import com.lb.quicknews.wedget.flipview.FlipView.OnOverFlipListener;
import com.lb.quicknews.wedget.flipview.OverFlipMode;

import android.widget.TextView;

@EActivity(R.layout.activity_image_detail)
public class ImageDetailActivity extends BaseActivity implements
		OnFlipListener, OnOverFlipListener {
	@ViewById(R.id.flip_view)
	FlipView mFlipView;
	@ViewById(R.id.new_title)
	TextView newTitle;
	private NewsModle newsModle;
	@Bean
	ImageAdapter imageAdapter;
	private List<String> imgList;
	private NewsDetailModle newsDetailModle;
	private String titleString;

	@AfterInject
	public void init() {
		try {
			if (getIntent().getExtras().getSerializable("newsDetailModle") != null) {
				newsDetailModle = (NewsDetailModle) getIntent().getExtras()
						.getSerializable("newsDetailModle");
				imgList = newsDetailModle.getImgList();
				titleString = newsDetailModle.getTitle();
			} else {
				newsModle = (NewsModle) getIntent().getExtras()
						.getSerializable("newsModle");
				imgList = newsModle.getImagesModle().getImgList();
				titleString = newsModle.getTitle();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterViews
	public void initView() {
		try {
			newTitle.setText(titleString);
			imageAdapter.appendList(imgList);
			mFlipView.setOnFlipListener(this);
			mFlipView.setAdapter(imageAdapter);
			mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
			mFlipView.setOnOverFlipListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onOverFlip(FlipView v, OverFlipMode mode,
			boolean overFlippingPrevious, float overFlipDistance,
			float flipDistancePerPage) {

	}

	@Override
	public void onFlippedToPage(FlipView v, int position, long id) {

	}

}
