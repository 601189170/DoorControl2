package com.yyide.doorcontrol.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.ControlLockCallback;
import com.ttlock.bl.sdk.callback.InitLockCallback;
import com.ttlock.bl.sdk.callback.ResetKeyCallback;
import com.ttlock.bl.sdk.callback.ScanLockCallback;
import com.ttlock.bl.sdk.callback.SetNBServerCallback;
import com.ttlock.bl.sdk.constant.ControlAction;
import com.ttlock.bl.sdk.constant.Feature;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.util.GsonUtil;
import com.ttlock.bl.sdk.util.SpecialValueUtil;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import demo.DateUtils;
import demo.MyApplication;
import demo.lock.model.KeyListObj;
import demo.lock.model.KeyObj;
import demo.lock.model.LockInitResultObj;
import demo.model.AccountInfo;
import demo.model.LockObj;
import demo.retrofit.ApiService;
import demo.retrofit.RetrofitAPIManager;
import okhttp3.ResponseBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Call;

public class LockActivity extends Activity {
    public String account="1085788412@qq.com";
//    public String password="yd18820221742";
    public String password="35F1F1EB7559F52ABF5471CDD28A60E3";
    public AccountInfo accountInfo;

    protected static final int REQUEST_PERMISSION_REQ_CODE = 11;

    private int pageNo = 1;
    private int pageSize = 100;

    LockObj  mCurrentLock;
    KeyObj mMyTestLockEKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mCurrentLock = MyApp.getInstance().getChoosedLock();
        startApi();


    }

    private void startApi() {
        Log.e("TAG", "startApi: " );
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.auth(ApiService.CLIENT_ID, ApiService.CLIENT_SECRET, "password", account, password, ApiService.REDIRECT_URI);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {


                String json = response.body();
                accountInfo = GsonUtil.toObject(json, AccountInfo.class);

                Log.e("TAG", "onResponse: "+accountInfo.toString() );
                if (accountInfo != null) {
                    Log.e("TAG", "accountInfo: "+ accountInfo);
                    if (accountInfo.errcode == 0) {
                        accountInfo.setMd5Pwd(password);
                        MyApp.getInstance().setAccountInfo(accountInfo);
                        Log.e("TAG", "lockList: " );
                        lockList();


                    } else {
                        makeToast(accountInfo.errmsg);
                        initBtService();
                        Log.e("TAG", "errmsgerrmsgerrmsg: "+ accountInfo.errmsg);
//                        makeToast(accountInfo.errmsg);
                    }
                } else {
                    Log.e("TAG", "accountInfonull: "+ response.message());
                    makeToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                makeToast(t.getMessage());
            }
        });
    }
    private void lockList() {
        Log.e("TAG", "lockList==> " );
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<String> call = apiService.getLockList(ApiService.CLIENT_ID, MyApp.getInstance().getAccountInfo().getAccess_token(), pageNo, pageSize, System.currentTimeMillis());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String json = response.body();
                if (json.contains("list")) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray array = jsonObject.getJSONArray("list");
                        ArrayList<LockObj> lockObjs = GsonUtil.toObject(array.toString(), new TypeToken<ArrayList<LockObj>>(){});
//                        mListApapter.updateData(lockObjs);
                        Log.e("TAG", "lockObjs.size==>" +lockObjs.size());
                        if (lockObjs.size()>0){
                            MyApp.getInstance().saveChoosedLock(lockObjs.get(0));
                            getUserKeyList();
                        }else {
                            initBtService();
                        }

//                        resetEKey();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
//                    makeToast(json);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                makeToast(t.getMessage());
            }
        });
    }

    private void resetEKey(){

        Log.e("TAG", "resetEKey: " );
        ensureBluetoothIsEnabled();
        showConnectLockToast();
         LockObj  mCurrentLock = MyApp.getInstance().getChoosedLock();
        TTLockClient.getDefault().resetEkey(mCurrentLock.getLockData(),mCurrentLock.getLockMac(), new ResetKeyCallback() {
            @Override
            public void onResetKeySuccess(int lockFlagPos) {
                uploadResetEkeyResult2Server();
            }

            @Override
            public void onFail(LockError error) {
//                makeErrorToast(error);
            }
        });
    }
    /**
     * make sure Bluetooth is enabled.
     */
    public void ensureBluetoothIsEnabled(){
        if(!TTLockClient.getDefault().isBLEEnabled(this)){
            TTLockClient.getDefault().requestBleEnable(this);
        }
    }
    public void showConnectLockToast(){
//        makeToast("start connect lock...");
    }
    private void uploadResetEkeyResult2Server(){
        Log.e("TAG", "uploadResetEkeyResult2Server: " );
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<ResponseBody> call = apiService.restEkey(ApiService.CLIENT_ID,  MyApp.getInstance().getAccountInfo().getAccess_token(), mCurrentLock.getLockId(),System.currentTimeMillis());
        RetrofitAPIManager.enqueue(call, new TypeToken<Object>() {
        }, result -> {
            if (!result.success) {
                makeToast("-init fail-upload to server-" + result.getMsg());
//                Toast.makeText(this,"开锁成功",Toast.LENGTH_LONG).show();
                //if upload fail you should cache lockData and upload again until success,or you should reset lock and do init again.
                return;
            }
            makeToast("--reset key and notify server success--");

        }, requestError -> {
            makeToast(requestError.getMessage());
        });
    }

    public void makeToast(String content){
        Toast.makeText(this,content,Toast.LENGTH_LONG).show();
    }

    private void initBtService(){
        Log.e("TAG", "initBtService: ");
        TTLockClient.getDefault().prepareBTService(getApplicationContext());
        boolean isBtEnable =  TTLockClient.getDefault().isBLEEnabled(LockActivity.this);
        Log.e("TAG", "isBtEnable: "+isBtEnable);
        if(!isBtEnable){
            TTLockClient.getDefault().requestBleEnable(LockActivity.this);
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
        }
        Log.e("TAG", "startScan: ");
        getScanLockCallback();
    }
    /**
     * start scan BT lock
     */
    private void getScanLockCallback(){
        Log.e("TAG", "getScanLockCallback: ");
        TTLockClient.getDefault().startScanLock(new ScanLockCallback() {
            @Override
            public void onScanLockSuccess(ExtendedBluetoothDevice device) {
//                if(mListApapter != null){
//                    mListApapter.updateData(device);
//                }
                Log.e("TAG", "onScanLockSuccess: "+ device);
                        ClearupdateData(device);




//                Log.e("TAG", "onScanLockSuccess: "+ device);
            }

            @Override
            public void onFail(LockError error) {

                Log.e("TAG", "onFail: "+ error);
            }
        });
    }
    public void ClearupdateData(ExtendedBluetoothDevice device){
        if(device != null) {
            if(device.isSettingMode()){
                TTLockClient.getDefault().stopScanLock();

                BLConturn(device);
            }
        }
    }


    void BLConturn(ExtendedBluetoothDevice device){
        /**
         * lockData: the server api lockData param need
         * isNBLock: is a NB-IoT lock.
         */
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
//                makeErrorToast(error);
                Log.e("TAG", "onFail: "+error );
            }
        });
    }

    /**
     * if a NB-IoT lock you'd better do set NB-IoT server before upload lockData to server to active NB-IoT lock service.
     * And no matter callback is success or fail,upload lockData to server.
     * @param lockData
     * @param lockMac
     */
    private void setNBServerForNBLock(String lockData,String lockMac){
        //NB server port
        short mNBServerPort = 8011;
        String mNBServerAddress = "192.127.123.11";
        TTLockClient.getDefault().setNBServerInfo(mNBServerPort, mNBServerAddress, lockData, lockMac, new SetNBServerCallback() {
            @Override
            public void onSetNBServerSuccess(int battery) {
                makeToast("--set NB server success--");
                upload2Server(lockData);

                Log.e("TAG", "onSetNBServerSuccess: " );

                doUnlock();
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
            Intent intent = new Intent(this,LockActivity.class);
            startActivity(intent);
            finish();

        }, requestError -> {

        });
    }


    public void makeErrorToast(LockError error){
        Toast.makeText(this,error.getDescription(),Toast.LENGTH_LONG).show();
    }

    private void doUnlock(){
        if(mMyTestLockEKey == null){
            makeToast(" you should get your key list first ");
            return;
        }
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        TTLockClient.getDefault().controlLock(ControlAction.UNLOCK, mMyTestLockEKey.getLockData(), mMyTestLockEKey.getLockMac(),new ControlLockCallback() {
            @Override
            public void onControlLockSuccess(int lockAction, int battery, int uniqueId) {
                Toast.makeText(LockActivity.this,"lock is unlock  success!",Toast.LENGTH_LONG).show();
                Log.e("TAG", "doUnlock: 开锁成功" );
            }

            @Override
            public void onFail(LockError error) {
                Toast.makeText(LockActivity.this,"unLock fail!--" + error.getDescription(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getUserKeyList(){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        HashMap<String,String> param = new HashMap<>(6);
        param.put("clientId",ApiService.CLIENT_ID);
        param.put("accessToken",MyApp.getInstance().getAccountInfo().getAccess_token());
        param.put("pageNo","1");
        param.put("pageSize","1000");
        param.put("date",String.valueOf(System.currentTimeMillis()));

        Call<ResponseBody> call = apiService.getUserKeyList(param);
        RetrofitAPIManager.enqueue(call, new TypeToken<KeyListObj>(){}, result -> {
            if(!result.success){
                makeToast("--get my key list fail-" + result.getMsg());
                return;
            }
            Log.d("OMG","===result===" + result.getResult() + "===" + result);
            KeyListObj keyListObj = result.getResult();
            ArrayList<KeyObj> myKeyList = keyListObj.getList();
            Log.e("TAG", "getUserKeyList: "+ JSON.toJSONString(myKeyList));
            mCurrentLock = MyApp.getInstance().getChoosedLock();
            Log.e("TAG", "mCurrentLock: "+ JSON.toJSONString(mCurrentLock));
            if(!myKeyList.isEmpty()){
                for(KeyObj keyObj : myKeyList){
                    if(keyObj.getLockId() == mCurrentLock.getLockId()){
                        mMyTestLockEKey = keyObj;
                        doUnlock();
                    }
                }
            }
        }, requestError -> {
            makeToast("--get key list fail-" + requestError.getMessage());
        });
//        doUnlock();
    }
}
