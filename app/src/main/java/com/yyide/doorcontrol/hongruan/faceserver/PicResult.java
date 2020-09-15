package com.yyide.doorcontrol.hongruan.faceserver;

import com.arcsoft.face.FaceFeature;

/**
 * Created by Administrator on 2020/6/23.
 */

public class PicResult {
    public boolean Result; //

    public String error;

    public String id;
    public com.arcsoft.face.FaceFeature  faceFeature;

        public PicResult(boolean Result,String error ){
            this.Result=Result;
            this.error=error;
        }
    public PicResult(boolean Result,String error ,FaceFeature faceFeature){
        this.Result=Result;
        this.error=error;
        this.faceFeature=faceFeature;
    }
}
