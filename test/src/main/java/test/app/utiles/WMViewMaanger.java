package test.app.utiles;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;

/**
 * WindowManager 窗口
 */
public class WMViewMaanger {
    private static WMViewMaanger viewManager;

    public static WMViewMaanger getInstance() {
        if (viewManager == null) {
            viewManager = new WMViewMaanger();
        }
        return viewManager;
    }

    public WindowManager getWindowManager(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        return manager;
    }

    public WindowManager.LayoutParams getWindowManagerParams() {
        //需要申请授权 或者 在app信息里 手动授权
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.UNKNOWN);
        //  TYPE_APPLICATION_OVERLAY  所有应用都显示：需要授权 或者在应用信息里手动授权
        //  TYPE_APPLICATION  只显示在本应用上，不需要授权
        //params.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        //params.alpha = 0.8f;
        //初始化后不首先获得窗口焦点。不妨碍设备上其他部件的点击、触摸事件。
        //params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置悬浮窗的初始位置
        params.gravity = Gravity.BOTTOM;
        //加了它 窗口无法被点击
        //params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        // params.x = 600;
        // params.y = 600;
        return params;
    }

    //屏幕右侧 移进到 0
    public void animateIn(View view) {
        TranslateAnimation slideAnimation = new TranslateAnimation(view.getWidth(), 0f, 0f, 0f);
        slideAnimation.setDuration(500);  // 动画时长500ms
        slideAnimation.setFillAfter(true); // 动画结束后保持最终状态
        view.startAnimation(slideAnimation);
    }

    //0 移除到 屏幕左侧
    public void animateOut(View view) {
        TranslateAnimation slideAnimation = new TranslateAnimation(0f, -view.getWidth(), 0f, 0f);
        slideAnimation.setDuration(500);  // 动画时长500ms
        slideAnimation.setFillAfter(true); // 动画结束后保持最终状态
        view.startAnimation(slideAnimation);
    }
}
