package com.example.xl.foursling.tools;

import com.google.gson.Gson;

/**
 * Description：解析gson
 * Copyright  ：Copyright（c）2016
 * Author     ：feng
 * Date       ：2016/7/6 10:01
 */
public class GsonUtil {
    /**
     * 解析json数据   T:泛型
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T processJson(String json,Class<T> clazz){
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

}
