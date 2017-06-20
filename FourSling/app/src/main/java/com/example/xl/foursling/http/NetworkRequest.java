package com.example.xl.foursling.http;

import android.app.Application;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xl on 2016/12/10.
 */

public class NetworkRequest {
    /**
     * 登陆
     * @param application
     * @param s
     * @param s1
     * @return
     */
    public static String landing(Application application, String s, String s1) {

        return null;
    }

    /**
     * 注册/修改密码
     * @param application
     * @param s
     * @param s1
     * @param s2
     * @return
     */
    public static String httpRegister(Application application, String s, String s1, String s2) {
        final String[] content = {null};

        return content[0];
    }

}
