package test.app.ui.view.fab;

import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * 设置  FloatingActionButton的行为:(下滑效果)
 *
 */
public class FabVerticalBehavior extends FloatingActionButton.Behavior {

    public FabVerticalBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        // 确定是在垂直方向上滑动
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    private int scall = -1, childHight;

    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                               final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
       //dyUnconsumed:上滑是正数，下滑是负数
        Log.e("onNestedScroll", "dyUnconsumed:" + dyUnconsumed);
        if (childHight == 0) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            childHight = child.getHeight() + fabBottomMargin;
            scall = 0;
        }
        scall += dyConsumed;
        if (scall >= childHight) {
            scall = childHight;
        }
        if (scall < 0) {
            scall = 0;
        }
        ViewCompat.setTranslationY(child, scall);

    }


}
