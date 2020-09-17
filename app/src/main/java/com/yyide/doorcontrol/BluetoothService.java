package com.yyide.doorcontrol;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.ControlLockCallback;
import com.ttlock.bl.sdk.callback.ResetLockCallback;
import com.ttlock.bl.sdk.constant.ControlAction;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.util.GsonUtil;
import com.yyide.doorcontrol.base.BaseConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import demo.lock.model.KeyListObj;
import demo.lock.model.KeyObj;
import demo.model.AccountInfo;
import demo.model.LockObj;
import demo.retrofit.ApiService;
import demo.retrofit.RetrofitAPIManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class BluetoothService extends Service  {
    public String account="1085788412@qq.com";
    //    public String password="yd18820221742";
    public String password="35F1F1EB7559F52ABF5471CDD28A60E3";
    public AccountInfo accountInfo;
    protected static final int REQUEST_PERMISSION_REQ_CODE = 11;

    private int pageNo = 1;
    private int pageSize = 100;

    LockObj mCurrentLock;
    KeyObj mMyTestLockEKey;
//    Activity activity;

    public BluetoothService( ) {
//        this.activity=activity;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        startApi();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String command = intent.getStringExtra(BaseConstant.Command);
        if (!TextUtils.isEmpty(command)) {
            switch (command) {
                case "open":
                    doUnlock();
                    break;
                case "reset":
                    resetLock();
                    break;
                case "start":
                    startApi();
                    break;

            }
        }
        return super.onStartCommand(intent, flags, startId);
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
//                        initBtService();
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
                        }
//                        else {
//                            initBtService();
//                        }

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

    public void makeToast(String content){
        Toast.makeText(this,content,Toast.LENGTH_LONG).show();
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
                    }
                }
            }
        }, requestError -> {
            makeToast("--get key list fail-" + requestError.getMessage());
        });

    }


    public void doUnlock(){
        if(mMyTestLockEKey == null){
            makeToast(" you should get your key list first ");
            return;
        }
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        TTLockClient.getDefault().controlLock(ControlAction.UNLOCK, mMyTestLockEKey.getLockData(), mMyTestLockEKey.getLockMac(),new ControlLockCallback() {
            @Override
            public void onControlLockSuccess(int lockAction, int battery, int uniqueId) {
                Toast.makeText(BluetoothService.this,"开锁成功!",Toast.LENGTH_LONG).show();
                Log.e("TAG", "doUnlock: 开锁成功" );
            }

            @Override
            public void onFail(LockError error) {
                Toast.makeText(BluetoothService.this,"开锁失败" + error.getDescription(),Toast.LENGTH_LONG).show();
            }
        });
    }


    public void ensureBluetoothIsEnabled(){
        if(!TTLockClient.getDefault().isBLEEnabled(this)){
//            TTLockClient.getDefault().requestBleEnable(activity);
        }
    }

    public void showConnectLockToast(){
//        makeToast("start connect lock...");
    }

    public void resetLock(){
        ensureBluetoothIsEnabled();
        showConnectLockToast();
        TTLockClient.getDefault().resetLock(mCurrentLock.getLockData(), mCurrentLock.getLockMac(),new ResetLockCallback() {
            @Override
            public void onResetLockSuccess() {
                makeToast("-lock is reset and now upload to  server -");
                uploadResetLock2Server();
            }

            @Override
            public void onFail(LockError error) {
                makeErrorToast(error);
            }
        });
    }
    private void uploadResetLock2Server(){
        ApiService apiService = RetrofitAPIManager.provideClientApi();
        Call<ResponseBody> call = apiService.deleteLock(ApiService.CLIENT_ID,  MyApp.getInstance().getAccountInfo().getAccess_token(), mCurrentLock.getLockId(),System.currentTimeMillis());
        RetrofitAPIManager.enqueue(call, new TypeToken<Object>() {
        }, result -> {
            if (!result.success) {
                makeToast("-reset lock -" + result.getMsg());
                //if upload fail you should cache lockData and upload again until success,or you should reset lock and do init again.
                return;
            }
            makeToast("--reset lock and notify server success--");

        }, requestError -> {
            makeToast(requestError.getMessage());
        });
    }
    public void makeErrorToast(LockError error){
        Toast.makeText(this,error.getDescription(),Toast.LENGTH_LONG).show();
    }






}
