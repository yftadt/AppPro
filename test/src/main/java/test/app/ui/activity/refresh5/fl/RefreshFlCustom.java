package test.app.ui.activity.refresh5.fl;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import sj.mblog.Logx;
import test.app.ui.activity.R;
import test.app.ui.activity.refresh5.child.RefreshLayoutChildLi;

/**
 * 其可以滑动的子View：RefreshLayoutChildLi
 */
public class RefreshFlCustom extends BaseRefreshLayoutFl {
    public RefreshFlCustom(Context context) {
        super(context);
    }

    public RefreshFlCustom(Context context, AttributeSet attrs) {
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
        if (target instanceof RefreshLayoutChildLi) {
            mTargetView = (RefreshLayoutChildLi) target;
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
            int temp = (int) (dy * getDragCoefficient());
            int newHeight = setViewParams(temp);
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
            int temp = (int) (dy * getDragCoefficient());
            int newHeight = setViewParams(temp);
            //
            consumed[1] = dy;//告诉child我消费了多少
            //setTargetViewOffset(newHeight);
            Logx.d("父类:" + " 2向上滑动 " + " dy=" + dy + " newHeight=" + newHeight);
        }

    }

}