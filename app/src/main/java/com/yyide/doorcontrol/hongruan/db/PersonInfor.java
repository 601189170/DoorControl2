package com.yyide.doorcontrol.hongruan.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Keep;

@Entity
public class PersonInfor {
    @Id(autoincrement = true)//设置自增长
    private Long id;

    @Index(unique = true)//设置唯一性
    private String perNo;//人员编号
    private byte[] featrue;  //人脸特征值
    private String time; //录入时间

    private String name;//人员姓名

    private String sex;//人员姓名

    @Keep
    public PersonInfor(Long id, String perNo, byte[] featrue, String name,
                       String sex, String time) {
        this.id = id;
        this.perNo = perNo;
        this.featrue = featrue;
        this.name = name;
        this.sex = sex;
        this.time=time;
    }

    @Generated(hash = 1362534400)
    public PersonInfor() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerNo() {
        return this.perNo;
    }

    public void setPerNo(String perNo) {
        this.perNo = perNo;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public byte[] getFeatrue() {
        return featrue;
    }

    public void setFeatrue(byte[] featrue) {
        this.featrue = featrue;
    }
}
