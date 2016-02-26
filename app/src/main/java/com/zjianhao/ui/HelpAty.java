package com.zjianhao.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zjianhao.constants.Constant;
import com.zjianhao.getluckymoney.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjianhao on 15-12-6.
 */
public class HelpAty extends Activity {
    private final static String TAG = HelpAty.class.getName();
    @Bind(R.id.help_back_tv)
    Button helpBackTv;
    @Bind(R.id.help_webview)
    WebView helpWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        setContentView(R.layout.help_main);
        ButterKnife.bind(this);
        helpBackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         Log.v(TAG,Constant.HELP_URL);
        helpWebview.loadUrl(Constant.HELP_URL);
        helpWebview.setVisibility(View.VISIBLE);
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
}
