package com.yyide.doorcontrol.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.base.BaseFragment;
import com.yyide.doorcontrol.observer.ObserverListener;
import com.yyide.doorcontrol.observer.ObserverManager;
import com.yyide.doorcontrol.requestbean.DoorControlReq;
import com.yyide.doorcontrol.rsponbean.DoorControlRsp;
import com.yyide.doorcontrol.utils.L;
import com.yyide.doorcontrol.utils.LoadingTools;


import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 *
 */
public class DoorCardFragment extends BaseFragment implements ObserverListener {

    SweetAlertDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_door_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pd = new LoadingTools().pd(activity);
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
        Identity(cardNo);
        L.d(cardNo);
    }

    void Identity(String cardNo) {
        ObserverManager.getInstance().remove(this);
        if (!activity.isDestroyed())
            pd.show();
        DoorControlReq req = new DoorControlReq();
//
//        public String officeId;
//        public String classesId;
//        public String accounts;
//        public String password;
//        public String temporaryPassword;
//        public String roomId;
        req.officeId = SpData.User().data.officeId;
//        req.classesId = SpData.User().classesId;
        req.roomId = SpData.User().data.roomId;
        MyApp.getInstance().requestDataBend(this, req, new signListenr(), new error());
    }

    class signListenr implements Response.Listener<DoorControlRsp> {

        @Override
        public void onResponse(final DoorControlRsp rsp) {
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
