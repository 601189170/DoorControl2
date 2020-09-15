package com.yyide.doorcontrol.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.yyide.doorcontrol.R;


/**
 * Created by Hao on 2017/11/17.
 */

public class LockDialog extends Dialog {

    Context context;

    public LockDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }


    public LockDialog(@NonNull Context context) {
        this(context, R.style.shareDialog);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lock_dialog);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        setCanceledOnTouchOutside(false);
        dialogWindow.setAttributes(lp);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    // 禁掉键盘事件
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return true;
    }

}
