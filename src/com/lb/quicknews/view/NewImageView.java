package com.lb.quicknews.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lb.quicknews.R;
import com.lb.quicknews.wedget.ProgressButton;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

@EViewGroup(R.layout.item_image)
public class NewImageView extends RelativeLayout implements
		ImageLoadingListener, ImageLoadingProgressListener {
	@ViewById(R.id.current_image)
	ImageView currentImage;
	@ViewById(R.id.current_page)
	TextView currentPage;
	@ViewById(R.id.progressButton)
	ProgressButton progressButton;
	CompoundButton.OnCheckedChangeListener checkedChangeListener;

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
