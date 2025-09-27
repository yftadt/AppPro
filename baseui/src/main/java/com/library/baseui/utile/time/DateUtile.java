package com.library.baseui.utile.time;

import android.health.connect.datatypes.StepsCadenceRecord;
import android.text.TextUtils;
import android.util.Log;


import com.library.baseui.utile.app.APKInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import sj.mblog.Logx;

/**
 * Created by Administrator on 2016/2/27.
 */
public class DateUtile {
    public static String DATA_FORMAT_Y = "yyyy";

    public static String DATA_FORMAT_YMD = "yyyy-MM-dd";
    public static String DATA_FORMAT_DMY = "dd-MM-yyyy";
    public static String DATA_FORMAT_YMD2 = "yy.MM.dd";
    public static String DATA_FORMAT_YMD_1 = "yyyy/MM/dd";
    public static String DATA_FORMAT_YMD_24HM = "yyyy-MM-dd HH:mm";
    public static String DATA_FORMAT_YMD_24HM_1 = "yyyy/MM/dd HH:mm";
    public static String DATA_FORMAT_YMD_24HMS = "yyyy-MM-dd HH:mm:ss";
    public static String DATA_FORMAT_YMD_24HMS_1 = "yyyy/MM/dd HH:mm:ss";
    //
    public static String DATA_FORMAT_ZW_YMD = "yyyy年MM月dd日";

    public static String DATA_FORMAT_ZW_YMD_24HMS = "yyyy年MM月dd日 HH:mm:ss";
    //
    public static String DATA_FORMAT_MD = "MM-dd";
    public static String DATA_FORMAT_DM = "dd-MM";
    public static String DATA_FORMAT_MD_1 = "MM/dd";
    public static String DATA_FORMAT_ZW_MD = "MM月dd日";
    //
    public static String DATA_FORMAT_MD_24HM = "MM-dd HH:mm";
    public static String DATA_FORMAT_MD_24HM_1 = "MM月dd日 HH:mm";
    public static String DATA_FORMAT_MD_24HM_2 = "MM/dd HH:mm";
    //
    public static String DATA_FORMAT_24HM = "HH:mm";
    public static String DATA_FORMAT_12HM = "HH:mm";
    public static String DATA_FORMAT_24HMS = "HH:mm:ss";

    //
    public static String getDateFormat(Date time, String type) {
        if (time == null) {
            time = new Date();
        }
        return getFormat(time, type);
    }

    //去秒
    public static String getDateFormatNoS(Date time, String type) {
        if (time == null) {
            time = new Date();
        }
        String timeFormat = getFormat(time, type);
        timeFormat = timeFormat.substring(0, (timeFormat.length() - 3));
        return timeFormat;
    }

    /**
     * 时间格式化
     *
     * @param date
     * @param template
     * @return
     */
    private static String getFormat(Date date, String template) {
        // SimpleDateFormat formatter = new SimpleDateFormat(template, Locale.SIMPLIFIED_CHINESE);
        //getDefault 用于获取系统默认的地区设置， 你需要特定地区（如美国），可以使用Locale.US。
        SimpleDateFormat formatter = new SimpleDateFormat(template, Locale.getDefault());
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
            time = y + "-" + m + "-" + d;
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

    /**
     * 获取一个易读时间
     *
     * @param date
     * @return
     */
    public static String getTimeReadability(Date date) {
        if (date == null) {
            return "";
        }
        Date nowDate = new Date();
        long nowTime = nowDate.getTime();
        long time = date.getTime();
        long temp = nowTime - time;
        if (temp < 0) {
            //时间 纠正 （一般来讲 现在时间是大于发布时间的的，修改系统时间的除外）
            if (temp > -60 * 1000) {
                return "just now";
            }
            //时间错误
            return getDateFormat(date, DATA_FORMAT_YMD_24HM);
        }
        long second = 1000;
        long minute1 = 60 * second;
        long hour1 = 60 * minute1;
        long hour24 = 24 * hour1;
        //
        long tempHour = temp / hour1;//小时
        long tempMinute = (temp % hour1) / minute1;
        long tempSecond = (temp % minute1) / second;
        //
        if (temp < 10 * minute1) {
            //10分钟内发布的内容（包括发布资讯/帖子，评论，回复和点赞）在前端页面显示“刚刚”；
            return "just now";
        }
        if (temp < 59 * minute1) {
            //10-59分钟内发布的内容，在前端显示为“对应N分钟以前”；
            String str = tempMinute + " minutes ago";
            return str;
        }
        if (temp < hour24) {
            //超过1小时小于24小时，显示对应的数值，比如说1小时59分钟显示“1小时以前”,6小时59分钟显示“6小时以前”
            String str = tempHour + " hours ago";
            return str;
        }
        String str1 = getDateFormat(date, DATA_FORMAT_Y);
        String str2 = getDateFormat(nowDate, DATA_FORMAT_Y);
        if (str1.equals(str2)) {
            //超过24小时显示：日/月
            String str = getDateFormat(date, DATA_FORMAT_DM);
            return str;
        }
        //超过跨年显示：日/月/年
        String str = getDateFormat(date, DATA_FORMAT_DMY);
        return str;

    }

    /**
     * @param date 当前时间
     * @return 1：上午  2：下午
     */
    public static int getAmOrPm(Date date) {
        int state = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 获取小时（24小时制）
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        // 判断是上午还是下午
        if (hourOfDay >= 12) {
            //下午
            state = 2;
        } else {
            //上午
        }
        return state;
    }

    public static String getAmOrPmStr(Date date) {
        String state = "AM";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 获取小时（24小时制）
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        // 判断是上午还是下午
        if (hourOfDay >= 12) {
            //下午
            state = "PM";
        } else {
            //上午
        }
        return state;
    }


    public static String getTimeStr(int time) {
        return getTimeStr((long) time);
    }

    public static String getTimeStr(long time) {
        long second = 1000;
        long minute1 = 60 * second;
        long hour1 = 60 * minute1;
        long h = time / hour1;
        long m = (time % hour1) / minute1;
        long s = (time % minute1) / second;
        String strH = "";
        if (h > 0) {
            strH = "" + h + ":";
        }
        String strM = "";
        if (m > 9) {
            strM = m + ":";
        } else {
            strM = "0" + m + ":";
        }
        String strS = "";
        if (s > 9) {
            strS = s + "";
        } else {
            strS = "0" + s;
        }
        return strH + strM + strS;
    }

    /**
     * @param time(秒)
     * @return 00:00
     */
    public static String getTime(int time) {
        String f = "";
        String s = "";
        int f1 = time / 60;
        int s1 = time % 60;
        f = String.valueOf(f1);
        s = String.valueOf(s1);
        if (f1 <= 9) {
            f = "0" + f1;
        }
        if (s1 <= 9) {
            s = "0" + s1;
        }
        return f + ":" + s;
    }

    public static String getTime(String date) {
        Date date1 = stringToDate(date);
        return getTime(date1);
    }

    public static String getTime(Date date1) {
        return getTime(date1, false);
    }

    public static String getTime(Date date1, boolean isTomorrow) {
        if (date1 == null) {
            return "";
        }
        Date date2 = new Date();
        int dayCount = CalendarUtile.getCompareDay(date1, date2);
        if (dayCount == -1 && isTomorrow) {
            //"明天  12:00"
            return "明天   " + getDateFormat(date1, DATA_FORMAT_24HM);
        }
        if (dayCount == 0) {
            //是今天 显示"12:00"
            return getDateFormat(date1, DATA_FORMAT_24HM);
        }
        if (dayCount == 1) {
            //"昨天  12:00"
            return "昨天   " + getDateFormat(date1, DATA_FORMAT_24HM);
        }
        int yearCount = CalendarUtile.getCompareYear(date1, date2);
        if (yearCount == 0) {
            //同一年
            //显示 "2018年12月 12:00"
            return getDateFormat(date1, DATA_FORMAT_MD_24HM_1);
        }
        //显示 "2017年12月"
        return getDateFormat(date1, DATA_FORMAT_ZW_YMD);
    }

    /**
     * 获取一个标准时间
     *
     * @param type
     * @return 标准时间
     */
    public static String getUTCDateFormat(String type) {
        TimeBean timeBean = DateZoneUtil.getUTCDateFormatBean(type);
        return timeBean.time;
    }


    /**
     * 标准时间转本地时间
     *
     * @param dateUCT 标准时间
     * @return 本地时间
     */
    public static long getUTCToLocalDateTimeStamp(long dateUCT) {
        Date timeBean = getUTCToLocalDate(dateUCT);
        return timeBean.getTime();
    }


    /**
     * 标准时间转本地时间
     *
     * @param dateUCT 标准时间
     * @param format  格式
     * @return 本地时间
     */
    public static String getUTCToLocalDateStr(long dateUCT, String format) {
        //
        Date date = getUTCToLocalDate(dateUCT);
        String str = getDateFormat(date, format);
        return str;
    }

    /**
     * 标准时间转本地时间
     *
     * @param dateUCT 标准时间
     * @return 本地时间
     */
    public static Date getUTCToLocalDate(long dateUCT) {
        Date date = DateZoneUtil.getUTCToLocal(dateUCT);
        return date;
    }

    /**
     * 本地时间转标准时间
     *
     * @param dateLocal
     * @return 标准时间
     */
    public static long getLocalToUTCDate(Date dateLocal) {
        TimeBean timeBean = DateZoneUtil.getLocalToUTCDateBean(dateLocal.getTime());
        return timeBean.timeStamp;
    }


    public static void testLog() {
        test3();
        test2();
        test();
    }


    //获取utc时间戳 没有问题
    private static void test3() {
        // 获取当前系统时间戳（毫秒）
        long currentTimeMillis = System.currentTimeMillis();
        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(utcTimeZone);
        //设置时间
        calendar.setTimeInMillis(currentTimeMillis);
        //获取 UTC 时间戳
        long utcTimeMillis = calendar.getTimeInMillis();
        //涉及时区的不能直接获取Date（会默认转一下本地时区，会出错）
        Date date = calendar.getTime();
        Logx.d("====>测试3 utc时间戳：" + utcTimeMillis + " date:" + date);
    }

    //时区转换 没有问题
    private static void test2() {
        // 以中国时区获取当前时间:
        ZonedDateTime zbj = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        // 转换为本地时间:
        ZonedDateTime zny = zbj.withZoneSameInstant(ZoneId.of(APKInfo.getInstance().getTimeZoneId()));
        // utc 时间
        ZonedDateTime utcTime = zbj.withZoneSameInstant(ZoneId.of("UTC"));
        Logx.d("====>测试2 时区转换 中国时间：" + zbj + "  本地时间：" + zny + " utc时间：" + utcTime);
    }

    //一个标准时间 没有问题
    private static void test() {
        // 获取当前当地时间和当地时区
        LocalDateTime localDateTime = LocalDateTime.now(); //  如果需要，替换为实际的LocalDateTime对象
        ZoneId localZone = ZoneId.systemDefault(); //获取当地时区
        ZonedDateTime zonedDateTime = localDateTime.atZone(localZone); //  使用本地区域将LocalDateTime转换为ZonedDateTime
        //转换为UTC时区日期时间
        ZonedDateTime utcZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC); //  转换为UTC时区日期时间
        // 如果需要的话，可以选择将UTC时间转换回LocalDateTime(大多数情况下并不一定需要)
        LocalDateTime utcLocalDateTime = utcZonedDateTime.toLocalDateTime(); // 如果需要进一步处理或显示，请转换回LocalDateTime
        // 将UTC时间格式化为字符串(可选)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC); // 用所需的模式和时区(UTC)定义UTC时间的格式化程序
        String utcTimeString = utcLocalDateTime.format(formatter); // 将LocalDateTime格式化为UTC时区格式的字符串
        Logx.d("====> 标准时间  " + utcTimeString);
    }


}
