package com.app.utiles.other;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

/**
 * Created by Administrator on 2016/7/19.
 */
public class Permission {
    private static Permission p;
    public static final String permission_record = Manifest.permission.RECORD_AUDIO;
    public static final String permission_location = Manifest.permission.ACCESS_FINE_LOCATION;
    private int targetSdkVersion;

    //获取实例
    public static Permission getInstance() {
        if (p == null) {
            p = new Permission();
        }
        return p;
    }


    private int getTargetSdkVersion(Context context) {
        int sdkVersion = 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            sdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return sdkVersion;
    }

    //检查权限
    public boolean checkSelfPermission(Context context, String permission) {
        if (targetSdkVersion == 0) {
            targetSdkVersion = getTargetSdkVersion(context);
        }
        boolean result = true;
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionState = -1;
            if (targetSdkVersion >= 23) {
                permissionState = ContextCompat.checkSelfPermission(context, permission);
            } else {
                permissionState = PermissionChecker.checkSelfPermission(context, permission);
            }
            result = permissionState == PermissionChecker.PERMISSION_GRANTED;
        }
        Log.e("授权", permission + ": " + result + " targetSdkVersion:" + targetSdkVersion);
        return result;
    }

    private int requestCode = 100;

    //请求权限 所申请授权必须要在androidManifest里声明过
    public void requestPermissions(Activity activity, String permission) {

        //去授权
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode++);
        Log.e("requestPermissions", "请求权限 :" + permission);
    }

    public void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String permissionsStr = "";
        if (permissions != null && permissions.length > 0) {
            for (String permission : permissions) {
                permissionsStr += permission + "\n";
            }
        }
        String grantResultsStr = "";
        if (grantResults != null && grantResults.length > 0) {
            for (int grant : grantResults) {
                grantResultsStr += grant + "\n";
            }
        }
        boolean isShow = ActivityCompat.shouldShowRequestPermissionRationale(activity,
                permissions[0]);
        if (isShow) {
            //是否要解释 为什么要用这个权限
        }
        Log.e("权限请求结果", "requestCode:" + requestCode
                + " 权限：" + permissionsStr
                + " 结果：" + grantResultsStr
                + " 是否要解释：" + isShow);
    }

    //到授权管理
    public void startAccredit(Context context ) {
         Intent intent = getAppDetailSettingIntent(context);
        context.startActivity(intent);

    }
    private Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
        //   action=  Settings.ACTION_MANAGE_OVERLAY_PERMISSION;
        localIntent.setAction(action);
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), "permission"));
        return localIntent;
    }
}
