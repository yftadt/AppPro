package com.library.baseui.utile.other;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @ClassName: StringUtils
 */
public class StatusBarUtile {
    /**
     * 可用
     * 设置全屏
     */
    public static void setBarFull(Activity activity) {
        //表示5.0
        int option = (SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        activity.getWindow().getDecorView().setSystemUiVisibility(option);
    }

    /**
     * 可用
     * 设置沉浸式状态栏颜色
     * 应在setContentView之后调用
     */
    public static void setStatusBarColor(Activity activity, Boolean isTranslucent, int color, Boolean lightMode) {
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        if (isTranslucent) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            //设置状态栏背景色
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int ui = decorView.getSystemUiVisibility();
            if (lightMode) {
                //状态栏白色字体
                ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                //状态栏黑色字体
                ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(ui);
        }
    }

    //获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
