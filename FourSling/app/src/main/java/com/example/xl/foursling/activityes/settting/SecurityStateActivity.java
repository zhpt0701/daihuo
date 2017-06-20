package com.example.xl.foursling.activityes.settting;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.activityes.LandingPassActivity;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2016/12/30.
 */
public class SecurityStateActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    @Bind(R.id.activity_security_state_text)
    TextView activity_security_state_text;
    @Bind(R.id.activity_security_state_linear)
    LinearLayout activity_security_state_linear;
    @Bind(R.id.activity_security_state_linear_id)
    LinearLayout activity_security_state_linear_id;
    @Bind(R.id.activity_security_state_text_id)
    TextView activity_security_state_text_id;
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    private String contnet;
    private PopupWindow popupWindow;
    private View view1;
    private Button popup_setting_cancel_btn;
    private LinearLayout activity_security_linear_id;
    private EditText activity_security_input_id;
    private Button popup_setting_ok_btn;
    private TextView popup_setting_textview;
    private TextView popup_setting_textview_content;
    private int state,user_state;
    private int STATE;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_state);
        //设置状态栏颜色
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
        activity_security_state_linear.setOnClickListener(this);
        activity_security_state_linear_id.setOnClickListener(this);
    }

    private void initData() {
        STATE = Constants.ZERO;
        activity_security_state_text_id.setText("身份证验证");
    }

    @Override
    protected void onStart() {
        super.onStart();
        loading_linearlayout.setVisibility(View.VISIBLE);
        httpsecurity();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.activity_security_state_linear:
                STATE = Constants.ONE;
                loading_linearlayout.setVisibility(View.VISIBLE);
                httpcheckCode();
                break;
            case R.id.activity_security_state_linear_id:
                STATE = Constants.TWO;
                loading_linearlayout.setVisibility(View.VISIBLE);
                httpcheckCode();
                break;
            //确定
            case R.id.popup_setting_ok_btn:
                popupWindow.dismiss();
                backgroundAlpha(1f);
                switch (user_state){
                    case 2:
                        if (!TextUtils.isEmpty(activity_security_input_id.getText().toString())){
                            if (StringUilts.isIdcard(activity_security_input_id.getText().toString())) {
                                loading_linearlayout.setVisibility(View.VISIBLE);
                                httpselectID();
                            } else {
                                Toast.makeText(getApplication(), "您输入的身份证号码不存在", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplication(), "请输入身份证号码", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        //android 6.0 版本权限获取判断
                        Log.d("this_content_sdk", Build.VERSION.SDK_INT+"");
                        if (Build.VERSION.SDK_INT >= 23) {
                            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, "android.permission.CALL_PHONE");
                            Log.d("this_content_sdk",checkCallPhonePermission+"");
                            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this, new String[]{"android.permission.CALL_PHONE"}, 2);
                            }else {
                                intent.setAction(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:029-88764288"));
                                startActivity(intent);
                            }
                        }else {
                            intent.setAction(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:029-88764288"));
                            startActivity(intent);
                        }
                        break;
                    case 4:
                        intent.setClass(this, UserAptitudeActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            //取消
            case R.id.popup_setting_cancel_btn:
                popupWindow.dismiss();
                backgroundAlpha(1f);
                break;

        }
    }
    /**
     * android 6.0 版本权限获取回掉事件
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:029-88764288"));
            startActivity(intent);
        } else {
            Toast.makeText(getApplication(),"未给予权限，部分功能无法正常使用",Toast.LENGTH_LONG).show();
        }
    }
    //请求密保状态
    private void httpsecurity() {
        OkHttpUtils.post(Api.select_security)
                .tag(this)
                .params("userCode", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        Log.d("this++++++++",s);
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                activity_security_state_text.setText("验证密保");
                                layout_title_textview.setText("验证密保");
                                contnet = s;
                            }else {
                                activity_security_state_text.setText("设置密保");
                                layout_title_textview.setText("设置密保");
                                contnet = "";
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
                            activity_security_state_text.setText("设置密保");
                            layout_title_textview.setText("设置密保");
                            contnet = "";
                        }else {
                            Toast.makeText(getApplication(),"无网络",Toast.LENGTH_LONG).show();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }

    //身份证验证
    private void httpselectID() {
        OkHttpUtils.post(Api.ID_verity)
                .tag(getApplication())
                .params("phone", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE, ""))
                .params("idCard", activity_security_input_id.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO) {
                                Intent intent = new Intent();
                                if (STATE == Constants.ONE){
                                    intent.putExtra("security",contnet);
                                    intent.putExtra("update_pass",getIntent().getStringExtra("update_pass"));
                                    intent.setClass(getApplication(), SecurityActivity.class);
                                }else {
                                    intent.setClass(getApplication(), LandingPassActivity.class);
                                    if (Integer.valueOf(getIntent().getStringExtra("update_pass")) == Constants.ONE) {
                                        intent.putExtra("update_code", String.valueOf(Constants.THREE));
                                    } else {
                                        intent.putExtra("update_code", String.valueOf(Constants.TWO));
                                    }
                                }
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplication(), new JSONObject(s).getString("message"), Toast.LENGTH_SHORT).show();
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
    //请求审核状态
    private void httpcheckCode() {
        OkHttpUtils.post(Api.select_user_audit)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        Log.d("this_________________",s);
                        try {
                            loading_linearlayout.setVisibility(View.GONE);
                            switch (new JSONObject(s).getInt("code")){
                                case 0:
                                    user_state = Constants.TWO;
                                    initpopup(Constants.TWO,true,"");
                                    backgroundAlpha(0.5f);
                                    break;
                                case 1:
                                    user_state = Constants.TWO;
                                    initpopup(Constants.TWO,true,"");
                                    backgroundAlpha(0.5f);
                                    break;
                                case 2:
                                    user_state = Constants.TWO;
                                    initpopup(Constants.TWO,true,"");
                                    backgroundAlpha(0.5f);
                                    break;
                                case 3:
                                    user_state = Constants.THREE;
                                    initpopup(Constants.THREE,true,"您的账号已被封,请联系客服具体解决");
                                    backgroundAlpha(0.5f);
                                    break;
                                case 4:
                                    user_state = Constants.FOUR;
                                    initpopup(Constants.FOUR,true,"请完善个人资料");
                                    backgroundAlpha(0.5f);
                                    break;
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
    //自定义popup弹框
    private void initpopup(int state,boolean flag,String str) {
        popupWindow = new PopupWindow();
        view1 = LayoutInflater.from(this).inflate(R.layout.popup_setting_back, null);
        popupWindow.setContentView(view1);
        //定义popupwindows宽高
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setClippingEnabled(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popup_setting_cancel_btn = (Button) view1.findViewById(R.id.popup_setting_cancel_btn);
        activity_security_linear_id = (LinearLayout) view1.findViewById(R.id.activity_security_linear_ID);
        activity_security_input_id = (EditText) view1.findViewById(R.id.activity_security_input_ID);
        popup_setting_ok_btn = (Button) view1.findViewById(R.id.popup_setting_ok_btn);
        popup_setting_textview = (TextView) view1.findViewById(R.id.popup_setting_textview);
        popup_setting_textview_content = (TextView) view1.findViewById(R.id.popup_setting_textview_content);
        switch (state) {
            case 2:
                this.state = state;
                popup_setting_textview.setText("身份验证");
                popup_setting_textview_content.setVisibility(View.GONE);
                activity_security_linear_id.setVisibility(View.VISIBLE);
                this.state = state;
                break;
            default:
                this.state = state;
                activity_security_linear_id.setVisibility(View.GONE);
                popup_setting_textview.setText("温馨提示");
                popup_setting_textview_content.setText(str);
                break;
        }
        TextPaint textPaint = popup_setting_textview.getPaint();
        textPaint.setFakeBoldText(true);
        popup_setting_cancel_btn.setOnClickListener(this);
        popup_setting_ok_btn.setOnClickListener(this);
        //设置点击空白消失
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });

        if(flag)

        {
            popupWindow.showAtLocation(view1, Gravity.CENTER, 0, 0);
        }
    }
    /** popup弹框app背景变暗
     * @param v
     */
    private void backgroundAlpha(float v) {
        WindowManager.LayoutParams ly = getWindow().getAttributes();
        ly.alpha = v;
        getWindow().setAttributes(ly);
    }
}
