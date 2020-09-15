package com.yyide.doorcontrol.mina;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arcsoft.imageutil.ArcSoftImageFormat;
import com.arcsoft.imageutil.ArcSoftImageUtil;
import com.arcsoft.imageutil.ArcSoftImageUtilError;
import com.blankj.utilcode.util.FileUtils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.R;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.brocast.MinaMsg;
import com.yyide.doorcontrol.dialog.LockDialog;
import com.yyide.doorcontrol.hongruan.db.DbController;
import com.yyide.doorcontrol.hongruan.faceserver.FaceServer;
import com.yyide.doorcontrol.hongruan.faceserver.PicResult;
import com.yyide.doorcontrol.requestbean.GetFacePhotoByUpdateDateReq;
import com.yyide.doorcontrol.requestbean.UntyingfaceDataReq;
import com.yyide.doorcontrol.requestbean.UpdateFacePhotoReq;
import com.yyide.doorcontrol.rsponbean.GetFacePhotoByUpdateDateRsp;
import com.yyide.doorcontrol.rsponbean.ImgFaceRsp;
import com.yyide.doorcontrol.rsponbean.UntyingfaceDataRsp;
import com.yyide.doorcontrol.rsponbean.UpdateFacePhotoRsp;
import com.yyide.doorcontrol.utils.L;


import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MinaClientHandler extends IoHandlerAdapter {

    static Activity context;
    private static DbController mDbController;


    //以下批量注册所用
    private ExecutorService executorService;

    //注册图所在的目录
    private static final String ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ArcfaceVisitor";
//    private static final String ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "arcfacedemo";
    private static final String REGISTER_DIR = ROOT_DIR + File.separator + "register";
    private static final String REGISTER_FAILED_DIR = ROOT_DIR + File.separator + "failed";


    List<UpdateFacePhotoReq.UpdateBean> list=new ArrayList<>();
    public  int count;
    GetFacePhotoByUpdateDateRsp bean;

    public static LockDialog dialog;
    ImgFaceRsp imgrsp;
//    String str="{\"msgType\":\"24\",\"data\":\"[{\\\"personName\\\":\\\"刘义\\\",\\\"officeId\\\":\\\"16971\\\",\\\"crop_name\\\":\\\"abc.jpg\\\",\\\"photo\\\":\\\"http://we.yyide.com:8077//static/visitor/image/20200701/1593583673478140628.jpg\\\",\\\"id\\\":\\\"094b6db9aca64217a553aba04a6145d7\\\"}]\"}";

    public MinaClientHandler(Activity context) {
        this.context = context;
        if(mDbController==null) {
            mDbController = DbController.getInstance(context);
        }
        if(executorService==null){
            executorService = Executors.newSingleThreadExecutor();
        }


    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        Log.d("IDLE", MinaMsg.MinaMsg(BaseConstant.MINA_IDLE));
        SessionManager.getInstance().writeToServer(MinaMsg.MinaMsg(BaseConstant.MINA_IDLE));
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String msg = message.toString();
        Log.e("TAG", "messageReceived: "+msg );
        Received(msg);
        super.messageReceived(session, message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }

    private void Received(String msg) {
        try {
            String jsonStr = msg.replaceAll("\\\\n", "\n")
                    .replaceAll("\\\\r", "\r").replaceAll("\\\\", "")
                    .replace("\"{", "{").replace("}\"", "}")
                    .replace("\"[", "[").replace("]\"", "]").toString();
            L.j(jsonStr);
            JSONObject jo = new JSONObject(jsonStr);
            int msgType = jo.getInt("msgType");
            switch (msgType) {
                //开关机
                case 4:
//                    PowerOnBean powerOnBean = JSON.parseObject(jsonStr, PowerOnBean.class);
//                    Log.e("TAG", "powerOnBean: " + powerOnBean);
//                    if (powerOnBean.data.size() > 0)
//                        SPUtil.put(SpData.POWEROFFON, jsonStr);
//                    SpData.timePower();
                    break;
                case 20:
//                    BdFaceAllRsp bdAllRsp = JSON.parseObject(jsonStr, BdFaceAllRsp.class);
//                    String newPath = "/data/data/com.yyide.visitor/databases/";
//                    L.d(GlideUtil.DataUrl(bdAllRsp.data));
//                    boolean isCreate = FileUtils.createOrExistsDir(newPath);
//                    if (isCreate) {
//                        File file = new File(newPath, "bdface.db");
//                        DBManager.getInstance().release();
//                        L.d(GlideUtil.DBUrl(bdAllRsp.data));
//                        Ion.with(MyApp.getInstance()).load(GlideUtil.DBUrl(bdAllRsp.data)).setTimeout(10000)
//                                .write(file).setCallback(new FutureCallback<File>() {
//                            @Override
//                            public void onCompleted(Exception arg0, File arg1) {
//                                if (arg0 != null) {
//                                    L.d(arg0.getMessage());
//                                    L.d("失败");
//                                } else {
//                                    L.d("成功");
//                                }
//                                DBManager.getInstance().init(context);
//                                FaceSDKManager.getInstance().setFeature();
//                            }
//                        });
//                    }
                    break;
                case 21:
//                    BdFeactureRsp rsp = JSON.parseObject(jsonStr, BdFeactureRsp.class);
//                    for (Feature datum : rsp.data) {
//                        Feature temp = datum;
//                        temp.setFeature(Base64.decode(datum.getFaceToken(), 2));
//                        DBManager.getInstance().addOrUpdateUser(datum);
//                    }
//                    FaceSDKManager.getInstance().setFeature();
                    break;
                case 22:


//                    DeleteFaceRsp faceRsp = JSON.parseObject(jsonStr, DeleteFaceRsp.class);
//                    mDbController.delete(faceRsp.data);

                    break;

                case 24:

//                    WeiXinFaceRsp bean = JSON.parseObject(jsonStr, WeiXinFaceRsp.class);
//                    Log.e("TAG", "WeiXinFaceRsp: "+JSON.toJSONString(bean) );
//                    downLoadImgWechat(bean.data.get(0).photo,bean.data.get(0).personName,bean.data.get(0).id);

                    break;
                //更新的推送
                case 10:
                    MinaEventType minaEventType = new MinaEventType();
                    minaEventType.i = jo.getInt("msgType");
                    EventBus.getDefault().post(minaEventType);
                    Log.e("TAG", "Received: " + JSON.toJSON(minaEventType));
                    break;
                case 33:
                    //批量注册

                     imgrsp = JSON.parseObject(jsonStr, ImgFaceRsp.class);
//                    Message message=new Message();
                    getImgs(imgrsp.data.date);
//                    handler.sendEmptyMessage(1);


                    break;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    void getImgs(String time) {
        GetFacePhotoByUpdateDateReq req = new GetFacePhotoByUpdateDateReq();
        req.time=time;
//        req.officeId= SpData.User().data.officeId;
        MyApp.getInstance().requestDataPost(this, req, new AuthListener(), new Error());
    }

    class AuthListener implements Response.Listener<GetFacePhotoByUpdateDateRsp> {

        @Override
        public void onResponse(GetFacePhotoByUpdateDateRsp rsp) {

            if (rsp.status == BaseConstant.REQUEST_SUCCES) {

                Log.e("TAG", "GetFacePhotoByUpdateDateRsp: "+JSON.toJSONString(rsp) );
                if (rsp.data.size()>0){
                    myHandler.sendEmptyMessage(1);
                count=0;
                list.clear();
                bean=rsp;



                for (GetFacePhotoByUpdateDateRsp.DataBean datum : rsp.data) {
                    if (TextUtils.isEmpty(datum.studentId)){
                        if (TextUtils.isEmpty(datum.teacherName)){
                            downLoadImg(datum.photo,datum.studentName,datum.id);
                        }else {
                            downLoadImg(datum.photo,datum.teacherName,datum.id);
                        }
//                        downLoadImg(datum.photo,datum.teacherId,datum.id);
                    }else {
                        if (TextUtils.isEmpty(datum.teacherName)){
                            downLoadImg(datum.photo,datum.studentName,datum.id);
                        }else {
                            downLoadImg(datum.photo,datum.teacherName,datum.id);
                        }
//                        downLoadImg(datum.photo,datum.studentId,datum.id);

                    }

                }
                }
//                doRegister();

            }
        }
    }
    class Error implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            myHandler.sendEmptyMessage(2);
        }
    }

    class Error3 implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }

    UpdateFacePhotoReq.UpdateBean setImgListType(String id,String type,String error){
        UpdateFacePhotoReq.UpdateBean bean=new UpdateFacePhotoReq.UpdateBean();
        bean.id=id;
        bean.type=type;
        bean.error=type;
        bean.officeId=SpData.User().data.officeId;
        try {
            bean.error= URLEncoder.encode(error, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return bean;
    }

    void UpDataImgmsg() {
        UpdateFacePhotoReq req = new UpdateFacePhotoReq();
        req.list=list;

        MyApp.getInstance().requestDataPost(this, req, new Listener778899(), new Error3());

    }
    class Listener778899 implements Response.Listener<UpdateFacePhotoRsp> {

        @Override
        public void onResponse(UpdateFacePhotoRsp rsp) {

            if (rsp.status == BaseConstant.REQUEST_SUCCES) {
//                Log.e("TAG", "onResponse: " );
            }
        }
    }


    String getIDs(String tsid){
        String id=null;
        if (bean!=null){

            for (GetFacePhotoByUpdateDateRsp.DataBean datum : bean.data) {
                //匹配出id
                if (datum.id.equals(tsid)){
                    id=datum.id;
                }

            }
        }
        return id;
    }


    String getIDsName(String tsid){
        String name=null;
        if (bean!=null){

            for (GetFacePhotoByUpdateDateRsp.DataBean datum : bean.data) {
                //匹配出id
                if (datum.id.equals(tsid)){
                    if (!TextUtils.isEmpty(datum.studentName)){
                        name=datum.studentName;
                    }else {
                        name=datum.teacherName;
                    }


                }
            }
        }
        return name;
    }

    String getIDsTecherId(String tsid){
        String teacherId="";
        if (bean!=null){

            for (GetFacePhotoByUpdateDateRsp.DataBean datum : bean.data) {
                //匹配出id
                if (datum.id.equals(tsid)){
                    if (!TextUtils.isEmpty(datum.teacherId)){
                        teacherId=datum.teacherId;
                    }
                }
            }
        }
        return teacherId;
    }
    String getIDsStudentId(String tsid){
        String studentId="";
        if (bean!=null){

            for (GetFacePhotoByUpdateDateRsp.DataBean datum : bean.data) {
                //匹配出id
                if (datum.id.equals(tsid)){
                    if (!TextUtils.isEmpty(datum.studentId)){
                        studentId=datum.studentId;
                    }
                }
            }
        }
        return studentId;
    }
    private void  doRegister() {

        final File dir = new File(REGISTER_DIR+File.separator);
        if (!dir.exists()) {
            showToast(context.getString(R.string.batch_process_path_is_not_exists, REGISTER_DIR));

        }
        if (!dir.isDirectory()) {
            showToast(context.getString(R.string.batch_process_path_is_not_dir, REGISTER_DIR));

        }
        final File[] jpgFiles = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {


                return name.endsWith(FaceServer.IMG_SUFFIX);
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final int totalCount = jpgFiles.length;

                int successCount = 0;


                Log.e("TAG", "totalCount: "+totalCount );
                for (int i = 0; i < totalCount; i++) {
                    final int finalI = i;

                    final File jpgFile = jpgFiles[i];
                    Bitmap bitmap = BitmapFactory.decodeFile(jpgFile.getAbsolutePath());
                    if (bitmap == null) {
                        File failedFile = new File(REGISTER_FAILED_DIR + File.separator + jpgFile.getName());
                        if (!failedFile.getParentFile().exists()) {
                            failedFile.getParentFile().mkdirs();
                        }
                        jpgFile.renameTo(failedFile);
                        continue;
                    }
                    bitmap = ArcSoftImageUtil.getAlignedBitmap(bitmap, true);
                    if (bitmap == null) {
                        File failedFile = new File(REGISTER_FAILED_DIR + File.separator + jpgFile.getName());
                        if (!failedFile.getParentFile().exists()) {
                            failedFile.getParentFile().mkdirs();
                        }
                        jpgFile.renameTo(failedFile);
                        continue;
                    }
                    byte[] bgr24 = ArcSoftImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), ArcSoftImageFormat.BGR24);
                    int transformCode = ArcSoftImageUtil.bitmapToImageData(bitmap, bgr24, ArcSoftImageFormat.BGR24);
                    if (transformCode != ArcSoftImageUtilError.CODE_SUCCESS) {
                        String tsid=jpgFile.getName().substring(0, jpgFile.getName().lastIndexOf("."));
                        list.add(setImgListType(getIDs(tsid),"2","上传照片不清晰"));
                        deleteFaceToken(getIDsTecherId(tsid),getIDsStudentId(tsid));

//                        list.add(setImgListType(jpgFile.getName().substring(0, jpgFile.getName().lastIndexOf(".")),"2","特征值提取失败"));
                        return;
                    }
                    String tsid2=jpgFile.getName().substring(0, jpgFile.getName().lastIndexOf("."));
                    PicResult result = FaceServer.getInstance().registerBgr24(context, bgr24, bitmap.getWidth(), bitmap.getHeight(),
                            getIDsName(tsid2),getIDsTecherId(tsid2),getIDsStudentId(tsid2));

//                    Log.e("TAG", "第"+i+"张:"+success +jpgFile.getName().substring(0, jpgFile.getName().lastIndexOf(".")));
                    Log.e("TAG", "第"+i+"张:"+result.Result +JSON.toJSONString(jpgFile));
                    if (!result.Result) {
                        String tsid=jpgFile.getName().substring(0, jpgFile.getName().lastIndexOf("."));
                        Log.e("TAG", "result: "+JSON.toJSONString(result));
                        list.add(setImgListType(getIDs(tsid),"2",result.error));
                        if (!result.error.contains("照片已被")){
                            deleteFaceToken(getIDsTecherId(tsid),getIDsStudentId(tsid));
                        }

//                        list.add(setImgListType(jpgFile.getName().substring(0, jpgFile.getName().lastIndexOf(".")),"2","特征值提取失败"));
                        File failedFile = new File(REGISTER_FAILED_DIR + File.separator + jpgFile.getName());
                        if (!failedFile.getParentFile().exists()) {
                            failedFile.getParentFile().mkdirs();
                        }jpgFile.renameTo(failedFile);
                    } else {
                        String tsid=jpgFile.getName().substring(0, jpgFile.getName().lastIndexOf("."));
                        list.add(setImgListType(getIDs(tsid),"1",""));
                        successCount++;
                    }
                }

                Log.i(MinaClientHandler.class.getSimpleName(), "run: " + executorService.isShutdown());
                deleteFile(dir);
                deleteFile(new File(REGISTER_FAILED_DIR));

                UpDataImgmsg();

                FaceServer.getInstance().init(context);
                FaceServer.getInstance().unInit();
                myHandler.sendEmptyMessage(2);

                Log.e("TAG", "UpDataImgmsg: "+"共"+totalCount+"张"+"成功"+successCount+"张");



            }
        });


    }

    void deleteFaceToken(String teacherId,String studentId){
        UntyingfaceDataReq req = new UntyingfaceDataReq();
        req.type="1";

        if (!TextUtils.isEmpty(teacherId)){
            req.teacherId=teacherId;
        }else {
            req.studentId=studentId;
        }
        MyApp.getInstance().requestDataHRFace(this, req, new listener(teacherId,studentId), new Error3());
    }
    class listener implements Response.Listener<UntyingfaceDataRsp> {
        public String teacherId;
        public String studentId;

        listener(String teacherId,String studentId){
            this.teacherId=teacherId;
            this.studentId=studentId;
        }
        @Override
        public void onResponse(UntyingfaceDataRsp rsp) {

            if (rsp.status == BaseConstant.REQUEST_SUCCES) {
                if (!TextUtils.isEmpty(teacherId)){
                    mDbController.delete(teacherId);
                }else {
                    mDbController.delete(studentId);
                }
            }
        }
    }

       //图片转化成Base64字符串
    public String convertToString(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] bts = baos.toByteArray();
        return Base64.encodeToString(bts, Base64.DEFAULT);
    }



    protected void showToast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    public void downLoadImgWechat (String photo, final String name, final String id){

        boolean isCreate = FileUtils.createOrExistsDir(REGISTER_DIR);



        if (isCreate) {

            final File file = new File(REGISTER_DIR+ File.separator+id+".jpg");
            //下载
            Ion.with(MyApp.getInstance()).load(photo).setTimeout(10000)
                    .write(file).setCallback(new FutureCallback<File>() {
                @Override
                public void onCompleted(Exception arg0, File arg1) {

                    if (arg1 != null && arg1.exists()) {
                        if (FaceServer.faceEngine==null){
                            FaceServer.getInstance().init(context);
                        }
                        doRegisterWechat(name,id,"");
                    }



                }
            });
        }


    }


   public void downLoadImg (String photo, final String name, final String ids){
//        boolean isCreate = FileUtils.createOrExistsDir(Environment.getExternalStorageDirectory() + "/BD_FEATURE/");
        boolean isCreate = FileUtils.createOrExistsDir(REGISTER_DIR);



        if (isCreate) {

//            final File file = new File(Environment.getExternalStorageDirectory() + "/BD_FEATURE/", System.currentTimeMillis() + ".jpg");

            final File file = new File(REGISTER_DIR+ File.separator+ids+".jpg");
            //下载
//            Ion.with(MyApp.getInstance()).load(photo).setTimeout(10000).
            Ion.with(MyApp.getInstance()).load(photo).setTimeout(10000)
                    .write(file).setCallback(new FutureCallback<File>() {
                @Override
                public void onCompleted(Exception arg0, File arg1) {

                    if (arg1 != null && arg1.exists()) {

                        list.add(setImgListType(ids,"1",""));
                    }else {

                        list.add(setImgListType(ids,"2","照片下载失败"));
                    }
                    count++;
                    Log.e("TAG", "onCompleted: "+count+"/"+bean.data.size() );

                    if (bean!=null&&count==bean.data.size()){
                        Log.e("TAG", "下载完成==>开始注册 "+ FaceServer.faceEngine);

                        if (FaceServer.faceEngine==null){
                            Log.e("TAG", "下载完成==>开始注册 faceEngine"+ FaceServer.faceEngine);
                            FaceServer.getInstance().init(context);
                            doRegister();
                        }else {
                            doRegister();
                        }

                    }
                }
            });
        }


    }

    //flie：要删除的文件夹的所在位置
    public void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
//            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    /*锁*/
    public static Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (dialog == null)
                dialog = new LockDialog(MyApp.getInstance());
//                dialog = new LockDialog(context);
            switch (msg.what) {
                case 1:
                    dialog.show();
                    break;
                case 2:
                    dialog.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void  doRegisterWechat(final String name, final String studentId, final String teacherId) {

        final File dir = new File(REGISTER_DIR+File.separator);
        if (!dir.exists()) {
            showToast(context.getString(R.string.batch_process_path_is_not_exists, REGISTER_DIR));

        }
        if (!dir.isDirectory()) {
            showToast(context.getString(R.string.batch_process_path_is_not_dir, REGISTER_DIR));
        }
        final File[] jpgFiles = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {


                return name.endsWith(FaceServer.IMG_SUFFIX);
            }
        });
        Log.e("TAG", "doRegisterWechat: "+ dir.getAbsolutePath());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final int totalCount = jpgFiles.length;

                int successCount = 0;


                Log.e("TAG", "totalCount: "+totalCount );
                for (int i = 0; i < totalCount; i++) {
                    final int finalI = i;

                    final File jpgFile = jpgFiles[i];
                    Bitmap bitmap = BitmapFactory.decodeFile(jpgFile.getAbsolutePath());
                    if (bitmap == null) {
                        File failedFile = new File(REGISTER_FAILED_DIR + File.separator + jpgFile.getName());
                        if (!failedFile.getParentFile().exists()) {
                            failedFile.getParentFile().mkdirs();
                        }
                        jpgFile.renameTo(failedFile);
                        continue;
                    }
                    bitmap = ArcSoftImageUtil.getAlignedBitmap(bitmap, true);
                    if (bitmap == null) {
                        File failedFile = new File(REGISTER_FAILED_DIR + File.separator + jpgFile.getName());
                        if (!failedFile.getParentFile().exists()) {
                            failedFile.getParentFile().mkdirs();
                        }
                        jpgFile.renameTo(failedFile);
                        continue;
                    }
                    byte[] bgr24 = ArcSoftImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), ArcSoftImageFormat.BGR24);
                    int transformCode = ArcSoftImageUtil.bitmapToImageData(bitmap, bgr24, ArcSoftImageFormat.BGR24);
                    if (transformCode != ArcSoftImageUtilError.CODE_SUCCESS) {
                        return;
                    }

                    PicResult result = FaceServer.getInstance().registerBgr24Wechat(context, bgr24, bitmap.getWidth(), bitmap.getHeight(),
                            name,"",studentId);
                    Log.e("TAG", "PicResult==>: "+ JSON.toJSONString(result));
                    if (!result.Result) {
                        File failedFile = new File(REGISTER_FAILED_DIR + File.separator + jpgFile.getName());
                        if (!failedFile.getParentFile().exists()) {
                            failedFile.getParentFile().mkdirs();
                        }jpgFile.renameTo(failedFile);
                    } else {
                        successCount++;
                    }
                }

                Log.i(MinaClientHandler.class.getSimpleName(), "run: " + executorService.isShutdown());
                deleteFile(dir);
                deleteFile(new File(REGISTER_FAILED_DIR));

                FaceServer.getInstance().init(context);
                FaceServer.getInstance().unInit();
                myHandler.sendEmptyMessage(2);




            }
        });


    }
}
