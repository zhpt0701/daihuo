package com.example.xl.foursling.adapter.message;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xl.foursling.R;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.RoderMrssage;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by admin on 2017/1/10.
 */
public class RoderMessageAdapter extends BaseAdapter{
    private List<RoderMrssage.MessageBean.RowsBean> message;
    private Context context;
    public RoderMessageAdapter(Context context, List<RoderMrssage.MessageBean.RowsBean> message) {
        this.context = context;
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_roder_item,null);
            viewHandler.activity_roder_item_times = (TextView)view.findViewById(R.id.activity_roder_item_times);
            viewHandler.activity_roder_item_text = (TextView)view.findViewById(R.id.activity_roder_item_text);
            viewHandler.activity_roder_item_text_roder = (TextView)view.findViewById(R.id.activity_roder_item_text_roder);
            viewHandler.order_activity_text_end = (TextView)view.findViewById(R.id.order_activity_text_end);
            viewHandler.order_activity_text_end_con = (TextView)view.findViewById(R.id.order_activity_text_end_con);
            viewHandler.order_activity_text_start = (TextView)view.findViewById(R.id.order_activity_text_start);
            viewHandler.order_activity_text_start_con = (TextView)view.findViewById(R.id.order_activity_text_start_con);
            view.setTag(viewHandler);
        }else {
            viewHandler = (ViewHandler)view.getTag();
        }
        viewHandler.activity_roder_item_times.setText(message.get(i).createDate);
        viewHandler.activity_roder_item_text.setText(message.get(i).title);
        viewHandler.activity_roder_item_text_roder.setText(message.get(i).parcelNO);
        if (!TextUtils.isEmpty(String.valueOf(message.get(i).name1))){
            viewHandler.order_activity_text_start.setText(message.get(i).name1.substring(0,message.get(i).name1.length()-4));
        }
        viewHandler.order_activity_text_start_con.setText("("+message.get(i).name1+")");
        if (!TextUtils.isEmpty(String.valueOf(message.get(i).name2))){
            viewHandler.order_activity_text_end.setText(message.get(i).name2.substring(0,message.get(i).name2.length()-4));
        }
        viewHandler.order_activity_text_end_con.setText("("+message.get(i).name2+")");
        return view;
    }
    class ViewHandler{
        TextView activity_roder_item_times,activity_roder_item_text,activity_roder_item_text_roder,
                order_activity_text_start,order_activity_text_start_con,
                order_activity_text_end,order_activity_text_end_con;
    }
}
