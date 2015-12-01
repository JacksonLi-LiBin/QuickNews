package com.lb.quicknews.adapter;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

public class MyOnClickListener implements OnClickListener {
	private int index = 0;
	private final ViewPager viewPager;

	public MyOnClickListener(int index, ViewPager viewPager) {
		super();
		this.index = index;
		this.viewPager = viewPager;
	}

	@Override
	public void onClick(View v) {
		viewPager.setCurrentItem(index);
	}

}
