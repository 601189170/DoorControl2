package com.yyide.doorcontrol.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.rsponbean.SaveMeetAttendanceRsp;
import com.yyide.doorcontrol.utils.GlideUtil;


/**
 * Created by Hao on 2018/4/26.
 */

public class AttendanceDialog extends Dialog {

    Context context;

    SaveMeetAttendanceRsp bean;

    ImageView head;

    TextView name, state, time;

    Handler handler;

    Runnable runnable;

    public AttendanceDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }


    public AttendanceDialog(@NonNull final Context context, SaveMeetAttendanceRsp bean) {
        this(context, R.style.shareDialog);
        this.context = context;
        this.bean = bean;
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
        setContentView(R.layout.layout_attendance);
        head = findViewById(R.id.head);
        name = findViewById(R.id.name);
        time = findViewById(R.id.time);
        state = findViewById(R.id.state);
        setData(bean);
    }

    public void setData(SaveMeetAttendanceRsp bean) {
        GlideUtil.loadCircleImage(context, bean.data.photo, head);
        name.setText(bean.data.name);
        state.setText(bean.info);

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 4000);
    }

    public void changeData(SaveMeetAttendanceRsp bean) {
        setData(bean);
        show();
    }
}
