package com.surekam.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 日期Util类
 * 
 * @author tangjun
 */
public class DateUtil {
	public static String defaultDatePattern = "yyyyMMdd";
	public static String defaultDateTimePattern = "yyyyMMddHHmmss";
	public static String defaultTimePattern = "HHmmss";

	/**
	 * 获得默认的 date pattern
	 */
	private static String getDatePattern() {
		return defaultDatePattern;
	}

	/**
	 * 返回 当前日期字符串
	 * 
	 * @return yyyyMMdd
	 */
	public static String getToday() {
		Date today = new Date();
		return format(today);
	}

	/**
	 * 返回 当前日期前一天字符串
	 * 
	 * @return yyyyMMdd
	 */
	public static String getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = format(cal.getTime());
		return yesterday;
	}

	/**
	 * 获取前天
	 * 
	 * @return yyyyMMdd
	 */
	public static String getBeforeYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		String bfyesterday = format(cal.getTime());
		return bfyesterday;
	}

	/**
	 * 使用预设Format格式化Date成字符串
	 */
	public static String format(Date date) {
		return date == null ? "" : format(date, getDatePattern());
	}

	/**
	 * 使用参数Format格式化Date成字符串
	 */
	public static String format(Date date, String pattern) {
		return date == null ? "" : new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 使用预设格式将字符串转为Date
	 */
	public static Date parse(String strDate) throws ParseException {
		return StringUtils.isBlank(strDate) ? null : parse(strDate, getDatePattern());
	}

	/**
	 * 使用参数Format将字符串转为Date
	 */
	public static Date parse(String strDate, String pattern) throws ParseException {
		return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(pattern).parse(strDate);
	}

	/**
	 * 在日期上增加数个整月
	 */
	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}

	/**
	 * 获得月末日 flag为下n个月的月末日 下个月则为1
	 * 
	 * @param year
	 * @param month
	 * @param flag
	 * @return
	 */
	public static Date getLastDayOfMonth(String year, String month, int flag) {

		Calendar cal = Calendar.getInstance();
		// 年
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		// 月，因为Calendar里的月是从0开始，所以要-1
		cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		// 日，设为一号
		cal.set(Calendar.DATE, 1);
		// 月份加一，得到下个月的一号
		cal.add(Calendar.MONTH, flag);
		// 下一个月减一为本月最后一天
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * 获得月末日: flag=1:获得上月月末日 flag=0：获得本月月末日
	 */
	public static Date getLastDayOfMonth(Date date, int flag) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 日，设为一号
		cal.set(Calendar.DATE, 1);
		// 月份加一，得到下个月的一号
		cal.add(Calendar.MONTH, flag);
		// 下一个月减一为本月最后一天
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * 获取本月第一天
	 * 
	 * @return yyyyMMdd
	 */
	public static String getMonthFirstDay() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar cale = Calendar.getInstance();
		cale.set(Calendar.DAY_OF_MONTH, 1);
		return format.format(cale.getTime());
	}

	/**
	 * 获得周末日
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * 在日期上增加数天
	 * 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addDay(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, n);
		return cal.getTime();
	}

	/**
	 * 在日期上增加数分
	 * 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addMin(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, n);
		return cal.getTime();
	}

	/**
	 * 得到当前时间
	 * 
	 * @return HHmmss
	 */
	public static String GetCurrentTime() {
		StringBuffer result = new StringBuffer();
		Calendar calendar = Calendar.getInstance();
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int m = calendar.get(Calendar.MINUTE);
		int s = calendar.get(Calendar.SECOND);
		if (h < 10) {
			result.append("0");
		}
		result.append(h);
		if (m < 10) {
			result.append("0");
		}
		result.append(m);
		if (s < 10) {
			result.append("0");
		}
		result.append(s);
		return result.toString();
	}

	/**
	 * 得到当前日期时间
	 * 
	 * @return yyyyMMddHHmmss
	 */
	public static String getCurrDateTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}

	/**
	 * Formats a Date object to return a date using the global locale.
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
		return outFormat.format(date);
	}

	/**
	 * Formats a Date object to return a date and time using the global locale.
	 */
	public static String formatDateTime(Date date) {
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return outFormat.format(date);
	}

	public static String formatDate2(Date myDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String strDate = formatter.format(myDate);
		return strDate;
	}

	public static String formatDate3(Date myDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
		String strDate = formatter.format(myDate);
		return strDate;
	}

	public static String formatDate4(Date myDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String strDate = formatter.format(myDate);
		return strDate;
	}

	public static String formatDate5(Date myDate) {
		String strDate = getYear(myDate) + "-" + getMonth(myDate) + "-" + getDay(myDate);
		return strDate;
	}

	public static String formatDate6(Date myDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String strDate = formatter.format(myDate);
		return strDate;
	}

	public static long Date2Long(int year, int month, int date) {
		Calendar cld = Calendar.getInstance();
		month = month - 1;
		cld.set(year, month, date);
		return cld.getTime().getTime();
	}

	public static long Time2Long(int year, int month, int date, int hour, int minute, int second) {
		Calendar cld = Calendar.getInstance();
		month = month - 1;
		cld.set(year, month, date, hour, minute, second);
		return cld.getTime().getTime();
	}

	public static int getYear(long t) {
		Calendar cld = Calendar.getInstance();
		if (t > 0) {
			cld.setTime(new java.util.Date(t));
		}
		return cld.get(Calendar.YEAR);
	}

	public static int getMonth(long t) {
		Calendar cld = Calendar.getInstance();
		if (t > 0) {
			cld.setTime(new java.util.Date(t));
		}
		return cld.get(Calendar.MONTH) + 1;
	}

	public static int getDay(long t) {
		Calendar cld = Calendar.getInstance();
		if (t > 0) {
			cld.setTime(new java.util.Date(t));
		}
		return cld.get(Calendar.DAY_OF_MONTH);
	}

	public static int getHour(long t) {
		Calendar cld = Calendar.getInstance();
		if (t > 0) {
			cld.setTime(new java.util.Date(t));
		}
		return cld.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute(long t) {
		Calendar cld = Calendar.getInstance();
		if (t > 0) {
			cld.setTime(new java.util.Date(t));
		}
		return cld.get(Calendar.MINUTE);
	}

	public static int getSecond(long t) {
		Calendar cld = Calendar.getInstance();
		if (t > 0) {
			cld.setTime(new java.util.Date(t));
		}
		return cld.get(Calendar.SECOND);
	}

	public static int getYear(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.YEAR);
	}

	public static int getMonth(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.MONTH) + 1;
	}

	public static int getDay(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取date的日期,日期格式dd
	 * 
	 * @param date
	 * @return
	 */
	public static String getFormatDay(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + String.valueOf(cld.get(Calendar.DAY_OF_MONTH))
				: String.valueOf(cld.get(Calendar.DAY_OF_MONTH));
	}

	public static int getHour(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取date的小时,小时格式hh
	 * 
	 * @param date
	 * @return
	 */
	public static String getFormatHour(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + String.valueOf(cld.get(Calendar.HOUR_OF_DAY))
				: String.valueOf(cld.get(Calendar.HOUR_OF_DAY));
	}

	public static int getMinute(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.MINUTE);
	}

	/**
	 * 获取date的分钟,分钟格式mm
	 * 
	 * @param date
	 * @return
	 */
	public static String getFormatMinute(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.MINUTE) < 10 ? "0" + String.valueOf(cld.get(Calendar.MINUTE))
				: String.valueOf(cld.get(Calendar.MINUTE));
	}

	public static int getSecond(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.SECOND);
	}

	public static int getYear() {
		Calendar cld = Calendar.getInstance();
		cld.setTime(new java.util.Date());
		return cld.get(Calendar.YEAR);
	}

	public static int getMonth() {
		Calendar cld = Calendar.getInstance();
		cld.setTime(new java.util.Date());
		return cld.get(Calendar.MONTH) + 1;
	}

	public static int getDay() {
		Calendar cld = Calendar.getInstance();
		cld.setTime(new java.util.Date());
		return cld.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 把日期转成2006/12/30格式
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(String date) {
		if (date != null && date.trim().length() >= 8) {
			return date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8);
		}
		return date;
	}

	public static String formatTime(String time) {
		if (time != null && time.trim().length() >= 6) {
			return time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6);
		}
		return time;
	}

	public static String formateTimeOfDay(String time) {
		if (time != null && time.trim().length() >= 12) {
			return time.substring(0, 4) + "/" + time.substring(4, 6) + "/" + time.substring(6, 8) + " "
					+ time.substring(8, 10) + "：" + time.substring(10, 12);
		}
		return time;
	}

	/**
	 * 获得两天之间相差天数 date-date1
	 * 
	 * @param date
	 * @param date1
	 * @return
	 */
	public static int getDiffDate(java.util.Date date, java.util.Date date1) {
		return (int) ((date.getTime() - date1.getTime()) / (24 * 3600 * 1000));
	}

	/**
	 * 获取两个时间的秒差 date-date1
	 * 
	 * @param date
	 * @param date1
	 * @return
	 */
	public static long getDiffTime(java.util.Date date, java.util.Date date1) {
		return (date.getTime() - date1.getTime()) / 1000;
	}

	/**
	 * 从 yyyyMMddHHmmss格式的时间中 获取日期 yyyyMMdd
	 * 
	 * @param time
	 * @return yyyyMMdd
	 */
	public static String getDateFromyyyyMMddHHmmss(String time) {
		return time.substring(0, 8);
	}

	/**
	 * 从 yyyyMMddHHmmss格式的时间中 获取时间 HHmmss
	 * 
	 * @param time
	 * @return HHmmss
	 */
	public static String getTimeFromyyyyMMddHHmmss(String time) {
		return time.substring(8, 14);
	}

	/**
	 * 验证日期时间,短时间，形如 YYYYMMDD(20110101)
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合网址格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isDateYYYYMMDD(String validateString) {
		String regex = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229))$";
		return patternValidator(regex, validateString);
	}

	/**
	 * 正则表达式验证数据
	 * 
	 * @param regexString
	 *            正则字符串
	 * @param validateString
	 *            验证字符串
	 * @return 如果validateString 符合 regex的正则表达式格式,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean patternValidator(String regex, String validateString) {
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(validateString);
		return m.matches();
	}

	/**
	 * 获取当前时间是本月第几天
	 * 
	 * @return
	 */
	public static int getMonthDay() {
		Calendar calendar = Calendar.getInstance();
		int DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);
		return DAY_OF_MONTH;
	}

	/**
	 * 财务月，获取上个月第22天
	 * 
	 * @return
	 */
	public static String getLastMonthTTwoDay() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 22);
		String firstDay = format.format(cal.getTime());
		return firstDay;
	}

	/**
	 * 财务月，获取本月第21天
	 * 
	 * @return
	 */
	public static String getThisMonthTOneDay() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar cale = Calendar.getInstance();
		cale.set(Calendar.DAY_OF_MONTH, 21);
		String lastDay = format.format(cale.getTime());
		return lastDay;
	}

	/**
	 * 获取昨天
	 * 
	 * @return
	 */
	public static String getLastDay() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar cale = Calendar.getInstance();
		cale.add(Calendar.DAY_OF_MONTH, -1);
		String lastDay = format.format(cale.getTime());
		return lastDay;
	}

	/**
	 * 获取昨天
	 * 
	 * @return
	 */
	public static String getLastDay1() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cale = Calendar.getInstance();
		cale.add(Calendar.DAY_OF_MONTH, -1);
		String lastDay = format.format(cale.getTime());
		return lastDay;
	}

	/**
	 * 获取明天
	 * 
	 * @return
	 */
	public static String getNextDay() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar cale = Calendar.getInstance();
		cale.add(Calendar.DAY_OF_MONTH, +1);
		String lastDay = format.format(cale.getTime());
		return lastDay;
	}

	/**
	 * 把接收到的日期转换为月日
	 * 
	 * @return MM-dd
	 * @throws ParseException
	 */
	public static String formatMmDd(String date) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format2 = new SimpleDateFormat("MM-dd");
		Date dt = format1.parse(date);
		return format2.format(dt);
	}

	/**
	 * 格式化数据库样式为web样式
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static String formatWebDtTm(String dateTime) throws ParseException {
		return format(parse(dateTime, "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm:ss");

	}
	
	
	/**
	 * 格式化数据库样式为web样式
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static String formatYYYYMMdd(Date dateTime) throws ParseException {
		return format(dateTime, "yyyyMMdd");
	}

	/**
	 * 格式化数据库样式为web样式
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static String formatDtTm(String dateTime) throws ParseException {
		return format(parse(dateTime, "yyyy-MM-dd HH:mm:ss"), "yyyyMMddHHmmss");
	}

	/**
	 * 把接收到的日期yyyyMMdd转换为yyyy-MM-dd
	 * 
	 * @return yyyy-MM-dd
	 * @throws ParseException
	 */
	public static String formatYyMmDd(String date) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = format1.parse(date);
		return format2.format(dt);
	}

	/**
	 * 格式化数据库样式为web样式
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static String formatWebDt(String dateTime) throws ParseException {

		return format(parse(dateTime, "yyyyMMdd"), "yyyy-MM-dd");
	}

	/**
	 * 格式化数据库样式为web样式
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static String formatSmsDt(String dateTime) {

		try {
			return format(parse(dateTime, "yyyyMMdd"), "yyyy年MM月dd日");
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String formatSmsDt(Date date) {
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy年MM月dd日");
		return outFormat.format(date);
	}
	

	/**
	 * 格式化数据库样式为web样式
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static String formatWebTm(String dateTime) throws ParseException {

		return format(parse(dateTime, "HHmmss"), "HH:mm:ss");
	}

	/**
	 * 得到当前日期时间
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrDateTime2() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = outFormat.format(now);
		return s;
	}

	/**
	 * 把接收到的日期yyyy-MM-dd转换为yyyyMMdd
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static String formatToY_M_D(String dateTime) throws ParseException {

		return format(parse(dateTime, "yyyy-MM-dd"), "yyyyMMdd");
	}

	/**
	 * 获得两天之间相差天数 date-date1
	 * 
	 * @param date
	 * @param date1
	 * @return
	 */
	public static Integer getDiffDate(String date, String date1) {
		try {
			Date dateCopy = parse(date, "yyyyMMdd");
			Date dateCopy1 = parse(date1, "yyyyMMdd");
			return (int) ((dateCopy.getTime() - dateCopy1.getTime()) / (24 * 3600 * 1000));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到当前日期时间
	 * 
	 * @return yyyy年MM月dd日
	 */
	public static String getCurrDate() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String s = outFormat.format(now);
		return s;
	}

	/**
	 * 得到当前日期时间
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getCurrDate2() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
		String s = outFormat.format(now);
		return s;
	}

	public static String formatYYMMDd(String date) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = format1.parse(date);
		return format2.format(dt);
	}

	/**
	 * 获取过去七天
	 */

	public static String getSevenDate() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		// 过去七天
		c.setTime(new Date());
		c.add(Calendar.DATE, -7);
		Date d = c.getTime();
		String day = format.format(d);
		return day;
	}

	/**
	 * 获取两个日期的List
	 * 
	 * @auth huhy 2017-7-28
	 * @param start
	 *            起始日期
	 * @param end
	 *            结束日期
	 * @return
	 */
	public static List<String> getDate(String start, String end) { //
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> list = new ArrayList<String>(); // 保存日期集合
		Date date_start = null;
		Date date_end = null;
		try {
			date_start = sdf.parse(DateUtil.formatYyMmDd(start));
			date_end = sdf.parse(DateUtil.formatYyMmDd(end));
			Date date = date_start;
			Calendar cd = Calendar.getInstance();// 用Calendar 进行日期比较判断
			while (date.getTime() <= date_end.getTime()) {
				list.add(sdf.format(date));
				cd.setTime(date);
				cd.add(Calendar.DATE, 1);// 增加一天 放入集合
				date = cd.getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		list.remove(list.size() - 1);
		return list;
	}

	/**
	 * 判断日期是否为当月最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
		if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Title: advanceDays Description: 前些日子
	 * 
	 * @param date
	 *            时间
	 * @param day
	 *            天数
	 * @return
	 */
	public static Date advanceDays(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

}