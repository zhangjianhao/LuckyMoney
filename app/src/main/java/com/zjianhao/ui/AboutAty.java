package com.zjianhao.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zjianhao.constants.Constant;
import com.zjianhao.getluckymoney.R;
import com.zjianhao.service.DownloadService;
import com.zjianhao.utils.UpdateUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjianhao on 15-12-6.
 */
public class AboutAty extends Activity implements View.OnClickListener{
    @Bind(R.id.about_back_tv)
    Button aboutBackTv;
    @Bind(R.id.update_information_tv)
    TextView updateInformationTv;
    @Bind(R.id.check_update_rl)
    RelativeLayout checkUpdateRl;
    @Bind(R.id.stay_in_memory_rl)
    RelativeLayout stayInMemoryRl;
    @Bind(R.id.use_help_rl)
    RelativeLayout useHelpRl;

    @Bind(R.id.problem_commit_rl)
    RelativeLayout problemCommitRl;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x00:
                    updateInformationTv.setText(R.string.have_new_version);
                    showUpdateDialog();
                    break;
                case 0x01:
                    updateInformationTv.setText(R.string.have_new_version);
                    break;
                case 0x02:
                    updateInformationTv.setText(R.string.had_latest_version);
                    Toast.makeText(AboutAty.this,"已是最新版",Toast.LENGTH_SHORT).show();
                    break;
                case 0x03:
                    updateInformationTv.setText(R.string.had_latest_version);

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        setContentView(R.layout.about_main);
        ButterKnife.bind(this);
        aboutBackTv.setOnClickListener(this);
        checkUpdateRl.setOnClickListener(this);
        stayInMemoryRl.setOnClickListener(this);
        useHelpRl.setOnClickListener(this);
        problemCommitRl.setOnClickListener(this);
        checkUpdate(false);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.about_back_tv:
                finish();
                break;


            case R.id.check_update_rl:
               checkUpdate(true);


                break;
            case R.id.stay_in_memory_rl:
                Toast.makeText(this,"随后将添加该功能",Toast.LENGTH_SHORT).show();
                break;
            case R.id.use_help_rl:
                Intent intent = new Intent(this,HelpAty.class);
                startActivity(intent);
                break;
            case R.id.problem_commit_rl:
                Intent intent1 = new Intent(this,CommitProblemAty.class);
                startActivity(intent1);

                break;

        }

    }

    public void checkUpdate(final boolean flag){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    if (UpdateUtil.canUpdate(Constant.CHECK_UPDATE_URL,AboutAty.this)){
                        if (flag)
                        handler.sendEmptyMessage(0x00);
                        else
                            handler.sendEmptyMessage(0x01);
                    }
                    else{
                        if (flag)
                        handler.sendEmptyMessage(0x02);
                        else handler.sendEmptyMessage(0x03);
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
                Intent intent = new Intent(AboutAty.this, DownloadService.class);
                startService(intent);

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }
}
