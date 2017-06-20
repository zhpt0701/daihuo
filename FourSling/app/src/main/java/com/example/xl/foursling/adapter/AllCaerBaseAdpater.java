package com.example.xl.foursling.adapter;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xl.foursling.R;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.StringUilts;
import com.example.xl.foursling.unity.AddCar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by admin on 2016/12/21.
 */
public class AllCaerBaseAdpater extends BaseAdapter{
    private Context context;
    private  ArrayList<AddCar.MessageBean> message;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private MyOnClickLisenter myOnClickLisenter;
    private String[] carType = {"小型客车","中型客车","大型客车","微型货车","轻型货车","中型货车","重型货车"};
    public AllCaerBaseAdpater(MyOnClickLisenter myOnClickLisenter,Application application, ArrayList<AddCar.MessageBean> message, ImageLoader imageLoader, DisplayImageOptions options) {
        context = application;
        this.message = message;
        this.imageLoader = imageLoader;
        this.options = options;
        this.myOnClickLisenter = myOnClickLisenter;
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_all_car_item,null);
            viewHandler.textView_sh = (TextView)view.findViewById(R.id.activity_all_car_item_sh);
            viewHandler.imageView_car = (ImageView)view.findViewById(R.id.activity_all_car_item_imageview);
            viewHandler.imageView_p = (ImageView)view.findViewById(R.id.activity_all_car_item_image_cp);
            viewHandler.imageView_x = (ImageView)view.findViewById(R.id.activity_all_car_item_image_cx);
            viewHandler.imageView_z = (ImageView)view.findViewById(R.id.activity_all_car_item_image_cz);
            viewHandler.textView_car_name = (TextView)view.findViewById(R.id.activity_all_car_item_car_name);
            viewHandler.textView_p = (TextView)view.findViewById(R.id.activity_all_car_item_textview_cp);
            viewHandler.textView_x = (TextView)view.findViewById(R.id.activity_all_car_item_textview_cx);
            viewHandler.textView_z = (TextView)view.findViewById(R.id.activity_all_car_item_textview_cz);
            viewHandler.btn = (Button)view.findViewById(R.id.activity_all_car_item_btn_select);
            view.setTag(viewHandler);
        }else {
            viewHandler = (ViewHandler)view.getTag();
        }
            switch (message.get(i).status){
                case 0:
                    viewHandler.imageView_p.setSelected(false);
                    viewHandler.imageView_x.setSelected(false);
                    viewHandler.imageView_z.setSelected(false);
                    viewHandler.textView_sh.setVisibility(View.GONE);
                    viewHandler.btn.setVisibility(View.VISIBLE);
                    if (message.get(i).usingStatus == Constants.ZERO){
                        viewHandler.btn.setSelected(true);
                    }else {
                        viewHandler.btn.setSelected(false);
                    }
                    break;
                case 1:
                    viewHandler.btn.setVisibility(View.GONE);
                    viewHandler.textView_sh.setVisibility(View.VISIBLE);
                    viewHandler.textView_sh.setText("审核中");
                    viewHandler.imageView_p.setSelected(true);
                    viewHandler.imageView_x.setSelected(true);
                    viewHandler.imageView_z.setSelected(true);
                    viewHandler.btn.setSelected(false);
                    break;
                case 2:
                    viewHandler.btn.setVisibility(View.GONE);
                    viewHandler.textView_sh.setVisibility(View.VISIBLE);
                    viewHandler.textView_sh.setText("审核失败");
                    viewHandler.imageView_p.setSelected(true);
                    viewHandler.imageView_x.setSelected(true);
                    viewHandler.imageView_z.setSelected(true);
                    viewHandler.btn.setSelected(false);
                    break;
            }
            viewHandler.btn.setOnClickListener(myOnClickLisenter);
            viewHandler.btn.setTag(i);
            imageLoader.displayImage(message.get(i).carPic,viewHandler.imageView_car,options);
            viewHandler.textView_car_name.setText(message.get(i).carName);
            viewHandler.textView_p.setText(message.get(i).carCode);
            viewHandler.textView_x.setText(carType[Integer.valueOf(message.get(i).carTypeCode)-1]);
            viewHandler.textView_z.setText(String.valueOf(StringUilts.doubleMoney((double)message.get(i).carWeight/1000))+"kg");
        return view;
    }
    class ViewHandler{
        ImageView imageView_car,imageView_p,imageView_x,imageView_z;
        TextView textView_p,textView_x,textView_z,textView_car_name,textView_sh;
        Button btn;
    }

//    /**
//     * 刷新item
//     * @param tag
//     * @param number
//     */
//    public void updateItem(Integer tag, int number,ListView listView) {
//        //只当要更新的view在可见的位置时才更新，不可见时，跳过不更新
//        int vb = listView.getFirstVisiblePosition();
//        Log.i("fkslkfksd","kfs;ldfksdfjsd;f"+tag);
//        if (tag-vb >= 0){
//            View view1 = listView.getChildAt(tag-vb);
//            ViewHandler holder = (ViewHandler) view1.getTag();
//            holder.btn = (Button)view1.findViewById(R.id.activity_all_car_item_btn_select);
//            if (message.get(tag).status == Constants.ZERO){
//                if (message.get(tag).usingStatus == Constants.ZERO){
//                    return;
//                }else {
//                    for (int i = 0;message.size()<i;i++) {
//                        message.get(i).usingStatus = Constants.ONE;
//                    }
//                    message.get(tag).usingStatus = number;
//                }
//            }else {
//                return;
//            }
////            if (ot.equals("A")){
////                holder.spinner.setVisibility(View.GONE);
////                holder.textView_ol.setText("已同意");
////                holder.textView_ol.setVisibility(View.VISIBLE);
////            }else if (ot.equals("R")){
////                holder.spinner.setVisibility(View.GONE);
////                holder.textView_ol.setText("已拒绝");
////                holder.textView_ol.setVisibility(View.VISIBLE);
////            }
////            arrayList.get(tag).setShifou(true);
////            arrayList.get(tag).setNum(ot);
//        }
//    }

    /**
     * 抽象方法
     */
    public static abstract class MyOnClickLisenter implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            myOnClickLisenter((Integer)view.getTag(),view);
        }

        protected abstract void myOnClickLisenter(Integer tag, View view);

    }
}
