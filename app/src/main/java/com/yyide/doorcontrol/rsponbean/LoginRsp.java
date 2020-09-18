package com.yyide.doorcontrol.rsponbean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2017/3/11.
 */

public class LoginRsp {


    /**
     * status : 200
     * msg : 操作成功
     * data : {"password":"123456","loginName":"zj0212","signId":"test1111111111111111111111111111111","scId":"2","scName":"会议门禁","roomId":"bcf43189c35d4392b3f562f9f6b7e2d2"}
     */

    public int status;
    public String msg;
    public DataBean data;

    public static class DataBean {
        /**
         * password : 123456
         * loginName : zj0212
         * signId : test1111111111111111111111111111111
         * scId : 2
         * scName : 会议门禁
         * roomId : bcf43189c35d4392b3f562f9f6b7e2d2
         */
        public int  officeId;
        public String password;
        public String loginName;
        public String signId;
        public String scId;
        public String scName;
        public String roomId;
        public List<String> list=new ArrayList<>();
    }
}
