package com.example.xl.foursling.tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.example.xl.foursling.activityes.mesages.RuleActivity;

/**
 * Created by admin on 2016/12/24.
 * textveiw中给某些字段设置onclick事件
 */

public class ShuoMClickableSpan extends ClickableSpan {

    String string;
    Context context;
    public ShuoMClickableSpan(String str,Context context){
        super();
        this.string = str;
        this.context = context;
    }


    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.BLUE);
    }


    @Override
    public void onClick(View widget) {
        Intent intent = new Intent();
        intent.setClass(context, RuleActivity.class);
        context.startActivity(intent);
    }

}