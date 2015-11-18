package com.lb.quicknews.wedget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class ColumnHorizontalScrollView extends HorizontalScrollView {
	private View ll_content;
	private View ll_more;
	private View rl_column;
	private ImageView leftImage;
	private ImageView rightImgage;
	private int mScreenWidth = 0;
	private Activity activity;

	public ColumnHorizontalScrollView(Context context) {
		super(context);
	}

	public ColumnHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ColumnHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		shade_ShowOrHide();
		if (!activity.isFinishing() && ll_content != null && leftImage != null
				&& rightImgage != null && ll_more != null && rl_column != null) {
			if (ll_content.getWidth() <= mScreenWidth) {
				leftImage.setVisibility(View.GONE);
				rightImgage.setVisibility(View.GONE);
			}
		} else {
			return;
		}
		if (l == 0) {
			leftImage.setVisibility(View.GONE);
			rightImgage.setVisibility(View.VISIBLE);
			return;
		}
		if (ll_content.getWidth() - l + ll_more.getWidth()
				+ rl_column.getWidth() == mScreenWidth) {
			leftImage.setVisibility(View.VISIBLE);
			rightImgage.setVisibility(View.GONE);
			return;
		}
		leftImage.setVisibility(View.VISIBLE);
		rightImgage.setVisibility(View.VISIBLE);
	}

	public void setParam(Activity activity, int mScreenWidth, View paramView1,
			ImageView paramView2, ImageView paramView3, View paramView4,
			View paramView5) {
		this.activity = activity;
		this.mScreenWidth = mScreenWidth;
		ll_content = paramView1;
		leftImage = paramView2;
		rightImgage = paramView3;
		ll_more = paramView4;
		rl_column = paramView5;
	}

	public void shade_ShowOrHide() {
		if (!activity.isFinishing() && ll_content != null) {
			measure(0, 0);
			if (mScreenWidth >= getMeasuredWidth()) {
				leftImage.setVisibility(View.GONE);
				rightImgage.setVisibility(View.GONE);
			}
		} else {
			return;
		}
		if (getLeft() == 0) {
			leftImage.setVisibility(View.GONE);
			rightImgage.setVisibility(View.VISIBLE);
			return;
		}
		if (getRight() == getMeasuredWidth() - mScreenWidth) {
			leftImage.setVisibility(View.VISIBLE);
			rightImgage.setVisibility(View.GONE);
			return;
		}
		leftImage.setVisibility(View.VISIBLE);
		rightImgage.setVisibility(View.VISIBLE);
	}
}
