package com.lb.quicknews.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.adapter.MyOnClickListener;
import com.lb.quicknews.adapter.NewsFragmentPagerAdapter;
import com.lb.quicknews.fragment.VideoGaoXiaoFragment_;
import com.lb.quicknews.fragment.VideoJingPinFragment_;
import com.lb.quicknews.fragment.VideoReDianFragment_;
import com.lb.quicknews.fragment.VideoYuLeFragment_;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

@EActivity(R.layout.activity_video)
public class VideoActivity extends BaseActivity {
	@ViewById(R.id.vPager)
	ViewPager mViewPager;
	@ViewById(R.id.video_redian)
	RadioButton mReDian;
	@ViewById(R.id.video_yule)
	RadioButton mYuLe;
	@ViewById(R.id.video_gaoxiao)
	RadioButton mGaoXiao;
	@ViewById(R.id.video_jingpin)
	RadioButton mJingPin;
	@ViewById(R.id.title)
	TextView mTitle;
	List<Fragment> fragments;

	@AfterInject
	void init() {
		fragments = new ArrayList<Fragment>();
		fragments.add(new VideoReDianFragment_());
		fragments.add(new VideoYuLeFragment_());
		fragments.add(new VideoGaoXiaoFragment_());
		fragments.add(new VideoJingPinFragment_());
	}

	@AfterViews
	void initView() {
		mTitle.setText("视频新闻");
		initPager();
	}

	private void initPager() {
		mViewPager.setOffscreenPageLimit(2);
		NewsFragmentPagerAdapter mAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager(),
				(ArrayList<Fragment>) fragments);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mReDian.setOnClickListener(new MyOnClickListener(0, mViewPager));
		mYuLe.setOnClickListener(new MyOnClickListener(1, mViewPager));
		mGaoXiao.setOnClickListener(new MyOnClickListener(2, mViewPager));
		mJingPin.setOnClickListener(new MyOnClickListener(3, mViewPager));
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			mViewPager.setCurrentItem(position);
			switch (position) {
			case 0:
				setRadioButtonCheck(true, false, false, false);
				break;
			case 1:
				setRadioButtonCheck(false, true, false, false);
				break;
			case 2:
				setRadioButtonCheck(false, false, true, false);
				break;
			case 3:
				setRadioButtonCheck(false, false, false, true);
				break;
			}
		}

	}

	private void setRadioButtonCheck(boolean b, boolean c, boolean d, boolean e) {
		mReDian.setChecked(b);
		mYuLe.setChecked(c);
		mGaoXiao.setChecked(d);
		mJingPin.setChecked(e);
	}
}
