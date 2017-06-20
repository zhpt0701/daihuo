package com.example.xl.foursling.fragments;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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

import com.example.xl.foursling.MainActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.activityes.LandingActivity;
import com.example.xl.foursling.activityes.settting.AboutActivity;
import com.example.xl.foursling.activityes.settting.SafetyActivity;
import com.example.xl.foursling.activityes.settting.SecurityActivity;
import com.example.xl.foursling.activityes.settting.SecurityStateActivity;
import com.example.xl.foursling.activityes.users.UserAptitudeActivity;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.IdcardValidator;
import com.example.xl.foursling.tools.LogUtil;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.tools.VibratorUtil;
import com.example.xl.foursling.view.SwicthView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.xl.foursling.R.id.btn_popup_error;
import static com.example.xl.foursling.R.id.fragment_route_radiobutton_fished;
import static com.example.xl.foursling.R.id.loading_linearlayout;
import static com.example.xl.foursling.R.id.popup_setting_cancel_btn;
import static com.example.xl.foursling.R.id.popup_user_btn_one;
import static com.example.xl.foursling.R.id.popup_user_btn_three;
import static com.example.xl.foursling.R.id.popup_user_btn_two;
import static com.example.xl.foursling.R.id.swipe;

/**
 * Created by xl on 2016/12/15.
 */

public class SettingFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    /**
     * 控件
     * @param button
     * @param swicthView
     * @param
     * @return
     */
    @Bind(R.id.fragment_setting_menu)
    Button fragment_setting_menu;
    @Bind(R.id.fragment_setting_btn_about)
    Button fragment_setting_btn_about;
    @Bind(R.id.fragment_setting_btn_out)
    Button fragment_setting_btn_out;
    @Bind(R.id.fragment_setting_btn_safety)
    Button fragment_setting_btn_safety;
    @Bind(R.id.fragment_setting_btn_warning)
    Button fragment_setting_btn_warning;
    @Bind(R.id.fragment_radiogroup_on_off)
    RadioGroup fragment_radiogroup_on_off;
    @Bind(R.id.fragment_setting_radiobutton_on)
    RadioButton fragment_setting_radiobutton_on;
    @Bind(R.id.fragment_setting_radiobutton_off)
    RadioButton fragment_setting_radiobutton_off;
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    private boolean isflag;
    private PopupWindow popupWindow;
    private View view1,view,view2;
    private Button popup_setting_cancel_btn;
    private Button popup_setting_ok_btn;
    private TextView popup_setting_textview;
    private PopupWindow popupWindowtime;
    private Button popup_setting_setting_btn_five;
    private Button popup_setting_setting_btn_ten;
    private Button popup_setting_setting_btn_fifteen;
    private Button popup_setting_setting_btn_on;
    private Button popup_setting_setting_btn_cancel;
    private TextView popup_setting_textview_content;
    private int state;
    private LinearLayout activity_security_linear_id;
    private EditText activity_security_input_id;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting,container,false);
        //绑定注解
        ButterKnife.bind(this,view);
        //初始化数据
        initData(view);
        //初始化控件
        initView(view);
        return view;
    }

    private void initView(View view) {
        fragment_radiogroup_on_off = (RadioGroup) view.findViewById(R.id.fragment_radiogroup_on_off);
        fragment_setting_btn_out.setOnClickListener(this);
        fragment_setting_btn_safety.setOnClickListener(this);
        fragment_setting_btn_warning.setOnClickListener(this);
        fragment_setting_menu.setOnClickListener(this);
        fragment_setting_btn_about.setOnClickListener(this);
        fragment_radiogroup_on_off.setOnCheckedChangeListener(this);
    }

    private void initData(View view) {
        String content = "[\\u4e00-\\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}";
    }

    @Override
    public void onStart() {
        super.onStart();
        //是否打开语音播报
        switch (SharePreferencesUlits.getinteger(getActivity(),CharConstants.MEDIA,0)){
            case 5:
                fragment_setting_btn_warning.setText("每5秒提醒一次");
                break;
            case 10:
                fragment_setting_btn_warning.setText("每10秒提醒一次");
                break;
            case 15:
                fragment_setting_btn_warning.setText("每15秒提醒一次");
                break;
            default:
                fragment_setting_btn_warning.setText("关闭提醒");
                break;
        }
        //是否打开震动
        if (SharePreferencesUlits.getinteger(getActivity(),CharConstants.SHAKE,0) == Constants.ONE){
            fragment_setting_radiobutton_off.setChecked(true);
            fragment_radiogroup_on_off.setBackgroundResource(R.drawable.fragment_radiogroup_back_one);
        }else {
            fragment_setting_radiobutton_on.setChecked(true);
            fragment_radiogroup_on_off.setBackgroundResource(R.drawable.fragment_radiogroup_back);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            //打开菜单
            case R.id.fragment_setting_menu:
                MainActivity.menu.showSecondaryMenu(true);
                break;
            //关于
            case R.id.fragment_setting_btn_about:
                intent.setClass(getActivity(),AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_setting_btn_safety:
                intent.setClass(getActivity(), SafetyActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_setting_btn_warning:
                initPopupSex(true);
                backgroundAlpha(0.5f);
                break;
            //退出登录
            case R.id.fragment_setting_btn_out:
                initpopup(Constants.ONE,true);
                backgroundAlpha(0.5f);
                break;
            case R.id.popup_setting_cancel_btn:
                popupWindow.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_setting_ok_btn:
                popupWindow.dismiss();
                backgroundAlpha(1f);
                loading_linearlayout.setVisibility(View.VISIBLE);
                httpCancel();
                break;
            case R.id.popup_setting_setting_btn_five:
                fragment_setting_btn_warning.setText("每5秒提醒一次");
                SharePreferencesUlits.saveInt(getActivity(),CharConstants.MEDIA,5);
                popupWindowtime.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_setting_setting_btn_ten:
                fragment_setting_btn_warning.setText("每10秒提醒一次");
                SharePreferencesUlits.saveInt(getActivity(),CharConstants.MEDIA,10);
                popupWindowtime.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_setting_setting_btn_fifteen:
                fragment_setting_btn_warning.setText("每15秒提醒一次");
                SharePreferencesUlits.saveInt(getActivity(),CharConstants.MEDIA,15);
                popupWindowtime.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_setting_setting_btn_on:
                fragment_setting_btn_warning.setText("关闭提醒");
                SharePreferencesUlits.saveInt(getActivity(),CharConstants.MEDIA,0);
                popupWindowtime.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_setting_setting_btn_cancel:
                popupWindowtime.dismiss();
                backgroundAlpha(1f);
                break;
        }
    }
    //注销账号
    private void httpCancel() {
        OkHttpUtils.post(Api.cancel_user)
                .tag(this)
                .params("phone",SharePreferencesUlits.getString(getActivity(),CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                       loading_linearlayout.setVisibility(View.GONE);
                        try {
                            Toast.makeText(getActivity(),new JSONObject(s).getString("message"),Toast.LENGTH_SHORT).show();
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                Intent intent = new Intent();
                                //退出登录时清除SharePreferences存储的数据
                                SharePreferencesUlits.dataclear(getActivity());
                                intent.setClass(getActivity(), LandingActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        if (StringUilts.isNetworkAvailable(getActivity()) != Constants.ZERO){
                            Toast.makeText(getActivity(),"服务异常",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getActivity(),"无网络",Toast.LENGTH_LONG).show();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }
    //自定义popup弹框
    private void initpopup(int state,boolean flag) {
        popupWindow = new PopupWindow();
        view1 = LayoutInflater.from(getActivity()).inflate(R.layout.popup_setting_back,null);
        popupWindow.setContentView(view1);
        //定义popupwindows宽高
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setClippingEnabled(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popup_setting_cancel_btn = (Button) view1.findViewById(R.id.popup_setting_cancel_btn);
        activity_security_linear_id = (LinearLayout)view1.findViewById(R.id.activity_security_linear_ID);
        activity_security_input_id = (EditText)view1.findViewById(R.id.activity_security_input_ID);
        popup_setting_ok_btn = (Button) view1.findViewById(R.id.popup_setting_ok_btn);
        popup_setting_textview = (TextView) view1.findViewById(R.id.popup_setting_textview);
        popup_setting_textview_content = (TextView)view1.findViewById(R.id.popup_setting_textview_content);
        switch (state){
            case 1:
                this.state = state;
                activity_security_linear_id.setVisibility(View.GONE);
                popup_setting_textview.setText("温馨提示");
                popup_setting_textview_content.setText("是否注销当前登录");
                break;
            case 2:
                this.state = state;
                popup_setting_textview.setText("身份验证");
                popup_setting_textview_content.setVisibility(View.GONE);
                activity_security_linear_id.setVisibility(View.VISIBLE);
                this.state = state;
                break;
            case 3:
                this.state = state;
                activity_security_linear_id.setVisibility(View.GONE);
                popup_setting_textview.setText("温馨提示");
                popup_setting_textview_content.setText("您还未完善信息，请先完善信息");
                break;
        }
        TextPaint textPaint = popup_setting_textview.getPaint();
        textPaint.setFakeBoldText(true);
        popup_setting_cancel_btn.setOnClickListener(this);
        popup_setting_ok_btn.setOnClickListener(this);
        //设置点击空白消失
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        if (flag){
            popupWindow.showAtLocation(view1, Gravity.CENTER,0,0);
        }
    }
    /**
     * 语音时间popup
     * @param b
     */
    private void initPopupSex(boolean b) {
        popupWindowtime = new PopupWindow();
        view2 = LayoutInflater.from(getActivity()).inflate(R.layout.popup_setting_time,null);
        popupWindowtime.setContentView(view2);
        popupWindowtime.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindowtime.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindowtime.setTouchable(true);
        popupWindowtime.setOutsideTouchable(true);
        popupWindowtime.setFocusable(true);
        popupWindowtime.setBackgroundDrawable(new BitmapDrawable());
        popup_setting_setting_btn_five = (Button)view2.findViewById(R.id.popup_setting_setting_btn_five);
        popup_setting_setting_btn_ten = (Button)view2.findViewById(R.id.popup_setting_setting_btn_ten);
        popup_setting_setting_btn_fifteen = (Button)view2.findViewById(R.id.popup_setting_setting_btn_fifteen);
        popup_setting_setting_btn_on = (Button)view2.findViewById(R.id.popup_setting_setting_btn_on);
        popup_setting_setting_btn_cancel = (Button)view2.findViewById(R.id.popup_setting_setting_btn_cancel);
        popup_setting_setting_btn_five.setOnClickListener(this);
        popup_setting_setting_btn_ten.setOnClickListener(this);
        popup_setting_setting_btn_fifteen.setOnClickListener(this);
        popup_setting_setting_btn_on.setOnClickListener(this);
        popup_setting_setting_btn_cancel.setOnClickListener(this);
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
    /**
     * popup弹框app背景变暗
     * @param v
     */
    private void backgroundAlpha(float v) {
        WindowManager.LayoutParams ly = getActivity().getWindow().getAttributes();
        ly.alpha = v;
        getActivity().getWindow().setAttributes(ly);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.fragment_setting_radiobutton_on:
                SharePreferencesUlits.saveInt(getActivity(),CharConstants.SHAKE,0);
                fragment_radiogroup_on_off.setBackgroundResource(R.drawable.fragment_radiogroup_back);
                break;
            case R.id.fragment_setting_radiobutton_off:
                SharePreferencesUlits.saveInt(getActivity(),CharConstants.SHAKE,1);
                fragment_radiogroup_on_off.setBackgroundResource(R.drawable.fragment_radiogroup_back_one);
                break;
        }
    }
}
