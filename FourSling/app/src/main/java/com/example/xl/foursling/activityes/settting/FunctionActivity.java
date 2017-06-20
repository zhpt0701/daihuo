package com.example.xl.foursling.activityes.settting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.view.titlebar.TeaskBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/1/14.
 */
public class FunctionActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.activity_function_webview)
    WebView activity_function_webview;
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        //注解框架
        ButterKnife.bind(this);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        //初始化数据
        initData();
        //初始化控件
        initView();
    }

    private void initView() {
        layout_title_back.setOnClickListener(this);
    }

    private void initData() {
        layout_title_textview.setText(getResources().getString(R.string.function));
        activity_function_webview.loadUrl("file:///android_asset/agreement.html");
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
