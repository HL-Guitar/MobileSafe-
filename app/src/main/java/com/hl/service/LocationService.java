package com.hl.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;

import com.hl.utils.ConstantValue;
import com.hl.utils.SpUtil;

/**
 * Created by UTT on 2018/7/15.
 */

public class LocationService extends Service {

    public void onCreate() {
        super.onCreate();
        //获取手机经纬度坐标
        //1.获取位置管离着对象
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //2.以最优方式获取经纬度坐标
        Criteria criteria = new Criteria();
        //允许花费
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = lm.getBestProvider(criteria, true);
        //3,在一定时间间隔,移动一定距离后获取经纬度坐标
        MyLocationListener myLocationListener = new MyLocationListener();
        lm.requestLocationUpdates(bestProvider,0,0,myLocationListener);
    }

    class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            //经度
            double longitude = location.getLongitude();
            //纬度
            double latitude = location.getLatitude();
            //4,发送短信(添加权限)
            SmsManager sms = SmsManager.getDefault();
            String phone  = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE,"");
            sms.sendTextMessage(phone, null, "longitude = "+longitude+",latitude = "+latitude, null, null);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
    public IBinder onBind(Intent intent) {
        return null;
    }
}
