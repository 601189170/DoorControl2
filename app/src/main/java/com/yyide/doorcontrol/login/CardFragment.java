package com.yyide.doorcontrol.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.base.BaseFragment;
import com.yyide.doorcontrol.observer.IdManager;
import com.yyide.doorcontrol.observer.ObserverListener;
import com.yyide.doorcontrol.observer.ObserverManager;
import com.yyide.doorcontrol.requestbean.AuthenticationReq;
import com.yyide.doorcontrol.rsponbean.AuthenticationRsp;
import com.yyide.doorcontrol.utils.LoadingTools;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 *
 */
public class CardFragment extends BaseFragment implements ObserverListener {
    SweetAlertDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pd = new LoadingTools().pd(activity);
//        auth("3922293990");
        if (AppUtils.isAppDebug()){
//            auth("76627483");
//            auth("77415467");
//            auth("3921910022");
//            auth("244038465");
//            auth("244038465");
//            auth("242781425");
//            auth("3921991238");
//            auth("3921910022");
//            auth("3921910022");
//            auth("3922371462");
         //   auth("3709721529");
//            auth("3921991238");
        }
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

        auth(cardNo);
    }

    void auth(String cardNo){
        pd.show();
        AuthenticationReq req=new AuthenticationReq();
        req.officeId= SpData.User().data.officeId;
        req.cardNo=cardNo;

        MyApp.getInstance().requestYySystemData(this,req,new AuthListener(),new ErrorListener());
    }

    class AuthListener implements Response.Listener<AuthenticationRsp>{

        @Override
        public void onResponse(AuthenticationRsp response) {
            pd.dismiss();
            if (response.status == BaseConstant.REQUEST_SUCCES) {
                IdManager.getInstance().notifyObserver(response.data.id);
//                SPUtils.getInstance().put(SpData.USERCENTER, JSON.toJSONString(rsp));
            } else {
                ToastUtils.showShort(response.info);
            }
        }
    }

    class ErrorListener implements Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            pd.dismiss();
        }
    }
}
