package com.yyide.doorcontrol.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.AppointmentHomePageInfoRsp;

public class AppointmentHomePageInfoReq  extends BaseBeanReq<AppointmentHomePageInfoRsp> {

//    loginName	是	string	用户名
//    passWord	是	string	密码

    public String signId;
    @Override
    public String myAddr() {
        return "/java-door/api/reservation-door/homePageList";
    }

    @Override
    public TypeReference<AppointmentHomePageInfoRsp> myTypeReference() {
        return new TypeReference<AppointmentHomePageInfoRsp>() {
        };
    }
}