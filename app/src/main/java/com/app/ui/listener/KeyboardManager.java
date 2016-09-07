package com.app.ui.listener;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Administrator on 2016/5/25.
 */
public class KeyboardManager {
    private final static int KEYBOARD_VISIBLE_THRESHOLD_DP = 100;
    private KeyboardListener listener;
    private Activity activity;
    private boolean isAnimatio = true;

    public void setEventListener(Activity activity, boolean isAnimatio,
                                 KeyboardListener keyListener) {
        this.isAnimatio = isAnimatio;
        listener = keyListener;
        this.activity = activity;
        activityListener();
    }

    private void activityListener() {
        View activityRoot = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        activityRoot.getViewTreeObserver().
                addOnGlobalLayoutListener(new LayoutListener(activityRoot));
    }

    private int getStatusBarHeight(View activityRoot) {
        int result = 0;
        int resourceId = activityRoot.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activityRoot.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    class LayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private View activityRoot;
        private Rect r = new Rect();
        //状态栏高度
        private int status;
        private int keyboardHeight;
        private int visibleThreshold = Math.round(
                dpToPx(activity, KEYBOARD_VISIBLE_THRESHOLD_DP));

        private boolean wasOpened = false;

        public LayoutListener(View activityRoot) {
            this.activityRoot = activityRoot;
        }

        @Override
        public void onGlobalLayout() {
            activityRoot.getWindowVisibleDisplayFrame(r);
            //包含状态栏和输入盘
            int heightDiff = activityRoot.getRootView().getHeight() - r.height();
            if (status == 0) {
                status = getStatusBarHeight(activityRoot);
            }
            boolean isOpen = heightDiff > visibleThreshold;
            boolean isChange = false;
            if (keyboardHeight > 0 && heightDiff > status && keyboardHeight != heightDiff) {
                // 输入法类型可能导致输入盘高度不一样，比如三星手机
                wasOpened = false;
                isChange = true;
            }
            if (isOpen == wasOpened) {
                // keyboard state has not changed
                return;
            }
            if (isOpen) {
                keyboardHeight = heightDiff;
            }
            wasOpened = isOpen;
            if (isOpen && !isChange) {
                animator(0, keyboardHeight, keyboardHeight);
            }

            if (!isOpen && !isChange) {
                animator(keyboardHeight, 0, keyboardHeight);
            }
            listener.onVisibilityChanged(isOpen, keyboardHeight, status);
        }

    }

    private static float dpToPx(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    private ValueAnimator valueAnimatorSmall;

    private void animator(float start, float end, final int moveLength) {
        if (!isAnimatio) {
            return;
        }
        if (valueAnimatorSmall == null) {
            valueAnimatorSmall = ValueAnimator.ofInt();
        }
        valueAnimatorSmall.cancel();

        valueAnimatorSmall.setFloatValues(start, end);
        valueAnimatorSmall.setDuration(200);
        valueAnimatorSmall.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float move = (Float) animation.getAnimatedValue();
                listener.onMove(move, moveLength);
            }
        });
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

    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }
}
