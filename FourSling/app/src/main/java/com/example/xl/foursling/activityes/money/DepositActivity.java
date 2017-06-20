package com.example.xl.foursling.activityes.money;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.activityes.settting.SetPayPassActivity;
import com.example.xl.foursling.activityes.users.UserAptitudeActivity;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
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
 * Created by admin on 2016/12/22.
 */
public class DepositActivity extends BaseActivity implements View.OnClickListener, TextWatcher{

    //返回
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    //明细
    @Bind(R.id.layout_title_btn)
    Button layout_title_btn;
    //标题
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    //提现
    @Bind(R.id.activity_pay_btn_deposit)
    Button activity_pay_btn_deposit;
    //余额
    @Bind(R.id.activity_deposit_textview_money)
    TextView activity_deposit_textview_money;
    //输入
    @Bind(R.id.activity_deposit_ed_input)
    EditText activity_deposit_ed_input;
    //加载
    @Bind(R.id.loading_linearlayout)
     LinearLayout loading_linearlayout;
    //支付宝
    @Bind(R.id.activity_deposit_imageview_alipay)
    ImageView activity_deposit_imageview_alipay;
    @Bind(R.id.activity_deposit_alipay)
    RelativeLayout activity_deposit_alipay;
    @Bind(R.id.activity_deposit_text_alipay)
    TextView activity_deposit_text_alipay;
    //微信
    @Bind(R.id.activity_deposit_imageview_wx)
    ImageView activity_deposit_imageview_wx;
    @Bind(R.id.activity_deposit_text_wx)
    TextView activity_deposit_text_wx;
    @Bind(R.id.activity_deposit_wx)
    RelativeLayout activity_deposit_wx;
    int state;
    private String content;
    private int state_code;
    private PopupWindow popupWindow;
    private View view;
    private Button popup_deposit_btn_ok;
    private EditText popup_deposit_edit;
    private Button popup_deposit_cancel;
    private boolean flag = false;
    private TextView popup_deposit_text;
    private int stuts,state_user;
    private double sum;
    private DecimalFormat df;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
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
        layout_title_btn.setOnClickListener(this);
        activity_pay_btn_deposit.setOnClickListener(this);
        activity_deposit_ed_input.addTextChangedListener(this);
        activity_deposit_wx.setOnClickListener(this);
        activity_deposit_alipay.setOnClickListener(this);
    }

    private void initData() {
        df = new DecimalFormat("0.00");
        stuts = Constants.ZERO;
        state_user = Constants.ZERO;
        if (!TextUtils.isEmpty(SharePreferencesUlits.getString(getApplication(), CharConstants.WECHATNO,""))){
            activity_deposit_text_wx.setText(SharePreferencesUlits.getString(getApplication(), CharConstants.WECHATNO,""));
        }
        if (!TextUtils.isEmpty(SharePreferencesUlits.getString(getApplication(), CharConstants.ALIPAY_NO,""))){
            activity_deposit_text_alipay.setText(SharePreferencesUlits.getString(getApplication(), CharConstants.ALIPAY_NO,""));
        }
//        activity_deposit_imageview_wx.setSelected(true);
        activity_deposit_imageview_alipay.setSelected(true);
        state = Constants.ZERO;
        state_code = Constants.ONE;
        activity_pay_btn_deposit.setSelected(true);
        layout_title_textview.setText("提现");
        layout_title_btn.setText("明细");
        if (!TextUtils.isEmpty(SharePreferencesUlits.getString(getApplication(), CharConstants.MONEY,""))){
            content = "账户余额"+ SharePreferencesUlits.getString(getApplication(), CharConstants.MONEY,"")+"元";
        }else {
            content = "账户余额0.00元";
        }
        SpannableString style_OL = new SpannableString(content);
        style_OL.setSpan(new ForegroundColorSpan(Color.RED), 4, content.length()-1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色
        activity_deposit_textview_money.setText(style_OL);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            //返回
            case R.id.layout_title_back:
                finish();
                break;
            //明细
            case R.id.layout_title_btn:
                intent.setClass(this,DetailActivity.class);
                intent.putExtra("detail_code",String.valueOf(Constants.TWO));
                startActivity(intent);
                break;
            case R.id.activity_deposit_wx:
                state_code = Constants.TWO;
                activity_deposit_imageview_wx.setSelected(true);
                activity_deposit_imageview_alipay.setSelected(false);
                break;
            case R.id.activity_deposit_alipay:
                state_code = Constants.ONE;
                activity_deposit_imageview_wx.setSelected(false);
                activity_deposit_imageview_alipay.setSelected(true);
                break;
            //提现
            case R.id.activity_pay_btn_deposit:
                if (state == Constants.ONE){
                    if (Double.parseDouble(activity_deposit_ed_input.getText().toString())>0){
                        loading_linearlayout.setVisibility(View.VISIBLE);
                        httpcheckCode();
                    }else {
                        Toast.makeText(getApplication(),"提现金额必须大于0元",Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(getApplication(),"请输入提现金额",Toast.LENGTH_LONG).show();
                }
                break;
            //确定
            case R.id.popup_deposit_btn_ok:
                if (stuts == Constants.ONE){
                    popupWindow.dismiss();
                    backgroundAlpha(1f);
                   intent.setClass(this,SetPayPassActivity.class);
                   startActivity(intent);
                }else {
                    if (state_user == Constants.ZERO){
                        if (!TextUtils.isEmpty(popup_deposit_edit.getText().toString())){
                            if (popup_deposit_edit.getText().toString().length() == 6){
                                popupWindow.dismiss();
                                backgroundAlpha(1f);
                                loading_linearlayout.setVisibility(View.VISIBLE);
                                httpdeopsit();
                            }else {
                                Toast.makeText(getApplication(),"支付密码输入有误",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplication(),"请输入密码",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        popupWindow.dismiss();
                        backgroundAlpha(1f);
                        if (state_user == Constants.FOUR){
                            intent.putExtra("user_code","");
                        }else {
                            intent.putExtra("user_code",String.valueOf(state_user));
                        }
                        intent.setClass(this, UserAptitudeActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            //关闭弹框
            case R.id.popup_deposit_cancel:
                popupWindow.dismiss();
                backgroundAlpha(1f);
                break;
        }
    }
    //请求审核状态
    private void httpcheckCode() {
        OkHttpUtils.post(Api.select_user_audit)
                .tag(this)
                .params("phone",SharePreferencesUlits.getString(getApplication(),CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            switch (new JSONObject(s).getInt("code")){
                                case 0:
                                    loading_linearlayout.setVisibility(View.VISIBLE);
                                    httpalipay();
                                    break;
                                case 1:
                                    Toast.makeText(getApplication(),"您的资料正在审核中,暂时无法进行提现操作",Toast.LENGTH_LONG).show();
                                    break;
                                case 2:
                                    stuts = Constants.TWO;
                                    state_user = Constants.TWO;
                                    backgroundAlpha(0.5f);
                                    initpopup("个人资料审核失败,请前往编辑",true);
                                    break;
                                case 3:
                                    Toast.makeText(getApplication(),"您的账号已被封,请联系客服具体解决",Toast.LENGTH_LONG).show();
                                    break;
                                case 4:
                                    stuts = Constants.TWO;
                                    state_user = Constants.FOUR;
                                    backgroundAlpha(0.5f);
                                    initpopup("请完善个人资料",true);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        if (StringUilts.isNetworkAvailable(getApplication()) != Constants.ZERO){
                            Toast.makeText(getApplication(),"服务异常",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplication(),"无网络",Toast.LENGTH_LONG).show();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }
    //提现
    private void httpdeopsit() {
        OkHttpUtils.post(Api.deposit)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .params("amount",String.valueOf(StringUilts.doubleMoney_deposit(Double.parseDouble(activity_deposit_ed_input.getText().toString()))))
                .params("accountPassWord",popup_deposit_edit.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                if (Double.parseDouble(activity_deposit_ed_input.getText().toString())>100){
                                    sum = Double.parseDouble(SharePreferencesUlits.getString(getApplication(), CharConstants.MONEY,""))-Double.parseDouble(activity_deposit_ed_input.getText().toString())-(Double.parseDouble(activity_deposit_ed_input.getText().toString())/1000)*6;
                                }else {
                                    sum = Double.parseDouble(SharePreferencesUlits.getString(getApplication(), CharConstants.MONEY,""))-Double.parseDouble(activity_deposit_ed_input.getText().toString())-0.6;
                                }
                                SharePreferencesUlits.saveString(getApplication(), CharConstants.MONEY, String.valueOf(df.format(sum)));
                                String content_ol = "账户余额"+ df.format(sum) +"元";
                                SpannableString style_OL = new SpannableString(content_ol);
                                style_OL.setSpan(new ForegroundColorSpan(Color.RED), 4, content_ol.length()-1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色
                                activity_deposit_textview_money.setText(style_OL);
                                finish();
                            }
                            Toast.makeText(getApplication(),new JSONObject(s).getString("message"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        if (StringUilts.isNetworkAvailable(getApplication()) != Constants.ZERO){
                            Toast.makeText(getApplication(),"服务异常",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplication(),"无网络",Toast.LENGTH_LONG).show();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }

    //输入文字之前
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
    //文字输入时
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!TextUtils.isEmpty(activity_deposit_ed_input.getText().toString())){
            if (activity_deposit_ed_input.getText().toString().equals(".")){
                activity_deposit_ed_input.setText("0.");
                activity_deposit_ed_input.setSelection(activity_deposit_ed_input.getText().toString().length());
            }
            if (Double.parseDouble(SharePreferencesUlits.getString(getApplication(), CharConstants.MONEY,""))<Double.parseDouble(activity_deposit_ed_input.getText().toString())){
                activity_pay_btn_deposit.setSelected(true);
                state = Constants.ZERO;
                Toast.makeText(getApplication(),"账户余额不足",Toast.LENGTH_SHORT).show();
            }else {
                if (Double.parseDouble(activity_deposit_ed_input.getText().toString())>100){
                    if ((Double.parseDouble(activity_deposit_ed_input.getText().toString())+(Double.parseDouble(activity_deposit_ed_input.getText().toString())/1000)*6)>Double.parseDouble(SharePreferencesUlits.getString(getApplication(), CharConstants.MONEY,""))){
                        activity_pay_btn_deposit.setSelected(true);
                        state = Constants.ZERO;
                        Toast.makeText(getApplication(),"账户余额不足",Toast.LENGTH_SHORT).show();
                    }else {
                        activity_pay_btn_deposit.setSelected(false);
                        state = Constants.ONE;
                    }
                }else {
                    if ((Double.parseDouble(activity_deposit_ed_input.getText().toString())+0.6)>Double.parseDouble(SharePreferencesUlits.getString(getApplication(), CharConstants.MONEY,""))){
                        activity_pay_btn_deposit.setSelected(true);
                        state = Constants.ZERO;
                        Toast.makeText(getApplication(),"账户余额不足",Toast.LENGTH_SHORT).show();
                    }else {
                        activity_pay_btn_deposit.setSelected(false);
                        state = Constants.ONE;
                    }
                }
            }
        }else {
            activity_pay_btn_deposit.setSelected(true);
            state = Constants.ZERO;
        }
    }
    //文字输入后
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
    //自定义popup弹框
    private void initpopup(String s,boolean flag) {
        popupWindow = new PopupWindow();
        view = LayoutInflater.from(this).inflate(R.layout.popup_deposit,null);
        popupWindow.setContentView(view);
        //定义popupwindows宽高
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setClippingEnabled(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popup_deposit_btn_ok = (Button) view.findViewById(R.id.popup_deposit_btn_ok);
        popup_deposit_edit = (EditText) view.findViewById(R.id.popup_deposit_edit);
        popup_deposit_cancel = (Button) view.findViewById(R.id.popup_deposit_cancel);
        popup_deposit_text = (TextView)view.findViewById(R.id.popup_deposit_text);
        switch (stuts){
            case 0:
                popup_deposit_text.setText("输入支付密码");
                popup_deposit_edit.setVisibility(View.VISIBLE);
                stuts = Constants.ZERO;
                break;
            case 1:
                popup_deposit_text.setText(s);
                popup_deposit_edit.setVisibility(View.GONE);
                break;
            case 2:
                popup_deposit_text.setText(s);
                popup_deposit_edit.setVisibility(View.GONE);
                break;
        }
        popup_deposit_btn_ok.setOnClickListener(this);
        popup_deposit_cancel.setOnClickListener(this);
        //设置点击空白消失
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        if (flag){
            popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
        }
    }
    //查询支付密码是否设置
    private void httpalipay() {
        OkHttpUtils.post(Api.select_pay_pass)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(this, CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        Log.d("this_content>>>>>>",s);
                        try {
                            switch (new JSONObject(s).getInt("code")){
                                case 0:
                                    stuts = Constants.ZERO;
                                    initpopup("",true);
                                    backgroundAlpha(0.5f);
                                    break;
                                default:
                                    stuts = Constants.ONE;
                                    initpopup(new JSONObject(s).getString("message"),true);
                                    backgroundAlpha(0.5f);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } {

                        }

                    }
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        if (StringUilts.isNetworkAvailable(getApplication()) != Constants.ZERO){
                            Toast.makeText(getApplication(),"服务异常",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplication(),"无网络",Toast.LENGTH_LONG).show();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }
    /**
     * popup弹框app背景变暗
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }
}
