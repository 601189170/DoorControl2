package com.yyide.doorcontrol.hongruan.db;


import com.alibaba.fastjson.TypeReference;
import com.yyide.doorcontrol.requestbean.BaseBeanReq;


/**
 * Created by Hao on 2018/4/11.
 */

public class VoicefaceReq extends BaseBeanReq<VoicefacesynRsp> {

//    voiceContent	是	string	文字
//    voiceMode	是	string	发音人选择, 0为女声，1为男声，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女声

    public String studentId;
    public String teacherId;
    public int officeId;
    public int numb;
    public String classId;
    public String photo;
    public String name;
    public String facialFeatures;

    @Override
    public String myAddr() {
        return "/ArcFace/saveFaceUpdate";
    }
    @Override
    public TypeReference<VoicefacesynRsp> myTypeReference() {
        return new TypeReference<VoicefacesynRsp>() {
        };
    }
}