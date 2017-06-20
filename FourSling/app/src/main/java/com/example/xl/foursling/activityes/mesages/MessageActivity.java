package com.example.xl.foursling.activityes.mesages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.adapter.message.MessageAdapter;
import com.example.xl.foursling.http.Api;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.Messagees;
import com.example.xl.foursling.view.titlebar.TeaskBar;
import com.example.xl.foursling.view.xlist.XListView;
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
 * Created by xl on 2016/12/14.
 */
public class MessageActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener{
    //返回
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    //title内容
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    //明细数据
    @Bind(R.id.activity_message_xlist)
    XListView activity_message_xlist;
    //加载
    @Bind(R.id.loading_linearlayout)
    LinearLayout loading_linearlayout;
    @Bind(R.id.not_data_linearlayout)
    LinearLayout not_data_linearlayout;
    private MessageAdapter messageAdapter;
    private Messagees messagees;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        //改变状态栏颜色
//        String content = "y37823483268e7yy《jsdlfosdhfd》jslfjsldkjflsd";
//        Log.d("this------------》》》》》》》》",content.substring(content.indexOf("《"),content.indexOf("》")+1)+"-=-=-="+content);
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

    private void initData() {
        layout_title_textview.setText(R.string.fragment_message_xt);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loading_linearlayout.setVisibility(View.VISIBLE);
        //是否有未读消息
        httpmessage();
    }

    private void httpmessage() {
        OkHttpUtils.post(Api.system_message)
                .tag(this)
                .params("phone", SharePreferencesUlits.getString(getApplication(), CharConstants.PHONE,""))
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        loading_linearlayout.setVisibility(View.GONE);
                        Log.d("this++++++",s);
                        onLoaderRefresh();
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                not_data_linearlayout.setVisibility(View.GONE);
                                jsonPare(s);
                            }else {
                                not_data_linearlayout.setVisibility(View.VISIBLE);
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
                        not_data_linearlayout.setVisibility(View.VISIBLE);
                        onLoader();
                    }
                });
    }

    private void jsonPare(String s) {
        messagees = GsonUtil.processJson(s,Messagees.class);
        //点击打开浏览器
        MessageAdapter.LayoutOnClickLister layoutOnClickLister = new MessageAdapter.LayoutOnClickLister() {
            @Override
            protected void layoutOnClickLister(Integer tag, View view) {
                if (!TextUtils.isEmpty(messagees.message.get(tag).messageUrl)){
                    Intent intent = new  Intent(Intent.ACTION_VIEW, Uri.parse(messagees.message.get(tag).messageUrl));
                    startActivity(intent);
                }
            }
        };
        //删除
        MessageAdapter.DeleteOnClickLister deleteOnClickLister = new MessageAdapter.DeleteOnClickLister() {
            @Override
            protected void deleteOnClickLister(Integer tag, View view) {
                loading_linearlayout.setVisibility(View.VISIBLE);
                String time = StringUilts.transfromTime(messagees.message.get(tag).createDate);
                httpdeleteMessage(time);
                MessageAdapter.swipeLayout.close();
            }
        };
        messageAdapter = new MessageAdapter(getApplication(), messagees.message,layoutOnClickLister,deleteOnClickLister);
        activity_message_xlist.setAdapter(messageAdapter);
    }
    //删除消息
    private void httpdeleteMessage(final String time) {
        OkHttpUtils.post(Api.delete_message)
                .tag(this)
                .params("createDate",time)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        Log.d("deleteMessage",s);
                        onLoaderRefresh();
                        try {
                            if (new JSONObject(s).getInt("code") == Constants.ZERO){
                                httpmessage();
                            }else {
                                loading_linearlayout.setVisibility(View.GONE);
                            }
                            if (!TextUtils.isEmpty(new JSONObject(s).getString("message"))){
                                Toast.makeText(getApplication(),new JSONObject(s).getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        loading_linearlayout.setVisibility(View.GONE);
                        onLoader();
                    }
                });
    }

    private void initView() {
        layout_title_back.setOnClickListener(this);
        activity_message_xlist.setRefreshTime(StringUilts.getTime());
        activity_message_xlist.setAutoLoadEnable(true);
        activity_message_xlist.setPullRefreshEnable(true);
        activity_message_xlist.setPullLoadEnable(true);
        activity_message_xlist.setXListViewListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_title_back:
                finish();
                break;
        }
    }
    //下拉刷新
    @Override
    public void onRefresh() {
        loading_linearlayout.setVisibility(View.VISIBLE);
        //获取数据
        httpmessage();
    }
    //上拉加载
    @Override
    public void onLoadMore() {
        onLoader();
    }
    // 获得数据后一定要调用onLoad()方法，否则刷新会一直进行，根本停不下来
    public void onLoader(){
        activity_message_xlist.stopLoadMore();//停止刷新
        activity_message_xlist.stopRefresh();//停止加载更多
        activity_message_xlist.setRefreshTime(StringUilts.getTime());//设置时间
        activity_message_xlist.setPullLoadEnable(false);
    }
    public void onLoaderRefresh(){
        activity_message_xlist.stopRefresh();
        activity_message_xlist.stopLoadMore();
        activity_message_xlist.setRefreshTime(StringUilts.getTime());
        activity_message_xlist.setPullLoadEnable(true);
    }
}
