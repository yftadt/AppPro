package test.app.ui.view.floating;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.library.baseui.utile.app.APKInfo;
import com.library.baseui.utile.other.StatusBarUtile;

import sj.mblog.Logx;
import test.app.ui.activity.R;

//悬浮窗（全APP/桌面）
public class DraggableFloatWindow {
    private static DraggableFloatWindow instance;
    private WindowManager windowManager;
    private View floatView;
    private Context context;
    private boolean isAdded = false;
    private int screenWidth, screenHeight;
    private int statusViewHeight;//状态栏高度
    private int navigationViewHeight;//导航条高度
    private int viewWidth = 300;
    private int viewHeight = 150;

    // 拖拽相关
    private int initialX, initialY;
    private float downX, downY;
    private int initX, initY;

    public static synchronized DraggableFloatWindow getInstance(Context context) {
        if (instance == null) {
            instance = new DraggableFloatWindow(context);
        }
        return instance;
    }

    private DraggableFloatWindow(Context context) {
        this.context = context.getApplicationContext();
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        statusViewHeight = APKInfo.getInstance().getStatusBarHeight();
        navigationViewHeight = StatusBarUtile.getNavigationBarHeight(context);
        viewWidth = context.getResources().getDimensionPixelSize(com.library.dimen.R.dimen.dp_300);
        viewHeight = context.getResources().getDimensionPixelSize(com.library.dimen.R.dimen.dp_150);
        if (initX == 0) {
            initX = (int) (APKInfo.getInstance().getScreenWidthPixels() - viewWidth);

        }
        if (initY == 0) {
            initY = (int) (APKInfo.getInstance().getScreenWidthPixels() - viewHeight) / 2;
        }
        Logx.d("宽高1：" + viewWidth + "_" + viewHeight);
        initScreenSize();
    }


    private void initScreenSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(metrics); // 获取真实屏幕尺寸（含状态栏）
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }

    public void showFloatWindow() {
        if (!checkPermission()) {
            Logx.d("请先开启悬浮窗权限");
            return;
        }
        //已添加悬浮窗
        if (isAdded) return;
        initFloatView();
        try {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_PHONE;
            }

            params.format = PixelFormat.TRANSLUCENT;
            params.gravity = Gravity.TOP | Gravity.START;
          /*  params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | // 允许超出屏幕（但我们自己做边界限制）
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL; // 触摸*/
            //  这是全局悬浮窗“能触摸”的标准标配 flags
            params.flags =
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
            // 初始位置：右上角
            params.x = initX;
            params.y = initY;
            params.width = viewWidth;
            params.height = viewHeight;
            windowManager.addView(floatView, params);
            isAdded = true;
        } catch (Exception e) {
            Logx.d("显示悬浮窗失败:" + e.getMessage());
        }
    }

    //初始化视图
    private void initFloatView() {
        if (floatView == null) {
            floatView = LayoutInflater.from(context).inflate(R.layout.float_window_draggable, null);

            TextView btn = floatView.findViewById(R.id.btn_float);
            /*btn.setOnClickListener(v -> {
                Toast.makeText(context, "悬浮窗被点击！", Toast.LENGTH_SHORT).show();
            });*/
            floatView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewWidth = floatView.getWidth();
                    viewHeight = floatView.getHeight();
                    Logx.d("宽高2：" + viewWidth + "_" + viewHeight);
                }
            }, 500);
            initTouch();
        }
    }

    //初始化拖动
    private void initTouch() {
        floatView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getRawX();
                        downY = event.getRawY();
                        // 获取当前视图位置
                        WindowManager.LayoutParams params = (WindowManager.LayoutParams) floatView.getLayoutParams();
                        initialX = params.x;
                        initialY = params.y;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float moveX = event.getRawX();
                        float moveY = event.getRawY();
                        int deltaX = (int) (moveX - downX);
                        int deltaY = (int) (moveY - downY);

                        // 计算新位置
                        int newX = initialX + deltaX;
                        int newY = initialY + deltaY;

                        // 应用边界限制
                        newX = applyXBoundary(newX);
                        newY = applyYBoundary(newY);

                        // 更新视图位置
                        params = (WindowManager.LayoutParams) floatView.getLayoutParams();
                        params.x = newX;
                        params.y = newY;
                        windowManager.updateViewLayout(floatView, params);
                        break;

                    case MotionEvent.ACTION_UP:
                        // 拖拽结束时吸附到最近边缘
                        snapToEdge();
                        break;
                }
                return false;
            }
        });
        /*floatView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isDragging = false;
                        downX = event.getRawX();
                        downY = event.getRawY();
                        // 获取当前视图位置
                        WindowManager.LayoutParams params = (WindowManager.LayoutParams) floatView.getLayoutParams();
                        initialX = params.x;
                        initialY = params.y;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        isDragging = true;
                        float moveX = event.getRawX();
                        float moveY = event.getRawY();
                        int deltaX = (int) (moveX - downX);
                        int deltaY = (int) (moveY - downY);

                        // 计算新位置
                        int newX = initialX + deltaX;
                        int newY = initialY + deltaY;

                        // 应用边界限制
                        newX = applyXBoundary(newX);
                        newY = applyYBoundary(newY);

                        // 更新视图位置
                        params = (WindowManager.LayoutParams) floatView.getLayoutParams();
                        params.x = newX;
                        params.y = newY;
                        windowManager.updateViewLayout(floatView, params);
                        break;

                    case MotionEvent.ACTION_UP:
                        if (isDragging) {
                            // 拖拽结束时吸附到最近边缘
                            snapToEdge();
                        }
                        break;
                }
                // 如果是点击事件（非拖拽），不消费事件，让按钮点击生效
                return isDragging;
            }
        });*/
    }


    // X轴边界限制：确保视图完全在屏幕内
    private int applyXBoundary(int x) {
        // 左边界：x >= 0
        if (x < 0) return 0;
        // 右边界：x <= screenWidth - viewWidth
        if (x > screenWidth - viewWidth) return screenWidth - viewWidth;
        return x;
    }

    // Y轴边界限制：确保视图完全在屏幕内
    private int applyYBoundary(int y) {
        // 上边界：y >= 0
        if (y < statusViewHeight) return statusViewHeight;
        // 下边界：y <= screenHeight - viewHeight
        int temp = screenHeight - viewHeight - (navigationViewHeight);
        if (y > temp) return temp;
        return y;
    }

    // 吸附到最近的屏幕边缘
    private void snapToEdge() {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) floatView.getLayoutParams();
        int centerX = params.x + viewWidth / 2;

        if (centerX < screenWidth / 2) {
            // 吸附到左边
            params.x = 0;
        } else {
            // 吸附到右边
            params.x = screenWidth - viewWidth;
        }

        // Y轴保持原位置（也可选择吸附到顶部/底部）
        windowManager.updateViewLayout(floatView, params);
    }

    private void hideFloatWindow() {
        if (isAdded && floatView != null) {
            try {
                windowManager.removeViewImmediate(floatView);
                isAdded = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void destroy() {
        hideFloatWindow();
        floatView = null;
        instance = null;
    }

    //检查权限

    public boolean checkPermission() {
        return Settings.canDrawOverlays(context);
    }

    //申请权限
    public void requestPermission(Activity activity, int requestCode) {
        if (!checkPermission()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.getPackageName()));
            activity.startActivityForResult(intent, requestCode);
        }
    }
    //使用方法在act中
   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            if (floatWindow?.checkPermission() == true) {
                floatWindow?.showFloatWindow()
                ToastUtile.showToast("权限已开启，悬浮窗已显示")
            } else {
                ToastUtile.showToast("权限未开启，无法显示悬浮窗")
            }
        }
    }*/
   /* if (true) {
        if (floatWindow == null) {
            floatWindow = DraggableFloatWindow.getInstance(this)
        }
        if (floatWindow?.checkPermission() == true) {
            floatWindow?.showFloatWindow();
        } else {
            floatWindow?.requestPermission(this, 1001)
        }

        return@setOnClickListener
    }*/
}


