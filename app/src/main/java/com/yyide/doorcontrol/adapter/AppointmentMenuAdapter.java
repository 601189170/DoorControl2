package com.yyide.doorcontrol.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.utils.VHUtil;

public class AppointmentMenuAdapter extends BaseAdapter {

    String[] list={"门禁申请","今日预约","房间信息","设备报修","账号切换","软件更新","系统设置","退出系统"};

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public String getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.appointment_menu_item, null, false);
        ImageView menu_img = VHUtil.ViewHolder.get(view, R.id.menu_img);
        TextView menu_name = VHUtil.ViewHolder.get(view, R.id.menu_name);
        menu_name.setText(getItem(position));


        return view;
    }


}

