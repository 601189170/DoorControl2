package com.yyide.doorcontrol.rsponbean;

public class AppointmentHomePageInfoRsp {


    /**
     * status : 200
     * msg : 操作成功
     * data : {"loginName":"","classRoomName":"会议室888","meetingName":"会议 1","zuzhizhe":"玉洁","signIn":"","signOut":"","startTime":"2020-09-19 11:00:00","endTime":"2020-09-19 11:30:00","roomNO":"888","freeTime":"15:30-23:59","nextMeetingName":"","meetId":"a9265225db7d49158bfab9291574b12b","yingdao":"","absenteeism":"","roomTypeId":"","capacity":"666","deviceName":"投影仪","disableRoom":"0","meetTime":"","homePageStatus":3,"officeId":"17061","classSignId":"66ea6514ec8347cb9207c1206028490d","isHomePage":"","auId":"a1b585eb13fd4d97b992934ca8d78463","phone":""}
     */

    public int status;
    public String msg;
    public DataBean data;

    public static class DataBean {
        /**
         * loginName :
         * classRoomName : 会议室888
         * meetingName : 会议 1
         * zuzhizhe : 玉洁
         * signIn :
         * signOut :
         * startTime : 2020-09-19 11:00:00
         * endTime : 2020-09-19 11:30:00
         * roomNO : 888
         * freeTime : 15:30-23:59
         * nextMeetingName :
         * meetId : a9265225db7d49158bfab9291574b12b
         * yingdao :
         * absenteeism :
         * roomTypeId :
         * capacity : 666
         * deviceName : 投影仪
         * disableRoom : 0
         * meetTime :
         * homePageStatus : 3
         * officeId : 17061
         * classSignId : 66ea6514ec8347cb9207c1206028490d
         * isHomePage :
         * auId : a1b585eb13fd4d97b992934ca8d78463 组织者id
         * phone :
         */

        public String loginName;
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
        public String homePageStatus;
        public String officeId;
        public String classSignId;
        public String isHomePage;
        public String auId;
        public String phone;
    }
}
