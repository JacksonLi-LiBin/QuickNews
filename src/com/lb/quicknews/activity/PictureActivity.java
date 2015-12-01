package com.lb.quicknews.activity;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.adapter.MyOnClickListener;
import com.lb.quicknews.fragment.TupianDuJiaFragment_;
import com.lb.quicknews.fragment.TupianMeiTuFragment_;
import com.lb.quicknews.fragment.TupianMingXingFragment_;
import com.lb.quicknews.fragment.TupianReDianFragment_;
import com.lb.quicknews.fragment.TupianTiTanFragment_;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;

@EActivity(R.layout.activity_picture)
public class PictureActivity extends BaseActivity {
	@ViewById(R.id.vPager)
	ViewPager mViewPager;
	@ViewById(R.id.redian)
	RadioButton mReDian;
	@ViewById(R.id.dujia)
	RadioButton mDuJia;
	@ViewById(R.id.titan)
	RadioButton mTiTan;
	@ViewById(R.id.mingxing)
	RadioButton mMingXing;
	@ViewById(R.id.meitu)
	RadioButton mMeiTu;
	private ArrayList<Fragment> fragments;

	@AfterInject
	void init() {
		fragments = new ArrayList<Fragment>();
		fragments.add(new TupianReDianFragment_());
		fragments.add(new TupianDuJiaFragment_());
		fragments.add(new TupianMingXingFragment_());
		fragments.add(new TupianTiTanFragment_());
		fragments.add(new TupianMeiTuFragment_());
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
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mReDian.setOnClickListener(new MyOnClickListener(0, mViewPager));
		mDuJia.setOnClickListener(new MyOnClickListener(1, mViewPager));
		mMingXing.setOnClickListener(new MyOnClickListener(2, mViewPager));
		mTiTan.setOnClickListener(new MyOnClickListener(3, mViewPager));
		mMeiTu.setOnClickListener(new MyOnClickListener(4, mViewPager));
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			mViewPager.setCurrentItem(arg0);
			switch (arg0) {
			case 0:
				setRadioButtonCheck(true, false, false, false, false);
				break;
			case 1:
				setRadioButtonCheck(false, true, false, false, false);
				break;
			case 2:
				setRadioButtonCheck(false, false, true, false, false);
				break;
			case 3:
				setRadioButtonCheck(false, false, false, true, false);
				break;
			case 4:
				setRadioButtonCheck(false, false, false, false, true);
				break;
			default:
				break;
			}
		}

	}

	private void setRadioButtonCheck(boolean b, boolean c, boolean d, boolean e, boolean f) {
		mReDian.setChecked(b);
		mDuJia.setChecked(c);
		mMingXing.setChecked(d);
		mTiTan.setChecked(e);
		mMeiTu.setChecked(f);
	}
}
