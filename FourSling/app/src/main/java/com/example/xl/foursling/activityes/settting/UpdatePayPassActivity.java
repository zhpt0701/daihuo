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
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.view.titlebar.TeaskBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2016/12/19.
 */
public class UpdatePayPassActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    @Bind(R.id.activity_update_pay_pass_btn_jd)
    Button activity_update_pay_pass_btn_jd;
    @Bind(R.id.activity_update_pau_pass_btn_wj)
    Button activity_update_pau_pass_btn_wj;
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pay_pass);
        //注解绑定
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        //初始化数据
        initData();
        //初始化控件
        initView();

    }

    private void initView() {
        layout_title_back.setOnClickListener(this);
        activity_update_pay_pass_btn_jd.setOnClickListener(this);
        activity_update_pau_pass_btn_wj.setOnClickListener(this);
    }

    private void initData() {
        if (Integer.valueOf(getIntent().getStringExtra("update_pass")) == Constants.ONE) {
            layout_title_textview.setText("修改登录密码");
            activity_update_pay_pass_btn_jd.setText("记得原登录密码");
            activity_update_pau_pass_btn_wj.setText("忘记原登录密码");
        } else {
            layout_title_textview.setText("修改支付密码");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            //退出当前
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.activity_update_pay_pass_btn_jd:
                intent.setClass(this, UpdateActivity.class);
                if (Integer.valueOf(getIntent().getStringExtra("update_pass")) == Constants.ONE) {
                    intent.putExtra("state_code", String.valueOf(Constants.ONE));
                } else {
                    intent.putExtra("state_code", String.valueOf(Constants.TWO));
                }
                startActivity(intent);
                break;
            case R.id.activity_update_pau_pass_btn_wj:
                intent.putExtra("update_pass",getIntent().getStringExtra("update_pass"));
                intent.setClass(this,SecurityStateActivity.class);
                startActivity(intent);
                break;

        }
    }

}
