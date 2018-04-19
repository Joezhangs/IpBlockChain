package com.wevolution.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wevolution.common.Constants;


public class ResponseUtil {
    /** 记录日志的变量 */
    private static Logger logger = LoggerFactory.getLogger(ResponseUtil.class);
    
	/**
	 * 统一获取返回的json格式
	 * @return	
	 */
    public static String getResponseJson(int statusCode,String msg, Object content){
    	return getResponseJson(statusCode, msg, content, null);
    }
	
	/**
	 * 统一获取返回的json格式
	 * @return	
	 */
	public static String getResponseJson(int statusCode,String msg, Object content, String callBack){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Constants.STATUS, statusCode);
		map.put(Constants.MSG, msg);
		if(content == null){
			content = "null";
		}
		map.put(Constants.DATA, content);
		String json = JacksonUtil.mapToJson(map);
		if(!StringUtil.isEmpty(callBack)){
			json = "(".concat(json).concat(")");
		}
		return json;
	}
	
	/**
	 * 获取忽略空值的json格式(忽略""和NULL)
	 * @return	
	 */
	public static String getResponseJsonNotEmpty(int statusCode,String msg, Object content, String callBack){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Constants.STATUS, statusCode);
		map.put(Constants.MSG, msg);
		map.put(Constants.DATA, content);
		String json = JacksonFilterUtil.mapToJson(map);
		if(!StringUtil.isEmpty(callBack)){
			json = "(".concat(json).concat(")");
		}
		return json;
	}
	
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("aa", "abc");
		map.put("result", 100);
		map.put("bb", null);
		String json = getResponseJson(0, "成功", map, null);
		logger.debug(json);
	}
}
