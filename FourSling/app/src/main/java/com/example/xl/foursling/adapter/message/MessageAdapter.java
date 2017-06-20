package com.example.xl.foursling.adapter.message;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.xl.foursling.R;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.Messagees;

import java.util.List;

/**
 * Created by admin on 2016/12/26.
 */
public class MessageAdapter extends BaseSwipeAdapter{
    private Context context;
    private List<Messagees.MessageBean> message;
    private LayoutOnClickLister layoutOnClickLister;
    private DeleteOnClickLister deleteOnClickLister;
    public static SwipeLayout swipeLayout;
    private LinearLayout linearLayout;

    public MessageAdapter(Context context, List<Messagees.MessageBean> message,LayoutOnClickLister layoutOnClickLister,DeleteOnClickLister deleteOnClickLister) {
        this.context = context;
        this.message = message;
        this.layoutOnClickLister = layoutOnClickLister;
        this.deleteOnClickLister = deleteOnClickLister;
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
    // SwipeLayout的布局id
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_message_item,
                parent, false);
        swipeLayout = (SwipeLayout) v
                .findViewById(getSwipeLayoutResourceId(position));
        linearLayout = (LinearLayout)v.findViewById(R.id.ll_menu);
        swipeLayout.setOnClickListener(layoutOnClickLister);
        linearLayout.setOnClickListener(deleteOnClickLister);
        swipeLayout.setTag(position);
        linearLayout.setTag(position);
        // 当隐藏的删除menu被打开的时候的回调函数
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                swipeLayout.setClickable(false);
            }
        });
        // 双击的回调函数
//        swipeLayout
//                .setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
//                    @Override
//                    public void onDoubleClick(SwipeLayout layout,
//                                              boolean surface) {
//
//                    }
//                });
        // 添加删除布局的点击事件
//        v.findViewById(R.id.ll_menu).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//                //点击完成之后，关闭删除menu
//                swipeLayout.close();
//            }
//        });
        return v;
    }
    //对控件的填值操作独立出来了，我们可以在这个方法里面进行item的数据赋值
    @Override
    public void fillValues(int position, View convertView) {
        TextView t = (TextView) convertView.findViewById(R.id.activity_message_item_http);
        TextView time = (TextView)convertView.findViewById(R.id.activity_message_item_time);
        TextView activity_message_item_textview_message = (TextView)convertView.findViewById(R.id.activity_message_item_textview_message);
        if (message.get(position).readCode == Constants.ZERO){
            activity_message_item_textview_message.setSelected(false);
        }else {
            activity_message_item_textview_message.setSelected(true);
        }
        t.setText(message.get(position).messageUrl);
        time.setText(StringUilts.transfromTime(message.get(position).createDate));
    }
    //点击打开浏览器
    public static abstract class LayoutOnClickLister implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            layoutOnClickLister((Integer)view.getTag(),view);
        }
        protected abstract void layoutOnClickLister(Integer tag, View view);
    }
    //点击删除
    public static abstract class DeleteOnClickLister implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            deleteOnClickLister((Integer)view.getTag(),view);
        }

        protected abstract void deleteOnClickLister(Integer tag, View view);
    }
}
