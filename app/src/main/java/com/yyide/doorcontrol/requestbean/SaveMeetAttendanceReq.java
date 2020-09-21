package com.yyide.doorcontrol.requestbean;


import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.SaveMeetAttendanceRsp;


/**
 * Created by Hao on 2018/4/20.
 */

public class SaveMeetAttendanceReq extends BaseBeanReq<SaveMeetAttendanceRsp> {



    public int officeId;

    public String userId;

    public String cardNo;

    public String meetingId;


    @Override
    public String myAddr() {
        return "/api/attendance/saveMeetAttendance";
    }

    @Override
    public TypeReference<SaveMeetAttendanceRsp> myTypeReference() {
        return new TypeReference<SaveMeetAttendanceRsp>() {
        };
    }
}
