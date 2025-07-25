package com.library.baseui.key;

/**
 * Created by Administrator on 2016/5/25.
 */
public interface KeyboardListener {
    /**
     * @param isOpen
     * @param keyboardHeight 键盘高度
     * @param status         0:关闭 1：开启  2：更新 3 分屏模式
     */
    void onVisibilityChanged(boolean isOpen, int keyboardHeight, int status);

    void onMove(float move, int moveLength);
}
