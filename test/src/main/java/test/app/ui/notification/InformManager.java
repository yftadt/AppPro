package test.app.ui.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import sj.mblog.Logx;
import test.app.ui.activity.R;


/**
 * 通知栏
 * Created by gm on 2016/7/7.
 */
public class InformManager {
    private static InformManager informManager;
    private NotificationManager notificationManager;
    private Context context;

    private NotificationCompat.Builder builder;

    public static InformManager getInstance(Context context) {
        if (informManager == null) {
            informManager = new InformManager(context);
        }
        return informManager;
    }

    private InformManager(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        boolean buildResult = notificationApi26(notificationManager);
        if (!buildResult) {
            Logx.d("InformManager", "创建通知失败");
        }
    }

    /**
     * 设置通知内容
     */
    public void setInformContent(InformBean informBean) {
        builder = new NotificationCompat.Builder(context, channelId);
        //0 消息通知 1 大图 ，2 长文字 3 进度通知 4 自定义通知
        switch (informBean.informType) {
            case 0:
                setText(informBean.msgTitle, informBean.msgContent, informBean.msgTicker);
                break;
            case 1:
                //
                setText(informBean.msgTitle, informBean.msgContent, informBean.msgTicker);
                setBigImage(informBean);
                break;
            case 2:
                setText(informBean.msgTitle, informBean.msgContent, informBean.msgTicker);
                setBigText(informBean);
                break;
            case 3:
                setText(informBean.msgTitle, informBean.msgContent, informBean.msgTicker);
                setProgress(informBean.progressMax, informBean.progress, informBean.progressIndeterminate);
                break;
            case 4:
                //自定义通知
                builder.setContent(informBean.customViews);
                builder.setSmallIcon(informBean.customSmallIcon);
                builder.setTicker(informBean.customTicker);
                return;
        }
        setIntent(informBean.intent, informBean.informReqCode);
        setIcon(R.mipmap.push, R.mipmap.ic_launcher);
        setFeeling(informBean);
        //true 点击消失
        builder.setAutoCancel(informBean.informAutoCancel);
    }

    //设置文本
    private void setText(String title, String content, String ticker) {
        builder.setContentTitle(title)
                .setContentText(content)
                .setTicker(ticker);

    }

    //设置大图样式
    private void setBigText(InformBean informBean) {
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.setSummaryText(informBean.bigTxtTitle);
        style.setBigContentTitle(informBean.bigTxtMsg);
        builder.setStyle(style);
    }

    private void setBigImage(InformBean informBean) {
        //设置大视图样式通知
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.setBigContentTitle(informBean.bigTxtTitle);
        style.setSummaryText(informBean.bigTxtMsg);
        Bitmap img = informBean.bigImg;
        if (informBean.bigResIdImg != 0) {
            img = BitmapFactory.decodeResource(context.getResources(), R.mipmap.push);
        }
        if (img != null) {
            style.bigPicture(img);
        }
        builder.setStyle(style);
    }


    //设置icon
    private void setIcon(int LargeIcon, int smallicon) {
        builder.setSmallIcon(smallicon);
        builder.setSmallIcon(smallicon, 2);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), LargeIcon);
        builder.setLargeIcon(bitmap);
    }

    //声音和震动 26 以下测试有效
    private void setFeeling(InformBean informBean) {
        switch (informBean.feelingType) {
            case 1:
                //使用默认声音 和 震动
                builder.setDefaults(Notification.DEFAULT_SOUND);
                pattern = informBean.pattern;
                builder.setVibrate(pattern);
                break;
            case 2:
                //使用默认的震动
                builder.setDefaults(Notification.DEFAULT_VIBRATE);
                break;
            case 3:
                //使用默认的震动和声音
                builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                break;
            case 4:
                //使用默认的声音、振动、闪光
                builder.setDefaults(Notification.DEFAULT_ALL);
                break;
            case 5:
                //自定义震动模式
                long[] pattern = informBean.pattern;
                builder.setVibrate(pattern);
                break;
            case 6:
                //自定义声音 和 震动
                //从raw
                //Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notificationsound);
                //Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/notificationsound");
                //Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.notificationsound);
                //从铃声管理器
                //Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                //从文件
                //Uri sound = Uri.fromFile(new File("/sdcard/sound.mp3"));
                //Uri sound = Uri.parse(new File("/sdcard/sound.mp3").toString()));
                Uri sound = informBean.sound;
                builder.setSound(sound);
                pattern = informBean.pattern;
                builder.setVibrate(pattern);
                break;
        }
    }

    //设置跳转activity
    private void setIntent(Intent intent, int reqCode) {
        PendingIntent pendingIntent;
        if (intent == null) {
            pendingIntent = PendingIntent.getActivity(context, reqCode,
                    new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, reqCode,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        builder.setContentIntent(pendingIntent);
        //Android 8.0之前实现悬挂通知 5.0中新增
        //builder.setFullScreenIntent(pendingIntent,true);
    }

    //设置进度条(更新进度条)
    public void setProgress(int max, int progress, boolean indeterminate) {
        // indeterminate 一种是有进度刻度的（false）,一种是循环流动的（true）
        builder.setProgress(max, progress, indeterminate);
    }

    public NotificationCompat.Builder getBuilder() {
        return builder;
    }

    //==============================8.0=============================
    private String channelId = "default_id";
    //显示在通知管理 相应的医院里
    private String channelName = "default_channel_name";

    /**
     * @param notificationManager
     * @return
     */
    @TargetApi(26)
    private boolean notificationApi26(NotificationManager notificationManager) {
        //Android O及以上必需的channelID
        if (notificationManager == null && TextUtils.isEmpty(channelId)) {
            return false;
        }
        //渠道描述
        String description = "描述";
        //通知的优先级
        int importance = NotificationManager.IMPORTANCE_LOW;
        //Android 8.0 提高等级：悬挂通知
        //importance= NotificationManager.IMPORTANCE_HIGH;
        //创建渠道 //channelName：渠道名,有说这个字段对用户可见。
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setBypassDnd(true); //设置绕过免打扰模式
        boolean isDnd = channel.canBypassDnd(); //检测是否绕过免打扰模式
        Logx.d("--", "isDnd---->" + isDnd);
        channel.setDescription(description);
        //设置在锁屏界面上显示这条通知
        channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        //
        channel.enableLights(true);
        //指示灯颜色，这个取决于设备是否支持
        channel.setLightColor(Color.RED);
        //是否使用震动
        channel.enableVibration(true);
        //震动节奏
        channel.setVibrationPattern(new long[]{100, 200});
        //声音
        channel.setSound(null, null);
        //将NotificationChannel对象传入NotificationManager
        notificationManager.createNotificationChannel(channel);
        return true;
    }


    //启动一个通知
    public void show(int NotifyId) {
        show(NotifyId, -1, false);
    }

    public void show(int NotifyId, int badgeCount) {
        show(NotifyId, badgeCount, false);
    }

    //启动一个通知
    //badgeCount 角标数， isResident true 常驻通知
    public void show(int NotifyId, int badgeCount, boolean isResident) {
        if (builder == null) {
            Logx.d("","状态栏显示，配置错误");
            return;
        }
        Notification notification = builder.build();
        if (isResident) {
            //FLAG_ONGOING_EVENT 在顶部常驻
            notification.flags = Notification.FLAG_ONGOING_EVENT;
        }
        //设置显示通知时的默认的 震动
        // notification.defaults = Notification.DEFAULT_VIBRATE;
        // 设置显示通知时的默认的 声音
        // notification.defaults = Notification.DEFAULT_SOUND;
        // notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        //设置发出通知的时间
        notification.when = System.currentTimeMillis();
        show(NotifyId, notification, badgeCount);
    }

    //启动一个通知
    private void show(int NotifyId, Notification notification, int badgeCount) {
        if (badgeCount >= 0) {
            //设置角标
            BadgeUtil.setBadgeNotification(notification, badgeCount);
        }
        notificationManager.notify(NotifyId, notification);
    }

    private Vibrator vibrator;
    // 停止 开启 停止 开启
    private long[] pattern = {100, 500, 400, 500};

    //震动
    public void vibrator() {
        if (vibrator == null) {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
        vibrator.cancel();
        vibrator.vibrate(pattern, -1);
    }

    //删除通知
    public void clearNotifyTag(String tag, int notifyId) {
        notificationManager.cancel(tag, notifyId);
    }

    public void clearNotifyId(int notifyId) {
        notificationManager.cancel(notifyId);
    }

    public void clearNotifyAll() {
        notificationManager.cancelAll();
    }
}
