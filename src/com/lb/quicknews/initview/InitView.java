package com.lb.quicknews.initview;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.lb.quicknews.R;
import com.lb.quicknews.utils.ScreenUtils;
import com.lb.quicknews.utils.SettingsManager;
import com.lb.quicknews.wedget.slidingmenu.SlidingMenu;
import com.lb.quicknews.wedget.swipelistview.SwipeListView;

public class InitView {
	public static InitView initView;
	private SlidingMenu slidingMenu;

	public static InitView getInstance() {
		if (initView == null) {
			initView = new InitView();
		}
		return initView;
	}

	/**
	 * 初始化侧滑控件
	 */
	public SlidingMenu initSlidingMenu(Activity activity, View view) {
		slidingMenu = new SlidingMenu(activity);
		slidingMenu.setMode(SlidingMenu.LEFT);// 设置左右滑菜单
		slidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);// 设置要使菜单华东，触碰屏幕的范围
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);// 设置阴影图片的宽度
		slidingMenu.setShadowDrawable(R.drawable.shadow);// 设置阴影图片
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// SlidingMenu划出时主页面显示的剩余宽度
		slidingMenu.setFadeDegree(0.35F);// SlidingMenu滑动时的渐变程度
		slidingMenu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);// 使SlidingMenu附加在Activity右边
		slidingMenu.setMenu(view);// 设置menu的布局文件
		return slidingMenu;
	}

	/**
	 * 设置下拉刷新控件颜色
	 */
	public void initSwipeRefreshLayout(SwipeRefreshLayout swipeLayout) {
		swipeLayout.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light,
				R.color.holo_red_light);
	}
	/**
	 * 初始化listview
	 */
	public void initListView(SwipeListView mListView,Context context){
		SettingsManager settings=SettingsManager.getInstance();
		 mListView.setSwipeMode(SwipeListView.SWIPE_MODE_NONE);
	        mListView.setSwipeActionLeft(settings.getSwipeActionLeft());
	        mListView.setSwipeActionRight(settings.getSwipeActionRight());
	        mListView.setOffsetLeft(ScreenUtils.convertDpToPixel(context,
	                settings.getSwipeOffsetLeft()));
	        mListView.setOffsetRight(ScreenUtils.convertDpToPixel(context,
	                settings.getSwipeOffsetRight()));
	        mListView.setAnimationTime(settings.getSwipeAnimationTime());
	        mListView.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
	}
}
