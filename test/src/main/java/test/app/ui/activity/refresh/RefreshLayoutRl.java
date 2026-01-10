/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.app.ui.activity.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ListViewCompat;

import test.app.ui.activity.R;


public class RefreshLayoutRl extends RelativeLayout implements NestedScrollingParent3, NestedScrollingParent2, NestedScrollingChild3, NestedScrollingChild2, NestedScrollingParent, NestedScrollingChild {


    private static String LOG_TAG = RefreshLayoutRl.class.getSimpleName();

    private static float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static int INVALID_POINTER = -1;
    private static float DRAG_RATE = .5f;

    private static int SCALE_DOWN_DURATION = 150;
    private static int ANIMATE_TO_TRIGGER_DURATION = 200;

    private static int ANIMATE_TO_START_DURATION = 200;

    private static int DEFAULT_CIRCLE_TARGET = 64;

    private View mTarget;
    OnRefreshListener mListener;
    boolean mRefreshing = false;
    private int mTouchSlop;
    private float mTotalDragDistance = -1;

    private float mTotalUnconsumed;
    private NestedScrollingParentHelper mNestedScrollingParentHelper;
    private NestedScrollingChildHelper mNestedScrollingChildHelper;
    private int[] mParentScrollConsumed = new int[2];
    private int[] mParentOffsetInWindow = new int[2];

    // Used for calls from old versions of onNestedScroll to v3 version of onNestedScroll. This only
    // exists to prevent GC costs that are present before API 21.
    private int[] mNestedScrollingV2ConsumedCompat = new int[2];
    private boolean mNestedScrollInProgress;


    private float mInitialMotionY;
    private float mInitialDownY;
    private boolean mIsBeingDragged;
    private int mActivePointerId = INVALID_POINTER;
    boolean mScale;


    private boolean mReturningToStart;
    private DecelerateInterpolator mDecelerateInterpolator;
    private static int[] LAYOUT_ATTRS = new int[]{android.R.attr.enabled};

    private View mCircleView;

    private int mCircleViewIndex = -1;

    protected int mFrom;

    float mStartingScale;

    protected int mOriginalOffsetTop;
    int mCurrentTargetOffsetTop;

    int mSpinnerOffsetEnd;

    int mCustomSlingshotDistance;


    private Animation mScaleDownAnimation;//结束时的缩小 （消失动画）

    boolean mNotify;


    // Whether the client has set a custom starting position;
    boolean mUsingCustomStart;


    private boolean mEnableLegacyRequestDisallowInterceptTouch;

    public RefreshLayoutRl(@NonNull Context context) {
        this(context, null);
    }

    private AnimationListener mRefreshListener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            setViewShow(2);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mRefreshing) {
                if (mNotify) {
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                }
                mCurrentTargetOffsetTop = mCircleView.getTop();
            } else {
                reset();
            }
        }
    };
    //消失动画监听
    private AnimationListener mVanishListener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            setViewShow(1);
        }
    };

    void reset() {
        mCircleView.clearAnimation();
        mCircleView.setVisibility(View.GONE);
        setViewShow(1);

        // Return the circle to its start position
        if (mScale) {

            setAnimationProgress(0 /* animation complete and view is hidden */);
        } else {
            setTargetOffsetTopAndBottom(mOriginalOffsetTop - mCurrentTargetOffsetTop);
        }
        mCurrentTargetOffsetTop = mCircleView.getTop();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            reset();
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }


    /**
     * Constructor that is called when inflating TestTeam from XML.
     *
     * @param context
     * @param attrs
     */
    public RefreshLayoutRl(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        setWillNotDraw(false);
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

        DisplayMetrics metrics = getResources().getDisplayMetrics();


        createProgressView();
        setChildrenDrawingOrderEnabled(true);
        // the absolute offset has to take into account that the circle starts at an offset
        mSpinnerOffsetEnd = (int) (DEFAULT_CIRCLE_TARGET * metrics.density);
        mTotalDragDistance = mSpinnerOffsetEnd;
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        int initTop1 = (int) (60 * metrics.density);
        int initTop = context.getResources().getDimensionPixelSize(R.dimen.dp_50);//(int) (60 * metrics.density);
        initTop1 = initTop;
        mCurrentTargetOffsetTop = -initTop1;
        mOriginalOffsetTop = -initTop1;
        moveToStart(1.0f);

        TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (mCircleViewIndex < 0) {
            return i;
        } else if (i == childCount - 1) {
            // Draw the selected child last
            return mCircleViewIndex;
        } else if (i >= mCircleViewIndex) {
            // Move the children after the selected child earlier one
            return i + 1;
        } else {
            // Keep the children before the selected child the same
            return i;
        }
    }

    private ImageView ivLoadData, ivLoadLogo;

    private void createProgressView() {
        //mCircleView = new CircleImageView(getContext());
        //
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_view_refresh, this, false);
        //

        ivLoadData = view.findViewById(R.id.iv_data_refresh);


        ivLoadLogo = view.findViewById(R.id.iv_data_refresh_logo);
        mCircleView = view;
        setViewShow(1);
        mCircleView.setVisibility(View.GONE);
        addView(mCircleView);
    }

    private void setViewShow(int type) {
        if (type == 1) {
            ivLoadData.setVisibility(View.GONE);
            ivLoadLogo.setVisibility(View.VISIBLE);
        }
        if (type == 2) {
            ivLoadData.setVisibility(View.VISIBLE);
            ivLoadLogo.setVisibility(View.GONE);
        }
    }

    public void setOnRefreshListener(@Nullable OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * Notify the widget that refresh state has changed. Do not call this when
     * refresh is triggered by a swipe gesture.
     *
     * @param refreshing Whether or not the view should show refresh progress.
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing && mRefreshing != refreshing) {
            // scale and show
            mRefreshing = refreshing;
            int endTarget = 0;
            if (!mUsingCustomStart) {
                endTarget = mSpinnerOffsetEnd + mOriginalOffsetTop;
            } else {
                endTarget = mSpinnerOffsetEnd;
            }
            setTargetOffsetTopAndBottom(endTarget - mCurrentTargetOffsetTop);
            mNotify = false;
        } else {
            setRefreshing(refreshing, false /* notify */);
        }
    }


    /**
     * Pre API 11, this does an alpha animation.
     *
     * @param progress
     */
    void setAnimationProgress(float progress) {
        mCircleView.setScaleX(progress);
        mCircleView.setScaleY(progress);
    }

    private void setRefreshing(boolean refreshing, boolean notify) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();
            mRefreshing = refreshing;
            if (mRefreshing) {
                animateOffsetToCorrectPosition(mCurrentTargetOffsetTop, mRefreshListener);
            } else {
                startScaleDownAnimation(mVanishListener);
            }
        }
    }

    void startScaleDownAnimation(AnimationListener listener) {
        mScaleDownAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                setAnimationProgress(1 - interpolatedTime);
            }
        };
        mScaleDownAnimation.setDuration(SCALE_DOWN_DURATION);
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleDownAnimation);
        //
        /*if (listener != null) {
             mScaleDownAnimation.setAnimationListener(listener);
        }*/
        mScaleDownAnimation.setAnimationListener(mVanishListener);

    }

    /**
     * @return Whether the SwipeRefreshWidget is actively showing refresh
     * progress.
     */
    public boolean isRefreshing() {
        return mRefreshing;
    }

    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(mCircleView)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

   /* @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) {
            return;
        }
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        super.onLayout(changed, l, t, r, b);
    }*/

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        View child = mTarget;
        int childLeft = getPaddingLeft();
        int childTop = getPaddingTop();
        int childWidth = width - getPaddingLeft() - getPaddingRight();
        int childHeight = height - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        int circleWidth = mCircleView.getMeasuredWidth();
        int circleHeight = mCircleView.getMeasuredHeight();
        mCircleView.layout((width / 2 - circleWidth / 2), mCurrentTargetOffsetTop, (width / 2 + circleWidth / 2), mCurrentTargetOffsetTop + circleHeight);

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        int mTargetW = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.EXACTLY);
        int mTargetH = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        mTarget.measure(mTargetW, mTargetH);
        //子view 指定高宽 mCircleDiameter
        //int mCircleViewW = MeasureSpec.makeMeasureSpec(mCircleDiameter, MeasureSpec.EXACTLY);
        //int mCircleViewH = MeasureSpec.makeMeasureSpec(mCircleDiameter, MeasureSpec.EXACTLY);
        //mCircleView.measure(mCircleViewW, mCircleViewH);
        mCircleViewIndex = -1;
        // Get the index of the circleview.
        for (int index = 0; index < getChildCount(); index++) {
            View temp = getChildAt(index);
            if (temp == mCircleView) {
                mCircleViewIndex = index;
                break;
            }
        }
    }

    private OnChildScrollUpCallback mChildScrollUpCallback;

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (mChildScrollUpCallback != null) {
            return mChildScrollUpCallback.canChildScrollUp(this, mTarget);
        }
        if (mTarget instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) mTarget, -1);
        }
        return mTarget.canScrollVertically(-1);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();

        int action = ev.getActionMasked();
        int pointerIndex;

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }

        if (!isEnabled() || mReturningToStart || canChildScrollUp() || mRefreshing || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setTargetOffsetTopAndBottom(mOriginalOffsetTop - mCircleView.getTop());
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                mInitialDownY = ev.getY(pointerIndex);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                float y = ev.getY(pointerIndex);
                startDragging(y);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        return mIsBeingDragged;
    }

    /**
     * Enables the legacy behavior of {@link #requestDisallowInterceptTouchEvent} from before
     * 1.1.0-alpha03, where the request is neither honored, nor propagated up to its parents,
     * in either of the following two cases:
     * <ul>
     *     <li>The child as an {@link AbsListView} and the runtime is API < 21</li>
     *     <li>The child has nested scrolling disabled</li>
     * </ul>
     * Use this method <em>only</em> if your application:
     * <ul>
     *     <li>is upgrading TestTeam from &lt; 1.1.1 to &gt;= 1.1.0-alpha03</li>
     *     <li>has a TestTeam, or its parent, that no longer responds to touch events
     *     when it should</li>
     *     <li>setting this method to {@code true} fixes that issue</li>
     * </ul>
     *
     * @param enabled {@code true} to enable the legacy behavior, {@code false} for default behavior
     * @deprecated Only use this method if the changes introduced in
     * {@link #requestDisallowInterceptTouchEvent} in version 1.1.0-alpha03 and 1.1.1
     * are breaking your application.
     */
    @Deprecated
    public void setLegacyRequestDisallowInterceptTouchEventEnabled(boolean enabled) {
        mEnableLegacyRequestDisallowInterceptTouch = enabled;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        if (mEnableLegacyRequestDisallowInterceptTouch && ((android.os.Build.VERSION.SDK_INT < 21 && mTarget instanceof AbsListView) || (mTarget != null && !ViewCompat.isNestedScrollingEnabled(mTarget)))) {
            // Legacy behavior: if this is a List < L or another view that doesn't support
            // nested scrolling, ignore this request so that the vertical scroll event
            // isn't stolen
            return;
        }
        super.requestDisallowInterceptTouchEvent(b);
    }

    // NestedScrollingParent 3

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @ViewCompat.NestedScrollType int type, @NonNull int[] consumed) {
        if (type != ViewCompat.TYPE_TOUCH) {
            return;
        }

        // This is a bit of a hack. onNestedScroll is typically called up the hierarchy of nested
        // scrolling parents/children, where each consumes distances before passing the remainder
        // to parents.  In our case, we want to try to run after children, and after parents, so we
        // first pass scroll distances to parents and consume after everything else has.
        int consumedBeforeParents = consumed[1];
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, mParentOffsetInWindow, type, consumed);
        int consumedByParents = consumed[1] - consumedBeforeParents;
        int unconsumedAfterParents = dyUnconsumed - consumedByParents;

        // There are two reasons why scroll distance may be totally consumed.  1) All of the nested
        // scrolling parents up the hierarchy implement NestedScrolling3 and consumed all of the
        // distance or 2) at least 1 nested scrolling parent doesn't implement NestedScrolling3 and
        // for comparability reasons, we are supposed to act like they have.
        //
        // We must assume 2) is the case because we have no way of determining that it isn't, and
        // therefore must fallback to a previous hack that was done before nested scrolling 3
        // existed.
        int remainingDistanceToScroll;
        if (unconsumedAfterParents == 0) {
            // The previously implemented hack is to see how far we were offset and assume that that
            // distance is equal to how much all of our parents consumed.
            remainingDistanceToScroll = dyUnconsumed + mParentOffsetInWindow[1];
        } else {
            remainingDistanceToScroll = unconsumedAfterParents;
        }

        // Not sure why we have to make sure the child can't scroll up... but seems dangerous to
        // remove.
        if (remainingDistanceToScroll < 0 && !canChildScrollUp()) {
            mTotalUnconsumed += Math.abs(remainingDistanceToScroll);
            moveSpinner(mTotalUnconsumed);

            // If we've gotten here, we need to consume whatever is left to consume, which at this
            // point is either equal to 0, or remainingDistanceToScroll.
            consumed[1] += unconsumedAfterParents;
        }
    }

    // NestedScrollingParent 2

    @Override
    public boolean onStartNestedScroll(View child, View target, int axes, int type) {
        if (type == ViewCompat.TYPE_TOUCH) {
            return onStartNestedScroll(child, target, axes);
        } else {
            return false;
        }
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes, int type) {
        // Should always be true because onStartNestedScroll returns false for all type !=
        // ViewCompat.TYPE_TOUCH, but check just in case.
        if (type == ViewCompat.TYPE_TOUCH) {
            onNestedScrollAccepted(child, target, axes);
        }
    }

    @Override
    public void onStopNestedScroll(View target, int type) {
        // Should always be true because onStartNestedScroll returns false for all type !=
        // ViewCompat.TYPE_TOUCH, but check just in case.
        if (type == ViewCompat.TYPE_TOUCH) {
            onStopNestedScroll(target);
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, mNestedScrollingV2ConsumedCompat);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {
        // Should always be true because onStartNestedScroll returns false for all type !=
        // ViewCompat.TYPE_TOUCH, but check just in case.
        if (type == ViewCompat.TYPE_TOUCH) {
            onNestedPreScroll(target, dx, dy, consumed);
        }
    }

    // NestedScrollingParent 1

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && !mReturningToStart && !mRefreshing && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mTotalUnconsumed = 0;
        mNestedScrollInProgress = true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = (int) mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
            moveSpinner(mTotalUnconsumed);
        }

        // If a client layout is using a custom start position for the circle
        // view, they mean to hide it again before scrolling the child view
        // If we get back to mTotalUnconsumed == 0 and there is more to go, hide
        // the circle so it isn't exposed if its blocking content is moved
        if (mUsingCustomStart && dy > 0 && mTotalUnconsumed == 0 && Math.abs(dy - consumed[1]) > 0) {
            mCircleView.setVisibility(View.GONE);
        }

        // Now let our nested parent consume the leftovers
        int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        if (mTotalUnconsumed > 0) {
            finishSpinner(mTotalUnconsumed);
            mTotalUnconsumed = 0;
        }
        // Dispatch up our nested parent
        stopNestedScroll();
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, ViewCompat.TYPE_TOUCH, mNestedScrollingV2ConsumedCompat);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    // NestedScrollingChild 3

    @Override
    public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, @ViewCompat.NestedScrollType int type, @NonNull int[] consumed) {
        if (type == ViewCompat.TYPE_TOUCH) {
            mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed);
        }
    }

    // NestedScrollingChild 2

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return type == ViewCompat.TYPE_TOUCH && startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll(int type) {
        if (type == ViewCompat.TYPE_TOUCH) {
            stopNestedScroll();
        }
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return type == ViewCompat.TYPE_TOUCH && hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type) {
        return type == ViewCompat.TYPE_TOUCH && mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow, int type) {
        return type == ViewCompat.TYPE_TOUCH && dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    // NestedScrollingChild 1

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    private boolean isAnimationRunning(Animation animation) {
        return animation != null && animation.hasStarted() && !animation.hasEnded();
    }

    private void moveSpinner(float overscrollTop) {
        float originalDragPercent = overscrollTop / mTotalDragDistance;

        float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
        float extraOS = Math.abs(overscrollTop) - mTotalDragDistance;
        float slingshotDist = mCustomSlingshotDistance > 0 ? mCustomSlingshotDistance : (mUsingCustomStart ? mSpinnerOffsetEnd - mOriginalOffsetTop : mSpinnerOffsetEnd);
        float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, slingshotDist * 2) / slingshotDist);
        float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow((tensionSlingshotPercent / 4), 2)) * 2f;
        float extraMove = slingshotDist * tensionPercent * 2;

        int targetY = mOriginalOffsetTop + (int) ((slingshotDist * dragPercent) + extraMove);
        // where 1.0f is a full circle
        if (mCircleView.getVisibility() != View.VISIBLE) {
            mCircleView.setVisibility(View.VISIBLE);
        }
        if (!mScale) {
            mCircleView.setScaleX(1f);
            mCircleView.setScaleY(1f);
        }

        if (mScale) {
            setAnimationProgress(Math.min(1f, overscrollTop / mTotalDragDistance));
        }
        if (overscrollTop < mTotalDragDistance) {

        } else {

        }
        setTargetOffsetTopAndBottom(targetY - mCurrentTargetOffsetTop);
    }

    private void finishSpinner(float overscrollTop) {
        if (overscrollTop > mTotalDragDistance) {
            setRefreshing(true, true /* notify */);
        } else {
            // cancel refresh
            mRefreshing = false;
            AnimationListener listener = null;
            if (!mScale) {
                listener = new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (!mScale) {
                            startScaleDownAnimation(null);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                };
            }
            animateOffsetToStartPosition(mCurrentTargetOffsetTop, listener);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        int pointerIndex = -1;

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }

        if (!isEnabled() || mReturningToStart || canChildScrollUp() || mRefreshing || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                float y = ev.getY(pointerIndex);
                startDragging(y);

                if (mIsBeingDragged) {
                    float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
                    if (overscrollTop > 0) {
                        // While the spinner is being dragged down, our parent shouldn't try
                        // to intercept touch events. It will stop the drag gesture abruptly.
                        getParent().requestDisallowInterceptTouchEvent(true);
                        moveSpinner(overscrollTop);
                    } else {
                        return false;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                pointerIndex = ev.getActionIndex();
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                    return false;
                }
                mActivePointerId = ev.getPointerId(pointerIndex);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    return false;
                }

                if (mIsBeingDragged) {
                    float y = ev.getY(pointerIndex);
                    float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
                    mIsBeingDragged = false;
                    finishSpinner(overscrollTop);
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }
            case MotionEvent.ACTION_CANCEL:
                return false;
        }

        return true;
    }

    private void startDragging(float y) {
        float yDiff = y - mInitialDownY;
        if (yDiff > mTouchSlop && !mIsBeingDragged) {
            mInitialMotionY = mInitialDownY + mTouchSlop;
            mIsBeingDragged = true;
        }
    }

    //已过刷新点回弹一部分
    private void animateOffsetToCorrectPosition(int from, AnimationListener listener) {
        mFrom = from;
        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(ANIMATE_TO_TRIGGER_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mAnimateToCorrectPosition);
        mAnimateToCorrectPosition.setAnimationListener(listener);
    }

    //没有到刷新点，要回弹
    private void animateOffsetToStartPosition(int from, AnimationListener listener) {
        if (mScale) {
            // Scale the item back down
            startScaleDownReturnToStartAnimation(from, listener);
        } else {
            //回弹动画
            mFrom = from;
            mAnimateToStartPosition.reset();
            mAnimateToStartPosition.setDuration(ANIMATE_TO_START_DURATION);
            mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
            mCircleView.clearAnimation();
            mCircleView.startAnimation(mAnimateToStartPosition);
        }
    }

    //已过刷新点，回弹已过的部分
    private Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int endTarget;
            if (!mUsingCustomStart) {
                endTarget = mSpinnerOffsetEnd - Math.abs(mOriginalOffsetTop);
            } else {
                endTarget = mSpinnerOffsetEnd;
            }
            int targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
            int offset = targetTop - mCircleView.getTop();
            setTargetOffsetTopAndBottom(offset);
        }
    };

    void moveToStart(float interpolatedTime) {
        int targetTop = (mFrom + (int) ((mOriginalOffsetTop - mFrom) * interpolatedTime));
        int offset = targetTop - mCircleView.getTop();
        setTargetOffsetTopAndBottom(offset);
    }

    //没有到刷新点，要回弹
    private Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    private void startScaleDownReturnToStartAnimation(int from, AnimationListener listener) {
        mFrom = from;
        mStartingScale = mCircleView.getScaleX();

    }

    void setTargetOffsetTopAndBottom(int offset) {
        mCircleView.bringToFront();
        ViewCompat.offsetTopAndBottom(mCircleView, offset);
        mCurrentTargetOffsetTop = mCircleView.getTop();
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = ev.getActionIndex();
        int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }


    public interface OnRefreshListener {

        void onRefresh();
    }

    /**
     * Classes that wish to override {@link RefreshLayoutRl#canChildScrollUp()} method
     * behavior should implement this interface.
     */
    public interface OnChildScrollUpCallback {
        /**
         * Callback that will be called when {@link RefreshLayoutRl#canChildScrollUp()} method
         * is called to allow the implementer to override its behavior.
         *
         * @param parent TestTeam that this callback is overriding.
         * @param child  The child view of TestTeam.
         * @return Whether it is possible for the child view of parent layout to scroll up.
         */
        boolean canChildScrollUp(@NonNull RefreshLayoutRl parent, @Nullable View child);
    }
}
