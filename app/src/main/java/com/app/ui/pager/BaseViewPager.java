package com.app.ui.pager;

import android.view.View;
import android.widget.RelativeLayout;

import com.app.ui.activity.base.BaseActivity;
import com.app.ui.activity.base.BaseApplication;
import com.app.ui.view.LoadingView;

/**
 * Created by Administrator on 2016/8/15.
 */
public abstract class BaseViewPager {
    protected BaseActivity baseActivity;
    private boolean isLoadingShow;
    private LoadingView loading;
    private BaseApplication baseApplication;

    public BaseViewPager(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public BaseViewPager(BaseActivity baseActivity, boolean isLoadingShow) {
        this.baseActivity = baseActivity;
        this.isLoadingShow = isLoadingShow;
        baseApplication = (BaseApplication) baseActivity.getApplication();
    }

    protected abstract View onViewCreated();

    /**
     * 加载页面
     **/

    private LoadingView loadingView;
    private View rootView;

    public View getView() {
        if (rootView == null) {
            rootView = assemblyView();
        }
        return rootView;
    }

    private View assemblyView() {
        if (rootView != null) {
            return rootView;
        }
        View view = onViewCreated();
        if (!isLoadingShow) {
            return view;
        }
        RelativeLayout relativeView = new RelativeLayout(baseActivity);
        RelativeLayout.LayoutParams contextRl = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(contextRl);
        relativeView.addView(view);
        //加载视图
        loadingView = new LoadingView(baseActivity);
        loadingView.setLayoutParams(contextRl);
        relativeView.addView(loadingView);
        loadingView.setOnResetLoagding(new OnCliickReset());
        loadingView.startLoading();
        return relativeView;
    }

    //重新加载
    public void doRequest() {
    }

    //加载失败
    public void loadingFailed() {
        if (loadingView == null) {
            return;
        }
        loadingView.setLoadingFailed();
    }

    //加载成功
    public void loadingSucceed() {
        if (loadingView == null) {
            return;
        }
        loadingView.setLoadingSucceed();
        loadingView.setVisibility(View.GONE);
    }

    //重新加载
    class OnCliickReset implements LoadingView.OnResetLoagding {
        @Override
        public void onResetRequest() {
            doRequest();
        }
    }
}
