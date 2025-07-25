package com.library.baseui.utile.time;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/2/27.
 */
public class CalendarUtile {
    /**
     * 数字
     * 获取某一天是周几
     */
    public static int getWeek(Date time) {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(time);
        int weekDate = lastDate.get(Calendar.DAY_OF_WEEK);
        int week = 1;
        // 1=星期日 7=星期六，其他类推
        switch (weekDate) {
            case 2:
                week = 1;
                break;
            case 3:
                week = 2;
                break;
            case 4:
                week = 3;
                break;
            case 5:
                week = 4;
                break;
            case 6:
                week = 5;
                break;
            case 7:
                week = 6;
                break;
            case 1:
                week = 7;
                break;

        }
        return week;
    }

    /**
     * 获取某一天是周几
     */
    public static String getWeekValue(Date time) {
        int weekDate = getWeek(time);
        String week = "";
        switch (weekDate) {
            case 1:
                week = "一";
                break;
            case 2:
                week = "二";
                break;
            case 3:
                week = "三";
                break;
            case 4:
                week = "四";
                break;
            case 5:
                week = "五";
                break;
            case 6:
                week = "六";
                break;
            case 7:
                week = "日";
                break;

        }
        return week;
    }

    /**
     * 获取某月份的最后一天
     */
    public static int getMonthEndDay(int year, int month) {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.YEAR, year);
        lastDate.set(Calendar.MONTH, month);
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        int day = lastDate.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 比较是 年份
     * 忽略 月，日
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public static int getCompareYear(Date startDate, Date endDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        //获取年
        int startYer = startCalendar.get(Calendar.YEAR);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        //获取年
        int endYer = endCalendar.get(Calendar.YEAR);
        return endYer - startYer;
    }

    /**
     * 获取两个日期之间的间隔天数
     * 忽略时分秒
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public static int getCompareDay(Date startDate, Date endDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        //
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);
        return (int) ((endCalendar.getTime().getTime() - startCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 获取两个日期之间的间隔小时
     * 忽略分秒
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public static int getCompareHour(Date startDate, Date endDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        //
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);
        return (int) ((endCalendar.getTime().getTime() - startCalendar.getTime().getTime()) / (1000 * 60 * 60));
    }

    /**
     * 获取两个日期之间的间隔分
     * 忽略秒
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public static int getCompareMinute(Date startDate, Date endDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        //
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);
        return (int) ((endCalendar.getTime().getTime() - startCalendar.getTime().getTime()) / (1000 * 60));
    }

    /***
     * 获取未来时间
     *
     * @param skip 和今天间隔几天
     */
    public static Date getTomorrowTime(int skip) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +skip);
        Date time = calendar.getTime();
        return time;
    }
}
