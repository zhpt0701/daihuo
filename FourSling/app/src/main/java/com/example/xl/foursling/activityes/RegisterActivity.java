package com.example.xl.foursling.activityes;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.PhoneNumber;
import com.example.xl.foursling.tools.StringUilts;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xl on 2016/12/10.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    //注册
    @Bind(R.id.btn_register)
    Button btn_register;
    //协议
    @Bind(R.id.btn_register_xieyi)
    Button btn_register_xieyi;
    //返回
    @Bind(R.id.btn_register_back)
    Button btn_register_back;
    //号码
    @Bind(R.id.editText_register_phone)
    EditText editText_register_phone;
    //验证码
    @Bind(R.id.editText_register_auth_code)
    EditText editText_register_auth_code;
    //密码
    @Bind(R.id.editText_register_pass)
    EditText editText_register_pass;
    //btn验证码
    @Bind(R.id.btn_register_auth_code)
    Button btn_register_auth_code;
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    private String rid;
    private String androidId;
    private TimeCount timeCount;
    //常量
    private int constants;
    private int codenumber = 1;
    //
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
        JPushInterface.init(getApplicationContext());
        //初始化数据
        initData();
        //初始化控件
        initView();
    }
    //初始化监听
    private void initView() {
        btn_register.setOnClickListener(this);
        btn_register_back.setOnClickListener(this);
        btn_register_xieyi.setOnClickListener(this);
        btn_register_auth_code.setOnClickListener(this);
    }
    //初始化数据
    private void initData() {
        constants = Constants.ZERO;
        //获取registertionid获取
        rid = JPushInterface.getRegistrationID(getApplicationContext());
        //设备id
        androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        Log.i("RegisterActivity",rid+"jpush---"+ androidId);
    }

    /**
     * 监听事件处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            //返回上一个活动
            case R.id.btn_register_back:
                finish();
                break;
            //查看协议内容
            case R.id.btn_register_xieyi:
                intent.setClass(this,AgreementActivity.class);
                startActivity(intent);
                break;
            //获取验证码
            case R.id.btn_register_auth_code:
                if (!TextUtils.isEmpty(editText_register_phone.getText().toString())&&editText_register_phone.getText().toString().length()==11){
                    if(PhoneNumber.IsMoblieNo(editText_register_phone.getText().toString())){
                        constants = Constants.ONE;
                        loading_linearlayout.setVisibility(View.VISIBLE);
                        httpNotPhoneNumber();
                    }else {
                        Toast.makeText(getApplication(),"您输入的手机号格式不存在!",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplication(),"您输入的手机号格式不存在!",Toast.LENGTH_SHORT).show();
                }
                break;
            //注册
            case R.id.btn_register:
                if (!TextUtils.isEmpty(editText_register_phone.getText().toString())){
                    if (editText_register_phone.getText().toString().length()==11&&PhoneNumber.IsMoblieNo(editText_register_phone.getText().toString())){
                        if (!TextUtils.isEmpty(editText_register_auth_code.getText().toString())){
                            if (constants != Constants.ZERO){
                                if (codenumber == Constants.ZERO){
                                    if (!TextUtils.isEmpty(editText_register_auth_code.getText().toString())){
                                        if (!TextUtils.isEmpty(editText_register_pass.getText().toString())){
                                            if (editText_register_pass.getText().toString().length()<17&&editText_register_pass.getText().toString().length()>5&& StringUilts.StringPass(editText_register_pass.getText().toString())){
                                                loading_linearlayout.setVisibility(View.VISIBLE);
                                                httpRegister();
                                            } else {
                                                Toast.makeText(getApplication(),"请输入6-18位数字字母组合密码!",Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(getApplication(),"请输入密码",Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(getApplication(),"请输入验证码!",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplication(),"您输入的手机号码有误",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplication(),"请输入您的手机号码",Toast.LENGTH_SHORT).show();
                }
        }
    }
    //判断用户是否存在
    private void httpNotPhoneNumber() {
        OkHttpUtils.post(Api.select_user)
                .tag(getApplication())
                .params("userCode",editText_register_phone.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            if (new JSONObject(s).getInt("code") == 1){
                                timeCount = new TimeCount(60000,1000);
                                timeCount.start();
                                httpAuthCode();
                            }else {
                                loading_linearlayout.setVisibility(View.GONE);
                                Toast.makeText(getApplication(),"号码已存在!",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        if (StringUilts.isNetworkAvailable(getApplication()) != Constants. ZERO){
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
            btn_register_auth_code.setClickable(false);
            btn_register_auth_code.setText(l /1000+"秒");
        }

        //计时完毕操作
        @Override
        public void onFinish() {
            btn_register_auth_code.setText("重新验证");
            btn_register_auth_code.setClickable(true);
        }
    }
    //注册提交
    private void httpRegister() {
        OkHttpUtils.post(Api.register)
                .tag(getApplication())
                .params("phone", editText_register_phone.getText().toString())
                .params("smsCode",editText_register_auth_code.getText().toString())
                .params("passWord",editText_register_pass.getText().toString())
                .params("registrationID",rid)
                .params("deviceId",androidId)
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
                                finish();
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
                        if (StringUilts.isNetworkAvailable(getApplication()) != Constants. ZERO){
                            Toast.makeText(getApplication(),"服务异常",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplication(),"无网络",Toast.LENGTH_LONG).show();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }
    //获取验证码
    private void httpAuthCode() {
        OkHttpUtils.post(Api.auth_code)
                .tag(getApplicationContext())
                .params("phone", editText_register_phone.getText().toString())
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
                     * 网络异常 627576
                     * @param isFromCache
                     * @param call
                     * @param response
                     * @param e
                     */
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        if (StringUilts.isNetworkAvailable(getApplication()) != Constants. ZERO){
                            Toast.makeText(getApplication(),"服务异常",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplication(),"无网络",Toast.LENGTH_LONG).show();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }

}
