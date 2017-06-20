package com.example.xl.foursling.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.example.xl.foursling.BuildConfig;
import com.example.xl.foursling.MainActivity;
import com.example.xl.foursling.Manifest;
import com.example.xl.foursling.MyApplication;
import com.example.xl.foursling.R;
import com.example.xl.foursling.activityes.SeachAddressActivity;
import com.example.xl.foursling.activityes.map.DriveRouteActivity;
import com.example.xl.foursling.activityes.map.GPSNaviActivity;
import com.example.xl.foursling.activityes.roder.SeachRoderActivity;
import com.example.xl.foursling.adapter.roder.RoderAdapter;
import com.example.xl.foursling.db.DbCtrl;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.service.CityService;
import com.example.xl.foursling.service.MdieService;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.MyThreadPoolManager;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.tools.VibratorUtil;
import com.example.xl.foursling.unity.RoderUnity;
import com.example.xl.foursling.view.xlist.XListView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.xl.foursling.R.id.btn_popup_error;

/**
 * Created by xl on 2016/12/15.
 */

public class RoderFragment extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener, View.OnClickListener {
    @Bind(R.id.fragment_roder_xlistview)
    XListView fragment_roder_xlistview;
    //loading加载
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    //无数据
    @Bind(R.id.not_data_linearlayout)
    LinearLayout not_data_linearlayout;
    //打开菜单
    @Bind(R.id.fragment_roder_btn_menu)
    Button fragment_roder_btn_menu;
    //搜索
    @Bind(R.id.fragment_roder_btn_search)
    Button fragment_roder_btn_search;
    //定位
    @Bind(R.id.fragment_roder_btn_location)
    Button fragment_roder_btn_location;
    //
    @Bind(R.id.fragment_roder_loading)
    ProgressBar fragment_roder_loading;
    private RoderAdapter roderAdapter;
    private RoderUnity roderUnity;
    private int pageindex,pagesize;
    private ArrayList<RoderUnity.MessageBean.RowsBean> array;
    private String cityCode;
    private PopupWindow popupWindow;
    private View view;
    private TextView popup_time_textview_content;
    private int state,number;
    private TimeCount timeCount;
    private AddressUpdateBroadcastReceiver addressUpdateBroadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roder, container, false);
        //注解绑定
        ButterKnife.bind(this,view);
        //注册局域广播
        initBroadcastReceiver();
        //初始化数据
        initData(view);
        //初始化控件
        initView(view);
        return view;
    }
    //广播注册方法
    private void initBroadcastReceiver() {
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        addressUpdateBroadcastReceiver = new AddressUpdateBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        intentFilter.addAction(CharConstants.BROADCAST_RECEIVER);
        localBroadcastManager.registerReceiver(addressUpdateBroadcastReceiver, intentFilter);
    }

    private void initView(View view) {
        fragment_roder_btn_menu.setOnClickListener(this);
        fragment_roder_btn_location.setOnClickListener(this);
        fragment_roder_btn_search.setOnClickListener(this);
        //xlixtview事件设置
        fragment_roder_xlistview.setPullRefreshEnable(true);
        //设置不显示上拉加载
        fragment_roder_xlistview.setPullLoadEnable(true);
        fragment_roder_xlistview.setAutoLoadEnable(true);
        fragment_roder_xlistview.setXListViewListener(this);
        fragment_roder_xlistview.setRefreshTime(getTime());
        fragment_roder_xlistview.setOnItemClickListener(this);
    }

    /**
     * 初始化数据
     * @param view
     */
    private void initData(View view) {
        state = Constants.ZERO;
        number = Constants.ZERO;
        array = new ArrayList<>();
        pageindex = Constants.ONE;
        pagesize = Constants.TEN;
        //查询cityCode
        MyThreadPoolManager.getInstance().execute(runnable);
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String code = null;
            code = DbCtrl.selectCityId(getActivity(),SharePreferencesUlits.getString(getActivity(),CharConstants.ADDRESS_DATEIL,""));
            Bundle bundle = new Bundle();
            Message message = new Message();
            bundle.putString("code",code);
            message.what = Constants.ONE;
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };
    Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (!TextUtils.isEmpty(msg.getData().getString("code"))){
                        cityCode = msg.getData().getString("code");
                        Log.d("city_code",cityCode+"");
                        //请求数据
                        loading_linearlayout.setVisibility(View.VISIBLE);
                        array.clear();
                        pageindex = Constants.ONE;
                        pagesize = Constants.TEN;
                        httpRoder();
                    }else {
                        Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:

                    break;
            }
        }
    };
    @Override
    public void onStart() {
        super.onStart();
        //初始化定位
        if (!TextUtils.isEmpty(SharePreferencesUlits.getString(getActivity(), CharConstants.LACTION,""))){
            fragment_roder_btn_location.setText(SharePreferencesUlits.getString(getActivity(),CharConstants.LACTION,""));
        }

    }
    private void httpRoder() {
        Log.d("city_code",cityCode+"");
        OkHttpUtils.post(Api.select_all_order)
                .tag(this)
                .params("cityCode",cityCode)
                .params("phone",SharePreferencesUlits.getString(getActivity(),CharConstants.PHONE,""))
                .params("page",String.valueOf(pageindex))
                .params("rows",String.valueOf(pagesize))
                .execute(new StringCallback(){
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        onLoderRefresh();
                        try {
                            Log.d("this_contnet",s);
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                not_data_linearlayout.setVisibility(View.GONE);
                                fragment_roder_xlistview.setVisibility(View.VISIBLE);
                                if (!TextUtils.isEmpty(new JSONObject(s).getString("message"))){
                                    jsonParse(s);
                                }
                            }else {
                                if (pageindex == Constants.ONE){
                                    not_data_linearlayout.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        if (StringUilts.isNetworkAvailable(getActivity()) != Constants.ZERO){
                            Toast.makeText(getActivity(),"服务异常",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getActivity(),"无网络",Toast.LENGTH_LONG).show();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
                        not_data_linearlayout.setVisibility(View.VISIBLE);
                        fragment_roder_xlistview.setVisibility(View.VISIBLE);
                        onLoder();
                    }
                });
    }

    /**
     * 数据解析并传入适配器
     * @param s
     */
    private void jsonParse(String s) {
        roderUnity = GsonUtil.processJson(s,RoderUnity.class);
        RoderAdapter.RoderOnClickLister roderOnClickLister = new RoderAdapter.RoderOnClickLister() {
            @Override
            protected void roderOnClickLister(Integer tag, View view) {
                loading_linearlayout.setVisibility(View.VISIBLE);
                state = Constants.TWO;
                number = tag;
                httpcheckCode();
            }
        };
        array.addAll(roderUnity.message.rows);
        roderAdapter = new RoderAdapter(getActivity(), array,roderOnClickLister);
        fragment_roder_xlistview.setAdapter(roderAdapter);
    }
    //请求审核状态
    private void httpcheckCode() {
        OkHttpUtils.post(Api.roder_state)
                .tag(getActivity())
                .params("userCode",SharePreferencesUlits.getString(getActivity(),CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            if(new JSONObject(s).getInt("code") == Constants.ZERO){
                                timeCount = new TimeCount(5000,1000);
                                timeCount.start();
                                backgroundAlpha(0.5f);
                                initpopup(true);
                            }else {
                                Toast.makeText(getActivity(),new JSONObject(s).getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        if (StringUilts.isNetworkAvailable(getActivity()) != Constants.ZERO){
                            Toast.makeText(getActivity(),"服务异常",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getActivity(),"无网络",Toast.LENGTH_LONG).show();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 接单
     * @param parcelNO
     * @param start_x
     * @param start_y
     * @param end_x
     * @param end_y
     * @param start_address
     * @param start_phone
     * @param end_address
     * @param end_phone
     * @param start
     * @param end
     * @param time
     * @param money
     */
    private void httpthisRoder(final String parcelNO, final String start_x,
                               final String start_y, final String end_x,
                               final String end_y, final String start_address,
                               final String start_phone, final String end_address,
                               final String end_phone, final String start, final String end,
                               final String time, final String money) {
        OkHttpUtils.post(Api.receiving_order)
                .tag(getActivity())
                .params("phone",SharePreferencesUlits.getString(getActivity(),CharConstants.PHONE,""))
                .params("parcelNO",parcelNO)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            Toast.makeText(getActivity(),new JSONObject(s).getString("message"),Toast.LENGTH_SHORT).show();
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                pageindex = Constants.ONE;
                                pagesize = Constants.TEN;
                                httpRoder();
                                Intent intent = new Intent();
                                intent.putExtra("start_x",start_y);
                                intent.putExtra("start_y",start_x);
                                intent.putExtra("end_x",end_y);
                                intent.putExtra("end_y",end_x);
                                intent.putExtra("start_phone",start_phone);
                                intent.putExtra("start_address",start_address);
                                intent.putExtra("end_phone",end_phone);
                                intent.putExtra("end_address",end_address);
                                intent.putExtra("start",start);
                                intent.putExtra("end",end);
                                intent.putExtra("end_time",time);
                                intent.putExtra("end_money",money);
                                intent.putExtra("parcelNO",parcelNO);
                                intent.setClass(getActivity(), DriveRouteActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        if (StringUilts.isNetworkAvailable(getActivity()) != Constants.ZERO){
                            Toast.makeText(getActivity(),"服务异常",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getActivity(),"无网络",Toast.LENGTH_LONG).show();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }
    //下拉刷新
    @Override
    public void onRefresh() {
        array.clear();
        loading_linearlayout.setVisibility(View.VISIBLE);
        pageindex = Constants.ONE;
        httpRoder();
    }
    //上拉加载
    @Override
    public void onLoadMore() {
        pageindex++;
        httpRoder();
    }
    //获取当前时间
    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    /**
     * item点击事件监听
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        loading_linearlayout.setVisibility(View.VISIBLE);
        state = Constants.TWO;
        number = (i-1);
        httpcheckCode();
    }

    /**
     * onclick事件响应
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            //打开菜单
            case R.id.fragment_roder_btn_menu:
                MainActivity.menu.showSecondaryMenu(true);
            break;
            //地址搜索界面
            case R.id.fragment_roder_btn_search:
//                intent.setClass(getActivity(), GPSNaviActivity.class);
//                intent.setClass(getActivity(), DriveRouteActivity.class);
                intent.setClass(getActivity(),SeachAddressActivity.class);
                startActivity(intent);
            break;
            case R.id.fragment_roder_btn_location:
                state = Constants.ONE;
                fragment_roder_btn_location.setVisibility(View.GONE);
                fragment_roder_loading.setVisibility(View.VISIBLE);
                timeCount = new TimeCount(2000,1000);
                MyApplication.mLocationClient.startLocation();
                timeCount.start();
                break;
        }
    }

    // 获得数据后一定要调用onLoad()方法，否则刷新会一直进行，根本停不下来
    public void onLoder(){
        fragment_roder_xlistview.setRefreshTime(StringUilts.getTime());
        fragment_roder_xlistview.stopLoadMore();
        fragment_roder_xlistview.stopRefresh();
        fragment_roder_xlistview.setPullLoadEnable(false);
    }
    public void onLoderRefresh(){
        fragment_roder_xlistview.setRefreshTime(StringUilts.getTime());
        fragment_roder_xlistview.stopRefresh();
        fragment_roder_xlistview.stopLoadMore();
        fragment_roder_xlistview.setPullLoadEnable(true);
    }
    //计时器
    public class TimeCount extends CountDownTimer{
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        //倒计时进行中
        @Override
        public void onTick(long l) {
            if (state == Constants.TWO){
                popup_time_textview_content.setText(l/1000+"秒后接单,若想取消订单请点击空白区域......");
            }
        }
        //倒计时结束
        @Override
        public void onFinish() {
            if (state == Constants.ONE){
                fragment_roder_loading.setVisibility(View.GONE);
                fragment_roder_btn_location.setVisibility(View.VISIBLE);
                //初始化定位
                if (!TextUtils.isEmpty(SharePreferencesUlits.getString(getActivity(), CharConstants.LACTION,""))){
                    fragment_roder_btn_location.setText(SharePreferencesUlits.getString(getActivity(),CharConstants.LACTION,""));
                    //查询cityCode
                    MyThreadPoolManager.getInstance().execute(runnable);
                }else {
                    Toast.makeText(getActivity(),"请打开定位权限",Toast.LENGTH_LONG).show();
                }
            }else if (state == Constants.TWO){
                popupWindow.dismiss();
                backgroundAlpha(1f);
                if (!TextUtils.isEmpty(roderUnity.message.rows.get(number).startCompanyLon)&&!TextUtils.isEmpty(roderUnity.message.rows.get(number).startCompanyLat)
                        &&!TextUtils.isEmpty(roderUnity.message.rows.get(number).company_companyLon)&&!TextUtils.isEmpty(roderUnity.message.rows.get(number).company_companyLat)) {
                    loading_linearlayout.setVisibility(View.VISIBLE);
                    httpthisRoder(roderUnity.message.rows.get(number).parcelNO,
                            roderUnity.message.rows.get(number).startCompanyLon,
                            roderUnity.message.rows.get(number).startCompanyLat,
                            roderUnity.message.rows.get(number).endCompanyLon,
                            roderUnity.message.rows.get(number).endCompanyLat,
                            roderUnity.message.rows.get(number).startAddress,
                            roderUnity.message.rows.get(number).startUserPhone,
                            roderUnity.message.rows.get(number).endAddress,
                            roderUnity.message.rows.get(number).endUserPhone,
                            roderUnity.message.rows.get(number).company_companyLon,
                            roderUnity.message.rows.get(number).company_companyLat,
                            StringUilts.transfromTime_ol(roderUnity.message.rows.get(number).moments),
                            String.valueOf((double)(roderUnity.message.rows.get(number).yunCost/100)));
                }else {
                    Toast.makeText(getActivity(),"当前数据有经纬度缺失",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    //自定义popup弹框
    private void initpopup(boolean s) {
        popupWindow = new PopupWindow();
        view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_time,null);
        popupWindow.setContentView(view);
        //定义popupwindows宽高
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setClippingEnabled(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popup_time_textview_content = (TextView) view.findViewById(R.id.popup_time_textview_content);
        //设置点击空白消失
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                state = Constants.ZERO;
                timeCount.onFinish();
                popupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        if (s){
            popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
        }
    }
    /**
     * popup弹框app背景变暗
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * @author 进行位置更新
     * @author 局域广播接收器
     * @author addressUpdateBroadcastReceiver
     * @param
     */
    public class AddressUpdateBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            state = Constants.ONE;
            timeCount = new TimeCount(2000,1000);
            fragment_roder_btn_location.setVisibility(View.GONE);
            fragment_roder_loading.setVisibility(View.VISIBLE);
            timeCount.start();
        }
    }
}
