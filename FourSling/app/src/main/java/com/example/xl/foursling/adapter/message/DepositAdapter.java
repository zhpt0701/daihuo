package com.example.xl.foursling.adapter.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.xl.foursling.R;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.DepositMessage;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by admin on 2016/12/24.
 */
public class DepositAdapter extends BaseAdapter{
    private Context context;
    private SelectOnclickLister selectOnclickLister;
    private List<DepositMessage.MessageBean> message;
    private DecimalFormat decimalFormat;
    public DepositAdapter(Context context, List<DepositMessage.MessageBean> message, SelectOnclickLister selectOnclickLister){
        this.context = context;
        this.message = message;
        this.selectOnclickLister = selectOnclickLister;
        decimalFormat = new DecimalFormat("0.00");
    }
    @Override
    public int getCount() {
        if (message != null){
            return message.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (message != null){
            return message.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.fragment_deposit_item,null);
            //时间
            viewHandler.fragment_deposit_item_txt_time = (TextView)view.findViewById(R.id.fragment_deposit_item_txt_time);
            //金额
            viewHandler.fragment_deposit_item_txt_money = (TextView)view.findViewById(R.id.fragment_deposit_item_txt_money);
            //支付宝账号
            viewHandler.fragment_deposit_item_txt_alipay = (TextView)view.findViewById(R.id.fragment_deposit_item_txt_alipay);
            //进度
            viewHandler.fragment_deposit_item_txt_cg = (TextView)view.findViewById(R.id.fragment_deposit_item_txt_cg);
            viewHandler.fragment_deposit_item_btn_cg = (Button)view.findViewById(R.id.fragment_deposit_item_btn);
            view.setTag(viewHandler);
        }else {
            viewHandler = (ViewHandler)view.getTag();
        }
        viewHandler.fragment_deposit_item_btn_cg.setOnClickListener(selectOnclickLister);
        viewHandler.fragment_deposit_item_btn_cg.setTag(i);
        switch (message.get(i).status){
            case 0:
                viewHandler.fragment_deposit_item_txt_cg.setText("提现成功");
                break;
            case 1:
                viewHandler.fragment_deposit_item_txt_cg.setText("正在处理中");
                break;
            case 2:
                viewHandler.fragment_deposit_item_txt_cg.setText("提现失败");
                break;
        }
        viewHandler.fragment_deposit_item_txt_time.setText(StringUilts.transfromTime(message.get(i).createDate));
        viewHandler.fragment_deposit_item_txt_alipay.setText(message.get(i).userPayNO);
        viewHandler.fragment_deposit_item_txt_money.setText(String.valueOf(decimalFormat.format((double)message.get(i).tradingAmount/100)));
        return view;
    }
    class ViewHandler{
        TextView fragment_deposit_item_txt_time,fragment_deposit_item_txt_money,
                fragment_deposit_item_txt_alipay,fragment_deposit_item_txt_cg;
        Button fragment_deposit_item_btn_cg;
    }
    //进度查询
    public static abstract class SelectOnclickLister implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            selectOnclickLister((Integer)view.getTag(),view);
        }

        protected abstract void selectOnclickLister(Integer tag, View view);
    }
}
