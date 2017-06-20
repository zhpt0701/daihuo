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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.activityes.LandingPassActivity;
import com.example.xl.foursling.adapter.setting.POpupAdapter;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.Security;
import com.example.xl.foursling.view.titlebar.TeaskBar;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 设置密保 身份验证
 * Created by admin on 2016/12/29.
 */

public class SecurityActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    @Bind(R.id.activity_security_select)
    Button activity_security_select;
    @Bind(R.id.activity_security_select_two)
    Button activity_security_select_two;
    @Bind(R.id.activity_security_select_three)
    Button activity_security_select_three;
    @Bind(R.id.activity_security_input_answer)
    EditText activity_security_input_answer;
    @Bind(R.id.activity_security_input_answer_two)
    EditText activity_security_input_answer_two;
    @Bind(R.id.activity_security_input_answer_three)
    EditText activity_security_input_answer_three;
    @Bind(R.id.activity_security_btn_next)
    Button activity_security_btn_next;
    @Bind(R.id.activity_security_linear_phone)
    LinearLayout activity_security_linear_phone;
    //问题二
    @Bind(R.id.activity_security_linear_security_two)
    LinearLayout activity_security_linear_security_two;
    //问题三
    @Bind(R.id.activity_security_linear_security_three)
    LinearLayout activity_security_linear_security_three;
    @Bind(R.id.activity_security_btn_phone)
    Button activity_security_btn_phone;
    //加载
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    private int state1;
    private PopupWindow popupWindowtime;
    private View view2;
    private String key = null;
    private HashMap<String,String> hashMap;
    private ListView popup_security_list;
    private POpupAdapter adapter;
    private List<String> list,list1;
    private String [] strs = {"1.你父亲的名字?","2.你母亲的名字?","3.你初恋的名字?","4.你的高中班主任的名字?",
            "5.你初中在哪一所学校读书?","6.你的车牌号是多少?","7.你的第一个宠物叫啥名字?","8.你配偶的名字?","9.你配偶的生日?"};
    private Security security;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        //绑定注解
        ButterKnife.bind(this);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        //初始化数据
        initData();
        // 初始化控件
        initView();
    }

    private void initView() {
        layout_title_back.setOnClickListener(this);
        activity_security_btn_next.setOnClickListener(this);
        activity_security_btn_phone.setOnClickListener(this);
        activity_security_select.setOnClickListener(this);
        activity_security_select_two.setOnClickListener(this);
        activity_security_select_three.setOnClickListener(this);
    }

    private void initData() {
        layout_title_textview.setText("密保验证");
        list1 = new ArrayList<>();
        list = new ArrayList<>();
        for (int i = 0;i<strs.length;i++){
            list.add(strs[i]);
        }
        hashMap = new HashMap<String,String>();
        state1 = Constants.ZERO;
        if (!TextUtils.isEmpty(getIntent().getStringExtra("security"))){
            state1 = Constants.ZERO;
            list.clear();
            security = GsonUtil.processJson(getIntent().getStringExtra("security"),Security.class);
            list1.add(strs[security.message.get(0).encryptedType1-1]);
            list.add(String.valueOf(security.message.get(0).encryptedType1));
            list1.add(strs[security.message.get(0).encryptedType2-1]);
            list.add(String.valueOf(security.message.get(0).encryptedType2));
            list1.add(strs[security.message.get(0).encryptedType3-1]);
            list.add(String.valueOf(security.message.get(0).encryptedType3));
            activity_security_linear_phone.setVisibility(View.VISIBLE);
            activity_security_select_two.setVisibility(View.GONE);
            activity_security_select_three.setVisibility(View.GONE);
            activity_security_select.setText("选择密保问题");
            activity_security_linear_security_three.setVisibility(View.GONE);
            activity_security_linear_security_two.setVisibility(View.GONE);
        }else {
            state1 = Constants.ONE;
            activity_security_linear_phone.setVisibility(View.GONE);
            activity_security_select_two.setVisibility(View.VISIBLE);
            activity_security_select_three.setVisibility(View.VISIBLE);
            activity_security_linear_security_three.setVisibility(View.VISIBLE);
            activity_security_linear_security_two.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.activity_security_btn_next:
                if (state1 == Constants.ONE){
                    if (!TextUtils.isEmpty(activity_security_input_answer.getText().toString())
                            &&!TextUtils.isEmpty(activity_security_input_answer_two.getText().toString())
                            &&!TextUtils.isEmpty(activity_security_input_answer_three.getText().toString())){
                        loading_linearlayout.setVisibility(View.VISIBLE);
                        //设置
                        httpsetting();
                    }else {
                        Toast.makeText(getApplication(),"请完整填写三个密保问题",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (!TextUtils.isEmpty(activity_security_input_answer.getText().toString())){
                        loading_linearlayout.setVisibility(View.VISIBLE);
                        //回答
                        httpanswer();
                    }else {
                        Toast.makeText(getApplication(),"请回答密保问题",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            //拨打电话
            case R.id.activity_security_btn_phone:
                Intent intent = new Intent();
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
            case R.id.activity_security_select:
                key = "encryptedType1";
                initPopupSex(true);
                backgroundAlpha(0.5f);
                break;
            case R.id.activity_security_select_two:
                key = "encryptedType2";
                initPopupSex(true);
                backgroundAlpha(0.5f);
                break;
            case R.id.activity_security_select_three:
                key = "encryptedType3";
                initPopupSex(true);
                backgroundAlpha(0.5f);
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
    //验证密保answer,Integer type,String userCode  验证密保问题
    private void httpanswer() {
        OkHttpUtils.post(Api.verift_security)
                .tag(this)
                .params("userCode", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .params("answer",activity_security_input_answer.getText().toString().trim())
                .params("type", hashMap.get("encryptedType1"))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                Intent intent = new Intent();
                                intent.setClass(getApplication(), LandingPassActivity.class);
                                if (Integer.valueOf(getIntent().getStringExtra("update_pass")) == Constants.ONE) {
                                    intent.putExtra("update_code", String.valueOf(Constants.THREE));
                                } else {
                                    intent.putExtra("update_code", String.valueOf(Constants.TWO));
                                }
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(getApplication(),new JSONObject(s).getString("message"),Toast.LENGTH_SHORT).show();
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
    //设置密保
    private void httpsetting() {
        OkHttpUtils.post(Api.setting_security)
                .tag(this)
                .params("userCode", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .params("answerOne",activity_security_input_answer.getText().toString().trim())
                .params("encryptedType1",hashMap.get("encryptedType1"))
                .params("answerTwo",activity_security_input_answer_two.getText().toString().trim())
                .params("encryptedType2",hashMap.get("encryptedType2"))
                .params("answerThree",activity_security_input_answer_three.getText().toString().trim())
                .params("encryptedType3",hashMap.get("encryptedType3"))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                Intent intent = new Intent();
                                intent.setClass(getApplication(), LandingPassActivity.class);
                                if (Integer.valueOf(getIntent().getStringExtra("update_pass")) == Constants.ONE) {
                                    intent.putExtra("update_code", String.valueOf(Constants.THREE));
                                } else {
                                    intent.putExtra("update_code", String.valueOf(Constants.TWO));
                                }
                                startActivity(intent);
                                finish();
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
    /**
     * 性别 图片 popup
     * @param b
     */
    private void initPopupSex(boolean b) {
        popupWindowtime = new PopupWindow();
        view2 = LayoutInflater.from(this).inflate(R.layout.popup_security_layout,null);
        popupWindowtime.setContentView(view2);
        popupWindowtime.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindowtime.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindowtime.setTouchable(true);
        popupWindowtime.setOutsideTouchable(true);
        popupWindowtime.setFocusable(true);
        popupWindowtime.setBackgroundDrawable(new BitmapDrawable());
        popup_security_list = (ListView)view2.findViewById(R.id.popup_security_list);
        if (state1 == Constants.ZERO){
            adapter = new POpupAdapter(getApplication(),list1);
        }else {
            adapter = new POpupAdapter(getApplication(),list);
        }
        popup_security_list.setAdapter(adapter);
        popup_security_list.setOnItemClickListener(this);
        popupWindowtime.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        if (b){
            popupWindowtime.showAtLocation(view2, Gravity.BOTTOM, 0, 0);
        }
    }
    //弹框改变背景颜色
    private void backgroundAlpha(float v) {
        WindowManager.LayoutParams ly = this.getWindow().getAttributes();
        ly.alpha = v ;
        getWindow().setAttributes(ly);
    }

    /**
     * item点击事件
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        popupWindowtime.dismiss();
        backgroundAlpha(1f);
        Iterator iter = hashMap.keySet().iterator();
        if (state1 == Constants.ZERO){
            if (!TextUtils.isEmpty(hashMap.get("encryptedType1"))){
                if (Integer.parseInt(hashMap.get("encryptedType1"))-1 != i){
                    activity_security_select.setText(list1.get(i));
                    hashMap.put(key,list.get(i));
                }else {
                    Toast.makeText(getApplication(),"不能选择重复的问题",Toast.LENGTH_SHORT).show();
                }
            }else {
                activity_security_select.setText(list1.get(i));
                hashMap.put(key,list.get(i));
            }
        }else {
            if (key.equals("encryptedType1")){
                if (hashMap.size()>0){
                    if (!hashMap.containsValue(String.valueOf(i+1))){
                        activity_security_select.setText(list.get(i));
                        hashMap.put(key,String.valueOf(i+1));
                    }else {
                        Toast.makeText(getApplication(),"不能选择重复的问题",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    activity_security_select.setText(list.get(i));
                    hashMap.put(key,String.valueOf(i+1));
                }
            }else if (key.equals("encryptedType2")){
                if (hashMap.size()>0){
                    if (!hashMap.containsValue(String.valueOf(i+1))){
                        activity_security_select_two.setText(list.get(i));
                        hashMap.put(key,String.valueOf(i+1));
                    }else {
                        Toast.makeText(getApplication(),"不能选择重复的问题",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    activity_security_select_two.setText(list.get(i));
                    hashMap.put(key,String.valueOf(i+1));
                }
            }else {
                if (hashMap.size()>0){
                    //判断当前值是否存在于hashMap中
                    if (!hashMap.containsValue(String.valueOf(i+1))){
                        activity_security_select_three.setText(list.get(i));
                        hashMap.put(key,String.valueOf(i+1));
                    }else {
                        Toast.makeText(getApplication(),"不能选择重复的问题",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    activity_security_select_three.setText(list.get(i));
                    hashMap.put(key,String.valueOf(i+1));
                }

            }

        }
    }
}
