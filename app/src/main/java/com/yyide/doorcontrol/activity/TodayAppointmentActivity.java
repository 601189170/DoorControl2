package com.yyide.doorcontrol.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.adapter.MTListAdapter;
import com.yyide.doorcontrol.base.BaseActivity;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.mina.MinaEventType;
import com.yyide.doorcontrol.requestbean.GetTodayReservationListReq;
import com.yyide.doorcontrol.rsponbean.GetTodayReservationListRsp;
import com.yyide.doorcontrol.view.MultiStateView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 今日预约界面
 */
public class TodayAppointmentActivity extends BaseActivity {


    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.list)
    ListView list;

    MTListAdapter adapter;
    @BindView(R.id.tx1)
    TextView tx1;
    @BindView(R.id.tx2)
    TextView tx2;
    @BindView(R.id.tx3)
    TextView tx3;
    @BindView(R.id.tx4)
    TextView tx4;

    @BindView(R.id.mu)
    MultiStateView mu;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_appointment);
        ButterKnife.bind(this);
        adapter = new MTListAdapter();
        list.setAdapter(adapter);
        mu.findViewById(R.id.retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
        EventBus.getDefault().register(this);
        handler.postDelayed(runnable, 6000 * 10 * 60 * 5);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MinaEventType messageEvent) {
        if (messageEvent.i == 10) {
            Log.e("TAG", "Event:收到推送" + messageEvent.i);
            getData();
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getData();
            handler.postDelayed(runnable, 6000 * 10 * 60 * 5);
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        handler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @OnClick(R.id.ll_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    void getData() {
        mu.setViewState(MultiStateView.VIEW_STATE_LOADING);
        GetTodayReservationListReq req = new GetTodayReservationListReq();
        req.officeId = SpData.User().data.officeId;
        req.roomId = "";

        MyApp.getInstance().requestData130(this, req, new sListener(), new Error());
    }


    class sListener implements Response.Listener<GetTodayReservationListRsp> {

        @Override
        public void onResponse(GetTodayReservationListRsp rsp) {

            if (rsp.status == BaseConstant.REQUEST_SUCCES) {
                Log.e("TAG", "rererererere: " + JSON.toJSONString(rsp));
                SPUtils.getInstance().put(SpData.ReservationList, JSON.toJSONString(rsp));
                int type = 0;

                if (rsp.data.size() > 0) {
                    type = setData(rsp);
                    mu.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                } else {
                    mu.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                }
                adapter.list.clear();
                Log.e("TAG", "sListener: " + type);
                adapter.notfyData(rsp.data, type);


            } else {
                mu.setViewState(MultiStateView.VIEW_STATE_ERROR);
                Toast.makeText(TodayAppointmentActivity.this, rsp.info, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class Error implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            mu.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    int setData(GetTodayReservationListRsp rsp) {
        //会议
        List<String> list1 = new ArrayList<>();
        //教学
        List<String> list2 = new ArrayList<>();


        int type = 0;

        for (GetTodayReservationListRsp.DataBean datum : rsp.data) {
            if (datum.meetType.equals("会议")) {
                list1.add(datum.name);
            } else {
                list2.add(datum.name);
            }
        }
        if (list1.size() > 0 && list2.size() > 0) {
            title.setText("今日预约列表");
            tx1.setText("预约主题");
            tx2.setVisibility(View.VISIBLE);
            tx3.setVisibility(View.VISIBLE);
            tx3.setText("时间");
            tx4.setText("组织者");
            type = 3;


        } else if (list1.size() > 0 && list2.size() == 0) {
            title.setText("今日会议列表");
            tx1.setText("会议主题");
            tx2.setVisibility(View.GONE);
            tx3.setVisibility(View.VISIBLE);
            tx3.setText("时间");
            tx4.setText("组织者");
            type = 1;

        } else if (list1.size() == 0 && list2.size() > 0) {
            title.setText("今日教学列表");
            tx1.setText("教学主题");
            tx2.setVisibility(View.VISIBLE);
            tx3.setVisibility(View.VISIBLE);
            tx3.setText("节次");
            tx4.setText("组织者");
            type = 2;

        }
        Log.e("TAG", "list1: " + JSON.toJSON(list1));
        Log.e("TAG", "list2: " + JSON.toJSON(list2));
        return type;

    }
}