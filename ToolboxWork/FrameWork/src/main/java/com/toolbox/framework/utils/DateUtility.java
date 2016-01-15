/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:DateUtility.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 13, 2012 10:27:38 AM
 * 
 */
package com.toolbox.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * 
 * @version $Revision: 1.1 $ $Date: 2012/03/13 02:27:03 $
 * @since Mar 13, 2012
 * 
 */
public class DateUtility extends DateUtils {
    private static final String   DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final TimeZone timeZone             = TimeZone.getTimeZone("GMT+8");

    public static String format(Date date) {
        return format(date, DEFAULT_DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        return date == null ? null : DateFormatUtils.format(date, pattern);
    }

    public static String format(Calendar calendar) {
        return format(calendar, DEFAULT_DATE_PATTERN);
    }

    public static String format(Calendar calendar, String pattern) {
        return calendar == null ? null : DateFormatUtils.format(calendar, pattern);
    }

    public static String format(Calendar calendar, String pattern, TimeZone timeZone) {
        return DateFormatUtils.format(calendar, pattern, timeZone);
    }

    public static String format(Date date, String pattern, TimeZone timeZone) {
        return DateFormatUtils.format(date, pattern, timeZone);
    }

    public static String format4UnixTime(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date * 1000L);
        return format(calendar, DEFAULT_DATE_PATTERN);
    }

    public static String format(int date, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date * 1000L);
        return format(calendar, pattern);
    }

    public static Date parseDate(String str, Locale locale, String parsePattern) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(parsePattern, locale);
        try {
            return format.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDate(int curr) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(curr * 1000L);
        return c.getTime();
    }

    public static Date parseDate(String str, String parsePatter) {
        SimpleDateFormat format = new SimpleDateFormat(parsePatter);
        try {
            return format.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static int currentUnixTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static int parseUnixTime(String dateStr, String parsePatter) {
        Date date = parseDate(dateStr, parsePatter);
        return (int) (date.getTime() / 1000);
    }

    public static int parseUnixTime(String dateStr) {
        return parseUnixTime(dateStr, DEFAULT_DATE_PATTERN);
    }

    public static Integer parseUnixTime(Date date) {
        if (date == null) {
            return null;
        }
        return (int) (date.getTime() / 1000);
    }

    public static String getCron(String date) {
        Date d = parseDate(date, DEFAULT_DATE_PATTERN);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.SECOND) + " " + c.get(Calendar.MINUTE) + " " + c.get(Calendar.HOUR_OF_DAY) + " " + c.get(Calendar.DAY_OF_MONTH) + " " + (c.get(Calendar.MONTH) + 1) + " ? " + c.get(Calendar.YEAR);
    }

    public static int differDays(Date start, Date end) {
        long differ = end.getTime() - start.getTime();

        return (int) (differ / 1000 / 60 / 60 / 24);
    }

}
