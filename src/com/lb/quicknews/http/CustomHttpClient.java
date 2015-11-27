package com.lb.quicknews.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.lb.quicknews.R;

import android.content.Context;

/**
 * Created by Administrator on 2015/11/17 0017.
 */
@SuppressWarnings("deprecation")
public class CustomHttpClient {
	private static final String TAG = "CustomHttpClient";
	private static final String CHARSET_UTF8 = HTTP.UTF_8;
	private static final String CHARSET_GB2312 = "GB2312";
	private static DefaultHttpClient customerHttpClient;
	private static CookieStore cookieStore;

	public static CookieStore getCookieStore() {
		return cookieStore;
	}

	public CustomHttpClient() {

	}

	/**
	 * post 方法
	 */
	public static String postToWebByHttpClient(Context context, String strUrl,
			NameValuePair... nameValuePairs) {
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (nameValuePairs != null) {
				for (int i = 0; i < nameValuePairs.length; i++) {
					params.add(nameValuePairs[i]);
				}
			}
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
					params, CHARSET_GB2312);
			HttpPost httpPost = new HttpPost(strUrl);
			httpPost.setEntity(urlEncodedFormEntity);
			DefaultHttpClient client = getHttpClient(context);
			if (cookieStore != null) {
				client.setCookieStore(cookieStore);
			}
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			cookieStore = client.getCookieStore();
			return (resEntity == null) ? null : EntityUtils.toString(resEntity,
					CHARSET_UTF8);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getFromWebByHttpClient(Context context, String strUrl,
			NameValuePair... nameValuePairs) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(strUrl);
			if (nameValuePairs != null && nameValuePairs.length > 0) {
				sb.append("?");
				for (int i = 0; i < nameValuePairs.length; i++) {
					if (i > 0) {
						sb.append("&");
					}
					sb.append(String.format("%s=%s",
							nameValuePairs[i].getName(),
							nameValuePairs[i].getValue()));
				}
			}
			// httpget连接对象
			HttpGet httpGet = new HttpGet(sb.toString());
			// 取得httpclient对象
			DefaultHttpClient client = getHttpClient(context);
			if (cookieStore != null) {
				client.setCookieStore(cookieStore);
			}
			// 请求httpclient，获得httpresponse
			HttpResponse response = client.execute(httpGet);
			// 请求成功
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException(context.getResources().getString(
						R.string.httpError));
			}
			cookieStore = client.getCookieStore();
			return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static synchronized DefaultHttpClient getHttpClient(Context context) {
		HttpParams params = new BasicHttpParams();
		// 设置参数
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, CHARSET_UTF8);
		HttpProtocolParams.setUseExpectContinue(params, true);
		HttpProtocolParams
				.setUserAgent(
						params,
						"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
								+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
		// 超时设置
		// 从连接池中取连接的超时
		ConnManagerParams.setTimeout(params, 10000);
		// 连接超时
		int connectionTimeOut = 20000;
		if (!HttpUtil.isWifiDataEnable(context)) {
			connectionTimeOut = 100000;
		}
		HttpConnectionParams.setConnectionTimeout(params, connectionTimeOut);
		// 请求超时
		HttpConnectionParams.setSoTimeout(params, 40000);
		// 设置我们的HttpClient支持http和HTTPS两种模式
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));
		// 使用线程安全的连接管理来创建httpclient
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);
		customerHttpClient = new DefaultHttpClient(conMgr, params);
		return customerHttpClient;
	}
}
