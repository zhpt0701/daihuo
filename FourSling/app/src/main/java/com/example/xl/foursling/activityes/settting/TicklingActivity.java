package com.example.xl.foursling.activityes.settting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.PhoneNumber;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.view.titlebar.TeaskBar;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2017/1/14.
 */
public class TicklingActivity extends BaseActivity implements View.OnClickListener {
    /**
     * @parm activity_tickling_btn_submit 提交
     * @parm layout_title_back 返回
     * @parm layout_title_textview title
     * @parm activity_tickling_edittext_content 反馈内容
     * @parm activity_tickling_edittext_phone 联系方式
     */
    @Bind(R.id.activity_tickling_btn_submit)
    Button activity_tickling_btn_submit;
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    @Bind(R.id.activity_tickling_edittext_content)
    EditText activity_tickling_edittext_content;
    @Bind(R.id.activity_tickling_edittext_phone)
    EditText activity_tickling_edittext_phone;
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickling);
        //绑定注解
        ButterKnife.bind(this);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);

        //初始化数据
        initData();
        //初始化控件
        initView();
    }

    private void initView() {
        activity_tickling_btn_submit.setOnClickListener(this);
        layout_title_back.setOnClickListener(this);
    }

    private void initData() {
        layout_title_textview.setText(getResources().getString(R.string.tickling));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //返回
            case R.id.layout_title_back:
                finish();
                break;
            //data提交
            case R.id.activity_tickling_btn_submit:
                if (!TextUtils.isEmpty(activity_tickling_edittext_content.getText().toString())){
                    if (!TextUtils.isEmpty(activity_tickling_edittext_phone.getText().toString())){
                        if (PhoneNumber.IsMoblieNo(activity_tickling_edittext_phone.getText().toString())){
                            loading_linearlayout.setVisibility(View.VISIBLE);
                            httpFeedBack();
                        }else {
                            Toast.makeText(getApplication(),"您输入的手机号码不存在",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplication(),"请输入手机号",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplication(),"您输入反馈内容",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    //意见反馈接口
    private void httpFeedBack() {
        OkHttpUtils.post(Api.Opinion)
                .tag(getApplication())
                .params("phone",activity_tickling_edittext_phone.getText().toString().trim())
                .params("message",activity_tickling_edittext_content.getText().toString().trim())
                .params("source","1")
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        Log.d("this_congeotn",s);
                        loading_linearlayout.setVisibility(View.GONE);
                        if (s.equals("true")){
                            finish();
                            Toast.makeText(getApplication(),"意见反馈成功",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplication(),"意见反馈失败",Toast.LENGTH_LONG).show();
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
}
