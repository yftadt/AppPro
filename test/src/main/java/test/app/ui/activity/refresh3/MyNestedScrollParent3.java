package test.app.ui.activity.refresh3;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;

import sj.mblog.Logx;

/**
 * Created by wangjitao on 2017/2/14 0014.
 * E-Mail：543441727@qq.com
 * 嵌套滑动机制父View
 */

public class MyNestedScrollParent3 extends LinearLayout implements NestedScrollingParent {
    private ImageView imgView;
    private TextView tvView;
    private MyNestedScrollChild3 myNestedScrollChild;
    private NestedScrollingParentHelper mNestedScrollingParentHelper;
    private int imgHeight;
    private int tvHeight;

    public MyNestedScrollParent3(Context context) {
        super(context);
    }

    public MyNestedScrollParent3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    //获取子view
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imgView = (ImageView) getChildAt(0);
        tvView = (TextView) getChildAt(1);
        myNestedScrollChild = (MyNestedScrollChild3) getChildAt(2);
        imgView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (imgHeight <= 0) {
                    imgHeight = imgView.getMeasuredHeight();
                }
            }
        });
        tvView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvHeight <= 0) {
                    tvHeight = tvView.getMeasuredHeight();
                }
            }
        });
    }

    //在此可以判断参数target是哪一个子view以及滚动的方向，然后决定是否要配合其进行嵌套滚动
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (target instanceof MyNestedScrollChild3) {
            return true;
        }
        return false;
    }


    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
    }

    //先于child滚动
    //前3个为输入参数，最后一个是输出参数  (dy<0 手指向上滑动)  (dy>0 手指向下滑动)
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (showImg(dy) || hideImg(dy)) {//如果需要显示或隐藏图片，即需要自己(parent)滚动
            //dy：垂直滚动偏移量（规则：正数 = 内容向上滚，负数 = 内容向下滚）
            scrollBy(0, -dy);//滚动
            consumed[1] = dy;//告诉child我消费了多少
        }
    }

    //后于child滚动
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    //返回值：是否消费了fling
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    //返回值：是否消费了fling
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    //下拉的时候是否要向下滚动以显示图片
    public boolean showImg(int dy) {
        if (dy > 0) {
            int scrollY = getScrollY();
            int childScrollY = myNestedScrollChild.getScrollY();
            Logx.d("下拉滚动位置：scrollY=" + scrollY + " childScrollY=" + childScrollY);
            if (scrollY > 0 && childScrollY == 0) {
                return true;
            }
        }

        return false;
    }

    //上拉的时候，是否要向上滚动，隐藏图片
    public boolean hideImg(int dy) {
        if (dy < 0) {
            int scrollY = getScrollY();
            Logx.d("上拉滚动位置：scrollY=" + scrollY + " imgHeight=" + imgHeight);
            if (scrollY < imgHeight) {
                return true;
            }
        }
        return false;
    }

    //scrollBy内部会调用scrollTo
    //限制滚动范围
    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > imgHeight) {
            y = imgHeight;
        }

        super.scrollTo(x, y);
    }
}
