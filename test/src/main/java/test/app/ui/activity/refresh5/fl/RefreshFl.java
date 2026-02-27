package test.app.ui.activity.refresh5.fl;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import test.app.ui.activity.R;

/**
 * 其可以滑动的子View：RecyclerView
 */
public class RefreshFl extends BaseRefreshLayoutFl {
    public RefreshFl(Context context) {
        super(context);
    }

    public RefreshFl(Context context, AttributeSet attrs) {
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled() || canChildScrollUp()) {
            // Fail fast if we're not in a state where a swipe is possible
            //当前状态还不具备进行滑动操作的条件
            // Logx.d(tag + " onInterceptTouchEvent false");
            return false;
        }
        //Logx.d(tag + " onInterceptTouchEvent super");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || canChildScrollUp()) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }
        return super.onTouchEvent(event);
    }


    //true 可以滑动
    private boolean canChildScrollUp() {
        if (mTargetView == null) {
            return false;
        }
        return ViewCompat.canScrollVertically(mTargetView, -1);
    }


    /**
     * @param child
     * @param target
     * @param nestedScrollAxes
     * @return
     */
    @Override
    protected boolean isStartNestedScroll(View child, View target, int nestedScrollAxes) {
        boolean result = false;
        //boolean result = isEnabled() && !mRefreshing && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (target instanceof RecyclerView) {
            mTargetView = (RecyclerView) target;
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
     * @param dy       正数：子 View 内容向上滚动；  负数：子 View 内容向下滚动；
     * @param consumed
     */
    @Override
    protected void setPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {
            setDataLoadHide(dy, consumed);
            //
        }
        if (dy < 0) {
            setDataLoadShow(dy, consumed);
        }
    }

    //负数：子 View 内容向下滚动；
    private void setDataLoadShow(int dy, int[] consumed) {
        //true 可以向上滑动
        boolean isCanUp = mTargetView.canScrollVertically(-1);
        if (isCanUp) {
            return;
        }
        /*if (Math.abs(dy) < mTouchSlop) {
            consumed[1] = dy;
            return;
        }*/
        int temp = (int) (dy * getDragCoefficient());
        int newHeight = setViewParams(-temp);
        consumed[1] = dy;//告诉child我消费了多少
    }

    //正数：子 View 内容向上滚动
    private void setDataLoadHide(int dy, int[] consumed) {
        boolean isCanUp = mTargetView.canScrollVertically(-1);
        if (isCanUp) {
            return;
        }
        /*if (Math.abs(dy) < mTouchSlop) {
            consumed[1] = dy;
            return;
        }*/
        int temp = (int) (dy * getDragCoefficient());
        int newHeight = setViewParams(-temp);
        //
        consumed[1] = dy;//告诉child我消费了多少
    }


}








