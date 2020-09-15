package com.yyide.doorcontrol.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.UntyingfaceDataRsp;


/**
 * Created by Hao on 2017/11/28.
 */

public class UntyingfaceDataReq extends BaseBeanReq<UntyingfaceDataRsp> {

//    参数名	必选	类型	说明
//    userId	是	string	userId
//    type	是	string	解绑类型2,指纹,1,卡号
//    userType	是	string	用户类型1学生,2老师

    public String teacherId ;
    public String type ;
    public String  studentId  ;


    @Override
    public String myAddr() {
        return "/ArcFace/liftFaceUpdate";
    }

    @Override
    public TypeReference<UntyingfaceDataRsp> myTypeReference() {
        return new TypeReference<UntyingfaceDataRsp>() {
        };
    }
}
