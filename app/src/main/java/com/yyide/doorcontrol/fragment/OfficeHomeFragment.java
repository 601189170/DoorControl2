package com.yyide.doorcontrol.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.base.BaseFragment;
import com.yyide.doorcontrol.identy.DoorCheackActivity;
import com.yyide.doorcontrol.requestbean.OfficeAccesshomeReq;
import com.yyide.doorcontrol.rsponbean.OfficeAccesshomeRsp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 物联
 */
public class OfficeHomeFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.door)
    TextView door;
    @BindView(R.id.room)
    TextView room;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.office_home_layout, container, false);


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getData();

    }

    void getData() {

        OfficeAccesshomeReq req = new OfficeAccesshomeReq();
        req.roomId = SpData.User().data.roomId;
        MyApp.getInstance().requestDataBend(this, req, new dateListener(), new error());
    }

    class dateListener implements Response.Listener<OfficeAccesshomeRsp> {
        @Override
        public void onResponse(final OfficeAccesshomeRsp rsp) {

            if (rsp.status == BaseConstant.REQUEST_SUCCES2) {
                room.setText(rsp.data.name);
            }
        }
    }

    class error implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {


        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @OnClick(R.id.door)
    public void onViewClicked() {
        Intent intent=new Intent();
        intent.setClass(activity,DoorCheackActivity.class);
        intent.putExtra(BaseConstant.DOSHOMTHING, BaseConstant.Door);
        startActivity(intent);
    }
}
