package test.app.ui.activity.refresh5.child;


import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;


/**
 * 下拉刷新子view
 */
public class RefreshChildLl extends LinearLayout implements NestedScrollingChild {
    private NestedScrollingChildHelper mNestedScrollingChildHelper;

    private int lastY;
    private int showHeight;


    public RefreshChildLl(Context context) {
        super(context);
    }

    public RefreshChildLl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //第一次测量，因为布局文件中高度是wrap_content，因此测量模式为atmost，即高度不超过父控件的剩余空间
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        showHeight = getMeasuredHeight();
        //第二次测量，对稿哦度没有任何限制，那么测量出来的就是完全展示内容所需要的高度
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int[] offset = new int[2]; //偏移量
    private int[] consumed = new int[2]; //消费

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            //按下
            case MotionEvent.ACTION_DOWN:
                lastY = (int) event.getRawY();
                offset[0] = 0;
                offset[1] = 0;
                consumed[0] = 0;
                consumed[1] = 0;
                break;
            //移动
            case MotionEvent.ACTION_MOVE:
                int y = (int) (event.getRawY());
                int dy = y - lastY;
                lastY = y;
                if (getScrollY() <= 0 && startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL) && dispatchNestedPreScroll(0, dy, consumed, offset)) //如果找到了支持嵌套滑动的父类,父类进行了一系列的滑动
                {
                    //获取滑动距离
                    int remain = dy - consumed[1];
                    if (remain != 0) {
                        scrollBy(0, -remain);
                    }

                } else {
                    scrollBy(0, -dy);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }


    //限制滚动范围
    @Override
    public void scrollTo(int x, int y) {
        int viewHeight = getHeight();
        int rootHeight = getDataRootViewHeight();
        int maxY = viewHeight - rootHeight;
        if (maxY < 0) {
            maxY = 0;
        }
        if (y > maxY) {
            y = maxY;
        }
        if (y < 0) {
            y = 0;
        }
        super.scrollTo(x, y);
    }

    private int rootViewHeight = 0;

    private int getDataRootViewHeight() {
        if (rootViewHeight != 0) {
            return rootViewHeight;
        }
        ViewParent view = getParent();
        view = view.getParent();
        rootViewHeight = ((View) view).getHeight();
        return rootViewHeight;
    }

    //初始化helper对象
    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (mNestedScrollingChildHelper == null) {
            mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
            mNestedScrollingChildHelper.setNestedScrollingEnabled(true);
        }
        return mNestedScrollingChildHelper;
    }

    //开始一个嵌套滚动。如果当前视图可以处理嵌套滚动，则返回true
    //实质上是寻找能够配合 child 进行嵌套滚动的 parent
    //父 view 会收到,会执行 onStartNestedScroll
    @Override
    public boolean startNestedScroll(int axes) {
        return getScrollingChildHelper().startNestedScroll(axes);
    }

    //设置是否能够滑动
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        getScrollingChildHelper().setNestedScrollingEnabled(enabled);
    }

    //返回是否能够滑动
    @Override
    public boolean isNestedScrollingEnabled() {
        return getScrollingChildHelper().isNestedScrollingEnabled();
    }


    //停止嵌套滚动
    @Override
    public void stopNestedScroll() {
        getScrollingChildHelper().stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return getScrollingChildHelper().hasNestedScrollingParent();
    }

    //触摸滚动
    //前四个参数为输入参数，用于告诉父view已经消费和尚未消费的距离，最后一个参数为输出参数，用于子view获取父view位置的偏移量
    //在子 view 自己进行滚动之后调用此方法，询问父view是否还要进行余下 (unconsumed) 的滚动。
    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    /**
     * @param dx             水平滑动距离
     * @param dy             垂直滑动距离
     * @param consumed       父类消耗掉的距离
     * @param offsetInWindow 父 view 位置的偏移量
     * @return
     */
    //触摸滚动之前
    //在滑动事件产生但是子 view 还没处理前可以调用
    //这个方法把事件传给父 view 这样父 view 就能在onNestedPreScroll 方法里面收到子 view 的滑动信息，
    //然后做出相应的处理把处理完后的结果通过 consumed 传给子 view。
    //true :表示父view消费了滚动
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    //惯性滚动
    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return getScrollingChildHelper().dispatchNestedFling(velocityX, velocityY, consumed);
    }

    //惯性滚动之前
    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getScrollingChildHelper().dispatchNestedPreFling(velocityX, velocityY);
    }
}