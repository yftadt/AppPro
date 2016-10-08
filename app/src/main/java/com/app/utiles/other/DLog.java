package com.app.utiles.other;

import android.util.Log;

/**
 * Created by Administrator on 2016/3/9.
 */
public class DLog {
    public static boolean DBUG = true;

    public static void e(String tag, Object value) {
        if (!DBUG) {
            return;
        }
        Log.e(tag, value + "");
    }
}