package com.zjianhao.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.util.Log;

import com.zjianhao.bean.App;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by zjianhao on 15-12-6.
 */
public class UpdateUtil {

    private final static String TAG = UpdateUtil.class.getName();
    private static App app;

    public static App getApp() {
        return app;
    }

    public static void setApp(App app) {
        UpdateUtil.app = app;
    }

    public static int getLocalVersionId(Context context) throws PackageManager.NameNotFoundException {
       return context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode;

    }

    public static String getLocalVersionName(Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
    }

    /**
     * 得到新版本的版本号
     * @return
     */

    public static int getRemoteVersionId(String url,Context context) throws IOException, XmlPullParserException, PackageManager.NameNotFoundException {
        URL url1 = new URL(url);
        URLConnection connection = url1.openConnection();
        List<App> apps = XmlParserUtil.parseXml(connection.getInputStream());
        if (apps.size()>0){
            setApp(apps.get(0));
            return apps.get(0).getVersionId();
        }
        else
            return getLocalVersionId(context);

    }

    /**
     * 检查是否可以更新
     * @param url 请求地址
     * @return true可以更新，false 没有新版本
     */
    public static boolean canUpdate(String url,Context context) throws PackageManager.NameNotFoundException, IOException, XmlPullParserException {
        int remoteId = getRemoteVersionId(url,context);
        int localId = getLocalVersionId(context);
        Log.v(TAG,"version id:"+remoteId);
        Log.v(TAG,"local version id:"+localId);
        if (remoteId>getLocalVersionId(context))
            return true;
        else return  false;
    }

    /**
     * 下载更新
     * @param url
     * @return
     */
    public static boolean update(String url){
        return true;
    }
}
