package com.yyide.doorcontrol.brocast;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Hao on 2017/10/26.
 */

public class Brocast {
    public final static String ACTION_REBOOT = "android.intent.action.reboot";  //鸿合 重启
    public final static String ACTION_SHUTDOWN = "android.intent.action.shutdown"; // 鸿合 关机
    private static String honghe="1"; //是非为鸿合机子： 1 是 0否
    /**
     * 系统重启
     */
    public static void reboot(Context context) {
        if (honghe.equals("1")){
            Intent chongqi=new Intent(ACTION_REBOOT);
            chongqi.putExtra("enable", false);
            context.sendBroadcast(chongqi);
        }
        Intent intent = new Intent("android.yide.intent.action.reboot");
        // true:确认重启 ，false:取消重启
        intent.putExtra("enable", true);
        context.sendBroadcast(intent);
    }

    /**
     * 设置系统时间
     */
    public static void systemtime(Context context, String time) {
        Intent intent = new Intent("android.yide.intent.action.systemtime");
        int year = Integer.valueOf(time.substring(0, 4));
        int moon = Integer.valueOf(time.substring(5, 7));
        int day = Integer.valueOf(time.substring(8, 10));
        int hour = Integer.valueOf(time.substring(11, 13));
        int min = Integer.valueOf(time.substring(14, 16));
        int second = Integer.valueOf(time.substring(17, 19));

        int[] settime = {year, moon, day, hour, min, second};
        intent.putExtra("settime", settime);
        // 设置系统时间为 2017 年 8 月 31 日 08:45 intent.putExtra("enable" , true); // true:打开 ，false:关闭
        context.sendBroadcast(intent);
    }

    /**
     * 显示导航栏示例
     */
    public static void showBar(Context context) {
        Intent intent = new Intent("android.yide.intent.action.showbar");
        context.sendBroadcast(intent);
    }

    /**
     * 隐藏导航栏示例
     */
    public static void hidebar(Context context) {
        Intent intent = new Intent("android.yide.intent.action.hidebar");
        context.sendBroadcast(intent);
    }

    /**
     * 定时开关机接口,
     * timeOnArray  开机时间
     * timeOffArray 关机时间
     */
    public static void setpoweronoff(Context context, int[] timeOnArray, int[] timeOffArray) {
        Intent intent = new Intent("android.yide.intent.action.setpoweronoff");
//        int[] timeOnArray = {2017,8,27,8,45} ; // 开机时间数组，开机时间2017年8月27日08:45
//        int[] timeOffArray = {2017,8,26,19,0} ; // 关机时间数组，关机时间2017年8月26日19:00
        intent.putExtra("timeOn", timeOnArray);
        intent.putExtra("timeOff", timeOffArray);
        intent.putExtra("enable", true); // true:打开 ，false:关闭
        context.sendBroadcast(intent);
    }

    /**
     * 开关显示屏背光示例
     */
    public static void backlight(Context context, boolean enable) {
        Intent intent = new Intent("android.yide.intent.action.backlight");
        // 屏幕背光，true:打开 ，false:关闭
        intent.putExtra("enable", enable);
        context.sendBroadcast(intent);
    }

    /**
     * 设置系统关机
     */
    public static void shutdown(Context context) {
        if (honghe.equals("1")){
            Intent chongqi=new Intent(ACTION_SHUTDOWN);
            chongqi.putExtra("enable", false);
            context.sendBroadcast(chongqi);
        }
        Intent intent = new Intent("android.yide.intent.action.shutdown");
        intent.putExtra("enable", true); // true:确认关机 ，false:取消关机
        context.sendBroadcast(intent);
    }

}
