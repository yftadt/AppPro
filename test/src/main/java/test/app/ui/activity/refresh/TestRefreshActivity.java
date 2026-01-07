package test.app.ui.activity.refresh;


import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sj.mblog.Logx;
import test.app.ui.activity.R;
import test.app.ui.adapter.test.TestRefreshAdapter;

//刷新
public class TestRefreshActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SwipeLoadLayout mSwipeLoadLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh);
        mSwipeLoadLayout = (SwipeLoadLayout) findViewById(R.id.swipe_load_layout);
        mRecyclerView = mSwipeLoadLayout.findViewById(R.id.recyler_view);
        //
        TestRefreshAdapter adapter = new TestRefreshAdapter(getItems());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        //
        mSwipeLoadLayout.setOpenMore(true);
        mSwipeLoadLayout.setMoreListener(new SwipeLoadLayout.OnMoreListener() {
            @Override
            public void onMore() {
                Logx.d("刷新-->加载更多");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLoadLayout.setMoreEnd();
                        Logx.d("刷新-->加载更多完成");
                    }
                }, 10 * 1000);
            }

            @Override
            public void onPullingUp(float dy, int pullOutDistance, float viewHeight) {
                Logx.d("刷新-->onPullingUp 上拉");
            }
        });

        mSwipeLoadLayout.setOpenRefresh(true);
        mSwipeLoadLayout.setRefreshListener(new SwipeLoadLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Logx.d("刷新-->刷新数据");
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLoadLayout.setRefreshEnd();
                        Logx.d("刷新-->刷新数据 完成");
                    }
                }, 10 * 1000);*/
            }

            @Override
            public void onPullingDown(float dy, int pullOutDistance, float viewHeight) {
                Logx.d("刷新-->onPullingDown 下拉");
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
}
