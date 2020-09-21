package com.yyide.doorcontrol.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class OfficeMenuAdapter extends BaseAdapter {
    int[] list={R.drawable.icon_sqkm,R.drawable.icon_qhzh,R.drawable.icon_rjgx,R.drawable.icon_xtsz,R.drawable.icon_tcxt};

//    String[] list={"门禁申请","账号切换","软件更新","系统设置","退出系统"};

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public String getItem(int position) {
        return "1";
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.office_menu_item, null, false);
        ImageView menu_img = VHUtil.ViewHolder.get(view, R.id.menu_img);
        menu_img.setBackgroundResource(list[position]);



        return view;
    }


}
