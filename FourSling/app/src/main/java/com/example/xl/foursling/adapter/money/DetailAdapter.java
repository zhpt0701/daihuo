package com.example.xl.foursling.adapter.money;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.GsonUtil;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.DepositDetail;
import com.example.xl.foursling.unity.RechargeDetail;

import java.text.DecimalFormat;

/**
 * Created by admin on 2016/12/23.
 */
public class DetailAdapter extends BaseAdapter{
    private Context context;
    private int detail_code;
    private DepositDetail depositDetail;
    private RechargeDetail rechargeDetail;
    private DecimalFormat decimalFormat;
    public DetailAdapter(Context context, Integer detail_code, String content){
        this.context = context;
        this.detail_code = detail_code;
        if (detail_code == Constants.ONE){
            rechargeDetail = GsonUtil.processJson(content,RechargeDetail.class);
        }else {
            depositDetail = GsonUtil.processJson(content,DepositDetail.class);
        }
        decimalFormat = new DecimalFormat("0.00");
    }
    @Override
    public int getCount() {
        if (detail_code == Constants.ONE){
            if (rechargeDetail.message != null){
                return rechargeDetail.message.size();
            }
        }else {
            if (depositDetail.message != null){
                return depositDetail.message.size();
            }
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (detail_code == Constants.ONE){
            if (rechargeDetail.message != null){
                return rechargeDetail.message.get(i);
            }
        }else {
            if (depositDetail.message != null){
                return depositDetail.message.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_detail_item,null);
            viewHandler.textView_title = (TextView)view.findViewById(R.id.activity_detail_item_textview_title);
            viewHandler.textView_money = (TextView)view.findViewById(R.id.activity_detail_item_textview_money);
            viewHandler.textView_time = (TextView)view.findViewById(R.id.activity_detail_item_textview_time);
            view.setTag(viewHandler);
        }else {
            viewHandler = (ViewHandler)view.getTag();
        }
        if (detail_code == Constants.ONE){
          //充值明细
            viewHandler.textView_time.setText(StringUilts.transfromTime(rechargeDetail.message.get(i).createDate));
            viewHandler.textView_money.setText(String.valueOf(decimalFormat.format((double)rechargeDetail.message.get(i).payAmount/100)));
            viewHandler.textView_title.setText("充值");
        }else {
            //提现明细
            viewHandler.textView_time.setText(StringUilts.transfromTime(depositDetail.message.get(i).createDate));
            viewHandler.textView_money.setText(String.valueOf(decimalFormat.format((double)depositDetail.message.get(i).tradingAmount/100)));
            viewHandler.textView_title.setText("提现");
        }
        return view;
    }
    class ViewHandler{
        TextView textView_money,textView_title,textView_time;
    }
}
