package test.app.ui.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
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
    // [0] 渠道编号，[1] 渠道名称，[2] 渠道 ID。
    //渠道编号用于 setCreateChannel 的分支选择，不等同于 feelingType；渠道 ID 应保持稳定。
    public static String[] alertsChannel = new String[]{"1", "消息提醒", "message_alert"};
    public static String[] downloadProgressChannel = new String[]{"2", "下载进度", "download_progress"};
    public static String[] patternAlertsChannel = new String[]{"3", "消息声音与震动模式", "message_pattern_alert"};
    public static String[] vibrationChannel = new String[]{"4", "消息震动", "message_vibration"};
    public static String[] allAlertsChannel = new String[]{"5", "消息声音、震动与指示灯", "message_all_alerts"};
    public static String[] customVibrationChannel = new String[]{"6", "自定义消息震动", "message_custom_vibration"};
    public static String[] customAlertsChannel = new String[]{"7", "自定义消息提醒", "message_custom_alert"};


    private static InformManager informManager;
    private NotificationManager notificationManager;
    private Context context;


    public static InformManager getInstance(Context context) {
        if (informManager == null) {
            informManager = new InformManager(context);
        }
        return informManager;
    }

    private InformManager(Context context) {
        this.context = context.getApplicationContext();
        notificationManager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 设置/构建 通知内容
     */
    public NotificationCompat.Builder setInformContent(InformBean informBean) {
        String channelId = TextUtils.isEmpty(informBean.channelId)
                ? alertsChannel[2] : informBean.channelId;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        int smallIcon = R.mipmap.ic_launcher;
        //0 消息通知 1 大图 ，2 长文字 3 进度通知 4 自定义通知
        switch (informBean.informType) {
            case 0:
                setText(builder, informBean.msgTitle, informBean.msgContent, informBean.msgTicker);
                break;
            case 1:
                //
                setText(builder, informBean.msgTitle, informBean.msgContent, informBean.msgTicker);
                setBigImage(builder, informBean);
                break;
            case 2:
                setText(builder, informBean.msgTitle, informBean.msgContent, informBean.msgTicker);
                setBigText(builder, informBean);
                break;
            case 3:
                //未指定渠道时使用下载进度渠道，由 setFeeling 统一绑定并按需创建。
                //强行设置下载渠道
                informBean.channelId = downloadProgressChannel[2];
                //
                setText(builder, informBean.msgTitle, informBean.msgContent, informBean.msgTicker);
                setProgress(builder, informBean.progressMax, informBean.progress, informBean.progressIndeterminate);
                //首次显示时允许按渠道设置提醒，更新同一条通知时不重复提醒。
                builder.setOnlyAlertOnce(true);
                break;
            case 4:
                //自定义通知
                builder.setContent(informBean.customViews);
                if (informBean.customSmallIcon != 0) {
                    smallIcon = informBean.customSmallIcon;
                }
                builder.setTicker(informBean.customTicker);
                break;
        }
        setIntent(builder, informBean.intent, informBean.informReqCode);
        setIcon(builder, R.mipmap.push, smallIcon);
        setFeeling(builder, informBean);
        //true 点击消失
        builder.setAutoCancel(informBean.informAutoCancel);
        return builder;
    }

    //设置文本
    private void setText(NotificationCompat.Builder builder, String title, String content, String ticker) {
        builder.setContentTitle(title)//标题
                .setContentText(content)//内容
                .setTicker(ticker);//旧版状态栏滚动提示文字；现代系统主要用于向无障碍服务提供提示，不能依赖它显示滚动文字

    }

    //设置长文本样式
    private void setBigText(NotificationCompat.Builder builder, InformBean informBean) {
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        //指定展开后的标题,未单独设置时默认沿用 setContentTitle()
        style.setBigContentTitle(informBean.bigTxtTitle);
        //指定展开后的长正文，未单独设置时默认沿用 setContentText() 内容
        style.bigText(informBean.bigTxtMsg);
        builder.setStyle(style);
    }

    private void setBigImage(NotificationCompat.Builder builder, InformBean informBean) {
        //设置大视图样式通知
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        //指定展开后的标题,未单独设置时默认沿用 setContentTitle()
        style.setBigContentTitle(informBean.bigTxtTitle);
        //大视图样式的摘要，例如“共 5 条消息”。不能保证仅展开时显示；部分系统模板会把它放到头部，作为未设置 subText 时的补充文字
        style.setSummaryText(informBean.bigTxtMsg);
        Bitmap img = informBean.bigImg;
        if (informBean.bigResIdImg != 0) {
            img = BitmapFactory.decodeResource(context.getResources(), informBean.bigResIdImg);
        }
        if (img != null) {
            style.bigPicture(img);
        }
        builder.setStyle(style);
    }


    //设置icon
    private void setIcon(NotificationCompat.Builder builder, int LargeIcon, int smallicon) {
        builder.setSmallIcon(smallicon);
        builder.setSmallIcon(smallicon, 2);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), LargeIcon);
        builder.setLargeIcon(bitmap);
    }


    /**
     * 选择通知使用的渠道，具体配置交给 setCreateChannel。
     * 指定 channelId 时优先使用该渠道，否则按 feelingType 选择。
     */
    private void setFeeling(NotificationCompat.Builder builder, InformBean informBean) {
        if (!TextUtils.isEmpty(informBean.channelId)) {
            setChannelId(builder, informBean);
            return;
        }
        //未指定提醒类型或类型不匹配时，使用消息提醒渠道。
        String[] channels = alertsChannel;
        switch (informBean.feelingType) {
            case 1:
                //默认通知声音，震动节奏使用 informBean.pattern。
                channels = patternAlertsChannel;
                break;
            case 2:
                //关闭声音，使用渠道的默认震动节奏。
                channels = vibrationChannel;
                break;
            case 3:
                //使用消息渠道的公共声音和震动配置。
                channels = alertsChannel;
                break;
            case 4:
                //使用默认声音、震动和红色指示灯。
                channels = allAlertsChannel;
                break;
            case 5:
                //关闭声音，使用自定义震动节奏。
                channels = customVibrationChannel;
                break;
            case 6:
                //使用自定义声音和震动节奏。
                channels = customAlertsChannel;
                break;
            default:
                break;
        }
        //本条通知都需要绑定渠道 ID。
        builder.setChannelId(channels[2]);
        setCreateChannel(channels, informBean);
    }

    //按渠道id 设置渠道
    private void setChannelId(NotificationCompat.Builder builder, InformBean informBean) {
        //本条通知绑定指定的渠道 ID。
        builder.setChannelId(informBean.channelId);
        String[] channels;
        //按渠道 ID 匹配内置渠道，case 的值与上方渠道数组的 ID 保持一致。
        switch (informBean.channelId) {
            case "message_alert":
                channels = alertsChannel;
                break;
            case "download_progress":
                channels = downloadProgressChannel;
                break;
            case "message_pattern_alert":
                channels = patternAlertsChannel;
                break;
            case "message_vibration":
                channels = vibrationChannel;
                break;
            case "message_all_alerts":
                channels = allAlertsChannel;
                break;
            case "message_custom_vibration":
                channels = customVibrationChannel;
                break;
            case "message_custom_alert":
                channels = customAlertsChannel;
                break;
            default:
                //非内置渠道由调用方提前创建。
                return;
        }
        //内置渠道按需创建，已存在时保留系统和用户设置。
        setCreateChannel(channels, informBean);
    }

    /**
     * 创建渠道
     * 声音、震动等是渠道配置，自定义参数仅在首次创建时使用，后续保留系统和用户设置。
     */
    private void setCreateChannel(String[] channels, InformBean informBean) {
        if (channels == null) {
            //获取渠道类别失败
            return;
        }
        NotificationChannel existingChannel = notificationManager.getNotificationChannel(channels[2]);
        if (existingChannel != null) {
            //渠道存在
            return;
        }
        //普通重要性用于提醒渠道，下载进度在对应分支中设置为低重要性。
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(
                channels[2], channels[1], importance);
        //系统默认通知音的 URI，使用声音的分支显式设置。
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //将声音标记为通知提示音。
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        //以渠道名称作为说明。
        channel.setDescription(channels[1]);
        //安全锁屏上
        //VISIBILITY_PUBLIC	显示完整内容
        //VISIBILITY_PRIVATE	显示通知，但隐藏敏感内容
        //VISIBILITY_SECRET	完全隐藏通知
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        //各分支独立配置提醒方式；免打扰和灯光效果受权限、用户设置及设备支持限制。
        //震动节奏单位为毫秒，{100, 200} 表示等待 100ms 后震动 200ms。
        switch (channels[0]) {
            case "1":
                //消息提醒：默认声音、固定震动节奏和红色指示灯。
                channel.setSound(defaultSound, audioAttributes); //使用系统默认通知提示音。
                channel.setBypassDnd(true); //请求绕过优先级免打扰，实际受权限和用户设置限制。
                channel.enableVibration(true); //启用通知震动。
                channel.setVibrationPattern(new long[]{100, 200}); //等待 100ms 后震动 200ms。
                channel.enableLights(true); //启用通知指示灯，需要设备支持。
                channel.setLightColor(Color.RED); //将通知指示灯颜色设为红色。
                break;
            case "2":
                //下载进度：
                //低重要性，无声音、震动、灯光，不请求绕过免打扰。
                channel.setImportance(NotificationManager.IMPORTANCE_LOW); //使用低重要性展示下载进度。
                channel.setSound(null, null); //不播放通知声音。
                channel.setBypassDnd(false); //不请求绕过免打扰。
                channel.enableLights(false); //关闭通知指示灯。
                channel.enableVibration(false); //关闭通知震动。
                break;
            case "3":
                //消息声音与震动模式：默认声音、自定义震动和红色指示灯。
                channel.setSound(defaultSound, audioAttributes); //使用系统默认通知提示音。
                channel.setBypassDnd(true); //请求绕过优先级免打扰，实际受权限和用户设置限制。
                channel.enableVibration(true); //启用通知震动。
                channel.setVibrationPattern(informBean.pattern); //使用传入的震动节奏，单位为毫秒，依次为等待、震动时长。
                channel.enableLights(true); //启用通知指示灯，需要设备支持。
                channel.setLightColor(Color.RED); //将通知指示灯颜色设为红色。
                break;
            case "4":
                //消息震动：无声音，固定震动节奏和红色指示灯。
                channel.setSound(null, null); //不播放通知声音。
                channel.setBypassDnd(true); //请求绕过优先级免打扰，实际受权限和用户设置限制。
                channel.enableVibration(true); //启用通知震动。
                channel.setVibrationPattern(new long[]{100, 200}); //等待 100ms 后震动 200ms。
                channel.enableLights(true); //启用通知指示灯，需要设备支持。
                channel.setLightColor(Color.RED); //将通知指示灯颜色设为红色。
                break;
            case "5":
                //消息声音、震动与指示灯：默认声音、固定震动节奏和红色指示灯。
                channel.setSound(defaultSound, audioAttributes); //使用系统默认通知提示音。
                channel.setBypassDnd(true); //请求绕过优先级免打扰，实际受权限和用户设置限制。
                channel.enableVibration(true); //启用通知震动。
                channel.setVibrationPattern(new long[]{100, 200}); //等待 100ms 后震动 200ms。
                channel.enableLights(true); //启用通知指示灯，需要设备支持。
                channel.setLightColor(Color.RED); //将通知指示灯颜色设为红色。
                break;
            case "6":
                //自定义消息震动：无声音，自定义震动节奏和红色指示灯。
                channel.setSound(null, null); //不播放通知声音。
                channel.setBypassDnd(true); //请求绕过优先级免打扰，实际受权限和用户设置限制。
                channel.enableVibration(true); //启用通知震动。
                channel.setVibrationPattern(informBean.pattern); //使用传入的震动节奏，单位为毫秒，依次为等待、震动时长。
                channel.enableLights(true); //启用通知指示灯，需要设备支持。
                channel.setLightColor(Color.RED); //将通知指示灯颜色设为红色。
                break;
            case "7":
                //自定义消息提醒：自定义声音、震动和红色指示灯；sound 为 null 时无声音。
                channel.setSound(informBean.sound, audioAttributes); //使用传入的声音 URI，null 表示无声。
                channel.setBypassDnd(true); //请求绕过优先级免打扰，实际受权限和用户设置限制。
                channel.enableVibration(true); //启用通知震动。
                channel.setVibrationPattern(informBean.pattern); //使用传入的震动节奏，单位为毫秒，依次为等待、震动时长。
                channel.enableLights(true); //启用通知指示灯，需要设备支持。
                channel.setLightColor(Color.RED); //将通知指示灯颜色设为红色。
                break;
        }
        //创建渠道
        notificationManager.createNotificationChannel(channel);
    }

    //设置跳转activity
    private void setIntent(NotificationCompat.Builder builder, Intent intent, int reqCode) {
        //reqCode 请求码（以下以 Intent 的其他标识相同为前提，extras 不参与匹配）。
        //相同：使用 FLAG_UPDATE_CURRENT 时复用同一个 PendingIntent，更新 extras，旧通知点击时也使用更新后的参数。
        //不相同：区分为不同的 PendingIntent，各自的点击参数互不覆盖。
        if (intent == null) {
            //没有跳转目标，不设置通知的整体点击操作。
            builder.setContentIntent(null);
            return;
        }
        //FLAG_CANCEL_CURRENT: 取消旧的，创建新的，旧引用失效
        //FLAG_UPDATE_CURRENT：保留原来的，更新 extras，旧引用仍可触发
        //FLAG_IMMUTABLE：     执行方触发它时，不能修改其中的 Intent
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        //Android 8.0之前实现悬挂通知 5.0中新增
        //builder.setFullScreenIntent(pendingIntent,true);
    }

    //设置进度条(更新进度条)
    public void setProgress(NotificationCompat.Builder builder, int max, int progress, boolean indeterminate) {
        // indeterminate 一种是有进度刻度的（false）,一种是循环流动的（true）
        builder.setProgress(max, progress, indeterminate);
    }


    //启动一个通知
    public void show(NotificationCompat.Builder builder, int NotifyId) {
        show(builder, NotifyId, -1, false);
    }

    public void show(NotificationCompat.Builder builder, int NotifyId, int badgeCount) {
        show(builder, NotifyId, badgeCount, false);
    }

    //启动一个通知
    //badgeCount 角标数， isResident true 常驻通知
    public void show(NotificationCompat.Builder builder, int NotifyId, int badgeCount, boolean isResident) {
        if (builder == null) {
            Logx.d("", "状态栏显示，配置错误");
            return;
        }
        if (isResident) {
            //在顶部常驻
            //true：设置为进行中的通知；false：取消进行中状态，保留其他通知标志。
            builder.setOngoing(true);
        }
        Notification notification = builder.build();
        //这种方式 会覆盖 flags， 要使用上面的方式
        /*if (isResident) {
            //FLAG_ONGOING_EVENT 在顶部常驻
            //覆盖：只保留 FLAG_ONGOING_EVENT，清掉其他标志。
            // notification.flags = Notification.FLAG_ONGOING_EVENT;
            //追加：添加 FLAG_ONGOING_EVENT，保留其他标志。
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
        }*/
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
