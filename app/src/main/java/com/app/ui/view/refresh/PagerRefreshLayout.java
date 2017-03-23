package com.app.ui.view.refresh;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

/**
 * SwipeRefreshLayout 在viewPer外面时,刷新list用
 * Created by Administrator on 2016/10/27.
 */
public class PagerRefreshLayout extends SwipeRefreshLayout {
    private View view;

    public PagerRefreshLayout(Context context) {
        super(context);
    }

    public PagerRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewGroup(View view) {
        this.view = view;
    }

    @Override
    public boolean canChildScrollUp() {
        View tempView = view;
        if (view != null && view instanceof ViewPager) {
            ViewPager viewPager = (ViewPager) view;
            int index = viewPager.getCurrentItem();
            View v = viewPager.getChildAt(index);
            if (v instanceof RelativeLayout) {
                RelativeLayout relativeLayout = (RelativeLayout) v;
                for (int i = 0; i < relativeLayout.getChildCount(); i++) {
                    View r = relativeLayout.getChildAt(i);
                    if (r instanceof AbsListView) {
                        tempView = r;
                        break;
                    }
                }
            }

        }
        if (tempView != null && tempView instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) tempView;
            return absListView.getChildCount() > 0
                    && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                    .getTop() < absListView.getPaddingTop());
        }
        return super.canChildScrollUp();

    }

}
