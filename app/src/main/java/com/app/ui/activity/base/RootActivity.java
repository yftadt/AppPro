package com.app.ui.activity.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
import com.library.baseui.activity.BaseCompatActivity;
import com.library.baseui.utile.HandlerUtil;
import com.library.baseui.utile.other.StatusBarUtile;
import com.library.baseui.utile.toast.ToastUtile;
import com.retrofits.net.common.RequestBack;

import sj.mblog.Logx;

/**
 * Created by Administrator on 2016/9/7.
 */
public class RootActivity<T extends ViewBinding> extends BaseCompatActivity implements RequestBack {
    protected MainApplication mainApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainApplication = (MainApplication) getApplication();
        setContentView();
        onInitImgVideo();
        onInitIntentData();
        onInitView();
    }


    protected void onInitImgVideo() {
    }

    protected void onInitIntentData() {
    }

    protected void onInitView() {
    }

    protected T bidingView;
    private View loadView, loadViewFail;

    protected void setContentView() {
        bidingView = getLayoutBinding(getLayoutInflater());

        if (bidingView != null) {
            View contentView = bidingView.getRoot();
            View wrRefreshView = contentView.findViewById(R.id.wr_refresh_view);
            if (wrRefreshView != null) {
                setSwipeRefreshLayout(wrRefreshView);
            }
            if (isLoadingLayout()) {
                setLayoutLoadView();
            } else {
                //setContentView(contentView);
                setRootView(contentView);
            }

        }
    }

    private void setRootView(View rootView) {
        if (true) {
            setContentView(rootView);//设置视图
            setNavigationBarEdge(rootView);
        } else {
            LinearLayout rootLinear = new LinearLayout(this);
            LinearLayout.LayoutParams loadViewLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            rootLinear.setLayoutParams(loadViewLp);
            rootLinear.setOrientation(LinearLayout.VERTICAL);
            //
            LinearLayout.LayoutParams contentViewLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0);
            rootLinear.setLayoutParams(loadViewLp);
            contentViewLp.weight = 1.0f;
            rootLinear.addView(rootView, contentViewLp);
            //
            int tabHeight = StatusBarUtile.getNavigationBarHeight(this);
            if (tabHeight > 0) {
                View tabView = new View(this);
                LinearLayout.LayoutParams tabViewLp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, tabHeight);
                tabView.setLayoutParams(tabViewLp);
                rootLinear.addView(tabView);
            }
            //
            setContentView(rootLinear);//设置视图
        }
    }

    // targetSdkVersion >= 35 时要使用： 获取底部 导航条可用大小
    private void setNavigationBarEdge(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply the insets as a margin to the view. This solution sets only the
            // bottom, left, and right dimensions, but you can apply whichever insets are
            // appropriate to your layout. You can also update the view padding if that's
            // more appropriate.
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.leftMargin = insets.left;
            mlp.bottomMargin = insets.bottom;
            mlp.rightMargin = insets.right;
            v.setLayoutParams(mlp);

            // Return CONSUMED if you don't want the window insets to keep passing
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });

    }

    //====================================================================
    protected T getLayoutBinding(LayoutInflater inflater) {
        return null;
    }

    @Override
    protected void setImmersion() {
        super.setImmersion();
        if (bidingView != null) {
            View view = findViewById(R.id.status_bar_height_view);
            if (view != null) {
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                lp.height = StatusBarUtile.getStatusBarHeight(this);
            }
        }
    }

    protected void setImmersionBlack() {
        super.setImmersion(false);
        if (bidingView != null) {
            View view = findViewById(R.id.status_bar_height_view);
            if (view != null) {
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                lp.height = StatusBarUtile.getStatusBarHeight(this);
            }
        }
    }

    protected void setBar() {

    }

    //==============================加载loading=============================================
    private void setLayoutLoadView() {

        //
        View contentView = bidingView.getRoot();
        View tempView = contentView.findViewById(R.id.rl_loading_view);
        if (tempView == null) {
            RelativeLayout rootView = new RelativeLayout(this);
            rootView.addView(contentView);//添加内容
            addLayoutLoadView(rootView);//添加loadingView
            //setContentView(rootView);//设置视图
            setRootView(rootView);

        } else {
            RelativeLayout loadingView = (RelativeLayout) tempView;
            addLayoutLoadView(loadingView);//添加loadingView
            //setContentView(contentView);//设置视图
            setRootView(contentView);
        }
    }

    private void addLayoutLoadView(RelativeLayout rootView) {
        RelativeLayout.LayoutParams loadViewLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        loadView = LayoutInflater.from(this).inflate(R.layout.layout_loading, null);
        rootView.addView(loadView, loadViewLp);
        //
        RelativeLayout.LayoutParams loadViewFailLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        loadViewFail = LayoutInflater.from(this).inflate(R.layout.layout_loading_fail, null);
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
        if (loadView != null) {
            loadView.setVisibility(View.GONE);
        }
        if (loadViewFail != null) {
            loadViewFail.setVisibility(View.VISIBLE);
        }
    }

    //设置请求成功
    protected void setDataLoadingSuccess() {
        isReqSuccess = true;
        if (loadViewFail != null) {
            loadViewFail.setVisibility(View.GONE);
        }
        if (loadView != null) {
            loadView.setVisibility(View.GONE);
        }
    }

    //恢复加载中....
    protected void setDataLoadingReset() {
        if (isReqSuccess) {
            isReqSuccess = false;
            loadViewFail.setVisibility(View.GONE);
            loadView.setVisibility(View.VISIBLE);
        }
    }

    protected boolean isLoadingLayout() {
        return false;
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

    //=======================等待dialog===============================
    private DialogCustomWaiting dialog;

    protected void dialogShow() {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        HandlerUtil.runInMainThread(dialogRun, 500);
    }

    protected void dialogDismiss() {
        HandlerUtil.removeCallbacks(dialogRun);
        dialogDie();
    }

    private void dialogDisplay() {
        if (isDestroyed() || isFinishing()) {
            return;
        }
        if (dialog == null) {
            dialog = new DialogCustomWaiting(this);
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


    //================================dialog事件回调=========================================


    //=============================网络请求回调==========================================
    public void onReqBack(int what, Object obj, String msg, String other) {
        showToast(msg, other);
    }

    protected void onUserPrivate() {
        setDataLoadingFail();
        dialogDismiss();
    }

    @Override
    public void onBack(int what, Object obj, String msg, String other) {
        if (isDestroyed() || isFinishing()) {
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

    //设置加载更多完成
    protected void setLoadMoreComplete() {
        if (loadMoreModule == null) {
            return;
        }
        //loadMoreModule.loadMoreComplete();
    }

    //没有更多数据了：（true 尾部会显示提示文案）
    protected void setLoadMoreEnd(boolean isHint) {
        if (loadMoreModule == null) {
            return;
        }
        loadMoreModule.setEnableLoadMore(false);
        loadMoreModule.loadMoreEnd(!isHint);
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

    //加载更多失败
    protected void setLoadMoreFail() {
        if (upFetchModule != null) {
            upFetchModule.setUpFetching(false);
        }
        if (loadMoreModule != null) {
            loadMoreModule.loadMoreFail();
        }
    }

    protected void doUpLoadMore() {

    }
    //=========================================================

    @Override
    protected void onResume() {
        super.onResume();

    }
}
