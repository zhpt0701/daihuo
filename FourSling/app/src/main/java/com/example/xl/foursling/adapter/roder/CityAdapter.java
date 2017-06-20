package com.example.xl.foursling.adapter.roder;

import android.app.Application;
import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xl.foursling.R;
import com.example.xl.foursling.adapter.setting.POpupAdapter;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.unity.ProvinceAndCity;

import java.util.ArrayList;

/**
 * Created by admin on 2017/1/3.
 */
public class CityAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<ProvinceAndCity> city_content;

    public CityAdapter(Context context, ArrayList<ProvinceAndCity> city_content) {
        this.context = context;
        this.city_content = city_content;
    }

    @Override
    public int getCount() {
        if (city_content != null){
            return city_content.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (city_content != null){
            return city_content.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.popup_sercurity_item,null);
            viewHandler.popup_security_item_text = (TextView)view.findViewById(R.id.popup_security_item_text);
            view.setTag(viewHandler);
        }else {
            viewHandler = (ViewHandler)view.getTag();
        }
        viewHandler.popup_security_item_text.setText(city_content.get(i).city);
        return view;
    }
    class ViewHandler{
        TextView popup_security_item_text;
    }
}
