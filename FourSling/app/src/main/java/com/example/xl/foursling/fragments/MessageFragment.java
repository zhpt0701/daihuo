package com.example.xl.foursling.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.MainActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.activityes.mesages.MessageActivity;
import com.example.xl.foursling.activityes.mesages.MoneyMessageActivity;
import com.example.xl.foursling.activityes.mesages.OrderActivity;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
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
 * Created by xl on 2016/12/15.
 */

public class MessageFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.fragment_message_btn_menu)
    Button fragment_message_btn_menu;
    //接单
    @Bind(R.id.fragment_message_btn_jd)
    Button fragment_message_btn_jd;
    @Bind(R.id.fragment_message_textview_jd)
    TextView fragment_message_textview_jd;
    // 钱包
    @Bind(R.id.fragment_message_btn_money)
    Button fragment_message_btn_money;
    @Bind(R.id.fragment_message_textview_money)
    TextView fragment_message_textview_money;
    // 系统
    @Bind(R.id.fragment_message_btn_xt)
    Button fragment_message_btn_xt;
    @Bind(R.id.fragment_message_textview_xt)
    TextView fragment_message_textview_xt;
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,container,false);
        //绑定注解
        ButterKnife.bind(this,view);
        //初始化数据
        initData(view);
        // 初始化控件
        initView(view);
        return view;
    }

    private void initView(View view) {
        fragment_message_btn_menu.setOnClickListener(this);
        fragment_message_btn_jd.setOnClickListener(this);
        fragment_message_btn_money.setOnClickListener(this);
        fragment_message_btn_xt.setOnClickListener(this);
    }

    private void initData(View view) {

    }

    @Override
    public void onStart() {
        super.onStart();
        loading_linearlayout.setVisibility(View.VISIBLE);
        httpmessage();
    }
    private void httpmessage() {
        OkHttpUtils.post(Api.select_message)
                .tag(getActivity())
                .params("phone", SharePreferencesUlits.getString(getActivity(), CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        Log.d("this_messsage",s);
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                if (new JSONObject(s).getString("message").equals("111")){
                                    fragment_message_textview_jd.setVisibility(View.VISIBLE);
                                    fragment_message_textview_money.setVisibility(View.VISIBLE);
                                    fragment_message_textview_xt.setVisibility(View.VISIBLE);
                                }else if (new JSONObject(s).getString("message").equals("100")){
                                    fragment_message_textview_jd.setVisibility(View.VISIBLE);
                                    fragment_message_textview_money.setVisibility(View.GONE);
                                    fragment_message_textview_xt.setVisibility(View.GONE);
                                }else if (new JSONObject(s).getString("message").equals("010")){
                                    fragment_message_textview_jd.setVisibility(View.GONE);
                                    fragment_message_textview_money.setVisibility(View.VISIBLE);
                                    fragment_message_textview_xt.setVisibility(View.GONE);
                                }else if (new JSONObject(s).getString("message").equals("001")){
                                    fragment_message_textview_jd.setVisibility(View.GONE);
                                    fragment_message_textview_money.setVisibility(View.GONE);
                                    fragment_message_textview_xt.setVisibility(View.VISIBLE);
                                }else if (new JSONObject(s).getString("message").equals("110")){
                                    fragment_message_textview_jd.setVisibility(View.VISIBLE);
                                    fragment_message_textview_money.setVisibility(View.VISIBLE);
                                    fragment_message_textview_xt.setVisibility(View.GONE);
                                }else if (new JSONObject(s).getString("message").equals("101")){
                                    fragment_message_textview_jd.setVisibility(View.VISIBLE);
                                    fragment_message_textview_money.setVisibility(View.GONE);
                                    fragment_message_textview_xt.setVisibility(View.VISIBLE);
                                }else if (new JSONObject(s).getString("message").equals("011")){
                                    fragment_message_textview_jd.setVisibility(View.GONE);
                                    fragment_message_textview_money.setVisibility(View.VISIBLE);
                                    fragment_message_textview_xt.setVisibility(View.VISIBLE);
                                }else if (new JSONObject(s).getString("message").equals("000")){
                                    fragment_message_textview_jd.setVisibility(View.GONE);
                                    fragment_message_textview_money.setVisibility(View.GONE);
                                    fragment_message_textview_xt.setVisibility(View.GONE);
                                }else {

                                }
                            }else {

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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.fragment_message_btn_menu:
                MainActivity.menu.showSecondaryMenu(true);
                break;
            case R.id.fragment_message_btn_jd:
                intent.setClass(getActivity(),OrderActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_message_btn_money:
                intent.setClass(getActivity(), MoneyMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_message_btn_xt:
                intent.setClass(getActivity(), MessageActivity.class);
                startActivity(intent);
                break;
        }
    }
}
