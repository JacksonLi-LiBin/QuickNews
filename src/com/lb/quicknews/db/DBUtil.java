package com.lb.quicknews.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBUtil {
	private static DBUtil mInstance;
	private SQLHelper mSQLHelper;
	private SQLiteDatabase mSQLiteDatabase;

	private DBUtil(Context context) {
		mSQLHelper = new SQLHelper(context);
		mSQLiteDatabase = mSQLHelper.getWritableDatabase();
	}

	public static DBUtil getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBUtil(context);
		}
		return mInstance;
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		mSQLHelper.close();
		mSQLHelper = null;
		mSQLiteDatabase.close();
		mSQLiteDatabase = null;
		mInstance = null;
	}

	/**
	 * 插入数据
	 */
	public void insertData(ContentValues values) {
		mSQLiteDatabase.insert(SQLHelper.TABLE_CHANNEL, null, values);
	}

	/**
	 * 更新数据
	 */
	public void updateData(ContentValues values, String whereClause,
			String[] whereArgs) {
		mSQLiteDatabase.update(SQLHelper.TABLE_CHANNEL, values, whereClause,
				whereArgs);
	}

	/**
	 * 删除数据
	 */
	public void deleteData(String whereClause, String[] whereArgs) {
		mSQLiteDatabase.delete(SQLHelper.TABLE_CHANNEL, whereClause, whereArgs);
	}

	/**
	 * 获取数据
	 */
	public Cursor selectData(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		Cursor cursor = mSQLiteDatabase.query(SQLHelper.TABLE_CHANNEL, columns,
				selection, selectionArgs, groupBy, having, orderBy);
		return cursor;
	}
}
