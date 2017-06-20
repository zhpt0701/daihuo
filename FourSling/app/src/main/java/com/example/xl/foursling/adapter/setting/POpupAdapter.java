package com.example.xl.foursling.adapter.setting;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xl.foursling.R;

import java.util.List;

/**
 * Created by admin on 2016/12/29.
 */
public class POpupAdapter extends BaseAdapter{
    private Context context;
    private List<String> list;
    public POpupAdapter(Context context, List<String> list) {
        this.context =context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (list != null){
            return list.get(i);
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
        viewHandler.popup_security_item_text.setText(list.get(i));
        return view;
    }
    class ViewHandler{
        TextView popup_security_item_text;
    }
}
