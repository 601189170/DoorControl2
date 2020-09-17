package com.yyide.doorcontrol.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.doorcontrol.LoginActivity;
import com.yyide.doorcontrol.MainActivity;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.WelcomeActivity;
import com.yyide.doorcontrol.activity.EquipmentUpgradingActivity;
import com.yyide.doorcontrol.activity.RoomInformationActivity;
import com.yyide.doorcontrol.activity.SettingActivity;
import com.yyide.doorcontrol.activity.TodayAppointmentActivity;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.base.BaseFragment;
import com.yyide.doorcontrol.hongruan.db.DbController;
import com.yyide.doorcontrol.hongruan.faceserver.FaceServer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
                Toast.makeText(getActivity(),"申请门禁被点击",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_today_appointment:
                intent =new Intent(getActivity(), TodayAppointmentActivity.class);
                startActivity(intent);

                break;
            case R.id.tv_room_information:

                intent =new Intent(getActivity(), RoomInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_equipment_upgrading:
                intent =new Intent(getActivity(), EquipmentUpgradingActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_switch_account:
                ActivityUtils.finishAllActivities();
                SPUtils.getInstance().remove(BaseConstant.LOGINNAME);
                SPUtils.getInstance().remove(BaseConstant.PASSWORD);
                SPUtils.getInstance().remove(SpData.LOGINDATA);
                SPUtils.getInstance().remove(SpData.AppointmentHome);
                //SPUtils.getInstance().remove(BaseConstant.loginSuccess);

                if (DbController.getInstance(getActivity()).searchAll().size()>0){
                    DbController.getInstance(getActivity()).deleteTable();
                }
                FaceServer.getInstance().clearAllFaces(getActivity());
               // startActivity(new Intent(this, LoginActivity.class));

                break;
            case R.id.tv_update:
                break;
            case R.id.tv_system_setting:

                break;
            case R.id.tv_out_system:
                break;
        }
    }
}
