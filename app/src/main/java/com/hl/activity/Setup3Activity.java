package com.hl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hl.mobilesafe.R;
import com.hl.utils.ConstantValue;
import com.hl.utils.SpUtil;
import com.hl.utils.ToastUtil;

/**
 * Created by UTT on 2018/7/5.
 */

public class Setup3Activity extends BaseSetupActivity {
    private EditText et_phone_number;
    private Button bt_select_number;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initUI();
    }

    @Override
    public void showNext() {
        String phone = et_phone_number.getText().toString();
        if(!TextUtils.isEmpty(phone)){
            Intent it = new Intent(getApplication(),Setup4Activity.class);
            startActivity(it);
            finish();
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
            //如果现在是输入电话号码,则需要去保存
            SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
        }else{
            ToastUtil.show(this,"请输入安全号码");
        }

    }

    @Override
    public void showPre() {
        Intent it = new Intent(getApplication(),Setup2Activity.class);
        startActivity(it);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }


    /**
     * 初始化界面控件
     */
    private void initUI() {
        //显示电话号码的输入框
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        //获取联系人电话号码回显过程
        String phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
        et_phone_number.setText(phone);
        //点击选择联系人的对话框
        bt_select_number = (Button) findViewById(R.id.bt_select_number);
        bt_select_number.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(),ContactListActivity.class);
                startActivityForResult(it,0);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null){
            //1,返回到当前界面的时候,接受结果的方法
            String phone = data.getStringExtra("phone");
            //2,将特殊字符过滤(中划线转换成空字符串)
            phone = phone.replace("-", "").replace(" ", "").trim();
            et_phone_number.setText(phone);
            //3.存储联系人到sp中
            SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}
