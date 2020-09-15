package com.yyide.doorcontrol.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SDcarfile {

    public static String read(String fileName) {

        FileInputStream fis = null;
        String result = null;

        try {
            File file = new File(Environment.getExternalStorageDirectory(), fileName + ".txt");
            fis = new FileInputStream(file);

            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            result = new String(buffer, "UTF-8");

        } catch (Exception ex) {

            ex.printStackTrace();

            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }
    //      * 定义文件保存的方法，写入到文件中，所以是输出流

    public static void save(String fileName, String content) {
        FileOutputStream fos = null;
        try {

            /* 判断sd的外部设置状态是否可以读写 */
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                File file = new File(Environment.getExternalStorageDirectory(), fileName + ".txt");
                Log.e("TAG", "getAbsolutePath: "+file.getAbsolutePath() );

                // 先清空内容再写入
                fos = new FileOutputStream(file);

                byte[] buffer = content.getBytes();
                fos.write(buffer);
                fos.close();
            }

        } catch (Exception ex) {

            ex.printStackTrace();

            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
