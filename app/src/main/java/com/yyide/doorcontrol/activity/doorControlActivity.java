package com.yyide.doorcontrol.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ttlock.bl.sdk.api.TTLockClient;
import com.yyide.doorcontrol.BluetoothService;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.brocast.Brocast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class doorControlActivity extends AppCompatActivity {

    @BindView(R.id.Reconnection)
    FrameLayout Reconnection;
    @BindView(R.id.delectconnect)
    FrameLayout delectconnect;
    @BindView(R.id.open)
    Button open;
    BluetoothService bluetoothService;
    @BindView(R.id.No)
    TextView No;
    protected static final int REQUEST_PERMISSION_REQ_CODE = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_control);
        ButterKnife.bind(this);

//        startService(new Intent(this, BluetoothService.class));
//        bluetoothService=new BluetoothService(this);


//        open.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                bluetoothService.doUnlock();
////                Intent intent = new Intent();
////                intent.setClass(doorControlActivity.this, BluetoothService.class);
////                intent.putExtra(BaseConstant.Command, "open");
////                startService(intent);
//                Brocast.open(doorControlActivity.this);
//            }
//        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
                if (MyApp.getInstance().getChoosedLock()!=null){
                    No.setText("已连接："+ MyApp.getInstance().getChoosedLock().getLockName());
                }else {
                    No.setText("未连接");
                    delectconnect.setEnabled(false);
                }
//
//            }
//        },5000);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        Brocast.start(this);
    }

    @OnClick({R.id.Reconnection, R.id.delectconnect})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.Reconnection:
                //重连
                initBtService();

                break;


            case R.id.delectconnect:
                //解绑
//                Intent intent = new Intent();
//                intent.setClass(doorControlActivity.this, BluetoothService.class);
//                intent.putExtra(BaseConstant.Command, "reset");
//                startService(intent);
                Brocast.reset(doorControlActivity.this);
                break;
        }
    }
    private void initBtService(){
        Log.e("TAG", "initBtService: ");
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        boolean isBtEnable =  TTLockClient.getDefault().isBLEEnabled(this);
        Log.e("TAG", "isBtEnable: "+isBtEnable);
        if(!isBtEnable){
            TTLockClient.getDefault().requestBleEnable(this);
        }else {
            startScan();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void startScan(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_REQ_CODE);

            Log.e("TAG", "requestPermissions: ");
            return;
        }else {
//            Intent intent = new Intent();
//            intent.setClass(doorControlActivity.this, BluetoothService.class);
//            intent.putExtra(BaseConstant.Command, "scan");
//            startService(intent);
//            Brocast.scan(doorControlActivity.this);
            startActivity(new Intent(this,doorListActivity.class));
        }
    }
}
