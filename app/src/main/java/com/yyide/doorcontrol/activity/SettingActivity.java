package com.yyide.doorcontrol.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主界面设置界面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_back)
    LinearLayout ll_back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        ll_back.setOnClickListener(this);

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
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;


        }

    }
}
