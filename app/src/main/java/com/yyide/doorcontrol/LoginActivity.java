package com.yyide.doorcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.requestbean.LoginReq;
import com.yyide.doorcontrol.rsponbean.LoginRsp;
import com.yyide.doorcontrol.utils.LoadingTools;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    SweetAlertDialog pd;
    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.post)
    TextView post;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        pd = new LoadingTools().pd(this);
        if (AppUtils.isAppDebug()){
            et1.setText("zj0212");
            et2.setText("123456");
        }

        Intent intent = getIntent();
        type = intent.getStringExtra("type");//点击按钮的类型
        post.setOnClickListener(this);

    }


    void PostData() {
        pd.show();
        LoginReq req = new LoginReq();
        try {
            req.loginName = URLEncoder.encode(et1.getText().toString().trim(), "UTF-8");
            req.password = URLEncoder.encode(et2.getText().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MyApp.getInstance().requestData130(this, req, new sListener(), new Error());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post:
                PostData();
                break;
        }
    }


    class sListener implements Response.Listener<LoginRsp> {
        @Override
        public void onResponse(LoginRsp rsp) {
            Log.e("TAG", "LoginRsp: " + JSON.toJSON(rsp));
            pd.dismiss();
            if (rsp.status == BaseConstant.REQUEST_SUCCES) {
                SPUtils.getInstance().put(BaseConstant.LOGINNAME, et1.getText().toString());
                SPUtils.getInstance().put(BaseConstant.PASSWORD, et2.getText().toString());
                SPUtils.getInstance().put(SpData.LOGINDATA, JSON.toJSONString(rsp));
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                Log.e("TAG", "SPUtils: " + JSON.toJSON(rsp));
//                L.j(JSON.toJSONString(rsp.data.cardRight));
                LoginActivity.this.finish();
            } else
                Toast.makeText(LoginActivity.this, rsp.msg, Toast.LENGTH_SHORT).show();
//                tips.setText(rsp.info);
        }
    }

    class Error implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
           // pd.dismiss();
            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.NORMAL_TYPE)
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
