package com.zjianhao.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zjianhao.getluckymoney.R;
import com.zjianhao.utils.SharePreferenceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjianhao on 16-1-1.
 */
public class SettingAty extends Activity implements View.OnClickListener{
    @Bind(R.id.help_back_tv)
    Button helpBackTv;
    @Bind(R.id.callback_command_message)
    Switch callbackCommandMessage;
    private  boolean state;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        setContentView(R.layout.setting_main);
        ButterKnife.bind(this);
        helpBackTv.setOnClickListener(this);

        callbackCommandMessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (state != isChecked)
                showChangeDialog(isChecked);
            }


        });
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.app_content_color);
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
    protected void onResume() {
        state = SharePreferenceUtils.getBooleanValue(getApplicationContext(),"message","can_callback_message");
        callbackCommandMessage.setChecked(state);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.help_back_tv:
                finish();
                break;

        }
    }

    private void showChangeDialog(final boolean state) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("需要重启抢红包服务设置才能生效");
        builder.setPositiveButton("去重启", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (state)
                    SharePreferenceUtils.saveBoolean(getApplicationContext(),"message","can_callback_message",true);
                else
                    SharePreferenceUtils.saveBoolean(getApplicationContext(),"message","can_callback_message",false);

                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
               callbackCommandMessage.setChecked(!state);

            }
        });
        builder.show();
    }


}

