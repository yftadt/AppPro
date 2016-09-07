package com.app.ui.activity.action;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.ui.activity.base.BaseActivity;
import com.app.ui.view.LoadingView;
import com.app.utiles.other.DLog;


/**
 * Created by Administrator on 2016/3/30.
 */
public class BaseBarActivity extends BaseActivity {

    protected View barView;
    private LoadingView loadingView;
    private int backgColor;

    public void setContentView(View view, boolean loadingShow) {
        setContentView(view, 0, loadingShow);
    }

    public void setContentView(int layoutCount, boolean loadingShow) {
        setContentView(LayoutInflater.from(this).inflate(layoutCount, null), 0, loadingShow);
    }

    public void setContentView(int layoutCount, int layuotBar, boolean loadingShow) {
        setContentView(LayoutInflater.from(this).inflate(layoutCount, null), layuotBar, loadingShow);
    }

    public void setContentView(View view, int layuotBar, boolean loadingShow) {
        setView(view, layuotBar, loadingShow);
        initView();
    }

    /***
     * 设置颜色
     *
     * @param backgColor 背景色
     */
    protected void setActionBarBackgColor(int backgColor) {
        this.backgColor = backgColor;
        if (barView != null) {
            barView.setBackgroundColor(backgColor);
        }
    }


    private void setView(View view, int layuotBar, boolean loadingShow) {
        RelativeLayout relativeView = new RelativeLayout(this);
        //添加bar
        if (layuotBar != 0) {
            barView = LayoutInflater.from(this).inflate(layuotBar,
                    null);
            barView.setId(layuotBar);
            relativeView.addView(barView, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
        if (isImmerse() && barView != null) {
            //是沉浸式状态栏，barView要包含状态栏高度
            int height = getStatusBarHeight();
            int paddingTop = barView.getPaddingTop() + height;
            barView.setPadding(0, paddingTop, 0, 0);
        }
        if (barView != null && backgColor != 0) {
            barView.setBackgroundColor(backgColor);
        }
        RelativeLayout.LayoutParams contextRl = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        if (barView != null) {
            contextRl.addRule(RelativeLayout.BELOW, barView.getId());
        }
        view.setLayoutParams(contextRl);
        relativeView.addView(view);
        //加载视图
        if (loadingShow) {
            RelativeLayout.LayoutParams loadingRl = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            if (barView != null) {
                loadingRl.addRule(RelativeLayout.BELOW, barView.getId());
            }
            loadingView = new LoadingView(this);
            loadingView.setLayoutParams(contextRl);
            relativeView.addView(loadingView);
            loadingView.setOnResetLoagding(new OnCliickReset());
            loadingView.startLoading();
        }
        super.setContentView(relativeView);
    }

    protected int getActionBarHeight() {
        int height = 0;
        if (barView != null) {
            height = barView.getHeight();
        }
        if (barView != null && barView.getVisibility() == View.GONE) {
            height = 0;
        }
        return height;
    }

    //判断当前状态栏是否为沉浸式
    private boolean isImmerse() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false;
        }
        if ((WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS & getWindow().getAttributes().flags)
                == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) {
            DLog.e("BaseBarView", "沉浸式");
            return true;
        }
        return false;
    }

    //修改为沉浸式
    protected void notStatus() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }

    protected void barViewShow() {
        if (barView == null) {
            return;
        }
        barView.setVisibility(View.VISIBLE);
    }

    protected void barViewGone() {
        if (barView == null) {
            return;
        }
        barView.setVisibility(View.GONE);
    }


    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //加载失败
    protected void loadingFailed() {
        if (loadingView == null) {
            return;
        }
        loadingView.setLoadingFailed();
    }

    //加载成功
    protected void loadingSucceed() {
        if (loadingView == null) {
            return;
        }
        loadingView.setLoadingSucceed();
        loadingView.setVisibility(View.GONE);
    }

    protected void initView() {
    }

    protected void doRequest() {
    }

    //重新加载
    class OnCliickReset implements LoadingView.OnResetLoagding {
        @Override
        public void onResetRequest() {
            doRequest();
        }
    }


    /**
     * @param tv
     * @param iconId
     * @param text
     * @param iconLocation 位置
     */
    protected void setText(TextView tv, int iconId, String text, int iconLocation) {
        if (iconId != 0) {
            Drawable drawable = getResources().getDrawable(iconId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            switch (iconLocation) {
                case 0:
                    tv.setCompoundDrawables(drawable, null, null, null);
                    break;
                case 1:
                    tv.setCompoundDrawables(null, drawable, null, null);
                    break;
                case 2:
                    tv.setCompoundDrawables(null, null, drawable, null);
                    break;
                case 3:
                    tv.setCompoundDrawables(null, null, null, drawable);
                    break;
            }
        }
        tv.setText(text);
    }


}
