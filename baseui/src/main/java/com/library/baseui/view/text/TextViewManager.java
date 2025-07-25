package com.library.baseui.view.text;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.TextView;

import static android.R.attr.lines;
import static android.R.attr.value;

/**
 * Created by Admin on 2016/8/15.
 */
public class TextViewManager {

    /**
     * @param tv
     * @param iconId
     * @param text
     * @param iconLocation 位置
     */
    public static void setText(Context contex, TextView tv, int iconId, String text, int iconLocation) {
        tv.setText(text);
        setText(contex, tv, iconId, iconLocation);
    }

    public static void setCompoundNull(Context context, TextView tv) {
        setText(context, tv, 0, "", 0);
    }

    public static void setText(Context contex, TextView tv, int iconId, int iconLocation) {
        if (iconId == 0) {
            tv.setCompoundDrawables(null, null, null, null);
            return;
        }
        Drawable drawable = contex.getResources().getDrawable(iconId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        switch (iconLocation) {
            case 0:
                tv.setCompoundDrawables(drawable, null, null, null);
                break;
            case 1:
                tv.setCompoundDrawables(null, drawable, null, null);
                break;
            case 2:
                tv.setCompoundDrawables(null, null, drawable, null);
                break;
            case 3:
                tv.setCompoundDrawables(null, null, null, drawable);
                break;
        }
    }


    //获取文字宽度
    public static int getTextWidth(Paint paint, String str) {
        int w = 0;
        if (TextUtils.isEmpty(str)) {
            return w;
        }
        int len = str.length();
        float[] widths = new float[len];
        paint.getTextWidths(str, widths);
        for (int j = 0; j < len; j++) {
            w += (int) Math.ceil(widths[j]);

        }
        return w;
    }

    //截取适合文字宽度的字符串
    public static String getSuitableText(int maxWidth, int lines, Paint paint, String str) {
        return getSuitableText(maxWidth, lines, 0, 0,paint, str);
    }

    //截取适合文字宽度的字符串
    public static String getSuitableText(int maxWidth, int lines, int marginStart, int marginEnd,
                                         Paint paint, String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        int line = lines;
        //要换行的索引
        int[] lineLndexs = new int[lines];
        //
        int t = getTextWidth(paint, "....");
        int endWidthCut = maxWidth - t;
        //
        boolean isCut = false;
        int index = 0;
        int indexCount = 0;
        int w = 0;
        int len = str.length();
        float[] widths = new float[len];
        paint.getTextWidths(str, widths);
        for (int z = 0; z < len; z++) {
            int Width = 0;
            if (line > 1) {
                Width = maxWidth - ((line == lines) ? marginStart : 0);
            }
            if (line == 1) {
                Width = endWidthCut - marginEnd;
            }
            int temp = (int) Math.ceil(widths[z]);
            w += temp;
            if (w == Width) {
                line -= 1;
                w = 0;
                if (line == 0) {
                    index = z;
                    isCut = z < (len - 1);
                    break;
                }
                continue;
            }
            if (w > Width) {
                line -= 1;
                w = 0;
                z -= 1;
                //String value = str.substring(0, (z + 1));
                lineLndexs[indexCount] = z;
                if (line == 0) {
                    index = z;
                    isCut = z < (len - 1);
                    lineLndexs[indexCount] = 0;
                    break;
                }
                indexCount += 1;
                continue;
            }
        }
        String value = str;
        if (isCut) {
            value = str.substring(0, (index + 1));
            int valueLength = value.length();
            int valueIndex = 0;
            for (int i = 0; i < (lineLndexs.length); i++) {
                int j = lineLndexs[i];
                if (j == 0) {
                    continue;
                }
                if (valueIndex == 0) {
                    value = "";
                }
                value += str.substring(valueIndex, (j + 1)) + "\n";
                valueIndex = j + 1;
            }
            if (valueIndex < (valueLength - 1)) {
                value += str.substring(valueIndex, valueLength);
            }
            value += "...";
        }
        return value;

    }
}
