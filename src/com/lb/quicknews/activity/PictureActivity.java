package com.lb.quicknews.activity;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.fragment.TupianSinaJingXuanFragment_;

import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;

@EActivity(R.layout.activity_picture)
public class PictureActivity extends BaseActivity {
	@ViewById(R.id.vPager)
	ViewPager mViewPager;
	@ViewById(R.id.redian)
	RadioButton mJingXuan;
	@ViewById(R.id.dujia)
	RadioButton mQuTu;
	@ViewById(R.id.titan)
	RadioButton mGuShi;
	@ViewById(R.id.mingxing)
	RadioButton mMeitu;
	private ArrayList<Fragment> fragments;

	@AfterInject
	void init() {
		fragments = new ArrayList<Fragment>();
		// fragments.add(new TupianSinaJingXuanFragment_());
	}
}
