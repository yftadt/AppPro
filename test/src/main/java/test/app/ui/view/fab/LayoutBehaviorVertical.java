package test.app.ui.view.fab;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;


import com.google.android.material.appbar.AppBarLayout;

import test.app.utiles.other.DLog;

/**
 * 设置  Layout的行为:(下滑效果)
 */
public class LayoutBehaviorVertical extends CoordinatorLayout.Behavior<View> {
    public LayoutBehaviorVertical(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    int childHeight, height, scall;
    float lastY = 0;

    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        if (childHeight == 0) {
            childHeight = child.getHeight();
            height = dependency.getHeight();
        }
        float tempY = dependency.getY();
        if (tempY < lastY) {
            //下滑动 滑动距离
            scall += ((Math.abs(tempY) - Math.abs(lastY)) / 2);
        } else {
            //上滑动 滑动距离
            scall -= ((Math.abs(lastY) - Math.abs(tempY)) / 2);
        }
        lastY = tempY;
        if (scall > childHeight) {
            scall = childHeight;
        }
        if (scall < 0) {
            scall = 0;
        }
        DLog.e("=======", " scaleY:" + scall + " childHeight:" + childHeight);
        ViewCompat.setTranslationY(child, scall);
        return true;
    }
}
