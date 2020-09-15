package com.yyide.doorcontrol.hongruan.faceserver;



public class CompareResult {
    private String userName; //
    private float similar;
    private int trackId;
    private String userid;

    public CompareResult(String userName, float similar,String userid) {  //比对成功的人脸userid
        this.userName = userName;
        this.similar = similar;
        this.userid=userid;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getSimilar() {
        return similar;
    }

    public void setSimilar(float similar) {
        this.similar = similar;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
