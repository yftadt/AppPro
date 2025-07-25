package test.app.ui.manager;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;


/**
 * Created by Administrator on 2016/5/25.
 */
public class KeyboardManager {
    //预设输入法弹起，收缩的变化门槛值
    private final static int KEYBOARD_VISIBLE_THRESHOLD_DP = 100;
    //监听
    private OnKeyboardStateListener keyboardStateListener;
    private OnKeyboardStateAnimatorListener animatorListener;
    private Activity activity;

    public void setEventListener(Activity activity) {
        this.activity = activity;
        View activityRoot = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        activityRoot.getViewTreeObserver().
                addOnGlobalLayoutListener(new LayoutListener(activityRoot));
    }

    public void setOnKeyboardStateListener(OnKeyboardStateListener keyboardStateListener) {
        this.keyboardStateListener = keyboardStateListener;
    }

    public void setOnKeyboardStateAnimatorListener(OnKeyboardStateAnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    //获取状态栏高度
    private int getStatusBarHeight(View activityRoot) {
        int result = 0;
        int resourceId = activityRoot.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activityRoot.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //监听contentView的变化
    class LayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private View activityRoot;
        private int visibleThreshold;
        //状态栏高度
        private int stateHeight;
        //视图高度
        private int viewHeight;
        //输入法高度
        private int keyboardHeight;
        private boolean isOpen;
        //rect.bottom是否包含状态栏
        private boolean isIncludeState;

        public LayoutListener(View activityRoot) {
            this.activityRoot = activityRoot;

            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, KEYBOARD_VISIBLE_THRESHOLD_DP,
                    activity.getResources().getDisplayMetrics());
            visibleThreshold = Math.round(px);
        }

        @Override
        public void onGlobalLayout() {
            Rect rect = new Rect();
            activityRoot.getWindowVisibleDisplayFrame(rect);
            int bottom = rect.bottom;
            //初始化一些值
            if (viewHeight == 0) {
                viewHeight = activityRoot.getHeight();
                stateHeight = getStatusBarHeight(activityRoot);
                isIncludeState = !(bottom == viewHeight);
            }
            if (viewHeight == 0) {
                //DLog.e("监听输入法====", "viewHeight:" + viewHeight + "初始化");
                return;
            }
            if(isIncludeState){
                bottom-=stateHeight;
            }
            // DLog.e("监听输入法====", "viewHeight:" + viewHeight +
            //       " bottom:" + bottom + " stateHeight:" + stateHeight+" isIncludeState:"+ isIncludeState);
            if (bottom >= viewHeight) {
                //输入法是关闭的
                if (!isOpen) {
                    return;
                }
                isOpen = false;
                keyboardState(false, keyboardHeight);
                return;
            }
            int keyboardHeightTemp = viewHeight - bottom;
            if (keyboardHeightTemp >= visibleThreshold) {
                if (isOpen && keyboardHeight == keyboardHeightTemp) {
                    return;
                }
                keyboardHeight = keyboardHeightTemp;
                isOpen = true;
                //输入法是可视的
                keyboardState(true, keyboardHeight);
            }
        }

    }

    private boolean keyboardState;

    public boolean getKeyboardState() {
        return keyboardState;
    }

    private void keyboardState(boolean isOpen, int keyboardHeight) {
        this.keyboardState = isOpen;
        // DLog.e("监听输入法====", "isOpen:" + isOpen + " keyboardHeight:" + keyboardHeight);
        if (animatorListener != null) {
            animator(isOpen, keyboardHeight);
        }
        if (keyboardStateListener != null) {
            keyboardStateListener.onVisibilityChanged(isOpen, keyboardHeight);
        }
    }


    private ValueAnimator valueAnimatorSmall;

    private void animator(final boolean isOpen, final int keyboardHeight) {
        if (valueAnimatorSmall == null) {
            valueAnimatorSmall = ValueAnimator.ofInt();
        }
        float start, end;
        if (isOpen) {
            start = 0;
            end = keyboardHeight;
        } else {
            start = keyboardHeight;
            end = 0;
        }
        valueAnimatorSmall.cancel();
        valueAnimatorSmall.setFloatValues(start, end);
        valueAnimatorSmall.setDuration(200);
        valueAnimatorSmall.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float move = (Float) animation.getAnimatedValue();
                animatorListener.onMove(isOpen, keyboardHeight, move);
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



    public interface OnKeyboardStateListener {
        void onVisibilityChanged(boolean isOpen, int keyboardHeight);
    }

    public interface OnKeyboardStateAnimatorListener {
        void onMove(boolean isOpen, int keyboardHeight, float move);
    }
}
