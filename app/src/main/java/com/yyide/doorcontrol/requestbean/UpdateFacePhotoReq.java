package com.yyide.doorcontrol.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.UpdateFacePhotoRsp;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by Hao on 2017/11/27.
 */

public class UpdateFacePhotoReq extends BaseBeanReq<UpdateFacePhotoRsp> {
//    id  标识 我给你的     type  状态1 成 2 失败   error  失败原因


    public List<UpdateBean> list=new ArrayList<>();


    public static class UpdateBean{

        public String id;

        public String type;

        public String error;

        public int officeId;
    }


    @Override
    public String myAddr() {
        return "/api/facephoto/updateFacePhoto";
    }

    @Override
    public TypeReference<UpdateFacePhotoRsp> myTypeReference() {
        return new TypeReference<UpdateFacePhotoRsp>() {
        };
    }
}
