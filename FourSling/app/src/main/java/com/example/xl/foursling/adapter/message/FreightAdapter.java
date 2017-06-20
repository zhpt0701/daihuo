package com.example.xl.foursling.adapter.message;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xl.foursling.R;
import com.example.xl.foursling.tools.ShuoMClickableSpan;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.Freight;

import java.util.List;

/**
 * Created by admin on 2016/12/24.
 * 带货运费adapter
 */
public class FreightAdapter extends BaseAdapter{
    private Context context;
    private List<Freight.MessageBean> message;
    public FreightAdapter(Context context, List<Freight.MessageBean> message){
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
            Log.d("this_content",message.size()+"-------00040");
            viewHandler = new ViewHandler();
            view = LayoutInflater.from(context).inflate(R.layout.fragment_freight_item,null);
            viewHandler.fragment_frieght_item_text_time = (TextView)view.findViewById(R.id.fragment_frieght_item_text_time);
            //title
            viewHandler.fragment_freight_item_textview_succeed = (TextView)view.findViewById(R.id.fragment_freight_item_textview_succeed);
            //content
            viewHandler.fragment_freight_item_textview_content = (TextView)view.findViewById(R.id.fragment_freight_item_textview_content);
            //运费
            viewHandler.fragment_freight_item_textview_yf = (TextView)view.findViewById(R.id.fragment_freight_item_textview_yf);
            //奖励
            viewHandler.fragment_freight_item_textview_award = (TextView)view.findViewById(R.id.fragment_freight_item_textview_award);

            view.setTag(viewHandler);
        }else {
            viewHandler = (ViewHandler)view.getTag();
        }

        viewHandler.fragment_frieght_item_text_time.setText(StringUilts.transfromTime(message.get(i).createDate));
        if (!TextUtils.isEmpty(message.get(i).messageTitle)){
            viewHandler.fragment_freight_item_textview_succeed.setText(message.get(i).messageTitle);
        }
        if (!TextUtils.isEmpty(message.get(i).messageContent)){
            viewHandler.fragment_freight_item_textview_content.setText(message.get(i).messageContent);
        }
        //实现改变某一段字符变色并且点击跳转
//        String conent = "";
//        SpannableString spannableString = new SpannableString(conent);
//        spannableString.setSpan(new ForegroundColorSpan(Color.RED),conent.indexOf("《"),conent.indexOf("》")+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        viewHandler.fragment_freight_item_textview_content.setText(spannableString);
//        SpannableString spansss = new SpannableString(conent.substring(conent.indexOf("《"),conent.indexOf("》")+1));
//        ClickableSpan clickttt = new ShuoMClickableSpan(conent.substring(conent.indexOf("《"),conent.indexOf("》")+1), context);
//        spansss.setSpan(clickttt, 0, conent.substring(conent.indexOf("《"),conent.indexOf("》")+1).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        viewHandler.fragment_freight_item_textview_content.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }
    class ViewHandler{
        TextView fragment_frieght_item_text_time,fragment_freight_item_textview_succeed,fragment_freight_item_textview_content,
                fragment_freight_item_textview_yf,fragment_freight_item_textview_award;
    }
}
