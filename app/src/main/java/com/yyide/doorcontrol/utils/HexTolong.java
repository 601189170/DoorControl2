package com.yyide.doorcontrol.utils;

/**
 * Created by Administrator on 2020/7/18.
 */

public class HexTolong {
    /**
     * isBlank
     *
     * @param value
     * @return true: blank; false: not blank
     */
    private static boolean isBlank(String value) {
        if (value == null || "".equals(value.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是16进制数
     *
     * @param strHex
     * @return
     */
    public static boolean isHex(String strHex) {
        int i = 0;
        if (strHex.length() > 2) {
            if (strHex.charAt(0) == '0' && (strHex.charAt(1) == 'X' || strHex.charAt(1) == 'x')) {
                i = 2;
            }
        }
        for (; i < strHex.length(); ++i) {
            char ch = strHex.charAt(i);
            if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f'))
                continue;
            return false;
        }
        return true;
    }

    /**
     * 计算16进制对应的数值
     *
     * @param ch
     * @return
     * @throws Exception
     */
    private static int getHex(char ch) throws Exception {
        if (ch >= '0' && ch <= '9')
            return (int) (ch - '0');
        if (ch >= 'a' && ch <= 'f')
            return (int) (ch - 'a' + 10);
        if (ch >= 'A' && ch <= 'F')
            return (int) (ch - 'A' + 10);
        throw new Exception("error param");
    }

    /**
     * 计算幂
     *
     * @param nValue
     * @param nCount
     * @return
     * @throws Exception
     */
    private static long getPower(int nValue, int nCount) throws Exception {
        if (nCount < 0)
            throw new Exception("nCount can't small than 1!");
        if (nCount == 0)
            return 1;
        long nSum = 1;
        for (int i = 0; i < nCount; ++i) {
            nSum = nSum * nValue;
        }
        return nSum;
    }

    /**
     * 16进制转10进制，对16进制数的每一位数乘以其对应的16的幂，相加。
     * @param strHex 待转换的字符串
     * @param force 是否强制按16进制转换，纯数字也可能是16进制，true则将纯数字按16进制处理
     * @return
     */
    public static long hexToLong(String strHex, boolean force) {
        long nResult = 0;
        String regex = "^\\d{1,}$";
        if (!isBlank(strHex)) {
            strHex = strHex.trim();
        } else {
            return nResult;
        }
        if (!force && strHex.matches(regex)) {
            return Long.parseLong(strHex);
        }
        if (!isHex(strHex)) {
            return nResult;
        }
        String str = strHex.toUpperCase();
        if (str.length() > 2) {
            if (str.charAt(0) == '0' && str.charAt(1) == 'X') {
                str = str.substring(2);
            }
        }
        int nLen = str.length();
        for (int i = 0; i < nLen; ++i) {
            char ch = str.charAt(nLen - i - 1);
            try {
                nResult += (getHex(ch) * getPower(16, i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return nResult;
    }

    /**
     * 16进制转10进制
     * @param strHex
     * @return
     */
    public static long hexToLong(String strHex) {
        return hexToLong(strHex, false);
    }


}
