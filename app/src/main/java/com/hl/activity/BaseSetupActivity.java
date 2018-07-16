package com.hl.activity;

import android.app.Activity;
import android.gesture.Gesture;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by UTT on 2018/7/9.
 */

public abstract class BaseSetupActivity extends Activity {
    //1.定义一个手势获取器
    private GestureDetector detector;
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //实例化这个手势识别器
        detector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                //屏蔽在X滑动很慢的情形

                if(Math.abs(velocityX)<200){
                    Toast.makeText(getApplicationContext(), "滑动得太慢了", 0).show();
                    return true;
                }



                if((e2.getRawX() - e1.getRawX())> 200 ){
                    //显示上一个页面：从左往右滑动
                   // System.out.println("显示上一个页面：从左往右滑动");
                    showPre();
                    return true;

                }

                if((e1.getRawX()-e2.getRawX()) > 200 ){
                    //显示下一个页面：从右往左滑动
                  //  System.out.println("显示下一个页面：从右往左滑动");
                    showNext();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    public abstract  void showNext();
    public abstract  void  showPre();
    /**
     * 下一步的点击事件
     * @param view
     */
    public void nextPage(View view){
        showNext();

    }

    /**
     *   上一步
     * @param view
     */
    public void prePage(View view){
        showPre();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
