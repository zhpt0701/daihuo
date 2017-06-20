package com.example.xl.foursling.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.amap.api.maps.model.LatLng;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.alipay.Base64;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/12/22.
 */

public class StringUilts {

    private static String str;
    private static DecimalFormat decimalFormat;

    //验证所有的身份证的合法性
    public static boolean isIdcard(String conent){
        IdcardValidator idcardValidator =  new IdcardValidator();
        return idcardValidator.isValidatedAllIdcard(conent);
    }
    //utf-8转iso-8859-1
    public static String iso(String content){
        try {
            str = new String(content.getBytes("utf-8"),"iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
    //获取当前时间
    public static String getTime() {
        return new SimpleDateFormat("MM:dd HH:mm", Locale.CANADA).format(new Date());
    }
    //时间转换
    public static String transfromTime(long time){
        Date d = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

    //时间转换
    public static String transfromTime_ol(long time){
        Log.d("this_time",time+"as");
        Date d = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH时mm分");
        return sdf.format(d);
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
    //密码正则判断
    public static boolean StringPass(String str){
        String pass = ".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]";
        if (!TextUtils.isEmpty(str)){
            return Pattern.compile(pass).matcher(str).matches();
        }
        return false;
    }

    /**
     * 车牌号正则判断
     * @param str
     * @return
     */
    public static boolean StringCarNumber(String str){
        String carRegular = "^[\\u4e00-\\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\\u4e00-\\u9fa5]$|^[a-zA-Z]{2}\\d{7}$";
        if (!TextUtils.isEmpty(str)){
            return Pattern.compile(carRegular).matcher(str).matches();
        }
        return false;
    }
    /**
     * double 精度
     * @param number
     * @return
     */
    public static String doubleMoney(double number){
        if (decimalFormat == null){
            decimalFormat = new DecimalFormat("0.00");
        }
        return String.valueOf(decimalFormat.format(number));
    }
    /**
     * double 精度
     * @param number
     * @return
     */
    public static int doubleMoney_deposit(double number){
        if (decimalFormat == null){
            decimalFormat = new DecimalFormat("0.00");
        }
        return Integer.parseInt(decimalFormat.format(number*100).substring(0,decimalFormat.format(number*100).indexOf(".")));
    }

    /**
     * 获取两点间距离
     *
     * @param start
     * @param end
     * @return
     */
    public static double calculateDistance(LatLng start, LatLng end) {
        double x1 = start.longitude;
        double y1 = start.latitude;
        double x2 = end.longitude;
        double y2 = end.latitude;
        return calculateDistances(x1, y1, x2, y2);
    }

    public static double calculateDistances(double x1, double y1, double x2, double y2) {
        final double NF_pi = 0.01745329251994329; // 弧度 PI/180
        x1 *= NF_pi;
        y1 *= NF_pi;
        x2 *= NF_pi;
        y2 *= NF_pi;
        double sinx1 = Math.sin(x1);
        double siny1 = Math.sin(y1);
        double cosx1 = Math.cos(x1);
        double cosy1 = Math.cos(y1);
        double sinx2 = Math.sin(x2);
        double siny2 = Math.sin(y2);
        double cosx2 = Math.cos(x2);
        double cosy2 = Math.cos(y2);
        double[] v1 = new double[3];
        v1[0] = cosy1 * cosx1 - cosy2 * cosx2;
        v1[1] = cosy1 * sinx1 - cosy2 * sinx2;
        v1[2] = siny1 - siny2;
        double dist = Math.sqrt(v1[0] * v1[0] + v1[1] * v1[1] + v1[2] * v1[2]);

        return (int) (Math.asin(dist / 2) * 12742001.5798544);
    }
}

