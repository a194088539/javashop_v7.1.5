package com.enation.javashop.reactnative.android.addressselector.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Toast;

/**
 * DSView 工具类
 */

public class Utils {
    /**
     * 获取屏幕宽度，px
     *
     * @param  context 上下文
     * @return  宽
     */
    public static float getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度，px
     *
     * @param context 上下文
     * @return 高
     */
    public static float getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * Toast提示
     * @param context 上下文
     * @param message message
     */
    public static void toastL(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
