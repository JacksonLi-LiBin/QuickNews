package com.lb.quicknews.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.bean.VideoModle;
import com.lb.quicknews.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@EViewGroup(R.layout.item_video)
public class VideoItemView extends LinearLayout {
	@ViewById(R.id.video_img)
	ImageView videoView;
	@ViewById(R.id.video_title)
	TextView videoTitle;
	@ViewById(R.id.video_time)
	TextView videoTime;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	public VideoItemView(Context context) {
		super(context);
		options = Options.getListOptions();
	}

	public void setData(VideoModle videoModle) {
		videoTime.setText(videoModle.getLength());
		videoTitle.setText(videoModle.getTitle());
		imageLoader.displayImage(videoModle.getCover(), videoView, options);
	}
}
