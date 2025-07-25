package test.app.ui.manager;

/**
 * Created by Administrator on 2016/5/25.
 */
public interface KeyboardListener {
    void onVisibilityChanged(boolean isOpen, int keyboardHeight, int status);
    void onMove(float move, int moveLength);
}
