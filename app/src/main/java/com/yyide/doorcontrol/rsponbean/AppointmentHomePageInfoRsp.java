package com.yyide.doorcontrol.rsponbean;

public class AppointmentHomePageInfoRsp {


    /**
     * status : 200
     * msg : 操作成功
     * data : {"loginName":null,"classRoomName":null,"meetingName":null,"zuzhizhe":null,"signIn":null,"signOut":null,"startTime":null,"endTime":null,"roomNO":null,"freeTime":null,"nextMeetingName":null,"meetId":null,"yingdao":null,"absenteeism":null,"roomTypeId":null,"capacity":null,"deviceName":null,"disableRoom":null,"meetTime":null,"status":4,"officeId":null}
     */

    public int status;
    public String msg;
    public DataBean data;

    public static class DataBean {
        /**
         * isHomePage: 0
         * loginName : null
         * classRoomName : null
         * meetingName : null
         * zuzhizhe : null
         * signIn : null
         * signOut : null
         * startTime : null
         * endTime : null
         * roomNO : null
         * freeTime : null
         * nextMeetingName : null
         * meetId : null
         * yingdao : null
         * absenteeism : null
         * roomTypeId : null
         * capacity : null
         * deviceName : null
         * disableRoom : null
         * meetTime : null
         * status : 4
         * officeId : null
         */

        public String isHomePage;
        public String  loginName;
        public String classRoomName;
        public String meetingName;
        public String zuzhizhe;
        public String signIn;
        public String signOut;
        public String startTime;
        public String endTime;
        public String roomNO;
        public String freeTime;
        public String nextMeetingName;
        public String meetId;
        public String yingdao;
        public String absenteeism;
        public String roomTypeId;
        public String capacity;
        public String deviceName;
        public String disableRoom;
        public String meetTime;
        public String status;
        public String officeId;
    }
}
