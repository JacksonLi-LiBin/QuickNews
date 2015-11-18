package com.lb.quicknews.http;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2015/11/17 0017.
 */
public class HttpUtil {
    //    网络连接部分
    public static String postByHttpURLConnection(String strUrl, NameValuePair... nameValuePairs) {
        return CustomHttpURLConnection.postToWebByHttpURLConnection(strUrl, nameValuePairs);
    }

    public static String getByHttpURLConnection(String strUrl, NameValuePair... nameValuePairs) {
        return CustomHttpURLConnection.getFromWebByHttpUrlConnection(strUrl, nameValuePairs);
    }

    public static String postByHttpClient(Context context, String strUrl, NameValuePair... nameValuePairs) {
        String result = CustomHttpClient.postToWebByHttpClient(context, strUrl, nameValuePairs);
        return result;
    }

    public static String getByHttpClient(Context context, String strUrl, NameValuePair... nameValuePairs) {
        String result = CustomHttpClient.getFromWebByHttpClient(context, strUrl, nameValuePairs);
        if (TextUtils.isEmpty(result)) {
            result = "";
        }
        return result;
    }
    //网络连接判断

    /**
     * 判断是否有网络
     */
    public static boolean isNetWorkAvailable(Context context) {
        String TAG = "HttpUtil.isNetWorkAvailable";
        try {
            return NetWorkHelper.isNetWorkAvailable(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断mobile网络是否可用
     */
    public static boolean isMobileDataEnable(Context context) {
        String TAG = "HttpUtil.isMobileDataEnable";
        try {
            return NetWorkHelper.isMobileDataEnable(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断WiFi网络是否可用
     */
    public static boolean isWifiDataEnable(Context context) {
        try {
            return NetWorkHelper.isWifiDataEnable(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否为漫游
     */
    public static boolean isNetWorkRoaming(Context context) {
        try {
            return NetWorkHelper.isNetWorkRoaming(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 编码测试
     */
    public static void testCharset(String datastr) {
        try {
            String temp = new String(datastr.getBytes(), "GBK");
            Log.v("TestCharset", "****** getBytes() -> GBK ******/n" + temp);
            temp = new String(datastr.getBytes("GBK"), "UTF-8");
            Log.v("TestCharset", "****** GBK -> UTF-8 *******/n" + temp);
            temp = new String(datastr.getBytes("GBK"), "ISO-8859-1");
            Log.v("TestCharset", "****** GBK -> ISO-8859-1 *******/n" + temp);
            temp = new String(datastr.getBytes("ISO-8859-1"), "UTF-8");
            Log.v("TestCharset", "****** ISO-8859-1 -> UTF-8 *******/n" + temp);
            temp = new String(datastr.getBytes("ISO-8859-1"), "GBK");
            Log.v("TestCharset", "****** ISO-8859-1 -> GBK *******/n" + temp);
            temp = new String(datastr.getBytes("UTF-8"), "GBK");
            Log.v("TestCharset", "****** UTF-8 -> GBK *******/n" + temp);
            temp = new String(datastr.getBytes("UTF-8"), "ISO-8859-1");
            Log.v("TestCharset", "****** UTF-8 -> ISO-8859-1 *******/n" + temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
