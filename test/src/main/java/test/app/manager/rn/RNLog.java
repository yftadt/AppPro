package test.app.manager.rn;

import sj.mblog.Logx;

public class RNLog {
    public static void d(String tag,String msg) {
        Logx.d("RN更新_"+tag, msg);
    }
}
