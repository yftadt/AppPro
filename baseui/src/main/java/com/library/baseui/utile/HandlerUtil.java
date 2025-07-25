package com.library.baseui.utile;

import android.os.Handler;
import android.os.Looper;

public class HandlerUtil {
    private static Handler sMainHandler = new Handler(Looper.getMainLooper());


    public static void runInMainThread(Runnable runnable) {
        runInMainThread(runnable, 0);
    }

    public static void runInMainThread(Runnable runnable, long j) {
        sMainHandler.postDelayed(runnable, j);
    }

    public static void removeCallbacks(Runnable runnable) {
        sMainHandler.removeCallbacks(runnable);
    }
}
