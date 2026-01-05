package com.library.baseui.utile.other;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * @ClassName: StringUtils
 */
public class StringUtile {
    /**
     * 去调换行符
     *
     * @param str
     * @return
     */
    public static String setDelLine(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        str = str.replace("<p>", "");
        str = str.replace("</p>", "");
        str = str.replace("<br>", "");
        str = str.replace("\n", "");
        return str;
    }
    /**
     * 验证手机号的合法性
     *
     * @param phone 手机号
     * @return
     */
    public static boolean isPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        // "[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3456789]\\d{9}";
        return phone.matches(telRegex);
    }

    /**
     * 验证邮箱的合法性
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return email.matches(strPattern);
        }
    }

    //选择字体的 开始索引到结束索引，设置颜色（颜色#）
    public static Spanned getHtmlOptionColor(String color, String txt, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(txt)) {
            return null;
        }
        int length = txt.length();
        if ((length - 1) < startIndex || (length - 1) < endIndex) {
            return Html.fromHtml(txt);
        }
        String strt1 = "";
        String strt3 = "";
        if (startIndex > 0) {
            strt1 = txt.substring(0, startIndex);
        }
        String strt2 = txt.substring(startIndex, endIndex + 1);
        if ((length - 1) > endIndex) {
            strt3 = txt.substring(endIndex + 1, length);
        }
        String html = "<html><body>";
        if (!TextUtils
                .isEmpty(strt1)) {
            html += strt1;
        }
        html += "<font color=\"" + color + "\">" + strt2 + "</font>";
        if (!TextUtils.isEmpty(strt3)) {
            html += strt3;
        }
        html += "</body></html>";
        Spanned spanned = Html.fromHtml(html);
        return spanned;
    }

    //n种颜色的字体 颜色加#
    public static Spanned getHtmlColor(String[] colors, String[] txts) {
        String html = "<html><body>";
        for (int i = 0; i < colors.length; i++) {
            html += "<font color=\"" + colors[i] + "\">" + txts[i] + "</font>";
        }
        html += "</body></html>";
        Spanned spanned = Html.fromHtml(html);
        return spanned;
    }

    //normals： 正常字的字符  bigs：要放大的字符
    public static Spanned getHtmlBig(String[] normals, String[] bigs) {
        return getHtml(normals, bigs, true);
    }

    //normals： 正常字的字符  bigs：要放大的字符 colors:放大字的颜色 颜色加#(#999999)
    public static Spanned getHtmlBig(String[] normals, String[] bigs, String[] colors) {
        return getHtml(normals, bigs, colors, false);
    }

    //normals： 正常字的字符  smalls：要缩小的字符
    public static Spanned getHtmlSmall(String[] normals, String[] smalls) {
        return getHtml(normals, smalls, false);
    }

    //normals： 正常字的字符  smalls：要缩小的字符 colors:缩小字的颜色 颜色加#(#999999)
    public static Spanned getHtmlSmall(String[] normals, String[] smalls, String[] colors) {
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

    /**
     * 价格加上"￥"
     *
     * @param obj
     * @return
     */
    public static String getPrice(Object obj) {
        if (obj == null) {
            obj = "0";
        }
        return "¥" + obj;
    }

    /***
     *138****8888
     * @param hint 要处理的字符串
     * @param hintStartCount 开始显示数
     * @param hintEndCount 结束显示数
     * @return
     */
    public static String getHint(String hint, int hintStartCount, int hintEndCount) {
        if (TextUtils.isEmpty(hint)) {
            return "";
        }
        int length = hint.length();
        int hintCount = hintStartCount + hintEndCount;
        if ((hintEndCount + hintStartCount) >= length) {
            return hint;
        }
        String a = hint.substring(0, hintStartCount);
        String c = hint.substring(length - hintEndCount, length);
        String b = "";
        for (int i = 0; i < length - hintCount; i++) {
            b += "*";
        }
        return a + b + c;
    }
}
