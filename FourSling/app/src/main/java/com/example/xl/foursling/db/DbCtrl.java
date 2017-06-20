package com.example.xl.foursling.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.xl.foursling.unity.ProvinceAndCity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/18.
 */

public class DbCtrl {
    /**
     * 查询city数据
     * @param context
     * @return
     */
    public static List<String> selectCity(Context context) {
        List<String> list = new ArrayList();
        String[] city_name = new String[]{};
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(DBManger.DB_PATH + "/" + DBManger.packagesname, null);
        cursor = sqLiteDatabase.rawQuery("select * from dataT",null);
        if (cursor.moveToFirst()){
            do {
                String city = null;
                city = cursor.getString(cursor.getColumnIndex("MergerName"));
                Log.i("ksldfj", city.length() + "-=-=-" + cursor.getString(cursor.getColumnIndex("MergerName")));
                list.add(city);
            }while (cursor.moveToNext());
            return list;
        }
        if (cursor != null){
            cursor.close();
        }
        return null;
    }
    /**
     * 查询cityid
     * @param context
     * @return
     */
    public static String selectCityId(Context context,String cityName) {
        int id = 0;
        Cursor cursor = null;
        try {
            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(DBManger.DB_PATH + "/" + DBManger.packagesname, null);
            cursor = sqLiteDatabase.rawQuery("select * from dataT where MergerName =?", new String[]{cityName});
            if (cursor.moveToFirst()){
                id = cursor.getInt(cursor.getColumnIndex("ID"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return String.valueOf(id);
    }
    /**
     * like查询
     * @param context
     * @return
     */
    public static ArrayList fuzzyselectCity(Context context,String cityName) {
        ArrayList<ProvinceAndCity> provinceAndCity = new ArrayList<>();
        int id = 0;
        Cursor cursor = null;
        try {

            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(DBManger.DB_PATH + "/" + DBManger.packagesname, null);
            cursor = sqLiteDatabase.rawQuery("select * from dataT where MergerName like ?", new String[]{"%"+cityName+"%"});
            if (cursor.moveToFirst()){
                do {
                    ProvinceAndCity province = new ProvinceAndCity();
                    Log.d("content_name",cursor.getString(cursor.getColumnIndex("MergerName")));
                    province.city = cursor.getString(cursor.getColumnIndex("MergerName"));
                    province.city_id = cursor.getString(cursor.getColumnIndex("ID"));
                    provinceAndCity.add(province);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return provinceAndCity;
    }
}
