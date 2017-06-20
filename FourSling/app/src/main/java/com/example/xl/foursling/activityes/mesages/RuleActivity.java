package com.example.xl.foursling.activityes.mesages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.view.titlebar.TeaskBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2016/12/24.
 */
public class RuleActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        //绑定注解
        ButterKnife.bind(this);
        //初始化数据
        initDate();
        //初始化控件
        initView();
    }

    private void initView() {
        layout_title_textview.setText("计算规则");
    }

    private void initDate() {
        layout_title_back.setOnClickListener(this);
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
