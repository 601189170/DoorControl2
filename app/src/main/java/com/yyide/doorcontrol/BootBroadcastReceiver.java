package com.yyide.doorcontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;




/**
 * Created by Hao on 2017/3/23.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    private final String Action="android.intent.action.BOOT_COMPLETED";


    @Override
    public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Action)) {
                Intent intent1 = new Intent(context, WelcomeActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }
    }



}
