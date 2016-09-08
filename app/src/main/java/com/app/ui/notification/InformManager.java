package com.app.ui.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * 通知栏
 * Created by gm on 2016/7/7.
 */
public class InformManager {
    private static InformManager informManager;
    private NotificationManager nm;
    private Context context;
    private Notification notification;
    private NotificationCompat.Builder builder;

    public static InformManager getInstance(Context context) {
        if (informManager == null) {
            informManager = new InformManager();
        }
        if (informManager.nm == null) {
            informManager.context = context;
            informManager.nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        }
        return informManager;
    }

    /**
     * 设置显示文本
     */
    public void setText(String title, String content, String ticker) {
        builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title)
                .setContentText(content)
                .setTicker(ticker);
    }

    public void setText(String title, String content, String ticker, int number) {
        builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title)
                .setContentText(content)
                .setNumber(number)
                .setTicker(ticker);
    }

    //自定义通知
    public void setCustomization(RemoteViews remoteViews, int smallIcon, String ticker) {
        builder = new NotificationCompat.Builder(context);
        builder.setContent(remoteViews);
        builder.setSmallIcon(smallIcon);
        builder.setTicker(ticker);
        notification = builder.build();
    }


    //设置icon
    public void setIcon(int LargeIcon, int smallicon) {
        builder.setSmallIcon(smallicon);
        builder.setSmallIcon(smallicon, 2);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), LargeIcon);
        builder.setLargeIcon(bitmap);
        notification = builder.build();
    }

    //设置跳转activity
    public void setIntent(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        notification = builder.build();

    }

    /**
     * 设置常驻
     */
    public void setResident() {
        notification = builder.build();
        //FLAG_ONGOING_EVENT 在顶部常驻
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        //设置显示通知时的默认的发声、震动、Light效果
        notification.defaults = Notification.DEFAULT_VIBRATE;
        //设置发出通知的时间
        notification.when = System.currentTimeMillis();
    }

    //设置进度条
    public void setProgress(int max, int progress, boolean indeterminate) {
        builder.setProgress(max, progress, indeterminate);
        notification = builder.build();
    }

    //启动一个通知
    public void show(int NotifyId) {
        nm.notify(NotifyId, notification);
    }
    //删除通知
    public void clearNotifyTag(String tag, int notifyId) {
        nm.cancel(tag, notifyId);
    }

    public void clearNotifyId(int notifyId) {
        nm.cancel(notifyId);
    }

    public void clearNotifyAll() {
        nm.cancelAll();
    }
}
