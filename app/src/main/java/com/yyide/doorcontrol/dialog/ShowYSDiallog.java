package com.yyide.doorcontrol.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;


import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.brocast.Brocast;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Hao on 2017/11/29.
 */

public class ShowYSDiallog extends Dialog implements View.OnClickListener {

    Context context;

    String str = "确定解除当前门禁？";

    TextView msg, yes, cancel;

    public ShowYSDiallog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    public ShowYSDiallog(@NonNull Context context) {
        this(context, R.style.shareDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_msg_layout);
        msg = findViewById(R.id.msg);
        yes = findViewById(R.id.yes);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        yes.setOnClickListener(this);
        msg.setText(str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes:
                Brocast.reset(context);
                cancel();

                break;
            case R.id.cancel:
                cancel();
                break;
        }
    }

}
