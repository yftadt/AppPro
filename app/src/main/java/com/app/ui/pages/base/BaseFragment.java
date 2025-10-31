package com.app.ui.pages.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewbinding.ViewBinding;

import com.app.ui.activity.MainApplication;
import com.app.ui.activity.R;
 import com.app.ui.view.CustomizeLoadMoreView;
import com.app.ui.window.dialog.DialogCustomWaiting;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.listener.OnUpFetchListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.chad.library.adapter.base.module.BaseUpFetchModule;
import com.library.baseui.utile.HandlerUtil;

import com.library.baseui.utile.toast.ToastUtile;
import com.retrofits.net.common.RequestBack;

import org.greenrobot.eventbus.EventBus;

import sj.mblog.Logx;

public class BaseFragment<T extends ViewBinding> extends Fragment implements RequestBack {
    private long id = 0;

    public long getId(int index) {
        if (id == 0) {
            id = System.currentTimeMillis();
        }
        return id + index;
    }

    public String getFrgName(Context context) {
        return "";
    }

    protected T bidingView;

    protected T getLayoutBinding(LayoutInflater inflater) {
        return null;
    }


    //==============================加载loading=============================================
    private View loadView, loadViewFail;

    private View getLayoutLoadView() {
        View contentView = bidingView.getRoot();
        View tempView = contentView.findViewById(R.id.rl_loading_view);
        RelativeLayout rootView;
        if (tempView == null) {
            rootView = new RelativeLayout(getContext());
            rootView.addView(contentView);
            addLayoutLoadView(rootView);
            return rootView;

        } else {
            rootView = (RelativeLayout) tempView;
            addLayoutLoadView(rootView);
            return contentView;
        }

    }

    private void addLayoutLoadView(RelativeLayout rootView) {
        RelativeLayout.LayoutParams loadViewLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        loadView = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading, null);
        rootView.addView(loadView, loadViewLp);
        //
        RelativeLayout.LayoutParams loadViewFailLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        loadViewFail = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_fail, null);
        rootView.addView(loadViewFail, loadViewFailLp);
        loadViewFail.setVisibility(View.GONE);
        loadViewFail.findViewById(R.id.tv_fail_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadViewFail.setVisibility(View.GONE);
                loadView.setVisibility(View.VISIBLE);
                doDataLoadingAgain();
            }
        });
        //点击事件
        setLoadViewClick();
    }

    private void setLoadViewClick() {
        loadView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        loadViewFail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }

    //重新请求
    protected void doDataLoadingAgain() {

    }

    //true 请求成功
    protected boolean isReqSuccess = false;

    //设置请求失败
    protected void setDataLoadingFail() {
        setLoadMoreFail();
        if (isReqSuccess) {
            return;
        }
        loadView.setVisibility(View.GONE);
        loadViewFail.setVisibility(View.VISIBLE);
    }

    //设置请求成功
    protected void setDataLoadingSuccess() {
        isViewDataShow = true;
        isReqSuccess = true;
        loadViewFail.setVisibility(View.GONE);
        loadView.setVisibility(View.GONE);
    }

    //恢复加载中....
    protected void setDataLoadingReset() {
        if (isReqSuccess) {
            isViewDataShow = false;
            isReqSuccess = false;
            loadViewFail.setVisibility(View.GONE);
            loadView.setVisibility(View.VISIBLE);
        }
    }

    protected boolean isLoadingLayout() {
        return false;
    }

    private boolean isViewDataShow = false;

    //true 表示正常数据界面，非加正在载中，加载失败 的
    public boolean isViewDataShow() {
        return isViewDataShow;
    }

    //=========================下拉刷新===================================
    private SwipeRefreshLayout swipeRefreshLayout;

    private void setSwipeRefreshLayout(View view) {
        if (view instanceof SwipeRefreshLayout) {
            swipeRefreshLayout = (SwipeRefreshLayout) view;
            // 设置下拉刷新的监听器
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // 这里可以执行网络请求等操作
                    doRefreshData();
                }

            });

        }
    }

    //开始刷新
    protected void doRefreshData() {
    }

    protected void setRefreshEnd() {
        if (swipeRefreshLayout == null) {
            return;
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    //===============设置加载更多布局==========================================
    protected BaseLoadMoreModule loadMoreModule;

    protected void setMoreLayout(BaseLoadMoreModule loadMoreModule) {
        if (loadMoreModule == null) {
            return;
        }
        this.loadMoreModule = loadMoreModule;
        loadMoreModule.setLoadMoreView(new CustomizeLoadMoreView());
    }

    private boolean isAddMoreListener;

    private void setOnLoadMoreListener() {
        if (isAddMoreListener) {
            loadMoreModule.loadMoreComplete();
            loadMoreModule.setEnableLoadMore(true);
            return;
        }
        isAddMoreListener = true;
        loadMoreModule.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                doLoadMore();
            }
        });
    }

    //true 开启加载更多
    protected void setLoadMoreOpen(boolean isOpen) {
        if (loadMoreModule == null) {
            return;
        }
        if (isOpen) {
            setOnLoadMoreListener();
        } else {
            loadMoreModule.setEnableLoadMore(false);
        }

    }

    //没有更多数据了：（true 尾部会显示提示文案）
    protected void setLoadMoreEnd(boolean isHint) {
        if (loadMoreModule == null) {
            return;
        }
        loadMoreModule.setEnableLoadMore(false);
        loadMoreModule.loadMoreEnd(!isHint);
    }

    //设置加载更多完成 废弃？
    protected void setLoadMoreComplete() {
        if (loadMoreModule == null) {
            return;
        }
        //loadMoreModule.loadMoreComplete();
    }


    //加载更多
    protected void doLoadMore() {

    }

    protected BaseUpFetchModule upFetchModule;

    //向上加载更多
    protected void setUpMoreLayout(BaseUpFetchModule fetchModule) {
        if (fetchModule == null) {
            return;
        }
        this.upFetchModule = fetchModule;
        upFetchModule.setStartUpFetchPosition(3);
        upFetchModule.setOnUpFetchListener(new OnUpFetchListener() {
            @Override
            public void onUpFetch() {
                upFetchModule.setUpFetching(true);
                doUpLoadMore();
            }
        });
    }

    //true 开启加载更多
    protected void setLoadUpMoreOpen(boolean isOpen) {
        if (upFetchModule == null) {
            return;
        }
        upFetchModule.setUpFetching(false);
        if (isOpen) {
            upFetchModule.setUpFetchEnable(true);
        } else {
            upFetchModule.setUpFetchEnable(false);
        }

    }

    protected void doUpLoadMore() {

    }

    //加载更多失败
    protected void setLoadMoreFail() {
        if (upFetchModule != null) {
            upFetchModule.setUpFetching(false);
        }
        if (loadMoreModule != null) {
            loadMoreModule.loadMoreFail();
        }
    }

    //=======================等待dialog===============================
    private DialogCustomWaiting dialog;

    protected void dialogShow() {
        HandlerUtil.runInMainThread(dialogRun, 500);
    }

    protected void dialogDismiss() {
        HandlerUtil.removeCallbacks(dialogRun);
        dialogDie();
    }

    private void dialogDisplay() {
        FragmentActivity act = getActivity();
        if (act == null || act.isDestroyed() || act.isFinishing()) {
            return;
        }
        if (dialog == null) {
            dialog = new DialogCustomWaiting(context);
        }
        dialog.show();
    }

    private void dialogDie() {
        if (dialog == null) {
            return;
        }
        dialog.dismiss();
    }

    private Runnable dialogRun = new Runnable() {

        @Override
        public void run() {
            dialogDisplay();
        }
    };
    //=========================================================

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bidingView = getLayoutBinding(getLayoutInflater());
        View contentView = bidingView.getRoot();
        if (isLoadingLayout()) {
            isViewDataShow = false;
            contentView = getLayoutLoadView();
        } else {
            isViewDataShow = true;
        }
        View wrRefreshView = contentView.findViewById(R.id.wr_refresh_view);
        if (wrRefreshView != null) {
            setSwipeRefreshLayout(wrRefreshView);
        }
        return contentView;
    }

    protected boolean isViewCreated;
    protected Context context;
    protected MainApplication mainApplication;
    protected FragmentActivity activity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isViewCreated) {
            setDataReset(view, savedInstanceState);
        }
        isViewCreated = true;
        context = getContext();
        activity = getActivity();
        mainApplication = (MainApplication) activity.getApplication();
        setViewInit(view, savedInstanceState);
        onInitImgVideo();
    }

    protected void onInitImgVideo() {
    }

    protected void setViewInit(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    protected void setDataReset(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    public void onReqBack(int what, Object obj, String msg, String other) {
        showToast(msg, other);
    }

    protected void onUserPrivate() {
        setDataLoadingSuccess();
        dialogDismiss();
    }

    @Override
    public void onBack(int what, Object obj, String msg, String other) {
        FragmentActivity act = getActivity();
        if (act == null || act.isDestroyed() || act.isFinishing()) {
            return;
        }


        onReqBack(what, obj, msg, other);
    }

    @Override
    public void onBackProgress(int i, String s, String s1, long l, long l1) {

    }

    private void showToast(String msg, String other) {
        Logx.d("消息-->", "msg:" + msg + " other:" + other);
        if ("成功".equals(msg)) {
            msg = "";
        }
        if (!TextUtils.isEmpty(other)) {
            ToastUtile.showToast(other);
            return;
        }
        if (!TextUtils.isEmpty(msg)) {
            ToastUtile.showToast(msg);
            return;
        }
    }


    //=========================
    protected void setEventBusRegistered() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    protected void setEventBusUnregister() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setEventBusUnregister();
    }

    protected boolean isResume;

    @Override
    public void onStop() {
        super.onStop();
        isResume = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isResume = true;
    }

    //============================
    //==============================Handler====================
    protected Handler uiHandler;

    protected Handler getHandler() {
        if (uiHandler == null) {
            initHandler();
        }
        return uiHandler;
    }

    private void initHandler() {
        uiHandler = new UiHandler();
    }

    class UiHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseFragment.this.handleMessage(msg);
        }
    }

    protected void handleMessage(Message msg) {

    }
}
