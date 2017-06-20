package com.example.xl.foursling.activityes.mesages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.fragments.FragmentFactory;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.view.titlebar.TeaskBar;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xl on 2016/12/16.
 */
public class MoneyMessageActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{
    //切换
    @Bind(R.id.activity_money_message_radiogroup)
    RadioGroup activity_money_message_radiogroup;
    @Bind(R.id.activity_money_message_linearlayout)
    LinearLayout activity_money_message_linearlayout;
    //返回
    @Bind(R.id.layout_title_back)
    Button layout_title_back;
    //title
    @Bind(R.id.layout_title_textview)
    TextView layout_title_textview;
    @Bind(R.id.relativerlyout_title)
    RelativeLayout relativerlyout_title;
    private ArrayList<String> fragmentTags;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_message);
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
        fragmentTags = new ArrayList<>(Arrays.asList("MessageFragment", "UserEnterFragment", "RoderFragment", "MoneyFragment","RouteFragment","SettingFragment","FreightFragment","FragmentDeposit"));
        setCurrentFragment(Constants.SIX);
        layout_title_textview.setText("钱包提示");

    }
    private void initView() {
        //设置监听
        layout_title_back.setOnClickListener(this);
        activity_money_message_radiogroup.setOnCheckedChangeListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_title_back:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.activity_money_message_radiobutton_freight:
                setCurrentFragment(Constants.SIX);
                break;

            case R.id.activity_money_message_radiobutton_deposit:
                setCurrentFragment(Constants.SEVEN);
                break;
        }
    }
    /**
     * fragment界面切换
     */
    public void setCurrentFragment(int currIndex) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = FragmentFactory.getFragment(currIndex);
        fragmentTransaction.replace(R.id.activity_money_message_linearlayout, fragment,fragmentTags.get(currIndex));//将fragment放到content_home中进行显示
        fragmentTransaction.commit();
    }


}
