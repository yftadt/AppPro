package com.library.baseui.activity;

import android.os.Build;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.library.baseui.view.loading.BaseLoadingLayout;
import com.library.baseui.view.loading.LoadingLayout;


/**
 * Created by Administrator on 2016/3/30.
 */
public class BaseBarActivity extends BaseActivity {

    protected View barView, compileView;
    protected BaseLoadingLayout loadingView;
    private int backgColor;

    public void setContentView(View view) {
        setContentView(view, 0, false);
    }

    public void setContentView(int layoutCount) {
        setContentView(layoutCount, 0, false);
    }

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

    /**
     * 初始化
     */
    protected void initView() {
    }

    /**
     * 不重写loadingInit时 必须要设置图片
     *
     * @return 获取加载图
     */
    protected int[] getLoadingImg() {
        return null;
    }

    /**
     * 加载视图初始化
     */
    protected void loadingInit(RelativeLayout relativeView) {
        RelativeLayout.LayoutParams loadingRl = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        if (barView != null) {
            loadingRl.addRule(RelativeLayout.BELOW, barView.getId());
        }
        //
        LoadingLayout loadingLayout = new LoadingLayout(this);
        loadingInit(loadingLayout);
        loadingView.setLayoutParams(loadingRl);
        relativeView.addView(loadingView);
    }

    protected void loadingInit(BaseLoadingLayout loadingLayout) {
        //加载视图
        int[] loadingImg = getLoadingImg();
        loadingLayout.init(this, loadingImg[0], loadingImg[1], loadingImg[2]);
        setLoadingLayout(loadingLayout);
    }

    protected void setLoadingLayout(BaseLoadingLayout layout) {
        if (layout == null) {
            return;
        }
        layout.setOnResetLoagding(new OnCliickReset());
        layout.startLoading();
        this.loadingView = layout;
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
        RelativeLayout rootView = new RelativeLayout(this);
        //添加bar
        if (layuotBar != 0) {
            barView = LayoutInflater.from(this).inflate(layuotBar,
                    null);
            barView.setId(layuotBar);
            rootView.addView(barView, RelativeLayout.LayoutParams.MATCH_PARENT,
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
        rootView.addView(view);
        compileView = view;
        //加载视图
        if (loadingShow) {
            loadingInit(rootView);
        }
        contentView(rootView);
        super.setContentView(rootView);
    }

    /**
     * @param relativeView 即将设置显示的view
     */
    protected void contentView(RelativeLayout relativeView) {
    }

    /**
     * @return barView 的高度
     */
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
            return true;
        }
        return false;
    }

    /**
     * 设置activity的透明度
     *
     * @param alpha ０.０全透明．１.０不透明．
     */
    protected void setActivityTransparent(float alpha) {
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        wl.alpha = alpha;
        window.setAttributes(wl);
    }

    /**
     * 设置状态栏透明
     */
    protected void setStatusTransparent() {
        Window window = getWindow();
        statusTransparent(window);
    }

    //沉浸式 需要在 setContentView 之前调用 才能生效
    protected void setStatusiImmersion(Window window) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    //沉浸式效果 需要在 setContentView 之前调用 才能生效
    protected boolean statusTransparent(Window window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false;
        }
        //状态栏 透明
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        return true;
    }

    //去掉沉浸式效果
    protected void statusUnTransparent(Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    //5.0以上 修改状态栏颜色
    protected boolean setStatusColor(Window window, int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        statusUnTransparent(window);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
        window.setNavigationBarColor(color);
        return true;
    }

    /**
     * @param isHide true 状态栏隐藏
     */
    private void setStatusHide(boolean isHide) {
        Window window = getWindow();
        if (isHide) {
            //状态栏隐藏
            window.getDecorView().setSystemUiVisibility(View.INVISIBLE);
        } else {
            //状态栏显示
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    /**
     * 设置状态栏字体图标颜色 5.0以上
     *
     * @param isDark true:黑色 false：白色
     */
    protected void setStatusIconCollor(boolean isDark) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        View decorView = getWindow().getDecorView();
        if (decorView == null) {
            return;
        }
        int vis = decorView.getSystemUiVisibility();
        if (isDark) {
            //设置状态栏字体图标为黑色
            vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            //设置状态栏字体图标为白色
            vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        decorView.setSystemUiVisibility(vis);
    }

    //isCover true 状态栏覆盖ui
    //强制修改状态栏为沉浸式（不用必须在setContentView之前调用）
    protected void setStatusCover(boolean isCover) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        Window window = getWindow();
        if (isCover) {
            //修改为沉浸式效果
            statusTransparent(window);
            //隐藏状态栏
            setStatusHide(true);
            getHandler().sendEmptyMessageDelayed(-1111, 400);
        } else {
            //去掉沉浸式效果
            statusUnTransparent(window);
            //隐藏状态栏
            window.getDecorView().setSystemUiVisibility(View.INVISIBLE);
            getHandler().sendEmptyMessageDelayed(-1111, 400);
        }
    }

    @Override
    protected void handleMessage(Message msg) {
        switch (msg.what) {
            case -1111:
                //显示状态栏
                Window window = getWindow();
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                break;
        }

    }

    /**
     * 显示barView
     */
    protected void barViewShow() {
        if (barView == null) {
            return;
        }
        barView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏barView
     */
    protected void barViewGone() {
        if (barView == null) {
            return;
        }
        barView.setVisibility(View.GONE);
    }

    /**
     * @return 状态栏的高度
     */
    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 数据加载（如：网络请求）
     */
    protected void doRequest() {
    }

    /**
     * 加载失败
     */
    protected void loadingFailed() {
        if (loadingView == null) {
            return;
        }
        loadingView.setLoadingFailed();
    }

    /**
     * 加载成功
     */
    protected void loadingSucceed() {
        if (loadingView == null) {
            return;
        }
        loadingView.setLoadingSucceed();
        loadingView.setVisibility(View.GONE);
    }

    /**
     * 重新显示加载视图,需要再次调用doRequest
     */
    protected void loadingRest() {
        if (loadingView == null) {
            return;
        }
        loadingView.setVisibility(View.VISIBLE);
        loadingView.startRest();
    }

    /**
     * @param isEmpty      是否显示空数据视图
     * @param iconResId    图片资源id
     * @param content      文本提示
     * @param isEmptyClick 是否响应点击事件
     */
    protected void loadingSucceed(boolean isEmpty, int iconResId, String content, boolean isEmptyClick) {
        if (!isEmpty) {
            loadingSucceed();
            return;
        }
        loadingEmptyClick(iconResId, content, isEmptyClick);
    }

    /**
     * @param iconResId    图片资源id
     * @param content      文本提示
     * @param isEmptyClick 是否响应点击事件
     */
    protected void loadingEmptyClick(int iconResId, String content, boolean isEmptyClick) {
        if (loadingView == null) {
            return;
        }
        int clickType = isEmptyClick ? 1 : 0;
        loadingView.setEmptyContent(iconResId, content, clickType);
        loadingView.setVisibility(View.VISIBLE);
    }

    //加载页面的点击事件
    protected void onLoadingClick(int clickType) {
    }

    //重新加载
    public class OnCliickReset implements BaseLoadingLayout.OnResetLoagding {

        @Override
        public void onWarningClick(int clickType) {
            if (clickType == 1) {
                doRequest();
                return;
            }
            onLoadingClick(clickType);
        }
    }
}
