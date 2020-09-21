package com.yyide.doorcontrol.rsponbean;

/**
 * Created by Hao on 2018/4/20.
 */

public class SaveMeetAttendanceRsp {


    /**
     * status : 10000
     * info : 重复签到
     * data : {"name":"张小小","photo":"upload/201906/13/1560405011191026357.jpg","id":"216594bfd73b4faa81e72d54b3d0abe5","type":1}
     */

    public int status;
    public String info;
    public DataBean data;

    public static class DataBean {
        /**
         * name : 张小小
         * photo : upload/201906/13/1560405011191026357.jpg
         * id : 216594bfd73b4faa81e72d54b3d0abe5
         * type : 1
         */

        public String name;
        public String photo;
        public String id;
        public int type;
    }
}
