package com.hl.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hl.mobilesafe.R;
import com.hl.utils.StreamUtil;
import com.hl.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.lang.Thread;

public class Splash extends Activity {

    private TextView tv_version_name;
    private int mLocalVersionCode=0;
    private String mVersionDes;
    private String mDownloadUrl;
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;


    /**
     * 更新新版本的状态码
     */
    protected static final int UPDATE_VERSION = 100;
    /**
     * 进入应用程序主界面状态码
     */
    protected static final int ENTER_HOME = 101;

    /**
     * url地址出错状态码
     */
    protected static final int URL_ERROR = 102;
    protected static final int IO_ERROR = 103;
    protected static final int JSON_ERROR = 104;
    private RelativeLayout splash_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
        initData();
        initAnimation();

    }

    /**
     * 初始化动画效果
     */
    private void initAnimation() {
        AlphaAnimation alphaAnimation =new AlphaAnimation(0,1);
        alphaAnimation.setDuration(3000);
        splash_layout.startAnimation(alphaAnimation);

    }

    private Handler mHandlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_VERSION:
                    showUpdateDialog();
                      break;
                case ENTER_HOME:
                    //进入应用程序主界面,activity跳转过程
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtil.show(getApplicationContext(), "url异常");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtil.show(getApplicationContext(), "读取异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtil.show(getApplicationContext(), "json解析异常");
                    enterHome();
                    break;
                default:
                    enterHome();
            }
            super.handleMessage(msg);
        }
    };
    /**
     * 初始化数据
     */
    private void initData() {
        tv_version_name.setText("版本名称："+getVersionName());
        //获取本地版本号
        mLocalVersionCode=getVersionCode();
        //检查版本
        checkVersion();
    }

    /**
     * 获取版本名陈
     */
    private String getVersionName() {
        //1.获取包管理者
        PackageManager pm = getPackageManager();
        //2.从包管理者中获取指定的信息
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(),0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化UI
     */
    private void initUI() {
         tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        splash_layout = (RelativeLayout)findViewById(R.id.splash_layout);
    }

    /**
     * 返回版本号
     * @return 非0代表获取成功
     */
    private int getVersionCode(){
        //1.获取包管理者
        PackageManager pm = getPackageManager();
        //2.从包管理者中获取指定的信息
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(),0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * 进入主界面
     */
    protected void enterHome(){
        Intent it = new Intent(this,HomeActivity.class);
        startActivity(it);
        finish();
    }

    /**
     * 弹出对话框，提示用户更新
     */
    protected void showUpdateDialog(){

        //对话框是依赖于ACtivicity存在的
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置左上角的图标
        builder.setIcon(R.drawable.home_apps);
        //设置标题
        builder.setTitle("版本更新");
        //设置内容
        builder.setMessage(mVersionDes);

        //设置积极按钮
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              downloadAPK();
            }
        });

        //消极按钮
        builder.setNegativeButton("取消更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enterHome();
            }
        });

        //取消事件监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                enterHome();
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    /**
     * 下载apk
     */
    private void downloadAPK() {
        checkVersion();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String path = Environment.getExternalStorageDirectory().getPath()+File.separator;
            Log.e("ds",path);
            RequestParams params  = new RequestParams(mDownloadUrl);
            //定义下载路径
            params.setSaveFilePath(path);
            //自动为文件命名
           params.setAutoRename(true);
            x.http().post(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File file) {
                    installAPK(file);
                }

                @Override
                public void onError(Throwable throwable, boolean b) {

                }

                @Override
                public void onCancelled(CancelledException e) {

                }

                @Override
                public void onFinished() {

                }

                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {
                   Log.e("e","开始下载");
                }

                @Override
                public void onLoading(long l, long l1, boolean b) {

                }
            });

        }
    }

    /**
     * 调用安装的apk
     * @param file
     */
    private void installAPK(File file) {

        //系统应用界面,安装apk入口
        Intent it  =new Intent("android.intent.action.VIEW");
        it.addCategory("android.intent.category.DEFAULT");
        //设置数据源和安装类型
        it.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        startActivityForResult(it,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 检查版本更新
     */
    protected void checkVersion(){
        new Thread(new Runnable() {
            Message msg = Message.obtain();
            long startTime = System.currentTimeMillis();
            @Override
            public void run() {
                try {

                    //发送请求获取数据,参数则为请求json的链接地址
                    //http://192.168.13.99:8080/update74.json	测试阶段不是最优
                    //仅限于模拟器访问电脑tomcat

                    //1.封装url地址
                    URL url = new URL("http://192.168.56.1:8080/update.json");
                    //2.开启一个连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3.设置请求参数
                    //请求超时
                    connection.setConnectTimeout(2000);
                    //读取超时
                    connection.setReadTimeout(2000);
                    //默认请求为get
                    connection.setRequestMethod("POST");

                    Log.i("e","开始请求");
                    //请求成功
                    if (connection.getResponseCode() == 200){
                        //以流的形式将数据获取下来
                        InputStream is = connection.getInputStream();
                        //将流转换为字符串
                       String json =  StreamUtil.streamToString(is);
                        //Log.i("e",json);
                        JSONObject jsonObject = new JSONObject(json);
                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");

                        //日志打印
                        Log.i("g", versionName);
                        Log.i("g", mVersionDes);
                        Log.i("g", versionCode);
                        Log.i("g", mDownloadUrl);

                        //对比版本号
                        if (mLocalVersionCode<Integer.parseInt(versionCode)){
                            //提示用户更新，弹出对话框（UI）消息机制
                            msg.what = UPDATE_VERSION;

                        }else{
                            //进入主界面
                            msg.what = ENTER_HOME;
                        }
                    }


                } catch (MalformedURLException e) {
                    msg.what = URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = IO_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = JSON_ERROR;
                    e.printStackTrace();
                }finally {
                    //指定休眠时间,如果网络请求超过4秒,则不做作处理
                    long endTime = System.currentTimeMillis();
                    if((endTime-startTime)<3000){
                        try {
                            Thread.sleep(3000-(endTime-startTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mHandlder.sendMessage(msg);
                }
            }
        }).start();
    }





    public void toast(String content){
        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT).show();
    }

}
