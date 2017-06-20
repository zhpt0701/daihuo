package com.example.xl.foursling.activityes.mesages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.VityPlan;
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
 * Created by admin on 2016/12/27.
 */
public class PlanActivity extends BaseActivity implements View.OnClickListener {
    //申请
    @Bind(R.id.activity_plan_image_sh)
    ImageView activity_plan_image_sh;
    @Bind(R.id.activity_plan_text_sh)
    TextView activity_plan_text_sh;
    @Bind(R.id.activity_plan_text_color_sh)
    TextView activity_plan_text_color_sh;
    @Bind(R.id.activity_plan_text_color_sh_time)
    TextView activity_plan_text_color_sh_time;
    //审核
    @Bind(R.id.activity_plan_image_sq)
    ImageView activity_plan_image_sq;
    @Bind(R.id.activity_plan_text_sq)
    TextView activity_plan_text_sq;
    @Bind(R.id.activity_plan_text_color_sq)
    TextView activity_plan_text_color_sq;
    @Bind(R.id.activity_plan_text_color_sq_time)
    TextView activity_plan_text_color_sq_time;
    //提现成功
    @Bind(R.id.activity_plan_image_tx)
    ImageView activity_plan_image_tx;
    @Bind(R.id.activity_plan_text_color_tx)
    TextView activity_plan_text_color_tx;
    @Bind(R.id.activity_plan_text_color_tx_time)
    TextView activity_plan_text_color_tx_time;
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;

    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    @Bind(R.id.layout_title_back)
    Button layout_title_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        //改变状态栏颜色
        TeaskBar.onSystemoutcolor(this, Constants.ZERO);
        //绑定注解框架
        ButterKnife.bind(this);
        //初始化数据
        initData();
        // 初始化界面
        initView();
    }

    private void initView() {
        layout_title_textview.setText("进度中心");;
        layout_title_back.setOnClickListener(this);
    }

    private void initData() {
        loading_linearlayout.setVisibility(View.VISIBLE);
        OkHttpUtils.post(Api.vity_plan)
                .tag(this)
                .params("orderCode",getIntent().getStringExtra("orderCode"))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        Log.d("this_content",s);
                        try {
                            loading_linearlayout.setVisibility(View.GONE);
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                VityPlan vityPlan = GsonUtil.processJson(s,VityPlan.class);
                                switch (vityPlan.message.get(0).status){
                                    //成功
                                    case 0:
                                        activity_plan_text_color_sq.setSelected(true);
                                        activity_plan_text_color_tx.setSelected(true);
                                        activity_plan_text_sh.setSelected(true);
                                        activity_plan_text_sq.setSelected(true);
                                        activity_plan_text_color_sq.setSelected(true);
                                        activity_plan_image_sq.setSelected(true);
                                        activity_plan_image_sh.setSelected(true);
                                        activity_plan_image_tx.setSelected(true);
                                        activity_plan_text_color_tx_time.setText(StringUilts.transfromTime(vityPlan.message.get(0).tradeDate));
                                        activity_plan_text_color_sq_time.setText(StringUilts.transfromTime(vityPlan.message.get(0).createDate));
                                        activity_plan_text_color_sh_time.setText(StringUilts.transfromTime(vityPlan.message.get(0).tradeDate));
                                        break;
                                    //审核中
                                    case 1:
                                        activity_plan_text_color_sq.setSelected(true);
                                        activity_plan_image_sq.setSelected(true);
                                        activity_plan_text_sq.setSelected(true);
                                        activity_plan_text_color_sh.setText("处理中");
                                        activity_plan_image_sh.setSelected(true);
                                        activity_plan_text_color_sh_time.setText(StringUilts.transfromTime(vityPlan.message.get(0).createDate));
                                        activity_plan_text_color_tx.setText("提现中");
                                        activity_plan_image_sq.setSelected(true);
                                        activity_plan_text_color_sq_time.setText(StringUilts.transfromTime(vityPlan.message.get(0).createDate));
                                        break;
                                    //失败
                                    case 2:
                                        activity_plan_text_color_sq.setSelected(true);
                                        activity_plan_text_color_tx.setSelected(true);
                                        activity_plan_text_color_sh.setText("审核失败");
                                        activity_plan_text_color_tx.setText("提现失败");
                                        activity_plan_text_sh.setSelected(true);
                                        activity_plan_text_sq.setSelected(true);
                                        activity_plan_text_color_sq.setSelected(true);
                                        activity_plan_image_sq.setSelected(true);
                                        activity_plan_image_sh.setSelected(true);
                                        activity_plan_image_tx.setSelected(true);
                                        activity_plan_text_color_sq_time.setText(StringUilts.transfromTime(vityPlan.message.get(0).createDate));
                                        activity_plan_text_color_sh_time.setText(StringUilts.transfromTime(vityPlan.message.get(0).tradeDate));
                                        activity_plan_text_color_tx_time.setText(StringUilts.transfromTime(vityPlan.message.get(0).tradeDate));
                                        break;
                                }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_title_back:
                finish();
                break;
        }
    }
}
