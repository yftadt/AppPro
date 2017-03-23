package com.app.ui.view.refresh;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.ListView;

import com.app.ui.activity.R;


public class RefreshMoreList extends ListView implements SwipeRefreshLayout.OnRefreshListener {


    public RefreshMoreList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RefreshMoreList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshMoreList(Context context) {
        super(context);
        init();
    }

    //刷新  默认可以下拉刷新
    private SwipeRefreshLayout swipeLayout;
    /**
     * 屏幕显示的最后一个item
     */
    private int lastVisiblePosition;
    /**
     * 正在加载更多
     */
    private boolean hasFootView;
    /**
     * 加载更多布局
     */
    private View loading;
    /**
     * 是否加载更多 默认不加载 false
     */
    private boolean isLoadMore = false;


    private void init() {
        // 加载更多布局
        LayoutInflater inflater = LayoutInflater.from(getContext());
        loading = inflater.inflate(R.layout.footer_loading_small, null);
        this.setOnScrollListener(new ScrollListener());
    }

    public void onRefresh() {
        loadingListener.onLoading(true);
    }

    //  添加加载更多
    private void addFootView() {
        hasFootView = true;
        addFooterView(loading);
    }

    private void loadComplete() {
        // 移除加载更多
        if (hasFootView) {
            removeFooterView(loading);
            hasFootView = false;
        }
        // 刷新完成
        if (swipeLayout != null && swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }

    private OnLoadingListener loadingListener;

    public void setOnLoadingListener(OnLoadingListener loadingListener) {
        this.loadingListener = loadingListener;
        ViewParent view = getParent();
        do {
            if (swipeLayout != null) {
                break;
            }
            if (view instanceof PagerRefreshLayout) {
                PagerRefreshLayout simpleRefreshLayout = (PagerRefreshLayout) view;
                simpleRefreshLayout.setViewGroup(this);
                simpleRefreshLayout.setColorSchemeColors(0xff28B28E);
                simpleRefreshLayout.setOnRefreshListener(this);
                break;
            }
            if (view instanceof SwipeRefreshLayout) {
                swipeLayout = (SwipeRefreshLayout) view;
                swipeLayout.setColorSchemeColors(0xff28B28E);
                swipeLayout.setOnRefreshListener(this);
                break;
            }
            if (view != null) {
                view = view.getParent();
            }
        } while (view != null);
        if (loadingListener == null) {
            isLoadMore = false;
        }
        if (loadingListener == null && swipeLayout != null) {
            swipeLayout.setEnabled(false);
        }
        if (loadingListener != null && swipeLayout != null) {
            swipeLayout.setEnabled(true);
        }
    }

    // 是否要加载更多
    public void setLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }

    //刷新完成 或加载完成
    public void OnRenovationComplete() {
        loadComplete();
    }

    class ScrollListener implements OnScrollListener {

        public void onScrollStateChanged(AbsListView arg0, int arg1) {
            if (lastVisiblePosition >= (getCount() - 1) && isLoadMore
                    && !hasFootView) {
                addFootView();
                if (loadingListener != null) {
                    loadingListener.onLoading(false);
                }
            }

        }

        public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
            lastVisiblePosition = getLastVisiblePosition();
        }
    }

    /**
     * 刷新回调
     */
    public interface OnLoadingListener {
        /**
         * @param isRefresh true 刷新 false 加载更多
         */
        void onLoading(boolean isRefresh);

    }


}
