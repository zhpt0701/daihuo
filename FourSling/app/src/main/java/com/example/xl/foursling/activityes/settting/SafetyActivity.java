package com.example.xl.foursling.activityes.settting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.SharePreferencesUlits;
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
public class SafetyActivity extends BaseActivity implements View.OnClickListener {
    /**
     * @pram button
     * @pram textview
     */
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    @Bind(R.id.activity_safety_btn_up_pass)
    Button activity_safety_btn_up_pass;
    @Bind(R.id.activity_safety_btn_up_pay_pass)
    Button activity_safety_btn_up_pay_pass;
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    //设置常量值
    public int STATE_CODE = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety);
        //改变状态蓝颜色
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
        layout_title_textview.setText(R.string.fragment_setting_textview_safety);
        //设置监听事件
        layout_title_back.setOnClickListener(this);
        activity_safety_btn_up_pass.setOnClickListener(this);
        activity_safety_btn_up_pay_pass.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        httpalipay();
        loading_linearlayout.setVisibility(View.VISIBLE);
    }

    private void httpalipay() {
        OkHttpUtils.post(Api.select_pay_pass)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(this, CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            switch (new JSONObject(s).getInt("code")){
                                case 0:
                                    STATE_CODE = 0;
                                    activity_safety_btn_up_pay_pass.setText("修改支付密码");
                                    break;
                                default:
                                    STATE_CODE = 1;
                                    activity_safety_btn_up_pay_pass.setText("设置支付密码");
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
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.activity_safety_btn_up_pass:
                intent.setClass(this,UpdatePayPassActivity.class);
                intent.putExtra("update_pass",String.valueOf(Constants.ONE));
                startActivity(intent);
                break;
            case R.id.activity_safety_btn_up_pay_pass:
                switch (STATE_CODE){
                    case 0:
                        intent.setClass(this,UpdatePayPassActivity.class);
                        intent.putExtra("update_pass",String.valueOf(Constants.TWO));
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(this,SetPayPassActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
        }
    }
}
