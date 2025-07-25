package com.library.baseui.page;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.library.baseui.view.loading.BaseLoadingLayout;
import com.library.baseui.view.loading.LoadingLayout;


/**
 * Created by Administrator on 2016/8/15.
 */
public abstract class BaseCompatPage {
    protected Context context;
    protected View rootView;
    //
    protected BaseLoadingLayout loadingView;
    private boolean isLoadingShow;
    protected boolean isViewInit;

    public BaseCompatPage(Context context) {
        this.context = context;
    }

    public BaseCompatPage(Context context, boolean isLoadingShow) {
        this.context = context;
        this.isLoadingShow = isLoadingShow;
    }

    //=================================
    protected abstract void onViewCreated();

    protected void setContentView(int layoutResID) {
        rootView = LayoutInflater.from(context).inflate(layoutResID, null);
    }

    protected void setContentView(View view) {
        rootView = view;
    }

    protected View findViewById(int id) {
        return rootView.findViewById(id);
    }

    //=================================
    public View getView() {
        if (rootView == null) {
            assemblyView();
            onInitData();
        }
        return rootView;
    }

    private void assemblyView() {
        onViewCreated();
        isViewInit = true;
        if (!isLoadingShow) {
            return;
        }
        RelativeLayout relativeView = new RelativeLayout(context);
        RelativeLayout.LayoutParams contextRl = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(contextRl);
        relativeView.addView(rootView);
        //加载视图
        loadingInit(relativeView);
        rootView = relativeView;
    }


    /**
     * 加载视图初始化
     */
    protected void loadingInit(RelativeLayout relativeView) {
        RelativeLayout.LayoutParams contextRl = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LoadingLayout loadingLayout = new LoadingLayout(context);
        loadingInit(loadingLayout);
        loadingView.setLayoutParams(contextRl);
        relativeView.addView(loadingView);
    }

    protected void loadingInit(BaseLoadingLayout loadingLayout) {
        //加载视图
        int[] loadingImg = getLoadingImg();
        loadingLayout.init(context, loadingImg[0], loadingImg[1], loadingImg[2]);
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
    /**
     * 设置LoadingView的位置
     *
     * @param top    上边距
     * @param bottom 下边距
     */
    protected void setLoadingViewPadding(int top, int bottom) {
        if (loadingView == null) {
            return;
        }
        loadingView.setPadding(0, top, 0, bottom);
    }

    /**
     * 设置LoadingView的高度
     *
     * @param height px
     */
    protected void setLoadingViewHeight(int height) {
        if (loadingView == null) {
            return;
        }
        RelativeLayout.LayoutParams contextRl = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, height);
        loadingView.setLayoutParams(contextRl);
        loadingView.setGravity(Gravity.CENTER);
    }

    /**
     * 不重写loadingInit时 必须要设置图片
     *
     * @return 获取加载图
     */
    protected int[] getLoadingImg() {
        return null;
    }



    //初始化数据
    public void onInitData() {
    }

    //传递数据
    public void onData(String... arg0) {
    }

    //和activity一致
    public void onDestroy() {

    }

    //和activity一致
    public void onResume() {

    }

    //选中的时候
    public void onOption(int pageIndex) {

    }

    /**
     * 数据加载（如：网络请求）
     */
    public void doRequest() {
    }

    /**
     * 加载失败
     */
    public void loadingFailed() {
        if (loadingView == null) {
            return;
        }
        loadingView.setLoadingFailed();
    }

    /**
     * 加载成功
     */
    public void loadingSucceed() {
        if (loadingView == null) {
            isLoadingShow = false;
            return;
        }
        loadingView.setLoadingSucceed();
        loadingView.setVisibility(View.GONE);
    }

    /**
     * 重新显示加载视图,需要再次调用doRequest
     */
    public void loadingRest() {
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
        int clickType = isEmptyClick ? 1 : 0;
        loadingSucceed(isEmpty, iconResId, content, clickType);
    }

    /**
     * @param isEmpty   是否显示空数据视图
     * @param iconResId 图片资源id
     * @param content   文本提示
     * @param clickType 0 没有响应事件 1：重新加载 2：其它
     */
    protected void loadingSucceed(boolean isEmpty, int iconResId, String content, int clickType) {
        if (!isEmpty) {
            loadingSucceed();
            return;
        }
        loadingEmptyClick(iconResId, content, clickType);
    }

    /**
     * @param iconResId 图片资源id
     * @param content   文本提示
     * @param clickType 0 没有响应事件 1：重新加载 2：其它
     */
    protected void loadingEmptyClick(int iconResId, String content, int clickType) {
        if (loadingView == null) {
            return;
        }
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
