package com.yyide.doorcontrol.base;


import android.os.Environment;

import com.blankj.utilcode.util.AppUtils;

/**
 * Created by Hao on 17/10/26.
 * 常量
 */
public class BaseConstant {
    /**
     * app打开次数
     */
    public static String openTimes = "OPENTIMES";
    /**
     * 分页加载数量
     */
    public static final int PAGE_SIZE = 20;

    /*请求成功消息编码*/
    public static int REQUEST_SUCCES = 10000;
    public static int REQUEST_SUCCES2 = 200;

    /*刷卡器接口*/
    public static String SPORT = "/dev/ttyS1";


    public static String SPORT2 = "/dev/ttyS4";

    public static String FILE_NAME = "FACE_TEMP.jpg";

    /*刷卡器波特率*/
//    public static int IBAUDRATE = 115200;
    public static int IBAUDRATE = 9600;
    /*无感考勤波特率*/
    public static int IBAUDRATE2 = 115200;

    /*缓存*/
    public static String SP_NAME = "LANDSCAPE";

    /*注册码*/
    public static String REGISTER = "REGISTER";

    /*注册码信息*/
    public static String REGISTERDATA = "REGISTERDATA";

    /*百度授权码*/
    public static String BAIDUKEY = "BAIDUKEY";

    /*账号*/
    public static String LOGINNAME = "LOGINNAME";

    /*密码*/
    public static String PASSWORD = "PASSWORD";

    /*mina状态，注册*/
    public static String MINA_REG = "reg";

    /*mina状态，心跳*/
    public static String MINA_IDLE = "idle";

    /*mina状态，退出*/
    public static String MINA_EXIT = "exit";

    /*请求服务器地址*/
    public static String NUMBER = "NUMBER";
    public static String StopJs = "StopJs";
    public static String StartJs = "StartJs";
    public static String DOSHOMTHING = "DOSHOMTHING";
    public static String STUDENTID = "STUDENTID";
    public static String TEACHERID = "TEACHERID";
    public static String GROUPID = "GROUPID";

    //品牌注册APK
    public static boolean IsBrand = false;

    //华瑞安机器
    public static boolean HRA = false;

    /*素质评价,教师权限*/
    public static final int QUALITY = 101;
    /*学生空间,学生权限*/
    public static final int USERCENTER = 102;
    /*班级德育,学生权限*/
    public static final int MORAL = 103;
    /*设置,学生权限*/
    public static final int SETTING = 104;
    //素质评价————教师点评
    public static final int QUALITY_DP = 105;
    public static final int SelectCourses = 106;
    public static final int WIFI = 107;
    /*加入社团*/
    public static final int ADDCLUB = 108;
    //活动报名
    public static final int ADDACTIVITE = 109;
    public static final int ADDACTIVITE2 = 113;
    //投票
    public static final int TP = 110;
    //社团联系老师
    public static final int MSGTOTEACHER = 111;
    //物联控制权限
    public static final int WLKZCHECK = 112;
    //出行身份验证
    public static final int CHEACK = 119;
    public static final int CHEACKSTUDENT = 120;
    public static final int STUDENTGO = 121;
    public static final int STUDENTBACK = 123;


    //进设置
    public static final int TOSETTING = 122;
    public static int baseWidth = 620;
//    public static int baseHeight = 309;
    public static int baseHeight = 469;
    //缓存首页排版
    public static String HOME_MODULE = "HOME_MODULE";
    //缓存第二页模板
    public static String HOME_MODULE2 = "HOME_MODULE2";
    //百度人脸db库
    public static String BD_PATH = "/data/data/" + AppUtils.getAppPackageName() + "/databases/";
    public static String BD_FEATURE_PATH = Environment.getExternalStorageDirectory() + "/BD_FEATURE/";
    public static final int GET_STUDENT = 599;
    public static final int GET_DATA = 600;
    public static final int SHOW_FKMSG = 601;
    public static final int CLOSE_FKMSG = 602;
    public static final int SHOW_ADDMSG = 603;
    public static final int CLOSE_ADDMSG = 604;
    public static final int SHOW_STUDENT = 605;
    public static final int CLOSE_STUDENT = 606;
    public static final int SHOW_NOMSG = 607;
    public static final int CLOSE_NOMSG = 608;
    public static final int SHOW_FKLEAVEMSG = 609;
    public static final int CLOSE_FKLEAVEMSG = 610;
    public static final int SHOW_FKATTEND = 611;
    public static final int CLOSE_FKATTEND = 612;
    public static final int SHOW_EWMCODE = 613;
    public static final int CLOSE_EWMCODE = 614;
    public static final int SHOW_FKLEAVETIME = 615;
    public static final int CLOSE_FKLEAVETIME = 616;
    public static final int SHOW_STUDENTBACK = 617;
    public static final int CLOSE_STUDENTBACK = 618;
    public static final int SHOW_NOSTUDENT = 619;
    public static final int CLOSE_NOSTUDENT = 620;
    public static final int SHOW_FKMSG2 = 621;
    public static final int CLOSE_FKMSG2 = 622;
    public static final int SHOW_CHEACKFACE = 623;
    public static final int CLOSE_CHEACKFACE= 624;
    public static final int SET_FACELAYOUT= 998;
    public static final int REMOVE_FACELAYOUT= 997;
    public static final int SHOW_STUDENTBACKTIME= 625;
    public static final int REMOVE_STUDENTBACKTIME= 626;
    public static final int GET_STUDENT2 = 627;
    public static final int SHOW_NOLEAVEMSG = 628;
    public static final int CLOSE_NOLEAVEMSG = 629;
    public static final int ChangeData= 999;
    public static final int stopCarmer= 630;
    public static final int startCarmer= 631;
    public static final int ERRMSG= 632;
    public static final int SETLOCATION= 633;
    public static final int NOMOLMSG= 634;
    public static final int ERRMSG2= 635;


    public static String Command="COMMAND";


}
