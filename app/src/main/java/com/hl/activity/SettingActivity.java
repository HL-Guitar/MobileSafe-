package com.hl.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.View;

import com.hl.utils.ConstantValue;
import com.hl.utils.SpUtil;
import com.hl.view.SettingItemView;

/**
 * Created by UTT on 2018/7/3.
 */

public class SettingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUpdate();
    }

    /**
     * 版本更新开关
     */
    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);

        //获取开关状态
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE,true);
        //是否选中,根据上一次存储的结果去做决定
        siv_update.setCheck(open_update);

        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果之前是选中的,点击过后,变成未选中
                //如果之前是未选中的,点击过后,变成选中

                //获取选中状态
                boolean ischeck = siv_update.isCheck();
                //将原有状态取反
                siv_update.setCheck(!ischeck);
                //将新的状态存入sp中
                SpUtil.putBoolean(getApplication(),ConstantValue.OPEN_UPDATE,true);
            }
        });
    }
}
