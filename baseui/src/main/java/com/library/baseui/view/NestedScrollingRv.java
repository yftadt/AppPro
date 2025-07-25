package com.library.baseui.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 设置自己不滑动
 */
public class NestedScrollingRv extends RecyclerView {
    public NestedScrollingRv(@NonNull Context context) {
        super(context);
        init();
    }

    public NestedScrollingRv(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestedScrollingRv(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setNestedScrollingEnabled(false);
    }
}
