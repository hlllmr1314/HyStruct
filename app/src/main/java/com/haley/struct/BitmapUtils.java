package com.haley.struct;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by haley on 2018/8/11.
 */

public final class BitmapUtils {


    /**
     * 获取Bitmap（支持vector drawable）
     *
     * @param context
     * @param drawableId
     * @return
     */
    private static Bitmap getBitmap(Context context, @DrawableRes int drawableId) {
        Bitmap bitmap;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Drawable vectorDrawable = context.getDrawable(drawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        }
        return bitmap;
    }

    public static byte[] getBitmapBytes(Context context, @DrawableRes int drawableRes) {

        Bitmap bitmap = null;
        ByteArrayOutputStream stream = null;
        byte[] bitmapData;
        try {
            bitmap = getBitmap(context, drawableRes);
            stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            bitmapData = stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            bitmapData = null;
        } finally {
            //释放bitmap
            if (bitmap != null) {
                bitmap.recycle();
            }

            //释放IO流
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmapData;
    }

}
