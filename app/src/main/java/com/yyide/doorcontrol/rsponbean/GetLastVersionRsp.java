package com.yyide.doorcontrol.rsponbean;

/**
 * Created by Hao on 2017/5/8.
 */

public class GetLastVersionRsp {

    /**
     * status : 10000
     * info : 操作成功
     * data : {"id":null,"remarks":"测试更新","createDate":"2017-09-24 10:18:41","updateDate":"2017-10-23 10:05:25","delFlag":"0","isNewRecord":true,"appId":53,"appType":"3","updateType":"1","appVersion":"40","appSize":"40993","appUrl":"http://alicdn.yyide.com/qt340.apk","packagename":"5","activityname":"2","officeId":12350,"officeName":null,"page":null}
     */

    public int status;
    public String info;
    public DataBean data;

    public static class DataBean {
        /**
         * id : null
         * remarks : 测试更新
         * createDate : 2017-09-24 10:18:41
         * updateDate : 2017-10-23 10:05:25
         * delFlag : 0
         * isNewRecord : true
         * appId : 53
         * appType : 3
         * updateType : 1
         * appVersion : 40
         * appSize : 40993
         * appUrl : http://alicdn.yyide.com/qt340.apk
         * packagename : 5
         * activityname : 2
         * officeId : 12350
         * officeName : null
         * page : null
         */

        public String id;
        public String remarks;
        public String createDate;
        public String updateDate;
        public String delFlag;
        public boolean isNewRecord;
        public int appId;
        public String appType;
        public int updateType;
        public int appVersion;
        public float appSize;
        public String appUrl;
        public String packagename;
        public String activityname;
        public int officeId;
        public Object officeName;
        public Object page;
    }
}
