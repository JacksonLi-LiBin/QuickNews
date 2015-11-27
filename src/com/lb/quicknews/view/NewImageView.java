package com.lb.quicknews.view;

import java.util.List;

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
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	Context context;

	public NewImageView(Context context) {
		super(context);
		this.context = context;
		options = Options.getListOptions();
		progress(context);
	}

	public void setImage(List<String> imgList, int position) {
		currentPage.setText((position + 1) + "/" + imgList.size());
		imageLoader.displayImage(imgList.get(position), currentImage, options,
				this, this);
	}

	@AfterViews
	void initView() {
		progressButton.setOnCheckedChangeListener(checkedChangeListener);
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

	@Override
	public void onProgressUpdate(String arg0, View arg1, int arg2, int arg3) {
		int currentpro = (arg2 * 100 / arg3);
		if (currentpro == 100) {
			progressButton.setVisibility(View.GONE);
		} else {
			progressButton.setVisibility(View.VISIBLE);
		}
		if (currentpro >= 0 && currentpro <= 100) {
			progressButton.setProgress(currentpro);
		}
		updatePinProgressContentDescription(progressButton, context);
	}

	public void progress(final Context context) {
		checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				updatePinProgressContentDescription(
						(ProgressButton) buttonView, context);
			}
		};
	}

	private void updatePinProgressContentDescription(ProgressButton button,
			Context context) {
		int progress = button.getProgress();
		if (progress <= 0) {
			button.setContentDescription(context.getString(button.isChecked() ? R.string.content_desc_pinned_not_downloaded
					: R.string.content_desc_unpinned_not_downloaded));
		} else if (progress >= button.getMax()) {
			button.setContentDescription(context.getString(button.isChecked() ? R.string.content_desc_pinned_downloaded
					: R.string.content_desc_unpinned_downloaded));
		} else {
			button.setContentDescription(context.getString(button.isChecked() ? R.string.content_desc_pinned_downloading
					: R.string.content_desc_unpinned_downloading));
		}
	}
}
