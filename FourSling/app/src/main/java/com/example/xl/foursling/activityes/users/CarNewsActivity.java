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
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.MyThreadPoolManager;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.tools.Tailoring;
import com.example.xl.foursling.view.titlebar.TeaskBar;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
public class CarNewsActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 注解控件
     * @pram button
     * @pram textView
     * @pram editText
     * @pram imageutton
     */
    @Bind(R.id.activity_car_news_btn_back)
    Button activity_car_news_btn_back;
    @Bind(R.id.activity_car_news_btn_submit)
    Button activity_car_news_btn_submit;
    @Bind(R.id.activity_car_news_button_pei_song)
    Button activity_car_news_button_pei_song;
    @Bind(R.id.activity_car_news_imagebutton_car_book)
    ImageButton activity_car_news_imagebutton_car_book;
    @Bind(R.id.activity_car_news_imagebutton_is_photo)
    ImageButton activity_car_news_imagebutton_is_photo;
    @Bind(R.id.activity_car_news_textview_car_weight_this)
    TextView activity_car_news_textview_car_weight_this;
    @Bind(R.id.activity_car_news_edittext_brand)
    EditText activity_car_news_edittext_brand;
    @Bind(R.id.activity_car_news_edittext_car_weight_car_number)
    EditText activity_car_news_edittext_car_weight_car_number;
    @Bind(R.id.loading_linearlayout_image)
    LinearLayout loading_linearlayout_image;
    private PopupWindow popupWindowSex;
    private Button popup_user_btn_one;
    private Button popup_user_btn_two;
    private Button popup_user_btn_three;
//    private Bitmap bitmap;
    //设置常量
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
    private String key = null;
    private HashMap<String,String> hashMap = null;
    private int car = 0;
    private Bitmap bitmap;
    private int stutes;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_acr_news);
        //初始化状态栏
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //绑定注解
        ButterKnife.bind(this);
        //初始化数据
        initData();
        //初始化控件
        initView();

    }
    private void initView() {
        //设置onclick监听
        activity_car_news_btn_back.setOnClickListener(this);
        activity_car_news_btn_submit.setOnClickListener(this);
        activity_car_news_button_pei_song.setOnClickListener(this);
        activity_car_news_imagebutton_car_book.setOnClickListener(this);
        activity_car_news_imagebutton_is_photo.setOnClickListener(this);
    }
    private void initData() {
        stutes = Constants.ZERO;
        car = 0;
        hashMap = new HashMap<>();
//        activity_car_news_edittext_car_weight_car_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (b) {
//                    Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
//                    Matcher m=p.matcher(activity_car_news_edittext_car_weight_car_number.getText().toString().substring(0,1));
//                    if(m.matches()){
//                        Toast.makeText(getApplication(),"输入的是汉字", Toast.LENGTH_SHORT).show();
//                    }else {
//                        activity_car_news_edittext_car_weight_car_number.getText().clear();
//                    }
//                    p=Pattern.compile("[A-Z]");
//                    m=p.matcher(activity_car_news_edittext_car_weight_car_number.getText().toString().substring(1,2));
//                    if(m.matches()){
//                        Toast.makeText(getApplication(),"输入的是大写字母", Toast.LENGTH_SHORT).show();
//                    }
//                    p=Pattern.compile("[0-9]");
//                    m=p.matcher(activity_car_news_edittext_car_weight_car_number.getText().toString().substring(2,activity_car_news_edittext_car_weight_car_number.getText().toString().length()));
//                    if(m.matches()){
//                        Toast.makeText(getApplication(),"输入的是数字", Toast.LENGTH_SHORT).show();
//                    }
//                    // 此处为得到焦点时的处理内容
//                } else {
//                    // 此处为失去焦点时的处理内容
//                }
//            }
//        });
    }
    /**
     * onclick响应事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //返回
            case R.id.activity_car_news_btn_back:
                finish();
                break;
            //车辆
            case R.id.activity_car_news_button_pei_song:
                initCarModel(view,true);
                backgroundAlpha(0.5f);
                break;
            case R.id.popup_user_btn_car_human_light:
                car = Constants.ONE;
                Log.d("car",car+"-=-=-=-=");
                activity_car_news_button_pei_song.setText(popup_user_btn_car_human_light.getText().toString());
                activity_car_news_textview_car_weight_this.setText("100kg");
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_car_human_middle_sized:
                car = Constants.TWO;
                activity_car_news_button_pei_song.setText(popup_user_btn_car_human_middle_sized.getText().toString());
                activity_car_news_textview_car_weight_this.setText("200kg");
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_car_human_weight:
                car = Constants.THREE;
                activity_car_news_button_pei_song.setText(popup_user_btn_car_human_weight.getText().toString());
                activity_car_news_textview_car_weight_this.setText("300kg");
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_car_small:
                car = Constants.FOUR;
                activity_car_news_button_pei_song.setText(popup_user_btn_car_small.getText().toString());
                activity_car_news_textview_car_weight_this.setText("10000kg");
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_car_light:
                car = Constants.FIVE;
                activity_car_news_button_pei_song.setText(popup_user_btn_car_light.getText().toString());
                activity_car_news_textview_car_weight_this.setText("20000kg");
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_car_middle_sized:
                car = Constants.SIX;
                activity_car_news_button_pei_song.setText(popup_user_btn_car_middle_sized.getText().toString());
                activity_car_news_textview_car_weight_this.setText("50000kg");
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_car_weight:
                car = Constants.SEVEN;
                activity_car_news_button_pei_song.setText(popup_user_btn_car_weight.getText().toString());
                activity_car_news_textview_car_weight_this.setText("100000kg");
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_car_moudel_cancel:
                popupCarMoudel.dismiss();
                backgroundAlpha(1f);
                break;
            //图片
            case R.id.activity_car_news_imagebutton_is_photo:
                key = "carPic";
                PHOTO_NUMBER = Constants.ONE;
                backgroundAlpha(0.5f);
                initPopupPhoto(view,true);
                break;
            case R.id.activity_car_news_imagebutton_car_book:
                key = "tracelPic";
                PHOTO_NUMBER = Constants.TWO;
                backgroundAlpha(0.5f);
                initPopupPhoto(view,true);
                break;
            //数据提交
            case R.id.activity_car_news_btn_submit:
                if (stutes == Constants.ZERO){
                    if (!TextUtils.isEmpty(activity_car_news_edittext_brand.getText().toString())){
                        if (!TextUtils.isEmpty(activity_car_news_edittext_car_weight_car_number.getText().toString())){
                            if (StringUilts.StringCarNumber(activity_car_news_edittext_car_weight_car_number.getText().toString().trim())){
                                if (car != Constants.ZERO){
                                    if(!TextUtils.isEmpty(hashMap.get("carPic"))&&!TextUtils.isEmpty(hashMap.get("tracelPic"))){
                                        stutes = Constants.ZERO;
                                        httpCarData();
                                        loading_linearlayout_image.setVisibility(View.VISIBLE);
                                    }else {
                                        Toast.makeText(getApplication(),"请选择图片",Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(getApplication(),"请选择车型",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getApplication(),"车牌号格式不正确",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplication(),"请输入车牌号！",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplication(),"请输入车辆名称",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplication(),"正在上传资料，请稍后。。。",Toast.LENGTH_SHORT).show();
                }

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
    /**
     *  图片 popup
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
     * 图片回掉
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (data != null){
                if (requestCode == Constants.ONE){
                    if (data.getExtras()!=null){
                        Bitmap bm = (Bitmap) data.getExtras().get("data");;
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
                            //在上传时做压缩处理不在本地做存储
                            initbit(bm);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initbit(Bitmap bitmap) {
        switch (PHOTO_NUMBER){
            case 1:
                activity_car_news_imagebutton_is_photo.setImageBitmap(bitmap);
                break;
            case 2:
                activity_car_news_imagebutton_car_book.setImageBitmap(bitmap);
                break;
        }
        this.bitmap = bitmap;
        if (bitmap != null){
            loading_linearlayout_image.setVisibility(View.VISIBLE);
            MyThreadPoolManager.getInstance().execute(runnable);
        }
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String path = Tailoring.ziphoto(bitmap);
            String up_path = InternetImageview.asyncPutObjectWithMD5Verify(CharConstants.BUCKRT, CharConstants.FILE_NAME + "/" +SharePreferencesUlits.getString(getApplication(),CharConstants.PHONE,"")+"/" + System.currentTimeMillis() + ".png", path);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("path", up_path);
            message.what = Constants.ONE;
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };
    public Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (!TextUtils.isEmpty(msg.getData().getString("path"))){
                        hashMap.put(key,msg.getData().getString("path"));
                    }else {
                        Toast.makeText(getApplication(),"图片上传失败",Toast.LENGTH_LONG).show();
                    }
                    loading_linearlayout_image.setVisibility(View.GONE);
                    break;
            }
        }
    };
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
        popup_user_btn_car_moudel_cancel =  (Button) view2.findViewById(R.id.popup_user_btn_car_moudel_cancel);
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
     * popup弹框app背景变暗
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }
    //上传cardata
    public void httpCarData(){
        OkHttpUtils.post(Api.add_car)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getApplicationContext(), CharConstants.PHONE,""))
                .params("carTypeCode",String.valueOf(car))
                .params("carCode",activity_car_news_edittext_car_weight_car_number.getText().toString())
                .params("carName",activity_car_news_edittext_brand.getText().toString())
                .params("carPic",hashMap.get("carPic"))
                .params("tracelPic",hashMap.get("tracelPic"))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout_image.setVisibility(View.GONE);
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                finish();
                                Toast.makeText(getApplication(),new JSONObject(s).getString("message"),Toast.LENGTH_SHORT).show();
                            }else {
                                stutes = Constants.ZERO;
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
                        loading_linearlayout_image.setVisibility(View.GONE);
                    }
                });

    }

}
