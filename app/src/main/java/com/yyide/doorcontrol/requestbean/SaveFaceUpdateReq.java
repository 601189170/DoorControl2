package com.yyide.doorcontrol.requestbean;


import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.rsponbean.SaveFaceUpdateRsp;


public class SaveFaceUpdateReq extends BaseBeanReq<SaveFaceUpdateRsp> {

    public int officeId;
    public String classId;
    public String updateDate;
    @Override
    public String myAddr() {
        return "/ArcFace/getArcFaceByUpdateDate";
    }

    @Override
    public TypeReference<SaveFaceUpdateRsp> myTypeReference() {
        return new TypeReference<SaveFaceUpdateRsp>() {
        };
    }
}
