package com.example.xl.foursling.activityes.money;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.adapter.money.IncomeAndExpendAdapter;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
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
public class IncomeAndExpendActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {
    //返回
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    //title内容
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    //明细数据
    @Bind(R.id.activity_income_expend_xlistview)
    XListView activity_income_expend_xlistview;
    //加载
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    @Bind(R.id.not_data_linearlayout)
    LinearLayout not_data_linearlayout;
    private IncomeAndExpendAdapter incomeAndExpendAdapter;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expend);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        //注解绑定
        ButterKnife.bind(this);
        //初始化数据
        initData();
        //初始化控件
        initView();
    }

    private void initView() {
        layout_title_back.setOnClickListener(this);
        activity_income_expend_xlistview.setPullLoadEnable(true);
        activity_income_expend_xlistview.setPullRefreshEnable(true);
        activity_income_expend_xlistview.setAutoLoadEnable(true);
        activity_income_expend_xlistview.setRefreshTime(StringUilts.getTime());
        activity_income_expend_xlistview.setXListViewListener(this);
    }
    private void initData() {
        if (Integer.valueOf(getIntent().getStringExtra("pay_code")) == Constants.ONE){
            url = Api.today_income_detail;
            layout_title_textview.setText("今日收入");
        }else {
            url = Api.today_deposit_detail;
            layout_title_textview.setText("今日支出");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loading_linearlayout.setVisibility(View.VISIBLE);
        //http网路请求
        httpIncomeandExpend(url);
    }
    private void httpIncomeandExpend(String url) {
        OkHttpUtils.post(url)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        Log.d("content_________",s);
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                not_data_linearlayout.setVisibility(View.GONE);
                                jsonParse(s);
                            }else {
                                not_data_linearlayout.setVisibility(View.VISIBLE);
                            }
                            onLoadRefresh();
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

    /**
     * json解析
     * @param s
     */
    private void jsonParse(String s) {
        incomeAndExpendAdapter = new IncomeAndExpendAdapter(getApplication(),Integer.valueOf(getIntent().getStringExtra("pay_code")),s);
        activity_income_expend_xlistview.setAdapter(incomeAndExpendAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_title_back:
                finish();
                break;
        }
    }
    //下拉刷新
    @Override
    public void onRefresh() {
        httpIncomeandExpend(url);
    }
    //上拉加载
    @Override
    public void onLoadMore() {
        onLoad();
    }
    // 获得数据后一定要调用onLoad()方法，否则刷新会一直进行，根本停不下来
    private void onLoad() {
        activity_income_expend_xlistview.stopRefresh();//停止刷新
        activity_income_expend_xlistview.stopLoadMore();//停止加载更多
        activity_income_expend_xlistview.setRefreshTime(StringUilts.getTime());//设置时间
        activity_income_expend_xlistview.setPullLoadEnable(false);
    }

    private void onLoadRefresh() {
        activity_income_expend_xlistview.stopRefresh();//停止刷新
        activity_income_expend_xlistview.stopLoadMore();//停止加载更多
        activity_income_expend_xlistview.setRefreshTime(StringUilts.getTime());//设置时间
        activity_income_expend_xlistview.setPullLoadEnable(true);
    }
}
