package com.yyide.doorcontrol.mina;


import com.yyide.doorcontrol.SpData;

/**
 * Created by Hao on 2017/5/12.
 */

/*连接mina成功发送注册*/
public class RegMina {

    public String msgType;

    public String alias;

    public String androidId;

    public int versionCode;

    public String versionName;

    /*固件版本*/
    public String incremental;
    /*开关机时间*/
    public String powerTime;

    public long update_time;

    public int office_id = 16932;
//    public int office_id = SpData.User() != null ? SpData.User().data.officeId : -1;
//    public int office_id = SpData.User() != null ? SpData.User().data.officeId : -1;
//
}
