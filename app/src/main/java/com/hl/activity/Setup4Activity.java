package com.hl.activity;

import android.Manifest;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.hl.mobilesafe.R;
import com.hl.receiver.AdminReceiver;
import com.hl.utils.ConstantValue;
import com.hl.utils.PermissionUtils;
import com.hl.utils.SpUtil;
import com.hl.utils.ToastUtil;

/**
 * Created by UTT on 2018/7/5.
 */

public class Setup4Activity extends BaseSetupActivity {
    private CheckBox cb_box;
    private Context context;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        context=this;
        initUI();
    }

    private void initUI() {
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        //1,是否选中状态的回显
        boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        //2,根据状态,修改checkbox后续的文字显示
        cb_box.setChecked(open_security);
        if(open_security){
            cb_box.setText("安全设置已开启");
        }else{
            cb_box.setText("安全设置已关闭");
        }

//		cb_box.setChecked(!cb_box.isChecked());
        //3,点击过程中,监听选中状态发生改变过程,
        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //5,根据开启关闭状态,去修改显示的文字
                if(isChecked){
                    PermissionUtils.requestMorePermissions(context,new String[]{Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},3);
                }else{
                    cb_box.setText("安全设置已关闭");
                }
            }
        });
        Button btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName mDeviceAdminSample = new ComponentName(getApplicationContext(),AdminReceiver.class);
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"超级管理员");
                startActivity(intent);
            }
        });
    }


    @Override
    public void showNext() {
        PermissionUtils.requestMorePermissions(context,new String[]{Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},3);
        Intent it = new Intent(getApplication(), SetupOverActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void showPre() {
        Intent it = new Intent(getApplication(),Setup3Activity.class);
        startActivity(it);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==3){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    cb_box.setChecked(false);
                    SpUtil.putBoolean(context, ConstantValue.OPEN_SECURITY, false);
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        PermissionUtils.toAppSetting(Setup4Activity.this);
                    }

                } else {
                    //4,isChecked点击后的状态,存储点击后状态
                    SpUtil.putBoolean(context, ConstantValue.OPEN_SECURITY, true);
                    cb_box.setText("安全设置已开启");
                    cb_box.setChecked(true);
                    boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);

                    if(open_security) {
                        SpUtil.putBoolean(this,ConstantValue.SETUP_OVER,true);
                        Intent it = new Intent(getApplication(), SetupOverActivity.class);
                        startActivity(it);
                        finish();
                        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
                    }else{
                        ToastUtil.show(getApplicationContext(), "请开启防盗保护。");
                    }
                }
            }
        }
    }
}
