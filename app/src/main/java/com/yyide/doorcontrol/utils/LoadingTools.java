package com.yyide.doorcontrol.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.KeyEvent;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Hao on 2016/10/19.
 */

public class LoadingTools {

    public SweetAlertDialog pd(Context context) {
        final SweetAlertDialog sd = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sd.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sd.setTitleText("Loading");
        sd.setCancelable(true);
        sd.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                    sd.dismiss();
                return false;
            }
        });
        return sd;
    }

}
