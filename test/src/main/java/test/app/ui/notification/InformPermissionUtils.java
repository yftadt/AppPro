package test.app.ui.notification;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationManagerCompat;
//检查通知权限
public class InformPermissionUtils {
    /**
     * 系统层面通知开关有没有开启
     * Build.VERSION.SDK_INT >= 19  否则一直返回 true
     *
     * @param mContext
     * @return
     */
    public static boolean checkNotifyPermission(Context mContext) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(mContext);
        boolean isOpened = manager.areNotificationsEnabled();
        return isOpened;
    }


    /**
     * 如果通知未打开 跳转到通知设定界面
     *
     * @param mContext
     */
    public static void tryJumpNotifyPage(Context mContext) {
        Intent intent = new Intent();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, mContext.getPackageName());
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", mContext.getPackageName());
                intent.putExtra("app_uid", mContext.getApplicationInfo().uid);
            } else {
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
            }
            mContext.startActivity(intent);
        } catch (Exception e) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
            intent.setData(uri);
            mContext.startActivity(intent);
        }
    }
    //
}
