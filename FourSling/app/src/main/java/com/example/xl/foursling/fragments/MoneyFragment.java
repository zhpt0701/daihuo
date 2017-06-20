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
import com.example.xl.foursling.activityes.money.DepositActivity;
import com.example.xl.foursling.activityes.money.IncomeAndExpendActivity;
import com.example.xl.foursling.activityes.money.PayActivity;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xl on 2016/12/15.
 */

public class MoneyFragment extends Fragment implements View.OnClickListener {
    //菜单
    @Bind(R.id.fragment_money_btn_back)
    Button fragment_money_btn_back;
    //金额
    @Bind(R.id.fragment_money_textview)
    TextView fragment_money_textview;
    //充值
    @Bind(R.id.fragment_money_btn_cz)
    Button fragment_money_btn_cz;
    //提现
    @Bind(R.id.fragment_money_btn_deposit)
    Button fragment_money_btn_deposit;
    //指出
    @Bind(R.id.fragment_money_btn_zc)
    Button fragment_money_btn_zc;
    //收入
    @Bind(R.id.fragment_money_btn_zs)
    Button fragment_money_btn_zs;
    @Bind(R.id.fragment_money_text_sr)
    TextView fragment_money_text_sr;
    @Bind(R.id.fragment_money_text_zc)
    TextView fragment_money_text_zc;
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    private DecimalFormat decimalFormat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_money,container,false);
        //绑定注解
        ButterKnife.bind(this,view);

        initData();
        initView();
        return view;
    }

    private void initView() {
        fragment_money_btn_back.setOnClickListener(this);
        fragment_money_btn_cz.setOnClickListener(this);
        fragment_money_btn_deposit.setOnClickListener(this);
        fragment_money_btn_zc.setOnClickListener(this);
        fragment_money_btn_zs.setOnClickListener(this);
    }

    private void initData() {
        decimalFormat = new DecimalFormat("0.00");
    }

    @Override
    public void onStart() {
        super.onStart();
        loading_linearlayout.setVisibility(View.VISIBLE);
        //收入
        httpincome();
        //支出
        httpdeposit();
        //余额
        httpBalance();
    }

    private void httpBalance() {
        OkHttpUtils.post(Api.select_balance)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getActivity(), CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            loading_linearlayout.setVisibility(View.GONE);
                            Log.d("this______....",s+"-=-=-=");
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                fragment_money_textview.setText(String.valueOf(decimalFormat.format(new JSONObject(s).getDouble("message")/100)));
                                SharePreferencesUlits.saveString(getActivity(),CharConstants.MONEY,String.valueOf(decimalFormat.format(new JSONObject(s).getDouble("message")/100)));
                            }else {
                                fragment_money_textview.setText("0.00");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        Toast.makeText(getActivity(),"服务异常",Toast.LENGTH_SHORT).show();
                        fragment_money_textview.setText("0.0");
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }
    //收入
    private void httpdeposit() {
        OkHttpUtils.post(Api.today_income)
                .tag(this)
                .params("userCode", SharePreferencesUlits.getString(getActivity(), CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            loading_linearlayout.setVisibility(View.GONE);
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                fragment_money_text_sr.setText("+"+String.valueOf(decimalFormat.format(new JSONObject(s).getDouble("message")/100)));
                            }else {
                                fragment_money_text_sr.setText("+0.00");
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
                        fragment_money_text_sr.setText("+0.00");
                    }
                });
    }
    //支出
    private void httpincome() {
        OkHttpUtils.post(Api.today_deposit)
                .tag(this)
                .params("userCode", SharePreferencesUlits.getString(getActivity(), CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            loading_linearlayout.setVisibility(View.GONE);
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                fragment_money_text_zc.setText("-"+String.valueOf(decimalFormat.format(new JSONObject(s).getDouble("message")/100)));
                            }else {
                                fragment_money_text_zc.setText("-0.00");
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
                        fragment_money_text_zc.setText("-0.00");
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            //菜单
            case R.id.fragment_money_btn_back:
                MainActivity.menu.showSecondaryMenu(true);
                break;
            //充值
            case R.id.fragment_money_btn_cz:
                intent.setClass(getActivity(),PayActivity.class);
                startActivity(intent);
                break;
            //提现
            case R.id.fragment_money_btn_deposit:
                intent.setClass(getActivity(),DepositActivity.class);
                startActivity(intent);
                break;
            //支出
            case R.id.fragment_money_btn_zc:
                intent.setClass(getActivity(),IncomeAndExpendActivity.class);
                intent.putExtra("pay_code",String.valueOf(Constants.TWO));
                startActivity(intent);
                break;
            //收入
            case R.id.fragment_money_btn_zs:
                intent.setClass(getActivity(),IncomeAndExpendActivity.class);
                intent.putExtra("pay_code",String.valueOf(Constants.ONE));
                startActivity(intent);
                break;
        }
    }
}
