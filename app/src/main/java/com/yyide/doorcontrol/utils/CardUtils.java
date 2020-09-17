package com.yyide.doorcontrol.utils;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

public class CardUtils {
    private static CardUtils instance;

    public static CardUtils getInstance() {
        if (instance == null)
            instance = new CardUtils();
        return instance;
    }

    // 4字节卡号，IC卡
    private ArrayList<Integer> B4_HEAD = new ArrayList<>();
    // 5字节卡号，ID卡
    private ArrayList<Integer> B5_HEAD = new ArrayList<>();
    // 8字节卡号，其他
    private ArrayList<Integer> B8_HEAD = new ArrayList<>();
    // 刷卡数据
    private ArrayList<Integer> CD_LIST = new ArrayList<>();

    private CardUtils() {
        Collections.addAll(B4_HEAD, 0x02, 0x09);
        Collections.addAll(B5_HEAD, 0x02, 0x0A);
        Collections.addAll(B8_HEAD, 0x02, 0x0D);
    }

    /**
     * 获取校验码
     * @param start_index   起始位置
     * @param end_index     结束位置
     * @return 校验码
     */
    private int getCheckCode(int start_index, int end_index) {
        int check_code = CD_LIST.get(start_index);
        for (int i = start_index + 1; i <= end_index; i++) {
            check_code ^= CD_LIST.get(i);
        }
        return check_code;
    }

    /**
     * 16进制卡号转10进制卡号
     * @param start_index   起始位置
     * @param end_index     结束位置
     * @return 10进制卡号
     */
    private String convertCardNo(int start_index, int end_index) {
        StringBuilder builder = new StringBuilder();
        for (int i = start_index; i <= end_index; i++) {
            builder.append(String.format("%02X", CD_LIST.get(i)));
        }
        return String.valueOf(Long.parseLong(builder.toString(), 16));
    }

    /**
     * 获取卡号
     * @param data      刷卡数据
     * @param offset    起始位置
     * @param length    数据长度
     * @return 卡号，非本系统卡返回null，数据不完整时返回null
     */
    @Nullable
    public String getCardNo(byte[] data, int offset, int length) {
        if (data != null) {
            // 数据转换
            for (int i = offset; i < length; i++) {
                CD_LIST.add(data[i] & 0xFF);
            }
        }

        int start_index = -1;
        // 判断是否存在4字节卡号的起始标志
        if ((start_index = Collections.lastIndexOfSubList(CD_LIST, B4_HEAD)) >= 0) {
            // 判断4字节卡数据完整性（共9字节，起始＋长度＋类型＋4节卡号＋校验＋结束）
            if (start_index + 9 <= CD_LIST.size()
                    // 判断结束码是否匹配
                    && CD_LIST.get(start_index + 8) == 0x03
                    // 卡数据校验，判断是否匹配校验码
                    && getCheckCode(start_index + 1, start_index + 6) == CD_LIST.get(start_index + 7)) {
                String card_no = convertCardNo(start_index + 3, start_index + 6);
                CD_LIST.clear();
                return card_no;
            }
        }

        // 判断是否存在5字节卡号的起始标志
        if ((start_index = Collections.lastIndexOfSubList(CD_LIST, B5_HEAD)) >= 0) {
            // 判断5字节卡数据完整性（共10字节，起始＋长度＋类型＋5节卡号＋校验＋结束）
            if (start_index + 10 <= CD_LIST.size()
                    // 判断结束码是否匹配
                    && CD_LIST.get(start_index + 9) == 0x03
                    // 卡数据校验，判断是否匹配校验码
                    && getCheckCode(start_index + 1, start_index + 7) == CD_LIST.get(start_index + 8)) {
                String card_no = convertCardNo(start_index + 3, start_index + 7);
                CD_LIST.clear();
                return card_no;
            }
        }

        // 判断是否存在8字节卡号的起始标志
        //        if ((start_index = Collections.lastIndexOfSubList(CD_LIST, B8_HEAD)) >= 0) {
        //            // 判断5字节卡数据完整性（共13字节，起始＋长度＋类型＋8节卡号＋校验＋结束）
        //            if (start_index + 13 <= CD_LIST.size()
        //                    // 判断结束码是否匹配
        //                    && CD_LIST.get(start_index + 12) == 0x03
        //                    // 卡数据校验，判断是否匹配校验码
        //                    && getCheckCode(start_index + 1, start_index + 10) == CD_LIST.get(start_index + 11)) {
        //                String card_no = convertCardNo(start_index + 3, start_index + 10);
        //                CD_LIST.clear();
        //                return card_no;
        //            }
        //        }
        return null;
    }
}
