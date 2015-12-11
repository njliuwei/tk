package com.fhsoft.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


public class DateUtil {

	private static Logger logger = Logger.getLogger(DateUtil.class);

	public static final String DATE_TIME_FORMAT_4SS = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_TIME_FORMAT_4MM = "yyyy-MM-dd HH:mm";

	public static final String DATE_FORMAT_4DD = "yyyy-MM-dd";

	public static final String DATE_FORMAT_YYYYMMDD = "yyyy/MM/dd";

	public static final String DATE_TIME_FORMAT_ALL = "yyyyMMddHHmmss";

	public static final String DATE_TIME_FORMAT_YMD = "yyyyMM";

	public static final String DATE_FORMAT_2DD = "yyMMdd";

	public static final String DATE_FORMAT_4YMD = "yyyyMMdd";

	public static final String DATE_FORMAT_HH24 = "yyMMddHH";

	public static final String DATE_FORMAT_DDHH = "yyyy-MM-dd HH:mm";

	public static final String DATE_FORMAT_HHMM = "HH:mm";

	/**
     * 
     */
	private DateUtil() {
	}

	/**
	 * 方法描述：str转Date方法
	 * 
	 * @param：
	 * @return：Date (返回日期)
	 */
	public static Date toDate(String dateStr, String format)
			throws ParseException {
		if (StringUtil.isEmpty(dateStr))
			return null;
		DateFormat df = new SimpleDateFormat(format);

		return df.parse(dateStr);

	}

	/**
	 * 获取当前时间
	 */
	public static String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(new Date());
		return dateString;
	}
	
	/**
	 * 获取当前日期
	 */
	public static String getDay() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(new Date());
		return dateString;
	}
	
	/**
	 * 获取当前时间
	 */
	public static String getCurrentDay() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(new Date());
		return dateString;
	}
	
	/**
	 * 获取当前时间
	 */
	public static String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT_ALL);
		String dateString = formatter.format(new Date());
		return dateString;
	}

	/**
	 * 方法描述：返回某月的开始日期
	 * 
	 * @param：
	 * @return：String (返回到列表页面)
	 */
	public static Date getMonBeginDate(String month) {
		Date date = null;
		Map<String, String> days = new HashMap<String, String>();
		days.put("01", "01-01 00:00:00");
		days.put("02", "02-01 00:00:00");
		days.put("03", "03-01 00:00:00");
		days.put("04", "04-01 00:00:00");
		days.put("05", "05-01 00:00:00");
		days.put("06", "06-01 00:00:00");
		days.put("07", "07-01 00:00:00");
		days.put("08", "08-01 00:00:00");
		days.put("09", "09-01 00:00:00");
		days.put("10", "10-01 00:00:00");
		days.put("11", "11-01 00:00:00");
		days.put("12", "12-01 00:00:00");
		String value = days.get(month);
		Integer year = getDByear();
		String beginDate = year + "-" + value;
		try {
			date = DateUtil.toDate(beginDate, DATE_TIME_FORMAT_4SS);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 方法描述：返回某月的结束日期
	 * 
	 * @param：
	 * @return：String (返回到列表页面)
	 */
	public static Date getMonEndDate(String month) {
		Date date = null;
		Map<String, String> days = new HashMap<String, String>();
		days.put("01", "01-31 23:59:59");
		days.put("02", "02-28 23:59:59");
		days.put("03", "03-31 23:59:59");
		days.put("04", "04-30 23:59:59");
		days.put("05", "05-31 23:59:59");
		days.put("06", "06-30 23:59:59");
		days.put("07", "07-31 23:59:59");
		days.put("08", "08-31 23:59:59");
		days.put("09", "09-30 23:59:59");
		days.put("10", "10-31 23:59:59");
		days.put("11", "11-30 23:59:59");
		days.put("12", "12-31 23:59:59");
		String value = days.get(month);
		Integer year = getDByear();
		String endDate = year + "-" + value;
		try {
			date = DateUtil.toDate(endDate, DATE_TIME_FORMAT_4SS);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 
	 * 获取当天的零点零分零秒
	 * <P>
	 * Function: getToDayBeginTime
	 * <P>
	 * Others:
	 * 
	 * @param startDate
	 * @return
	 */
	public static Date getToDayBeginTime(String startDate) {
		try {
			if (startDate == null || "".equals(startDate)) {
				return null;
			}
			Date d = new SimpleDateFormat(DATE_FORMAT_4DD).parse(startDate);
			Calendar currentDate = new GregorianCalendar();
			currentDate.setTime(d);
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			return currentDate.getTime();
		} catch (ParseException e) {
			logger.error("日期转换异常：" + e.getMessage());
			return null;
		}
	}

	/**
	 * 获取当天的23时59分59秒
	 * <P>
	 * Function: getToDayBeginTime
	 * <P>
	 * Others:
	 * 
	 * @param endDate
	 * @return
	 */
	public static Date getToDayEndTime(String endDate) {
		try {
			if (endDate == null || "".equals(endDate)) {
				return null;
			}
			Date d = new SimpleDateFormat(DATE_FORMAT_4DD).parse(endDate);
			Calendar currentDate = new GregorianCalendar();
			currentDate.setTime(d);
			currentDate.set(Calendar.HOUR_OF_DAY, 23);
			currentDate.set(Calendar.MINUTE, 59);
			currentDate.set(Calendar.SECOND, 59);
			return currentDate.getTime();
		} catch (ParseException e) {
			logger.error("日期转换异常：" + e.getMessage());
			return null;
		}
	}

	/**
	 * <P>
	 * Function: getSimpleDateFormat
	 * <P>
	 * Description:
	 * <P>
	 * Others:返回SimpleDateFormat
	 * 
	 * @param formate
	 * @return SimpleDateFormat
	 */
	protected static SimpleDateFormat getSimpleDateFormat(String formate) {
		SimpleDateFormat formatdater = new SimpleDateFormat(formate);
		return formatdater;
	}

	public static int getDByear() {

		Date currentDate = new Date();

		SimpleDateFormat formatdater = getSimpleDateFormat(DATE_FORMAT_4DD);

		String yearStrTemp = formatdater.format(currentDate);

		int dbYear = -1;

		try {
			dbYear = Integer.parseInt(yearStrTemp.substring(0, 4));
		} catch (Exception e) {
			dbYear = -1;
		}

		return dbYear;
	}

	/**
	 * 按指定的格式将日期对象转换为字符串
	 * <P>
	 * Function: toString
	 * <P>
	 * Description:
	 * <P>
	 * Others:返回日期字符串
	 * 
	 * @param date
	 * @param format
	 * @return String
	 */
	public static String toString(Date date, String format) {
		if (date == null) {
			return null;
		}
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * 将java中Date(不包含时间)转换成Timestamp
	 * 
	 * @param startDate
	 * @return
	 */
	public static Timestamp getTimestamp() {
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_4SS);// 设定格式
		String dateStr = dateFormat.format(new Date());
		dateFormat.setLenient(false);
		java.util.Date timeDate = null;
		try {
			timeDate = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// util类型
		java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());// Timestamp类型,timeDate.getTime()返回一个long型
		return dateTime;
	}

}
