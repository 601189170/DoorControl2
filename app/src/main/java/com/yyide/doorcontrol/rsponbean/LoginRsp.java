package com.yyide.doorcontrol.rsponbean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2017/3/11.
 */

public class LoginRsp {

    /**
     * status : 10000
     * info : 请求成功
     * data : {"officeName":"测试001","officeId":16790,"name":"一年级二班","logo":"upload/201908/20/1566311258772072583.jpg","id":"4cc4b46acf204fc8820255dae3a86a01","list":["6","4","1","2","3","5"]}
     */

    public int status;
    public String msg;
    public DataBean data;

    public static class DataBean {
        /**
         * loginName : zj0212
         * signId : test1111111111111111111111111111111
         */


        public int officeId;
        public String loginName;
        public String signId;

    }
}
