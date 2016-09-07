package com.app.utiles.other;

import android.util.Log;

/**
 * Created by Administrator on 2016/3/9.
 */
public class DLog {
  public static void e(String tag, Object value) {
    Log.e(tag, value + "");
  }
}