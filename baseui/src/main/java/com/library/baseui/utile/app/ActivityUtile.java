package com.library.baseui.utile.app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.core.view.ViewCompat;

import com.library.baseui.activity.BaseApplication;

import java.io.Serializable;


public class ActivityUtile {
    /**
     * activity的通用跳转
     */
    public static void startActivityCommon(Class<?> activity) {
        startActivityBundle(activity, "", "", "", null);
    }

    //-------------------------------------String---------------------------------
    public static void startActivityString(Class<?> activity, String arg0) {
        startActivityBundle(activity, arg0, "", "", null);
    }


    public static void startActivityString(Class<?> activity, String arg0, String arg1) {
        startActivityBundle(activity, arg0, arg1, "", null);
    }

    public static void startActivityString(Class<?> activity, String arg0, String arg1, String arg2) {
        startActivityBundle(activity, arg0, arg1, arg2, null);
    }
    //-----------------------------------Serializable--------------------------------------------

    public static void startActivitySerializable(Class<?> activity, Serializable serializable) {
        startActivityBundle(activity, "", "", "", getBundle(serializable));
    }

    public static void startActivitySerializable(Class<?> activity, String arg0, Serializable serializable) {
        startActivityBundle(activity, arg0, "", "", getBundle(serializable));
    }


    public static void startActivitySerializable(Class<?> activity, String arg0, String arg1,
                                                 Serializable serializable) {
        startActivityBundle(activity, arg0, arg1, "", getBundle(serializable));
    }
//--------------------------------------bundle------------------------------------------

    public static void startActivityBundle(Class<?> activity, Bundle bundle) {
        startActivityBundle(activity, "", "", "", bundle);
    }


    public static void startActivityBundle(Class<?> activity, String arg0, Bundle bundle) {
        startActivityBundle(activity, arg0, "", "", bundle);
    }
//=================================================================

    /**
     * activity的通用跳转
     */
    public static void startActivityBundle(Class<?> activity, String arg0, String arg1, String arg2,
                                           Bundle bundle) {
        Intent it = new Intent();
        if (bundle != null) {
            it.putExtras(bundle);
        }
        if (!TextUtils.isEmpty(arg0)) {
            it.putExtra("arg0", arg0);
        }
        if (!TextUtils.isEmpty(arg1)) {
            it.putExtra("arg1", arg1);
        }
        if (!TextUtils.isEmpty(arg2)) {
            it.putExtra("arg2", arg2);
        }
        it.setClass(BaseApplication.context, activity);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.context.startActivity(it);
    }

    public static void startActivityElement(Activity activity, Class<?> actCls,
                                            View view, String elementShareName) {
        startActivityElement(activity, actCls, null, view, elementShareName);
    }
    /**
     * 启动共享元素动画
     *
     * @param view             共享元素View
     * @param elementShareName 共享元素名称 比如 shared_iv
     */
    /**
     * @param activity
     * @param dataBundle       要传递的数据
     * @param view             共享元素View
     * @param elementShareName 共享元素名称 比如 shared_iv
     */
    public static void startActivityElement(Activity activity, Class<?> actCls, Bundle dataBundle,
                                            View view, String elementShareName) {
        if (view != null) {
            String name = view.getTransitionName();
            if (TextUtils.isEmpty(name)) {
                view.setTransitionName(elementShareName);
            }
        }
        Intent intent = new Intent(activity, actCls);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                activity, view, elementShareName
        ).toBundle();
        if (dataBundle != null) {
            intent.putExtras(dataBundle);
        }
        activity.startActivity(intent, bundle);
        ViewCompat.setTransitionName(view, elementShareName);
    }

    public static void finishActivityElement(Activity activity) {
        finishActivityElement(activity, null, "");
    }

    /**
     * 返回上一个页面 共享元素动画
     *
     * @param activity
     * @param view             共享元素View
     * @param elementShareName 共享元素名称 比如 shared_iv
     */
    public static void finishActivityElement(Activity activity, View view, String elementShareName) {
        if (view != null) {
            String name = view.getTransitionName();
            if (TextUtils.isEmpty(name)) {
                view.setTransitionName(elementShareName);
            }
        }
        activity.finishAfterTransition();
    }

    /**
     * 关闭activity
     */
    public static void closeTopActivity(Class<?> cl) {
        closeTopActivity(cl, "");
    }

    public static void closeTopActivity(Class<?> cl, String arg0) {
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(arg0)) {
            intent.putExtra("arg0", arg0);
        }
        intent.setClass(BaseApplication.context, cl);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.context.startActivity(intent);
    }

    public static Bundle getBundle(Serializable value) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", value);
        return bundle;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm =
                new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 拨号界面
     */
    public static void callPhone(String tile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + tile));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.context.startActivity(intent);
    }

    /**
     * 分享
     *
     * @param subject
     * @param text
     */
    public static void shareFor(String subject, String text, String chooserName) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent = Intent.createChooser(intent, chooserName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.context.startActivity(intent);
    }

    /**
     * 打开nbc 应用
     */
    public static void startNBC(Context context) {
        String pckName = "com.xbkj.nbc";
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(pckName);
        //Intent intent = new Intent();
        intent.setData(Uri.parse("https://baidu.com?guom=1&utm_source=2&utm_campaign=3"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
