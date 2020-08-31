/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 * @author sureserve
 * @version 2013-3-15
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

	private static String[] parsePatterns = { "yyyy-MM-dd","yyyyMMdd",
			"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd",
			"yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyyMMddHHmmss",
			"yyyy年MM月dd日"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyyMMdd）
	 */
	public static String getDateYYYYMMDD() {
		return getDate("yyyyMMdd");
	}
	

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern,Locale.CHINA);
	}
	
	/**
	 * 把传入的日期字符串格式化后输出 pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 * @throws ParseException 
	 */
	public static String getDate(String dateStr,String pattern) throws ParseException {
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd",Locale.CHINA);
		Date date = sdf.parse(dateStr);
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	public static Date getDateStart(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getDateEnd(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	// 获取前天所在的星期
	public static List currentWeek() {
		List dateList = new ArrayList();
		Calendar cal = Calendar.getInstance();
		int date = cal.get(Calendar.DAY_OF_MONTH);
		int n = cal.get(Calendar.DAY_OF_WEEK);
		if (n == 1) {
			n = 7;
		} else {
			n = n - 1;
		}
		System.out.println("当天为本周第" + n + "天");
		// 日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		for (int i = 1; i <= 7; i++) {
			cal.set(Calendar.DAY_OF_MONTH, date + i - n);
			dateList.add(sdf.format(cal.getTime()));
			//System.out.println(sdf.format(cal.getTime()));
		}
		return dateList;
	}

	// 计算指定日期所在的星期
	public static List currentWeek(String str) throws ParseException {
		List dateList = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		Date date1 = sdf.parse(str);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		
		for (int i = 1; i <= 7; i++) {
			cal.set(Calendar.DAY_OF_WEEK, i);
			dateList.add(sdf.format(cal.getTime()));
			//System.out.println(sdf.format(cal.getTime()));
		}
		
		return dateList;
	}

	// 计算指定日期所在的星期,按指定模式输出周列表
	public static List currentWeek(String str, String pattern)
			throws ParseException {
		List dateList = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern,Locale.CHINA);
		Date date1 = sdf.parse(str);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		
		for (int i = 1; i <= 7; i++) {
			cal.set(Calendar.DAY_OF_WEEK, i);
			dateList.add(sdf.format(cal.getTime()));
			//System.out.println(sdf.format(cal.getTime()));
		}
		
		return dateList;
	}
	
	/**
	 * 计算指定日期所在的星期,按指定模式输出周列表
	 * @param str  时间字符串
	 * @param inPattern  输入时间格式
	 * @param outPattern 输出时间格式
	 * @return
	 * @throws ParseException
	 */
	public static List currentWeek(String str, String inPattern,String outPattern)
			throws ParseException {
		List dateList = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat(inPattern,Locale.CHINA);
		Date date1 = sdf.parse(str);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		SimpleDateFormat sdf1 = new SimpleDateFormat(outPattern,Locale.CHINA);
		for (int i = 1; i <= 7; i++) {
			cal.set(Calendar.DAY_OF_WEEK, i);
			dateList.add(sdf1.format(cal.getTime()));
			//System.out.println(sdf1.format(cal.getTime()));
		}
		
		return dateList;
	}

	/**
	 * 计算给定日期的上一周，下一周日期列表
	 * @param str  给定的日期
	 * @param inPattern  给定的日期格式
	 * @param outPattern 输入日期格式
	 * @param week  -1上一周，1下一周
	 * @return
	 * @throws ParseException
	 */
	public static List getWeekdays(String str,String inPattern,String outPattern,int week) throws ParseException {
		List dateList = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat(inPattern);
		Date date1 = sdf.parse(str);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		cal.add(Calendar.WEEK_OF_MONTH,week);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		SimpleDateFormat sdf1 = new SimpleDateFormat(outPattern);
		for (int i = 1; i <= 7; i++) {
			cal.set(Calendar.DAY_OF_WEEK, i);
			dateList.add(sdf1.format(cal.getTime()));
			//System.out.println(sdf1.format(cal.getTime()));
		}
		return dateList;
	}
	
	/**
	 * 计算时长
	 *
	 * @param beginTime 开始时间
	 * * @param endTime 结束时间
	 * @return
	 * @throws ParseException 
	 */
	public static String calcDuration(String beginTime, String endTime) throws ParseException 
	{
		//通过字符串创建两个日期对象
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date firstDate = format.parse(beginTime);
		Date secondDate = format.parse(endTime);
		
		//得到两个日期对象的总毫秒数
		long firstDateMilliSeconds = firstDate.getTime();
		long secondDateMilliSeconds = secondDate.getTime();
		
		//得到两者之差
		long firstMinusSecond = secondDateMilliSeconds - firstDateMilliSeconds;
		
		//毫秒转为秒
		long milliSeconds = firstMinusSecond;
		long totalSeconds = (int)(milliSeconds / 1000);

		//得到总天数
		long days = totalSeconds / (3600*24);
		long days_remains = totalSeconds % (3600*24);
		
		//得到总小时数
		long hours = days_remains / 3600;
		long remains_hours = days_remains % 3600;
		
		//得到分种数
		long minutes = remains_hours / 60;
		
		//得到总秒数
		long seconds = remains_hours % 60;
		
		return days+"天"+hours+"小时"+minutes+"分"+seconds+"秒";
	}
	
	/**
	 * 计算两个日期相差时长  后面日期比前面日期大
	 *
	 * @return
	 * @throws ParseException 
	 */
	public static long calcDays(Date firstDate, Date secondDate) throws ParseException 
	{
		//得到两个日期对象的总毫秒数
		long firstDateMilliSeconds = firstDate.getTime();
		long secondDateMilliSeconds = secondDate.getTime();
		
		//得到两者之差
		long firstMinusSecond = secondDateMilliSeconds - firstDateMilliSeconds;
		
		//毫秒转为秒
		long milliSeconds = firstMinusSecond;
		long totalSeconds = (int)(milliSeconds / 1000);

		//得到总天数
		long days = totalSeconds / (3600*24);
		
		return days;
	}
	
	/**
	 * @description 将日期字符串"yyyyMMdd"转换成"yyyy-MM-dd"
	 * @param dateStr
	 * @return String "yyyy-MM-dd"
	 */
	public static String formatDateStr(String dateStr){
		if(dateStr == null || StringUtils.isEmpty(dateStr)){
			return null;
		}
		return dateStr.substring(0,4) + "-" + dateStr.substring(4,6)
				+ "-" + dateStr.substring(6,8);
	}
	
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		// System.out.println(formatDate(parseDate("2010/3/6")));
		// System.out.println(getDate("E MM-dd"));
		// long time = new Date().getTime()-parseDate("2012-11-19").getTime();
		// System.out.println(time/(24*60*60*1000));
		// currentWeek("2016-5-27");
//		currentWeek("20160527","yyyyMMdd","E MM-dd");
//		System.out.println(formatDateStr("20160708"));
		System.out.println(getBeginDayOfWeek());
		System.out.println(getBeginDayOfMonth());
	}
	
	/**
	 * @格式化日期，返回 yyyy.mm.dd格式字符串；
	 * @param date
	 * @return 20170901
	 */
	public static String getfmDate(String date){
		if(StringUtils.isNotBlank(date) && date.length() > 0){
			return date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
		}
		return date;
	}
	
	/**
	 * 数据库日期转 yyyy-MM-dd
	 * @param type
	 * @return
	 */
	public static String getDateString(String date){
		try {
			if(StringUtils.isNotEmpty(date) && 8 == date.length()){
				return date.substring(0, 4) + "-" + date.substring(4, 6)+ "-" + date.substring(6, 8);
			}
			if(StringUtils.isNotEmpty(date) && 6 == date.length()){
				return date.substring(0, 4) + "-" + date.substring(4, 6);
			}
		} catch (Exception e) {
		}
		return date;

	}
	
	/**
	 * 数据库时间转 hh:mm:ss
	 * @param type
	 * @return
	 */
	public static String getTimeString(String time){
		try {
			if(StringUtils.isNotEmpty(time) &&  time.length()>=4){
				return time.substring(0, 2) + ":" + time.substring(2, 4);
			}
		} catch (Exception e) {
		}
		return time;
	}
	
	 /**
     * 判断time是否在now的n小时之内
     * @param time
     * @param now
     * @param n    正数表示在条件时间n小时之内，负数表示在条件时间n小时之后
     * @return
     */
    public static boolean belongDate(Date time, Date now, int n) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(time);//把当前时间赋给日历
        calendar.add(Calendar.HOUR_OF_DAY, n);
        Date before7days = calendar.getTime();   //得到n前的时间
        if (before7days.getTime() > now.getTime()) {
            return true;
        } else {
            return false;
        }
    }

	//获取本周的开始时间
    public static String getBeginDayOfWeek() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek);
		return DateFormatUtils.format(cal.getTime(), "yyyy-MM-dd",Locale.CHINA);
	}
    
    //获取本月的开始时间
  	public static String getBeginDayOfMonth() {
  		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH,1);
		return DateFormatUtils.format(cal.getTime(), "yyyy-MM-dd",Locale.CHINA);
  	}

}


