package com.example.xl.foursling;

import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.navi.AMapNavi;
import com.example.xl.foursling.tools.exception.AppException;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.exception.AppMailException;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cookie.store.PersistentCookieStore;
import com.lzy.okhttputils.model.HttpHeaders;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by xl on 2016/12/1.
 */

public class MyApplication extends Application{

    //声明AMapLocationClient类对象
    public static AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;
    public static OSS oss;
    private LocalBroadcastManager localBroadcastManager;
    @Override
    public void onCreate() {
        super.onCreate();
        //异常崩溃邮件发送
//        registerUncaughtExceptionHandler();
//        AppMailException.getInstance().init(getApplicationContext());
        /**
         * setApiKey是静态方法,内部引用了Context，建议放在Application中
         * 如果你在meta-data中配置了key，那么以meta-data中的为准，此行代码
         * 可以忽略，这个方法主要是为那些不想在xml里配置key的用户使用。
         * **/
//        AMapNavi.setApiKey(this, "9bd799407251aee85d4ef9f56ba7b265");
        //判断app运行在tv还是phone
//        UiModeManager uiModeManager = (UiModeManager)getSystemService(UI_MODE_SERVICE);
//        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION){
//            Log.d("this_TV_Content","Running on a TV");
//        }else {
//            Log.d("this_TV_Content","Running on a non-TV");
//        }
        //初始化阿里云
        initalipay();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();
//---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
//        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
//        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
//        params.put("commonParamsKey2", "这里支持中文参数");
        //-----------------------------------------------------------------------------------//
        OkHttpUtils.init(this);
        OkHttpUtils.getInstance()
                .debug("OkHttpUtils")
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                 //全局的写入超时时间
                //如果不想让框架管理cookie,以下不需要
//                .setCookieStore(new MemoryCookieStore())                           //cookie使用内存缓存（app退出后，cookie消失）
                .setCookieStore(new PersistentCookieStore())                       //cookie持久化存储，如果cookie不过期，则一直有效
                .addCommonHeaders(headers);
        //初始化定位
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        }
        //设置定位回调监听
        mLocationClient.setLocationListener(new LocationClickListner());

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);// 初始化 JPush
        //初始化定位
        initLoaction();
    }
    private void initLoaction() {
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        //获取一次定位结果：
//        mLocationOption.setOnceLocation(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(3000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(30000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //获取最近3s内精度最高的一次定位结果：(打开则定位只进行一次)
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    //定位回调监听
    public class LocationClickListner implements AMapLocationListener{
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                    aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    aMapLocation.getLatitude();//获取纬度
                    aMapLocation.getLongitude();//获取经度
                    aMapLocation.getAccuracy();//获取精度信息
                    aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    aMapLocation.getCountry();//国家信息
                    aMapLocation.getProvince();//省信息
                    aMapLocation.getCity();//城市信息
                    aMapLocation.getDistrict();//城区信息
                    aMapLocation.getStreet();//街道信息
                    aMapLocation.getStreetNum();//街道门牌号信息
                    aMapLocation.getCityCode();//城市编码
                    aMapLocation.getAdCode();//地区编码
                    Log.i("this______zhpt",aMapLocation.getAdCode()+"+++"+aMapLocation.getCityCode()+"___"+aMapLocation.getDistrict()+"-=-=-="+aMapLocation.getLatitude()+"+"+aMapLocation.getLongitude());
                    if (!TextUtils.isEmpty(SharePreferencesUlits.getString(getApplicationContext(),CharConstants.LACTION,""))){
                        if (!SharePreferencesUlits.getString(getApplicationContext(),CharConstants.LACTION,"").equals(aMapLocation.getDistrict())){
                            localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                            Intent intent = new Intent();
                            intent.setAction(CharConstants.BROADCAST_RECEIVER);
                            localBroadcastManager.sendBroadcast(intent);
                        }
                    }
                    SharePreferencesUlits.saveString(getApplicationContext(),CharConstants.LATITUDE,String.valueOf(aMapLocation.getLatitude()));
                    SharePreferencesUlits.saveString(getApplicationContext(),CharConstants.LONGITUDE,String.valueOf(aMapLocation.getLongitude()));
                    SharePreferencesUlits.saveString(getApplicationContext(), CharConstants.CITY_CODE,aMapLocation.getAdCode());
                    SharePreferencesUlits.saveString(getApplicationContext(), CharConstants.ADDRESS_DATEIL,"中国 "+aMapLocation.getProvince()+" "+aMapLocation.getCity());
//                    SharePreferencesUlits.saveString(getApplicationContext(), CharConstants.ADDRESS_DATEIL,"中国 "+"江西省"+" "+"吉安市"+" "+"吉水县");
                    SharePreferencesUlits.saveString(getApplicationContext(), CharConstants.LACTION,aMapLocation.getDistrict());
                    SharePreferencesUlits.saveString(getApplicationContext(),CharConstants.THIS_ADDRESS,aMapLocation.getCity()+aMapLocation.getDistrict());
                    //获取定位时间
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(aMapLocation.getTime());
                    df.format(date);
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error,ErrCode:"+aMapLocation.getErrorCode()+",errInfo:"+aMapLocation.getErrorInfo());
                }
            }
        }

    }
    // 注册App异常崩溃处理器
    private void registerUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }
    private void initalipay() {
        String endpoint = "http://"+CharConstants.COM_UP;
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的访问控制章节
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(CharConstants.ACCESSKEYID, CharConstants.ACCESSKEYSERCRET);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);
    }
    public static Context getContext(){
        return context;
    }
    /**
     * @return handler
     */
    public static Handler getHandler() {
        return handler;
    }

    /**
     * @return mainThreadId
     */
    public static int getMainThreadId() {
        return mainThreadId;
    }
}
