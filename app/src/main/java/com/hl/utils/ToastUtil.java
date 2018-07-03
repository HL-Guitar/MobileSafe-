package com.hl.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by UTT on 2018/7/2.
 */

public class ToastUtil {

    public static void show(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();

    }

}
