package com.yyide.doorcontrol.requestbean;


import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.LoginOfficeRsp;
import com.yyide.doorcontrol.rsponbean.LoginRsp;


/**
 * Created by Hao on 2017/3/11.
 */

public class LoginOfficeReq extends BaseBeanReq<LoginOfficeRsp> {

//    loginName	是	string	用户名
//    passWord	是	string	密码

    public String loginName;

    public String passWord;

    @Override
    public String myAddr() {
        return "/api/officeAccess/home";
    }

    @Override
    public TypeReference<LoginOfficeRsp> myTypeReference() {
        return new TypeReference<LoginOfficeRsp>() {
        };
    }
}
