package com.yyide.doorcontrol.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.base.BaseFragment;
import com.yyide.doorcontrol.identy.DoorCheckAppointmentActivity;
import com.yyide.doorcontrol.requestbean.AppointmentHomePageInfoReq;
import com.yyide.doorcontrol.rsponbean.AppointmentHomePageInfoRsp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentHomeFragment extends BaseFragment {

    AppointmentHomePageInfoRsp rsp;

    @BindView(R.id.ll_status1)
    LinearLayout ll_status1;
    @BindView(R.id.tv_title_name)
    TextView tv_title_name;
    @BindView(R.id.tv_meeting_time)
    TextView tv_meeting_time;
    @BindView(R.id.tv_organizer)
    TextView tv_organizer;
    @BindView(R.id.tv_check_num)
    TextView tv_check_num;
    @BindView(R.id.tv_look_detail)
    TextView tv_look_detail;
    @BindView(R.id.tv_next_meeting_name)
    TextView tv_next_meeting_name;
    @BindView(R.id.tv_free_time)
    TextView tv_free_time;

    @BindView(R.id.img_open_door)
    ImageView img_open_door;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_home, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null != rsp) {
            rsp = SpData.AppointmentHomeData();
            setAllData(rsp);
        } else {
            FindHomePage();
        }

    }

    void FindHomePage() {
        AppointmentHomePageInfoReq req = new AppointmentHomePageInfoReq();
        req.signId = SpData.User().data.signId;
        MyApp.getInstance().requestData130(this, req, new Listener(), new Error());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.img_open_door)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.img_open_door:
                startActivity(new Intent(getActivity(), DoorCheckAppointmentActivity.class));
                break;


        }

    }

    class Listener implements Response.Listener<AppointmentHomePageInfoRsp> {
        @Override
        public void onResponse(AppointmentHomePageInfoRsp rsp) {
            Log.e("TAG", "AppointmentHomePageInfoRsp: " + JSON.toJSON(rsp));
            if (rsp.status == BaseConstant.REQUEST_SUCCES || rsp.status == BaseConstant.REQUEST_SUCCES2) {
                SPUtils.getInstance().put(SpData.AppointmentHome, JSON.toJSONString(rsp));
                setAllData(rsp);
            }
        }
    }


    class Error implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("TAG", "courseError2: " + error.getMessage().toString());
            FindHomePage();
        }
    }

    /**
     * 开始设置首页数据
     */
    private void setAllData(AppointmentHomePageInfoRsp rsp) {
        Log.e("TAG", "setAllData+Rsp: " + JSON.toJSON(rsp));
        if (null != rsp && null != rsp.data.homePageStatus) {
            if (rsp.data.homePageStatus.equals("1")) {
                ll_status1.setVisibility(View.VISIBLE);


            } else if (rsp.data.homePageStatus.equals("2")) {

            } else if (rsp.data.homePageStatus.equals("3")) {

            } else if (rsp.data.homePageStatus.equals("4")) {

            }


        }

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
