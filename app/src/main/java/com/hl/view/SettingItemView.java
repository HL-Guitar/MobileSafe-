package com.hl.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hl.mobilesafe.R;


/**
 * Created by UTT on 2018/7/3.
 */

public class SettingItemView  extends RelativeLayout {

    private final TextView tv_des;
    private final CheckBox cb_box;

    private static String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private String mDestitle;
    private String mDesoff;
    private String mDeson;

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //xml--->view	将设置界面的一个条目转换成view对象,直接添加到了当前SettingItemView对应的view中
        View.inflate(context, R.layout.setting_item_view, this);

        //等同于以下两行代码
		/*View view = View.inflate(context, R.layout.setting_item_view, null);
		this.addView(view);*/

        //自定义组合控件中的标题描述
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);

        //获取自定义属性的原生操作,写在此处,AttributeSet attrs对象中获取
        initAttrs(attrs);
        //获取布局文件中定义的字符串,赋值给自定义组合控件的标题
        tv_title.setText(mDestitle);

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

        //获取自定义属性的原生操作,写在此处,AttributeSet attrs对象中获取
        initAttrs(attrs);
        //获取布局文件中定义的字符串,赋值给自定义组合控件的标题
        tv_title.setText(mDestitle);
    }

    /**
     * 返回属性集合中的自定义属性集
     * @param attrs 属性集合
     */
    private void initAttrs(AttributeSet attrs) {
       	/*//获取属性的总个数
		Log.i(tag, "attrs.getAttributeCount() = "+attrs.getAttributeCount());
		//获取属性名称以及属性值
		for(int i=0;i<attrs.getAttributeCount();i++){
			Log.i(tag, "name = "+attrs.getAttributeName(i));
			Log.i(tag, "value = "+attrs.getAttributeValue(i));
			Log.i(tag, "分割线 ================================= ");
		}*/
        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");
        

    }

    //单选框是否选中
    public boolean isCheck(){
        return cb_box.isChecked();
    }

    //设置选中状态
    public void setCheck(boolean checked){
        cb_box.setChecked(checked);
        if (checked){
            tv_des.setText(mDeson);
        }else{
            tv_des.setText(mDesoff);
        }
    }
}
