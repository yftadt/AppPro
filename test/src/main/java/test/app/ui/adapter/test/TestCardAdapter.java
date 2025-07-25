package test.app.ui.adapter.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.list.library.adapter.recycler.AbstractRecyclerAdapter;
import com.list.library.adapter.recycler.BaseHolder;

import test.app.ui.activity.R;


/**
 * 卡片
 * Created by Administrator on 2017/8/18.
 */

public class TestCardAdapter extends AbstractRecyclerAdapter<String, TestCardAdapter.ViewHolder> {


    @Override
    protected ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_card, null);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    protected void onCreateData(ViewHolder holder, int position) {
        String t = list.get(position);
        holder.tv.setText(t);
    }

    class ViewHolder extends BaseHolder {
        private TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView)findViewById(R.id.tv);
        }
    }


}
