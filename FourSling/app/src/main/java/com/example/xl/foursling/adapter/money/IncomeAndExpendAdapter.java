package com.example.xl.foursling.adapter.money;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.Expend;
import com.example.xl.foursling.unity.Income;

import java.text.DecimalFormat;

/**
 * Created by admin on 2016/12/23.
 */
public class IncomeAndExpendAdapter extends BaseAdapter{
    private Context context;
    private int number;
    private Income income = null;
    private Expend expend = null;
    public IncomeAndExpendAdapter(Context context, int number, String s){
        this.context = context;
        this.number = number;
        if (number == Constants.ONE){
            income = GsonUtil.processJson(s,Income.class);
        }else {
            expend = GsonUtil.processJson(s,Expend.class);
        }
    }
    @Override
    public int getCount() {
        if (number == Constants.ONE){
            if (income.message != null){
                return income.message.size();
            }
        }else {
            if (expend.message != null){
                return expend.message.size();
            }
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (number == Constants.ONE){
            if (income.message != null){
                return income.message.get(i);
            }
        }else {
            if (expend.message != null){
                return expend.message.get(i);
            }
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHandler viewHandler;
        if (view == null){
            viewHandler = new ViewHandler();
            view = LayoutInflater.from(context).inflate(R.layout.activity_income_expend_item,null);
            //时间
            viewHandler.activity_income_expend_item_time = (TextView)view.findViewById(R.id.activity_income_expend_item_time);
            //标题
            viewHandler.activity_income_expend_item_title = (TextView)view.findViewById(R.id.activity_income_expend_item_title);
            //金额
            viewHandler.activity_income_expend_item_money = (TextView)view.findViewById(R.id.activity_income_expend_item_money);
            //余额
            viewHandler.activity_income_expend_item_yve_money = (TextView)view.findViewById(R.id.activity_income_expend_item_yve_money);
            //方式
            viewHandler.activity_income_expend_item_fs_and_zh = (TextView)view.findViewById(R.id.activity_income_expend_item_fs_and_zh);
            //方式content
            viewHandler.activity_income_expend_item_wx_and_alipay = (TextView)view.findViewById(R.id.activity_income_expend_item_wx_and_alipay);
            //手续
            viewHandler.activity_income_expend_item_dh_and_sxf = (TextView)view.findViewById(R.id.activity_income_expend_item_dh_and_sxf);
            //手续费
            viewHandler.activity_income_expend_item_odd_number = (TextView)view.findViewById(R.id.activity_income_expend_item_odd_number);
            //时间
            viewHandler.activity_income_expend_item_time_nu = (TextView)view.findViewById(R.id.activity_income_expend_item_time_nu);
            view.setTag(viewHandler);
        }else {
            viewHandler = (ViewHandler)view.getTag();
        }
        if (number == Constants.ONE){
            //充值
            viewHandler.activity_income_expend_item_time.setText(StringUilts.transfromTime(income.message.get(i).createDate));
            viewHandler.activity_income_expend_item_fs_and_zh.setText("充值方式");
            viewHandler.activity_income_expend_item_money.setText(StringUilts.doubleMoney((double)income.message.get(i).payAmount/100));
            if (income.message.get(i).payType == Constants.ONE){
                viewHandler.activity_income_expend_item_wx_and_alipay.setText("支付宝");
            }else if (income.message.get(i).payType == Constants.TWO){
                viewHandler.activity_income_expend_item_wx_and_alipay.setText("微信");
            }
            if (income.message.get(i).type_service.equals(CharConstants.RECHARGE)){
                viewHandler.activity_income_expend_item_title.setText("充值");
            }else if (income.message.get(i).type_service.equals(CharConstants.DEPOSIT)){
                viewHandler.activity_income_expend_item_title.setText("提现");
            }else if (income.message.get(i).type_service.equals(CharConstants.FREIGHT)){
                viewHandler.activity_income_expend_item_title.setText("带货运费");
            }
            viewHandler.activity_income_expend_item_yve_money.setText(StringUilts.doubleMoney((double)income.message.get(i).balance/100));
            viewHandler.activity_income_expend_item_time_nu.setText(StringUilts.transfromTime(income.message.get(i).createDate));
            if (true){
                viewHandler.activity_income_expend_item_dh_and_sxf.setVisibility(View.GONE);
                viewHandler.activity_income_expend_item_odd_number.setVisibility(View.GONE);
            }else {
                viewHandler.activity_income_expend_item_dh_and_sxf.setVisibility(View.VISIBLE);
                viewHandler.activity_income_expend_item_odd_number.setVisibility(View.VISIBLE);
                viewHandler.activity_income_expend_item_dh_and_sxf.setText("订单号");
            }
        }else {
            //提现
            viewHandler.activity_income_expend_item_time.setText(StringUilts.transfromTime(expend.message.get(i).createDate));
            viewHandler.activity_income_expend_item_fs_and_zh.setText("提现账户");
            viewHandler.activity_income_expend_item_dh_and_sxf.setVisibility(View.VISIBLE);
            viewHandler.activity_income_expend_item_odd_number.setVisibility(View.VISIBLE);
            viewHandler.activity_income_expend_item_dh_and_sxf.setText("手续费");
            viewHandler.activity_income_expend_item_title.setText("提现");
            viewHandler.activity_income_expend_item_yve_money.setText(StringUilts.doubleMoney((double)expend.message.get(i).balance/100));
            viewHandler.activity_income_expend_item_time_nu.setText(StringUilts.transfromTime(expend.message.get(i).createDate));
            viewHandler.activity_income_expend_item_money.setText(StringUilts.doubleMoney((double)expend.message.get(i).payAmount/100));
            if (expend.message.get(i).payType == Constants.ONE){
                viewHandler.activity_income_expend_item_wx_and_alipay.setText("支付宝"+ SharePreferencesUlits.getString(context,CharConstants.ALIPAY_NO,""));
            }else if (expend.message.get(i).payType == Constants.TWO){
                viewHandler.activity_income_expend_item_wx_and_alipay.setText("微信"+ SharePreferencesUlits.getString(context,CharConstants.WECHATNO,""));
            }
            viewHandler.activity_income_expend_item_odd_number.setText(StringUilts.doubleMoney((double)expend.message.get(i).amount/100));

        }
        return view;
    }
    class ViewHandler{
        TextView activity_income_expend_item_odd_number,activity_income_expend_item_yve_money,activity_income_expend_item_title,
                activity_income_expend_item_money,activity_income_expend_item_wx_and_alipay,activity_income_expend_item_dh_and_sxf,
                activity_income_expend_item_time,activity_income_expend_item_fs_and_zh,activity_income_expend_item_time_nu;
    }
}
