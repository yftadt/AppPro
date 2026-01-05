package test.app.ui.adapter.test;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import test.app.ui.activity.R;

public class TestRefreshViewHolder extends RecyclerView.ViewHolder {
    private TextView textView;


    public TestRefreshViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.list_item);
    }

    public void bind(String text) {
        textView.setText(text);
    }

}
