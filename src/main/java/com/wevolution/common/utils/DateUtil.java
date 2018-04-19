package com.wevolution.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期工具类
 */
public class DateUtil {
    /** 记录日志 */
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
    
    /** 年月日 yyyy-MM-dd */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /** 24小时制 yyyy-MM-dd HH:mm:ss */
    public static final String DATETIMEPATTERN24H = "yyyy-MM-dd HH:mm:ss";
    /** 24小时制 yyyy-MM-dd HH:mm:ss SSS*/
    public static final String DATETIMEPATTERN24HSS = "yyyy-MM-dd HH:mm:ss SSS";

    /** Base ISO 8601 Date format yyyyMMdd i.e., 20021225 for the 25th day of December in the year 2002 */
    public static final String ISO_DATE_FORMAT = "yyyyMMdd";

    /** Default lenient setting for getDate. */
    private static final boolean LENIENTDATE = false;

    /** 一天的秒数 **/
    private static final int DAYSERCOND = 24 * 60 * 60;

    /**
     * 返回当前日期字符串
     * 
     * @param pattern 日期字符串样式
     * @return
     */
    public static String getCurrentDateString(String pattern) {
        return dateToString(getCurrentDateTime(), pattern);
    }
    
    /**
     * 字符串转换为日期java.util.Date
     * 
     * @param dateText 字符串
     * @param format 日期格式
     * @return
     */
    public static Date stringToDate(String dateString) {
        return stringToDate(dateString, DATE_FORMAT, LENIENTDATE);
    }
    
    /**
     * 字符串转换为日期java.util.Date
     * 
     * @param dateText 字符串
     * @param format 日期格式
     * @return
     */
    public static Date stringToDateTime(String dateString) {
        return stringToDate(dateString, DATETIMEPATTERN24H, LENIENTDATE);
    }
    
    
    /**
     * 字符串转换为时间戳java.sql.Timestamp
     * 
     * @param dateText 字符串
     * @param format 日期格式
     * @return
     */
    public static Timestamp stringDatetimeToTimestamp(String dateString) {
    	if(dateString == null || dateString.trim().length() == 0){
    		return null;
    	}
    	if(dateString.trim().length() <= 10){
    		dateString = dateString.trim().concat(" 00:00:00");
    	}
        Date date = stringToDate(dateString, DATETIMEPATTERN24H, LENIENTDATE);
        if(date == null){
        	return null;
        }
        return new Timestamp(date.getTime());
    }
    public static Timestamp stringDatetimeToTimestampss(String dateString) {
    	if(dateString == null || dateString.trim().length() == 0){
    		return null;
    	}
    	if(dateString.trim().length() <= 10){
    		dateString = dateString.trim().concat(" 00:00:00");
    	}
    	Date date = stringToDate(dateString, DATETIMEPATTERN24HSS, LENIENTDATE);
    	if(date == null){
    		return null;
    	}
    	return new Timestamp(date.getTime());
    }

    /**
     * 字符串转换为日期java.util.Date
     * 
     * @param dateText 字符串
     * @param format 日期格式
     * @return
     */
    public static Date stringToDate(String dateString, String format) {
        return stringToDate(dateString, format, LENIENTDATE);
    }

    /**
     * 字符串转换为日期java.util.Date
     * @param dateText 字符串
     * @param format 日期格式
     * @param lenient 日期越界标志
     * @return
     */
    public static Date stringToDate(String dateText, String format, boolean lenient) {
        if (dateText == null) {
            return null;
        }
        dateText = dateText.replaceAll("\\+", " ");
        DateFormat df = null;
        try {
            if (format == null) {
                df = new SimpleDateFormat();
            } else {
                df = new SimpleDateFormat(format);
            }
            // setLenient avoids allowing dates like 9/32/2001
            // which would otherwise parse to 10/2/2001
            df.setLenient(false);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return df.parse(dateText);
        } catch (ParseException e) {
        	logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 返回当前时间
     * @return 返回当前时间
     */
    public static Date getCurrentDateTime() {
        java.util.Calendar calNow = java.util.Calendar.getInstance();
        java.util.Date dtNow = calNow.getTime();
        return dtNow;
    }
    
    /**
     * 返回当前时间戳
     * 
     * @return 返回当前时间戳
     */
    public static Timestamp getCurrentTimestamp() {
        java.util.Calendar calNow = java.util.Calendar.getInstance();
        return new Timestamp(calNow.getTimeInMillis());
    }

    /**
     * 根据时间变量返回时间字符串
     * 
     * @return 返回时间字符串
     * @param pattern 时间字符串样式
     * @param date 时间变量
     */
    public static String dateToString(Object date, String pattern) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat sfDate = new SimpleDateFormat(pattern);
            sfDate.setLenient(false);
            return sfDate.format(date);
        } catch (RuntimeException e) {
        	logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获取几天的秒数
     * @param day
     * @return
     */
    public static int transformDayToSecond(int day) {
        return day * DAYSERCOND;
    }

    /**
     * 取得当前年
     * 
     * @return
     */
    public static int getCurrentYear() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return Integer.parseInt(df.format(new Date()));
    }
    
    /**
     * 取得当前年月
     * @return
     */
    public static int getCurrentYearMonth() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
        return Integer.parseInt(df.format(new Date()));
    }
    
    /**
     * 取得当前年月日
     * @return
     */
    public static int getCurrentYearMonthDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(df.format(new Date()));
    }

    public static void main(String[] args) {
    	 String s = "2015-10-01+00:00:00";
    	 Date d = stringToDate(s);
    	logger.debug(d.toString());
    	logger.debug(getCurrentTimestamp().toString());
	}
}