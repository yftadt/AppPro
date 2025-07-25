package com.library.baseui.utile.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;


import com.library.baseui.activity.BaseApplication;
import com.library.baseui.utile.other.APKSignature;
import com.library.baseui.utile.time.DateUtile;

import java.util.TimeZone;


/**
 * Created by Administrator on 2017/8/2.
 */

public class APKInfo {
    private static APKInfo aPKInfo;
    //版本名称，版本号
    private String versionCode, versionName;
    //手机系统版本,app目标版本
    private int sdkVersion, targetSdkVersion;
    private Context context;
    public DisplayMetrics displayMetrics;
    private String appName;
    private String applicationId;

    public static APKInfo getInstance() {
        if (aPKInfo == null) {
            aPKInfo = new APKInfo();
        }
        return aPKInfo;
    }

    public APKInfo() {
        context = BaseApplication.context;
        displayMetrics = context.getResources().getDisplayMetrics();
        getVersion();
    }

    //应用签名校验
    public boolean isMd5Verify(Activity activity) {
        return new APKSignature(activity,getApplicationId()).
                isMd5("C690EA5B53D505B2E6500A389CF2B20C");
    }

    public String getAppName() {
        return appName;
    }

    private void getVersion() {
        try {
            PackageManager manager = context.getPackageManager();
            String name = context.getPackageName();
            PackageInfo info = manager.getPackageInfo(name, 0);
            applicationId = info.packageName;
            //版本名称
            versionName = info.versionName;
            //版本号
            versionCode = String.valueOf(info.versionCode);
            int labelRes = info.applicationInfo.labelRes;
            appName = context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();

        }
        //手机系统版本
        sdkVersion = Build.VERSION.SDK_INT;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            //app目标版本
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    //获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //===============================================================
    //dip转px
    public float getDIPTOPX(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, displayMetrics);
    }



    /**
     * 已测试：加载图片有效
     *
     * @param resourceId 资源id
     * @return 资源path(比如图片)
     */
    public String getResourcePath(int resourceId) {
        return "android.resource://" + context.getApplicationContext().getPackageName() + "/" + resourceId;
    }

    /**
     * @param resourceName 资源名称
     * @return 资源id
     */
    public int getResourceId(String resourceName) {
        ApplicationInfo applicantionInfo = context.getApplicationInfo();
        //取得该图片的id  (name 指定图片的名称，"drawable"指定图片存放的目录，appInfo.packageName指定应用程序的包名)
        int resID = context.getResources().getIdentifier(resourceName, "drawable", applicantionInfo.packageName);
        if (resID == 0) {
            resID = context.getResources().getIdentifier(resourceName, "mipmap", applicantionInfo.packageName);
        }
        return resID;
    }

    /**
     * @param name Assts下的资源名称（包括后缀名）
     * @return 资源Path
     */
    public static String getAsstsPath(String name) {
        String path = "file:///android_asset/" + name;
        return path;
    }

    //获取屏幕宽度
    public float getScreenWidthPixels() {
        return displayMetrics.widthPixels;
    }

    //获取屏幕高度
    public float getScreenHeightPixels() {
        return displayMetrics.heightPixels;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public int getSdkVersion() {
        return sdkVersion;
    }

    public int getTargetSdkVersion() {
        return targetSdkVersion;
    }

    //
    //Application 下的meta-data 值
    public String getMetaDataApplication(String key) {
        String value = "";
        try {
            ApplicationInfo appInfo = BaseApplication.context.getPackageManager()
                    .getApplicationInfo(BaseApplication.context.getPackageName(),
                            PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    //Activity 下的meta-data 值
    public String getMetaDataActivity(Activity activity, String key) {
        String value = "";
        try {
            ActivityInfo info = BaseApplication.context.getPackageManager()
                    .getActivityInfo(activity.getComponentName(),
                            PackageManager.GET_META_DATA);
            value = info.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    //Service 下的meta-data 值
    public String getMetaDataService(Class<?> serviceClass, String key) {
        String value = "";
        try {
            ComponentName cn = new ComponentName(BaseApplication.context, serviceClass);
            ServiceInfo info = BaseApplication.context.getPackageManager().getServiceInfo(cn, PackageManager.GET_META_DATA);
            value = info.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    //获取receiver 下的meta-data 值
    public String getMetaDataReceiver(Class<?> receiverClass, String key) {
        String value = "";
        try {
            ComponentName cn = new ComponentName(BaseApplication.context, receiverClass);
            ActivityInfo info = BaseApplication.context.getPackageManager().getReceiverInfo(cn, PackageManager.GET_META_DATA);
            value = info.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    private String timeZoneID;

    //获取时区id
    public String getTimeZoneId() {
        if (!TextUtils.isEmpty(timeZoneID)) {
            return timeZoneID;
        }
        TimeZone timeZone = TimeZone.getDefault();
        timeZoneID = timeZone.getID();
        //displayName = timeZone.getDisplayName();
        return timeZoneID;
    }

    //重置时区
    public void restTimeZone() {
        timeZoneID = "";
    }

}
