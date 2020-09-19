package com.yyide.doorcontrol.identy;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.activity.EquipmentUpgradingActivity;
import com.yyide.doorcontrol.activity.SettingActivity;
import com.yyide.doorcontrol.base.BaseActivity;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.brocast.Brocast;
import com.yyide.doorcontrol.dialog.EQEMsgDiallog;
import com.yyide.doorcontrol.login.DoorCardAppointmentFragment;
import com.yyide.doorcontrol.login.FaceEquitmentAppointmentFragment;
import com.yyide.doorcontrol.observer.IdListener;
import com.yyide.doorcontrol.observer.IdManager;
import com.yyide.doorcontrol.requestbean.SaveDeviceRepairReq;
import com.yyide.doorcontrol.rsponbean.SaveDeviceRepairRsp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IdentityEquipmentAppointmentActivity extends BaseActivity implements IdListener {

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
    public static String SETTING = "SETTING";
    @BindView(R.id.close_layout)
    FrameLayout closeLayout;
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);
        ButterKnife.bind(this);
        name = getIntent().getStringExtra("content");
        if (savedInstanceState == null) {

//                if (SpData.CheackPower("4"))
//                    setTab(0);
//                else if (SpData.CheackPower("3"))
//                    setTab(1);
//                else if (SpData.CheackPower("5"))
//                    setTab(2);
            setTab(0);
        }
        toDoType = getIntent().getIntExtra(BaseConstant.DOSHOMTHING, -1);
//        btn1.setVisibility(SpData.CheackPower("4") ? View.VISIBLE : View.GONE);
//        btn2.setVisibility(SpData.CheackPower("3") ? View.VISIBLE : View.GONE);
//        btn3.setVisibility(SpData.CheackPower("5") ? View.VISIBLE : View.GONE);

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

        switch (position) {
            case 0:
                if (fCard == null) {
                    fCard = new CardEquitmentAppointmentFragment();
                    ft.add(R.id.content, fCard, String.valueOf(btn1.getId()));
                } else
                    ft.show(fCard);
                btn1.setChecked(true);
                break;
            case 1:
                if (fFace == null) {
                    fFace = new FaceEquitmentAppointmentFragment();
                    ft.add(R.id.content, fFace, String.valueOf(btn2.getId()));
                } else
                    ft.show(fFace);
                btn2.setChecked(true);
                break;

            case 2:
                if (fAccount == null) {
                    //    fAccount = new FingerFragment();
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
//            case BaseConstant.MYOD:
//                Log.e("TAG", "BaseConstant.MYOD: " + id);
//                Intent intent = new Intent(this, MyOdActivity.class);
//                intent.putExtra(MyOdActivity.TEACHERID, id);
//                startActivity(intent);
//                finish();
//                break;
//            case BaseConstant.TCOD:
//                Intent intent1 = new Intent(this, TeacherOdActivity1.class);
//                intent1.putExtra(TeacherOdActivity.TEACHERID, id);
//                startActivity(intent1);
//                finish();
//                break;
//            case BaseConstant.MEETOD:
//                Intent intent2 = new Intent(this, AddMeetActivityStep1.class);
//                intent2.putExtra(AddMeetActivityStep1.TEACHERID, id);
//                startActivity(intent2);
//                finish();
//                break;
            case BaseConstant.EQE:
                PostData(id);
                break;
//            case BaseConstant.WIFI:
//                startActivity(new Intent(Settings.ACTION_SETTINGS));
//                Brocast.showBar(this);
//                finish();
//                break;
//            case BaseConstant.SETTING:
//                Intent intent4 = new Intent(this, SettingActivity.class);
//                intent4.putExtra(SettingActivity.TEACHERID, id);
//                startActivity(intent4);
//                finish();
//                break;
        }
    }


    void PostData(String id) {
        SaveDeviceRepairReq req = new SaveDeviceRepairReq();
        req.officeId = SpData.User().data.officeId;
        req.repairId = id;
//        Log.e("TAG", "PostData: "+SpData.User().data );
        req.roomId = SpData.User().data.roomId;
        try {

            req.details = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MyApp.getInstance().requestYySystemData(this, req, new sListener(), new Error());
    }


    class sListener implements Response.Listener<SaveDeviceRepairRsp> {

        @Override
        public void onResponse(SaveDeviceRepairRsp rsp) {
            Log.e("TAG", "onResponse: " + JSON.toJSON(rsp));

            if (rsp.status == BaseConstant.REQUEST_SUCCES) {

                if (!isDestroyed()) {
                    new EQEMsgDiallog(IdentityEquipmentAppointmentActivity.this, "您的报修已提交至会议室管理员").show();
                }


            } else {
                Toast.makeText(IdentityEquipmentAppointmentActivity.this, rsp.info, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class Error implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }


}
