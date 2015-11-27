package com.lb.quicknews.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.utils.StringUtils;
import com.lb.quicknews.wedget.discrollview.DiscrollView;

import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2015/11/17 0017.
 */
@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {
	@ViewById(R.id.layout)
	protected DiscrollView mDiscrollView;

	@ViewById(R.id.lastView3)
	protected Button mButton;
	private String cache;

	@AfterViews
	public void initView() {
		String result = getCacheStr("welcome");
		cache = getCacheStr("MoreActivity");
		if (StringUtils.isBlank(cache)) {
			if (!StringUtils.isEmpty(result)) {
				mDiscrollView.setVisibility(View.GONE);
				MainActivity_.intent(this).start();
				defaultFinishNotActivityHelper();
			}
		} else {
			mDiscrollView.setVisibility(View.VISIBLE);
		}
	}

	@Click(R.id.lastView3)
	public void startApp(View view) {
		if (StringUtils.isEmpty(cache)) {
			setCacheStr("welcome", "welcome");
			MainActivity_.intent(this).start();
		}
		defaultFinishNotActivityHelper();
		setCacheStr("MoreActivity", "");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
