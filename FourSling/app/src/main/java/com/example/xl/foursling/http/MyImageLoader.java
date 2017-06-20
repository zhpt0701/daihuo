package com.example.xl.foursling.http;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.example.xl.foursling.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by admin on 2016/12/20.
 */

public class MyImageLoader {
    public static ImageLoader imageLoader;
    public static DisplayImageOptions options;
    public MyImageLoader(Context context){
        imageLoader = ImageLoader.getInstance();
        if (context != null){
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            //显示图片的配置
            options = new DisplayImageOptions.Builder()
//                    .displayer(new RoundedBitmapDisplayer(100))  //将图片显示为圆形
                    .showImageOnLoading(R.mipmap.avatar_default)
                    .showImageOnFail(R.mipmap.avatar_default)
                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .displayer(new CircleBitmapDisplayer())//将图片显示为圆形
                    .cacheInMemory(true)                               //启用内存缓存
                    .cacheOnDisk(true)
                    .build();
        }
    }
}
