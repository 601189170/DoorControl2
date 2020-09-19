package com.yyide.doorcontrol.requestbean;


import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.SaveDeviceRepairRsp;


/**
 * Created by Hao on 2018/4/20.
 */

public class SaveDeviceRepairReq extends BaseBeanReq<SaveDeviceRepairRsp> {
//    repairId  报修人id(必填)
//    officeId  学校id(必填)
//    roomId    会议室id(必填)
//    details   报修内容(必填)
    public int officeId;
    public String repairId;
    public String roomId;
    public String details;


    @Override
    public String myAddr() {
        return "/api/reservation/saveDeviceRepair";
    }

    @Override
    public TypeReference<SaveDeviceRepairRsp> myTypeReference() {
        return new TypeReference<SaveDeviceRepairRsp>() {
        };
    }
}
