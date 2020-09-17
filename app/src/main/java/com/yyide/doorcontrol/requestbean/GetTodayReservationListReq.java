package com.yyide.doorcontrol.requestbean;


import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.GetTodayReservationListRsp;


/**
 * Created by Hao on 2018/4/20.
 */

public class GetTodayReservationListReq extends BaseBeanReq<GetTodayReservationListRsp> {
//    roomId  房间id[必填]
//    officeId  学校id(必填)

    public int officeId;

    public String roomId;



    @Override
    public String myAddr() {
        return "/api/reservation/getTodayReservationList";
    }

    @Override
    public TypeReference<GetTodayReservationListRsp> myTypeReference() {
        return new TypeReference<GetTodayReservationListRsp>() {
        };
    }
}
