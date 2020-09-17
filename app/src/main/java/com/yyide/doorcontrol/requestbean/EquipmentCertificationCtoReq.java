package com.yyide.doorcontrol.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.EquipmentCertificationCtoRsp;

public class EquipmentCertificationCtoReq extends BaseBeanReq<EquipmentCertificationCtoRsp> {


    public int officeId;
    public String classesId;
    public String accounts;
    public String password;
    public String temporaryPassword;
    public String roomId;
    public String cardNo;


    @Override
    public String myAddr() {
        return "/api/reservation-door/equipmentCertificationCto";
    }

    @Override
    public TypeReference<EquipmentCertificationCtoRsp> myTypeReference() {
        return new TypeReference<EquipmentCertificationCtoRsp>() {
        };
    }
}