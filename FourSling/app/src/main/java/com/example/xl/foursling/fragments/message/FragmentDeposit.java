package com.example.xl.foursling.fragments.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.xl.foursling.R;
import com.example.xl.foursling.activityes.mesages.PlanActivity;
import com.example.xl.foursling.adapter.message.DepositAdapter;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.DepositMessage;
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

public class FragmentDeposit extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener {
    @Bind(R.id.fragment_deposit_xlistview)
    XListView fragment_deposit_xlistview;
    //加载
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    @Bind(R.id.not_data_linearlayout)
    LinearLayout not_data_linearlayout;
    private View view;
    private DepositAdapter depositAdapter;
    private DepositMessage messages;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_deposit,container,false);
        //绑定注解
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
        //提现
        loading_linearlayout.setVisibility(View.VISIBLE);
        httpmessage();
    }

    private void httpmessage() {
        OkHttpUtils.post(Api.money_message)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getActivity(), CharConstants.PHONE,""))
                .params("messageType","2")
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        Log.d("this_content",s);
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            onLoaderRefresh();
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                not_data_linearlayout.setVisibility(View.GONE);
                                jsonPare(s);
                            }else {
                                not_data_linearlayout.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(),new JSONObject(s).getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        not_data_linearlayout.setVisibility(View.VISIBLE);
                        fragment_deposit_xlistview.setVisibility(View.GONE);
                        loading_linearlayout.setVisibility(View.GONE);
                        onLoader();
                    }
                });
    }

    private void jsonPare(String s) {
        messages = GsonUtil.processJson(s,DepositMessage.class);
        DepositAdapter.SelectOnclickLister selectOnclickLister = new DepositAdapter.SelectOnclickLister() {
            @Override
            protected void selectOnclickLister(Integer tag, View view) {
                Intent intent = new Intent();
                intent.putExtra("orderCode",messages.message.get(tag).tradingOrderNO);
                intent.setClass(getActivity(),PlanActivity.class);
                startActivity(intent);
            }
        };
        depositAdapter = new DepositAdapter(getActivity(),messages.message,selectOnclickLister);
        fragment_deposit_xlistview.setAdapter(depositAdapter);
    }

    private void initView() {
        fragment_deposit_xlistview.setRefreshTime(StringUilts.getTime());
        fragment_deposit_xlistview.setAutoLoadEnable(true);
        fragment_deposit_xlistview.setPullLoadEnable(true);
        fragment_deposit_xlistview.setPullRefreshEnable(true);
        fragment_deposit_xlistview.setXListViewListener(this);
        fragment_deposit_xlistview.setOnItemClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onRefresh() {
        loading_linearlayout.setVisibility(View.VISIBLE);
        httpmessage();
    }

    @Override
    public void onLoadMore() {
        onLoader();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
    // 获得数据后一定要调用onLoad()方法，否则刷新会一直进行，根本停不下来
    public void onLoader(){
        fragment_deposit_xlistview.stopLoadMore();//停止刷新
        fragment_deposit_xlistview.stopRefresh();//停止加载更多
        fragment_deposit_xlistview.setRefreshTime(StringUilts.getTime());//设置时间
        fragment_deposit_xlistview.setPullLoadEnable(false);
    }
    public void onLoaderRefresh(){
        fragment_deposit_xlistview.stopRefresh();
        fragment_deposit_xlistview.stopLoadMore();
        fragment_deposit_xlistview.setRefreshTime(StringUilts.getTime());
        fragment_deposit_xlistview.setPullLoadEnable(true);
    }
}
