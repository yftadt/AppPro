package com.library.baseui.utile.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import sj.mblog.Logx;

//activity 生命周期
public class ActivityCycle {
    private int refCount = 0;
    private boolean isRegister;
    private List<SoftReference<Activity>> activitys = new ArrayList<>();

    class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {


        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            onActivityDestroyed(activity);
            activitys.add(new SoftReference(activity));
        }

        @Override
        public void onActivityStarted(Activity activity) {
            refCount++;

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            refCount--;


        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            int size = activitys.size();
            if (size == 0) {
                return;
            }
            for (int i = (size - 1); i >= 0; i--) {
                SoftReference<Activity> r = activitys.get(i);
                Activity a = r.get();
                if (a == null) {
                    activitys.remove(i);
                    continue;
                }
                // String a1 = a.getClass().getName();
                // String a2 = activity.getClass().getName();
                if (a.getClass().getName().equals(activity.getClass().getName())) {
                    activitys.remove(i);
                    break;
                }
            }
        }
    }

    //获取要返回的上一个activity 要在非onCreate里调用
    public Activity getBackActivity() {
        int size = activitys.size();
        if (size < 2) {
            return null;
        }
        return activitys.get(size - 2).get();
    }

    public int getBackActivitys() {
        return activitys.size();
    }

    /**
     * 监听程序是否在后台（在Application 调用）
     *
     * @param application
     */
    public void registerCallbacks(Application application) {
        if (isRegister) {
            return;
        }
        isRegister = true;
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleListener());
    }

    //true: 在后台运行
    public boolean isBackground() {
        boolean isBack = refCount == 0;
        String hint = isBack ? "后" : "前";
        Logx.d("APKInfo", "------" + hint + refCount);
        return isBack;
    }

    private static ActivityCycle instance;

    public static ActivityCycle getInstance() {
        if (instance == null) {
            instance = new ActivityCycle();
        }
        return instance;
    }
}

