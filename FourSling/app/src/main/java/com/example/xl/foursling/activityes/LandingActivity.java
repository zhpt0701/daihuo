package com.example.xl.foursling.activityes;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.Text;
import com.amap.api.navi.model.NaviLatLng;
import com.android.volley.toolbox.HttpClientStack;
import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.MainActivity;
import com.example.xl.foursling.Manifest;
import com.example.xl.foursling.MyApplication;
import com.example.xl.foursling.R;
import com.example.xl.foursling.fragments.RoderFragment;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.service.MdieService;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.PhoneNumber;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.User;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.helper.HttpConnection;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xl on 2016/12/10.
 */
public class LandingActivity extends BaseActivity implements View.OnClickListener {
    //注册
    @Bind(R.id.btn_landing_register)
    Button btn_landing_register;
    //密码找回
    @Bind(R.id.btn_landing_not_pass)
    Button btn_landing_not_pass;
    //登陆
    @Bind(R.id.btn_landing)
    Button btn_landing;
    //输入手机号码
    @Bind(R.id.editText_landing_phone_number)
    EditText editText_landing_phone_number;
    //输入密码
    @Bind(R.id.editText_landing_pass)
    EditText editText_landing_pass;
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    private Button btn_popup_error;
    private TextView popup_textView;
    private PopupWindow popupWindow;
    private boolean flag = false;
    private View view;
    private String androidId;
    private String rid;
    private long finshApp = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        //注解绑定
        ButterKnife.bind(this);
        //初始化数据
        initData();
        //初始化控件
        initView();
    }
    //初始化监听事件
    private void initView() {
        btn_landing_register.setOnClickListener(this);
        btn_landing_not_pass.setOnClickListener(this);
        btn_landing.setOnClickListener(this);
    }
    private void initData() {
//        Intent intent = new Intent();
//        intent.setClass(this, MdieService.class);
//        startService(intent);
        //重新定位
        MyApplication.mLocationClient.startLocation();
        //获取registertionid获取
        rid = JPushInterface.getRegistrationID(getApplicationContext());
        androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        Log.i("RegisterActivity","jpush---"+ androidId);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    /**
     * 监听时间响应处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            //注册
            case R.id.btn_landing_register:
                intent.setClass(this,RegisterActivity.class);
                startActivity(intent);
                break;
            //密码找回
            case R.id.btn_landing_not_pass:
                intent.setClass(this,LandingPassActivity.class);
                intent.putExtra("update_code",String.valueOf(Constants.ONE));
                startActivity(intent);
                break;
            //登陆
            case R.id.btn_landing:
//                intent.setClass(this,MainActivity.class);
//                startActivity(intent);
//                finish();
                if (StringUilts.isNetworkAvailable(getApplication()) != Constants.ZERO){
                    //判断号码是否为空
                    if (!TextUtils.isEmpty(editText_landing_phone_number.getText().toString())&&editText_landing_phone_number.getText().toString().length()==11){
                        //判断号码是否存在
                        if (PhoneNumber.IsMoblieNo(editText_landing_phone_number.getText().toString())){
                            if (!TextUtils.isEmpty(editText_landing_pass.getText().toString().trim())){
                                //判断密码格式是否正确
                                if (5<editText_landing_pass.getText().toString().length()&&editText_landing_pass.getText().length()<17){
                                    loading_linearlayout.setVisibility(View.VISIBLE);
                                    httpLanding();
                                }else {
                                    flag = true;
                                    backgroundAlpha(0.5f);
                                    initpopup("请输入6-18位密码");
                                }
                            }else {
                                flag = true;
                                backgroundAlpha(0.5f);
                                initpopup("请输入密码");
                            }
                        }else {
                            flag = true;
                            backgroundAlpha(0.5f);
                            initpopup("您输入的号码格式不存在!");
                        }
                    }else {
                        flag = true;
                        backgroundAlpha(0.5f);
                        initpopup("您输入的号码格式不正确!");
                    }
                }else {
                    flag = true;
                    backgroundAlpha(0.5f);
                    initpopup("当前无网络");
                }
                break;
            case R.id.btn_popup_error:
                popupWindow.dismiss();
                backgroundAlpha(1f);
                break;
        }
    }
    //自定义popup弹框
    private void initpopup(String s) {
        popupWindow = new PopupWindow();
        view = LayoutInflater.from(this).inflate(R.layout.popup_error,null);
        popupWindow.setContentView(view);
        //定义popupwindows宽高
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setClippingEnabled(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        btn_popup_error = (Button) view.findViewById(R.id.btn_popup_error);
        popup_textView = (TextView) view.findViewById(R.id.popup_error_textview);
        popup_textView.setText(s);
        btn_popup_error.setOnClickListener(this);
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

    //登陆
    private void httpLanding() {
        OkHttpUtils.post(Api.landing)
                .tag(getApplication())
                .params("phone",editText_landing_phone_number.getText().toString())
                .params("passWord",editText_landing_pass.getText().toString())
                .params("deviceId",androidId)
                .params("registrationID",rid)
                .execute(new StringCallback() {
                    /**
                     * request返回值
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
                                User user = GsonUtil.processJson(s,User.class);
                                if (!TextUtils.isEmpty(user.message.address)){
                                    SharePreferencesUlits.saveString(getApplication(), CharConstants.ADDRESS,user.message.address);
                                }
                                if (!TextUtils.isEmpty(user.message.alipayNo)){
                                    SharePreferencesUlits.saveString(getApplication(), CharConstants.ALIPAY_NO,user.message.alipayNo);
                                }
                                if (!TextUtils.isEmpty(user.message.userName)){
                                    SharePreferencesUlits.saveString(getApplication(), CharConstants.USER_NAME,user.message.userName);
                                }
                                if (!TextUtils.isEmpty(user.message.carCode)){
                                    SharePreferencesUlits.saveString(getApplication(), CharConstants.CAR_CODE,user.message.carCode);
                                }
                                if (!TextUtils.isEmpty(user.message.wechatNo)){
                                    SharePreferencesUlits.saveString(getApplication(), CharConstants.WECHATNO,user.message.wechatNo);
                                }
                                if (!TextUtils.isEmpty(user.message.phone)){
                                    SharePreferencesUlits.saveString(getApplication(),CharConstants.PHONE,user.message.phone);
                                }
                                if (!TextUtils.isEmpty(user.message.userPic)){
                                    SharePreferencesUlits.saveString(getApplication(), CharConstants.PHOTO,user.message.userPic);
                                }
                                if (user.message.userSex == Constants.ZERO){
                                    SharePreferencesUlits.saveString(getApplication(),CharConstants.USER_SEX, "女");
                                }else {
                                    SharePreferencesUlits.saveString(getApplication(),CharConstants.USER_SEX, "男");
                                }
                                SharePreferencesUlits.saveString(getApplication(),CharConstants.PASSWORD,editText_landing_pass.getText().toString());
                                Intent intent = new Intent();
                                intent.setClass(getApplication(), MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(getApplication(),"登录成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplication(),new JSONObject(s).getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    /**
                     * error报错信息
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
    /**
     * 设置退出控制
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if ((System.currentTimeMillis() - finshApp) > 2000) {
                Toast.makeText(this, "再按一次后退键退出程序",
                        Toast.LENGTH_SHORT).show();
                finshApp = System.currentTimeMillis();
            }else {
                this.finish();
            }
            return true;
        }else {
            return  super.onKeyDown(keyCode, event);
        }

    }
}
