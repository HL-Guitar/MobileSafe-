package com.hl.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hl.utils.ConstantValue;
import com.hl.utils.SpUtil;

/**
 * Created by UTT on 2018/7/13.
 */

public class BootReceiver extends BroadcastReceiver{
    private static final String tag = "BootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(tag,"手机重启了并接收到了广播");
        //1,获取开机后手机的sim卡的序列号
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = tm.getSimSerialNumber();
        //2,sp中存储的序列卡号
        String sim_number = SpUtil.getString(context, ConstantValue.SIM_NUMBER, "");
        //3,比对不一致
        if(!simSerialNumber.equals(sim_number)){
            //4,发送短信给选中联系人号码
            String phone  = SpUtil.getString(context,ConstantValue.CONTACT_PHONE,"");
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phone, null, "sim change!!!", null, null);
        }

    }
}
