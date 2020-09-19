package com.yyide.doorcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.InitLockCallback;
import com.ttlock.bl.sdk.callback.ScanLockCallback;
import com.ttlock.bl.sdk.callback.SetNBServerCallback;
import com.ttlock.bl.sdk.constant.Feature;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.util.SpecialValueUtil;
import com.yyide.doorcontrol.BluetoothService;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.adapter.LockListAdapter;
import com.yyide.doorcontrol.base.BaseActivity;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.brocast.Brocast;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.DateUtils;
import demo.MyApplication;
import demo.lock.model.LockInitResultObj;
import demo.retrofit.ApiService;
import demo.retrofit.RetrofitAPIManager;
import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 * Created by Hao on 2017/10/18.
 * 继承BaseActivity的Activity，不要重复添加友盟统计
 */
public class doorListActivity extends BaseActivity implements LockListAdapter.onLockItemClick{
    public boolean isshow;
    @BindView(R.id.list_device)
    RecyclerView listDevice;
    private LockListAdapter mListApapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_door_list);
        ButterKnife.bind(this);
        initList();

        getScanLockCallback();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TTLockClient.getDefault().stopScanLock();
            }
        },3000);


    }

    private void initList(){
        mListApapter = new LockListAdapter(this);
        listDevice.setAdapter(mListApapter);
        listDevice.setLayoutManager(new LinearLayoutManager(this));
        mListApapter.setOnLockItemClick(this);
    }
    /**
     * start scan BT lock
     */
    private void getScanLockCallback() {
        Log.e("TAG", "getScanLockCallback: ");
        TTLockClient.getDefault().startScanLock(new ScanLockCallback() {
            @Override
            public void onScanLockSuccess(ExtendedBluetoothDevice device) {
                if (mListApapter != null) {
                    mListApapter.updateData(device);
                }
                Log.e("TAG", "onScanLockSuccess: " + device);
//                ClearupdateData(device);


            }

            @Override
            public void onFail(LockError error) {

                Log.e("TAG", "onFail: " + error);
            }
        });
    }


    @Override
    public void onClick(ExtendedBluetoothDevice device) {
        makeToast("--start init lock--");
        /**
         * lockData: the server api lockData param need
         * isNBLock: is a NB-IoT lock.
         */
//        Brocast.reset(this);

        //如果已绑定，先重置锁  再连接
        if (MyApp.getInstance().getChoosedLock()!=null){
            Brocast.reset(this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    TTLockClient.getDefault().initLock(device, new InitLockCallback() {
                        @Override
                        public void onInitLockSuccess(String lockData,int specialValue) {
                            //this must be done after lock is initialized,call server api to post to your server
                            if(SpecialValueUtil.isSupportFeature(specialValue, Feature.NB_LOCK)){
                                setNBServerForNBLock(lockData,device.getAddress());
                            }else {
                                makeToast("--lock is initialized success--");
                                upload2Server(lockData);
                            }


                        }

                        @Override
                        public void onFail(LockError error) {
                            makeErrorToast(error);
                        }
                    });
                }
            },2000);
        }else {
            TTLockClient.getDefault().initLock(device, new InitLockCallback() {
                        @Override
                        public void onInitLockSuccess(String lockData,int specialValue) {
                            //this must be done after lock is initialized,call server api to post to your server
                            if(SpecialValueUtil.isSupportFeature(specialValue, Feature.NB_LOCK)){
                                setNBServerForNBLock(lockData,device.getAddress());
                            }else {
                                makeToast("--lock is initialized success--");
                                upload2Server(lockData);
                            }


                        }

                        @Override
                        public void onFail(LockError error) {
                            makeErrorToast(error);
                        }
                    });


        }



    }


    private void setNBServerForNBLock(String lockData,String lockMac){
        //NB server port
        short mNBServerPort = 8011;
        String mNBServerAddress = "192.127.123.11";
        TTLockClient.getDefault().setNBServerInfo(mNBServerPort, mNBServerAddress, lockData, lockMac, new SetNBServerCallback() {
            @Override
            public void onSetNBServerSuccess(int battery) {
                makeToast("--set NB server success--");
                upload2Server(lockData);
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
                //no matter callback is success or fail,upload lockData to server.
                upload2Server(lockData);
            }
        });
    }

    private void upload2Server(String lockData){
        String lockAlias = "MyTestLock" + DateUtils.getMillsTimeFormat(System.currentTimeMillis());
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<ResponseBody> call = apiService.lockInit(ApiService.CLIENT_ID,  MyApp.getInstance().getAccountInfo().getAccess_token(), lockData,lockAlias,System.currentTimeMillis());
        RetrofitAPIManager.enqueue(call, new TypeToken<LockInitResultObj>() {
        }, result -> {
            if (!result.success) {
                makeToast("-init fail-to server-" + result.getMsg());
                //if upload fail you should cache lockData and upload again until success,or you should reset lock and do init again.
                return;
            }
            makeToast("--init lock success--");
//            Intent intent = new Intent(this,UserLockActivity.class);
//            startActivity(intent);
//            finish();

        }, requestError -> {

        });
    }
    public void makeToast(String content){
        Toast.makeText(this,content,Toast.LENGTH_LONG).show();
    }
    public void makeErrorToast(LockError error){
        Toast.makeText(this,error.getDescription(),Toast.LENGTH_LONG).show();
    }
}
