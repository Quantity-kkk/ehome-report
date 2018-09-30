package com.pro.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class DateUtil
{
  public static final SimpleDateFormat dfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  public static final SimpleDateFormat nodfTime = new SimpleDateFormat("yyyyMMddHHmmssSSS");
  public static final SimpleDateFormat dfYear = new SimpleDateFormat("yyyy-MM-dd");
  public static final SimpleDateFormat nodfYear = new SimpleDateFormat("yyyyMMdd");

  public static String getDateTime() {
    Date date = new Date();
    return dfTime.format(date);
  }

  public static String addByDay(String datestr, int days) {
    String dateStrs = datestr.substring(0, 8);
    String timeStrs = datestr.substring(8, datestr.length());
    Calendar cal = parseCalendar(dateStrs);
    cal.add(5, days);
    Date date = cal.getTime();
    return nodfYear.format(date) + timeStrs;
  }

  public static String getDateTimeStr(Date date, SimpleDateFormat nodfTime) {
    return nodfTime.format(date);
  }

  public static String getDataByStr(String time, SimpleDateFormat f1, SimpleDateFormat f2)
  {
    try
    {
      return f2.format(f1.parse(time)); } catch (ParseException e) {
    }
    return "";
  }

  public static String getDateTimeNoFormat()
  {
    Date date = new Date();
    return nodfTime.format(date);
  }

  public static String getDateYear() {
    Date date = new Date();
    return dfYear.format(date);
  }

  public static boolean isBetween(String time1, String time2)
  {
    try
    {
      Date dt1 = nodfTime.parse(time1);
      Date dt2 = nodfTime.parse(time2);
      Date date = new Date();
      if ((dt1.getTime() < date.getTime()) && (dt2.getTime() > date.getTime()))
        return true;
    }
    catch (Exception localException) {
    }
    return false;
  }

  public static boolean isTimeBefore(String time1, String time2)
  {
    try
    {
      Date dt1 = nodfTime.parse(time1);
      Date dt2 = nodfTime.parse(time2);
      if (dt1.getTime() > dt2.getTime())
        return true;
    }
    catch (ParseException localParseException) {
    }
    return false;
  }

  public static boolean isTimeAfter(String time1, String time2)
  {
    try
    {
      Date dt1 = nodfTime.parse(time1);
      Date dt2 = nodfTime.parse(time2);
      if (dt1.getTime() < dt2.getTime())
        return true;
    }
    catch (ParseException localParseException) {
    }
    return false;
  }

  public static boolean isBefore(String strdate1, String strdate2)
  {
    Calendar cal1 = parseCalendar(strdate1);
    Calendar cal2 = parseCalendar(strdate2);
    return cal1.before(cal2);
  }

  public static boolean isAfter(String strdate1, String strdate2)
  {
    Calendar cal1 = parseCalendar(strdate1);
    Calendar cal2 = parseCalendar(strdate2);
    return cal1.after(cal2);
  }

  public static Calendar parseCalendar(String strdate)
  {
    if (isDateStr(strdate)) {
      int year = Integer.parseInt(strdate.substring(0, 4));
      int month = Integer.parseInt(strdate.substring(4, 6)) - 1;
      int day = Integer.parseInt(strdate.substring(6, 8));
      return new GregorianCalendar(year, month, day);
    }
    return null;
  }

  public static boolean isDateStr(String strdate)
  {
    if (strdate.length() != 8) {
      return false;
    }

    String reg = "^(\\d{4})((0([1-9]{1}))|(1[012]))((0[1-9]{1})|([1-2]([0-9]{1}))|(3[0|1]))$";

    if (Pattern.matches(reg, strdate)) {
      return getDaysOfMonth(strdate) >= Integer.parseInt(strdate
        .substring(6, 8));
    }
    return false;
  }

  public static int getDaysOfMonth(String strdate)
  {
    int m = Integer.parseInt(strdate.substring(4, 6));
    switch (m) {
    case 1:
    case 3:
    case 5:
    case 7:
    case 8:
    case 10:
    case 12:
      return 31;
    case 4:
    case 6:
    case 9:
    case 11:
      return 30;
    case 2:
      if (isLeapYear(strdate)) {
        return 29;
      }
      return 28;
    }

    return 0;
  }

  public static boolean isLeapYear(String strdate)
  {
    int y = Integer.parseInt(strdate.substring(0, 4));
    if (y <= 999) {
      return false;
    }

    return (y % 400 == 0) || ((y % 4 == 0) && (y % 100 != 0));
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.util.DateUtil
*
 */