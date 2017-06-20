package com.example.xl.foursling.activityes.money;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.adapter.money.DetailAdapter;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.DepositDetail;
import com.example.xl.foursling.unity.RechargeDetail;
import com.example.xl.foursling.view.titlebar.TeaskBar;
import com.example.xl.foursling.view.xlist.XListView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2016/12/23.
 */
public class DetailActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {
    //返回
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    //title内容
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    //明细数据
    @Bind(R.id.activity_detail_xlistview)
    XListView activity_detail_xlistview;
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    @Bind(R.id.not_data_linearlayout)
    LinearLayout not_data_linearlayout;
    private DetailAdapter detailAdapter;
    private String url;
    private String con;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        //绑定注解框架
        ButterKnife.bind(this);
        //初始化数据
        initData();
        //初始化控件
        initView();
    }

    private void initView() {
        layout_title_back.setOnClickListener(this);
        //取消上拉加载布局
        activity_detail_xlistview.setFooterDividersEnabled(false);
        activity_detail_xlistview.setAutoLoadEnable(true);
        activity_detail_xlistview.setPullLoadEnable(true);
        activity_detail_xlistview.setPullRefreshEnable(true);
        activity_detail_xlistview.setXListViewListener(this);
        activity_detail_xlistview.setRefreshTime(StringUilts.getTime());
        activity_detail_xlistview.setOnItemClickListener(this);
    }

    private void initData() {
        if (Integer.valueOf(getIntent().getStringExtra("detail_code")) == Constants.ONE){
            url = Api.recharge_detail;
            layout_title_textview.setText("充值明细");
        }else {
            url = Api.deposit_detail;
            layout_title_textview.setText("提现明细");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loading_linearlayout.setVisibility(View.VISIBLE);
        //请求数据
        httpData(url);
    }
    //数据请求
    private void httpData(String url) {
        OkHttpUtils.post(url)
                .tag(this)
                .params("userCode", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        Log.d("this______....",s+"-=-=-=");
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                not_data_linearlayout.setVisibility(View.GONE);
                                con = s;
                                jsonPress(s);
                                onLoadRefresh();
                            }else {
                                not_data_linearlayout.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        loading_linearlayout.setVisibility(View.GONE);
                        not_data_linearlayout.setVisibility(View.VISIBLE);
                        onLoad();
                    }
                });
    }
    //数据解析
    private void jsonPress(String content) {
        detailAdapter = new DetailAdapter(getApplication(),Integer.valueOf(getIntent().getStringExtra("detail_code")),content);
        activity_detail_xlistview.setAdapter(detailAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //返回
            case R.id.layout_title_back:
                finish();
                break;
        }
    }
    //上啦刷新
    @Override
    public void onRefresh() {
        httpData(url);
    }
    //下拉加载
    @Override
    public void onLoadMore() {
        httpData(url);
    }
    // 获得数据后一定要调用onLoad()方法，否则刷新会一直进行，根本停不下来
    private void onLoad() {
        activity_detail_xlistview.stopRefresh();//停止刷新
        activity_detail_xlistview.stopLoadMore();//停止加载更多
        activity_detail_xlistview.setRefreshTime(StringUilts.getTime());//设置时间
        activity_detail_xlistview.setPullLoadEnable(false);
    }

    private void onLoadRefresh() {
        activity_detail_xlistview.stopRefresh();//停止刷新
        activity_detail_xlistview.stopLoadMore();//停止加载更多
        activity_detail_xlistview.setRefreshTime(StringUilts.getTime());//设置时间
        activity_detail_xlistview.setPullLoadEnable(true);
    }

    /**
     * item点击事件
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.setClass(this,RechargeAndDepositActivity.class);
        if (Integer.valueOf(getIntent().getStringExtra("detail_code")) == Constants.ONE){
            RechargeDetail rechargeDetail = GsonUtil.processJson(con,RechargeDetail.class);
            intent.putExtra("recharge_code",String.valueOf(Constants.ONE));
            Log.d("lthis+++++",rechargeDetail.message.get(i-1).orderCode);
            intent.putExtra("tradingOrderNO",rechargeDetail.message.get(i-1).orderCode);
        }else {
            DepositDetail depositDetail = GsonUtil.processJson(con,DepositDetail.class);
            intent.putExtra("recharge_code",String.valueOf(Constants.TWO));
            intent.putExtra("tradingOrderNO",depositDetail.message.get(i-1).tradingOrderNO);
        }
        startActivity(intent);
    }
}
