package com.example.xl.foursling.fragments.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.xl.foursling.R;
import com.example.xl.foursling.adapter.message.FreightAdapter;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.Freight;
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

public class FreightFragment extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener {
    @Bind(R.id.fragment_freight_xlistview)
    XListView fragment_freight_xlistview;
    //加载
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    @Bind(R.id.not_data_linearlayout)
    LinearLayout not_data_linearlayout;
    private View view;
    //适配器
    private FreightAdapter freightAdapter;
    private Freight freight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_freight,container,false);
        //注解绑定
        ButterKnife.bind(this,view);
        //初始化数据
        initData();
        //初始化控件
        initView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loading_linearlayout.setVisibility(View.VISIBLE);
        //消息
        httpmessage();
    }

    private void initView() {
        fragment_freight_xlistview.setRefreshTime(StringUilts.getTime());
        fragment_freight_xlistview.setAutoLoadEnable(true);
        fragment_freight_xlistview.setPullRefreshEnable(true);
        fragment_freight_xlistview.setPullLoadEnable(true);
        fragment_freight_xlistview.setXListViewListener(this);
        fragment_freight_xlistview.setOnItemClickListener(this);
    }

    private void initData() {

    }
    private void httpmessage() {
        OkHttpUtils.post(Api.four_sling_message)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getActivity(), CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        Log.d("this_content",s);
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                jsonPare(s);
                            }else {
                                not_data_linearlayout.setVisibility(View.VISIBLE);
                            }
                            onLoaderRefresh();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        loading_linearlayout.setVisibility(View.GONE);
                        not_data_linearlayout.setVisibility(View.VISIBLE);
                        onLoader();
                    }
                });
    }

    private void jsonPare(String s) {
        freight = GsonUtil.processJson(s,Freight.class);
        freightAdapter = new FreightAdapter(getActivity(), freight.message);
        fragment_freight_xlistview.setAdapter(freightAdapter);
    }
    //下拉刷新
    @Override
    public void onRefresh() {
        httpmessage();
    }
    //上拉加载
    @Override
    public void onLoadMore() {
        onLoader();
    }
    // 获得数据后一定要调用onLoad()方法，否则刷新会一直进行，根本停不下来
    public void onLoader(){
        fragment_freight_xlistview.stopLoadMore();//停止刷新
        fragment_freight_xlistview.stopRefresh();//停止加载更多
        fragment_freight_xlistview.setRefreshTime(StringUilts.getTime());//设置时间
        fragment_freight_xlistview.setPullLoadEnable(false);
    }
    public void onLoaderRefresh(){
        fragment_freight_xlistview.stopRefresh();
        fragment_freight_xlistview.stopLoadMore();
        fragment_freight_xlistview.setRefreshTime(StringUilts.getTime());
        fragment_freight_xlistview.setPullLoadEnable(true);
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

    }
}
