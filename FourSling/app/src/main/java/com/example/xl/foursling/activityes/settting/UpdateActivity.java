package com.example.xl.foursling.activityes.settting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.activityes.LandingActivity;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.AppManager;
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
 * Created by admin on 2016/12/16.
 */
public class UpdateActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.activity_update_ed_new)
    EditText activity_update_ed_new;
    @Bind(R.id.activity_update_ed_old)
    EditText activity_update_ed_old;
    @Bind(R.id.activity_update_ed_ok_new)
    EditText activity_update_ed_ok_new;
    @Bind(R.id.activity_update_btn_ok)
    Button activity_update_btn_ok;
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    @Bind(R.id.activity_update_pay_ed_new)
    EditText activity_update_pay_ed_new;
    @Bind(R.id.activity_update_pay_ed_old)
    EditText activity_update_pay_ed_old;
    @Bind(R.id.activity_update_pay_ed_ok_new)
    EditText activity_update_pay_ed_ok_new;
    @Bind(R.id.activity_update_pay_linearlayout)
    LinearLayout activity_update_pay_linearlayout;
    @Bind(R.id.activity_update_pass)
    LinearLayout activity_update_pass;
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //绑定注解
        ButterKnife.bind(this);
        //初始化数据
        initData();
        //初始化控件
        initView();
    }

    private void initView() {
        layout_title_back.setOnClickListener(this);
        activity_update_btn_ok.setOnClickListener(this);
    }

    private void initData() {
        if (Integer.valueOf(getIntent().getStringExtra("state_code")) == Constants.ONE){
            layout_title_textview.setText("记得原登录密码");
            activity_update_pay_linearlayout.setVisibility(View.GONE);
            activity_update_pass.setVisibility(View.VISIBLE);
        }else {
            activity_update_pay_linearlayout.setVisibility(View.VISIBLE);
            activity_update_pass.setVisibility(View.GONE);
            layout_title_textview.setText("记得原支付密码");
        }

    }
    //监听响应事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.activity_update_btn_ok:
                if (Integer.valueOf(getIntent().getStringExtra("state_code")) == Constants.TWO){
                    if (!TextUtils.isEmpty(activity_update_pay_ed_new.getText().toString())){
                        if (activity_update_pay_ed_old.getText().toString().length()==6){
                            if (activity_update_pay_ed_new.getText().toString().equals(activity_update_pay_ed_ok_new.getText().toString())){
                                httpUpdatePass("accountPassWord", Api.old_pass_update_pay_pass
                                        ,activity_update_pay_ed_old.getText().toString()
                                        ,activity_update_pay_ed_ok_new.getText().toString());
                                loading_linearlayout.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getApplication(),"两次设置密码不一致",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplication(),"请设置6位数字密码",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplication(),"密码设置不能为空",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (!TextUtils.isEmpty(activity_update_ed_old.getText().toString().trim())&&activity_update_ed_old.getText().toString().length()>=6){
                        if (!TextUtils.isEmpty(activity_update_ed_new.getText().toString())&&activity_update_ed_new.getText().toString().length()>=6&& StringUilts.StringPass(activity_update_ed_new.getText().toString())){
                            if (activity_update_ed_new.getText().toString().equals(activity_update_ed_ok_new.getText().toString())){
                                loading_linearlayout.setVisibility(View.VISIBLE);
                                httpUpdatePass("passWord", Api.old_pass_upate_landing_pass,
                                        activity_update_ed_old.getText().toString(),
                                        activity_update_ed_ok_new.getText().toString()
                                );
                            } else {
                                Toast.makeText(getApplication(),"两次设置密码不一致",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplication(),"请设置6-18位数字字母组合密码",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplication(),"原始输入不正确密码",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void httpUpdatePass(String pass,String url,String old,String conent) {
        OkHttpUtils.post(url)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .params(pass,old)
                .params("newPwd",conent)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            Toast.makeText(getApplication(),new JSONObject(s).getString("message"),Toast.LENGTH_SHORT).show();
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                if (Integer.valueOf(getIntent().getStringExtra("state_code")) == Constants.ONE){
                                    Intent intent = new Intent();
                                    intent.setClass(getApplication(), LandingActivity.class);
                                    startActivity(intent);
                                    finish();
                                    AppManager.getAppManager().finishAllActivity();
                                }else {
                                    finish();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                    }
                });
    }
}
