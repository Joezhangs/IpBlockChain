package com.wevolution.common.utils;

import java.util.UUID;
/**
 * 
* @ClassName: UUIDTool 
* @Description: uuidtool
* @author jiangjian
* @date 2017年8月16日 上午11:44:29 
*
 */
public class UUIDTool {
	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
