package com.example.xl.foursling.adapter;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by admin on 2017/1/4.
 */
public class NavigationAdapter extends PagerAdapter{
    private Context context;
    private ArrayList<View> mList;
    public NavigationAdapter(Context context, ArrayList<View> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList != null){
            return mList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return true;
    }
}
