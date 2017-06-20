package com.example.xl.foursling.activityes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.activityes.roder.SeachRoderActivity;
import com.example.xl.foursling.adapter.roder.CityAdapter;
import com.example.xl.foursling.db.DbCtrl;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.MyThreadPoolManager;
import com.example.xl.foursling.unity.ProvinceAndCity;
import com.example.xl.foursling.view.titlebar.TeaskBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xl on 2016/12/15.
 */
public class SeachAddressActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //取消
    @Bind(R.id.layout_title_btn)
    Button layout_title_btn;
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    // 起始
    @Bind(R.id.activity_search_address_edit_start)
    EditText activity_search_address_edit_start;
    // 结束
    @Bind(R.id.activity_search_address_edit_end)
    EditText activity_search_address_edit_end;
    // 查询
    @Bind(R.id.activity_search_address_image_go)
    ImageButton activity_search_address_image_go;
    // 数据
    @Bind(R.id.activity_Search_address_listview)
    ListView activity_Search_address_listview;

    //字符常量
    private String content = null;
    private int state,state_code;
    private CityAdapter cityAdapter;
    private ArrayList<ProvinceAndCity> arrayList;
    private String start_code = null,end_code = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);
        //绑定注解
        ButterKnife.bind(this);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //初始化数据
        initData();
        //初始化控件
        initView();
    }

    private void initView() {
        layout_title_btn.setOnClickListener(this);
        activity_search_address_image_go.setOnClickListener(this);
        //item点击事件
        activity_Search_address_listview.setOnItemClickListener(this);
        //开始edittext
        activity_search_address_edit_start.addTextChangedListener(new TextWatcher() {
            //输入文字前
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            //输入文字时
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(activity_search_address_edit_start.getText().toString())){
                    content = activity_search_address_edit_start.getText().toString();
                    activity_search_address_image_go.setSelected(true);
                    state = Constants.ONE;
                    state_code = Constants.ONE;
                    MyThreadPoolManager.getInstance().execute(runnable);
                }else {
                    state = Constants.ZERO;
                    activity_search_address_image_go.setSelected(false);
                }
            }
            //输入文字后
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //结束edittext
        activity_search_address_edit_end.addTextChangedListener(new TextWatcher() {
            //输入文字前
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            //输入文字时
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(activity_search_address_edit_end.getText().toString())){
                    content = activity_search_address_edit_end.getText().toString();
                    MyThreadPoolManager.getInstance().execute(runnable);
                    activity_search_address_image_go.setSelected(true);
                    state_code = Constants.TWO;
                    state = Constants.TWO;
                }else {
                    state = Constants.ZERO;
                    activity_search_address_image_go.setSelected(false);
                }
            }
            //输入文字后
            @Override
            public void afterTextChanged(Editable editable) {
                activity_Search_address_listview.setVisibility(View.GONE);
            }
        });
    }
    //查询数据
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
           ArrayList<ProvinceAndCity> arrayList = DbCtrl.fuzzyselectCity(getApplication(),content);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("city_content",arrayList);
            message.what = Constants.ONE;
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };
    private void initData() {
        layout_title_back.setVisibility(View.INVISIBLE);
        layout_title_textview.setText(R.string.activity_search_address);
        layout_title_btn.setText(R.string.cancel);
        state = Constants.ZERO;
        state_code = Constants.ZERO;
//        activity_search_address_edit_start
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            //返回
            case R.id.layout_title_btn:
                this.finish();
                break;
            case R.id.activity_search_address_image_go:
                if (state != Constants.ZERO){
                    if (!TextUtils.isEmpty(activity_search_address_edit_start.getText().toString())){
                        if (!TextUtils.isEmpty(activity_search_address_edit_end.getText().toString())){
//                        intent.putExtra("start_code",start_code);
//                        intent.putExtra("end_code",end_code);
                            intent.putExtra("start_code",start_code);
                            intent.putExtra("end_code",end_code);
                            intent.setClass(this,SeachRoderActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplication(),"请输入目的地",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplication(),"请输入出发地",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplication(),"请输入开始或结束位置",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    /**
     * 查询的城市数据
     */
    Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Log.d("this_content_text",msg.getData().getParcelableArrayList("city_content").size()+"");
                    if (msg.getData().getParcelableArrayList("city_content") != null){
                        arrayList = msg.getData().getParcelableArrayList("city_content");
                        activity_Search_address_listview.setVisibility(View.VISIBLE);
                        cityAdapter = new CityAdapter(getApplication(), arrayList);
                        activity_Search_address_listview.setAdapter(cityAdapter);
                    }
                    break;
                case 2:

                    break;
            }
        }
    };
    /**
     * item 点击选择城市
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (state_code){
            case 1:
                start_code = arrayList.get(position).city_id;
//                start_code = arrayList.get(position).city;
                activity_search_address_edit_start.setText(arrayList.get(position).city);
                activity_search_address_edit_start.setSelection(activity_search_address_edit_start.getText().toString().length());

                break;
            case 2:
                end_code = arrayList.get(position).city_id;
//                end_code = arrayList.get(position).city;
                activity_search_address_edit_end.setText(arrayList.get(position).city);
                activity_search_address_edit_end.setSelection(activity_search_address_edit_end.getText().toString().length());
                break;
        }
    }
}
