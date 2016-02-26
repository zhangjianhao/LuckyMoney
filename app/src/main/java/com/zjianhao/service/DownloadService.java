package com.zjianhao.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;
import com.zjianhao.constants.Constant;
import com.zjianhao.getluckymoney.R;
import com.zjianhao.utils.UpdateUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zjianhao on 15-12-12.
 */
public class DownloadService extends Service {
    private final static String TAG = DownloadService.class.getName();
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder  mNotification;
    private Timer mTimer;
    private int downloadProgress = 0;
    float lastProgress = 0;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x00:
                    downloadProgress = msg.arg1;
                    Log.v(TAG,"downloadprogress:"+downloadProgress);
                    break;
                case 0x01:
                    if (mTimer != null)
                        mTimer.cancel();
                    mNotification.setProgress(100,100,false);
                    mNotification.setContentText("下载完成，点击安装");

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    File file = new File("/sdcard/luckymoney/download/"+UpdateUtil.getApp().getFileName());
                    if (file.exists()){
                        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    }
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mNotification.setContentIntent(pendingIntent);
                    mNotificationManager.notify(1,mNotification.build());
                    stopSelf();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG,"download 服务开启");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new NotificationCompat.Builder(this);
        mNotification.setSmallIcon(R.drawable.ic_launcher);
        mNotification.setContentText("0%");
        mNotification.setProgress(100,0,false);
        mNotificationManager.notify(1,mNotification.build());
        download();

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.v(TAG,"progres run"+downloadProgress);
                mNotification.setContentText(downloadProgress+"%");
                mNotification.setProgress(100,downloadProgress,false);
                mNotificationManager.notify(1,mNotification.build());

            }
        },0,200);


    }

    public boolean download(){




        Log.v(TAG,"filename"+UpdateUtil.getApp().getFileName());

        File fileDir = new File("/sdcard/luckymoney/download");
        if (!fileDir.exists()){
            Log.v(TAG,"file dir not exist");
            fileDir.mkdirs();
        }
        new OkHttpRequest.Builder()
                .url(Constant.DOWNLOAD_URL)
                .destFileDir("/sdcard/luckymoney/download")
                .destFileName(UpdateUtil.getApp().getFileName())
                .download(new ResultCallback<Object>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.v(TAG,"下载出错");
                        e.printStackTrace();

                    }

                    @Override
                    public void onResponse(Object response) {

                    }


                    @Override
                    public void inProgress(float progress) {
//                        Log.v(TAG,"progress:"+progress);
                        if (progress<1){
                            lastProgress = progress;
                            Message message = new Message();
                            message.what = 0x00;
                            message.arg1 = (int)(progress*100);
                            mHandler.sendMessage(message);
                        }
                        else{
                           mHandler.sendEmptyMessage(0x01);

                        }
                    }
                });
        return true;


    }
}
