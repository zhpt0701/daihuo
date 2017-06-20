package com.example.xl.foursling.adapter.roder;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.xl.foursling.R;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.RoderUnity;
import com.example.xl.foursling.unity.SeachRoderUnity;

import java.util.List;

/**
 * Created by admin on 2016/12/28.
 */
public class RoderAdapter extends BaseAdapter{
    private Context context;
    private RoderOnClickLister roderOnClickLister;
    private List<RoderUnity.MessageBean.RowsBean> message;
    public RoderAdapter(Context context, List<RoderUnity.MessageBean.RowsBean> message,RoderOnClickLister roderOnClickLister) {
        this.context = context;
        this.roderOnClickLister = roderOnClickLister;
        this.message = message;
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
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHandler viewHandler;
        if (view == null){
            viewHandler = new ViewHandler();
            view = LayoutInflater.from(context).inflate(R.layout.fragment_roder_item,null);
            //运费
            viewHandler.fragment_roder_text_money = (TextView)view.findViewById(R.id.fragment_roder_text_money);
            //状态
            viewHandler.fragment_roder_text_stats = (TextView)view.findViewById(R.id.fragment_roder_text_stats);
            //时间
            viewHandler.fragment_roder_text_time = (TextView)view.findViewById(R.id.fragment_roder_text_time);
            //始发地
            viewHandler.fragment_order_text_start = (TextView)view.findViewById(R.id.fragment_order_text_start);
            viewHandler.fragment_order_text_start_con = (TextView)view.findViewById(R.id.fragment_order_text_start_con);
            //目的地
            viewHandler.fragment_order_text_end = (TextView)view.findViewById(R.id.fragment_order_text_end);
            viewHandler.fragment_order_text_end_con = (TextView)view.findViewById(R.id.fragment_order_text_end_con);
            //距离
            viewHandler.fragment_order_item_text_lang = (TextView)view.findViewById(R.id.fragment_order_item_text_lang);
            //重量
            viewHandler.fragment_order_item_text_weight = (TextView)view.findViewById(R.id.fragment_order_item_text_weight);
            //耗时
            viewHandler.fragment_order_item_text_time = (TextView)view.findViewById(R.id.fragment_order_item_text_time);
            //submit
            viewHandler.fragment_roder_btn_jd = (Button)view.findViewById(R.id.fragment_roder_btn_jd);
            view.setTag(viewHandler);
        }else {
            viewHandler = (ViewHandler)view.getTag();
        }
        viewHandler.fragment_roder_btn_jd.setOnClickListener(roderOnClickLister);
        viewHandler.fragment_roder_btn_jd.setTag(i);
        if (!TextUtils.isEmpty(String.valueOf(message.get(i).company_companyLon))){
            viewHandler.fragment_order_text_start.setText(message.get(i).company_companyLon.substring(0,message.get(i).company_companyLon.length()-4));
        }
        viewHandler.fragment_order_text_start_con.setText("("+message.get(i).company_companyLon+")");
        if (!TextUtils.isEmpty(String.valueOf(message.get(i).company_companyLat))){
            viewHandler.fragment_order_text_end.setText(message.get(i).company_companyLat.substring(0,message.get(i).company_companyLat.length()-4));
        }
        viewHandler.fragment_order_text_end_con.setText("("+message.get(i).company_companyLat+")");
        //运费
        viewHandler.fragment_roder_text_money.setText(StringUilts.doubleMoney((double)message.get(i).yunCost/100));
        //重量
        viewHandler.fragment_order_item_text_weight.setText(StringUilts.doubleMoney((double) message.get(i).weight/1000)+"kg");
        //耗时
        if (message.get(i).moments != Constants.ZERO){
            viewHandler.fragment_order_item_text_time.setText(StringUilts.transfromTime_ol(message.get(i).moments*1000));
        }else {
            viewHandler.fragment_order_item_text_time.setText("0时0分");
        }
        //距离
        viewHandler.fragment_order_item_text_lang.setText(StringUilts.doubleMoney((double) message.get(i).distance/1000)+"km");
        //时间
        viewHandler.fragment_roder_text_time.setText(message.get(i).createDate);
        //状态
        if (message.get(i).parcelStatus == Constants.ZERO){
            viewHandler.fragment_roder_text_stats.setText("未集包");
        }else {
            viewHandler.fragment_roder_text_stats.setText("已集包");
        }
        return view;
    }
    class ViewHandler{
        TextView fragment_roder_text_money,fragment_roder_text_stats,fragment_roder_text_time,fragment_order_text_start,
                fragment_order_text_start_con,fragment_order_text_end,fragment_order_text_end_con,fragment_order_item_text_lang,
                fragment_order_item_text_weight,fragment_order_item_text_time;
        Button fragment_roder_btn_jd;
    }
    //接单监听抽象类
    public static abstract class RoderOnClickLister implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            roderOnClickLister((Integer)view.getTag(),view);
        }
        protected abstract void roderOnClickLister(Integer tag, View view);

    }
}
