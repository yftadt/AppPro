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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;

import java.util.ArrayList;

import sj.mblog.Logx;

/**
 * 嵌套滚动（子 -> 父）
 * 1.startNestedScroll       ->  onStartNestedScroll,onNestedScrollAccepted
 * 2.dispatchNestedPreScroll ->  onNestedPreScroll
 * 3.dispatchNestedScroll    ->  onNestedScroll
 * 4.dispatchNestedPreFling  ->  onNestedPreFling  子 View 处理 Fling 前先问父 View 是否要处理，父 View 处理了则子 View 放弃
 * 5.dispatchNestedFling     ->  onNestedFling     子 View 处理完 Fling 后，把剩余事件交给父 View 处理
 * 6.stopNestedScroll        ->  onStopNestedScroll
 */
public class BaseRefreshLayout extends FrameLayout implements NestedScrollingChild, NestedScrollingParent {
    public BaseRefreshLayout(Context context) {
        super(context);
    }

    public BaseRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private String tag = "下拉刷新_";
    private int mTouchSlop;

    private NestedScrollingParentHelper mNestedScrollingParentHelper;
    private NestedScrollingChildHelper mNestedChildHelper;

    private void init() {
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedChildHelper = new NestedScrollingChildHelper(this);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        //true 启用联动
        setNestedScrollingEnabled(true);
    }


    protected View rlRootLoad;
    //头部高度
    protected int headViewHeight = 0;

    //加载视图高度
    protected int loadViewHeight = 0;
    protected STATE stateType = STATE.Empty;

    private int type = 0;


    /**
     * @param headView       头部view
     * @param headViewHeight 头部view的高度
     * @param loadViewHeight 加载View的高度/触发刷新的阈值
     */
    public void setHeadView(View headView, int headViewHeight, int loadViewHeight) {
        stateType = STATE.Empty;
        if (rlRootLoad != null) {
            removeView(rlRootLoad);
            rlRootLoad = null;
        }
        this.rlRootLoad = headView;
        this.headViewHeight = headViewHeight;
        this.loadViewHeight = loadViewHeight;
        //
        if (type == 0) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, 0);
            addView(headView, 0, lp);
        } else {
            int count = getChildCount();
            ArrayList<View> childViews = new ArrayList();
            for (int i = 0; i < count; i++) {
                View viewtemp = getChildAt(i);
                childViews.add(viewtemp);
            }
            childViews.add(0, headView);
            removeAllViews();
            for (int i = 0; i < childViews.size(); i++) {
                View childTemp = childViews.get(i);
                if (childTemp == headView) {
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, 0);
                    addView(childTemp, lp);
                } else {
                    addView(childTemp);
                }
            }
        }
        stateType = STATE.Init;
    }

    //拖动阻力
    protected float getDragCoefficient() {
        return 0.4f;
    }

    //##==========================NestedScrollingParent 开始=======================
    //
    //在此可以判断参数target是哪一个子view以及滚动的方向，然后决定是否要配合其进行嵌套滚动
    //返回true 表示要配合子view做出响应
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isStartNestedScroll(child, target, nestedScrollAxes);
    }

    protected boolean isStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return false;
    }

    /**
     * 是嵌套滑动的「初始化回调」，在 onStartNestedScroll 返回 true 后触发，核心做状态初始化；
     * 不是处理滑动事件，而是初始化状态
     * 1.重置滑动相关的变量（比如清空上一次的滑动偏移量、重置动画状态）；
     * 2.记录滑动开始的初始位置（比如获取 target 的初始坐标，用于计算滑动距离）；
     * 3.标记滑动类型 / 方向（比如记录本次是垂直触摸滑动，后续事件只处理垂直方向）；
     * 4.初始化动画 / 手势检测器（比如提前创建 Scroller 实例处理惯性滑动）。
     *
     * @param child            父 View 的直接子 View（是 target 的父级，不一定是直接父级），简单说就是「嵌套滑动链中，离父 View 最近的那个子 View」。
     *                         //比如布局结构是 ParentLayout -> ChildLayout -> RecyclerView，
     *                         //那么 child 是 ChildLayout，target 是 RecyclerView；
     *                         // 如果布局是 ParentLayout -> RecyclerView，
     *                         //那么 child 和 target 是同一个 View。
     * @param target           实际触发嵌套滑动的子 View（比如 RecyclerView、ScrollView、ViewPager 等）
     * @param nestedScrollAxes 表示嵌套滑动的方向轴（二进制位标识），只有两种有效值：
     *                         ViewCompat.SCROLL_AXIS_HORIZONTAL：水平方向滑动；
     *                         ViewCompat.SCROLL_AXIS_VERTICAL：垂直方向滑动；
     * @param type:            Int 表示嵌套滑动的触发类型，是 Android 5.0+ 新增的参数，核心区分「手指触摸滑动」和「非触摸滑动（如 fling）
     *                         ViewCompat.TYPE_TOUCH：手指触摸屏幕触发的滑动（最常见，比如手指拖动 RecyclerView）；
     *                         ViewCompat.TYPE_NON_TOUCH：非触摸触发的滑动（比如 fling 惯性滑动、代码主动调用 scrollTo 等）。
     *                         父 View 可根据类型做差异化处理，比如「仅处理手指触摸的滑动，忽略 fling 惯性滑动」
     */
    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        //startNestedScroll(nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    //本次滑动结束
    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        Logx.d(tag + "onStopNestedScroll_" + "滑动结束" + " stateType=" + stateType.toString());
        setStop();
    }

    private void setStop() {
        if (stateType != STATE.Init) {
            //只有初始化状态 才有回弹
            return;
        }
        if (animationRebound != null && animationRebound.isAnimRunning()) {
            //正在动画就停止
            return;
        }
        int viewHeight = rlRootLoad.getHeight();
        int teamHeight = 0;
        int tempType = 0;
        if (viewHeight >= loadViewHeight) {
            //只显示大于loadViewHeight的部分
            teamHeight = viewHeight - loadViewHeight;
            tempType = 2;
        } else {
            //全部消失
            teamHeight = viewHeight;
            tempType = 1;
        }
        if (teamHeight == 0) {
            return;
        }
        Logx.d(tag + "设置回弹动画" + " 总高度=" + headViewHeight + " 阈值=" + loadViewHeight + " 现高度=" + viewHeight + " 回弹高度=" + teamHeight + " stateType=" + stateType.toString());
        setReboundAnimator(tempType, teamHeight);
    }

    //先于child滚动
    //在子视图消费滚动之前调用
    //做出相应的处理把处理完后的结果通过 consumed 传给子 view。
    //前3个为输入参数，最后一个是输出参数  正数：子 View 内容向上滚动；  负数：子 View 内容向下滚动；
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (stateType == STATE.Init || stateType == STATE.Refresh) {
            setPreScroll(target, dx, dy, consumed);
        }
    }

    protected void setPreScroll(View target, int dx, int dy, int[] consumed) {
    }

    protected int setViewParams(int dy) {
        ViewGroup.LayoutParams lp = rlRootLoad.getLayoutParams();
        int tempHeight = lp.height;
        int newHeight = tempHeight + (dy);
        int minHeight = getHeadMinHeight();
        if (newHeight < minHeight) {
            newHeight = minHeight;
        }
        int maxHeight = getHeadMaxHeight();
        if (newHeight > maxHeight) {
            newHeight = maxHeight;
        }
        Logx.d(tag + "" + " 总高度=" + maxHeight + " 当前高度=" + tempHeight + " 移动=" + dy + "设置高度=" + newHeight + " stateType=" + stateType.toString());
        if (newHeight != lp.height) {
            lp.height = newHeight;
            rlRootLoad.setLayoutParams(lp);
            setTargetViewOffset(newHeight);
        }
        return newHeight;
    }

    protected View mTargetView;

    //设置偏移，否则会出现抖动
    private void setTargetViewOffset(float dis) {
        if (mTargetView != null) {
            mTargetView.setTranslationY(dis);
        }
    }

    //返回head可以显示的最大高度
    private int getHeadMaxHeight() {
        int temp = headViewHeight;
        if (STATE.Empty == stateType || STATE.Init == stateType) {
            temp = headViewHeight;
        }
        if (STATE.AminRebound_Refresh == stateType || STATE.AminRebound_Init == stateType) {
            temp = headViewHeight;
        }
        if (STATE.Refresh == stateType || STATE.RefreshEnd_Init == stateType) {
            temp = loadViewHeight;
        }
        return temp;
    }

    //返回head可以显示的最小高度
    private int getHeadMinHeight() {
        int temp = 0;
        if (STATE.AminRebound_Refresh == stateType) {
            temp = loadViewHeight;
        }
        return temp;
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
    private int[] mParentOffsetInWindow;

    //后于child滚动
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (mParentOffsetInWindow == null) {
            mParentOffsetInWindow = new int[2];
        }
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, mParentOffsetInWindow);
    }


    /**
     * 当惯性嵌套滚动时被调用之前
     *
     * @param target    触发 fling 事件的子 View（即嵌套滑动的子 View，比如 RecyclerView、ScrollView 等）
     * @param velocityX 水平方向的 fling 速度（单位：像素 / 秒）
     * @param velocityY 垂直方向的 fling 速度（单位：像素 / 秒）
     *                  //
     *                  正数：子 View 内容向上滚动；
     *                  负数：子 View 内容向下滚动；
     *                  //
     * @return 返回 true 表示父 View 处理了，子 View 不再处理
     */
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Logx.d(tag + "onNestedPreFling_" + "惯性滑动之前" + " stateType=" + stateType.toString());
        //
        if (stateType == STATE.Empty) {
            return dispatchNestedPreFling(velocityX, velocityY);
        }
        int height = rlRootLoad.getHeight();
        if (height != 0) {
            //一般是有head只显示了部分，子滑动之前，先滑动head，至其消失或者全部显示
            boolean isConsumed = setFlingAnimator(1, velocityY);
            if (isConsumed) {
                return true;
            }
        }
        return dispatchNestedPreFling(velocityX, velocityY);
    }


    /**
     * 当惯性嵌套滚动时被调用,只有子 View 消耗了 Fling（consumed=true），且有剩余滑动时，父 View 才处理
     *
     * @param target    触发 fling 事件的子 View（即嵌套滑动的子 View，比如 RecyclerView、ScrollView 等）
     * @param velocityX 水平方向的 fling 速度（单位：像素 / 秒）
     * @param velocityY 垂直方向的 fling 速度（单位：像素 / 秒）
     *                  //
     *                  正数：子 View 内容向上滚动；
     *                  负数：子 View 内容向下滚动；
     *                  //
     * @param consumed  true 子View消费了这个 fling 事件，父 View 不会再执行 fling；
     * @return
     */
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        //
        Logx.d(tag + "onNestedPreFling_" + "惯性滑动之后" + " stateType=" + stateType.toString());
        if (consumed) {
            //setViewParams(-(int) velocityY);
            setFlingAnimator(1, velocityY);
        }
        return false;//dispatchNestedFling(velocityX, velocityY, consumed);
    }


    private int animationType;
    private AnimationManager animationFling;

    /**
     * @param type      显示可消失动画
     * @param velocityY 正数：子 View 内容向上滚动；  负数：子 View 内容向下滚动；
     * @param
     * @return true 已消耗
     */
    private boolean setFlingAnimator(int type, float velocityY) {
        if (stateType != STATE.Refresh) {
            return false;
        }
        if (animationRebound != null && animationRebound.isAnimRunning()) {
            //正在回弹动画
            return false;
        }
        if (velocityY == 0) {
            return false;
        }
        int viewHeight = rlRootLoad.getHeight();
        int tempHeight = 0;
        int y = (int) Math.abs(velocityY);
        if (velocityY > 0) {
            if (viewHeight == 0) {
                return false;
            }
            //head 全部消失
            //tempHeight = Math.min(height, y);
            tempHeight = viewHeight;
            animationType = 1;
        }
        if (velocityY < 0) {
            //head 全部显示
            int maxHeight = getHeadMaxHeight();
            if (viewHeight >= maxHeight) {
                return false;
            }
            int surplusHeight = maxHeight - viewHeight;
            //tempHeight = Math.min(surplusHeight, y);
            tempHeight = surplusHeight;
            animationType = 2;
        }
        Logx.d(tag + "" + "惯性滑动动画开始_" + " stateType=" + stateType.toString());

        //消失 或者全部显示
        animationFling = new AnimationManager();
        animationFling.setAnimatorPar(tempHeight, new OnAnimationListener() {
            //上次移动距离
            private float upMoveValue;
            //移动总距离
            private float numTotal;

            @Override
            public void onAnimationStart(AnimationManager manager, Animator animation) {

            }

            @Override
            public void onAnimationEnd(AnimationManager manager, Animator animation) {

            }

            @Override
            public void onAnimationUpdate(AnimationManager manager, ValueAnimator animation) {
                //动画移动速度
                float value = (float) animation.getAnimatedValue(); // 获取当前动画值
                long animationTime = animation.getCurrentPlayTime(); // 获取动画已播放的时间（毫秒）

                int move = (int) (value - upMoveValue);
                if (move > 0) {
                    upMoveValue = value;
                }
                if (value == manager.animDistance) {
                    move = (int) (manager.animDistance - numTotal) + 1;
                }
                String str = "";
                if (animationType == 1 && move > 0) {
                    str = "隐藏";
                    numTotal += move;
                    setViewParams(-move);
                }
                if (animationType == 2 && move > 0) {
                    numTotal += move;
                    setViewParams(move);
                    str = "显示";
                }
            }
        });
        return true;
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }
    //##==========================NestedScrollingParent  结束=======================

    //##==========================NestedScrollingChild  开始=======================

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable @Size(value = 2) int[] offsetInWindow) {
        return mNestedChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable @Size(value = 2) int[] consumed, @Nullable @Size(value = 2) int[] offsetInWindow) {
        return mNestedChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        //子 View 处理完 Fling 后，把剩余事件交给父 View 处理
        return mNestedChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {

        //子 View 处理 Fling 前先问父 View 是否要处理，父 View 处理了则子 View 放弃。
        return mNestedChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    //##==========================NestedScrollingChild  结束=======================
    //回弹动画
    private AnimationManager animationRebound;

    /**
     * @param type      修改stateType的状态  （1： Init 2： Refresh）
     * @param velocityY 移动距离
     */
    private void setReboundAnimator(int type, int velocityY) {
        if (type == 1) {
            stateType = STATE.AminRebound_Init;
        }
        if (type == 2) {
            stateType = STATE.AminRebound_Refresh;
        }
        animationRebound = new AnimationManager();
        animationRebound.setTypeState(type);
        animationRebound.setAnimatorPar(velocityY, new OnAnimationListener() {
            //上次移动距离
            private float upMoveValue;
            //移动总距离
            private float numTotal;

            @Override
            public void onAnimationStart(AnimationManager manager, Animator animation) {

            }

            @Override
            public void onAnimationEnd(AnimationManager manager, Animator animation) {
                // 修改stateType的状态  （1： Init 2： Refresh）
                switch (manager.typeState) {
                    case 1:
                        int viewHeight = rlRootLoad.getHeight();
                        Logx.d(tag + "" + "回弹动画_结束_回弹总高度=" + numTotal + " 修正高度_" + viewHeight + " stateType=" + stateType.toString());
                        if (viewHeight != 0) {
                            setViewParams(-viewHeight);
                        }
                        stateType = STATE.Init;
                        break;
                    case 2:
                        //这里调刷新接口
                        if (refreshListener != null) {
                            refreshListener.onRefresh();
                        }
                        viewHeight = rlRootLoad.getHeight();
                        int tempHeight = viewHeight - loadViewHeight;
                        Logx.d(tag + "" + "回弹动画_结束_回弹总高度=" + numTotal + " 修正高度_" + tempHeight + " stateType=" + stateType.toString());
                        if (tempHeight != 0) {
                            setViewParams(-tempHeight);
                        }
                        stateType = STATE.Refresh;
                        break;
                }
            }

            @Override
            public void onAnimationUpdate(AnimationManager manager, ValueAnimator animation) {
                //动画移动速度
                float value = (float) animation.getAnimatedValue(); // 获取当前动画值
                long animationTime = animation.getCurrentPlayTime(); // 获取动画已播放的时间（毫秒）
                int move = (int) (value - upMoveValue);
                if (move > 0) {
                    upMoveValue = value;
                }
                if (value == manager.animDistance) {
                    move = (int) (manager.animDistance - numTotal) + 1;
                }
                String str = "隐藏";
                numTotal += move;
                if (move > 0) {
                    Logx.d(tag + "滚动 有效" + " 本次=" + value + " 上一次=" + upMoveValue + " move=" + move + " numTotal=" + numTotal + " stateType=" + stateType.toString());
                    setViewParams(-move);
                } else {
                    Logx.d(tag + "滚动 无效" + " 本次=" + value + " 上一次=" + upMoveValue + " move=" + move + " numTotal=" + numTotal + " stateType=" + stateType.toString());
                }
                //
            }
        });
    }

    //设置下拉刷新 结束
    public void setRefreshingEnd() {
        if (stateType != STATE.Refresh) {
            return;
        }
        stateType = STATE.RefreshEnd_Init;
        if (animationFling != null && animationFling.isAnimRunning()) {
            animationFling.setAnimStop();
        }
        int viewHeight = rlRootLoad.getHeight();
        if (viewHeight == 0) {
            stateType = STATE.Init;
            return;
        }
        Logx.d(tag + "" + "结束下拉刷新动画_" + " stateType=" + stateType.toString());
        AnimationManager temp = new AnimationManager();
        temp.setAnimatorPar(viewHeight, new OnAnimationListener() {
            //上次移动距离
            private float upMoveValue;
            //移动总距离
            private float numTotal;

            @Override
            public void onAnimationStart(AnimationManager manager, Animator animation) {

            }

            @Override
            public void onAnimationEnd(AnimationManager manager, Animator animation) {
                // 修改stateType的状态
                stateType = STATE.Init;
            }

            @Override
            public void onAnimationUpdate(AnimationManager manager, ValueAnimator animation) {
                //动画移动速度
                float value = (float) animation.getAnimatedValue(); // 获取当前动画值
                long animationTime = animation.getCurrentPlayTime(); // 获取动画已播放的时间（毫秒）
                int move = (int) (value - upMoveValue);
                if (move > 0) {
                    upMoveValue = value;
                }
                if (value == manager.animDistance) {
                    move = (int) (manager.animDistance - numTotal) + 1;
                }
                String str = "隐藏";
                numTotal += move;
                if (move > 0) {
                    setViewParams(-move);
                }
                //
            }
        });
    }

    private OnRefreshListener refreshListener;

    public void setOnRefreshListener(@Nullable OnRefreshListener listener) {
        refreshListener = listener;
    }

    //================================状态====================================
    public static enum STATE {

        Empty,//空
        Init,//初始状态
        Refresh, //刷新状态
        RefreshEnd_Init,//刷新结束状态->Init
        AminRebound_Refresh,//回弹动画->Refresh
        AminRebound_Init//回弹动画->Init
    }

    //================================动画====================================
    class AnimationManager {
        private ValueAnimator animator;
        //移动总距离
        private int animDistance;
        // 修改stateType的状态  （1： Init 2： Refresh）
        private int typeState;

        private void setTypeState(int typeState) {
            this.typeState = typeState;
        }

        private boolean isAnimRunning() {
            return animator.isRunning();
        }

        private void setAnimStop() {
            animator.cancel();
        }

        /**
         * @param animationDistance 移动距离
         */
        private void setAnimatorPar(int animationDistance, OnAnimationListener listener) {
            animDistance = animationDistance;
            animator = ValueAnimator.ofFloat(0, animationDistance);
            //
            float temp = ((float) animationDistance) / 100;
            //每100的距离 移动时间是50毫秒
            int time = (int) (temp * 50);
            if (time == 0) {
                time = 10;
            }
            animator.setDuration(time);
            animator.setInterpolator(new AccelerateDecelerateInterpolator()); // 插值器（先加速后减速）
            // animator.setRepeatCount(ValueAnimator.INFINITE); // 重复次数（INFINITE 无限）
            // animator.setRepeatMode(ValueAnimator.REVERSE); // 重复模式（REVERSE 反向，RESTART 重启）
            animator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(@NonNull Animator animation) {
                    listener.onAnimationStart(AnimationManager.this, animation);
                }

                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    listener.onAnimationEnd(AnimationManager.this, animation);
                }

                @Override
                public void onAnimationCancel(@NonNull Animator animation) {

                }

                @Override
                public void onAnimationRepeat(@NonNull Animator animation) {
                    //动画重复
                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                    listener.onAnimationUpdate(AnimationManager.this, animation);
                }
            });

            animator.start();
        }
    }

    interface OnAnimationListener {
        void onAnimationStart(AnimationManager manager, Animator animation);

        void onAnimationEnd(AnimationManager manager, Animator animation);

        void onAnimationUpdate(AnimationManager manager, ValueAnimator animation);
    }


    public interface OnRefreshListener {

        void onRefresh();
    }
}