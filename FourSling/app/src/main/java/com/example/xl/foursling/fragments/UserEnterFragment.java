package com.example.xl.foursling.fragments;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.xl.foursling.MainActivity;
import com.example.xl.foursling.MyApplication;
import com.example.xl.foursling.R;
import com.example.xl.foursling.activityes.users.UserAptitudeActivity;
import com.example.xl.foursling.activityes.users.UserCarActivity;
import com.example.xl.foursling.adapter.ArrayWheelAdapter;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.http.InternetImageview;
import com.example.xl.foursling.http.MyImageLoader;
import com.example.xl.foursling.http.ThisPhoto;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.MyThreadPoolManager;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.tools.Tailoring;
import com.example.xl.foursling.unity.CityModel;
import com.example.xl.foursling.unity.DistrictModel;
import com.example.xl.foursling.unity.Province;
import com.example.xl.foursling.unity.ProvinceModel;
import com.example.xl.foursling.view.roller.OnWheelChangedListener;
import com.example.xl.foursling.view.roller.WheelView;
import com.example.xl.foursling.view.roller.XmlParserHandler;
import com.example.xl.foursling.view.titlebar.TeaskBar;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xl on 2016/12/15.
 */

public class UserEnterFragment extends Fragment implements View.OnClickListener, OnWheelChangedListener {
    //选择图片
    @Bind(R.id.fragment_user_enter_relativelayout)
    RelativeLayout fragment_user_enter_relativelayout;
    //菜单
    @Bind(R.id.fragment_user_enter_btn_menu)
    Button fragment_user_enter_btn_menu;
    //图片
    @Bind(R.id.fragment_user_enter_imageview_photo)
    ImageView fragment_user_enter_imageview_photo;
    //地址
    @Bind(R.id.fragment_user_enter_btn_address)
    Button fragment_user_enter_btn_address;
    //消息
    @Bind(R.id.fragment_user_enter_btn_news)
    Button fragment_user_enter_btn_news;
    //车辆信息
    @Bind(R.id.fragment_user_enter_btn_car)
    Button fragment_user_enter_btn_car;
    //性别
    @Bind(R.id.fragment_user_enter_btn_sex)
    Button fragment_user_enter_btn_sex;
    //加载
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    private PopupWindow popupWindow,popupWindowSex;
    private Button popup_user_btn_cancel;
    private Button popup_user_btn_ok;
    private WheelView popup_user_wheelview_province;
    private WheelView popup_user_wheelview_city;
    private WheelView popup_user_wheelview_district;

    String[] mProvinceDatas;
    Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    String mCurrentProviceName;
    String mCurrentCityName;
    String mCurrentDistrictName ="";
    String mCurrentZipCode ="";
    private int c= 0,select = 3;
    private ArrayList<Province> arrayList;
    private ArrayWheelAdapter<String> arrayWheelAdapter;
    private View view1,view2;
    private Button popup_user_btn_one;
    private Button popup_user_btn_two;
    private Button popup_user_btn_three;
    private View view;
    private int state;
    private Bitmap bitmap;
    private String path;
    private ThisPhoto imageLoader;
    private String up_path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_enter,container,false);
        //注解绑定
        ButterKnife.bind(this, view);
        //初始化数据
        initData(view);
        //初始化控件
        initView(view);
        return view;
    }



    private void initView(View view) {
        //监听事件设置
        fragment_user_enter_relativelayout.setOnClickListener(this);
        fragment_user_enter_btn_address.setOnClickListener(this);
        fragment_user_enter_btn_car.setOnClickListener(this);
        fragment_user_enter_btn_menu.setOnClickListener(this);
        fragment_user_enter_btn_news.setOnClickListener(this);
        fragment_user_enter_btn_sex.setOnClickListener(this);
    }

    private void initData(View view) {
        state = Constants.NINE;
        imageLoader = new ThisPhoto(getActivity());
        if (!TextUtils.isEmpty(SharePreferencesUlits.getString(getActivity(),CharConstants.PHOTO,""))){
            imageLoader.imageLoader.displayImage(SharePreferencesUlits.getString(getActivity(),CharConstants.PHOTO,""), fragment_user_enter_imageview_photo, imageLoader.options);
        }
        arrayList = new ArrayList<Province>();
        //获取数据
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getActivity().getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            provinceList = handler.getDataList();
            if (provinceList!= null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList!= null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();

                }
            }
            //获取升级数据
            mProvinceDatas = new String[provinceList.size()];
            for (int i=0; i< provinceList.size(); i++) {
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                //获取市级数据
                for (int j=0; j< cityList.size(); j++) {
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    //区级数据
                    for (int k=0; k<districtList.size(); k++) {
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!TextUtils.isEmpty(SharePreferencesUlits.getString(getActivity(),CharConstants.USER_SEX,""))){
            fragment_user_enter_btn_sex.setText(SharePreferencesUlits.getString(getActivity(),CharConstants.USER_SEX,""));
        }
        if (!TextUtils.isEmpty(SharePreferencesUlits.getString(getActivity(),CharConstants.ADDRESS,""))){
            fragment_user_enter_btn_address.setText(SharePreferencesUlits.getString(getActivity(),CharConstants.ADDRESS,""));
        }
        //查询用户审核状态
        httpcheckCode();
        loading_linearlayout.setVisibility(View.VISIBLE);
    }

    //请求审核状态
    private void httpcheckCode() {
        OkHttpUtils.post(Api.select_user_audit)
                .tag(getActivity())
                .params("phone",SharePreferencesUlits.getString(getActivity(),CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            state = new JSONObject(s).getInt("code");
                            if (!TextUtils.isEmpty(new JSONObject(s).getString("message"))){
                                fragment_user_enter_btn_news.setText(new JSONObject(s).getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loading_linearlayout.setVisibility(View.GONE);
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

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            //菜单
            case R.id.fragment_user_enter_btn_menu:
                MainActivity.menu.showSecondaryMenu(true);
                break;
            //图片
            case R.id.fragment_user_enter_relativelayout:
                backgroundAlpha(0.5f);
                select = Constants.ZERO;
                initPopupSex(view,true, select);
                break;
            //性别
            case R.id.fragment_user_enter_btn_sex:
                backgroundAlpha(0.5f);
                select = Constants.ONE;
                initPopupSex(view,true,select);
                break;
            //地址
            case R.id.fragment_user_enter_btn_address:
                backgroundAlpha(0.5f);
                initpopup(view,true);
                break;
            //车辆
            case R.id.fragment_user_enter_btn_car:
                intent.setClass(getActivity(),UserCarActivity.class);
                startActivity(intent);
                break;
            //信息
            case R.id.fragment_user_enter_btn_news:
                if (state == Constants.FOUR){
                    intent.putExtra("user_code","");
                }else {
                    intent.putExtra("user_code",String.valueOf(state));
                }
                intent.setClass(getActivity(),UserAptitudeActivity.class);
                startActivity(intent);
                break;
            //取消选择城市
            case R.id.popup_user_btn_cancel:
                popupWindow.dismiss();
                backgroundAlpha(1f);
                break;
            //确认选择
            case R.id.popup_user_btn_ok:
                httpUpData(Constants.ONE,Api.update_address,"address", StringUilts.iso(mCurrentProviceName+mCurrentCityName+mCurrentDistrictName));
                popupWindow.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_one:
                if (select == Constants.ZERO){
                    state = Constants.ONE;
                    startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), Constants.ONE);
                }else {
                    loading_linearlayout.setVisibility(View.VISIBLE);
                    httpUpData(Constants.THREE,Api.update_sex,"userSex","0");
                }
                popupWindowSex.dismiss();
                backgroundAlpha(1f);
                break;
            case R.id.popup_user_btn_two:
                if (select == Constants.ZERO){
                    state = Constants.TWO;
                    Intent intent1 = new Intent(Intent.ACTION_PICK);
                    intent1.setType("image/*");//相片类型
                    startActivityForResult(intent1, Constants.TWO);
                }else {
                    loading_linearlayout.setVisibility(View.VISIBLE);
                    httpUpData(Constants.THREE,Api.update_sex,"userSex","1");
                }
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
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (data != null){
                if(bitmap!=null&&!bitmap.isRecycled()){
                    bitmap.recycle() ;
                    bitmap=null;
                    System.gc();
                }
                if (requestCode == Constants.ONE){
                    if (data.getExtras()!=null){
                        Bitmap bm = (Bitmap) data.getExtras().get("data");
                        bitmap = Tailoring.phototailoring(bm);
                        fragment_user_enter_imageview_photo.setImageBitmap(bitmap);
                    }
                }else if (requestCode == Constants.TWO){
                    if (data.getData()!=null){
                        Uri uri = data.getData();
                        if(uri==null){
                            //use bundle to get data
                            Bundle bundle = data.getExtras();
                            if (bundle!=null) {
                                Bitmap  photo = (Bitmap) bundle.get(uri.toString());
                                bitmap = Tailoring.phototailoring(photo);
                                fragment_user_enter_imageview_photo.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(getActivity(), "err****", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }else{
                            ContentResolver resolver = getActivity().getContentResolver();
                            Bitmap bm = null;
                            try {
                                bm = MediaStore.Images.Media.getBitmap(resolver, uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //在上传时做压缩处理不在本地做存储
                            bitmap = Tailoring.phototailoring(bm);
                            fragment_user_enter_imageview_photo.setImageBitmap(bitmap);
                        }
                    }
                }
            }
            if (bitmap != null){
                loading_linearlayout.setVisibility(View.VISIBLE);
                MyThreadPoolManager.getInstance().execute(runnable);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //上传图片
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            path = Tailoring.ziphoto(bitmap);
            up_path = InternetImageview.asyncPutObjectWithMD5Verify(CharConstants.BUCKRT,CharConstants.FILE_NAME+"/"+SharePreferencesUlits.getString(getActivity(),CharConstants.PHONE,"")+"/"+System.currentTimeMillis()+".png",path);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("path",InternetImageview.asyncPutObjectWithMD5Verify(CharConstants.BUCKRT,CharConstants.FILE_NAME+"/"+SharePreferencesUlits.getString(getActivity(),CharConstants.PHONE,"")+"/"+System.currentTimeMillis()+".png",path));
            message.what = Constants.ONE;
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };
    /**
     * 性别 图片 popup
     * @param v
     * @param b
     * @param i
     */
    private void initPopupSex(View v, boolean b,int i) {
        popupWindowSex = new PopupWindow();
        view2 = LayoutInflater.from(getActivity()).inflate(R.layout.popup_user_sex,null);
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
        popup_user_btn_one.setOnClickListener(this);
        popup_user_btn_two.setOnClickListener(this);
        popup_user_btn_three.setOnClickListener(this);
        if (i == Constants.ZERO){
            popup_user_btn_one.setText("相机");
            popup_user_btn_two.setText("相册");
        }else {
            popup_user_btn_one.setText("女");
            popup_user_btn_two.setText("男");
        }
        popupWindowSex.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        if (b){
            popupWindowSex.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 城市列表弹出框
     * @param v
     * @param flag
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void initpopup(View v, boolean flag) {
        popupWindow = new PopupWindow();
        view1 = LayoutInflater.from(getActivity()).inflate(R.layout.popup_user_enter_city,null);
        popupWindow.setContentView(view1);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popup_user_btn_cancel = (Button) view1.findViewById(R.id.popup_user_btn_cancel);
        popup_user_btn_ok = (Button) view1.findViewById(R.id.popup_user_btn_ok);
        popup_user_wheelview_province = (WheelView) view1.findViewById(R.id.popup_user_wheelview_province);
        popup_user_wheelview_city = (WheelView) view1.findViewById(R.id.popup_user_wheelview_city);
        popup_user_wheelview_district = (WheelView) view1.findViewById(R.id.popup_user_wheelview_district);
        popup_user_btn_ok.setOnClickListener(this);
        popup_user_btn_cancel.setOnClickListener(this);
        popup_user_wheelview_province.addChangingListener(this);
        popup_user_wheelview_city.addChangingListener(this);
        popup_user_wheelview_district.addChangingListener(this);
        arrayWheelAdapter = new ArrayWheelAdapter<String>(getActivity(), mProvinceDatas);
        popup_user_wheelview_province.setViewAdapter(arrayWheelAdapter);
        popup_user_wheelview_province.setVisibleItems(7);
        popup_user_wheelview_city.setVisibleItems(7);
        popup_user_wheelview_district.setVisibleItems(7);
        updateProvince();
        updateCity();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        if (flag){
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    }
    //根据省获取市
    private void updateProvince() {
        int pCurrent = popup_user_wheelview_province.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        popup_user_wheelview_city.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), cities));
        popup_user_wheelview_city.setCurrentItem(0);
        updateCity();
    }
    //根据市获取区
    private void updateCity() {
        int pCurrent = popup_user_wheelview_city.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[] { "" };
        }
        popup_user_wheelview_district.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), areas));
        popup_user_wheelview_district.setCurrentItem(0);
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
    }

    /**
     * popup弹框app背景变暗
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == popup_user_wheelview_province) {
            updateProvince();
        } else if (wheel == popup_user_wheelview_city) {
            updateCity();
        } else if (wheel == popup_user_wheelview_district) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }
    //界面刷新
    public Handler  handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (!TextUtils.isEmpty(msg.getData().getString("path"))){
                        loading_linearlayout.setVisibility(View.VISIBLE);
                        httpUpData(Constants.TWO,Api.update_photo,"userPic",msg.getData().getString("path"));
                    }else {
                        Toast.makeText(getActivity(),"上传图片失败",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };
    //数据上传
    public void httpUpData(final int i, String url, String key, final String path_ol){
        OkHttpUtils.post(url)
                .tag(this)
                .params("phone",SharePreferencesUlits.getString(getActivity(),CharConstants.PHONE,""))
                .params(key,path_ol)
                .execute(new StringCallback() {
                    /**
                     * 返回数据处理
                     * @param isFromCache
                     * @param s
                     * @param request
                     * @param response
                     */
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        try {
                            switch (i){
                                case 1:
                                    if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                        fragment_user_enter_btn_address.setText(mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
                                        SharePreferencesUlits.saveString(getActivity(),CharConstants.ADDRESS,mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
                                    }
                                    Toast.makeText(getActivity(),new JSONObject(s).getString("message"),Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                        SharePreferencesUlits.saveString(getActivity(),CharConstants.PHOTO,path_ol);
                                        imageLoader.imageLoader.displayImage(path_ol, fragment_user_enter_imageview_photo, imageLoader.options);
                                        MainActivity.imageView.setImageBitmap(bitmap);
                                        Toast.makeText(getActivity(),"图片上传成功",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getActivity(),"图片上传失败",Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 3:
                                    if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                        if (Integer.parseInt(path_ol) == Constants.ZERO) {
                                            fragment_user_enter_btn_sex.setText("女");
                                            SharePreferencesUlits.saveString(getActivity(), CharConstants.USER_SEX, "女");
                                        }else {
                                            fragment_user_enter_btn_sex.setText("男");
                                            SharePreferencesUlits.saveString(getActivity(),CharConstants.USER_SEX,"男");
                                        }
                                    }
                                    Toast.makeText(getActivity(),new JSONObject(s).getString("message"),Toast.LENGTH_LONG).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    /**
                     * 异常处理
                     * @param isFromCache
                     * @param call
                     * @param response
                     * @param e
                     */
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        loading_linearlayout.setVisibility(View.GONE);
                    }
                });
    }
}
