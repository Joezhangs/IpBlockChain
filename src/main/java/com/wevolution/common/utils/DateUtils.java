package com.wevolution.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期工具类
 * @Description: TODO
 * @author wenhua.li@c2bmotors.com  
 * @date 2015-9-5 下午5:18:30
 */
public class DateUtils{

	private static GregorianCalendar gc = null;

	static {
		gc = new GregorianCalendar(Locale.CHINA);
		gc.setLenient(true);
		gc.setFirstDayOfWeek(Calendar.MONDAY);
	}

	private static void initCalendar(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("argument date must be not null");
		}
		gc.clear();
		gc.setTime(date);
	}

	/**
	 * 返回传入日期的后一天
	 * 
	 * @param Date
	 *            date
	 * @return Date
	 * @author MaChao
	 */
	public static Date getNextDay(Date date) {
		initCalendar(date);
		long tomorrow = gc.getTimeInMillis() + 1 * 60 * 60 * 24 * 1000;
		gc.setTimeInMillis(tomorrow);
		return gc.getTime();
	}

	/**
	 * 返回传入日期的前一天
	 * 
	 * @param Date
	 *            date
	 * @return Date
	 * @author MaChao
	 */
	public static Date getLastDay(Date date) {
		initCalendar(date);
		long yestarday = gc.getTimeInMillis() - 1 * 60 * 60 * 24 * 1000;
		gc.setTimeInMillis(yestarday);
		return gc.getTime();
	}
	/**
	 * 获取传入时间的过去几天的日期
	* @Title: getPassDay 
	* @param date
	* @param pass
	* @return 
	*
	 */
	public static Date getPassDay(Date date,int pass) {
		initCalendar(date);
		long yestarday = gc.getTimeInMillis() - pass * 60 * 60 * 24 * 1000;
		gc.setTimeInMillis(yestarday);
		return gc.getTime();
	}

	/**
	 * 获得日期所在月的最后一天
	 * 
	 * @param String
	 *            dateStr(yyyy-MM-dd)
	 * @return String(yyyy-MM-dd)
	 * @throws ParseException
	 * @author MaChao
	 */
	public static String getLastMonthDay(String dateStr) throws ParseException {
		Date date = praseString2Date(dateStr);
		Date lastMonthDay = getLastMonthDay(date);
		return formatDate2ShortString(lastMonthDay);
	}

	/**
	 * 将Date格式化为短型String
	 * 
	 * @param Date
	 *            date
	 * @return String(yyyy-MM-dd)
	 * @author MaChao
	 */
	public static String formatDate2ShortString(Date date) {
		initCalendar(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(date);
		return dateStr;
	}
	
	/**
	 * 输出当天是星期几
	 * 
	 * @param Date
	 *            date
	 * @return 输出当天星期几
	 * @author wenhua.li@c2bmotors.com
	 */
	
	public static String formatTodayOfWeek(Date date) {
		initCalendar(date);
		String today = null;
		int weekDay = gc.get(Calendar.DAY_OF_WEEK);
		if(weekDay == Calendar.SUNDAY ){
			today =  "星期天";
		}else if(weekDay == Calendar.MONDAY){
			today =  "星期一";
		}else if(weekDay == Calendar.TUESDAY){
			today =  "星期二";
		}else if(weekDay == Calendar.WEDNESDAY){
			today =  "星期三";
		}else if(weekDay == Calendar.THURSDAY){
			today =  "星期四";
		}else if(weekDay == Calendar.FRIDAY){
			today =  "星期五";
		}else if(weekDay == Calendar.SATURDAY){
			today =  "星期六";
		}
		return today;
	}

	/**
	 * 将短日期格式的String转化为Date类型
	 * 
	 * @param String
	 *            dateStr(yyyy-MM-dd)
	 * @return Date
	 * @throws ParseException
	 * @author MaChao
	 */
	public static Date praseString2Date(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = (Date) sdf.parse(dateStr);
		return date;
	}

	/**
	 * 将Date格式化为长型String
	 * @param date
	 * @return
	 * @author baiys
	 */
	public static String formatDate2LongString(Date date) {
		initCalendar(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(date);
		return dateStr;
	}
	
	/**
	 * 将Date格式化为长型String
	 * @param date
	 * @return
	 * @author baiys
	 */
	public static String formatDateLongString(Date date) {
		initCalendar(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	/**
	 * 将长日期格式的String转化为Date类型
	 * 
	 * @param String
	 *            dateStr(yyyy-MM-dd HH:mm:ss)
	 * @return Date
	 * @throws ParseException
	 * @author MaChao
	 */
	public static Date praseString2LongDate(String dateStr)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = (Date) sdf.parse(dateStr);
		return date;
	}
	
	
	/**
	 * 将长日期格式的String转化为Date类型
	 * 
	 * @param String
	 *            dateStr(yyyy-MM-dd HH:mm:ss)
	 * @return Date
	 * @throws ParseException
	 * @author MaChao
	 */
	public static Date praseStringToLongDate(String dateStr)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = (Date) sdf.parse(dateStr);
		return date;
	}

	/**
	 * 取得日期所在周的第一天
	 * 
	 * @param Date
	 *            date
	 * @return Date
	 */
	public static Date getFirstWeekDay(Date date) {
		initCalendar(date);
		gc.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return gc.getTime();
	}

	/**
	 * 取得日期所在周的最后一天
	 * 
	 * @param Date
	 *            date
	 * @return Date
	 */
	public static Date getLastWeekDay(Date date) {
		initCalendar(date);
		gc.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return gc.getTime();
	}

	/**
	 * 取得日期所在月的第一天
	 * 
	 * @param Date
	 *            date
	 * @return Date
	 */
	public static Date getFirstMonthDay(Date date) {
		initCalendar(date);
		int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
		gc.add(Calendar.DAY_OF_MONTH, 1 - dayOfMonth);
		return gc.getTime();
	}

	/**
	 * 取得日期所在月的最后一天
	 * 
	 * @param Date
	 *            date
	 * @return Date
	 */
	public static Date getLastMonthDay(Date date) {
		initCalendar(date);
		int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
		int maxDaysOfMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
		gc.add(Calendar.DAY_OF_MONTH, maxDaysOfMonth - dayOfMonth);
		return gc.getTime();
	}

	/**
	 * 取得日期所在旬的最后一天
	 * 
	 * @param Date
	 *            date
	 * @return Date
	 */
	public static Date getFirstTenDaysDay(Date date) {
		initCalendar(date);
		int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth <= 10) {
			gc.set(Calendar.DAY_OF_MONTH, 1);
		} else if (dayOfMonth > 20) {
			gc.set(Calendar.DAY_OF_MONTH, 21);
		} else {
			gc.set(Calendar.DAY_OF_MONTH, 11);
		}
		return gc.getTime();
	}

	/**
	 * 取得日期所在旬的最后一天
	 * 
	 * @param Date
	 *            date
	 * @return Date
	 */
	public static Date getLastTenDaysDay(Date date) {
		initCalendar(date);
		int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth <= 10) {
			gc.set(Calendar.DAY_OF_MONTH, 10);
		} else if (dayOfMonth > 20) {
			gc.set(Calendar.DAY_OF_MONTH, gc
					.getActualMaximum(Calendar.DAY_OF_MONTH));
		} else {
			gc.set(Calendar.DAY_OF_MONTH, 19);
		}
		return gc.getTime();
	}

	public static void main(String[] args) {
		Date date;
		try {
			date = praseString2Date("2009-10-31");
			System.out.println("Today = " + formatDate2ShortString(date));
			System.out.println("Yesterday = "
					+ formatDate2ShortString(getLastDay(date)));
			System.out.println("Tomorrow = "
					+ formatDate2ShortString(getNextDay(date)));
			System.out.println("getFirstWeekDay = " + getFirstWeekDay(date));
			System.out.println("getLastWeekDay = " + getLastWeekDay(date));
			System.out.println("getFirstMonthDay = " + getFirstMonthDay(date));
			System.out.println("getLastMonthDay = " + getLastMonthDay(date));
			System.out.println("getFirstTenDaysDay = "
					+ getFirstTenDaysDay(date));
			System.out
					.println("getLastTenDaysDay = " + getLastTenDaysDay(date));

			System.out.println("getLastMonthDay = "
					+ getLastMonthDay("2008-2-2"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
}
