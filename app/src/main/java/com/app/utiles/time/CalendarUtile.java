package com.app.utiles.time;

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
    public static int getDayOfWeekNumber(Date time) {
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
    public static String getDayOfWeek(Date time) {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(time);
        int weekDate = lastDate.get(Calendar.DAY_OF_WEEK);
        String week = "";
        // 1=星期日 7=星期六，其他类推
        switch (weekDate) {
            case 2:
                week = "一";
                break;
            case 3:
                week = "二";
                break;
            case 4:
                week = "三";
                break;
            case 5:
                week = "四";
                break;
            case 6:
                week = "五";
                break;
            case 7:
                week = "六";
                break;
            case 1:
                week = "日";
                break;

        }
        return week;
    }

    /**
     * 获取某月份的最后一天
     */
    public int getMonthEndDay(int year, int month) {
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
     * @param forDate 开始时间
     * @param toDate  结束时间
     * @return 时间差  int[]{年 ，月 ，日}
     */
    public static int[] getDateDiff(Date forDate, Date toDate) {
        int[] dates = new int[3];
        dates[0] = getYearDiff(forDate, toDate);
        dates[1] = getMonthDiff(forDate, toDate);
        dates[2] = getDayDiff(forDate, toDate);

        return dates;
    }

    // 2个日期相差多少年  只比年
    private static int getYearDiff(Date forDate, Date toDate) {
        Calendar forCalendar = Calendar.getInstance();
        forCalendar.setTime(forDate);
        int forYear = forCalendar.get(Calendar.YEAR);
        int forMonth = forCalendar.get(Calendar.MONTH);
        int forDay = forCalendar.get(Calendar.DATE);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(toDate);
        int toYear = toCalendar.get(Calendar.YEAR);
        int toMonth = toCalendar.get(Calendar.MONTH);
        int toDay = toCalendar.get(Calendar.DATE);
        //
        int result = toYear - forYear;
        if (result == 0) {
            return result;
        }
        if (result > 0 && toMonth < forMonth) {
            result--;
        }
        if (result > 0 && toMonth == forMonth && toDay < forDay) {
            result--;
        }
        if (result < 0 && toMonth > forMonth) {
            result++;
        }
        if (result < 0 && toMonth == forMonth && toDay > forDay) {
            result++;
        }
        return result;
    }

    // 2个日期相差多少月 只比月
    private static int getMonthDiff(Date forDate, Date toDate) {
        Calendar forCalendar = Calendar.getInstance();
        forCalendar.setTime(forDate);
        int forMonth = forCalendar.get(Calendar.MONTH);
        int forDay = forCalendar.get(Calendar.DATE);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(toDate);
        int toMonth = toCalendar.get(Calendar.MONTH);
        int toDay = toCalendar.get(Calendar.DATE);
        //
        int result = toMonth - forMonth;
        if (result == 0) {
            return result;
        }
        if (result > 0 && toDay < forDay) {
            result--;
        }

        if (result < 0 && toDay > forDay) {
            result++;
        }
        return result;
    }

    // 2个日期相差多少月 只比天
    private static int getDayDiff(Date forDate, Date toDate) {
        Calendar forCalendar = Calendar.getInstance();
        forCalendar.setTime(forDate);
        int forDay = forCalendar.get(Calendar.DATE);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(toDate);
        int toDay = toCalendar.get(Calendar.DATE);
        //
        int result = toDay - forDay;
        return result;
    }

    /**
     * 负数 =未来时间
     *
     * @param forDate 开始时间
     * @param toDate  结束时间
     * @return int[]{ 时, 分, 秒};
     */
    public static int[] getHMSDiff(Date forDate, Date toDate) {
        long forTime = forDate.getTime();
        long toTime = toDate.getTime();
        long time = toTime - forTime;
        long H = time / (1000 * 60 * 60);
        if (H > 24 || H < -24) {
            //超过一天
            return null;
        }
        long M = (time - (H * 60 * 60 * 1000)) / (1000 * 60);
        long S = (time - (H * 60 * 60 * 100 + M * 60 * 1000)) / 1000;
        return new int[]{(int) H, (int) M, (int) S};
    }

    /**
     * @param times times[]{开始时，开始分，结束时，结束分}
     * @return
     */
    public static boolean getIsHMS(int[] times) {
        if (times == null || times.length != 4) {
            return false;
        }
        int startH = times[0];
        int startM = times[1];
        int endH = times[2];
        int endM = times[3];
        if (endH < startH) {
            //时间设置不正确
            return false;
        }
        if (endH == startH && endM < startM) {
            //时间设置不正确
            return false;
        }
        Date nowDate = new Date();
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(nowDate);
        int nowH = nowCalendar.get(Calendar.HOUR_OF_DAY);
        int nowM = nowCalendar.get(Calendar.MINUTE);
        if (nowH < endH && nowH > startH) {
            return true;
        }
        if (nowH == startH && nowM > startM) {
            return true;
        }
        return nowH == endH && nowM < endM;
    }

    /**
     * 获取两个日期之间的间隔天数
     * 忽略时分秒
     *
     * @return
     */
    public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    /***
     * 获取未来时间
     *
     * @param skip
     */
    public static Date getTomorrowTime(int skip) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +skip);
        Date time = calendar.getTime();
        return time;
    }
}
