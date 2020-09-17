package com.yyide.doorcontrol.requestbean;


import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.GetLastVersionRsp;


/**
 * Created by Hao on 2017/5/8.
 */

public class AccessToApplyReq extends BaseBeanReq<GetLastVersionRsp> {


    public String classesSinId;

    @Override
    public String myAddr() {
        return "/api/officeAccess/accessToApply";
    }

    @Override
    public TypeReference<GetLastVersionRsp> myTypeReference() {
        return new TypeReference<GetLastVersionRsp>() {
        };
    }
}
