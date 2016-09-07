package com.app.utiles.time;

import android.text.TextUtils;

import com.app.utiles.other.NumberUtile;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/2/27.
 */
public class DateUtile {
    public static String DATA_FORMAT_HM = "HH:mm";
    public static String DATA_FORMAT_MD = "MM-dd";
    public static String DATA_FORMAT_YMD = "yyyy-MM-dd";
    public static String DATA_FORMAT_YMD_HM = " yyyy-MM-dd HH:mm";
    public static String DATA_FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static String DATA_FORMAT_ZW_MD = "MM月dd日";
    public static String DATA_FORMAT_ZW_YMD = "yyyy年MM月dd日";
    public static String DATA_FORMAT_ZW_YMD_HMS = "yyyy年MM月dd日 HH:mm:ss";


    public static String getDateFormat(Date time, String type) {
        if (time == null) {
            time = new Date();
        }
        return getFormatDate(time, type);
    }

    //去秒
    public static String getDateFormatNoS(Date time, String type) {
        if (time == null) {
            time = new Date();
        }
        String timeFormat = getFormatDate(time, type);
        timeFormat = timeFormat.substring(0, (timeFormat.length() - 3));
        return timeFormat;
    }

    /**
     * 获取时间
     *
     * @param date
     * @param template
     * @return
     */
    private static String getFormatDate(Date date, String template) {
        SimpleDateFormat formatter = new SimpleDateFormat(template,
                Locale.SIMPLIFIED_CHINESE);
        String strDate = formatter.format(date);
        return strDate;
    }


    //支持yyyy-MM-dd HH:mm:ss格式，及被包含的格式
    public static Date stringToDate(String time, Date defout) {
        if (TextUtils.isEmpty(time) || time.length() < 10) {
            return defout;
        }
        try {
            return stringToDate(time);
        } catch (Exception e) {
            return defout;
        }
    }

    //支持yyyy-MM-dd HH:mm:ss格式，及被包含的格式
    private static Date stringToDate(String time) {
        time.replace("年", "-");
        time.replace("月", "-");
        time.replace("日", "-");
        if (!time.contains("-")) {
            String y = time.substring(0, 4);
            String m = time.substring(4, 6);
            String d = time.substring(6, 8);
            time = y + "-" + m + "-"+d;
        }
        if (TextUtils.isEmpty(time) || time.length() < 10) {
            return new Date();
        }
        if (time.length() == 10) {
            time += " 00:00:00";
        }
        if (time.length() == 16) {
            time += ":00";
        }

        if (time.length() != 19) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date daye = formatter.parse(time, pos);
        return daye;
    }

    //yyyy年MM月dd日 HH:mm:ss
    // 月-日 -周几
    public static String[] getTimeMDW(String timeStr) {
        String[] times = new String[3];
        Date timeDate = stringToDate(timeStr);
        String time = getDateFormat(timeDate, DATA_FORMAT_ZW_MD);
        String week = CalendarUtile.getDayOfWeek(timeDate);
        int day = CalendarUtile.getGapCount(timeDate, new Date());
        String futureDay = "";
        switch (day) {
            case 0:
                futureDay = "今天";
                break;
            case -1:
                futureDay = "明天";
                break;
        }
        if (TextUtils.isEmpty(futureDay)) {
            times[0] = "星期" + week;
            times[1] = time;
        } else {
            times[0] = futureDay;
            times[1] = time + " (星期" + week + ")";
        }
        times[2] = day + "";
        return times;
    }

    //年-月-日-时-分-周几
    public static String getTimeYMDHMSW(String timeStr) {
        Date timeDate = stringToDate(timeStr);
        String time = getDateFormatNoS(timeDate, DATA_FORMAT_ZW_YMD_HMS);
        time += " 周" + CalendarUtile.getDayOfWeek(timeDate);
        return time;
    }


    public static String getTimeObj(Object timeStr) {
        String hint = "--";
        if (timeStr == null) {
            return hint;
        }
        if (timeStr instanceof String) {
            String time = (String) timeStr;
            long timeL = NumberUtile.getStringToIntLong(time, 0);
            if (timeL > 0) {
                Date date = new Date();
                date.setTime(timeL);
                return getTimeMsg(date);
            }
            return getTimeMsg(time);
        }
        if (timeStr instanceof Long) {
            Long time = (Long) timeStr;
            long timeL = NumberUtile.getStringToIntLong(time + "", 0);
            if (timeL > 0) {
                Date date = new Date();
                date.setTime(timeL);
                return getTimeMsg(date);
            }
        }
        return hint;
    }

    //用于评论显示的时间
    public static String getTimeMsg(String timeStr) {
        Date date = stringToDate(timeStr, null);
        if (date == null) {
            return timeStr;
        }
        return getTimeMsg(date);
    }

    //时间比较提示
    public static String getTimeMsg(Date timeDate) {
        int[] HMS = CalendarUtile.getHMSDiff(timeDate, new Date());
        if (HMS != null) {
            return getHMSmsg(HMS);
        } else {
            int[] YMD = CalendarUtile.getDateDiff(timeDate, new Date());
            return getYMDmsg(timeDate, YMD);
        }
    }

    //时间比较（年 月 日）提示
    private static String getYMDmsg(Date timeDate, int[] YMD) {
        String msg = "???";
        int y = YMD[0];
        int m = YMD[1];
        int d = YMD[2];
        if (y != 0) {
            msg = getFormatDate(timeDate, DATA_FORMAT_ZW_YMD);
            return msg;
        }
        if (y == 0 && m == 0 && d == -1) {
            msg = "昨天";
            return msg;
        }
        m = Math.abs(m);
        d = Math.abs(d);
        if (y == 0 && (m > 0 || d >= 1)) {
            //显示完整时间
            msg = getFormatDate(timeDate, DATA_FORMAT_ZW_MD);
            return msg;
        }
        return msg;

    }

    //时间比较（时 分 秒）提示
    private static String getHMSmsg(int[] HMS) {
        String msg = "";
        int h = HMS[0];
        int m = HMS[1];
        int s = HMS[2];
        //isFuture:true 未来时间;
        if (h < 0 || m < 0 || s < 0) {
            // 未来时间;
            return msg;
        }
        if (h != 0) {
            msg = h + "小时前";
        }
        if (h == 0 && m != 0) {
            msg = m + "分钟前";
        }
        if (h == 0 && m == 0) {
            msg = "刚刚";
        }
        return msg;
    }

}
