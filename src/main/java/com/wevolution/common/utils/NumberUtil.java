package com.wevolution.common.utils;

/**
 * Title: NumberUtil.java    
 * Description: 数字处理工具类
 */
public class NumberUtil {
	
	/**
	 * @discription 转换为字符串
	 */
	public static String toString(Number num){
		if(num == null){
			return null;
		}
		return num.toString();
	}
	
    /**
     * 将Object型转换Short
     * @param o
     * @return
     */
    public static Short dealShort(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number || o instanceof String) {
           return Short.parseShort(o.toString());
        }
        return null;
    }
    
    /**
     * 将Object型转换Integer
     * @param o
     * @return
     */
    public static Integer dealInteger(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number || o instanceof String) {
           return Integer.parseInt(o.toString());
        }
        return null;
    }
    
    /**
     * 将Object型转换Long
     * @param o
     * @return
     */
    public static Long dealLong(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number || o instanceof String) {
           return Long.parseLong(o.toString());
        }
        return null;
    }
    
    /**
     * 将Object型转换Float
     * @param o
     * @return
     */
    public static Float dealFloat(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number || o instanceof String) {
           return Float.parseFloat(o.toString());
        }
        return null;
    }
	
	
}
