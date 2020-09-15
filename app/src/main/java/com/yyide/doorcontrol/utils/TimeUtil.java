package com.yyide.doorcontrol.utils;

import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hao on 2017/7/18.
 */

public class TimeUtil {

    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {

        boolean result = false;

        final long aDayInMillis = 1000 * 60 * 60 * 24;

        final long currentTimeMillis = System.currentTimeMillis()+10*60*1000;

        Time now = new Time();
        now.set(currentTimeMillis);

        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;

        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;

        if (!startTime.before(endTime)) {
            // 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            // 普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }

    /**
     * 传入的时间是否已经结束
     * @param endDate 传入的时间
     * @return 返回
     */
    public static boolean isDateOver(String endDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date now = new Date();
            Date old = sdf.parse(endDate);
            return now.before(old) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false ;
    }
    /**
     * 先用SimpleDateFormat.parse() 方法将日期字符串转化为Date格式
     * 通过Date.getTime()方法，将其转化为毫秒数  String date = "2001-03-15 15：37：05";
     */
    public static long gettime(String str){





        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//24小时制
        //      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");//12小时制
        long time2 = 0;
        if (str!=null){
            try {
                time2 = simpleDateFormat.parse(str).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println(time2);
        return time2;

    }
    public static long gettime10(String str){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//24小时制
        //      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");//12小时制
        long time2 = 0;
        if (str!=null){
            try {
                time2 = simpleDateFormat.parse(str).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println(time2);
        return time2;

    }
    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long timeMillis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }
    public static String stampToDate2(long timeMillis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }
    public static String stampToDate3(long timeMillis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }
    public static String stampToDate4(long timeMillis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }
    public static String stampToDate6(long timeMillis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }
    public static String stampToDate7(long timeMillis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }
    public static String stampToDate5(long timeMillis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月-dd日");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    public static List<WeekDay> getWeekDay() {



        Calendar calendar = Calendar.getInstance();

        calendar.setFirstDayOfWeek(Calendar.SUNDAY);

        // 获取本周的第一天
        int firstDayOfWeek = calendar.getFirstDayOfWeek();

        List<WeekDay> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + i);
            WeekDay weekDay = new WeekDay();
            // 获取星期的显示名称，例如：周一、星期一、Monday等等
            weekDay.week = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.CHINA);
            weekDay.day = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

            list.add(weekDay);
        }

        return list;
    }

    public static class WeekDay {
        /** 星期的显示名称*/
        public String week;
        /** 对应的日期*/
        public String day;




        @Override
        public String toString() {
//			return "WeekDay{" +
//					"week='" + week + '\'' +
//					", day='" + day + '\'' +
//					'}';
            return day ;


        }
    }
    public static long gettime2(String str){


//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//24小时制
              SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");//12小时制
        long time2 = 0;
        if (str!=null){
            try {
                time2 = simpleDateFormat.parse(str).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println(time2);
        return time2;

    }
    public static long gettime3(String str){


//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//24小时制
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//12小时制
        long time2 = 0;
        if (str!=null){
            try {
                time2 = simpleDateFormat.parse(str).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println(time2);
        return time2;

    }
    /**
     * 日期格式字符串转换成时间戳
//     * @param date 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStamp(String date_str,String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime()/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static String getWeek(long time) {

        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(time));

        int year  = cd.get(Calendar.YEAR); //获取年份
        int month = cd.get(Calendar.MONTH); //获取月份
        int day   = cd.get(Calendar.DAY_OF_MONTH); //获取日期
        int week  = cd.get(Calendar.DAY_OF_WEEK); //获取星期

        String weekString;
        switch (week) {
            case Calendar.SUNDAY:
                weekString = "周日";
                break;
            case Calendar.MONDAY:
                weekString = "周一";
                break;
            case Calendar.TUESDAY:
                weekString = "周二";
                break;
            case Calendar.WEDNESDAY:
                weekString = "周三";
                break;
            case Calendar.THURSDAY:
                weekString = "周四";
                break;
            case Calendar.FRIDAY:
                weekString = "周五";
                break;
            default:
                weekString = "周六";
                break;

        }

        return weekString;
    }
    public static String stampToDate10(long timeMillis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }
}
