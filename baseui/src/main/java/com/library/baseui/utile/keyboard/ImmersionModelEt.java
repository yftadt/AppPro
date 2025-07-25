package com.library.baseui.utile.keyboard;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.library.baseui.R;

/**
 * 沉浸式状态栏模式下的输入框
 */
public class ImmersionModelEt {

    private View mChildOfContent;
    private int recordUseHeight;
    private FrameLayout.LayoutParams frameLayoutParams;
    private boolean isaddOnGlobal = false;
    private Activity activity;
    private View rlRoot;
    private GlobalLayoutListener globalLayoutListener = new GlobalLayoutListener();

    public ImmersionModelEt(Activity activity) {
        this.activity = activity;
        rlRoot = activity.findViewById(R.id.view_root);
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        setOnGlobalLayoutDel();
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    public void setOnGlobalLayoutDel() {
        if (isaddOnGlobal) {
            isaddOnGlobal = false;
            mChildOfContent.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
        }

    }

    public void setOnGlobalLayoutAdd() {
        if (!isaddOnGlobal) {
            isaddOnGlobal = true;
            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        }

    }

    private int childHeight;

    //重新计算高度
    private void calculateContextHeight() {
        int useHeight = getUsableHeight();
        if (useHeight == recordUseHeight) {
            return;
        }
        //1.decorView1与decorView2 是相等的，没某些情况下会包含底部导航条的高度
        View decorView1 = activity.getWindow().getDecorView();//
        View decorView2 = mChildOfContent.getRootView();//
        boolean isTest = decorView1 == decorView2;
        if (childHeight == 0) {
            if (rlRoot != null) {
                //rlRoot 是act根布局的 R.id.view_root 有就设置自己布局的高度
                childHeight = rlRoot.getHeight();
            } else {
                childHeight = recordUseHeight;
            }
        }
        int height = childHeight - useHeight;
        int temp = childHeight / 4;
        if (height > temp) {
            frameLayoutParams.height = useHeight;
        } else {
            frameLayoutParams.height = childHeight;
        }
        mChildOfContent.requestLayout();
        recordUseHeight = useHeight;

    }

    //获取可用高度
    private int getUsableHeight() {
        Rect r = new Rect();
        //获取界面高度（除去了标题栏（状态栏）、软键盘挡住的部分）
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        int bottom = r.bottom;
        //状态栏高度
        int top = r.top;
        //
        //return (bottom - top);
        //因为是沉浸模式 不要减去状态栏
        return bottom;
    }

    class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        public void onGlobalLayout() {
            calculateContextHeight();
        }
    }
}
