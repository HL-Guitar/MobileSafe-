package com.hl.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by UTT on 2018/7/3.
 */

public class SpUtil {
    public static SharedPreferences sp;

    /**
     * 写入boolean变量到sp中
     * @param ctx 上下文
     * @param key 存储的节点名陈
     * @param value 存储的节点值
     */
   public static void putBoolean(Context ctx,String key,boolean value){
        if (sp==null){
          sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);

        }
        sp.edit().putBoolean(key,value).commit();
   }

    /**
     * 从sp中读取Boolean的值
     * @param ctx
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context ctx,String key,boolean defValue){
       if (sp==null){
           sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);

       }
       return sp.getBoolean(key,defValue);
    }

    /**
     * 写入boolean变量到sp中
     * @param ctx 上下文
     * @param key 存储的节点名陈
     * @param value 存储的节点值
     */
    public static void putString(Context ctx,String key,String value){
        if (sp==null){
            sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);

        }
        sp.edit().putString(key,value).commit();
    }

    /**
     * 从sp中读取Boolean的值
     * @param ctx
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(Context ctx, String key, String defValue){
        if (sp==null){
            sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);

        }
        return sp.getString(key,defValue);
    }
}
