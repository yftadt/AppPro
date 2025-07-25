package com.library.baseui.utile.time;

/**
 * Created by 郭敏 on 2017/11/21 0021.
 */

public interface OnPickerDialogListener {
    /**
     * @param type 标识
     * @param y    年
     * @param m    月
     * @param d    日
     */
    void onPickerDate(int type, int y, int m, int d);

    /**
     * @param type 标识
     * @param h    小时
     * @param m    分钟
     */
    void onPickerTime(int type, int h, int m);


}
