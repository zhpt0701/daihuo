package com.example.xl.foursling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.activityes.LandingActivity;
import com.example.xl.foursling.broadcast.ExampleUtil;
import com.example.xl.foursling.fragments.FragmentFactory;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.http.MyImageLoader;
import com.example.xl.foursling.http.ThisPhoto;
import com.example.xl.foursling.service.MdieService;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.view.titlebar.TeaskBar;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    /**
     * 静态常量值
     */
    public static boolean isForeground = false;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.xl.foursling.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private long finshApp = 0;
    //用户图像显示
    public static ImageView imageView;
    //消息
    private ImageButton layout_menu_imagebutton_message;
    //个人中心
    private RelativeLayout layout_menu_relation;
    // 消息 user 车牌
    private Button btn_main_activity;
    private TextView layout_menu_textview_message,nav_header_main_textview,textView;
    //接单 钱包 行程 设置
    private Button layout_menu_radiobutton_order,layout_menu_radiobutton_money,
            layout_menu_radiobutton_route,layout_menu_radiobutton_setting;
    public static SlidingMenu menu;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private ArrayList<String> fragmentTags;
    private ThisPhoto thisPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //改变状态栏背景色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        //初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
        JPushInterface.init(getApplicationContext());
        //注册广布接收器
        registerMessageReceiver();
        fragmentTags = new ArrayList<>(Arrays.asList("MessageFragment", "UserEnterFragment", "RoderFragment", "MoneyFragment","RouteFragment","SettingFragment","FreightFragment","FragmentDeposit"));
        setCurrentFragment(Constants.TWO);
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.activity_height_fifteen);
        menu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        menu.setOffsetFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.layout_menu);
//        clickTab(1);
        initView();
        init();

    }
    //初始化控件
    private void initView() {
        layout_menu_radiobutton_order = (Button)findViewById(R.id.layout_menu_radiobutton_order);
        layout_menu_radiobutton_money = (Button)findViewById(R.id.layout_menu_radiobutton_money);
        layout_menu_radiobutton_route = (Button)findViewById(R.id.layout_menu_radiobutton_route);
        layout_menu_radiobutton_setting = (Button)findViewById(R.id.layout_menu_radiobutton_setting);
        layout_menu_radiobutton_order.setSelected(true);
        //设置点击事件
        layout_menu_radiobutton_order.setOnClickListener(this);
        layout_menu_radiobutton_money.setOnClickListener(this);
        layout_menu_radiobutton_route.setOnClickListener(this);
        layout_menu_radiobutton_setting.setOnClickListener(this);
        layout_menu_relation = (RelativeLayout)findViewById(R.id.layout_menu_relation);
        imageView = (ImageView)findViewById(R.id.imageView);
        layout_menu_imagebutton_message = (ImageButton)findViewById(R.id.layout_menu_imagebutton_message);
        nav_header_main_textview = (TextView)findViewById(R.id.nav_header_main_textview);
//        btn_main_activity = (Button)findViewById(R.id.btn_main_activity);
        layout_menu_textview_message = (TextView)findViewById(R.id.layout_menu_textview_message);
        textView = (TextView)findViewById(R.id.textView);
        //设置监听事件
        layout_menu_imagebutton_message.setOnClickListener(this);
        layout_menu_relation.setOnClickListener(this);
//        btn_main_activity.setOnClickListener(this)
    }

    private void init() {
        thisPhoto = new ThisPhoto(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        //每次进入界面需要刷新的数据
        if (!TextUtils.isEmpty(SharePreferencesUlits.getString(getApplication(),CharConstants.CAR_CODE,""))){
            textView.setText(SharePreferencesUlits.getString(getApplication(),CharConstants.CAR_CODE,""));
        }
        if (!TextUtils.isEmpty(SharePreferencesUlits.getString(getApplication(), CharConstants.PHOTO,""))){
            thisPhoto.imageLoader.displayImage(SharePreferencesUlits.getString(getApplication(), CharConstants.PHOTO,""),imageView,thisPhoto.options);
        }
        if (!TextUtils.isEmpty(SharePreferencesUlits.getString(getApplication(),CharConstants.PHONE,""))){
            nav_header_main_textview.setText(SharePreferencesUlits.getString(getApplication(),CharConstants.PHONE,""));
        }
        if (!TextUtils.isEmpty(SharePreferencesUlits.getString(getApplication(),CharConstants.CAR_CODE,""))){
            textView.setText(SharePreferencesUlits.getString(getApplication(),CharConstants.CAR_CODE,""));
        }
        //查询是否有消息
        httpmessage();
    }
    private void httpmessage() {
        OkHttpUtils.post(Api.select_message)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                if (!new JSONObject(s).getString("message").equals("000")){
                                    layout_menu_textview_message.setVisibility(View.VISIBLE);
                                }else {
                                    layout_menu_textview_message.setVisibility(View.GONE);
                                }
                            }else {

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
                    }
                });
    }

    //用户交互
    @Override
    protected void onResume() {
        isForeground = true;

        super.onResume();
    }

    //启动或恢复另一个活动时调用
    @Override
    protected void onPause() {
        isForeground = false; 
        super.onPause();
    }
    //广播注册方法
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }
    /**
     * btn监听响应事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //个人中心
            case R.id.layout_menu_relation:
                setCurrentFragment(Constants.ONE);
                //改变状态栏颜色
                TeaskBar.onSystemoutcolor(this, Constants.ZERO);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                layout_menu_radiobutton_order.setSelected(false);
                layout_menu_radiobutton_money.setSelected(false);
                layout_menu_radiobutton_route.setSelected(false);
                layout_menu_radiobutton_setting.setSelected(false);
                //关闭菜单
                menu.showContent();
                break;
            case R.id.layout_menu_imagebutton_message:
                setCurrentFragment(Constants.ZERO);
                //改变状态栏颜色
                TeaskBar.onSystemoutcolor(this, Constants.ZERO);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                layout_menu_radiobutton_order.setSelected(false);
                layout_menu_radiobutton_money.setSelected(false);
                layout_menu_radiobutton_route.setSelected(false);
                layout_menu_radiobutton_setting.setSelected(false);
                //关闭菜单
                menu.showContent();
                break;
            //接单
            case R.id.layout_menu_radiobutton_order:
                setCurrentFragment(Constants.TWO);
                layout_menu_radiobutton_order.setSelected(true);
                layout_menu_radiobutton_money.setSelected(false);
                layout_menu_radiobutton_route.setSelected(false);
                layout_menu_radiobutton_setting.setSelected(false);
                TeaskBar.onSystemoutcolor(this,Constants.ZERO);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                menu.showContent();
                break;
            //钱包
            case R.id.layout_menu_radiobutton_money:
                setCurrentFragment(Constants.THREE);
                layout_menu_radiobutton_order.setSelected(false);
                layout_menu_radiobutton_money.setSelected(true);
                layout_menu_radiobutton_route.setSelected(false);
                layout_menu_radiobutton_setting.setSelected(false);
                TeaskBar.onSystemoutcolor(this,Constants.ONE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                menu.showContent();
                break;
            //线路
            case R.id.layout_menu_radiobutton_route:
                layout_menu_radiobutton_order.setSelected(false);
                layout_menu_radiobutton_money.setSelected(false);
                layout_menu_radiobutton_route.setSelected(true);
                layout_menu_radiobutton_setting.setSelected(false);
                setCurrentFragment(Constants.FOUR);
                //改变状态栏颜色
                TeaskBar.onSystemoutcolor(this, Constants.ZERO);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                //关闭菜单
                menu.showContent();

                break;
            //设置
            case R.id.layout_menu_radiobutton_setting:
                layout_menu_radiobutton_order.setSelected(false);
                layout_menu_radiobutton_money.setSelected(false);
                layout_menu_radiobutton_route.setSelected(false);
                layout_menu_radiobutton_setting.setSelected(true);
                setCurrentFragment(Constants.FIVE);
                TeaskBar.onSystemoutcolor(this,Constants.ZERO);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                menu.showContent();
                break;
        }
    }



    //局域广播
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(messge);
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }

    private void setCostomMsg(String msg){
        String androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        if (msg.equals(String.valueOf(Constants.TWO))){
            Toast.makeText(getApplication(),"您的账号已在其它设备上登录！",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            //退出登录时清除SharePreferences存储的数据
            SharePreferencesUlits.dataclear(getApplication());
            intent.setClass(this, LandingActivity.class);
            startActivity(intent);
            this.finish();
        }else if (msg.equals(String.valueOf(Constants.SIX))){
            Intent intent = new Intent();
            intent.setClass(this, MdieService.class);
            startService(intent);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
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
    /**
     * fragment界面切换
     */
    public void setCurrentFragment(int currIndex) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = FragmentFactory.getFragment(currIndex);
        fragmentTransaction.replace(R.id.activity_main_linearlayout, fragment,fragmentTags.get(currIndex));//将fragment放到content_home中进行显示
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
//        mMessageReceiver.clearAbortBroadcast();
    }
}
