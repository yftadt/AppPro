package test.app.ui.activity.refresh4;



import android.content.Context;
import android.os.Handler;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
/**
 * Created by wangjitao on 2017/2/14 0014.
 * E-Mail：543441727@qq.com
 * 嵌套滑动机制父View
 */

public class MyNestedScrollParent4 extends LinearLayout implements NestedScrollingParent {
    private ImageView img;
    private TextView tv;
    private ProgressBar progressBar;
    private MyNestedScrollChild4 myNestedScrollChild;
    private NestedScrollingParentHelper mNestedScrollingParentHelper;
    private int imgHeight;
    private int tvHeight;
    private int MoveY;
    protected STATE mState;
    private Handler handler = new Handler();


    enum STATE {
        REFRESH, START, PULL, COMPLETE
    }

    public MyNestedScrollParent4(Context context) {
        super(context);
    }

    public MyNestedScrollParent4(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mState = STATE.REFRESH;
        // progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void setState(STATE state) {
        mState = state;
    }

    //获取子view
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        img = (ImageView) getChildAt(0);
        tv = (TextView) getChildAt(2);
        progressBar = (ProgressBar) getChildAt(1);
        myNestedScrollChild = (MyNestedScrollChild4) getChildAt(3);
        img.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (imgHeight <= 0) {
                    imgHeight = img.getMeasuredHeight();
                    img.setVisibility(View.GONE);
                }
            }
        });
        tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvHeight <= 0) {
                    tvHeight = tv.getMeasuredHeight();
                }
            }
        });
    }

    //在此可以判断参数target是哪一个子view以及滚动的方向，然后决定是否要配合其进行嵌套滚动
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (target instanceof MyNestedScrollChild4) {
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
    //前3个为输入参数，最后一个是输出参数
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {
            // 正在刷新的时候，不让下拉
            if (mState.ordinal() > STATE.START.ordinal()) {
                consumed[1] = dy;
            } else {
                MoveY += dy/2;
                img.setVisibility(View.VISIBLE);
                setImgHeight(dy/2 + getImgHeight());
                // getScrollY 就是移动的距离，朝着负方向移动距离为正；
                if (MoveY > imgHeight) {
                    setState(STATE.START);
                    // scrollBy 大于0  的话，朝着 - 方向移动，
                    consumed[1] = dy;   //告诉child我消费了多少
                } else {
                    consumed[1] = dy;   //告诉child我消费了多少
                }
            }

        } else {
            setState(STATE.REFRESH);
            int imgH = getImgHeight();
            if (imgH > 0) {
                MoveY += dy/2;
                if (-dy > imgH){
                    setImgHeight(0);
                    consumed[1] = dy + imgH;
                    img.setVisibility(View.GONE);
                } else {
                    setImgHeight(imgH - (-dy));
                    consumed[1] = dy;
                }
            }else{
                consumed[1] = 0;
            }

        }
/*
        if (showImg(dy) || hideImg(dy)) {//如果需要显示或隐藏图片，即需要自己(parent)滚动
            scrollBy(0, -dy);//滚动
            consumed[1] = dy;//告诉child我消费了多少
        }*/
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
            if (getScrollY() > 0 && myNestedScrollChild.getScrollY() == 0) {
                return true;
            }
        }

        return false;
    }

    //上拉的时候，是否要向上滚动，隐藏图片
    public boolean hideImg(int dy) {
        if (dy < 0) {
            if (getScrollY() < imgHeight) {
                return true;
            }
        }
        return false;
    }

    //scrollBy内部会调用scrol

    public void setImgHeight(int height) {
        LinearLayout.LayoutParams imgPL = (LayoutParams) img.getLayoutParams();
        imgPL.height = height;
        img.setLayoutParams(imgPL);
    }

    public void pointerUp(){
        // 如果状态是已经开始刷新了，那么就显示progressbar
        if (mState.ordinal()> STATE.REFRESH.ordinal()) {
            setImgHeight(imgHeight);
            setImgHeight(0);
            progressBar.setVisibility(View.VISIBLE);
            setState(STATE.PULL);
            handler.postDelayed(runnable, 2000);
        }
        MoveY = 0;
    }

    public int getImgHeight() {
        LinearLayout.LayoutParams imgPL = (LayoutParams) img.getLayoutParams();
        return imgPL.height;
    }
    // 用于控制progressBar的显示时间
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            progressBar.setVisibility(View.GONE);
            // 重置状态
            setState(STATE.REFRESH);
        }
    };
}