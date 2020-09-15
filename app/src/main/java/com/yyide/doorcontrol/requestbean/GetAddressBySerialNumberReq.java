package com.yyide.doorcontrol.requestbean;


import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.GetAddressBySerialNumberRsp;


/**
 * Created by Hao on 2018/3/29.
 */

public class GetAddressBySerialNumberReq extends BaseBeanReq<GetAddressBySerialNumberRsp> {

    //    serialNumber	是	string	编号

    public String serialNumber;

    @Override
    public String myAddr() {
        return ":800/yide_manager/api/getAddressBySerialNumber.html";
    }

    @Override
    public TypeReference<GetAddressBySerialNumberRsp> myTypeReference() {
        return new TypeReference<GetAddressBySerialNumberRsp>() {
        };
    }


}
