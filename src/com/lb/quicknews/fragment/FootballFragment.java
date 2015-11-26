package com.lb.quicknews.fragment;

import org.androidannotations.annotations.EFragment;

import android.support.v4.widget.SwipeRefreshLayout;

import com.lb.quicknews.R;
import com.lb.quicknews.wedget.viewimage.SliderTypes.BaseSliderView;
import com.lb.quicknews.wedget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;

@EFragment(R.layout.fragment_football)
public class FootballFragment extends BaseFragment implements
		SwipeRefreshLayout.OnRefreshListener, OnSliderClickListener {

	@Override
	public void onSliderClick(BaseSliderView slider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

}
