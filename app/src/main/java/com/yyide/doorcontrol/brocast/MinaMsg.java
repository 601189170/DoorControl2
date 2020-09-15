package com.yyide.doorcontrol.brocast;

import android.os.Build;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.mina.RegMina;


/**
 * Created by Hao on 2017/11/16.
 */

public class MinaMsg {

    /**
     * 注册mina====reg===
     * 空闲mina====idle===
     * 退出mina====exit===
     */
    public static String MinaMsg(String type) {
        RegMina regMina = new RegMina();
        regMina.msgType = type;
        regMina.alias = SPUtils.getInstance().getString(BaseConstant.LOGINNAME, null);
        regMina.androidId = DeviceUtils.getAndroidID();
        regMina.incremental = Build.VERSION.INCREMENTAL;
        regMina.versionCode = AppUtils.getAppVersionCode();
        regMina.versionName = AppUtils.getAppVersionName();
//        regMina.powerTime = timePowerStr(MyApp.getInstance().powerOffDao.getPower());
        return JSON.toJSONString(regMina);
    }
}
