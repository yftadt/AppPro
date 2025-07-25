package com.library.baseui.utile.other;

import android.util.Log;

/**
 * Created by Administrator on 2016/3/9.
 */
public class BaseUILog {
    private static boolean DBUG = true;

    public static void setIsBug(boolean isDebug) {
        DBUG = isDebug;
    }

    public static void e(String tag, Object value) {
        if (!DBUG) {
            return;
        }
        Log.e(tag, value + "");
    }

    public static void e(Object value) {
        if (!DBUG) {
            return;
        }
        Log.e("BaseUILog", value + "");
    }
}