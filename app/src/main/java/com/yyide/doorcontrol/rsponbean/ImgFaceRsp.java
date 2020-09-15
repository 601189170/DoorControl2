package com.yyide.doorcontrol.rsponbean;

/**
 * Created by Administrator on 2019/7/15.
 */

public class ImgFaceRsp {

    /**
     * msgType : 33
     * data : {"date":"2020-06-22 09:58:42"}
     */

    public String msgType;
    public DataBean data;



    public static class DataBean {
        /**
         * date : 2020-06-22 09:58:42
         */

        public String date;

    }
}
