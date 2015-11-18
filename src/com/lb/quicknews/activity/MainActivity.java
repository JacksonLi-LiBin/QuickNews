package com.lb.quicknews.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lb.quicknews.R;
import com.lb.quicknews.bean.ChannelItem;
import com.lb.quicknews.view.LeftView;
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
}
