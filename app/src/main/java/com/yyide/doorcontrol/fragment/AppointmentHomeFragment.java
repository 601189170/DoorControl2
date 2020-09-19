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
import android.widget.RelativeLayout;
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
    @BindView(R.id.img_open_door)
    ImageView img_open_door;
    AppointmentHomePageInfoRsp rsp;
    @BindView(R.id.ll_status1)
    LinearLayout ll_status1;
    @BindView(R.id.tv_now_appointment_name)
    TextView tv_now_appointment_name;
    @BindView(R.id.tv_now_appointment_time)
    TextView tv_now_appointment_time;
    @BindView(R.id.tv_organizer)
    TextView tv_organizer;
    @BindView(R.id.tv_check_num)
    TextView tv_check_num;
    @BindView(R.id.tv_look_detail)
    TextView tv_look_detail;

    @BindView(R.id.ll_status2)
    RelativeLayout llStatus2;
    @BindView(R.id.tv_next_appointment_name)
    TextView tvNextAppointmentName;
    @BindView(R.id.tv_next_appointment_time)
    TextView tvNextAppointmentTime;
    @BindView(R.id.tv_next_appointment_organizer)
    TextView tvNextAppointmentOrganizer;

    @BindView(R.id.ll_status3)
    LinearLayout llStatus3;
    @BindView(R.id.tv_galleryful)
    TextView tvGalleryful;
    @BindView(R.id.tv_device_info)
    TextView tvDeviceInfo;
    @BindView(R.id.tv_administrator)
    TextView tvAdministrator;

    @BindView(R.id.ll_status4)
    LinearLayout llStatus4;
    @BindView(R.id.tv_next_meeting_name_little)
    TextView tvNextMeetingNameLittle;
    @BindView(R.id.ll_next_meeting_free_time)
    LinearLayout llNextMeetingFreeTime;
    @BindView(R.id.ll_next_meeting)
    LinearLayout llNextMeeting;

    @BindView(R.id.tv_free_time)
    TextView tvFreeTime;



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
        FindHomePage();

    }

    void FindHomePage() {
        AppointmentHomePageInfoReq req = new AppointmentHomePageInfoReq();
        req.signId = SpData.User().data.signId;
        MyApp.getInstance().requestData(this, req, new Listener(), new Error());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

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
                llStatus2.setVisibility(View.GONE);
                llStatus3.setVisibility(View.GONE);
                llStatus4.setVisibility(View.GONE);
                llNextMeetingFreeTime.setVisibility(View.VISIBLE);
                if (null != rsp.data.meetingName) {//会议名称
                    tv_now_appointment_name.setText(rsp.data.meetingName);
                }
                if (null != rsp.data.meetTime) {//会议名称
                    tv_now_appointment_time.setText(rsp.data.meetTime);
                }
                if (null != rsp.data.zuzhizhe) {
                    tv_organizer.setText("组织者:" + rsp.data.zuzhizhe);
                }
                if (null != rsp.data.yingdao) {//应到人数
                    tv_check_num.setText("应到" + rsp.data.yingdao + "人，");
                }
                if (null != rsp.data.absenteeism) {
                    tv_look_detail.setText("缺勤" + rsp.data.absenteeism + "人");
                }
                if (null != rsp.data.nextMeetingName) {
                    tvNextMeetingNameLittle.setText(rsp.data.nextMeetingName);
                }
                if (null != rsp.data.freeTime) {
                    tvFreeTime.setText(rsp.data.freeTime);
                }


            } else if (rsp.data.homePageStatus.equals("2")) {
                ll_status1.setVisibility(View.GONE);
                llStatus2.setVisibility(View.VISIBLE);
                llStatus3.setVisibility(View.GONE);
                llStatus4.setVisibility(View.GONE);
                llNextMeeting.setVisibility(View.GONE);
                if (null != rsp.data.nextMeetingName) {
                    tvNextAppointmentName.setText(rsp.data.nextMeetingName);
                }
                if (null != rsp.data.meetTime) {
                    tvNextAppointmentTime.setText(rsp.data.meetTime);
                }
                if (null != rsp.data.zuzhizhe) {
                    tvNextAppointmentOrganizer.setText(rsp.data.zuzhizhe);
                }
                if (null != rsp.data.freeTime) {
                    tvFreeTime.setText(rsp.data.freeTime);
                }

            } else if (rsp.data.homePageStatus.equals("3")) {
                ll_status1.setVisibility(View.GONE);
                llStatus2.setVisibility(View.GONE);
                llStatus3.setVisibility(View.VISIBLE);
                llStatus4.setVisibility(View.GONE);
                llNextMeeting.setVisibility(View.GONE);

                if (null != rsp.data.capacity) {
                    tvGalleryful.setText(rsp.data.capacity);
                }
                if (null != rsp.data.deviceName) {
                    tvDeviceInfo.setText(rsp.data.deviceName);
                }
                if (null!=rsp.data.zuzhizhe){
                    tvAdministrator.setText(rsp.data.zuzhizhe);
                }
                if (null != rsp.data.freeTime) {
                    tvFreeTime.setText(rsp.data.freeTime);
                }


            } else if (rsp.data.homePageStatus.equals("4")) {
                ll_status1.setVisibility(View.GONE);
                llStatus2.setVisibility(View.GONE);
                llStatus3.setVisibility(View.GONE);
                llStatus4.setVisibility(View.VISIBLE);
                llNextMeetingFreeTime.setVisibility(View.GONE);

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



    @OnClick(R.id.img_open_door)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.img_open_door:
                Intent intent=new Intent(getActivity(),DoorCheckAppointmentActivity.class);
                intent.putExtra(BaseConstant.DOSHOMTHING, BaseConstant.Door);
                startActivity(intent);
                break;


        }

    }
}
