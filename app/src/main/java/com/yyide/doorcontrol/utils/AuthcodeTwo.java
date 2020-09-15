package com.yyide.doorcontrol.utils;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Random;

public class AuthcodeTwo {
    public enum DiscuzAuthcodeMode {
        Encode, Decode
    }

    ;

    // / <summary>
    // / 从字符串的指定位置截取指定长度的子字符串
    // / </summary>
    // / <param name="str">原字符串</param>
    // / <param name="startIndex">子字符串的起始位置</param>
    // / <param name="length">子字符串的长度</param>
    // / <returns>子字符串</returns>
    public static String CutString(String str, int startIndex, int length) {
        if (startIndex >= 0) {
            if (length < 0) {
                length = length * -1;
                if (startIndex - length < 0) {
                    length = startIndex;
                    startIndex = 0;
                } else {
                    startIndex = startIndex - length;
                }
            }

            if (startIndex > str.length()) {
                return "";
            }

        } else {
            if (length < 0) {
                return "";
            } else {
                if (length + startIndex > 0) {
                    length = length + startIndex;
                    startIndex = 0;
                } else {
                    return "";
                }
            }
        }

        if (str.length() - startIndex < length) {

            length = str.length() - startIndex;
        }

        return str.substring(startIndex, startIndex + length);
    }

    // / <summary>
    // / 从字符串的指定位置开始截取到字符串结尾的了符串
    // / </summary>
    // / <param name="str">原字符串</param>
    // / <param name="startIndex">子字符串的起始位置</param>
    // / <returns>子字符串</returns>
    public static String CutString(String str, int startIndex) {
        return CutString(str, startIndex, str.length());
    }

    // / <summary>
    // / 返回文件是否存在
    // / </summary>
    // / <param name="filename">文件名</param>
    // / <returns>是否存在</returns>
    public static boolean FileExists(String filename) {
        File f = new File(filename);
        return f.exists();
    }

    // / <summary>
    // / MD5函数
    // / </summary>
    // / <param name="str">原始字符串</param>
    // / <returns>MD5结果</returns>
    // public static String MD5(String str) {
    // return md5.getMD5ofStr(str);
    // }

    // / <summary>
    // / 字段串是否为Null或为""(空)
    // / </summary>
    // / <param name="str"></param>
    // / <returns></returns>
    public static boolean StrIsNullOrEmpty(String str) {
        // #if NET1
        if (str == null || str.trim().equals("")) {
            return true;
        }

        return false;
    }

    // / <summary>
    // / 用于 RC4 处理密码
    // / </summary>
    // / <param name="pass">密码字串</param>
    // / <param name="kLen">密钥长度，一般为 256</param>
    // / <returns></returns>
    static private byte[] GetKey(byte[] pass, int kLen) {
        byte[] mBox = new byte[kLen];

        for (int i = 0; i < kLen; i++) {
            mBox[i] = (byte) i;
        }

        int j = 0;
        for (int i = 0; i < kLen; i++) {

            j = (j + (int) ((mBox[i] + 256) % 256) + pass[i % pass.length])
                    % kLen;

            byte temp = mBox[i];
            mBox[i] = mBox[j];
            mBox[j] = temp;
        }

        return mBox;
    }

    // / <summary>
    // / 生成随机字符
    // / </summary>
    // / <param name="lens">随机字符长度</param>
    // / <returns>随机字符</returns>
    public static String RandomString(int lens) {
        char[] CharArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int clens = CharArray.length;
        String sCode = "";
        Random random = new Random();
        for (int i = 0; i < lens; i++) {
            sCode += CharArray[Math.abs(random.nextInt(clens))];
        }
        return sCode;
    }

    // / <summary>
    // / 使用 Discuz authcode 方法对字符串加密
    // / </summary>
    // / <param name="source">原始字符串</param>
    // / <param name="key">密钥</param>
    // / <param name="expiry">加密字串有效时间，单位是秒</param>
    // / <returns>加密结果</returns>
    public static String authcodeEncode(String source, String key, int expiry) {
        return authcode(source, key, DiscuzAuthcodeMode.Encode, expiry);
    }

    // / <summary>
    // / 使用 Discuz authcode 方法对字符串加密
    // / </summary>
    // / <param name="source">原始字符串</param>
    // / <param name="key">密钥</param>
    // / <returns>加密结果</returns>
    public static String authcodeEncode(String source, String key) {
        return authcode(source, key, DiscuzAuthcodeMode.Encode, 0);
    }

    public static String authcodeEncodeFace(String source, String key) {
        try {
            return authcode(URLEncoder.encode(source, "UTF-8"), key, DiscuzAuthcodeMode.Encode, 0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    // / <summary>
    // / 使用 Discuz authcode 方法对字符串解密
    // / </summary>
    // / <param name="source">原始字符串</param>
    // / <param name="key">密钥</param>
    // / <returns>解密结果</returns>
    public static String authcodeDecode(String source, String key) {
        return authcode(source, key, DiscuzAuthcodeMode.Decode, 0);
    }

    // / <summary>
    // / 使用 变形的 rc4 编码方法对字符串进行加密或者解密
    // / </summary>
    // / <param name="source">原始字符串</param>
    // / <param name="key">密钥</param>
    // / <param name="operation">操作 加密还是解密</param>
    // / <param name="expiry">加密字串过期时间</param>
    // / <returns>加密或者解密后的字符串</returns>
    private static String authcode(String source, String key,
                                   DiscuzAuthcodeMode operation, int expiry) {
        try {
            if (source == null || key == null) {
                return "";
            }

            int ckey_length = 0;
            String keya, keyb, keyc, cryptkey, result;

            key = MD52(key);

            keya = MD52(CutString(key, 0, 16));

            keyb = MD52(CutString(key, 16, 16));

            keyc = ckey_length > 0 ? (operation == DiscuzAuthcodeMode.Decode ? CutString(
                    source, 0, ckey_length) : RandomString(ckey_length))
                    : "";

            cryptkey = keya + MD52(keya + keyc);

            if (operation == DiscuzAuthcodeMode.Decode) {
                byte[] temp;

                temp = Base64.decode(CutString(source, ckey_length));
                result = new String(RC4(temp, cryptkey));
                if (CutString(result, 10, 16).equals(
                        CutString(MD52(CutString(result, 26) + keyb), 0, 16))) {
                    return CutString(result, 26);
                } else {
                    temp = Base64.decode(CutString(source + "=", ckey_length));
                    result = new String(RC4(temp, cryptkey));
                    if (CutString(result, 10, 16)
                            .equals(CutString(
                                    MD52(CutString(result, 26) + keyb), 0, 16))) {
                        return CutString(result, 26);
                    } else {
                        temp = Base64.decode(CutString(source + "==",
                                ckey_length));
                        result = new String(RC4(temp, cryptkey));
                        if (CutString(result, 10, 16).equals(
                                CutString(MD52(CutString(result, 26) + keyb),
                                        0, 16))) {
                            return CutString(result, 26);
                        } else {
                            return "2";
                        }
                    }
                }
            } else {
                source = "0000000000" + CutString(MD52(source + keyb), 0, 16)
                        + source;

                byte[] temp = RC4(source.getBytes("GBK"), cryptkey);

                return keyc + Base64.encode(temp);

            }
        } catch (Exception e) {
            return "";
        }

    }

    // / <summary>
    // / RC4 原始算法
    // / </summary>
    // / <param name="input">原始字串数组</param>
    // / <param name="pass">密钥</param>
    // / <returns>处理后的字串数组</returns>
    private static byte[] RC4(byte[] input, String pass) {
        if (input == null || pass == null)
            return null;

        byte[] output = new byte[input.length];
        byte[] mBox = GetKey(pass.getBytes(), 256);

        // 加密
        int i = 0;
        int j = 0;

        for (int offset = 0; offset < input.length; offset++) {
            i = (i + 1) % mBox.length;
            j = (j + (int) ((mBox[i] + 256) % 256)) % mBox.length;

            byte temp = mBox[i];
            mBox[i] = mBox[j];
            mBox[j] = temp;
            byte a = input[offset];

            // byte b = mBox[(mBox[i] + mBox[j] % mBox.Length) % mBox.Length];
            // mBox[j] 一定比 mBox.Length 小，不需要在取模
            byte b = mBox[(toInt(mBox[i]) + toInt(mBox[j])) % mBox.length];

            output[offset] = (byte) ((int) a ^ (int) toInt(b));
        }

        return output;
    }

    public static String MD52(String MD5) {
        StringBuffer sb = new StringBuffer();
        String part = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5 = md.digest(MD5.getBytes());

            for (int i = 0; i < md5.length; i++) {
                part = Integer.toHexString(md5[i] & 0xFF);
                if (part.length() == 1) {
                    part = "0" + part;
                }
                sb.append(part);
            }

        } catch (NoSuchAlgorithmException ex) {
        }
        return sb.toString();

    }

    public static int toInt(byte b) {
        return (int) ((b + 256) % 256);
    }

    public long getUnixTimestamp() {
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * main测试方法
     */
//	public static void main(String[] args) {
//		try {
//			MyHashMap map = new MyHashMap();
//			map.put("classesId", "24ca2bc0a088439b926572b5fb6233fb");
//			System.out.println(URLEncoder.encode(AuthcodeTwo.authcodeEncode(
//					map.toString(), ConstantUtil.UrlConstant.URL_KEY),
//					"utf-8"));
//
//			String result = AuthcodeTwo
//					.authcodeDecode(
//							"wFV3jn6/lLI17LQ8fXiG+RhTSTU6vrh3HQmm/8iVBnfISkztUUFd3OxxwARGOWU2aOcBYrZjv/RE2W1it0BI0VS/oFyivXz8Zox11qyyHeD0vAkf58Gf3ouq5j693+sUEfGyEz400e7ojovIjfzLqkWt0S48NonADcS1Lt/6qVK5clE6UUKpyaaRIVJR6DNarbpq2ERWv/2WxBTuoJ5LJp67TlPTDpqai6ZXKQLLu20wAM95IFPkBwyCOfiApKOLQouW4CFjWki4m4XLHH4EKUY69NJvmOGbrsga5h+a1tqI8TcX4X8hWD09IUUXINPShtiowIdORCQrUFAOml8Ikk5lhpgFdRI/RU0CE0TLIWX0yGiJmIK4xeTSsmMXX91cZlGIcL4JMa25Tz5way8uQi4e2pWwmlqOTNjnrVEPVM0O9UkdrDL2YcoDsRtFupn1tjtOzVMEm6yPOYMhUnYXLfx9NUB/sDdS+1ptGEfzEVBipRvCJQbaLgURX3diJ9e/3H7jbVEtBkm22GHXf8AHvMJPUyq1EvsMtEFdmvGHIMP0bV4/kvPP3JbioXTatwmi9SkJudMIuZOUJ596F0bdpciTIoCDPOlPRvna5Xa+0nA2bnY5451qu/tZjkLzwnkMF1q1ZwwjdS55Na5R/lrC22oFbO7LwHD3+IwcV/ZGzt0BnZi0lKK1OhqU94qTSu3qBA0ofQyK/+3asi3nitGJLYHHXHVpDvU3RXqn5rGTTJHIKNddoBjl0aC5iRHCPIk1PgmFrkjn1ehMy22fkeGKQ9YpfFuWC8w3Txo8dk6u31/Wfkzsak4x324BMtuNeDQ5MOdRqOmtp8HWqmzdyw3ExfWmsCH01BilzI6TRbA3UNyh5K+6ZTxQSI+vDnGHo+6wD+ADkZudDMKO59EzX4pf2OgET7zgLGc0p+Tq4EkwNLPPsV+8XC2Y5498vvvBKm6rlj5fTPj9g7ioe32+KMNk2x/mLu3cO3dGluN1dQ+wzlQ4szZsZHOJZqwvvBaeQJVMdd3wFAXW39I+nG/pgYO/UmztAOY4h0d8upYJpvysiX0KLeAO8+f/0Ei81Nk2MWrDaZhPoyIsQOJ2KtHz0nBPpa8Of3DFD6jwm6gtZ4p8uGA2ByNoYak5iBKyzKd/WgBu3TCdgLIqObDSAi6iWYPBZ9XsZtRqDuMNCnjQxw/GhknVkkGMTNhQhqUFPbRMgfxC/MBU9duAUJF1Acrv0/dejpqN/0kGNJ3IDQO+rTaT3+/LDV+R3aDLofw2TXf2TcWUu0kHOXF2AoaI2REmV64oThxjpewh94QmB3eHdKCo0g==",
//							ConstantUtil.UrlConstant.URL_KEY);
//
//			System.out.println("请求结果为：" + result);
//
//			String a = AuthcodeTwo
//					.authcodeDecode(
//							"c6d50FVvmklFRl+kfPxFfPH5mhohyMzDvhUbxA/OIPL1581BST2Hoez+NmRXGqvAU2E++ws7kK0xiFF1uftniyzWsFknzv+7rppagTzTOwLACYDPeao1hYRKFLQxqk3B/iTeJl2m4ZbAaljBqjE2xONRvQN1hrbLOuAjWD0BVJRZ5rANS6IwSXkwD7qDuX8irXlwx9BarF7mv3/QWny36/HFWA",
//							"a77czxGu2du6xv2Q0btTM2xftzoQt2qmq3NNslc");
//			// String a =
//			// AuthcodeTwo.authcodeDecode("04JViSwdw77NxevtH9t5lYwB3YLtA5t3dgBgczIwLZg5yPAjN5kE3xNUttZpUsayViMbNqHVdLAJEJOxBjOB0d6F0fLegigdxHIHJGSwsvwQh+HnPq0wfft80MnCOoamNoEQeqqPyB0/ezhocj2NsfaV2WOix1xAhYv6flf6SeYCUFqE9TiY6wWweWZty81uKZHt5BmVRKvy4EEdjdMXkg==",
//			// "a77czxGu2du6xv2Q0btTM2xftzoQt2qmq3NNslc");
//			System.out.println(a);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}

}
