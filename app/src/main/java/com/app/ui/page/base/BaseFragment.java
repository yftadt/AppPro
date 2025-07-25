package com.app.ui.page.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;



public class BaseFragment<T extends ViewBinding> extends Fragment {
    private long id = System.currentTimeMillis();

    public long getId(int index) {
        return id + index;
    }

    protected T bidingView;

    protected T getLayoutBinding(LayoutInflater inflater) {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bidingView = getLayoutBinding(getLayoutInflater());
        return bidingView.getRoot();
    }

    //设置加载更多布局
    /*protected void setMoreLayout(BaseLoadMoreModule loadMoreModule) {
        if (loadMoreModule == null) {
            return;
        }
        loadMoreModule.setLoadMoreView(new CustomizeLoadMoreView());
    }*/
}
