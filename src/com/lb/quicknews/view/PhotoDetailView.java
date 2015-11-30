package com.lb.quicknews.view;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.utils.Options;
import com.lb.quicknews.wedget.ProgressButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EViewGroup(R.layout.photo_item_detail)
public class PhotoDetailView extends RelativeLayout implements ImageLoadingListener, ImageLoadingProgressListener {
	@ViewById(R.id.current_image)
	ImageView currentImage;
	@ViewById(R.id.photo_content)
	TextView photoContent;
	@ViewById(R.id.photo_count)
	TextView photoCount;
	@ViewById(R.id.photo_title)
	TextView photoTitle;
	@ViewById(R.id.progressButton)
	ProgressButton progressButton;
	CompoundButton.OnCheckedChangeListener checkedChangeListener;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	Context context;

	public PhotoDetailView(Context context) {
		super(context);
		this.context = context;
		options = Options.getListOptions();
		progress(context);
	}

	public void setImage(int count, int position, String content, String title, String imgUrl) {
		photoCount.setText((position + 1) + "/" + count);
		photoContent.setText(content);
		photoTitle.setText(title);
		imgUrl = imgUrl.replace("auto", "854x480x75x0x0x3");
		imageLoader.displayImage(imgUrl, currentImage, options, this, this);
	}

	@AfterViews
	void initView() {
		progressButton.setOnCheckedChangeListener(checkedChangeListener);
	}

	public void progress(final Context context) {
		checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				updatePinProgressContentDescription((ProgressButton) buttonView, context);
			}
		};
	}

	private void updatePinProgressContentDescription(ProgressButton button, Context context) {
		int progress = button.getProgress();
		if (progress <= 0) {
			button.setContentDescription(context.getString(button.isChecked()
					? R.string.content_desc_pinned_not_downloaded : R.string.content_desc_unpinned_not_downloaded));
		} else if (progress >= button.getMax()) {
			button.setContentDescription(context.getString(button.isChecked() ? R.string.content_desc_pinned_downloaded
					: R.string.content_desc_unpinned_downloaded));
		} else {
			button.setContentDescription(context.getString(button.isChecked() ? R.string.content_desc_pinned_downloading
					: R.string.content_desc_unpinned_downloading));
		}
	}

	@Override
	public void onProgressUpdate(String arg0, View arg1, int arg2, int arg3) {
		int currentpro = (arg2 * 100 / arg3);
		if (currentpro == 100) {
			progressButton.setVisibility(View.GONE);
		} else {
			progressButton.setVisibility(View.VISIBLE);
		}
		progressButton.setProgress(currentpro);
		updatePinProgressContentDescription(progressButton, context);
	}

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
		progressButton.setVisibility(View.GONE);
	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		progressButton.setVisibility(View.GONE);
	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		progressButton.setVisibility(View.GONE);
	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {

	}

}
