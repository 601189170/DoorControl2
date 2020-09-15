package com.yyide.doorcontrol;


import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
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

    public static LoginRsp User() {
        return JSON.parseObject(SPUtils.getInstance().getString(LOGINDATA, ""), LoginRsp.class);
    }

}
