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
    public static TimeBean getUTCDateFormatBean(String type) {
        return getTimeZone("UTC", type);
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
        //标准时间
        Calendar calendar = getZoneToCalendar("Asia/Shanghai", timStamp + difference);
        String str = redCalendar(calendar);
        //Logx.d("日历时间：" + str);
        //会有问题 不准
        //Date date = calendar.getTime();
        Date date = DateUtile.stringToDate(str, new Date());
        return date;
    }

    /**
     * 本地时间转标准时间
     *
     * @param dateLocal 本地时间戳
     * @return 标准时间
     */
    public static TimeBean getLocalToUTCDateBean(Long dateLocal) {
        long currentTimeMillis = dateLocal;
        //
        Calendar calendar = getZoneToCalendar("UTC", dateLocal);
        //设置时间
        calendar.setTimeInMillis(currentTimeMillis);
        String utcTime = redCalendar(calendar);
        //获取utc时间戳
        ZoneOffset shOffset = ZoneId.of("Asia/Shanghai").getRules().getOffset(Instant.now());
        int shTime = shOffset.getTotalSeconds();
        long shDifference = shTime * 1000;
        long shNewTime = dateLocal - shDifference;
        Calendar shCalendar = getZoneToCalendar("Asia/Shanghai", shNewTime);
        long times = shCalendar.getTime().getTime();
        //
        TimeBean bean = new TimeBean();
        bean.time = utcTime;
        bean.timeStamp = times;
        bean.zoneId = utcTime.toString();
        return bean;
    }

    /**
     * 获取时区时间
     *
     * @param zoneId 时区id
     * @param format 输出格式
     * @return
     */
    private static TimeBean getTimeZone(String zoneId, String format) {
        ZoneId id = ZoneId.of(zoneId);
        ZonedDateTime utcTime = ZonedDateTime.now(id);
        //
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format).withZone(id);
        String timeUTC = utcTime.format(formatter);
        //
        Instant instant = utcTime.toInstant();
        //时间戳 无用 会获取到本地的当前时间错
        long zoneTimeJab = instant.toEpochMilli();
        //转化时间戳
        //时差
        ZoneOffset shOffset = ZoneId.of("Asia/Shanghai").getRules().getOffset(Instant.now());
        int shTime = shOffset.getTotalSeconds();
        long shDifference = shTime * 1000;
        long shNewTime = new Date().getTime() - shDifference;
        //标准时间
        Calendar shCalendar = getZoneToCalendar("Asia/Shanghai", shNewTime);
        long times = shCalendar.getTime().getTime();
        //Logx.d(" 时差3：" + times + "   testTime:" + testTime + " difference2=" + difference2);
        TimeBean bean = new TimeBean();
        bean.time = timeUTC;
        bean.timeStamp = times;
        bean.zoneId = utcTime.toString();
        //
        Logx.d(tag+"获取时区时间 time=" + timeUTC + " 时间戳：" + times + " 时区：" + utcTime + " utcTime:" + utcTime + " zoneId:" + zoneId + " 时差：" + shTime);

        return bean;
    }

    /**
     * 转成指定时区时间
     *
     * @param uctTime 时间戳（毫秒）
     * @return 指定时区时间
     */
    private static Calendar getZoneToCalendar(String zoneId, Long uctTime) {
        long currentTimeMillis = uctTime;
        //
        TimeZone utcTimeZone = TimeZone.getTimeZone(zoneId);
        Calendar calendar = Calendar.getInstance(utcTimeZone);
        //设置时间
        calendar.setTimeInMillis(currentTimeMillis);
        return calendar;
    }

    /**
     * 读取日历
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    private static String redCalendar(Calendar calendar) {
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
