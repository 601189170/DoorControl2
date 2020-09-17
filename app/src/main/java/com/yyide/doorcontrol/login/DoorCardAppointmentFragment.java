package com.yyide.doorcontrol.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.base.BaseFragment;
import com.yyide.doorcontrol.observer.ObserverListener;
import com.yyide.doorcontrol.observer.ObserverManager;
import com.yyide.doorcontrol.requestbean.AppointmentOpenDoorReq;
import com.yyide.doorcontrol.rsponbean.AppointmentOpenDoorRsp;
import com.yyide.doorcontrol.utils.LoadingTools;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 预约刷卡认证
 */
public class DoorCardAppointmentFragment extends BaseFragment implements ObserverListener {

    SweetAlertDialog pd;

    String cardNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_door_card, container, false);
        cardNo = "456123";
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pd = new LoadingTools().pd(activity);
       // Identity(cardNo);
    }


    @Override
    public void onResume() {
        super.onResume();
        ObserverManager.getInstance().add(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ObserverManager.getInstance().remove(this);
    }

    @Override
    public void observerUpData(String cardNo) {
        Log.e("json", cardNo);
        Identity(cardNo);

    }

    void Identity(String cardNo) {
        AppointmentOpenDoorReq req=new AppointmentOpenDoorReq();
        req.cardNo = "456123";
        req.officeId = SpData.User().data.officeId;
        req.roomId = SpData.User().data.roomId;
        MyApp.getInstance().requestData130(this, req, new signListenr(), new error());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    class signListenr implements Response.Listener<AppointmentOpenDoorRsp> {

        @Override
        public void onResponse(final AppointmentOpenDoorRsp rsp) {
            Log.e("json", "kaimenren" + JSON.toJSONString(rsp));
            if (!activity.isDestroyed())
                pd.dismiss();

//            if (rsp == BaseConstant.REQUEST_SUCCES) {
//                        IdManager.getInstance().notifyObserver(rsp.data.id);
//            } else
//                ToastUtils.showShort(rsp.info);
        }
    }

    class error implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (!activity.isDestroyed())
                pd.dismiss();

            ToastUtils.showShort("请求失败，请重试");
        }
    }

}
