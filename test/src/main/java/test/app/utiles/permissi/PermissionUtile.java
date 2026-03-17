package test.app.utiles.permissi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;

import java.util.ArrayList;
import java.util.List;

import sj.mblog.Logx;

//权限
public class PermissionUtile {
    /**
     * 获取读取写入文件权限
     */
    private static ArrayList getReadWrite(Context context) {
        int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        int sdkinit = Build.VERSION.SDK_INT;
        Logx.d("权限获取", "targetSdkVersion:" + targetSdkVersion + " sdkinit:" + sdkinit);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ArrayList<String> permissions = new ArrayList();
            //permissions.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
            return permissions;
        } else {
            //android 10 （api 29 任然要）
            ArrayList<String> permissions = new ArrayList();
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            return permissions;
        }
    }

    //获取 照片和视频权限
    private static ArrayList getImgAndVideo() {
        ArrayList<String> permissions = new ArrayList();
        //Android 13 (API Level 33)开始，安卓将 READ_EXTERNAL_STORAGE  权限，作了细分
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES);
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO);
            //一般的也可能需要 文件管理权限
            //permissions.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        }
        return permissions;
    }

    /**
     * @param act
     * @param type     1:检查读写文件
     *                 2.通知栏权限
     *                 3.相机权限
     *                 4.读取照片和视频
     * @param callback 回调
     */
    public static void setCheckPermission(Activity act, int type, OnPMCallback callback) {
        ArrayList<String> permissions = new ArrayList();
        //权限检查
        switch (type) {
            case 1:
                permissions = getReadWrite(act);
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissions.add(Manifest.permission.POST_NOTIFICATIONS);
                }
                break;
            case 3:
                permissions = getReadWrite(act);
                permissions.add(Manifest.permission.CAMERA);
                break;
            case 4:
               /* if (true) {
                    callback.onGranted(permissions, true);
                    return;
                }*/
                permissions = getReadWrite(act);
                /*if (true) {
                    //现在用的是google 照片选择器 不需要权限
                    callback.onGranted(permissions, true);
                    return;
                }
                ArrayList temp2 = getImgAndVideo();
                permissions.addAll(temp2);*/
                break;
        }
        if (permissions == null) {
            callback.onError();
            return;
        }
        ArrayList<String> newPermission = new ArrayList<String>();
        for (int i = 0; i < permissions.size(); i++) {
            String str = permissions.get(i);
            if (Manifest.permission.MANAGE_EXTERNAL_STORAGE.equals(str)) {
                newPermission.add(str);
                continue;
            }
            boolean checkPermissionFirst = XXPermissions.isGranted(act, str);
            Logx.d("权限获取", str + " 已获取:"
                    + checkPermissionFirst + "总共：" + permissions.size() + " type:" + type);
            if (!checkPermissionFirst) {
                newPermission.add(str);
            }
        }
        if (newPermission.size() == 0) {
            //有权限
            Logx.d("权限获取", " 已获取所有权限");
            callback.onAcquiredAll();
            return;
        }

        XXPermissions.with(act).permission(newPermission).request(new OnPermissionCallback() {
            @Override
            public void onGranted(List<String> permissions, boolean all) {
                //同意
                Logx.d("权限获取", "" + permissions.toString() + " all:" + all);
                if (all) {
                    callback.onGranted(permissions, all);
                }
            }

            @Override
            public void onDenied(List<String> permissions, boolean never) {
                //XXPermissions.with(act).isPermanentDenied()
                callback.onDenied(permissions, never);
                Logx.d("权限获取", "" + permissions.toString() + " never:" + never);

            }
        });
    }


}
