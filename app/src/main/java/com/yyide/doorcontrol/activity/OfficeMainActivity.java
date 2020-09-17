package com.yyide.doorcontrol.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.adapter.RecyleviewIndexAdapter;
import com.yyide.doorcontrol.adapter.ViewPagerFragmentAdapter;
import com.yyide.doorcontrol.base.BaseActivity;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.fragment.AppointmentSettingFragment;
import com.yyide.doorcontrol.fragment.OfficeHomeFragment;

import com.yyide.doorcontrol.fragment.OfficeMenuFragment;
import com.yyide.doorcontrol.hongruan.common.Constants;
import com.yyide.doorcontrol.hongruan.db.DbController;
import com.yyide.doorcontrol.hongruan.db.PersonInfor;
import com.yyide.doorcontrol.hongruan.faceserver.FaceServer;
import com.yyide.doorcontrol.hongruan.util.ConfigUtil;
import com.yyide.doorcontrol.requestbean.SaveFaceUpdateReq;
import com.yyide.doorcontrol.rsponbean.SaveFaceUpdateRsp;
import com.yyide.doorcontrol.utils.SDcarfile;
import com.yyide.doorcontrol.utils.SpaceItemDecoration;
import com.yyide.doorcontrol.utils.TimeUtil;

import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OfficeMainActivity extends BaseActivity {

    RecyleviewIndexAdapter indexAdapter;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.recyleview)
    RecyclerView recyleview;
    private static DbController mDbController;
    private PersonInfor personInfor1;//数据库模板
    public static boolean ispost = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_main);
        ButterKnife.bind(this);

        indexAdapter = new RecyleviewIndexAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyleview.setLayoutManager(linearLayoutManager);

        recyleview.addItemDecoration(new SpaceItemDecoration(8));
        recyleview.setAdapter(indexAdapter);

        pager.setOffscreenPageLimit(0);
        initFragmist();
        mDbController = DbController.getInstance(this);
//        byte[] aaa = {1, 1, 1, 1, 1, 1,};
//        personInfor1 = new PersonInfor(null, "430981199403208315", aaa, "xp", "男", "1");
//        mDbController.update(personInfor1);//本地库 添加人脸信息 如果存在。进行update 无则添加
        ConfigUtil.setFtOrient(this, FaceEngine.ASF_OP_0_HIGHER_EXT);//设置人脸框填充

        activeEngine(null);  //激活引擎
//        FaceServer.getInstance().init(this);

    }
    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    private static final String TAG = "MainActivity";
    private Toast toast = null;
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    private FaceEngine faceEngine = new FaceEngine();

    public void activeEngine(final View view) {
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return;
        }
        if (view != null) {
            view.setClickable(false);
        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                int activeCode = faceEngine.activeOnline(OfficeMainActivity.this, Constants.APP_ID, Constants.SDK_KEY);
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
                            showToast(getString(R.string.active_success));
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            showToast(getString(R.string.already_activated));
                        } else {
                            showToast(getString(R.string.active_failed, activeCode));
                        }

                        if (view != null) {
                            view.setClickable(true);
                        }
                        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
                        int res = faceEngine.getActiveFileInfo(OfficeMainActivity.this, activeFileInfo);
                        if (res == ErrorInfo.MOK) {
                            Log.i(TAG, activeFileInfo.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private void showToast(String s) {
        if (toast == null) {
            toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(s);
            toast.show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getfacefeature();
    }

    public void initFragmist() {

        List<Fragment> mFragmentList = new ArrayList<>();
        FragmentManager fm = getSupportFragmentManager();

        int num = 1;
        indexAdapter.notifydata(num + 1);
        pager.setOffscreenPageLimit(num);

        OfficeHomeFragment home = new OfficeHomeFragment();
        mFragmentList.add(home);
        OfficeMenuFragment menu = new OfficeMenuFragment();//班牌最后一页所有按钮
        mFragmentList.add(menu);

        ViewPagerFragmentAdapter pagerAdapter = new ViewPagerFragmentAdapter(fm, mFragmentList);
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                indexAdapter.setPosition(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void getfacefeature() {
        ispost = false;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        String datetime;
        if (null != SDcarfile.read("arcfacetime") && !SDcarfile.read("arcfacetime").equals("") && mDbController.searchAll().size() > 0) {
            datetime = SDcarfile.read("arcfacetime");
        } else {
            datetime = "2019-12-12 00:00:00";
        }
        getface(datetime);//读取上次同步时间戳
        SDcarfile.save("arcfacetime", str);//写入当前时间戳。
        Log.e("同步人脸的时间", "当前时间str:" + str + "/datetime：" + datetime);
    }

    //字符串转时间戳
    public static String getTime(String timeString) {
        String timeStamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
        Date d;
        try {
            d = sdf.parse(timeString);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    public void getface(String datetime) { //人脸同步

        Log.e("同步人脸的时间", "datetime:" + datetime);

        SaveFaceUpdateReq req = new SaveFaceUpdateReq();
        req.officeId = SpData.User().data.officeId;
//        req.officeId = 16955;
        req.updateDate = datetime;
        MyApp.getInstance().requestDataHRFace(this, req, new dbListener(), new dbError());
    }

    class dbListener implements Response.Listener<SaveFaceUpdateRsp> {
        @Override
        public void onResponse(SaveFaceUpdateRsp rsp) {
            Log.e("人脸识别", JSON.toJSONString(rsp));
            Log.e("SaveFaceUpdateRsp",rsp.data.size()+"");
            if (rsp.status == BaseConstant.REQUEST_SUCCES2) {

                if (rsp.data.size()==0){
                    byte[] aaa = {1, 1, 1, 1, 1, 1,};
                    personInfor1 = new PersonInfor(null, "430981199403208315", aaa, "xp", "男", "1");
                    mDbController.update(personInfor1);//本地库 添加人脸信息 如果存在。进行update 无则添加
                }
//                URLDecoder.decode(str, "utf-8");  //还原encode

                try {
                    for (int i = 0; i < rsp.data.size(); i++) {
                        String newfacetrue = rsp.data.get(i).facialFeatures.replace(" ", "+");
                        byte[] featrue = Base64.decode(newfacetrue, 2);
//                        Log.e("xupengper", "name:" + rsp.data.get(i).name + " /studentId:" + rsp.data.get(i).studentId + " /teacherId:" + rsp.data.get(i).teacherId +
//                                " /delFlag:" + rsp.data.get(i).delFlag + "/photo:" + rsp.data.get(i).photo + "/size:" + rsp.data.size());
                        if (rsp.data.get(i).delFlag == 0) { //判断是否是新增人脸
                            if (rsp.data.get(i).teacherId == null || rsp.data.get(i).teacherId.equals("") || rsp.data.get(i).teacherId.equals("null")) {
                                personInfor1 = new PersonInfor(null, rsp.data.get(i).studentId, featrue, URLDecoder.decode(rsp.data.get(i).name, "utf-8"), "男", "1");
                                mDbController.update(personInfor1);//本地库 添加人脸信息 如果存在。进行update 无则添加
                            } else {
                                personInfor1 = new PersonInfor(null, rsp.data.get(i).teacherId, featrue, URLDecoder.decode(rsp.data.get(i).name, "utf-8"), "男", "0");
                                mDbController.update(personInfor1);//本地库 添加人脸信息
                            }
                        } else if (rsp.data.get(i).delFlag == 1) { //判断解绑人脸
                            if (rsp.data.get(i).teacherId == null || rsp.data.get(i).teacherId.equals("") || rsp.data.get(i).teacherId.equals("null")) {
                                mDbController.delete(rsp.data.get(i).studentId); //删除老师id对应的人脸值
                            } else {
                                mDbController.delete(rsp.data.get(i).teacherId);//删除学生id对应的人脸值
                            }
                        }
//                        FaceRegisterInfo info=new FaceRegisterInfo(featrue, URLDecoder.decode(rsp.data.get(i).name, "utf-8"),rsp.data.get(i).teacherId,Long.valueOf(i+123),"111");
//                        FaceServer.getInstance().addfaceregis(info);//
                    }
                } catch (Exception e) {
                    Log.e("compare", e.toString());
                }
                showDataList();
                ispost = true;
//                bean=rsp;
//                myAsyncTask.execute(100);

            } else {
                Log.e("compare", rsp.info + "");
                ispost = true;
                showToast(rsp.info);
            }
//            pd.dismiss();
        }
    }

    private void showDataList() {
        StringBuilder sb = new StringBuilder();
        List<PersonInfor> personInfors = mDbController.searchAll();
        for (PersonInfor personInfor : personInfors) {
            sb.append("id:").append(personInfor.getId())
                    .append("perNo:").append(personInfor.getPerNo())
                    .append("name:").append(personInfor.getName())
                    .append("sex:").append(personInfor.getSex())
                    .append("featrue:").append(personInfor.getFeatrue())
                    .append("time:").append(personInfor.getTime())
                    .append("\n");
//            Log.i("xupengface",sb.toString());
        }
    }

    class dbError implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            ispost = true;

//            pd.dismiss();
        }
    }


}
