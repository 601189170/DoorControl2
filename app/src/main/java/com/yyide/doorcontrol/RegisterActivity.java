package com.yyide.doorcontrol;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.requestbean.ValidSerialKeyReq;
import com.yyide.doorcontrol.rsponbean.ValidSerialKeyRsp;
import com.yyide.doorcontrol.utils.LoadingTools;
import com.yyide.doorcontrol.utils.SDcarfile;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class RegisterActivity extends AppCompatActivity {

    SweetAlertDialog pd;
    @BindView(R.id.et1)
    EditText et1;

    @BindView(R.id.post)
    TextView post;


    @BindView(R.id.regist_b)
    TextView registB;
    private String registfileName = "regist";

    /**
     * 屏幕数组
     **/
    private Display[] displays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        pd = new LoadingTools().pd(this);
//        if (AppUtils.isAppDebug()){
//            et1.setText("202001020000");
//
//            if (AppUtils.isAppDebug()){
//                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                RegisterActivity.this.finish();
//
//            }
//        }
        try {
            if (SDcarfile.read(registfileName).equals("")) {
                registB.setVisibility(View.GONE);
            } else {
                et1.setHint(new SpannedString("请点击右侧按钮获取注册码"));
                registB.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            registB.setVisibility(View.GONE);
        }
        registB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et1.setText(SDcarfile.read(registfileName));
            }
        });
        if (AppUtils.isAppDebug())
            startActivity(new Intent(this,LoginOfficeActivity.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("TAG", "onPause: " );
    }

    @OnClick(R.id.post)
    public void onViewClicked() {
        if (TextUtils.isEmpty(et1.getText())) {
            ToastUtils.showShort("请输入注册码");
        }  else {
            request();
        }
    }

    void request() {
        pd.show();
        ValidSerialKeyReq req = new ValidSerialKeyReq();
        req.serialKey = et1.getText().toString();
        req.machineCode = DeviceUtils.getAndroidID();
        MyApp.getInstance().requestData2(this, req, new listener(), new error());
    }

    class listener implements Response.Listener<ValidSerialKeyRsp> {

        @Override
        public void onResponse(ValidSerialKeyRsp rsp) {
            pd.dismiss();
            if (rsp.status == BaseConstant.REQUEST_SUCCES) {
                SDcarfile.save(registfileName, et1.getText().toString());
                SPUtils.getInstance().put(BaseConstant.REGISTER, et1.getText().toString());
                SPUtils.getInstance().put(BaseConstant.REGISTERDATA, JSON.toJSONString(rsp));
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            RegisterActivity.this.finish();
            } else
                ToastUtils.showShort(rsp.info);
        }
    }

    class error implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            pd.dismiss();
            new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("网络设置").setContentText("网络异常，请检查网络。").setCancelText("取消").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.cancel();
                }
            }).setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                    sweetAlertDialog.cancel();
                }
            }).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }


}
