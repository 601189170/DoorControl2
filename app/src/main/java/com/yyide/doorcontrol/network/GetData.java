package com.yyide.doorcontrol.network;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.doorcontrol.base.BaseConstant;
import com.yyide.doorcontrol.requestbean.BaseBeanReq;
import com.yyide.doorcontrol.utils.AuthcodeTwo;
import com.yyide.doorcontrol.utils.L;
import com.yyide.doorcontrol.utils.MyHashMap;
import com.yyide.doorcontrol.utils.Object2Map;

import java.net.URLEncoder;

import static com.yyide.doorcontrol.utils.Object2Map.object2Map;


/**
 * Created by Hao on 16/9/6.
 */
public class GetData {
    // public static final String URL_IP = "120.76.189.190";
    public static final String URL_IP = "192.168.3.130";


    // 音频上传的接口
//    public static final String UPLOAD_URL = URL + "upload.html";
    public static final String FaceUrl = "http://120.76.189.190:8099/java-external-docking";
    //    public static final String FaceUrl = "http://192.168.3.130:8000/java-docking";
    public static final String url2 = "http://120.76.189.190";

    public static final String url3 = "http://192.168.3.130";

    public static String BIp = ":800/yide_manager/";

    public final static int PORT = 9123;


    /**
     * 网络请求的解析的密匙key
     **/
    public final static String URL_KEY = "24ca8a8a8a8888439b926572b5fb6233fb";

    public static String YydUrl2() {
        String url;
        String port = SPUtils.getInstance().getString(BaseConstant.NUMBER, GetData.URL_IP);

        if (port.contains(":")) {
            String[] ip = port.split(":");
            url = "http://" + ip[0];
            Log.e("TAG", "MinaUrl1: " + url);
        } else {
            url = "http://" + SPUtils.getInstance().getString(BaseConstant.NUMBER, URL_IP);
            Log.e("TAG", "MinaUrl2: " + url);
        }
        return url + BIp;
//        return "http://" + SPUtils.getInstance().getString(BaseConstant.NUMBER, URL_IP) + BIp;
    }

    public static <T> String requestUrlBP(BaseBeanReq<T> bean) {

        try {

            MyHashMap map = new MyHashMap();

            map.putAll(object2Map(bean));

            String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), URL_KEY);

            String urlRequest = MinaUrl2() + bean.myAddr() + "?input=" + URLEncoder.encode(encryStr, "UTF-8");

            L.d("JSON", urlRequest);

            L.d("JSON", JSON.toJSONString(bean));

            return urlRequest;

        } catch (Exception e) {

            return null;
        }

    }

    public static String MinaUrl2() {
        String url;
        String port = SPUtils.getInstance().getString(BaseConstant.NUMBER, GetData.URL_IP);

        if (port.contains(":")) {
            String[] ip = port.split(":");
            url = "http://" + ip[0];
            Log.e("TAG", "MinaUrl1: " + url);
        } else {
            url = "http://" + SPUtils.getInstance().getString(BaseConstant.NUMBER, URL_IP);
            Log.e("TAG", "MinaUrl2: " + url);
        }
        return url;

    }


    /**
     * 独立操作130端口请求
     */
    public static <T> String requestUrl130(BaseBeanReq<T> bean) {
        try {
            MyHashMap map = new MyHashMap();
            map.putAll(object2Map(bean));
            String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), URL_KEY);
            String urlRequest = "http://192.168.3.130:8022" + bean.myAddr() + "?input=" + URLEncoder.encode(encryStr, "UTF-8");//130环境
            //  String urlRequest = "http://192.168.3.109:8022" + bean.myAddr() + "?input=" + URLEncoder.encode(encryStr, "UTF-8");  //本地
            //  String urlRequest = "http://192.168.3.109:8890" + bean.myAddr() + "?input=" + URLEncoder.encode(encryStr, "UTF-8");
            // String urlRequest = "http://192.168.3.209:8080" + bean.myAddr() + "?input=" + URLEncoder.encode(encryStr, "UTF-8");//刘瑞斌本地
            //String urlRequest = "http://192.168.3.13:8080" + bean.myAddr() + "?input=" + URLEncoder.encode(encryStr, "UTF-8");
            // String urlRequest = "http://120.76.189.190" + bean.myAddr() + "?input=" + URLEncoder.encode(encryStr, "UTF-8");//黄文飞本地
            L.d("JSON本地", urlRequest);
            L.d("JSON本地", JSON.toJSONString(bean));
            return urlRequest;
        } catch (Exception e) {
            return null;
        }

    }


    public static <T> String requestUrlyd(BaseBeanReq<T> bean) {

        try {

            MyHashMap map = new MyHashMap();

            map.putAll(Object2Map.object2Map(bean));

            String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), URL_KEY);

            String urlRequest = YydUrl2() + bean.myAddr() + "?input=" + URLEncoder.encode(encryStr, "UTF-8");

            L.d("JSON", urlRequest);

            L.d("JSON", JSON.toJSONString(bean));

            return urlRequest;

        } catch (Exception e) {

            return null;
        }

    }

    public static <T> String requestUrlHRFace(BaseBeanReq<T> bean) {

        try {

            MyHashMap map = new MyHashMap();

            map.putAll(object2Map(bean));

            String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), URL_KEY);
            String urlRequest = FaceUrl + bean.myAddr() + "?input=" + URLEncoder.encode(encryStr, "UTF-8");
//            String urlRequest = FaceUrl + bean.myAddr() ;

            L.d("JSON2", urlRequest);

            L.d("JSON2", JSON.toJSONString(bean));

            return urlRequest;

        } catch (Exception e) {

            return null;
        }

    }

    public static <T> String requestUrl(BaseBeanReq<T> bean) {

        try {

            MyHashMap map = new MyHashMap();

            map.putAll(object2Map(bean));

            String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), URL_KEY);

            String urlRequest = url() + bean.myAddr() + "?input=" + URLEncoder.encode(encryStr, "UTF-8");

            L.d("JSON", urlRequest);

            L.d("JSON", JSON.toJSONString(bean));

            return urlRequest;

        } catch (Exception e) {

            return null;
        }

    }

    public static <T> String requestUrl3(BaseBeanReq<T> bean) {

        try {

            MyHashMap map = new MyHashMap();

            map.putAll(object2Map(bean));

            String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), URL_KEY);

            String urlRequest = url() + bean.myAddr();

            L.d("JSON2", urlRequest);

            L.d("JSON2", JSON.toJSONString(bean));

            return urlRequest;

        } catch (Exception e) {

            return null;
        }

    }

    public static <T> String requestUrl2(BaseBeanReq<T> bean) {

        try {

            MyHashMap map = new MyHashMap();

            map.putAll(object2Map(bean));

            String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), URL_KEY);

            String urlRequest = url2 + bean.myAddr() + "?input=" + URLEncoder.encode(encryStr, "UTF-8");

            L.d("JSON", urlRequest);

            L.d("JSON", JSON.toJSONString(bean));

            return urlRequest;

        } catch (Exception e) {

            return null;

        }

    }

    public static <T> String request130(BaseBeanReq<T> bean) {

        try {

            MyHashMap map = new MyHashMap();

            map.putAll(object2Map(bean));

            String encryStr = AuthcodeTwo.authcodeEncodeFace(map.toString(), URL_KEY);

            String urlRequest = "http://120.76.189.190" + bean.myAddr() + "?input=" + URLEncoder.encode(encryStr, "UTF-8");

            L.d("JSON", urlRequest);

            L.d("JSON", JSON.toJSONString(bean));

            return urlRequest;

        } catch (Exception e) {

            return null;
        }

    }


    public static <T> String requestFaceUrl(BaseBeanReq<T> bean) {

        try {

            MyHashMap map = new MyHashMap();

            map.putAll(object2Map(bean));

            String encryStr = AuthcodeTwo.authcodeEncodeFace(map.toString(), URL_KEY);

            String urlRequest = url() + bean.myAddr() + "?input=" + URLEncoder.encode(encryStr, "UTF-8");

            L.d("JSON", urlRequest);

            L.d("JSON", JSON.toJSONString(bean));

            return urlRequest;

        } catch (Exception e) {

            return null;
        }

    }

    public static String url() {
        return "http://" + SPUtils.getInstance().getString(BaseConstant.NUMBER, URL_IP);
    }

    public static String imageUrl() {
        return "http://" + SPUtils.getInstance().getString(BaseConstant.NUMBER, URL_IP) + "/";
    }

    public static String imageUrl2() {
        return "http://" + "120.76.189.190" + "/";
    }

    public static String YydUrl() {
//        Log.e("TAG", "YydUrl: "+ SPUtils.getInstance().getString(BaseConstant.NUMBER));
        return "http://" + SPUtils.getInstance().getString(BaseConstant.NUMBER, URL_IP) + BIp;
    }

    public static String uploadUrl() {
        return "http://" + SPUtils.getInstance().getString(BaseConstant.NUMBER, URL_IP) + "/" + "/upload.html";
    }
}
