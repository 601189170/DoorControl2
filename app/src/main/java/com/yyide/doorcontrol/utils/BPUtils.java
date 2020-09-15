package com.yyide.doorcontrol.utils;

import android.util.Log;

import com.blankj.utilcode.util.AppUtils;

/**
 * Created by Administrator on 2019/12/11.
 */

public class BPUtils {
    public  static String  startName="startName";
        public static boolean CheckBP(){
            boolean rg=false;
            try{
                if (SDcarfile.read(startName).equals(AppUtils.getAppPackageName())) {
                    rg=true;
                } else {
                    rg=false;
                }
                Log.e("TAG", "CheckBP: " +SDcarfile.read(startName));
            }catch (Exception e){

            }

         return rg;
        }


}
