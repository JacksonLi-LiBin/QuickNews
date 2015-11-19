package com.lb.quicknews.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.database.SQLException;

import com.lb.quicknews.dao.ChannelDao;
import com.lb.quicknews.db.SQLHelper;

public class ChannelManage {
	public static ChannelManage channelManage;
	// 默认的用户选择频道列表
	public static List<ChannelItem> defaultUserChannels;
	// 默认的其他频道列表
	public static List<ChannelItem> defaultOtherChannels;
	private ChannelDao channelDao;
	// 判断数据库中是否存在用户数据
	private boolean userExist = false;
	static {
		defaultUserChannels = new ArrayList<ChannelItem>();
		defaultOtherChannels = new ArrayList<ChannelItem>();
		defaultUserChannels.add(new ChannelItem(1, "头条", 1, 1));
		defaultUserChannels.add(new ChannelItem(2, "足球", 2, 1));
		defaultUserChannels.add(new ChannelItem(3, "娱乐", 3, 1));
		defaultUserChannels.add(new ChannelItem(4, "体育", 4, 1));
		defaultUserChannels.add(new ChannelItem(5, "财经", 5, 1));
		defaultUserChannels.add(new ChannelItem(6, "科技", 6, 1));
		// defaultUserChannels.add(new ChannelItem(7, "图片", 1, 0));
		defaultOtherChannels.add(new ChannelItem(7, "CBA", 1, 0));
		defaultOtherChannels.add(new ChannelItem(8, "笑话", 2, 0));
		defaultOtherChannels.add(new ChannelItem(9, "汽车", 3, 0));
		defaultOtherChannels.add(new ChannelItem(10, "时尚", 4, 0));
		defaultOtherChannels.add(new ChannelItem(11, "北京", 5, 0));
		defaultOtherChannels.add(new ChannelItem(12, "军事", 6, 0));
		defaultOtherChannels.add(new ChannelItem(13, "房产", 7, 0));
		defaultOtherChannels.add(new ChannelItem(14, "游戏", 8, 0));
		defaultOtherChannels.add(new ChannelItem(15, "精选", 9, 0));
		defaultOtherChannels.add(new ChannelItem(16, "电台", 10, 0));
		defaultOtherChannels.add(new ChannelItem(17, "情感", 11, 0));
		defaultUserChannels.add(new ChannelItem(18, "电影", 12, 0));
		defaultUserChannels.add(new ChannelItem(19, "NBA", 13, 0));
		defaultUserChannels.add(new ChannelItem(20, "数码", 14, 0));
		defaultUserChannels.add(new ChannelItem(21, "移动", 15, 0));
		defaultUserChannels.add(new ChannelItem(22, "彩票", 16, 0));
		defaultUserChannels.add(new ChannelItem(23, "教育", 17, 0));
		defaultUserChannels.add(new ChannelItem(24, "论坛", 18, 0));
		defaultOtherChannels.add(new ChannelItem(25, "旅游", 19, 0));
		defaultOtherChannels.add(new ChannelItem(26, "手机", 20, 0));
		defaultOtherChannels.add(new ChannelItem(27, "博客", 21, 0));
		defaultOtherChannels.add(new ChannelItem(28, "社会", 22, 0));
		defaultOtherChannels.add(new ChannelItem(29, "家居", 23, 0));
		defaultOtherChannels.add(new ChannelItem(30, "暴雪", 24, 0));
		defaultUserChannels.add(new ChannelItem(31, "亲子", 25, 0));
	}

	private ChannelManage(SQLHelper paramDBHelper) throws SQLException {
		if (channelDao == null) {
			channelDao = new ChannelDao(paramDBHelper.getContext());
		}
		initDefaultChannel();
		return;
	}

	/**
	 * 初始化数据库内的数据
	 */
	private void initDefaultChannel() {
		deleteAllChannel();
		saveUserChannel(defaultUserChannels);
		saveOtherChannel(defaultOtherChannels);
	}

	private void deleteAllChannel() {
		channelDao.clearFeedTable();
	}

	/**
	 * 保存用户频道到数据库
	 */
	private void saveUserChannel(List<ChannelItem> userList) {
		for (int i = 0; i < userList.size(); i++) {
			ChannelItem channelItem = userList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(Integer.valueOf(1));
			channelDao.addCache(channelItem);
		}
	}

	/**
	 * 保存其他频道到数据库
	 */
	private void saveOtherChannel(List<ChannelItem> otherList) {
		for (int i = 0; i < otherList.size(); i++) {
			ChannelItem channelItem = otherList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(Integer.valueOf(0));
			channelDao.addCache(channelItem);
		}
	}

	/**
	 * 初始化频道管理类
	 */
	public static ChannelManage getManage(SQLHelper dbHelper) {
		if (channelManage == null) {
			synchronized (ChannelManage.class) {
				if (channelManage == null) {
					channelManage = new ChannelManage(dbHelper);
				}
			}
		}
		return channelManage;
	}

	/**
	 * 获取用户频道 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
	 */
	public List<ChannelItem> getUserChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + " = ?",
				new String[] { "1" });
		if (cacheList != null && !((List<?>) cacheList).isEmpty()) {
			userExist = true;
			List<Map<String, String>> mapList = (List) cacheList;
			int count = mapList.size();
			List<ChannelItem> list = new ArrayList<ChannelItem>();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate = new ChannelItem();
				navigate.setId(Integer
						.valueOf(mapList.get(i).get(SQLHelper.ID)));
				navigate.setName(mapList.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(mapList.get(i).get(
						SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(mapList.get(i).get(
						SQLHelper.SELECTED)));
				list.add(navigate);
			}
			return list;
		}
		return defaultUserChannels;
	}

	/**
	 * 获取其他频道 数据库存在用户配置 ? 数据库内的其它频道 : 默认其它频道 ;
	 */
	public List<ChannelItem> getOtherChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + " = ?",
				new String[] { "0" });
		List<ChannelItem> list = new ArrayList<ChannelItem>();
		if (cacheList != null && !((List<?>) cacheList).isEmpty()) {
			List<Map<String, String>> mapList = (List) cacheList;
			int count = mapList.size();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate = new ChannelItem();
				navigate.setId(Integer
						.valueOf(mapList.get(i).get(SQLHelper.ID)));
				navigate.setName(mapList.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(mapList.get(i).get(
						SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(mapList.get(i).get(
						SQLHelper.SELECTED)));
				list.add(navigate);
			}
			return list;
		}
		if (userExist) {
			return list;
		}
		cacheList = defaultOtherChannels;
		return (List<ChannelItem>) cacheList;
	}
}
