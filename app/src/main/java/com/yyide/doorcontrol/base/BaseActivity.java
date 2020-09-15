package com.yyide.doorcontrol.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.yyide.doorcontrol.utils.ActivityContrl;


/**
 * Created by Hao on 2017/10/18.
 * 继承BaseActivity的Activity，不要重复添加友盟统计
 */
public class BaseActivity extends AppCompatActivity {
    public boolean isshow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ActivityContrl.add(this);
//        powerManager = (PowerManager)this.getSystemService(this.POWER_SERVICE);
//        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");

    }








}
