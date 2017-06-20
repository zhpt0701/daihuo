package com.example.xl.foursling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xl.foursling.BaseActivity;
import com.example.xl.foursling.R;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.unity.RouteContent;

import java.util.List;

/**
 * Created by admin on 2016/12/24.
 * 适配器
 */
public class RoutAdapter extends BaseAdapter{
    private Context context;
    private int state;
    private String content;
    private RouteOnClickLister routeOnClickLister;
    private List<RouteContent.MessageBean.RowsBean> rows;
    public RoutAdapter(Context context, int state, List<RouteContent.MessageBean.RowsBean> rows, RouteOnClickLister routeOnClickLister){
        this.context = context;
        this.state = state;
        this.routeOnClickLister = routeOnClickLister;
        switch (state){
            case 1:
                this.rows = rows;
                break;
            case 2:
                this.rows = rows;
                break;
            case 3:

                break;
            default:
                break;
        }
    }
    @Override
    public int getCount() {
        switch (state){
            case 1:
                if (rows != null){
                    return rows.size();
                }
                break;
            case 2:
                if (rows != null){
                    return rows.size();
                }
                break;
            case 3:

                break;
            default:
                break;
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        switch (state){
            case 1:
                if (rows != null){
                    return rows.get(i);
                }
                break;
            case 2:
                if (rows != null){
                    return rows.get(i);
                }
                break;
            case 3:

                break;
            default:
                break;
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
            view = LayoutInflater.from(context).inflate(R.layout.fragment_route_item,null);
            //时间
            viewHandler.fragment_route_text_time = (TextView)view.findViewById(R.id.fragment_route_text_time);
            viewHandler.fragment_route_item_line = (TextView)view.findViewById(R.id.fragment_route_item_line);
            viewHandler.fragment_route_item_line_one = (TextView)view.findViewById(R.id.fragment_route_item_line_one);
            //始发地
            viewHandler.fragment_route_text_start = (TextView)view.findViewById(R.id.fragment_route_text_start);
            viewHandler.fragment_route_text_start_con = (TextView)view.findViewById(R.id.fragment_route_text_start_con);
            //目的地
            viewHandler.fragment_route_text_end = (TextView)view.findViewById(R.id.fragment_route_text_end);
            viewHandler.fragment_route_text_end_con = (TextView)view.findViewById(R.id.fragment_route_text_end_con);
            //接单
            viewHandler.fragment_route_image_jd = (ImageView)view.findViewById(R.id.fragment_route_image_jd);
            viewHandler.fragment_route_text_jd = (TextView)view.findViewById(R.id.fragment_route_text_jd);
            //取单
            viewHandler.fragment_route_image_q = (ImageView)view.findViewById(R.id.fragment_route_image_q);
            viewHandler.fragment_route_text_q = (TextView)view.findViewById(R.id.fragment_route_text_q);
            //运输途中
            viewHandler.fragment_route_image_yh = (ImageView)view.findViewById(R.id.fragment_route_image_yh);
            viewHandler.fragment_route_text_yh = (TextView)view.findViewById(R.id.fragment_route_text_yh);
            //带货成功
            viewHandler.fragment_route_image_dh = (ImageView)view.findViewById(R.id.fragment_route_image_dh);
            viewHandler.fragment_route_text_dh = (TextView)view.findViewById(R.id.fragment_route_text_dh);

            viewHandler.fragment_route_imagebtn = (ImageButton)view.findViewById(R.id.fragment_route_imagebtn);
            viewHandler.fragment_route_frame = (FrameLayout)view.findViewById(R.id.fragment_route_frame);
            view.setTag(viewHandler);
        }else {
            viewHandler = (ViewHandler)view.getTag();
        }
        switch (state){
            case 1:
                viewHandler.fragment_route_imagebtn.setOnClickListener(routeOnClickLister);
                viewHandler.fragment_route_imagebtn.setTag(i);
                viewHandler.fragment_route_item_line_one.setVisibility(View.GONE);
                viewHandler.fragment_route_item_line.setVisibility(View.VISIBLE);
                viewHandler.fragment_route_frame.setVisibility(View.VISIBLE);
                viewHandler.fragment_route_imagebtn.setVisibility(View.VISIBLE);
                viewHandler.fragment_route_text_start.setText(rows.get(i).company_companyLon.substring(0,rows.get(i).company_companyLon.length()-4));
                viewHandler.fragment_route_text_end.setText(rows.get(i).company_companyLat.substring(0,rows.get(i).company_companyLat.length()-4));
                viewHandler.fragment_route_text_time.setText(rows.get(i).createDate);
                switch (rows.get(i).status){
                    case 1:
                        viewHandler.fragment_route_image_jd.setSelected(true);
                        viewHandler.fragment_route_text_jd.setSelected(true);
                        viewHandler.fragment_route_image_q.setSelected(false);
                        viewHandler.fragment_route_text_q.setSelected(false);
                        viewHandler.fragment_route_image_yh.setSelected(false);
                        viewHandler.fragment_route_text_yh.setSelected(false);
                        viewHandler.fragment_route_image_dh.setSelected(false);
                        viewHandler.fragment_route_text_dh.setSelected(false);
                        break;
                    case 2:
                        viewHandler.fragment_route_image_jd.setSelected(true);
                        viewHandler.fragment_route_text_jd.setSelected(true);
                        viewHandler.fragment_route_image_q.setSelected(true);
                        viewHandler.fragment_route_text_q.setSelected(true);
                        viewHandler.fragment_route_image_yh.setSelected(true);
                        viewHandler.fragment_route_text_yh.setSelected(true);
                        viewHandler.fragment_route_image_dh.setSelected(false);
                        viewHandler.fragment_route_text_dh.setSelected(false);
                        break;
                    case 3:
                        viewHandler.fragment_route_image_jd.setSelected(true);
                        viewHandler.fragment_route_text_jd.setSelected(true);
                        viewHandler.fragment_route_image_q.setSelected(true);
                        viewHandler.fragment_route_text_q.setSelected(true);
                        viewHandler.fragment_route_image_yh.setSelected(true);
                        viewHandler.fragment_route_text_yh.setSelected(true);
                        viewHandler.fragment_route_image_dh.setSelected(true);
                        viewHandler.fragment_route_text_dh.setSelected(true);
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                viewHandler.fragment_route_frame.setVisibility(View.GONE);
                viewHandler.fragment_route_imagebtn.setVisibility(View.GONE);
                viewHandler.fragment_route_item_line.setVisibility(View.GONE);
                viewHandler.fragment_route_item_line_one.setVisibility(View.VISIBLE);
                viewHandler.fragment_route_text_start.setText(rows.get(i).company_companyLon.substring(0,rows.get(i).company_companyLon.length()-4));
                viewHandler.fragment_route_text_end.setText(rows.get(i).company_companyLat.substring(0,rows.get(i).company_companyLat.length()-4));
                viewHandler.fragment_route_text_time.setText(rows.get(i).createDate);
                switch (rows.get(i).status){
                    case 1:
                        viewHandler.fragment_route_image_jd.setSelected(true);
                        viewHandler.fragment_route_text_jd.setSelected(true);
                        viewHandler.fragment_route_image_q.setSelected(false);
                        viewHandler.fragment_route_text_q.setSelected(false);
                        viewHandler.fragment_route_image_yh.setSelected(false);
                        viewHandler.fragment_route_text_yh.setSelected(false);
                        viewHandler.fragment_route_image_dh.setSelected(false);
                        viewHandler.fragment_route_text_dh.setSelected(false);
                        break;
                    case 2:
                        viewHandler.fragment_route_image_jd.setSelected(true);
                        viewHandler.fragment_route_text_jd.setSelected(true);
                        viewHandler.fragment_route_image_q.setSelected(true);
                        viewHandler.fragment_route_text_q.setSelected(true);
                        viewHandler.fragment_route_image_yh.setSelected(true);
                        viewHandler.fragment_route_text_yh.setSelected(true);
                        viewHandler.fragment_route_image_dh.setSelected(false);
                        viewHandler.fragment_route_text_dh.setSelected(false);
                        break;
                    case 3:
                        viewHandler.fragment_route_image_jd.setSelected(true);
                        viewHandler.fragment_route_text_jd.setSelected(true);
                        viewHandler.fragment_route_image_q.setSelected(true);
                        viewHandler.fragment_route_text_q.setSelected(true);
                        viewHandler.fragment_route_image_yh.setSelected(true);
                        viewHandler.fragment_route_text_yh.setSelected(true);
                        viewHandler.fragment_route_image_dh.setSelected(true);
                        viewHandler.fragment_route_text_dh.setSelected(true);
                        break;
                    default:
                        break;
                }
                break;
            case 3:
                viewHandler.fragment_route_item_line_one.setVisibility(View.VISIBLE);
                viewHandler.fragment_route_item_line.setVisibility(View.GONE);
                viewHandler.fragment_route_frame.setVisibility(View.GONE);
                viewHandler.fragment_route_imagebtn.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        return view;
    }
    class ViewHandler{
        TextView fragment_route_text_jd,fragment_route_text_q,fragment_route_text_yh,fragment_route_text_dh,
                fragment_route_text_time,fragment_route_text_start,fragment_route_text_start_con,
                fragment_route_text_end,fragment_route_text_end_con,fragment_route_item_line,fragment_route_item_line_one;
        ImageView fragment_route_image_dh,fragment_route_image_yh,fragment_route_image_q,fragment_route_image_jd;
        ImageButton fragment_route_imagebtn;
        FrameLayout fragment_route_frame;
    }
    //监听事件抽象类
    public static abstract class RouteOnClickLister implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            routeOnClickLister((Integer)view.getTag(),view);
        }

        protected abstract void routeOnClickLister(Integer tag, View view);
    }
}
