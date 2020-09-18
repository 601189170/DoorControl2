package com.yyide.doorcontrol.rsponbean;

public class AuthenticationRsp {

    /**
     * status : 10000
     * info : 请求成功
     * data : {"name":"共和国h","photo":"","id":"016c347c95f84df78ca5bd865040c70f","type":1}
     */

    public int status;
    public String info;
    public DataBean data;

    public static class DataBean {
        /**
         * name : 共和国h
         * photo :
         * id : 016c347c95f84df78ca5bd865040c70f
         * type : 1
         */

        public String name;
        public String photo;
        public String id;
        public int type;
    }
}
