package com.example.xl.foursling.activityes.mesages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.adapter.message.RoderMessageAdapter;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.RoderMrssage;
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
 * Created by admin on 2016/12/24.
 */
public class OrderActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {
    //数据
    @Bind(R.id.activity_order_xlist)
    XListView activity_order_xlist;
    //返回
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    //title
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    @Bind(R.id.not_data_linearlayout)
    LinearLayout not_data_linearlayout;
    private RoderMessageAdapter roderMessageAdapter;
    private RoderMrssage roderMrssage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        //绑定注解
        ButterKnife.bind(this);
        //初始化数据
        initData();
        //初始化控件
        initView();
    }

    private void initData() {
        layout_title_textview.setText(R.string.activity_order_content);
        loading_linearlayout.setVisibility(View.VISIBLE);
        //数据请求
        httpdate();
    }
    private void httpdate() {
        OkHttpUtils.post(Api.oder_message)
                .tag(getApplication())
                .params("phone", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                jsonParse(s);
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
                    }
                });
    }

    private void jsonParse(String s) {
        roderMrssage = GsonUtil.processJson(s,RoderMrssage.class);
        if (roderMrssage.message.rows.size()>0){
            roderMessageAdapter = new RoderMessageAdapter(getApplication(),roderMrssage.message.rows);
            activity_order_xlist.setAdapter(roderMessageAdapter);
            not_data_linearlayout.setVisibility(View.GONE);
        }else {
            not_data_linearlayout.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        layout_title_back.setOnClickListener(this);
        activity_order_xlist.setRefreshTime(StringUilts.getTime());
        activity_order_xlist.setAutoLoadEnable(true);
        activity_order_xlist.setPullRefreshEnable(true);
        activity_order_xlist.setPullLoadEnable(true);
        activity_order_xlist.setXListViewListener(this);
        activity_order_xlist.setOnItemClickListener(this);
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
        onLoader();
    }
    //上拉加载
    @Override
    public void onLoadMore() {
        onLoader();
    }
    // 获得数据后一定要调用onLoad()方法，否则刷新会一直进行，根本停不下来
    public void onLoader(){
        activity_order_xlist.stopLoadMore();//停止刷新
        activity_order_xlist.stopRefresh();//停止加载更多
        activity_order_xlist.setRefreshTime(StringUilts.getTime());//设置时间
        activity_order_xlist.setPullLoadEnable(false);
    }
    public void onLoaderRefresh(){
        activity_order_xlist.stopRefresh();
        activity_order_xlist.stopLoadMore();
        activity_order_xlist.setRefreshTime(StringUilts.getTime());
        activity_order_xlist.setPullLoadEnable(true);
    }

    /**
     * item 点击事件
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
