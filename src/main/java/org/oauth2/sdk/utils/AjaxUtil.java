package org.oauth2.sdk.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;


/**
 * 发送网络请求
 */
public class AjaxUtil {
	
	protected static final Logger LOGGER = Logger.getLogger("GithubOauth");

	/**
	 * 发起https请求并获取结果
	 */
	public static String get(String path) {
		return httpsRequest(path, "GET", null);
	}
	
	/**
	 * 发起https请求并获取结果
	 */
	public static String get(String path, String params) {
		return httpsRequest(path, "GET", params);
	}
	
	/**
	 * 发起https请求并获取结果
	 */
	public static String post(String path, String params) {
		return httpsRequest(path, "POST", params);
	}
	
	/**
	 * 发起https请求并获取结果
	 */
	public static String httpsRequest(String path, String methodType, String params) {
		StringBuffer buffer = new StringBuffer();
		String encoded = Const.ENCODING;
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(path);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(methodType);
			if ("GET".equalsIgnoreCase(methodType)) {
				httpUrlConn.connect();
			}
			// 当有数据需要提交时
			if (!StrUtil.isBlank(params)) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(params.getBytes(encoded));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, encoded);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (ConnectException ce) {
			LOGGER.info("微信服务器连接超时！");
			ce.printStackTrace();
		} catch (Exception e) {
			LOGGER.info("HTTPS 访问错误：" + e);
			e.printStackTrace();
		}
		return buffer.toString();
	}
}