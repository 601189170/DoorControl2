package com.yyide.doorcontrol.rsponbean;

/**
 * Created by Hao on 2017/5/8.
 */

public class OfficeAccesshomeRsp {


    /**
     * status : 200
     * msg : 操作成功
     * data : {"roomNumber":"200","name":"巨大会议室"}
     */

    public int status;
    public String msg;
    public DataBean data;



    public static class DataBean {
        /**
         * roomNumber : 200
         * name : 巨大会议室
         */

        public String roomNumber;
        public String name;

   
    }
}
