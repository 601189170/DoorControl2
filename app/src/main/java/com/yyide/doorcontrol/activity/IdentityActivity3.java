package com.yyide.doorcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.base.BaseActivity;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.identy.CardFragment3;
import com.yyide.doorcontrol.identy.FaceFragment3;
import com.yyide.doorcontrol.observer.IdListener;
import com.yyide.doorcontrol.observer.IdManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预约门禁——会议考勤界面
 * */
public class IdentityActivity3 extends BaseActivity implements IdListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.btn1)
    CheckedTextView btn1;
    @BindView(R.id.btn2)
    CheckedTextView btn2;
    @BindView(R.id.btn3)
    CheckedTextView btn3;
    @BindView(R.id.close)
    TextView close;
    int toDoType;
    String meetId;
    public static String MEETID = "MEETID";
    @BindView(R.id.close_layout)
    FrameLayout closeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity3);
        ButterKnife.bind(this);
        toDoType = getIntent().getIntExtra(BaseConstant.DOSHOMTHING, -1);
        meetId = getIntent().getStringExtra("meetId");
        if (savedInstanceState == null) {

                if (SpData.CheackPower("4"))
                    setTab(0);
                else if (SpData.CheackPower("3"))
                    setTab(1);
                else if (SpData.CheackPower("5"))
                    setTab(2);

        }
        btn1.setVisibility(SpData.CheackPower("4") ? View.VISIBLE : View.GONE);
        btn2.setVisibility(SpData.CheackPower("3") ? View.VISIBLE : View.GONE);
        btn3.setVisibility(SpData.CheackPower("5") ? View.VISIBLE : View.GONE);

        if (!SpData.CheackPower("5") && !SpData.CheackPower("3") && !SpData.CheackPower("4")) {
            Toast.makeText(this, "请联系管理员开通身份验证方式", Toast.LENGTH_SHORT).show();
        }
        closeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.close})
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
            case R.id.close:
                finish();
                break;
        }
    }

    void setTab(int position) {
        if (position == 1) {
            SPUtils.getInstance().put(SpData.IDENTYFACE, true);
        } else {
            SPUtils.getInstance().put(SpData.IDENTYFACE, false);
        }
        btn1.setChecked(false);
        btn2.setChecked(false);
        btn3.setChecked(false);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fCard = fm.findFragmentByTag(String.valueOf(btn1.getId()));
        Fragment fFace = fm.findFragmentByTag(String.valueOf(btn2.getId()));
        Fragment fAccount = fm.findFragmentByTag(String.valueOf(btn3.getId()));
        if (fCard != null) ft.hide(fCard);
        if (fFace != null) ft.hide(fFace);
        if (fAccount != null) ft.hide(fAccount);
        Bundle bundle = new Bundle();
        bundle.putString(MEETID, meetId);
        switch (position) {
            case 0:
                if (fCard == null) {
                    fCard = new CardFragment3();
                    fCard.setArguments(bundle);
                    Log.e("TAG", "setArguments: " + JSON.toJSON(bundle));
                    ft.add(R.id.content, fCard, String.valueOf(btn1.getId()));
                } else
                    ft.show(fCard);
                btn1.setChecked(true);

                break;
            case 1:
                if (fFace == null) {
                    fFace = new FaceFragment3();
                    fFace.setArguments(bundle);
                    ft.add(R.id.content, fFace, String.valueOf(btn2.getId()));
                } else
                    ft.show(fFace);
                btn2.setChecked(true);
                break;
            case 2:
                if (fAccount == null) {
                //    fAccount = new FingerFragment3();
                    fAccount.setArguments(bundle);
                    ft.add(R.id.content, fAccount, String.valueOf(btn3.getId()));
                } else
                    ft.show(fAccount);
                btn3.setChecked(true);
                break;
        }
        ft.commitAllowingStateLoss();
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
        switch (toDoType) {
            /*学生空间*/
                //break;


        }
    }
}
