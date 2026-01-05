package test.app.ui.adapter.test;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import test.app.ui.activity.R;

/**
 * Created by anupamchugh on 05/10/16.
 */

public class TestRefreshAdapter extends RecyclerView.Adapter<TestRefreshViewHolder> {

    ArrayList<String> items;

    public TestRefreshAdapter(ArrayList<String> items) {
        this.items = items;
    }

    @Override
    public TestRefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_refresh, parent, false);
        return new TestRefreshViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TestRefreshViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
