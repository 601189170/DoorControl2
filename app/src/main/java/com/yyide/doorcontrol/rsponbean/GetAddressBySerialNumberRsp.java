package com.yyide.doorcontrol.rsponbean;

/**
 * Created by Hao on 2018/3/29.
 */

public class GetAddressBySerialNumberRsp {
    /**
     * status : 10000
     * info : 操作成功
     * data : {"ipAddress":"10.200.0.205"}
     */

    public int status;
    public String info;
    public DataBean data;

    public static class DataBean {
        /**
         * ipAddress : 10.200.0.205
         */

        public String ipAddress;
    }


//    /**
//     * status : 10000
//     * info : 操作成功
//     * data : {"ipAddress":"120.76.189.190"}
//     */
//
//    public int status;
//    public String info;
//    public DataBean data;
//
//    public static class DataBean {
//        /**
//         * ipAddress : 120.76.189.190
//         */
//
//        public String ipAddress;
//    }
}
