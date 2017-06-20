package com.example.xl.foursling.tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

/**
 * Created by admin on 2016/12/13.
 */

public class SharePreferencesUlits {
    /**使用SharedPreferences 来储存与读取数据**/
    private static SharedPreferences sp;
    /**程序中可以同时存在多个SharedPreferences数据， 根据SharedPreferences的名称就可以拿到对象**/
    public final static String SHARED_MAIN = "data";

    /**SharedPreferences中储存数据的路径**/
    public final static String DATA_URL = "/data/data/";
    public final static String SHARED_MAIN_XML = "data.xml";

    /**
     * 保存boolean信息
     * key : 保存信息key
     * value ： 保存信息的值
     */
    public static void saveBoolean(Context context, String key, boolean value){
        if (sp == null) {
            sp = context.getSharedPreferences(SHARED_MAIN, Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }
    /**
     * 获取boolean信息
     * key : 保存信息key
     * defValue ：缺省的值
     */
    public static boolean getBoolean(Context context,String key,boolean defValue){
        if (sp == null) {
            sp = context.getSharedPreferences(SHARED_MAIN, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }
    /**
     * 保存int信息
     * key : 保存信息key
     * value ： 保存信息的值
     */
    public static void saveInt(Context context,String key,int value){
        if (sp == null) {
            sp = context.getSharedPreferences(SHARED_MAIN, Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key,value).commit();
    }
    /** 获取int信息
    * key : 保存信息key
    * defValue ：缺省的值
    */
    public static int getinteger(Context context,String key,int defValue){
        if (sp == null) {
            sp = context.getSharedPreferences(SHARED_MAIN, Context.MODE_PRIVATE);
        }
        return sp.getInt(key, defValue);
    }
    /**
     * 保存long信息
     * key : 保存信息key
     * value ： 保存信息的值
     */
    public static void saveLong(Context context,String key,long value){
        if (sp == null) {
            sp = context.getSharedPreferences(SHARED_MAIN, Context.MODE_PRIVATE);
        }
        sp.edit().putLong(key,value).commit();
    }
    /** 获取long信息
     * key : 保存信息key
     * defValue ：缺省的值
     */
    public static long getLong(Context context,String key,long defValue){
        if (sp == null) {
            sp = context.getSharedPreferences(SHARED_MAIN, Context.MODE_PRIVATE);
        }
        return sp.getLong(key, defValue);
    }
    /**
     * 保存flot信息
     * key : 保存信息key
     * value ： 保存信息的值
     */
    public static void saveFloat(Context context,String key,float value){
        if (sp == null) {
            sp = context.getSharedPreferences(SHARED_MAIN, Context.MODE_PRIVATE);
        }
        sp.edit().putFloat(key,value).commit();
    }
    /** 获取float信息
     * key : 保存信息key
     * defValue ：缺省的值
     */
    public static float getFloat(Context context,String key,float defValue){
        if (sp == null) {
            sp = context.getSharedPreferences(SHARED_MAIN, Context.MODE_PRIVATE);
        }
        return sp.getFloat(key, defValue);
    }
    /**
     * 保存String信息
     * key : 保存信息key
     * value ： 保存信息的值
     */
    public static void saveString(Context context,String key,String value){
        if (sp == null) {
            sp = context.getSharedPreferences(SHARED_MAIN, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }
    /**
     * 获取String信息
     * key : 保存信息key
     * defValue ：缺省的值
     */
    public static String getString(Context context,String key,String defValue){
        if (sp == null) {
            sp = context.getSharedPreferences(SHARED_MAIN, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    /**
     * 清除sph中的某些数据
     * @param context
     * @param key
     */
    public static void removeDate(Context context,String key){
        if (sp == null){
            sp = context.getSharedPreferences(SHARED_MAIN, Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();
    }
    /**
     * 清除data数据
     * @param context
     */
    public static void dataclear(Context context){
        if (sp == null){
            sp = context.getSharedPreferences(SHARED_MAIN,Context.MODE_PRIVATE);
        }
        sp.edit().clear().commit();
    }
    /**
     * 删除data文件
     * @param context
     */
    public static void deleteFile(Context context){
        /** 删除SharedPreferences文件 **/
        File file = new File(DATA_URL + context.getPackageName().toString()
                + "/shared_prefs", SHARED_MAIN_XML);
        if (file.exists()) {
            file.delete();
        }
    }
}
