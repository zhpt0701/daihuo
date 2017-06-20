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
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.PrepaidDetails;
import com.example.xl.foursling.unity.ShowDetail;
import com.example.xl.foursling.view.titlebar.TeaskBar;
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
 * Created by admin on 2016/12/23.
 */
public class RechargeAndDepositActivity extends BaseActivity implements View.OnClickListener {
    //标题
    @Bind(R.id.activity_register_deposit_item_title)
    TextView activity_register_deposit_item_title;
    //金额
    @Bind(R.id.activity_register_deposit_item_money)
    TextView activity_register_deposit_item_money;
    //手续
    @Bind(R.id.activity_register_deposit_item_dh_and_sxf)
    TextView activity_register_deposit_item_dh_and_sxf;
    //手续费
    @Bind(R.id.activity_register_deposit_item_odd_number)
    TextView activity_register_deposit_item_odd_number;
    //时间
    @Bind(R.id.activity_register_deposit_item_time_nu)
    TextView activity_register_deposit_item_time_nu;
    //方式
    @Bind(R.id.activity_register_deposit_item_wx_and_alipay)
    TextView activity_register_deposit_item_wx_and_alipay;
    //账户
    @Bind(R.id.activity_register_deposit_item_fs_and_zh)
    TextView activity_register_deposit_item_fs_and_zh;
    //余额
    @Bind(R.id.activity_register_deposit_item_yve_money)
    TextView activity_register_deposit_item_yve_money;

    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    //返回
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    //加载
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    private String url;
    private DecimalFormat decimalFormat;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_deposit);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        //绑定注解
        ButterKnife.bind(this);
        //初始化数据
        initData();
        //初始化控件
        initView();
    }

    private void initView() {
        layout_title_back.setOnClickListener(this);
    }

    private void initData() {
        decimalFormat = new DecimalFormat("0.00");
        if (Integer.parseInt(getIntent().getStringExtra("recharge_code")) == Constants.ONE){
            url = Api.recharge_select;
            layout_title_textview.setText("充值详情");
            activity_register_deposit_item_title.setText("充值");
            activity_register_deposit_item_fs_and_zh.setText("充值方式");
            activity_register_deposit_item_odd_number.setVisibility(View.GONE);
            activity_register_deposit_item_dh_and_sxf.setVisibility(View.GONE);
        }else {
            url = Api.deposit_select;
            layout_title_textview.setText("提现详情");
            activity_register_deposit_item_title.setText("提现");
            activity_register_deposit_item_fs_and_zh.setText("提现账户");
            activity_register_deposit_item_dh_and_sxf.setText("手续费");
            activity_register_deposit_item_odd_number.setVisibility(View.VISIBLE);
            activity_register_deposit_item_dh_and_sxf.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loading_linearlayout.setVisibility(View.VISIBLE);
        httprechargeAnddeposit(url);
    }

    private void httprechargeAnddeposit(String url) {
        OkHttpUtils.post(url)
                .tag(this)
                .params("userCode", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .params("orderCode",getIntent().getStringExtra("tradingOrderNO"))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        Log.d("this_________....",s);
                        try {
                            loading_linearlayout.setVisibility(View.GONE);
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                if (Integer.parseInt(getIntent().getStringExtra("recharge_code")) == Constants.ONE){
                                    PrepaidDetails prepaidDetails = GsonUtil.processJson(s,PrepaidDetails.class);
                                    //提现金额
                                    activity_register_deposit_item_money.setText(String.valueOf(decimalFormat.format((double)prepaidDetails.message.get(0).payAmount/100)));
                                    //账号
                                    if (prepaidDetails.message.get(0).payType == Constants.ONE){
                                        activity_register_deposit_item_wx_and_alipay.setText("支付宝");
                                    }else if (prepaidDetails.message.get(0).payType == Constants.TWO){
                                        activity_register_deposit_item_wx_and_alipay.setText("微信");
                                    }
                                    //时间
                                    activity_register_deposit_item_time_nu.setText(StringUilts.transfromTime(prepaidDetails.message.get(0).createDate));
                                    //余额
                                    activity_register_deposit_item_yve_money.setText(String.valueOf(decimalFormat.format((double) prepaidDetails.message.get(0).balance/100)));
                                }else {
                                    ShowDetail showDetail = GsonUtil.processJson(s,ShowDetail.class);
                                    //提现金额
                                    activity_register_deposit_item_money.setText(String.valueOf(decimalFormat.format((double) showDetail.message.get(0).payAmount/100)));
                                    //手续费
                                    activity_register_deposit_item_odd_number.setText(String.valueOf(decimalFormat.format((double) showDetail.message.get(0).amount/100)));
                                    //账号
                                    activity_register_deposit_item_wx_and_alipay.setText(showDetail.message.get(0).userPayNO);
                                    //时间
                                    activity_register_deposit_item_time_nu.setText(StringUilts.transfromTime(showDetail.message.get(0).createDate));
                                    //余额
                                    activity_register_deposit_item_yve_money.setText(String.valueOf(decimalFormat.format((double) showDetail.message.get(0).balance/100)));
                                }
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_title_back:
                finish();
                break;
        }
    }
}
