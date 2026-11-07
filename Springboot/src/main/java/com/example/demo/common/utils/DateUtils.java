package com.example.demo.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final Locale DEFAULT_LOCALE = Locale.getDefault();
    private static final String[] WEEK_NAMES = new String[] { "일", "월", "화", "수", "목", "금", "토"};
    public static final int millisPerSecond = 1000;
    public static final int millisPerMinute = 60000;
    public static final int millisPerHour = 3600000;
    public static final int millisPerDay = 86400000;
  
    public static Date check(String dateTime, String pattern) throws ParseException {
        if (dateTime == null)
            throw new ParseException("date string to check is null", 0); 
        if (pattern == null)
            throw new ParseException("pattern string to check date is null", 0);
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, DEFAULT_LOCALE);
        Date date = simpleDateFormat.parse(dateTime);
        
        if (!simpleDateFormat.format(date).equals(dateTime))
            throw new ParseException("Out of bound date:\"" + dateTime + "\" with pattern \"" + pattern + "\"", 0); 
        return date;
    }
  
    public static boolean isDate(String dateTime, String pattern) {
        try {
            check(dateTime, pattern);
        } catch (ParseException pe) {
            return false;
        } 
        return true;
    }
  
    public static Date toDate(String dateTime, String pattern) {
        Date date = null;
    
        try {
            date = check(dateTime, pattern);
        } catch (ParseException pe) {
            pe.printStackTrace();
        } 
        return date;
    }
  
    public static Date toDate(Calendar calendar) {
        return calendar.getTime();
    }
  
    public static Calendar toCalendar(String dateTime, String pattern) {
        return toCalendar(toDate(dateTime, pattern));
    }
  
    public static Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance(DEFAULT_LOCALE);
        calendar.setTime(date);
        
        return calendar;
    }
  
    public static String toString(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
  
    public static String toString(Calendar calendar, String pattern) {
        return toString(toDate(calendar), pattern);
    }
  
    public static String getCurrent(String pattern) {
        return toString(new Date(), pattern);
    }
  
    public static String getCurrent() {
        return getCurrent("yyyy-MM-dd HH:mm:ss");
    }
  

    public static int betweenYear(String from, String to, String pattern) {
        Calendar fromCalendar = toCalendar(from, pattern);
        Calendar toCalendar = toCalendar(to, pattern);
        
        return toCalendar.get(1) - fromCalendar.get(1);
    }
  
    public static int betweenMonth(String from, String to, String pattern) {
        Calendar fromCalendar = toCalendar(from, pattern);
        Calendar toCalendar = toCalendar(to, pattern);
        
        int year = toCalendar.get(1) - fromCalendar.get(1);
        int month = toCalendar.get(2) - fromCalendar.get(2);
    
        return year * 12 + month;
    }
  
    public static int betweenDays(String from, String to, String pattern) {
       return (int)(betweenTime(from, to, pattern) / 86400000L);
    }
  
    public static long betweenTime(String from, String to, String pattern) {
       Date fromTime = toDate(from, pattern);
       Date toTime = toDate(to, pattern);
    
       return toTime.getTime() - fromTime.getTime();
    }
  
    public static String addYears(String dateTime, String pattern, int years) {
      Calendar calendar = toCalendar(dateTime, pattern);
      calendar.add(1, years);
    
      return toString(calendar, pattern);
    }
  
    public static String addMonths(String dateTime, String pattern, int months) {
       Calendar calendar = toCalendar(dateTime, pattern);
       calendar.add(2, months);
    
       return toString(calendar, pattern);
    }
  
    public static String addDays(String dateTime, String pattern, int days) {
       Calendar calendar = toCalendar(dateTime, pattern);
       calendar.add(5, days);
    
       return toString(calendar, pattern);
    }
  
    public static String addHours(String dateTime, String pattern, int hours) {
    	Calendar calendar = toCalendar(dateTime, pattern);
        calendar.add(10, hours);
    
        return toString(calendar, pattern);
    }
  
    public static String addMinute(String dateTime, String pattern, int minutes) {
       Calendar calendar = toCalendar(dateTime, pattern);
       calendar.add(12, minutes);
       
       return toString(calendar, pattern);
    }
  
    public static String addSeconds(String dateTime, String pattern, int seconds) {
       Calendar calendar = toCalendar(dateTime, pattern);
       calendar.add(13, seconds);
       
       return toString(calendar, pattern);
   }
  
   public static String getLastDayOfMonth(String dateTime, String pattern) {
      Calendar calendar = toCalendar(dateTime, pattern);
      calendar.set(5, calendar.getActualMaximum(5));
      
      return toString(calendar, pattern);
   }
  
   public static int getDayOfWeek(String dateTime, String pattern) {
      return toCalendar(dateTime, pattern).get(7);
   }
  
   public static String dayOfWeek(String dateTime, String pattern) {
      return WEEK_NAMES[getDayOfWeek(dateTime, pattern) - 1];
   }
   
	// 오늘 날자
	public static String toDay(String pattern) {
	   LocalDate date = LocalDate.now();
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
       
       return date.format(formatter);
	}
}
