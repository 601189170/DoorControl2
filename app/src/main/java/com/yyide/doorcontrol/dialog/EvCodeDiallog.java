package com.yyide.doorcontrol.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.utils.GlideUtil;


/**
 * Created by Hao on 2017/11/29.
 */

public class EvCodeDiallog extends Dialog {

    Context context;

    String str="";
    ImageView img;

    public EvCodeDiallog(@NonNull Context context, int theme) {
        super(context, theme);
    }


    public EvCodeDiallog(@NonNull Context context, String str) {
        this(context, R.style.shareDialog);
        this.context = context;
        this.str = str;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evcode_layout);
        img=findViewById(R.id.img);
        GlideUtil.loadImage(context, str, img);


    }

}
