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
import android.support.v4.content.PermissionChecker;


/**
 * Created by Administrator on 2016/7/19.
 */
public class PermissionManager {
    private static  PermissionManager p;
    public static final String permission_record = Manifest.permission.RECORD_AUDIO;
    public static final String permission_location = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String permission_camera = Manifest.permission.CAMERA;
    private int targetSdkVersion;

    public static PermissionManager getInstance() {
        if (p == null) {
            p = new  PermissionManager();
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

    public int checkSelfPermission(Context context, String permission) {
        if (targetSdkVersion == 0) {
            targetSdkVersion = getTargetSdkVersion(context);
        }
        int permissionState = 0;
        if (Build.VERSION.SDK_INT >= 23) {
            //  permissionState = ContextCompat.checkSelfPermission(context, permission);
            permissionState = PermissionChecker.checkSelfPermission(context, permission);
        }
        DLog.e("授权", permission + ": " + permissionState + " targetSdkVersion:" + targetSdkVersion);
        return permissionState;
    }


    //请求权限
    public void requestPermissions(Activity activity, int state, String permission, int reqCode) {
        //true:拒绝授权，并且选择了不要再次提示 ,然而，在实际开发中，需要注意的是，
        // 很多手机对原生系统做了修改，比如小米，小米4的6.0,三星的1505-A01 6.0  就一直返回false，
        // 而且在申请权限时，如果用户选择了拒绝，则不会再弹出对话框了
        // false:拒绝了授权返回false
        boolean isShow = ActivityCompat.shouldShowRequestPermissionRationale(activity,
                permission);
        DLog.e("requestPermissions", "请求权限 isShow:" + isShow);
        if (!isShow) {
            //是否要解释 为什么要用这个权限
           /* startAccredit(activity);
            ToastUtile.showToast("此操作需要您手动的授权");
            return;*/
        }
        if (state == PermissionChecker.PERMISSION_DENIED_APP_OP) {
            startAccredit(activity);
            ToastUtile.showToast("此操作需要您的授权");
            return;
        }
        //去授权
        ActivityCompat.requestPermissions(activity, new String[]{permission}, reqCode);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

        DLog.e("权限请求结果", "requestCode:" + requestCode
                + " 权限：" + permissionsStr
                + " 结果：" + grantResultsStr);


    }

    //到授权管理
    public void startAccredit(Context context) {
        Intent intent = getAppDetailSettingIntent(context);
        context.startActivity(intent);
    }

    private Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
        localIntent.setAction(action);
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), ""));
        return localIntent;
    }
}
