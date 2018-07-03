package com.hl.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {

    private String[] mTitleStrs; //功能菜单
    private int[] mDrawableIds; //图标id集合
    private GridView gv_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeactivity);
        initUI();
        initData();
    }

    /**
     * 初始化界面
     */
    private void initUI() {
         gv_home = (GridView)findViewById(R.id.gv_home);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //准备数据，文字九个,图片九张
        mTitleStrs = new String[]{
                "手机防盗","通信卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"
        };
        mDrawableIds = new int[]{
                R.drawable.home_safe,R.drawable.home_callmsgsafe,
                R.drawable.home_apps,R.drawable.home_taskmanager,
                R.drawable.home_netmanager,R.drawable.home_trojan,
                R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings
        };
        //九宫格控件设置阿达adapter
        gv_home.setAdapter(new MyAdapter());
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){

                    case 8:
                        Intent it = new Intent(getApplicationContext(),SettingActivity.class);
                       startActivity(it);
                       break;
                }
            }
        });

    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mTitleStrs.length;
        }

        @Override
        public Object getItem(int i) {
            return mTitleStrs[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View views = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            TextView tv_title = views.findViewById(R.id.tv_title);
            ImageView tv_icon = views.findViewById(R.id.tv_icon);

            tv_title.setText(mTitleStrs[i]);
            tv_icon.setBackgroundResource(mDrawableIds[i]);

            return views;
        }
    }


}
