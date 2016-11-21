package com.app.ui.adapter.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public abstract class AbstractBaseAdapter<T> extends BaseAdapter {
    protected List<T> list = new ArrayList<T>();

    public void setData(List<T> list) {
        if (list == null) {
            return;
        }
        this.list = list;
        notifyDataSetChanged();
    }

    public void setAddData(List<T> list) {
        if (list == null) {
            return;
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setAddData(T item) {
        if (item == null) {
            return;
        }
        list.add(item);

        notifyDataSetChanged();
    }

    public void setAddData(int index, T item) {
        if (item == null) {
            return;
        }
        list.add(index, item);

        notifyDataSetChanged();
    }

    public List<T> getList() {
        return list;
    }

    public void remove(int index) {
        if (index < 0) {
            return;
        }
        list.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        return createView(i, view, viewGroup);
    }

    protected abstract View createView(int i, View view, ViewGroup viewGroup);

    class Click implements View.OnClickListener {
        private int index;


        public Click(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            onViewClick(index, view.getId());

        }
    }

    protected void onViewClick(int index, int viewId) {
        if (clickListener != null) {
            clickListener.onItemPartClick(index, viewId);
        }
    }

    private OnItemPartClickListener clickListener;

    public void setOnItemPartClick(OnItemPartClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnItemPartClickListener {
        void onItemPartClick(int index, int viewId);
    }
}
