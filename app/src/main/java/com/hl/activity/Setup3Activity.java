package com.hl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hl.mobilesafe.R;

/**
 * Created by UTT on 2018/7/5.
 */

public class Setup3Activity extends Activity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }
    public void nextPage(View v){
        Intent it = new Intent(getApplication(),Setup4Activity.class);
        startActivity(it);
        finish();

    }
    public void prePage(View v){
        Intent it = new Intent(getApplication(),Setup2Activity.class);
        startActivity(it);
        finish();

    }
}
