package com.lb.quicknews.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

public class NewImageView extends RelativeLayout implements
		ImageLoadingListener, ImageLoadingProgressListener {

	public NewImageView(Context context) {
		super(context);
	}

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProgressUpdate(String arg0, View arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

}
