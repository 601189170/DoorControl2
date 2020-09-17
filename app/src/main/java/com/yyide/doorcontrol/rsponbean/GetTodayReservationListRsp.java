package com.yyide.doorcontrol.rsponbean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2018/4/20.
 */

public class GetTodayReservationListRsp {


    /**
     * status : 10000
     * info : 请求成功
     * data : [{"classesName":[],"meetDay":"2019年8月1日","teacherName":"教务","endDate":"08:45","meetType":"会议","name":"hh","photo":"","section":"","id":"044523f992084eaf8164d3c4ce05b6ad","startDate":"08:00"},{"classesName":["二年级（3）班"],"meetDay":"2019年8月1日","teacherName":"教务","endDate":"08:30","meetType":"教学","name":"gg","photo":"","section":"","id":"ab38e2332a5d4d2081429023c85dc8ab","startDate":"08:00"},{"classesName":[],"meetDay":"2019年8月1日","teacherName":"教务","endDate":"09:30","meetType":"会议","name":"hh","photo":"","section":"","id":"6729b4d63eff41e5b8de8f66f0ee902a","startDate":"08:30"},{"classesName":[],"meetDay":"2019年8月1日","teacherName":"教务","endDate":"10:00","meetType":"会议","name":"gg","photo":"","section":"","id":"575ac8661f674ba19447d348576d8b72","startDate":"09:30"},{"classesName":["二年级（3）班"],"meetDay":"2019年8月1日","teacherName":"教务","endDate":"10:30","meetType":"教学","name":"ff","photo":"","section":"","id":"c9f904d83bc3430783d9ce2b1385fe87","startDate":"10:00"},{"classesName":[],"meetDay":"2019年8月1日","teacherName":"教务","endDate":"11:30","meetType":"会议","name":"cc","photo":"","section":"","id":"7067c57ce9fe4bf3b86e880b283fd0c3","startDate":"11:00"}]
     */

    public int status;
    public String info;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * classesName : []
         * meetDay : 2019年8月1日
         * teacherName : 教务
         * endDate : 08:45
         * meetType : 会议
         * name : hh
         * photo :
         * section :
         * id : 044523f992084eaf8164d3c4ce05b6ad
         * startDate : 08:00
         */

        public String meetDay;
        public String teacherName;
        public String endDate;
        public String meetType;
        public String name;
        public String photo;
        public String section;
        public String id;
        public String startDate;
        public int status ;
        public List<String> classesName=new ArrayList<>();
    }
}
