package com.example.xl.foursling.activityes;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.AppManager;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.PhoneNumber;
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
 * Created by xl on 2016/12/10.
 */
public class LandingPassActivity extends BaseActivity implements View.OnClickListener {
    //手机号码
    @Bind(R.id.editText_lading_pass_phone)
    EditText editText_lading_pass_phone;
    @Bind(R.id.editText_ok_lading_pass_pass)
    EditText editText_ok_lading_pass_pass;
    @Bind(R.id.editText_lading_pass_auth_code)
    EditText editText_lading_pass_auth_code;
    @Bind(R.id.editText_lading_pass_pass)
    EditText editText_lading_pass_pass;
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    @Bind(R.id.btn_lading_pass)
    Button btn_lading_pass;
    @Bind(R.id.btn_lading_pass_auth_code)
    Button btn_lading_pass_auth_code;
    @Bind(R.id.activity_lading_pass)
    LinearLayout activity_lading_pass;
    @Bind(R.id.activity_lading_pay_pass)
    LinearLayout activity_lading_pay_pass;
    //支付密码
    @Bind(R.id.editText_lading_pay_pass)
    EditText editText_lading_pay_pass;
    @Bind(R.id.editText_ok_lading_pay_pass)
    EditText editText_ok_lading_pay_pass;
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    private TimeCount timeCount;
    //常量
    private int constants;
    private int codenumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lading_pass);
        //注解绑定
        ButterKnife.bind(this);
        //初始化数据
        initData();
        //初始化控件
        initView();
    }
    private void initView() {
        //初始化提交按钮监听
        btn_lading_pass.setOnClickListener(this);
        //初始化返回按钮监听
        layout_title_back.setOnClickListener(this);
        //初始化验证码按钮监听
        btn_lading_pass_auth_code.setOnClickListener(this);
    }

    private void initData() {
        if (Integer.valueOf(getIntent().getStringExtra("update_code")) == Constants.TWO){
            layout_title_textview.setText("修改支付密码");
            activity_lading_pay_pass.setVisibility(View.VISIBLE);
            activity_lading_pass.setVisibility(View.GONE);
        }else {
            layout_title_textview.setText("修改登录密码");
            activity_lading_pay_pass.setVisibility(View.GONE);
            activity_lading_pass.setVisibility(View.VISIBLE);
        }
        constants = Constants.ZERO;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //back返回
            case R.id.layout_title_back:
                finish();
                break;
            //验证码
            case R.id.btn_lading_pass_auth_code:
                if (!TextUtils.isEmpty(editText_lading_pass_phone.getText().toString())&&PhoneNumber.IsMoblieNo(editText_lading_pass_phone.getText().toString())){
                    if (Integer.valueOf(getIntent().getStringExtra("update_code")) == Constants.ONE){
                        constants = Constants.ONE;
                        httpNotPhoneNumber();
                        loading_linearlayout.setVisibility(View.VISIBLE);
                    }else {
                        if (editText_lading_pass_phone.getText().toString().equals(SharePreferencesUlits.getString(getApplication(),CharConstants.PHONE,""))){
                            constants = Constants.ONE;
                            httpNotPhoneNumber();
                            loading_linearlayout.setVisibility(View.VISIBLE);
                        }else {
                            Toast.makeText(getApplication(),"请输入当前注册手机号",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(getApplication(),"您输入的手机号格式不正确!",Toast.LENGTH_SHORT).show();
                }

                break;
            //提交
            case R.id.btn_lading_pass:
                if (!TextUtils.isEmpty(editText_lading_pass_phone.getText().toString())){
                    if (editText_lading_pass_phone.getText().toString().length()==11){
                        if (PhoneNumber.IsMoblieNo(editText_lading_pass_phone.getText().toString())){
                            if (!TextUtils.isEmpty(editText_lading_pass_auth_code.getText().toString())){
                                if (constants != Constants.ZERO){
                                    if (codenumber == Constants.ZERO){
                                        if (Integer.valueOf(getIntent().getStringExtra("update_code")) == Constants.TWO){
                                            if (editText_lading_pay_pass.getText().toString().length() == 6){
                                                if (editText_lading_pass_phone.getText().toString().equals(SharePreferencesUlits.getString(getApplication(),CharConstants.PHONE,""))){
                                                    if (editText_lading_pay_pass.getText().toString().equals(editText_ok_lading_pay_pass.getText().toString())){
                                                        httpLandingPass("accountPassWord",Api.phone_update_pay_pass,editText_ok_lading_pay_pass.getText().toString());
                                                        loading_linearlayout.setVisibility(View.VISIBLE);
                                                    } else {
                                                        Toast.makeText(getApplication(),"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                                                    }
                                                }else {
                                                    Toast.makeText(getApplication(),"请输入当前注册号码",Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(getApplication(),"请输入六位数字密码",Toast.LENGTH_SHORT).show();
                                            }
                                        }else if (Integer.valueOf(getIntent().getStringExtra("update_code")) == Constants.ONE){
                                            if (!TextUtils.isEmpty(editText_lading_pass_pass.getText().toString())&&editText_lading_pass_pass.getText().toString().length()>=6&&StringUilts.StringPass(editText_lading_pass_pass.getText().toString().trim())){
                                                if (editText_lading_pass_pass.getText().toString().equals(editText_ok_lading_pass_pass.getText().toString())){
                                                    httpLandingPass("newPassWord",Api.pass,editText_ok_lading_pass_pass.getText().toString());
                                                    loading_linearlayout.setVisibility(View.VISIBLE);
                                                }else {
                                                    Toast.makeText(getApplication(),"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(getApplication(),"请输入6-18位数字字母组合密码!",Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            if (editText_lading_pass_phone.getText().toString().equals(SharePreferencesUlits.getString(getApplication(),CharConstants.PHONE,""))){
                                                if (!TextUtils.isEmpty(editText_lading_pass_pass.getText().toString())&&editText_lading_pass_pass.getText().toString().length()>=6&&StringUilts.StringPass(editText_lading_pass_pass.getText().toString().trim())){
                                                    if (editText_lading_pass_pass.getText().toString().equals(editText_ok_lading_pass_pass.getText().toString())){
                                                        httpLandingPass("newPassWord",Api.pass,editText_ok_lading_pass_pass.getText().toString());
                                                        loading_linearlayout.setVisibility(View.VISIBLE);
                                                    }else {
                                                        Toast.makeText(getApplication(),"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                                                    }
                                                }else {
                                                    Toast.makeText(getApplication(),"请输入6-18位数字字母组合密码!",Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(getApplication(),"请输入当前注册号码",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }else {
                                        Toast.makeText(getApplication(),"验证有误!",Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    Toast.makeText(getApplication(),"请获取验证码!",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getApplication(),"请输入验证码!",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplication(),"您输入的手机号不存在",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplication(),"您输入的手机号码有误",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplication(),"请输入您的手机号",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //判断用户是否存在
    private void httpNotPhoneNumber() {
        OkHttpUtils.post(Api.select_user)
                .tag(getApplication())
                .params("userCode",editText_lading_pass_phone.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            if (new JSONObject(s).getInt("code") == 0){
                                timeCount = new TimeCount(60000,1000);
                                timeCount.start();
                                httpAuthCode();
                            }else {
                                loading_linearlayout.setVisibility(View.GONE);
                                Toast.makeText(getApplication(),"号码不存在!",Toast.LENGTH_SHORT).show();
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

    //修改密码
    private void httpLandingPass(String name,String url,String pass) {
        OkHttpUtils.post(url)
                .tag(getApplication())
                .params("phone", editText_lading_pass_phone.getText().toString())
                .params("smsCode",editText_lading_pass_auth_code.getText().toString())
                .params(name,pass)
                .execute(new StringCallback() {
                    /**
                     * 返回值
                     * @param isFromCache
                     * @param s
                     * @param request
                     * @param response
                     */
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            loading_linearlayout.setVisibility(View.GONE);
                            if (new JSONObject(s).getInt("code") == 0){
                                Toast.makeText(getApplication(),new JSONObject(s).getString("message"),Toast.LENGTH_SHORT).show();
                                SharePreferencesUlits.saveString(getApplication(), CharConstants.PASSWORD,editText_ok_lading_pass_pass.getText().toString());
                               if (Integer.valueOf(getIntent().getStringExtra("update_code")) == Constants.ONE){
                                   Intent intent = new Intent();
                                   intent.setClass(getApplication(),LandingActivity.class);
                                   startActivity(intent);
                                   finish();
                                   AppManager.getAppManager().finishAllActivity();
                               }else {
                                   finish();
                               }
                            }else {
                                Toast.makeText(getApplication(),new JSONObject(s).getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    /**
                     * 网络异常 627576
                     * @param isFromCache
                     * @param call
                     * @param response
                     * @param e
                     */
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
    private void httpAuthCode() {
        OkHttpUtils.post(Api.auth_code)
                .tag(getApplicationContext())
                .params("phone", editText_lading_pass_phone.getText().toString())
                .execute(new StringCallback() {
                    /**
                     * 返回值
                     * @param isFromCache
                     * @param s
                     * @param request
                     * @param response
                     */
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            loading_linearlayout.setVisibility(View.GONE);
                            JSONObject jsonObject = new JSONObject(s);
                            codenumber = jsonObject.getInt("code");
                            Toast.makeText(getApplication(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    /**
                     * 网络异常
                     * @param isFromCache
                     * @param call
                     * @param response
                     * @param e
                     */
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
     * 计时器用于验证码
     */
    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        //计时过程显示
        @Override
        public void onTick(long l) {
            btn_lading_pass_auth_code.setClickable(false);
            btn_lading_pass_auth_code.setText(l /1000+"秒");
        }

        //计时完毕操作
        @Override
        public void onFinish() {
            btn_lading_pass_auth_code.setText("重新验证");
            btn_lading_pass_auth_code.setClickable(true);
        }
    }
}
