package com.app.ui.activity.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/9/7.
 */
public class BaseApplication extends Application {
    public static BaseApplication application;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = this;
    }
}
