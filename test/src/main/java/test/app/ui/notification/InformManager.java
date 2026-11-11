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
                setText(builder, informBean.msgTitle, informBean.msgContent, informBean.msgTicker);
                setProgress(builder, informBean.progressMax, informBean.progress, informBean.progressIndeterminate);
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
     * 显式指定 channelId 时优先使用该渠道，否则按 feelingType 选择。
     */
    private void setFeeling(NotificationCompat.Builder builder, InformBean informBean) {
        if (!TextUtils.isEmpty(informBean.channelId)) {
            //内置渠道可以按需创建；非内置渠道由调用方提前创建。
            String[][] channelList = new String[][]{
                    alertsChannel, downloadProgressChannel, patternAlertsChannel,
                    vibrationChannel, allAlertsChannel, customVibrationChannel, customAlertsChannel
            };
            for (String[] channels : channelList) {
                if (informBean.channelId.equals(channels[2])) {
                    setCreateChannel(channels, builder, informBean);
                    return;
                }
            }
            //其他显式渠道由调用方提前创建。
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
        setCreateChannel(channels, builder, informBean);
    }

    /**
     * 将 Builder 绑定到渠道，渠道不存在时才配置并创建。
     * 声音、震动等是渠道配置，自定义参数仅在首次创建时使用，后续保留系统和用户设置。
     */
    private void setCreateChannel(String[] channels, NotificationCompat.Builder builder, InformBean informBean) {
        //无论渠道是否已存在，本条通知都需要绑定渠道 ID。
        builder.setChannelId(channels[2]);
        NotificationChannel existingChannel = notificationManager.getNotificationChannel(channels[2]);
        if (existingChannel != null) {
            //读取已保存渠道的免打扰配置，不重新创建或覆盖用户设置。
            Logx.d("InformManager", "channelId:" + channels[2]
                    + " isDnd---->" + existingChannel.canBypassDnd());
            return;
        }
        //提醒渠道默认使用普通重要性，下载进度分支会降低重要性。
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(
                channels[2], channels[1], importance);
        //获取系统默认“通知提示音”的 UR
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //请求绕过优先级免打扰，是否生效取决于权限和用户设置。
        channel.setBypassDnd(true);
        //默认以名称作为说明，消息和下载渠道在对应分支中提供更具体的描述。
        channel.setDescription(channels[1]);
        //安全锁屏上
        //VISIBILITY_PUBLIC	显示完整内容
        //VISIBILITY_PRIVATE	显示通知，但隐藏敏感内容
        //VISIBILITY_SECRET	完全隐藏通知
        channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        //启用通知指示灯，需要设备支持。
        channel.enableLights(true);
        //将通知指示灯颜色设为红色，需要灯光已启用且设备支持该颜色。
        channel.setLightColor(Color.RED);
        //启用通知震动，具体节奏可通过 setVibrationPattern() 设置。
        channel.enableVibration(true);
        //单位为毫秒：等待 100ms 后震动 200ms，自定义分支会覆盖此节奏。
        channel.setVibrationPattern(new long[]{100, 200});
        switch (channels[0]) {
            case "1":
                //消息提醒：
                // 沿用公共声音、震动和灯光配置。
                break;
            case "2":
                //下载进度：
                // 低重要性，无声音、震动、灯光，不请求绕过免打扰。
                channel.setImportance(NotificationManager.IMPORTANCE_LOW);
                sound = null;
                channel.setBypassDnd(false);
                channel.enableLights(false);
                channel.enableVibration(false);
                break;
            case "3":
                //消息声音与震动模式:
                //默认通知声音，自定义震动节奏。
                channel.setVibrationPattern(informBean.pattern);
                break;
            case "4":
                //消息声音、震动与指示灯:
                //仅震动：关闭声音，沿用公共震动节奏和灯光配置。
                sound = null;
                break;
            case "5":
                //消息震动:
                //声音、震动、灯光已在公共配置中启用，无需重复设置。
                break;
            case "6":
                //自定义消息震动
                //自定义震动：关闭声音并替换震动节奏。
                sound = null;
                channel.setVibrationPattern(informBean.pattern);
                break;
            case "7":
                //自定义消息提醒
                //自定义声音和震动；sound 为 null 时无声音。
                sound = informBean.sound;
                channel.setVibrationPattern(informBean.pattern);
                break;
        }
        //将声音标记为通知提示音，交由系统按通知音量和策略处理。
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        //Uri uri = informBean.sound;
        //builder.setSound(uri);
        channel.setSound(sound, audioAttributes);
        notificationManager.createNotificationChannel(channel);
        //创建后重新读取系统保存的渠道，日志不使用创建前对象上的请求值。
        NotificationChannel createdChannel = notificationManager.getNotificationChannel(channels[2]);
        boolean isDnd = createdChannel != null && createdChannel.canBypassDnd();
        Logx.d("InformManager", "channelId:" + channels[2] + " isDnd---->" + isDnd);
    }

    //设置跳转activity
    private void setIntent(NotificationCompat.Builder builder, Intent intent, int reqCode) {
        PendingIntent pendingIntent;
        //FLAG_CANCEL_CURRENT: 取消旧的，创建新的，旧引用失效
        //FLAG_UPDATE_CURRENT：保留原来的，更新 extras，旧引用仍可触发
        //FLAG_IMMUTABLE：     执行方触发它时，不能修改其中的 Intent
        if (intent == null) {
            //可取消
            pendingIntent = PendingIntent.getActivity(context, reqCode,
                    new Intent(), PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, reqCode,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }
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
