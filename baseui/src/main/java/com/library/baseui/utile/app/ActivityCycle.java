package com.library.baseui.utile.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import sj.mblog.Logx;

//activity 生命周期
public class ActivityCycle {
    private int refCount = 0;
    private boolean isRegister;
    private List<SoftReference<Activity>> acts = new ArrayList<>();
    //当前的act
    private Activity cursorAct;

    public Activity getCursorAct() {
        return cursorAct;
    }

    //true 在前台
    private boolean isResumed;

    class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {


        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            onActivityDestroyed(activity);
            acts.add(new SoftReference(activity));
            isResumed = true;
        }

        @Override
        public void onActivityStarted(Activity activity) {
            refCount++;
            isResumed = true;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            cursorAct = activity;
            isResumed = true;
        }

        @Override
        public void onActivityPaused(Activity activity) {
            isResumed = false;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            refCount--;
            Logx.d("===>isShow: " + isResumed);
            if (!isResumed) {
                APKInfo.getInstance().restTimeZone();
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            int size = acts.size();
            if (size == 0) {
                return;
            }
            for (int i = (size - 1); i >= 0; i--) {
                SoftReference<Activity> sf = acts.get(i);
                Activity act = sf.get();
                if (act == null) {
                    acts.remove(i);
                    continue;
                }
                if (act == activity) {
                    acts.remove(i);
                    break;
                }

            }
        }
    }

    public void isAppInForeground(Context context) {
        boolean isShow = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                isShow = true;
            }
        }
        Logx.d("===>isShow: " + isShow);
    }

    //获取要返回的上一个activity 要在非onCreate里调用
    public Activity getBackActivity() {
        int size = acts.size();
        if (size < 2) {
            return null;
        }
        return acts.get(size - 2).get();
    }

    public int getBackActivitys() {
        return acts.size();
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

    public void setFinish(Class cls) {
        String name = cls.getName();
        for (int i = 0; i < acts.size(); i++) {
            SoftReference<Activity> temp = acts.get(i);
            if (temp == null) {
                continue;
            }
            Activity act = temp.get();
            if (act == null) {
                continue;
            }
            String actName = act.getLocalClassName();
            if (name.equals(actName)) {
                act.finish();
                return;
            }
        }
    }


    public void setFinishAll(Class cls) {
        String name = cls.getName();
        for (int i = 0; i < acts.size(); i++) {
            SoftReference<Activity> temp = acts.get(i);
            if (temp == null) {
                continue;
            }
            Activity act = temp.get();
            if (act == null) {
                continue;
            }
            if (act.isDestroyed()) {
                continue;
            }
            String actName = act.getLocalClassName();
            if (name.equals(actName)) {
                continue;
            }
            act.finish();
        }
    }

    public boolean isActExist(Class cls) {
        String name = cls.getName();
        boolean isActExist = false;
        for (int i = 0; i < acts.size(); i++) {
            SoftReference<Activity> temp = acts.get(i);
            if (temp == null) {
                continue;
            }
            Activity act = temp.get();
            if (act == null) {
                continue;
            }
            if (act.isDestroyed()) {
                continue;
            }
            String actName = act.getLocalClassName();
            if (name.equals(actName)) {
                isActExist = true;
                break;
            }

        }
        return isActExist;
    }

    private static ActivityCycle instance;

    public static ActivityCycle getInstance() {
        if (instance == null) {
            instance = new ActivityCycle();
        }
        return instance;
    }
}

