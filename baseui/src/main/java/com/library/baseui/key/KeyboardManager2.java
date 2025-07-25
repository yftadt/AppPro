package com.library.baseui.key;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by Administrator on 2016/5/25.
 */
public class KeyboardManager2 extends PopupWindow {

    private View popupView;
    private int heightMax;

    private OnMultiWindowMode onMultiWindowMode;

    public KeyboardManager2(Activity activity) {
        super(activity);
        // 基础配置
        popupView = new View(activity);
        setContentView(popupView);

        // 监听全局Layout变化
        popupView.getViewTreeObserver().
                addOnGlobalLayoutListener(new GlobalLayout());
        setBackgroundDrawable(new ColorDrawable(0));
        setWidth(0);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        //设置键盘弹出方式
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //
        View view = activity.getWindow().getDecorView();
        view.getViewTreeObserver().
                addOnGlobalLayoutListener(new RootLayout(view));

    }

    public void setOnMultiWindowMode(OnMultiWindowMode onMultiWindowMode) {
        this.onMultiWindowMode = onMultiWindowMode;
    }

    private KeyboardListener listener;

    //设置监听
    public void setKeyboardListener(KeyboardListener listener) {
        this.listener = listener;
    }

    class RootLayout implements ViewTreeObserver.OnGlobalLayoutListener {
        private View rootView;

        private RootLayout(View rootView) {
            this.rootView = rootView;
        }

        @Override
        public void onGlobalLayout() {
            int height = rootView.getHeight();
            if (height <= 0) {
                return;
            }
            if (isShowing()) {
                return;
            }
            showAtLocation(rootView, Gravity.NO_GRAVITY, 0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        }
    }

    class GlobalLayout implements ViewTreeObserver.OnGlobalLayoutListener {
        //上一次打开的高度
        private int upBoardHeight;
        //上一次打开的状态
        private int upKayState;

        @Override
        public void onGlobalLayout() {
            boolean isMult = false;
            if (onMultiWindowMode != null) {
                isMult = onMultiWindowMode.isInMultiWindowMode();
            }
            if (!isMult) {
                onLayout();
                return;
            }
            //分屏模式
            heightMax = 0;
            upBoardHeight = 0;
            upKayState = 0;
            if (listener == null) {
                return;
            }
            listener.onVisibilityChanged(false, 0, 3);

        }

        private void onLayout() {
            Rect rect = new Rect();
            popupView.getWindowVisibleDisplayFrame(rect);
            if (heightMax == 0) {
                heightMax = rect.bottom;
            }
            int keyboardHeight = heightMax - rect.bottom;
            if (keyboardHeight == 0 && upBoardHeight == 0) {
                //初始化
                return;
            }
            int keyState = 0;
            if (keyboardHeight == 0) {
                //关闭键盘
                keyState = 0;
            }
            if (keyboardHeight > 0 && upBoardHeight == 0) {
                //打开键盘
                keyState = 1;
            }
            if (keyboardHeight > 0 && upBoardHeight > 0) {
                //更新键盘
                keyState = 2;
            }
            if (keyState == upKayState) {
                return;
            }
            if (listener == null) {
                upBoardHeight = keyboardHeight;
                upKayState = keyState;
                return;
            }
            listener.onVisibilityChanged((keyState != 0), keyboardHeight, keyState);
            onAnimator(upBoardHeight, keyboardHeight);
            upBoardHeight = keyboardHeight;
            upKayState = keyState;
        }
    }

    private ValueAnimator valueAnimatorSmall;
    private UpdateListener updateListener;

    private void onAnimator(int start, int keyboardHeight) {
        if (start == keyboardHeight) {
            return;
        }
        //DLog.e("动画--start：" + start + " keyboardHeight:" + keyboardHeight);
        if (valueAnimatorSmall == null) {
            valueAnimatorSmall = ValueAnimator.ofInt();
            updateListener = new UpdateListener();
            valueAnimatorSmall.addUpdateListener(updateListener);

        }
        if (valueAnimatorSmall.isRunning()) {
            valueAnimatorSmall.cancel();
        }
        updateListener.setEnd(keyboardHeight);
        valueAnimatorSmall.setFloatValues(start, keyboardHeight);
        valueAnimatorSmall.setDuration(200);

        valueAnimatorSmall.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimatorSmall.start();
    }

    class UpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private int end;

        private void setEnd(int end) {
            this.end = end;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float move = (Float) animation.getAnimatedValue();
            listener.onMove(move, end);
        }

    }

    public interface OnMultiWindowMode {
        //true 是分屏模式
        boolean isInMultiWindowMode();
    }
}
