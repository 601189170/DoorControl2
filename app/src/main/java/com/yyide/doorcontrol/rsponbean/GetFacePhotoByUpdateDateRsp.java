package com.yyide.doorcontrol.rsponbean;

import java.util.List;

/**
 * Created by Hao on 2017/11/27.
 */

public class GetFacePhotoByUpdateDateRsp {


    /**
     * status : 10000
     * info : 查询成功
     * data : [{"studentId":null,"teacherId":"09d9b2f915c148f582ae2b206756d35a","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/5915647892358.jpeg","id":"039b87bb37034537864ba7800292d6f1","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"1fa100eda19342768f87f79269acc56a","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/对的15647892358.jpg","id":"07ed9da7751d4afa803df981ede1582b","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"c4dc70a858944789a262774ba2635c65","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/菲菲13555555566.jpg","id":"217ebcbb0df346d980ce83388a4de9e5","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"eeb267ba55fe44b5b0c78e92f1efa4b5","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/59715684935789.jpg","id":"21c7cdb564d8428dbf6553226c633df7","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"c7199f62fc9b4b0f9cd4107bc3206b02","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/王一博15726991235.jpg","id":"2b522c9b55af47129010e652f6cb59be","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"0cb85061a67d46ed8e4f935b42c65d5e","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/12312118512341234.jpg","id":"3e210baf8c7c44628cd10f32b512f132","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"dff2b00ab5a14b4b953987f176803535","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/hhhh13545678901.jpg","id":"3e9e3746f69e446b88d2d2a13de5571d","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"11449aa1c8ea4960b78771839fd839a5","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/老王13522224869.jpeg","id":"41c47a80da534f90bd1e8ca1686f2676","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"e631f25fc14f4f64acb243fd9a5c216f","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/test15945612387.jpeg","id":"44b1b6c166374b6492ef1bfacbc221a2","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"124c2e2c060145538d5c3b7f0e4ab2e9","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/隔壁老王13699999888.jpg","id":"4acf131caf714af2b3ea2040c7481db2","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"cd0c1424760b4a35a455921fa2e919d3","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/已16985478965.jpg","id":"63b52a9abdf14b4eb832793a27ab7e38","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"7bac43191afb4a51848dc41cf9fe8f7e","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/黄晓明13988888881.jpg","id":"6690402fe1fe47c082b67f337ff8ee11","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"be655651a7cd493bae6562253bc8c2d5","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/二年级（1）班15678235941.jpg","id":"7cdb6834d8c843eba1acc8cbf78e7e77","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"a04a0b9a52a4484ebf1959c1340df469","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/阮莞15527011111.jpeg","id":"813d96597b9041358c587f6816f51304","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"98b7fb0679854bd38786ad20d14f3c86","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/张老师15523456982.jpeg","id":"9ce7680100264f74a69f864dafc7b400","type":"0","error":"","delFlag":"0"},{"studentId":null,"teacherId":"58078606e3b74980891dc7dba80c0aaf","officeId":16932,"photo":"http://alicdn.yyide.com/officePhono/16932/2020-06-17/36915975315975.jpeg","id":"a47079b6c4f6435b828fb7f23b25cf5f","type":"0","error":"","delFlag":"0"}]
     */

    public int status;
    public String info;
    public List<DataBean> data;


    public static class DataBean {
        /**
         * studentId : null
         * teacherId : 09d9b2f915c148f582ae2b206756d35a
         * officeId : 16932
         * photo : http://alicdn.yyide.com/officePhono/16932/2020-06-17/5915647892358.jpeg
         * id : 039b87bb37034537864ba7800292d6f1
         * type : 0
         * error : 
         * delFlag : 0
         */

        public String studentId;
        public String teacherId;
        public String studentName;
        public String teacherName;
        public int officeId;
        public String photo;
        public String id;
        public String type;
        public String error;
        public String delFlag;
        public int stuas=0;

     
    }
}
