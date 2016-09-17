package com.app.ui.manager;

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

import com.app.utiles.other.DLog;
import com.app.utiles.other.ToastUtile;


/**
 * Created by Administrator on 2016/7/19.
 */
public class PermissionManager {
    private static PermissionManager p;
    public static final String permission_record = Manifest.permission.RECORD_AUDIO;
    public static final String permission_location = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String permission_camera = Manifest.permission.CAMERA;
    private int targetSdkVersion;

    private PackageManager pkm;
    private String pakName;

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
        boolean isShow = ActivityCompat.shouldShowRequestPermissionRationale(activity,
                permission);
        if (isShow) {
            //是否要解释 为什么要用这个权限
        }
        if (state == PermissionChecker.PERMISSION_DENIED_APP_OP) {
            //曾拒绝过授权
            startAccredit(activity);
            ToastUtile.showToast("此操作需要您的授权");
            return;
        }
        //去授权
        ActivityCompat.requestPermissions(activity, new String[]{permission}, reqCode);
        DLog.e("requestPermissions", "请求权限 isShow:" + isShow);
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
        if (onPermissionsListener != null) {
            onPermissionsListener.onPermissions(requestCode, permissions, grantResults);
        }
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

    private OnPermissionsListener onPermissionsListener;

    public void setOnPermissionsListener(OnPermissionsListener onPermissionsListener) {
        this.onPermissionsListener = onPermissionsListener;
    }

    //权限检查监听
    public interface OnPermissionsListener {
        void onPermissions(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }
}
