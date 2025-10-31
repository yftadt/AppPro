package com.library.baseui.page;


import androidx.fragment.app.Fragment;

public abstract class BaseFragmentOld extends Fragment {
    private long id = 0;

    public long getId(int index) {
        if (id == 0) {
            id = System.currentTimeMillis();
        }
        return id + index;
    }
    //
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPause && isFragmentVisible && isVisibleToUser) {
            isPause = false;
            onLoadDataSatrt(isPause);
        }
    }

    //true 停止加载
    private boolean isPause;

    @Override
    public void onPause() {
        super.onPause();
        if (isFragmentVisible && isVisibleToUser) {
            isPause = true;
            onLoadDataStop(isPause);
        }
     }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    abstract boolean isInitComplete();

    private boolean isVisibleToUser;
    //true isFragmentVisible 处于可的状态
    private boolean isFragmentVisible;

    void loadData() {
        if (!isInitComplete()) {
            return;
        }
        if (isFragmentVisible && isVisibleToUser) {
            return;
        }
        if (!isFragmentVisible && !isVisibleToUser) {
            return;
        }
        if (!isFragmentVisible && isVisibleToUser) {
            //不可见到可见
            onLoadDataSatrt(isPause);
            isFragmentVisible = isVisibleToUser;
            return;
        }
        if (isFragmentVisible && !isVisibleToUser) {
            //可见到不可见
            onLoadDataStop(isPause);
            isFragmentVisible = isVisibleToUser;
            return;
        }
    }

    //开始加载数据
    abstract void onLoadDataSatrt(boolean isPause);

    //停止加载数据
    abstract void onLoadDataStop(boolean isPause);
}
