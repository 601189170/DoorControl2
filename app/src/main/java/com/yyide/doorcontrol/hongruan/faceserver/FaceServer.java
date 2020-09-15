package com.yyide.doorcontrol.hongruan.faceserver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.util.ImageUtils;
import com.arcsoft.imageutil.ArcSoftImageFormat;
import com.arcsoft.imageutil.ArcSoftImageUtil;
import com.arcsoft.imageutil.ArcSoftImageUtilError;
import com.arcsoft.imageutil.ArcSoftRotateDegree;
import com.yyide.doorcontrol.MyApp;
import com.yyide.doorcontrol.SpData;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.hongruan.db.DbController;
import com.yyide.doorcontrol.hongruan.db.PersonInfor;
import com.yyide.doorcontrol.hongruan.db.VoicefaceReq;
import com.yyide.doorcontrol.hongruan.db.VoicefacesynRsp;
import com.yyide.doorcontrol.hongruan.model.FaceRegisterInfo;
import com.yyide.doorcontrol.requestbean.UntyingfaceDataReq;
import com.yyide.doorcontrol.rsponbean.UntyingfaceDataRsp;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 人脸库操作类，包含注册和搜索
 */
public class FaceServer {

    FaceFeature NowFace;

    private static final String TAG = "FaceServer";
    public static final String IMG_SUFFIX = ".jpg";
//    public static final String IMG_SUFFIX = ".JPEG";
    public static FaceEngine faceEngine = null;
    private static FaceServer faceServer = null;
    private static List<FaceRegisterInfo> faceRegisterInfoList; //这个里面不是有很多。解绑一个就要删除掉一个 把这个list置空？删除特定的
    public static String ROOT_PATH;

    public List<FaceRegisterInfo>  getRegisterInfoList(){
        return faceRegisterInfoList; //??啥没用  比对的时候 还是要在这个faceServer比对ruturn没用   ？啥子
    }

    private FaceServer(){

    }
    /**
     * 存放注册图的目录
     */
    public static final String SAVE_IMG_DIR = "register" + File.separator + "imgs";
    /**
     * 存放特征的目录
     */
    private static final String SAVE_FEATURE_DIR = "register" + File.separator + "features";

    /**
     * 是否正在搜索人脸，保证搜索操作单线程进行
     */
    private boolean isProcessing = false;

    public static FaceServer getInstance() {
        if (faceServer == null) {
            synchronized (FaceServer.class) {
                if (faceServer == null) {
                    faceServer = new FaceServer();
                }
            }
        }
        return faceServer;
    }

    /**
     * 初始化
     *
     * @param context 上下文对象
     * @return 是否初始化成功
     */

    public boolean init(Context context) {
//这些东西初始化一下 ，每次都初始化
        synchronized (this) {
            if (faceEngine == null && context != null) {
                mDbController = DbController.getInstance(context);
                faceEngine = new FaceEngine();
                int engineCode = faceEngine.init(context, FaceEngine.ASF_DETECT_MODE_IMAGE, FaceEngine.ASF_OP_0_ONLY, 16, 1, FaceEngine.ASF_FACE_RECOGNITION | FaceEngine.ASF_FACE_DETECT);
                if (engineCode == ErrorInfo.MOK) {
                    Log.e(TAG, "init1_face" );
                    initFaceList(context);
                    return true;
                } else {
                    faceEngine = null;
                    Log.e(TAG, "init: failed! code = " + engineCode);//这里打印错的？
                    return false;
                }
            }
            return false;
        }
    }

    /**
     * 销毁
     */
    public void unInit() {
        synchronized (this) {
            if (faceRegisterInfoList != null) {
                faceRegisterInfoList.clear();
                faceRegisterInfoList = null;
            }
            if (faceEngine != null) {
                faceEngine.unInit();
                faceEngine = null;
            }
        }
    }

    /**
     * 初始化人脸特征数据以及人脸特征数据对应的注册图
     *
     * @param context 上下文对象
     */
    private void initFaceList(Context context) {//
        synchronized (this) {
            if (ROOT_PATH == null) {
                ROOT_PATH = Environment.getExternalStorageDirectory()+"/Androidfi";
            }
//            File featureDir = new File(ROOT_PATH + File.separator + SAVE_FEATURE_DIR);
//            if (!featureDir.exists() || !featureDir.isDirectory()) {
//                return;
//            }
//
//            File[] featureFiles = featureDir.listFiles();
//            if (featureFiles == null || featureFiles.length == 0) {
//                return;
//            }
            faceRegisterInfoList = new ArrayList<>();//
            addfaceRegisterInfoList();
            //从文件夹中获取
//            for (File featureFile : featureFiles) {
//                try {
//                    FileInputStream fis = new FileInputStream(featureFile);
//                    byte[] feature = new byte[FaceFeature.FEATURE_SIZE];
//                    fis.read(feature);
//                    fis.close();
//                    Log.e(TAG, "init1_face1" );
//
////                    faceRegisterInfoList.add(new FaceRegisterInfo(feature, featureFile.getName()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    public void addfaceregis(FaceRegisterInfo info){
        faceRegisterInfoList.add(info);
    }

    public void addfaceRegisterInfoList(){
        try {
            StringBuilder sb = new StringBuilder();
            List<PersonInfor> personInfors = mDbController.searchAll();
            Log.e(TAG, "init1_face2412412_"+personInfors.size() );
            for(PersonInfor personInfor:personInfors){
                // dataArea.setText("id:"+p);
                sb.append("id:").append(personInfor.getId())
                        .append("perNo:").append(personInfor.getPerNo())
                        .append("name:").append(personInfor.getName())
                        .append("sex:").append(personInfor.getSex())
                        .append("featrue:").append(personInfor.getFeatrue())
                        .append("\n");

                Log.e("featrue",personInfor.getFeatrue().length+"");
                FaceRegisterInfo info=new FaceRegisterInfo(personInfor.getFeatrue(),personInfor.getName(),personInfor.getPerNo(),personInfor.getId(),personInfor.getTime());

                faceRegisterInfoList.add(info);
//                Log.e(TAG, "init1_face23432"+sb.toString());
                Log.e(TAG, "init1_face23432"+personInfor.getTime()+"==>"+"personInfor"+personInfor.getName());

            }

        }catch (Exception  e){
             Log.e("xupeng","trycatch1"+e);
        }
    }
    public int getFaceNumber(Context context) {
        synchronized (this) {
            if (context == null) {
                return 0;
            }
            if (ROOT_PATH == null) {
                ROOT_PATH = Environment.getExternalStorageDirectory()+"/Androidfi";
            }

            File featureFileDir = new File(ROOT_PATH + File.separator + SAVE_FEATURE_DIR);
            int featureCount = 0;
            if (featureFileDir.exists() && featureFileDir.isDirectory()) {
                String[] featureFiles = featureFileDir.list();
                featureCount = featureFiles == null ? 0 : featureFiles.length;
            }
            int imageCount = 0;
            File imgFileDir = new File(ROOT_PATH + File.separator + SAVE_IMG_DIR);
            if (imgFileDir.exists() && imgFileDir.isDirectory()) {
                String[] imageFiles = imgFileDir.list();
                imageCount = imageFiles == null ? 0 : imageFiles.length;
            }
            return featureCount > imageCount ? imageCount : featureCount;
        }
    }

    public int clearAllFaces(Context context) {
        synchronized (this) {
            if (context == null) {
                return 0;
            }
            if (ROOT_PATH == null) {
                ROOT_PATH = Environment.getExternalStorageDirectory()+"/Androidfi";
            }
            if (faceRegisterInfoList != null) {
                faceRegisterInfoList.clear();
            }
            File featureFileDir = new File(ROOT_PATH + File.separator + SAVE_FEATURE_DIR);
            int deletedFeatureCount = 0;
            if (featureFileDir.exists() && featureFileDir.isDirectory()) {
                File[] featureFiles = featureFileDir.listFiles();
                if (featureFiles != null && featureFiles.length > 0) {
                    for (File featureFile : featureFiles) {
                        if (featureFile.delete()) {
                            deletedFeatureCount++;
                        }
                    }
                }
            }
            int deletedImageCount = 0;
            File imgFileDir = new File(ROOT_PATH + File.separator + SAVE_IMG_DIR);
            if (imgFileDir.exists() && imgFileDir.isDirectory()) {
                File[] imgFiles = imgFileDir.listFiles();
                if (imgFiles != null && imgFiles.length > 0) {
                    for (File imgFile : imgFiles) {
                        if (imgFile.delete()) {
                            deletedImageCount++;
                        }
                    }
                }
            }
            Log.e(TAG, "deletedFeatureCount: "+deletedFeatureCount );
            Log.e(TAG, "deletedImageCount: "+deletedImageCount );
            Log.e(TAG, "deletedImageCount: "+deletedImageCount );
            Log.e(TAG, "deletedFeatureCount: "+deletedFeatureCount );
            return deletedFeatureCount > deletedImageCount ? deletedImageCount : deletedFeatureCount;
        }
    }

    /**
     * 用于预览时注册人脸
     *
     * @param context  上下文对象
     * @param nv21     NV21数据
     * @param width    NV21宽度
     * @param height   NV21高度
     * @param faceInfo {@link FaceEngine#detectFaces(byte[], int, int, int, List)}获取的人脸信息
     * @param name     保存的名字，若为空则使用时间戳
     * @return 是否注册成功
     */
    private PersonInfor personInfor1;//数据库模板
    public boolean registerNv21(Context context, byte[] nv21, int width, int height, FaceInfo faceInfo, String name, String numbid, String type) {
        synchronized (this) {
            if (faceEngine == null || context == null || nv21 == null || width % 4 != 0 || nv21.length != width * height * 3 / 2) {
                Log.e(TAG, "registerNv21: " +faceEngine);
                Log.e(TAG, "registerNv21: " +context);
                Log.e(TAG, "registerNv21: " +nv21);
                Log.e(TAG, "registerNv21: " +width);
                Log.e(TAG, "registerNv21: " +nv21.length);
                return false;
            }


            ROOT_PATH = Environment.getExternalStorageDirectory()+"/Androidfi";

            boolean dirExists = true;
            //特征存储的文件夹
            File featureDir = new File(ROOT_PATH + File.separator + SAVE_FEATURE_DIR);
            if (!featureDir.exists()) {
                dirExists = featureDir.mkdirs();
            }
            if (!dirExists) {
                return false;
            }
            //图片存储的文件夹
//            File imgDir = new File(ROOT_PATH + File.separator + SAVE_IMG_DIR);
            Log.i("21fileimage",ROOT_PATH + File.separator + SAVE_IMG_DIR);
            Log.i("21filefeature",ROOT_PATH + File.separator + SAVE_FEATURE_DIR);
//            if (!imgDir.exists()) {
//                dirExists = imgDir.mkdirs();
//            }
            if (!dirExists) {
                return false;
            }
            FaceFeature faceFeature = new FaceFeature();

            //特征提取

            int code = faceEngine.extractFaceFeature(nv21, width, height, FaceEngine.CP_PAF_NV21, faceInfo, faceFeature);
            String userName = name == null ? String.valueOf("没有录入姓名") : name;
            try {
                //保存注册结果（注册图、特征数据）
                if (code == ErrorInfo.MOK) {
                    YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
                    //为了美观，扩大rect截取注册图
                    Rect cropRect = getBestRect(width, height, faceInfo.getRect());
                    if (cropRect == null) {
                        return false;
                    }
                    //以下注释，删除录入人脸图片。需要图片 则取消注释。
//                    File file = new File(imgDir + File.separator + userName + IMG_SUFFIX);
//                    FileOutputStream fosImage = new FileOutputStream(file);
//                    yuvImage.compressToJpeg(cropRect, 100, fosImage);
//                    fosImage.close();
//                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                    //判断人脸旋转角度，若不为0度则旋转注册图
                    boolean needAdjust = false;
//                    if (bitmap != null) {
//                        switch (faceInfo.getOrient()) {
//                            case FaceEngine.ASF_OC_0:
//                                break;
//                            case FaceEngine.ASF_OC_90:
//                                bitmap = ImageUtils.rotateBitmap(bitmap, 90);
//                                needAdjust = true;
//                                break;
//                            case FaceEngine.ASF_OC_180:
//                                bitmap = ImageUtils.rotateBitmap(bitmap, 180);
//                                needAdjust = true;
//                                break;
//                            case FaceEngine.ASF_OC_270:
//                                bitmap = ImageUtils.rotateBitmap(bitmap, 270);
//                                needAdjust = true;
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                    if (needAdjust) {
//                        fosImage = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fosImage);
//                        fosImage.close();
//                    }

                    FileOutputStream fosFeature = new FileOutputStream(featureDir + File.separator + userName);
                    fosFeature.write(faceFeature.getFeatureData());
                    fosFeature.close();

                    //内存中的数据同步
                    if (faceRegisterInfoList == null) {
                        faceRegisterInfoList = new ArrayList<>();
                    }

                    Date date = new Date();
                    String time = date.toLocaleString();
                    Log.i("md", "时间time为： "+time);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒 E");
                    String sim = dateFormat.format(date);

                    Log.i("md", "时间sim为： "+sim);

                    Log.i("facefeature",userName);
//                    public FaceFeature(FaceFeature obj) {
//                        if (obj == null) {
//                            this.featureData = new byte[1032];
//                        } else {
//                            this.featureData = (byte[])obj.getFeatureData().clone();
//                        }
//
//                    }


                    faceRegisterInfoList.add(new FaceRegisterInfo(faceFeature.getFeatureData(), userName,numbid,Long.valueOf("123"),sim));  // 录入人脸信息添加到内存
//                    personInfor1 = new PersonInfor(null,numbid,faceFeature.getFeatureData(),userName,"男",sim);
                    personInfor1 = new PersonInfor(null,numbid,faceFeature.getFeatureData(),userName,"男",sim);
                    Log.i("xupengface","userid:"+numbid+"/time"+sim+"/username:"+userName+"/职位（1为学生2为老师）："+type+"/facefeature"+faceFeature.getFeatureData().length);
//                    Log.i("xupengface","_id:"+"/time"+sim+"/username:"+userName+"/职位（1为学生2为老师）："+type+"/facefeature"+faceFeature.getFeatureData().length);
                    mDbController.insert(personInfor1);//本地库 添加人脸信息   //加载到本地库中间
//                    Log.e(TAG, "init1_face2"+faceRegisterInfoList.size() );  //每采集一个  本地内存存一个人脸信息。这个采集进去  而且在比对那里打印的是0.8-1之间  是没有任何问题的
//                    showDataList();
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

    }
    public boolean IsCanregisterNv21(Context context, byte[] nv21, int width, int height, FaceInfo faceInfo, String name, String numbid, String type) {
        synchronized (this) {
            if (faceEngine == null || context == null || nv21 == null || width % 4 != 0 || nv21.length != width * height * 3 / 2) {
                return false;
            }


            ROOT_PATH = Environment.getExternalStorageDirectory()+"/Androidfi";

            boolean dirExists = true;
            //特征存储的文件夹
            File featureDir = new File(ROOT_PATH + File.separator + SAVE_FEATURE_DIR);
            if (!featureDir.exists()) {
                dirExists = featureDir.mkdirs();
            }
            if (!dirExists) {
                return false;
            }
            //图片存储的文件夹
//            File imgDir = new File(ROOT_PATH + File.separator + SAVE_IMG_DIR);
            Log.i("21fileimage",ROOT_PATH + File.separator + SAVE_IMG_DIR);
            Log.i("21filefeature",ROOT_PATH + File.separator + SAVE_FEATURE_DIR);
//            if (!imgDir.exists()) {
//                dirExists = imgDir.mkdirs();
//            }
            if (!dirExists) {
                return false;
            }
            FaceFeature faceFeature = new FaceFeature();

            //特征提取

            int code = faceEngine.extractFaceFeature(nv21, width, height, FaceEngine.CP_PAF_NV21, faceInfo, faceFeature);
            String userName = name == null ? String.valueOf("没有录入姓名") : name;
            try {
                //保存注册结果（注册图、特征数据）
                if (code == ErrorInfo.MOK) {

                    //为了美观，扩大rect截取注册图
                    Rect cropRect = getBestRect(width, height, faceInfo.getRect());
                    if (cropRect == null) {
                        return false;
                    }





                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }
    public byte[] registerNv22(Context context, byte[] nv21, int width, int height, FaceInfo faceInfo, String name, String numbid, String type) {
        synchronized (this) {
            if (faceEngine == null || context == null || nv21 == null || width % 4 != 0 || nv21.length != width * height * 3 / 2) {
                return null;
            }


            ROOT_PATH = Environment.getExternalStorageDirectory()+"/Androidfi";

            boolean dirExists = true;
            //特征存储的文件夹
            File featureDir = new File(ROOT_PATH + File.separator + SAVE_FEATURE_DIR);
            if (!featureDir.exists()) {
                dirExists = featureDir.mkdirs();
            }
            if (!dirExists) {
                return null;
            }
            //图片存储的文件夹
//
//            if (!imgDir.exists()) {
//                dirExists = imgDir.mkdirs();
//            }
            if (!dirExists) {
                return null;
            }
            FaceFeature faceFeature = new FaceFeature();

            //特征提取

            int code = faceEngine.extractFaceFeature(nv21, width, height, FaceEngine.CP_PAF_NV21, faceInfo, faceFeature);
            String userName = name == null ? String.valueOf("没有录入姓名") : name;
//            try {
                if (code == ErrorInfo.MOK) {
//                    FileOutputStream fosFeature = new FileOutputStream(featureDir + File.separator + userName);
//                    fosFeature.write(faceFeature.getFeatureData());
//                    fosFeature.close();

                    //内存中的数据同步
                    if (faceRegisterInfoList == null) {
                        faceRegisterInfoList = new ArrayList<>();
                    }


                    return faceFeature.getFeatureData();
                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            return null;
        }

    }
    private static DbController mDbController;
    private void showDataList() {
        StringBuilder sb = new StringBuilder();
        List<PersonInfor>personInfors = mDbController.searchAll();
        for(PersonInfor personInfor:personInfors){
            // dataArea.setText("id:"+p);
            sb.append("id:").append(personInfor.getId())
                    .append("perNo:").append(personInfor.getPerNo())
                    .append("name:").append(personInfor.getName())
                    .append("sex:").append(personInfor.getSex())
                    .append("featrue:").append(personInfor.getFeatrue())
                    .append("time:").append(personInfor.getTime())
                    .append("\n");
            Log.i("xupengface",sb.toString());
        }
    }
    private void getcountId() {
        StringBuilder sb = new StringBuilder();
        List<PersonInfor>personInfors = mDbController.searchAll();
        for(PersonInfor personInfor:personInfors){
            // dataArea.setText("id:"+p);
            sb.append("id:").append(personInfor.getId())
                    .append("perNo:").append(personInfor.getPerNo())
                    .append("name:").append(personInfor.getName())
                    .append("sex:").append(personInfor.getSex())
                    .append("featrue:").append(personInfor.getFeatrue())
                    .append("time:").append(personInfor.getTime())
                    .append("\n");
            Log.i("xupengface22",sb.toString());
        }
    }


    /**
     * 用于注册照片人脸
     *
     * @param context  上下文对象
     * @param bgr24    bgr24数据
     * @param width    bgr24宽度
     * @param height   bgr24高度
     * @param name     保存的名字，若为空则使用时间戳
     * @return 是否注册成功
     */
    //集成没有用到  不用改！

    public PicResult registerBgr24(Context context, byte[] bgr24, int width, int height, String name,String teacherId,String studentId) {
        boolean Result=false;
        String error="";
        synchronized (this) {

            if (faceEngine == null || context == null || bgr24 == null || width % 4 != 0 || bgr24.length != width * height * 3) {
//                Log.e(TAG, "registerBgr24: false111");
                Log.e(TAG, "registerBgr24:  invalid params"+faceEngine);
                deleteFaceToken(teacherId,studentId);
                Result=false;
                if (faceEngine==null){
                    error="人脸引擎未初始化";
                }else {
                    error="上传照片不清晰";
                }
                return new PicResult(Result,error);
            }

            if (ROOT_PATH == null) {
//                ROOT_PATH = Environment.getExternalStorageDirectory()+"/Androidfi";
                ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ArcfaceVisitor";
            }
            boolean dirExists = true;
            //特征存储的文件夹
//            File featureDir = new File(ROOT_PATH + File.separator + SAVE_FEATURE_DIR);
//            if (!featureDir.exists()) {
//                dirExists = featureDir.mkdirs();
//            }
//            if (!dirExists) {
//                return false;
//            }
            //图片存储的文件夹
            File imgDir = new File(ROOT_PATH + File.separator + "register");

            if (!imgDir.exists()) {
                dirExists = imgDir.mkdirs();
            }
            if (!dirExists) {
                Log.e(TAG, "registerBgr24: false222"+ dirExists);
                deleteFaceToken(teacherId,studentId);

                return new PicResult(false,"找不到该图片");
//                Result=false;
//                error="找不到该图片";
            }
            //人脸检测
            List<FaceInfo> faceInfoList = new ArrayList<>();
            int code = faceEngine.detectFaces(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList);
            Log.e(TAG, "registerBgr24: "+code );
            Log.e(TAG, "detectFaces: "+faceInfoList.size() );
            if (code == ErrorInfo.MOK && faceInfoList.size() > 0) {
                FaceFeature faceFeature = new FaceFeature();

                //特征提取
                code = faceEngine.extractFaceFeature(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
                String userName = name == null ? String.valueOf("") : name;
                try {
                    //保存注册结果（注册图、特征数据）
                    if (code == ErrorInfo.MOK) {
                        //为了美观，扩大rect截取注册图
                        Rect cropRect = getBestRect(width, height, faceInfoList.get(0).getRect());
                        if (cropRect == null) {
                            deleteFaceToken(teacherId,studentId);

                            return new PicResult(false,"上传照片不清晰");
                        }
                        if ((cropRect.width() & 1) == 1) {
                            cropRect.right--;
                        }
                        if ((cropRect.height() & 1) == 1) {
                            cropRect.bottom--;
                        }
                        File file = new File(imgDir + File.separator + userName + IMG_SUFFIX);
                        FileOutputStream fosImage = new FileOutputStream(file);
                        byte[] headBgr24 = ImageUtils.cropBgr24(bgr24, width, height, cropRect);
                        Bitmap headBmp = ImageUtils.bgrToBitmap(headBgr24, cropRect.width(), cropRect.height());
                        headBmp.compress(Bitmap.CompressFormat.JPEG, 100, fosImage);
                        fosImage.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                        //判断人脸旋转角度，若不为0度则旋转注册图
                        boolean needAdjust = false;
                        if (bitmap != null) {
                            switch (faceInfoList.get(0).getOrient()) {
                                case FaceEngine.ASF_OC_0:
                                    break;
                                case FaceEngine.ASF_OC_90:
                                    bitmap = ImageUtils.rotateBitmap(bitmap, 90);
                                    needAdjust = true;
                                    break;
                                case FaceEngine.ASF_OC_180:
                                    bitmap = ImageUtils.rotateBitmap(bitmap, 180);
                                    needAdjust = true;
                                    break;
                                case FaceEngine.ASF_OC_270:
                                    bitmap = ImageUtils.rotateBitmap(bitmap, 270);
                                    needAdjust = true;
                                    break;
                                default:
                                    break;
                            }
                        }
                        if (needAdjust) {
                            fosImage = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fosImage);
                            fosImage.close();
                        }
                        Date date = new Date();
                        String time = date.toLocaleString();
                        Log.i("md", "时间time为： "+time);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒 E");
                        String sim = dateFormat.format(date);
//                        FileOutputStream fosFeature = new FileOutputStream(featureDir + File.separator + userName);
//                        fosFeature.write(faceFeature.getFeatureData());
//                        fosFeature.close();



                        //内存中的数据同步
                        if (faceRegisterInfoList == null) {
                            faceRegisterInfoList = new ArrayList<>();
                        }
                    //先比较数据库 1:N
                        FaceFeature tempFaceFeature = new FaceFeature();
                        tempFaceFeature.setFeatureData(faceFeature.getFeatureData());

                       CompareResult result=getTopOfFaceLib2(tempFaceFeature);

                        Log.e(TAG, "CompareResult: "+JSON.toJSONString(result) );
                            if (result!=null) {


                                if (result.getSimilar() > SpData.facefortrue) {
                                    //如存在 则对比信息

                                    if (result.getUserid().equals(teacherId) || result.getUserid().equals(studentId)) {
                                        //如是同一个人覆盖
                                        if (TextUtils.isEmpty(teacherId)) {
                                            personInfor1 = new PersonInfor(null, studentId, faceFeature.getFeatureData(), userName, "男", sim);
                                            faceRegisterInfoList.add(new FaceRegisterInfo(faceFeature.getFeatureData(), userName, studentId, Long.valueOf("123"), sim));  // 录入人脸信息添加到内存
                                        } else {
                                            personInfor1 = new PersonInfor(null, teacherId, faceFeature.getFeatureData(), userName, "男", sim);
                                            faceRegisterInfoList.add(new FaceRegisterInfo(faceFeature.getFeatureData(), userName, teacherId, Long.valueOf("123"), sim));  // 录入人脸信息添加到内存

                                        }
                                        mDbController.insertOrReplace(personInfor1);//本地库 添加人脸信息   //加载到本地库中间
                                        Log.e(TAG, "同一个人覆盖：" + JSON.toJSONString(personInfor1));  //每采集一个  本地内存存一个人脸信息。这个采集进去  而且在比对那里打印的是0.8-1之间  是没有任何问题的

                                        postFaceToken(faceFeature.getFeatureData(), name, studentId, teacherId);
                                        Result = true;
                                        error = "";
                                        return new PicResult(Result, "");
                                    } else {
                                        Log.e(TAG, "照片已被" + result.getUserName() + "采集");  //每采集一个  本地内存存一个人脸信息。这个采集进去  而且在比对那里打印的是0.8-1之间  是没有任何问题的
                                        Result = false;
                                        error = "照片已被" + result.getUserName() + "采集";
                                        return new PicResult(Result, error);
                                    }


                                } else {
                                    //如不存在则直接添加
                                    if (TextUtils.isEmpty(teacherId)) {
                                        personInfor1 = new PersonInfor(null, studentId, faceFeature.getFeatureData(), userName, "男", sim);
                                        faceRegisterInfoList.add(new FaceRegisterInfo(faceFeature.getFeatureData(), userName, studentId, Long.valueOf("123"), sim));  // 录入人脸信息添加到内存
                                    } else {
                                        personInfor1 = new PersonInfor(null, teacherId, faceFeature.getFeatureData(), userName, "男", sim);
                                        faceRegisterInfoList.add(new FaceRegisterInfo(faceFeature.getFeatureData(), userName, teacherId, Long.valueOf("123"), sim));  // 录入人脸信息添加到内存
                                    }
                                    mDbController.insertOrReplace(personInfor1);//本地库 添加人脸信息   //加载到本地库中间
                                    Log.e(TAG, "不存在添加：" + JSON.toJSONString(personInfor1));  //每采集一个  本地内存存一个人脸信息。这个采集进去  而且在比对那里打印的是0.8-1之间  是没有任何问题的
                                    postFaceToken(faceFeature.getFeatureData(), name, studentId, teacherId);

                                    Result = true;
                                    error = "";
                                    return new PicResult(Result, "");
                                }
//                       }else {
//                           Log.e(TAG, "registerBgr24: "+ "特征值提取失败");

//                       }
                            }else {
                                return new PicResult(false,"上传照片不清晰");
                            }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                Result=false;
                error="上传照片不清晰";
                deleteFaceToken(teacherId,studentId);
                return new PicResult(Result,error);
            }

        }
//            if (!Result){
//                deleteFaceToken(teacherId,studentId);
//            }
        return new PicResult(Result,error);
    }

    public PicResult registerBgrCard(Context context, byte[] bgr24, int width, int height) {
        boolean Result = false;
        String error = "";
        synchronized (this) {

            if (faceEngine == null || context == null || bgr24 == null || width % 4 != 0 || bgr24.length != width * height * 3) {
//                Log.e(TAG, "registerBgr24: false111");
                Log.e(TAG, "registerBgr24:  invalid params" + faceEngine);
                Result = false;
                if (faceEngine == null) {
                    error = "人脸引擎未初始化";
                }
                return new PicResult(Result, error);
            }


            //人脸检测
            List<FaceInfo> faceInfoList = new ArrayList<>();
            int code = faceEngine.detectFaces(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList);
            Log.e(TAG, "registerBgr24: " + code);
            Log.e(TAG, "detectFaces: " + faceInfoList.size());
            if (code == ErrorInfo.MOK && faceInfoList.size() > 0) {
                FaceFeature faceFeature = new FaceFeature();

                //特征提取
                code = faceEngine.extractFaceFeature(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);


                //保存注册结果（注册图、特征数据）
                if (code == ErrorInfo.MOK) {
                    //先比较数据库 1:N
                    FaceFeature tempFaceFeature = new FaceFeature();
                    tempFaceFeature.setFeatureData(faceFeature.getFeatureData());
                    NowFace = tempFaceFeature;
                    error="身份证提取成功";
                    Result=true;

//                    return new PicResult(Result, error);
                }else {
                    Result=false;

                    error = "身份证提取失败";

                }


            }

        }
        return new PicResult(Result, error);
    }
    public PicResult registerBgrCard222(Context context, byte[] bgr24, int width, int height) {
        boolean Result = false;
        String error = "";
        FaceFeature face = null;

        synchronized (this) {

            if (faceEngine == null || context == null || bgr24 == null || width % 4 != 0 || bgr24.length != width * height * 3) {
//                Log.e(TAG, "registerBgr24: false111");
                Log.e(TAG, "registerBgr24:  invalid params" + faceEngine);
                Result = false;
                if (faceEngine == null) {
                    error = "人脸引擎未初始化";
                }
                return new PicResult(Result, error);
            }


            //人脸检测
            List<FaceInfo> faceInfoList = new ArrayList<>();
            int code = faceEngine.detectFaces(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList);
            Log.e(TAG, "registerBgr24: " + code);
            Log.e(TAG, "detectFaces: " + faceInfoList.size());
            if (code == ErrorInfo.MOK && faceInfoList.size() > 0) {
                FaceFeature faceFeature = new FaceFeature();

                //特征提取
                code = faceEngine.extractFaceFeature(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);


                //保存注册结果（注册图、特征数据）
                if (code == ErrorInfo.MOK) {
                    //先比较数据库 1:N
                    FaceFeature tempFaceFeature = new FaceFeature();
                    tempFaceFeature.setFeatureData(faceFeature.getFeatureData());
                    face = tempFaceFeature;
                    error="身份证提取成功";
                    Result=true;

//                    return new PicResult(Result, error);
                }else {
                    Result=false;

                    error = "身份证提取失败";

                }


            }

        }
        return new PicResult(Result, error,face);
    }


    void deleteFaceToken(String teacherId,String studentId){
        UntyingfaceDataReq req = new UntyingfaceDataReq();
        req.type="1";
        if (!TextUtils.isEmpty(teacherId)){
            req.teacherId=teacherId;
        }else {
            req.studentId=studentId;
        }
        MyApp.getInstance().requestDataHRFace(this, req, new listener(teacherId,studentId), new error());
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

    class error implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }
    void postFaceToken( byte[] base64,String name,String studentId,String teacherId) {
        try {

            byte[] base = Base64.encode(base64, 2);
            String faceToken = new String(base);
//            byte[] featrue123 = Base64.decode(faceToken, 2);
//            Log.e("strbyte", str.length + "///" + faceToken.length() + "////" + featrue123.length);
            Log.e("strbyte", faceToken);

            VoicefaceReq req = new VoicefaceReq();
//            req.officeId= SpData.User().data.officeId;
            try {
                req.name = URLEncoder.encode(name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(studentId)){
                req.studentId = studentId;
            }
            if (!TextUtils.isEmpty(teacherId)){
                req.teacherId = teacherId;
            }
//            req.photo = convertToString(arcfacebitmap);
            req.facialFeatures = faceToken; //zhege
//        req.voiceContent = URLEncoder.encode(str);
            MyApp.getInstance().requestDataHRFace(this, req, new voiceListener(), new voiceError());
        } catch (Exception e) {

        }
    }
    class voiceListener implements Response.Listener<VoicefacesynRsp> {
        @Override
        public void onResponse(VoicefacesynRsp rsp) {

            if (rsp.status == BaseConstant.REQUEST_SUCCES ) {
//                Toast.makeText(context, rsp.info, Toast.LENGTH_SHORT).show();
            }

        }
    }

    class voiceError implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

            Log.e("jsonforxperror", volleyError.toString());
        }
    }
    //图片转化成Base64字符串
    public String convertToString(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] bts = baos.toByteArray();
        return Base64.encodeToString(bts, Base64.DEFAULT);
    }
    private Bitmap getHeadImage(byte[] originImageData, int width, int height, int orient, Rect cropRect, ArcSoftImageFormat imageFormat) {
        byte[] headImageData = ArcSoftImageUtil.createImageData(cropRect.width(), cropRect.height(), imageFormat);
        int cropCode = ArcSoftImageUtil.cropImage(originImageData, headImageData, width, height, cropRect, imageFormat);
        if (cropCode != ArcSoftImageUtilError.CODE_SUCCESS) {
            throw new RuntimeException("crop image failed, code is " + cropCode);
        }

        //判断人脸旋转角度，若不为0度则旋转注册图
        byte[] rotateHeadImageData = null;
        int rotateCode;
        int cropImageWidth;
        int cropImageHeight;
        // 90度或270度的情况，需要宽高互换
        if (orient == FaceEngine.ASF_OC_90 || orient == FaceEngine.ASF_OC_270) {
            cropImageWidth = cropRect.height();
            cropImageHeight = cropRect.width();
        } else {
            cropImageWidth = cropRect.width();
            cropImageHeight = cropRect.height();
        }
        ArcSoftRotateDegree rotateDegree = null;
        switch (orient) {
            case FaceEngine.ASF_OC_90:
                rotateDegree = ArcSoftRotateDegree.DEGREE_270;
                break;
            case FaceEngine.ASF_OC_180:
                rotateDegree = ArcSoftRotateDegree.DEGREE_180;
                break;
            case FaceEngine.ASF_OC_270:
                rotateDegree = ArcSoftRotateDegree.DEGREE_90;
                break;
            case FaceEngine.ASF_OC_0:
            default:
                rotateHeadImageData = headImageData;
                break;
        }
        // 非0度的情况，旋转图像
        if (rotateDegree != null){
            rotateHeadImageData = new byte[headImageData.length];
            rotateCode = ArcSoftImageUtil.rotateImage(headImageData, rotateHeadImageData, cropRect.width(), cropRect.height(), rotateDegree, imageFormat);
            if (rotateCode != ArcSoftImageUtilError.CODE_SUCCESS) {
                throw new RuntimeException("rotate image failed, code is " + rotateCode);
            }
        }
        // 将创建一个Bitmap，并将图像数据存放到Bitmap中
        Bitmap headBmp = Bitmap.createBitmap(cropImageWidth, cropImageHeight, Bitmap.Config.RGB_565);
        if (ArcSoftImageUtil.imageDataToBitmap(rotateHeadImageData, headBmp, imageFormat) != ArcSoftImageUtilError.CODE_SUCCESS) {
            throw new RuntimeException("failed to transform image data to bitmap");
        }
        return headBmp;
    }
    /**
     * 在特征库中搜索
     *
     * @param faceFeature 传入特征数据
     * @return 比对结果
     */
    public CompareResult getTopOfFaceLib(FaceFeature faceFeature) {
        if (faceEngine == null || isProcessing || faceFeature == null || faceRegisterInfoList == null || faceRegisterInfoList.size() == 0) {
            Log.e(TAG, "getTopOfFaceLib:  "+faceEngine);
            Log.e(TAG, "getTopOfFaceLib:  "+isProcessing);
            Log.e(TAG, "getTopOfFaceLib:  "+faceFeature);
            Log.e(TAG, "getTopOfFaceLib:  "+faceRegisterInfoList);
            Log.e(TAG, "getTopOfFaceLib:  "+faceRegisterInfoList.size());
            return null;
        }
        FaceFeature tempFaceFeature = new FaceFeature();
        FaceSimilar faceSimilar = new FaceSimilar();
        float maxSimilar = 0;
        int maxSimilarIndex = -1;
        isProcessing = true;

        Log.e(TAG, "getTopOfFaceLib: +dbsize"+ faceRegisterInfoList.size());
        Log.e(TAG, "getTopOfFaceLib: +faceFeature"+ faceFeature.getFeatureData());
        for (int i = 0; i < faceRegisterInfoList.size(); i++) {
            tempFaceFeature.setFeatureData(faceRegisterInfoList.get(i).getFeatureData());

            Log.e("facelenth1",faceRegisterInfoList.get(i).getFeatureData().length+"");
            faceEngine.compareFaceFeature(faceFeature, tempFaceFeature, faceSimilar); //faceFeature 当前摄像头的人脸  tempFaceFeature  是 faceRegisterInfoList内存中的人脸  faceSimilar 比对的相似度
            Log.e("facelenth",faceFeature.getFeatureData().length+"///"+tempFaceFeature.getFeatureData().length);
            Log.e("facelenth.get",faceSimilar.getScore()+"");//
            if (faceSimilar.getScore() > maxSimilar) {   //
                maxSimilar = faceSimilar.getScore();
                maxSimilarIndex = i;// maxSimilarIndex  比对的下标 、 现在的问题我说下
            }
        }
        isProcessing = false;
        if (maxSimilarIndex != -1) { //gg什么jb  你这里写的对吗  你这里返回的是单个resukt  这里面写的没问题的。下面的if不应该放到for循环里面吗 哪个if？  这里只需要返回一个
            Log.i("xupengface_xpxp","姓名："+faceRegisterInfoList.get(maxSimilarIndex).getName()+"/Perno:"+faceRegisterInfoList.get(maxSimilarIndex).getPerno()+"/time:"
                    +faceRegisterInfoList.get(maxSimilarIndex).getTime()+"/id:"+faceRegisterInfoList.get(maxSimilarIndex).getId());  //
            return new CompareResult(faceRegisterInfoList.get(maxSimilarIndex).getName(), maxSimilar,faceRegisterInfoList.get(maxSimilarIndex).getPerno()); //将大于0.0的相似度人脸 return出来
        }
        return null;
    }
    public CompareResult getTopOfFaceLib2(FaceFeature faceFeature) {

        if (faceEngine == null || isProcessing || faceFeature == null || faceRegisterInfoList == null || faceRegisterInfoList.size() == 0) {
            Log.e(TAG, "getTopOfFaceLib:  "+faceEngine);
            Log.e(TAG, "getTopOfFaceLib:  "+isProcessing);
            Log.e(TAG, "getTopOfFaceLib:  "+faceFeature);
            Log.e(TAG, "getTopOfFaceLib:  "+faceRegisterInfoList);

            return null;
        }
        FaceFeature tempFaceFeature = new FaceFeature();
        FaceSimilar faceSimilar = new FaceSimilar();
        float maxSimilar = 0;
        int maxSimilarIndex = -1;
        isProcessing = true;
        for (int i = 0; i < faceRegisterInfoList.size(); i++) {
            tempFaceFeature.setFeatureData(faceRegisterInfoList.get(i).getFeatureData());

            Log.e("facelenth1",faceRegisterInfoList.get(i).getFeatureData().length+"");
            faceEngine.compareFaceFeature(faceFeature, tempFaceFeature, faceSimilar); //faceFeature 当前摄像头的人脸  tempFaceFeature  是 faceRegisterInfoList内存中的人脸  faceSimilar 比对的相似度
            Log.e("facelenth",faceFeature.getFeatureData().length+"///"+tempFaceFeature.getFeatureData().length);
            Log.e("facelenth.get",faceSimilar.getScore()+"");//
            if (faceSimilar.getScore() > maxSimilar) {   //
                maxSimilar = faceSimilar.getScore();
                maxSimilarIndex = i;// maxSimilarIndex  比对的下标 、 现在的问题我说下
            }
        }
        isProcessing = false;
        if (maxSimilarIndex != -1) { //gg什么jb  你这里写的对吗  你这里返回的是单个resukt  这里面写的没问题的。下面的if不应该放到for循环里面吗 哪个if？  这里只需要返回一个
            Log.i("xupengface_xpxp","姓名："+faceRegisterInfoList.get(maxSimilarIndex).getName()+"/Perno:"+faceRegisterInfoList.get(maxSimilarIndex).getPerno()+"/time:"
                    +faceRegisterInfoList.get(maxSimilarIndex).getTime()+"/id:"+faceRegisterInfoList.get(maxSimilarIndex).getId());  //
            return new CompareResult(faceRegisterInfoList.get(maxSimilarIndex).getName(), maxSimilar,faceRegisterInfoList.get(maxSimilarIndex).getPerno()); //将大于0.0的相似度人脸 return出来
        }
        return new CompareResult("123",-1,"00");
    }
    public CompareResult getTopOfFaceLib3(FaceFeature faceFeature) {

        if (faceEngine == null || isProcessing || faceFeature == null || faceRegisterInfoList == null || faceRegisterInfoList.size() == 0) {
            Log.e(TAG, "getTopOfFaceLib3:  "+faceEngine);
            Log.e(TAG, "getTopOfFaceLib3:  "+isProcessing);
            Log.e(TAG, "getTopOfFaceLib3:  "+faceFeature);
            Log.e(TAG, "getTopOfFaceLib3:  "+faceRegisterInfoList);
            return null;
        }
//        FaceFeature tempFaceFeature = new FaceFeature();
        FaceSimilar faceSimilar = new FaceSimilar();
        float maxSimilar = 0;
        int maxSimilarIndex = -1;
        isProcessing = true;
////        for (int i = 0; i < faceRegisterInfoList.size(); i++) {
//            tempFaceFeature.setFeatureData(content);
            int jg=faceEngine.compareFaceFeature(faceFeature, NowFace, faceSimilar); //faceFeature 当前摄像头的人脸  tempFaceFeature  是 faceRegisterInfoList内存中的人脸  faceSimilar 比对的相似度
//            Log.e("facelenth",faceFeature.getFeatureData().length+"///"+tempFaceFeature.getFeatureData().length);
            Log.e("facelenth.get",faceSimilar.getScore()+"");//
            Log.e("compareFaceFeature",+jg+"");
        Log.e(TAG, "compareFaceFeaturefaceSimilar: "+JSON.toJSONString(faceSimilar) );
//            Log.e("compareFaceFeaturefaceSimilar",+JSON.toJSONString(faceSimilar));
            if (faceSimilar.getScore() > maxSimilar) {   //
                maxSimilar = faceSimilar.getScore();
//                maxSimilarIndex = i;// maxSimilarIndex  比对的下标 、 现在的问题我说下
            }
//        }
        isProcessing = false;
//        if (maxSimilarIndex != -1) { //gg什么jb  你这里写的对吗  你这里返回的是单个resukt  这里面写的没问题的。下面的if不应该放到for循环里面吗 哪个if？  这里只需要返回一个
//            Log.i("xupengface_xpxp","姓名："+faceRegisterInfoList.get(maxSimilarIndex).getName()+"/Perno:"+faceRegisterInfoList.get(maxSimilarIndex).getPerno()+"/time:"
//                    +faceRegisterInfoList.get(maxSimilarIndex).getTime()+"/id:"+faceRegisterInfoList.get(maxSimilarIndex).getId());  //
//            return new CompareResult(faceRegisterInfoList.get(maxSimilarIndex).getName(), maxSimilar,faceRegisterInfoList.get(maxSimilarIndex).getPerno()); //将大于0.0的相似度人脸 return出来


//        }
//        Log.e(TAG, "getTopOfFaceLib3==>:"+maxSimilar);
//        Log.e(TAG, "getTopOfFaceLib3==>:"+JSON.toJSONString(content));
        return new CompareResult("123",maxSimilar,"00");
//        Log.e(TAG, "getTopOfFaceLib3==>:"+maxSimilarIndex);

//        Log.e(TAG, "getTopOfFaceLib3:"+JSON.toJSONString())
    }
    /**
     * 将图像中需要截取的Rect向外扩张一倍，若扩张一倍会溢出，则扩张到边界，若Rect已溢出，则收缩到边界
     *
     * @param width   图像宽度
     * @param height  图像高度
     * @param srcRect 原Rect
     * @return 调整后的Rect
     */
    private static Rect getBestRect(int width, int height, Rect srcRect) {

        if (srcRect == null) {
            return null;
        }
        Rect rect = new Rect(srcRect);
        //1.原rect边界已溢出宽高的情况
        int maxOverFlow = 0;
        int tempOverFlow = 0;
        if (rect.left < 0) {
            maxOverFlow = -rect.left;
        }
        if (rect.top < 0) {
            tempOverFlow = -rect.top;
            if (tempOverFlow > maxOverFlow) {
                maxOverFlow = tempOverFlow;
            }
        }
        if (rect.right > width) {
            tempOverFlow = rect.right - width;
            if (tempOverFlow > maxOverFlow) {
                maxOverFlow = tempOverFlow;
            }
        }
        if (rect.bottom > height) {
            tempOverFlow = rect.bottom - height;
            if (tempOverFlow > maxOverFlow) {
                maxOverFlow = tempOverFlow;
            }
        }
        if (maxOverFlow != 0) {
            rect.left += maxOverFlow;
            rect.top += maxOverFlow;
            rect.right -= maxOverFlow;
            rect.bottom -= maxOverFlow;
            return rect;
        }
        //2.原rect边界未溢出宽高的情况
        int padding = rect.height() / 2;
        //若以此padding扩张rect会溢出，取最大padding为四个边距的最小值
        if (!(rect.left - padding > 0 && rect.right + padding < width && rect.top - padding > 0 && rect.bottom + padding < height)) {
            padding = Math.min(Math.min(Math.min(rect.left, width - rect.right), height - rect.bottom), rect.top);
        }

        rect.left -= padding;
        rect.top -= padding;
        rect.right += padding;
        rect.bottom += padding;
        return rect;
    }

    public PicResult registerBgr24Wechat(Context context, byte[] bgr24, int width, int height, String name,String teacherId,String studentId) {
        boolean Result=false;
        String error="";
        synchronized (this) {

            if (faceEngine == null || context == null || bgr24 == null || width % 4 != 0 || bgr24.length != width * height * 3) {


                Result=false;
                if (faceEngine==null){
                    error="人脸引擎未初始化";
                }else {
                    error="上传照片不清晰";
                }
                return new PicResult(Result,error);
            }

            if (ROOT_PATH == null) {
                ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ArcfaceVisitor";
            }
            boolean dirExists = true;

            //图片存储的文件夹
            File imgDir = new File(ROOT_PATH + File.separator + "register");

            if (!imgDir.exists()) {
                dirExists = imgDir.mkdirs();
            }
            if (!dirExists) {
                return new PicResult(false,"找不到该图片");
            }
            //人脸检测
            List<FaceInfo> faceInfoList = new ArrayList<>();
            int code = faceEngine.detectFaces(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList);
            Log.e(TAG, "registerBgr24Wechat: "+code );
            Log.e(TAG, "detectFacesWechat: "+faceInfoList.size() );
            if (code == ErrorInfo.MOK && faceInfoList.size() > 0) {
                FaceFeature faceFeature = new FaceFeature();

                //特征提取
                code = faceEngine.extractFaceFeature(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
                String userName = name == null ? String.valueOf("") : name;
                try {
                    //保存注册结果（注册图、特征数据）
                    if (code == ErrorInfo.MOK) {
                        //为了美观，扩大rect截取注册图
                        Rect cropRect = getBestRect(width, height, faceInfoList.get(0).getRect());
                        if (cropRect == null) {
                            return new PicResult(false,"上传照片不清晰");
                        }
                        if ((cropRect.width() & 1) == 1) {
                            cropRect.right--;
                        }
                        if ((cropRect.height() & 1) == 1) {
                            cropRect.bottom--;
                        }
                        File file = new File(imgDir + File.separator + userName + IMG_SUFFIX);
                        FileOutputStream fosImage = new FileOutputStream(file);
                        byte[] headBgr24 = ImageUtils.cropBgr24(bgr24, width, height, cropRect);
                        Bitmap headBmp = ImageUtils.bgrToBitmap(headBgr24, cropRect.width(), cropRect.height());
                        headBmp.compress(Bitmap.CompressFormat.JPEG, 100, fosImage);
                        fosImage.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                        //判断人脸旋转角度，若不为0度则旋转注册图
                        boolean needAdjust = false;
                        if (bitmap != null) {
                            switch (faceInfoList.get(0).getOrient()) {
                                case FaceEngine.ASF_OC_0:
                                    break;
                                case FaceEngine.ASF_OC_90:
                                    bitmap = ImageUtils.rotateBitmap(bitmap, 90);
                                    needAdjust = true;
                                    break;
                                case FaceEngine.ASF_OC_180:
                                    bitmap = ImageUtils.rotateBitmap(bitmap, 180);
                                    needAdjust = true;
                                    break;
                                case FaceEngine.ASF_OC_270:
                                    bitmap = ImageUtils.rotateBitmap(bitmap, 270);
                                    needAdjust = true;
                                    break;
                                default:
                                    break;
                            }
                        }
                        if (needAdjust) {
                            fosImage = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fosImage);
                            fosImage.close();
                        }
                        Date date = new Date();
                        String time = date.toLocaleString();
                        Log.i("md", "时间time为： "+time);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒 E");
                        String sim = dateFormat.format(date);



                        //内存中的数据同步
                        if (faceRegisterInfoList == null) {
                            faceRegisterInfoList = new ArrayList<>();
                        }
                        //先比较数据库 1:N
                        FaceFeature tempFaceFeature = new FaceFeature();
                        tempFaceFeature.setFeatureData(faceFeature.getFeatureData());

                        CompareResult result=getTopOfFaceLib2(tempFaceFeature);

                        Log.e(TAG, "CompareResult: "+JSON.toJSONString(result) );
                        if (result!=null) {

                            if (result.getSimilar() > SpData.facefortrue) {
                                //如存在 则对比信息

//                                if (result.getUserid().equals(teacherId) || result.getUserid().equals(studentId)) {
//                                    //如是同一个人覆盖
//                                    if (TextUtils.isEmpty(teacherId)) {
//                                        personInfor1 = new PersonInfor(null, studentId, faceFeature.getFeatureData(), userName, "男", sim);
//                                        faceRegisterInfoList.add(new FaceRegisterInfo(faceFeature.getFeatureData(), userName, studentId, Long.valueOf("123"), sim));  // 录入人脸信息添加到内存
//                                    } else {
//                                        personInfor1 = new PersonInfor(null, teacherId, faceFeature.getFeatureData(), userName, "男", sim);
//                                        faceRegisterInfoList.add(new FaceRegisterInfo(faceFeature.getFeatureData(), userName, teacherId, Long.valueOf("123"), sim));  // 录入人脸信息添加到内存
//
//                                    }
//                                    mDbController.insertOrReplace(personInfor1);//本地库 添加人脸信息   //加载到本地库中间
//                                    Log.e(TAG, "同一个人覆盖：" + JSON.toJSONString(personInfor1));  //每采集一个  本地内存存一个人脸信息。这个采集进去  而且在比对那里打印的是0.8-1之间  是没有任何问题的
//
//                                    postFaceToken(faceFeature.getFeatureData(), name, studentId, teacherId);
//                                    Result = true;
//                                    error = "";
//                                    return new PicResult(Result, "");
//                                } else {
//                                    Log.e(TAG, "照片已被" + result.getUserName() + "采集");  //每采集一个  本地内存存一个人脸信息。这个采集进去  而且在比对那里打印的是0.8-1之间  是没有任何问题的
//                                    Result = false;
//                                    error = "照片已被" + result.getUserName() + "采集";
//                                    return new PicResult(Result, error);
//                                }


                            } else {
                                //如不存在则直接添加
                                if (TextUtils.isEmpty(teacherId)) {
                                    personInfor1 = new PersonInfor(null, studentId, faceFeature.getFeatureData(), userName, "男", sim);
                                    faceRegisterInfoList.add(new FaceRegisterInfo(faceFeature.getFeatureData(), userName, studentId, Long.valueOf("123"), sim));  // 录入人脸信息添加到内存
                                } else {
                                    personInfor1 = new PersonInfor(null, teacherId, faceFeature.getFeatureData(), userName, "男", sim);
                                    faceRegisterInfoList.add(new FaceRegisterInfo(faceFeature.getFeatureData(), userName, teacherId, Long.valueOf("123"), sim));  // 录入人脸信息添加到内存
                                }
                                mDbController.insertOrReplace(personInfor1);//本地库 添加人脸信息   //加载到本地库中间
                                Log.e(TAG, "不存在添加：" + JSON.toJSONString(personInfor1));  //每采集一个  本地内存存一个人脸信息。这个采集进去  而且在比对那里打印的是0.8-1之间  是没有任何问题的
                                postFaceToken(faceFeature.getFeatureData(), name, studentId, teacherId);

                                Result = true;
                                error = "";
                                return new PicResult(Result, "");
                            }
//                       }else {
//                           Log.e(TAG, "registerBgr24: "+ "特征值提取失败");

//                       }
                        }else {
                            return new PicResult(false,"上传照片不清晰");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                Result=false;
                error="上传照片不清晰";

                return new PicResult(Result,error);
            }

        }
//            if (!Result){
//                deleteFaceToken(teacherId,studentId);
//            }
        return new PicResult(Result,error);
    }
}
