package com.hl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.hl.mobilesafe.R;

/**
 * Created by UTT on 2018/7/5.
 */

public class Setup1Activity extends BaseSetupActivity {


    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

    }

    @Override
    public void showNext() {
        Intent it = new Intent(getApplication(),Setup2Activity.class);
        startActivity(it);
        finish();
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }

    @Override
    public void showPre() {

    }


}
