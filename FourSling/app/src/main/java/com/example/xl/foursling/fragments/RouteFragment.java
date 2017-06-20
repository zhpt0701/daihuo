package com.example.xl.foursling.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.xl.foursling.MainActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.activityes.map.DriveRouteActivity;
import com.example.xl.foursling.adapter.RoutAdapter;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.RouteContent;
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
 * Created by xl on 2016/12/15.
 */

public class RouteFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {
    //菜单
    @Bind(R.id.fragment_route_btn_menu)
    Button fragment_route_btn_menu;
    @Bind(R.id.fragment_route_radiobutton_going)
    RadioButton fragment_route_radiobutton_going;
    //切换
    @Bind(R.id.fragment_route_radiogroup)
    RadioGroup fragment_route_radiogroup;
    //数据
    @Bind(R.id.fragment_route_xlist)
    XListView fragment_route_xlist;
    //加载
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    @Bind(R.id.not_data_linearlayout)
    LinearLayout not_data_linearlayout;
    private int state_code,pageindex,pagesize;
    private RoutAdapter routAdapter;
    private ArrayList<RouteContent.MessageBean.RowsBean> arrayList;
    private RouteContent routeContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route,container,false);
        //绑定注解
        ButterKnife.bind(this,view);
        //初始化数据
        initData();
        //初始化控件
        initView();
        return view;
    }

    private void initView() {
        fragment_route_btn_menu.setOnClickListener(this);
        fragment_route_radiogroup.setOnCheckedChangeListener(this);
        fragment_route_xlist.setRefreshTime(StringUilts.getTime());
        fragment_route_xlist.setPullRefreshEnable(true);
        //隐藏上拉加载
        fragment_route_xlist.setPullLoadEnable(false);
        fragment_route_xlist.setAutoLoadEnable(true);
        fragment_route_xlist.setXListViewListener(this);
        fragment_route_xlist.setOnItemClickListener(this);
    }

    private void initData() {
        state_code = Constants.ONE;
        pageindex = Constants.ONE;
        pagesize = Constants.TEN;
        arrayList = new ArrayList<>();
        arrayList.clear();
        fragment_route_radiobutton_going.setChecked(true);
        //请求数据
        httpDate(Api.select_all_route);
    }
    @Override
    public void onStart() {
        super.onStart();

    }
    private void httpDate(String url) {
        OkHttpUtils.post(url)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getActivity(), CharConstants.PHONE,""))
                .params("rows",String.valueOf(pagesize))
                .params("page",String.valueOf(pageindex))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            onLoadRefresh();
                            loading_linearlayout.setVisibility(View.GONE);
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                not_data_linearlayout.setVisibility(View.GONE);
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
                        onLoad();
                        loading_linearlayout.setVisibility(View.GONE);
                        not_data_linearlayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    /**
     * 数据解析
     * @param s
     */
    private void jsonParse(String s) {
        routeContent = GsonUtil.processJson(s,RouteContent.class);
        RoutAdapter.RouteOnClickLister routeOnClickLister = new RoutAdapter.RouteOnClickLister() {
            @Override
            protected void routeOnClickLister(Integer tag, View view) {
                if (routeContent.message.rows.get(tag).status == Constants.ONE){
                    loading_linearlayout.setVisibility(View.VISIBLE);
                    Log.d("this_content",routeContent.message.rows.get(tag).parcelNO);
                    httpcancel(Api.cancel_order, routeContent.message.rows.get(tag).parcelNO);
                }else {
                    Toast.makeText(getActivity(),"快件已运输在途暂时无法取消",Toast.LENGTH_LONG).show();
                }
            }
        };
        arrayList.addAll(routeContent.message.rows);
        routAdapter = new RoutAdapter(getActivity(),state_code,arrayList,routeOnClickLister);
        fragment_route_xlist.setAdapter(routAdapter);

    }
    //取消订单
    private void httpcancel(String cancel_order,String code) {
        OkHttpUtils.post(cancel_order)
                .tag(this)
                .params("phone",SharePreferencesUlits.getString(getActivity(),CharConstants.PHONE,""))
                .params("parcelNo",code)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {

                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                pageindex = Constants.ONE;
                                arrayList.clear();
                                httpDate(Api.select_all_route);
                                Toast.makeText(getActivity(), new JSONObject(s).getString("message"),Toast.LENGTH_SHORT).show();
                            }else {
                                loading_linearlayout.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), new JSONObject(s).getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.fragment_route_radiobutton_going:
                pageindex = Constants.ONE;
                if (arrayList != null){
                    arrayList.clear();
                }
                state_code = Constants.ONE;
                //请求数据
                httpDate(Api.select_all_route);
                break;
            case R.id.fragment_route_radiobutton_fished:
                if (arrayList != null){
                    arrayList.clear();
                }
                pageindex = Constants.ONE;
                state_code = Constants.TWO;
                //请求数据
                httpDate(Api.select_all_over_route);
                break;
            case R.id.fragment_route_radiobutton_cancelled:
                state_code = Constants.THREE;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_route_btn_menu:
                MainActivity.menu.showSecondaryMenu(true);
                break;
        }
    }
    //下拉刷新
    @Override
    public void onRefresh() {
        pageindex = Constants.ONE;
        if (state_code == Constants.ONE){
            arrayList.clear();
            httpDate(Api.select_all_route);
        }else {
            arrayList.clear();
            httpDate(Api.select_all_over_route);
        }
    }
    //上拉加载
    @Override
    public void onLoadMore() {
        pageindex++;
        if (state_code == Constants.ONE){
            httpDate(Api.select_all_route);
        }else {
            httpDate(Api.select_all_over_route);
        }

    }
    // 获得数据后一定要调用onLoad()方法，否则刷新会一直进行，根本停不下来
    private void onLoad() {
        fragment_route_xlist.stopRefresh();//停止刷新
        fragment_route_xlist.stopLoadMore();//停止加载更多
        fragment_route_xlist.setRefreshTime(StringUilts.getTime());//设置时间
        fragment_route_xlist.setPullLoadEnable(false);
    }

    private void onLoadRefresh() {
        fragment_route_xlist.stopRefresh();//停止刷新
        fragment_route_xlist.stopLoadMore();//停止加载更多
        fragment_route_xlist.setRefreshTime(StringUilts.getTime());//设置时间
        fragment_route_xlist.setPullLoadEnable(true);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (state_code == Constants.ONE){
            Intent intent = new Intent();
            intent.putExtra("start_x",routeContent.message.rows.get(i-1).startCompanyLat);
            intent.putExtra("start_y",routeContent.message.rows.get(i-1).startCompanyLon);
            intent.putExtra("end_x",routeContent.message.rows.get(i-1).endCompanyLat);
            intent.putExtra("end_y",routeContent.message.rows.get(i-1).endCompanyLon);
            intent.putExtra("start_phone",routeContent.message.rows.get(i-1).startUserPhone);
            intent.putExtra("start_address",routeContent.message.rows.get(i-1).startAddress);
            intent.putExtra("end_phone",routeContent.message.rows.get(i-1).endUserPhone);
            intent.putExtra("end_address",routeContent.message.rows.get(i-1).endAddress);
            intent.putExtra("start",routeContent.message.rows.get(i-1).company_companyLon);
            intent.putExtra("end",routeContent.message.rows.get(i-1).company_companyLat);
            intent.putExtra("status",String.valueOf(routeContent.message.rows.get(i-1).status));
            intent.putExtra("end_time",StringUilts.transfromTime_ol(routeContent.message.rows.get(i-1).moments));
            intent.putExtra("end_money",String.valueOf((double)(routeContent.message.rows.get(i-1).yunCost/100)));
            intent.putExtra("parcelNO",routeContent.message.rows.get(i-1).parcelNO);
            intent.setClass(getActivity(), DriveRouteActivity.class);
            startActivity(intent);
        }else {

        }
    }
}
