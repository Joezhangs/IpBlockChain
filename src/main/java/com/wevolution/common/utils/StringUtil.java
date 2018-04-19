package com.wevolution.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 是否为空判断
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }
    
    /**
     * 是否为空判断
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
        return o == null || "".equals(o.toString().trim());
    }
    
    /**
     * 字段串数组第一组不为空
     * @param ss
     * @return
     */
    public static boolean firstNotEmpty(final String[] ss) {
        return ss != null && ss.length > 0 && ss[0] != null && ss[0].length() > 0;
    }
    
    /**
     * 将Object型转换为字符串
     * 
     * @param str
     * @return
     */
    public static String valueOf(Object o) {
        if (o == null) {
            return null;
        }
        String s = null;
        if (o instanceof Number) {
            s = String.valueOf(o);
        } else {
            s = o.toString();
        }
        return s;
    }

    /**
     * 取数组中的第一组值
     * 
     * @param obj
     * @return
     */
    public static String getFirstString(Object obj) {
        if (obj == null) {
            return "";
        }
        String s = null;
        if (obj instanceof String[]) {
            String[] ss = (String[]) obj;
            s = ss[0];
        } else if (obj instanceof String) {
            s = (String) obj;
        } else {
            s = obj.toString();
        }
        return s;
    }

    /**
     * 获取数组的第一个元素，并且作了字符串null、""的判断，这两种情况都处理为null
     * 
     * @param obj
     * @return
     */
    public static String getFirstStr(Object obj) {
        if (obj == null) {
            return null;
        }
        String tmp = null;
        if (obj instanceof String[]) {
            String[] ss = (String[]) obj;
            tmp = ss[0];
        } else if (obj instanceof String) {
            tmp = (String) obj;
        }
        if ("".equals(tmp)) {
            tmp = null;
        }
        return tmp;
    }

    /**
     * 功能描述: 驼峰字符串转换成下划线连接<br>
     * 例如: nextValueMySql 转换为 next_value_my_sql
     * 
     * @param param
     * @return
     */
    public static String camelVunderline(String param) {
        Pattern p = Pattern.compile("[A-Z]");
        if (param == null || param.equals("")) {
            return "";
        }
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }
        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }


    /**
     * 将Null处理为空字符串
     * 
     * @param str
     * @return
     */
    public static String toEmpty(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }
    
	/**
	 * 删除最后的逗号
	 */
	public static StringBuilder removeLastChar(String sb){
		return removeLastChar(sb,",");
	}
	/**
	 * 删除最后的符号
	 */
	public static StringBuilder removeLastChar(String sb,String patten){
		if(sb.trim().endsWith(patten)){
			sb = sb.substring(0, sb.lastIndexOf(patten));
		}
		return new StringBuilder(sb);
	}
	/**
	 * 
	* @Title: createRandomVcode 
	* @Description: 6位随机数验证码（短信验证码）
	* @return  String 
	* @throws
	 */
	public static String createRandomVcode(){
        String vcode="";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
        return vcode;
    }
	/**
	 * 
	* @Title: checkMobile 
	* @Description: 手机号校验
	* @param mobile
	* @return 
	* @throws
	 */
	public static boolean checkMobile(String mobile) {
		if(isEmpty(mobile))
			return false;
			/*^匹配开始地方$匹配结束地方，[3|4|5|7|8]选择其中一个{4,8},\d从[0-9]选择
			{4,8}匹配次数4~8	，java中/表示转义，所以在正则表达式中//匹配/,/匹配""*/
		//验证手机号码格式是否正确
		else 
			return mobile.matches("^1[3|4|5|7|8][0-9]\\d{4,8}$");
	}
	/**
	 * 
	* @Title: matchingPwd 
	* @Description: 密码匹配（数字加字母组合）
	* @param num
	* @return 
	* boolean 
	* @throws
	 */
	public static boolean matchingPwd(String num){
		return !isEmpty(num)&&num.matches("(?!^(\\d+|[a-zA-Z]+|[~!@#$%^&*?]+)$)^[\\w~!@#$%\\^&*?]{8,16}+$");
	}
	public static void main(String[] args) {
		System.out.println(matchingPwd("a1234567"));;
	}
}
