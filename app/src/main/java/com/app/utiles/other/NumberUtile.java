package com.app.utiles.other;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * Created by Admin on 2016/8/17.
 */
public class NumberUtile {
    /**
     * String转int
     */
    public static int getStringToInt(String number) {
        return getStringToInt(number, -1);
    }

    /**
     * String转int
     */
    public static int getStringToInt(String number, int defaultInt) {
        if (TextUtils.isEmpty(number)) {
            return defaultInt;
        }
        try {
            return Integer.parseInt(number);
        } catch (Exception e) {
            return defaultInt;
        }
    }

    /**
     * String转long
     */
    public static long getStringToIntLong(String number) {
        return getStringToIntLong(number, 0);
    }

    /**
     * String转long
     */
    public static long getStringToIntLong(String number, long defaultInt) {
        if (TextUtils.isEmpty(number)) {
            return defaultInt;
        }
        try {
            return Long.parseLong(number);
        } catch (Exception e) {
            return defaultInt;
        }
    }


    /**
     * String转Float
     */
    public static float geStringToFloat(String number) {
        return geStringToFloat(number, 0);
    }

    /**
     * String转Float
     */
    public static float geStringToFloat(String number, float def) {
        if (TextUtils.isEmpty(number)) {
            return def;
        }
        try {
            return Float.parseFloat(number);
        } catch (Exception e) {
            return def;
        }
    }

    //string计算
    public static int count(String s1, String s2) {
        BigDecimal d1 = new BigDecimal(s1);
        BigDecimal d2 = new BigDecimal(s2);
        BigDecimal d = d1.add(d2);
        int value = d.intValue();
        return value;
    }

    //浮点计算
    public static float count(Float s1, Float s2) {
        BigDecimal d1 = new BigDecimal(s1.toString());
        BigDecimal d2 = new BigDecimal(s2.toString());
        BigDecimal d = d1.add(d2);
        float value = d.floatValue();
        return value;
    }

    public static double count(double s1, double s2) {
        BigDecimal d1 = new BigDecimal(s1);
        BigDecimal d2 = new BigDecimal(s2);
        BigDecimal d = d1.add(d2);
        double value = d.doubleValue();
        return value;
    }
}
