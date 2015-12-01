package com.lb.quicknews.activity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import com.lb.quicknews.R;

import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

@WindowFeature({ Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS })
@Fullscreen
@EActivity(R.layout.activity_play_video)
public class VideoPlayActivity extends BaseActivity
		implements OnInfoListener, OnBufferingUpdateListener, OnPreparedListener {
	@ViewById(R.id.buffer)
	VideoView mVideoView;
	@ViewById(R.id.probar)
	ProgressBar mProgressBar;
	@ViewById(R.id.load_rate)
	TextView mLoadRate;
	@ViewById(R.id.video_title)
	TextView mVideoTitle;
	@ViewById(R.id.video_end)
	ImageView mVideoEnd;
	private Uri uri;
	private String playUrl;
	private String title;

	@AfterInject
	void init() {
		try {
			if (!LibsChecker.checkVitamioLibs(this)) {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterViews
	public void initView() {
		try {
			playUrl = getIntent().getExtras().getString("playUrl");
			title = getIntent().getExtras().getString("filename");
			if ("".equals(playUrl) || playUrl == null) {
				showShortToast("请求地址错误");
				finish();
			}
			mVideoTitle.setText(title == null ? "" : title);
			uri = Uri.parse(playUrl);
			mVideoView.setVideoURI(uri);
			mVideoView.setMediaController(new MediaController(this));
			mVideoView.requestFocus();
			mVideoView.setOnInfoListener(this);
			mVideoView.setOnBufferingUpdateListener(this);
			mVideoView.setOnPreparedListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.setPlaybackSpeed(1.0f);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		mLoadRate.setText(percent + "%");
		mVideoView.setFileName(title);
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				mProgressBar.setVisibility(View.VISIBLE);
				mLoadRate.setText("");
				mLoadRate.setVisibility(View.VISIBLE);
			}
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			mVideoView.start();
			mProgressBar.setVisibility(View.GONE);
			mLoadRate.setVisibility(View.GONE);
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			break;
		default:
			break;
		}
		return false;
	}
}
