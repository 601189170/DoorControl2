package com.yyide.doorcontrol.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;

import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.adapter.RecyleviewIndexAdapter;
import com.yyide.doorcontrol.adapter.ViewPagerFragmentAdapter;
import com.yyide.doorcontrol.base.BaseActivity;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.fragment.AppointmentHomeFragment;
import com.yyide.doorcontrol.fragment.AppointmentSettingFragment;
import com.yyide.doorcontrol.requestbean.AppointmentHomePageInfoReq;
import com.yyide.doorcontrol.requestbean.LoginReq;
import com.yyide.doorcontrol.rsponbean.AppointmentHomePageInfoRsp;
import com.yyide.doorcontrol.utils.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AppointmentMainActivity extends BaseActivity {


    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.recyleview)
    RecyclerView recyleview;
    RecyleviewIndexAdapter indexAdapter;

    public static String AppointHomeInfo = "AppointHomeInfo";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_main);
        ButterKnife.bind(this);
        indexAdapter = new RecyleviewIndexAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyleview.setLayoutManager(linearLayoutManager);

        recyleview.addItemDecoration(new SpaceItemDecoration(8));
        recyleview.setAdapter(indexAdapter);
        FindHomePage();
        pager.setOffscreenPageLimit(0);


    }

    void FindHomePage() {
        AppointmentHomePageInfoReq req = new AppointmentHomePageInfoReq();
        req.signId = SpData.User().data.signId;
        MyApp.getInstance().requestData130(this, req, new courseListener2(), new courseError2());
    }


    class courseListener2 implements Response.Listener<AppointmentHomePageInfoRsp> {
        @Override
        public void onResponse(AppointmentHomePageInfoRsp rsp) {
            Log.e("TAG", "AppointmentHomePageInfoRsp: " + JSON.toJSON(rsp));
            if (rsp.status == BaseConstant.REQUEST_SUCCES || rsp.status == BaseConstant.REQUEST_SUCCES2) {
                SPUtils.getInstance().put(SpData.AppointmentHome, JSON.toJSONString(rsp));
//                Log.e("TAG", "FindHomePageInfoRsp: " + JSON.toJSON(SpData.HomeIndexData()));
                initFragmist(rsp);
            }
        }
    }

    class courseError2 implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("TAG", "courseError2: " + error.getMessage().toString());
            FindHomePage();
        }
    }


    public void initFragmist(AppointmentHomePageInfoRsp rsp) {

        List<Fragment> mFragmentList = new ArrayList<>();
        FragmentManager fm = getSupportFragmentManager();

        int num = 1;
        indexAdapter.notifydata(num + 1);
        pager.setOffscreenPageLimit(num);

        Bundle bundle = new Bundle();
        bundle.putString(AppointHomeInfo, rsp.data.isHomePage);
        Fragment home = new AppointmentHomeFragment();
        home.setArguments(bundle);
        mFragmentList.add(home);
        Fragment menu = new AppointmentSettingFragment();//班牌最后一页所有按钮
        mFragmentList.add(menu);

        ViewPagerFragmentAdapter pagerAdapter = new ViewPagerFragmentAdapter(fm, mFragmentList);
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                indexAdapter.setPosition(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
