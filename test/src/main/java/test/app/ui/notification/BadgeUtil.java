package test.app.ui.notification;

import android.app.Notification;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.library.baseui.activity.BaseApplication;
import com.library.baseui.utile.file.DataSave;
import com.library.baseui.utile.other.NumberUtile;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import sj.mblog.Logx;


/**
 * Created by Administrator on 2017/8/1.
 */

public class BadgeUtil {


    /**
     * 重置Badge(归0)
     */
    public static void resetBadgeCount(Context context) {
        setBadgeCount(context, 0);
    }

    private static int manufacturerType = -1;
    //是否支持角标
    private static boolean isSupportedBade = true;
    //记录角标数量
    private static int countUpdata;
    private static String TAG = "BadgeUtil";

    /**
     * 设置Badge
     */
    public static void setBadgeCount(final Context context, int count) {
        countUpdata = count;
        /*if (!APKInfo.getInstance().isBackground()) {
            countUpdata = count;
             Logx.d("角标设置：应用不在后台 记录数值 不去更新");
            return;
        }*/
        setBadgeCountForce(context, count, false);
    }

    //更新角标（有的手机应用在前台 设置角标无效，所以退到后台时在设置一次）
    public static void setBadgeCountUpdata(final Context context, int count) {
        if (count <= 0) {
            count = countUpdata;
        }
         Logx.d(TAG, "角标更新");
        setBadgeCountForce(context, count, true);

    }

    //true 支持角标
    private static boolean isSupportedBade() {
        if (manufacturerType == -1) {
            String type = DataSave.stringGet(DataSave.MANUFACTURE_TYPE);
            String isSupported = DataSave.stringGet(DataSave.MANUFACTURE_BADGE);
            isSupportedBade = "true".equals(isSupported);
            manufacturerType = NumberUtile.getStringToInt(type, -1);
        }
        if (manufacturerType == -1) {
            manufacturerType = getManufacturerType();
        }
        if (manufacturerType <= 0) {
             Logx.d(TAG, "获取厂商失败");
            return false;
        }
         Logx.d(TAG, "厂商  " + manufacturerType);
         Logx.d(TAG, "支持角标——》" + isSupportedBade);
        return isSupportedBade;
    }

    private static void setBadgeCountForce(Context context, int count, boolean isUpdate) {
        if (count <= 0) {
            count = 0;
        } else {
            count = Math.max(0, Math.min(count, 99));
        }
        if (!isSupportedBade()) {
            return;
        }
        setBadgeForce(context, count, isUpdate);

    }

    //设置角标
    private static void setBadgeForce(final Context context, int count, boolean isUpdate) {
        switch (manufacturerType) {
            case 1:
                //文档：https://dev.mi.com/console/doc/detail?pId=939
                //MIUI 6 至 MIUI 11
                //应用权限：
                //首先打开应用通知设置页面，在”设置-通知管理“里点击应用，/查看”显示桌面图标角标“开关是否开启。大部分应用默认是关闭状态。
                //通知类型：
                //非媒体通知、进度条通知和常驻通
                //点击应用图标后,会默认隐藏掉应用图标角标。有如下两种方式可以重新显示：
                //1:发一条新的通知，其通知id与之前发送的通知id不重复。
                //2:更新已发送通知的messageCount值。
                if (true) {
                    //小米不做角标设置,只在创建通知的时候做
                    //在有更新通知api的情况下 才有可能往下走
                    break;
                }
                int id = -1000;
                InformManager informManager = InformManager.getInstance(context);
                informManager.clearNotifyId(id);
                if (count == 0) {
                     Logx.d(TAG, "小米角标为0");
                    return;
                }
                String packageName = context.getPackageName();
                launcherClassName = getLauncherClassName(context);
                Intent it = new Intent();
                it.setClassName(packageName, launcherClassName);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                InformBean informBean = new InformBean();
                informBean.msgTitle = "衢州市人民医院";
                informBean.msgContent = "未读消息数" + count;
                informBean.msgTicker = "未读消息数" + count;
                informBean.intent = it;
                informBean.informId = id;
                informBean.informReqCode = id;
                informBean.informAutoCancel = true;
                informManager.setInformContent(informBean);
                NotificationCompat.Builder builder = informManager.getBuilder();
                Notification notification = builder.build();
                //
                setBadgeXM(notification, count);
                //不支持角标
                if (!isSupportedBade) {
                    return;
                }
                informManager.show(id);
                break;
            case 2:
                //sony 索尼
                //需添加权限：<uses-permission android:name="com.sonyericsson.home.permission.BROADCAST_BADGE" />
                String launcherClassName = getLauncherClassName(context);
                if (launcherClassName == null) {
                    return;
                }
                boolean isShow = true;
                if (count == 0) {
                    isShow = false;
                }
                Intent localIntent = new Intent();
                localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
                localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);//是否显示
                localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherClassName);//启动页
                localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));//数字
                localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());//包名
                context.sendBroadcast(localIntent);
                break;
            case 3:
                //samsung,lg 三星
                // 获取你当前的应用
                launcherClassName = getLauncherClassName(context);
                if (launcherClassName == null) {
                    return;
                }
                Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                intent.putExtra("badge_count", count);
                intent.putExtra("badge_count_package_name", context.getPackageName());
                intent.putExtra("badge_count_class_name", launcherClassName);
                context.sendBroadcast(intent);
                break;
            case 4:
                //htc HTC
                Intent intentNotification = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
                ComponentName localComponentName = new ComponentName(context.getPackageName(), getLauncherClassName(context));
                intentNotification.putExtra("com.htc.launcher.extra.COMPONENT", localComponentName.flattenToShortString());
                intentNotification.putExtra("com.htc.launcher.extra.COUNT", count);
                context.sendBroadcast(intentNotification);
                //
                Intent intentShortcut = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
                intentShortcut.putExtra("packagename", context.getPackageName());
                intentShortcut.putExtra("count", count);
                context.sendBroadcast(intentShortcut);
                break;
            case 5:
                //nova NOVE
                ContentValues contentValues = new ContentValues();
                contentValues.put("tag", context.getPackageName() + "/" + getLauncherClassName(context));
                contentValues.put("count", count);
                context.getContentResolver().insert(Uri.parse("msgContent://com.teslacoilsw.notifier/unread_count"),
                        contentValues);
                break;
            case 6:
                //文档 https://developer.huawei.com/consumer/cn/doc/development/system-Examples/30802
                //权限<uses-permission android:name="com.huawei.android.launcher.permission.CHANGE_BADGE"/>
                //huawei 华为
                //系统版本：EMUI4.1 及以上
                //桌面版本：6.3.29
                try {
                    Bundle extra = new Bundle();
                    packageName = context.getPackageName();
                    extra.putString("package", packageName);//包名
                    launcherClassName = getLauncherClassName(context);
                     Logx.d(TAG, "华为角标：" + count + " 包名=》" + packageName + " 启动类名=》" + launcherClassName);
                    extra.putString("class", launcherClassName);//桌面图标对应的应用入口Activity类
                    extra.putInt("badgenumber", count);//badgenumber：角标数字
                    context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, extra);
                } catch (Exception e) {
                     Logx.d(TAG, "华为角标：不支持");
                    isSupportedBade = false;
                    DataSave.stringSave(DataSave.MANUFACTURE_BADGE, isSupportedBade + "");
                }
                break;
            case 7:
                //vivo
                //文档: https://dev.vivo.com.cn/documentCenter/doc/459
                //权限：<uses-permission android:name="com.vivo.notification.permission.BADGE_ICON" />
                //需要在应用信息，通知里面 开启“桌面角标”
                intent = new Intent();
                packageName = context.getPackageName();
                launcherClassName = getLauncherClassName(context);
                intent.setAction("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
                intent.putExtra("packageName", packageName);
                intent.putExtra("className", launcherClassName);
                intent.putExtra("notificationNum", count);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    intent.addFlags(0x01000000);
                     Logx.d(TAG, "vivo角标：8.0");
                }
                context.sendBroadcast(intent);
                 Logx.d(TAG, "vivo角标：更新" + count + " 包名：" + packageName + " 启动：" + launcherClassName);
                break;
            case 8:
                //oppo 没有开放 官方文档网站没有介绍
                //在线客服：
                // https://vs.rainbowred.com/visitor/pc/chat.html?companyId=418&routeEntranceId=86
                //必须在oppo商店上架
                //OPPO手机为了保护用户体验，ColorOS的角标功能主要采取邀请制为主，
                //开发者主动申请为辅的原则，推送内容需遵循国家规范，
                // 其中营销类和推广类应用暂不接受申请。
                //从保护用户体验的角度出发，ColorOS的角标功能采取邀请制，
                // 暂时只邀请用户量较大的即时通信类、邮件类应用；未收到邀请的应用，
                // 角标接入需按照流程主动申请，经OPPO方评估后，应用角标能够提升用户体验的应用，
                // 方可通过申请；申请接入应用角标的应用必须不违反国家或地方相关法律和政策规定；
                // ColorOS的角标功能不允许应用发出营销活动、广告等任何商业推广类通知，
                // 原则上只通过应用的IM类提醒功能角标申请。
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("app_badge_count", count);
                    context.getContentResolver().call(Uri.parse("msgContent://com.android.badge/badge"), "setAppBadgeCount", null, extras);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                break;
        }
    }

    //设置角标通知
    public static void setBadgeNotification(Notification notification, int count) {
        boolean isSupport = isSupportedBade();
        if (!isSupport) {
            return;
        }
        switch (manufacturerType) {
            case 1:
                //小米 角标通知
                //小米默认角标+1，想要角标显示，通知栏新增加通知的时候，应用必须在后台
                 Logx.d(TAG, "角标通知不设置：" + count);
                //setBadgeXM(notification, count);
                break;
            default:
                //其它的 设置角标
                setBadgeForce(BaseApplication.context, count, false);
                break;
        }

    }

    //设置小米的角标数量 true 设置成功
    private static void setBadgeXM(Notification notification, int count) {

        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, count);
             Logx.d(TAG, "小米角标：" + count);
        } catch (Exception e) {
            e.printStackTrace();
             Logx.d(TAG, "小米：不支持角标:" + e.getMessage());
            isSupportedBade = false;
            DataSave.stringSave(DataSave.MANUFACTURE_BADGE, isSupportedBade + "");
        }

    }

    //获取厂商类型
    private static int getManufacturerType() {
        int manufacturerType = 0;
        String type = Build.MANUFACTURER;
        if (type == null) {
            type = "";
        }
        type = type.toLowerCase();
         Logx.d(TAG, "厂商：" + type);
        if (type.contains("xiaomi")) {
            manufacturerType = 1;
        }
        if (type.contains("sony")) {
            manufacturerType = 2;
        }
        if (type.contains("samsung") || type.contains("lg")) {
            manufacturerType = 3;
        }
        if (type.contains("htc")) {
            manufacturerType = 4;
        }
        if (type.contains("nova")) {
            manufacturerType = 5;
        }
        if (type.contains("huawei")) {
            manufacturerType = 6;
        }
        if (type.contains("vivo")) {
            manufacturerType = 7;
        }
        if (type.contains("oppo")) {
            manufacturerType = 8;
        }
        if (manufacturerType > 0) {
            DataSave.stringSave(DataSave.MANUFACTURE_TYPE, manufacturerType);
            isSupportedBade = true;
            DataSave.stringSave(DataSave.MANUFACTURE_BADGE, isSupportedBade + "");
        }
        return manufacturerType;
    }

    //清除通知
    public static void clearNotifyIdOfMIUI(Context context) {
        int id = -11110;
        InformManager informManager = InformManager.getInstance(context);
        informManager.clearNotifyId(id);
    }

    private static String launcherClassName;

    //获取默认启动页面
    private static String getLauncherClassName(Context context) {
        if (!TextUtils.isEmpty(launcherClassName)) {
            return launcherClassName;
        }
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }
        String name = info.activityInfo.name;
        launcherClassName = name;
         Logx.d(TAG, "启动页面:" + name);
        return name;
    }


}
