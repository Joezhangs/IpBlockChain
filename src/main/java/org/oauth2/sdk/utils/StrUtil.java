package org.oauth2.sdk.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 字符串相关方法
 */
public class StrUtil {

	/**
	 * 获取32位UUID
	 */
	public static String get32UUID() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}

	/**
	 * 字符串是否为空,调用：StrUtil.isBlank(null),返回：true
	 */
	public static boolean isBlank(String str) {
		return str == null || "".equals(str.trim()) || str.equals("null") ? true : false;
	}

	/**
	 * 获取map中的值
	 */
	public static String getString(Map<String, Object> map, String key) {
		Object obj = map.get(key);
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	/**
	 * 判断字符串是否为空
	 */
	public static boolean notBlank(String str) {
		return str == null || "".equals(str.trim()) ? false : true;
	}

	/**
	 * 获取系统编码
	 */
	public static String getEncoding() {
		return System.getProperty("file.encoding");
	}
	
	/**
	 * 获取系统编码
	 */
	public static String URLEncoder(String s) {
		try {
			return URLEncoder.encode(s, Const.ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 转换JSON数据
	 */
	public static JSONObject paramToJson(String s) {
		String[] ary = s.split("&");
		Map<String, String> dataMap = new HashMap<String, String>();
		for (int i = 0; i < ary.length; i++) {
			String text = ary[i];
			if (!StrUtil.isBlank(text)) {
				String[] nv = text.split("=");
				dataMap.put(nv[0], nv[1]);
			}
		}
		return JSONObject.parseObject(JSON.toJSONString(dataMap));
	}
	
	/**
	 * 转换JSON数据
	 */
	public static JSONObject funToJson(String s) {
		int begin = s.indexOf("(");
		int end = s.indexOf(")");
		s = s.substring(begin + 1, end).trim();
		return JSONObject.parseObject(s);
	}
}