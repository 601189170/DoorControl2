package com.yyide.doorcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yyide.doorcontrol.activity.SettingActivity;
import com.yyide.doorcontrol.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择门禁内行，预约门禁，宿舍门禁，办公室门禁 ，设置功能
 */
public class SelectDoorControlSystemActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_appointment)
    TextView tv_appointment;
    @BindView(R.id.tv_hostel)
    TextView tv_hostel;
    @BindView(R.id.tv_office)
    TextView tv_office;
    @BindView(R.id.tv_setting)
    TextView tv_setting;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_door_control_system);
        ButterKnife.bind(this);
        tv_appointment.setOnClickListener(this);
        tv_hostel.setOnClickListener(this);
        tv_office.setOnClickListener(this);
        tv_setting.setOnClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_appointment:
                Toast.makeText(this, "点击预约门禁", Toast.LENGTH_SHORT).show();
                intent=new Intent(SelectDoorControlSystemActivity.this, LoginActivity.class);
                intent.putExtra("type",SpData.appointment);//预约门禁
                startActivity(intent);
                break;
            case R.id.tv_hostel:
                intent=new Intent(SelectDoorControlSystemActivity.this,LoginActivity.class);
                intent.putExtra("type","hostel");
                startActivity(intent);
                break;
            case R.id.tv_office:
                intent=new Intent(SelectDoorControlSystemActivity.this,LoginActivity.class);
                intent.putExtra("type",SpData.OfficeType);
                startActivity(intent);
                break;
            case R.id.tv_setting:
                Toast.makeText(this, "点击设置", Toast.LENGTH_SHORT).show();
                intent = new Intent(SelectDoorControlSystemActivity.this, SettingActivity.class);
                startActivity(intent);
                break;

        }

    }








}
