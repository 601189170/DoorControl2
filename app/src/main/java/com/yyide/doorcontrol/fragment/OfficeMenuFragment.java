package com.yyide.doorcontrol.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.yyide.doorcontrol.LoginActivity;
import com.yyide.doorcontrol.LoginOfficeActivity;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SelectDoorControlSystemActivity;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.activity.SettingActivity;
import com.yyide.doorcontrol.adapter.OfficeMenuAdapter;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.base.BaseFragment;
import com.yyide.doorcontrol.dialog.EvCodeDiallog;
import com.yyide.doorcontrol.dialog.MsgDiallog;
import com.yyide.doorcontrol.dialog.UpdataDiallog;
import com.yyide.doorcontrol.hongruan.db.DbController;
import com.yyide.doorcontrol.hongruan.faceserver.FaceServer;
import com.yyide.doorcontrol.requestbean.AccessToApplyReq;
import com.yyide.doorcontrol.requestbean.GetLastVersionReq;
import com.yyide.doorcontrol.rsponbean.GetLastVersionRsp;
import com.yyide.doorcontrol.utils.LoadingTools;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 物联
 */
public class OfficeMenuFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.grid)
    GridView grid;

    OfficeMenuAdapter adapter;
    String[] list={"门禁申请","账号切换","软件更新","系统设置","退出系统"};
    ProgressDialog progressDialog = null;

    SweetAlertDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.office_menu_layout, container, false);


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter=new OfficeMenuAdapter();
        pd = new LoadingTools().pd(activity);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //门禁申请
//                        new EvCodeDiallog(activity,"").show();
                        getData();

                    break;
                    case 1:
                        //切换账号
                        ActivityUtils.finishAllActivities();
                        SPUtils.getInstance().remove(BaseConstant.LOGINNAME);
                        SPUtils.getInstance().remove(BaseConstant.PASSWORD);
                        SPUtils.getInstance().remove(SpData.LOGINDATA);
                        DbController.getInstance(activity).deleteTable();
                        FaceServer.getInstance().unInit();
                        FaceServer.getInstance().clearAllFaces(activity);
                        startActivity(new Intent(activity, LoginActivity.class));
                        break;
                    case 2:
                        //软件更新
                        update();

                        break;
                    case 3:
                        //系统设置
                        startActivity(new Intent(activity, SettingActivity.class));
                        break;
                    case 4:
                        //退出系统
                        ActivityUtils.finishAllActivities();

                        startActivity(new Intent(activity, SelectDoorControlSystemActivity.class));
                        break;
                }
            }
        });

    }

    void getData() {
        pd.show();
        AccessToApplyReq req = new AccessToApplyReq();
        req.classesSinId=SpData.User().data.signId;

        MyApp.getInstance().requestDataBend(this, req, new dateListener(), new updateError());//一德公司管理系统请求跟新
    }

    class dateListener implements Response.Listener<GetLastVersionRsp> {
        @Override
        public void onResponse(final GetLastVersionRsp rsp) {
            pd.dismiss();
            if (rsp.status == BaseConstant.REQUEST_SUCCES) {

            }
        }
    }
    /*更新*/
    void update() {
        pd.show();
        GetLastVersionReq req = new GetLastVersionReq();
        req.officeId = SpData.User().data.officeId;
        //  MyApp.getInstance().requestData(this, req, new updateListener(), new updateError()); //钦州请求跟新
        MyApp.getInstance().requestDataYydUrl(this, req, new updateListener(), new updateError());//一德公司管理系统请求跟新
    }

    class updateListener implements Response.Listener<GetLastVersionRsp> {
        @Override
        public void onResponse(final GetLastVersionRsp rsp) {
            pd.dismiss();
            if (rsp.status == BaseConstant.REQUEST_SUCCES) {
                if (rsp.data.appVersion > AppUtils.getAppVersionCode()) {
                    //是否强制更新，1=否，2=是
                    switch (rsp.data.updateType) {
                        case 1:
                            new UpdataDiallog(activity, rsp).show();
                            break;
                        case 2:
                            downApk(rsp);
                            break;
                    }
                } else new MsgDiallog(activity, "当前最新版本").show();
            }
        }
    }

    class updateError implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            pd.dismiss();
            Toast.makeText(activity, "请求异常", Toast.LENGTH_SHORT).show();
        }
    }


    private void downApk(GetLastVersionRsp rsp) {

        progressDialog = new ProgressDialog(activity);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.cancel();
            }
        });
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        File file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "apkpath.apk");
        Ion.with(this).load(rsp.data.appUrl).setTimeout(10000).progressDialog(progressDialog)
                .write(file).setCallback(new FutureCallback<File>() {
            @Override
            public void onCompleted(Exception arg0, File arg1) {

                progressDialog.cancel();
                if (arg0 != null) {
                    AlertDialog dialog = new AlertDialog.Builder(
                            activity)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("温馨提示")
                            .setMessage("安装包下载失败")
                            .setNegativeButton(
                                    "确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            dialog.cancel();
                                        }
                                    }).create();
                    dialog.getWindow()
                            .setType(
                                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    dialog.show();
                } else {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(Uri.fromFile(arg1),
                                "application/vnd.android.package-archive");
                        activity.startActivity(intent);


                }
            }
        });
        progressDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

}
