package test.app.ui.activity.refresh5;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ListViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sj.mblog.Logx;
import test.app.ui.activity.R;


public class MyNestedScrollParent52 extends BaseRefreshLayout {
    public MyNestedScrollParent52(Context context) {
        super(context);
    }

    public MyNestedScrollParent52(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    //获取子view
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rlRootLoad = findViewById(R.id.rl_root_load);
        headViewHeight = getContext().getResources().getDimensionPixelSize(R.dimen.dp_180);
        loadViewHeight = getContext().getResources().getDimensionPixelSize(R.dimen.dp_50);
        stateType = STATE.Init;
    }

    @Override
    protected boolean isStartNestedScroll(View child, View target, int nestedScrollAxes) {
        boolean result = false;
        //boolean result = isEnabled() && !mRefreshing && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (target instanceof MyNestedScrollChild5) {
            mTargetView = (MyNestedScrollChild5) target;
            result = true;
        }
        return result;
    }

    /**
     * 先于child滚动
     * 在子视图消费滚动之前调用
     * 做出相应的处理把处理完后的结果通过 consumed 传给子 view。
     *
     * @param target
     * @param dx
     * @param dy       正数：子 View 内容向下滚动；  负数：子 View 内容向上滚动；
     * @param consumed
     */
    @Override
    protected void setPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {
            setDataLoadShow(dy, consumed);
            //
        }
        if (dy < 0) {
            setDataLoadHide(dy, consumed);
        }
    }

    private void setDataLoadShow(int dy, int[] consumed) {

        //
        int scrollY = getScrollY();
        int childScrollY = mTargetView.getScrollY();
        if (scrollY >= 0 && childScrollY == 0) {
            int newHeight = setViewParams(dy);
            consumed[1] = dy;//告诉child我消费了多少
            //setTargetViewOffset(newHeight);
            Logx.d("父类:" + " 1向下滑动 " + " dy=" + dy + " newHeight=" + newHeight + " headViewHeight=" + headViewHeight);
        }

    }
    //
    private void setDataLoadHide(int dy, int[] consumed) {
        int scrollY = getScrollY();
        int childScrollY = mTargetView.getScrollY();
        int height = rlRootLoad.getHeight();
        Logx.d("滑动--> :" + " scrollY=" + scrollY + " childScrollY=" + childScrollY);
        if (height != 0) {
            int newHeight = setViewParams(dy);
            //
            consumed[1] = dy;//告诉child我消费了多少
            //setTargetViewOffset(newHeight);
            Logx.d("父类:" + " 2向上滑动 " + " dy=" + dy + " newHeight=" + newHeight);
        }

    }

}