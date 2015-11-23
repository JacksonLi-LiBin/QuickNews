package com.lb.quicknews.view;

import java.util.List;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lb.quicknews.R;
import com.lb.quicknews.bean.NewsModle;
import com.lb.quicknews.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@EViewGroup(R.layout.item_new)
public class NewItemView extends LinearLayout {
	@ViewById(R.id.left_image)
	ImageView leftImage;
	@ViewById(R.id.item_title)
	TextView itemTitle;
	@ViewById(R.id.item_content)
	TextView itemContent;
	@ViewById(R.id.article_top_layout)
	RelativeLayout articleLayout;
	@ViewById(R.id.layout_image)
	LinearLayout imageLayout;
	@ViewById(R.id.item_iamge_0)
	ImageView item_image0;
	@ViewById(R.id.item_image_1)
	ImageView item_image1;
	@ViewById(R.id.item_image_2)
	ImageView item_image2;
	@ViewById(R.id.item_abstract)
	TextView itemAbstract;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	public NewItemView(Context context) {
		super(context);
		options = Options.getListOptions();
	}

	public void setTexts(String titleText, String contentText, String imgUrl, String currentItem) {
		articleLayout.setVisibility(View.VISIBLE);
		imageLayout.setVisibility(View.GONE);
		itemTitle.setText(titleText);
		if ("北京".equals(currentItem)) {

		} else {
			itemContent.setText(contentText);
		}
		if (!"".equals(imgUrl)) {
			leftImage.setVisibility(View.VISIBLE);
			imageLoader.displayImage(imgUrl, leftImage, options);
		} else {
			leftImage.setVisibility(View.GONE);
		}
	}

	public void setImages(NewsModle newsModle) {
		imageLayout.setVisibility(View.VISIBLE);
		articleLayout.setVisibility(View.GONE);
		itemAbstract.setText(newsModle.getTitle());
		List<String> imageModle = newsModle.getImagesModle().getImgList();
		imageLoader.displayImage(imageModle.get(0), item_image0, options);
		imageLoader.displayImage(imageModle.get(1), item_image1, options);
		imageLoader.displayImage(imageModle.get(2), item_image2, options);
	}
}
