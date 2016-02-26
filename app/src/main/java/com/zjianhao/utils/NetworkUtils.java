package com.zjianhao.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by 张建浩 on 2015/3/14.
 */
public class NetworkUtils {
    private static final String TAG = "NetworkUtils";
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    private Context context;


        public NetworkUtils(Context context) {
            this.context = context;
        }

        /**
         * 检测网络是否可用
         * @return
         */
        public boolean isNetworkConnected() {

            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();

            return ni != null && ni.isAvailable();
        }

        /**
         * 获取当前网络类型
         * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
         */

        public int getNetworkType() {
            int netType = 0;
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null) {
                return netType;
            }
            int nType = networkInfo.getType();
            if (nType == ConnectivityManager.TYPE_MOBILE) {
                String extraInfo = networkInfo.getExtraInfo();
                if(extraInfo != null){
                    if (extraInfo.toLowerCase().equals("cmnet")) {
                        netType = NETTYPE_CMNET;
                    } else {
                        netType = NETTYPE_CMWAP;
                    }
                }
            } else if (nType == ConnectivityManager.TYPE_WIFI) {
                netType = NETTYPE_WIFI;
            }
            return netType;
        }

    /**
     * 设置手机的移动数据
     */
    public boolean setMobileData(boolean pBoolean) {

        try {

            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            Class ownerClass = mConnectivityManager.getClass();

            Class[] argsClass = new Class[1];
            argsClass[0] = boolean.class;

            Method method = ownerClass.getMethod("setMobileDataEnabled", argsClass);

            method.invoke(mConnectivityManager, pBoolean);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.v(TAG, "-----------移动数据打开失败----------");
            return false;
        }
        return true;
    }


}
