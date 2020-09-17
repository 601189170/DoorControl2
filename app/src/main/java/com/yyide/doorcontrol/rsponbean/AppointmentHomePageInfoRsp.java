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
         * loginName : null  用户名
         * classRoomName : null房间名称
         * meetingName : null会议名称
         * zuzhizhe : null组织者
         * signIn : null提前多少分钟开场
         * signOut : null推后多少分钟离场
         * startTime : null会议开始时间
         * endTime : 会议结束时间
         * roomNO : 房间号
         * freeTime : null空闲时间
         * nextMeetingName : null下一场会议名称
         * meetId : 会议id
         * yingdao : 应到人数
         * absenteeism : 缺勤
         * roomTypeId : 房间类型id
         * capacity : 房间容纳人数
         * deviceName : 设备
         * disableRoom : 是否启用
         * meetTime :当前会议时间
         *  homePageStatus : 4
         * officeId : 学校id
         * classSignId:    //门禁类别id  2，会议门禁  1，普通门禁， 3，宿舍门禁
         * private String scId;
         *
         * private String scName;
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
        public String  homePageStatus;
        public String officeId;
        private String classSignId;
    }
}
