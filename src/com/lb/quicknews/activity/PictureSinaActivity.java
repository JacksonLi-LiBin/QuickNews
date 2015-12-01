package com.lb.quicknews.activity;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.adapter.MyOnClickListener;
import com.lb.quicknews.adapter.NewsFragmentPagerAdapter;
import com.lb.quicknews.fragment.TupianSinaGuShiFragment_;
import com.lb.quicknews.fragment.TupianSinaJingXuanFragment_;
import com.lb.quicknews.fragment.TupianSinaMeiTuFragment_;
import com.lb.quicknews.fragment.TupianSinaQuTuFragment_;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;

@EActivity(R.layout.activity_picture)
public class PictureSinaActivity extends BaseActivity {
	@ViewById(R.id.vPager)
	ViewPager mViewPager;
	@ViewById(R.id.redian)
	RadioButton mJingXuan;
	@ViewById(R.id.dujia)
	RadioButton mQuTu;
	@ViewById(R.id.titan)
	RadioButton mGuShi;
	@ViewById(R.id.mingxing)
	RadioButton mMeiTu;
	private ArrayList<Fragment> fragments;

	@AfterInject
	void init() {
		fragments = new ArrayList<Fragment>();
		fragments.add(new TupianSinaJingXuanFragment_());
		fragments.add(new TupianSinaQuTuFragment_());
		fragments.add(new TupianSinaMeiTuFragment_());
		fragments.add(new TupianSinaGuShiFragment_());
	}

	@AfterViews
	void initView() {
		try {
			initPager();
		} catch (Exception e) {
		}
	}

	private void initPager() {
		mViewPager.setOffscreenPageLimit(2);
		NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mJingXuan.setOnClickListener(new MyOnClickListener(0, mViewPager));
		mQuTu.setOnClickListener(new MyOnClickListener(1, mViewPager));
		mMeiTu.setOnClickListener(new MyOnClickListener(2, mViewPager));
		mGuShi.setOnClickListener(new MyOnClickListener(3, mViewPager));
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
		mJingXuan.setChecked(b);
		mQuTu.setChecked(c);
		mMeiTu.setChecked(d);
		mGuShi.setChecked(e);
	}
}
