package com.yyide.doorcontrol.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.AppointmentOpenDoorRsp;

public class AppointmentOpenDoorReq extends BaseBeanReq<AppointmentOpenDoorRsp> {


    public int officeId;
    public String classesId;
    public String accounts;
    public String password;
    public String temporaryPassword;
    public String roomId;
    public String cardNo;


    @Override
    public String myAddr() {
        return "/api/reservation-door/openDoor";
    }

    @Override
    public TypeReference<AppointmentOpenDoorRsp> myTypeReference() {
        return new TypeReference<AppointmentOpenDoorRsp>() {
        };
    }
}