package com.library.baseui.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.library.baseui.utile.app.ActivityCycle;
import com.library.baseui.utile.app.DeviceSafe;


/**
 * Created by Administrator on 2016/9/7.
 */
public class BaseApplication extends Application {
    public static BaseApplication application;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }

    private void initApplication() {
        if (!getPackageName().equals(getCurrentProcessName())) {
            return;
        }
        application = this;
        context = this;
        ActivityCycle.getInstance().registerCallbacks(this);
        initSdk();
    }

    protected void initSdk() {

    }

    /**
     * 获取当前进程名
     */
    private String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    //突破64k
    protected void attachBaseContext(Context base) {
        //系统语言等设置发生改变时会调用此方法，需要要重置app语言
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
