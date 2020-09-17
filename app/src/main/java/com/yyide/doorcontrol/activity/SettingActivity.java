package com.yyide.doorcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.base.BaseActivity;
import com.yyide.doorcontrol.brocast.Brocast;
import com.yyide.doorcontrol.dialog.SeirPortDiallog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主界面设置界面
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.layoutsuo)
    LinearLayout layoutsuo;
    @BindView(R.id.layout3)
    LinearLayout layout3;
    @BindView(R.id.layout4)
    LinearLayout layout4;
    @BindView(R.id.layout5)
    LinearLayout layout5;
    @BindView(R.id.msg2)
    TextView msg2;
    @BindView(R.id.layout6)
    LinearLayout layout6;
    @BindView(R.id.layout7)
    LinearLayout layout7;
    @BindView(R.id.layout8)
    LinearLayout layout8;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.ll_back, R.id.layoutsuo, R.id.layout3, R.id.layout4, R.id.layout5, R.id.layout6, R.id.layout7, R.id.layout8})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.layoutsuo:
                startActivity(new Intent(this,doorControlActivity.class));
                break;
            case R.id.layout3:
                //网络
                startActivity(new Intent(Settings.ACTION_SETTINGS));
                Brocast.showBar(this);
                break;
            case R.id.layout4:
                //声音
                startActivity(new Intent(Settings.ACTION_SOUND_SETTINGS));
                Brocast.showBar(this);
                break;
            case R.id.layout5:
                //串口
                new SeirPortDiallog(this).show();
                break;
            case R.id.layout7:
                //重启
                Brocast.reboot(this);
                break;
            case R.id.layout8:
                //关机
                Brocast.shutdown(this);
                break;
        }
    }
}
