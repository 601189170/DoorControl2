package com.yyide.doorcontrol.identy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.base.BaseActivity;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.login.DoorAccountAppointmentFragment;
import com.yyide.doorcontrol.login.DoorCardAppointmentFragment;
import com.yyide.doorcontrol.login.DoorFaceAppointmentFragment;
import com.yyide.doorcontrol.observer.IdListener;
import com.yyide.doorcontrol.observer.IdManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预约门禁认证开门弹窗界面
 * */
public class DoorCheckAppointmentActivity  extends BaseActivity implements IdListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.btn1)
    CheckedTextView btn1;
    @BindView(R.id.btn2)
    CheckedTextView btn2;
    @BindView(R.id.btn3)
    CheckedTextView btn3;
    @BindView(R.id.btn4)
    CheckedTextView btn4;
    @BindView(R.id.close)
    TextView close;

    public static String TEACHERID = "TEACHERID";
    public static String STUDENTID = "STUDENTID";
    public static String SETTING = "SETTING";

    int toDoType;
    String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_cheack_appointment);
        ButterKnife.bind(this);
        toDoType = getIntent().getIntExtra(BaseConstant.DOSHOMTHING, -1);
        studentId = getIntent().getStringExtra(BaseConstant.STUDENTID);
//        if (savedInstanceState == null) {
//            if (BaseConstant.cardOrFace) {
//                if (PowerUtils.power(23))
//                    setTab(0);
//                else if (PowerUtils.power(24))
//                    setTab(1);
//                else if (PowerUtils.power(26))
//                    setTab(2);
//                else if (PowerUtils.power(25))
//                    setTab(3);
//            } else {
//                if (PowerUtils.power(24))
//                    setTab(1);
//                else if (PowerUtils.power(23))
//                    setTab(0);
//                else if (PowerUtils.power(26))
//                    setTab(2);
//                else if (PowerUtils.power(25))
//                    setTab(3);
//
//            }
//        }

//        btn1.setVisibility(PowerUtils.power(23) ? View.VISIBLE : View.GONE);
//        btn2.setVisibility(PowerUtils.power(24) ? View.VISIBLE : View.GONE);
//        btn3.setVisibility(PowerUtils.power(26) ? View.VISIBLE : View.GONE);
//        btn4.setVisibility(PowerUtils.power(25) ? View.VISIBLE : View.GONE);
        setTab(0);
    }

    void setTab(int position) {
        btn1.setChecked(false);
        btn2.setChecked(false);
        btn3.setChecked(false);
        btn4.setChecked(false);
        Bundle bundle = new Bundle();
        bundle.putString(SETTING, "WIFI");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fCard = fm.findFragmentByTag(String.valueOf(btn1.getId()));
        Fragment fFace = fm.findFragmentByTag(String.valueOf(btn2.getId()));
        Fragment fAccount = fm.findFragmentByTag(String.valueOf(btn3.getId()));
        Fragment fFinger = fm.findFragmentByTag(String.valueOf(btn4.getId()));
        if (fCard != null) ft.hide(fCard);
        if (fFace != null) ft.hide(fFace);
        if (fAccount != null) ft.hide(fAccount);
        if (fFinger != null) ft.hide(fFinger);
        switch (position) {
            case 0:
                if (fCard == null) {
                    fCard = new DoorCardAppointmentFragment();
                    ft.add(R.id.content, fCard, String.valueOf(btn1.getId()));
                } else
                    ft.show(fCard);
                btn1.setChecked(true);
                break;
            case 1:
                if (fFace == null) {
                    fFace = new DoorFaceAppointmentFragment();
                    ft.add(R.id.content, fFace, String.valueOf(btn2.getId()));
                } else
                    ft.show(fFace);
                btn2.setChecked(true);
                break;
            case 2:
                if (fAccount == null) {
                    fAccount = new DoorAccountAppointmentFragment();
                    fAccount.setArguments(bundle);
                    ft.add(R.id.content, fAccount, String.valueOf(btn3.getId()));
                } else
                    ft.show(fAccount);
                btn3.setChecked(true);
                break;
            case 3:
                if (fFinger == null) {
//                    fFinger = new TeacherFingerFragment();
                    ft.add(R.id.content, fFinger, String.valueOf(btn4.getId()));
                } else
                    ft.show(fFinger);
                btn4.setChecked(true);
                break;
        }


        ft.commitAllowingStateLoss();
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.close, R.id.btn4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                setTab(0);
                break;
            case R.id.btn2:
                setTab(1);
                break;
            case R.id.btn3:
                setTab(2);
                break;
            case R.id.btn4:
                setTab(3);
                break;
            case R.id.close:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IdManager.getInstance().add(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        IdManager.getInstance().remove(this);
    }

    @Override
    public void observerUpData(String id) {
        doSomeThing(id);
    }

    /*处理事件*/
    void doSomeThing(String id) {
        Intent intent;
        switch (toDoType) {
            case BaseConstant.SETTING:
//                intent = new Intent(this, SettingActivity.class);
//                startActivity(intent);
//                finish();
                break;

        }
    }



}
