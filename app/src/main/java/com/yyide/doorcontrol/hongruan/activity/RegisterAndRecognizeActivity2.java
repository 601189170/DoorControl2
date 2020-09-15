package com.yyide.doorcontrol.hongruan.activity;//package com.yyide.twovisitors.hongruan.activity;
//
//import android.Manifest;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.ActivityInfo;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.ImageFormat;
//import android.graphics.Matrix;
//import android.graphics.Point;
//import android.graphics.Rect;
//import android.graphics.YuvImage;
//import android.hardware.Camera;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.Base64;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Surface;
//import android.view.View;
//import android.view.ViewTreeObserver;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.Switch;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSON;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.arcsoft.face.AgeInfo;
//import com.arcsoft.face.ErrorInfo;
//import com.arcsoft.face.FaceEngine;
//import com.arcsoft.face.FaceFeature;
//import com.arcsoft.face.GenderInfo;
//import com.arcsoft.face.LivenessInfo;
//import com.arcsoft.face.VersionInfo;
//import com.blankj.utilcode.util.SPUtils;
//import com.blankj.utilcode.util.ToastUtils;
//import com.bumptech.glide.Glide;
//import com.yyide.twovisitors.R;
//import com.yyide.twovisitors.hongruan.db.DbController;
//import com.yyide.twovisitors.hongruan.faceserver.CompareResult;
//import com.yyide.twovisitors.hongruan.model.FacePreviewInfo;
//import com.yyide.twovisitors.hongruan.util.face.FaceHelper;
//import com.yyide.twovisitors.hongruan.widget.FaceRectView;
//import com.yyide.twovisitors.hongruan.widget.FaceSearchResultAdapter;
//import com.yyide.twovisitors.responbean.TemporBean;
//
//
//import java.io.ByteArrayOutputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.TimeUnit;
//
//import cn.pedant.SweetAlert.SweetAlertDialog;
//
//
///**
// *人脸采集界面
// * */
//public class RegisterAndRecognizeActivity2 extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener {
//    private static final String TAG = "RegisterAndRecognize";
//    private static final int MAX_DETECT_NUM = 10;
//    byte[] nv21fornow;
//    List<FacePreviewInfo> facePreviewInfoListcj=new ArrayList<>();
//    int ztcj = 0;
//    int tjzt = 0;
//    int insertface = 0; //提交人脸中
//    Bitmap bitmap_arcface;
////    private DbController mDbController;
//    /**
//     * 当FR成功，活体未成功时，FR等待活体的时间
//     */
//
//    private static final int WAIT_LIVENESS_INTERVAL = 100;
//    /**
//     * 失败重试间隔时间（ms）
//     */
//    private static final long FAIL_RETRY_INTERVAL = 1000;
//    /**
//     * 出错重试最大次数
//     */
//    private static final int MAX_RETRY_TIME = 3;
//
//    private CameraHelper cameraHelper;
//    private DrawHelper drawHelper;
//    private Camera.Size previewSize;
//    /**
//     * 优先打开的摄像头，本界面主要用于单目RGB摄像头设备，因此默认打开前置
//     */
//    private Integer rgbCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
//
//    /**
//     * VIDEO模式人脸检测引擎，用于预览帧人脸追踪
//     */
//    private FaceEngine ftEngine;
//    /**
//     * 用于特征提取的引擎
//     */
//    private FaceEngine frEngine;
//    /**
//     * IMAGE模式活体检测引擎，用于预览帧人脸活体检测
//     */
//    private FaceEngine flEngine;
//
//
//    private int ftInitCode = -1;
//    private int frInitCode = -1;
//    private int flInitCode = -1;
//    private FaceHelper faceHelper;
//    private List<CompareResult> compareResultList;
//    private FaceSearchResultAdapter adapter;
//    /**
//     * 活体检测的开关
//     */
//    private boolean livenessDetect = true;
//    /**
//     * 注册人脸状态码，准备注册
//     */
//    private static final int REGISTER_STATUS_READY = 0;
//    /**
//     * 注册人脸状态码，注册中
//     */
//    private static final int REGISTER_STATUS_PROCESSING = 1;
//    /**
//     * 注册人脸状态码，注册结束（无论成功失败）
//     */
//    private static final int REGISTER_STATUS_DONE = 2;
//
//
//
//    private static final int REGISTER_STATUS_SUSCUUSS = 99;
//
//    private int registerStatus = REGISTER_STATUS_DONE;
//
//    private ConcurrentHashMap<Integer, Integer> requestFeatureStatusMap = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<Integer, Integer> extractErrorRetryMap = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<Integer, Integer> livenessMap = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<Integer, Integer> livenessErrorRetryMap = new ConcurrentHashMap<>();
//    private CompositeDisposable getFeatureDelayedDisposables = new CompositeDisposable();
//    private CompositeDisposable delayFaceTaskCompositeDisposable = new CompositeDisposable();
//    /**
//     * 相机预览显示的控件，可为SurfaceView或TextureView
//     */
//    private View previewView;
//    /**
//     * 绘制人脸框的控件
//     */
//    private FaceRectView faceRectView;
//
//    private Switch switchLivenessDetect;
//
//    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
//    private static final float SIMILAR_THRESHOLD = 0.75F;
//    /**
//     * 所需的所有权限信息
//     */
//    private static final String[] NEEDED_PERMISSIONS = new String[]{
//            Manifest.permission.CAMERA,
//            Manifest.permission.READ_PHONE_STATE
//
//    };
//    private ImageView imageView;
//    private Button button_djcc, button_tj;
//    private FrameLayout framet;
//    private String name, id, type;
//    private Button close;
//    private TextView textView, nameloh;
//    private int abcde = 0;
//    private int getLivenessFace=0;//判断是否是活体，1是活体，0不是活体
//    TemporBean bean;
//
//    IssusscsBD bd;
//
//    public Handler getHander() {
//        return mHandler;
//    }
//
//    private Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 864:
//                    if (abcde != 1) {
//                        button_djcc.setVisibility(View.VISIBLE);
//                        textView.setVisibility(View.GONE);
//                    }
//                    break;
////                Message msg = new Message();
////                Bundle bundle = new Bundle();
////                msg.setData(bundle);
////                msg.what = 864;
////                mHandler.sendMessage(msg);
//
//            }
//        }
//    };
//
//    private static DbController mDbController;
//    public boolean isClose=false;
//    TemporaryDiallog temporaryDiallog;
//
//    public static String ISSUCCES="ISSUCCES";
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout_qd.activity_register_and_recognize);
//        String s = getIntent().getStringExtra("bean");
//        bd=new IssusscsBD();
//        IntentFilter intentFilter=new IntentFilter();
//        intentFilter.addAction(ISSUCCES);
//        registerReceiver(bd,intentFilter);
//        mDbController = DbController.getInstance(this);
//        bean = JSON.parseObject(s, TemporBean.class);
//        name = bean.name;
//        id =Tool.getMyUUID();
//
//        type = "1";
////        id = bean.name;
//        //保持亮屏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        pd = new LoadingTools().pd(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams attributes = getWindow().getAttributes();
//            attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//            getWindow().setAttributes(attributes);
//        }
//        close = findViewById(R.id.close);
//        nameloh = findViewById(R.id.nameloh);
////        Intent intent = getIntent();
////        name = intent.getStringExtra("name");
//
//
//
////        type = intent.getStringExtra("type");  //1为 student录入  2为teacher 录入
//        nameloh.setText("请" + name + "对准摄像头信息采集");
//        textView = findViewById(R.id.textview);
//        imageView = findViewById(R.id.imageview);
//        framet = findViewById(R.id.framet);
//        button_djcc = findViewById(R.id.button_djcc);
//        button_tj = findViewById(R.id.button_tj);
//        button_tj.setVisibility(View.GONE);
////        button_djcc.setVisibility(View.GONE);
//
//        // Activity启动后就锁定为启动时的方向
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
//        //本地人脸库初始化
////        FaceServer.getInstance().init(this);
////        mDbController = DbController.getInstance(RegisterAndRecognizeActivity.this);
//        //本地人脸库初始化
//        if (FaceServer.faceEngine==null){
//            FaceServer.getInstance().init(this);
//        }else {
//            FaceServer.getInstance().unInit();
//            FaceServer.getInstance().init(this);
//        }
//        initView();
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        button_djcc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (button_djcc.getText().toString().equals("点击采集")) {
//                    ztcj = 1;
//                    imageView.setVisibility(View.VISIBLE);
//                    button_tj.setVisibility(View.VISIBLE);
//                } else {
//                    ztcj = 0;
//                    imageView.setVisibility(View.GONE);
//                    button_tj.setVisibility(View.GONE);
//                    button_djcc.setTextColor(Color.parseColor("#FFFFFF"));
//                    button_djcc.setText("点击采集");
//                    button_djcc.setBackgroundResource(R.drawable.bg_blue4);
//                }
//            }
//        });
//        button_djcc.setVisibility(View.GONE);
//        new Thread(new ThreadShow2()).start();
////        button_tj.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                insertface = 1;
//////              registerFace(nv21fornow, facePreviewInfoListcj); //录入人脸信息
////                tjzt = 1;  //录入人脸信息
////                voice(nv21fornow, bitmap_arcface);
////
////            }
////        });
//        button_tj.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                insertface = 1;
//                tjzt = 1;  //录入人脸信息
//                //优先弹窗显示信息，等待门卫保安大叔确认
//                temporaryDiallog = new TemporaryDiallog(RegisterAndRecognizeActivity2.this,
//                        name, bean.time, bean.yourname, bean.dosome, "信息已采集", new TemporaryDiallog.TemporaryDiallogListener() {
//                    @Override
//                    public void onClick(View view) {
//                        switch (view.getId()) {
//                            case R.id.no:
////                                mDbController.delete(id);
//                                temporaryDiallog.cancel();
//                                isClose = true;
//
//                                finish();
//
//                                break;
//                            case R.id.yes:
//
//                                temporaryDiallog.cancel();
////                                Identity(id);
////
//
//
//
//
////                                IsregisterFace(nv21fornow,facePreviewInfoListcj);
//
//                                if (facePreviewInfoListcj.size()>0&&nv21fornow!=null){
//
//
//                                 byte[] featureData=FaceServer.getInstance().registerNv22(RegisterAndRecognizeActivity2.this, nv21fornow.clone(), previewSize.width, previewSize.height,
//                                        facePreviewInfoListcj.get(0).getFaceInfo(), name, id, type);
//
//                                if (featureData!=null) {
//                                    FaceData faceData=new FaceData();
//                                    faceData.nv21=nv21fornow.clone();
//                                    faceData.width=previewSize.width;
//                                    faceData.height=previewSize.height;
//                                    faceData.faceInfo=facePreviewInfoListcj.get(0).getFaceInfo();
//                                    faceData.name=name;
//                                    faceData.numbid=id;
//                                    faceData.type=type;
//                                    faceData.featureData=featureData;
//                                    SPUtils.getInstance().put(SpData.FACEDATA,JSON.toJSONString(faceData));
//
//                                    Intent intent=new Intent();
//                                    intent.putExtra("vistorId", id);
//                                    intent.putExtra("bean", JSON.toJSONString(bean));
////                                    intent.putExtra("FaceData", JSON.toJSONString(faceData));
//                                    intent.putExtra(BaseConstant.DOSHOMTHING, BaseConstant.TEMPORARYCHEACK);
//                                    intent.setClass(RegisterAndRecognizeActivity2.this, DoorIdentityActivity2.class);
//                                    startActivity(intent);
//                                    finish();
//                                } else {
////                                    mDbController.delete(id);
//                                    Toast.makeText(RegisterAndRecognizeActivity2.this,"照片不清晰，请重新采集人脸",Toast.LENGTH_SHORT).show();
//                                    finish();
//                                }
//                                }else {
////                                    mDbController.delete(id);
//                                    ToastUtils.showShort("人脸采集失败，请重试");
//                                    finish();
//                                }
//                                break;
//                        }
//                    }
//                });
//                temporaryDiallog.show();
//
//                temporaryDiallog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
////                        mDbController.delete(id);
//                    }
//                });
//            }
//        });
//
//    }
//    void Identity(String userId) {
//
//        VerifyIdcardReq req = new VerifyIdcardReq();
//        req.officeId = SpData.User().data.officeId;
//        req.id = userId;
//        MyApp.getInstance().requestDataPost(this, req, new AuthListener(), new ErrorListener());
//    }
//
//    class AuthListener implements Response.Listener<VerifyIdcardRsp> {
//
//        @Override
//        public void onResponse(VerifyIdcardRsp rsp) {
//
//            if (rsp.status == BaseConstant.REQUEST_SUCCES) {
//                if (rsp.data.size() > 0) {
//                    SetData(rsp);
//                }
//            } else {
//                ToastUtils.showShort(rsp.info);
//            }
//        }
//    }
//
//    class ErrorListener implements Response.ErrorListener {
//
//        @Override
//        public void onErrorResponse(VolleyError error) {
//        pd.dismiss();
//
//        }
//    }
//    void SetData(VerifyIdcardRsp rsp) {
//        //未到访
//        List<VerifyIdcardRsp.DataBean> list0 = new ArrayList<>();
//
//        for (VerifyIdcardRsp.DataBean datum : rsp.data) {
//            if (datum.status == 0) {
//                list0.add(datum);
//            }
//        }
//       if (list0.size()>0)
//           postData("",list0.get(0).id);
//
//
//    }
//
//    void postData(String personId,String id){
//
//        pd.show();
//        VerifyDealReq req=new VerifyDealReq();
//        req.officeId= SpData.User().data.officeId;
//        req.pedestriansId=personId;
//
//        req.ids=id;
//        req.status=0;
//
//        MyApp.getInstance().requestData(this,req,new AuthListener2(),new ErrorListener());
//    }
//
//    class AuthListener2 implements Response.Listener<VerifyDealRsp>{
//
//        @Override
//        public void onResponse(VerifyDealRsp response) {
//            pd.dismiss();
//            if (response.status == BaseConstant.REQUEST_SUCCES) {
//                Toast.makeText(RegisterAndRecognizeActivity2.this,"同意通行",Toast.LENGTH_SHORT).show();
////                new NoMsgDiallog(DoorIdentityActivity.this,"同意通行").show();
//                finish();
//            }
//        }
//    }
//
//
//    class ThreadShow2 implements Runnable //定时器    为1秒一次
//    {
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            Looper.prepare();
//            while (true) {
//                try {
//                    Thread.sleep(3000);
//                    Message msg = new Message();
//                    Bundle bundle = new Bundle();
//                    msg.setData(bundle);
//                    msg.what = 864;
//                    mHandler.sendMessage(msg);
//                    Looper.loop();
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private void initView() {
//        previewView = findViewById(R.id.single_camera_texture_preview);
//        //在布局结束后才做初始化操作
//        previewView.getViewTreeObserver().addOnGlobalLayoutListener(this);
//        faceRectView = findViewById(R.id.single_camera_face_rect_view);
//        switchLivenessDetect = findViewById(R.id.single_camera_switch_liveness_detect);
//        switchLivenessDetect.setChecked(true);
////        switchLivenessDetect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                livenessDetect = isChecked;
////            }
////        });
//        RecyclerView recyclerShowFaceInfo = findViewById(R.id.single_camera_recycler_view_person);
//        compareResultList = new ArrayList<>();
//        adapter = new FaceSearchResultAdapter(compareResultList, this);
//        recyclerShowFaceInfo.setAdapter(adapter);
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        int spanCount = (int) (dm.widthPixels / (getResources().getDisplayMetrics().density * 100 + 0.5f));
//        recyclerShowFaceInfo.setLayoutManager(new GridLayoutManager(this, spanCount));
//        recyclerShowFaceInfo.setItemAnimator(new DefaultItemAnimator());
//    }
//
//    /**
//     * 初始化引擎
//     */
//    private void initEngine() {
//        ftEngine = new FaceEngine();
//        ftInitCode = ftEngine.init(this, FaceEngine.ASF_DETECT_MODE_VIDEO, ConfigUtil.getFtOrient(this),
//                16, MAX_DETECT_NUM, FaceEngine.ASF_FACE_DETECT);
//
//        frEngine = new FaceEngine();
//        frInitCode = frEngine.init(this, FaceEngine.ASF_DETECT_MODE_IMAGE, FaceEngine.ASF_OP_0_ONLY,
//                16, MAX_DETECT_NUM, FaceEngine.ASF_FACE_RECOGNITION);
//
//        flEngine = new FaceEngine();
//        flInitCode = flEngine.init(this, FaceEngine.ASF_DETECT_MODE_IMAGE, FaceEngine.ASF_OP_0_ONLY,
//                16, MAX_DETECT_NUM, FaceEngine.ASF_LIVENESS);
//
//        VersionInfo versionInfo = new VersionInfo();
//        ftEngine.getVersion(versionInfo);
//        Log.i(TAG, "initEngine:  init: " + ftInitCode + "  version:" + versionInfo);
//
//        if (ftInitCode != ErrorInfo.MOK) {
//            String error = getString(R.string.specific_engine_init_failed, "ftEngine", ftInitCode);
//            Log.i(TAG, "initEngine: " + error);
//            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
//        }
//        if (frInitCode != ErrorInfo.MOK) {
//            String error = getString(R.string.specific_engine_init_failed, "frEngine", frInitCode);
//            Log.i(TAG, "initEngine: " + error);
//            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
//        }
//        if (flInitCode != ErrorInfo.MOK) {
//            String error = getString(R.string.specific_engine_init_failed, "flEngine", flInitCode);
//            Log.i(TAG, "initEngine: " + error);
//            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * 销毁引擎，faceHelper中可能会有特征提取耗时操作仍在执行，加锁防止crash
//     */
//    private void unInitEngine() {
//        if (ftInitCode == ErrorInfo.MOK && ftEngine != null) {
//            synchronized (ftEngine) {
//                int ftUnInitCode = ftEngine.unInit();
//                Log.i(TAG, "unInitEngine: " + ftUnInitCode);
//            }
//        }
//        if (frInitCode == ErrorInfo.MOK && frEngine != null) {
//            synchronized (frEngine) {
//                int frUnInitCode = frEngine.unInit();
//                Log.i(TAG, "unInitEngine: " + frUnInitCode);
//            }
//        }
//        if (flInitCode == ErrorInfo.MOK && flEngine != null) {
//            synchronized (flEngine) {
//                int flUnInitCode = flEngine.unInit();
//                Log.i(TAG, "unInitEngine: " + flUnInitCode);
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (cameraHelper != null) {
//            cameraHelper.release();
//            cameraHelper = null;
//        }
//            unregisterReceiver(bd);
//        unInitEngine();
//        if (faceHelper != null) {
//            ConfigUtil.setTrackedFaceCount(this, faceHelper.getTrackedFaceCount());
//            faceHelper.release();
//            faceHelper = null;
//        }
//        if (getFeatureDelayedDisposables != null) {
//            getFeatureDelayedDisposables.clear();
//        }
//        if (delayFaceTaskCompositeDisposable != null) {
//            delayFaceTaskCompositeDisposable.clear();
//        }
//
//        FaceServer.getInstance().unInit();
//
//        super.onDestroy();
//        if (isClose){
//            MinaEventType minaEventType = new MinaEventType();
//            minaEventType.i = 10;
//            EventBus.getDefault().post(minaEventType);
//        }
//    }
//
//    public void safeDispose(Disposable disposable) {
//        if (disposable != null && !disposable.isDisposed()) {
//            disposable.dispose();
//        }
//    }
//
//    private boolean checkPermissions(String[] neededPermissions) {
//        if (neededPermissions == null || neededPermissions.length == 0) {
//            return true;
//        }
//        boolean allGranted = true;
//        for (String neededPermission : neededPermissions) {
//            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
//        }
//        return allGranted;
//    }
//
//
//    private void initCamera() {
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        final FaceListener faceListener = new FaceListener() {
//            @Override
//            public void onFail(Exception e) {
//                Log.e(TAG, "onFail: " + e.getMessage());
//            }
//
//            //请求FR的回调
//            @Override
//            public void onFaceFeatureInfoGet(@Nullable final FaceFeature faceFeature, final Integer requestId, final Integer errorCode) {
//                //FR成功
//                if (faceFeature != null) {
//                    Log.i(TAG, "onPreview: fr end = " + System.currentTimeMillis() + " trackId = " + requestId + "feature" + faceFeature.getFeatureData().toString());
//                    Integer liveness = livenessMap.get(requestId);
//                    getLivenessFace=0;
//                    //不做活体检测的情况，直接搜索
//                    if (insertface != 1) {
//                        //活体检测通过，搜索特征
//                     //   Log.e("活体检测", "onFaceFeatureInfoGet:" + liveness);
//                        if (liveness != null && liveness == LivenessInfo.ALIVE) {
//                            getLivenessFace=1;
//                            searchFace(faceFeature, requestId);
//                        }
//                        // 活体检测未出结果，或者非活体，延迟执行该函数
//                        else {
//                         //   Log.e("活体检测", "onFaceFeatureInfoGet:" + "活体检测未出结果，或者非活体，延迟执行该函数"+liveness+"/getLivenessFace:"+getLivenessFace);
//                            getLivenessFace=0;
//                            if (requestFeatureStatusMap.containsKey(requestId)) {
//                                Observable.timer(WAIT_LIVENESS_INTERVAL, TimeUnit.MILLISECONDS)
//                                        .subscribe(new Observer<Long>() {
//                                            Disposable disposable;
//
//                                            @Override
//                                            public void onSubscribe(Disposable d) {
//                                                disposable = d;
//                                                getFeatureDelayedDisposables.add(disposable);
//                                            }
//
//                                            @Override
//                                            public void onNext(Long aLong) {
//                                                onFaceFeatureInfoGet(faceFeature, requestId, errorCode);
//                                            }
//
//                                            @Override
//                                            public void onError(Throwable e) {
//
//                                            }
//
//                                            @Override
//                                            public void onComplete() {
//                                                getFeatureDelayedDisposables.remove(disposable);
//                                            }
//                                        });
//                            }
//                        }
//                    }
//                }
//                //特征提取失败
//                else {
//                    getLivenessFace=0;
//                    if (increaseAndGetValue(extractErrorRetryMap, requestId) > MAX_RETRY_TIME) {
//                        extractErrorRetryMap.put(requestId, 0);
//                        String msg;
//                        // 传入的FaceInfo在指定的图像上无法解析人脸，此处使用的是RGB人脸数据，一般是人脸模糊
//                        if (errorCode != null && errorCode == ErrorInfo.MERR_FSDK_FACEFEATURE_LOW_CONFIDENCE_LEVEL) {
//                            msg = getString(R.string.low_confidence_level);
//                        } else {
//                            msg = "ExtractCode:" + errorCode;
//                        }
//                        if (faceHelper!=null)
//                        faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, msg));
//                        // 在尝试最大次数后，特征提取仍然失败，则认为识别未通过
//                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
//                        retryRecognizeDelayed(requestId);
//                    } else {
//                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.TO_RETRY);
//                    }
//                }
//            }
//
//            @Override
//            public void onFaceLivenessInfoGet(@Nullable LivenessInfo livenessInfo, final Integer requestId, Integer errorCode) {
//                if (livenessInfo != null) {
//                    int liveness = livenessInfo.getLiveness();
//                    livenessMap.put(requestId, liveness);
//                    // 非活体，重试
//                    if (liveness == LivenessInfo.NOT_ALIVE) {
//                        getLivenessFace=0;
//                        if (faceHelper!=null)
//                        faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, "NOT_ALIVE"));
//                        // 延迟 FAIL_RETRY_INTERVAL 后，将该人脸状态置为UNKNOWN，帧回调处理时会重新进行活体检测
//                        retryLivenessDetectDelayed(requestId);
//                    }
//                } else {
//                    if (increaseAndGetValue(livenessErrorRetryMap, requestId) > MAX_RETRY_TIME) {
//                        livenessErrorRetryMap.put(requestId, 0);
//                        String msg;
//                        // 传入的FaceInfo在指定的图像上无法解析人脸，此处使用的是RGB人脸数据，一般是人脸模糊
//                        if (errorCode != null && errorCode == ErrorInfo.MERR_FSDK_FACEFEATURE_LOW_CONFIDENCE_LEVEL) {
//                            msg = getString(R.string.low_confidence_level);
//                        } else {
//                            msg = "ProcessCode:" + errorCode;
//                        }
//                        if (faceHelper!=null)
//                        faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, msg));
//                        retryLivenessDetectDelayed(requestId);
//                    } else {
//                      //  Log.e("活体检测", "onFaceLivenessInfoGet:" + getLivenessFace);
//                        getLivenessFace=0;
//                        livenessMap.put(requestId, LivenessInfo.UNKNOWN);
//                    }
//                }
//            }
//        };
//
//        CameraListener cameraListener = new CameraListener() {
//            @Override
//            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
//                Camera.Size lastPreviewSize = previewSize;
//                previewSize = camera.getParameters().getPreviewSize();
//                drawHelper = new DrawHelper(previewSize.width, previewSize.height, previewView.getWidth(), previewView.getHeight(), displayOrientation
//                        , cameraId, isMirror, false, false);
//                Log.i(TAG, "onCameraOpened: " + drawHelper.toString());
//                // 切换相机的时候可能会导致预览尺寸发生变化
//                if (faceHelper == null ||
//                        lastPreviewSize == null ||
//                        lastPreviewSize.width != previewSize.width || lastPreviewSize.height != previewSize.height) {
//                    Integer trackedFaceCount = null;
//                    // 记录切换时的人脸序号
//                    if (faceHelper != null) {
//                        trackedFaceCount = faceHelper.getTrackedFaceCount();
//                        faceHelper.release();
//                    }
//
//                    faceHelper = new FaceHelper.Builder()
//                            .ftEngine(ftEngine)
//                            .frEngine(frEngine)
//                            .flEngine(flEngine)
//                            .frQueueSize(MAX_DETECT_NUM)
//                            .flQueueSize(MAX_DETECT_NUM)
//                            .previewSize(previewSize)
//                            .faceListener(faceListener)
//                            .trackedFaceCount(trackedFaceCount == null ? ConfigUtil.getTrackedFaceCount(RegisterAndRecognizeActivity2.this.getApplicationContext()) : trackedFaceCount)
//                            .build();
//                }
//            }
//
//
//            @Override
//            public void onPreview(final byte[] nv21, Camera camera) {
//                try {
//
//
//                if (faceRectView != null) {
//                    faceRectView.clearFaceInfo();
//                }
//
//                List<FacePreviewInfo> facePreviewInfoList = faceHelper.onPreviewFrame(nv21);
//                if (facePreviewInfoList != null && faceRectView != null && drawHelper != null) {
//                    drawPreviewInfo(facePreviewInfoList);
//                }
//
//               // Log.e("活体检测", "face_123123" +"/onPreview:"+ getLivenessFace);
//                try {
//                    if (ztcj == 1&&getLivenessFace==1) {
//                        getLivenessFace=0;
//                        ztcj = 0;
//                        YuvImage yuv = new YuvImage(nv21, ImageFormat.NV21, previewSize.width, previewSize.height, null);
//                        Rect cropRect = getBestRect(previewSize.width, previewSize.height, facePreviewInfoList.get(0).getFaceInfo().getRect());
//                        if (cropRect == null) {
//                        }
//                        ByteArrayOutputStream fosImage = new ByteArrayOutputStream();
//                        yuv.compressToJpeg(cropRect, 100, fosImage);
//
//                        byte[] bytes = fosImage.toByteArray();
//                        bitmap_arcface = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                        int cameraDegress = cameraHelper.getCameraDegress();
//                        int angle = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
//                        int screenDegress = 0;
//                        if (angle == 0) {
//                            screenDegress = 0;
//                        } else if (angle == 1) {
//                            screenDegress = 90;
//                        } else if (angle == 2) {
//                            screenDegress = 180;
//                        } else if (angle == 3) {
//                            screenDegress = 270;
//                        }
//                        Log.e("获取摄像头旋转的角度01", "摄像头的角度cameraDegress:" + cameraDegress + "/获取屏幕值angle:" + angle + "/获取屏幕的角度:" + screenDegress);
//                        int imgDegress = 0;
//                        Matrix matrix = new Matrix();
//                        if (cameraDegress == 90 && screenDegress == 90) {//竖版班牌使用情况
//                            imgDegress = 180;
//                        } else if (cameraDegress == 90 && screenDegress == 270) {//竖版班牌使用情况
//                            imgDegress = 0;
//                        } else if (cameraDegress == 0 && screenDegress == 0) {//横版班牌使用情况
////                            imgDegress = 0;
//                            imgDegress = -90;
//                        } else if (cameraDegress == 0 && screenDegress == 180) {//横版班牌使用情况
//                            imgDegress = 180;
//                        }
//                        matrix.setRotate(imgDegress);
//                        bitmap_arcface = Bitmap.createBitmap(bitmap_arcface, 0, 0, bitmap_arcface.getWidth(), bitmap_arcface.getHeight(), matrix, true);
////                        bitmap_arcface = Tool.rotateBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length),getCameradegrees(cameraHelper.getCameraID()));
//
//
//                        Glide.with(RegisterAndRecognizeActivity2.this).load(bitmap_arcface).into(imageView);
//                        nv21fornow = nv21;
//                        facePreviewInfoListcj = facePreviewInfoList;
//                        button_djcc.setText("重新采集");
//                        button_djcc.setTextColor(Color.parseColor("#4F91FF"));
//                        button_djcc.setBackgroundResource(R.drawable.bg_blue28);
////                      framet.setVisibility(View.GONE);
//                    }
//                } catch (Exception e) {
//                    Log.e("face_12312333", e.toString());
//                }
//
////                try {
////                    if (tjzt == 1) {
////                        tjzt = 0;
////                        registerStatus = REGISTER_STATUS_READY;
////                        registerFace(nv21fornow, facePreviewInfoListcj); //录入人脸信息
////                        Log.e("face_12312333", "face_123123" +"/456");
////                    }
////
////                } catch (Exception e) {
////
////                }
//
//                clearLeftFace(facePreviewInfoList);
//
//                if (facePreviewInfoList != null && facePreviewInfoList.size() > 0 && previewSize != null) {
//                    for (int i = 0; i < facePreviewInfoList.size(); i++) {
//                        Integer status = requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId());
//                        /**
//                         * 在活体检测开启，在人脸识别状态不为成功或人脸活体状态不为处理中（ANALYZING）且不为处理完成（ALIVE、NOT_ALIVE）时重新进行活体检测
//                         */
//                        if (livenessDetect && (status == null || status != RequestFeatureStatus.SUCCEED)) {
//                            Integer liveness = livenessMap.get(facePreviewInfoList.get(i).getTrackId());
//                            if (liveness == null || (liveness != LivenessInfo.ALIVE && liveness != LivenessInfo.NOT_ALIVE && liveness != RequestLivenessStatus.ANALYZING)) {
//                                livenessMap.put(facePreviewInfoList.get(i).getTrackId(), RequestLivenessStatus.ANALYZING);
//                                if (faceHelper!=null)
//                                faceHelper.requestFaceLiveness(nv21, facePreviewInfoList.get(i).getFaceInfo(), previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, facePreviewInfoList.get(i).getTrackId(), LivenessType.RGB);
//                            }
//                        }
//                        /**
//                         * 对于每个人脸，若状态为空或者为失败，则请求特征提取（可根据需要添加其他判断以限制特征提取次数），
//                         * 特征提取回传的人脸特征结果在{@link FaceListener#onFaceFeatureInfoGet(FaceFeature, Integer, Integer)}中回传
//                         */
//                        if (status == null || status == RequestFeatureStatus.TO_RETRY) {
//                            requestFeatureStatusMap.put(facePreviewInfoList.get(i).getTrackId(), RequestFeatureStatus.SEARCHING);
//                            if (faceHelper!=null)
//                            faceHelper.requestFaceFeature(nv21, facePreviewInfoList.get(i).getFaceInfo(), previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, facePreviewInfoList.get(i).getTrackId());
////                            Log.i(TAG, "onPreview: fr start = " + System.currentTimeMillis() + " trackId = " + facePreviewInfoList.get(i).getTrackedFaceCount());
//                        }
//                    }
//                }
//                }catch (Exception e){
//
//                }
//            }
//
//            @Override
//            public void onCameraClosed() {
//                Log.i(TAG, "onCameraClosed: ");
//            }
//
//            @Override
//            public void onCameraError(Exception e) {
//                Log.i(TAG, "onCameraError: " + e.getMessage());
//            }
//
//            @Override
//            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
//                if (drawHelper != null) {
//                    drawHelper.setCameraDisplayOrientation(displayOrientation);
//                }
//                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
//            }
//        };
//        Log.e("face_12312333", "face_123123" +"/777");
//        cameraHelper = new CameraHelper.Builder()
//                .previewViewSize(new Point(previewView.getMeasuredWidth(), previewView.getMeasuredHeight()))
//                .rotation(getWindowManager().getDefaultDisplay().getRotation())
//                .specificCameraId(rgbCameraID != null ? rgbCameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
//                .isMirror(false)
//                .previewOn(previewView)
//                .cameraListener(cameraListener)
//                .build();
//        cameraHelper.init();
//        cameraHelper.start();
//    }
//    int getCameradegrees(int CameraID){
//        Camera.CameraInfo info = new Camera.CameraInfo();
//        Camera.getCameraInfo(CameraID , info);
//        int rotation = DisplayUtil.getRotation(this);
//        int degrees = 0;
//        switch (rotation) {
//            case Surface.ROTATION_0:
//                degrees = 0;
//                break;
//            case Surface.ROTATION_90:
//                degrees = 0;
//                break;
//            case Surface.ROTATION_180:
//                degrees = -90;
//                break;
//            case Surface.ROTATION_270:
//                degrees = 180;
//                break;
//        }
//        Log.e(TAG, "getCameradegrees: "+rotation );
//        return degrees;
//
//    }
//    /**
//     * 将图像中需要截取的Rect向外扩张一倍，若扩张一倍会溢出，则扩张到边界，若Rect已溢出，则收缩到边界
//     *
//     * @param width   图像宽度
//     * @param height  图像高度
//     * @param srcRect 原Rect
//     * @return 调整后的Rect
//     */
//    private static Rect getBestRect(int width, int height, Rect srcRect) {
//
//        if (srcRect == null) {
//            return null;
//        }
//        Rect rect = new Rect(srcRect);
//        //1.原rect边界已溢出宽高的情况
//        int maxOverFlow = 0;
//        int tempOverFlow = 0;
//        if (rect.left < 0) {
//            maxOverFlow = -rect.left;
//        }
//        if (rect.top < 0) {
//            tempOverFlow = -rect.top;
//            if (tempOverFlow > maxOverFlow) {
//                maxOverFlow = tempOverFlow;
//            }
//        }
//        if (rect.right > width) {
//            tempOverFlow = rect.right - width;
//            if (tempOverFlow > maxOverFlow) {
//                maxOverFlow = tempOverFlow;
//            }
//        }
//        if (rect.bottom > height) {
//            tempOverFlow = rect.bottom - height;
//            if (tempOverFlow > maxOverFlow) {
//                maxOverFlow = tempOverFlow;
//            }
//        }
//        if (maxOverFlow != 0) {
//            rect.left += maxOverFlow;
//            rect.top += maxOverFlow;
//            rect.right -= maxOverFlow;
//            rect.bottom -= maxOverFlow;
//            return rect;
//        }
//        //2.原rect边界未溢出宽高的情况
//        int padding = rect.height() / 2;
//        //若以此padding扩张rect会溢出，取最大padding为四个边距的最小值
//        if (!(rect.left - padding > 0 && rect.right + padding < width && rect.top - padding > 0 && rect.bottom + padding < height)) {
//            padding = Math.min(Math.min(Math.min(rect.left, width - rect.right), height - rect.bottom), rect.top);
//        }
//
//        rect.left -= padding;
//        rect.top -= padding;
//        rect.right += padding;
//        rect.bottom += padding;
//        return rect;
//    }
//
//    private void registerFace(final byte[] nv21, final List<FacePreviewInfo> facePreviewInfoList) {
//        if (registerStatus == REGISTER_STATUS_READY && facePreviewInfoList != null && facePreviewInfoList.size() > 0) {
//            registerStatus = REGISTER_STATUS_PROCESSING;
//            Observable.create(new ObservableOnSubscribe<Boolean>() {
//                @Override
//                public void subscribe(ObservableEmitter<Boolean> emitter) {
//                    //registerNv21  存储人脸信息。顺便加到内存listface里面
//                    boolean success = FaceServer.getInstance().registerNv21(RegisterAndRecognizeActivity2.this, nv21.clone(), previewSize.width, previewSize.height,
//                            facePreviewInfoList.get(0).getFaceInfo(), name, id, type);
//                    if (success) {
//
//                    }
//                    emitter.onNext(success);
//                }
//            })
//                    .subscribeOn(Schedulers.computation())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<Boolean>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(Boolean success) {
//                            String result = success ? name + "本地录取成功" : name + "本地录取成功";
////                            Toast.makeText(RegisterAndRecognizeActivity.this, result, Toast.LENGTH_SHORT).show();
//                            registerStatus = REGISTER_STATUS_DONE;
//
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
////                            Toast.makeText(RegisterAndRecognizeActivity.this, "录取失败", Toast.LENGTH_SHORT).show();
//                            registerStatus = REGISTER_STATUS_DONE;
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
//        }
//    }
//    private void IsregisterFace(final byte[] nv21, final List<FacePreviewInfo> facePreviewInfoList) {
//        if (registerStatus == REGISTER_STATUS_READY && facePreviewInfoList != null && facePreviewInfoList.size() > 0) {
//            registerStatus = REGISTER_STATUS_PROCESSING;
//            Observable.create(new ObservableOnSubscribe<Boolean>() {
//                @Override
//                public void subscribe(ObservableEmitter<Boolean> emitter) {
//                    //registerNv21  存储人脸信息。顺便加到内存listface里面
//                    boolean success = FaceServer.getInstance().IsCanregisterNv21(RegisterAndRecognizeActivity2.this, nv21.clone(), previewSize.width, previewSize.height,
//                            facePreviewInfoList.get(0).getFaceInfo(), name, id, type);
//                    if (success) {
//
//
////                        Log.e(TAG, "subscribe: "+JSON.toJSONString(faceData) );
//
//                    }
//                    emitter.onNext(success);
//                }
//            })
//                    .subscribeOn(Schedulers.computation())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<Boolean>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(Boolean success) {
//                            String result = success ? name + "本地录取成功" : name + "本地录取成功";
////                            Toast.makeText(RegisterAndRecognizeActivity.this, result, Toast.LENGTH_SHORT).show();
//                            registerStatus = REGISTER_STATUS_DONE;
//
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
////                            Toast.makeText(RegisterAndRecognizeActivity.this, "录取失败", Toast.LENGTH_SHORT).show();
//                            registerStatus = REGISTER_STATUS_DONE;
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
//        }
//    }
//    //    private SimpleDateFormat dff;
////    private  String datedff;
//    private void drawPreviewInfo(List<FacePreviewInfo> facePreviewInfoList) {
//        List<DrawInfo> drawInfoList = new ArrayList<>();
//        for (int i = 0; i < facePreviewInfoList.size(); i++) {
//            if (faceHelper!=null){
//                String name = faceHelper.getName(facePreviewInfoList.get(i).getTrackId());
//            }
//
//            Integer liveness = livenessMap.get(facePreviewInfoList.get(i).getTrackId());
//            Integer recognizeStatus = requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId());
//
//            // 根据识别结果和活体结果设置颜色
//            int color = RecognizeColor.COLOR_UNKNOWN;
//            if (recognizeStatus != null) {
//                if (recognizeStatus == RequestFeatureStatus.FAILED) {
//                    color = RecognizeColor.COLOR_FAILED;
//                }
//                if (recognizeStatus == RequestFeatureStatus.SUCCEED) {
//                    color = RecognizeColor.COLOR_SUCCESS;
//                }
//            }
//            if (liveness != null && liveness == LivenessInfo.NOT_ALIVE) {
//                color = RecognizeColor.COLOR_FAILED;
//            }
//
////            dff = new SimpleDateFormat("HH:mm:ss:SSS");//获取年月
////            dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));//时间格式
////            datedff = dff.format(new Date());
////            Toast.makeText(RegisterAndRecognizeActivity.this,dff.format(new Date())+"", Toast.LENGTH_LONG).show();
//            //绘制人脸框
//            drawInfoList.add(new DrawInfo(drawHelper.adjustRect(facePreviewInfoList.get(i).getFaceInfo().getRect()),
//                    GenderInfo.UNKNOWN, AgeInfo.UNKNOWN_AGE, liveness == null ? LivenessInfo.UNKNOWN : liveness, color,
//                    name == null ? String.valueOf(facePreviewInfoList.get(i).getTrackId()) : name));
////            if (registerStatus == REGISTER_STATUS_DONE) {
////                registerStatus = REGISTER_STATUS_READY;
////            }
//        }
//        drawHelper.draw(faceRectView, drawInfoList);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
//            boolean isAllGranted = true;
//            for (int grantResult : grantResults) {
//                isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
//            }
//            if (isAllGranted) {
//                initEngine();
//                initCamera();
//            } else {
//                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    /**
//     * 删除已经离开的人脸
//     *
//     * @param facePreviewInfoList 人脸和trackId列表
//     */
//    private void clearLeftFace(List<FacePreviewInfo> facePreviewInfoList) {
//        if (compareResultList != null) {
//            for (int i = compareResultList.size() - 1; i >= 0; i--) {
//                if (!requestFeatureStatusMap.containsKey(compareResultList.get(i).getTrackId())) {
//                    compareResultList.remove(i);
//                    adapter.notifyItemRemoved(i);
//                }
//            }
//        }
//        if (facePreviewInfoList == null || facePreviewInfoList.size() == 0) {
//            requestFeatureStatusMap.clear();
//            livenessMap.clear();
//            livenessErrorRetryMap.clear();
//            extractErrorRetryMap.clear();
//            if (getFeatureDelayedDisposables != null) {
//                getFeatureDelayedDisposables.clear();
//            }
//            return;
//        }
//        Enumeration<Integer> keys = requestFeatureStatusMap.keys();
//        while (keys.hasMoreElements()) {
//            int key = keys.nextElement();
//            boolean contained = false;
//            for (FacePreviewInfo facePreviewInfo : facePreviewInfoList) {
//                if (facePreviewInfo.getTrackId() == key) {
//                    contained = true;
//                    break;
//                }
//            }
//            if (!contained) {
//                requestFeatureStatusMap.remove(key);
//                livenessMap.remove(key);
//                livenessErrorRetryMap.remove(key);
//                extractErrorRetryMap.remove(key);
//            }
//        }
//
//
//    }
//
//
//    private void searchFace(final FaceFeature frFace, final Integer requestId) {
//        Observable
//                .create(new ObservableOnSubscribe<CompareResult>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<CompareResult> emitter) {
//                        Log.i(TAG, "subscribe: fr search start = " + System.currentTimeMillis() + " trackId = " + requestId);
//                        CompareResult compareResult = FaceServer.getInstance().getTopOfFaceLib(frFace);  //获取循环比对结果
////                        Log.i(TAG, "subscribe: fr search end = " + System.currentTimeMillis() + " trackId = " + requestId);
//                        emitter.onNext(compareResult);
//
//                    }
//                })
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<CompareResult>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(CompareResult compareResult) {
//                        if (compareResult == null || compareResult.getUserName() == null) {
//                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
//                            if (faceHelper!=null)
//                            faceHelper.setName(requestId, "VISITOR " + requestId);
//                            return;
//                        }
//
//                        Log.i(TAG, "onNext: fr search get result  = " + System.currentTimeMillis() + " trackId = " + requestId + "  similar = " + compareResult.getSimilar());
//                        if (compareResult.getSimilar() > SIMILAR_THRESHOLD) {
////                            button_djcc.setVisibility(View.VISIBLE);
//
//                            boolean isAdded = false;
//                            if (compareResultList == null) {
//                                requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
//                                if (faceHelper!=null)
//                                faceHelper.setName(requestId, "VISITOR " + requestId);
//                                return;
//                            }
//                            for (CompareResult compareResult1 : compareResultList) {
//                                if (compareResult1.getTrackId() == requestId) {
//                                    isAdded = true;
//                                    break;
//                                }
//                            }
//                            if (!isAdded) {
//                                //对于多人脸搜索，假如最大显示数量为 MAX_DETECT_NUM 且有新的人脸进入，则以队列的形式移除
//                                if (compareResultList.size() >= MAX_DETECT_NUM) {
//                                    compareResultList.remove(0);
//                                    adapter.notifyItemRemoved(0);
//                                }
//                                //添加显示人员时，保存其trackId
//                                compareResult.setTrackId(requestId);
//                                compareResultList.add(compareResult);
//                                adapter.notifyItemInserted(compareResultList.size() - 1);
//
////
//                                textView.setVisibility(View.GONE);
//                                button_djcc.setVisibility(View.VISIBLE);
//                                button_tj.setVisibility(View.GONE);
//
//                            }
//
//                            if (ztcj == 0 || button_djcc.getText().toString().equals("点击采集")) {
//                                textView.setVisibility(View.VISIBLE);
//                                textView.setText("人脸采集失败（该人脸已被" + compareResultList.get(0).getUserName() + "采集)");
//                                textView.setTextColor(Color.parseColor("#FF3D3D"));
//                                button_djcc.setVisibility(View.GONE);
//                                button_tj.setVisibility(View.GONE);
//                                abcde = 1;
//                            }
//                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.SUCCEED);
//                            if (faceHelper!=null)
//                            faceHelper.setName(requestId, getString(R.string.recognize_success_notice, compareResult.getUserName()));
//
//                        } else {
//                            if (faceHelper!=null)
//                            faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, "!未通过"));
//                            retryRecognizeDelayed(requestId);
//                            textView.setVisibility(View.GONE);
//                            button_djcc.setVisibility(View.VISIBLE);
//                            button_tj.setVisibility(View.VISIBLE);
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        try {
//                            if (faceHelper!=null)
//                            faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, "!未通过"));
//                            retryRecognizeDelayed(requestId);
//                        } catch (Exception e12) {
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//                });
//    }
//
//
//    /**
//     * 将准备注册的状态置为{@link #REGISTER_STATUS_READY}
//     *
//     * @param view 注册按钮
//     */
//    public void register(View view) {
//        if (registerStatus == REGISTER_STATUS_DONE) {
//            registerStatus = REGISTER_STATUS_READY;
//        }
//    }
//
//    /**
//     * 切换相机。注意：若切换相机发现检测不到人脸，则及有可能是检测角度导致的，需要销毁引擎重新创建或者在设置界面修改配置的检测角度
//     *
//     * @param view
//     */
//    public void switchCamera(View view) {
//        if (cameraHelper != null) {
//            boolean success = cameraHelper.switchCamera();
//            if (!success) {
//                Toast.makeText(this, getString(R.string.switch_camera_failed), Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, getString(R.string.notice_change_detect_degree), Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    /**
//     * 在{@link #previewView}第一次布局完成后，去除该监听，并且进行引擎和相机的初始化
//     */
//    @Override
//    public void onGlobalLayout() {
//        previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//        if (!checkPermissions(NEEDED_PERMISSIONS)) {
//            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
//        } else {
//            initEngine();
//            initCamera();
//        }
//    }
//
//    /**
//     * 将map中key对应的value增1回传
//     *
//     * @param countMap map
//     * @param key      key
//     * @return 增1后的value
//     */
//    public int increaseAndGetValue(Map<Integer, Integer> countMap, int key) {
//        if (countMap == null) {
//            return 0;
//        }
//        Integer value = countMap.get(key);
//        if (value == null) {
//            value = 0;
//        }
//        countMap.put(key, ++value);
//        return value;
//    }
//
//    /**
//     * 延迟 FAIL_RETRY_INTERVAL 重新进行活体检测
//     *
//     * @param requestId 人脸ID
//     */
//    private void retryLivenessDetectDelayed(final Integer requestId) {
//     //   Log.e("活体检测", "retryLivenessDetectDelayed" + "非活体重新检测"+getLivenessFace);
//        Observable.timer(FAIL_RETRY_INTERVAL, TimeUnit.MILLISECONDS)
//                .subscribe(new Observer<Long>() {
//                    Disposable disposable;
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        disposable = d;
//                        delayFaceTaskCompositeDisposable.add(disposable);
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        // 将该人脸状态置为UNKNOWN，帧回调处理时会重新进行活体检测
//                        if (livenessDetect) {
//                            if (faceHelper!=null)
//                            faceHelper.setName(requestId, Integer.toString(requestId));
//                        }
//                        livenessMap.put(requestId, LivenessInfo.UNKNOWN);
//                        delayFaceTaskCompositeDisposable.remove(disposable);
//                        getLivenessFace=0;
//                    }
//                });
//    }
//
//    /**
//     * 延迟 FAIL_RETRY_INTERVAL 重新进行人脸识别
//     *
//     * @param requestId 人脸ID
//     */
//    private void retryRecognizeDelayed(final Integer requestId) {
//        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
//        Observable.timer(FAIL_RETRY_INTERVAL, TimeUnit.MILLISECONDS)
//                .subscribe(new Observer<Long>() {
//                    Disposable disposable;
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        disposable = d;
//                        delayFaceTaskCompositeDisposable.add(disposable);
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                  //      Log.e("活体检测","retryRecognizeDelayed_onError");
//                        getLivenessFace=0;
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        // 将该人脸特征提取状态置为FAILED，帧回调处理时会重新进行活体检测
//                        if (faceHelper!=null)
//                        faceHelper.setName(requestId, Integer.toString(requestId));
//                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.TO_RETRY);
//                        delayFaceTaskCompositeDisposable.remove(disposable);
//                     //   Log.e("活体检测","retryRecognizeDelayed_onComplete");
//                        getLivenessFace=0;
//                    }
//                });
//    }
//
//    SweetAlertDialog pd;
//
//    void voice(byte[] str, Bitmap arcfacebitmap) {
//        try {
//            byte[] base64 = FaceServer.getInstance().registerNv22(this, str.clone(), previewSize.width, previewSize.height,
//                    facePreviewInfoListcj.get(0).getFaceInfo(), name, id, type);
//            pd.show();
//            byte[] base = Base64.encode(base64, 2);
//            String faceToken = new String(base);
//            byte[] featrue123 = Base64.decode(faceToken, 2);
//            Log.e("strbyte", str.length + "///" + faceToken.length() + "////" + featrue123.length);
//            Log.e("strbyte", faceToken);
//
//            VoicefaceReq req = new VoicefaceReq();
////        req.numb=faceToken.length();
//            req.officeId = SpData.User().data.officeId;
////            req.classId = SpData.User().classesId;
//            try {
//                req.name = URLEncoder.encode(name, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            if (type.equals("1")) {
//                req.studentId = id;
//            } else if (type.equals("2")) {
//                req.teacherId = id;
//            }
//            req.photo = convertToString(arcfacebitmap);
//            req.facialFeatures = faceToken; //zhege
////        req.voiceContent = URLEncoder.encode(str);
//            MyApp.getInstance().requestDataHRFace(this, req, new voiceListener(), new voiceError());
//        } catch (Exception e) {
//            pd.dismiss();
//            ToastUtils.showShort("人脸清晰度不高，请重新采集!");
//            button_tj.setVisibility(View.GONE);
//            insertface = 0;
//        }
//    }
//
//    //String 转Bitmap
//    public Bitmap GenerateImage(String imgStr) {
//        byte[] decodeBytes = Base64.decode(imgStr.getBytes(), Base64.DEFAULT);
//        Bitmap bmp = BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.length);
//        return bmp;
//    }
//
//    //图片转化成Base64字符串
//    public String convertToString(Bitmap bmp) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
//        byte[] bts = baos.toByteArray();
//        return Base64.encodeToString(bts, Base64.DEFAULT);
//    }
//
//    class voiceListener implements Response.Listener<VoicefacesynRsp> {
//        @Override
//        public void onResponse(VoicefacesynRsp rsp) {
//            pd.dismiss();
//            Log.e("jsonforxp", rsp.status + "");
//            insertface = 0;
//            if (rsp.status == BaseConstant.REQUEST_SUCCES && !TextUtils.isEmpty(rsp.data)) {
//                Log.e("jsonforxp", rsp.status + "");
//                //这里服务器没返回吗   返回是成工  和失败的 就
//                if (type.equals("1") || type.equals("2")) {
////                    RegisterAndRecognizeActivity2.this.finish();//录取成功后 关闭当前识别页。
////                    PostData(id);
//                    isClose=true;
//                    finish();
//
//                }
//            }
//            Toast.makeText(RegisterAndRecognizeActivity2.this, rsp.info, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    class voiceError implements Response.ErrorListener {
//        @Override
//        public void onErrorResponse(VolleyError volleyError) {
//            pd.dismiss();
//            Log.e("jsonforxperror", volleyError.toString());
//        }
//    }
//
//
//    void PostData(String visitorId) {
//        pd.show();
//        TmpVisitorAddReq req = new TmpVisitorAddReq();
//        req.tmpVisitorId=visitorId;
//        req.officeId = SpData.User().data.officeId;
//        int times;
//
//        if (TextUtils.isEmpty(bean.time)){
//            times=0;
//        }else {
//            times = Integer.parseInt(bean.time);
//        }
//
//        long mils = times * 60 * 1000;
//        long now = System.currentTimeMillis();
//        long endT = now + mils;
//        long startT = now;
//        String startTime = Tool.stampToDate(String.valueOf(startT));
//        String endTime = Tool.stampToDate(String.valueOf(endT));
//
//        try {
//            req.name = URLEncoder.encode(bean.name, "UTF-8");
//            req.identity = URLEncoder.encode(bean.Num, "UTF-8");
//            req.phone = URLEncoder.encode(bean.phone, "UTF-8");
//            req.teacherName = URLEncoder.encode(bean.yourname, "UTF-8");
//
//            req.startTime = startTime;
//            req.endTime = endTime;
//            req.concerned = URLEncoder.encode(bean.dosome, "UTF-8");
//
////            req.visitDuration= URLEncoder.encode(bean.time, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        MyApp.getInstance().requestDataPost(this, req, new listener(), new errorListener());
//    }
//
//    class listener implements Response.Listener<TmpVisitorAddRsp> {
//
//        @Override
//        public void onResponse(TmpVisitorAddRsp rsp) {
//            pd.dismiss();
//            if (rsp.status == BaseConstant.REQUEST_SUCCES) {
//                Log.e(TAG, "TmpVisitorAddRsp: "+JSON.toJSONString(rsp) );
////                adapter.notifyData(rsp.data);
//                ToastUtils.showShort("登记成功");
//                isClose=true;
//                finish();
//
////                ActivityUtils.finishActivity(TemporaryActivity.class);
//
////                FaceRegistActivity.this.finish();
////                finishActivity();
////                MinaEventType minaEventType = new MinaEventType();
////                minaEventType.i = 10;
////                EventBus.getDefault().post(minaEventType);
//            }
//        }
//    }
//
//    class errorListener implements Response.ErrorListener {
//
//        @Override
//        public void onErrorResponse(VolleyError volleyError) {
//            ToastUtils.showShort("申请失败");
//            isClose=true;
//            pd.dismiss();
//
//            finish();
//        }
//    }
//
//
//    public class IssusscsBD extends BroadcastReceiver{
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(ISSUCCES)){
//
//                voice(nv21fornow, bitmap_arcface);
//            }
//        }
//    }
//}