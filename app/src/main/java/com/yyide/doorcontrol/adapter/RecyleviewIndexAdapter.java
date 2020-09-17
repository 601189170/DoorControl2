package com.yyide.doorcontrol.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.yyide.doorcontrol.R;


/**
 * Created by Administrator on 2018/6/15.
 */

public class RecyleviewIndexAdapter extends RecyclerView.Adapter<RecyleviewIndexAdapter.ViewHolder> {

    public int index;

    int ct;


    public String getItem(int i) {
        return "0";
    }

    @Override
    public RecyleviewIndexAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_index_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.item = view.findViewById(R.id.item);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyleviewIndexAdapter.ViewHolder viewHolder, final int i) {

        if (i==index){
            viewHolder.item.setChecked(true);
        }else {
            viewHolder.item.setChecked(false);
        }



    }

    @Override
    public int getItemCount() {
        return ct;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View arg0) {
            super(arg0);
        }


        CheckedTextView item;


    }
    public void notifydata(int ct){
        this.ct=ct;
        notifyDataSetChanged();
    }
    public void setPosition(int index){
        this.index=index;
        notifyDataSetChanged();
    }
}
