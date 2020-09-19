package com.yyide.doorcontrol;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;

import android.serialport.SerialPort;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.network.FastJsonRequest;
import com.yyide.doorcontrol.network.FastJsonRequestPost;
import com.yyide.doorcontrol.network.GetData;
import com.yyide.doorcontrol.network.OkHttpStack;
import com.yyide.doorcontrol.observer.ObserverManager;
import com.yyide.doorcontrol.requestbean.BaseBeanReq;
import com.yyide.doorcontrol.utils.AuthcodeTwo;
import com.yyide.doorcontrol.utils.CardUtils;
import com.yyide.doorcontrol.utils.CardUtils55;
import com.yyide.doorcontrol.utils.MyHashMap;


import java.io.File;
import java.io.InputStream;

import demo.MyApplication;
import demo.model.AccountInfo;
import demo.model.LockObj;
import okhttp3.OkHttpClient;

import static com.yyide.doorcontrol.utils.Object2Map.object2Map;


public class MyApp extends Application {

    static MyApp app;

    public RequestQueue queue;

    OkHttpClient mOkHttpClient = new OkHttpClient();







    private final Handler cardHandler = new Handler();





    public  long times=System.currentTimeMillis();




    private volatile ReadThread mReadThread;

    private InputStream mInputStream;

    SerialPort mSerialPort;

    StringBuffer stringBuffer=new StringBuffer();
    public boolean PickDataTime=true;
    int font;



    private static MyApp mInstance;
    private AccountInfo accountInfo;
    private LockObj mTestLockObj;



    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        queue = Volley.newRequestQueue(this, new OkHttpStack());

//        CrashReport.initCrashReport(getApplicationContext(), "61902a65c0", true);
//        CrashReport.initCrashReport(getApplicationContext(), "61902a65c0", true);


//        PreferencesUtil.initPrefs(this);
//        PreferencesUtil.putInt("TYPE_PREVIEW_ANGLE", 0);
//        GlobalSet.setLiveStatusValue(GlobalSet.LIVE_STATUS.values()[0]);

        Utils.init(this);

        //bugly初始化
        if (ScreenUtils.isTablet()) {
            /**刷卡器线程*/
//            readCard();
//            readIcCard();
        }

        readCard();

    }



    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public void saveChoosedLock(LockObj lockObj){
        this.mTestLockObj = lockObj;
    }

    public LockObj getChoosedLock(){
        return this.mTestLockObj;
    }


    private void readCard() {
        //获取串口实例
        try {
            mSerialPort = new SerialPort(new File(SPUtils.getInstance().getString("serialPort", BaseConstant.SPORT)), BaseConstant.IBAUDRATE, 0,1,0);
            Log.e("初始化串口", "readCard: " + SPUtils.getInstance().getString("serialPort", BaseConstant.SPORT));
            mInputStream = mSerialPort.getInputStream();
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读串口线程
     */
    private class ReadThread extends Thread {
        @Override
        public void run() {
            while (mInputStream != null && mReadThread == Thread.currentThread()) {

                    byte[] buffer = new byte[64];
                    try {
                        int size = mInputStream.read(buffer);

                        if (size > 0) {

                            String cardNo;
                            if (BaseConstant.HRA) {
                                cardNo = CardUtils55.getInstance().getCardNo(buffer, 0, size);
                            } else {
                                cardNo = CardUtils.getInstance().getCardNo(buffer, 0, size);
                            }
                            if (!TextUtils.isEmpty(cardNo))
                                cardHandler.post(new CardBroadcast(cardNo));




                        }


                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

            }
            }

    }


    private class CardBroadcast implements Runnable {

        private String cardNo;

        private CardBroadcast(String cardNo) {
            this.cardNo = cardNo;
        }

        @Override
        public void run() {
            ObserverManager.getInstance().notifyObserver(cardNo);
        }
    }
    public long time = System.currentTimeMillis() / 1000;
    public void DisPachTime() {
        this.time = System.currentTimeMillis() / 1000;

        Log.e("TAG", "DisPachTime: ");
    }

    public static MyApp getInstance() {
        return app;
    }

    public <T> void requestDataBP(Object tag, BaseBeanReq<T> object, Response.Listener<T> listener,
                                  Response.ErrorListener errorListener) {

        MyHashMap map = new MyHashMap();

        map.putAll(object2Map(object));

        String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), GetData.URL_KEY);

        FastJsonRequest<T> request = new FastJsonRequest<>(GetData.requestUrlBP(object),
                object.myTypeReference(), listener, errorListener,
                encryStr);

        request.setShouldCache(true);

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setTag(tag);

        queue.add(request);

    }
    public <T> void requestDataYydUrl(Object tag, BaseBeanReq<T> object, Response.Listener<T> listener,
                                      Response.ErrorListener errorListener) {
        MyHashMap map = new MyHashMap();
        map.putAll(object2Map(object));
        String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), GetData.URL_KEY);
        FastJsonRequest<T> request = new FastJsonRequest<>(GetData.requestUrlYydUrl(object),
                object.myTypeReference(), listener, errorListener,
                encryStr);
        request.setShouldCache(true);
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        queue.add(request);

    }
    /**
     * 独立操作130端口请求
     */
    public <T> void requestData130(Object tag, BaseBeanReq<T> object, Response.Listener<T> listener,
                                   Response.ErrorListener errorListener) {
        MyHashMap map = new MyHashMap();
        map.putAll(object2Map(object));
        String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), GetData.URL_KEY);
        FastJsonRequest<T> request = new FastJsonRequest<>(GetData.requestUrl130(object),
                object.myTypeReference(), listener, errorListener,
                encryStr);
        request.setShouldCache(true);
        request.setRetryPolicy(new DefaultRetryPolicy(
                20000, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        queue.add(request);

    }

    public <T> void requestData(Object tag, BaseBeanReq<T> object, Response.Listener<T> listener,
                                Response.ErrorListener errorListener) {

        MyHashMap map = new MyHashMap();

        map.putAll(object2Map(object));

        String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), GetData.URL_KEY);

        FastJsonRequest<T> request = new FastJsonRequest<>(GetData.requestUrl(object),
                object.myTypeReference(), listener, errorListener,
                encryStr);


        request.setShouldCache(true);

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setTag(tag);

        queue.add(request);

    }
    public <T> void requestDataHRFace(Object tag, BaseBeanReq<T> object, Response.Listener<T> listener,
                                      Response.ErrorListener errorListener) {

        MyHashMap map = new MyHashMap();

        map.putAll(object2Map(object));

        String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), GetData.URL_KEY);

        FastJsonRequest<T> request = new FastJsonRequest<>(GetData.requestUrlHRFace(object),
                object.myTypeReference(), listener, errorListener,
                encryStr);

        request.setShouldCache(true);

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setTag(tag);

        queue.add(request);

    }

    public <T> void requestData2(Object tag, BaseBeanReq<T> object, Response.Listener<T> listener,
                                 Response.ErrorListener errorListener) {

        MyHashMap map = new MyHashMap();

        map.putAll(object2Map(object));

        String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), GetData.URL_KEY);

        FastJsonRequest<T> request = new FastJsonRequest<>(GetData.requestUrl2(object),
                object.myTypeReference(), listener, errorListener,
                encryStr);

        request.setShouldCache(true);

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setTag(tag);

        queue.add(request);

    }

    public <T> void requestDataPost(Object tag, BaseBeanReq<T> object, Response.Listener<T> listener,
                                    Response.ErrorListener errorListener) {

        MyHashMap map = new MyHashMap();

        map.putAll(object2Map(object));


        String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), GetData.URL_KEY);
        map.put("input", encryStr);
        FastJsonRequestPost<T> request = new FastJsonRequestPost<>(GetData.requestUrl3(object),
                object.myTypeReference(), listener, errorListener,
                encryStr, map);

        request.setShouldCache(true);
        Log.e("TAG", "requestData2: " + encryStr);
        Log.e("TAG", "map: " + JSON.toJSON(map));
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setTag(tag);

        queue.add(request);

    }





    public <T> void requestDataBend(Object tag, BaseBeanReq<T> object, Response.Listener<T> listener,Response.ErrorListener errorListener) {
        MyHashMap map = new MyHashMap();
        map.putAll(object2Map(object));
        String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), GetData.URL_KEY);
        FastJsonRequest<T> request = new FastJsonRequest<>(GetData.requestUrlBend(object),
                object.myTypeReference(), listener, errorListener,
                encryStr);
        request.setShouldCache(true);
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        queue.add(request);

    }



    public <T> void requestYideData(Object tag, BaseBeanReq<T> object, Response.Listener<T> listener, Response.ErrorListener errorListener) {

        MyHashMap map = new MyHashMap();

        map.putAll(object2Map(object));

        String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), GetData.URL_KEY);

        FastJsonRequest<T> request = new FastJsonRequest<>(GetData.requestUrlyd(object),
                object.myTypeReference(), listener, errorListener,
                encryStr);

        request.setShouldCache(true);

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setTag(tag);

        queue.add(request);

    }

    /**
    * 直接使用的预约系统的接口
    * */

    public <T> void requestYySystemData(Object tag, BaseBeanReq<T> object, Response.Listener<T> listener, Response.ErrorListener errorListener) {

        MyHashMap map = new MyHashMap();

        map.putAll(object2Map(object));

        String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), GetData.URL_KEY);

        FastJsonRequest<T> request = new FastJsonRequest<>(GetData.requestUrlYySystem(object),
                object.myTypeReference(), listener, errorListener,
                encryStr);

        request.setShouldCache(true);

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setTag(tag);

        queue.add(request);

    }
}
