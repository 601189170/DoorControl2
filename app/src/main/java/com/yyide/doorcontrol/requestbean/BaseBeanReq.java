package com.yyide.doorcontrol.requestbean;


import com.alibaba.fastjson.TypeReference;

/**
 * Created by Hao on 17/10/26.
 */
public abstract class BaseBeanReq<T> {

    public abstract String myAddr();

    public abstract TypeReference<T> myTypeReference();

}
