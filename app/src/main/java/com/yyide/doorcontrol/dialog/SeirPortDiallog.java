package com.yyide.doorcontrol.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.utils.VHUtil;


/**
 * Created by Hao on 2017/11/29.
 */

public class SeirPortDiallog extends Dialog implements View.OnClickListener {

    Context context;
    String[] str;

    TextView ok,cancel,now_text;
    serialAdapter adapter;
    ListView list;


    public SeirPortDiallog(@NonNull Context context, int theme) {
        super(context, theme);
    }


    public SeirPortDiallog(@NonNull Context context) {
        this(context, R.style.shareDialog);
        this.context = context;

        Resources res = context.getResources();
        str = res.getStringArray(R.array.Serial_item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seirport_layout_dialog);
        cancel=findViewById(R.id.cancel);
        ok=findViewById(R.id.ok);
        list=findViewById(R.id.list);
        now_text=findViewById(R.id.now_text);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        adapter=new serialAdapter();
        list.setAdapter(adapter);
        String t= String.valueOf(SPUtils.getInstance().getString("serialPort", BaseConstant.SPORT));
        now_text.setText("当前串口："+t);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifydata(position);
            }
        });






    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                this.cancel();
                break;
            case R.id.ok :
                String str = null;
                if (adapter.index!=-1){
                    str= String.valueOf(adapter.getItem(adapter.index));
                    Log.e("TAG", "onClick: "+ str);
                }

                if (str==null){

                    Toast.makeText(context,"请设置串口",Toast.LENGTH_SHORT).show();
                }else {
                    SPUtils.getInstance().put("serialPort",str);
                    Log.e("设置串口", "onClick: "+str);
                    new MsgDiallog(context,"设置串口成功，请重新启动").show();
                    cancel();

                }

                break;
        }
    }
    class serialAdapter extends BaseAdapter {
        int index=-1;

        @Override
        public int getCount() {
            return str.length;
        }

        @Override
        public Object getItem(int i) {
            return str[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null)
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seirport_item, null, false);
            CheckedTextView item = VHUtil.ViewHolder.get(view, R.id.item);
            String s= String.valueOf(getItem(i));
            item.setText(s);
            if (index==i){
                item.setChecked(true);
            }else{
                item.setChecked(false);
            }


            return view;
        }
        void notifydata(int index){
            this.index=index;
            notifyDataSetChanged();
        }
    }
}
