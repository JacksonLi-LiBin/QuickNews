package com.lb.quicknews.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.App;
import com.lb.quicknews.R;
import com.lb.quicknews.adapter.NewsFragmentPagerAdapter;
import com.lb.quicknews.bean.ChannelItem;
import com.lb.quicknews.bean.ChannelManage;
import com.lb.quicknews.fragment.NewsFragment_;
import com.lb.quicknews.initview.SlidingMenuView;
import com.lb.quicknews.utils.BaseTools;
import com.lb.quicknews.view.LeftView;
import com.lb.quicknews.view.LeftView_;
import com.lb.quicknews.wedget.ColumnHorizontalScrollView;
import com.lb.quicknews.wedget.slidingmenu.SlidingMenu;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
	@ViewById(R.id.mColumnHorizontalScrollView)
	ColumnHorizontalScrollView mColumnHorizontalScrollView;
	@ViewById(R.id.mRadioGroup_content)
	LinearLayout mRadioGroup_content;
	@ViewById(R.id.ll_more_columns)
	LinearLayout ll_more_columns;
	@ViewById(R.id.rl_column)
	RelativeLayout rl_column;
	@ViewById(R.id.button_more_columns)
	ImageView button_more_columns;
	@ViewById(R.id.mViewPager)
	ViewPager mViewPager;
	@ViewById(R.id.shade_left)
	ImageView shade_left;
	@ViewById(R.id.shade_right)
	ImageView shade_right;
	LeftView leftView;
	SlidingMenu side_drawer;
	// 屏幕宽度
	private int mScreenWidth = 0;
	// item宽度
	private int mItemWidth = 0;
	// head头部的左侧菜单按钮
	@ViewById(R.id.top_head)
	ImageView top_head;
	// head头部的右侧菜单按钮
	@ViewById(R.id.top_more)
	ImageView top_more;
	// 用户选择的新闻分类列表
	static List<ChannelItem> userChannelList;
	// 请求code
	public static final int CHANNELREQUEST = 1;
	// 调整返回的result code
	public static final int CHANNELRESULT = 10;
	// 当前选中的栏目
	private int columnSelectIndex = 0;
	private List<Fragment> fragments;
	private Fragment newFragment;
	private double back_pressed;
	public static boolean isChange = false;
	private NewsFragmentPagerAdapter mAdapter;

	@Override
	public boolean isSupportSlide() {
		return false;
	}

	@AfterInject
	void init() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 7;// 一个item宽度为屏幕的1/7
		userChannelList = new ArrayList<ChannelItem>();
		fragments = new ArrayList<Fragment>();
	}

	@AfterViews
	void initView() {
		try {
			initSlidingMenu();
			initViewPager();
			initColumnData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Click(R.id.top_head)
	void onMenu(View view) {
		if (side_drawer.isMenuShowing()) {
			side_drawer.showContent();
		} else {
			side_drawer.showMenu();
		}
	}

	private void initSlidingMenu() {
		leftView = LeftView_.build(this);
		side_drawer = SlidingMenuView.instance().initSlidingMenuView(this, leftView);
	}

	private void initViewPager() {
		mAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setOffscreenPageLimit(1);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(pageListener);
	}

	private void initColumnData() {
		userChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(App.getApplication().getSQLHelper())
				.getUserChannel());
		initTabColumn();
		// initFragment();
	}

	/**
	 * 初始化Column栏目项
	 * 
	 * @author JacksonLi
	 */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count = userChannelList.size();
		mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right,
				ll_more_columns, rl_column);
		for (int i = 0; i < count; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
			columnTextView.setBackgroundResource(R.drawable.radio_button_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(i);
			columnTextView.setText(userChannelList.get(i).getName());
			columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
			if (columnSelectIndex == i) {
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
						View localView = mRadioGroup_content.getChildAt(i);
						if (localView != v) {
							localView.setSelected(false);
						} else {
							localView.setSelected(true);
							mViewPager.setCurrentItem(i);
						}
					}

				}
			});
			mRadioGroup_content.addView(columnTextView, i, params);
		}
	}

	private void initFragment() {
		fragments.clear();
		int count = userChannelList.size();
		for (int i = 0; i < count; i++) {
			String nameString = userChannelList.get(i).getName();
			fragments.add(initFragment(nameString));
		}
		mAdapter.appendList((ArrayList<Fragment>) fragments);
	}

	private Fragment initFragment(String channelName) {
		if (channelName.equals("头条")) {
			newFragment = new NewsFragment_();
		}
		return newFragment;
	}

	public OnPageChangeListener pageListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			mViewPager.setCurrentItem(arg0);
			selectTab(arg0);
		}
	};

	/**
	 * 选择的column里面的tab
	 */
	private void selectTab(int tab_position) {
		columnSelectIndex = tab_position;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_position);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
		}
		// 判断是否选中
		for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean isCheck;
			if (j == tab_position) {
				isCheck = true;
			} else {
				isCheck = false;
			}
			checkView.setSelected(isCheck);
		}
	}
}
