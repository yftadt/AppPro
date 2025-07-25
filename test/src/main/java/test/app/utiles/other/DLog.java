package test.app.utiles.other;

import android.util.Log;

import com.library.baseui.utile.other.BaseUILog;

/**
 * Created by Administrator on 2016/3/9.
 */
public class DLog extends BaseUILog {
    public static boolean DBUG = true;

    public static void e(String tag, Object value) {
        if (!DBUG) {
            return;
        }
        Log.e(tag, value + "");
    }
}