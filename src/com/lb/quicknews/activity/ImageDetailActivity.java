package com.lb.quicknews.activity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.widget.TextView;

import com.lb.quicknews.R;
import com.lb.quicknews.bean.NewsModle;
import com.lb.quicknews.wedget.flipview.FlipView;
import com.lb.quicknews.wedget.flipview.FlipView.OnFlipListener;
import com.lb.quicknews.wedget.flipview.FlipView.OnOverFlipListener;
import com.lb.quicknews.wedget.flipview.OverFlipMode;

@EActivity(R.layout.activity_image_detail)
public class ImageDetailActivity extends BaseActivity implements
		OnFlipListener, OnOverFlipListener {
	@ViewById(R.id.flip_view)
	FlipView mFlipView;
	@ViewById(R.id.new_title)
	TextView newTitle;
	private NewsModle newsModle;

	@Override
	public void onOverFlip(FlipView v, OverFlipMode mode,
			boolean overFlippingPrevious, float overFlipDistance,
			float flipDistancePerPage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFlippedToPage(FlipView v, int position, long id) {
		// TODO Auto-generated method stub

	}

}
