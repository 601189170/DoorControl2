package com.yyide.doorcontrol.rsponbean;

/**
 * Created by Hao on 2017/3/11.
 */

public class ValidSerialKeyRsp {

    /**
     * status : 10000
     * info : 操作成功
     * data : {"type":1}
     */

    public int status;
    public String info;
    public DataBean data;

    public static class DataBean {
        /**
         * type : 1    注册码类型 1=测试用，2=正式用
         */

        public int type;
    }
}
