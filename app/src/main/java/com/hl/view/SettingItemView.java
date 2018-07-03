package com.hl.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hl.activity.R;

/**
 * Created by UTT on 2018/7/3.
 */

public class SettingItemView  extends RelativeLayout {

    private final TextView tv_des;
    private final CheckBox cb_box;

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml--->view	将设置界面的一个条目转换成view对象,直接添加到了当前SettingItemView对应的view中
        View.inflate(context, R.layout.setting_item_view, this);

        //等同于以下两行代码
		/*View view = View.inflate(context, R.layout.setting_item_view, null);
		this.addView(view);*/

        //自定义组合控件中的标题描述
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);

    }

    //单选框是否选中
    public boolean isCheck(){

        return cb_box.isChecked();
    }

    public void setCheck(boolean checked){
        cb_box.setChecked(checked);
        if (isCheck()){

        }
    }
}
