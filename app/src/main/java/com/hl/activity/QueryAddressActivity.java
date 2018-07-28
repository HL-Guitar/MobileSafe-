package com.hl.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hl.engine.AddressDao;
import com.hl.mobilesafe.R;

/**
 * Created by UTT on 2018/7/17.
 */

public class QueryAddressActivity extends Activity {
    private EditText et_phone;
    private Button bt_query;
    private TextView tv_query_result;
    private String mAddress;
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            //4,控件使用查询结果
            tv_query_result.setText(mAddress);
        };
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);
        initUI();
    }

    private void initUI() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        bt_query = (Button) findViewById(R.id.bt_query);
        tv_query_result = (TextView) findViewById(R.id.tv_query_result);

        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                if (!TextUtils.isEmpty(phone)){

                    query(phone);
                }else{
                    //抖动
                    Animation shake = AnimationUtils.loadAnimation(
                            getApplicationContext(), R.anim.shake);
                    et_phone.startAnimation(shake);

                    //震动
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    //整栋毫秒值
                    vibrator.vibrate(2000);
                    //规律震动(震动规则(不震动时间,震动时间,不震动时间,震动时间.......),重复次数)

                    vibrator.vibrate(new long[]{500,500,500,500},-1);

                }
            }
        });

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = et_phone.getText().toString();
                query(phone);
            }
        });
    }

    private void query(final String phone) {
        new Thread(){
            public void run() {
                mAddress = AddressDao.getAddress(phone,getApplicationContext());
                //3,消息机制,告知主线程查询结束,可以去使用查询结果
                mHandler.sendEmptyMessage(0);
            };
        }.start();
    }
}
