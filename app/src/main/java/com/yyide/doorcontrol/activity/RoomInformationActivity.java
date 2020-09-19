package com.yyide.doorcontrol.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.base.BaseActivity;
import com.yyide.doorcontrol.rsponbean.AppointmentHomePageInfoRsp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 *
 */
public class RoomInformationActivity extends BaseActivity {


    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_galleryful)
    TextView tvGalleryful;
    @BindView(R.id.tv_device_info)
    TextView tvDeviceInfo;
    @BindView(R.id.tv_administrator)
    TextView tvAdministrator;
    AppointmentHomePageInfoRsp bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_information);
        ButterKnife.bind(this);

        bean=   SpData.AppointmentHomeData();

        if (null != bean.data.capacity) {
            tvGalleryful.setText(bean.data.capacity);
        }
        if (null != bean.data.deviceName) {
            tvDeviceInfo.setText(bean.data.deviceName);
        }
        if (null!=bean.data.zuzhizhe){
            tvAdministrator.setText(bean.data.zuzhizhe+"      "+bean.data.phone);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick(R.id.ll_back)
    public void onViewClicked(View view) {
        switch ( view.getId()){
            case R.id.ll_back:
                finish();
                break;




        }


    }
}
