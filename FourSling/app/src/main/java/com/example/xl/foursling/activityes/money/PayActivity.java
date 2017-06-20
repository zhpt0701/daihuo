package com.example.xl.foursling.activityes.money;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.alipay.AliSinglePay;
import com.example.xl.foursling.view.titlebar.TeaskBar;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.xl.foursling.tools.alipay.AliSinglePay.CallBack;

/**
 * Created by admin on 2016/12/22.
 */
public class PayActivity extends BaseActivity implements View.OnClickListener, TextWatcher, RadioGroup.OnCheckedChangeListener {
    //加载
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    //title按钮
    @Bind(R.id.layout_title_btn)
    Button layout_title_btn;
    //返回
    @Bind(R.id.layout_title_back)
    Button layout_title_back;

    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    //选择
    @Bind(R.id.activity_pay_radioGroup)
    RadioGroup activity_pay_radioGroup;
    //输入框
    @Bind(R.id.activity_pay_ed_input)
    EditText activity_pay_ed_input;
    //提交
    @Bind(R.id.activity_pay_btn_submit)
    Button activity_pay_btn_submit;
    String orderInfo = null;
    int state;
    private int state_code;
    private DecimalFormat decimalFormat;
    /** 输入框小数的位数*/
    private static final int DECIMAL_DIGITS = 2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        //注解绑定
        ButterKnife.bind(this);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);

        initData();
        initView();

    }

    private void initView() {
        activity_pay_radioGroup.setOnCheckedChangeListener(this);
        layout_title_btn.setOnClickListener(this);
        layout_title_back.setOnClickListener(this);
        activity_pay_btn_submit.setOnClickListener(this);
    }

    private void initData() {
        decimalFormat = new DecimalFormat("0.00");
        state = Constants.ZERO;
//        state_code = Constants.TWO;
        state_code = Constants.ONE;
        layout_title_textview.setText("充值");
        layout_title_btn.setText("明细");
        activity_pay_btn_submit.setSelected(true);
        activity_pay_ed_input.addTextChangedListener(this);
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            //明细
            case R.id.layout_title_btn:
                intent.setClass(this,DetailActivity.class);
                intent.putExtra("detail_code",String.valueOf(Constants.ONE));
                startActivity(intent);
                break;
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.activity_pay_btn_submit:
                if (state == Constants.ONE){
                    if (Double.parseDouble(activity_pay_ed_input.getText().toString())>0){
                        if (state_code == Constants.ONE){
                            loading_linearlayout.setVisibility(View.VISIBLE);
                            //支付宝充值
                            httpRecharge(Api.alipay_code);
                        }else {
                        loading_linearlayout.setVisibility(View.VISIBLE);
//                            微信支付
                        httpRechargeWx(Api.Whcat_wx);
                        }
                    }else {
                        Toast.makeText(this,"输入金额不能为0元",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this,"请输入充值金额",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //微信订单号
    private void httpRechargeWx(String whcat_wx) {
        OkHttpUtils.post(whcat_wx)
                .tag(getApplication())
                .params("userCode", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .params("amount",String.valueOf((int)(Double.parseDouble(activity_pay_ed_input.getText().toString())*100)))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            Log.d("this_wx",s);
                            loading_linearlayout.setVisibility(View.GONE);
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                IWXAPI api = WXAPIFactory.createWXAPI(getApplication(), Constants.APP_ID);
                                api.registerApp(Constants.APP_ID);
                                PayReq rpdt = new PayReq();
                                rpdt.appId = Constants.APP_ID;// 微信开放平台审核通过的应用APPID
                                rpdt.partnerId = "1900000109";// 微信支付分配的商户号
                                rpdt.prepayId= "1101000000140415649af9fc314aa427";// 预支付订单号，app服务器调用“统一下单”接口获取
                                rpdt.packageValue = "Sign=WXPay";// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
                                rpdt.nonceStr= "CDATA[16644551130521405395521167957562";// 随机字符串，不长于32位，服务器小哥会给咱生成
                                rpdt.timeStamp= "1398746574";// 时间戳，app服务器小哥给出
                                rpdt.sign= "CDATA[F8BD75CD643482E6107995BA2CACFA49";// 签名，服务器小哥给出，他会根据：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3指导得到这个
                                api.sendReq(rpdt);
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

    //订单号
    private void httpRecharge(String url) {
        OkHttpUtils.post(url)
                .tag(getApplication())
                .params("userCode", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .params("amount",String.valueOf((int)(Double.parseDouble(activity_pay_ed_input.getText().toString())*100)))
                .params("payType", String.valueOf(state_code))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            loading_linearlayout.setVisibility(View.GONE);
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                orderInfo = new JSONObject(s).getString("message");
                                payment(orderInfo,activity_pay_ed_input.getText().toString());

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
    //第三方支付
    private void payment(final String orderInfo, final String money) {
        new AliSinglePay(PayActivity.this, new CallBack() {
            /**
             * 商品订单编号
             */
            @Override
            public String commodityOrderId() {
                return orderInfo;
            }
            /**
             * 商品名称
             *
             * @return
             */
            @Override
            public String commodityName() {
                return "i带货";
            }
            /**
             * 商品备注
             *
             * @return
             */
            @Override
            public String commodityRemarks() {
                return "i带货支付";
            }
            /**
             * 商品总价
             *
             * @return
             */
            @Override
            public Integer commodityNumber() {
                return 1;
            }
            /**
             * 商品单价
             *
             * @return
             */
            @Override
            public Double commodityUnitPrice() {
                return Double.parseDouble(money);
            }
            /**
             * 查询终端设备是否存在支付宝认证账户
             *
             * @return
             */
            @Override
            public void checkAliPayUser(boolean isExist) {

            }
            /**
             * 支付结果
             */
            @Override
            public void payResult(int resultCode, String resultMessage, String sdkVersion) {
                if (resultCode == 9000){
                   finish();
                    Toast.makeText(getApplication(),"充值成功",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplication(),"充值失败",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //文字变化前
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
    //文字变化时
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(!TextUtils.isEmpty(activity_pay_ed_input.getText().toString())){
            if (activity_pay_ed_input.getText().toString().equals(".")){
                activity_pay_ed_input.setText("0.");
                activity_pay_ed_input.setSelection(activity_pay_ed_input.getText().toString().length());
            }
            if (Double.parseDouble(activity_pay_ed_input.getText().toString())>10000000){
                Toast.makeText(getApplication(),"输入金额不能大于1千万",Toast.LENGTH_LONG).show();
                activity_pay_btn_submit.setSelected(true);
                state = Constants.ZERO;
            }else {
                activity_pay_btn_submit.setSelected(false);
                state = Constants.ONE;
            }
        }else {
            activity_pay_btn_submit.setSelected(true);
            state = Constants.ZERO;
        }
    }
    //文字变化后
    @Override
    public void afterTextChanged(Editable editable) {
        //小数点保留位数
        String temp = editable.toString();
        int posDot = temp.indexOf(".");
        if (posDot <= 0) return;
        if (temp.length() - posDot - 1 > 2)
        {
            editable.delete(posDot + 3, posDot + 4);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            //微信
            case R.id.activity_pay_radioButton_WX:
                state_code = Constants.TWO;
                break;
            //支付宝
            case R.id.activity_pay_radioButton_alipay:
                state_code = Constants.ONE;
                break;
        }
    }
}
