package com.example.xl.foursling.activityes;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.view.titlebar.TeaskBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xl on 2016/12/12.
 */
public class AgreementActivity extends BaseActivity implements View.OnClickListener {
    //返回
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    //协议内容
    @Bind(R.id.webView_agreement)
    WebView webView_agreement;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        //注解绑定
        ButterKnife.bind(this);
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        //初始化数据
        initData();
        //初始化控件
        initView();
    }

    private void initView() {
        //初始化返回监听
        layout_title_back.setOnClickListener(this);
    }

    private void initData() {
        layout_title_textview.setText(getResources().getString(R.string.agreement_textview));
//        webView_ol();
        webView_agreement.loadUrl("file:///android_asset/i_fourxling_arg.html");
        //加载协议内容
//        webView_agreement.loadUrl();
//        webView_agreement.loadDataWithBaseURL(null, getNewContent(con), "text/html", "utf-8", null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //返回
            case R.id.layout_title_back:
                finish();
                break;
        }

    }
    //处理带图片的html
    private void webView_ol(String mDescription) {
        WebSettings settings = webView_agreement.getSettings();
        settings.setJavaScriptEnabled(true);//启用js功能
        settings.setLoadWithOverviewMode(true);//适应屏幕
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);//关键点

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        settings.setSupportZoom(true); // 支持缩放
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        Log.d("maomao", "densityDpi = " + mDensity);
        if (mDensity == 240) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else{
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        settings.setUseWideViewPort(true);
        //开始加载网页
//        linearLayout.setVisibility(View.GONE);
        webView_agreement.loadDataWithBaseURL(null, mDescription, "text/html", "utf-8", null);
//        webview.setWebChromeClient(new WebChromeClient());
        webView_agreement.setWebViewClient(new WebViewClient() {
            //页面开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.println("开始加载....");
            }

            //跳转链接
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("跳转链接...." + url);
                //所有跳转链接强制在当前webview加载,不跳浏览器
//                webview.loadUrl(url);

                return true;
            }

            //加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("加载结束....");
                super.onPageFinished(view, url);
            }

        });

    }

//    /**
//     * 处理图片
//     * @param htmltext
//     * @return
//     */
//    private String getNewContent(String htmltext){
//
//        Document doc= Jsoup.parse(htmltext);
//        Elements elements=doc.getElementsByTag("img");
//        for (Element element : elements) {
//            element.attr("width","100%").attr("height","auto");
//        }
//        return doc.toString();
//    }
}
