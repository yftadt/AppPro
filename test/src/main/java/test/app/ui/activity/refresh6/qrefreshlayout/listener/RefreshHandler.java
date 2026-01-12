package test.app.ui.activity.refresh6.qrefreshlayout.listener;


import test.app.ui.activity.refresh6.qrefreshlayout.QRefreshLayout;

/**
 * @author chqiu
 */
public interface RefreshHandler {
    void onRefresh(QRefreshLayout refresh);

    void onLoadMore(QRefreshLayout refresh);
}
