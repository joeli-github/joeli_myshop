package com.mmall.util;
/**
 * 时间转换器
 * @author 94420
 *
 */

import java.util.Date;


import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeUtil {
	//joda-time
	public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
	//str-->date
	public static Date strToDate(String dateTimeStr,String formatStr) {
		DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(formatStr);
		DateTime dateTime = dateTimeFormat.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}
	
	//date-->str
	public static String dateToStr(Date date,String formatStr) {
		if (date==null) {
			return StringUtils.EMPTY;
		}
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(formatStr);
	}
	
	//str-->date
	public static Date strToDate(String dateTimeStr) {
		DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(STANDARD_FORMAT);
		DateTime dateTime = dateTimeFormat.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}
	
	//date-->str
	public static String dateToStr(Date date) {
		if (date==null) {
			return StringUtils.EMPTY;
		}
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(STANDARD_FORMAT);
	}
	
	
	
	
	
	
	
}




