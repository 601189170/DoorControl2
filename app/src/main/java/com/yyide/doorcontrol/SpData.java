package com.yyide.doorcontrol;


import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;

import com.yyide.doorcontrol.rsponbean.AppointmentHomePageInfoRsp;
import com.yyide.doorcontrol.rsponbean.GetTodayReservationListRsp;
import com.yyide.doorcontrol.rsponbean.LoginRsp;

/**
 * Created by Hao on 2017/11/16.
 */

public class SpData {


    public static String HomeWeaher = "HomeWeaher";
    public static String LOGINDATA = "LOGINDATA";
    public static String POWEROFFON = "POWEROFFON";
    public static String IMGList = "IMGList";
//    public static float facefortrue=0.70F;//人脸值

    public static float facefortrue=0.75F;//人脸值

    public static String  FACEDATA="FACEDATA";
    public static String  ICCard="ICCard";
    public static String  HOMEINDEX="HOMEINDEX";

    public static String  ISCARDSHOW="ISCARDSHOW";
    public static String  SHOWIDENTY="SHOWIDENTY";

    public static String  appointment="appointment";
    public static String  OfficeType="OfficeType";
    public static String AppointmentHome = "AppointmentHome";
    public static String ReservationList = "ReservationList";//预约会议
    public static String IDENTYFACE = "IDENTYFACE";



    public static boolean isIdentyface() {
        return SPUtils.getInstance().getBoolean(IDENTYFACE);
    }

    public static String getLoginType() {
        String type = null;

        if (User()!=null){
           type=User().data.scId;
        }
        return type;
    }
    public static LoginRsp User() {
        return JSON.parseObject(SPUtils.getInstance().getString(LOGINDATA, ""), LoginRsp.class);
    }
    public static AppointmentHomePageInfoRsp AppointmentHomeData() {
        return JSON.parseObject(SPUtils.getInstance().getString(AppointmentHome, ""),AppointmentHomePageInfoRsp.class);
    }

    public static GetTodayReservationListRsp ReservationList() {
        return JSON.parseObject(SPUtils.getInstance().getString(ReservationList, ""), GetTodayReservationListRsp.class);
    }


    public static boolean CheackPower(String i){
        boolean result=false;
        if (SpData.User()!=null){
            for (String s : SpData.User().data.list) {
                if (TextUtils.equals(s,i)){
                    result=true;

                }
            }
        }

        return result;

    }




}
