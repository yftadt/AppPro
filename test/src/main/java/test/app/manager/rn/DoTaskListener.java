package test.app.manager.rn;

public interface DoTaskListener {

    //1 下载开始 2下载完成
    void onDownloadState(RNBean rnBean, int state);

    //1 下载更新与当前版本相同 2 下载出错 3 解压出错
    void onError(RNBean rnBean, int code, String msg);

    //下载进度
    void onDownloadProgress(RNBean rnBean, long pro, long length);

    //开始解压
    void onDecompressionStart(RNBean rnBean);

    //解压完成
    void onDecompressionComplete(RNBean rnBean);

    //结束
    void onEnd(RNBean rnBean);
}
