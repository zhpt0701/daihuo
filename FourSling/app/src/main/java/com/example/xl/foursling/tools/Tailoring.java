package com.example.xl.foursling.tools;

import android.content.Context;
import android.graphics.*;
import android.util.Log;

import java.io.*;

/**
 * Created by Administrator on 2016/4/28 0028.
 */
public class Tailoring {
    /**
     * 图片切圆
     * @param bitmap1
     * @return
     */
    public static Bitmap phototailoring(Bitmap bitmap1){
        int width = bitmap1.getWidth();
        int height = bitmap1.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap1, src, dst, paint);
        return output;
    }

    /**
     * 相册图片切圆
     * @param bitmap1
     * @param context
     * @return
     */
//    public static Bitmap phototailoring_ol(Bitmap bitmap1,Context context){
//        int width = bitmap1.getWidth();
//        int height = bitmap1.getHeight();
//        float roundPx;
//        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
//        if (width <= height) {
//            roundPx = width / 2;
//            top = 0;
//            bottom = width;
//            left = 0;
//            right = width;
//            height = width;
//            dst_left = 0;
//            dst_top = 0;
//            dst_right = width;
//            dst_bottom = width;
//        } else {
//            roundPx = height / 2;
//            float clip = (width - height) / 2;
//            left = clip;
//            right = width - clip;
//            top = 0;
//            bottom = height;
//            width = height;
//            dst_left = 0;
//            dst_top = 0;
//            dst_right = height;
//            dst_bottom = height;
//        }
//        Bitmap output1 = Bitmap.createBitmap(width,
//                height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output1);
//        final int color = 0xff424242;
//        final Paint paint = new Paint();
//        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
//        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
//        final RectF rectF = new RectF(dst);
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(color);
//        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap1, src, dst, paint);
//        Bitmap bitmp2 = ziphoto(output1);
//        String path = photo(bitmp2);
//        Bitmap bitmap_ol = null;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
//        options.inJustDecodeBounds = false;
//        bitmap_ol=BitmapFactory.decodeFile(path, options);
//        return bitmap_ol;
//    }
    /**
     * 图片压缩
     * @param bitmap2
     * @return
     */
    public static String ziphoto(Bitmap bitmap2){
        //图片允许最大空间   单位：KB
        double maxSize =800.00;
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length/1024;
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitmap2 = zoomImage(bitmap2, bitmap2.getWidth() / Math.sqrt(i),
                    bitmap2.getHeight() / Math.sqrt(i));
        }
        return photo(bitmap2);
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 存储图片路径
     * @param bitmap
     * @return
     */
    public static String photo(Bitmap bitmap){
        File file=new File("/sdcard/"+System.currentTimeMillis()+".png");
        try {
            FileOutputStream out=new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)){
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i( "&(ULL:<", "74362984" + file.toString());
        return file.toString();
    }
    /**
     * 品论图片压缩
     * @param
     * @return
     */
    public static String ziphoneluntan(String bit){
        Log.i("this","83472394823"+bit);
        BitmapFactory.Options options = new BitmapFactory.Options();
        //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
        options.inJustDecodeBounds = false;
        Bitmap bitmap_ol=BitmapFactory.decodeFile(bit, options);
        //图片允许最大空间   单位：KB
        double maxSize =200.00;
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap_ol.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length/1024;
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitmap_ol = zoomImage(bitmap_ol, bitmap_ol.getWidth() / Math.sqrt(i),
                    bitmap_ol.getHeight() / Math.sqrt(i));
        }
        String path = photo(bitmap_ol);
        return path;
    }

}
