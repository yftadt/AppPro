package sj.mblog;

import android.text.TextUtils;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;

/**
 * Created by sj on 16/4/13.
 */
public class Logx {

    public static final String LOG_TAG_DEFUALT = "MBLog";

    public static void init() {
        XLog.init(LogLevel.ALL);
    }

    public static synchronized void setTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            XLog.tag(LOG_TAG_DEFUALT);
        } else {
            XLog.tag(tag);
        }
    }

    public static synchronized void d(Object... args) {
        if (isDebug()) {
            Object[] data = new Object[args.length + 1];
            data[0] = getMethod();
            System.arraycopy(args, 0, data, 1, args.length);
            XLog.d(data);
        }
    }

    public static synchronized void d(String tag, Object... args) {
        if (isDebug()) {
            Object[] data = new Object[args.length + 1];
            data[0] = getMethod() + "  " + tag;
            System.arraycopy(args, 0, data, 1, args.length);
            XLog.d(data);
        }
    }


    public static synchronized void e(Object... args) {
        if (isDebug()) {
            Object[] data = new Object[args.length + 1];
            data[0] = getMethod();
            System.arraycopy(args, 0, data, 1, args.length);
            XLog.e(data);
        }
    }

    public static synchronized void e(String args, Throwable throwable) {
        if (isDebug()) {
            XLog.e(getMethod() + "   " + args, throwable);
        }
    }


    public static synchronized void e(String tag, Object... args) {
        if (isDebug()) {
            Object[] data = new Object[args.length + 1];
            data[0] = getMethod() + "  " + tag;
            System.arraycopy(args, 0, data, 1, args.length);
            XLog.e(data);
        }
    }

    public static synchronized void w(Object args) {
        if (isDebug())
            XLog.w(getMethod() + "%s\n", args);
    }

    public static synchronized void i(Object... args) {
        if (isDebug()) {
            Object[] data = new Object[args.length + 1];
            data[0] = getMethod();
            System.arraycopy(args, 0, data, 1, args.length);
            XLog.i(data);
        }
    }

    public static synchronized void v(Object args) {
        if (isDebug())
            XLog.v(getMethod() + "%s\n", args);
    }

    protected static String getMethod() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackOffset = 0;
        for (int i = trace.length - 1; i >= 0; i--) {
            if (trace[i].getClassName().contains(Logx.class.getName())) {
                break;
            }
            stackOffset = i;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("$ ")
                .append("(")
                .append(trace[stackOffset].getFileName())
                .append(":")
                .append(trace[stackOffset].getLineNumber())
                .append(")  ");
        return builder.toString();
    }

    public static synchronized void json(String args) {
        if (isDebug()) {
            XLog.json(args);
        }
    }


    public static synchronized void xml(String args) {
        if (isDebug()) {
            XLog.xml(args);
        }
    }

    private static boolean isDebug() {
        return true;
    }
}
