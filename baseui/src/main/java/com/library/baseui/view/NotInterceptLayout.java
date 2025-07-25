package com.library.baseui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 设置分类不不拦截
 */
class NotInterceptLayout extends FrameLayout {
    public NotInterceptLayout(@NonNull Context context) {
        super(context);
    }

    public NotInterceptLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NotInterceptLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NotInterceptLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        //设置这个view之上的所有viewGroup 不执行拦截，其作用在dispatchTouchEvent 标志设置为true之后，
        // 不会走onInterceptTouchEvent方法。
        // dispatchTouchEvent 还会记录所消费MotionEvent.ACTION_DOWN事件的view，之后所有的触摸事件会分发到这个view上
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
