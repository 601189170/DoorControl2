package com.yyide.doorcontrol.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.rsponbean.GetTodayReservationListRsp;
import com.yyide.doorcontrol.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;


public class MTListAdapter extends BaseAdapter {
    public List<GetTodayReservationListRsp.DataBean> list=new ArrayList<>();
    public int type;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GetTodayReservationListRsp.DataBean getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_mtlist_item, null, false);
        TextView tv1 = VHUtil.ViewHolder.get(view, R.id.tv1);
        TextView tv2 = VHUtil.ViewHolder.get(view, R.id.tv2);
        TextView tv3 = VHUtil.ViewHolder.get(view, R.id.tv3);
        TextView tv4 = VHUtil.ViewHolder.get(view, R.id.tv4);
        tv1.setText(getItem(i).name);
        StringBuffer buffer=new StringBuffer();
        for (int i1 = 0; i1 < getItem(i).classesName.size(); i1++) {
            if (i1==getItem(i).classesName.size()-1){
                buffer.append(getItem(i).classesName.get(i1));
            }else {
                buffer.append(getItem(i).classesName.get(i1)+",");
            }

        }

        tv2.setText(buffer);
//        if (getItem(i).meetType.equals("会议")){
//            tv3.setText(getItem(i).startDate);
//
//        }else {
//            tv3.setText(getItem(i).section);
//
//        }
        if (TextUtils.isEmpty(getItem(i).section)){
            tv3.setText(getItem(i).startDate);
        }else {
            tv3.setText(getItem(i).section);
        }
        tv4.setText(getItem(i).teacherName);
     if (type==1){
         tv2.setVisibility(View.GONE);
     }else {
         tv2.setVisibility(View.VISIBLE);
     }
//            tv2.setVisibility(View.GONE);

     tv3.setText(getItem(i).startDate+"-"+getItem(i).endDate);
        return view;
    }

    public void notfyData(List<GetTodayReservationListRsp.DataBean> list,int type){
            this.list=list;
            this.type=type;
            notifyDataSetChanged();
    }
}
