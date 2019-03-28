package com.wonhigh.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private static final Logger LOGGER = Logger.getLogger(DateUtil.class);
	public static final String DATE_DEFAULT_FORMAT = "yyyy-MM-dd";
	public static final String TIME_DEFAULT_FORMAT = "HH:mm:ss";
	public static final String DATETIME_DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static DateFormat dateFormat = null;
	private static DateFormat dateTimeFormat = null;
	private static DateFormat timeFormat = null;
	
	/**
	 * 私有构造器
	 */
	private DateUtil() {}
	
	static {
		dateFormat = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
		dateTimeFormat = new SimpleDateFormat(DATETIME_DEFAULT_FORMAT);
		timeFormat = new SimpleDateFormat(TIME_DEFAULT_FORMAT);
	}

	/**
	 * 日期格式化yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static Date formatDate(String date, String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			LOGGER.error("formatDate error",e);
		}
		return null;
	}

	/**
	 * 日期格式化yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateFormat(Date date) {
		return dateFormat.format(date);
	}

	/**
	 * 日期格式化yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateTimeFormat(Date date) {
		return dateTimeFormat.format(date);
	}

	/**
	 * 时间格式化
	 * 
	 * @param date
	 * @return HH:mm:ss
	 */
	public static String getTimeFormat(Date date) {
		return timeFormat.format(date);
	}

	/**
	 * 日期格式化
	 * 
	 * @param date
	 * @param 格式类型
	 * @return
	 */
	public static String getDateFormat(Date date, String formatStr) {
		if (StringUtils.isNotBlank(formatStr) && date != null) {
			return new SimpleDateFormat(formatStr).format(date);
		}
		return null;
	}

	/**
	 * 日期格式化
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateFormat(String date) {
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			LOGGER.error("getDateFormat error",e);
		}
		return null;
	}

	/**
	 * 时间格式化
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateTimeFormat(String date) {
		try {
			return dateTimeFormat.parse(date);
		} catch (ParseException e) {
			LOGGER.error("getDateTimeFormat error"+e);
		}
		return null;
	}

	/**
	 * 获取当前日期(yyyy-MM-dd)
	 * 
	 * @param date
	 * @return
	 */
	public static Date getNowDate() {
		return DateUtil.getDateFormat(dateFormat.format(new Date()));
	}

}
