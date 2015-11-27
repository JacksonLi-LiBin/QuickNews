package com.lb.quicknews;

import java.io.File;

import org.androidannotations.annotations.EApplication;

import com.lb.quicknews.db.SQLHelper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

@EApplication
public class App extends Application {
	private static App mApplication;
	private SQLHelper sqlHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		initImageLoader(mApplication);
	}

	/**
	 * 获取Application
	 */
	public static App getApplication() {
		return mApplication;
	}

	/**
	 * 获取数据库helper
	 */
	public SQLHelper getSQLHelper() {
		if (sqlHelper == null) {
			sqlHelper = new SQLHelper(mApplication);
		}
		return sqlHelper;
	}

	@Override
	public void onTerminate() {
		if (sqlHelper != null) {
			sqlHelper.close();
		}
		// 整体摧毁的时候调用这个方法
		super.onTerminate();
	}

	/**
	 * 初始化ImageLoader
	 */
	public static void initImageLoader(Context context) {
		String filePath = Environment.getExternalStorageDirectory()
				+ "/Android/data" + context.getPackageName() + "/cache/";
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, filePath);// 获取缓存的目录地址
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				context)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiskCache(cacheDir))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(configuration);
	}
}
