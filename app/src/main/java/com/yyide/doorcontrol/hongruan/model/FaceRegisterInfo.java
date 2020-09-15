package com.yyide.doorcontrol.hongruan.model;

public class FaceRegisterInfo {
    private byte[] featureData;
    private String name;
    private String perno;
    private Long id;
    private String time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPerno() {
        return perno;
    }

    public void setPerno(String perno) {
        this.perno = perno;
    }

    public FaceRegisterInfo(byte[] faceFeature, String name,String perno,Long id,String time) {
        this.featureData = faceFeature;
        this.name = name;
        this.perno=perno;
        this.id=id;
        this.time=time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFeatureData() {
        return featureData;
    }

    public void setFeatureData(byte[] featureData) {
        this.featureData = featureData;
    }
}
