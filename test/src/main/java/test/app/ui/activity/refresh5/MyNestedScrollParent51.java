package test.app.ui.activity.refresh5;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import sj.mblog.Logx;
import test.app.ui.activity.R;

/**
 * 嵌套滚动（子 -> 父）
 * 1.startNestedScroll       ->  onStartNestedScroll,onNestedScrollAccepted
 * 2.dispatchNestedPreScroll ->  onNestedPreScroll
 * 3.dispatchNestedScroll    ->  onNestedScroll
 * 4.stopNestedScroll        ->  onStopNestedScroll
 */
public class MyNestedScrollParent51 extends LinearLayout implements NestedScrollingParent {
    public MyNestedScrollParent51(Context context) {
        super(context);
    }

    public MyNestedScrollParent51(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private int mTouchSlop;

    private NestedScrollingParentHelper mNestedScrollingParentHelper;

    private void init() {
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        setNestedScrollingEnabled(false);
    }


    private View rlLoad;
    //头部高度
    private int headViewHeight = 0;
    private View ivLoad;

    //获取子view
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rlLoad = findViewById(R.id.rl_load);
        headViewHeight = getContext().getResources().getDimensionPixelSize(R.dimen.dp_80);
        ivLoad = findViewById(R.id.iv_load);
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

    public boolean canChildScrollUp() {
        if (recyclerView == null) {
            return false;
        }
        return ViewCompat.canScrollVertically(recyclerView, -1);
    }

    private RecyclerView recyclerView;

    //
    //在此可以判断参数target是哪一个子view以及滚动的方向，然后决定是否要配合其进行嵌套滚动
    //返回true 表示要配合子view做出响应
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        boolean result = false;
        //boolean result = isEnabled() && !mRefreshing && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (target instanceof RecyclerView) {
            recyclerView = (RecyclerView) target;
            result = true;
        }
        return result;
    }

    //接受滚动
    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    //本次滑动结束
    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
    }

    //先于child滚动
    //在子视图消费滚动之前调用
    //做出相应的处理把处理完后的结果通过 consumed 传给子 view。
    //前3个为输入参数，最后一个是输出参数  (dy<0 手指向上滑动)  (dy>0 手指向下滑动)
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {
            Logx.d("父类:" + " 2向上滑动 dy>0");
            setDataLoadHide(dy, consumed);
            //
        }
        if (dy < 0) {
            Logx.d("父类:" + " 1向下滑动 dy<0");
            setDataLoadShow(dy, consumed);
        }
    }

    //手指向下滑动 dy<0
    private void setDataLoadShow(int dy, int[] consumed) {
        //true 可以向上滑动
        boolean isCanUp = recyclerView.canScrollVertically(-1);
        if (isCanUp) {
            return;
        }
        /*if (Math.abs(dy) < mTouchSlop) {
            consumed[1] = dy;
            return;
        }*/
        ViewGroup.LayoutParams lp = rlLoad.getLayoutParams();
        int tempHeight = lp.height;
        int newHeight = tempHeight + Math.abs(dy);
        if (newHeight > headViewHeight) {
            newHeight = headViewHeight;
        }
        lp.height = newHeight;
        rlLoad.setLayoutParams(lp);
        consumed[1] = dy;//告诉child我消费了多少
        Logx.d("父类:" + " 1向下滑动 tempHeight=" + tempHeight + " dy=" + dy + " newHeight=" + newHeight + " headViewHeight=" + headViewHeight);
    }

    //手指上划 dy >0
    private void setDataLoadHide(int dy, int[] consumed) {
        boolean isCanUp = recyclerView.canScrollVertically(-1);
        if (isCanUp) {
            return;
        }
        /*if (Math.abs(dy) < mTouchSlop) {
            consumed[1] = dy;
            return;
        }*/
        ViewGroup.LayoutParams lp = rlLoad.getLayoutParams();
        int tempHeight = lp.height;
        int newHeight = tempHeight - Math.abs(dy);
        if (newHeight < 0) {
            newHeight = 0;
        }
        lp.height = newHeight;
        rlLoad.setLayoutParams(lp);
        consumed[1] = dy;//告诉child我消费了多少
        Logx.d("父类:" + " 2向上滑动 tempHeight=" + tempHeight + " dy=" + dy + " newHeight=" + newHeight);
    }

    // scrollBy内部会调用scrollTo
    // 限制滚动范围
    //y：垂直滚动偏移量（规则：正数 = 内容向上滚，负数 = 内容向下滚）
   /* @Override
    public void scrollTo(int x, int y) {
        Logx.d("父类:" + " y=" + y);
        if (y < 0) {
            //headView 已经完全展示，再向下移动，会留白，因此 要归0
            y = 0;
        }
        super.scrollTo(x, y);
    }*/

    //后于child滚动
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    //当惯性嵌套滚动时被调用之前
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    //当惯性嵌套滚动时被调用
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

}