package com.yyide.doorcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.network.GetData;
import com.yyide.doorcontrol.requestbean.GetAddressBySerialNumberReq;
import com.yyide.doorcontrol.requestbean.LoginReq;
import com.yyide.doorcontrol.requestbean.ValidSerialKeyReq;
import com.yyide.doorcontrol.rsponbean.GetAddressBySerialNumberRsp;
import com.yyide.doorcontrol.rsponbean.LoginRsp;
import com.yyide.doorcontrol.rsponbean.ValidSerialKeyRsp;


import butterknife.ButterKnife;


public class WelcomeActivity extends AppCompatActivity {

    /*序列号数据，用户信息，序列号、登录名、密码*/
    private String registerDataStr, loginDataStr, register, loginName, passWord;

//    public static String IpNum = "1592";
    public static String IpNum = "1003";
//    public static String IpNum = "45646465";
//    public static String IpNum = "198";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        initData();

        getIp();
//        SDcarfile.save(BPUtils.startName, AppUtils.getAppPackageName());



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void initData() {
        registerDataStr = SPUtils.getInstance().getString(BaseConstant.REGISTERDATA, "");
        loginDataStr = SPUtils.getInstance().getString(SpData.LOGINDATA, "");
        register = SPUtils.getInstance().getString(BaseConstant.REGISTER, null);
        loginName = SPUtils.getInstance().getString(BaseConstant.LOGINNAME, null);
        passWord = SPUtils.getInstance().getString(BaseConstant.PASSWORD, null);
    }

    void getIp() {
        GetAddressBySerialNumberReq req = new GetAddressBySerialNumberReq();
        req.serialNumber = IpNum;
        MyApp.getInstance().requestData2(this, req, new ipListener(), new ipErrorListener());
    }

    class ipListener implements Response.Listener<GetAddressBySerialNumberRsp> {

        @Override
        public void onResponse(GetAddressBySerialNumberRsp rsp) {
            if (rsp.status == BaseConstant.REQUEST_SUCCES) {
                SPUtils.getInstance().put(BaseConstant.NUMBER, !TextUtils.isEmpty(rsp.data.ipAddress) ? rsp.data.ipAddress : GetData.URL_IP);
                getRegister();
            }
        }
    }

    class ipErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (TextUtils.isEmpty(registerDataStr))
                goActivity(RegisterActivity.class);
            else {
                ValidSerialKeyRsp serialKeyRsp = JSON.parseObject(registerDataStr, ValidSerialKeyRsp.class);
                if (serialKeyRsp.data == null) {
                    goActivity(RegisterActivity.class);
                } else
                    switch (serialKeyRsp.data.type) {
                        /* 1=测试用，2=正式用*/
                        case 1:
                            goActivity(RegisterActivity.class);
                            break;
                        case 2:
                            goActivity(TextUtils.isEmpty(loginDataStr) ? LoginActivity.class : MainActivity.class);
                            break;
                    }
            }
        }
    }

    void getRegister() {
        if (TextUtils.isEmpty(register)) {
            goActivity(RegisterActivity.class);
        } else {
            ValidSerialKeyReq req = new ValidSerialKeyReq();
            req.serialKey = register;
            req.machineCode = DeviceUtils.getAndroidID();
            MyApp.getInstance().requestData2(this, req, new registerListener(), new registerErrorListener());
        }
    }

    class registerListener implements Response.Listener<ValidSerialKeyRsp> {

        @Override
        public void onResponse(ValidSerialKeyRsp rsp) {
            if (rsp.status == BaseConstant.REQUEST_SUCCES) {
                getLogin();
            } else {
                goActivity(RegisterActivity.class);
            }
        }
    }

    class registerErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (TextUtils.isEmpty(registerDataStr))
                goActivity(RegisterActivity.class);
            else {
                ValidSerialKeyRsp serialKeyRsp = JSON.parseObject(registerDataStr, ValidSerialKeyRsp.class);
                if (serialKeyRsp.data == null) {
                    goActivity(RegisterActivity.class);
                } else
                    switch (serialKeyRsp.data.type) {
                        /* 1=测试用，2=正式用*/
                        case 1:
                            goActivity(RegisterActivity.class);
                            break;
                        case 2:
                            goActivity(TextUtils.isEmpty(loginDataStr) ? LoginActivity.class : MainActivity.class);
                            break;
                    }
            }

        }
    }

    void getLogin() {
        LoginReq req = new LoginReq();
        req.loginName = loginName;
        req.passWord = passWord;
        MyApp.getInstance().requestData(WelcomeActivity.this, req, new loginListener(), new loginErrorListener());
    }

    class loginListener implements Response.Listener<LoginRsp> {

        @Override
        public void onResponse(LoginRsp rsp) {
            if (rsp.status == BaseConstant.REQUEST_SUCCES) {
                Log.e("TAG", "LoginRspLoginRsp: "+JSON.toJSONString(rsp) );
                SPUtils.getInstance().put(SpData.LOGINDATA, JSON.toJSONString(rsp));
                goActivity(MainActivity.class);
            } else goActivity(LoginActivity.class);
        }
    }

    class loginErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            goActivity(LoginActivity.class);
        }
    }

    /**
     * 跳转Activity
     */
    void goActivity(Class clz) {
        startActivity(new Intent(WelcomeActivity.this, clz));
        finish();
    }


}
