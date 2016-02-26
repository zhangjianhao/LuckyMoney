package com.zjianhao.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.zjianhao.constants.Constant;
import com.zjianhao.getluckymoney.R;
import com.zjianhao.utils.SharePreferenceUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TreeMap;

/**
 * Created by zjianhao on 15-11-6.
 */
public class GetMoneyService extends AccessibilityService {
    private final static String TAG = GetMoneyService.class.getName();
    private Timer timer;
    private final static int PACKET_MM = 0;
    private final static int PACKET_QQ = 1;
    private boolean autoClick = false;
    private boolean autoClick2 = false;
    private boolean autoClickNotification = false;
    private int qqCommandPacketLock = 0;
    private int messageRevocation = 0;
    private int messageRevocation2 = 0;
    private String revocationMessage = null;
    private int inputMessageFlag = 0;
    private boolean canRevocation = false;
    //可信度
    private int confidenceLevel = 0;
    private int protectQQCommandLock = 0;
    private boolean willDeleteMessage = false;
    private boolean needCallbackMessage = false;





    public String getCurrentActivity(){
        if (android.os.Build.VERSION.SDK_INT<21){
            ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTasks = manager .getRunningTasks(1);
            ActivityManager.RunningTaskInfo cinfo = runningTasks.get(0);
            ComponentName component = cinfo.topActivity;
            return component.getClassName();

        }
        else if (android.os.Build.VERSION.SDK_INT<23) {
            return getCurrentPkgName(this);
        }
        else
            return getProcessNew();





    }

    private String getProcessNew()  {
        String topPackageName = null;
        UsageStatsManager usage = (UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();

        List<UsageStats> stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*1000, time);
        if (stats != null) {
            SortedMap<Long, UsageStats> runningTask = new TreeMap<Long,UsageStats>();
            for (UsageStats usageStats : stats) {
                runningTask.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (runningTask.isEmpty()) {
//                Log.v(TAG,"running tast is empty");
                return null;
            }
            topPackageName =  runningTask.get(runningTask.lastKey()).getPackageName();
        }
        return topPackageName;
    }














    public String getCurrentPkgName(Context context) {
        ActivityManager.RunningAppProcessInfo currentInfo = null;
        Field field = null;
        int START_TASK_TO_FRONT = 2;
        String pkgName = null;
        try {
            field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
        } catch (Exception e) { e.printStackTrace(); }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
         List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo app:appList) {
            if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                Integer state = null;
                try {
                    state = field.getInt( app );
                } catch (Exception e) { e.printStackTrace(); }
                if (state != null && state == START_TASK_TO_FRONT) {
                    currentInfo = app;
                    break;
                }
            }
        }
        if (currentInfo != null) {
            pkgName = currentInfo.processName;
        }
        return pkgName;


//        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
//
//        List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
//
//        Log.i(TAG,"current_app"+tasks.get(0).processName);
//        return tasks.get(0).processName;
    }





    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.v(TAG, "服务已连接");
        AccessibilityServiceInfo info = getServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.packageNames = new String[]{"com.tencent.mm", "com.tencent.mobileqq"};
        setServiceInfo(info);
        qqCommandPacketLock = 0;
        needCallbackMessage = SharePreferenceUtils.getBooleanValue(getApplicationContext(),"message","can_callback_message");
//        Log.v(TAG,"need callback:"+needCallbackMessage);
//        timer  = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//              Log.v(TAG,"current acitivity:"+getCurrentActivity());
//            }
//        },0,2000);

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        confidenceLevel = 0;
        canRevocation = false;
        Log.v(TAG,"回调了");

        switch (eventType) {
            //第一步：监听通知栏消息
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        Log.i(TAG, "text:"+content);
                        if (content.contains(Constant.MM_LUCKY_MONEY)||content.contains(Constant.QQ_LUCKY_MONEY)) {
                            //模拟打开通知栏消息
                            if (event.getParcelableData() != null
                                    &&
                                    event.getParcelableData() instanceof Notification) {
                                Notification notification = (Notification) event.getParcelableData();
                                PendingIntent pendingIntent = notification.contentIntent;
                                try {
                                    String currentApp = getCurrentActivity();

                                    if (currentApp != null && currentApp.matches("com.\\w+launcher\\w+")){
                                        autoClickNotification = true;
                                    }
                                    pendingIntent.send();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                break;
            //第二步：监听是否进入红包消息界面
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                String className = event.getClassName().toString();
                Log.v(TAG,"class name:"+className);
                switch (className){
                    case "com.tencent.mm.ui.LauncherUI":
                        //开始抢红包
                        Log.v(TAG,"开始寻找微信红包");
                        if (autoClickNotification)
                        getPacket(PACKET_MM);
                        break;
                    case "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI":
                        //开始打开红包
//                        Log.v(TAG,"准备打开微信红包啦");
                        openPacket();
                        break;
                    case "com.tencent.mobileqq.activity.SplashActivity":
//                        Log.v(TAG,"开始进入qq界面");
                        qqCommandPacketLock = 0;

                        recycle(getRootInActiveWindow(),Constant.QQ_LUCKY_MONEY);
                        getPacket(PACKET_QQ);
                        break;
                    case "cooperation.qwallet.plugin.QWalletPluginProxyActivity":
                        qqCommandPacketLock = 0;
                        if (messageRevocation == 1)
                            messageRevocation ++;


                      Log.v(TAG,"出现了窗口");

                        if (autoClick) {
                            performGlobalAction(GLOBAL_ACTION_BACK);
                            autoClick = false;
                            if (autoClickNotification) {
                                performGlobalAction(GLOBAL_ACTION_HOME);
//                                performGlobalAction(GLOBAL_ACTION_RECENTS);
                                autoClickNotification = false;
                            }
                        }


                        break;
                }
                break;

            case AccessibilityEvent.TYPE_VIEW_SCROLLED:

//                    if ("com.tencent.mm.ui.LauncherUI".contains(getCurrentActivity()))
//                        getPacket(PACKET_MM);
//                    else if ("com.tencent.mobileqq.activity.SplashActivity".contains(getCurrentActivity())){
//                        getPacket(PACKET_QQ);
//                    }
                break;
           default:
                Log.v(TAG,"content update:"+getCurrentActivity());
                String currentActivity = getCurrentActivity();
//               getPacket(PACKET_QQ);
//               getPacket(PACKET_MM);
//               Log.v (TAG,"current contain:"+(currentActivity!= null&&currentActivity.contains("com.tencent.mobileqq.activity")));
//               if ("com.tencent.mm.ui.LauncherUI".contains(currentActivity)) {
//                       getPacket(PACKET_MM);
//                   }
               if (currentActivity == null){
                   Log.v(TAG,"------------current is null:------------");
               }
               getPacket(PACKET_QQ);
//               if ("com.tencent.mobileqq.activity.SplashActivity".contains(currentActivity)||(currentActivity!= null&&currentActivity.contains("com.tencent.mobileqq.activity"))){
//                   }
                break;

        }
    }


    /**
     * 查找到
     */
    private void openPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            autoClickNotification = false;
            recycle(nodeInfo, Constant.MM_GET_PACKET);
        }

    }

    private void getPacket(int type) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (type == PACKET_MM)
        recycle(rootNode, Constant.MM_LUCKY_MONEY2);
        else if (type == PACKET_QQ)
            recycle(rootNode,Constant.QQ_GET_PACKET);
    }

    private void recycle(AccessibilityNodeInfo info,String keywords) {

        if (info != null){
            if (info.getChildCount() == 0) {
                if(info.getText() != null){
                    Log.v(TAG,"qqcommandLock:"+qqCommandPacketLock+"protectlock"+protectQQCommandLock);
                    Log.v(TAG,info.getText().toString());
                    String uitext = info.getText().toString();
                    if (inputMessageFlag == 1){
//                        Log.v(TAG,"获取到输入的文本3："+uitext);
                        revocationMessage = uitext;
                        inputMessageFlag = 0;

                    }
                    if (uitext.equals(getString(R.string.copy))||uitext.equals(getString(R.string.forward))||uitext.equals(getString(R.string.collect))||uitext.equals(getString(R.string.delete))){
                        //识别关键字可信度
                        confidenceLevel++;
//                        Log.v(TAG,"confidence:"+confidenceLevel);
                    }

                    if (needCallbackMessage&&uitext.equals(getString(R.string.delete))){
                        //可信度大于等于3并且不可撤回,

                        if (confidenceLevel>=3 && !canRevocation&& messageRevocation2 >= 1){
//                            performGlobalAction(GLOBAL_ACTION_BACK);

                            messageRevocation2++;
                            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);

                        }
                       else if (messageRevocation2 >=2){
                            messageRevocation2 = 0;
                            autoClickNodeInfo(info);
                        }
//                        Log.v(TAG,"messageRevocaton2:"+messageRevocation2+" confidence:"+confidenceLevel+"canrevocation"+canRevocation);
                        //是删除并且要删除信息

                    }


                    if(keywords.equals(info.getText().toString())){
                        //这里有一个问题需要注意，就是需要找到一个可以点击的View
                        Log.i("demo", "Click" + ",isClick:" + info.isClickable());
                        autoClick = true;
                        autoClickNodeInfo(info);


                    }else if (uitext.contains(Constant.QQ_LUCKY_MONEY) && !uitext.equals(Constant.QQ_LUCKY_MONEY)){
                        autoClickNodeInfo(info);
                    }else if ((qqCommandPacketLock ==0&& Constant.QQ_COMMAND_PACKET.equals(info.getText().toString()))){
                        //点击拆开
                        qqCommandPacketLock = 1;
                        protectQQCommandLock = 0;//防止死锁变量
                        autoClick = true;
                        autoClickNodeInfo(info);

                    }else if (qqCommandPacketLock == 1&&Constant.QQ_PACKET_COMMAND_INPUT.equals(uitext)){
                            qqCommandPacketLock = 2;
                            inputMessageFlag = 1;
                            protectQQCommandLock = 1;
                            autoClick = true;
                            autoClickNodeInfo(info);

                    }else if (qqCommandPacketLock == 2 && getString(R.string.send).equals(info.getText().toString())){
                            qqCommandPacketLock = 0;
                            protectQQCommandLock = 0;
                            messageRevocation = 1;
                            autoClick = true;
                            autoClickNodeInfo(info);
                    }else if (messageRevocation >1&&info.getText().toString().equals(revocationMessage)){
                        if (needCallbackMessage){
                            messageRevocation2 ++;
                            info.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                        }


                    }else if (messageRevocation >=1&&getString(R.string.revocation).equals(uitext)){
                        if (needCallbackMessage){
                            canRevocation = true;
                            messageRevocation = -1;
                            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }

                    }else if (messageRevocation <0 && getString(R.string.confirm).equals(info.getText().toString())){
                        messageRevocation = 0;
                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);

                    }


                }

            } else {
                for (int i = 0; i < info.getChildCount(); i++) {
                    if(info.getChild(i)!=null){
                        recycle(info.getChild(i),keywords);
                    }
                }
            }
        }


    }

    private void autoClickNodeInfo(AccessibilityNodeInfo info) {
        autoClick = true;
        if (info.isClickable())
        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        else {
            AccessibilityNodeInfo parent = info.getParent();
            while(parent != null){
//            Log.i("demo", "parent isClick:"+parent.isClickable());
                if(parent.isClickable()){
                    autoClick = true;
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
                parent = parent.getParent();
            }
        }

    }




    @Override
    public void onInterrupt() {
        if (timer != null){
            timer.cancel();
            timer = null;
        }

    }
}
