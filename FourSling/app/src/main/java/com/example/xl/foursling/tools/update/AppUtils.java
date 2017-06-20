package com.example.xl.foursling.tools.update;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;


import com.example.xl.foursling.tools.AppManager;
import com.example.xl.foursling.view.ProgressDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zk on 2016/12/7.
 */

public class AppUtils {
    private static Toast toast;

    /**
     * getMetaValue(获取api_key)
     *
     * @Title: getMetaValue
     * @Description: TODO
     * @param @param context
     * @param @param metaKey
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * 判断当前是否有可用的网络以及网络类型 0：无网络 1：WIFI 2：CMWAP 3：CMNET
     *
     * @param context
     * @return 0：无网络 1：WIFI 2：CMWAP 3：CMNET
     */
    public static int isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return 0;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return 1;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            String extraInfo = netWorkInfo.getExtraInfo();
                            if ("cmwap".equalsIgnoreCase(extraInfo)
                                    || "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
                                return 2;
                            }
                            return 3;
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * getSDPath(获取存储卡路径)
     *
     * @Title: getSDPath
     * @Description: TODO
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            File file = new File(sdDir.toString() + "/idaihuo");
            if (!file.exists()) {
                file.mkdir();
            }
            return file.toString();
        } else {
            String filePath = Environment.getDataDirectory().getPath();
            File file = new File(filePath.toString() + "/idaihuo");
            if (!file.exists()) {
                file.mkdir();
            }
            return file.toString();
        }
    }

    /**
     * md5ForLower(MD5加密算法，返回小写密文)
     *
     * @Title: md5ForLower
     * @Description: TODO
     * @param @param sourceStr
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public static String md5ForLower(String sourceStr) {
        byte[] source = sourceStr.getBytes();
        String s = null;
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();
            char[] str = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (Exception e) {
        }
        return s;

    }

    /**
     * md5ForCapital(MD5加密算法，返回大写密文)
     *
     * @Title: md5ForCapital
     * @Description: TODO
     * @param @param sourceStr
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public static String md5ForCapital(String sourceStr) {
        byte[] source = sourceStr.getBytes();
        String s = null;
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();
            char[] str = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (Exception e) {
        }
        return s;

    }

    /**
     * getVersionName(获取app版本号)
     *
     * @Title: getVersionName
     * @Description: TODO
     * @param @param ct
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public static String getVersionName(Context ct) {
        String versionName = null;
        PackageManager packageManager = ct.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(
                    ct.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * isUpdate(是否更新)
     *
     * @Title: isUpdate
     * @Description: TODO
     * @param @param oldVersionName
     * @param @param newVersionName
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public static boolean isUpdate(String newVersionName, String oldVersionName) {
        boolean result = false;
        try {
            String n[] = newVersionName.split("\\.");
            String o[] = oldVersionName.split("\\.");
            Log.e("zengkang","newVersionName===="+ n.toString());
            Log.e("zengkang","oldVersionName===="+ o);
            for (int i = 0; i < n.length; i++) {
                if (Integer.parseInt(n[i]) > Integer.parseInt(o[i])) {
                    return true;
                }
                if (Integer.parseInt(n[i]) < Integer.parseInt(o[i])) {
                    return false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * setMaxExpressNO(设定当前最大单号)
     *
     * @Title: setMaxExpressNO
     * @Description: TODO
     * @param @param context
     * @param @param maxExpressNO 设定文件
     * @return void 返回类型
     * @throws
     */
    public static void setMaxExpressNO(Context context, String maxExpressNO) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("maxExpressNO", maxExpressNO);
        editor.commit();
    }

    /**
     * compareNO(单号比较，小于本地单号不更新)
     *
     * @Title: compareNO
     * @Description: TODO
     * @param @param newNO
     * @param @param oldNO
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public static boolean compareNO(String newNO, String oldNO) {
        if (oldNO == null) {
            return true;
        }
        String no1 = newNO.substring(8, newNO.length());
        String no2 = oldNO.substring(8, oldNO.length());
        int n1 = Integer.parseInt(no1);
        int n2 = Integer.parseInt(no2);
        return n1 > n2;
    }

    /**
     * getLoaclExpressNO(取得当前本地单号)
     *
     * @Title: getLoaclExpressNO
     * @Description: TODO
     * @param @param context
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public static String getLoaclExpressNO(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String maxExpressNO = sp.getString("maxExpressNO", null);
        return maxExpressNO;
    }

    /**
     * getExpressNO(生成新单号)
     *
     * @Title: getExpressNO
     * @Description: TODO
     * @param @param context
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public static String getExpressNO(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String maxExpressNO = sp.getString("maxExpressNO", null);
        if (maxExpressNO != null && maxExpressNO.length() > 8) {
            String front = maxExpressNO.substring(0, 8);
            String behind = maxExpressNO.substring(8, maxExpressNO.length());
            long temp = Integer.parseInt(behind) + 1;
            String result = new DecimalFormat("0000000").format(temp);
            setMaxExpressNO(context, front + result);
            return front + result;
        } else {
            return null;
        }
    }

    public static void minusExpressNO(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String maxExpressNO = sp.getString("maxExpressNO", null);
        if (maxExpressNO != null && maxExpressNO.length() > 8) {
            String front = maxExpressNO.substring(0, 8);
            String behind = maxExpressNO.substring(8, maxExpressNO.length());
            long temp = Integer.parseInt(behind) - 1;
            String result = new DecimalFormat("0000000").format(temp);
            setMaxExpressNO(context, front + result);
        }
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param mContext
     * @param className
     *            判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection,
                        null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }

        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

//    public static void displayImage(Context context, ImageView imageView,
//                                    String url) {
//        BitmapUtils utils = new BitmapUtils(context);
//        utils.clearCache();
//        BitmapDisplayConfig config = new BitmapDisplayConfig();
//        config.setLoadingDrawable(context.getResources().getDrawable(
//                R.drawable.user));
//        config.setLoadFailedDrawable(context.getResources().getDrawable(
//                R.drawable.user));
//        config.setBitmapMaxSize(new BitmapSize(60, 60));
//        utils.display(imageView, url, config);
//    }

    /**
     * 创建普通对话框
     *
     * @param context
     *            上下文 必填
     * @param message
     *            显示内容 必填
     * @param btnName
     *            按钮名称 必填
     * @param listener
     *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createNormalDialog(Context context, String title,
                                            String message, String btnName, DialogInterface.OnClickListener listener) {
        Dialog dialog = null;
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                context);
        // 设置对话框的标题
        builder.setTitle(title);
        // 设置对话框的显示内容
        builder.setMessage(message);
        // 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
        builder.setPositiveButton(btnName, listener);
        // 创建一个普通对话框
        dialog = builder.create();
        return dialog;
    }

    /**
     * 创建普通对话框
     *
     * @param context
     *            上下文 必填
     * @param message
     *            显示内容 必填
     * @param btn1Name,btn2Name
     *            按钮名称 必填
     * @param listener
     *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createChooseDialog(Context context, String title,
                                            String message, String btn1Name, String btn2Name,
                                            DialogInterface.OnClickListener listener, DialogInterface.OnClickListener listener2) {
        Dialog dialog = null;
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                context);
        // 设置对话框的标题
        builder.setTitle(title);
        // 设置对话框的显示内容
        builder.setMessage(message);
        // 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
        builder.setPositiveButton(btn1Name, listener);
        builder.setNeutralButton(btn2Name, listener2);
        // 创建一个普通对话框
        dialog = builder.create();
        return dialog;
    }

    /**
     * @param context
     * @param installPath
     *            根据文件路径安装APK
     */
    public static void install(Context context, String installPath) {
        // 创建URI
        Uri uri = Uri.fromFile(new File(installPath));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 启动新的activity
        // 设置Uri和类型
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        // 执行安装
        context.startActivity(intent);
    }

    /**
     * @param path
     *            获取url文件名
     * @return
     */
    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }

    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
    }

    /**
     * @param file
     *            删除文件
     */
    public static void deleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }

    /**
     * 集合 转 String
     * @param SceneList
     * @return
     * @throws IOException
     */
    public static String SceneList2String(List SceneList) throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(SceneList);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;
    }

    /**
     * String 转 集合
     * @param SceneListString
     * @return
     * @throws StreamCorruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static List String2SceneList(String SceneListString)
            throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List SceneList = (List) objectInputStream.readObject();
        objectInputStream.close();
        return SceneList;
    }

    /**
     * 判断是否有两个x 以此判断是否接收了多次返回的数据
     *
     * @param string
     * @return
     */
    public static boolean isHaveTwoX(String string) {
        int j = 0;
        for (int i = 0; i < string.length(); i++) {
            String valueOf = String.valueOf(string.charAt(i));
            if (valueOf.equals("x")) {
                j++;
            }
        }
        if (j > 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * CRC校验是否通过
     *
     * @param string
     *            格式：xl+(带小数点判断位)真实数据+;+校验位
     * @return
     */
    public static boolean isPassCRC(String string) {
        Log.e("123", "返回的数据===="+string);
        String[] split = string.split(";");
        // 获取校验位数据
        String verifyString = split[1].replaceAll("=", "");
        Log.e("123", "校验位数据=="+verifyString);
        // 获取真实数据
        String string2 = split[0];
        String weightString = string2.substring(2);
        Log.e("123", "称重数据=="+weightString);
        // 校验后的数据
        int x = 0;

        int[] weights = new int[weightString.length()];
        for (int i = 0; i < weightString.length(); i++) {

            // 先由字符串转换成char,再转换成String,然后Integer
            weights[i] = Integer
                    .parseInt(String.valueOf(weightString.charAt(i)));

        }
        for (int j = 0; j < weights.length; j++) {
            x = x ^ weights[j];
        }
        Log.e("123", "计算校验结果==" + x);

        // 比较校验数据
        int parseInt = Integer.parseInt(verifyString);
//		Toast.makeText(AppContext.getInstance(), "校验结果==" + (x == parseInt), 0).show();
        Log.e("123", "校验结果==" + (x == parseInt));
        if (x == parseInt) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取屏幕高，宽
     * @param context
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> getDefaultDisplay(Context context) {
        @SuppressWarnings("rawtypes")
        List list = new ArrayList();
        DisplayMetrics dm = new DisplayMetrics();
        // 取得窗口属性
        Activity activity = (Activity)context;
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        // 窗口的宽度
        int screenWidth = dm.widthPixels;

        // 窗口高度
        int screenHeight = dm.heightPixels;
        list.add(screenWidth);
        list.add(screenHeight);
        return list;
    }

    /**
     * 自定义吐司
     * @param context
     * @param content
     */
    public static void toastBottom(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        }else {
            toast.setText(content);
        }
        toast.show();
    }

    private static ProgressDialog progressDialog;

    /**
     * 请求开始动画
     * @param message
     * @param ct
     */
    public static void startProgressDialog(String message, Context ct) {
        try {
            if (progressDialog == null) {
                progressDialog = ProgressDialog.createDialog(ct);
                progressDialog.setMessage(message);
                progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode,
                                         KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            return true;
                        }
                        return false;
                    }
                });
            }
            if (!((Activity) ct).isFinishing())
                if (progressDialog.isShowing()) {
                    return;
                }
            progressDialog.show();
        } catch (Exception e) {

        }
    }

    /**
     * 请求结束动画
     */
    public static void stopProgressDialog() {
        try {
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭程序
     * @param cont
     */
    public static void Exit(final Context cont) {
        clearNotifi(cont);
        AppManager.getAppManager().AppExit(cont);
        System.exit(0);
    }
    private static void clearNotifi(Context context) {
        try {
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取设备id
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String deviceId = JPushInterface.getUdid(context);
        return deviceId;
    }

    /**
     * 防止短时间内重复点击多次
     */
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 800) {
            return true;
        }

        lastClickTime = time;
        return false;
    }

}
