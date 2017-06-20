package com.example.xl.foursling;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.activityes.LandingActivity;
import com.example.xl.foursling.adapter.NavigationAdapter;
import com.example.xl.foursling.fragments.RoderFragment;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.service.CityService;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.tools.update.ApkUpdateUtils;
import com.example.xl.foursling.tools.update.AppUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xl on 2016/12/3.
 */
public class StartActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.viewFlipper)
    ViewPager viewFlipper;
    @Bind(R.id.activity_start_radiogroup)
    RadioGroup activity_start_radiogroup;
    @Bind(R.id.activity_start_radiobtn_one)
    RadioButton activity_start_radiobtn_one;
    @Bind(R.id.activity_start_radiobtn_two)
    RadioButton activity_start_radiobtn_two;
    @Bind(R.id.activity_start_radiobtn_three)
    RadioButton activity_start_radiobtn_three;
    @Bind(R.id.activity_start_textView)
    TextView activity_start_textView;
    //计时器
    private TimeCount time;
    private MViewPageAdapter navigationAdapter;
    //导航页图片资源
    public int[] guides = new int[]{R.mipmap.new_feature1,
            R.mipmap.new_feature2, R.mipmap.new_feature3};
    private PackageManager manager;
    private int currentVersionCode;
    private String appVersionName;
    private PopupWindow popupWindow;
    private View view1;
    private Button update_version_button;
    private TextView update_version_text_content;
    private TextView update_version_text_title;
    private long finshApp = 0;
    private String download_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //注解绑定
        ButterKnife.bind(this);
        //初始化数据
        initData();

    }

    //初始化控件
    private void initView() {
        final ArrayList<View> mList = new ArrayList<View>();
        LayoutInflater inflat = LayoutInflater.from(this);
        //先添加一个最左侧空的view
        View item = inflat.inflate(R.layout.pager_navigation, null);
        for (int index : guides) {
            item = inflat.inflate(R.layout.pager_navigation, null);
            item.setBackgroundResource(index);
            mList.add(item);
        }
        Button button = (Button) item.findViewById(R.id.pager_navigation_btn);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
        navigationAdapter = new MViewPageAdapter(mList);
        viewFlipper.setAdapter(navigationAdapter);
        viewFlipper.setOnPageChangeListener(navigationAdapter);
        viewFlipper.setCurrentItem(0);

    }

    /**
     * 初始化数据
     *
     * @param
     */
    private void initData() {
        //获取版本号
        currentVersionCode = 0;
        manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            // 版本名
            appVersionName = info.versionName;
            currentVersionCode = info.versionCode; // 版本号
            httpJudge();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.setClass(this, CityService.class);
        startService(intent);
        //android 6.0 版本权限获取判断
        Log.d("this_content_sdk", Build.VERSION.SDK_INT + "");
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION");
            int checkCallPhonePermissions = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
            Log.d("this_content_sdk", checkCallPhonePermission + "");
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED && checkCallPhonePermissions != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
            }
        }
    }

    /**
     * android 6.0 版本权限获取回掉事件
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            MyApplication.mLocationClient.startLocation();
        } else {
            Toast.makeText(getApplication(), "未给予权限，部分功能无法正常使用", Toast.LENGTH_LONG).show();
        }
    }

    //版本判断
    private void httpJudge() {
        OkHttpUtils.get(Api.version_update)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            Log.d("this+content", s);
                            if (new JSONArray(s).length()>0) {
                                if (new JSONArray(s).getJSONObject(0).getInt("id") > currentVersionCode) {
                                    backgroundAlpha(0.1f);
                                    initpopup(true,new JSONArray(s).getJSONObject(0).getString("versionNum"),new JSONArray(s).getJSONObject(0).getString("versionContent"));
                                    download_path = new JSONArray(s).getJSONObject(0).getString("versionUrl");
                                } else {
                                    new TimeCount(2000, 1000).start();
                                }
                            } else {
                                new TimeCount(2000, 1000).start();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        if (StringUilts.isNetworkAvailable(getApplication()) != Constants.ZERO) {
                            Toast.makeText(getApplication(), "服务异常", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplication(), "无网络", Toast.LENGTH_LONG).show();
                        }
                        new TimeCount(2000, 1000).start();
                    }
                });
    }

    /**
     * 主界面弹框更新apk
     *
     * @param downloadUrl
     */
    private void initupdate(String downloadUrl) {
        OkHttpUtils.get(downloadUrl)
                .tag(this)
                .execute(new FileCallback(AppUtils.getSDPath() + "/download", "i带货.apk") {
                    @Override
                    public void onResponse(boolean isFromCache, File file, Request request, @Nullable Response response) {
                        backgroundAlpha(1f);
                        AppUtils.toastBottom(StartActivity.this, "成功");
                        AppUtils.install(StartActivity.this, file.toString());
                    }
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        AppUtils.toastBottom(StartActivity.this, "失败");
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                        AppUtils.toastBottom(StartActivity.this, "进度" + StringUilts.doubleMoney(Double.parseDouble(String.valueOf(progress))*100)+"%");
                    }
                });

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pager_navigation_btn:
                Intent intent = new Intent();
                intent.setClass(this, LandingActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.update_version_button:
//                download(view);//状态栏更新
                initupdate(download_path);
                popupWindow.dismiss();
                break;
        }
    }

    /**
     * 计时器用于延时跳转
     */
    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程显示
        @Override
        public void onTick(long l) {
            activity_start_textView.setVisibility(View.VISIBLE);
        }

        //计时完毕操作
        @Override
        public void onFinish() {
            activity_start_textView.setVisibility(View.GONE);
            Intent intent = new Intent();
            //网络判断
            if (StringUilts.isNetworkAvailable(getApplication()) != Constants.ZERO) {
                //判断是否已登录
                if (!TextUtils.isEmpty(SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE, "")) && !TextUtils.isEmpty(SharePreferencesUlits.getString(getApplication(), CharConstants.PASSWORD, ""))) {
                    intent.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //初始化控件
                    initView();
                }
            } else {
                //初始化控件
                initView();
                Toast.makeText(getApplication(), "当前无网络链接", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
//        isForeground = true;
        super.onResume();
    }

    /**
     * 内部类，继承PagerAdapter，当然你也可以直接 new PageAdapter
     *
     * @author admin
     */
    class MViewPageAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private List<View> mViewList;

        public MViewPageAdapter(List<View> views) {
            mViewList = views;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position), 0);
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {

            switch (position) {
                case 0:
                    activity_start_radiobtn_one.setChecked(true);
                    break;
                case 1:
                    activity_start_radiobtn_two.setChecked(true);
                    break;
                case 2:
                    activity_start_radiobtn_three.setChecked(true);
                    break;
            }


        }

    }

    //自定义popup弹框
    private void initpopup(boolean flag,String str,String content) {
        popupWindow = new PopupWindow();
        view1 = LayoutInflater.from(this).inflate(R.layout.update_vserion, null);
        popupWindow.setContentView(view1);
        //定义popupwindows宽高
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setClippingEnabled(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        update_version_button = (Button) view1.findViewById(R.id.update_version_button);
        update_version_text_content = (TextView) view1.findViewById(R.id.update_version_text_content);
        update_version_text_title = (TextView) view1.findViewById(R.id.update_version_text_title);
        update_version_button.setOnClickListener(this);
        update_version_text_title.setText("最新版本:"+str);
        update_version_text_content.setText(content);
        if (flag) {
            popupWindow.showAtLocation(view1, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 背景变色
     *
     * @param v
     */
    private void backgroundAlpha(float v) {
        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.alpha = v;
        getWindow().setAttributes(windowManager);
    }

    /**
     * 状态栏更新apk
     */
    private void showDownloadSetting() {
        String packageName = "com.example.xl.foursling";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            startActivity(intent);
        }
    }

    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    private boolean canDownloadState() {
        try {
            int state = this.getPackageManager().getApplicationEnabledSetting("com.example.xl.foursling");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void download(View view) {
        if (!canDownloadState()) {
            Toast.makeText(this, "下载服务不用,请您启用", Toast.LENGTH_SHORT).show();
            showDownloadSetting();
            return;
        }
//        String url = null;
//        if (TextUtils.isEmpty(download_path)) {
//            url = download_path;
//        }
        ApkUpdateUtils.download(this, download_path, getResources().getString(R.string.app_name));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - finshApp) > 2000) {
                Toast.makeText(this, "再按一次后退键退出程序",
                        Toast.LENGTH_SHORT).show();
                finshApp = System.currentTimeMillis();
            } else {
                this.finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
