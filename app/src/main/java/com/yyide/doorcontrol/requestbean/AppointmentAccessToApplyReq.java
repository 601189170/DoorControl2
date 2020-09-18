package com.yyide.doorcontrol.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.AppointmentAccessToApplyRsp;

public class AppointmentAccessToApplyReq extends BaseBeanReq<AppointmentAccessToApplyRsp> {

    public String classesSinId;

    @Override
    public String myAddr() {
        return "/api/reservation-door/accessToApply";
    }

    @Override
    public TypeReference<AppointmentAccessToApplyRsp> myTypeReference() {
        return new TypeReference<AppointmentAccessToApplyRsp>() {
        };
    }
}
