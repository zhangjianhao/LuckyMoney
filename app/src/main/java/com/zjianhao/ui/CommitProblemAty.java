package com.zjianhao.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zjianhao.getluckymoney.R;
import com.zjianhao.utils.NetworkUtils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjianhao on 15-12-6.
 */
public class CommitProblemAty extends Activity implements View.OnClickListener {
    private final static String TAG = CommitProblemAty.class.getName();
    @Bind(R.id.commit_back_tv)
    Button commitBackTv;
    @Bind(R.id.user_suggestions_et)
    EditText userSuggestionsEt;
    @Bind(R.id.commit_problem_btn)
    Button commitProblemBtn;
    private int commitCount = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        setContentView(R.layout.commit_problem_main);
        ButterKnife.bind(this);
        commitBackTv.setOnClickListener(this);
        commitProblemBtn.setOnClickListener(this);

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
            case R.id.commit_back_tv:
                finish();
                break;
            case R.id.commit_problem_btn:
                if (TextUtils.isEmpty(userSuggestionsEt.getText())){
                    Toast.makeText(CommitProblemAty.this,R.string.input_not_empty,Toast.LENGTH_SHORT).show();
                }else {
                    NetworkUtils networkUtils = new NetworkUtils(this);
                    if (networkUtils.isNetworkConnected()){

                        if (commitCount>1){
                            Toast.makeText(CommitProblemAty.this,R.string.commit_too_frequent,Toast.LENGTH_SHORT).show();

                        }else {
                            String suggestion = userSuggestionsEt.getText().toString().trim();
                            if (suggestion.length() < 10){
                                Toast.makeText(CommitProblemAty.this,R.string.commit_info_too_short,Toast.LENGTH_SHORT).show();
                                return ;
                            }
                            callPermission();
                        }

                    }else {
                        Toast.makeText(CommitProblemAty.this,R.string.please_check_network,Toast.LENGTH_SHORT).show();

                    }




                }
                break;
        }

    }


    public void commitProblems(){

        String suggestion = userSuggestionsEt.getText().toString().trim();


        String title = "用户号码为："+getPhoneNumber()+"\n反馈内容为：\n";
        suggestion = title+"\t"+suggestion;
        sendEmail(suggestion);
        commitCount++;
        userSuggestionsEt.setText("");
        Toast.makeText(CommitProblemAty.this,R.string.thanks_feedback,Toast.LENGTH_SHORT).show();

    }

    public void callPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},0x01);
                return;
            }else{
                //上面已经写好的拨号方法
               commitProblems();
            }
        } else {
            //上面已经写好的拨号方法
           commitProblems();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0x01:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    commitProblems();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "权限获取失败，无法反馈", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private String getPhoneNumber(){
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (manager == null)
            return null;
        return manager.getLine1Number();

    }

    public void sendEmail(final String content){

        new Thread(new Runnable() {
            @Override
            public void run() {
                send("1519503862@qq.com",getString(R.string.email_subject),content);
            }
        }).start();

    }

    public boolean  send(String to,String subject,String content) {

        //第一步：创建propeties
        Properties props = new Properties();
        props.put("mail.host","smtp.163.com");
        props.put("mail.smtp.auth","true");
        //第二步：获取用户名和密码进行认证
        Authenticator authenticator = new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // TODO Auto-generated method stub
                return new PasswordAuthentication("13788583917", "19941111928473");
            }

        };


        //第三步：获取session对象
        Session session = Session.getInstance(props, authenticator);
        //第四步：设置邮件发送信息，并添加附件
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("13788583917@163.com"));

            message.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(to));
            message.setSubject(subject,"utf-8");
            MimeMultipart mp = new MimeMultipart();
//
            MimeBodyPart part = new MimeBodyPart();
            part.setContent(content,"text/html;charset=utf-8");
            mp.addBodyPart(part);
            message.setContent(mp);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
            Log.v(TAG, "发送异常");
            return false;
        }
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            Log.v(TAG, "发送异常");
            // TODO Auto-generated catch block
            return false;
        }

        return true;



    }
}
