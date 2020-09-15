package com.yyide.doorcontrol.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.yyide.doorcontrol.MyApp;


public class BaseFragment extends Fragment {

    protected AppCompatActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (AppCompatActivity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MyApp.getInstance().queue.cancelAll(this);
    }
}
