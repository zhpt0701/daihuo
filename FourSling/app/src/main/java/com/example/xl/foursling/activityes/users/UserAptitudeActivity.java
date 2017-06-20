package com.example.xl.foursling.activityes.users;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.http.InternetImageview;
import com.example.xl.foursling.http.MyImageLoader;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.MyThreadPoolManager;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.tools.Tailoring;
import com.example.xl.foursling.unity.UserData;
import com.example.xl.foursling.view.titlebar.TeaskBar;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2016/12/16.
 */
public class UserAptitudeActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    @Bind(R.id.layout_title_btn)
    Button layout_title_btn;
    //支付宝账号
    @Bind(R.id.activity_user_aptitude_ed_alipay)
    EditText activity_user_aptitude_ed_alipay;
    //驾驶证
    @Bind(R.id.activity_user_aptitude_imagebutton_car_book)
    ImageButton activity_user_aptitude_imagebutton_car_book;
    //行驶证
    @Bind(R.id.activity_user_aptitude_imagebutton_car_isbook)
    ImageButton activity_user_aptitude_imagebutton_car_isbook;
    //手持身份证
    @Bind(R.id.activity_user_aptitude_imagebutton_ID)
    ImageButton activity_user_aptitude_imagebutton_ID;
    //车辆正面照
    @Bind(R.id.activity_user_aptitude_imagebutton_is_photo)
    ImageButton activity_user_aptitude_imagebutton_is_photo;
    //身份证正面
    @Bind(R.id.activity_user_aptitude_imagebutton_isID)
    ImageButton activity_user_aptitude_imagebutton_isID;
    //交强险
    @Bind(R.id.activity_user_aptitude_imagebutton_JQX)
    ImageButton activity_user_aptitude_imagebutton_JQX;
    //个人图像半身照
    @Bind(R.id.activity_user_aptitude_imagebutton_this_photo)
    ImageButton activity_user_aptitude_imagebutton_this_photo;
    //提交资料
    @Bind(R.id.activity_user_aptitude_btn_submit)
    Button activity_user_aptitude_btn_submit;
    //身份证
    @Bind(R.id.activity_user_aptitude_ed_ID)
    EditText activity_user_aptitude_ed_ID;
    //真实姓名
    @Bind(R.id.activity_user_aptitude_ed_username)
    EditText activity_user_aptitude_ed_username;
    //微信账号
    @Bind(R.id.activity_user_aptitude_account_ed_WX)
    EditText activity_user_aptitude_account_ed_WX;
    //车辆载重
    @Bind(R.id.activity_car_news_textview_car_weight_content)
    TextView activity_car_news_textview_car_weight_content;
    //配送车型
    @Bind(R.id.activity_user_aptitude_button_pei_song)
    Button activity_user_aptitude_button_pei_song;
    //车牌号
    @Bind(R.id.activity_car_ap_edittext_car_weight_car_number)
    EditText activity_car_ap_edittext_car_weight_car_number;
    //车辆品牌
    @Bind(R.id.activity_user_aptitude_edittext_brand)
    EditText activity_user_aptitude_edittext_brand;
    @Bind(R.id.loading_linearlayout_image)
    LinearLayout loading_linearlayout_image;
    private PopupWindow popupWindowSex;
    private Button popup_user_btn_one;
    private Button popup_user_btn_two;
    private Button popup_user_btn_three;
    private Bitmap bitmap;
    private int PHOTO_NUMBER = 0;
    private PopupWindow popupCarMoudel;
    private Button popup_user_btn_car_human_light;
    private Button popup_user_btn_car_human_middle_sized;
    private Button popup_user_btn_car_human_weight;
    private Button popup_user_btn_car_small;
    private Button popup_user_btn_car_light;
    private Button popup_user_btn_car_middle_sized;
    private Button popup_user_btn_car_weight;
    private Button popup_user_btn_car_moudel_cancel;
    private String path;
    private String up_path;
    private String key = null;
    private int car = 0;
    private HashMap<String ,String> hashMap = null;
    private int stures;
    private MyImageLoader myImageLoader;
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_aptitude);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        //注解绑定
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //初始化数据
        initData();
        //初始化控件
        initView();
    }

    private void initView() {
//
        //设置点击事件
        activity_user_aptitude_imagebutton_car_book.setOnClickListener(this);
        activity_user_aptitude_imagebutton_car_isbook.setOnClickListener(this);
        activity_user_aptitude_imagebutton_ID.setOnClickListener(this);
        activity_user_aptitude_imagebutton_is_photo.setOnClickListener(this);
        activity_user_aptitude_imagebutton_isID.setOnClickListener(this);
        activity_user_aptitude_imagebutton_JQX.setOnClickListener(this);
        activity_user_aptitude_imagebutton_this_photo.setOnClickListener(this);

        activity_user_aptitude_btn_submit.setOnClickListener(this);
        activity_user_aptitude_button_pei_song.setOnClickListener(this);
        layout_title_back.setOnClickListener(this);
        layout_title_btn.setOnClickListener(this);
    }

    private void initData() {
        decimalFormat = new DecimalFormat("0.00");
        myImageLoader = new MyImageLoader(getApplication());
        layout_title_textview.setText(getResources().getString(R.string.activity_user_aptitude_news));
        layout_title_btn.setText(getResources().getString(R.string.compile));
        if (!TextUtils.isEmpty(getIntent().getStringExtra("user_code"))){
            stures = Integer.parseInt(getIntent().getStringExtra("user_code"));
            Toast.makeText(getApplication(),"请点击编辑进行带货资质信息编辑",Toast.LENGTH_LONG).show();
            loading_linearlayout_image.setVisibility(View.VISIBLE);
            httpselectuser();
        }else {
            stures = Constants.NINE;
        }
        initEdit(stures);

        //控制edit无法输入
//        activity_user_aptitude_ed_username.setFocusable(false);
        //是否取消onclick事件
//        activity_user_aptitude_imagebutton_ID.setClickable(false);
        car = Constants.ZERO;
        hashMap = new HashMap<String, String>();
    }
    //查询个人信息
    private void httpselectuser() {
        OkHttpUtils.post(Api.select_user_data)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getApplicationContext(), CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                       loading_linearlayout_image.setVisibility(View.GONE);
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                UserData userData = GsonUtil.processJson(s,UserData.class);
                                Log.d("this_____0909090",s);
                                activity_user_aptitude_button_pei_song.setText(userData.message.get(0).carTypeName);
                                activity_car_news_textview_car_weight_content.setText(String.valueOf(decimalFormat.format((double)userData.message.get(0).carWeight/1000))+"kg");
                                activity_user_aptitude_account_ed_WX.setText(userData.message.get(0).wechatNO);
                                activity_user_aptitude_edittext_brand.setText(userData.message.get(0).carName);
                                activity_user_aptitude_ed_alipay.setText(userData.message.get(0).alipayNO);
                                activity_car_ap_edittext_car_weight_car_number.setText(userData.message.get(0).carCode);
                                activity_user_aptitude_ed_ID.setText(userData.message.get(0).idCard);
                                activity_user_aptitude_ed_username.setText(userData.message.get(0).userName);
                                myImageLoader.imageLoader.displayImage(userData.message.get(0).bust,activity_user_aptitude_imagebutton_this_photo,myImageLoader.options);
                                hashMap.put("bust",userData.message.get(0).bust);
                                myImageLoader.imageLoader.displayImage(userData.message.get(0).idCardPic,activity_user_aptitude_imagebutton_isID,myImageLoader.options);
                                hashMap.put("idCardPic",userData.message.get(0).idCardPic);
                                myImageLoader.imageLoader.displayImage(userData.message.get(0).handheldCardPic,activity_user_aptitude_imagebutton_ID,myImageLoader.options);
                                hashMap.put("handheldCardPic",userData.message.get(0).handheldCardPic);
                                myImageLoader.imageLoader.displayImage(userData.message.get(0).carPic,activity_user_aptitude_imagebutton_is_photo,myImageLoader.options);
                                hashMap.put("carPic",userData.message.get(0).carPic);
                                myImageLoader.imageLoader.displayImage(userData.message.get(0).drivalPic,activity_user_aptitude_imagebutton_car_book,myImageLoader.options);
                                hashMap.put("drivalPic",userData.message.get(0).drivalPic);
                                myImageLoader.imageLoader.displayImage(userData.message.get(0).tracelPic,activity_user_aptitude_imagebutton_car_isbook,myImageLoader.options);
                                hashMap.put("tracelPic",userData.message.get(0).tracelPic);
                                myImageLoader.imageLoader.displayImage(userData.message.get(0).insurancePic,activity_user_aptitude_imagebutton_JQX,myImageLoader.options);
                                hashMap.put("insurancePic",userData.message.get(0).insurancePic);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        loading_linearlayout_image.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * onclick 响应事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_title_back:
                 finish();
                break;
            case R.id.layout_title_btn:
                stures = Constants.NINE;
               initEdit(stures);
                break;
            //个人半身照
            case R.id.activity_user_aptitude_imagebutton_this_photo:
                if (stures == Constants.NINE){
                    key = "bust";
                    PHOTO_NUMBER = Constants.ONE;
                    initPopupPhoto(view,true);
                    backgroundAlpha(0.5f);
                }else {
                    Toast.makeText(getApplication(),"请点击编辑进行资料修改",Toast.LENGTH_SHORT).show();
                }
                break;
            //身份证正面
            case R.id.activity_user_aptitude_imagebutton_isID:
                if (stures == Constants.NINE){
                    key = "idCardPic";
                    PHOTO_NUMBER = Constants.TWO;
                    initPopupPhoto(view,true);
                    backgroundAlpha(0.5f);
                }else {
                    Toast.makeText(getApplication(),"请点击编辑进行资料修改",Toast.LENGTH_SHORT).show();
                }
                break;
            //手持身份证
            case R.id.activity_user_aptitude_imagebutton_ID:
                if (stures == Constants.NINE){
                    key = "handheldCardPic";
                    PHOTO_NUMBER = Constants.THREE;
                    initPopupPhoto(view,true);
                    backgroundAlpha(0.5f);
                }else {
                    Toast.makeText(getApplication(),"请点击编辑进行资料修改",Toast.LENGTH_SHORT).show();
                }
                break;
            //车辆正面照
            case R.id.activity_user_aptitude_imagebutton_is_photo:
                if (stures == Constants.NINE){
                    key = "carPic";
                    PHOTO_NUMBER = Constants.FOUR;
                    initPopupPhoto(view,true);
                    backgroundAlpha(0.5f);
                }else {
                    Toast.makeText(getApplication(),"请点击编辑进行资料修改",Toast.LENGTH_SHORT).show();
                }
                break;
            //驾驶证
            case R.id.activity_user_aptitude_imagebutton_car_book:
                if (stures == Constants.NINE){
                    key = "drivalPic";
                    PHOTO_NUMBER = Constants.FIVE;
                    initPopupPhoto(view,true);
                    backgroundAlpha(0.5f);
                }else {
                    Toast.makeText(getApplication(),"请点击编辑进行资料修改",Toast.LENGTH_SHORT).show();
                }
                break;
            //行驶证
            case R.id.activity_user_aptitude_imagebutton_car_isbook:
                if (stures == Constants.NINE){
                    key = "tracelPic";
                    PHOTO_NUMBER = Constants.SIX;
                    initPopupPhoto(view,true);
                    backgroundAlpha(0.5f);
                }else {
                    Toast.makeText(getApplication(),"请点击编辑进行资料修改",Toast.LENGTH_SHORT).show();
                }
                break;
            //交强险
            case R.id.activity_user_aptitude_imagebutton_JQX:
                if (stures == Constants.NINE){
                    key = "insurancePic";
                    PHOTO_NUMBER = Constants.SEVEN;
                    initPopupPhoto(view,true);
                    backgroundAlpha(0.5f);
                }else {
                    Toast.makeText(getApplication(),"请点击编辑进行资料修改",Toast.LENGTH_SHORT).show();
                }
                break;
            //配送车型
            case R.id.activity_user_aptitude_button_pei_song:
                if (stures == Constants.NINE){
                    initCarModel(view,true);
                    backgroundAlpha(0.5f);
                }else {
                    Toast.makeText(getApplication(),"请点击编辑进行资料修改",Toast.LENGTH_SHORT).show();
                }
                break;
            //提交数据
            case R.id.activity_user_aptitude_btn_submit:
                if (stures == Constants.NINE){
                    if (!TextUtils.isEmpty(activity_user_aptitude_ed_username.getText().toString())){
                        if (!TextUtils.isEmpty(activity_user_aptitude_ed_ID.getText().toString())&& StringUilts.isIdcard(activity_user_aptitude_ed_ID.getText().toString())){
                            if (!TextUtils.isEmpty(activity_user_aptitude_account_ed_WX.getText().toString())){
                                if (!TextUtils.isEmpty(activity_user_aptitude_ed_alipay.getText().toString())){
                                    if (!TextUtils.isEmpty(activity_user_aptitude_edittext_brand.getText().toString())){
                                        if (!TextUtils.isEmpty(activity_car_ap_edittext_car_weight_car_number.getText().toString())){
                                            if (StringUilts.StringCarNumber(activity_car_ap_edittext_car_weight_car_number.getText().toString().trim())){
                                                if (car != Constants.ZERO){
                                                    if (!TextUtils.isEmpty(hashMap.get("bust"))){
                                                        if (!TextUtils.isEmpty(hashMap.get("idCardPic"))){
                                                            if (!TextUtils.isEmpty(hashMap.get("handheldCardPic"))){
                                                                if (!TextUtils.isEmpty(hashMap.get("carPic"))){
                                                                    if (!TextUtils.isEmpty(hashMap.get("drivalPic"))){
                                                                        if (!TextUtils.isEmpty(hashMap.get("tracelPic"))){
                                                                          if (!TextUtils.isEmpty(hashMap.get("insurancePic"))){
                                                                              stures = Constants.ONE;
                                                                              httpuserData();
                                                                              loading_linearlayout_image.setVisibility(View.VISIBLE);
                                                                          }else {
                                                                              Toast.makeText(getApplication(),"交强险照片上传失败,请重新选择照片",Toast.LENGTH_SHORT).show();
                                                                          }
                                                                        }else {
                                                                            Toast.makeText(getApplication(),"行驶证照片上传失败,请重新选择照片",Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }else {
                                                                        Toast.makeText(getApplication(),"驾驶证照片上传失败,请重新选择照片",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }else {
                                                                    Toast.makeText(getApplication(),"车辆正面照片上传失败,请重新选择照片",Toast.LENGTH_SHORT).show();
                                                                }
                                                            }else {
                                                                Toast.makeText(getApplication(),"手持身份证照片上传失败,请选重新选择照片",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }else {
                                                            Toast.makeText(getApplication(),"身份证正面照片上传失败,请重新选择照片",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }else {
                                                        Toast.makeText(getApplication(),"个人半身照片上传失败,请重新选择照片",Toast.LENGTH_SHORT).show();
                                                    }
                                                }else {
                                                    Toast.makeText(getApplication(),"请选择车型",Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(getApplication(),"车牌号输入不正确",Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(getApplication(),"请输入车牌号",Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(getApplication(),"请输入车辆名称",Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(getApplication(),"请输入支付宝号",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getApplication(),"请输入微信号",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplication(),"您输入18的身份证号码不存在",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplication(),"请输入真实姓名",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplication(),"请点击编辑进行资料修改",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.popup_user_btn_car_human_light:
                car = Constants.ONE;
                activity_user_aptitude_button_pei_song.setText(popup_user_btn_car_human_light.getText().toString());
                activity_car_news_textview_car_weight_content.setText("100kg");
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_car_human_middle_sized:
                car = Constants.TWO;
                activity_user_aptitude_button_pei_song.setText(popup_user_btn_car_human_middle_sized.getText().toString());
                activity_car_news_textview_car_weight_content.setText("200kg");
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_car_human_weight:
                car = Constants.THREE;
                activity_user_aptitude_button_pei_song.setText(popup_user_btn_car_human_weight.getText().toString());
                activity_car_news_textview_car_weight_content.setText("300kg");
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_car_small:
                car = Constants.FOUR;
                activity_user_aptitude_button_pei_song.setText(popup_user_btn_car_small.getText().toString());
                activity_car_news_textview_car_weight_content.setText("10000kg");
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_car_light:
                car = Constants.FIVE;
                activity_user_aptitude_button_pei_song.setText(popup_user_btn_car_light.getText().toString());
                activity_car_news_textview_car_weight_content.setText("20000kg");
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_car_middle_sized:
                car = Constants.SIX;
                activity_user_aptitude_button_pei_song.setText(popup_user_btn_car_middle_sized.getText().toString());
                activity_car_news_textview_car_weight_content.setText("20000kg");
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_car_weight:
                car = Constants.SEVEN;
                activity_user_aptitude_button_pei_song.setText(popup_user_btn_car_weight.getText().toString());
                activity_car_news_textview_car_weight_content.setText("100000kg");
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_car_moudel_cancel:
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_one:
                startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), Constants.ONE);
                popupWindowSex.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_two:
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");//相片类型
                startActivityForResult(intent1, Constants.TWO);
                popupWindowSex.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_three:
                popupWindowSex.dismiss();
                backgroundAlpha(1f);
                break;
        }
    }
    //初始化edit是否可编辑
    private void initEdit(int i) {
        if (i == Constants.NINE){
            //设置edittext可编辑
            activity_user_aptitude_button_pei_song.setTextColor(getResources().getColor(R.color.colorBlackFour));
            activity_car_news_textview_car_weight_content.setTextColor(getResources().getColor(R.color.colorBlackFour));

            activity_user_aptitude_ed_ID.setTextColor(getResources().getColor(R.color.colorBlackFour));
            activity_user_aptitude_ed_ID.setFocusable(true);
            activity_user_aptitude_ed_ID.setFocusableInTouchMode(true);
            activity_user_aptitude_ed_ID.requestFocus();
            activity_user_aptitude_ed_ID.findFocus();

            activity_user_aptitude_ed_username.setTextColor(getResources().getColor(R.color.colorBlackFour));
            activity_user_aptitude_ed_username.setFocusable(true);
            activity_user_aptitude_ed_username.setFocusableInTouchMode(true);
            activity_user_aptitude_ed_username.requestFocus();
            activity_user_aptitude_ed_username.findFocus();

            activity_car_ap_edittext_car_weight_car_number.setTextColor(getResources().getColor(R.color.colorBlackFour));
            activity_car_ap_edittext_car_weight_car_number.setFocusable(true);
            activity_car_ap_edittext_car_weight_car_number.setFocusableInTouchMode(true);
            activity_car_ap_edittext_car_weight_car_number.requestFocus();
            activity_car_ap_edittext_car_weight_car_number.findFocus();

            activity_user_aptitude_account_ed_WX.setTextColor(getResources().getColor(R.color.colorBlackFour));
            activity_user_aptitude_account_ed_WX.setFocusable(true);
            activity_user_aptitude_account_ed_WX.setFocusableInTouchMode(true);
            activity_user_aptitude_account_ed_WX.requestFocus();
            activity_user_aptitude_account_ed_WX.findFocus();

            activity_user_aptitude_ed_alipay.setTextColor(getResources().getColor(R.color.colorBlackFour));
            activity_user_aptitude_ed_alipay.setFocusable(true);
            activity_user_aptitude_ed_alipay.setFocusableInTouchMode(true);
            activity_user_aptitude_ed_alipay.requestFocus();
            activity_user_aptitude_ed_alipay.findFocus();

            activity_user_aptitude_edittext_brand.setTextColor(getResources().getColor(R.color.colorBlackFour));
            activity_user_aptitude_edittext_brand.setFocusable(true);
            activity_user_aptitude_edittext_brand.setFocusableInTouchMode(true);
            activity_user_aptitude_edittext_brand.requestFocus();
            activity_user_aptitude_edittext_brand.findFocus();
        }else {
            //控制edit无法输入
            activity_user_aptitude_button_pei_song.setTextColor(getResources().getColor(R.color.colorGrayText));
            activity_car_news_textview_car_weight_content.setTextColor(getResources().getColor(R.color.colorGrayText));
            activity_user_aptitude_ed_ID.setTextColor(getResources().getColor(R.color.colorGrayText));
            activity_user_aptitude_ed_ID.setFocusable(false);

            activity_user_aptitude_ed_username.setTextColor(getResources().getColor(R.color.colorGrayText));
            activity_user_aptitude_ed_username.setFocusable(false);

            activity_car_ap_edittext_car_weight_car_number.setTextColor(getResources().getColor(R.color.colorGrayText));
            activity_car_ap_edittext_car_weight_car_number.setFocusable(false);

            activity_user_aptitude_account_ed_WX.setTextColor(getResources().getColor(R.color.colorGrayText));
            activity_user_aptitude_account_ed_WX.setFocusable(false);

            activity_user_aptitude_ed_alipay.setTextColor(getResources().getColor(R.color.colorGrayText));
            activity_user_aptitude_ed_alipay.setFocusable(false);

            activity_user_aptitude_edittext_brand.setTextColor(getResources().getColor(R.color.colorGrayText));
            activity_user_aptitude_edittext_brand.setFocusable(false);
        }
    }

    /**
     * 选择车型
     * @param view
     * @param b
     */
    private void initCarModel(View view, boolean b) {
        popupCarMoudel = new PopupWindow();
        View view2 = LayoutInflater.from(this).inflate(R.layout.popup_user_car_moudel,null);
        popupCarMoudel.setContentView(view2);
        popupCarMoudel.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupCarMoudel.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupCarMoudel.setTouchable(true);
        popupCarMoudel.setOutsideTouchable(true);
        popupCarMoudel.setFocusable(true);
        popupCarMoudel.setBackgroundDrawable(new BitmapDrawable());
        popup_user_btn_car_human_light = (Button) view2.findViewById(R.id.popup_user_btn_car_human_light);
        popup_user_btn_car_human_middle_sized = (Button) view2.findViewById(R.id.popup_user_btn_car_human_middle_sized);
        popup_user_btn_car_human_weight = (Button) view2.findViewById(R.id.popup_user_btn_car_human_weight);
        popup_user_btn_car_small = (Button) view2.findViewById(R.id.popup_user_btn_car_small);
        popup_user_btn_car_light = (Button) view2.findViewById(R.id.popup_user_btn_car_light);
        popup_user_btn_car_middle_sized = (Button) view2.findViewById(R.id.popup_user_btn_car_middle_sized);
        popup_user_btn_car_weight = (Button) view2.findViewById(R.id.popup_user_btn_car_weight);
        popup_user_btn_car_moudel_cancel = (Button) view2.findViewById(R.id.popup_user_btn_car_moudel_cancel);
        popup_user_btn_car_human_light.setOnClickListener(this);
        popup_user_btn_car_human_middle_sized.setOnClickListener(this);
        popup_user_btn_car_human_weight.setOnClickListener(this);
        popup_user_btn_car_small.setOnClickListener(this);
        popup_user_btn_car_light.setOnClickListener(this);
        popup_user_btn_car_middle_sized.setOnClickListener(this);
        popup_user_btn_car_weight.setOnClickListener(this);
        popup_user_btn_car_moudel_cancel.setOnClickListener(this);
        popupCarMoudel.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        if (b){
            popupCarMoudel.showAtLocation(view2, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 图片 popup
     * @param v
     * @param b
     */
    private void initPopupPhoto(View v, boolean b) {
        popupWindowSex = new PopupWindow();
        View view2 = LayoutInflater.from(this).inflate(R.layout.popup_user_sex,null);
        popupWindowSex.setContentView(view2);
        popupWindowSex.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindowSex.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindowSex.setTouchable(true);
        popupWindowSex.setOutsideTouchable(true);
        popupWindowSex.setFocusable(true);
        popupWindowSex.setBackgroundDrawable(new BitmapDrawable());
        popup_user_btn_one = (Button) view2.findViewById(R.id.popup_user_btn_one);
        popup_user_btn_two = (Button) view2.findViewById(R.id.popup_user_btn_two);
        popup_user_btn_three = (Button) view2.findViewById(R.id.popup_user_btn_three);
        popup_user_btn_one.setText("相机");
        popup_user_btn_two.setText("相册");
        popup_user_btn_one.setOnClickListener(this);
        popup_user_btn_two.setOnClickListener(this);
        popup_user_btn_three.setOnClickListener(this);
        popupWindowSex.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        if (b){
            popupWindowSex.showAtLocation(view2, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * popup弹框app背景变暗
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (data != null){
                if (requestCode == Constants.ONE){
                    if (data.getExtras()!=null){
                        Bitmap bm = (Bitmap) data.getExtras().get("data");
                        initbit(bm);
                    }
                }else if (requestCode == Constants.TWO){
                    if (data.getData()!=null){
                        Uri uri = data.getData();
                        if(uri==null){
                            //use bundle to get data
                            Bundle bundle = data.getExtras();
                            if (bundle!=null) {
                                Bitmap  photo = (Bitmap) bundle.get(uri.toString());
                                initbit(photo);
                            } else {
                                return;
                            }
                        }else{
                            ContentResolver resolver = getContentResolver();
                            Bitmap bm = null;
                            try {
                                bm = MediaStore.Images.Media.getBitmap(resolver, uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            initbit(bm);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //图片处理
    private void initbit(final Bitmap bitmap) {
        switch (PHOTO_NUMBER){
            case 1:
                activity_user_aptitude_imagebutton_this_photo.setImageBitmap(bitmap);
                break;
            case 2:
                activity_user_aptitude_imagebutton_isID.setImageBitmap(bitmap);
                break;
            case 3:
                activity_user_aptitude_imagebutton_ID.setImageBitmap(bitmap);
                break;
            case 4:
                activity_user_aptitude_imagebutton_is_photo.setImageBitmap(bitmap);
                break;
            case 5:
                activity_user_aptitude_imagebutton_car_book.setImageBitmap(bitmap);
                break;
            case 6:
                activity_user_aptitude_imagebutton_car_isbook.setImageBitmap(bitmap);
                break;
            case 7:
                activity_user_aptitude_imagebutton_JQX.setImageBitmap(bitmap);
                break;

        }
        this.bitmap = bitmap;
        if (bitmap != null){
            MyThreadPoolManager.getInstance().execute(runnable);
            loading_linearlayout_image.setVisibility(View.VISIBLE);
        }
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            path = Tailoring.ziphoto(bitmap);
            up_path = InternetImageview.asyncPutObjectWithMD5Verify(CharConstants.BUCKRT, CharConstants.FILE_NAME+"/"+SharePreferencesUlits.getString(getApplication(),CharConstants.PHONE,"")+"/"+System.currentTimeMillis()+".png", path);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("path", up_path);
            message.what = Constants.ONE;
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };
    //界面刷新
    public Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    hashMap.put(key,msg.getData().getString("path"));
                    loading_linearlayout_image.setVisibility(View.GONE);
                    break;
            }
        }
    };
    //请求数据
    public void httpuserData(){
        OkHttpUtils.post(Api.add_this_news)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .params("carName",activity_user_aptitude_edittext_brand.getText().toString())
                .params("carTypeCode",String.valueOf(car))
                .params("carCode",activity_car_ap_edittext_car_weight_car_number.getText().toString())
                .params("wechatNo",activity_user_aptitude_account_ed_WX.getText().toString())
                .params("alipayNo",activity_user_aptitude_ed_alipay.getText().toString())
                .params("idCard",activity_user_aptitude_ed_ID.getText().toString())
                .params("userName",activity_user_aptitude_ed_username.getText().toString())
                .params("insurancePic",hashMap.get("insurancePic"))
                .params("tracelPic",hashMap.get("tracelPic"))
                .params("carPic",hashMap.get("carPic"))
                .params("bust",hashMap.get("bust"))
                .params("drivalPic",hashMap.get("drivalPic"))
                .params("handheldCardPic",hashMap.get("handheldCardPic"))
                .params("idCardPic",hashMap.get("idCardPic"))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout_image.setVisibility(View.GONE);
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                stures = Constants.ONE;
                                initEdit(stures);
                                SharePreferencesUlits.saveString(getApplication(), CharConstants.ALIPAY_NO,activity_user_aptitude_ed_alipay.getText().toString());
                                SharePreferencesUlits.saveString(getApplication(), CharConstants.WECHATNO,activity_user_aptitude_account_ed_WX.getText().toString());
                                finish();
                            }else {
                                stures = Constants.NINE;
                                initEdit(stures);
                            }
                            if (!TextUtils.isEmpty(new JSONObject(s).getString("message"))){
                                Toast.makeText(getApplication(),new JSONObject(s).getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        initEdit(Constants.NINE);
                        Toast.makeText(getApplication(),"服务异常",Toast.LENGTH_SHORT).show();
                        loading_linearlayout_image.setVisibility(View.GONE);
                    }
                });
    }
}


