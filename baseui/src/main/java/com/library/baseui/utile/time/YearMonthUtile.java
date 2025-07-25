package com.library.baseui.utile.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

import sj.mblog.Logx;

/**
 * Created by Administrator on 2016/2/27.
 */
public class YearMonthUtile {
    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getNowYear() {
        YearMonth yearMonth = YearMonth.now();
        int year = yearMonth.getYear();
        return year;
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getNowMonth() {
        YearMonth yearMonth = YearMonth.now();
        int month = yearMonth.getMonthValue();
        return month;
    }

    /**
     * 获取今天是几号
     *
     * @return
     */
    public static int getNowDay() {
        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();
        return dayOfMonth;
    }

    /**
     * 获取指定年月的月 有多少天
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDayLength(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        return daysInMonth;
    }

    /**
     * 获取指定年月的月 的第一天 是星期几
     *
     * @return
     */

    public static int getMonthDayFirstWeek(int year, int month) {
        return getDayToWeek(year, month, 1);
    }

    /**
     * 获取指定的年月日是星期几
     *
     * @param year
     * @param month
     * @param day
     * @return 返回（1到7  1是星期1）
     */
    public static int getDayToWeek(int year, int month, int day) {
        LocalDate localDate = getDay(year, month, day);
        int week = localDate.getDayOfWeek().getValue();
        return week;
    }

    /**
     * 获取指定年月日 转化成 Date
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getDayToDate(int year, int month, int day) {
        LocalDate localDate = getDay(year, month, day);
        Instant temp = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        //将LocalDate转换为java.util.Date
        Date date = Date.from(temp);
        return date;
    }

    /**
     * 获取指定年月的月 的第几天（从1开始）
     *
     * @return
     */
    public static LocalDate getDay(int year, int month, int day) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate localDate = yearMonth.atDay(day);
        return localDate;
    }

    public static void logo() {
        String tag = "日期";
        Logx.d(tag, "今年是：" + getNowYear());
        Logx.d(tag, "当月是：" + getNowMonth() + "月 共：" + getMonthDayLength(getNowYear(), getNowMonth()) + "天");
        Logx.d(tag, "当天是：" + getNowDay()+"号");
        Logx.d(tag, "星期：" + getDayToWeek(getNowYear(), getNowMonth(), getNowDay()));
    }
}
