package com.hl.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hl.mobilesafe.R;
import com.hl.utils.ConstantValue;
import com.hl.utils.Md5Util;
import com.hl.utils.PermissionUtils;
import com.hl.utils.SpUtil;
import com.hl.utils.ToastUtil;

import org.xutils.common.util.MD5;



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
        PermissionUtils.requestMorePermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
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
                    case 0:
                        showDialog();
                        break;
                    case 1:
                        checkPer(Manifest.permission.RECEIVE_BOOT_COMPLETED);
                        break;
                    case 8:
                        Intent it = new Intent(getApplicationContext(),SettingActivity.class);
                       startActivity(it);
                       break;
                }
            }
        });

    }

    private void checkPer(String permission) {

        PermissionUtils.checkAndRequestPermission(this,permission,1);

        PermissionUtils.checkPermission(this,permission, new PermissionUtils.PermissionCheckCallBack(){
            public void onHasPermission() {
                ToastUtil.show(getApplicationContext(),"成功授权");
            }

            public void onUserHasAlreadyTurnedDown(String... permission) {
                ToastUtil.show(getApplicationContext(),"拒绝授权");
            }

            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                PermissionUtils.toAppSetting(getApplicationContext());
            }
        } );
    }


    //显示m密码对话框
    private void showDialog() {
        String psd = SpUtil.getString(getApplication(),ConstantValue.MOBILE_SAFE_PSD,"");
        if (TextUtils.isEmpty(psd)){
        //1,初始设置密码对话框
            showSetPsdDialog();
        }else{
            //2,确认密码对话框
            showConfirmPsdDialog();
        }
    }

    //2,确认密码对话框
    private void showConfirmPsdDialog() {
        //因为要自己设置对画框样式,所以要使用dialog.setView（view）
        //view是自己编写的xml文件
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        final View view = View.inflate(getApplicationContext(),R.layout.dialog_confirm_psd,null);
        alertDialog.setView(view);
        alertDialog.show();
        Button btn_confim = (Button)view.findViewById(R.id.btn_confim);
        Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);

        btn_confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e1 = (EditText)view.findViewById(R.id.first_pwd);

                String pwd1 = e1.getText().toString();
                String pwd2 = SpUtil.getString(getApplicationContext(),ConstantValue.MOBILE_SAFE_PSD,"");
                if(Md5Util.encoder(pwd1).equals(pwd2)){
                    //进入应用手机防盗模块,开启一个新的activity
                    //Intent it =new Intent(getApplicationContext(),TestActivity.class);
                    Intent it = new Intent(getApplicationContext(), Setup1Activity.class);
                    startActivity(it);
                    //跳转到新的界面以后需要去隐藏对话框
                    alertDialog.dismiss();
                }else{
                    ToastUtil.show(getApplicationContext(),"密码错误...");
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    //显示密码设置框
    private void showSetPsdDialog() {
     //因为要自己设置对画框样式,所以要使用dialog.setView（view）
        //view是自己编写的xml文件
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        final View view = View.inflate(getApplicationContext(),R.layout.dialog_set_psd,null);
        alertDialog.setView(view);
        alertDialog.show();
        Button btn_confim = (Button)view.findViewById(R.id.btn_confim);
        Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);

        btn_confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e1 = (EditText)view.findViewById(R.id.first_pwd);
                EditText e2 = (EditText)view.findViewById(R.id.second_pwd);

                String pwd1 = e1.getText().toString();
                String pwd2 = e2.getText().toString();
                if(!TextUtils.isEmpty(pwd1) && !TextUtils.isEmpty(pwd2)){
                    if (pwd1.equals(pwd2)){
                        //进入应用手机防盗模块,开启一个新的activity
                        Intent it =new Intent(getApplicationContext(),Setup1Activity.class);
                        startActivity(it);
                        //跳转到新的界面以后需要去隐藏对话框
                        alertDialog.dismiss();
                        SpUtil.putString(getApplication(),ConstantValue.MOBILE_SAFE_PSD, Md5Util.encoder(pwd1));
                    }else{
                        ToastUtil.show(getApplicationContext(),"两次输入密码不一致！");
                    }
                }else{
                    ToastUtil.show(getApplicationContext(),"请输入密码...");
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
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
            TextView tv_title = (TextView)views.findViewById(R.id.tv_title);
            ImageView tv_icon = (ImageView) views.findViewById(R.id.tv_icon);
            tv_title.setText(mTitleStrs[i]);
            tv_icon.setImageResource(mDrawableIds[i]);

            return views;
        }
    }

}
