package com.app.ui;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;


import com.library.baseui.activity.BaseApplication;
import com.library.baseui.utile.app.DeviceSafe;
import com.tencent.bugly.crashreport.CrashReport;

import sj.mblog.Logx;

/**
 * Created by Administrator on 2016/9/7.
 */
public class MainApplication extends BaseApplication {


    @Override
    protected void initSdk() {
        super.initSdk();
        Logx.init();
        //bugly
        CrashReport.initCrashReport(getApplicationContext(), "7e8d6a4fc6", true);
        DeviceSafe.setCheck(context);
    }
}
