package com.yyide.doorcontrol.requestbean;


import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.DoorControlRsp;
import com.yyide.doorcontrol.rsponbean.OfficeAccesshomeRsp;


/**
 * Created by Hao on 2017/5/8.
 */

public class OfficeAccesshomeReq extends BaseBeanReq<OfficeAccesshomeRsp> {



    public String roomId;



    @Override
    public String myAddr() {
//        return "/api/officeAccess/home";
        return "/java-door//api/officeAccess/home";
    }

    @Override
    public TypeReference<OfficeAccesshomeRsp> myTypeReference() {
        return new TypeReference<OfficeAccesshomeRsp>() {
        };
    }
}
