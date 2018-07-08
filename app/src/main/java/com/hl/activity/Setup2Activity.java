package com.hl.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.hl.mobilesafe.R;
import com.hl.utils.ConstantValue;
import com.hl.utils.PermissionUtils;
import com.hl.utils.SpUtil;
import com.hl.utils.ToastUtil;
import com.hl.view.SettingItemView;

/**
 * Created by UTT on 2018/7/6.
 */

public class Setup2Activity extends Activity {
    private SettingItemView siv_sim_bound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUI();
    }

    /**
     * 初始化界面控件
     */
    private void initUI() {
        siv_sim_bound = (SettingItemView)findViewById(R.id.siv_sim_bound);
        //1,回显(读取已有的绑定状态,用作显示,sp中是否存储了sim卡的序列号)
        final String sim_number = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        //2,判断是否序列卡号为""
        if(TextUtils.isEmpty(sim_number)){
            siv_sim_bound.setCheck(false);
        }else{
            siv_sim_bound.setCheck(true);
        }
        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取原有状态
                boolean ischeck = siv_sim_bound.isCheck();

                if (!ischeck){
                    //存储序列卡号
                    PermissionUtils.checkAndRequestPermission(Setup2Activity.this, Manifest.permission.READ_PHONE_STATE, 1, new PermissionUtils.PermissionRequestSuccessCallBack() {
                        @Override
                        public void onHasPermission() {
                            setSimSeriesNum();
                        }
                    });
                }else{
                    //7,将存储序列卡号的节点,从sp中删除掉
                    SpUtil.remove(getApplicationContext(), ConstantValue.SIM_NUMBER);
                    siv_sim_bound.setCheck(!ischeck);
                }
            }
        });
    }

    public void nextPage(View v){
        String serialNumber = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        if (!TextUtils.isEmpty(serialNumber)){
            Intent it = new Intent(getApplication(),Setup3Activity.class);
            startActivity(it);
            finish();

        }else {
            ToastUtil.show(this,"请绑定sim卡");
        }

    }
    public void prePage(View v){
        Intent it = new Intent(getApplication(),Setup1Activity.class);
        startActivity(it);
        finish();

    }

    /**
     * 存储
     */
    public void setSimSeriesNum(){
        ToastUtil.show(this,"权限获取成功");
        //获取sim卡序列号的TelephonyManager
        TelephonyManager manager = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        //获取sim卡的序列号
        String simSerialNumber = manager.getSimSerialNumber();
        Log.e("sim",simSerialNumber);

        //存储
        SpUtil.putString(getApplicationContext(),ConstantValue.SIM_NUMBER,simSerialNumber);
        //将原有条目取反
        //设置给当前条目
        boolean ischeck = siv_sim_bound.isCheck();
        siv_sim_bound.setCheck(!ischeck);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        PermissionUtils.toAppSetting(Setup2Activity.this);
                    }
                } else {
                    setSimSeriesNum();
                }
            }
        }
    }
}
