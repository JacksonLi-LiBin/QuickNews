package com.lb.quicknews.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lb.quicknews.App;
import com.lb.quicknews.R;
import com.lb.quicknews.adapter.NewsFragmentPagerAdapter;
import com.lb.quicknews.bean.ChannelItem;
import com.lb.quicknews.bean.ChannelManage;
import com.lb.quicknews.fragment.BaoxueFragment_;
import com.lb.quicknews.fragment.BeijingFragment_;
import com.lb.quicknews.fragment.BokeFragment_;
import com.lb.quicknews.fragment.CBAFragment_;
import com.lb.quicknews.fragment.CaijingFragment_;
import com.lb.quicknews.fragment.CaipiaoFragment_;
import com.lb.quicknews.fragment.DiantaiFragment_;
import com.lb.quicknews.fragment.DianyingFragment_;
import com.lb.quicknews.fragment.FangchanFragment_;
import com.lb.quicknews.fragment.FootballFragment_;
import com.lb.quicknews.fragment.JiajuFragment_;
import com.lb.quicknews.fragment.JiaoyuFragment_;
import com.lb.quicknews.fragment.JingxuanFragment_;
import com.lb.quicknews.fragment.JunshiFragment_;
import com.lb.quicknews.fragment.KejiFragment_;
import com.lb.quicknews.fragment.LuntanFragment_;
import com.lb.quicknews.fragment.LvyouFragment_;
import com.lb.quicknews.fragment.NBAFragment_;
import com.lb.quicknews.fragment.NewsFragment_;
import com.lb.quicknews.fragment.QicheFragment_;
import com.lb.quicknews.fragment.QingganFragment_;
import com.lb.quicknews.fragment.QinziFragment_;
import com.lb.quicknews.fragment.ShehuiFragment_;
import com.lb.quicknews.fragment.ShishangFragment_;
import com.lb.quicknews.fragment.ShoujiFragment_;
import com.lb.quicknews.fragment.ShumaFragment_;
import com.lb.quicknews.fragment.TiyuFragment_;
import com.lb.quicknews.fragment.TupianFragment_;
import com.lb.quicknews.fragment.XiaohuaFragment_;
import com.lb.quicknews.fragment.YidongFragment_;
import com.lb.quicknews.fragment.YouxiFragment_;
import com.lb.quicknews.fragment.YuleFragment_;
import com.lb.quicknews.initview.SlidingMenuView;
import com.lb.quicknews.utils.BaseTools;
import com.lb.quicknews.view.LeftView;
import com.lb.quicknews.view.LeftView_;
import com.lb.quicknews.wedget.ColumnHorizontalScrollView;
import com.lb.quicknews.wedget.slidingmenu.SlidingMenu;

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
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
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

	private void initSlidingMenu() {
		leftView = LeftView_.build(this);
		side_drawer = SlidingMenuView.instance().initSlidingMenuView(this,
				leftView);
	}

	private void initViewPager() {
		mAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setOffscreenPageLimit(1);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(pageListener);
	}

	private void initColumnData() {
		userChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(
				App.getApplication().getSQLHelper()).getUserChannel());
		initTabColumn();
		 initFragment();
	}

	/**
	 * 初始化Column栏目项
	 * 
	 * @author JacksonLi
	 */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count = userChannelList.size();
		mColumnHorizontalScrollView.setParam(this, mScreenWidth,
				mRadioGroup_content, shade_left, shade_right, ll_more_columns,
				rl_column);
		for (int i = 0; i < count; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					mItemWidth, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this,
					R.style.top_category_scroll_view_item_text);
			columnTextView.setBackgroundResource(R.drawable.radio_button_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(i);
			columnTextView.setText(userChannelList.get(i).getName());
			columnTextView.setTextColor(getResources().getColorStateList(
					R.color.top_category_scroll_text_color_day));
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
		} else if (channelName.equals("足球")) {
			newFragment = new FootballFragment_();
		} else if (channelName.equals("娱乐")) {
			newFragment = new YuleFragment_();
		} else if (channelName.equals("体育")) {
			newFragment = new TiyuFragment_();
		} else if (channelName.equals("财经")) {
			newFragment = new CaijingFragment_();
		} else if (channelName.equals("科技")) {
			newFragment = new KejiFragment_();
		} else if (channelName.equals("电影")) {
			newFragment = new DianyingFragment_();
		} else if (channelName.equals("汽车")) {
			newFragment = new QicheFragment_();
		} else if (channelName.equals("笑话")) {
			newFragment = new XiaohuaFragment_();
		} else if (channelName.equals("时尚")) {
			newFragment = new ShishangFragment_();
		} else if (channelName.equals("北京")) {
			newFragment = new BeijingFragment_();
		} else if (channelName.equals("军事")) {
			newFragment = new JunshiFragment_();
		} else if (channelName.equals("房产")) {
			newFragment = new FangchanFragment_();
		} else if (channelName.equals("游戏")) {
			newFragment = new YouxiFragment_();
		} else if (channelName.equals("情感")) {
			newFragment = new QingganFragment_();
		} else if (channelName.equals("精选")) {
			newFragment = new JingxuanFragment_();
		} else if (channelName.equals("电台")) {
			newFragment = new DiantaiFragment_();
		} else if (channelName.equals("图片")) {
			newFragment = new TupianFragment_();
		} else if (channelName.equals("NBA")) {
			newFragment = new NBAFragment_();
		} else if (channelName.equals("数码")) {
			newFragment = new ShumaFragment_();
		} else if (channelName.equals("移动")) {
			newFragment = new YidongFragment_();
		} else if (channelName.equals("彩票")) {
			newFragment = new CaipiaoFragment_();
		} else if (channelName.equals("教育")) {
			newFragment = new JiaoyuFragment_();
		} else if (channelName.equals("论坛")) {
			newFragment = new LuntanFragment_();
		} else if (channelName.equals("旅游")) {
			newFragment = new LvyouFragment_();
		} else if (channelName.equals("手机")) {
			newFragment = new ShoujiFragment_();
		} else if (channelName.equals("博客")) {
			newFragment = new BokeFragment_();
		} else if (channelName.equals("社会")) {
			newFragment = new ShehuiFragment_();
		} else if (channelName.equals("家居")) {
			newFragment = new JiajuFragment_();
		} else if (channelName.equals("暴雪")) {
			newFragment = new BaoxueFragment_();
		} else if (channelName.equals("亲子")) {
			newFragment = new QinziFragment_();
		} else if (channelName.equals("CBA")) {
			newFragment = new CBAFragment_();
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

	@Click(R.id.top_head)
	void onMenu(View view) {
		if (side_drawer.isMenuShowing()) {
			side_drawer.showContent();
		} else {
			side_drawer.showMenu();
		}
	}

	@Click(R.id.button_more_columns)
	void onMoreColumns(View view) {
		openActivityForResult(ChannelActivity_.class, CHANNELREQUEST);
	}

	@Override
	public void onBackPressed() {
		if (side_drawer.isMenuShowing()) {
			side_drawer.showContent();
		} else {
			if (isShowing()) {
				dismissProcessDialog();
			} else {
				if (back_pressed + 3000 > System.currentTimeMillis()) {
					finish();
					super.onBackPressed();
				} else {
					showCustomToast(getString(R.string.press_again_exit));
					back_pressed = System.currentTimeMillis();
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		try {
			if (isChange) {
				initColumnData();
				isChange = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
