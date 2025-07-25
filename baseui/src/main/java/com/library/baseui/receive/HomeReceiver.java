package com.library.baseui.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import sj.mblog.Logx;

/**
 * Created by Administrator on 2017/8/2.
 */

public class HomeReceiver extends BroadcastReceiver {
    /**
     * 监听是否点击了home键将客户端推到后台
     */
    String SYSTEM_REASON = "reason";
    String SYSTEM_HOME_KEY = "homekey";
    String SYSTEM_HOME_KEY_LONG = "recentapps";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_REASON);
            if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                //表示按了home键,程序到了后台
                Logx.d("home键", "程序到了后台");
              //  BadgeUtil.setBadgeCount(context, 10, R.mipmap.ic_launcher);
            } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                //表示长按home键,显示最近使用的程序列表
                Logx.d("home键", "表示长按home键,显示最近使用的程序列表");
             }
        }
    }

    public static void registerReceiver(Context context) {
        context.registerReceiver(new HomeReceiver(), new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    public static void unregisterReceiver(Context context) {
        context.unregisterReceiver(new HomeReceiver());
    }
}
