package com.hl.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.hl.mobilesafe.R;
import com.hl.service.LocationService;
import com.hl.utils.ConstantValue;
import com.hl.utils.SpUtil;
import com.hl.utils.ToastUtil;

/**
 * Created by UTT on 2018/7/14.
 */

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
     //1.是否开启了手机防盗
        boolean open_security = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
        if(open_security){
            //2.获取短信内容
            Object[] pdus = (Object[])intent.getExtras().get("pdus");
            //3.循环遍历短信过程
            for (Object object:pdus){
                //获取短信对象
                SmsMessage sms = SmsMessage.createFromPdu((byte[])object);
                //获取短信对象的基本信息
                String messageBody = sms.getMessageBody();
                //判断是否包含播放音乐的关键字
                if (messageBody.contains("#*alarm*#")){
                    //播放音乐
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.xpg);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
                if(messageBody.contains("#*location*#")){
                    Log.e("smsreceiver","正在定位");
                    //8,开启获取位置服务
                    context.startService(new Intent(context,LocationService.class));
                }
                DevicePolicyManager  mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName mDeviceAdminSample = new ComponentName(context,AdminReceiver.class);
                if(messageBody.contains("#*lockscreen*#")){
                    if(mDPM.isAdminActive(mDeviceAdminSample)){
                        mDPM.lockNow();
                        mDPM.resetPassword("123", 0);
                    }else{
                        ToastUtil.show(context,"请先激活");
                    }
                }
                if(messageBody.contains("#*wipedate*#")){
                    if(mDPM.isAdminActive(mDeviceAdminSample)){
                      //  mDPM.wipeData(0);
 					   mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                    }else{
                        ToastUtil.show(context,"请先激活");                    }
                }
            }
        }

    }
}
