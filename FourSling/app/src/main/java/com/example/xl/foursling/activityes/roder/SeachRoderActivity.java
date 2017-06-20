package com.example.xl.foursling.activityes.roder;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.activityes.map.DriveRouteActivity;
import com.example.xl.foursling.adapter.roder.RoderAdapter;
import com.example.xl.foursling.adapter.roder.SeachRoderAdapter;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.RoderUnity;
import com.example.xl.foursling.unity.SeachRoderUnity;
import com.example.xl.foursling.view.titlebar.TeaskBar;
import com.example.xl.foursling.view.xlist.XListView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2017/1/4.
 */
public class SeachRoderActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {
    //返回
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    //title
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    //data
    @Bind(R.id.activity_search_roder_xlist)
    XListView activity_search_roder_xlist;
    //loading
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    //无数据
    @Bind(R.id.not_data_linearlayout)
    LinearLayout not_data_linearlayout;
    private RoderAdapter roderAdapter;
    private SeachRoderAdapter seachRoderAdapter;
    private SeachRoderUnity roderUnity;
    private int pageIndex,pageSize;
    private TimeCount timeCount;
    private PopupWindow popupWindow_ol;
    private View view;
    private int state;
    private TextView popup_time_textview_content;
    private int number;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_roder);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //绑定注解框架
        ButterKnife.bind(this);
        //初始化数据
        initData();
        // 初始化控件
        initView();
    }

    private void initView() {
        layout_title_back.setOnClickListener(this);
        activity_search_roder_xlist.setRefreshTime(StringUilts.transfromTime(System.currentTimeMillis()));
        activity_search_roder_xlist.setPullLoadEnable(true);
        activity_search_roder_xlist.setPullRefreshEnable(true);
        activity_search_roder_xlist.setAutoLoadEnable(true);
        activity_search_roder_xlist.setXListViewListener(this);
        activity_search_roder_xlist.setOnItemClickListener(this);
    }
    private void initData() {
        state = Constants.ZERO;
        number = Constants.ZERO;
        pageIndex = Constants.ONE;
        pageSize = Constants.TEN;
        layout_title_textview.setText("搜索结果");
        not_data_linearlayout.setVisibility(View.VISIBLE);
        loading_linearlayout.setVisibility(View.VISIBLE);
        httpRoder();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_title_back:
                finish();
                break;
        }
    }
    //请求数据
    private void httpRoder() {
        OkHttpUtils.get(Api.select_order)
                .tag(this)
                .params("startCity",getIntent().getStringExtra("start_code"))
                .params("endCity",getIntent().getStringExtra("end_code"))
                .params("page",String.valueOf(pageIndex))
                .params("size",String.valueOf(pageSize))
                .execute(new StringCallback() {
                    /**
                     * 返回值处理
                     * @param isFromCache
                     * @param s
                     * @param request
                     * @param response
                     */
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            loading_linearlayout.setVisibility(View.GONE);
                            onLoderRefresh();
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                jsonPare(s);
                                not_data_linearlayout.setVisibility(View.GONE);
                            }else {
                                not_data_linearlayout.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    /**
                     * 报错处理
                     * @param isFromCache
                     * @param call
                     * @param response
                     * @param e
                     */
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        loading_linearlayout.setVisibility(View.GONE);
                        not_data_linearlayout.setVisibility(View.VISIBLE);
                        onLoder();
                    }
                });
    }

    /**
     * json 数据处理
     * @param s
     */
    private void jsonPare(String s) {
        Log.d("this_thisconntent",s);
        roderUnity = GsonUtil.processJson(s,SeachRoderUnity.class);
        SeachRoderAdapter.RoderOnClickLister roderOnClickLister = new SeachRoderAdapter.RoderOnClickLister() {
            @Override
            protected void roderOnClickLister(Integer tag, View view) {
                state = Constants.TWO;
                number = tag;
                httpcheckCode();
            }
        };
        seachRoderAdapter = new SeachRoderAdapter(getApplication(),roderUnity.message.get(Constants.ZERO).rows,roderOnClickLister);
        activity_search_roder_xlist.setAdapter(seachRoderAdapter);
    }
    //请求审核状态
    private void httpcheckCode() {
        OkHttpUtils.post(Api.roder_state)
                .tag(getApplication())
                .params("userCode",SharePreferencesUlits.getString(getApplication(),CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            if(new JSONObject(s).getInt("code") == Constants.ZERO){
                                timeCount = new TimeCount(5000,1000);
                                timeCount.start();
                                backgroundAlpha(0.5f);
                                initpopup(true);
                            }else {
                                Toast.makeText(getApplication(),new JSONObject(s).getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        if (StringUilts.isNetworkAvailable(getApplication()) != Constants.ZERO){
                            Toast.makeText(getApplication(),"服务异常",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplication(),"无网络",Toast.LENGTH_LONG).show();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }
    //下拉刷新
    @Override
    public void onRefresh() {
        loading_linearlayout.setVisibility(View.VISIBLE);
        pageIndex = Constants.ONE;
        httpRoder();
    }
    //上拉加载
    @Override
    public void onLoadMore() {
        pageIndex++;
        httpRoder();
    }
    // 获得数据后一定要调用onLoad()方法，否则刷新会一直进行，根本停不下来
    public void onLoder(){
        activity_search_roder_xlist.setRefreshTime(StringUilts.getTime());
        activity_search_roder_xlist.stopLoadMore();
        activity_search_roder_xlist.stopRefresh();
        activity_search_roder_xlist.setPullLoadEnable(false);
    }
    public void onLoderRefresh(){
        activity_search_roder_xlist.setRefreshTime(StringUilts.getTime());
        activity_search_roder_xlist.stopRefresh();
        activity_search_roder_xlist.stopLoadMore();
        activity_search_roder_xlist.setPullLoadEnable(true);
    }
    //自定义popup弹框
    private void initpopup(boolean s) {
        popupWindow_ol = new PopupWindow();
        view = LayoutInflater.from(this).inflate(R.layout.popup_time,null);
        popupWindow_ol.setContentView(view);
        //定义popupwindows宽高
        popupWindow_ol.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow_ol.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow_ol.setOutsideTouchable(true);
        popupWindow_ol.setFocusable(true);
        popupWindow_ol.setClippingEnabled(true);
        popupWindow_ol.setBackgroundDrawable(new BitmapDrawable());
        popup_time_textview_content = (TextView) view.findViewById(R.id.popup_time_textview_content);
        //设置点击空白消失
        popupWindow_ol.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                state = Constants.ZERO;
                timeCount.onFinish();
                popupWindow_ol.dismiss();
                backgroundAlpha(1f);
            }
        });
        if (s){
            popupWindow_ol.showAtLocation(view, Gravity.CENTER,0,0);
        }
    }

    /**
     * 接单接口
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
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .params("parcelNO",parcelNO)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            Toast.makeText(getApplication(),new JSONObject(s).getString("message"),Toast.LENGTH_SHORT).show();
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
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
                                intent.setClass(getApplication(), DriveRouteActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        if (StringUilts.isNetworkAvailable(getApplication()) != Constants.ZERO){
                            Toast.makeText(getApplication(),"服务异常",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplication(),"无网络",Toast.LENGTH_LONG).show();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        state = Constants.TWO;
        number = (i-1);
        httpcheckCode();
    }

    //计时器
    public class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        //倒计时进行中
        @Override
        public void onTick(long l) {
            popup_time_textview_content.setText(l/1000+"秒后接单,若想取消订单请点击空白区域......");
        }
        //倒计时结束
        @Override
        public void onFinish() {
            if (state == Constants.TWO){
                popupWindow_ol.dismiss();
                backgroundAlpha(1f);
                Log.d("this_content_00000002",state+"");
                loading_linearlayout.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(roderUnity.message.get(Constants.ZERO).rows.get(number).startCompanyLon)&&!TextUtils.isEmpty(roderUnity.message.get(Constants.ZERO).rows.get(number).startCompanyLat)
                        &&!TextUtils.isEmpty(roderUnity.message.get(Constants.ZERO).rows.get(number).company_companyLon)&&!TextUtils.isEmpty(roderUnity.message.get(Constants.ZERO).rows.get(number).company_companyLat)) {
                    httpthisRoder(roderUnity.message.get(Constants.ZERO).rows.get(number).parcelNO,
                            roderUnity.message.get(Constants.ZERO).rows.get(number).startCompanyLon,
                            roderUnity.message.get(Constants.ZERO).rows.get(number).startCompanyLat,
                            roderUnity.message.get(Constants.ZERO).rows.get(number).endCompanyLon,
                            roderUnity.message.get(Constants.ZERO).rows.get(number).endCompanyLat,
                            roderUnity.message.get(Constants.ZERO).rows.get(number).startAddress,
                            roderUnity.message.get(Constants.ZERO).rows.get(number).startUserPhone,
                            roderUnity.message.get(Constants.ZERO).rows.get(number).endAddress,
                            roderUnity.message.get(Constants.ZERO).rows.get(number).endUserPhone,
                            roderUnity.message.get(Constants.ZERO).rows.get(number).company_companyLon,
                            roderUnity.message.get(Constants.ZERO).rows.get(number).company_companyLat,
                            StringUilts.transfromTime_ol(roderUnity.message.get(Constants.ZERO).rows.get(number).moments),
                            String.valueOf((double)(roderUnity.message.get(Constants.ZERO).rows.get(number).yunCost/100)));
                }else {
                    Toast.makeText(getApplication(),"当前数据有经纬度缺失",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    /**
     * popup弹框app背景变暗
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
}
