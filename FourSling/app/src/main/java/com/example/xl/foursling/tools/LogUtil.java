package com.example.xl.foursling.tools;

import android.app.Activity;
import android.os.Environment;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public class LogUtil {

    private static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static String fileName = "CBDALOG.txt";
    //日志等级
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static final int LEVEL = INFO;
    //等级一
    public static void v(String tag,String msg){
        if (LEVEL<=VERBOSE){
            long times = System.currentTimeMillis();
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss:SSS");
            String times_this =df.format(new Date(times));
            Log.v(tag, msg);
            String content = times_this+"   "+tag+"   [1][VERBOSE]："+msg;
            writeTxtToFile(content,filePath,fileName);
        }
    }
    //等级二
    public static void d(String tag,String msg){
        if (LEVEL<= DEBUG){
            long times = System.currentTimeMillis();
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss:SSS");
            String times_this =df.format(new Date(times));
            Log.d(tag,msg);
            String content = times_this+"   "+tag+"   [2][DEBUG]："+msg;
            writeTxtToFile(content,filePath,fileName);
        }
    }
    //等级三
    public static void i(String tag,String msg){
        if (LEVEL<=INFO){
            long times = System.currentTimeMillis();
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss:SSS");
            String times_this =df.format(new Date(times));
            Log.i(tag,msg);
            String content = times_this+"   "+tag+"   [3][INFO]："+msg;
            writeTxtToFile(content,filePath,fileName);
        }
    }
    //等级四
    public static void w(String tag,String msg){
        if (LEVEL<= WARN){
            long times = System.currentTimeMillis();
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss:SSS");
            String times_this =df.format(new Date(times));
            Log.w(tag,msg);
            String content = times_this+"   "+tag+"   [4][WARN]："+msg;
            writeTxtToFile(content,filePath,fileName);
        }
    }
    //等级五
    public static void e(String tag, String msg){
        if (LEVEL<=ERROR){
            long times = System.currentTimeMillis();
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss:SSS");
            String times_this =df.format(new Date(times));
            Log.e(tag, msg);
            String content = times_this+"   "+tag+"  "+msg;
            writeTxtToFile(content,filePath,fileName);
//           PrintStream(new File(Environment.getExternalStorageDirectory() + "/error.log"))
        }
    }

    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath+"/"+fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }
}
