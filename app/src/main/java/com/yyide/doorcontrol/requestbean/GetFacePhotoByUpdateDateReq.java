package com.yyide.doorcontrol.requestbean;


import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.GetFacePhotoByUpdateDateRsp;

/**
 * Created by Hao on 2017/11/27.
 */

public class GetFacePhotoByUpdateDateReq extends BaseBeanReq<GetFacePhotoByUpdateDateRsp> {
//    参数officeId 学校id    time   时间 yyyy-mm-dd hh：mm：ss 这种格式
    public int officeId;

    public String time;




    @Override
    public String myAddr() {
        return "/api/facephoto/getFacePhotoByUpdateDate";
    }

    @Override
    public TypeReference<GetFacePhotoByUpdateDateRsp> myTypeReference() {
        return new TypeReference<GetFacePhotoByUpdateDateRsp>() {
        };
    }
}
