package com.yyide.doorcontrol.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.AuthenticationRsp;


public class AuthenticationReq extends BaseBeanReq<AuthenticationRsp> {

    public int officeId;

    public String userId;

    public String cardNo;

    public String type;

    public String loginName;

    public String password;
    @Override
    public String myAddr() {
        return "/api/reservation/authentication.html";
    }

    @Override
    public TypeReference<AuthenticationRsp> myTypeReference() {
        return new TypeReference<AuthenticationRsp>() {
        };
    }
}
