package com.yyide.doorcontrol.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.doorcontrol.LoginActivity;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.activity.EquipmentUpgradingActivity;
import com.yyide.doorcontrol.activity.RoomInformationActivity;
import com.yyide.doorcontrol.activity.TodayAppointmentActivity;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.base.BaseFragment;
import com.yyide.doorcontrol.dialog.EvCodeDiallog;
import com.yyide.doorcontrol.hongruan.db.DbController;
import com.yyide.doorcontrol.hongruan.faceserver.FaceServer;
import com.yyide.doorcontrol.identy.AppointmentSettingCheckActivity;
import com.yyide.doorcontrol.requestbean.AppointmentAccessToApplyReq;
import com.yyide.doorcontrol.rsponbean.AppointmentAccessToApplyRsp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 预约门禁meun界面
 */
public class AppointmentSettingFragment extends BaseFragment {


    @BindView(R.id.tv_access_control)
    TextView tvAccessControl;
    @BindView(R.id.tv_today_appointment)
    TextView tvTodayAppointment;
    @BindView(R.id.tv_room_information)
    TextView tvRoomInformation;
    @BindView(R.id.tv_equipment_upgrading)
    TextView tvEquipmentUpgrading;
    @BindView(R.id.tv_switch_account)
    TextView tvSwitchAccount;
    @BindView(R.id.tv_update)
    TextView tvUpdate;
    @BindView(R.id.tv_system_setting)
    TextView tvSystemSetting;
    @BindView(R.id.tv_out_system)
    TextView tvOutSystem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_setting, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    @OnClick({R.id.tv_access_control, R.id.tv_today_appointment, R.id.tv_room_information, R.id.tv_equipment_upgrading,
            R.id.tv_switch_account, R.id.tv_update, R.id.tv_system_setting, R.id.tv_out_system})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_access_control:
                Toast.makeText(getActivity(), "申请门禁被点击", Toast.LENGTH_SHORT).show();
                getData();

                break;
            case R.id.tv_today_appointment:
                intent = new Intent(getActivity(), TodayAppointmentActivity.class);
                startActivity(intent);

                break;
            case R.id.tv_room_information:
                intent = new Intent(getActivity(), RoomInformationActivity.class);
                startActivity(intent);

                break;
            case R.id.tv_equipment_upgrading:
                intent = new Intent(getActivity(), EquipmentUpgradingActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_switch_account:


                break;
            case R.id.tv_update:
                break;
            case R.id.tv_system_setting:
                //系统设置
                intent = new Intent(getActivity(), AppointmentSettingCheckActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_out_system:

                break;
        }
    }


    void getData() {
        AppointmentAccessToApplyReq req=new AppointmentAccessToApplyReq();
        req.classesSinId=SpData.User().data.classSignId;
        MyApp.getInstance().requestData(this, req, new dateListener(), new updateError());//一德公司管理系统请求跟新
    }

    class dateListener implements Response.Listener<AppointmentAccessToApplyRsp> {
        @Override
        public void onResponse(final AppointmentAccessToApplyRsp rsp) {
            Log.e("TAG","预约门禁申请"+ JSON.toJSONString(rsp));
            if (rsp.status == BaseConstant.REQUEST_SUCCES||rsp.status==BaseConstant.REQUEST_SUCCES2) {
                new EvCodeDiallog(activity,rsp.data).show();
            }
        }
    }
    class updateError implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(activity, "请求异常", Toast.LENGTH_SHORT).show();
        }
    }
}
