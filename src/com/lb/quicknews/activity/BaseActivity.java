package com.lb.quicknews.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.lb.quicknews.http.HttpUtil;
import com.lb.quicknews.http.Url;
import com.lb.quicknews.utils.ACache;
import com.lb.quicknews.utils.DialogUtil;
import com.lb.quicknews.utils.StringUtils;
import com.lb.quicknews.wedget.slidingactivity.IntentUtils;
import com.lb.quicknews.wedget.slidingactivity.SlidingActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/11/17 0017.
 */
public class BaseActivity extends SlidingActivity {
/**
 * 手势监听
 */
//    private GestureDetector mGestureDetector;
    /**
     * 是否需要监听手势关闭功能
     */
    private boolean mNeedBackGesture = false;
    private Dialog processDialog;
    public static final int REQUEST_CODE = 1000;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean isSupportSlide() {
        return true;
    }

    public String getUrl(String newId) {
        return Url.NewDetail + newId + Url.endDetailUrl;
    }

    public String getMsgUrl(String index, String itemId) {
        String urlString = Url.CommonUrl + itemId + "/" + index + "-40.html";
        return urlString;
    }

    public String getWeatherUrl(String cityName) throws UnsupportedEncodingException {
        String urlString = Url.WeatherHost + URLEncoder.encode(cityName, "utf-8");
        return urlString;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        无标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否进行手势监听
     */
    public void setNeedBackGesture(boolean mNeedBackGesture) {
        this.mNeedBackGesture = mNeedBackGesture;
    }

    /**
     * 显示Dialog
     */
    public void showProcessDialog() {
        try {
            if (processDialog == null) {
                processDialog = DialogUtil.createLoadingDialog(this);
            }
            processDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏Dialog
     */
    public void dismissProcessDialog() {
        try {
            if (processDialog != null && processDialog.isShowing()) {
                processDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dialog是否显示
     *
     * @return
     */
    public boolean isShowing() {
        try {
            if (processDialog != null && processDialog.isShowing()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 工具类打开Activity
     */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null, 0);
    }

    public void openActivityForResult(Class<?> pClass, int requestCode) {
        openActivity(pClass, null, requestCode);
    }

    /**
     * 工具类打开Activity并携带参数
     */
    public void openActivity(Class<?> pClass, Bundle pBundle, int requestCode) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        if (requestCode == 0) {
            IntentUtils.startPreviewActivity(this, intent, 0);
        } else {
            IntentUtils.startPreviewActivity(this, intent, requestCode);
        }
    }

    /**
     * 判断是否有网络
     */
    public boolean hasNetWork() {
        return HttpUtil.isNetWorkAvailable(this);
    }

    /**
     * 显示short toast
     */
    public void showShortToast(String pMsg) {
        Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示自定义Toast
     */
    public void showCustomToast(String pMsg) {
//        Crouton.makeText(this,pMsg, Style.ALERT, R.id)
    }

    //设置缓存数据
    public void setCacheStr(String key, String value) {
        if (!StringUtils.isEmpty(value)) {
            ACache.get(this).put(key, value);
        }
    }

    //获取缓存数据根据key
    public String getCacheStr(String key) {
        return ACache.get(this).getAsString(key);
    }

    /**
     * 系统默认关闭
     */
    public void defaultFinish() {
        super.finish();
    }

    /**
     * 系统默认关闭
     */
    public void defaultFinishNotActivityHelper() {
        super.finish();
    }

    public void doBack(View view) {
        onBackPressed();
    }
}
