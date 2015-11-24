package com.lb.quicknews.activity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lb.quicknews.R;
import com.lb.quicknews.bean.NewsDetailModle;
import com.lb.quicknews.bean.NewsModle;
import com.lb.quicknews.http.HttpUtil;
import com.lb.quicknews.http.json.NewsDetailJson;
import com.lb.quicknews.utils.Options;
import com.lb.quicknews.utils.StringUtils;
import com.lb.quicknews.wedget.ProgressPieView;
import com.lb.quicknews.wedget.htmltextview.HtmlTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

@EActivity(R.layout.activity_details)
public class DetailsActivity extends BaseActivity implements
		ImageLoadingListener, ImageLoadingProgressListener {
	@ViewById(R.id.new_title)
	TextView newTitle;
	@ViewById(R.id.new_time)
	TextView newTime;
	@ViewById(R.id.web_details)
	HtmlTextView webDetails;
	@ViewById(R.id.progressPieView)
	ProgressPieView mProgressPieView;
	@ViewById(R.id.new_img)
	ImageView newImg;
	@ViewById(R.id.img_count)
	TextView imgCount;
	@ViewById(R.id.play)
	ImageView mPlay;
	private String newUrl;
	private NewsModle newsModle;
	private String newId;
	ImageLoader imageLoader;
	private String imgCountString;
	DisplayImageOptions options;
	private NewsDetailModle newsDetailModle;

	@AfterInject
	void init() {
		try {
			newsModle = (NewsModle) getIntent().getExtras().getSerializable(
					"newsModle");
			newId = newsModle.getDocid();
			newUrl = getUrl(newId);
			imageLoader = ImageLoader.getInstance();
			options = Options.getListOptions();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterViews
	public void initWebView() {
		try {
			mProgressPieView.setShowText(true);
			mProgressPieView.setShowImage(false);
			showProcessDialog();
			loadData(newUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Click(R.id.new_img)
	public void imageMore(View view) {
		try {
			Bundle bundle = new Bundle();
			bundle.putSerializable("newsDetailModle", newsDetailModle);
			if (!"".equals(newsDetailModle.getUrl_mp4())) {
				bundle.putString("playUrl", newsDetailModle.getUrl_mp4());
				openActivity(VideoPlayActivity_.class, bundle, 0);
			} else {
				openActivity(ImageDetailActivity_.class, bundle, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onProgressUpdate(String arg0, View arg1, int arg2, int arg3) {
		int currentPro = (arg2 * 100 / arg3);
		if (currentPro == 100) {
			mProgressPieView.setVisibility(View.GONE);
			mProgressPieView.setText("");
		} else {
			mProgressPieView.setVisibility(View.VISIBLE);
			mProgressPieView.setProgress(currentPro);
			mProgressPieView.setText(currentPro + "%");
		}
	}

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
		mProgressPieView.setVisibility(View.GONE);
	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		if (!"".equals(newsDetailModle.getUrl_mp4())) {
			mPlay.setVisibility(View.VISIBLE);
		} else {
			imgCount.setVisibility(View.VISIBLE);
			imgCount.setText(imgCountString);
		}
		mProgressPieView.setVisibility(View.GONE);
	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		mProgressPieView.setVisibility(View.GONE);
	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {
		mProgressPieView.setVisibility(View.VISIBLE);
	}

	private void loadData(String url) {
		if (hasNetWork()) {
			loadNewDetailData(url);
		} else {
			dismissProcessDialog();
			showShortToast(getString(R.string.not_network));
			String result = getCacheStr(newUrl);
			if (!StringUtils.isBlank(result)) {
				getResult(result);
			}
		}
	}

	@Background
	public void loadNewDetailData(String url) {
		String result;
		try {
			result = HttpUtil.getByHttpClient(this, url);
			getResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@UiThread
	public void getResult(String result) {
		newsDetailModle = NewsDetailJson.getInstance(this).readjsonNewsModles(
				result, newId);
		if (newsDetailModle == null) {
			return;
		}
		setCacheStr(newUrl, result);
		if (!"".equals(newsDetailModle.getUrl_mp4())) {
			imageLoader.displayImage(newsDetailModle.getCover(), newImg,
					options, this, this);
			newImg.setVisibility(View.VISIBLE);
		} else {
			if (newsDetailModle.getImgList().size() > 0) {
				imgCountString = "共" + newsDetailModle.getImgList().size()
						+ "张";
				imageLoader.displayImage(newsDetailModle.getImgList().get(0),
						newImg, options, this, this);
				newImg.setVisibility(View.VISIBLE);
			}
		}
		newTitle.setText(newsDetailModle.getTitle());
		newTime.setText("来源:" + newsDetailModle.getSource() + " "
				+ newsDetailModle.getPtime());
		String content = newsDetailModle.getBody();
		content = content.replace("<!--VIDEO#1--></p><p>", "");
		content = content.replace("<!--VIDEO#2--></p><p>", "");
		content = content.replace("<!--VIDEO#3--></p><p>", "");
		content = content.replace("<!--VIDEO#4--></p><p>", "");
		content = content.replace("<!--REWARD#0--></p><p>", "");
		webDetails.setHtmlFromString(content, false);
		dismissProcessDialog();
	}
}
