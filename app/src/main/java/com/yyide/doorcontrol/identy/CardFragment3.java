package com.yyide.doorcontrol.identy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.yyide.doorcontrol.activity.IdentityActivity3;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.base.BaseFragment;
import com.yyide.doorcontrol.dialog.AttendanceDialog;
import com.yyide.doorcontrol.observer.ObserverListener;
import com.yyide.doorcontrol.observer.ObserverManager;
import com.yyide.doorcontrol.requestbean.SaveMeetAttendanceReq;
import com.yyide.doorcontrol.rsponbean.SaveMeetAttendanceRsp;
import com.yyide.doorcontrol.utils.LoadingTools;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 *会议考勤刷卡
 */
public class CardFragment3 extends BaseFragment implements ObserverListener {
    SweetAlertDialog pd;
    String meetId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pd = new LoadingTools().pd(activity);
        meetId = getArguments().getString(IdentityActivity3.MEETID);
        Log.e("TAG", "CardFragment3: "+meetId );

        if (AppUtils.isAppDebug()){
//            auth("3921910022");
//            auth("3709721529");
            auth("244200481");
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
        SaveMeetAttendanceReq req=new SaveMeetAttendanceReq();
        req.officeId= SpData.User().data.officeId;
        req.cardNo=cardNo;
        req.meetingId=meetId;
        MyApp.getInstance().requestYySystemData(this,req,new AuthListener(),new ErrorListener());
    }

    class AuthListener implements Response.Listener<SaveMeetAttendanceRsp>{

        @Override
        public void onResponse(SaveMeetAttendanceRsp response) {
            pd.dismiss();
            if (response.status == BaseConstant.REQUEST_SUCCES) {
                new AttendanceDialog(activity,response).show();

//                IdManager.getInstance().notifyObserver("");
//                SPUtils.getInstance().put(SpData.USERCENTER, JSON.toJSONString(rsp));
            } else {
                ToastUtils.showShort(response.info);
            }
        }
    }

    class ErrorListener implements Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            ToastUtils.showShort("打卡失败");
            pd.dismiss();
        }
    }
}
