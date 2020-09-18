package com.yyide.doorcontrol.login;

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
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.base.BaseFragment;
import com.yyide.doorcontrol.observer.IdManager;
import com.yyide.doorcontrol.requestbean.AppointmentOpenDoorReq;
import com.yyide.doorcontrol.rsponbean.AppointmentOpenDoorRsp;
import com.yyide.doorcontrol.utils.LoadingTools;

import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DoorAccountAppointmentFragment extends BaseFragment {

    @BindView(R.id.userName)
    EditText userName;
    @BindView(R.id.userPwd)
    EditText userPwd;
    @BindView(R.id.tips)
    TextView tips;
    @BindView(R.id.config)
    TextView config;

    SweetAlertDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_door_account_appointment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pd = new LoadingTools().pd(activity);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.config)
    public void onViewClicked() {
        String loginName = userName.getText().toString().trim();
        String passw = userPwd.getText().toString().trim();
        if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(passw)) {
            tips.setText("请确认账号密码输入正确！");
            tips.setVisibility(View.VISIBLE);
            return;
        } else
            Identity();
    }

    void Identity() {
        if (!activity.isDestroyed())
            pd.show();
        AppointmentOpenDoorReq req=new AppointmentOpenDoorReq();
        req.officeId = SpData.User().data.officeId;
        req.roomId = SpData.User().data.roomId;
        req.accounts = URLEncoder.encode(userName.getText().toString());
        req.password = userPwd.getText().toString();
        MyApp.getInstance().requestData130(this, req, new signListenr(), new error());

    }


    class signListenr implements Response.Listener<AppointmentOpenDoorRsp> {
        @Override
        public void onResponse(final AppointmentOpenDoorRsp rsp) {
            Log.e("json", "kaimenren" + JSON.toJSONString(rsp));
            if (!activity.isDestroyed())
                pd.dismiss();

//            if (rsp == BaseConstant.REQUEST_SUCCES) {
//                        IdManager.getInstance().notifyObserver(rsp.data.id);
//            } else
//                ToastUtils.showShort(rsp.info);
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

}

