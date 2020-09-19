package com.yyide.doorcontrol.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.yyide.doorcontrol.R;


/**
 * Created by Hao on 2017/11/29.
 */

public class EQEMsgDiallog extends Dialog {

    Activity context;


    String str = "设置串口成功,请重新启动";
    TextView msg, cancel;
    Handler handler;

    Runnable runnable;
    public EQEMsgDiallog(@NonNull Context context, int theme) {
        super(context, theme);
    }


    public EQEMsgDiallog(@NonNull final Activity context, String str) {
        this(context, R.style.shareDialog);
        this.context = context;
        this.str = str;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (context!=null){
                        cancel();
                    }
                }catch (Exception e){

                }




            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_layout2);
        msg = findViewById(R.id.msg);
        cancel = findViewById(R.id.cancel);
        msg.setText(str);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                cancel();
                if (context!=null){
                    context.finish();
                }

            }
        });

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 3000);
    }

}
