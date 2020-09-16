package com.yyide.doorcontrol.requestbean;


import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.LoginRsp;


/**
 * Created by Hao on 2017/3/11.
 */

public class LoginReq extends BaseBeanReq<LoginRsp> {

//    loginName	是	string	用户名
//    passWord	是	string	密码

    public String loginName;

    public String password;

    @Override
    public String myAddr() {
        return "/api/reservation-door/doorLoginIn";
    }

    @Override
    public TypeReference<LoginRsp> myTypeReference() {
        return new TypeReference<LoginRsp>() {
        };
    }
}
