package com.yyide.doorcontrol.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.koushikdutta.ion.Ion;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.rsponbean.GetLastVersionRsp;


import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Hao on 2017/11/29.
 */

public class UpdataDiallog extends Dialog implements View.OnClickListener {

    Context context;
    String str="是否更新到最新版本？";
    TextView msg,yes,cancel,msg2;
    SweetAlertDialog pd;
    ProgressDialog progressDialog = null;
    GetLastVersionRsp rsp;

    public UpdataDiallog(@NonNull Context context, int theme) {
        super(context, theme);
    }


    public UpdataDiallog(@NonNull Context context, GetLastVersionRsp rsp) {
        this(context, R.style.shareDialog);
        this.context = context;
        this.rsp=rsp;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_msg_layout2);
        msg=findViewById(R.id.msg);
        msg2=findViewById(R.id.msg2);
        yes=findViewById(R.id.yes);
        cancel=findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        yes.setOnClickListener(this);
        msg.setText(str);
        msg2.setText("当前版本V"+ AppUtils.getAppVersionName());



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yes:
                downApk(rsp);

                cancel();
                break;
            case R.id.cancel:
                cancel();
                break;

        }
    }
    private void downApk(GetLastVersionRsp rsp) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.cancel();
            }
        });
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "apkpath.apk");
        Ion.with(context).load(rsp.data.appUrl).setTimeout(10000).progressDialog(progressDialog)
                .write(file).setCallback(new com.koushikdutta.async.future.FutureCallback<File>() {
            @Override
            public void onCompleted(Exception arg0, File arg1) {

                progressDialog.cancel();
                if (arg0 != null) {
                    AlertDialog dialog = new AlertDialog.Builder(
                            context)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("温馨提示")
                            .setMessage("安装包下载失败")
                            .setNegativeButton(
                                    "确定",
                                    new OnClickListener() {
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
                        context.startActivity(intent);


                }
            }
        });
        progressDialog.show();
    }
}
