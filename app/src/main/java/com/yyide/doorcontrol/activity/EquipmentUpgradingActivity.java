package com.yyide.doorcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.base.BaseActivity;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.identy.IdentityEquipmentAppointmentActivity;
import com.yyide.doorcontrol.utils.Tool;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设备报修界面
 * */
public class EquipmentUpgradingActivity  extends BaseActivity {


    @BindView(R.id.bg)
    ImageView bg;
    @BindView(R.id.room)
    TextView room;
    @BindView(R.id.back_btn)
    FrameLayout backBtn;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.layout_f)
    FrameLayout layoutF;
    @BindView(R.id.index)
    TextView index;
    @BindView(R.id.post)
    TextView post;
    String TeacherID;
    @BindView(R.id.tx)
    TextView tx;
    String name;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.officename)
    TextView officename;
    @BindView(R.id.weathericon)
    ImageView weathericon;
    @BindView(R.id.weathertext)
    TextView weathertext;
    @BindView(R.id.weatherlayout)
    LinearLayout weatherlayout;
    @BindView(R.id.calendar_layout)
    LinearLayout calendarLayout;
    @BindView(R.id.wifi)
    TextView wifi;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_upgrading);
        ButterKnife.bind(this);

        index.setText("0/50");
        edit.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(50)});

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 50) {
                    index.setText(s.length() + "/50");
                }
                if (s.length() > 0) {
                    tx.setVisibility(View.GONE);
                } else {
                    tx.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(edit.getText())) {
                    name = edit.getText().toString();


                }

            }
        });
   //     initData();
    }
    InputFilter inputFilter = new InputFilter() {

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_`~!@#$^&*()=|{}':;',\\\\[\\\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");

        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Matcher matcher = pattern.matcher(charSequence);
            if (!matcher.find()) {
                return null;
            } else {
                return "";
            }

        }
    };
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @OnClick({R.id.back_btn, R.id.post})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.post:
                Tool.closeKeybord(this);
                if (TextUtils.isEmpty(edit.getText())) {
                    Toast.makeText(EquipmentUpgradingActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else{
                    Intent intent = new Intent(EquipmentUpgradingActivity.this, IdentityEquipmentAppointmentActivity.class);
                    intent.putExtra(BaseConstant.DOSHOMTHING, BaseConstant.EQE);
                    startActivity(intent);
                }
                   // PostData();
                break;
        }
    }

//    void PostData() {
//        SaveDeviceRepairReq req = new SaveDeviceRepairReq();
//        req.officeId = SpData.User().data.officeId;
//        req.repairId = TeacherID;
////        Log.e("TAG", "PostData: "+SpData.User().data );
//        req.roomId = SpData.User().data.roomId;
//        try {
//
//            req.details = URLEncoder.encode(name, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        MyApp.getInstance().requestData(this, req, new sListener(), new Error());
//    }


//    class sListener implements Response.Listener<SaveDeviceRepairRsp> {
//
//        @Override
//        public void onResponse(SaveDeviceRepairRsp rsp) {
//            Log.e("TAG", "onResponse: " + JSON.toJSON(rsp));
//
//            if (rsp.status == BaseConstant.REQUEST_SUCCES) {
//
//                if (!isDestroyed()){
//                    new EQEMsgDiallog(EqpMentActivity.this, "您的报修已提交至会议室管理员").show();
//                }
//                edit.setText("");
//
//            } else {
//                Toast.makeText(EqpMentActivity.this, rsp.info, Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    class Error implements Response.ErrorListener {
//
//        @Override
//        public void onErrorResponse(VolleyError volleyError) {
//
//        }
//    }



}
