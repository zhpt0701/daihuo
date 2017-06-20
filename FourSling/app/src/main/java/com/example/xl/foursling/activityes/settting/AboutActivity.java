package com.example.xl.foursling.activityes.settting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.MyApplication;
import com.example.xl.foursling.R;
import com.example.xl.foursling.StartActivity;
import com.example.xl.foursling.activityes.AgreementActivity;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.tools.update.AppUtils;
import com.example.xl.foursling.view.titlebar.TeaskBar;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2017/1/14.
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {
    /**
     *
     * @param activity_about_update 版本更新
     * @param activity_about_function 功能介绍
     * @param activity_about_agreement 用户协议
     * @param activity_about_tickling 意见反馈
     * @param activity_about_phone 客服电话
     * @param layout_title_back 返回
     * @param activity_about_text_version 版本号
     * @param layout_title_textview title
     */
    @Bind(R.id.activity_about_update)
    Button activity_about_update;
    @Bind(R.id.activity_about_function)
    Button activity_about_function;
    @Bind(R.id.activity_about_agreement)
    Button activity_about_agreement;
    @Bind(R.id.activity_about_tickling)
    Button activity_about_tickling;
    @Bind(R.id.activity_about_phone)
    Button activity_about_phone;
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    @Bind(R.id.activity_about_text_version)
    TextView activity_about_text_version;
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    private PopupWindow popupWindow;
    private View view;
    private Button update_version_button;
    private TextView update_version_text_content;
    private TextView update_version_text_title;
    private String url = null;
    private PackageInfo info;
    private PackageManager packageManager;
    private String url1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //绑定注解
        ButterKnife.bind(this);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        //初始化数据
        initData();
        //初始化控件
        initView();
    }

    private void initData() {
        packageManager = getPackageManager();
        try {
            info = packageManager.getPackageInfo(getPackageName(),0);
            activity_about_text_version.setText("版本:"+info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        layout_title_textview.setText(getResources().getString(R.string.about));

    }

    private void initView() {
        activity_about_update.setOnClickListener(this);
        activity_about_agreement.setOnClickListener(this);
        activity_about_function.setOnClickListener(this);
        activity_about_phone.setOnClickListener(this);
        activity_about_function.setOnClickListener(this);
        activity_about_tickling.setOnClickListener(this);
        layout_title_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.layout_title_back:
                finish();
                break;
            //版本更新
            case R.id.activity_about_update:
                httpupdate();
                break;
            //功能简介
            case R.id.activity_about_function:
                intent.setClass(this,FunctionActivity.class);
                startActivity(intent);
                break;
            //用户协议
            case R.id.activity_about_agreement:
                intent.setClass(this, AgreementActivity.class);
                startActivity(intent);
                break;
            //意见反馈
            case R.id.activity_about_tickling:
                intent.setClass(this,TicklingActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_about_phone:
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
            case R.id.update_version_button:
                if (!TextUtils.isEmpty(url)){
                    initupdate(url);
                }else {
                    popupWindow.dismiss();
                    windowsPopupback(1f);
                }
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
    private void httpupdate() {
        OkHttpUtils.post(Api.version_update)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        Log.d("this+content",s);
                        try {
                            if (new JSONArray(s).length()>0) {
                                if (new JSONArray(s).getJSONObject(0).getInt("id") > info.versionCode) {
                                    windowsPopupback(0.5f);
                                    url1 = new JSONArray(s).getJSONObject(0).getString("versionUrl");
                                    initpopup(true,"最新版本:"+new JSONArray(s).getJSONObject(0).getString("versionNum"),new JSONArray(s).getJSONObject(0).getString("versionContent"));
                                } else {
                                    windowsPopupback(0.5f);
                                    initpopup(true,"当前版本:"+info.versionName,"");
                                }
                            } else {
                                windowsPopupback(0.5f);
                                initpopup(true,"当前版本:"+info.versionName,"");
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
                        windowsPopupback(0.5f);
                        initpopup(true,"当前版本:"+info.versionName,"");
                    }
                });
    }

    private void initpopup(boolean b, String s,String content) {
        popupWindow = new PopupWindow();
        view = LayoutInflater.from(this).inflate(R.layout.update_vserion,null);
        popupWindow.setContentView(view);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setClippingEnabled(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        update_version_text_title = (TextView)view.findViewById(R.id.update_version_text_title);
        update_version_text_content = (TextView)view.findViewById(R.id.update_version_text_content);
        update_version_button = (Button)view.findViewById(R.id.update_version_button);
        update_version_button.setOnClickListener(this);
        if (!TextUtils.isEmpty(content)){
            update_version_text_title.setText(s);
            update_version_text_content.setText(content);
        }else{
            update_version_text_content.setText(s);
        }
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
                windowsPopupback(1f);
            }
        });
        if (b){
            popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
        }
    }
    //改变背景色
    private void windowsPopupback(float v) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = v;
        getWindow().setAttributes(layoutParams);
    }
    /**
     * 主界面弹框更新apk
     * @param downloadUrl
     */
    private void initupdate(String downloadUrl) {
        OkHttpUtils.get(downloadUrl)
                .tag(getApplication())
                .execute(new FileCallback(AppUtils.getSDPath()+"/download","i带货.apk") {
                    @Override
                    public void onResponse(boolean isFromCache, File file, Request request, @Nullable Response response) {
                        windowsPopupback(1f);
                        AppUtils.toastBottom(AboutActivity.this,"成功");
                        AppUtils.install(AboutActivity.this,file.toString());
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        AppUtils.toastBottom(AboutActivity.this,"失败");
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                        AppUtils.toastBottom(AboutActivity.this, "进度" + StringUilts.doubleMoney(Double.parseDouble(String.valueOf(progress))*100)+"%");
                    }
                });

    }
}
