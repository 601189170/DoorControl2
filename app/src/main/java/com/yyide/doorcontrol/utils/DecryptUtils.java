package com.yyide.doorcontrol.utils;




import com.yyide.doorcontrol.network.GetData;

import java.net.URLDecoder;


public class DecryptUtils {
    /***
     * 解密的工具类，从网络请求的数据都需要通过解密之后再使用，切记要定义成static才能暴露给外部调用
     * @param  encryptedData  传入的需要进行解密的原始字符串
     * @return 解密之后的字符串
     */
    public final static String decrypt(String encryptedData) {
        String result = null;
        try {
            // 通过和服务端约定好的Key来解析请求返回的加密字符串
            result = URLDecoder.decode(AuthcodeTwo.authcodeDecode(encryptedData, GetData.URL_KEY), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解密的工具类 主要解密存储的IP值
     *
     * @param encryptedData
     * @return
     */
    public final static String decryptIp(String encryptedData) {
        String result = null;
        try {
            // 通过和服务端约定好的Key来解析请求返回的加密字符串
            result = AuthcodeTwo.authcodeDecode(encryptedData, GetData.URL_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
