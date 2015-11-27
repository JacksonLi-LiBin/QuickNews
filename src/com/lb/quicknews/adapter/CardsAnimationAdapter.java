package com.lb.quicknews.adapter;

import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CardsAnimationAdapter extends AnimationAdapter {
	private final float mTrnslationY = 400;
	private final float mRotationX = 15;

	public CardsAnimationAdapter(BaseAdapter baseAdapter) {
		super(baseAdapter);
	}

	@Override
	public Animator[] getAnimators(ViewGroup arg0, View arg1) {
		return new Animator[] { ObjectAnimator.ofFloat(arg1, "translationY", mTrnslationY, 0),
				ObjectAnimator.ofFloat(arg1, "rotationX", mRotationX, 0) };
	}

}
