package com.zjianhao.getluckymoney;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zjianhao.constants.Constant;
import com.zjianhao.service.DownloadService;
import com.zjianhao.ui.AboutAty;
import com.zjianhao.ui.SettingAty;
import com.zjianhao.utils.UpdateUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = MainActivity.class.getName();
    @Bind(R.id.start_service)
    LinearLayout startService;
    @Bind(R.id.share_app_btn)
    Button shareAppBtn;
    @Bind(R.id.about_btn)
    Button aboutBtn;
    @Bind(R.id.user_setting_btn)
    Button userSettingBtn;
    private SystemBarTintManager tintManager;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x00:
                    showUpdateDialog();
                    break;
                case 0x01:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        setContentView(R.layout.activity_main);

        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,"8D5A8w1tOodDFI6fKzRDBMwa");

        ButterKnife.bind(this);
        startService.setOnClickListener(this);
        shareAppBtn.setOnClickListener(this);
        aboutBtn.setOnClickListener(this);
        userSettingBtn.setOnClickListener(this);


        checkUpdate();
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.titlebar_bg);
        }
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service:
                callAccessNotificationPermission();
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                break;
            case R.id.share_app_btn:
                onClickShare();
                break;
            case R.id.about_btn:
                Intent intent1 = new Intent(this, AboutAty.class);
                startActivity(intent1);
                break;
            case R.id.user_setting_btn:
                Intent intent2 = new Intent(this, SettingAty.class);
                startActivity(intent2);
                break;
        }

    }

    public void checkUpdate(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    if (UpdateUtil.canUpdate(Constant.CHECK_UPDATE_URL,MainActivity.this)){


                        handler.sendEmptyMessage(0x00);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("检测到新版本");
        builder.setMessage("是否下载更新?");
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                callPermission();

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }

    public void onClickShare() {

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_information));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "分享"));

    }

    public void callPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0x01);
                return;
            }else{
                //上面已经写好的拨号方法
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                startService(intent);
            }
        } else {
            Intent intent = new Intent(MainActivity.this, DownloadService.class);
            startService(intent);
        }
    }

    public void callAccessNotificationPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, 0x02);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0x01:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Intent intent = new Intent(MainActivity.this, DownloadService.class);
                    startService(intent);

                } else {
                    // Permission Denied
                    Toast.makeText(this, "权限获取失败，无法下载", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case 0x02:
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}
