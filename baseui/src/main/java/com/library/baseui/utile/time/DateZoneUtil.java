package com.library.baseui.utile.time;

import com.library.baseui.utile.app.APKInfo;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import sj.mblog.Logx;

/**
 * 时区
 */
class DateZoneUtil {
    private static String tag = "DateUtile_";

    /**
     * 获取一个标准时间
     *
     * @param type
     * @return 标准时间
     */
    public static String getUTCDateFormat(String type) {
        return getTimeZone("UTC", type).time;
    }


    /**
     * 获取时区时间
     *
     * @param zoneId 时区id
     * @param format 输出格式
     * @return
     */
    public static TimeBean getTimeZone(String zoneId, String format) {
        ZoneId id = ZoneId.of(zoneId);
        ZonedDateTime utcTime = ZonedDateTime.now(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format).withZone(id);
        String timeUTC = utcTime.format(formatter);
        Instant instant = utcTime.toInstant();
        //时间戳
        long zoneTimeJab = instant.toEpochMilli();
        Logx.d("获取时区时间 time=" + timeUTC + " 时间戳：" + zoneTimeJab + " 时区：" + utcTime + " utcTime:" + utcTime);
        TimeBean bean = new TimeBean();
        bean.time = timeUTC;
        bean.timeStamp = zoneTimeJab;
        bean.zoneId = utcTime.toString();
        return bean;
    }

    /**
     * 时间转化
     *
     * @param timeZone 时区 （UTC，Asia/Shanghai）
     * @param times    要转的时间
     * @param format   格式化（yyyy-MM-dd HH:mm:ss）
     * @return 时间
     */
    public static String getZoneTime(String timeZone, long times, String format) {
        Instant instant = Instant.ofEpochMilli(times);
        ZoneId id = ZoneId.of(timeZone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format).withZone(id);
        String zoneTime = formatter.format(instant);
        //Logx.d("====> 时间转换 时区（" + timeZone + "）：" + zoneTime + "  " + times);
        return zoneTime;
    }

    private static Integer timeSpace;

    /**
     * 获取时差（本地时间-标准时间）
     *
     * @return
     */
    private static long getTimeDifference() {
        if (timeSpace != null) {
            return timeSpace;
        }
        //
        String zoneId = APKInfo.getInstance().getTimeZoneId();
        ZoneOffset offset = ZoneId.of(zoneId).getRules().getOffset(Instant.now());
        //毫秒
        int time = offset.getTotalSeconds();
        timeSpace = time;
        Logx.d(tag, "标准时间：" + time + " 时差:" + time);
        return time;
    }

    //重置时差
    public static void restTimeDifference() {
        timeSpace = null;
    }

//

    /**
     * 标准时间转本地时间
     *
     * @param uctTime 时间戳（毫秒）
     * @return 本地时间
     */
    public static long getUTCToLocalDate(Long uctTime) {
        return getUTCToLocalDate(APKInfo.getInstance().getTimeZoneId(), uctTime);
    }

    /**
     * 标准时间转本地时间
     *
     * @param uctTime 时间戳（毫秒）
     * @return 本地时间
     */
    public static long getUTCToLocalDate(String zoneId, Long uctTime) {
        Calendar calendar = getUTCToCalendar(zoneId, uctTime);
        long utcTimeMillis = calendar.getTimeInMillis();
        return utcTimeMillis;
    }

    /**
     * 服务器上的标准时间 转化为本地时间 用中国时间去转
     *
     * @param timStamp 时间戳（毫秒）
     * @return
     */
    public static Date getUTCToLocal(long timStamp) {
        //时差
        ZoneOffset offset = ZoneId.of("Asia/Shanghai").getRules().getOffset(Instant.now());
        //秒
        int time = offset.getTotalSeconds();
        long difference = time * 1000;
        Logx.d(tag, "时差：" + time);
        //标准时间
        Calendar calendar = getUTCToCalendar("Asia/Shanghai", timStamp + difference);
        String str = redCalendar(calendar);
        Logx.d(tag, "时间纠正为 Asia/Shanghai：" + str);
        //转为本地时间 调用下面注释的方法也行
       /* String zoneId = APKInfo.getInstance().getTimeZoneId();
        calendar = getUTCToCalendar(zoneId, calendar.getTimeInMillis());
        str = redCalendar(calendar);*/
        // Logx.d(tag, "时间纠正为" + zoneId + "：" + str);
        //转换为本地时间  可以用Date（calendar.getTime()），否因为时区的关系，Date 会自动转为本地时间
        return calendar.getTime();
    }

    /**
     * 标准时间转本地时间
     *
     * @param uctTime 时间戳（毫秒）
     * @return 本地时间
     */
    public static Calendar getUTCToCalendar(String zoneId, Long uctTime) {
        long currentTimeMillis = uctTime;
        //
        TimeZone utcTimeZone = TimeZone.getTimeZone(zoneId);
        Calendar calendar = Calendar.getInstance(utcTimeZone);
        //设置时间
        calendar.setTimeInMillis(currentTimeMillis);
        return calendar;
    }

    /**
     * 本地时间转标准时间
     *
     * @param dateLocal(本地时间戳)
     * @return 标准时间
     */
    public static long getLocalToUTCDate(Long dateLocal) {
        return getLocalToUTCDate("UTC", dateLocal);
    }

    /**
     * 本地时间转标准时间
     *
     * @param dateLocal(本地时间戳)
     * @return 标准时间
     */
    public static long getLocalToUTCDate(String zoneId, Long dateLocal) {
        long currentTimeMillis = dateLocal;
        //
        TimeZone utcTimeZone = TimeZone.getTimeZone(zoneId);
        Calendar calendar = Calendar.getInstance(utcTimeZone);
        //设置时间
        calendar.setTimeInMillis(currentTimeMillis);
        //获取 UTC 时间戳
        long utcTimeMillis = calendar.getTimeInMillis();
        return utcTimeMillis;
    }

    /**
     * 本地时间转标准时间
     *
     * @param dateLocal(本地时间戳)
     * @return 标准时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getLocalToUTCDateStr(Long dateLocal) {
        long currentTimeMillis = dateLocal;
        //
        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(utcTimeZone);
        //设置时间
        calendar.setTimeInMillis(currentTimeMillis);
        String utcTime = redCalendar(calendar);
        return utcTime;
    }
    //

    /**
     * 读取日历
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String redCalendar(Calendar calendar) {
        // 获取年、月、日、时、分、秒
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份+1，因为0代表一月
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        //年
        String timeStr = year + "";
        //月
        String monthStr = month + "";
        if (month < 10) {
            monthStr = "0" + month + "";
        }
        timeStr += ":" + monthStr;
        //日
        String dayStr = dayOfMonth + "";
        if (dayOfMonth < 10) {
            dayStr = "0" + dayStr + "";
        }
        timeStr += ":" + dayStr;
        //时
        String hourOfDayStr = hourOfDay + "";
        if (hourOfDay < 10) {
            hourOfDayStr = "0" + hourOfDay + "";
        }
        timeStr += " " + hourOfDayStr;
        //分
        String minuteStr = minute + "";
        if (minute < 10) {
            minuteStr = "0" + minute + "";
        }
        timeStr += ":" + minuteStr;
        //秒
        String secondStr = second + "";
        if (second < 10) {
            secondStr = "0" + second + "";
        }
        timeStr += ":" + secondStr;
        return timeStr;
    }
}
