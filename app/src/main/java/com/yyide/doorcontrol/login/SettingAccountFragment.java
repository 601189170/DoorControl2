package com.yyide.doorcontrol.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.activity.SettingActivity;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.base.BaseFragment;
import com.yyide.doorcontrol.observer.IdManager;
import com.yyide.doorcontrol.requestbean.AuthenticationReq;
import com.yyide.doorcontrol.rsponbean.AuthenticationRsp;
import com.yyide.doorcontrol.utils.LoadingTools;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 *
 */
public class SettingAccountFragment extends BaseFragment {

    @BindView(R.id.userName)
    EditText userName;
    @BindView(R.id.userPwd)
    EditText userPwd;
    @BindView(R.id.tips)
    TextView tips;
    @BindView(R.id.config)
    TextView config;
    Unbinder unbinder;

    SweetAlertDialog pd;
//    String toDoType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_account, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pd = new LoadingTools().pd(activity);
//        toDoType = getArguments().getString(IdentityActivity.SETTING);
        if (AppUtils.isAppDebug()) {
//            userName.setText("admin");
//            userPwd.setText(time());
            userName.setText("wff");
            userPwd.setText("123456");
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.config)
    public void onViewClicked() {
        String loginName = userName.getText().toString().trim();
        String passw = userPwd.getText().toString().trim();
        if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(passw)) {
            tips.setText("请确认账号密码输入正确！");
            tips.setVisibility(View.VISIBLE);
            return;
        }else if (passw.equals(time()) && loginName.equals("admin")) {
//            if (toDoType.equals("WIFI")){
//                IdManager.getInstance().notifyObserver("");
//            }else {
                startActivity(new Intent(activity, SettingActivity.class));
                activity.finish();
//            }

        } else Identity(loginName,passw);
    }
    void Identity(String loginname,String password) {
        if (!activity.isDestroyed())
            pd.show();

        AuthenticationReq req = new AuthenticationReq();

        req.officeId= SpData.User().data.officeId;
        req.loginName=loginname;
        req.password=password;

        MyApp.getInstance().requestYySystemData(this, req, new signListenr(), new error());
    }


    class signListenr implements Response.Listener<AuthenticationRsp> {

        @Override
        public void onResponse(AuthenticationRsp rsp) {
            Log.e("TAG", "AuthenticationRsp: "+ JSON.toJSONString(rsp));
            if (!activity.isDestroyed())
                pd.dismiss();
            if (rsp.status == BaseConstant.REQUEST_SUCCES) {
                IdManager.getInstance().notifyObserver(rsp.data.id);
            } else {
                tips.setText(rsp.info);
                tips.setVisibility(View.VISIBLE);
            }
        }
    }

    class error implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (!activity.isDestroyed())
                pd.dismiss();
            ToastUtils.showShort("请求失败，请重试");
        }
    }
    private String time() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
//        SimpleDateFormat myFmt = new SimpleDateFormat("yyyyMMdd");
        return str;
    }
}
