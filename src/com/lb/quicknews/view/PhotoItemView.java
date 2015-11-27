package com.lb.quicknews.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EViewGroup(R.layout.item_photo)
public class PhotoItemView extends RelativeLayout {
	@ViewById(R.id.photo_img)
	ImageView photoImg;
	@ViewById(R.id.photo_title)
	TextView photoTitle;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	public PhotoItemView(Context context) {
		super(context);
		options = Options.getListOptions();
	}

	public void setData(String title, String picUrl) {
		picUrl = picUrl.replace("auto", "854x480x75x0x0x3");
		photoTitle.setText(title);
		imageLoader.displayImage(picUrl, photoImg, options);
	}
}
