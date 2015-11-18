package com.lb.quicknews.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.NameValuePair;

import android.util.Log;

/**
 * Created by Administrator on 2015/11/17 0017.
 */
public class CustomHttpURLConnection {
    private static final String TAG = "CustomHttpURLConnection";
    private static HttpURLConnection conn;

    public CustomHttpURLConnection() {

    }

    public static String getFromWebByHttpUrlConnection(String strUrl, NameValuePair... nameValuePairs) {
        String result = "";
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(4000);
            conn.setRequestProperty("accept", "*/*");
            conn.connect();
            InputStream stream = conn.getInputStream();
            InputStreamReader inReader = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(inReader);
            String strLine = null;
            while ((strLine = buffer.readLine()) != null) {
                result += strLine;
            }
            inReader.close();
            conn.disconnect();
            return result;
        } catch (Exception e) {
            Log.e(TAG, "get from web by http connection:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String postToWebByHttpURLConnection(String strUrl, NameValuePair... nameValuePairs) {
        String result = "";
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
//            设置是否从httpURLconnection读入，默认情况下是true
            conn.setDoInput(true);
//            设置是否向httpURLconnection输出，因为这个是post请求，参数要放在
//                    http正文内，因此设为true，默认情况下是false
            conn.setDoOutput(true);
//            设定请求的方法为post，默认是get
            conn.setRequestMethod("POST");
//            设置超时
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(4000);
//            post请求不能使用缓存
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            //设定传递的内容类型是可序列化的Java对象
            //如果不设定此项，咱传递序列化对象时，当web服务默认的不是这种类型时可能抛java.io.EOFException
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //连接，从上述url.openConnection()至此的配置必须要在connect之前完成
            InputStream stream = conn.getInputStream();
            InputStreamReader inStream = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(inStream);
            String readLine = null;
            while ((readLine = buffer.readLine()) != null) {
                result += readLine;
            }
            return result;
        } catch (Exception e) {
            Log.e(TAG, "post to web by http connection:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
