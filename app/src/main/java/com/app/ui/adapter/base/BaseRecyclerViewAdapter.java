package com.app.ui.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by guom on 2016/11/20.
 */

public class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    protected List<T> list = new ArrayList<>();

    public void setData(List<T> list) {
        if (list == null) {
            return;
        }
        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(List<T> list) {
        if (list == null) {
            return;
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(int index, List<T> list) {
        if (list == null) {
            return;
        }
        this.list.addAll(index, list);
        notifyDataSetChanged();
    }

    public void addData(T data) {
        if (data == null) {
            return;
        }
        list.add(data);
        notifyDataSetChanged();
    }

    public void addData(int index, T data) {
        if (data == null) {
            return;
        }
        list.add(index, data);
        notifyDataSetChanged();
    }

    public void setData(int index, T data) {
        if (data == null) {
            return;
        }
        list.set(index, data);
        notifyDataSetChanged();
    }

    //-------------------------------------
    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Hodler extends RecyclerView.ViewHolder {

        public Hodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    //--------------------------
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH vh = onCreateHolder(parent, viewType);
        return vh ;
    }

    protected VH onCreateHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onCreateData(holder, position);
        if (isOnItemClick) {
            holder.itemView.setOnClickListener(new OnClick(position));
        }
    }

    protected void onCreateData(VH holder, int position) {
    }

    //------------------------------------
    protected boolean isOnItemClick;

    public void setOnItemClickListener(boolean isOnItemClick) {
        this.isOnItemClick = isOnItemClick;
    }

    class OnClick implements View.OnClickListener {
        private int index;

        public OnClick(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            onViewClick(view, index);
        }

    }

    public void onViewClick(View view, int index) {
    }
}
