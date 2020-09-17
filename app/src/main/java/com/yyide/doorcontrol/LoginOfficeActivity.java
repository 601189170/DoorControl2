package com.yyide.doorcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.mina.MinaThread;
import com.yyide.doorcontrol.requestbean.LoginOfficeReq;
import com.yyide.doorcontrol.requestbean.LoginReq;
import com.yyide.doorcontrol.rsponbean.LoginOfficeRsp;
import com.yyide.doorcontrol.rsponbean.LoginRsp;
import com.yyide.doorcontrol.utils.LoadingTools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginOfficeActivity extends AppCompatActivity {
    SweetAlertDialog pd;

    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.post)
    TextView post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_office);
        ButterKnife.bind(this);
        pd = new LoadingTools().pd(this);
//        titie.setBackgroundResource(R.drawable.fk_titile);
        if (AppUtils.isAppDebug()){
            et1.setText("FK0064");
            et2.setText("123456");
        }


    }

    @OnClick(R.id.post)
    public void onViewClicked() {
//        startActivity(new Intent(this, Ma inActivity.class));
        if (TextUtils.isEmpty(et1.getText())) {
            Toast.makeText(LoginOfficeActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et2.getText())) {
            Toast.makeText(LoginOfficeActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
        } else PostData();

//        finish();
    }

    void PostData() {
        pd.show();
        LoginOfficeReq req = new LoginOfficeReq();
        try {
            req.loginName = URLEncoder.encode(et1.getText().toString().trim(), "UTF-8");
            req.passWord = URLEncoder.encode(et2.getText().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        MyApp.getInstance().requestDataBend(this, req, new sListener(), new Error());
    }


    class sListener implements Response.Listener<LoginOfficeRsp> {

        @Override
        public void onResponse(LoginOfficeRsp rsp) {
            Log.e("TAG", "LoginRsp: " + JSON.toJSON(rsp));
            pd.dismiss();
//            if (rsp.status == BaseConstant.REQUEST_SUCCES) {
//                SPUtils.getInstance().put(BaseConstant.LOGINNAME, et1.getText().toString());
//                SPUtils.getInstance().put(BaseConstant.PASSWORD, et2.getText().toString());
//                SPUtils.getInstance().put(SpData.LOGINDATA, JSON.toJSONString(rsp));
//                startActivity(new Intent(LoginOfficeActivity.this, MainActivity.class));
//                Log.e("TAG", "SPUtils: " + JSON.toJSON(rsp));
////                L.j(JSON.toJSONString(rsp.data.cardRight));
//                LoginOfficeActivity.this.finish();
//            } else
//                Toast.makeText(LoginOfficeActivity.this, rsp.info, Toast.LENGTH_SHORT).show();
//                tips.setText(rsp.info);
        }
    }

    class Error implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            pd.dismiss();
            new SweetAlertDialog(LoginOfficeActivity.this, SweetAlertDialog.NORMAL_TYPE)
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
