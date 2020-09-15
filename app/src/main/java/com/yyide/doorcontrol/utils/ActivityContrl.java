package com.yyide.doorcontrol.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2017/10/18.
 */

public class ActivityContrl {

    private static List<Activity> activityList = new ArrayList<>();

    public static void add(Activity activity) {
        activityList.add(activity);
    }

    public static void remove(Activity activity) {
        activityList.remove(activity);
    }

    public static void finishProgram() {
        for (Activity activity : activityList) {
            if (null != activity) {
                activity.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
