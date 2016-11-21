package com.app.utiles.other;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * @ClassName: StringUtils
 */
public class StringUtile {

    public static boolean isPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        // "[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3578]\\d{9}";
        return phone.matches(telRegex);

    }

    public static String getWeek(int num) {
        String week = "";
        switch (num) {
            case 1:
                week = "周日";
                break;
            case 2:
                week = "周一";
                break;
            case 3:
                week = "周二";
                break;
            case 4:
                week = "周三";
                break;
            case 5:
                week = "周四";
                break;
            case 6:
                week = "周五";
                break;
            case 7:
                week = "周六";
                break;
        }
        return week;
    }

    public static String getAmPm(int num) {
        String ampm = "";
        switch (num) {
            case 0:
                ampm = "上午";
                break;
            case 1:
                ampm = "下午";
                break;
            case 2:
                ampm = "晚上";
                break;
        }
        return ampm;
    }

    //n种颜色的字体 颜色加#
    public static Spanned getColorHtml(String[] colors, String[] txts) {
        String html = "<html><body>";
        for (int i = 0; i < colors.length; i++) {
            html += "<font color=\"" + colors[i] + "\">" + txts[i] + "</font>";
        }
        html += "</body></html>";
        Spanned spanned = Html.fromHtml(html);
        return spanned;
    }
    //normals： 正常字的字符  bigs：要放大的字符
    public static Spanned getBigHtml(String[] normals, String[] bigs) {
        return getHtml(normals, bigs, true);
    }
    //normals： 正常字的字符  bigs：要放大的字符 colors:放大字的颜色 颜色加#(#999999)
    public static Spanned getBigHtml(String[] normals, String[] bigs, String[] colors) {
        return getHtml(normals, bigs, colors, false);
    }
    //normals： 正常字的字符  smalls：要缩小的字符
    public static Spanned getSmallHtml(String[] normals, String[] smalls) {
        return getHtml(normals, smalls, false);
    }

    //normals： 正常字的字符  smalls：要缩小的字符 colors:缩小字的颜色 颜色加#(#999999)
    public static Spanned getSmallHtml(String[] normals, String[] smalls, String[] colors) {
        return getHtml(normals, smalls, colors, false);
    }

    private static Spanned getHtml(String[] normals, String[] tag, String[] colors, boolean isBig) {
        String type = isBig ? "big" : "small";
        String html = "<html><body>";
        for (int i = 0; i < normals.length; i++) {
            html += "<font>" + normals[i] + "</font>";
        }
        for (int i = 0; i < tag.length; i++) {
            html += "<" + type + "><font color=\"" + colors[i] + "\">" + tag[i] + "</font></" + type + ">";
        }
        html += "</body></html>";
        Spanned spanned = Html.fromHtml(html);
        return spanned;
    }

    private static Spanned getHtml(String[] normals, String[] tag, boolean isBig) {
        String type = isBig ? "big" : "small";
        String html = "<html><body>";
        for (int i = 0; i < normals.length; i++) {
            html += "<font>" + normals[i] + "</font>";

        }
        for (int i = 0; i < tag.length; i++) {
            html += "<" + type + "><font>" + tag[i] + "</font></" + type + ">";

        }
        html += "</body></html>";
        Spanned spanned = Html.fromHtml(html);
        return spanned;
    }
}
