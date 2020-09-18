package com.yyide.doorcontrol.requestbean;


import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.DoorControlRsp;
import com.yyide.doorcontrol.rsponbean.GetLastVersionRsp;


/**
 * Created by Hao on 2017/5/8.
 */

public class DoorControlReq extends BaseBeanReq<DoorControlRsp> {


    public int officeId;
    public String temporaryPassword="";
    public String roomId="";
    public String cardNo="";
    public String userId="";


    @Override
    public String myAddr() {
//        return "/java-door/api/officeAccess/doorControl";
        return "/api/officeAccess/doorControl";
    }

    @Override
    public TypeReference<DoorControlRsp> myTypeReference() {
        return new TypeReference<DoorControlRsp>() {
        };
    }
}
