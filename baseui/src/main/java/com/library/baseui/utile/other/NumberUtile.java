package com.library.baseui.utile.other;

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
    public static long getStringToLong(String number) {
        return getStringToLong(number, 0);
    }

    /**
     * String转long
     */
    public static long getStringToLong(String number, long defaultInt) {
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
    public static float getStringToFloat(String number) {
        return getStringToFloat(number, 0);
    }

    /**
     * String转Float
     */
    public static float getStringToFloat(String number, float def) {
        if (TextUtils.isEmpty(number)) {
            return def;
        }
        try {
            return Float.parseFloat(number);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * String转double
     */
    public static double getStringToDouble(String number) {
        return getStringToDouble(number, -1);
    }

    /**
     * String转String转double
     */
    public static double getStringToDouble(String number, double defaultInt) {
        if (TextUtils.isEmpty(number)) {
            return defaultInt;
        }
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {
            return defaultInt;
        }
    }


    //浮点计算 加法
    public static double decimalsAdd(double s1, double s2) {
        BigDecimal d1 = new BigDecimal(String.valueOf(s1));
        BigDecimal d2 = new BigDecimal(String.valueOf(s2));
        BigDecimal d = d1.add(d2);
        double value = d.doubleValue();
        return value;
    }

    //浮点计算 减法
    public static double decimalsSub(double s1, double s2) {
        BigDecimal d1 = new BigDecimal(String.valueOf(s1));
        BigDecimal d2 = new BigDecimal(String.valueOf(s2));
        BigDecimal d = d1.subtract(d2);
        double value = d.doubleValue();
        return value;
    }

    //浮点计算 除法
    public static double decimalsDivision(double s1, double s2 ) {
        return decimalsDivision(s1,s2,0);
    }
    public static double decimalsDivision(double s1, double s2, int type) {
        BigDecimal d1 = new BigDecimal(String.valueOf(s1));
        BigDecimal d2 = new BigDecimal(String.valueOf(s2));
        if (type == 0) {
            type = BigDecimal.ROUND_HALF_UP;
        }
        BigDecimal d = d1.divide(d2, type);
        double value = d.doubleValue();
        return value;
    }

    //浮点计算 乘法
    public static double decimalsX(double s1, double s2,int type) {
        BigDecimal d1 = new BigDecimal(String.valueOf(s1));
        BigDecimal d2 = new BigDecimal(String.valueOf(s2));
        BigDecimal d = d1.multiply(d2);
        double value = d.doubleValue();
        return value;
    }

    public static double decimalsNumerDelt0(double bumber) {
        BigDecimal d1 = new BigDecimal(String.valueOf(bumber));
        d1 = d1.stripTrailingZeros();
        double value = d1.doubleValue();
        return value;
    }

    public static double decimalsNumerDelt0(String bumber) {
        BigDecimal d1 = new BigDecimal(bumber);
        d1 = d1.stripTrailingZeros();
        double value = d1.doubleValue();
        return value;
    }

    //Double转String 不以科学计数法显示
    public static String getDoubleToString(double number) {
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(number));
        return bigDecimal.toPlainString();
    }

    //保留几位小数 d2：10 表示一位 isRound true:四舍五入无
    public static String decimalsLength(String d1, int d2, boolean isRound) {
        if (d2 % 10 != 0 || d2 < 10) {
            return "0";
        }
        if (TextUtils.isEmpty(d1)) {
            return "0";
        }
        boolean isDecimals = d1.contains("-");
        d1 = d1.replace("-", "");
        String number = d1;
        if (isRound) {
            double r = ((double) 5) / (d2 * 10);
            number = decimalsAdd(d1, String.valueOf(r));
        }
        String[] s1 = number.split("\\.");
        String n = s1[0];
        if (TextUtils.isEmpty(n)) {
            return "0";
        }
        String xf = "";
        if (s1.length > 1) {
            xf = s1[1];
        }
        if (TextUtils.isEmpty(xf)) {
            return n;
        }
        String temp = "";
        for (int i = 0; i < String.valueOf(d2).length() - 1; i++) {
            if (i >= xf.length()) {
                break;
            }
            temp += xf.charAt(i);
        }
        //去掉尾部的0
        temp = decimalsDelt0(temp);
        if (!TextUtils.isEmpty(temp)) {
            temp = "." + temp;
        }
        String f = isDecimals ? "-" : "";
        return f + n + temp;

    }

    /**
     * 加法
     * 用 double的数值 转string 可能会出现科学计算法，会导致计算错误
     *
     * @param d1
     * @param d2
     * @return
     */
    public static String decimalsAdd(String d1, String d2) {
        long[] numbers = getNumber(d1, d2);
        long multiple = numbers[4];
        if (multiple == 0) {
            multiple = 1;
        }
        long n1 = numbers[0] * multiple + numbers[1];
        long n2 = numbers[2] * multiple + numbers[3];
        long n = n1 + n2;
        if (n == 0) {
            return "0";
        }
        //true 负数
        boolean isDecimals = n < 0;
        String d = getNumberMerge(n, numbers[4]);
        if (isDecimals) {
            return "-" + d;
        }
        return d;

    }

    /**
     * 减法
     * 用 double的数值 转string 可能会出现科学计算法，会导致计算错误
     *
     * @param d1
     * @param d2
     * @return
     */
    public static String decimalsSub(String d1, String d2) {
        long[] numbers = getNumber(d1, d2);
        long multiple = numbers[4];
        if (multiple == 0) {
            multiple = 1;
        }
        long n1 = numbers[0] * multiple + numbers[1];
        long n2 = numbers[2] * multiple + numbers[3];
        long n = n1 - n2;
        if (n == 0) {
            return "0";
        }
        //true 负数
        boolean isDecimals = n < 0;
        String d = getNumberMerge(n, numbers[4]);
        if (isDecimals) {
            return "-" + d;
        }
        return d;
    }

    /**
     * 乘法
     * 用 double的数值 转string 可能会出现科学计算法，会导致计算错误
     *
     * @param d1
     * @param d2
     * @return
     */
    public static String decimalsX(String d1, String d2) {
        long[] numbers = getNumber(d1, d2);
        long multiple = numbers[4];
        if (multiple == 0) {
            multiple = 1;
        }
        long n1 = numbers[0] * multiple + numbers[1];
        long n2 = numbers[2] * multiple + numbers[3];
        long n = n1 * n2;
        e("乘法：" + n1 + "*" + n2 + "=" + n + " multiple=" + multiple);
        if (n == 0) {
            return "0";
        }
        //true 负数
        boolean isDecimals = n < 0;
        String d = getNumberMerge(n, multiple * multiple);
        if (isDecimals) {
            return "-" + d;
        }
        return d;

    }


    /**
     * 除法（最多6位小数）
     * 用 double的数值 转string 可能会出现科学计算法，会导致计算错误
     *
     * @param d1
     * @param d2
     * @return 值
     */
    public static String decimalsDivision(String d1, String d2) {
        long[] numbers = getNumber(d1, d2);
        long multiple = numbers[4];
        if (multiple == 0) {
            multiple = 1;
        }
        long n1 = numbers[0] * multiple + numbers[1];
        long n2 = numbers[2] * multiple + numbers[3];
        e("要除的数_" + n1 + " / " + n2);
        if (n2 == 0) {
            return "0";
        }
        //true 负数
        boolean isDecimals = n1 < 0 && n2 > 0;
        if (!isDecimals) {
            isDecimals = n1 > 0 && n2 < 0;
        }
        n1 = Math.abs(n1);
        n2 = Math.abs(n2);
        long z = n1 / n2;
        long remainder = n1 % n2;
        if (remainder == 0) {
            String d = "";
            if (isDecimals && z > 0) {
                d = "-";
            }
            return d + z;
        }
        String decimals = "";
        //最多6次循环(最多6 位小数)
        int maxLength = 6;
        while (maxLength > 0) {
            maxLength -= 1;
            long newN1 = remainder * 10;
            long d = newN1 / n2;
            decimals += d;
            remainder = newN1 % n2;
            if (remainder == 0) {
                break;
            }
        }
        decimals = decimalsDelt0(decimals);
        String value = String.valueOf(z);
        if (!TextUtils.isEmpty(decimals)) {
            value += "." + decimals;
        }
        if (isDecimals && (z > 0 || !TextUtils.isEmpty(decimals))) {
            value = "-" + value;
        }
        return value;

    }


    //整数和小数合并
    private static String getNumberMerge(long xF, long multiple) {
        if (xF == 0) {
            return "0";
        }
        xF = Math.abs(xF);
        //小数提取的整数
        long z1;
        String xT = "";
        if (multiple > 0) {
            z1 = xF / multiple;
            xT = getNumber(xF, multiple);
        } else {
            z1 = xF;
        }
        return z1 + xT;

    }

    //处理为小数
    private static String getNumber(long number, long multiple) {
        long temp = number % multiple;
        //e("小数：" + number + "%" + multiple + "=" + temp);
        int ml = String.valueOf(multiple).length();
        int xNl = String.valueOf(temp).length();
        //补0
        String placeHolder = "";
        for (int i = (xNl + 1); i < ml; i++) {
            placeHolder += "0";
        }
        //小数
        String xT = placeHolder + temp;
        xT = decimalsDelt0(xT);
        //e("小数：" + xT);
        if (xT.length() > 0) {
            xT = "." + xT;
        }
        return xT;
    }

    //小数去掉尾部无用的0
    public static String decimalsDelt0(String decimals) {
        if (TextUtils.isEmpty(decimals)) {
            return "";
        }
        while (decimals.length() > 0) {
            int index = decimals.length() - 1;
            char c = decimals.charAt(index);
            if (c == '0') {
                decimals = decimals.substring(0, index);
                continue;
            }
            break;
        }
        return decimals;
    }

    //数去掉尾部无用的0
    public static String numberDelt0(String number) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        if (!number.contains(".")) {
            return number;
        }
        if (number.startsWith(".")) {
            number = "0" + number;
        }
        if (number.startsWith("-.")) {
            number = number.replace("-.", "-0.");
        }
        String[] numbers = number.split("\\.");
        String n1 = numbers[0];
        String n2 = "";
        if (numbers.length > 1) {
            n2 = numbers[1];
        }
        n2 = decimalsDelt0(n2);
        if (!TextUtils.isEmpty(n2)) {
            n2 = "." + n2;
        }
        return n1 + n2;
    }

    //小数化为整数
    private static long[] getNumber(String temp1, String temp2) {
        if (TextUtils.isEmpty(temp1) || TextUtils.isEmpty(temp2)) {
            return new long[]{0, 0, 0, 0, 0};
        }
        if (temp1.startsWith(".")) {
            temp1 = "0" + temp1;
        }
        if (temp1.startsWith("-.")) {
            temp1 = temp1.replace("-.", "-0.");
        }
        if (temp2.startsWith(".")) {
            temp2 = "0" + temp2;
        }
        if (temp2.startsWith("-.")) {
            temp2 = temp2.replace("-.", "-0.");
        }
        e("转换：temp1=" + temp1 + " temp2=" + temp2);
        String[] s1 = temp1.split("\\.");
        long a1 = 0;
        String a2 = "";
        if (s1.length > 0) {
            a1 = getNumber(s1[0]);
        }

        if (s1.length > 1) {
            a2 = s1[1];
        }
        e("转换：a1=" + a1 + " a2=" + a2);
        String[] s2 = temp2.split("\\.");
        long b1 = 0;
        String b2 = "";
        if (s2.length > 0) {
            b1 = getNumber(s2[0]);
        }
        if (s2.length > 1) {
            b2 = s2[1];
        }
        e("转换：b1=" + b1 + " b2=" + b2);
        //小数成整数的倍数
        long xC = 0;
        //
        int a2Length = a2.length();
        int b2Length = b2.length();
        if (a2Length > b2Length) {
            for (int i = b2Length; i < a2Length; i++) {
                b2 += "0";
                if (xC == 0) {
                    xC = 10;
                    continue;
                }
                xC *= 10;
            }
        } else if (b2Length > a2Length) {
            for (int i = a2Length; i < b2Length; i++) {
                a2 += "0";
                if (xC == 0) {
                    xC = 10;
                    continue;
                }
                xC *= 10;
            }
        }
        long a3 = getNumber(a2);
        if (temp1.startsWith("-")) {
            a3 = -a3;
        }
        long b3 = getNumber(b2);
        if (temp2.startsWith("-")) {
            b3 = -b3;
        }
        e("转换：numbers_ a1=" + a1 + "_a3=" + a3 + "_b1="
                + b1 + "_b3=" + b3 + "_xC=" + xC);
        //整数1 小数1 整数2 小数2 小数倍数
        return new long[]{a1, a3, b1, b3, xC};
    }

    private static long getNumber(String num) {
        return getStringToLong(num, 0);
    }

    private static void e(String msg) {
        BaseUILog.e(msg);
    }
}
