package test.app.utiles.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;


import com.library.baseui.utile.app.APKInfo;

import test.app.ui.window.dialog.BaseDialog;
import test.app.ui.window.dialog.DialogHint;


/**
 * Created by Administrator on 2016/7/19.
 */
public class PermissionManager {
    private static PermissionManager p;

    public static PermissionManager getInstance() {
        if (p == null) {
            p = new PermissionManager();
        }
        return p;
    }

    /***检查权限 含调用自动或者手动去授权
     * @param activity
     * @param permission
     * @return true (没有此权限)结束调用此方法的调用者
     */
    public boolean isCheckPermission(Activity activity, String permission, int reqCode) {
        boolean isStopProgress = false;
        int permissionState = ActivityCompat.checkSelfPermission(activity, permission);
        int targetSdkVersion = APKInfo.getInstance().getTargetSdkVersion();
        DLog.e("授权", "版本:" + targetSdkVersion + "  手机系统版本：" + Build.VERSION.SDK_INT);
        if (permissionState == PackageManager.PERMISSION_GRANTED) {
            //该权限是被授权的。
            DLog.e("授权", "该权限是已经授予权限");
            return isStopProgress;
        }
        if (permissionState == PermissionChecker.PERMISSION_DENIED) {
            //需要去授权
            isStopProgress = true;
            DLog.e("授权", "需要去授权");
            //去授权
            requestPermissions(activity, permission, reqCode);
        }
        if (permissionState == PermissionChecker.PERMISSION_DENIED_APP_OP) {
            //选择了不再提醒，需要手动授权
            isStopProgress = true;
            DLog.e("授权", "需要手动授权");
            dialogShow(1, activity, permission);
        }
        return isStopProgress;
    }

    //请求权限
    private void requestPermissions(Activity activity, String permission, int reqCode) {
        //PackageManager.PERMISSION_DENIED：该权限是被拒绝的。
        //true:首次申请权限，拒绝了，要提示用户（不然用户选择“不再提醒”就永远授权不了） 。
        // 然而，在实际开发中，需要注意的是，
        // 很多手机对原生系统做了修改，比如小米，小米4的6.0,三星的1505-A01 6.0  就一直返回false，
        // 而且在申请权限时，如果用户选择了拒绝，则不会再弹出对话框了
        // false:拒绝了授权返回false
        boolean isShow = ActivityCompat.shouldShowRequestPermissionRationale(activity,
                permission);
        DLog.e("授权", "状态：" + " isShow:" + isShow);
        if (isShow) {
            //解释这个权限是干什么用的
            dialogShow(2, activity, permission);
        }
        ActivityCompat.requestPermissions(activity, new String[]{permission}, reqCode);
    }
    //到授权管理
    private void startPermissionSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
        intent.setAction(action);
        intent.setData(Uri.fromParts("package", context.getPackageName(), ""));
        context.startActivity(intent);
    }
    //授权结果
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults == null || grantResults.length == 0) {
            return;
        }
        if (permissions == null || permissions.length == 0) {
            return;
        }
        for (int i = 0; i < permissions.length; i++) {
            String permissionName = permissions[i];
            int permissionCode = grantResults[i];
            String codeStateHint = "";
            if (permissionCode == PackageManager.PERMISSION_GRANTED) {
                codeStateHint = "已授权";
            }
            if (permissionCode == PackageManager.PERMISSION_DENIED) {
                codeStateHint = "授权失败";
            }
            DLog.e("权限请求结果", "requestCode:" + requestCode
                    + " 权限：" + permissionName + " 结果：" + permissionCode
                    + " 状态：" + codeStateHint);
        }

    }



    private void dialogShow(int type, Activity activity, String permission) {
        DialogHint dialog = new DialogHint(activity);
        dialog.setTitle("授权");
        switch (type) {
            case 1:
                //手动去授权
                String hint = Permissions.getPermissionHint(permission);
                dialog.setMsg(hint + ",您需要手动去授权");
                dialog.setBtnsHint("取消", "确定");
                break;
            case 2:
                //告知用户，此权限的作用
                hint = Permissions.getPermissionHint(permission);
                dialog.setMsg(hint);
                dialog.setBtnHint("确定");
                break;
        }

        dialog.setOnDialogBackListener(new DialogOptionListener(activity));

    }

    class DialogOptionListener implements BaseDialog.OnDialogBackListener {
        private Activity activity;

        public DialogOptionListener(Activity activity) {
            this.activity = activity;
        }


        @Override
        public void onDialogBack(int dialogType, int eventType, String... arg) {

        }
    }
}
