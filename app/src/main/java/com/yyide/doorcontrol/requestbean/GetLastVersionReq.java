package com.yyide.doorcontrol.requestbean;


import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.GetLastVersionRsp;


/**
 * Created by Hao on 2017/5/8.
 */

public class GetLastVersionReq extends BaseBeanReq<GetLastVersionRsp> {

    //appType	是	string	1=标准版，2=精简版
    public int appType = 6;

    public int officeId;
    public int remarks=1;//控制群体跟新的


    @Override
    public String myAddr() {
        return "api/getLastVersion.html";
    }

    @Override
    public TypeReference<GetLastVersionRsp> myTypeReference() {
        return new TypeReference<GetLastVersionRsp>() {
        };
    }
}
