package com.yyide.doorcontrol.requestbean;


import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.GetLastVersionRsp;
import com.yyide.doorcontrol.rsponbean.ToApplyRsp;


/**
 * Created by Hao on 2017/5/8.
 */

public class AccessToApplyReq extends BaseBeanReq<ToApplyRsp> {


    public String classesSinId;

    @Override
    public String myAddr() {
        return "/api/officeAccess/accessToApply";
//        return "/java-door/api/officeAccess/accessToApply";
    }

    @Override
    public TypeReference<ToApplyRsp> myTypeReference() {
        return new TypeReference<ToApplyRsp>() {
        };
    }
}
