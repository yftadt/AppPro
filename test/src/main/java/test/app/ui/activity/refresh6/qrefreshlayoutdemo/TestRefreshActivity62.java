package test.app.ui.activity.refresh6.qrefreshlayoutdemo;

import android.os.Handler;
 import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.LinkedList;

import test.app.ui.activity.R;
import test.app.ui.activity.refresh6.qrefreshlayout.QRefreshLayout;
import test.app.ui.activity.refresh6.qrefreshlayout.listener.RefreshHandler;
import test.app.ui.activity.refresh6.qrefreshlayout.widget.Material.MaterialBlackHeaderView;


public class TestRefreshActivity62 extends AppCompatActivity {
    private QRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinkedList<String> mDatas;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh_62);
        getData();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerAdapter());
        refreshLayout = (QRefreshLayout) findViewById(R.id.refreshlayout);
        refreshLayout.setHeaderView(new MaterialBlackHeaderView(this));
        refreshLayout.setLoadMoreEnable(true);
        refreshLayout.setRefreshHandler(new RefreshHandler() {
            @Override
            public void onRefresh(QRefreshLayout refresh) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDatas.addFirst("下拉刷新增加的数据");
                        recyclerView.getAdapter().notifyDataSetChanged();
                        refreshLayout.refreshComplete();
                    }
                }, 5000);
            }

            @Override
            public void onLoadMore(QRefreshLayout refresh) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDatas.addLast("上拉增加的数据");
                        recyclerView.getAdapter().notifyDataSetChanged();
                        refreshLayout.loadMoreComplete();
                    }
                }, 5000);
            }
        });
    }

    public void getData() {
        mDatas = new LinkedList<>();
        Collections.addAll(mDatas, "第1条数据", "第2条数据", "第3条数据",
                "第4条数据", "第7条数据", "第8条数据", "第9条数据", "第10条数据", "第11条数据",
                "第12条数据", "第13条数据", "第14条数据", "第15条数据", "第16条数据", "第17条数据", "第18条数据"
                , "第19条数据", "第20条数据");
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textview,
                    parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(final Holder holder, int position) {
            holder.view.setText(mDatas.get(position));
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(holder.view.getContext(), "点击 : " +
                            mDatas.get(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(holder.view.getContext(), "长按 : " +
                            mDatas.get(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView view;

            public Holder(View itemView) {
                super(itemView);
                view = (TextView) itemView.findViewById(R.id.textview);
            }
        }
    }

}
