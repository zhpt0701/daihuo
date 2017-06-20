package com.example.xl.foursling.activityes.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.adapter.AllCaerBaseAdpater;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.http.MyImageLoader;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.AddCar;
import com.example.xl.foursling.view.titlebar.TeaskBar;
import com.example.xl.foursling.view.xlist.XListView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xl on 2016/12/16.
 */
public class UserCarActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {
    //返回
    @Bind(R.id.activity_user_car_btn_back)
    Button activity_user_car_btn_back;
    //车辆信息
    @Bind(R.id.activity_user_car_xlistview)
    XListView activity_user_car_xlistview;
    //加载
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    //添加车辆
    @Bind(R.id.activity_user_car_linearlayout_news)
    LinearLayout activity_user_car_linearlayout_news;
    //无数据
    @Bind(R.id.not_data_linearlayout)
    LinearLayout not_data_linearlayout;

    private AllCaerBaseAdpater allCaerBaseAdpater;
    private MyImageLoader myImageLoader;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_car);
        //初始化状态栏
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        //注解绑定
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //初始化数据
        initData();
        //初始化控件
        initView();
    }

    private void initView() {
        //onclick事件设置
        activity_user_car_btn_back.setOnClickListener(this);
        activity_user_car_linearlayout_news.setOnClickListener(this);
        //xlist事件设置
        activity_user_car_xlistview.setPullLoadEnable(true);
        activity_user_car_xlistview.setAutoLoadEnable(true);
        activity_user_car_xlistview.setPullRefreshEnable(true);
        activity_user_car_xlistview.setXListViewListener(this);
        activity_user_car_xlistview.setRefreshTime(StringUilts.getTime());
        activity_user_car_xlistview.setOnItemClickListener(this);
    }

    private void initData() {
        myImageLoader = new MyImageLoader(getApplication());
        activity_user_car_xlistview.setOnScrollListener(new PauseOnScrollListener(myImageLoader.imageLoader,true,false));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //查寻数据
        httpselectCar();
        loading_linearlayout.setVisibility(View.VISIBLE);
    }

    private void httpselectCar() {
        OkHttpUtils.post(Api.select_all_car)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            loading_linearlayout.setVisibility(View.GONE);
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                AddCar addCar = GsonUtil.processJson(s, AddCar.class);
                                if (!TextUtils.isEmpty(new JSONObject(s).getString("message"))){
                                    not_data_linearlayout.setVisibility(View.GONE);
                                    pressJson(addCar);
                                }else {
                                    not_data_linearlayout.setVisibility(View.VISIBLE);
                                }
                            }else {
                                not_data_linearlayout.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplication(),new JSONObject(s).getString("message"),Toast.LENGTH_SHORT).show();
                            }
                            onLoadRefresh();
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
                        not_data_linearlayout.setVisibility(View.VISIBLE);
                        loading_linearlayout.setVisibility(View.GONE);
                        onLoad();
                    }
                });
    }
    //数据解析
    private void pressJson(final AddCar addCar) {
        AllCaerBaseAdpater.MyOnClickLisenter myOnClickLisenter = new AllCaerBaseAdpater.MyOnClickLisenter() {
            @Override
            protected void myOnClickLisenter(Integer tag, View view) {
                loading_linearlayout.setVisibility(View.VISIBLE);
                httpCarCode(StringUilts.iso(addCar.message.get(tag).carCode));
            }
        };
        allCaerBaseAdpater = new AllCaerBaseAdpater(myOnClickLisenter,getApplication(),(ArrayList<AddCar.MessageBean>)addCar.message,myImageLoader.imageLoader,myImageLoader.options);
        activity_user_car_xlistview.setAdapter(allCaerBaseAdpater);
    }

    /**
     * 修改运输车辆
     * @param code
     */
    private void httpCarCode(final String code) {
        OkHttpUtils.post(Api.this_car)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .params("carCode",code)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                Toast.makeText(getApplication(),"设置成功",Toast.LENGTH_SHORT).show();
                                SharePreferencesUlits.getString(getApplication(),CharConstants.CAR_CODE,code);
                                httpselectCar();
                            }else {
                                Toast.makeText(getApplication(),"设置失败",Toast.LENGTH_SHORT).show();
                                loading_linearlayout.setVisibility(View.GONE);
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
    public void onClick(View view) {
          switch (view.getId()){
              //返回
              case R.id.activity_user_car_btn_back:
                  finish();
                  break;
              case R.id.activity_user_car_linearlayout_news:
                  Intent intent = new Intent();
                  intent.setClass(this,CarNewsActivity.class);
                  startActivity(intent);
                  break;
          }
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        loading_linearlayout.setVisibility(View.VISIBLE);
        httpselectCar();
    }
    //上拉加载
    @Override
    public void onLoadMore() {
        onLoad();
    }

    /**
     * listview点击事件
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    // 获得数据后一定要调用onLoad()方法，否则刷新会一直进行，根本停不下来
    private void onLoad() {
        activity_user_car_xlistview.stopRefresh();//停止刷新
        activity_user_car_xlistview.stopLoadMore();//停止加载更多
        activity_user_car_xlistview.setRefreshTime(StringUilts.getTime());//设置时间
        activity_user_car_xlistview.setPullLoadEnable(false);
    }

    private void onLoadRefresh() {
        activity_user_car_xlistview.stopRefresh();//停止刷新
        activity_user_car_xlistview.stopLoadMore();//停止加载更多
        activity_user_car_xlistview.setRefreshTime(StringUilts.getTime());//设置时间
        activity_user_car_xlistview.setPullLoadEnable(true);
    }
}
