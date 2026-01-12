package test.app.ui.activity.refresh6.qrefreshlayoutdemo;

import android.os.Handler;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import test.app.ui.activity.R;
import test.app.ui.activity.refresh6.qrefreshlayout.QRefreshLayout;
import test.app.ui.activity.refresh6.qrefreshlayout.listener.RefreshHandler;
import test.app.ui.adapter.test.TestRefreshAdapter;


public class TestRefreshActivity61 extends AppCompatActivity {

    private QRefreshLayout mRefreshLayout;
    private ArrayAdapter mAdapter;
    private LinkedList<String> mDatas;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh_61);
        getData();
        //  ListView  mListView = (ListView) findViewById(R.id.listview);
        //initView();
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        initView(mRecyclerView);
        initListener();
    }

    private void initView(ListView mListView) {

        mAdapter = new ArrayAdapter<>(this, R.layout.item_textview, R.id.textview, mDatas);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), mDatas.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private TestRefreshAdapter mRecyclerAdapter;

    private void initView(RecyclerView mRecyclerView) {
        mRecyclerAdapter = new TestRefreshAdapter(getItems());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);

    }

    private void initListener() {
        mRefreshLayout = (QRefreshLayout) findViewById(R.id.refreshlayout);
        //设置上拉加载更多可用
        mRefreshLayout.setLoadMoreEnable(true);
        //
        mRefreshLayout.setRefreshHandler(new RefreshHandler() {
            @Override
            public void onRefresh(QRefreshLayout refresh) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (mAdapter != null) {
                            mDatas.addFirst("下拉刷新增加的数据");
                            mAdapter.notifyDataSetChanged();
                        }
                        if (mRecyclerAdapter != null) {
                            mRecyclerAdapter.addHead("下拉刷新增加的数据");
                        }
                        mRefreshLayout.refreshComplete();
                    }
                }, 5000);
            }

            @Override
            public void onLoadMore(QRefreshLayout refresh) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter != null) {
                            mDatas.addLast("上拉增加的数据");
                            mAdapter.notifyDataSetChanged();
                        }
                        if (mRecyclerAdapter != null) {
                            mRecyclerAdapter.addData("上拉增加的数据");
                        }
                        mRefreshLayout.loadMoreComplete();
                    }
                }, 5000);
            }
        });
    }

    private ArrayList<String> getItems() {
        ArrayList<String> datas = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            datas.add("item+" + i);
        }
        return datas;
    }

    public void getData() {
        mDatas = new LinkedList<>();
        Collections.addAll(mDatas, "第1条数据", "第2条数据", "第3条数据", "第4条数据", "第5条数据", "第6条数据", "第7条数据", "第8条数据", "第9条数据", "第10条数据", "第11条数据", "第12条数据", "第13条数据", "第14条数据");
    }
}
