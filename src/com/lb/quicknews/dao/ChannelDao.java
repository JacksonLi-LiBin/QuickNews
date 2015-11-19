package com.lb.quicknews.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lb.quicknews.bean.ChannelItem;
import com.lb.quicknews.db.SQLHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChannelDao implements ChannelService {
	private SQLHelper helper;

	public ChannelDao(Context context) {
		helper = new SQLHelper(context);
	}

	@Override
	public boolean addCache(ChannelItem item) {
		boolean flag = false;
		SQLiteDatabase database = null;
		long id = -1;
		try {
			database = helper.getWritableDatabase();
			database.beginTransaction();
			ContentValues values = new ContentValues();
			Class<? extends ChannelItem> clazz = item.getClass();
			String tableName = clazz.getSimpleName();
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				String mName = method.getName();
				if (mName.startsWith("get") && !mName.startsWith("getClas")) {
					String fieldName = mName.substring(3, mName.length())
							.toLowerCase();
					Object value = method.invoke(item, null);
					if (value instanceof String) {
						values.put(fieldName, (String) value);
					}
				}
			}
			id = database.insert(tableName, null, values);
			flag = (id != -1 ? true : false);
			database.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.endTransaction();
				database.close();
			}
		}
		return flag;
	}

	@Override
	public boolean deleteCache(ContentValues values, String whereClause,
			String[] whereArgs) {
		boolean flag = false;
		SQLiteDatabase database = null;
		int count = 0;
		try {
			database = helper.getWritableDatabase();
			database.beginTransaction();
			count = database.delete(SQLHelper.TABLE_CHANNEL, whereClause,
					whereArgs);
			flag = (count > 0 ? true : false);
			database.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.endTransaction();
				database.close();
			}
		}
		return flag;
	}

	@Override
	public boolean updateCache(ContentValues values, String whereClause,
			String[] whereArgs) {
		SQLiteDatabase database = null;
		try {
			database = helper.getWritableDatabase();
			String sql = "update " + SQLHelper.TABLE_CHANNEL
					+ " set selected = " + values.getAsString("selected")
					+ " where id = " + values.getAsString("id");
			database.execSQL(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return false;
	}

	@Override
	public Map<String, String> viewCache(String selection,
			String[] selectionArgs) {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			database = helper.getWritableDatabase();
			database.beginTransaction();
			cursor = database.query(true, SQLHelper.TABLE_CHANNEL, null,
					selection, selectionArgs, null, null, null, null);
			int cols_len = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < cols_len; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_values = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_values == null) {
						cols_name = "";
					}
					map.put(cols_name, cols_values);
				}
			}
			database.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.endTransaction();
				cursor.close();
				database.close();
			}
		}
		return map;
	}

	@Override
	public List<Map<String, String>> listCache(String selection,
			String[] selectionArgs) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = helper.getWritableDatabase();
			database.beginTransaction();
			cursor = database.query(false, SQLHelper.TABLE_CHANNEL, null,
					selection, selectionArgs, null, null, null, null);
			int cols_len = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
			database.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.endTransaction();
				cursor.close();
				database.close();
			}
		}
		return list;
	}

	@Override
	public void clearFeedTable() {
		String sql = "delete from " + SQLHelper.TABLE_CHANNEL + ";";
		SQLiteDatabase database = helper.getWritableDatabase();
		database.execSQL(sql);
		revertSeq();
	}

	private void revertSeq() {
		String sql = "update sqlite_sequence set seq=0 where name='"
				+ SQLHelper.TABLE_CHANNEL + "'";
		SQLiteDatabase database = helper.getWritableDatabase();
		database.execSQL(sql);
	}

}
